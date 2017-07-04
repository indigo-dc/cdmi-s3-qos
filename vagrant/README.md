# CentOS 7 based environment for testing indigo-dc.cdmi-s3-qos ansible role 

There is Vagrant box with CentOS 7 configured in this folder.

It is provided in order to test Ansible role indigo-dc.cdmi-s3-qos available at https://github.com/indigo-dc/ansible-role-cdmi-s3-qos.

This role is downloaded and placed inside vagrant box when the below procedure is being executed.

To carry out tests follow the below steps.


##### 1. First clone the cdmi-s3-qos project and go to pointed directory.

```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git
cd cdmi-s3-qos/vagrant
```

##### 2. Run build-centos-ansible-test.sh bash script.

```
bash ./build-centos-ansible-test.sh
```

##### 3. Connect to CentOS 7 box:

```
vagrant ssh
```

##### 4. Change user context to root. The box is preconfigured so that no password will be required:

```
sudo su -
```

##### 5. To see example playbook and inventory files you can list the current directory:

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

##### 6. Run the provided example playbook against the provided example inventory file:

```
ansible-playbook playbook-with-vars -i ./inventory
``` 

##### 7. Check if adequate rpm has been installed:

```
rpm -qa | grep cdmi-s3-qos
```

In response you should see that cdmi-s3-qos rpm package is present in system:

```
cdmi-s3-qos-2.0.0-1.el7.centos.x86_64
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
