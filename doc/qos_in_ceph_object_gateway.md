# Ceph Object Gateway QoS

## Introduction
This chapter presents the general idea of QoS management in context of Ceph Object Gateway QoS. The description introduces general concept. For specific documetation refering the configuration details of [CEPH](http://docs.ceph.com/docs/master/) and [Ceph Object Gateway](http://docs.ceph.com/docs/master/radosgw/), please check out these projects documentation.

The assumption is that cdmi-s3-qos module operates over already existing and configured Ceph Object Gateway.

## Introduction to CEPH

Ceph Object Gateway is only one of components constituting CEPH storage platform. CEPH allows to build distributed storage clusters. The same data stored inside such a cluster can be accessed through different interfaces (object, block and file system like interfaces).

Internally CEPH 

## Ceph Object Gateway

Ceph Object Gateway is responsible for exposing data as objects via REST interface. As for now (year 2016), there are two specific REST interfaces supported: S3 and Swift.
