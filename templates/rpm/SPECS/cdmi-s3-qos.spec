%define __jar_repack 	%{nil}
%define _tmppath	%{_topdir}/tmp
%define buildroot	%{_topdir}/build-rpm-root

%define name            cdmi-s3-qos
%define jarversion      @SERVICE_VERSION@
%define user            cdmi

Name:		%{name}
Version:	%{jarversion}
Release:	1%{?dist}
Summary:	SNIA CDMI server reference implementation with QoS module for RADOS S3 gateway.

Group:		Applications/Web
License:	MIT
URL:		https://github.com/indigo-dc/cdmi-s3-qos

Requires:	jre >= 1.8

%description
Standalone Spring Boot application version.

%prep

%build

%install
mkdir -p %{buildroot}/var/lib/%{name}/config
mkdir -p %{buildroot}/etc/systemd/system
cp -rf %{_topdir}/SOURCES/var %{buildroot}
cp %{_topdir}/SOURCES/%{name}.service %{buildroot}/etc/systemd/system

%files
/var/lib/%{name}/config/application.yml
/var/lib/%{name}/config/fixed-mode/*
/var/lib/%{name}/%{name}-%{jarversion}.jar
/etc/systemd/system/%{name}.service

%changelog

%post
/usr/bin/id -u %{user} > /dev/null 2>&1
if [ $? -eq 1 ]; then
  adduser --system --user-group %{user}
fi

if [ -f /var/lib/%{name}/%{name}-%{jarversion}.jar ]; then
  chmod +x /var/lib/%{name}/%{name}-%{jarversion}.jar
fi

chown -R %{user}:%{user} /var/lib/%{name}

systemctl start %{name}.service
systemctl enable %{name}.service

