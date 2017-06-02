# Log files

Depending on the way and platform you used to deploy cdmi-s3-qos module, the log files are available in slightly different locations. 

## Ubuntu

On Ubuntu platform, if cdmi-s3-qos service has been installed from binary packages described in section "Installing and running as a standalone service" then log file of cdmi-qos server and of integrated cdmi-s3-qos module is lacated at path:

```
/var/log/cdmi-s3-qos.log
```

## Centos

In CentOS 7, system intercepts messages printed by services and stores them in so called sysetm journal. To display log messages printed by cdmi-s3-qos service, use this command:

```
journalctl _SYSTEM_UNIT=cdmi-s3-qos.service
```

## Docker container

To display logs of cdmi-qos server and cdmi-s3-qos module which were launched within docker container use this command:

```
docker logs cdmi-s3-qos
```

The assumption is that the name of  involved docker container is the same as the name of module (cdmi-s3-qos).

## Manual integration

In general logs of cdmi-s3-qos module can be found in the same file which contains logs of cdmi-qos server. If you integrated the cdmi-s3-qos module manually, with earlier installed cdmi-qos server then please check out the server documentation to locate the log files.