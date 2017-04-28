%define __jar_repack 	%{nil}
%define _tmppath	%{_topdir}/tmp
%define buildroot	%{_topdir}/build-rpm-root

%define name            cdmi-s3-qos
%define jarversion      @MODULE_VERSION@

Name:		%{name}
Version:	%{jarversion}
Release:	1%{?dist}
Summary:	Module for INDIGO CDMI server. The module integrates RADOS GW S3 with the CDMI server.

Group:		Applications/Web
License:	Apache-2.0
URL:		https://github.com/indigo-dc/cdmi-s3-qos

Requires:	jre >= 1.8

%description
Module which can be used by INDIGO CDMI server to integrate RADOS GW S3 with QoS facilities provided by CDMI server.

%prep

%build

%install
mkdir -p %{buildroot}/usr/lib/cdmi-server/plugins/
mkdir -p %{buildroot}/etc/cdmi-server/plugins/cdmi-s3-qos
cp -rf %{_topdir}/SOURCES/usr %{buildroot}
cp -rf %{_topdir}/SOURCES/etc %{buildroot}

%files
/usr/lib/cdmi-server/plugins/*
/etc/cdmi-server/plugins/cdmi-s3-qos/*

%changelog

%post

