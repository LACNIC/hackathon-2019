package net.lacnic.rpki.provisioning.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyPair;

import javax.security.auth.x500.X500Principal;

import org.xml.sax.SAXException;

import com.google.common.io.Files;

import net.lacnic.rpki.provisioning.client.RPKIProvisioningWsClient;
import net.ripe.rpki.commons.provisioning.identity.ChildIdentity;
import net.ripe.rpki.commons.provisioning.identity.ChildIdentitySerializer;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningIdentityCertificate;

public class ChildRequest {
	public static String TOKEN = "fc8ff61-1d9c-4b05-94e5-3e95b5e92d20";
	public static String child_handle = "BR-NICB-LACNIC-5a7qxQ";
	public static final X500Principal SELF_SIGNING_DN_1 = new X500Principal("CN=" + child_handle);

	public static void main(String[] args) throws Exception {
		childRequest();
	}

	private static void childRequest() throws Exception, IOException, SAXException, UnsupportedEncodingException {
		final KeyPair KEY_PAIR = GenerarKeyPair.generarParClave();
		// KeyPair KEY_PAIR = StoragekeyPair.cargarKeyPair();
		StoragekeyPair.almacenarKeyPair(KEY_PAIR);
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

		writeToDisk("child-bpki-ta.xml", xml);
		writeToDisk("child-bpki-ta.cer", childIdentity.getIdentityCertificate().getEncoded());
		// // // hasta acá. 1

		// 2. Tomo el child request y lo envió al server
		String childXml = new String(UtilsConstantes.getBytesFromFile(new File(UtilsConstantes.getChildRequest())), UtilsConstantes.UTF8);
		String respuesta = RPKIProvisioningWsClient.childRequest(childXml);
		if (respuesta != null) {
			if (respuesta.contains("parent_response"))
				writeToDisk("parent-identity.xml", respuesta);
			else
				writeToDisk("error-identity.xml", respuesta);
		} else
			System.out.println("Algo fallo en el servicio");
		// 3. Hasta acá
	}

	private static void writeToDisk(String fileName, String xml) throws IOException {
		File file = new File(UtilsConstantes.getRutaChild() + fileName);
		Files.write(xml, file, Charset.forName("UTF-8"));
	}

	private static void writeToDisk(String fileName, byte[] encoded) throws IOException {
		File file = new File(UtilsConstantes.getRutaChild() + fileName);
		Files.write(encoded, file);
	}

}