#!/bin/bash

VERSION=0.0.1
NAME=cdmi-s3-qos

#
# compile and install cdmi-spi
#
git clone https://github.com/indigo-dc/cdmi-spi.git
cd cdmi-spi
git checkout b4817ed
mvn clean install


#
# compile and install cdmi-s3-qos (as a library)
#
cd ..
mvn clean install

#
# clone, configure (to use cdmi-s3-qos) and package CDMI server
#
git clone https://github.com/indigo-dc/CDMI.git
cd CDMI
git checkout 326eec3
cd ..
cp -rf config CDMI/
rm -f CDMI/config/objectstore.properties
cd CDMI

sed -i 's/dummy_filesystem/radosgw/g' config/application.yml
sed -i 's/<dependencies>/<dependencies>\r\n<dependency>\r\n<groupId>pl.psnc<\/groupId>\r\n<artifactId>cdmi-s3-qos<\/artifactId>\r\n<version>0.0.1-SNAPSHOT<\/version>\r\n<\/dependency>/g' pom.xml
mvn clean package -Dmaven.test.skip=true

cp target/cdmi-server-0.1-SNAPSHOT.jar target/$NAME-${VERSION}-SNAPSHOT.jar


cd ..

mkdir -p debian/var/lib/$NAME/config/
cp -r config/fixed-mode debian/var/lib/$NAME/config/
cp CDMI/target/$NAME-${VERSION}-SNAPSHOT.jar debian/var/lib/$NAME/
cp CDMI/config/* debian/var/lib/$NAME/config/

chmod 0775 debian/DEBIAN/postinst

dpkg --build debian

mv debian.deb $NAME-${VERSION}.deb
