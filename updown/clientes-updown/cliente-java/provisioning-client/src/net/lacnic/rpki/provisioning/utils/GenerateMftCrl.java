package net.lacnic.rpki.provisioning.utils;

import java.io.File;
import java.math.BigInteger;
import java.net.URI;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.cert.CRLException;
import java.security.cert.X509CRL;
import java.util.EnumSet;

import javax.naming.NamingException;
import javax.security.auth.x500.X500Principal;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.joda.time.DateTime;

import com.google.common.io.Files;

import net.ripe.ipresource.IpResourceSet;
import net.ripe.ipresource.IpResourceType;
import net.ripe.rpki.commons.crypto.ValidityPeriod;
import net.ripe.rpki.commons.crypto.cms.manifest.ManifestCms;
import net.ripe.rpki.commons.crypto.cms.manifest.ManifestCmsBuilder;
import net.ripe.rpki.commons.crypto.crl.X509CrlBuilder;
import net.ripe.rpki.commons.crypto.x509cert.RpkiSignedObjectEeCertificateBuilder;
import net.ripe.rpki.commons.crypto.x509cert.X509ResourceCertificate;
import net.ripe.rpki.commons.crypto.x509cert.X509ResourceCertificateParser;
import net.ripe.rpki.commons.provisioning.payload.issue.request.CertificateIssuanceRequestPayload;
import net.ripe.rpki.commons.provisioning.payload.issue.request.CertificateIssuanceRequestPayloadSerializerBuilder;
import net.ripe.rpki.commons.xml.XStreamXmlSerializer;

public class GenerateMftCrl {
	private static final XStreamXmlSerializer<CertificateIssuanceRequestPayload> SERIALIZER = new CertificateIssuanceRequestPayloadSerializerBuilder().build();

	public static X500Principal ISSUER_CA = new X500Principal("CN=NIC O=NICBR");
	public static final KeyPair ISSUER_KEY = StoragekeyPair.cargarIssuanceKeyPair("public_issuance.key", "private_issuance.key");

	public static final KeyPair KEY = GenerarKeyPair.generarParClave();
	public static final KeyPair KEY2 = GenerarKeyPair.generarParClave();

	public static final String DEFAULT_SIGNATURE_PROVIDER = "SunRsaSign";

	private static final URI CRL_URI = URI.create("rsync://repository.prueba.net/rpki-prueba/certificado-crl.crl");

	// publication point del certificado emitido por lacnic
	private static final URI AIA = URI.create("rsync://repository.lacnic.net/rpki/lacnic/48f083bb-f603-4893-9990-0284c04ceb85/b1a43a71fd3fb07499b20881b274d1a9ce30a331.cer");

	private static final URI MFT_URI = URI.create("rsync://repository.prueba.net/rpki-prueba/manifiesto.mft");

	public static void main(String[] args) throws NamingException, Exception {
		X509CRL crl = generateCrl();

		createValidManifestEECertificate();
		X509ResourceCertificateParser parser2 = new X509ResourceCertificateParser();
		parser2.parse("<cert>", Files.toByteArray(new File(Utils.getRutaMftCrlCargarCertEe())));
		X509ResourceCertificate certificadoEe = parser2.isSuccess() ? parser2.getCertificate() : null;
		ManifestCmsBuilder subject = new ManifestCmsBuilder();
		subject.withManifestNumber(BigInteger.valueOf(68));
		subject.withThisUpdateTime(new DateTime());
		subject.withNextUpdateTime(new DateTime().plusDays(1));
		subject.withCertificate(certificadoEe);
		subject.withSignatureProvider(DEFAULT_SIGNATURE_PROVIDER);
		subject.addFile("certificado-crl.crl", crl.getEncoded());
		ManifestCms manifestCms = subject.build(StoragekeyPair.cargarKeyPairParaMftCrl().getPrivate());
		writeToDisk("manifiesto.mft", manifestCms.getEncoded());

	}

	static X509ResourceCertificate createValidManifestEECertificate() throws Exception {
		RpkiSignedObjectEeCertificateBuilder subject = new RpkiSignedObjectEeCertificateBuilder();
		KeyPair KEY = GenerarKeyPair.generarParClave();
		StoragekeyPair.almacenarKeyPairParaMftCrl(KEY);
		URI crlUri = CRL_URI;
		subject.withCrlUri(crlUri);
		URI manifestUri = MFT_URI;
		subject.withCorrespondingCmsPublicationPoint(manifestUri);
		subject.withSigningKeyPair(ISSUER_KEY);
		subject.withPublicKey(KEY.getPublic());
		DateTime now = new DateTime();
		subject.withValidityPeriod(new ValidityPeriod(now, now.plusDays(1)));
		URI publicationUri = AIA;
		subject.withParentResourceCertificatePublicationUri(publicationUri);
		subject.withSerial(BigInteger.TEN);
		subject.withSubjectDN(getX500Format(wantHashSHA1(KEY.getPublic().getEncoded())));
		subject.withIssuerDN(ISSUER_CA);
		subject.withSignatureProvider(DEFAULT_SIGNATURE_PROVIDER);
		subject.withResources(new IpResourceSet());
		subject.withInheritedResourceTypes(EnumSet.allOf(IpResourceType.class));
		X509ResourceCertificate certificado = subject.build();
		writeToDisk("certificado-ee.cer", certificado.getEncoded());
		return certificado;
	}

	private static X509CRL generateCrl() throws CRLException, Exception {
		X509CrlBuilder builder = new X509CrlBuilder();
		builder.withIssuerDN(ISSUER_CA);
		builder.withAuthorityKeyIdentifier(ISSUER_KEY.getPublic());
		DateTime now = new DateTime();
		builder.withThisUpdateTime(now);
		builder.withNextUpdateTime(now.plusDays(1));
		builder.withNumber(BigInteger.TEN);
		X509CRL crl = builder.build(ISSUER_KEY.getPrivate()).getCrl();
		writeToDisk("certificado-crl.crl", crl.getEncoded());
		return crl;
	}

	private static void writeToDisk(String fileName, byte[] encoded) throws Exception {
		File file = new File(Utils.getRutaMftCrl() + fileName);
		Files.write(encoded, file);
	}

	public static String wantHashSHA1(byte[] buffer) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		md.update(buffer);
		byte[] digest = md.digest();
		HexBinaryAdapter hex = new HexBinaryAdapter();
		return hex.marshal(digest).toLowerCase();

	}

	public static final X500Principal getX500Format(String name) {
		String principalName = "";

		if (name.contains("CN=") || name.contains("cn=")) {
			principalName = name;
		} else {
			principalName = "cn=" + name;
		}

		return new X500Principal(principalName);
	}
}
