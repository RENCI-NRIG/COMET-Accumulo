package io.swagger.api.factories;

import io.swagger.api.DeleteScopeApiService;
import io.swagger.api.impl.DeleteScopeApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-08T17:32:14.261Z")
public class DeleteScopeApiServiceFactory {
    private final static DeleteScopeApiService service = new DeleteScopeApiServiceImpl();

    public static DeleteScopeApiService getDeleteScopeApi() {
        return service;
    }
}
