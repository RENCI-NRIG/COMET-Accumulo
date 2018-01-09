package io.swagger.api.factories;

import io.swagger.api.EnumerateScopeApiService;
import io.swagger.api.impl.EnumerateScopeApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-08T17:32:14.261Z")
public class EnumerateScopeApiServiceFactory {
    private final static EnumerateScopeApiService service = new EnumerateScopeApiServiceImpl();

    public static EnumerateScopeApiService getEnumerateScopeApi() {
        return service;
    }
}
