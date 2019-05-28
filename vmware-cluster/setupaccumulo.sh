#!/bin/sh
if [ $# -ne 1 ]; then
    echo "Required arguments [accumulo version] not provided"
    exit 1
fi
ACCUMULO_VERSION=$1
ACCUMULO_HOME=/opt/accumulo-$ACCUMULO_VERSION
ACCUMULO_PASSWORD=secret
ACCUMULO_INSTANCE=aws-development
ACCUMULO_MASTER=localhost
ACCUMULO_WORKERS=''

HOME=/root
LANG=en_US.UTF-8
LC_ALL=en_US.UTF-8

echo "Setup accumulo directory"
mkdir -p $ACCUMULO_HOME
WORKDIR=$HADOOP_INSTALL_DIR
HADOOP_INSTALL_DIR=/home/hadoop
echo "Download accumulo-${ACCUMULO_VERSION}"
curl -o /opt/accumulo-${ACCUMULO_VERSION}.tgz  "https://dist.apache.org/repos/dist/release/accumulo/${ACCUMULO_VERSION}/accumulo-${ACCUMULO_VERSION}-bin.tar.gz"
echo "Extract accumulo-${ACCUMULO_VERSION}"
tar -C /opt/accumulo-${ACCUMULO_VERSION} --extract --file /opt/accumulo-${ACCUMULO_VERSION}.tgz --strip-components=1
echo "Clean up"
rm -f /opt/accumulo-${ACCUMULO_VERSION}.tgz*
chown -R hadoop:hadoop ${ACCUMULO_HOME}

#Setup hadoop profile
cat > /etc/profile.d/accumulo.sh <<EOL
  export ACCUMULO_VERSION=${ACCUMULO_VERSION}
  export ACCUMULO_HOME=/opt/accumulo-${ACCUMULO_VERSION}
  export ACCUMULO_CONF_DIR=${ACCUMULO_HOME}/conf
  export ACCUMULO_PASSWORD=secret
  export ACCUMULO_INSTANCE=aws-development
  export ACCUMULO_MASTER=localhost
  export ACCUMULO_WORKERS=''
EOL
