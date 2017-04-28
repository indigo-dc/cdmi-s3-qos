#!/bin/bash

#
# read commits or branches names which are to be used in packaging process
#

#. ./PACKAGING_COMMITS

NAME=cdmi-s3-qos

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
# compile cdmi-s3-qos
#
mvn package

#
# remove old control dir (if any)
#
rm -rf debian || test 1


#
# place cdmi-s3-qos.jar in destination location
#
mkdir -p debian/usr/lib/cdmi-server/plugins
cp -f  target/$NAME-${MODULE_JAR_VERSION}.jar debian/usr/lib/cdmi-server/plugins/$NAME-${MODULE_VERSION}.jar



#
# place configuration files in destination location
#
mkdir -p debian/etc/cdmi-server/plugins/cdmi-s3-qos
cp -f templates/all/etc/cdmi-server/plugins/cdmi-s3-qos/* debian/etc/cdmi-server/plugins/cdmi-s3-qos 


#
# prepare files and folders required by dpkg --build 
#
mkdir -p debian/DEBIAN

sed "s/@MODULE_VERSION@/$MODULE_VERSION/g" templates/debian/DEBIAN/control > debian/DEBIAN/control 

#
# build package
#
dpkg --build debian

#
# set final name for the package
#
mv debian.deb $NAME-${MODULE_VERSION}.deb

