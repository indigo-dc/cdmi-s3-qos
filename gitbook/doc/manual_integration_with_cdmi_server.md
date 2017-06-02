# Manual integration with cdmi server

## Introduction

To manually integrate the cdmi-s3-qos module with already installed cdmi-qos server the following general steps have to be done:

* Clone from guthub and build cdmi-s3-qos module.
* Copy the built jar with cdmi-s3-qos module to the path where cdmi-qos server looks for modules.
* copy example, module’s configuration files to the destination, production stage path.
* Adjust module’s configuration files to your needs.
* Edit main cdmi-qos configuration file to tell the server to use cdmi-s3-qos module.

## Procedure

The bellow procedure will work on Linux based OS with pre-installed cdmi-qos server and java development tools.

1. Clone and build cdmi-s3-qos module:

```
git clone https://github.com/indigo-dc/cdmi-s3-qos.git
cd cdmi-s3-qos
mvn package
```

2. Copy the jar file built in previous step, to the path where cdmi-qos server looks for modules:

```
cp target/cdmi-s3-qos-*.jar /usr/lib/cdmi-server/plugins
```

3. Copy example configuration files to destination location

```
mkdir /etc/cdmi-server/plugins/cdmi-s3-qos/*
cp config/fixed-mode/* /etc/cdmi-server/plugins/cdmi-s3-qos/
cp test/objectstore.properties /etc/cdmi-server/plugins/cdmi-s3-qos/
```

4. Adjust configuration files to reflect your environment.

5. Tell cdmi-qos server to use cdmi-s3-qos module, that is make sure that file /var/lib/cdmi-server/config/application.yml contains this entry (see: https://indigo-dc.gitbooks.io/cdmi-qos/content/doc/configuration.html):

```
cdmi:
    qos:
        backed:
            type: radosgw
````

