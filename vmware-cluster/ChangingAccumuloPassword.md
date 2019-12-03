## Changing Accumulo Password after setup

**Note: There is limited documents for changing root password** <br>
Here are some links I reference: 
> [ACCUMULO-3626](https://issues.apache.org/jira/browse/ACCUMULO-3626) <br>
> [Accumulo User Manual](https://accumulo.apache.org/1.9/accumulo_user_manual.html#_administrative_user) - Not exactly our case, but similar commands flow 

If I miss something, please let me know. Thanks! <br>


### 0. Prerequisite
#### a) Resetting root password will delete existing users; so make sure we know which users we will need to re-create later.

Following commands let you know the users. <br>
There are 2 users at the moment for two comet head nodes.

```
[root@comet-master]# runuser -l hadoop -c "${ACCUMULO_HOME}/bin/accumulo shell -u root -p secret -e users"
comet-hn1
comet-hn2
root
```


#### b) Change "trace.token.property.password" value in accumulo_site.xml, including comet-master, comet-w1, comet-w2

This is because the trace.user is set to root. <br>
in /opt/accumulo-1.9.3/conf/accumulo-site.xml

```
<property>
    <name>trace.token.property.password</name>
    <!-- change this to the root user's password, and/or change the user below -->
    <value>secret</value>  # HERE
</property>
```


#### c) Modify old password 'secret' to new password in /etc/profile.d/accumulo.sh in every node. (comet-master, comet-w1, comet-w2, comet-nn, comet-mgr).

It should be a good idea to sync these values, <br> 
In case we will use setup\*.sh/start\*.sh scripts in future which will reference this file 

```
export ACCUMULO_VERSION=1.9.3
export ACCUMULO_PASSWORD=secret # HERE
...
```

### 1. Stop/reset the password/restart Accumulo 

This is the real step that changes the password.

Running "accumulo init --reset-security" will prompt you for a new root password. <br>
**CAUTION**: this will delete all existing users. You will need to verify the instance name to proceed, <br>
in our case: **exogeni-accumulo**

```
[root@comet-master]# ${ACCUMULO_HOME}/bin/stop-all.sh

[root@comet-master]# ${ACCUMULO_HOME}/bin/accumulo init --reset-security

"WARNING: This will remove all users from Accumulo! If you wish to proceed enter the instance name: 
Enter initial password for root (this may not be applicable for your security setup): ********
Confirm initial password for root: ********

[root@comet-master]# ${ACCUMULO_HOME}/bin/start-all.sh
```


### 2. Re-create users in Accumulo

Run `~/COMET-Accumulo/vmware-cluster/create-accu-user.sh` script to create comet-hn1, comet-hn2 users again.

The user password should match with comet-hn1, comet-hn2 /root/COMET-Accumulo/vmware-cluster/docker-compose.yml setting.

```
[root@comet-master]# ~/COMET-Accumulo/vmware-cluster/create-accu-user.sh 
password of accumulo root: 
username of the new user: comet-hn1
Enter new password for 'comet-hn1': *********
*********
Please confirm new password for 'comet-hn1': *********
*********
```






