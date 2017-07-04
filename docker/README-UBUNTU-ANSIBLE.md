# Ubuntu 14.04 based environment for testing indigo-dc.cdmi-s3-qos ansible role

Bellow procedure is meant to test if indigo-dc.cdmi-s3-qos ansible role installs cdmi-s3-qos service on Ubuntu 14.04 platform.
The actual role is hosted at https://github.com/indigo-dc/ansible-role-cdmi-s3-qos

When the below procedure is being executed, the ansible role is being downloaded and placed inside the docker image. 

##### 1. First clone the cdmi-s3-qos project and go to pointed directory.

```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git
cd cdmi-s3-qos/docker
```


##### 2. Build and run docker containers with Ubuntu 14.04.

```
bash ./build-ubuntu-ansible-tests.sh
```

##### 3. Connect to the docker container.

```
docker exec -ti ubuntu-ansible-container bash
```

##### 4. Check playbooks and invertory files are in place:

```
ls -l
```

The outlook of the current directory should look similar to this one:

```
-rw-r--r-- 1 root root   41 Jan 24 14:55 inventory
-rw-r--r-- 1 root root   50 Jan 24 14:55 playbook
-rw-r--r-- 1 root root  111 Jan 24 14:55 playbook-with-vars
drwxr-xr-x 3 root root 4096 Jan 24 14:55 roles
```

##### 5. Apply the indigo-dc.cdmi-s3-qos using.

```
ansible-playbook playbook-with-vars -i ./inventory
```

##### 5. Check if cdmi-s3-qos package has been installed.

The command:

```
dpkg -l | grep cdmi
```

should reveal that the cdmi-s3-qos package has been installed:

```
ii  cdmi-s3-qos      2.0.0        all        Module for INDIGO CDMI server. The module integrates RADOS GW S3 with the CDMI server.
```


##### 6. Disconnect from docker container.

```
exit
```

##### 7. Destroy testing environmed and free the resources.

```
bash ./clean-ubuntu-ansible-test.sh
```
