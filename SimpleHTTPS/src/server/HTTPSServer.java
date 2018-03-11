package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertPath;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
 
public class HTTPSServer {
    private int port = 9999;
    private boolean isServerDone = false;
     
    public static void main(String[] args){
        HTTPSServer server = new HTTPSServer();
        server.run();
    }
     
    HTTPSServer(){      
    }
     
    HTTPSServer(int port){
        this.port = port;
    }
     
    private SSLContext createSSLContext(){
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream("/Users/congwang/Desktop/CAMP/keys/p12s/cometserver.p12");
            keyStore.load(fis, "".toCharArray());
            fis.close();
            
            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "".toCharArray());
            KeyManager[] km = keyManagerFactory.getKeyManagers();
            
            // Create trust manager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            //TrustManager[] tm = trustManagerFactory.getTrustManagers();
            TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                		return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {

                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            };
            
            // Initialize SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(km, new TrustManager[] { tm }, null);
            //sslContext.init(km, tm, null);
             
            return sslContext;
        } catch (Exception ex){
            ex.printStackTrace();
        }
         
        return null;
    }
     
    // Start to run the server
    public void run(){
        SSLContext sslContext = this.createSSLContext();
        
        try {
            // Create server socket factory
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            
            // Create server socket
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(this.port);
            String[] protocols = sslServerSocket.getSupportedProtocols();
            String[] suites = sslServerSocket.getSupportedCipherSuites();
            sslServerSocket.setEnabledProtocols(protocols);
            sslServerSocket.setEnabledCipherSuites(suites);
 
            System.out.println("SSL server started");
            while(!isServerDone){
            		System.out.println("!isServerDone");
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                System.out.println("sslServerSocket accepted");
                // Start the server thread
                new ServerThread(sslSocket).start();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
     
    // Thread handling the socket from client
    static class ServerThread extends Thread {
        private SSLSocket sslSocket = null;
         
        ServerThread(SSLSocket sslSocket){
            this.sslSocket = sslSocket;
        }
         
        public void run(){            
            try {
            	sslSocket.setNeedClientAuth(true);
                // Start handshake
                sslSocket.startHandshake();
                 
                System.out.println("handshake started");
                // Get session after the connection is established
                SSLSession sslSession = sslSocket.getSession();
                System.out.println("get session");
                
                //java.security.cert.Certificate[] clientCerts = sslSession.getPeerCertificates();
                //List mylist = new ArrayList();
                //for (int i = 0; i < clientCerts.length; i++) {
                //  mylist.add(clientCerts[i]);
                //}
                //CertificateFactory cf = CertificateFactory.getInstance("X.509");
                //CertPath cp = cf.generateCertPath(mylist);
                 
                //String host = sslSession.getPeerHost();
                javax.security.cert.X509Certificate[] certs = sslSession.getPeerCertificateChain();
                System.out.println("got cert chain");
                for (javax.security.cert.Certificate cert : certs)
                		System.out.println("Server says found client cert: " + cert);
                
                System.out.println("Server: SSLSession :");
                System.out.println("Server: Protocol : "+sslSession.getProtocol());
                System.out.println("Server: Cipher suite : "+sslSession.getCipherSuite());
                 
                // Start handling application content
                InputStream inputStream = sslSocket.getInputStream();
                OutputStream outputStream = sslSocket.getOutputStream();
                 
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
                 
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    System.out.println("Server: Input : "+line);
                     
                    if(line.trim().isEmpty()){
                        break;
                    }
                }
                 
                // Write data
                printWriter.print("HTTP/1.1 200\r\n");
                printWriter.flush();
                 
                sslSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
