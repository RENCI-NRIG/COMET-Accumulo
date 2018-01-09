package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.InlineResponse2002;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-08T17:32:14.261Z")
public abstract class DeleteScopeApiService {
    public abstract Response deleteScopePost( @NotNull String contextID, @NotNull String family, @NotNull String key, @NotNull String readToken, @NotNull String writeToken,SecurityContext securityContext) throws NotFoundException;
}
