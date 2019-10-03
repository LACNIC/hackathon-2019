package net.lacnic.rpki.provisioning.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class StoragekeyPair {

	public static void almacenarKeyPair(String path, KeyPair key, String publicName, String privateName) {
		StoragekeyPair storage = new StoragekeyPair();
		try {
			storage.almacenarKey(path, key, publicName, privateName);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private void almacenarKey(String path, KeyPair keyPair, String publicName, String privateName) throws IOException {
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		FileOutputStream fos = new FileOutputStream(path + publicName);
		fos.write(x509EncodedKeySpec.getEncoded());
		fos.close();

		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		fos = new FileOutputStream(path + privateName);
		fos.write(pkcs8EncodedKeySpec.getEncoded());
		fos.close();
	}

	public static KeyPair cargarKeyPair(String path, String publicName, String privateName) {
		StoragekeyPair storage = new StoragekeyPair();
		try {
			KeyPair loadedKeyPair = storage.cargarKeyPair(path, "RSA", publicName, privateName);
			return loadedKeyPair;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private KeyPair cargarKeyPair(String path, String algorithm, String publicName, String privateName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		// Read Public Key.
		File filePublicKey = new File(path + publicName);
		FileInputStream fis = new FileInputStream(path + publicName);
		byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
		fis.read(encodedPublicKey);
		fis.close();

		// Read Private Key.
		File filePrivateKey = new File(path + privateName);
		fis = new FileInputStream(path + privateName);
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		fis.read(encodedPrivateKey);
		fis.close();

		// Generate KeyPair.
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		return new KeyPair(publicKey, privateKey);
	}
}
