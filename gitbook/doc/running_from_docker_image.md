# Running from docker image

By definition, the cdmi-s3-qos is not meant to be executed on its own. It doesn't constitute a standalone product, it has to be used by cdmi-qos server.

However, to facilitate quick path module testing, the source codes come with Docker and docker-compose configuration files which can be used to launch pre-configured testing environment. The testing environment consists of two Docker containers. One container provides cdmi-qos server which is pre-configured to use cdmi-s3-qos module. Second container runs minio server which provides S3 compatible API meant to mimic presence of RADOS gateway.

**NOTE:** By default, container with cdmi-qos server will try to listen on *TCP* port number *8080*. If on your machine this port is not eligible then before you will follow the below procedure please set and export `INDIGO_CDMI_PORT` environment variable to value indicating the *TCP* port number to be exposed by container which hosts cdmi-qos server.

For example, to use TCP port number *8888* the `INDIGO_CDMI_PORT` environment variable should be set and exported like this:

```
export INDIGO_CDMI_PORT=8888
```

To build and run the docker based testing environment follow this commands:

```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git
cd cdmi-s3-qos
docker-compose -f docker/docker-compose-integration.yml up -d
```

To begin interaction with such launched environment you can use `curl` command and *Basic Auth* auhentication method (user and password are just “restadmin”). For example to list supported data-object’s QoS profiles type:

```
curl -s -X GET http://restadmin:restadmin@localhost:${INDIGO_CDMI_PORT:-8080}/cdmi_capabilities/dataobject -H "Content-Type: application/cdmi-capability"
``` 

To clean-up the testing environment (remove containers and images) run command:

```
docker-compose -f docker/docker-compose-integration.yml down --rmi all
``` 


