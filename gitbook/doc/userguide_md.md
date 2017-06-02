# User guide

##Refer to cdmi-qos documentation

The cdmi-s3-qos module is meant to be part of [cdmi-qos](https://indigo-dc.gitbooks.io/cdmi-qos/content) server, and as such doesn’t introduce any direct interaction with end-users.

The basics of user interaction with cdmi-qos server are presented in cdmi-qos documentation, in chapter [API Walkthrough](https://indigo-dc.gitbooks.io/cdmi-qos/content/doc/api_walkthrough.html).

##Limitations

In general cdmi-qos processes four types of queries related to QoS managemnt: ask for all supported QoS profiles, ask for details of specific QoS profile, ask for QoS profile related with specific data object or container and finally change the QoS profile assigned to container or data object.

The cdmi-qos server processes the mentioned queries and requests by delegating them to "QoS Storage Back-End Modules". Such a module can be treated as kind of proxy to underlying storage technology and its QoS related functionality. The cdmi-s3-qos module is a proxy to QoS management facilities of CEPH cluster and to its Object Storage Gateway sub-component.

Due to intrinsic Object Storage Gateway qualities, the QoS properties assigned to a given S3 data object can not be changed on the fly. It means that when cdmi-qos server delegates QoS policy change request to cdmi-s3-qos module, the request cannot be realized. In such a case the module informs the server that the request cannot be fulfilled and server returns proper error status to the caller.
