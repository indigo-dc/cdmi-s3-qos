# RADOSGW, Ceph Object Gateway and QoS

## Introduction

In general, the cdmi-s3-qos module is meant to expose QoS related metrics of storage which is served through RADOSGW via S3 API. Without a doubt, to better understand the purpose of cdmi-s3-qos module, the basic knowlage of CEPH’s components like Ceph Object Gateway and RADOSGW comes in handy. Therefore this section explains relation between these components and briefly introduces CEPH’s features which are relevant from QoS management point of view. 
This document doesn't tell about CEPH cluster maintenance routines. This piece of knowledge is to be gained from documentation strictly related to CEPH cluster.

## What is CEPH?

CEPH allows us to build distributed storage clusters. Data stored inside such a cluster can be accessed through different interfaces (object like, block like and file system like interfaces).

Internally, CEPH organizes data into so called pools. Each pool can be configured separately. From QoS point of view, each pool can hold different configuration. Among other things, each pool can be adjusted to
* use specific type of storage media,
* use storage media form specific physical localization,
* use specific number of additional copies of each file,
* use erasure codes with specific number of coding chunks.

Differently configured CEPH pools can make up distinct QoS profiles.
The configuration of individual pools is static in its nature. That means that frequent reconfiguration are inconvenient and not welcome. Once established, set of pool parameters, should remain fixed for longer time. The changes are possible but not recommended. They involve pool rebuilding process and therefore are costly and ineffective (at least when it comes to the aforementioned QoS related qualities).
Please keep in mind that from cdmi-qos server point of view, the pool reconfiguration is considered as the CEPH administrator internal affair. It means that we don’t plan to expose pools reconfiguration options neither through cdmi-qos server nor via cdmi-s3-qos module.

## What is Ceph Object Gateway and RADOSGW?

Ceph Object Gateway is one of components constituting CEPH storage platform. This component is responsible for exposing data as objects via REST interface. As for now (year 2017), there are two specific REST interfaces supported: S3 and Swift. The cdmi-s3-qos module is designed to work with S3 variation of Ceph Object Gateway.

The term Ceph Object Gateway pertains to the object related interfaces in very general sense. The actual piece of software which implements the REST interface is named RADOSGW and actually is FastCGI module.

In S3 based storage the data is stored within so called buckets. Ceph Object Gateway establishes direct relation between S3 buckets and CEPH pools. Each bucket is assigned to only one pool, but one pool can host many buckets.
Because pools can be configured to provide different QoS profiles, the S3 buckets which are build on top of those pools are automatically stuck to these profiles.

The cdmi-s3-qos module functionality focuses on providing information about all pre-existing S3 buckets and their QoS profiles. In other words it exposes QoS properties of underlying pools associated with given buckets.