/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

import org.indigo.cdmi.BackendCapability;
import org.indigo.cdmi.CdmiObjectStatus;
import org.indigo.cdmi.backend.capattrs.CdmiAttributeProvider;
import org.indigo.cdmi.backend.capattrs.CdmiAttributeProviderRegistry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Implementation of GatewayResponseTranslator interface which translated responses achieved from
 * {@link LifeModeBackendGateway}.
 * 
 * @author Gracjan Jankowski (gracjan@man.poznan.pl)
 *
 */
public class JsonResponseTranlator implements GatewayResponseTranslator {

  private static final Logger log = LoggerFactory.getLogger(JsonResponseTranlator.class);

  private static final String JSON_KEY_NAME = "name";
  private static final String JSON_KEY_TYPE = "type";
  private static final String JSON_KEY_METADATA = "metadata";
  private static final String JSON_KEY_CAPABILITIES = "capabilities";
  private static final String JSON_KEY_ALLOWED_PROFILES = "allowed_profiles";

  private static final String URI_CAPABILITIES_DISCRIMINANT = "cdmi_capabilities";

  
  private static final String CDMI_OBJECT_TYPE_CONTAINER = "container";
  private static final String CDMI_OBJECT_TYPE_DATAOBJECT = "dataobject";

  
  private final CdmiAttributeProviderRegistry cdmiAttributeProviderRegistry;
  
  
  /**
   * 
   */
  @Inject
  public JsonResponseTranlator(CdmiAttributeProviderRegistry cdmiAttributeProviderRegistry) {
    
    this.cdmiAttributeProviderRegistry = cdmiAttributeProviderRegistry;
  
  } // end of constructor
  
  
  
  /**
   * Maps QoS profile name to associated URI. 
   * @param profileName Name of profile
   * @param cdmObjectType Type of CDMI object (container or dataobject).
   */
  private String profileToUri(String profileName, String cdmObjectType) {

    return "/" + URI_CAPABILITIES_DISCRIMINANT + "/" + cdmObjectType + "/" + profileName;

  }





  /**
   * Retrieves object type from passed JSON object. 
   * The assumption is that profileInfo represents QoS profile in JSON format. 
   * 
   * @param profileInfo QoS profile description as JSON object (in JSONObject format).
   * @return String which represents object type of derived from profileInfo 
   *     (object type is container or dataobject).
   */
  private String retriveObjectTypeAsString(JSONObject profileInfo) {

    String typeAsString = profileInfo.getString(JSON_KEY_TYPE);
    return typeAsString;

  }


  /**
   * Basing on passed profileInfo, creates object of type BackendCapability.
   * 
   * @param profileInfo Profile described in JSOSObject.
   * @return Object of BackendCapability derived from passed profileInfo.
   */
  private BackendCapability createBackedCapability(JSONObject profileInfo) {

    /*
     * determine value of name parameter to be used with BackendCapability 
     */
    String profileName = profileInfo.getString(JSON_KEY_NAME);

    /*
     * determine value of type attribute to be used with BackendCapability
     */
    BackendCapability.CapabilityType type = null;
    String typeAsString = profileInfo.getString(JSON_KEY_TYPE);
    switch (typeAsString) {
      case CDMI_OBJECT_TYPE_CONTAINER: 
        type = BackendCapability.CapabilityType.CONTAINER;
        break;
      case CDMI_OBJECT_TYPE_DATAOBJECT: 
        type = BackendCapability.CapabilityType.DATAOBJECT;
        break;
      default:
        throw new RuntimeException("Unknown capability type");
    } // switch() 

    
    
    /*
     * create new instance of BackendCapability 
     * (it is empty for now, the metadata and capabilities properties have to be created, 
     * populated and passed to the BackendCapability object)
     */
    final BackendCapability returnBackendCapability = new BackendCapability(profileName, type);

    
    
    /*
     * create metadata object (to be populated and injected into returned BackendCapability)
     */
    Map<String, Object> metadata = new HashMap<>();
    
    JSONObject metadataObj = profileInfo.getJSONObject(JSON_KEY_METADATA);
  
    log.debug("Processing metadata array from profile returned by BackendGateway");
  
    Iterator<?> metadataKeys = metadataObj.keys();
    while (metadataKeys.hasNext()) {
  
      /*
       * get key name for current item in metadata array
       */
      String key = (String) metadataKeys.next();
  
  
      /*
       * get value assigned to the current key, and convert the value to String
       * NOTE: The convention is required because returned value can be for example of array type 
       * or of another JSONObject, it not necessary has to be String, so usage of 
       * metadataObj.getString(key) would be wrong 
       */
      Object valueObj = metadataObj.get(key);
  
      log.debug("Current metadata key: {}", key);
      log.debug("Current metadata value: {}", valueObj);
      log.debug("Metadata value class/type is: {}", valueObj.getClass());
  
      /*
       * add "calculated" key and value to the metadata map
       */
      metadata.put(key, valueObj);
  
    } // while()

    
    /*
     * create capabilities object (to be populated and injected into returned BackendCapability)
     */
    Map<String, Object> capabilities = new HashMap<>();

    JSONObject capabilitiesObj = profileInfo.getJSONObject(JSON_KEY_CAPABILITIES);
    
    log.debug("Processing capabilities array from profile returned by BackendGateway");
  
    Iterator<?> capabilitiesKeys = capabilitiesObj.keys();
    while (capabilitiesKeys.hasNext()) {
    
      /*
       * get key name for current item in metadata array
       */
      String key = (String) capabilitiesKeys.next();
  
      Object valueObj = capabilitiesObj.get(key);
  
      log.debug("Current capabilities key: {}", key);
      log.debug("Current capabilities value: {}", valueObj);
      log.debug("Metadata value class/type is: {}", valueObj.getClass());
  
      /*
       * add "calculated" key and value to the metadata map
       */
      capabilities.put(key, valueObj);
      
    } // while()    
    
    returnBackendCapability.setCapabilities(capabilities);
    returnBackendCapability.setMetadata(metadata);

    return returnBackendCapability;

    
//    /*
//     * Add always present capabilities
//     */
//    capabilities.put("cdmi_capabilities_templates", "true");
//    capabilities.put("cdmi_capabilities_exact_inherit", "true");
//    capabilities.put("cdmi_capability_association_time", "true");
//    capabilities.put("cdmi_export_container_s3", "true");
//    capabilities.put("cdmi_geographic_placement", "true");  
//    capabilities.put("cdmi_durability", "true");
//
//    
//    /*
//     * attributes defined in Lisbona
//     */
//    //capabilities.put("cdmi_capability_lifetime", "true");
//    //capabilities.put("cdmi_capability_lifetime_action", "true");
//    //capabilities.put("cdmi_default_dataobject_capability_class", "true");
//    //capabilities.put("cdmi_data_storage_lifetime", "true");
//        
//    
//    //"cdmi_capabilities_templates": true,
//    //"cdmi_capabilities_exact_inherit": true,
//    //"cdmi_capabilities_allowed": true, - list of capabilitiesURI to which transition is allowed
//    //"cdmi_capability_lifetime": true, - minimal lifetime an object will stay in current capability class
//    //"cdmi_capability_lifetime_action": true, - action after the current capability class lifetime expiration, if no defined do action is done
//    //"cdmi_capability_association_time": true, - time indicating the point in time when current capability class has been assigned to object
//    //"cdmi_recommended_polling_interval": true, - suggested polling interval, probably meaningful only when transitions are supported
//    //"cdmi_default_dataobject_capability_class": true, - capabiity class assigned by default to newly created objects
//    //"cdmi_location": true, container location for capability class
//    //"cdmi_data_storage_lifetime": true, - time interval indicating the minimum lifetime an object will be stored
//    //"cdmi_durability": true, The probability of the dataobject not being lost per year
//    //"cdmi_data_redundancy": true, - czy plik jest przechowywany w wielu kopiach
//    //"cdmi_geographic_placement": true,
//    //"cdmi_latency": true,
//    //"cdmi_throughput": true
//    
//    
//
//    /*
//     * create metadata object (to be populated and injected into returned BackendCapability)
//     */
//    Map<String, Object> metadata = new HashMap<>();
//
//    /*
//     * iterate through metadata in profileInfo and populate capabilities and 
//     * metadata in BackendCapability object
//     */
//    JSONObject metadataObj = profileInfo.getJSONObject(JSON_KEY_METADATA);
//
//    log.debug("Processing metadata array from profile returned by BackendGateway");
//
//    Iterator<?> keys = metadataObj.keys();
//    while (keys.hasNext()) {
//
//      /*
//       * get key name for current item in metadata array
//       */
//      String key = (String) keys.next();
//
//
//      /*
//       * get value assigned to the current key, and convert the value to String
//       * NOTE: The convention is required because returned value can be for example of array type 
//       * or of another JSONObject, it not necessary has to be String, so usage of 
//       * metadataObj.getString(key) would be wrong 
//       */
//      Object valueObj = metadataObj.get(key);
//      
//
//      log.debug("Current metadata key: {}", key);
//      log.debug("Current metadata value: {}", valueObj);
//      log.debug("Metadata value class/type is: {}", valueObj.getClass());
//
//      /*
//       * Create key and value to be added to capabilities.
//       * Separate variables for key and value objects are introduced deliberately 
//       * to note that in future or in case of any special values, an additional 
//       * logic / calculation can be required to obtain key and value to be used 
//       * with capabilities map 
//       */
//      String cdmiCapabilityKey = key;
//      String cdmiCapabilityValue = "true";
//
//      /*
//       * add "calculated" key and value to the capabilities map
//       */
//      capabilities.put(cdmiCapabilityKey, cdmiCapabilityValue);
//
//      /*
//       * see above comments for capabilities related keys and values
//       */
//      String cdmiMetadataKey = key;
//      Object cdmiMetadataValue = valueObj;
//      
//      /*
//       * add "calculated" key and value to the metadata map
//       */
//      metadata.put(cdmiMetadataKey, cdmiMetadataValue);
//
//
//    } // while()
//
//    //capabilities.put("cdmi_capabilities_allowed", "true");
//
//    /*
//     * process allowed profiles
//     */
//    try {
//    
//      JSONArray allowedProfiles = profileInfo.getJSONArray(JSON_KEY_ALLOWED_PROFILES);
//      String profilesUris = profilesToUris(allowedProfiles, retriveObjectTypeAsString(profileInfo));
//      log.debug("allowedProfiles: {}", allowedProfiles);
//      log.debug("profilesURIs: {}", profilesUris);
//  
//      metadata.put("cdmi_capabilities_allowed", profilesUris);
//    
//    } catch (JSONException ex) {
//      
//      log.debug("No {} key in processed JSON document",  JSON_KEY_ALLOWED_PROFILES);
//      
//    } // try{}
//    
//    returnBackendCapability.setCapabilities(capabilities);
//    returnBackendCapability.setMetadata(metadata);
//
//    return returnBackendCapability;

  } // createBackendCapability()



  /**
   * Making use of String in JSON format, creates list of BackedCapability objects 
   * related to each QoS profile described in passed JSON. 
   * 
   * @see GatewayResponseTranslator#getBackendCapabilitiesList(String) 
   */
  @Override
  public List<BackendCapability> getBackendCapabilitiesList(String gatewayResponse) {

    List<BackendCapability> backendCapabilities = new ArrayList<>();

    /*
     * check input parameters sanity conditions
     */
    if (gatewayResponse == null) {
      log.error("Argument of name input cannot be null");
      throw new IllegalArgumentException("input argument cannot be null");
    }

   
    /*
     * convert response from string to JSONArray representation and from 
     * now on treat this object as data model for further processing
     */
    JSONArray backendProfilesArray = new JSONArray(gatewayResponse);

    log.debug("JSONArray:", backendProfilesArray);


    /*
     * iterate over all profiles described by backendProfilesArray, 
     * and for each profile create related BackedndCapability object
     */
    for (int i = 0; i < backendProfilesArray.length(); i++) {

      JSONObject profileAsJsonObj = backendProfilesArray.getJSONObject(i);
      log.debug("JSONObject: {}", profileAsJsonObj);

      BackendCapability backendCapability = createBackedCapability(profileAsJsonObj);
      log.debug("Created BackendCapability: {}", backendCapability);
      backendCapabilities.add(backendCapability);

    } // for()

    return backendCapabilities;

  } // getBackendCapabilitiesList()


  /**
   * 
   * @param attributeProviderDefinition
   * @return
   */
  private String extractAttributeProviderNamme(String attributeProviderDefinition) {
    
    int inputStrLength = attributeProviderDefinition.length();
    
    if (inputStrLength <= 3) return "";
    
    return attributeProviderDefinition.trim().substring(1, inputStrLength-1).split(" ")[0];
    
  } // extractAttributeProviderNamme
  
  
  /**
   * Basing on passed JSON in String format, creates object of CdmiObjecStatus.
   * (Translates JSON in String format into CdmiObjectStatus)
   */
  @Override
  public CdmiObjectStatus getCdmiObjectStatus(String objectPath, String gatewayResponse, boolean isContainer) {

    //log.debug("Translate {} to CdmiObjectStatus", gatewayResponse);

    Map<String, Object> monitoredAttributes = new HashMap<>();

    JSONObject profile = new JSONObject(gatewayResponse);
    //log.debug("profile: {}", profile);

    JSONObject metadataProvided = profile.getJSONObject("metadata_provided");
    //log.debug("metadata_provided: {}", metadataProvided);

    Iterator<?> keys = metadataProvided.keys();
    while (keys.hasNext()) {

      /*
       * get key name for current item in metadata array
       */
      String key = (String) keys.next();
      //log.debug("key: {}", key);

      Object metadataProvidedAsObj    = metadataProvided.get(key);
      
      
      /*
       * get attr value from attribute value provider (if required)
       */
      if (metadataProvidedAsObj instanceof String && 
          ((String) metadataProvidedAsObj).startsWith("{") &&
          ((String) metadataProvidedAsObj).endsWith("}") ) {
        
        
        String providerName = extractAttributeProviderNamme(((String)metadataProvidedAsObj).trim());
        if(null == providerName) {
          throw new RuntimeException("Unexpectedly providerName is null.");
        }

        
        CdmiAttributeProvider attributeValueProvider = cdmiAttributeProviderRegistry.getProvider(providerName);
        if(attributeValueProvider == null) {
          throw new RuntimeException("Couldn't find attribute value provider named " + providerName);
        }
       
        
        if (attributeValueProvider != null) {
          metadataProvidedAsObj = 
              attributeValueProvider.attributeValue(objectPath, key, (String)metadataProvidedAsObj); 
        }
          
        
      } // if()

      monitoredAttributes.put(key, metadataProvidedAsObj);

    }

    String profileName = profile.getString("name");
    if (profileName == null) {
      throw new RuntimeException("Could not determne the name of profile: " + profile);
    }
    
    
    String type = profile.getString("type");
    if (type == null) {
      throw new RuntimeException("Could not determne the type of profile: " + profile);
    }
    
    String currentCapabilitiesUri = "/cdmi_capabilities/" + type + "/" + profileName;

    return new CdmiObjectStatus(monitoredAttributes, currentCapabilitiesUri, null);

  } // getCdmiObjectStatus()


} // end of JSONResponseTranslator class
