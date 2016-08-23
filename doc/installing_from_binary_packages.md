# Installing

## Introduction
 Please note that cdmi-s3-qos implements a QoS module which is meant to be run as part of cdmi server. Nevertheles, the installation packages provides both the cdmi server and properly integrated QoS module.
 
After installation, there is cdmi-s3-qos service available in the system. The service consists of cdmi server which is configured to use QoS module.

The binary packages has been prepared for Ubuntu 14.04 and for CentOS 7. Building the QoS module from sources as well as running the service inside docker container are described in succeeding chapters.

## Requirements
* Java >= 8 runtime
* OS: Ubuntu >= 14.04 or Centos 7
* S3 compatible Ceph Object Gateway with SSH and [cdmi-s3-qos-ceph-provider](https://github.com/indigo-dc/cdmi-s3-qos-ceph-provider) installed


