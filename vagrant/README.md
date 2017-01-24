# CentOS 7 based environment for testing indigo-dc.cdmi-s3-qos ansible role 

There is Vagrant box with CentOS 7 configured in this folder.

It is provided in order to test ansible role indigo-dc.cdmi-s3-qos available at https://github.com/indigo-dc/ansible-role-cdmi-s3-qos.

This role is being downloaded and placed inside vagrant box when the below procedure is being executed.

To carry out tests follow the below procedure.

##### 1. first run build-centos-ansible-test.sh bash script.

```
bash ./build-centos-ansible-test.sh
```

##### 2. Connect to CentOS 7 box:

```
vagrant ssh
```

##### 3. Change user context to root. The box is preconfigured so that no password will be required:

```
sudo su -
```

##### 4. To see example playbook and inventory files you can list the current directory:

```
ls -l
``` 

In response you should see something like this:

```
-rw-------. 1 root root 5385 Nov  4 23:11 anaconda-ks.cfg
-rw-r--r--. 1 root root   41 Jan 24 12:06 inventory
-rw-r--r--. 1 root root   50 Jan 24 12:06 playbook
-rw-r--r--. 1 root root  111 Jan 24 12:06 playbook-with-vars
drwxr-xr-x. 3 root root   34 Jan 24 12:06 roles
``` 

##### 5. Run the provided example playbook against provided example inventory file:

```
ansible-playbook playbook-with-vars -i ./inventory
``` 

##### 6. Check if cdmi-s3-qos service has been installed and if it is running:

```
systemctl status cdmi-s3-qos
```

##### 7. Check if cdmi server responses to requests:

```
curl -s -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/container -H "Content-Type: application/cdmi-capability" | python -mjson.tool
```

##### 8. Exit from vagrant provided ssh session:

```
# fist exit from root account
exit
# ultimately disconnect from vagrant box
exit
``` 

##### 9. Clean up the vagrant box and associated resources:

```
bash ./clean-centos-ansible-test.sh
```
