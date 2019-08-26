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

### Start Zookeeper ###
# If restarting zookeeper, stop it first:
# ${ZOOKEEPER_HOME}/bin/zkServer.sh stop
echo "starting ZooKeeper"
${ZOOKEEPER_HOME}/bin/zkServer.sh start

### Start Accumulo Master ###
# If restarting Accumulo, stop it first:
# run bin/stop-all.sh and the master will orchestrate the shutdown of all the tablet servers.
# Alternatively, you can ssh to each of the hosts you want to remove and run $ACCUMULO_HOME/bin/stop-here.sh
runuser -l hadoop -c $'${ACCUMULO_HOME}/bin/start-here.sh'
