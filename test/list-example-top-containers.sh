#!/bin/bash


#
# The bellow commands examine profiles associated with containers exposed by RADOS GW
# The bellow command are to be executed against CDMI server which listens on port 8888. In testing environment
# it most often will be port exposed by docker container. 
#

#
# get all defined profiles
#
curl -X GET http://localhost:8888/cdmi_capabilities/container -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-capability"

#
# get properties of individual profiles
#
curl -X GET http://localhost:8888/cdmi_capabilities/container/default -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-capability"

curl -X GET http://localhost:8888/cdmi_capabilities/container/standard -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-capability"

curl -X GET http://localhost:8888/cdmi_capabilities/container/silver -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-capability"

curl -X GET http://localhost:8888/cdmi_capabilities/container/golden -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-capability"

#
# check profiles assigned to individual containers
#
curl -X GET http://localhost:8888/default -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-object"

curl -X GET http://localhost:8888/standard -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-object"

curl -X GET http://localhost:8888/silver -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-object"

curl -X GET http://localhost:8888/golden -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-object"

