package io.swagger.api.factories;

import io.swagger.api.WriteScopeApiService;
import io.swagger.api.impl.WriteScopeApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-08T17:32:14.261Z")
public class WriteScopeApiServiceFactory {
    private final static WriteScopeApiService service = new WriteScopeApiServiceImpl();

    public static WriteScopeApiService getWriteScopeApi() {
        return service;
    }
}
