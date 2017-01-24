#!/bin/bash

vagrant destroy -f
rm -rf sync
rm -rf cdmi-s3-qos-tmp

mkdir sync
cp ../templates/ansible/* sync/.
#cp -r //192.168.56.200/gracjan/projects/ansible-role-cdmi-s3-qos sync/
cp ../PACKAGING_COMMITS sync/
cp ../build_release_rpm.sh sync/

mkdir cdmi-s3-qos-tmp
cd ..
rsync -av --exclude vagrant * vagrant/cdmi-s3-qos-tmp

cd vagrant

vagrant up
