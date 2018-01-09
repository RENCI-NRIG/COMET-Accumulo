package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.ReadScopeApiService;
import io.swagger.api.factories.ReadScopeApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.InlineResponse200;

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

@Path("/readScope")


@io.swagger.annotations.Api(description = "the readScope API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-08T17:32:14.261Z")
public class ReadScopeApi  {
   private final ReadScopeApiService delegate;

   public ReadScopeApi(@Context ServletConfig servletContext) {
      ReadScopeApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("ReadScopeApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (ReadScopeApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = ReadScopeApiServiceFactory.getReadScopeApi();
      }

      this.delegate = delegate;
   }

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve a value from a named scope within a context.  ", notes = "Retrieve a value from a named scope within a context.  - Operation requires read access  Need to distinguish the following situations: - The scope value is null - The scope existed, but was deleted - The scope never existed (for the period of garbage collection) - Scope visibility doesn’t match ", response = InlineResponse200.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = InlineResponse200.class) })
    public Response readScopeGet(@ApiParam(value = "",required=true) @QueryParam("contextID") String contextID
,@ApiParam(value = "",required=true) @QueryParam("family") String family
,@ApiParam(value = "",required=true) @QueryParam("Key") String key
,@ApiParam(value = "",required=true) @QueryParam("readToken") String readToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.readScopeGet(contextID,family,key,readToken,securityContext);
    }
}
