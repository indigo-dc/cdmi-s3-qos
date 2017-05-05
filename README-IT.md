## Introduction

This document describes how to carry out integration tests of cdmi-s3-qos module. As the module is meant to be used by INDIGO CDMI server, the tests are designed to check if the module provides the server with expected functionality.

In order to provide you with self-contained testing environment, the special Docker and docker-compose based environment has been worked out. This testing environment consists of two docker containers. One container provides pre-configured INDIGO CDMI server which is integrated with cdmi-s3-qos module. The second container runs minio S3 server which mimics presence of S3 interface exposed by RADOS gateway. 
 
Please have in mind that these docker containers are meant merely for testing purposes, just to check if cdmi-s3-qos module complies with the assumed contract. For example, no effort has been put to configure and manage the INDIGO CDMI server data persistency.

In general, testing procedure consists of the following steps (exact commands and precise steps are presented later):

1. The docker image with INDIGO CDMI server which is integrated with cdmi-s3-qos module and image with S3 server are built and run in background with help of docker-compose tool.
2. Examination of available QoS profiles is performed. This is the point where actual interaction between modules is tested. In response to REST queries, INDIGO CDMI server has to answer with QoS profiles exposed by cdmi-s3-qos module. As there is a few QoS profiles, first the available profiles have to be discovered and next the specific attributes of selected profiles are to be examined.

## Detailed testing procedure

**NOTE:** Below tests often use curl command to query INDIGO CDMI server for QoS metrics. Please note that each server response will contain attributes named **"objectID"** and **"parentID"**. While values of all other attributes will have to match presented reference response patterns, these two attributes have dynamic nature and their values don't have to be the same as values seen in the provided reference response patterns.

### 1. Clone cdmi-s3-qos source codes

```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git
```

### 2. Use docker-compose to build and run docker images and containers

**NOTE:** By default, container with INDIGO CDMI server will try to listen on TCP port number **8080**. If on your machine this port is not eligible then before you will follow the below procedure please set and export `INDIGO_CDMI_PORT` environment variable to value indicating the TCP port number to be exposed by container which hosts INDIGO CDMI server.

For example, to use TCP port number 8888 the INDIGO_CDMI_PORT environment variable should be set and exported like this:

```
export INDIGO_CDMI_PORT=8888
```

To build docker images and run the containers use docker-compose tool:

```
cd cdmi-s3-qos
docker-compose -f docker/docker-compose-integration.yml up -d
```


### 2. Discover all QoS profiles exposed by cdmi-s3-qos module

Here the actual test begins. With help of REST API we are asking the INDIGO CDMI server about QoS profiles exposed by underlying cdmi-s3-qos.

NOTE: From now on, standard output of each curl command will be piped into "python -mjson.tool" command. It is only in order to format the standard output to be more readable for human beings. 


First ask the server for list of all supported QoS profiles of data objects:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/dataobject | python -mjson.tool
```

The output should be similar to this:

```
{
    "capabilities": {},
    "children": [
        "DataobjectProfile1",
        "DataobjectProfile2",
        "DataobjectProfile3"
    ],
    "childrenrange": "0-2",
    "metadata": {},
    "objectID": "0001869F0018F59933353532336334322D653036662D3433",
    "objectName": "dataobject",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F0018C7F936366530313437322D313833342D3438",
    "parentURI": "/cdmi_capabilities"
}
```

For further tests, the most important part of the above result is "children" list. This list contains names of supported QoS profiles. When we now names of all profiles, we can ask for details of specific profile.

To ask for QoS properties associated with DataobjectProfile1 profile issue the command:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/dataobject/DataobjectProfile1 | python -mjson.tool
```

You should obtain output like this:

```
{
    "capabilities": {
        "cdmi_capabilities_exact_inherit": "true",
        "cdmi_capabilities_templates": "true",
        "cdmi_capability_association_time": "true",
        "cdmi_capability_lifetime": "true",
        "cdmi_capability_lifetime_action": "true",
        "cdmi_data_redundancy": "true",
        "cdmi_data_storage_lifetime": "true",
        "cdmi_geographic_placement": "true",
        "cdmi_latency": "true",
        "cdmi_throughput": "true"
    },
    "metadata": {
        "cdmi_capability_lifetime": "P20Y",
        "cdmi_capability_lifetime_action": "delete",
        "cdmi_data_redundancy": "1",
        "cdmi_data_storage_lifetime": "P20Y",
        "cdmi_geographic_placement": [
            "PL"
        ],
        "cdmi_latency": "3000",
        "cdmi_throughput": "100000"
    },
    "objectID": "0001869F001839FD66336137313662302D393462392D3430",
    "objectName": "DataobjectProfile1",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F0018F59933353532336334322D653036662D3433",
    "parentURI": "/cdmi_capabilities/dataobject"
}
```

Now, let's ask for properties of next QoS profile, that is DataObjectProfile2:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/dataobject/DataobjectProfile2 | python -mjson.tool
```

The output should look like this:

```
{
    "capabilities": {
        "cdmi_capabilities_exact_inherit": "true",
        "cdmi_capabilities_templates": "true",
        "cdmi_capability_association_time": "true",
        "cdmi_capability_lifetime": "true",
        "cdmi_capability_lifetime_action": "true",
        "cdmi_data_redundancy": "true",
        "cdmi_data_storage_lifetime": "true",
        "cdmi_geographic_placement": "true",
        "cdmi_latency": "true",
        "cdmi_throughput": "true"
    },
    "metadata": {
        "cdmi_capability_lifetime": "P20Y",
        "cdmi_capability_lifetime_action": "delete",
        "cdmi_data_redundancy": "2",
        "cdmi_data_storage_lifetime": "P20Y",
        "cdmi_geographic_placement": [
            "PL",
            "UK"
        ],
        "cdmi_latency": "2000",
        "cdmi_throughput": "110000"
    },
    "objectID": "0001869F0018086D38663265373535332D623331652D3433",
    "objectName": "DataobjectProfile2",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F0018F59933353532336334322D653036662D3433",
    "parentURI": "/cdmi_capabilities/dataobject"
}
```

In similar way, let's ask for properties of DataobjectProfile3:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/dataobject/DataobjectProfile3 | python -mjson.tool
```

The output of this command should be like this:

```
{
    "capabilities": {
        "cdmi_capabilities_exact_inherit": "true",
        "cdmi_capabilities_templates": "true",
        "cdmi_capability_association_time": "true",
        "cdmi_capability_lifetime": "true",
        "cdmi_capability_lifetime_action": "true",
        "cdmi_data_redundancy": "true",
        "cdmi_data_storage_lifetime": "true",
        "cdmi_geographic_placement": "true",
        "cdmi_latency": "true",
        "cdmi_throughput": "true"
    },
    "metadata": {
        "cdmi_capability_lifetime": "P20Y",
        "cdmi_capability_lifetime_action": "delete",
        "cdmi_data_redundancy": "3",
        "cdmi_data_storage_lifetime": "P20Y",
        "cdmi_geographic_placement": [
            "NL",
            "ES",
            "PL"
        ],
        "cdmi_latency": "500",
        "cdmi_throughput": "120000"
    },
    "objectID": "0001869F001847AF31303431386336612D626536312D3433",
    "objectName": "DataobjectProfile3",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F0018F59933353532336334322D653036662D3433",
    "parentURI": "/cdmi_capabilities/dataobject"
}
```

In above steps we examined all available QoS profiles which can be assigned to data objects. The thing is that CDMI specification provides for defining separate QoS profilers associated with containers (roughly speaking container is a counterpart of folder or directory). 

Therefore, now we will query for list of available containers' QoS profiles and next we will query for properties of these profiles.

Query for available container profiles:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/container | python -mjson.tool
```

Expected answer:

```
{
    "capabilities": {},
    "children": [
        "RootContainer",
        "ContainerProfile1",
        "ContainerProfile2",
        "ContainerProfile3"
    ],
    "childrenrange": "0-3",
    "metadata": {},
    "objectID": "0001869F001897C366333661323466652D323730392D3431",
    "objectName": "container",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F0018C7F936366530313437322D313833342D3438",
    "parentURI": "/cdmi_capabilities"
}
```

In the above answer, the children attribute contains names of all available profiles. Now we will ask for properties of each of these profiles.

Ask for attributes of profile named RootContainer:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/container/RootContainer | python -mjson.tool 
```

Expected answer:

```
{
    "capabilities": {
        "cdmi_capabilities_exact_inherit": "true",
        "cdmi_capabilities_templates": "true"
    },
    "metadata": {
        "cdmi_location": [
            "/"
        ]
    },
    "objectID": "0001869F001896E166623932313565382D663731652D3437",
    "objectName": "RootContainer",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F001897C366333661323466652D323730392D3431",
    "parentURI": "/cdmi_capabilities/container"
}
```

Ask for properties of profile named ContainerProfile1:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/container/ContainerProfile1 | python -mjson.tool
```

Expected answer:

```
{
    "capabilities": {
        "cdmi_capabilities_exact_inherit": "true",
        "cdmi_capabilities_templates": "true",
        "cdmi_capability_association_time": "true",
        "cdmi_capability_lifetime": "true",
        "cdmi_capability_lifetime_action": "true",
        "cdmi_data_redundancy": "true",
        "cdmi_data_storage_lifetime": "true",
        "cdmi_default_dataobject_capability_class": "true",
        "cdmi_geographic_placement": "true",
        "cdmi_latency": "true",
        "cdmi_location": "true",
        "cdmi_throughput": "true"
    },
    "metadata": {
        "cdmi_capability_lifetime": "P20Y",
        "cdmi_capability_lifetime_action": "delete",
        "cdmi_data_redundancy": "1",
        "cdmi_data_storage_lifetime": "P20Y",
        "cdmi_default_dataobject_capability_class": "/cdmi_capabilities/dataobject/DataobjectProfile1",
        "cdmi_geographic_placement": [
            "DE"
        ],
        "cdmi_latency": "3000",
        "cdmi_location": [
            "/standard"
        ],
        "cdmi_throughput": "100000"
    },
    "objectID": "0001869F0018238264346566373765362D383264372D3436",
    "objectName": "ContainerProfile1",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F001897C366333661323466652D323730392D3431",
    "parentURI": "/cdmi_capabilities/container"
}
```

Ask for properties of profile named ContainerProfile2:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/container/ContainerProfile2 | python -mjson.tool
```

Expected answer:

```
{
    "capabilities": {
        "cdmi_capabilities_exact_inherit": "true",
        "cdmi_capabilities_templates": "true",
        "cdmi_capability_association_time": "true",
        "cdmi_capability_lifetime": "true",
        "cdmi_capability_lifetime_action": "true",
        "cdmi_data_redundancy": "true",
        "cdmi_data_storage_lifetime": "true",
        "cdmi_default_dataobject_capability_class": "true",
        "cdmi_geographic_placement": "true",
        "cdmi_latency": "true",
        "cdmi_location": "true",
        "cdmi_throughput": "true"
    },
    "metadata": {
        "cdmi_capability_lifetime": "P20Y",
        "cdmi_capability_lifetime_action": "delete",
        "cdmi_data_redundancy": "2",
        "cdmi_data_storage_lifetime": "P20Y",
        "cdmi_default_dataobject_capability_class": "/cdmi_capabilities/dataobject/DataobjectProfile2",
        "cdmi_geographic_placement": [
            "PL",
            "UK"
        ],
        "cdmi_latency": "2000",
        "cdmi_location": [
            "/silver"
        ],
        "cdmi_throughput": "110000"
    },
    "objectID": "0001869F0018B3D764666431353163332D376565352D3432",
    "objectName": "ContainerProfile2",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F001897C366333661323466652D323730392D3431",
    "parentURI": "/cdmi_capabilities/container"
}
```

Ask for properties of profile named ContainerProfile3:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/container/ContainerProfile3 | python -mjson.tool
```

Expected answer:

```
{
    "capabilities": {
        "cdmi_capabilities_exact_inherit": "true",
        "cdmi_capabilities_templates": "true",
        "cdmi_capability_association_time": "true",
        "cdmi_capability_lifetime": "true",
        "cdmi_capability_lifetime_action": "true",
        "cdmi_data_redundancy": "true",
        "cdmi_data_storage_lifetime": "true",
        "cdmi_default_dataobject_capability_class": "true",
        "cdmi_geographic_placement": "true",
        "cdmi_latency": "true",
        "cdmi_location": "true",
        "cdmi_throughput": "true"
    },
    "metadata": {
        "cdmi_capability_lifetime": "P20Y",
        "cdmi_capability_lifetime_action": "delete",
        "cdmi_data_redundancy": "3",
        "cdmi_data_storage_lifetime": "P20Y",
        "cdmi_default_dataobject_capability_class": "/cdmi_capabilities/dataobject/DataobjectProfile3",
        "cdmi_geographic_placement": [
            "NL",
            "ES"
        ],
        "cdmi_latency": "500",
        "cdmi_location": [
            "/golden"
        ],
        "cdmi_throughput": "120000"
    },
    "objectID": "0001869F00187F1630666561313832372D643038612D3461",
    "objectName": "ContainerProfile3",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F001897C366333661323466652D323730392D3431",
    "parentURI": "/cdmi_capabilities/container"
}
```

### 3. Asking for QoS profiles assigned to actual containers.

To check QoS profiles assigned to selected containers, first we will list containers which are in "/" path (in root path), and then we will ask for QoS properties of two of them.

To get list of containers which reside in root path invoke this command:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/ | python -mjson.tool
```

Expected answer:

```
{
    "capabilitiesURI": "/cdmi_capabilities/container/RootContainer",
    "children": [
        "golden/",
        "silver/",
        "standard/"
    ],
    "childrenrange": "0-2",
    "completionStatus": "Complete",
    "domainURI": "/cdmi_domains",
    "exports": {
        "s3": {
            "url": "http://radosgw:9000/"
        }
    },
    "metadata": {},
    "objectID": "0001869F0018A72465613837636364362D303662392D3432",
    "objectName": "/",
    "objectType": "application/cdmi-container",
    "parentID": "0001869F0018A72465613837636364362D303662392D3432",
    "parentURI": "/"
}
```

The children attribute in the above answer contains list of top-level containers (as we will see in further answers containers names are featured by '/' sign at the end of the name, while names of data objects don't contain this sign). 

Now we will ask for QoS profile assigned to container named "golden/":

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/golden/ | python -mjson.tool
```

Expected answer:

```
{
    "capabilitiesURI": "/cdmi_capabilities/container/ContainerProfile3",
    "children": [
        "file6.txt"
    ],
    "childrenrange": "0",
    "completionStatus": "Complete",
    "domainURI": "/cdmi_domains",
    "exports": {
        "s3": {
            "url": "http://radosgw:9000/golden"
        }
    },
    "metadata": {
        "cdmi_capability_lifetime": "P20Y",
        "cdmi_capability_lifetime_action": "delete",
        "cdmi_data_redundancy_provided": "3",
        "cdmi_data_storage_lifetime": "P20Y",
        "cdmi_default_dataobject_capability_class": "/cdmi_capabilities/dataobject/DataobjectProfile3",
        "cdmi_geographic_placement_provided": [
            "NL",
            "ES"
        ],
        "cdmi_latency_provided": "3000",
        "cdmi_location": [
            "/golden"
        ],
        "cdmi_throughput_provided": "110000"
    },
    "objectID": "0001869F0018D13935366335303239622D356536392D3437",
    "objectName": "golden",
    "objectType": "application/cdmi-container",
    "parentID": "0001869F0018A72465613837636364362D303662392D3432",
    "parentURI": "/"
}
```

Ask for QoS properties of container named "standard/":


```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/standard/ | python -mjson.tool
```

Expected answer:

```
{
    "capabilitiesURI": "/cdmi_capabilities/container/ContainerProfile1",
    "children": [
        "file1.txt",
        "file2.txt",
        "subfolder/"
    ],
    "childrenrange": "0-2",
    "completionStatus": "Complete",
    "domainURI": "/cdmi_domains",
    "exports": {
        "s3": {
            "url": "http://radosgw:9000/standard"
        }
    },
    "metadata": {
        "cdmi_capability_lifetime": "P20Y",
        "cdmi_capability_lifetime_action": "delete",
        "cdmi_data_redundancy_provided": "1",
        "cdmi_data_storage_lifetime": "P20Y",
        "cdmi_default_dataobject_capability_class": "/cdmi_capabilities/dataobject/DataobjectProfile1",
        "cdmi_geographic_placement_provided": [
            "DE"
        ],
        "cdmi_latency_provided": "1000",
        "cdmi_location": [
            "/standard"
        ],
        "cdmi_throughput_provided": "90000"
    },
    "objectID": "0001869F00183D2461633837383532332D386239612D3430",
    "objectName": "standard",
    "objectType": "application/cdmi-container",
    "parentID": "0001869F0018A72465613837636364362D303662392D3432",
    "parentURI": "/"
}
```

### 4. Asking for QoS profiles assigned to actual objects stored in INDIGO CDMI server.

Now we will ask for QoS profiles assigned to selected data objects. From the above answers we can select two data objects which we will use in tests. Let it be:

- /golden/file6.txt, and
- /standard/file1.txt


First ask for QoS profile of "/golden/file6.txt" data object

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/golden/file6.txt | python -mjson.tool
```

Expected answer: 

```
{
    "capabilitiesURI": "/cdmi_capabilities/dataobject/DataobjectProfile3",
    "completionStatus": "Complete",
    "domainURI": "/cdmi_domains",
    "metadata": {
        "cdmi_capability_association_time_provided": "Fri May 05 07:20:57 UTC 2017",
        "cdmi_capability_lifetime": "P20Y",
        "cdmi_capability_lifetime_action": "delete",
        "cdmi_data_redundancy_provided": "3",
        "cdmi_data_storage_lifetime": "P20Y",
        "cdmi_geographic_placement_provided": [
            "NL",
            "ES",
            "PL"
        ],
        "cdmi_latency_provided": "3000",
        "cdmi_throughput_provided": "110000"
    },
    "mimetype": "application/octet-stream",
    "objectID": "0001869F0018D20439653861383230342D653630382D3439",
    "objectName": "file6.txt",
    "objectType": "application/cdmi-object",
    "parentID": "0001869F0018D13935366335303239622D356536392D3437",
    "parentURI": "/golden"
}
```

In short, the above answer points out that the profile assigned with the being examined object is "DataobjectProfile3" (we can tell it from "capabilitiesURI" attribute). Additionally the properties of this profile are presented as well.


In similar way we can ask for QoS profile of "/standard/file1.txt" data object:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/standard/file1.txt | python -mjson.tool
```

The answer should be like this:

```
{
    "capabilitiesURI": "/cdmi_capabilities/dataobject/DataobjectProfile1",
    "completionStatus": "Complete",
    "domainURI": "/cdmi_domains",
    "metadata": {
        "cdmi_capability_association_time_provided": "Fri May 05 07:20:57 UTC 2017",
        "cdmi_capability_lifetime_action_provided": "delete",
        "cdmi_capability_lifetime_provided": "P20Y",
        "cdmi_data_redundancy_provided": "1",
        "cdmi_data_storage_lifetime_provided": "P20Y",
        "cdmi_geographic_placement_provided": [
            "PL"
        ],
        "cdmi_latency_provided": "1000",
        "cdmi_throughput_provided": "90000"
    },
    "mimetype": "application/octet-stream",
    "objectID": "0001869F0018EECE34333633396666322D386566372D3466",
    "objectName": "file1.txt",
    "objectType": "application/cdmi-object",
    "parentID": "0001869F00183D2461633837383532332D386239612D3430",
    "parentURI": "/standard"
}
```

This time, we see that the QoS profile assigned to "/standard/file1.txt" data object is DataobjectProfile1.


### 5. Clean up after tests
 
To clean up after tests (stop and remove containers and images created by docker-compose command) invoke this command:
 
```
docker-compose -f docker/docker-compose-integration.yml down --rmi all
```
 
