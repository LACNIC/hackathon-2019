package net.lacnic.rpki.provisioning.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UtilsConstantes {

	public static String UTF8 = "UTF-8";

	public static final String getResourcesURI() {
		String property = System.getProperty("user.home");
		if (property != null && !property.isEmpty())
			return property;
		else
			return null;
	}

	public static final String getChildRequest() {
		return getRutaChild() + "child-bpki-ta.xml";
	}

	public static final String getParentResponse() {
		return getRutaChild() + "parent-identity.xml";
	}

	public static final String getChildCert() {
		return getRutaChild() + "child-bpki-ta.cer";
	}

	public static final String getChildListCMSRequestAux(String name) {
		return getRutaList() + name;
	}

	public static final String getChildRevokeCMSRequestAux(String name) {
		return getRutaRevoke() + name;
	}

	public static final String getChildCMSRequestAux(String name) {
		return getRutaIssuance() + name;
	}

	public static final String getIssuanceRequestAux(String issuanceName) {
		return getRutaIssuance() + issuanceName;
	}

	public static final String getListRequestAux(String name) {
		return getRutaList() + name;
	}

	public static final String getRevokeRequestPayLoadAux(String name) {
		return getRutaRevoke() + name;
	}

	public static final String getRuta() {
		return getResourcesURI().concat("/pruebasUpDown/");
	}

	public static final String getRutaChild() {
		return getResourcesURI().concat("/pruebasUpDown/child/");
	}

	public static final String getRutaIssuance() {
		return getResourcesURI().concat("/pruebasUpDown/issuance/");
	}

	public static final String getRutaMftCrl() {
		return getResourcesURI().concat("/rpki-mft-crl/");
	}

	public static final String getRutaMftCrlCargarCa() {
		return getRutaMftCrl() + "certificado-ca.cer";
	}

	public static final String getRutaCrl() {
		return getRutaMftCrl() + "certificado-crl.crl";
	}

	public static final String getRutaMftCrlCargarCertEe() {
		return getRutaMftCrl() + "certificado-ee.cer";
	}

	public static final String getRutaMft() {
		return getRutaMftCrl() + "manifiesto.mft";
	}

	public static final String getRutaList() {
		return getResourcesURI().concat("/pruebasUpDown/list/");
	}

	public static final String getRutaRevoke() {
		return getResourcesURI().concat("/pruebasUpDown/revoke/");
	}

	public static final String getResources() {
		return getResourcesURI().concat("/resources-updown/");
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		// Get the size of the file
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}
		is.close();
		return bytes;
	}
}
