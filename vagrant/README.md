There is Vagrant box with CentOS 7 configured in this folder.

It is provided in order to test ansible role indigo-dc.cdmi-s3-qos available at https://github.com/indigo-dc/ansible-role-cdmi-s3-qos.

To carry out tests:

1. first run build-centos-ansible-test.sh bash script.

```
bash ./build-centos-ansible-test.sh
```

2. Connect to CentOS 7 box:

```
vagrant ssh
```

3. Change user context to root. The box is preconfigured so that no password will be required:

```
sudo su -
```

4. 