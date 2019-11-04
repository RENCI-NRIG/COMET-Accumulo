#!/bin/sh
read -sp 'password of accumulo root: ' rootpass; echo
read -p 'new table name: ' table

runuser -l hadoop -c "${ACCUMULO_HOME}/bin/accumulo shell -u root -p ${rootpass} -e \"createtable $table\""
