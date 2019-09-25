package net.lacnic.rpki.provisioning.utils;

import java.math.BigInteger;
import java.security.KeyPair;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.lang3.Validate;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.joda.time.DateTime;

import net.ripe.rpki.commons.crypto.ValidityPeriod;
import net.ripe.rpki.commons.crypto.x509cert.X509CertificateBuilderHelper;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningIdentityCertificate;

public class BuilderBPKI {
	public static final X500Principal SELF_SIGNING_DN_2 = new X500Principal("CN=Prueba O=Nic");
	private static final int DEFAULT_VALIDITY_TIME_YEARS_FROM_NOW = 10;

	public static ProvisioningIdentityCertificate createProvisioningIdentityCertificate(KeyPair key) throws Exception {
		X509CertificateBuilderHelper builderHelper = new X509CertificateBuilderHelper();
		Validate.notNull(key, "Self Signing KeyPair is required");
		Validate.notNull(ChildRequest.SELF_SIGNING_DN_1, "Self Signing DN is required");
		Validate.notNull(builderHelper.DEFAULT_SIGNATURE_PROVIDER, "Signature Provider is required");
		builderHelper.withSerial(BigInteger.ONE);
		builderHelper.withValidityPeriod(new ValidityPeriod(new DateTime(), new DateTime().plusYears(DEFAULT_VALIDITY_TIME_YEARS_FROM_NOW)));
		builderHelper.withCa(true);
		builderHelper.withKeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign);
		builderHelper.withPublicKey(key.getPublic());
		builderHelper.withSigningKeyPair(key);
		builderHelper.withSubjectDN(ChildRequest.SELF_SIGNING_DN_1);
		builderHelper.withIssuerDN(ChildRequest.SELF_SIGNING_DN_1);
		builderHelper.withSignatureProvider(builderHelper.DEFAULT_SIGNATURE_PROVIDER);
		return new ProvisioningIdentityCertificate(builderHelper.generateCertificate());
	}

	public static ProvisioningIdentityCertificate createProvisioningIdentityCertificateError(KeyPair key) throws Exception {
		X509CertificateBuilderHelper builderHelper = new X509CertificateBuilderHelper();
		Validate.notNull(key, "Self Signing KeyPair is required");
		Validate.notNull(ChildRequest.SELF_SIGNING_DN_1, "Self Signing DN is required");
		Validate.notNull(builderHelper.DEFAULT_SIGNATURE_PROVIDER, "Signature Provider is required");
		builderHelper.withSerial(BigInteger.ONE);
		builderHelper.withValidityPeriod(new ValidityPeriod(new DateTime(), new DateTime().plusYears(DEFAULT_VALIDITY_TIME_YEARS_FROM_NOW)));
		builderHelper.withCa(true);
		builderHelper.withKeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign);
		builderHelper.withPublicKey(key.getPublic());
		builderHelper.withSigningKeyPair(key);
		builderHelper.withSubjectDN(ChildRequest.SELF_SIGNING_DN_1);
		builderHelper.withIssuerDN(SELF_SIGNING_DN_2);
		builderHelper.withSignatureProvider(builderHelper.DEFAULT_SIGNATURE_PROVIDER);
		return new ProvisioningIdentityCertificate(builderHelper.generateCertificate());
	}
}
