# Configuration

## Integration with cdmi-qos server

First, to tell cdmi-qos server to use cdmi-s3-qos mdule, the server has to be configured. Details regarding server configuration can be found on this page: https://indigo-dc.gitbooks.io/cdmi-qos/content/. Anyway, enabling a particular module is straightforward (of course the module itself can have complex configuration, but enabling the module in cdmi-qos server is simple). Each module has its name as it is seen by the server. The name of cdmi-s3-qos module is `radosgw`. The sub-page of server’s documentation which describers how to use the module name to activate it can be found here: https://indigo-dc.gitbooks.io/cdmi-qos/content/doc/configuration.html .

## Module configuration

In general, configuration of cdmi-s3-qos module boils down to providing information about:

* connection parameters to back-end, RADOSGW based S3 server,
* metadata describing available QoS profiles.

All configuration files are stored in this folder: 

```
/etc/cdmi-server/plugins/cdmi-s3-qos
```
where the cdmi-s3-qos folder designates the configuration sub-folder for cdmi-s3-qos module.

All configuration files have to be readable for "cdmi" (this user is created by installation package which installs cdmi-qos server).

### Connection to S3 back-end

Connection properties to S3 server exposed by RADOSGW are configured in this file: 

```
/etc/cdmi-server/plugins/cdmi-s3-qos/objectstore.properties
```

The parameters to be set in this file are:

* `objectstore.s3.endpoint=<s3-endpoint>`

where `<s3-endpoint>` is URL to RADOSGW exposed S3 server’s endpoint

* `objectstore.s3.access-key=<access-key>`

where `<access-key>` is S3 access key to be used with the S3 endpoint

* `objectstore.s3.secret-key=<secret-key>`

where `<secret-key>` is S3 secret key

### QoS profiles’ metadata

Things related to available QoS profiles are configured in json files. There are three of them:

* `/etc/cdmi-server/plugins/cdmi-s3-qos/all-profiles.json` – contains metadata of all available QoS profiles,

* `/etc/cdmi-server/plugins/cdmi-s3-qos/profiles-map.json` – contains “entry points” to given QoS profiles, where entry point is expressed in form of path to a folder (or container in CDMI lingo),

* `/etc/cdmi-server/plugins/cdmi-s3-qos/exports.json` – here the S3 URLs to individual QoS profiles “entry points” can be configured.

### Example of all-profiles.json file

The all-profiles.json file contains array of JSON objects, each of which describes one QoS profile. The bellow example of this file describes three QoS profiles named respectively: *RootContainer*, *DataobjectProfile1* and *ContainerProfile1*. Except for `name` parameter, all other parameters are directly derived from CDMI specification itself or from its extension developed by INDIGO project. The mentioned `name` parameter is used to refer to particular profile from within `profiles-map.json` file which defines mappings between particular paths and profiles.

```
[
{
	"name":"RootContainer", 
	"type":"container", 
	"capabilities": {
	    "cdmi_capabilities_exact_inherit": "true",
                "cdmi_capabilities_templates": "true"
	},
	"metadata":{
		"cdmi_location": ["/"]
	},
	"metadata_provided": {
	}, 
},

{
	"name":"DataobjectProfile1", 
	"type":"dataobject", 
	"capabilities": {
	    "cdmi_capabilities_exact_inherit": "true",
            "cdmi_capabilities_templates": "true",
            "cdmi_capability_lifetime": "true",
	    "cdmi_capability_lifetime_action": "true",
	    "cdmi_capability_association_time": "true",
	    "cdmi_data_storage_lifetime": "true",
	    "cdmi_data_redundancy": "true",
	    "cdmi_geographic_placement": "true",
	    "cdmi_latency": "true",
	    "cdmi_throughput": "true"      
	},

	"metadata":{
		"cdmi_latency":"3000",
		"cdmi_throughput": "100000", 
		"cdmi_data_redundancy":"1",
		"cdmi_geographic_placement": ["PL"],
		"cdmi_capability_lifetime": "P20Y",
		"cdmi_capability_lifetime_action": "delete",
		"cdmi_data_storage_lifetime": "P20Y"
	},
	
	"metadata_provided": {
		"cdmi_latency_provided":"1000", 
		"cdmi_throughput_provided": "90000",
		"cdmi_data_redundancy_provided": "1",
		"cdmi_geographic_placement_provided": ["PL"],
		"cdmi_capability_lifetime_provided": "P20Y",
		"cdmi_capability_lifetime_action_provided": "delete",
		"cdmi_data_storage_lifetime_provided": "P20Y",
		"cdmi_capability_association_time_provided": "{cdmi_capability_association_time}"		
	},
	
},

{
	"name":"ContainerProfile1", 
	"type":"container", 
	"capabilities": {
	    "cdmi_capabilities_exact_inherit": "true",
            "cdmi_capabilities_templates": "true",
            "cdmi_capability_lifetime": "true",
	    "cdmi_capability_lifetime_action": "true",
	    "cdmi_capability_association_time": "true",
	    "cdmi_default_dataobject_capability_class": "true",
	    "cdmi_location": "true",
	    "cdmi_data_storage_lifetime": "true",
	    "cdmi_data_redundancy": "true",
	    "cdmi_geographic_placement": "true",
	    "cdmi_latency": "true",
	    "cdmi_throughput": "true",
	},
	"metadata":{
		"cdmi_latency":"3000",
		"cdmi_throughput": "100000",
		"cdmi_data_redundancy":"1",
		"cdmi_geographic_placement": ["DE"],
		"cdmi_default_dataobject_capability_class": "/cdmi_capabilities/dataobject/DataobjectProfile1",
		"cdmi_location": ["/standard"],
		"cdmi_capability_lifetime": "P20Y",
		"cdmi_capability_lifetime_action": "delete",
		"cdmi_data_storage_lifetime": "P20Y",
		
	},
	"metadata_provided": {
		"cdmi_latency_provided":"1000",
		"cdmi_throughput_provided": "90000", 
		"cdmi_data_redundancy_provided":"1",
		"cdmi_geographic_placement_provided": ["DE"],
		"cdmi_default_dataobject_capability_class": "/cdmi_capabilities/dataobject/DataobjectProfile1",
		"cdmi_location": ["/standard"],
		"cdmi_capability_lifetime": "P20Y",
		"cdmi_capability_lifetime_action": "delete",
		"cdmi_data_storage_lifetime": "P20Y",	
	}, 
},
]
```

### Example of  profiles-map.json file

Technical conditions imposed by RADOSGW and S3 traits define the way the mapping between QoS profiles and logical paths can be done.

In general, from RADOSGW point of view, the only "entry point" to given QoS profile is S3 bucket. The QoS profiles can be assigned only to buckets, and all objects which reside in this bucket inherit the QoS profile from the "hosting" bucket.

As it comes form CDMI specification, data objects (files counterparts) and containers (folders counterparts) are to have assigned different QoS profiles.
The rule of thumb is that relation between buckets and QoS profiles is to be established upront. All new buckets, which were not earlier assigned by administrator to particular QoS profile will be automatically assigned to the default one, and there is only one such default QoS profile.

From the above rules result that:

* The QoS profile is always determined by "parent" bucket.
* Buckets are assigned the QoS profiles with help of `profiles-map.json` file.
* The profiles assigned to buckets are later used to assign QoS profiles to objects which are being created within these buckets.
* Because from CDMI point of view, containers and data objects need separately defined profiles, the buckets have to be assigned both, the QoS profiles which later are being inherited by data objects being placed in the bucket and the QoS profiles which are being inheritetd by "simulated" containers (see below explanation why container are "simulated").
* The CDMI containers are simulated because the S3 specification, apart from buckets concept,  doesn't support any kind of containers, all objects stored in bucket have flat structure, and only “coincidentally” objects names can contain slash sings (“/”) which can be used to simulate existence of containers (the slash sign is considered as containers separator). 

The below example of `profiles-map.json` is split into three parts. The JSON object referred by `dataobjects` key maps pre-existing S3 buckets to QoS profiles defined and named in `all-profiles.json` file. The mappings defined in this place will be used to assign QoS profiles to dataobjects created within the given bucket. The JSON object referred by `containers` key maps pre-existing buckets to profiles defined in `all-profiles.json` file and this mappings will be used to assign QoS profiles to containers being simulated under given bucket. Additionally the mapping between *root* path and QoS profile is done in this place. Actually you are not allowed to store data in *root* path of S3 server (data always has to land in one of buckets), so this last mapping is only for coherence purposes, just to avoid errors in tools which read properties of QoS profile assigned to *root* path. Finally, the properties defined within JSON object referred by `defaults` key define QoS profile to be assigned to objects and containers not matched by all other mappings. In practice, these default profiles are used to assign QoS profiles to buckets which are created dynamically by end users and therefore are mapped to default QoS proifles (according to rules described earlier).

```
{
	"dataobjects": {
		"standard":"DataobjectProfile1",
	},
	"containers":  {
		"standard":"ContainerProfile1",
		"/":"RootContainer"	
	},
	"defaults": {
		"container": "ContainerProfile1",
		"dataobject": "DataobjectProfile1"
	}
}
```

### Example of  exports.json file

The cdmi-qos server presents clients with information about protocol and path to underlying data objects. In case of cdmi-s3-qos module, the underlying protocol is S3. Therefore the cdmi-s3-qos module has to tell the cdmi-qos server what is the S3 compliant URL to the data which are accessible through RADOSGW. These URLs are named *"export S3 URL"*.

The cdmi-s3-qos module builds export S3 URLs taking into account configuration stored in `exports.json` file. The file contains JSON object which describes how to build URLs for objects which are accessible through specific paths.
The format of this JSON file if as follows. On top level, each JSON key name represents entry path from CDMI point of view. This key is assigned to another JSON object which defines the way the export S3 URL for given path is being built. 

Many top level entry paths can be defined. The order in which these entry points are defined is meaningful. The cdmi-s3-qos module will use first rule which will match the CDMI path for which the export S3 URL is just being built. 

The CDMI path is considered as matching the entry point when the CDMI path begins exactly with the same string which defines the entry point. Note that in the below example, the last defined entry point is indentyfied by “/” (individual slash sign which denotes *root* folder)  which acts as kind of “catch-all” entry point.

The `exports.json` file can contain directives and variables which are interpreted by the cdmi-s3-qos module. There is one directive and two variables used in the bellow example `exports.json` file:

`export-pattern-url` – This directive accepts one parameter which simply is final export S3 URL but possibly with some variables which will be resolved in run time. The variables are placed between curly brackets. There are three variables you can use:

* `full-path` – This variable represents full CDMI path for which the export S3 URL is to be build. 

* `relative-path` – This variable represents part of CDMI path for which the exports S3 URL is to be built. The used path is relative to the S3 bucket what in practice means that it is CDMI path but with out the first element (container in CDMI lingo).

* `bucket` – This variable is simply replaced with the name of bucket which contains the data object identified by CDMI path for which the export S3 URL is to be built.

```
{
	
  "/standard": {
    "s3": {
      "url":"{export-pattern-url http://standard.example.addr:9000/{relative-path}}",
    },
  },
  
  "/" :  {
    "s3": {
      "url":"{export-pattern-url http://example.addr:9000/{full-path}}",		
      },
    }
	
}
```
