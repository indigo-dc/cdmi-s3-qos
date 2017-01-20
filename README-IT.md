## Introduction

This document describes how to carry out integration tests of cdmi-s3-qos module. As the module is meant to be used by INDIGO CDMI server, the tests are designed to check if the module provides the server with expected information.

In order to provide you with self-contained testing environment, the special Dockerfile has been created. It means that all tests can be performed inside one, pre-configured docker container. All required components are already embedded into it.

Please have in mind that the docker container you get from the aforementioned Dockerfile is meant merely for testing purposes, just to check if cdmi-s3-qos module complies to the assumed contract. For example, no effort has been put to configure and manage the INDIGO CDMI server data persistency. Docker container, once used in testing procedure becomes useless anymore and as  such should be removed. Testing and configuration aspects not related to integration between cdmi-s3-qos and INDIGO CDMI server has been omitted.

In general, testing procedures consists of the following steps (exact commands and precise procedure is presented later):

1. The docker image with INDIGO CDMI server and integrated cdmi-s3-qos module is built. 
2. The docker container in interactive mode is launched (we end-up with bash session executed inside the container).
3. The pre-configured INDIGO CDMI server is ran (pre-configured means already integrated with cdmi-s3-qos module). As the console will be required to type further commands, the server has to be ran in background.
4. Test fixture is set-up. In the test procedure we are going to ask for QoS profiles of some CDMI containers and dataobjects. In this step the related containers and dataobjects are being created.
5. Examination of all available QoS profiles is performed. This is the point where actual interaction between modules is tested. In response to REST queries, CDMI server has to answer with QoS profiles exposed by cdmi-s3-qos module. As there is a few QoS profiles, first, available profiles have to be discovered and next specific attributes of each profile are examined.  

## Detailed testing procedure

### 1. Clone cdmi-s3-qos source codes

```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git
```

### 2. Build the docker image with integrated testing environment

```
cd cdmi-s3-qos/docker
docker build -f Dockerfile-integration-tests -t integration-tests-image ..
```

### 3. Run bash session inside the docker container

```
docker run -ti --name integration-tests-container integration-tests-image bash
```

### 4. Run INDIGO CDMI server

Note that at this point we are already operating in bash session which is running inside docker container. What is important, we run the CDMI server in background and we redirect standard error and standard output channels to file. It will allow us to type further commands in bash session.

```
./run.sh >> /tmp/cdmi.log 2>&1 &
```

It is good idea to wait here for a few seconds, just to let the server to finish start-up procedure (30 sec. should be enough).
To be sure that CDMI server is ready, you can check if it already listen on port 8080:

```
netstat -nltp 
```

If you will see output similar to this one:

```
Active Internet connections (only servers)
Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name
tcp        0      0 0.0.0.0:63799           0.0.0.0:*               LISTEN      54/redis-server-2.8
tcp6       0      0 :::8080                 :::*                    LISTEN      31/java
tcp6       0      0 :::63799                :::*                    LISTEN      54/redis-server-2.8
```

it means that INDIGO CDMI server has started.


### 5. Create related CDMI containers and dataobjects

Here, using REST API of INDIGO CDMI server, we are creating relevant containers and dataobjects. It is not main test yes. For now we are only preparing appropriate objects in appropriate locations. Please consider these commands as test fixture establishment.

```
curl -s -X PUT http://restadmin:restadmin@localhost:8080/standard -H "Content-Type: application/cdmi-container" -d '{}'

curl -s -X PUT http://restadmin:restadmin@localhost:8080/silver -H "Content-Type: application/cdmi-container" -d '{}'

curl -s -X PUT http://restadmin:restadmin@localhost:8080/golden -H "Content-Type: application/cdmi-container" -d '{}'

curl -s -X PUT http://restadmin:restadmin@localhost:8080/standard/file1.txt -H "Content-Type: application/cdmi-object" -d '{"value" : "file1.txt dummy content"}'

curl -s -X PUT http://restadmin:restadmin@localhost:8080/silver/file2.txt -H "Content-Type: application/cdmi-object" -d '{"value" : "file2.txt dummy content"}'

curl -s -X PUT http://restadmin:restadmin@localhost:8080/golden/file3.txt -H "Content-Type: application/cdmi-object" -d '{"value" : "file3.txt dummy content"}'
```

### 6. Discover all QoS profiles exposed by cdmi-s3-qos module

Here actual test begins. With help of REST API we are asking the INDIGO CDMI server about QoS profiles exposed by underlying cdmi-s3-qos.

NOTE: From now on, standard output of each curl command will be piped into "python -mjson.tool" command. It is only in order to format the standard output in a way easier received by human being. 

First ask server for list of all potential QoS profiles of dataobjects:

```
curl -s -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/dataobject | python -mjson.tool
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
    "objectID": "0001869F0018E22A38336333343439372D353966332D3430",
    "objectName": "dataobject",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F0018243130613038346530652D303537392D3430",
    "parentURI": "/cdmi_capabilities"
}
```

Except for values of attributes "objectID" and "parentID", all attributes and values returned by previous command should look the same as in the above excerpt.

For further tests, the most important part of the above result is "children" list. This list contains names of supported QoS profiles. When we now names of all profiles, we can ask for details of specific profile.

To ask for properties of DataobjectProfile1 issue the command:

```
curl -s -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/dataobject/DataobjectProfile1 | python -mjson.tool
```

You should obtain output like this:

```
{
    "capabilities": {
        "cdmi_capabilities_exact_inherit": "true",
        "cdmi_capabilities_templates": "true",
        "cdmi_data_redundancy": "true",
        "cdmi_geographic_placement": "true",
        "cdmi_latency": "true"
    },
    "metadata": {
        "cdmi_data_redundancy": "1",
        "cdmi_geographic_placement": [
            "DE"
        ],
        "cdmi_latency": "3000"
    },
    "objectID": "0001869F0018BC6466643237626466372D343835622D3433",
    "objectName": "DataobjectProfile1",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F0018E22A38336333343439372D353966332D3430",
    "parentURI": "/cdmi_capabilities/dataobject"
}
```

This is JSON object which characterizes the DataobjectProfile1. Apart from values of attributes "objectID" and "parentID", all attributes and values revealed by previous command should be the same as in the above excerpt.

Now, let's ask for properties of next QoS profile, that is DataObjectProfile2:

```
curl -s -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/dataobject/DataobjectProfile2 | python -mjson.tool
```

The output should look like this (as earlier, the values of objectID and parentID attributes can differ):

```
{
    "capabilities": {
        "cdmi_capabilities_exact_inherit": "true",
        "cdmi_capabilities_templates": "true",
        "cdmi_data_redundancy": "true",
        "cdmi_geographic_placement": "true",
        "cdmi_latency": "true"
    },
    "metadata": {
        "cdmi_data_redundancy": "2",
        "cdmi_geographic_placement": [
            "PL",
            "UK"
        ],
        "cdmi_latency": "2000"
    },
    "objectID": "0001869F0018779F61386138323237362D666235612D3466",
    "objectName": "DataobjectProfile2",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F0018E22A38336333343439372D353966332D3430",
    "parentURI": "/cdmi_capabilities/dataobject"
}
```

In similar way, let's ask for properties of DataobjectProfile2:

```
curl -s -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/dataobject/DataobjectProfile3 | python -mjson.tool
```

Still having in mind that values of objectID and parentID attributes can differ, the output of this command should be like this:

```
{
    "capabilities": {
        "cdmi_capabilities_exact_inherit": "true",
        "cdmi_capabilities_templates": "true",
        "cdmi_data_redundancy": "true",
        "cdmi_geographic_placement": "true",
        "cdmi_latency": "true"
    },
    "metadata": {
        "cdmi_data_redundancy": "3",
        "cdmi_geographic_placement": [
            "NL",
            "ES"
        ],
        "cdmi_latency": "500"
    },
    "objectID": "0001869F0018916036666138653638642D343631382D3431",
    "objectName": "DataobjectProfile3",
    "objectType": "application/cdmi-capability",
    "parentID": "0001869F0018E22A38336333343439372D353966332D3430",
    "parentURI": "/cdmi_capabilities/dataobject"
}
```

### 7. Asking for QoS properties (or we can say profiles) assigned to actual objects stored in INDIGO CDMI server.

In first steps of this testing procedure, we have created some exemplary CDMI containers and data objects. Now we will ask the INDIGO CDMI server for QoS profiles assigned to this entities.

First ask for QoS profile of "/standard/file1.txt" data object

```
curl -s -X GET http://restadmin:restadmin@localhost:8080/standard/file1.txt -H "Content-Type: application/cdmi-object" | python -mjson.tool
```

Apart from values of attributes "objectID" and "parentID" the result should look like this: 

```
{
    "capabilitiesURI": "/cdmi_capabilities/dataobject/DataobjectProfile1",
    "completionStatus": "Complete",
    "domainURI": "/cdmi_domains",
    "metadata": {
        "cdmi_data_redundancy_provided": "1",
        "cdmi_geographic_placement_provided": [
            "DE"
        ],
        "cdmi_latency_provided": "1000"
    },
    "mimetype": "application/octet-stream",
    "objectID": "0001869F0018846935663733646536662D656562342D3462",
    "objectName": "file1.txt",
    "objectType": "application/cdmi-object",
    "parentID": "0001869F0018E56F63396235383762392D386634352D3436",
    "parentURI": "/standard"
}
```

In short, the above ansver points out that the profile assigned with the being examined object is "DataobjectProfile1" (we can tell it from "capabilitiesURI" attribute). Additionally the properties of this profile are presented as well.


In similar way we can ask for QoS profile of "/silver/file2.txt" data object:

```
curl -s -X GET http://restadmin:restadmin@localhost:8080/silver/file2.txt -H "Content-Type: application/cdmi-object"  | python -mjson.tool
```

The answer should be like this:

```
{
    "capabilitiesURI": "/cdmi_capabilities/dataobject/DataobjectProfile2",
    "completionStatus": "Complete",
    "domainURI": "/cdmi_domains",
    "metadata": {
        "cdmi_data_redundancy_provided": "2",
        "cdmi_geographic_placement_provided": [
            "PL",
            "UK"
        ],
        "cdmi_latency_provided": "2000"
    },
    "mimetype": "application/octet-stream",
    "objectID": "0001869F00188A6530316531346134392D353931312D3464",
    "objectName": "file2.txt",
    "objectType": "application/cdmi-object",
    "parentID": "0001869F001869D463646465383733392D643265362D3462",
    "parentURI": "/silver"
}
```

This time, we see that the QoS profile assigned to "/silver/file2.txt" data object is DataobjectProfile2.
Again, the only parts of the above excerpt which can differ from those obtained during tests, are values of "objectID" and "parentID" attributes.


And finally, let's check QoS profile of the third data object "/golder/file3.txt":

```
curl -s -X GET http://restadmin:restadmin@localhost:8080/golden/file3.txt -H "Content-Type: application/cdmi-object" | python -mjson.tool
```

The output should look like this:

```
{
    "capabilitiesURI": "/cdmi_capabilities/dataobject/DataobjectProfile3",
    "completionStatus": "Complete",
    "domainURI": "/cdmi_domains",
    "metadata": {
        "cdmi_data_redundancy_provided": "3",
        "cdmi_geographic_placement_provided": [
            "NL",
            "ES"
        ],
        "cdmi_latency_provided": "3000"
    },
    "mimetype": "application/octet-stream",
    "objectID": "0001869F0018E99666393262633863382D316139322D3437",
    "objectName": "file3.txt",
    "objectType": "application/cdmi-object",
    "parentID": "0001869F0018C02631353562306564362D306333662D3431",
    "parentURI": "/golden"
}
```

This time, the output reveals that the QoS profile assigned to "/golden/file3.txt" is DataobjectProfile3.  
The disclaimer related to attributes "objectID" and "parentID" is still valid.

NNOTE: In theory cdmi-spi interface, which is implemented by cdmi-s3-qos module, provides for changing QoS profiles assigned to data objects. However, this feature is to be implemented only by back-ends which can support such kind a functionality. Actually, in case of cdmi-s3-qos module this functionality is not provided due to back-end imposed limitations. 

 ### 8. Clean up after tests
 
 At the end we should exit the bash session and wipe the docker container and image used during tests.
 
 So, first exit the bash session:
 
 ```
 exit
 ```
 
 And delete docker container and image respectively:
 
 ```
 docker rm integration-tests-container
 docker rmi integration-tests-image
 ```
 