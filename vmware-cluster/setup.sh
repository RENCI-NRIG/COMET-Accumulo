#!/usr/bin/env bash
set -e

if [ $# -ne 1 ]; then
    echo "Required arguments [Node]"
    exit 1
fi

Node=$1


if [ "$Node" = "namenode" ]; then
    #namenode
    ./configure.sh comet-master comet-nn false false false true false false false 'comet-w1 comet-w2' 'comet-nn comet-mgr comet-w1 comet-w2 comet-master' > /var/log/configure.log 2>&1
fi
if [ "$Node" = "resourcemgr" ]; then

    #resourcemgr
    ./configure.sh comet-master comet-nn false false false false false false true 'comet-w1 comet-w2' 'comet-nn comet-mgr comet-w1 comet-w2 comet-master' > /var/log/configure.log 2>&1
fi

if [ "$Node" = "worker" ]; then
    #worker1 and worker2
    ./configure.sh comet-master comet-nn false true false false false true false 'comet-w1 comet-w2' 'comet-nn comet-mgr comet-w1 comet-w2 comet-master' > /var/log/configure.log 2>&1
fi

if [ "$Node" = "master" ]; then
    #master
    ./configure.sh comet-master comet-nn true false false false false false false 'comet-w1 comet-w2' 'comet-nn comet-mgr comet-w1 comet-w2 comet-master' > /var/log/configure.log 2>&1
fi
