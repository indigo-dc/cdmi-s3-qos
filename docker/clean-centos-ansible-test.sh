#!/bin/bash

. ./CENTOS_ANSIBLE_TEST_VARS

docker kill $JDK_CONTAINER
docker rm $JDK_CONTAINER
docker rmi $JDK_IMAGE

docker kill $APACHE_CONTAINER
docker rm $APACHE_CONTAINER
docker rmi $APACHE_IMAGE

docker kill $ANSIBLE_CONTAINER
docker rm $ANSIBLE_CONTAINER
docker rmi $ANSIBLE_IMAGE
