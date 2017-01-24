#!/bin/bash

#destroy without confirmation
vagrant destroy -f

#remove sync dir
rm -rf sync

#remove cdmi-s3-qos-tmp dir
rm -rf cdmi-s3-qos-tmp
