#!/bin/bash

NAME=cdmi-s3-qos
TOPDIR=`pwd`/rpm


VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -v " " | grep -o "[0-9.]*" )
VERSION_ERR=$?

if [ $VERSION_ERR -ne 0 ] ||  [ "x$VERSION" == "x" ]; then
echo "ERROR: Could not determine the version of maven procjet."
echo "Check by hand if this comman succeed:"
echo "mvn help:evaluate -Dexpression=project.version | grep -v \" \" | grep -o \"[0-9.]*\""
exit 1
fi



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


cp -f target/cdmi-server-0.1-SNAPSHOT.jar target/$NAME-${VERSION}-SNAPSHOT.jar
cp -f target/$NAME-$VERSION-SNAPSHOT.jar $TOPDIR/SOURCES
#cp config/application.yml $TOPDIR/SOURCES

cd ..

mkdir -p $TOPDIR/SOURCES/var/lib/$NAME/config/
cp -fr config/fixed-mode $TOPDIR/SOURCES/var/lib/$NAME/config/

cp CDMI/target/$NAME-${VERSION}-SNAPSHOT.jar $TOPDIR/SOURCES/var/lib/$NAME/

cp -f CDMI/config/* $TOPDIR/SOURCES/var/lib/$NAME/config/



rpmbuild --define "_topdir ${TOPDIR}" -ba $TOPDIR/SPECS/$NAME.spec

cp ${TOPDIR}/RPMS/x86_64/cdmi-s3-qos-0.0.1-1.el7.centos.x86_64.rpm .
