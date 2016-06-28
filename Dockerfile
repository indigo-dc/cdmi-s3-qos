FROM ubuntu:14.04

MAINTAINER gracjan@man.poznan.pl

RUN echo "deb http://ppa.launchpad.net/openjdk-r/ppa/ubuntu trusty main" >> /etc/apt/sources.list && \
	echo "deb-src http://ppa.launchpad.net/openjdk-r/ppa/ubuntu trusty main" >> /etc/apt/sources.list && \
	apt-get update && \
	apt-get --force-yes install -y openjdk-8-jdk && \
	apt-get install -y maven git curl && \
	git clone https://github.com/indigo-dc/cdmi-spi.git && \
	cd cdmi-spi && \
	mvn install && \
	cd .. && \
	git clone https://github.com/indigo-dc/cdmi-s3-qos.git && \
	cd cdmi-s3-qos && \
	mvn install && \
	cd .. && \
	git clone https://github.com/indigo-dc/CDMI.git && \
	cp -rf cdmi-s3-qos/conf CDMI/ && \
	cd CDMI && \
	git checkout b7f3304 && \
	sed -i 's/cdmi.qos.backend.type: dummy_filesystem/cdmi.qos.backend.type: radosgw/g' src/main/resources/application.properties && \
	sed -i 's/<dependencies>/<dependencies>\r\n<dependency>\r\n<groupId>pl.psnc<\/groupId>\r\n<artifactId>cdmi-s3-qos<\/artifactId>\r\n<version>0.0.1-SNAPSHOT<\/version>\r\n<\/dependency>/g' pom.xml && \
	mvn package -Dmaven.test.skip=true && \
	(java -jar target/cdmi-server-0.1-SNAPSHOT.jar &) && \
	sleep 20 && \
	curl -X PUT http://localhost:8080/standard -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-container" -d '{}' && \
	curl -X PUT http://localhost:8080/silver -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-container" -d '{}' && \
	curl -X PUT http://localhost:8080/gold -H "Authorization: Basic cmVzdGFkbWluOnJlc3RhZG1pbg==" -H "Content-Type: application/cdmi-container" -d '{}' && \
	killall java

WORKDIR /CDMI

ENTRYPOINT java -jar target/cdmi-server-0.1-SNAPSHOT.jar
#CMD java -jar target/cdmi-server-0.1-SNAPSHOT.jar
