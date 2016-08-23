![INDIGO Logo](https://www.indigo-datacloud.eu/sites/default/files/logo_new_1.png)
Copyright 2016 - INDIGO-DataCloud
---
# INDIGO-DataCloud QoS Storage Back-End Module supporting S3-compatible Ceph Object Gateway

This project (cdmi-s3-qos) provides a QoS backend storage module for cdmi-qos server. It is distributed as pre-configured cdmi-qos server which is already integrated with cdmi-s3-qos module and can be run as a separate service.

The cdmi-s3-qos imposes following prerequisites on Cepht Object Gateway node:
* The [cdmi-s3-qos-ceph-provider](https://github.com/indigo-dc/cdmi-s3-qos-ceph-provider) has to be installed and configured on Ceph Object Gateway node.
* The Ceph Object Gateway has to allow on SSH based access (cdmi-s3-qos executes remote commands through SSH protocol).
