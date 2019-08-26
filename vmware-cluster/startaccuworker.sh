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

### DataNode startup commands ###
# If restarting DataNode, stop it first:
# runuser -l hadoop -c $'$HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script hdfs stop datanode'
echo "Staring DataNode"
runuser -l hadoop -c $'$HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script hdfs start datanode'

### Start Accumulo Worker ###
# If restarting Accumulo, stop it firs by $ACCUMULO_HOME/bin/stop-here.sh
runuser -l hadoop -c $'${ACCUMULO_HOME}/bin/start-here.sh'
