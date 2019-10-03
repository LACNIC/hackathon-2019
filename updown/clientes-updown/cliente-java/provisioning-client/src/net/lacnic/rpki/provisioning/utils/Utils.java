package net.lacnic.rpki.provisioning.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.cert.X509CRL;

import javax.security.auth.x500.X500Principal;

import org.joda.time.DateTime;

import com.google.common.io.Files;

import net.ripe.rpki.commons.crypto.crl.X509CrlBuilder;
import net.ripe.rpki.commons.crypto.x509cert.X509CertificateBuilderHelper;
import net.ripe.rpki.commons.provisioning.cms.ProvisioningCmsObject;
import net.ripe.rpki.commons.provisioning.cms.ProvisioningCmsObjectBuilder;
import net.ripe.rpki.commons.provisioning.cms.ProvisioningCmsObjectValidator;
import net.ripe.rpki.commons.provisioning.payload.AbstractProvisioningPayload;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningCmsCertificate;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningCmsCertificateBuilder;
import net.ripe.rpki.commons.provisioning.x509.ProvisioningIdentityCertificate;
import net.ripe.rpki.commons.validation.ValidationOptions;
import net.ripe.rpki.commons.validation.ValidationResult;

public class Utils {

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
		return getResourcesURI().concat("/carpeta_recursos/");
	}

	public static final String getRutaChild() {
		return getRuta().concat("child/");
	}

	public static final String getRutaIssuance() {
		return getRuta().concat("issuance/");
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
		return getRuta().concat("list/");
	}

	public static final String getRutaRevoke() {
		return getRuta().concat("revoke/");
	}

	public static final String getResources() {
		return getRuta().concat("resources/");
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

	public static void createValidCmsObjec(String path, KeyPair EE_KEYPAIR, AbstractProvisioningPayload payload, String fileName, KeyPair keyPairCliente, ProvisioningIdentityCertificate childCertificate) throws Exception {
		ProvisioningCmsObject resourceClassIssuanceQueryCms = createProvisioningCmsObjectForPayload(EE_KEYPAIR, payload, keyPairCliente, childCertificate);
		validateCmsObject(resourceClassIssuanceQueryCms, childCertificate);
		writeToDisk(path, fileName, resourceClassIssuanceQueryCms.getEncoded());
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

	private static void writeToDisk(String path, String fileName, byte[] encoded) throws IOException {
		File file = new File(path + fileName);
		Files.write(encoded, file);
	}

	public static void writeToDisk(String path, String fileName, String xml) throws IOException {
		File file = new File(path + fileName);
		Files.write(xml, file, Charset.forName("UTF-8"));

	}
}
