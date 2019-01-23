
## Build docker image locally
Put the Dockerfile in the same directory as pom.xml: 
```
comet-accumulo/
    Dockerfile
    pom.xml
```
Follwing command will build the docker image:
```
cd comet-accumulo/
mvn package dockerfile:build
```


## Run the COMET
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

### `COMET_ACCUMULO_NAME`

Accumulo server name. Default is `aws-development`. 

### `COMET_ACCUMULO_ZOOSERVERS`

Zookeeper server names. Default is `zoo1,zoo2,zoo3`.

### `COMET_ACCUMULO_USER`

Accumulo user name. Default is `root`.

### `COMET_ACCUMULO_PASSWORD`

Accumulo password. Default is `secret`.

### `COMET_ACCUMULO_TABLENAME`

Accumulo table name. Default is `trace`.


You can use a file of environment variables: 

Example of `comet_example.env`:

```
COMET_CHECK_TOKEN_STRENGTH=true
COMET_CHECK_CLIENT_CERT=true
COMET_ACCUMULO_NAME=accu1
COMET_ACCUMULO_ZOOSERVERS=zoo_a,zoo_b,zoo_c
COMET_ACCUMULO_USER=user1
COMET_ACCUMULO_PASSWORD=password
COMET_ACCUMULO_TABLENAME=tablename1
```



## Log file

Log file is stored in /var/log/comet.log on the container
To see the log, mount your desired log folder to /var/log on the container. 



