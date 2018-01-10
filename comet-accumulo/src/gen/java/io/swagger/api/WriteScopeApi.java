package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.WriteScopeApiService;
import io.swagger.api.factories.WriteScopeApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.CometResponse;
import io.swagger.model.Value;

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

@Path("/writeScope")


@io.swagger.annotations.Api(description = "the writeScope API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-10T21:01:26.756Z")
public class WriteScopeApi  {
   private final WriteScopeApiService delegate;

   public WriteScopeApi(@Context ServletConfig servletContext) {
      WriteScopeApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("WriteScopeApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (WriteScopeApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = WriteScopeApiServiceFactory.getWriteScopeApi();
      }

      this.delegate = delegate;
   }

    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): ", notes = "Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): - Operation requires write access - Substitute existing value with new value ", response = CometResponse.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = CometResponse.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = CometResponse.class),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden", response = CometResponse.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found", response = CometResponse.class),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error", response = CometResponse.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected Error", response = CometResponse.class) })
    public Response writeScopePost(@ApiParam(value = "" ,required=true) Value value
,@ApiParam(value = "",required=true) @QueryParam("contextID") String contextID
,@ApiParam(value = "",required=true) @QueryParam("family") String family
,@ApiParam(value = "",required=true) @QueryParam("Key") String key
,@ApiParam(value = "",required=true) @QueryParam("readToken") String readToken
,@ApiParam(value = "",required=true) @QueryParam("writeToken") String writeToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.writeScopePost(value,contextID,family,key,readToken,writeToken,securityContext);
    }
}
