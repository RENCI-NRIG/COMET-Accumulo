package io.swagger.api.factories;

import io.swagger.api.ReadScopeApiService;
import io.swagger.api.impl.ReadScopeApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-08T17:32:14.261Z")
public class ReadScopeApiServiceFactory {
    private final static ReadScopeApiService service = new ReadScopeApiServiceImpl();

    public static ReadScopeApiService getReadScopeApi() {
        return service;
    }
}
