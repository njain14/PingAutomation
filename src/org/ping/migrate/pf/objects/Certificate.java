package org.ping.migrate.pf.objects;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONObject;
import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;

public class Certificate {

	
	public static JSONObject getSPCertificate(Part filePart) throws IOException {
		
		JSONObject encryptCertJson = new JSONObject();
		JSONObject encryptCertViewJson = new JSONObject();
		JSONObject encryptCertX509fileJson = new JSONObject();
		InputStream in = filePart.getInputStream();
		CertificateFactory certFactory;
		try {
			certFactory = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
			if (null == cert) {
				System.out.println("cert must Not null");
			}
			try {
				cert.checkValidity();
				encryptCertViewJson.put("serialNumber", cert.getSerialNumber().toString(16));
				encryptCertViewJson.put("subjectDN", cert.getSubjectDN());
				encryptCertViewJson.put("subjectAlternativeNames", getSubjectAlternativeNames(cert));
				encryptCertViewJson.put("issuerDN", cert.getIssuerDN());
				encryptCertViewJson.put("signatureAlgorithm", cert.getSigAlgName());
				encryptCertViewJson.put("version", cert.getVersion());
				encryptCertViewJson.put("status", "VALID");
				SimpleDateFormat sdf;
				sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				encryptCertViewJson.put("validFrom", sdf.format(cert.getNotBefore()));
				encryptCertViewJson.put("expires", sdf.format(cert.getNotAfter()));

				encryptCertViewJson.put("sha1Fingerprint", DatatypeConverter
						.printHexBinary(MessageDigest.getInstance("SHA-1").digest(cert.getEncoded())).toUpperCase());
				encryptCertViewJson.put("sha256Fingerprint", DatatypeConverter
						.printHexBinary(MessageDigest.getInstance("SHA-256").digest(cert.getEncoded())).toUpperCase());

				RSAPublicKey rsaPk = (RSAPublicKey) cert.getPublicKey();

				encryptCertViewJson.put("keyAlgorithm", cert.getPublicKey().getAlgorithm());
				encryptCertViewJson.put("keySize", rsaPk.getModulus().bitLength());

				BASE64Encoder encoder = new BASE64Encoder();
				String fileData = X509Factory.BEGIN_CERT + "\n" + encoder.encodeBuffer(cert.getEncoded())
						+ X509Factory.END_CERT + "\n";
				encryptCertX509fileJson.put("fileData", fileData);

				encryptCertJson.put("primaryVerificationCert", true);
				encryptCertJson.put("secondaryVerificationCert", false);
				encryptCertJson.put("certView", encryptCertViewJson);
				encryptCertJson.put("x509File", encryptCertX509fileJson);
				encryptCertJson.put("encryptionCert", true);
				encryptCertJson.put("activeVerificationCert", true);

			} catch (Exception e) {
				System.out.println("verifyCertificate fail" + e);
			}

		} catch (CertificateException e1) {
			e1.printStackTrace();
		}
		return encryptCertJson;
	}

	public static List<String> getSubjectAlternativeNames(final X509Certificate certificate)
			throws CertificateParsingException {

		final Collection<List<?>> altNames = certificate.getSubjectAlternativeNames();
		if (altNames == null) {
			return new ArrayList<>();
		}

		final List<String> result = new ArrayList<>();
		for (final List<?> generalName : altNames) {
			final Object value = generalName.get(1);
			if (value instanceof String) {
				result.add(((String) value).toLowerCase());
			}
		}
		return result;
	}
}
