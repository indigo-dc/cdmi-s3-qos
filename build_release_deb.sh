#!/bin/bash

VERSION=0.0.1
NAME=cdmi-s3-qos


mvn clean package

mkdir -p debian/var/lib/$NAME/config/
cp -r config/fixed-mode debian/var/lib/$NAME/config/
cp target/$NAME-$VERSION-SNAPSHOT.jar debian/var/lib/$NAME/

dpkg --build debian

mv debian.deb $NAME-$VERSION.deb
