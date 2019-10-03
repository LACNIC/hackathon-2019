package net.lacnic.rpki.provisioning.utils;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.naming.NamingException;

import com.google.common.io.Files;

import net.lacnic.rpki.provisioning.client.RPKIProvisioningWsClient;
import net.ripe.rpki.commons.crypto.x509cert.X509CertificateParser;
import net.ripe.rpki.commons.provisioning.cms.ProvisioningCmsObject;
import net.ripe.rpki.commons.provisioning.cms.ProvisioningCmsObjectParser;
import net.ripe.rpki.commons.provisioning.identity.ParentIdentity;
import net.ripe.rpki.commons.provisioning.identity.ParentIdentitySerializer;
import net.ripe.rpki.commons.provisioning.payload.PayloadMessageType;
import net.ripe.rpki.commons.provisioning.payload.error.RequestNotPerformedResponsePayload;
import net.ripe.rpki.commons.provisioning.payload.error.RequestNotPerformedResponsePayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.payload.revocation.request.CertificateRevocationRequestPayload;
import net.ripe.rpki.commons.provisioning.payload.revocation.request.CertificateRevocationRequestPayloadBuilder;
import net.ripe.rpki.commons.provisioning.payload.revocation.request.CertificateRevocationRequestPayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.payload.revocation.response.CertificateRevocationResponsePayload;
import net.ripe.rpki.commons.provisioning.payload.revocation.response.CertificateRevocationResponsePayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningIdentityCertificate;
import net.ripe.rpki.commons.validation.ValidationCheck;
import net.ripe.rpki.commons.validation.ValidationLocation;
import net.ripe.rpki.commons.validation.ValidationResult;
import net.ripe.rpki.commons.xml.XStreamXmlSerializer;

public class Revoke {
	private static final XStreamXmlSerializer<CertificateRevocationRequestPayload> SERIALIZER = new CertificateRevocationRequestPayloadSerializerBuilder().build();

	private static String eEPrivateKeyName = "private_EE_CMS.key";
	private static String eEPublicKeyName = "public_EE_CMS.key";

	private static String issuancePrivateKeyName = "private_issuance.key";
	private static String issuancePublicKeyName = "public_issuance.key";

	private static String childPrivateKeyName = "private_child.key";
	private static String childPublicKeyName = "public_child.key";

	private static String childPath = Utils.getRutaChild();
	private static String revokePath = Utils.getRutaRevoke();
	private static String issuancePath = Utils.getRutaIssuance();

	public static void main(String[] args) throws NamingException, Exception {
		revokeRequest();

	}

	private static void revokeRequest() throws Exception {
		// // 0. Obtener parent Response
		try {
			byte[] encoded = Utils.getBytesFromFile(new File(Utils.getParentResponse()));
			String parentResponse = new String(encoded, Utils.UTF8);
			ParentIdentitySerializer serializer = new ParentIdentitySerializer();
			ParentIdentity parentIdentity = serializer.deserialize(parentResponse);
			// 1. armar request xml
			final KeyPair issuanceClientKeyPair = StoragekeyPair.cargarKeyPair(issuancePath, issuancePublicKeyName, issuancePrivateKeyName);
			final CertificateRevocationRequestPayload TEST_CERTIFICATE_REVOCATION_QUERY_PAYLOAD = createCertificateRevocationRequestPayload(issuanceClientKeyPair.getPublic());
			TEST_CERTIFICATE_REVOCATION_QUERY_PAYLOAD.setRecipient(parentIdentity.getParentHandle());
			TEST_CERTIFICATE_REVOCATION_QUERY_PAYLOAD.setSender(parentIdentity.getChildHandle());
			String instant = new Date().toString();
			String revokeName = "revoke-request" + instant + ".xml";
			Utils.writeToDisk(revokePath, revokeName, SERIALIZER.serialize(TEST_CERTIFICATE_REVOCATION_QUERY_PAYLOAD));

			// 2. Crear CMS
			String xmlRevokeRequestPayload = new String(Utils.getBytesFromFile(new File(Utils.getRevokeRequestPayLoadAux(revokeName))), Utils.UTF8);
			System.out.println(xmlRevokeRequestPayload);
			CertificateRevocationRequestPayload payload = SERIALIZER.deserialize(xmlRevokeRequestPayload);

			X509Certificate childCertificate = X509CertificateParser.parseX509Certificate(Files.toByteArray(new File(Utils.getChildCert())));
			ProvisioningIdentityCertificate provisioning = new ProvisioningIdentityCertificate(childCertificate);
			KeyPair keyPairCliente = StoragekeyPair.cargarKeyPair(childPath, childPublicKeyName, childPrivateKeyName);
			final KeyPair EE_KEYPAIR = GenerarKeyPair.generarParClave();
			StoragekeyPair.almacenarKeyPair(revokePath, EE_KEYPAIR, eEPublicKeyName, eEPrivateKeyName);
			String cmsName = "revoke-request" + instant + ".cms";
			Utils.createValidCmsObjec(revokePath, EE_KEYPAIR, payload, cmsName, keyPairCliente, provisioning);

			// 3. Enviar CMS
			byte[] cmsRevokeRequest = Files.toByteArray(new File(Utils.getChildRevokeCMSRequestAux(cmsName)));
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
				Utils.writeToDisk(revokePath, "parent-revoke-response" + instant + ".xml", revokeResponseXml);
				System.out.println(revokeResponseXml);
			} else {
				RequestNotPerformedResponsePayload payloadError = (RequestNotPerformedResponsePayload) provisioningCmsObject.getPayload();
				XStreamXmlSerializer<RequestNotPerformedResponsePayload> SERIALIZER3 = new RequestNotPerformedResponsePayloadSerializerBuilder().build();
				String issuanceErrorResponseXml = SERIALIZER3.serialize(payloadError);
				Utils.writeToDisk(revokePath, "parent-list-error-response", issuanceErrorResponseXml);
				System.out.println(issuanceErrorResponseXml);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static CertificateRevocationRequestPayload createCertificateRevocationRequestPayload(PublicKey publicKey) {
		CertificateRevocationRequestPayloadBuilder builder = new CertificateRevocationRequestPayloadBuilder();
		builder.withClassName("lacnic-resources");
		builder.withPublicKey(publicKey);
		return builder.build();
	}
}
