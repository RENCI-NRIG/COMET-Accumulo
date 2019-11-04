#!/bin/bash
read -sp 'password of accumulo root: ' rootpass; echo
read -p 'username of the new user: ' username

runuser -l hadoop -c "${ACCUMULO_HOME}/bin/accumulo shell -u root -p ${rootpass} -e \"createuser $username\""

read -p 'Enter the table name for the user to READ/WRITE: ' table
runuser -l hadoop -c "${ACCUMULO_HOME}/bin/accumulo shell -u root -p ${rootpass} -e \"grant Table.READ -t ${table} -u ${username}\""
runuser -l hadoop -c "${ACCUMULO_HOME}/bin/accumulo shell -u root -p ${rootpass} -e \"grant Table.WRITE -t ${table} -u ${username}\""
runuser -l hadoop -c "${ACCUMULO_HOME}/bin/accumulo shell -u root -p ${rootpass} -e \"grant -s System.ALTER_USER -u ${username}\""
