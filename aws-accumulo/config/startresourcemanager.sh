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

### ResourceManager startup commands ###
# If restarting resourcemanager, stop it first:
# runuser -l hadoop -c $'$YARN_HOME/sbin/yarn-daemon.sh --config $HADOOP_CONF_DIR stop resourcemanager'
echo "Staring ResourceManager"
runuser -l hadoop -c $'$YARN_HOME/sbin/yarn-daemon.sh --config $HADOOP_CONF_DIR start resourcemanager'

### Start Zookeeper ###
# If restarting zookeeper, stop it first:
# ${ZOOKEEPER_HOME}/bin/zkServer.sh stop
echo "starting ZooKeeper"
${ZOOKEEPER_HOME}/bin/zkServer.sh start
