#!/usr/bin/env bash
yum install -y java-1.8.0-openjdk
yum install -y java-1.8.0-openjdk-devel
yum install -y docker
yum install -y curl 
chkconfig --add docker
service docker start
curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
docker-compose up -d
