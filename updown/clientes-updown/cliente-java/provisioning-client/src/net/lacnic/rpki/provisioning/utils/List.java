package net.lacnic.rpki.provisioning.utils;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
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
import net.ripe.rpki.commons.provisioning.payload.list.request.ResourceClassListQueryPayload;
import net.ripe.rpki.commons.provisioning.payload.list.request.ResourceClassListQueryPayloadBuilder;
import net.ripe.rpki.commons.provisioning.payload.list.request.ResourceClassListQueryPayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.payload.list.response.ResourceClassListResponsePayload;
import net.ripe.rpki.commons.provisioning.payload.list.response.ResourceClassListResponsePayloadSerializerBuilder;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningIdentityCertificate;
import net.ripe.rpki.commons.validation.ValidationCheck;
import net.ripe.rpki.commons.validation.ValidationLocation;
import net.ripe.rpki.commons.validation.ValidationResult;
import net.ripe.rpki.commons.xml.XStreamXmlSerializer;

public class List {
	private static final XStreamXmlSerializer<ResourceClassListQueryPayload> SERIALIZER = new ResourceClassListQueryPayloadSerializerBuilder().build();

	private static String eEPrivateKeyName = "private_EE_CMS.key";
	private static String eEPublicKeyName = "public_EE_CMS.key";

	private static String childPrivateKeyName = "private_child.key";
	private static String childPublicKeyName = "public_child.key";

	private static String childPath = Utils.getRutaChild();
	private static String listPath = Utils.getRutaList();

	public static void main(String[] args) throws NamingException, Exception {
		listRequest();
	}

	private static void listRequest() throws Exception {
		// // 0. Obtener parent Response
		try {
			byte[] encoded = Utils.getBytesFromFile(new File(Utils.getParentResponse()));
			String parentResponse = new String(encoded, Utils.UTF8);
			ParentIdentitySerializer serializer = new ParentIdentitySerializer();
			ParentIdentity parentIdentity = serializer.deserialize(parentResponse);

			// 1. Crear request xml
			final ResourceClassListQueryPayload TEST_RESOURCE_CLASS_LIST_QUERY_PAYLOAD = createResourceClassListQueryPayload();
			TEST_RESOURCE_CLASS_LIST_QUERY_PAYLOAD.setRecipient(parentIdentity.getParentHandle());
			TEST_RESOURCE_CLASS_LIST_QUERY_PAYLOAD.setSender(parentIdentity.getChildHandle());
			String instant = new Date().toString();
			String listName = "list-request" + instant + ".xml";
			String actualXml = SERIALIZER.serialize(TEST_RESOURCE_CLASS_LIST_QUERY_PAYLOAD);
			Utils.writeToDisk(listPath, listName, actualXml);
			System.out.println(actualXml);

			// 2. Crear CMS
			String xmlListRequestPayload = new String(Utils.getBytesFromFile(new File(Utils.getListRequestAux(listName))), Utils.UTF8);
			System.out.println(xmlListRequestPayload);
			ResourceClassListQueryPayload payload = SERIALIZER.deserialize(xmlListRequestPayload);

			X509Certificate childCertificate = X509CertificateParser.parseX509Certificate(Files.toByteArray(new File(Utils.getChildCert())));
			ProvisioningIdentityCertificate provisioning = new ProvisioningIdentityCertificate(childCertificate);
			KeyPair keyPairCliente = StoragekeyPair.cargarKeyPair(childPath, childPublicKeyName, childPrivateKeyName);
			final KeyPair EE_KEYPAIR = GenerarKeyPair.generarParClave();
			StoragekeyPair.almacenarKeyPair(listPath, EE_KEYPAIR, eEPublicKeyName, eEPrivateKeyName);
			String cmsName = "list-request" + instant + ".cms";
			Utils.createValidCmsObjec(listPath, EE_KEYPAIR, payload, cmsName, keyPairCliente, provisioning);

			// 3. Enviar CMS
			byte[] cmsListRequest = Files.toByteArray(new File(Utils.getChildListCMSRequestAux(cmsName)));
			byte[] cmsResponse = RPKIProvisioningWsClient.updown(cmsListRequest);
			ProvisioningCmsObjectParser parser = new ProvisioningCmsObjectParser();
			parser.parseCms("cms", cmsResponse);
			ValidationResult validationResult = parser.getValidationResult();
			for (ValidationCheck check : validationResult.getFailures(new ValidationLocation("cms"))) {
				System.err.println("Failure: " + check);
			}
			ProvisioningCmsObject provisioningCmsObject = parser.getProvisioningCmsObject();
			if (provisioningCmsObject.getPayload().getType().equals(PayloadMessageType.list_response)) {
				ResourceClassListResponsePayload payloadResponce = (ResourceClassListResponsePayload) provisioningCmsObject.getPayload();
				XStreamXmlSerializer<ResourceClassListResponsePayload> SERIALIZER2 = new ResourceClassListResponsePayloadSerializerBuilder().build();
				String listResponseXml = SERIALIZER2.serialize(payloadResponce);
				Utils.writeToDisk(listPath, "parent-list-response" + instant + ".xml", listResponseXml);
				System.out.println(listResponseXml);
			} else {
				RequestNotPerformedResponsePayload payloadError = (RequestNotPerformedResponsePayload) provisioningCmsObject.getPayload();
				XStreamXmlSerializer<RequestNotPerformedResponsePayload> SERIALIZER3 = new RequestNotPerformedResponsePayloadSerializerBuilder().build();
				String issuanceErrorResponseXml = SERIALIZER3.serialize(payloadError);
				Utils.writeToDisk(listPath, "parent-list-error-response", issuanceErrorResponseXml);
				System.out.println(issuanceErrorResponseXml);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ResourceClassListQueryPayload createResourceClassListQueryPayload() {
		ResourceClassListQueryPayloadBuilder builder = new ResourceClassListQueryPayloadBuilder();
		return builder.build();
	}
}
