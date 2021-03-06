#!/usr/bin/env bash
set -e

if [ $# -ne 5 ]; then
    echo "Required arguments [ZooKeeperVersion] [HadoopVersion] [AccumuloVersion] [Node] [AccumuloPassword]"
    exit 1
fi

ZooKeeperVersion=$1
HadoopVersion=$2
AccumuloVersion=$3
Node=$4
AccumuloPassword=$5

sed -i "s/ACCUMULO_PASSWORD=.*/ACCUMULO_PASSWORD=${AccumuloPassword}/" ./setupaccumulo.sh

yum install -y java-1.8.0-openjdk
./setupzookeeper.sh $ZooKeeperVersion > /var/log/setupzookeeper.log 2>&1
./setuphadoop.sh $HadoopVersion > /var/log/setuphadoop.log 2>&1
./setupaccumulo.sh $AccumuloVersion > /var/log/setupaccumulo.log 2>&1


source /etc/profile.d/hadoop.sh
if [ "$Node" = "namenode" ]; then 
    runuser -l hadoop -c 'mkdir -p $HADOOP_USER_HOME/.ssh'
    runuser -l hadoop -c "ssh-keygen -t rsa -N '' -f $HADOOP_USER_HOME/.ssh/id_rsa"
    runuser -l hadoop -c 'cat $HADOOP_USER_HOME/.ssh/id_rsa.pub >> $HADOOP_USER_HOME/.ssh/authorized_keys'
    chmod 0600 $HADOOP_USER_HOME/.ssh/authorized_keys
    chown -R hadoop:hadoop $HADOOP_USER_HOME/.ssh
else
    runuser -l hadoop -c 'mkdir -p $HADOOP_USER_HOME/.ssh'
    runuser -l hadoop -c 'touch $HADOOP_USER_HOME/.ssh/id_rsa.pub'
    runuser -l hadoop -c 'touch $HADOOP_USER_HOME/.ssh/id_rsa'
    chown -R hadoop:hadoop $HADOOP_USER_HOME/.ssh
fi

