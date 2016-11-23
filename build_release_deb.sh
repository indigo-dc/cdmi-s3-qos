#!/bin/bash

#
# read commits or branches names which are to be used in packaging process
#

. ./PACKAGING_COMMITS

NAME=cdmi-s3-qos

QOS_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -v " " | grep -o "[0-9.]*" )
QOS_VERSION_ERR=$?

if [ $QOS_VERSION_ERR -ne 0 ] ||  [ "x$QOS_VERSION" == "x" ]; then
echo "ERROR: Could not determine the version of maven project."
echo "Please, check by hand if this command succeed:"
echo "mvn help:evaluate -Dexpression=project.version | grep -v \" \" | grep -o \"[0-9.]*\""
exit 1
fi



#
# compile and install cdmi-spi
#
rm -rf cdmi-spi
git clone https://github.com/indigo-dc/cdmi-spi.git
cd cdmi-spi
git checkout $CDMI_SPI_COMMIT
mvn clean install


#
# compile and install cdmi-s3-qos (as a library)
#
cd ..
mvn clean install

#
# clone, configure (to use cdmi-s3-qos) and package CDMI server
#
rm -rf CDMI
git clone https://github.com/indigo-dc/CDMI.git
cd CDMI
git checkout $CDMI_COMMIT
cd ..
cp -rf config CDMI/
rm -f CDMI/config/objectstore.properties
cd CDMI

sed -i 's/dummy_filesystem/radosgw/g' config/application.yml
sed -i 's/active: redis/active: redis-embedded/g' config/application.yml
sed -i 's/<dependencies>/<dependencies>\r\n<dependency>\r\n<groupId>pl.psnc<\/groupId>\r\n<artifactId>cdmi-s3-qos<\/artifactId>\r\n<version>0.0.1-SNAPSHOT<\/version>\r\n<\/dependency>/g' pom.xml
mvn clean package -Dmaven.test.skip=true

#
# determine version of CDMI server
#
CDMI_JAR_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -v " " )
CDMI_JAR_VERSION_ERR=$?

CDMI_VERSION=$(echo $CDMI_JAR_VERSION | grep -o "[0-9.]*")

SERVICE_VERSION=${QOS_VERSION}-cdmi${CDMI_VERSION}

#
# copy cdmi server to its final name (it is cdmi server with included cdmi-s3-qos module)
#
cp -f target/cdmi-server-$CDMI_JAR_VERSION.jar target/$NAME-${SERVICE_VERSION}.jar



cd ..



mkdir -p debian/var/lib/$NAME/config/
cp -fr config/fixed-mode debian/var/lib/$NAME/config/
cp -f  CDMI/target/$NAME-${SERVICE_VERSION}.jar debian/var/lib/$NAME/
cp -f CDMI/config/* debian/var/lib/$NAME/config/


#
# prepare files and folders required by dpkg --build 
#

#debian/DEBIAN
mkdir -p debian/DEBIAN

#debian/etc/systemd/system
mkdir -p debian/etc/systemd/system

#debian/DEBIAN/control
sed "s/@SERVICE_VERSION@/$SERVICE_VERSION/g" templates/debian/DEBIAN/control > debian/DEBIAN/control 

#debian/DEBIAN/postinst
sed "s/@SERVICE_VERSION@/$SERVICE_VERSION/g" templates/debian/DEBIAN/postinst > debian/DEBIAN/postinst
chmod 0775 debian/DEBIAN/postinst

#debian/etc/systemd/system/cdmi-s3-qos.service
sed "s/@SERVICE_VERSION@/$SERVICE_VERSION/g" templates/debian/etc/systemd/system/cdmi-s3-qos.service > debian/etc/systemd/system/cdmi-s3-qos.service

#
# build package
#
dpkg --build debian

#
# set final name for the package
#
mv debian.deb $NAME-${SERVICE_VERSION}.deb

