# Building from source codes

Source codes of cdmi-s3-qos project are maintained on github platform. The link to the project on github is [https://github.com/indigo-dc/cdmi-s3-qos](https://github.com/indigo-dc/cdmi-s3-qos)

## Requirements

Tools required to compile the project:

* git
* JDK 1.8+
* Maven 3+
* rpm-build package (on CentOS 7, optionally to build rpm package)

## Dependencies

All dependences are pre-declared in .pom file and are resolved automaticaly by maven tool.

## Packaging to jar module

To obtain source codes and to build jar file which contains cdmi-s3-qos module execute these commands:

```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git
cd cdmi-s3-qos
mvn package
```

After this operation, the jar file with cdmi-s3-qos module can be found in the ```target/cdmi-s3-qos-<version>.jar``` file.

In general .rpm and .deb packages install the module in proper place automatically. Howerver "manual" integration of cdmi-s3-qos module and cdmi-qos server is also possible. To perform this manual integration follow hints from "Manual integration with cdmi server" chapter and from [documentation](https://indigo-dc.gitbooks.io/cdmi-qos/content/) of cdmi-qos server.

## Building deb package

The cdmi-s3-qos source codes come with scripts which can build binary installation package for Ubuntu. To build installation packages for Ubuntu get access to Ubuntu 14.04 platform and from command line type:

```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git
cd cdmi-s3-qos
sh build_release_deb.sh
```
The built deb package is placed in current working directory.

## Building rpm package

The cdmi-s3-qos source codes come also with scripts which can build binary installation package for CentOS 7.  To build installation packages for CentOS 7 you need access to command line on CentOS 7. Plese ensure that the rpm-build package has been installed earlier, and then issue the commands:

```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git
cd cdmi-s3-qos
sh build_release_rpm.sh
```

The built rpm package is placed in current working directory.
