# COMET phase 2 

## About

COMET is a distributed RESTful meta-data service which stores key-value oriented configuration information about resources and applications running in the distributed cloud. Clients of COMET are elements of IaaS cloud provider system, user client tools, applications running in tenant virtual systems/slices. COMET provides strong authorization controls ensuring that information is only shared with appropriate clients.

COMET phase 2 is based on new design of mapping cloud service entries to [Accumulo](https://accumulo.apache.org) table entries.


![comet accumulo architecture](https://user-images.githubusercontent.com/29924060/35002282-2bcad8f4-fab7-11e7-882c-0f5041f81cbd.png)

## COMET API implementation

The [COMET Accumulo Query Layer API](https://app.swaggerhub.com/apis/cwang/Comet-Accumulo-Query-Layer/1.0.0) has been created using [Swagger API 2.0](https://swagger.io/docs/specification/2-0/basic-structure/), and is also available as YAML in [specifications/swagger.yaml](specifications/swagger.yaml)

### API Server

Swagger enables the generation of clients and servers in a variety of common programming languages via the [swagger codegen](https://github.com/swagger-api/swagger-codegen) project.

- **Clients** are generated to be fully formed and functional from the generated files including documentation
- **Servers** are generated as stubbed code, and require the logical operations to be added by the user

The server within this repository is based on [Jersey](https://jersey.github.io) and [Java API for RESTful Web Services (JAX-RS)](https://en.wikipedia.org/wiki/Java_API_for_RESTful_Web_Services)

**Generate a new server stub**

- In a browser, go to [https://app.swaggerhub.com/apis/cwang/Comet-Accumulo-Query-Layer/1.0.0](https://app.swaggerhub.com/apis/cwang/Comet-Accumulo-Query-Layer/1.0.0)
- From the generate code icon (downward facing arrow), select **Server** > **jaxrs**
- A file named `jaxrs-server-generated.zip` should be downloaded.
- Once unzipped, the file structure should look as follows

  ![screen shot 2018-01-10 at 1 41 20 pm](https://user-images.githubusercontent.com/5332509/34789379-07249b7a-f60c-11e7-837a-578588b0d55d.png)

### Running the server

The server stub is runnable upon generation, though may require a small modification to the `pom.xml` file to do so the first time.

Remove the provided scope line from the `pom.xml` file as discussed in [swagger-codegen/issues/5091](https://github.com/swagger-api/swagger-codegen/issues/5091).

- From:

	```xml
	...
	    <!-- Bean Validation API support -->
	    <dependency>
	        <groupId>javax.validation</groupId>
	        <artifactId>validation-api</artifactId>
	        <version>1.1.0.Final</version>
	        <scope>provided</scope>
	    </dependency>
	...
	```

- To:

	```xml
	...
	    <!-- Bean Validation API support -->
	    <dependency>
	        <groupId>javax.validation</groupId>
	        <artifactId>validation-api</artifactId>
	        <version>1.1.0.Final</version>
	    </dependency>
	...
	```

Run the server

```
cd /PATH_TO/jaxrs-server-generated/
mvn clean package jetty:run
```

Validate that the server is running at: [http://localhost:8080/v1/swagger.json](http://localhost:8080/v1/swagger.json)

The stubbed server will not have any logic encoded into it, however should return the response `magic` for calls made to valid endpoints.

Example:

```
$ curl -i "localhost:8080/v1/readScope"
HTTP/1.1 200 OK
Date: Wed, 10 Jan 2018 18:56:27 GMT
Access-Control-Allow-Origin: *
Access-Control-Allow-Methods: GET, POST, DELETE, PUT
Access-Control-Allow-Headers: Content-Type
Content-Type: application/json
Content-Length: 41
Server: Jetty(9.2.9.v20150224)

{"code":4,"type":"ok","message":"magic!"}
```

### Updates to Swagger specification

Since swaggerhub only generates server stub code, it becomes the task of the developer(s) to differentiate foundational code changes that occur when the underlying specification is updated.

There is no good way to predict a-priori which elements will need to be modified, and the experience of the developer(s) integrating the updated code will be relied upon to do the updates effectively.

**Workflow for updates**:

1. Update the specification in swaggerhub and save the results
2. Generate new JAX-RS server stub code into a separate directory
3. Diff the elements of the new stub code as they correspond to their counterparts in the repository
4. Manually implement the diffs where needed
5. Add new code to enable the new features of the updated specification
