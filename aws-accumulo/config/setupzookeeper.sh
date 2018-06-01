#!/bin/sh
ZOOKEEPER_VERSION=$1
HOME=/root
LANG=en_US.UTF-8
LC_ALL=en_US.UTF-8

if [ $# -ne 1 ]; then
    echo "Required arguments [version] not provided"
    exit 1
fi

JAVA_VERSION=`java -version 2>&1 | awk -F '\"' '/version/ {print $2}'| cut -d'_' -f1`
if ["$JAVA_VERSION" !="1.8.0" ]; then
    echo "Removing $JAVA_VERSION"
    java_rpm_to_be_removed=`rpm -qa | grep openjdk-$JAVA_VERSION`
    rpm -e \"$java_rpm_to_be_removed
fi
# zookeeper install
WORKDIR=/opt/
ZOO_USER=zookeeper
ZOO_CONF_DIR=/conf
ZOO_DATA_DIR=/data
ZOO_DATA_LOG_DIR=/datalog
ZOO_PORT=2181
ZOO_TICK_TIME=2000
ZOO_INIT_LIMIT=5
ZOO_SYNC_LIMIT=2
ZOO_MAX_CLIENT_CNXNS=60

# Install zookeeper
echo "Adding user $ZOO_USER"
adduser $ZOO_USER
echo "Setting up zookeeper directories"
mkdir -p $ZOO_DATA_LOG_DIR $ZOO_DATA_DIR $ZOO_CONF_DIR
chown "$ZOO_USER:$ZOO_USER" $ZOO_DATA_LOG_DIR $ZOO_DATA_DIR $ZOO_CONF_DIR
DISTRO_NAME=zookeeper-${ZOOKEEPER_VERSION}
echo "Download zookeeper tarball ${DISTRO_NAME}.tar.gz "
curl -o $WORKDIR/${DISTRO_NAME}.tar.gz "https://archive.apache.org/dist/zookeeper/$DISTRO_NAME/$DISTRO_NAME.tar.gz"
echo "Extract zookeeper tarball"
tar -xzf $WORKDIR/${DISTRO_NAME}.tar.gz -C $WORKDIR
echo "Clean up"
mv $WORKDIR/$DISTRO_NAME $WORKDIR/zookeeper
rm -rf $WORKDIR/$DISTRO_NAME.tar.gz

#Setup zookeper profile
cat > /etc/profile.d/zookeeper.sh <<EOL
export ZOOKEEPER_HOME=$WORKDIR/zookeeper
export ZOOKEEPER_NODES="zoo1 zoo2 zoo3"
export ZOO_USER=zookeeper
export ZOO_CONF_DIR=/conf
export ZOO_DATA_DIR=/data
export ZOO_DATA_LOG_DIR=/datalog
export ZOO_PORT=2181
export ZOO_TICK_TIME=2000
export ZOO_INIT_LIMIT=5
export ZOO_SYNC_LIMIT=2
export ZOO_MAX_CLIENT_CNXNS=60
export ZOOCFGDIR=/conf
EOL

# Setup zoo.cfg
cat > $ZOO_CONF_DIR/zoo.cfg <<'EOL'
clientPort=2181
dataDir=/data
dataLogDir=/datalog
tickTime=2000
initLimit=5
syncLimit=2
maxClientCnxns=60
server.1=zoo1:2888:3888
server.2=zoo2:2888:3888
server.3=zoo3:2888:3888
EOL


