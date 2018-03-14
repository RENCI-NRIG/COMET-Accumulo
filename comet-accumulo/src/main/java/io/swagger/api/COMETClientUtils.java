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
import java.security.KeyStoreException;
import java.io.FileInputStream; 
import java.io.IOException; 
import java.io.InputStream; 
import java.util.Arrays;
import java.util.Enumeration;

public class COMETClientUtils {

	private static final Logger log = Logger.getLogger(COMETClientUtils.class);

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
		//Checks if property file has been properly completed.
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
}
