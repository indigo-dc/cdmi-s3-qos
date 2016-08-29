![INDIGO Logo](https://www.indigo-datacloud.eu/sites/default/files/logo_new_1.png)
Copyright 2016 - INDIGO-DataCloud
---
# INDIGO-DataCloud QoS Storage Back-End Module supporting S3-compatible Ceph Object Gateway

This project ([cdmi-s3-qos](https://github.com/indigo-dc/cdmi-s3-qos)) provides a QoS backend storage module for [cdmi-qos](https://github.com/indigo-dc/cdmi) server. Generally, the module is destined to be integrated with the already installed cdmi-qos server. Nevertheless, for the sake of simplicity, the installation packages with properly pre-configured cdmi-qos server has been also provided.

This documentation covers the following questions:
* General description of QoS in a context of Ceph Object Gateway.
* Installation and configuration based on binary packages with pre-configured cdmi-qos server (i.e. QoS module is installed toghether with cdmi-qos server).
* Building the module from sources.
* Running the pre-configured cdmi-qos server from docker image.
* Reference and informations required to manual integration with cdmi-qos server.

The cdmi-s3-qos imposes following prerequisites on Ceph Object Gateway node:
* The [cdmi-s3-qos-ceph-provider](https://github.com/indigo-dc/cdmi-s3-qos-ceph-provider) has to be installed and configured on Ceph Object Gateway node.
* The Ceph Object Gateway has to allow on SSH based access (cdmi-s3-qos executes remote commands through SSH protocol).
