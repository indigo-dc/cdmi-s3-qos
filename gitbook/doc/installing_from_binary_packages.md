# Installation

## Introduction

The cdmi-s3-qos module is meant to be run as part of cdmi-qos server. It means that the module should be integrated with the pre-installed server accordingly to the server documentation.

This chapter presents cdmi-s3-qos module installation form binary packages for Ubuntu 14.04 and for CentOS 7. The procedure of source codes building is described as well.

Additionally the way of building and running docker based, testing environment with cdmi-qos server pre-configured to use cdmi-s3-qos module is shown.


## Requirements

* Java &lt; = 8 runtime
* OS: Ubuntu &lt; = 14.04 (x86_64 architecture) or CentOS7 (x86_64 architecture)
* pre-configured Ceph Object Gateway with S3 API
* pre-installed cdmi-qos server

## Repository

The binary packages used in the bellow procedure are available in [INDIGO-DataCloud Software Repository](http://repo.indigo-datacloud.eu).

## Ubuntu

Installation from deb package:

```
sudo dpkg -i cdmi-s3-qos-<version>.deb
``` 
where &lt; version &gt; is placeholder for actuall package version.

Installation from INDIGO-DataCloud Software Repository repository:

```
sudo apt-get install cdmi-s3-qos
```
After installation, the cdmi-s3-qos module will be „visible” for cdmi-qos server, so the server can be configured to start using it.

## CentOS

Installation from rpm package:

```
sudo rpm -ivh cdmi-s3-qos-<version>.el7.centos.x86_64.rpm
```
where &lt; version &gt; is placeholder for actual package version.

Installation from INDIGO-DataCloud Software Repository:

```
sudo yum install cdmi-s3-qos
```

After installation, the cdmi-s3-qos module will be „visible” for cdmi-qos server, so the server can be configured to start using the module.

