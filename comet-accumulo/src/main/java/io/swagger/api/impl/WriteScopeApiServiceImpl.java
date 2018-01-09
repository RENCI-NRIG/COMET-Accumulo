package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;


import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-08T17:32:14.261Z")
public class WriteScopeApiServiceImpl extends WriteScopeApiService {
    @Override
    public Response writeScopePost( @NotNull String contextID,  @NotNull String family,  @NotNull String key,  @NotNull String value,  @NotNull String readToken,  @NotNull String writeToken, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
