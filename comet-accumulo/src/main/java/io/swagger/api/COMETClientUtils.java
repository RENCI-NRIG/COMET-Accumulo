package io.swagger.api;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import certutils.*;
import org.apache.ws.commons.util.Base64;

import javax.net.ssl.HttpsURLConnection; 
import javax.net.ssl.KeyManagerFactory; 
import javax.net.ssl.SSLContext; 
import javax.net.ssl.SSLSocketFactory; 
import javax.net.ssl.TrustManagerFactory; 
import java.security.GeneralSecurityException; 
import java.security.KeyStore; 
import java.io.FileInputStream; 
import java.io.IOException; 
import java.io.InputStream; 
import java.util.Arrays;
import certutils.*;


public class COMETClientUtils {

	private static final Logger log = Logger.getLogger(COMETClientUtils.class);
	private static ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	private boolean strongCheck = true;

	public static Properties getClientConfigProps(String filePathString)	throws IOException {

		Path path = Paths.get(filePathString);
		Properties props = new Properties();

		if (Files.notExists(path)) {
			log.error("Client configuration file  " + filePathString	+ " not found.");
			throw new FileNotFoundException(filePathString);
		}

		props.load(new FileInputStream(filePathString));

	//	if (log.getLevel().isGreaterOrEqual(Level.DEBUG)) {
			PrintWriter writer = new PrintWriter(System.out);
			props.list(writer);
			writer.flush();
	//	}

		if(!checkProperties(props)) {
			try {
				throw new Exception("Missing property");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return props;
	}

	private static boolean checkProperties(Properties props){		
		//To be implemented
		return true;
	}
	
	 public static ByteBuffer storeStringInByteBuffer(String inputString) {
		 
		// Allocate a new non-direct byte buffer with a 50 byte capacity

	    // set this to a big value to avoid BufferOverflowException
		ByteBuffer buf = ByteBuffer.allocate(50); 
		
		// Creates a view of this byte buffer as a char buffer
		CharBuffer cbuf = buf.asCharBuffer();
		
		// Write a string to char buffer
		cbuf.put(inputString);
		
		// Flips this buffer.  The limit is set to the current position and then
		// the position is set to zero.  If the mark is defined then it is discarded
		cbuf.flip();
		
		String s = cbuf.toString();  // a string
		
		//System.out.println(s);
		
		return buf;
	 }
	 
	 /**
	     * Compare base64 encoding of a cert to a first cert in a chain
	     * @param act_base64
	     * @param chain
	     * @return
	     */
    protected static boolean compareCertsBase64(String act_base64, X509Certificate[] chain) {
	    	
	    	if ((act_base64 == null) || (chain == null) || (chain.length == 0))
	    		return false;

	      	// compare the 64-bit encodings of certificates (for simplicity)
	    	byte[] bytes = null;

	    	try {
	    		bytes = chain[0].getEncoded();
	    	} catch (CertificateEncodingException e) {
	    		throw new RuntimeException("Failed to encode the certificate");
	    	}
	    	
	    	String base64 = Base64.encode(bytes).trim();
	    	
	    	return act_base64.trim().equals(base64);
    }
	 
	public String testSSLCert(String guid, String cert64) {
	    	String status = "STATUS for " + guid + " : ";
	    	
	    	if (checkSecure()) {
	    		status += "Security = SSL USED; ";
	    		if ((getClientCerts() == null) || (getClientCerts().length == 0)) {
	    			status += "Certificate = NONE; ";
	    		} else {
	    			if (COMETClientUtils.compareCertsBase64(cert64, getClientCerts())) {
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
	     * @return
	     */
    protected boolean checkSecure() {
	    	// if comms are secure, session id will be set
	    	HttpServletRequest pRequest = COMETClientUtils.getThreadRequest();
	    	String sslSessionId = (String)pRequest.getAttribute("javax.servlet.request.ssl_session");

	    	// if ssl session id is present, client using SSL/TLS
	    	if (sslSessionId == null) {
	    		return false;
	    	}
	    	return true;
    }
    
    /**
     * Retrieve certificate chain if available (null if not)
     * @return
     */
    protected X509Certificate[] getClientCerts() {
	    	if (checkSecure()) {
	        	HttpServletRequest pRequest = COMETClientUtils.getThreadRequest();
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
     * @param act_guid
     * @return
     */
    protected boolean clientCertCheck(String clientID,  String trustKeyStorePath, String keyStorePasswor) {
	    	// get the cert chains from SSL
	    	X509Certificate[] certChain = getClientCerts();
	    	
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
	    	
	    	try {
			X509Certificate cert = CertificateUtil.verifyCertChain(Arrays.asList(certChain), trustKeyStorePath, keyStorePasswor);
		} catch (CertPathValidatorException e) {
			return false;
		}
	    	
	    return false;
    }
    
}
