package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.CometResponse;
import io.swagger.client.model.Value;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DefaultApi
 */
@Ignore
public class DefaultApiTest {

    private final DefaultApi api = new DefaultApi();

    
    /**
     * Delete scope within a context.  
     *
     * Delete scope within a context.   - Operation requires write access 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteScopeDeleteTest() throws ApiException {
        String contextID = null;
        String family = null;
        String key = null;
        String readToken = null;
        String writeToken = null;
        CometResponse response = api.deleteScopeDelete(contextID, family, key, readToken, writeToken);

        // TODO: test validations
    }
    
    /**
     * Retrieve a list of existing scopes within a given context.   
     *
     * Retrieve a list of existing scopes within a given context.   - Operation requires read access - Returns list of  [ {family, key} ] 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void enumerateScopeGetTest() throws ApiException {
        String contextID = null;
        String readToken = null;
        String family = null;
        CometResponse response = api.enumerateScopeGet(contextID, readToken, family);

        // TODO: test validations
    }
    
    /**
     * Retrieve the current Comet version and Comet API version. 
     *
     * Retrieve the current Comet version and Comet API version. 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getVersionGetTest() throws ApiException {
        CometResponse response = api.getVersionGet();

        // TODO: test validations
    }
    
    /**
     * Retrieve a value from a named scope within a context.  
     *
     * Retrieve a value from a named scope within a context.  - Operation requires read access  Need to distinguish the following situations: - The scope value is null - The scope existed, but was deleted - The scope never existed (for the period of garbage collection) - Scope visibility doesn’t match 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void readScopeGetTest() throws ApiException {
        String contextID = null;
        String family = null;
        String key = null;
        String readToken = null;
        CometResponse response = api.readScopeGet(contextID, family, key, readToken);

        // TODO: test validations
    }
    
    /**
     * Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): 
     *
     * Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): - Operation requires write access - Substitute existing value with new value 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void writeScopePostTest() throws ApiException {
        Value string = null;
        String contextID = null;
        String family = null;
        String key = null;
        String readToken = null;
        String writeToken = null;
        CometResponse response = api.writeScopePost(string, contextID, family, key, readToken, writeToken);

        // TODO: test validations
    }
    
}
