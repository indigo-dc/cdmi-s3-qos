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

##### 6. Check if service cdmi-s3-qos has been installed and if is running:

```
service cdmi-s3-qos status
```

##### 7. Send simple REST request to the INDIGO CDMI server

curl -s -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/container -H "Content-Type: application/cdmi-capability" | python -mjson.tool


##### 8. Stop cdmi-s3-qos service.

NOTE: The purpose of this procedure is only to check if ansible role properly installs cdmi-s3-qos service. Becouse of some docker related peculiarity, the service has to be stopped before we disconnect from docker container. Otherwise, even thouh we issue exit command, the bash which is running inside docker will not release  the docker session will release the terminal.

```
service cdmi-s3-qos stop
``` 

##### 9. Disconnect from docker container.

```
exit
```

##### 10. Destroy testing environmed and free the resources.

```
bash ./clean-ubuntu-ansible-test.sh
```
