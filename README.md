# WORK IN PROGRESS


Testing procedure:

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

curl -X GET http://localhost:8080/gold -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-object"

docker stop cdmi-s3-qos-container
```