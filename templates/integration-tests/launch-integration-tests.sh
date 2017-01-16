#!/bin/bash

## 1. Run CDMI server with cdmi-s3-qos module
echo "Launching the CDMI server."
./run.sh >> /tmp/cdmi.log 2>&1 &

## 2. give server some time to start-up
echo "Waiting 60 sec. to allow the server to start-up."
sleep 60


## 3. Set up tests fixtures

echo "TESTS FIXTURE SET-UP"
echo "Creating containers and dataobjects inside CDMI server. "
echo "cdmi-s3-qos module will later provide information about QoS profiles related with these containers and dataobjects"

echo ""
echo "Create standard container"
curl -X PUT http://restadmin:restadmin@localhost:8080/standard -H "Content-Type: application/cdmi-container" -d '{}'

echo ""
echo "Create silver container"
curl -X PUT http://restadmin:restadmin@localhost:8080/silver -H "Content-Type: application/cdmi-container" -d '{}'

echo ""
echo "Create golden container"
curl -X PUT http://restadmin:restadmin@localhost:8080/golden -H "Content-Type: application/cdmi-container" -d '{}'

echo ""
echo "Create standard/file1.txt dataobject"
curl -X PUT http://restadmin:restadmin@localhost:8080/standard/file1.txt -H "Content-Type: application/cdmi-object" -d '{"value" : "file1.txt dummy content"}'

echo ""
echo "Create silver/file2.txt dataobject"
curl -X PUT http://restadmin:restadmin@localhost:8080/silver/file2.txt -H "Content-Type: application/cdmi-object" -d '{"value" : "file2.txt dummy content"}'

echo ""
echo "Create golden/file3.txt dataobject"
curl -X PUT http://restadmin:restadmin@localhost:8080/golden/file3.txt -H "Content-Type: application/cdmi-object" -d '{"value" : "file3.txt dummy content"}'

## 4. Ask for all supported QoS profiles

echo "Asking for supported types of objects"
curl -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/


## 5. From the above output, get values from children list ("children":["container","dataobject"]) and ask for profiles supported by these children:

echo "Asking for profiles related with containers"
curl -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/container

echo "Asking for profiles related with dataobjects"
curl -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/dataobject


## 6. Ask for QoS of above created containers
echo ""
echo "Asking for QoS capabilities of standard container"
curl -X GET http://restadmin:restadmin@localhost:8080/standard -H "Content-Type: application/cdmi-container"

echo ""
echo "Asking for QoS capabilities of silver container"
curl -X GET http://restadmin:restadmin@localhost:8080/silver -H "Content-Type: application/cdmi-container"

echo ""
echo "Asking for QoS capabilities of golden container"
curl -X GET http://restadmin:restadmin@localhost:8080/golden -H "Content-Type: application/cdmi-container"


## 7. Ask for QoS of above created dataobjects

echo ""
echo "Asking for QoS capabilities of standard/file1.txt dataobject"
curl -X GET http://restadmin:restadmin@localhost:8080/standard/file1.txt -H "Content-Type: application/cdmi-object"

echo ""
echo "Asking for QoS capabilities of silver/file2.txt dataobject"
curl -X GET http://restadmin:restadmin@localhost:8080/silver/file2.txt -H "Content-Type: application/cdmi-object"

echo ""
echo "Asking for QoS capabilities of golden/file3.txt dataobject"
curl -X GET http://restadmin:restadmin@localhost:8080/golden/file3.txt -H "Content-Type: application/cdmi-object"
