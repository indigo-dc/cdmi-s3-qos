#!/bin/bash

## 1. Run CDMI server with cdmi-s3-qos module
echo "Launching the CDMI server."
./run.sh >> /tmp/cdmi.log 2>&1 &

## 2. give server some time to start-up
echo "Waiting 30 sec. to allow the INDIGO CDMI server to start-up."
sleep 30


## 3. Set up tests fixtures

echo "TESTS FIXTURE SET-UP"
echo "Creating containers and dataobjects inside CDMI server. "
echo "cdmi-s3-qos module will later provide information about QoS profiles related with these containers and dataobjects"


echo ""
echo "Create standard container"
curl -s -X PUT http://restadmin:restadmin@localhost:8080/standard -H "Content-Type: application/cdmi-container" -d '{}'  | python -mjson.tool

echo ""
echo "Create silver container"
curl -s -X PUT http://restadmin:restadmin@localhost:8080/silver -H "Content-Type: application/cdmi-container" -d '{}' | python -mjson.tool

echo ""
echo "Create golden container"
curl -s -X PUT http://restadmin:restadmin@localhost:8080/golden -H "Content-Type: application/cdmi-container" -d '{}' | python -mjson.tool

echo ""
echo "Create standard/file1.txt dataobject"
curl -s -X PUT http://restadmin:restadmin@localhost:8080/standard/file1.txt -H "Content-Type: application/cdmi-object" -d '{"value" : "file1.txt dummy content"}'  | python -mjson.tool

echo ""
echo "Create silver/file2.txt dataobject"
curl -s -X PUT http://restadmin:restadmin@localhost:8080/silver/file2.txt -H "Content-Type: application/cdmi-object" -d '{"value" : "file2.txt dummy content"}'  | python -mjson.tool

echo ""
echo "Create golden/file3.txt dataobject"
curl -s -X PUT http://restadmin:restadmin@localhost:8080/golden/file3.txt -H "Content-Type: application/cdmi-object" -d '{"value" : "file3.txt dummy content"}'  | python -mjson.tool

## 4. Ask for all supported QoS profiles

echo "Asking for supported types of objects"
curl -s -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/  | python -mjson.tool


## 5. From the above output, get values from children list ("children":["container","dataobject"]) and ask for profiles supported by these children:

echo "Asking for profiles related with dataobjects"
curl -s -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/dataobject | python -mjson.tool

echo "Asking for DataobjectProfile1's profile properties"
curl -s -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/dataobject/DataobjectProfile1 | python -mjson.tool

echo "Asking for DataobjectProfile2's profile properties"
curl -s -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/dataobject/DataobjectProfile2 | python -mjson.tool

echo "Asking for DataobjectProfile3's profile properties"
curl -s -X GET http://restadmin:restadmin@localhost:8080/cdmi_capabilities/dataobject/DataobjectProfile3 | python -mjson.tool


## 6. Ask for QoS of above created containers
echo ""
echo "Asking for QoS capabilities of standard container"
curl -s -X GET http://restadmin:restadmin@localhost:8080/standard -H "Content-Type: application/cdmi-container" | python -mjson.tool

echo ""
echo "Asking for QoS capabilities of silver container"
curl -s -X GET http://restadmin:restadmin@localhost:8080/silver -H "Content-Type: application/cdmi-container" | python -mjson.tool

echo ""
echo "Asking for QoS capabilities of golden container"
curl -s -X GET http://restadmin:restadmin@localhost:8080/golden -H "Content-Type: application/cdmi-container" | python -mjson.tool


## 7. Ask for QoS of above created dataobjects

echo ""
echo "Asking for QoS capabilities of standard/file1.txt dataobject"
curl -s -X GET http://restadmin:restadmin@localhost:8080/standard/file1.txt -H "Content-Type: application/cdmi-object" | python -mjson.tool

echo ""
echo "Asking for QoS capabilities of silver/file2.txt dataobject"
curl -s -X GET http://restadmin:restadmin@localhost:8080/silver/file2.txt -H "Content-Type: application/cdmi-object" | python -mjson.tool

echo ""
echo "Asking for QoS capabilities of golden/file3.txt dataobject"
curl -s -X GET http://restadmin:restadmin@localhost:8080/golden/file3.txt -H "Content-Type: application/cdmi-object" | python -mjson.tool
