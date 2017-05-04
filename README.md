## Short description

This project provides module which adheres to [**cdmi-spi**](https://github.com/indigo-dc/cdmi-spi) defined interface. This interface is meant to be implemented by extension modules for [**INDIGO CDMI**](https://github.com/indigo-dc/cdmi) server.

The cdmi-s3-qos module aims at integrating RADOS gateway based S3 object storage with QoS management layer of [**INDIGO CDMI**](https://github.com/indigo-dc/cdmi) server.

## Compilation

### Requirements

Tools required to compile the project:

* [git](https://git-scm.com/) (optionally, if you want to follow the bellow procedures literally)
* JDK 1.8+
* [Maven 3+](https://maven.apache.org/)

### Dependencies

All dependencies are defined in pom.xml file and are accessible through default maven repositories.

### Compilation workflow

To build the jar with the final module follow the commands: 

```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git
cd cdmi-s3-qos
mvn package
```

## Usage

The outcome artifact of this project is **cdmi-s3-qos-\<VERSION\>.jar** file. This file has to be seen by INDIGO CDMI server's class loader. By default INDIGO CDMI server looks for modules in **/usr/lib/cdmi-server/plugins** directory. So, the cdmi-s3-qos module has to be placed in this directory on the node where INDIGO CDMI server is installed.

## Configuration

### INDIGO CDMI server configuration

In order to use the cdmi-s3-qos module, the INDIGO CDMI server has to be properly configured. The server's **cdmi.qos.backend.type** configuration parameter has to be set to **radosgw**:

```
cdmi.qos.backend.type: radosgw
```

More details about INDIGO CDMI server configuration and location of configuration files can be found on this GitBook site: [https://indigo-dc.gitbooks.io/cdmi-qos/content/doc/configuration.html](https://indigo-dc.gitbooks.io/cdmi-qos/content/doc/configuration.html).

### cdmi-s3-qos module configuration

The module configuration is described on GitBook site: [https://www.gitbook.com/book/indigo-dc/cdmi-s3-qos/details](https://www.gitbook.com/book/indigo-dc/cdmi-s3-qos/details).

## Quick path testing procedure

To facilitate quick path module testing, the source codes come with Docker and docker-compose configuration files which can be used to launch pre-configured testing environment. The testing environment consists of two Docker containers. One container provides  CDMI INDIGO server which is pre-configured to use cdmi-s3-qos module. Second container runs minio server which provides S3 compatible API meant to mimic presence of RADOS gateway.    

**NOTE:** By default, container with INDIGO CDMI server will try to listen on TCP port number 8080. If on your machine this port is not eligible then before you will follow the below procedure please set and export `INDIGO_CDMI_PORT` environment variable to value indicating the TCP port number to be exposed by container which hosts INDIGO CDMI server. If you do it then remember to change the port number in all bellow curl commands as well.

For example, to use TCP port number 8888 the `INDIGO_CDMI_PORT` environment variable should be set and exported like this:

```
export INDIGO_CDMI_PORT=8888
```


**NOTE** To improve readability of the output from below curl commands, each curl invocation can be piped to `python -mjson.tool` command.

Example:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/container -H "Content-Type: application/cdmi-capability" | python -mjson.tool
```

To build, run and query the testing environment do this steps:


```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git

cd cdmi-s3-qos

docker-compose -f docker/docker-compose-integration.yml up -d

curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/container -H "Content-Type: application/cdmi-capability"

curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/container/ContainerProfile1  -H "Content-Type: application/cdmi-capability"

curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/container/ContainerProfile2 -H "Content-Type: application/cdmi-capability"

curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/container/ContainerProfile3 -H "Content-Type: application/cdmi-capability"

curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/standard -H "Content-Type: application/cdmi-container"

curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/silver -H "Content-Type: application/cdmi-container"

curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/golden -H "Content-Type: application/cdmi-container"

```

Clean-up docker containers and images:

```
docker-compose -f docker/docker-compose-integration.yml down --rmi all
```
