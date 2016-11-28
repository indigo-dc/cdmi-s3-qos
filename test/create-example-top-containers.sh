#!/bin/bash

#
# The bellow commands create top level containers of names corresponding to the bucket names in RADOS GW
# The bellow command are to be executed against CDMI server which listens on port 8888. In testing environment
# it most often will be port exposed by docker container. 
#
curl -X PUT http://localhost:8888/default -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-container" -d '{}'
curl -X PUT http://localhost:8888/standard -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-container" -d '{}'
curl -X PUT http://localhost:8888/silver -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-container" -d '{}'
curl -X PUT http://localhost:8888/golden -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-container" -d '{}'
