#!/usr/bin/env bash
set -e

if [ $# -ne 4 ]; then
    echo "Required arguments [ZooKeeperVersion] [HadoopVersion] [AccumuloVersion] [Node]"
    exit 1
fi

ZooKeeperVersion=$1
HadoopVersion=$2
AccumuloVersion=$3
Node=$4


./setupzookeeper.sh $ZooKeeperVersion > /var/log/setupzookeeper.log 2>&1
./setuphadoop.sh $HadoopVersion > /var/log/setuphadoop.log 2>&1
./setupaccumulo.sh $AccumuloVersion /var/log/setupaccumulo.log 2>&1


if [ "$Node" = "namenode" ]; then 
    source /etc/profile.d/hadoop.sh
    runuser -l hadoop -c 'mkdir -p $HADOOP_USER_HOME/.ssh'
    runuser -l hadoop -c "ssh-keygen -t rsa -N '' -f $HADOOP_USER_HOME/.ssh/id_rsa"
    runuser -l hadoop -c 'cat $HADOOP_USER_HOME/.ssh/id_rsa.pub >> $HADOOP_USER_HOME/.ssh/authorized_keys'
    chmod 0600 $HADOOP_USER_HOME/.ssh/authorized_keys
    chown -R hadoop:hadoop $HADOOP_USER_HOME/.ssh
fi

