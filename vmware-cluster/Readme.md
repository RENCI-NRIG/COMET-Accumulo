# COMET in Vmware Cluster
COMET is deployed in VMware Cluster for Exogeni. Use the following steps to install and configure COMET in Vmware cluster.

## Pre-requisites
Request 7 VMs from ACIS with following profile.
Group 1: 

3 VMs with the names below:
- VM1: comet-master.exogeni.net
- VM2: comet-w1.exogeni.net
- VM3: comet-w2.exogeni.net

Resource specifications:
- OS: CentOS 7 (64bit) 
- 4 cores 
- 16 GB RAM 
- 50 GB storage 
- 2 NICs: NIC1: connected to RENCI Research (with 1 public IP address)
          NIC2: connected to “ExoGENI Management”  (IP address will be assigned by us from private ExoGENI Management subnet)

Group 2: 

4 VMs with the names below:
- VM1: comet-nn.exogeni.net
- VM2: comet-mgr.exogeni.net
- VM3: comet-hn1.exogeni.net
- VM4: comet-hn2.exogeni.net

Resource specifications:
- OS: CentOS 7 (64bit) 
- 2 cores 
- 8 GB RAM 
- 20 GB storage 
- 2 NICs: NIC1: connected to RENCI Research (with 1 public IP address)
          NIC2: connected to “ExoGENI Management”  (IP address will be assigned by us from private ExoGENI Management subnet)
          
## Install & Configuration
Install and configure using the steps below.

### Namenode
Namenode server should be configured the first. Execute the following commands as root user.
```
git clone https://github.com/RENCI-NRIG/COMET-Accumulo.git
cd COMET-Accumulo/vmware-cluster
# ./install.sh <ZooKeeperVersion> <HadoopVersion> <AccumuloVersion> namenode
./install.sh 3.4.12 2.9.0 1.9.3 namenode
```
### Others
Configure rest of the servers(master, resource manager, worker1 and worker2) using following commands as root user.
```
git clone https://github.com/RENCI-NRIG/COMET-Accumulo.git
cd COMET-Accumulo/vmware-cluster
# ./install.sh <ZooKeeperVersion> <HadoopVersion> <AccumuloVersion> accumulo
./install.sh 3.4.12 2.9.0 1.9.3 accumulo
```
### Host names
Update /etc/hosts on all servers(master, resource manager, worker1, worker2, comet-hn1 and comet-hn2) with hostnames pointing to internal IPs
```
192.168.100.31 comet-master zoo3
192.168.100.32 comet-w1
192.168.100.33 comet-w2
192.168.100.34 comet-nn zoo1
192.168.100.35 comet-mgr zoo2
192.168.100.36 comet-hn1
192.168.100.37 comet-hn2
```
### SSH Keys
Copy SSH Keys from namenode to rest of the servers(master, resource manager, worker1 and worker2)
Files to be copied at the same location:
```
/home/hadoop/.ssh/id_rsa.pub
/home/hadoop/.ssh/id_rsa
```
Execute the following commands to fix up the permissions:
```
source /etc/profile.d/hadoop.sh
chown -R hadoop:hadoop $HADOOP_USER_HOME/.ssh
runuser -l hadoop -c 'cat /home/hadoop/.ssh/id_rsa.pub >> /home/hadoop/.ssh/authorized_keys'
chmod 0600 $HADOOP_USER_HOME/.ssh/authorized_keys
chmod 0600 $HADOOP_USER_HOME/.ssh/id_rsa
```
### Allow Ports in firewalld
Allow ports in firewalld as indicated below.

#### Ports on Master and worker nodes
Ports indicated below should be allowed on comet-master, comet-w1 and comet-w2 nodes.

```
cat /etc/firewalld/zones/public.xml
<?xml version="1.0" encoding="utf-8"?>
<zone>
  <short>Public</short>
  <description>For use in public areas. You do not trust the other computers on networks to not harm your computer. Only selected incoming connections are accepted.</description>
  <service name="dhcpv6-client"/>
  <port protocol="tcp" port="9995"/>
  <port protocol="tcp" port="22"/>
  <port protocol="tcp" port="9996"/>
  <port protocol="tcp" port="9997"/>
  <port protocol="tcp" port="9998"/>
  <port protocol="tcp" port="9999"/>
  <port protocol="tcp" port="4560"/>
  <port protocol="tcp" port="10001"/>
  <port protocol="tcp" port="10002"/>
  <port protocol="tcp" port="2181"/>
  <port protocol="tcp" port="2888"/>
  <port protocol="tcp" port="3888"/>
  <rule family="ipv4">
    <source address="152.54.0.0/16"/>
    <accept/>
  </rule>
</zone>
```
#### Ports on head nodes
Ports indicated below should be allowed on comet-hn1 and comet-hn2 nodes.
```
<?xml version="1.0" encoding="utf-8"?>
<zone>
  <short>Public</short>
  <description>For use in public areas. You do not trust the other computers on networks to not harm your computer. Only selected incoming connections are accepted.</description>
  <service name="dhcpv6-client"/>
  <port protocol="tcp" port="8111"/>
  <port protocol="tcp" port="22"/>
  <rule family="ipv4">
    <source address="152.54.0.0/16"/>
    <accept/>
  </rule>
</zone>
```

### Configure Accumulo 
Execute the following commands in order as root user
#### Namenode
```
./setup.sh namenode
```
#### Resourcemgr
```
./setup.sh resourcemgr
```
#### Accumulomaster
```
./setup.sh master
```

**[First-Time Setup for new comet head node]**

* If the table which the new comet head node(s) going to use is not yet existing,
create a new table by:


```
#./create-accu-table.sh
password of accumulo root:
new table name: < tablename >

```


* Each comet head node will use its own username/password to communicate with Accumulo.
To create new user in Accumulo for the new comet head node:

```
#./create-accu-user.sh
password of accumulo root:
username of the new user: < username >
Enter new password for 'username': < password >
Please confirm new password for 'username': < password >
Enter the table name for the user to READ/WRITE: < tablename >

```


#### Workers
```
./setup.sh worker
```
#### Head nodes
NOTE: All commands to be executed as root user

**[Before starting new head node]**
Each head node uses its own username/password to communicate with Accumulo. Make sure the username/password are created in Accumulo for every headnode. see [First-Time Setup in Accumulo](#Accumulomaster).

**[Start comet head nodes]**

- Clone code and go to vmware-cluster
```
git clone https://github.com/RENCI-NRIG/COMET-Accumulo.git
cd COMET-Accumulo/vmware-cluster/
```
- Update docker-compose.yml to include IPs for zoo servers and workers
  and set the accumulo username/password of this comet head node

```
    extra_hosts:
      - "zoo1:192.168.100.34"
      - "zoo2:192.168.100.35"
      - "zoo3:192.168.100.31"
      - "comet-w1:192.168.100.32"
      - "comet-w2:192.168.100.33"
    environment:
      ACCUMULO_USER: < username >
      ACCUMULO_PASSWORD: < password >
      ACCUMULO_TABLENAME: < tablename >
```
- Bring up comet head node
```
./setupheadnode.sh
```
