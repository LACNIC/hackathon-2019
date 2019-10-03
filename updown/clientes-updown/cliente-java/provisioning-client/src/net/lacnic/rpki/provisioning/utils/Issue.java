package net.lacnic.rpki.provisioning.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.naming.NamingException;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import com.google.common.io.Files;

import net.lacnic.rpki.provisioning.client.RPKIProvisioningWsClient;
import net.ripe.ipresource.IpResourceSet;
import net.ripe.rpki.commons.crypto.x509cert.X509CertificateParser;
import net.ripe.rpki.commons.provisioning.cms.ProvisioningCmsObject;
import net.ripe.rpki.commons.provisioning.cms.ProvisioningCmsObjectParser;
import net.ripe.rpki.commons.provisioning.identity.ParentIdentity;
import net.ripe.rpki.commons.provisioning.identity.ParentIdentitySerializer;
import net.ripe.rpki.commons.provisioning.payload.PayloadMessageType;
import net.ripe.rpki.commons.provisioning.payload.error.RequestNotPerformedResponsePayload;
import net.ripe.rpki.commons.provisioning.payload.error.RequestNotPerformedResponsePayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.payload.issue.request.CertificateIssuanceRequestPayload;
import net.ripe.rpki.commons.provisioning.payload.issue.request.CertificateIssuanceRequestPayloadBuilder;
import net.ripe.rpki.commons.provisioning.payload.issue.request.CertificateIssuanceRequestPayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.payload.issue.response.CertificateIssuanceResponsePayload;
import net.ripe.rpki.commons.provisioning.payload.issue.response.CertificateIssuanceResponsePayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningIdentityCertificate;
import net.ripe.rpki.commons.provisioning.x509.pkcs10.RpkiCaCertificateRequestBuilder;
import net.ripe.rpki.commons.validation.ValidationCheck;
import net.ripe.rpki.commons.validation.ValidationLocation;
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
	private static String asn = null;
	private static String ipv4 = null;
	private static String ipv6 = null;

	private static String eEPrivateKeyName = "private_EE_CMS.key";
	private static String eEPublicKeyName = "public_EE_CMS.key";

	private static String issuancePrivateKeyName = "private_issuance.key";
	private static String issuancePublicKeyName = "public_issuance.key";

	private static String childPrivateKeyName = "private_child.key";
	private static String childPublicKeyName = "public_child.key";

	private static String childPath = Utils.getRutaChild();
	private static String issuancePath = Utils.getRutaIssuance();

	public static void main(String[] args) throws NamingException, Exception {
		primeraEmision();
	}

	private static void primeraEmision() throws UnsupportedEncodingException, Exception {
		// 0. Obtener parent Response
		try {
			ParentIdentitySerializer serializer = new ParentIdentitySerializer();
			ParentIdentity parentIdentity = serializer.deserialize(new String(Utils.getBytesFromFile(new File(Utils.getParentResponse())), Utils.UTF8));
			// 1. Crear request xml
			final KeyPair RPKI_CA_CERT_REQUEST_KEYPAIR = GenerarKeyPair.generarParClave();
			// final KeyPair RPKI_CA_CERT_REQUEST_KEYPAIR =
			// StoragekeyPair.cargarKeyPair(issuancePath, issuancePublicKeyName,
			// issuancePrivateKeyName);
			StoragekeyPair.almacenarKeyPair(issuancePath, RPKI_CA_CERT_REQUEST_KEYPAIR, issuancePublicKeyName, issuancePrivateKeyName);

			final CertificateIssuanceRequestPayload TEST_CERTIFICATE_ISSUANCE_REQUEST_PAYLOAD = createCertificateIssuanceRequestPayloadForPkcs10RequestAux(createRpkiCaCertificateRequest(RPKI_CA_CERT_REQUEST_KEYPAIR), asn, ipv4, ipv6);
			TEST_CERTIFICATE_ISSUANCE_REQUEST_PAYLOAD.setRecipient(parentIdentity.getParentHandle());
			TEST_CERTIFICATE_ISSUANCE_REQUEST_PAYLOAD.setSender(parentIdentity.getChildHandle());

			String actualXml = SERIALIZER.serialize(TEST_CERTIFICATE_ISSUANCE_REQUEST_PAYLOAD);
			String instant = new Date().toString();
			String issuanceName = "issuance-request" + instant + ".xml";
			Utils.writeToDisk(issuancePath, issuanceName, actualXml);
			System.out.println(actualXml);

			// 2. Crear CMS
			String xmlRequestIssuancePayload = new String(Utils.getBytesFromFile(new File(Utils.getIssuanceRequestAux(issuanceName))), Utils.UTF8);
			System.out.println(xmlRequestIssuancePayload);
			CertificateIssuanceRequestPayload payload = SERIALIZER.deserialize(xmlRequestIssuancePayload);

			X509Certificate childCertificate = X509CertificateParser.parseX509Certificate(Files.toByteArray(new File(Utils.getChildCert())));
			ProvisioningIdentityCertificate provisioning = new ProvisioningIdentityCertificate(childCertificate);
			KeyPair keyPairCliente = StoragekeyPair.cargarKeyPair(childPath, childPublicKeyName, childPrivateKeyName);
			final KeyPair EE_KEYPAIR = GenerarKeyPair.generarParClave();
			StoragekeyPair.almacenarKeyPair(issuancePath, EE_KEYPAIR, eEPublicKeyName, eEPrivateKeyName);
			String cmsName = "certificate-issuance-request" + instant + ".cms";
			Utils.createValidCmsObjec(issuancePath, EE_KEYPAIR, payload, cmsName, keyPairCliente, provisioning);

			// 3. Enviar CMS
			byte[] cmsIssuanceRequest = Files.toByteArray(new File(Utils.getChildCMSRequestAux(cmsName)));
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
					Utils.writeToDisk(issuancePath, "parent-issuance-response" + instant + ".xml", issuanceResponseXml);
					System.out.println(issuanceResponseXml);
				} else {
					RequestNotPerformedResponsePayload payloadError = (RequestNotPerformedResponsePayload) provisioningCmsObject.getPayload();
					XStreamXmlSerializer<RequestNotPerformedResponsePayload> SERIALIZER3 = new RequestNotPerformedResponsePayloadSerializerBuilder().build();
					String issuanceErrorResponseXml = SERIALIZER3.serialize(payloadError);
					Utils.writeToDisk(issuancePath, "parent-issuance-error-response", issuanceErrorResponseXml);
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
}
