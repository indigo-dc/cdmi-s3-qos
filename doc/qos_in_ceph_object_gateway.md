# Ceph Object Gateway QoS

## Introduction
This chapter presents the general idea of QoS management in context of Ceph Object Gateway QoS. The description introduces general concept. For specific documetation refering the configuration details of [CEPH](http://docs.ceph.com/docs/master/) and [Ceph Object Gateway](http://docs.ceph.com/docs/master/radosgw/), please check out these projects documentation.

The assumption is that cdmi-s3-qos module operates over already existing and configured Ceph Object Gateway.

## Introduction to CEPH

Ceph Object Gateway is just one of components constituting CEPH storage platform. CEPH allows to build distributed storage clusters. Data stored inside such a cluster can be accessed through different interfaces (object, block and file system like interfaces).

Internally CEPH organizes data into so called pools. Each pool can be configured separately. From QoS point of view, each pool can hold different configuration. Among other things, each pool can be configured to
* use specific type of storage media,
* use storage media form specific physical localization,
* use specific number of additional copies of each file,
* use erasure codes with specific number of coding chunks.

Different QoS profiles can be defined through different sets of configuration parameters assigned to individual pools.

(ADD THAT POOLS HAVE STATIC NATURE AND HAVE TO BE MANUALLY CONFIGUERD VIA ADMIN, TO CHANGE QOS OF GIVEN DATA SET THE DATA HAVE TO BE COPIED/MOVED TO ANOTHER POOL, FROM INDIGO POINT OF VIEW ON THE FLY CHANGES OF POOL CONFIGURATION ARE NOT CONSIDERED)

## Ceph Object Gateway

Ceph Object Gateway is responsible for exposing data as objects via REST interface. As for now (year 2016), there are two specific REST interfaces supported: S3 and Swift.
