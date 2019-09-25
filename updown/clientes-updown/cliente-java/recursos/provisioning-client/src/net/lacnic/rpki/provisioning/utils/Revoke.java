package net.lacnic.rpki.provisioning.utils;

import static net.ripe.rpki.commons.crypto.x509cert.X509CertificateBuilderHelper.DEFAULT_SIGNATURE_PROVIDER;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.naming.NamingException;
import javax.security.auth.x500.X500Principal;

import org.joda.time.DateTime;

import com.google.common.io.Files;

import net.lacnic.rpki.provisioning.client.RPKIProvisioningWsClient;
import net.ripe.rpki.commons.crypto.crl.X509CrlBuilder;
import net.ripe.rpki.commons.crypto.x509cert.X509CertificateParser;
import net.ripe.rpki.commons.provisioning.cms.ProvisioningCmsObject;
import net.ripe.rpki.commons.provisioning.cms.ProvisioningCmsObjectBuilder;
import net.ripe.rpki.commons.provisioning.cms.ProvisioningCmsObjectParser;
import net.ripe.rpki.commons.provisioning.cms.ProvisioningCmsObjectValidator;
import net.ripe.rpki.commons.provisioning.identity.ParentIdentity;
import net.ripe.rpki.commons.provisioning.identity.ParentIdentitySerializer;
import net.ripe.rpki.commons.provisioning.payload.AbstractProvisioningPayload;
import net.ripe.rpki.commons.provisioning.payload.PayloadMessageType;
import net.ripe.rpki.commons.provisioning.payload.error.RequestNotPerformedResponsePayload;
import net.ripe.rpki.commons.provisioning.payload.error.RequestNotPerformedResponsePayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.payload.revocation.request.CertificateRevocationRequestPayload;
import net.ripe.rpki.commons.provisioning.payload.revocation.request.CertificateRevocationRequestPayloadBuilder;
import net.ripe.rpki.commons.provisioning.payload.revocation.request.CertificateRevocationRequestPayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.payload.revocation.response.CertificateRevocationResponsePayload;
import net.ripe.rpki.commons.provisioning.payload.revocation.response.CertificateRevocationResponsePayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningCmsCertificate;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningCmsCertificateBuilder;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningIdentityCertificate;
import net.ripe.rpki.commons.validation.ValidationCheck;
import net.ripe.rpki.commons.validation.ValidationLocation;
import net.ripe.rpki.commons.validation.ValidationOptions;
import net.ripe.rpki.commons.validation.ValidationResult;
import net.ripe.rpki.commons.xml.XStreamXmlSerializer;

public class Revoke {
	private static final XStreamXmlSerializer<CertificateRevocationRequestPayload> SERIALIZER = new CertificateRevocationRequestPayloadSerializerBuilder().build();

	public static void main(String[] args) throws NamingException, Exception {
		revokeRequest();

	}

	private static void revokeRequest() throws UnsupportedEncodingException, IOException {
		// // 0. Obtener parent Response
		try {
			byte[] encoded = UtilsConstantes.getBytesFromFile(new File(UtilsConstantes.getParentResponse()));
			String parentResponse = new String(encoded, UtilsConstantes.UTF8);
			ParentIdentitySerializer serializer = new ParentIdentitySerializer();
			ParentIdentity parentIdentity = serializer.deserialize(parentResponse);
			// //
			KeyPair issuanceClientKeyPair = StoragekeyPair.cargarIssuanceKeyPair("public_issuance.key", "private_issuance.key");
			final CertificateRevocationRequestPayload TEST_CERTIFICATE_REVOCATION_QUERY_PAYLOAD = createCertificateRevocationRequestPayload(issuanceClientKeyPair.getPublic());
			TEST_CERTIFICATE_REVOCATION_QUERY_PAYLOAD.setRecipient(parentIdentity.getParentHandle());
			TEST_CERTIFICATE_REVOCATION_QUERY_PAYLOAD.setSender(parentIdentity.getChildHandle());
			String instant = new Date().toString();
			String revokeName = "revoke-request" + instant + ".xml";
			writeToDisk(revokeName, SERIALIZER.serialize(TEST_CERTIFICATE_REVOCATION_QUERY_PAYLOAD));

			// // hasta acá. 1

			// 2. Obtener Certificado y KeyPair del Child
			String xmlRevokeRequestPayload = new String(UtilsConstantes.getBytesFromFile(new File(UtilsConstantes.getRevokeRequestPayLoadAux(revokeName))), UtilsConstantes.UTF8);
			System.out.println(xmlRevokeRequestPayload);
			CertificateRevocationRequestPayload payload = SERIALIZER.deserialize(xmlRevokeRequestPayload);

			X509Certificate childCertificate = X509CertificateParser.parseX509Certificate(Files.toByteArray(new File(UtilsConstantes.getChildCert())));
			ProvisioningIdentityCertificate provisioning = new ProvisioningIdentityCertificate(childCertificate);
			KeyPair keyPairCliente = StoragekeyPair.cargarKeyPair();
			final KeyPair EE_KEYPAIR = GenerarKeyPair.generarParClave();
			StoragekeyPair.almacenarKeyPairEndEntityCMS(EE_KEYPAIR);
			String cmsName = "revoke-request" + instant + ".cms";
			createValidCmsObjectAndWriteItToDisk(EE_KEYPAIR, payload, cmsName, keyPairCliente, provisioning);
			// // Hasta aca, fin 2.

			// 3. Enviar CMS
			byte[] cmsRevokeRequest = Files.toByteArray(new File(UtilsConstantes.getChildRevokeCMSRequestAux(cmsName)));
			byte[] cmsResponse = RPKIProvisioningWsClient.updown(cmsRevokeRequest);
			ProvisioningCmsObjectParser parser = new ProvisioningCmsObjectParser();
			parser.parseCms("cms", cmsResponse);
			ValidationResult validationResult = parser.getValidationResult();
			for (ValidationCheck check : validationResult.getFailures(new ValidationLocation("cms"))) {
				System.err.println("Failure: " + check);
			}
			ProvisioningCmsObject provisioningCmsObject = parser.getProvisioningCmsObject();
			if (provisioningCmsObject.getPayload().getType().equals(PayloadMessageType.revoke_response)) {
				CertificateRevocationResponsePayload payloadResponce = (CertificateRevocationResponsePayload) provisioningCmsObject.getPayload();
				XStreamXmlSerializer<CertificateRevocationResponsePayload> SERIALIZER2 = new CertificateRevocationResponsePayloadSerializerBuilder().build();
				String revokeResponseXml = SERIALIZER2.serialize(payloadResponce);
				writeToDisk("parent-revoke-response" + instant + ".xml", revokeResponseXml);
				System.out.println(revokeResponseXml);
			} else {
				RequestNotPerformedResponsePayload payloadError = (RequestNotPerformedResponsePayload) provisioningCmsObject.getPayload();
				XStreamXmlSerializer<RequestNotPerformedResponsePayload> SERIALIZER3 = new RequestNotPerformedResponsePayloadSerializerBuilder().build();
				String issuanceErrorResponseXml = SERIALIZER3.serialize(payloadError);
				writeToDisk("parent-list-error-response", issuanceErrorResponseXml);
				System.out.println(issuanceErrorResponseXml);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static CertificateRevocationRequestPayload createCertificateRevocationRequestPayload(PublicKey publicKey) {
		CertificateRevocationRequestPayloadBuilder builder = new CertificateRevocationRequestPayloadBuilder();
		builder.withClassName("lacnic-resources");
		builder.withPublicKey(publicKey);
		return builder.build();
	}

	public static void createValidCmsObjectAndWriteItToDisk(KeyPair EE_KEYPAIR, AbstractProvisioningPayload payload, String fileName, KeyPair keyPairCliente, ProvisioningIdentityCertificate childCertificate) throws IOException {
		ProvisioningCmsObject revokeRequestQueryCms = createProvisioningCmsObjectForPayload(EE_KEYPAIR, payload, keyPairCliente, childCertificate);
		validateCmsObject(revokeRequestQueryCms, childCertificate);
		writeToDisk(fileName, revokeRequestQueryCms.getEncoded());
	}

	public static void validateCmsObject(ProvisioningCmsObject revokeRequestQueryCms, ProvisioningIdentityCertificate childCertificate) {
		ProvisioningCmsObjectValidator validator = new ProvisioningCmsObjectValidator(new ValidationOptions(), revokeRequestQueryCms, childCertificate);
		ValidationResult result = ValidationResult.withLocation("n/a");
		validator.validate(result);
		// assertTrue(!result.hasFailures());
	}

	public static ProvisioningCmsObject createProvisioningCmsObjectForPayload(KeyPair EE_KEYPAIR, AbstractProvisioningPayload payload, KeyPair keyPairCliente, ProvisioningIdentityCertificate childCertificate) {
		ProvisioningCmsObjectBuilder builder = new ProvisioningCmsObjectBuilder();
		builder.withCmsCertificate(getTestProvisioningCmsCertificate(EE_KEYPAIR, keyPairCliente, childCertificate).getCertificate());
		builder.withCrl(generateCrl(keyPairCliente));
		builder.withSignatureProvider(DEFAULT_SIGNATURE_PROVIDER);
		builder.withPayloadContent(payload);
		return builder.build(EE_KEYPAIR.getPrivate());
	}

	private static ProvisioningCmsCertificate getTestProvisioningCmsCertificate(KeyPair EE_KEYPAIR, KeyPair keyPairCliente, ProvisioningIdentityCertificate childCertificate) {
		ProvisioningCmsCertificateBuilder cmsCertificateBuilder = getTestBuilder(EE_KEYPAIR, keyPairCliente, childCertificate);
		return cmsCertificateBuilder.build();
	}

	private static ProvisioningCmsCertificateBuilder getTestBuilder(KeyPair EE_KEYPAIR, KeyPair keyPairCliente, ProvisioningIdentityCertificate childCertificate) {
		ProvisioningCmsCertificateBuilder builder = new ProvisioningCmsCertificateBuilder();
		builder.withIssuerDN(childCertificate.getSubject());
		builder.withSerial(BigInteger.TEN);
		builder.withPublicKey(EE_KEYPAIR.getPublic());
		builder.withSubjectDN(new X500Principal("CN=end-entity"));
		builder.withSigningKeyPair(keyPairCliente);
		builder.withSignatureProvider(DEFAULT_SIGNATURE_PROVIDER);
		return builder;
	}

	private static X509CRL generateCrl(KeyPair keyPairCliente) {
		X509CrlBuilder builder = new X509CrlBuilder();
		builder.withIssuerDN(new X500Principal("CN=lacnic prueba"));
		builder.withAuthorityKeyIdentifier(keyPairCliente.getPublic());
		DateTime now = new DateTime();
		builder.withThisUpdateTime(now);
		builder.withNextUpdateTime(now.plusHours(24));
		builder.withNumber(BigInteger.TEN);

		return builder.build(keyPairCliente.getPrivate()).getCrl();
	}

	private static void writeToDisk(String fileName, byte[] encoded) throws IOException {
		File file = new File(UtilsConstantes.getRutaRevoke() + fileName);
		Files.write(encoded, file);
	}

	private static void writeToDisk(String fileName, String xml) throws IOException {
		File file = new File(UtilsConstantes.getRutaRevoke() + fileName);
		Files.write(xml, file, Charset.forName("UTF-8"));

	}
}
