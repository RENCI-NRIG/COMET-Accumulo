# swagger-java-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-java-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-java-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-java-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.DefaultApi;

import java.io.File;
import java.util.*;

public class DefaultApiExample {

    public static void main(String[] args) {
        
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
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://localhost:8080*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*DefaultApi* | [**deleteScopeDelete**](docs/DefaultApi.md#deleteScopeDelete) | **DELETE** /deleteScope | Delete scope within a context.  
*DefaultApi* | [**enumerateScopeGet**](docs/DefaultApi.md#enumerateScopeGet) | **GET** /enumerateScope | Retrieve a list of existing scopes within a given context.   
*DefaultApi* | [**getVersionGet**](docs/DefaultApi.md#getVersionGet) | **GET** /getVersion | Retrieve the current Comet version and Comet API version. 
*DefaultApi* | [**readScopeGet**](docs/DefaultApi.md#readScopeGet) | **GET** /readScope | Retrieve a value from a named scope within a context.  
*DefaultApi* | [**writeScopePost**](docs/DefaultApi.md#writeScopePost) | **POST** /writeScope | Create or modify a named scope for slice/sliver within a context, with visibility label (user_key | comet_admin): 


## Documentation for Models

 - [CometResponse](docs/CometResponse.md)
 - [Value](docs/Value.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

cwang@renci.org

