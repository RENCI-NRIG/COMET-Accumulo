package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;


 
public class HTTPSClient {
    private String host = "127.0.0.1";
    private int port = 9999;
     
    public static void main(String[] args){
        HTTPSClient client = new HTTPSClient();
        client.run();
    }
     
    HTTPSClient(){      
    }
     
    HTTPSClient(String host, int port){
        this.host = host;
        this.port = port;
    }
     
    // Create the and initialize the SSLContext
    private SSLContext createSSLContext(){
        try{
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream("/Users/congwang/Desktop/CAMP/keys/p12s/cometclient-self.p12");
            keyStore.load(fis, "".toCharArray());
            fis.close();
            
            //KeyStore trustStore = KeyStore.getInstance("PKCS12");
            //FileInputStream fis1 = new FileInputStream("/Users/congwang/Desktop/CAMP/keys/p12s/cometclient-trust.p12");
            //trustStore.load(fis1, "".toCharArray());
            //fis1.close();

            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "".toCharArray());
            KeyManager[] km = keyManagerFactory.getKeyManagers();
             
            // Create trust manager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    //return null;
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
             
            return sslContext;
        } catch (Exception ex){
            ex.printStackTrace();
        }
         
        return null;
    }
     
    // Start to run the server
    public void run() {
        SSLContext sslContext = this.createSSLContext();
         
        try{
            // Create socket factory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
             
            // Create socket
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(this.host, this.port);
            sslSocket.setEnabledProtocols(sslSocket.getEnabledProtocols());
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
             
            System.out.println("Client: SSL client started");
            new ClientThread(sslSocket).start();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
     
    // Thread handling the socket to server
    static class ClientThread extends Thread {
        private SSLSocket sslSocket = null;
         
        ClientThread(SSLSocket sslSocket){
            this.sslSocket = sslSocket;
        }
         
        public void run(){
            try {
                // Start handshake
                sslSocket.startHandshake();
                 
                // Get session after the connection is established
                SSLSession sslSession = sslSocket.getSession();
                 
                System.out.println("Client: SSLSession :");
                System.out.println("Client: Protocol : "+sslSession.getProtocol());
                System.out.println("Client: Cipher suite : "+sslSession.getCipherSuite());
                 
                // Start handling application content
                InputStream inputStream = sslSocket.getInputStream();
                OutputStream outputStream = sslSocket.getOutputStream();
                 
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
                 
                // Write data
                printWriter.println("Hello server");
                printWriter.println();
                printWriter.flush();
                 
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    System.out.println("Client: Input : "+line);
                     
                    if(line.trim().equals("HTTP/1.1 200\r\n")){
                        break;
                    }
                }
                 
                sslSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
