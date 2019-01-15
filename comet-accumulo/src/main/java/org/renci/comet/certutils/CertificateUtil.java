package org.renci.comet.certutils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.ws.commons.util.Base64;
import org.apache.ws.commons.util.Base64.DecodingException;
//import orca.util.Base64;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInputStream;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.renci.comet.certutils.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.ws.commons.util.Base64;

import javax.net.ssl.HttpsURLConnection; 
import javax.net.ssl.KeyManagerFactory; 
import javax.net.ssl.SSLContext; 
import javax.net.ssl.SSLSocketFactory; 
import javax.net.ssl.TrustManagerFactory; 
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;

public class CertificateUtil {
	private static final Logger log = Logger.getLogger(COMETClientUtils.class);
	private static ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	private boolean strongCheck = true;
	
    /**
     * converts a certificate chain string in to a list of certificate objects
     * 
     * @param  certChainString certChainString
     * @return list of certificate objects
     * @throws CertificateException certificate error
     * @throws IOException io exception error
     * @return list of certs
     */
    public static List<X509Certificate> getCertChain(String certChainString) throws CertificateException, IOException {

        ArrayList<X509Certificate> certificateChain = new ArrayList<X509Certificate>();

        if (certChainString.indexOf("-----BEGIN CERTIFICATE") != -1) {

            BufferedReader bufferedReader = new BufferedReader(new StringReader(certChainString));

            String nextLine = bufferedReader.readLine();
            while (nextLine != null && nextLine.indexOf("-----BEGIN CERTIFICATE") != 1) {
                StringBuffer stringbuffer = new StringBuffer();
                String s1;
                for (; (s1 = bufferedReader.readLine()) != null
                        && s1.indexOf("-----END CERTIFICATE") == -1; stringbuffer.append(s1.trim()));

                if (s1 == null)
                    throw new IOException((new StringBuilder()).append("-----END CERTIFICATE").append(" not found").toString());

                certificateChain.add(createCertFromPem(stringbuffer.toString()));

                nextLine = bufferedReader.readLine();
            }

        } else {
            certificateChain.add(createCertFromPem(certChainString));
        }

        return certificateChain;
    }

    /**
     * converts a pem encoded certificate string in to a certificate object
     * 
     * @param pemString pemString
     * @return X509Certificate
     * @throws CertificateException certificate error
     * @throws DecodingException decoding error 
     */
    public static X509Certificate createCertFromPem(String pemString) throws CertificateException, DecodingException {
        ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(Base64.decode(pemString));
        CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificatefactory.generateCertificate(bytearrayinputstream);
        return certificate;
    }

    /**
     * verifies if the given certificate chain has a path up to a trusted authority
     * 
     * @param certChain certChain
     * @param trustKeyStorePath trustKeyStorePath
     * @param keyStorePassword keyStorePassword
     * @return the trusted root authority
     * @throws CertPathValidatorException certificate validation error
     *             - if a path does not exist
     */
    public static X509Certificate verifyCertChain(List<X509Certificate> certChain, String trustKeyStorePath,
            String keyStorePassword) throws CertPathValidatorException {

        try {
            /* Givens. */
            InputStream trustStoreInput = new FileInputStream(trustKeyStorePath);
            char[] password = keyStorePassword.toCharArray();
            List<X509Certificate> chain = certChain;
            // Collection<X509CRL> crls = new ArrayList<X509CRL>();

            /* Construct a valid path. */
            KeyStore anchors = KeyStore.getInstance(KeyStore.getDefaultType());
            anchors.load(trustStoreInput, password);

            X509CertSelector target = new X509CertSelector();
            target.setCertificate(chain.get(0));

            PKIXBuilderParameters params = new PKIXBuilderParameters(anchors, target);

            CertStoreParameters intermediates = new CollectionCertStoreParameters(chain);
            params.addCertStore(CertStore.getInstance("Collection", intermediates));

            params.setRevocationEnabled(false);

            // CertStoreParameters revoked = new CollectionCertStoreParameters(crls);
            // params.addCertStore(CertStore.getInstance("Collection", revoked));

            CertPath certPath = CertificateFactory.getInstance("X.509").generateCertPath(chain);
            // CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");
            // CertPath certPath = builder.build(params).getCertPath();

            CertPathValidator validator = CertPathValidator.getInstance("PKIX");
            PKIXCertPathValidatorResult pkixCertPathValidatorResult = (PKIXCertPathValidatorResult) validator
                    .validate(certPath, params);

            return pkixCertPathValidatorResult.getTrustAnchor().getTrustedCert();

        } catch (Exception exception) {
            throw new CertPathValidatorException("A valid certificate path does not exist"
                    + System.getProperty("line.separator") + exception.getMessage());
        }
    }
    
    /**
     * Checks whether given X.509 certificate is self-signed.
     * @param cert cert
     * @throws CertificateException cert exception
     * @throws NoSuchAlgorithmException algo exception
     * @throws NoSuchProviderException no provider exception
     * @return boolean
     */
    public static boolean isSelfSigned(X509Certificate cert)
            throws CertificateException, NoSuchAlgorithmException,
            NoSuchProviderException {
        try {
            // Try to verify certificate signature with its own public key
            PublicKey key = cert.getPublicKey();
            cert.verify(key);
            return true;
        } catch (SignatureException sigEx) {
            // Invalid signature --> not self-signed
            return false;
        } catch (InvalidKeyException keyEx) {
            // Invalid key --> not self-signed
            return false;
        }
    }

    /**
     * Method to get the keyid of a certificate; Calls getKeyidFromDER to do the actual work
     * 
     * @param cert cert
     * @return string representing the sha-1 hash of the public key in cert
     * @throws NoSuchAlgorithmException algo error
     * @throws IOException io exception error
     * @return String
     */

    public static String getCertKeyid(X509Certificate cert) throws NoSuchAlgorithmException, IOException {

        if (cert == null) {
            return null;
        }

        @SuppressWarnings({ "deprecation", "resource" })
		DERInputStream inp = new DERInputStream(new ByteArrayInputStream(cert.getPublicKey().getEncoded())); // get
                                                          // cert
        @SuppressWarnings("deprecation")
		String keyid = getKeyidFromDER(inp.readObject());
        if (keyid != null) {
            return keyid;
        } else {
            return null;
        }

    }

    /**
     * Method to find keyid from a DER encoded object (public key in this case)
     * 
     * @param obj obj
     * @return String
     * @throws NoSuchAlgorithmException no algo error
     * @return String
     */
    public static String getKeyidFromDER(DEREncodable obj) throws NoSuchAlgorithmException {
        String returnString = null;
        if (obj instanceof ASN1Sequence) {

            Enumeration seq = ((ASN1Sequence) obj).getObjects();
            while (seq.hasMoreElements()) {
                String hexString = getKeyidFromDERIdOrBitString((DEREncodable) seq.nextElement());
                if (hexString != null) {
                    returnString = hexString;
                }
            }

        }

        return returnString;
    }

    public static String getKeyidFromDERIdOrBitString(DEREncodable obj) throws NoSuchAlgorithmException {
        String hexString = null;
        if (obj instanceof DERObjectIdentifier) {
            // System.out.println(((DERObjectIdentifier) obj).getId());
            // Do nothing
        }
        if (obj instanceof DERBitString) {
            // System.out.println((new BigInteger(((DERBitString)obj).getBytes())).toString(16));
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] der = ((DERBitString) obj).getBytes();
            md.update(der);
            byte[] digest = md.digest();
            hexString = hexify(digest);
        }

        return hexString;
    }

    
    /**
     * Compare base64 encoding of a cert to a first cert in a chain
     * @param cert_base64 cert_base64
     * @param chain chain
     * @return boolean
     */
	protected static boolean compareCertsBase64(String cert_base64, X509Certificate[] chain) {
	    	
	    	if ((cert_base64 == null) || (chain == null) || (chain.length == 0))
	    		return false;
	
	      	// compare the 64-bit encodings of certificates (for simplicity)
	    	byte[] bytes = null;
	
	    	try {
	    		bytes = chain[0].getEncoded();
	    	} catch (CertificateEncodingException e) {
	    		throw new RuntimeException("Failed to encode the certificate");
	    	}
	    	
	    	String base64 = Base64.encode(bytes).trim();
	    	
	    	return cert_base64.trim().equals(base64);
	}
	
	public String testSSLCert(String guid, String cert64) {
		String status = "STATUS for " + guid + " : ";
		
		if (checkSecure()) {
			status += "Security = SSL USED; ";
			if ((getClientCerts() == null) || (getClientCerts().length == 0)) {
				status += "Certificate = NONE; ";
			} else {
				if (compareCertsBase64(cert64, getClientCerts())) {
					status += "Certificate = MATCH; ";
				} else {
					status += "Certificate = MISMATCH; ";
				}
			}
		} else {
			status += "Security = NO SSL USED; ";
		}
		return status;
	}
	
	/**
     * Check if the client is using secure channel
     * @return boolean
     */
	protected boolean checkSecure() {
	    	// if comms are secure, session id will be set
	    	HttpServletRequest pRequest = getThreadRequest();
	    	String sslSessionId = (String)pRequest.getAttribute("javax.servlet.request.ssl_session");
	
	    	// if ssl session id is present, client using SSL/TLS
	    	if (sslSessionId == null) {
	    		return false;
	    	}
	    	return true;
	}
    
	/**
     * Retrieve certificate chain if available (null if not)
     * @return X509Certificate
     */
    protected X509Certificate[] getClientCerts() {
	    	if (checkSecure()) {
	        	HttpServletRequest pRequest = getThreadRequest();
	        	Object certChain = pRequest.getAttribute("javax.servlet.request.X509Certificate");
	    		if (certChain != null) {
	    			return (X509Certificate[])certChain;
	    		} else {
	    			return null;
	    		}
	    	}
	    	return null;
    }
    
    protected static HttpServletRequest getThreadRequest() {
		return (HttpServletRequest)request.get();
	}
    
    /**
     * Check client certificate against the database
     * @param clientID clientID
     * @param trustKeyStorePath trustKeyStorePath
     * @param keyStorePassword keyStorePassword
     * @return boolean
     * @throws CertPathValidatorException certificate validation error 
     */
    protected boolean clientCertCheck(String clientID,  String trustKeyStorePath, String keyStorePassword) throws CertPathValidatorException {
	    	try {
	    		// get the cert chains from SSL
		    	X509Certificate[] certChain = getClientCerts();
		    	InputStream trustStoreInput = new FileInputStream(trustKeyStorePath);
		    	char[] password = keyStorePassword.toCharArray();
		    	KeyStore anchors = KeyStore.getInstance(KeyStore.getDefaultType());
	        anchors.load(trustStoreInput, password);
		    	
		    	if ((certChain == null) || (certChain.length == 0)) {
		    		if (!strongCheck) {
		    			log.info("Client did not present an SSL client certificate, strong checking is disabled, proceeding.");
		    			return true;
		    		} else {
		    			log.error("Client did not present an SSL client certificate, strong checking is enabled, blocking");
		    			return false;
		    		}
		    	}
		    	
		    	if (clientID == null)
		    		return false;
		    	
			X509Certificate cert = CertificateUtil.verifyCertChain(Arrays.asList(certChain), trustKeyStorePath, keyStorePassword);
			
			Enumeration<String> en = anchors.aliases();
			
			while (en.hasMoreElements()) {
				String ali = (String)en.nextElement();
			    if(cert ==  (X509Certificate) anchors.getCertificate(ali)) {
			         return true;
			    }
			}
			
		    return false;
		    
	    	} catch (Exception e) {
	    		throw new CertPathValidatorException("A valid certificate path does not exist"
	                    + System.getProperty("line.separator") + e.getMessage());
	    	}
    }
	
    /**
     * Method to convert to hex from byte array
     * 
     * @param bytes bytes
     * @return String
     */
    public static String hexify(byte bytes[]) {

        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        StringBuffer buf = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; ++i) {
            buf.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
            buf.append(hexDigits[bytes[i] & 0x0f]);
        }

        return buf.toString();
    }

}
