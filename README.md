# WORK IN PROGRESS

## Short description

This project provides service which adheres to [**cdmi-spi**](https://github.com/indigo-dc/cdmi-spi) defined interface which is meant to be used within [**INDIGO CDMI**](https://github.com/indigo-dc/cdmi) server.

The service aims at integrating RADOS based S3 object storage with QoS management layer of [INDIGO CDMI](https://github.com/indigo-dc/cdmi) server. It can be configured to operate either in *fixed-mode* or *life-mode*.

In the *fixed-mode*, the service exposes QoS attributes defined in configuration files, while in the *life-mode*, the service communicates with RADOS gateway and provides the most recent attributes on the fly.  

The *life-mode* assumes that there is [**cdmi-s3-qos-ceph-provider**](https://github.com/indigo-dc/cdmi-s3-qos-ceph-provider) installed on the RADOS gateway.

## Compilation and installation

### Requirements

Tools required to compile the project:

* [git](https://git-scm.com/) (optionally, if you want to follow the bellow procedures literally)
* JDK 1.8+
* [Maven 3+](https://maven.apache.org/)

### Dependencies

The service, to be compiled depends on:

* [cdmi-spi](https://github.com/indigo-dc/cdmi-spi)

### Compilation workflow

As [cdmi-spi](https://github.com/indigo-dc/cdmi-spi) is not available thorough any public packages repository, it has to be cloned, compiled and installed locally:

```
git clone https://github.com/indigo-dc/cdmi-spi.git
cd cdmi-spi
mvn install
```

Next, the actual service can be cloned and compiled / installed:

```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git
cd cdmi-s3-qos
mvn install
```

## Usage

The outcome artifact of this project is **cdmi-s3-qos-<VERSION>.jar** file. This file has to be seen by INDIGO CDMI server's class loader. As INDIGO CDMI server leverages the boot spring technology and is packaged into single, standalone jar file, the easiest way to provide server with the service is to define adequate dependencies in server's pom.xml file and re-package the server.

```xml
	<dependency>
		<groupId>pl.psnc</groupId>
		<artifactId>cdmi-s3-qos</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
```

## Configuration

### INDIGO CDMI server configuration

In order to use the cdmi-s3-qos service, the INDIGO CDMI server has to be configured. The server's **cdmi.qos.backend.type** configuration parameter has to be set to **radosgw**:

```
cdmi.qos.backend.type: radosgw
```

### Service configuration

The cdmi-s3-qos service can be configured through configuration file

`conf/objectstore.properties`

which has to be placed within INDIGO CDMI server's working directory. Subsequent sections describe the available parameters.


### Fixed-mode

The *fixed-mode* is the default one. To enable this mode explicitly set the following parameter in `conf/objectstore.properties` file:

```
objectstore.backend-gateway=org.indigo.cdmi.backend.radosgw.FixedModeBackendGateway
``` 

In *fixed-mode* all QoS profiles (which denote particular QoS classes) and their attributes are defined in JSON file **conf/fixed-mode/all-profiles.json** (in INDIGO CDMI server's working directory). Example JSON file can look like that:

```json
[
{
	"name":"Profile1", 
	"type":"container", 
	"metadata":{
		"cdmi_latency":"1000", 
		"cdmi_data_redundancy":"1",
		"cdmi_geographic_placement": ["DE", "FR"]
	},
	"metadata_provided": {
		"cdmi_latency_provided":"1000", 
		"cdmi_data_redundancy_provided":"1",
		"cdmi_geographic_placement_provided": ["DE", "FR"]	
	}, 
	"allowed_profiles":["Profile2"]
},
{
	"name":"Profile2", 
	"type":"container", 
	"metadata": {
		"cdmi_latency":"2000",
		"cdmi_data_redundancy":"2",
		"cdmi_geographic_placement": ["PL", "UK"]
	},
	"metadata_provided":{
		"cdmi_latency_provided":"2000",
		"cdmi_data_redundancy_provided":"2",
		"cdmi_geographic_placement_provided": ["PL", "UK"]	
	}, 
	"allowed_profiles":["Profile1"]
}
]
```

Mappings between S3 buckets and QoS profiles provided by those buckets are defined in JSON file **conf/fixed-mode/buckets-profiles.json** (in INDIGO CDMI server's working directory). Example mapping can look like that:

```json
{
	"silver":"Profile1",
	"golden":"Profile2",
}
```

The above says that bucket of name *silver* provides QoS class defined by profile of name *Profile1*, and the bucket of name *golden* provides QoS class defined by profile of name *Profile2*. 

### Life-mode

Before employing *life-mode*, make sure that the [**cdmi-s3-qos-ceph-provider**](https://github.com/indigo-dc/cdmi-s3-qos-ceph-provider) is installed on RADOS server.

To enable INDIGO CDMI server to use cdmi-s3-qos service in *life-mode*, the following parameter in `conf/objectstore.properties` (countig form server's working directory) file has to be set:

```
objectstore.backend-gateway=org.indigo.cdmi.backend.radosgw.LifeModeBackendGateway
``` 

In *life-mode* the cdmi-s3-qos service uses **SSH** protocol to enquiry the RADOS gateway about available QoS profiles and about QoS classed of individual buckets.  

The credentials required to use SSH protocol are to be set in `conf/objectstore.properties` file:

```
objectstore.ssh-gateway.host=<RADOS gateway address>
objectstore.ssh-gateway.port=<SSH DAEMON PORT, DEFAULT IS 22>
objectstore.ssh-gateway.user=<USER_NAME>
objectstore.ssh-gateway.password=<USER_PASSWORD>
```

Note that the RADOS server has to expose SSH protocol and the user of name <USER_NAME> and with password <USER_PASSWORD> has to be set-up over there.

The [**cdmi-s3-qos-ceph-provider**](https://github.com/indigo-dc/cdmi-s3-qos-ceph-provider) shuld be installed on RADOS server and should provide executables called by cdmi-s3-qos service. The paths of these executables can be configured in `conf/objectstore.properties` file:

```
objectstore.ssh-gateway.get-profiles-command=/usr/bin/python3 /opt/cdmi-s3-qos-ceph-provider/cdmi-s3-qos-ceph-provider/capabilities_provider.py --all
objectstore.ssh-gateway.get-bucket-profile-command=/usr/bin/python3 /opt/cdmi-s3-qos-ceph-provider/cdmi-s3-qos-ceph-provider/capabilities_provider.py --bucket 
 ```

Actually, the above paths are the default ones which are used if there the is no other configuration.

## Quick path testing procedure

Clone codes, prepare docker image, run it, connect to it and stop the container: 

```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git

cd cdmi-s3-qos

docker build -t cdmi-s3-qos-image .

docker run -d -p 8080:8080 --name cdmi-s3-qos-container cdmi-s3-qos-image

curl -X GET http://localhost:8080/cdmi_capabilities/container -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-capability"

curl -X GET http://localhost:8080/cdmi_capabilities/container/Profile1 -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-capability"

curl -X GET http://localhost:8080/cdmi_capabilities/container/Profile2 -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-capability"

curl -X GET http://localhost:8080/cdmi_capabilities/container/Profile3 -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-capability"

curl -X GET http://localhost:8080/standard -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-object"

curl -X GET http://localhost:8080/silver -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-object"

curl -X GET http://localhost:8080/golden -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-object"

docker stop cdmi-s3-qos-container
```

Clean-up docker container and image:

```
docker rm -f cdmi-s3-qos-container
docker rmi -f cdmi-s3-qos-image
```
