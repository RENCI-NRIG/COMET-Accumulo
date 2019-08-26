#!/usr/bin/env bash

source /etc/profile.d/zookeeper.sh
source /etc/profile.d/hadoop.sh
source /etc/profile.d/accumulo.sh

# location of hadoop configuration files
HADOOP_CONF_DIR=${HADOOP_PREFIX}/etc/hadoop
ACCUMULO_CONF_DIR=${ACCUMULO_HOME}/conf
CORE_SITE_FILE=${HADOOP_CONF_DIR}/core-site.xml
HDFS_SITE_FILE=${HADOOP_CONF_DIR}/hdfs-site.xml
MAPRED_SITE_FILE=${HADOOP_CONF_DIR}/mapred-site.xml
YARN_SITE_FILE=${HADOOP_CONF_DIR}/yarn-site.xml
WORKERS_FILE=${HADOOP_CONF_DIR}/slaves
ACCUMULO_SITE_FILE=${ACCUMULO_CONF_DIR}/accumulo-site.xml

### NameNode startup commands ###
# If restarting NameNode, stop it first:
# runuser -l hadoop -c $'$HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script hdfs stop namenode'
echo "Staring NameNode"
runuser -l hadoop -c $'$HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script hdfs start namenode'

### Start Zookeeper ###
# If restarting zookeeper, stop it first:
# ${ZOOKEEPER_HOME}/bin/zkServer.sh stop
echo "starting ZooKeeper"
${ZOOKEEPER_HOME}/bin/zkServer.sh start

