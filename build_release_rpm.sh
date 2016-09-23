#!/bin/bash

NAME=cdmi-s3-qos
TOPDIR=`pwd`/rpm
CDMI_SPI_COMMIT=b4817ed
CDMI_COMMIT=326eec3

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
git clone https://github.com/indigo-dc/CDMI.git
cd CDMI
git checkout $CDMI_COMMIT
cd ..
cp -rf config CDMI/
rm -f CDMI/config/objectstore.properties
cd CDMI

sed -i 's/dummy_filesystem/radosgw/g' config/application.yml
sed -i 's/<dependencies>/<dependencies>\r\n<dependency>\r\n<groupId>pl.psnc<\/groupId>\r\n<artifactId>cdmi-s3-qos<\/artifactId>\r\n<version>0.0.1-SNAPSHOT<\/version>\r\n<\/dependency>/g' pom.xml
mvn clean package -Dmaven.test.skip=true

#
# create TOP dir structure
#
mkdir -p $TOPDIR
mkdir -p $TOPDIR/SOURCES
mkdir -p $TOPDIR/SPECS

#
# determine version of CDMI server
#
CDMI_JAR_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -v " " )
CDMI_JAR_VERSION_ERR=$?

CDMI_VERSION=$(echo $CDMI_JAR_VERSION | grep -o "[0-9.]*")

SERVICE_VERSION=${QOS_VERSION}cdmi${CDMI_VERSION}


#
# set final name of cdmi server with included cdmi-s3-module
#
cp -f target/cdmi-server-${CDMI_JAR_VERSION}.jar target/$NAME-${SERVICE_VERSION}.jar

# bellow line seems to be redundant (probably will be removed)
cp -f target/$NAME-$SERVICE_VERSION.jar $TOPDIR/SOURCES

cd ..

mkdir -p $TOPDIR/SOURCES/var/lib/$NAME/config/
cp -fr config/fixed-mode $TOPDIR/SOURCES/var/lib/$NAME/config/

cp CDMI/target/$NAME-${SERVICE_VERSION}.jar $TOPDIR/SOURCES/var/lib/$NAME/

cp -f CDMI/config/* $TOPDIR/SOURCES/var/lib/$NAME/config/

#
# prepare files and folders required by rpmbuild 
#

#rpm/SOURCES/cdmi-s3-qos.service
sed "s/@SERVICE_VERSION@/$SERVICE_VERSION/g" templates/rpm/SOURCES/cdmi-s3-qos.service > rpm/SOURCES/cdmi-s3-qos.service 

#rpm/SPECS/cdmi-s3-qos.spec
sed "s/@SERVICE_VERSION@/$SERVICE_VERSION/g" templates/rpm/SPECS/cdmi-s3-qos.spec > rpm/SPECS/cdmi-s3-qos.spec 

rpmbuild --define "_topdir ${TOPDIR}" -ba $TOPDIR/SPECS/$NAME.spec

cp ${TOPDIR}/RPMS/x86_64/cdmi-s3-qos-$SERVICE_VERSION-1.el7.centos.x86_64.rpm .
