![INDIGO Logo](https://www.indigo-datacloud.eu/sites/default/files/logo_new_1.png)

## Copyright 2016 - INDIGO-DataCloud

# About cdmi-s3-qos

The [cdmi-s3-qos](https://github.com/indigo-dc/cdmi-s3-qos) project provides a QoS storage back-end module for [cdmi-qos server](https://github.com/indigo-dc/cdmi). The module complies with the server imposed interface so that the module can be found and loaded by the server during boot time. The module exposes to the server QoS related properties of underlying CEPH cluster which in turn is hidden after S3 API implemented by [RADOSGW](http://docs.ceph.com/docs/master/man/8/radosgw/) \(CEPH’s component exporting data through object API\).

This documentation covers the following topics:

* General description of QoS in the context of RADOSGW and S3 API.
* Binary packages based installation and configuration of cdmi-s3-qos module.
* Building the cdmi-s3-qos module from sources.
* Reference and information required to manual integration with cdmi-qos server.

