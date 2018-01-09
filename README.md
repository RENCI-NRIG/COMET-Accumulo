# The COMET phase 2 implementation with Accumulo integration

COMET is a distributed RESTful meta-data service which stores key-value oriented configuration information about resources and applications running in the distributed cloud. Clients of COMET are elements of IaaS cloud provider system, user client tools, applications running in tenant virtual systems/slices. COMET provides strong authorization controls ensuring that information is only shared with appropriate clients.

The COMET phase 2 is based on new design of mapping cloud service entries to Accumulo table entries.

The COMET API specifications can be found on [SwaggerHub](https://app.swaggerhub.com/apis/cwang/Comet-Accumulo-Query-Layer/1.0.0#/), which is also available as YAML in /specifications/swagger.yaml

To run the COMET server, please execute the following:

```
mvn clean package jetty:run
```