#!/usr/bin/env bash
set -e

if [ $# -ne 11 ]; then
     echo "Required arguments [s3bucket] not provided"
     exit 1
fi

source /etc/profile.d/accumulo.sh

# start the Accumulo Woker (tserver) nodes
until runuser -l hadoop -c $'${HADOOP_PREFIX}/bin/hdfs dfs -ls /accumulo/instance_id > /dev/null 2>&1';
do
    sleep 1
done
runuser -l hadoop -c $'${ACCUMULO_HOME}/bin/start-here.sh'
/usr/bin/aws s3 rm s3://$1/ --recursive
