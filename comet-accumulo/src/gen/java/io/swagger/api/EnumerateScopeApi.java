package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.EnumerateScopeApiService;
import io.swagger.api.factories.EnumerateScopeApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.InlineResponse2001;

import java.util.Map;
import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/enumerateScope")


@io.swagger.annotations.Api(description = "the enumerateScope API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-08T17:32:14.261Z")
public class EnumerateScopeApi  {
   private final EnumerateScopeApiService delegate;

   public EnumerateScopeApi(@Context ServletConfig servletContext) {
      EnumerateScopeApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("EnumerateScopeApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (EnumerateScopeApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = EnumerateScopeApiServiceFactory.getEnumerateScopeApi();
      }

      this.delegate = delegate;
   }

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve a list of existing scopes within a given context.   ", notes = "Retrieve a list of existing scopes within a given context.   - Operation requires read access - Returns list of  [ {family, key} ] ", response = InlineResponse2001.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = InlineResponse2001.class) })
    public Response enumerateScopeGet(@ApiParam(value = "",required=true) @QueryParam("contextID") String contextID
,@ApiParam(value = "",required=true) @QueryParam("family") String family
,@ApiParam(value = "",required=true) @QueryParam("readToken") String readToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.enumerateScopeGet(contextID,family,readToken,securityContext);
    }
}
