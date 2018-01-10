package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.DeleteScopeApiService;
import io.swagger.api.factories.DeleteScopeApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.CometResponse;

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

@Path("/deleteScope")


@io.swagger.annotations.Api(description = "the deleteScope API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-10T21:01:26.756Z")
public class DeleteScopeApi  {
   private final DeleteScopeApiService delegate;

   public DeleteScopeApi(@Context ServletConfig servletContext) {
      DeleteScopeApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("DeleteScopeApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (DeleteScopeApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = DeleteScopeApiServiceFactory.getDeleteScopeApi();
      }

      this.delegate = delegate;
   }

    @DELETE
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete scope within a context.  ", notes = "Delete scope within a context.   - Operation requires write access ", response = CometResponse.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = CometResponse.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = CometResponse.class),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden", response = CometResponse.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found", response = CometResponse.class),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error", response = CometResponse.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected Error", response = CometResponse.class) })
    public Response deleteScopeDelete(@ApiParam(value = "",required=true) @QueryParam("contextID") String contextID
,@ApiParam(value = "",required=true) @QueryParam("family") String family
,@ApiParam(value = "",required=true) @QueryParam("Key") String key
,@ApiParam(value = "",required=true) @QueryParam("readToken") String readToken
,@ApiParam(value = "",required=true) @QueryParam("writeToken") String writeToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.deleteScopeDelete(contextID,family,key,readToken,writeToken,securityContext);
    }
}
