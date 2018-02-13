package io.swagger.api;

import io.swagger.jaxrs.config.SwaggerContextService;

import io.swagger.models.*;

import io.swagger.models.auth.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.accumulo.core.client.AccumuloException;

import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.ClientConfiguration;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.TableExistsException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.accumulo.core.client.admin.TableOperations;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.log4j.Logger;
import org.apache.hadoop.io.Text;
import certutils.*;


public class Bootstrap extends HttpServlet {
    public static final int DefaultServerPort = 8080;
    public static final int DefaultSSLServerPort = 8443;
    private final boolean withSSL = true;
    String configFile;
    String keystore;
	String truststore;
	String keystorePass;
	String truststorePass;
	String accumuloInstance;
	String zookeeperHosts;
	ClientConfiguration clientConf;
	Connector cometConnector;
	
  @Override
  	public void init(ServletConfig config) throws ServletException {
	  	Properties props = null;
	  	final Logger log = Logger.getLogger(Bootstrap.class);
	  	final String keystore;
		final String truststore;
		final String keystorePass;
		final String truststorePass;
		final String accumuloInstance;
		final String zookeeperHosts;
		
	  	Info info = new Info()
	      .title("Swagger Server")
	      .description("COMET Accumulo Query Layer API")
	      .termsOfService("None")
	      .contact(new Contact()
    		  .email("cwang@renci.org"))
	      .license(new License()
		  .name("Eclipse Public License"));
	
	  	ServletContext context = config.getServletContext();
	    Swagger swagger = new Swagger().info(info);
	    
	    super.init(config);
	    
	    new SwaggerContextService().withServletConfig(config).updateSwagger(swagger);
	    
	    try {
			props = COMETClientUtils.getClientConfigProps(configFile);
		} catch (IOException e1) {
			log.error("Error initializing accumulo client. Message: " + e1.getMessage());
			e1.printStackTrace();
		}
	    
	    keystore = props
				.getProperty(ClientConfigProps.COMET_CLIENT_KEYSTORE_PROP);
		truststore = props
				.getProperty(ClientConfigProps.COMET_CLIENT_TRUSTTORE_PROP);
		keystorePass = props
				.getProperty(ClientConfigProps.COMET_CLIENT_KEYSTORE_PASS_PROP);
		truststorePass = props
				.getProperty(ClientConfigProps.COMET_CLIENT_TRUSTSTORE_PASS_PROP);
		accumuloInstance = props
				.getProperty(ClientConfigProps.COMET_CLIENT_ACCUMULO_INSTANCE_PROP);
		zookeeperHosts = props
				.getProperty(ClientConfigProps.COMET_CLIENT_ZOOKEEPERS_HOSTS_PROP);
		
		clientConf = new ClientConfiguration();
		clientConf.withSsl(withSSL);
		clientConf.withKeystore(keystore, keystorePass, "JKS");
		clientConf.withTruststore(truststore, truststorePass, "JKS");
		clientConf.withInstance(accumuloInstance);
		clientConf.withZkHosts(zookeeperHosts);
		Instance instance = new ZooKeeperInstance(clientConf);

		try {
			cometConnector = instance.getConnector("root", "secret");
		} catch (AccumuloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AccumuloSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	}
}
