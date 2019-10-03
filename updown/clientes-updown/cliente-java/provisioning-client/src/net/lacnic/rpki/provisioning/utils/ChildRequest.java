package net.lacnic.rpki.provisioning.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;

import javax.security.auth.x500.X500Principal;

import org.xml.sax.SAXException;

import net.ripe.rpki.commons.provisioning.identity.ChildIdentity;
import net.ripe.rpki.commons.provisioning.identity.ChildIdentitySerializer;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningIdentityCertificate;

public class ChildRequest {
	public static String TOKEN = "";
	public static String child_handle = "";
	public static final X500Principal SELF_SIGNING_DN_1 = new X500Principal("CN=" + child_handle);

	private static String childPrivateKeyName = "private_child.key";
	private static String childPublicKeyName = "public_child.key";

	private static String childPath = Utils.getRutaChild();

	public static void main(String[] args) throws Exception {
		childRequest();
	}

	private static void childRequest() throws Exception, IOException, SAXException, UnsupportedEncodingException {
		final KeyPair KEY_PAIR = GenerarKeyPair.generarParClave();
		// KeyPair KEY_PAIR = StoragekeyPair.cargarKeyPair(childPath,
		// childPublicKeyName, childPrivateKeyName);
		StoragekeyPair.almacenarKeyPair(childPath, KEY_PAIR, childPublicKeyName, childPrivateKeyName);
		ProvisioningIdentityCertificate provisioningIdentityCertificate = BuilderBPKI.createProvisioningIdentityCertificate(KEY_PAIR);
		ChildIdentity childIdentity = new ChildIdentity(child_handle, provisioningIdentityCertificate);
		ChildIdentitySerializer serializer = new ChildIdentitySerializer();
		String xml = "";
		if (childIdentity.getIdentityCertificate() != null) {
			xml = serializer.serialize(childIdentity);
			if (xml.contains("version=\"1\"") & xml.contains("child_handle=")) {
				if (ValidacionesUpDown.validateAgainstRelaxNg(xml))
					System.out.println("Todo bien");
			} else
				System.out.println("No coindicen los atributos");
		} else
			System.out.println("No tiene certificado");
		System.out.println(xml);

		Utils.writeToDisk(childPath, "child-bpki-ta.xml", xml);
		Utils.writeToDisk(childPath, "child-bpki-ta.cer", childIdentity.getIdentityCertificate().getEncoded());

		// // 2. Tomo el child request y lo envió al server
		// String childXml = new String(UtilsConstantes.getBytesFromFile(new
		// File(UtilsConstantes.getChildRequest())), UtilsConstantes.UTF8);
		// String respuesta = RPKIProvisioningWsClient.childRequest(childXml);
		// if (respuesta != null) {
		// if (respuesta.contains("parent_response"))
		// writeToDisk("parent-identity.xml", respuesta);
		// else
		// writeToDisk("error-identity.xml", respuesta);
		// } else
		// System.out.println("Algo fallo en el servicio");
		// // 3. Hasta acá
	}
}