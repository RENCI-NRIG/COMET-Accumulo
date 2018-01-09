package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.DeleteScopeApiService;
import io.swagger.api.factories.DeleteScopeApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.InlineResponse2002;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-08T17:32:14.261Z")
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

    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete scope within a context.  ", notes = "Delete scope within a context.   - Operation requires write access ", response = InlineResponse2002.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = InlineResponse2002.class) })
    public Response deleteScopePost(@ApiParam(value = "",required=true) @QueryParam("contextID") String contextID
,@ApiParam(value = "",required=true) @QueryParam("family") String family
,@ApiParam(value = "",required=true) @QueryParam("Key") String key
,@ApiParam(value = "",required=true) @QueryParam("readToken") String readToken
,@ApiParam(value = "",required=true) @QueryParam("writeToken") String writeToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.deleteScopePost(contextID,family,key,readToken,writeToken,securityContext);
    }
}
