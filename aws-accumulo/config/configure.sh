#!/usr/bin/env bash
set -e

if [ $# -ne 11 ]; then
    echo "Required arguments [accumulo master] [IS_ACCUMULO_MASTER] [IS_ACCUMULO_WORKER] [IS_NODE_MANAGER] [IS_NAME_NODE] [IS_SECONDARY_NAME_NODE] [IS_DATA_NODE] [IS_RESOURCE_MANAGER] [ACCUMULOWORKERS] [CLUSTER_NODES] [s3bucket] not provided"
    exit 1
fi

ACCUMULOMASTER=$1
IS_ACCUMULO_MASTER=$2
IS_ACCUMULO_WORKER=$3
IS_NODE_MANAGER=$4
IS_NAME_NODE=$5
IS_SECONDARY_NAME_NODE=$6
IS_DATA_NODE=$7
IS_RESOURCE_MANAGER=$8
ACCUMULOWORKERS=${9}
CLUSTERNODES=${10}
S3BUCKET=${11}

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

# generate or use provided core-site.xml
_core_site_xml () {
  echo "Setting up $CORE_SITE_FILE"
  if [ -f /site-files/core-site.xml ]; then
    echo "USE: /site-files/core-site.xml"
    cat /site-files/core-site.xml > $CORE_SITE_FILE
  else
    cat > $CORE_SITE_FILE << EOF
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<!-- Put site-specific property overrides in this file. -->
<configuration>
  <property>
    <name>fs.default.name</name>
    <value>hdfs://namenode:9000</value>
  </property>
</configuration>
EOF
  fi
  chown hadoop:hadoop $CORE_SITE_FILE
}

# generate or use provided hdfs-site.xml
_hdfs_site_xml () {
  if [ -f /site-files/hdfs-site.xml ]; then
    cat /site-files/hdfs-site.xml > $HDFS_SITE_FILE
  else
    cat > $HDFS_SITE_FILE << EOF
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<!-- Put site-specific property overrides in this file. -->
<configuration>
  <property>
    <name>dfs.replication</name>
    <value>2</value>
  </property>
  <property>
    <name>dfs.name.dir</name>
    <value>file:///hdfsdata/namenode</value>
  </property>
  <property>
    <name>dfs.data.dir</name>
    <value>file:///hdfsdata/datanode</value>
  </property>
</configuration>
EOF
  fi
  chown hadoop:hadoop $HDFS_SITE_FILE
}

# generate or use provided mapred-site.xml
_mapred_site_xml() {
  if [ -f /site-files/mapred-site.xml ]; then
    cat /site-files/mapred-site.xml > $MAPRED_SITE_FILE
  else
    cat > $MAPRED_SITE_FILE << EOF
<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<!-- Put site-specific property overrides in this file. -->
<configuration>
  <property>
    <name>mapreduce.framework.name</name>
    <value>yarn</value>
  </property>
</configuration>
EOF
  fi
  chown hadoop:hadoop $MAPRED_SITE_FILE
}

# generate or use provided yarn-site.xml
_yarn_site_xml() {
  if [ -f /site-files/yarn-site.xml ]; then
    cat /site-files/yarn-site.xml > $YARN_SITE_FILE
  else
    cat > $YARN_SITE_FILE << EOF
<?xml version="1.0"?>
<!-- Site specific YARN configuration properties -->
<configuration>
  <property>
    <name>yarn.resourcemanager.hostname</name>
    <value>resourcemanager</value>
  </property>
  <property>
    <name>yarn.resourcemanager.bind-host</name>
    <value>0.0.0.0</value>
  </property>
  <property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
  </property>
</configuration>
EOF
  fi
  chown hadoop:hadoop $YARN_SITE_FILE
}

# generate or use provided workers (slaves)
_workers() {
  for node in $ACCUMULOWORKERS; do
  echo $node >> $WORKERS_FILE
  done
  chown hadoop:hadoop $WORKERS_FILE
}

# set /etc/profile.d/hadoop.sh
_hadoop_profile() {
  cat > /etc/profile.d/hadoop.sh << EOF
export HADOOP_USER_HOME=${HADOOP_USER_HOME}
export HADOOP_HOME=${HADOOP_USER_HOME}/hadoop
export HADOOP_PREFIX=${HADOOP_USER_HOME}/hadoop
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
export PATH=$PATH:${HADOOP_PREFIX}/sbin:${HADOOP_PREFIX}/bin:/opt/zookeeper/bin
EOF
}

# set /etc/profile.d/accumulo.sh
_accumulo_profile() {
  cat > /etc/profile.d/accumulo.sh << EOF
export ACCUMULO_VERSION=${ACCUMULO_VERSION}
export ACCUMULO_PASSWORD=${ACCUMULO_PASSWORD}
export ACCUMULO_INSTANCE=${ACCUMULO_INSTANCE}
export ACCUMULO_HOME=${ACCUMULO_HOME}
export ACCUMULO_CONF_DIR=${ACCUMULO_HOME}/conf
export ACCUMULO_MASTER=${ACCUMULOMASTER}
export ACCUMULO_WORKERS=${ACCUMULOWORKERS}
export IS_ACCUMULO_MASTER=${IS_ACCUMULO_MASTER}
export IS_ACCUMULO_WORKER=${IS_ACCUMULO_WORKER}
export IS_NODE_MANAGER=${IS_NODE_MANAGER}
export IS_NAME_NODE=${IS_NAME_NODE}
export IS_SECONDARY_NAME_NODE=${IS_SECONDARY_NAME_NODE}
export IS_DATA_NODE=${IS_DATA_NODE}
export IS_RESOURCE_MANAGER=${IS_RESOURCE_MANAGER}
export CLUSTER_NODES=${CLUSTERNODES}
EOF
}

_accumulo_site_xml() {
  if [ -f /site-files/accumulo-site.xml ]; then
    cat /site-files/accumulo-site.xml > $ACCUMULO_SITE_FILE
  else
    # setup zookeeper hosts
    sed -i "/localhost:2181/ s/localhost:2181/zoo1:2181,zoo2:2181,zoo3:2181/" ${ACCUMULO_HOME}/conf/accumulo-site.xml
    # disable SASL (?) Kerberos ??
    # this is disabled correctly by bootstrap_config.sh
    #sed -i '/instance.rpc.sasl.enabled/!b;n;s/true/false/' ${ACCUMULO_HOME}/conf/accumulo-site.xml
    # if the password is changed by the user, the script needs to change it here too.
    sed -i "/<value>secret/s/secret/${ACCUMULO_PASSWORD}/" ${ACCUMULO_HOME}/conf/accumulo-site.xml

    sed -i '/<name>instance.volumes<\/name>/!b;n;c\ \ \ \ <value>hdfs:\/\/namenode:9000\/accumulo<\/value>' ${ACCUMULO_HOME}/conf/accumulo-site.xml
  fi
  chown hadoop:hadoop $ACCUMULO_SITE_FILE
}

# set profile and check env for hadoop user
_hadoop_profile
_accumulo_profile

if $IS_NAME_NODE; then
  mkdir -p /hdfsdata/namenode
  chown -R hadoop:hadoop /hdfsdata/namenode
fi
if $IS_DATA_NODE; then
  mkdir -p /hdfsdata/datanode
  chown -R hadoop:hadoop /hdfsdata/datanode
fi

# update JAVA_HOME in hadoop-env
runuser -l hadoop -c $'sed -i \'s:export JAVA_HOME=.*:export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.171-7.b10.37.amzn1.x86_64/jre:\' /home/hadoop/hadoop/etc/hadoop/hadoop-env.sh'

# set haddop configuration files
_core_site_xml
_hdfs_site_xml
_mapred_site_xml
_yarn_site_xml
_workers

# Register all other cluster nodes in known_hosts
for node in $CLUSTERNODES ; do
    echo "node = $node"
    echo "ssh-keyscan $node >> /home/hadoop/.ssh/known_hosts"
    until runuser -l hadoop -c "ssh-keyscan $node >> /home/hadoop/.ssh/known_hosts"; do sleep 2; done
done

# Fix permissions in .ssh
chown -R hadoop:hadoop /home/hadoop/.ssh
chmod -R g-w /home/hadoop/.ssh
chmod -R o-w /home/hadoop/.ssh

# NameNode startup commands
if $IS_NAME_NODE; then
    echo "Staring NameNode"
    runuser -l hadoop -c $'$HADOOP_PREFIX/bin/hdfs namenode -format'
    runuser -l hadoop -c $'$HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script hdfs start namenode'
fi

# SecondaryNameNode startup commands
if $IS_SECONDARY_NAME_NODE; then
    echo "Staring SecondaryNameNode"
    runuser -l hadoop -c $'$HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script hdfs start secondarynamenode'
fi

# DataNode startup commands
if $IS_DATA_NODE; then
    echo "Staring DataNode"
    runuser -l hadoop -c $'$HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script hdfs start datanode'
fi

# ResourceManager startup commands
if $IS_RESOURCE_MANAGER; then
    echo "Staring ResourceManager"
    runuser -l hadoop -c $'$YARN_HOME/sbin/yarn-daemon.sh --config $HADOOP_CONF_DIR start resourcemanager'
fi

# NodeManager startup commands
if $IS_NODE_MANAGER; then
    echo "Staring NodeManager"
    runuser -l hadoop -c $'$YARN_HOME/sbin/yarn-daemon.sh --config $HADOOP_CONF_DIR start nodemanager'
fi

#zookepeer configuration
if $IS_NAME_NODE; then
    echo 1 > "$ZOO_DATA_DIR/myid"
    echo "starting ZooKeeper"
    ${ZOOKEEPER_HOME}/bin/zkServer.sh start
fi

if $IS_RESOURCE_MANAGER; then
    echo 2 > "$ZOO_DATA_DIR/myid"
    echo "starting ZooKeeper"
    ${ZOOKEEPER_HOME}/bin/zkServer.sh start
fi

if $IS_ACCUMULO_MASTER; then
    echo 3 > "$ZOO_DATA_DIR/myid"
    echo "starting ZooKeeper"
    ${ZOOKEEPER_HOME}/bin/zkServer.sh start
fi

# Accumulo configuration
# bootstrap node
runuser -l hadoop -c $'cd ${ACCUMULO_HOME}; ${ACCUMULO_HOME}/bin/bootstrap_config.sh --size 1GB --jvm --version 2;'

# define Accumulo node roles
sed -i "/localhost/ s/.*/$ACCUMULOMASTER/" ${ACCUMULO_HOME}/conf/masters
sed -i "/localhost/ s/.*/$ACCUMULOMASTER/" ${ACCUMULO_HOME}/conf/monitor
sed -i "/localhost/ s/.*/$ACCUMULOMASTER/" ${ACCUMULO_HOME}/conf/gc
sed -i "/localhost/ s/.*/$ACCUMULOMASTER/" ${ACCUMULO_HOME}/conf/tracers

# set Accumulo workers
> ${ACCUMULO_HOME}/conf/slaves
for node in $ACCUMULOWORKERS; do
  echo $node >> ${ACCUMULO_HOME}/conf/slaves
done

_accumulo_site_xml

# Need monitor to bind to public port
sed -i "/ACCUMULO_MONITOR_BIND_ALL/ s/^# //" ${ACCUMULO_HOME}/conf/accumulo-env.sh
sed -i '/HOSTS=/ cTEMP=`grep $IP /etc/hosts | grep -v zoo|cut -d\" \" -f2`;HOSTS=\"$(hostname -a 2> /dev/null) $(hostname) localhost 127.0.0.1 $IP $TEMP\"' ${ACCUMULO_HOME}/bin/start-here.sh

if $IS_NAME_NODE; then
    runuser -l hadoop -c $'touch setupcomplete'
fi

# Commenting the below code; will start master and workers manually
if $IS_ACCUMULO_MASTER; then
  until sudo -u hadoop ssh hadoop@namenode ls /home/hadoop/setupcomplete; do sleep 2; done
  # initialize the Accumulo cluster
  runuser -l hadoop -c $'${ACCUMULO_HOME}/bin/accumulo init --instance-name ${ACCUMULO_INSTANCE} <<EOF
${ACCUMULO_PASSWORD}
${ACCUMULO_PASSWORD}
EOF'
  # start the AccumuloMaster node
  runuser -l hadoop -c $'${ACCUMULO_HOME}/bin/start-here.sh'
fi

# start the Accumulo Woker (tserver) nodes
if $IS_ACCUMULO_WORKER; then
  until runuser -l hadoop -c $'${HADOOP_PREFIX}/bin/hdfs dfs -ls /accumulo/instance_id > /dev/null 2>&1'
  do
    sleep 1
  done
  runuser -l hadoop -c $'${ACCUMULO_HOME}/bin/start-here.sh'
  /usr/bin/aws s3 rm s3://$S3BUCKET --recursive
fi
echo "Finished executing configure"
