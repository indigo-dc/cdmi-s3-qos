#!/bin/bash


yum update -y
yum install git curl mc maven java-1.8.0-openjdk-devel rpm-build net-tools epel-release httpd elinks -y
yum update -y
yum install ansible -y
cd /root
cp /vagrant/sync/inventory .
cp /vagrant/sync/playbook* .

chmod -x playbook inventory playbook-with-vars
mkdir roles
cd roles
#cp -r /vagrant/sync/ansible-role-cdmi-s3-qos indigo-dc.cdmi-s3-qos
git clone https://github.com/indigo-dc/ansible-role-cdmi-s3-qos.git indigo-dc.cdmi-s3-qos
cd indigo-dc.cdmi-s3-qos
#git checkout devel
cd
systemctl enable httpd
systemctl start httpd

cd /vagrant/cdmi-s3-qos-tmp
./build_release_rpm.sh
cp *.rpm /var/www/html
chmod 444 /var/www/html/*.rpm

echo "" >> /etc/hosts
echo "127.0.0.1 testing-source" >> /etc/hosts
