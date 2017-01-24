#!/bin/bash


. ./UBUNTU_ANSIBLE_TEST_VARS


#
# JDK
#
docker kill $JDK_CONTAINER
docker rm $JDK_CONTAINER
docker rmi $JDK_IMAGE

docker build -f $JDK_DOCKERFILE -t $JDK_IMAGE ..
docker run -di --name $JDK_CONTAINER $JDK_IMAGE
docker exec $JDK_CONTAINER $PACKAGE_BUILD_COMMAND 

#
# APACHE
#
docker kill $APACHE_CONTAINER
docker rm $APACHE_CONTAINER
docker rmi $APACHE_IMAGE


docker build -f $APACHE_DOCKERFILE -t $APACHE_IMAGE ..
docker run -di --name $APACHE_CONTAINER $APACHE_IMAGE

#
# copy binary package from JDK to APACHE
#
docker cp $JDK_CONTAINER:/cdmi-s3-qos/$PACKAGE_NAME .
docker kill $JDK_CONTAINER
docker rm $JDK_CONTAINER
docker rmi $JDK_IMAGE
docker cp ./$PACKAGE_NAME $APACHE_CONTAINER:/var/www/html/$PACKAGE_NAME
docker exec $APACHE_CONTAINER chmod 644 /var/www/html/$PACKAGE_NAME
rm -f ./$PACKAGE_NAME 


#
# ANSIBLE
#
docker kill $ANSIBLE_CONTAINER
docker rm $ANSIBLE_CONTAINER
docker rmi $ANSIBLE_IMAGE

docker build -f $ANSIBLE_DOCKERFILE -t $ANSIBLE_IMAGE ..
docker run -di --link $APACHE_CONTAINER:testing-source --name $ANSIBLE_CONTAINER $ANSIBLE_IMAGE
#docker cp /home/gracjan/projects/ansible-role-cdmi-s3-qos $ANSIBLE_CONTAINER:/root/roles/indigo-dc.cdmi-s3-qos
