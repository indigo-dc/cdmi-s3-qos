# Installing and running as a standalone service

## Introduction
 Please note that cdmi-s3-qos implements a QoS module which is meant to be run as part of cdmi server. Nevertheles, the installation packages provides both the cdmi server and properly integrated QoS module.
 
After installation, there is cdmi-s3-qos service available in the system. The service consists of cdmi server which is configured to use QoS module.

The binary packages has been prepared for Ubuntu 14.04 and for CentOS 7. Building the QoS module from sources as well as running the service inside docker container are described in succeeding chapters.

## Requirements
* Java >= 8 runtime
* OS: Ubuntu >= 14.04 (on x86_64 architecture) or Centos 7 (on x86_64 architecture)
* S3 compatible Ceph Object Gateway with SSH and [cdmi-s3-qos-ceph-provider](https://github.com/indigo-dc/cdmi-s3-qos-ceph-provider) installed


## Installation

### Ubuntu
(TODO: Tell how to get the binary package, possibly there is or there will be indigo repository.)

Installation from dep package:

```sudo dpkg -i cdmi-s3-qos-<version>.deb```

where <version> is placeholder for actuall package version.

Installation from indigo repository:

``` sudo apt-get install cdmi-s3-qos ```

After installation, the cdmi-s3-qos service will appear. The service configuration file is in /etc/systemd/system/cdmi-s3-qos.service file.

The service will start imidietly after instllation, and later it will be started automatically, together with OS at boot time.

### Centos

(TODO: Tell how to get binary packages and / or how to configure repositories)

Installation from rpm package:

``` sudo rpm -ivh cdmi-s3-qos-<version>.el7.centos.x86_64.rpm ```

where <version> is placeholder for actuall package version.

Instalation from indigo repository:

```sudo yum install cdmi-s3-qos ```

## Starting, stoping and enabling

### Ubuntu

### Centos

## Configuration