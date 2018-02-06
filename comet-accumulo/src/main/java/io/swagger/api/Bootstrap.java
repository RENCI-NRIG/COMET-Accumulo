package io.swagger.api;

import io.swagger.jaxrs.config.SwaggerContextService;

import io.swagger.models.*;

import io.swagger.models.auth.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.accumulo.core.client.AccumuloException;

import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchWriter;
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


public class Bootstrap extends HttpServlet {
    public static final int DefaultServerPort = 8080;
    public static final int DefaultSSLServerPort = 8443;
    private final boolean withSSL = true;

  @Override
  	public void init(ServletConfig config) throws ServletException {
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
	
	    new SwaggerContextService().withServletConfig(config).updateSwagger(swagger);
  	}
}
