package net.lacnic.rpki.provisioning.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.naming.NamingException;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.joda.time.DateTime;

import com.google.common.io.Files;

import net.lacnic.rpki.provisioning.client.RPKIProvisioningWsClient;
import net.ripe.ipresource.IpResourceSet;
import net.ripe.rpki.commons.crypto.crl.X509CrlBuilder;
import net.ripe.rpki.commons.crypto.x509cert.X509CertificateBuilderHelper;
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
import net.ripe.rpki.commons.provisioning.payload.issue.request.CertificateIssuanceRequestPayload;
import net.ripe.rpki.commons.provisioning.payload.issue.request.CertificateIssuanceRequestPayloadBuilder;
import net.ripe.rpki.commons.provisioning.payload.issue.request.CertificateIssuanceRequestPayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.payload.issue.response.CertificateIssuanceResponsePayload;
import net.ripe.rpki.commons.provisioning.payload.issue.response.CertificateIssuanceResponsePayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningCmsCertificate;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningCmsCertificateBuilder;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningIdentityCertificate;
import net.ripe.rpki.commons.provisioning.x509.pkcs10.RpkiCaCertificateRequestBuilder;
import net.ripe.rpki.commons.validation.ValidationCheck;
import net.ripe.rpki.commons.validation.ValidationLocation;
import net.ripe.rpki.commons.validation.ValidationOptions;
import net.ripe.rpki.commons.validation.ValidationResult;
import net.ripe.rpki.commons.xml.XStreamXmlSerializer;

public class Issue {
	private static final XStreamXmlSerializer<CertificateIssuanceRequestPayload> SERIALIZER = new CertificateIssuanceRequestPayloadSerializerBuilder().build();

	public static URI prueba_repo = URI.create("rsync://repository.prueba.net/rpki-prueba/");
	public static URI prueba_mft = URI.create("rsync://repository.prueba.net/rpki-prueba/manifiesto.mft");
	public static URI prueba_not_uri = URI.create("http://localhost:8080/module/subdir/notification.xml");
	// public static X500Principal RPKI_CA_CERT_REQUEST_CA_SUBJECT = new
	// X500Principal("CN=prueba lacnic O=LACNIC");
	public static X500Principal RPKI_CA_CERT_REQUEST_CA_SUBJECT = new X500Principal("CN=66666666666666777777777777");
	private static String asn = "11751";
	private static String ipv4 = "177.234.144.0-177.234.191.255";
	private static String ipv6 = "2804:44fc::/32";

	public static void main(String[] args) throws NamingException, Exception {
		primeraEmision();
	}

	private static void primeraEmision() throws UnsupportedEncodingException, IOException {
		// 0. Obtener parent Response
		try {
			ParentIdentitySerializer serializer = new ParentIdentitySerializer();
			ParentIdentity parentIdentity = serializer.deserialize(new String(UtilsConstantes.getBytesFromFile(new File(UtilsConstantes.getParentResponse())), UtilsConstantes.UTF8));
			// // 1. Crear Payload y guardarlo
			final KeyPair RPKI_CA_CERT_REQUEST_KEYPAIR = GenerarKeyPair.generarParClave();
			StoragekeyPair.almacenarKeyPairIssuanceRequest(RPKI_CA_CERT_REQUEST_KEYPAIR);

			// final KeyPair RPKI_CA_CERT_REQUEST_KEYPAIR =
			// StoragekeyPair.cargarIssuanceKeyPair("public_issuance.key",
			// "private_issuance.key");

			final CertificateIssuanceRequestPayload TEST_CERTIFICATE_ISSUANCE_REQUEST_PAYLOAD = createCertificateIssuanceRequestPayloadForPkcs10RequestAux(createRpkiCaCertificateRequest(RPKI_CA_CERT_REQUEST_KEYPAIR), asn, ipv4, ipv6);
			TEST_CERTIFICATE_ISSUANCE_REQUEST_PAYLOAD.setRecipient(parentIdentity.getParentHandle());
			TEST_CERTIFICATE_ISSUANCE_REQUEST_PAYLOAD.setSender(parentIdentity.getChildHandle());
			//
			String actualXml = SERIALIZER.serialize(TEST_CERTIFICATE_ISSUANCE_REQUEST_PAYLOAD);
			String instant = new Date().toString();
			String issuanceName = "issuance-request" + instant + ".xml";
			writeToDisk(issuanceName, actualXml);
			System.out.println(actualXml);

			// // hasta aca, fin 1.

			// 2. Obtener Xml y realizar parseo
			// y Obtener Certificado y KeyPair del Child, para crear CMS
			String xmlRequestIssuancePayload = new String(UtilsConstantes.getBytesFromFile(new File(UtilsConstantes.getIssuanceRequestAux(issuanceName))), UtilsConstantes.UTF8);
			System.out.println(xmlRequestIssuancePayload);
			CertificateIssuanceRequestPayload payload = SERIALIZER.deserialize(xmlRequestIssuancePayload);

			X509Certificate childCertificate = X509CertificateParser.parseX509Certificate(Files.toByteArray(new File(UtilsConstantes.getChildCert())));
			ProvisioningIdentityCertificate provisioning = new ProvisioningIdentityCertificate(childCertificate);
			KeyPair keyPairCliente = StoragekeyPair.cargarKeyPair();
			final KeyPair EE_KEYPAIR = GenerarKeyPair.generarParClave();
			StoragekeyPair.almacenarKeyPairEndEntityCMS(EE_KEYPAIR);
			String cmsName = "certificate-issuance-request" + instant + ".cms";
			createValidCmsObjectAndWriteItToDisk(EE_KEYPAIR, payload, cmsName, keyPairCliente, provisioning);
			System.out.println("Todo bien");
			// Hasta aca, fin 2.

			// 3. Enviar CMS
			byte[] cmsIssuanceRequest = Files.toByteArray(new File(UtilsConstantes.getChildCMSRequestAux(cmsName)));
			byte[] cmsResponse = RPKIProvisioningWsClient.updown(cmsIssuanceRequest);
			if (cmsResponse != null) {
				ProvisioningCmsObjectParser parser = new ProvisioningCmsObjectParser();
				parser.parseCms("cms", cmsResponse);
				ValidationResult validationResult = parser.getValidationResult();
				for (ValidationCheck check : validationResult.getFailures(new ValidationLocation("cms"))) {
					System.err.println("Failure: " + check);
				}
				ProvisioningCmsObject provisioningCmsObject = parser.getProvisioningCmsObject();
				if (provisioningCmsObject.getPayload().getType().equals(PayloadMessageType.issue_response)) {
					CertificateIssuanceResponsePayload payload2 = (CertificateIssuanceResponsePayload) provisioningCmsObject.getPayload();
					XStreamXmlSerializer<CertificateIssuanceResponsePayload> SERIALIZER2 = new CertificateIssuanceResponsePayloadSerializerBuilder().build();
					String issuanceResponseXml = SERIALIZER2.serialize(payload2);
					writeToDisk("parent-issuance-response" + instant + ".xml", issuanceResponseXml);
					System.out.println(issuanceResponseXml);
				} else {
					RequestNotPerformedResponsePayload payloadError = (RequestNotPerformedResponsePayload) provisioningCmsObject.getPayload();
					XStreamXmlSerializer<RequestNotPerformedResponsePayload> SERIALIZER3 = new RequestNotPerformedResponsePayloadSerializerBuilder().build();
					String issuanceErrorResponseXml = SERIALIZER3.serialize(payloadError);
					writeToDisk("parent-issuance-error-response", issuanceErrorResponseXml);
					System.out.println(issuanceErrorResponseXml);
				}
			} else
				System.out.println("Algo fallo en el servicio");

		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}

	private static CertificateIssuanceRequestPayload createCertificateIssuanceRequestPayloadForPkcs10RequestAux(PKCS10CertificationRequest pkcs10Request, String asn, String ipv4, String ipv6) {
		CertificateIssuanceRequestPayloadBuilder builder = new CertificateIssuanceRequestPayloadBuilder();
		builder.withClassName("lacnic-resources");
		if (asn != null)
			if (asn.isEmpty())
				builder.withAllocatedAsn(new IpResourceSet());
			else
				builder.withAllocatedAsn(IpResourceSet.parse(asn));
		else
			builder.withAllocatedAsn(null);

		if (ipv4 != null)
			if (ipv4.isEmpty())
				builder.withIpv4ResourceSet(new IpResourceSet());
			else
				builder.withIpv4ResourceSet(IpResourceSet.parse(ipv4));
		else
			builder.withIpv4ResourceSet(null);

		if (ipv6 != null)
			if (ipv6.isEmpty())
				builder.withIpv6ResourceSet(new IpResourceSet());
			else
				builder.withIpv6ResourceSet(IpResourceSet.parse(ipv6));
		else
			builder.withIpv6ResourceSet(null);

		builder.withCertificateRequest(pkcs10Request);
		return builder.build();
	}

	public static PKCS10CertificationRequest createRpkiCaCertificateRequest(KeyPair RPKI_CA_CERT_REQUEST_KEYPAIR) {
		RpkiCaCertificateRequestBuilder requestBuilder = new RpkiCaCertificateRequestBuilder();
		requestBuilder.withCaRepositoryUri(prueba_repo);
		requestBuilder.withManifestUri(prueba_mft);
		requestBuilder.withNotificationUri(prueba_not_uri);
		requestBuilder.withSubject(RPKI_CA_CERT_REQUEST_CA_SUBJECT);
		PKCS10CertificationRequest pkcs10Request = requestBuilder.build(RPKI_CA_CERT_REQUEST_KEYPAIR);
		return pkcs10Request;
	}

	public static void createValidCmsObjectAndWriteItToDisk(KeyPair EE_KEYPAIR, AbstractProvisioningPayload payload, String fileName, KeyPair keyPairCliente, ProvisioningIdentityCertificate childCertificate) throws IOException {
		ProvisioningCmsObject resourceClassIssuanceQueryCms = createProvisioningCmsObjectForPayload(EE_KEYPAIR, payload, keyPairCliente, childCertificate);
		validateCmsObject(resourceClassIssuanceQueryCms, childCertificate);
		writeToDisk(fileName, resourceClassIssuanceQueryCms.getEncoded());
	}

	public static void validateCmsObject(ProvisioningCmsObject resourceClassIssuanceQueryCms, ProvisioningIdentityCertificate childCertificate) {
		ProvisioningCmsObjectValidator validator = new ProvisioningCmsObjectValidator(new ValidationOptions(), resourceClassIssuanceQueryCms, childCertificate);
		ValidationResult result = ValidationResult.withLocation("n/a");
		validator.validate(result);
		// assertTrue(!result.hasFailures());
	}

	public static ProvisioningCmsObject createProvisioningCmsObjectForPayload(KeyPair EE_KEYPAIR, AbstractProvisioningPayload payload, KeyPair keyPairCliente, ProvisioningIdentityCertificate childCertificate) {
		ProvisioningCmsObjectBuilder builder = new ProvisioningCmsObjectBuilder();
		builder.withCmsCertificate(getTestProvisioningCmsCertificate(EE_KEYPAIR, keyPairCliente, childCertificate).getCertificate());
		builder.withCrl(generateCrl(keyPairCliente));
		builder.withSignatureProvider(X509CertificateBuilderHelper.DEFAULT_SIGNATURE_PROVIDER);
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
		builder.withSignatureProvider(X509CertificateBuilderHelper.DEFAULT_SIGNATURE_PROVIDER);
		return builder;
	}

	private static X509CRL generateCrl(KeyPair keyPairCliente) {
		X509CrlBuilder builder = new X509CrlBuilder();
		builder.withIssuerDN(new X500Principal("CN=NIC O=NICBR"));
		builder.withAuthorityKeyIdentifier(keyPairCliente.getPublic());
		DateTime now = new DateTime();
		builder.withThisUpdateTime(now);
		builder.withNextUpdateTime(now.plusHours(24));
		builder.withNumber(BigInteger.TEN);

		return builder.build(keyPairCliente.getPrivate()).getCrl();
	}

	private static void writeToDisk(String fileName, byte[] encoded) throws IOException {
		File file = new File(UtilsConstantes.getRutaIssuance() + fileName);
		Files.write(encoded, file);
	}

	private static void writeToDisk(String fileName, String xml) throws IOException {
		File file = new File(UtilsConstantes.getRutaIssuance() + fileName);
		Files.write(xml, file, Charset.forName("UTF-8"));

	}

}
