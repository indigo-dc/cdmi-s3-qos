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

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private static final String JSON_KEY_ALLOWED_PROFILES = "allowed_profiles";

  private static final String URI_CAPABILITIES_DISCRIMINANT = "cdmi_capabilities";

  
  private static final String CDMI_OBJECT_TYPE_CONTAINER = "container";
  private static final String CDMI_OBJECT_TYPE_DATAOBJECT = "dataobject";
  

  /**
   * Maps QoS profile name to associated URI. 
   * @param profileName Name of profile
   * @param cdmObjectType Type of CDMI object (container or dataobject).
   */
  private String profileToUri(String profileName, String cdmObjectType) {

    return "/" + URI_CAPABILITIES_DISCRIMINANT + "/" + cdmObjectType + "/" + profileName;

  }


  /**
   * Maps names of profiles from prifilesNames JSON array to comma separated sequence of 
   * associated URIs.
   * @param profilesNames JSON array of profiles' names
   */
  private String profilesToUris(JSONArray profilesNames, String cdmiObjectType) {

    StringBuffer rv = new StringBuffer();

    log.debug("In profilesToURIs(): {}", profilesNames);

    for (int index = 0; index < profilesNames.length(); index++) {
      
      String profileName = profilesNames.getString(index);
      log.debug("Processed profile name: {}", profileName);
      String capabilityUri = profileToUri(profileName, cdmiObjectType);
      if (index > 0) {
        rv.append(", ");
      }
      rv.append(capabilityUri);

    } // for()

    return rv.toString();

  } // profilesToURIs()


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
    }

    /*
     * create new instance of BackendCapability 
     * (it is empty for now, the metadata and capabilities properties have to be created, 
     * populated and passed to the BackendCapability object)
     */
    final BackendCapability returnBackendCapability = new BackendCapability(profileName, type);

    /*
     * create capabilities object (to be populated and injected into returned BackendCapability)
     */
    Map<String, Object> capabilities = new HashMap<>();

    /*
     * Add always present capabilities
     */
    capabilities.put("cdmi_capabilities_templates", "true");
    capabilities.put("cdmi_capabilities_exact_inherit", "true");

    /*
     * create metadata object (to be populated and injected into returned BackendCapability)
     */
    Map<String, Object> metadata = new HashMap<>();

    /*
     * iterate through metadata in profileInfo and populate capabilities and 
     * metadata in BackendCapability object
     */
    JSONObject metadataObj = profileInfo.getJSONObject(JSON_KEY_METADATA);

    log.debug("Processing metadata array from profile returned by BackendGateway");

    Iterator<?> keys = metadataObj.keys();
    while (keys.hasNext()) {

      /*
       * get key name for current item in metadata array
       */
      String key = (String) keys.next();


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
       * Create key and value to be added to capabilities.
       * Separate variables for key and value objects are introduced deliberately 
       * to note that in future or in case of any special values, an additional 
       * logic / calculation can be required to obtain key and value to be used 
       * with capabilities map 
       */
      String cdmiCapabilityKey = key;
      String cdmiCapabilityValue = "true";

      /*
       * add "calculated" key and value to the capabilities map
       */
      capabilities.put(cdmiCapabilityKey, cdmiCapabilityValue);

      /*
       * see above comments for capabilities related keys and values
       */
      String cdmiMetadataKey = key;
      Object cdmiMetadataValue = valueObj;
      
      /*
       * add "calculated" key and value to the metadata map
       */
      metadata.put(cdmiMetadataKey, cdmiMetadataValue);


    } // while()

    capabilities.put("cdmi_capabilities_allowed", "true");

    /*
     * process allowed profiles
     */
    JSONArray allowedProfiles = profileInfo.getJSONArray(JSON_KEY_ALLOWED_PROFILES);
    String profilesUris = profilesToUris(allowedProfiles, retriveObjectTypeAsString(profileInfo));
    log.debug("allowedProfiles: {}", allowedProfiles);
    log.debug("profilesURIs: {}", profilesUris);

    metadata.put("cdmi_capabilities_allowed", profilesUris);

    returnBackendCapability.setCapabilities(capabilities);
    returnBackendCapability.setMetadata(metadata);

    return returnBackendCapability;

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
   * Basing on passed JSON in String format, creates object of CdmiObjecStatus.
   * (Translates JSON in String format into CdmiObjectStatus)
   */
  @Override
  public CdmiObjectStatus getCdmiObjectStatus(String gatewayResponse) {

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
      //String metadataProvidedAsString = metadataProvidedAsObj.toString();
      
      //monitoredAttributes.put(key, metadataProvidedAsString);
      monitoredAttributes.put(key, metadataProvidedAsObj);

    }

    String profileName = profile.getString("name");
    String type = profile.getString("type");

    String currentCapabilitiesUri = "/cdmi_capabilities/" + type + "/" + profileName;

    return new CdmiObjectStatus(monitoredAttributes, currentCapabilitiesUri, null);

  } // getCdmiObjectStatus()


} // end of JSONResponseTranslator class
