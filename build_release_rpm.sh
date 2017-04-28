#!/bin/bash

NAME=cdmi-s3-qos
TOPDIR=`pwd`/rpm


MODULE_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -v " " | grep -o "[0-9.]*" )
MODULE_VERSION_ERR=$?


if [ $MODULE_VERSION_ERR -ne 0 ] ||  [ "x$MODULE_VERSION" == "x" ]; then
echo "ERROR: Could not determine the version of maven project."
echo "Please, check by hand if this command succeed:"
echo "mvn help:evaluate -Dexpression=project.version | grep -v \" \" | grep -o \"[0-9.]*\""
exit 1
fi


MODULE_JAR_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -v " " )



#
# compile and install cdmi-s3-qos (as a library)
#
mvn clean install



#
# clean top dir
#
rm -rf $TOPDIR

#
# create TOP dir structure
#
mkdir -p $TOPDIR
mkdir -p $TOPDIR/SOURCES
mkdir -p $TOPDIR/SPECS




#
# set final name of jar file with cdmi-s3-qos module
#
cp -f target/cdmi-s3-qos-${MODULE_JAR_VERSION}.jar target/cdmi-s3-qos-${MODULE_VERSION}.jar


#
# prepare structure with config files
#
mkdir -p $TOPDIR/SOURCES/etc/cdmi-server/plugins/$NAME
cp -f templates/all/etc/cdmi-server/plugins/cdmi-s3-qos/* $TOPDIR/SOURCES/etc/cdmi-server/plugins/$NAME 

#
# structure for module binary
#
mkdir -p $TOPDIR/SOURCES/usr/lib/cdmi-server/plugins
cp target/$NAME-${MODULE_VERSION}.jar $TOPDIR/SOURCES/usr/lib/cdmi-server/plugins


#rpm/SPECS/cdmi-s3-qos.spec
sed "s/@MODULE_VERSION@/$MODULE_VERSION/g" templates/rpm/SPECS/cdmi-s3-qos.spec > rpm/SPECS/cdmi-s3-qos.spec 


rpmbuild --define "_topdir ${TOPDIR}" -ba $TOPDIR/SPECS/$NAME.spec

cp ${TOPDIR}/RPMS/x86_64/cdmi-s3-qos-$MODULE_VERSION-1.el7.centos.x86_64.rpm .
