This is the docker image of [COMET server](https://github.com/RENCI-NRIG/COMET-Accumulo/tree/dockerize/).

## Pull image from dockerhub

```
docker pull rencinrig/comet-spring:latest
```

## Or Build docker image locally
Dockerfile is in the same directory as pom.xml: 
```
comet-accumulo/
    Dockerfile
    pom.xml
```
Follwing command will build the docker image:
```
cd comet-accumulo/
mvn clean package dockerfile:build
```

## RUN in Docker Compose
Run together with zookeeper and RENCI accumulo docker images to form the cluster
- [Zookeeper](https://hub.docker.com/_/zookeeper)
- [Accumulo](https://github.com/RENCI-NRIG/accumulo)

The [docker-compose.yml](https://github.com/RENCI-NRIG/COMET-Accumulo/tree/dockerize/docker-compose/docker-compose.yml) and the site-files to start the Accumulo cluster instances are under folder [docker-compose](docker-compose).

Start Comet after Zookeepers and Accumulo are ready:
```
cd docker-compose/
docker-compose run start-zoo
docker-compose run start-accumulo
docker-compose up comet
```
Stop and Romove the containers
```
docker-compose stop
codker-compose rm -f
```

## To run the COMET standalone
```
docker run -it --rm \
  --name comet \
  -p 8111:8111 \
  -v $(pwd)/log:/var/log \
  --env-file <env file> \
  rencinrig/comet-spring
```

or  in detached mode:
	
```
docker run -it -d \
  --name comet \
  -p 8111:8111 \
  -v $(pwd)/log:/var/log \
  --env-file <env file> \
  rencinrig/comet-spring
```

## Environment Variables

Some properties of COMET can be configured through following environment variables. 

### `COMET_CHECK_TOKEN_STRENGTH`

Turn on/off token strength checking. Default is `false`. 

### `COMET_CHECK_CLIENT_CERT`

Turn on/off client certificate checking. Default is `true`. 

### `ACCUMULO_INSTANCE`

Accumulo Server name. Default is `aws-development`. 
Change this to your Accumulo's instance name such as `docker-developement`.

### `ACCUMULO_ZOOSERVERS`

Zookeeper server names. Default is `zoo1,zoo2,zoo3`.

### `ACCUMULO_USER`

Accumulo user name. Default is `root`.

### `ACCUMULO_PASSWORD`

Accumulo password. Default is `secret`.

### `ACCUMULO_TABLENAME`

Accumulo table name. Default is `trace`.


You can use a file of environment variables: 

Example of `comet_example.env`:

```
COMET_CHECK_TOKEN_STRENGTH=false
COMET_CHECK_CLIENT_CERT=false
ACCUMULO_INSTANCE=docker-development
ACCUMULO_ZOOSERVERS=zoo1,zoo2,zoo3
ACCUMULO_USER=root
ACCUMULO_PASSWORD=secret
ACCUMULO_TABLENAME=trace
```


## Log file

Log file is stored in /var/log/comet.log on the container
To see the log, mount your desired log folder to /var/log on the container. 



