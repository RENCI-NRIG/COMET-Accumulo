#!/bin/sh
if [ $# -ne 1 ]; then
    echo "Required arguments [maven version] not provided"
    exit 1
fi
MAVEN_VERSION=$1

HOME=/root
LANG=en_US.UTF-8
LC_ALL=en_US.UTF-8

wget http://mirror.stjschools.org/public/apache/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz
tar -zxvf apache-maven-$MAVEN_VERSION-bin.tar.gz -C /opt

#Setup maven profile
cat > /etc/profile.d/maven.sh <<EOL
  export PATH=/opt/apache-maven-$MAVEN_VERSION/bin:$PATH
EOL
