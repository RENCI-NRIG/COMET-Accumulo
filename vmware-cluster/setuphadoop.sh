#!/bin/sh
if [ $# -ne 1 ]; then
    echo "Required arguments [hadoop version] not provided"
    exit 1
fi
HADOOP_VERSION=$1

HOME=/root
LANG=en_US.UTF-8
LC_ALL=en_US.UTF-8

JAVA_VERSION=`java -version 2>&1 | awk -F '\"' '/version/ {print $2}'| cut -d'_' -f1`
if [ "$JAVA_VERSION" != "1.8.0" ]; then
    echo "Removing $JAVA_VERSION"
    java_rpm_to_be_removed=`rpm -qa | grep openjdk-$JAVA_VERSION`
    rpm -e \"$java_rpm_to_be_removed
fi

# hadoop install
HADOOP_INSTALL_DIR=/home/hadoop

HADOOP_USER=hadoop

echo "Adding user ${HADOOP_USER}"
adduser -m -d ${HADOOP_INSTALL_DIR} ${HADOOP_USER}

echo "Download hadoop-${HADOOP_VERSION}"
sudo -u ${HADOOP_USER} curl -o ${HADOOP_INSTALL_DIR}/hadoop-${HADOOP_VERSION}.tar.gz "https://archive.apache.org/dist/hadoop/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz"

echo "Extract hadoop-${HADOOP_VERSION}"
sudo -u ${HADOOP_USER} tar -zxf ${HADOOP_INSTALL_DIR}/hadoop-${HADOOP_VERSION}.tar.gz -C ${HADOOP_INSTALL_DIR}/
sudo -u ${HADOOP_USER} mv ${HADOOP_INSTALL_DIR}/hadoop-${HADOOP_VERSION} ${HADOOP_INSTALL_DIR}/hadoop

echo "Clean up hadoop-${HADOOP_VERSION}"
sudo -u ${HADOOP_USER} rm -f ${HADOOP_INSTALL_DIR}/hadoop-${HADOOP_VERSION}.tar.gz

HADOOP_USER_HOME=/home/hadoop
HADOOP_PREFIX=${HADOOP_USER_HOME}/hadoop
HADOOP_CONF_DIR=${HADOOP_PREFIX}/etc/hadoop
#Setup hadoop profile
cat > /etc/profile.d/hadoop.sh <<EOL
  export HADOOP_USER_HOME=${HADOOP_USER_HOME}
  export HADOOP_HOME=${HADOOP_USER_HOME}/hadoop
  export HADOOP_PREFIX=${HADOOP_PREFIX}
  export HADOOP_INSTALL=${HADOOP_PREFIX}
  export HADOOP_MAPRED_HOME=${HADOOP_PREFIX}
  export HADOOP_COMMON_HOME=${HADOOP_PREFIX}
  export HADOOP_HDFS_HOME=${HADOOP_PREFIX}
  export JAVA_LIBRARY_PATH=${JAVA_LIBRARY_PATH}:${HADOOP_PREFIX}/lib/native
  export YARN_HOME=${HADOOP_PREFIX}
  export HADOOP_COMMON_LIB_NATIVE_DIR=${HADOOP_PREFIX}/lib/native
  export HADOOP_CONF_DIR=${HADOOP_PREFIX}/etc/hadoop
  export CORE_SITE_FILE=${HADOOP_CONF_DIR}/core-site.xml
  export HDFS_SITE_FILE=${HADOOP_CONF_DIR}/hdfs-site.xml
  export MAPRED_SITE_FILE=${HADOOP_CONF_DIR}/mapred-site.xml
  export YARN_SITE_FILE=${HADOOP_CONF_DIR}/yarn-site.xml
  export WORKERS_FILE=${HADOOP_CONF_DIR}/slaves
EOL
