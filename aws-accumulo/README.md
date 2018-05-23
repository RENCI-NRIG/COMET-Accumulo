# accumulo
# Apache Accumulo in AWS
This work is inspired by: 
1. [Exogeni Recipies](https://github.com/RENCI-NRIG/exogeni-recipes/blob/master/accumulo/accumulo_exogeni_postboot.sh)
2. [Accumulo in Docker](https://github.com/RENCI-NRIG/accumulo)

## What Is Apache Accumulo?
Apache Accumulo is a key/value store based on the design of Google's BigTable. Accumulo stores its data in Apache Hadoop's HDFS and uses Apache Zookeeper for consensus. While many users interact directly with Accumulo, several open source projects use Accumulo as their underlying store.

See official documentation for more information.

## How to use this cloudformation?
### Pre-requisites
1. User must have AWS account with privilges to create/delete IAMRole, IAMPolicy and IAMProfile
2. Key pair has been created
### Create a Accumulo Stack
Create a stack on AWS Cloudformation service by using accumuloCloudFormation.json. 
#### Logon to AWS Console and Search for Cloudformation service
![Cloudformation](../master/images/aws1.png)
#### Click Create 
![Cloudformation](../master/images/aws2.png)
#### Choose accumuloCloudFormation.json and click Next
![Cloudformation](../master/images/aws3.png)
#### Specify the Stack name and KeyPair and click Next
![Cloudformation](../master/images/aws4.png)
#### Click Next
![Cloudformation](../master/images/aws5.png)
#### Ensure the checkbox for IAMRole warning is checked and click Create
![Cloudformation](../master/images/aws6.png)
#### Stack creation will begin and status will be displayed as below
![Cloudformation](../master/images/aws7.png)

### namenode instance: NameNode Web UI on port 50070

NameNode: http://[PublicIPv4 of Instance]:50070/dfshealth.html#tab-datanode

![NameNode](../master/images/namenode.png)

### resourcemanager instance: ResourceManager Web UI on port 8088

ResourceManager: http://[PublicIPv4 of Instance]:8088

![ResourceManager](../master/images/resourcemanager.png)

### accumulomaster instance: Accumulomaster Web UI on port 9995

Accumulomaster: http://[PublicIPv4 of Instance]:9995

![Accumulomaster](../master/images/accumulomaster.png)

### worker instance: Worker Web UI on port 9995

Worker: http://[PublicIPv4 of Instance]:9995

![Worker](../master/images/worker1.png)

## Test Cluster
NOTE: Assumes the cluster is running as configured.

A script named [usertable-example.sh](../master/test/usertable-example.sh) will create a sample usertable in Accumulo using 100 randomly generated user entries. 

This script should be executed on accumulomaster console as root user.



