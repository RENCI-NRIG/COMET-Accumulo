# DefaultApi

All URIs are relative to *https://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteScopeDelete**](DefaultApi.md#deleteScopeDelete) | **DELETE** /deleteScope | Delete scope within a context.  
[**enumerateScopeGet**](DefaultApi.md#enumerateScopeGet) | **GET** /enumerateScope | Retrieve a list of existing scopes within a given context.   
[**getVersionGet**](DefaultApi.md#getVersionGet) | **GET** /getVersion | Retrieve the current Comet version and Comet API version. 
[**readScopeGet**](DefaultApi.md#readScopeGet) | **GET** /readScope | Retrieve a value from a named scope within a context.  
[**writeScopePost**](DefaultApi.md#writeScopePost) | **POST** /writeScope | Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): 


<a name="deleteScopeDelete"></a>
# **deleteScopeDelete**
> CometResponse deleteScopeDelete(contextID, family, key, readToken, writeToken)

Delete scope within a context.  

Delete scope within a context.   - Operation requires write access 

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String contextID = "contextID_example"; // String | 
String family = "family_example"; // String | 
String key = "key_example"; // String | 
String readToken = "readToken_example"; // String | 
String writeToken = "writeToken_example"; // String | 
try {
    CometResponse result = apiInstance.deleteScopeDelete(contextID, family, key, readToken, writeToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#deleteScopeDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contextID** | **String**|  |
 **family** | **String**|  |
 **key** | **String**|  |
 **readToken** | **String**|  |
 **writeToken** | **String**|  |

### Return type

[**CometResponse**](CometResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="enumerateScopeGet"></a>
# **enumerateScopeGet**
> CometResponse enumerateScopeGet(contextID, readToken, family)

Retrieve a list of existing scopes within a given context.   

Retrieve a list of existing scopes within a given context.   - Operation requires read access - Returns list of  [ {family, key} ] 

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String contextID = "contextID_example"; // String | 
String readToken = "readToken_example"; // String | 
String family = "family_example"; // String | 
try {
    CometResponse result = apiInstance.enumerateScopeGet(contextID, readToken, family);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#enumerateScopeGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contextID** | **String**|  |
 **readToken** | **String**|  |
 **family** | **String**|  | [optional]

### Return type

[**CometResponse**](CometResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getVersionGet"></a>
# **getVersionGet**
> CometResponse getVersionGet()

Retrieve the current Comet version and Comet API version. 

Retrieve the current Comet version and Comet API version. 

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
try {
    CometResponse result = apiInstance.getVersionGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#getVersionGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**CometResponse**](CometResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="readScopeGet"></a>
# **readScopeGet**
> CometResponse readScopeGet(contextID, family, key, readToken)

Retrieve a value from a named scope within a context.  

Retrieve a value from a named scope within a context.  - Operation requires read access  Need to distinguish the following situations: - The scope value is null - The scope existed, but was deleted - The scope never existed (for the period of garbage collection) - Scope visibility doesn’t match 

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String contextID = "contextID_example"; // String | 
String family = "family_example"; // String | 
String key = "key_example"; // String | 
String readToken = "readToken_example"; // String | 
try {
    CometResponse result = apiInstance.readScopeGet(contextID, family, key, readToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#readScopeGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contextID** | **String**|  |
 **family** | **String**|  |
 **key** | **String**|  |
 **readToken** | **String**|  |

### Return type

[**CometResponse**](CometResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="writeScopePost"></a>
# **writeScopePost**
> CometResponse writeScopePost(string, contextID, family, key, readToken, writeToken)

Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): 

Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): - Operation requires write access - Substitute existing value with new value 

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
Value string = new Value(); // Value | 
String contextID = "contextID_example"; // String | 
String family = "family_example"; // String | 
String key = "key_example"; // String | 
String readToken = "readToken_example"; // String | 
String writeToken = "writeToken_example"; // String | 
try {
    CometResponse result = apiInstance.writeScopePost(string, contextID, family, key, readToken, writeToken);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#writeScopePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **string** | [**Value**](Value.md)|  |
 **contextID** | **String**|  |
 **family** | **String**|  |
 **key** | **String**|  |
 **readToken** | **String**|  |
 **writeToken** | **String**|  |

### Return type

[**CometResponse**](CometResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

