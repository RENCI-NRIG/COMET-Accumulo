

package io.swagger.client.api;

import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;
import io.swagger.client.ProgressRequestBody;
import io.swagger.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import io.swagger.client.model.CometResponse;
import io.swagger.client.model.Value;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultApi {
    private ApiClient apiClient;

    public DefaultApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DefaultApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for deleteScopeDelete
     * @param contextID  (required)
     * @param family  (required)
     * @param key  (required)
     * @param readToken  (required)
     * @param writeToken  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteScopeDeleteCall(String contextID, String family, String key, String readToken, String writeToken, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/deleteScope";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (contextID != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("contextID", contextID));
        if (family != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("family", family));
        if (key != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("Key", key));
        if (readToken != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("readToken", readToken));
        if (writeToken != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("writeToken", writeToken));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call deleteScopeDeleteValidateBeforeCall(String contextID, String family, String key, String readToken, String writeToken, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'contextID' is set
        if (contextID == null) {
            throw new ApiException("Missing the required parameter 'contextID' when calling deleteScopeDelete(Async)");
        }
        
        // verify the required parameter 'family' is set
        if (family == null) {
            throw new ApiException("Missing the required parameter 'family' when calling deleteScopeDelete(Async)");
        }
        
        // verify the required parameter 'key' is set
        if (key == null) {
            throw new ApiException("Missing the required parameter 'key' when calling deleteScopeDelete(Async)");
        }
        
        // verify the required parameter 'readToken' is set
        if (readToken == null) {
            throw new ApiException("Missing the required parameter 'readToken' when calling deleteScopeDelete(Async)");
        }
        
        // verify the required parameter 'writeToken' is set
        if (writeToken == null) {
            throw new ApiException("Missing the required parameter 'writeToken' when calling deleteScopeDelete(Async)");
        }
        

        com.squareup.okhttp.Call call = deleteScopeDeleteCall(contextID, family, key, readToken, writeToken, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Delete scope within a context.  
     * Delete scope within a context.   - Operation requires write access 
     * @param contextID  (required)
     * @param family  (required)
     * @param key  (required)
     * @param readToken  (required)
     * @param writeToken  (required)
     * @return CometResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public CometResponse deleteScopeDelete(String contextID, String family, String key, String readToken, String writeToken) throws ApiException {
        ApiResponse<CometResponse> resp = deleteScopeDeleteWithHttpInfo(contextID, family, key, readToken, writeToken);
        return resp.getData();
    }

    /**
     * Delete scope within a context.  
     * Delete scope within a context.   - Operation requires write access 
     * @param contextID  (required)
     * @param family  (required)
     * @param key  (required)
     * @param readToken  (required)
     * @param writeToken  (required)
     * @return ApiResponse&lt;CometResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<CometResponse> deleteScopeDeleteWithHttpInfo(String contextID, String family, String key, String readToken, String writeToken) throws ApiException {
        com.squareup.okhttp.Call call = deleteScopeDeleteValidateBeforeCall(contextID, family, key, readToken, writeToken, null, null);
        Type localVarReturnType = new TypeToken<CometResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Delete scope within a context.   (asynchronously)
     * Delete scope within a context.   - Operation requires write access 
     * @param contextID  (required)
     * @param family  (required)
     * @param key  (required)
     * @param readToken  (required)
     * @param writeToken  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteScopeDeleteAsync(String contextID, String family, String key, String readToken, String writeToken, final ApiCallback<CometResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteScopeDeleteValidateBeforeCall(contextID, family, key, readToken, writeToken, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<CometResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for enumerateScopeGet
     * @param contextID  (required)
     * @param readToken  (required)
     * @param family  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call enumerateScopeGetCall(String contextID, String readToken, String family, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/enumerateScope";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (contextID != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("contextID", contextID));
        if (family != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("family", family));
        if (readToken != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("readToken", readToken));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call enumerateScopeGetValidateBeforeCall(String contextID, String readToken, String family, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'contextID' is set
        if (contextID == null) {
            throw new ApiException("Missing the required parameter 'contextID' when calling enumerateScopeGet(Async)");
        }
        
        // verify the required parameter 'readToken' is set
        if (readToken == null) {
            throw new ApiException("Missing the required parameter 'readToken' when calling enumerateScopeGet(Async)");
        }
        

        com.squareup.okhttp.Call call = enumerateScopeGetCall(contextID, readToken, family, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Retrieve a list of existing scopes within a given context.   
     * Retrieve a list of existing scopes within a given context.   - Operation requires read access - Returns list of  [ {family, key} ] 
     * @param contextID  (required)
     * @param readToken  (required)
     * @param family  (optional)
     * @return CometResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public CometResponse enumerateScopeGet(String contextID, String readToken, String family) throws ApiException {
        ApiResponse<CometResponse> resp = enumerateScopeGetWithHttpInfo(contextID, readToken, family);
        return resp.getData();
    }

    /**
     * Retrieve a list of existing scopes within a given context.   
     * Retrieve a list of existing scopes within a given context.   - Operation requires read access - Returns list of  [ {family, key} ] 
     * @param contextID  (required)
     * @param readToken  (required)
     * @param family  (optional)
     * @return ApiResponse&lt;CometResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<CometResponse> enumerateScopeGetWithHttpInfo(String contextID, String readToken, String family) throws ApiException {
        com.squareup.okhttp.Call call = enumerateScopeGetValidateBeforeCall(contextID, readToken, family, null, null);
        Type localVarReturnType = new TypeToken<CometResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Retrieve a list of existing scopes within a given context.    (asynchronously)
     * Retrieve a list of existing scopes within a given context.   - Operation requires read access - Returns list of  [ {family, key} ] 
     * @param contextID  (required)
     * @param readToken  (required)
     * @param family  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call enumerateScopeGetAsync(String contextID, String readToken, String family, final ApiCallback<CometResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = enumerateScopeGetValidateBeforeCall(contextID, readToken, family, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<CometResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for getVersionGet
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call getVersionGetCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/getVersion";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call getVersionGetValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        

        com.squareup.okhttp.Call call = getVersionGetCall(progressListener, progressRequestListener);
        return call;

    }

    /**
     * Retrieve the current Comet version and Comet API version. 
     * Retrieve the current Comet version and Comet API version. 
     * @return CometResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public CometResponse getVersionGet() throws ApiException {
        ApiResponse<CometResponse> resp = getVersionGetWithHttpInfo();
        return resp.getData();
    }

    /**
     * Retrieve the current Comet version and Comet API version. 
     * Retrieve the current Comet version and Comet API version. 
     * @return ApiResponse&lt;CometResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<CometResponse> getVersionGetWithHttpInfo() throws ApiException {
        com.squareup.okhttp.Call call = getVersionGetValidateBeforeCall(null, null);
        Type localVarReturnType = new TypeToken<CometResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Retrieve the current Comet version and Comet API version.  (asynchronously)
     * Retrieve the current Comet version and Comet API version. 
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getVersionGetAsync(final ApiCallback<CometResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getVersionGetValidateBeforeCall(progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<CometResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for readScopeGet
     * @param contextID  (required)
     * @param family  (required)
     * @param key  (required)
     * @param readToken  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call readScopeGetCall(String contextID, String family, String key, String readToken, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/readScope";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (contextID != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("contextID", contextID));
        if (family != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("family", family));
        if (key != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("Key", key));
        if (readToken != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("readToken", readToken));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call readScopeGetValidateBeforeCall(String contextID, String family, String key, String readToken, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'contextID' is set
        if (contextID == null) {
            throw new ApiException("Missing the required parameter 'contextID' when calling readScopeGet(Async)");
        }
        
        // verify the required parameter 'family' is set
        if (family == null) {
            throw new ApiException("Missing the required parameter 'family' when calling readScopeGet(Async)");
        }
        
        // verify the required parameter 'key' is set
        if (key == null) {
            throw new ApiException("Missing the required parameter 'key' when calling readScopeGet(Async)");
        }
        
        // verify the required parameter 'readToken' is set
        if (readToken == null) {
            throw new ApiException("Missing the required parameter 'readToken' when calling readScopeGet(Async)");
        }
        

        com.squareup.okhttp.Call call = readScopeGetCall(contextID, family, key, readToken, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Retrieve a value from a named scope within a context.  
     * Retrieve a value from a named scope within a context.  - Operation requires read access  Need to distinguish the following situations: - The scope value is null - The scope existed, but was deleted - The scope never existed (for the period of garbage collection) - Scope visibility doesn’t match 
     * @param contextID  (required)
     * @param family  (required)
     * @param key  (required)
     * @param readToken  (required)
     * @return CometResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public CometResponse readScopeGet(String contextID, String family, String key, String readToken) throws ApiException {
        ApiResponse<CometResponse> resp = readScopeGetWithHttpInfo(contextID, family, key, readToken);
        return resp.getData();
    }

    /**
     * Retrieve a value from a named scope within a context.  
     * Retrieve a value from a named scope within a context.  - Operation requires read access  Need to distinguish the following situations: - The scope value is null - The scope existed, but was deleted - The scope never existed (for the period of garbage collection) - Scope visibility doesn’t match 
     * @param contextID  (required)
     * @param family  (required)
     * @param key  (required)
     * @param readToken  (required)
     * @return ApiResponse&lt;CometResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<CometResponse> readScopeGetWithHttpInfo(String contextID, String family, String key, String readToken) throws ApiException {
        com.squareup.okhttp.Call call = readScopeGetValidateBeforeCall(contextID, family, key, readToken, null, null);
        Type localVarReturnType = new TypeToken<CometResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Retrieve a value from a named scope within a context.   (asynchronously)
     * Retrieve a value from a named scope within a context.  - Operation requires read access  Need to distinguish the following situations: - The scope value is null - The scope existed, but was deleted - The scope never existed (for the period of garbage collection) - Scope visibility doesn’t match 
     * @param contextID  (required)
     * @param family  (required)
     * @param key  (required)
     * @param readToken  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call readScopeGetAsync(String contextID, String family, String key, String readToken, final ApiCallback<CometResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = readScopeGetValidateBeforeCall(contextID, family, key, readToken, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<CometResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for writeScopePost
     * @param string  (required)
     * @param contextID  (required)
     * @param family  (required)
     * @param key  (required)
     * @param readToken  (required)
     * @param writeToken  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call writeScopePostCall(Value string, String contextID, String family, String key, String readToken, String writeToken, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = string;

        // create path and map variables
        String localVarPath = "/writeScope";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (contextID != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("contextID", contextID));
        if (family != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("family", family));
        if (key != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("Key", key));
        if (readToken != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("readToken", readToken));
        if (writeToken != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("writeToken", writeToken));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call writeScopePostValidateBeforeCall(Value string, String contextID, String family, String key, String readToken, String writeToken, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'string' is set
        if (string == null) {
            throw new ApiException("Missing the required parameter 'string' when calling writeScopePost(Async)");
        }
        
        // verify the required parameter 'contextID' is set
        if (contextID == null) {
            throw new ApiException("Missing the required parameter 'contextID' when calling writeScopePost(Async)");
        }
        
        // verify the required parameter 'family' is set
        if (family == null) {
            throw new ApiException("Missing the required parameter 'family' when calling writeScopePost(Async)");
        }
        
        // verify the required parameter 'key' is set
        if (key == null) {
            throw new ApiException("Missing the required parameter 'key' when calling writeScopePost(Async)");
        }
        
        // verify the required parameter 'readToken' is set
        if (readToken == null) {
            throw new ApiException("Missing the required parameter 'readToken' when calling writeScopePost(Async)");
        }
        
        // verify the required parameter 'writeToken' is set
        if (writeToken == null) {
            throw new ApiException("Missing the required parameter 'writeToken' when calling writeScopePost(Async)");
        }
        

        com.squareup.okhttp.Call call = writeScopePostCall(string, contextID, family, key, readToken, writeToken, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): 
     * Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): - Operation requires write access - Substitute existing value with new value 
     * @param string  (required)
     * @param contextID  (required)
     * @param family  (required)
     * @param key  (required)
     * @param readToken  (required)
     * @param writeToken  (required)
     * @return CometResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public CometResponse writeScopePost(Value string, String contextID, String family, String key, String readToken, String writeToken) throws ApiException {
        ApiResponse<CometResponse> resp = writeScopePostWithHttpInfo(string, contextID, family, key, readToken, writeToken);
        return resp.getData();
    }

    /**
     * Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): 
     * Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): - Operation requires write access - Substitute existing value with new value 
     * @param string  (required)
     * @param contextID  (required)
     * @param family  (required)
     * @param key  (required)
     * @param readToken  (required)
     * @param writeToken  (required)
     * @return ApiResponse&lt;CometResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<CometResponse> writeScopePostWithHttpInfo(Value string, String contextID, String family, String key, String readToken, String writeToken) throws ApiException {
        com.squareup.okhttp.Call call = writeScopePostValidateBeforeCall(string, contextID, family, key, readToken, writeToken, null, null);
        Type localVarReturnType = new TypeToken<CometResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin):  (asynchronously)
     * Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): - Operation requires write access - Substitute existing value with new value 
     * @param string  (required)
     * @param contextID  (required)
     * @param family  (required)
     * @param key  (required)
     * @param readToken  (required)
     * @param writeToken  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call writeScopePostAsync(Value string, String contextID, String family, String key, String readToken, String writeToken, final ApiCallback<CometResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = writeScopePostValidateBeforeCall(string, contextID, family, key, readToken, writeToken, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<CometResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
