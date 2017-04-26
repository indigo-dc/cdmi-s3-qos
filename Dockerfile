#
# 
#
FROM ubuntu:14.04

MAINTAINER gracjan@man.poznan.pl

COPY . /cdmi-s3-qos

RUN echo "deb http://ppa.launchpad.net/openjdk-r/ppa/ubuntu trusty main" >> /etc/apt/sources.list && \
	echo "deb-src http://ppa.launchpad.net/openjdk-r/ppa/ubuntu trusty main" >> /etc/apt/sources.list && \
	apt-get update && \
	apt-get --force-yes install -y openjdk-8-jdk && \
	apt-get install -y maven git curl redis-tools httpie && \
	cd cdmi-s3-qos && \
	mvn install && \
	cd .. && \
	git clone https://github.com/indigo-dc/CDMI.git && \
	cp -rf cdmi-s3-qos/config CDMI/ && \
	rm -f CDMI/config/objectstore.properties && \
	cd CDMI && \
	git checkout c6c1178 && \
	sed -i 's/dummy_filesystem/radosgw/g' config/application.yml && \
	sed -i 's/active: redis/active: redis-embedded/g' config/application.yml && \
	sed -i 's/<dependencies>/<dependencies>\r\n<dependency>\r\n<groupId>pl.psnc<\/groupId>\r\n<artifactId>cdmi-s3-qos<\/artifactId>\r\n<version>2.0.0-SNAPSHOT<\/version>\r\n<\/dependency>/g' pom.xml && \
	echo "java -Djava.security.egd=file:/dev/./urandom -jar target/cdmi-server-1.2.jar  --server.port=8080" > run.sh && \
	chmod +x run.sh && \
	mvn package -Dmaven.test.skip=true

WORKDIR /CDMI

CMD java -Djava.security.egd=file:/dev/./urandom -jar target/cdmi-server-1.1.jar --server.port=8080
#ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar target/cdmi-server-1.1.jar --server.port=8080
