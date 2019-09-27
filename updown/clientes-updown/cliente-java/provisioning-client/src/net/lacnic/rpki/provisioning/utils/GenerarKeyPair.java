package net.lacnic.rpki.provisioning.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class GenerarKeyPair {

	public GenerarKeyPair() {

	}

	public static KeyPair generarParClave() {
		try {
			KeyPairGenerator keyGen;
			keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(2048);
			return keyGen.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
