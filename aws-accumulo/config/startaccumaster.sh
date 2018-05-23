#!/usr/bin/env bash
set -e

source /etc/profile.d/accumulo.sh

# initialize the Accumulo cluster
runuser -l hadoop -c $'${ACCUMULO_HOME}/bin/accumulo init --clear-instance-name <<EOF
${ACCUMULO_INSTANCE}
${ACCUMULO_PASSWORD}
${ACCUMULO_PASSWORD}
EOF'
# start the AccumuloMaster node
runuser -l hadoop -c $'${ACCUMULO_HOME}/bin/start-here.sh'

# start the Accumulo Woker (tserver) nodes
#if $IS_ACCUMULO_WORKER; then
#  until runuser -l hadoop -c $'${HADOOP_PREFIX}/bin/hdfs dfs -ls /accumulo/instance_id > /dev/null 2>&1'
#  do
#    sleep 1
#  done
#  runuser -l hadoop -c $'${ACCUMULO_HOME}/bin/start-here.sh'
#fi
