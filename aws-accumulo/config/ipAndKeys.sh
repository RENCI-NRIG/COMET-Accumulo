#!/usr/bin/env bash
set -e

if [ $# -ne 4 ]; then
    echo "Required arguments [HOSTANME] [BUCKETNAME] [CLUSTER_NODES] [IS_NAME_NODE] not provided"
    exit 1
fi

HOSTNAME=$1
BUCKETNAME=$2
CLUSTERNODES=$3
IS_NAME_NODE=$4

source /etc/profile.d/hadoop.sh

/opt/aws/bin/ec2-metadata -o | cut -d' ' -f2 > /root/$HOSTNAME.ip

/usr/bin/aws s3 cp /root/$HOSTNAME.ip s3://$BUCKETNAME/

if $IS_NAME_NODE; then
    runuser -l hadoop -c 'mkdir -p $HADOOP_USER_HOME/.ssh'

    runuser -l hadoop -c "ssh-keygen -t rsa -N '' -f $HADOOP_USER_HOME/.ssh/id_rsa"

    runuser -l hadoop -c 'cat $HADOOP_USER_HOME/.ssh/id_rsa.pub >> $HADOOP_USER_HOME/.ssh/authorized_keys'

    /usr/bin/aws s3 cp /home/hadoop/.ssh/id_rsa.pub s3://$BUCKETNAME
    /usr/bin/aws s3 cp /home/hadoop/.ssh/id_rsa s3://$BUCKETNAME

    chmod 0600 $HADOOP_USER_HOME/.ssh/authorized_keys

    chown -R hadoop:hadoop $HADOOP_USER_HOME/.ssh
else
    echo "Waiting to get public key"
    until /usr/bin/aws s3 cp s3://$BUCKETNAME/id_rsa.pub /home/hadoop/.ssh/id_rsa.pub;
    do
       sleep 2
    done
    echo "Waiting to get private key"
    until /usr/bin/aws s3 cp s3://$BUCKETNAME/id_rsa /home/hadoop/.ssh/id_rsa;
    do
       sleep 2
    done
    chown -R hadoop:hadoop $HADOOP_USER_HOME/.ssh
    runuser -l hadoop -c 'cat /home/hadoop/.ssh/id_rsa.pub >> /home/hadoop/.ssh/authorized_keys'
    chmod 0600 $HADOOP_USER_HOME/.ssh/authorized_keys
    chmod 0600 $HADOOP_USER_HOME/.ssh/id_rsa
fi
echo "Waiting to receive all the IP addresses"
for node in $CLUSTERNODES ; do
    echo "node = $node"
    until /usr/bin/aws s3 ls s3://$BUCKETNAME/$node.ip; do sleep 2; done
    /usr/bin/aws s3 cp s3://$BUCKETNAME/$node.ip /tmp
    IP=`cat /tmp/$node.ip`
    echo "$IP $node" >> /etc/hosts
    if [ $node == "namenode" ]; then
        echo "$IP zoo1" >> /etc/hosts
    fi
    if [ $node == "resourcemanager" ]; then
        echo "$IP zoo2" >> /etc/hosts
    fi
    if [ $node == "accumulomaster" ]; then
        echo "$IP zoo3" >> /etc/hosts
    fi
done
echo "All Ip addresses have been configured"

