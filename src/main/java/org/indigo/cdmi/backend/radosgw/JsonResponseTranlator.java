/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

import com.google.inject.Inject;

import org.indigo.cdmi.BackendCapability;
import org.indigo.cdmi.CdmiObjectStatus;
import org.indigo.cdmi.backend.capattrs.CdmiAttributeProvider;
import org.indigo.cdmi.backend.capattrs.CdmiAttributeProviderRegistry;
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
 * {@link BackendGateway}.
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

  private static final String CDMI_OBJECT_TYPE_CONTAINER = "container";
  private static final String CDMI_OBJECT_TYPE_DATAOBJECT = "dataobject";

  
  private final CdmiAttributeProviderRegistry cdmiAttributeProviderRegistry;
  
  
  /**
   * Constructor.
   */
  @Inject
  public JsonResponseTranlator(CdmiAttributeProviderRegistry cdmiAttributeProviderRegistry) {
    
    this.cdmiAttributeProviderRegistry = cdmiAttributeProviderRegistry;
  
  } // end of constructor
  
  
  
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

  } // createBackendCapability()



  /**
   * Making use of String in JSON format, creates list of BackedCapability objects 
   * related to each QoS profile described in passed JSON. 
   * 
   * @see GatewayResponseTranslator#getBackendCapabilitiesList(String) 
   */
  @Override
  public List<BackendCapability> getBackendCapabilitiesList(String gatewayResponse) {

    //log.error("--------------------------------------------------------------");
    //log.error("gatewayResponse: {}", gatewayResponse);
    //log.error("--------------------------------------------------------------");
    
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
   * Extracts name of {@link CdmiAttributeProvider} provider which is to be used to evaluate 
   * value of associated attribute. 
   * 
   * @param attributeProviderDefinition String with encoded name of attribute provider.
   * @return Name of provider encoded in passed {@code attributeProviderDefinition}
   */
  private String extractAttributeProviderNamme(String attributeProviderDefinition) {
    
    int inputStrLength = attributeProviderDefinition.length();
    
    if (inputStrLength <= 3) {
      return "";
    }
    
    return attributeProviderDefinition.trim().substring(1, inputStrLength - 1).split(" ")[0];
    
  } // extractAttributeProviderNamme
  
  
  /**
   * Basing on passed JSON in String format, creates object of CdmiObjecStatus.
   * (Translates JSON in String format into CdmiObjectStatus)
   */
  @Override
  public CdmiObjectStatus getCdmiObjectStatus(
                          String objectPath, String gatewayResponse, boolean isContainer) {

    //log.error("-----------------------------------------------------------------------");
    //log.error("objectPath: {}", objectPath);
    //log.error("gatewayResponse: {}", gatewayResponse);
    //log.error("isContainer: {}", isContainer);

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
      if (metadataProvidedAsObj instanceof String 
          && ((String) metadataProvidedAsObj).startsWith("{") 
          && ((String) metadataProvidedAsObj).endsWith("}") ) {
        
        
        String providerName = extractAttributeProviderNamme(((String)metadataProvidedAsObj).trim());
        if (null == providerName) {
          throw new RuntimeException("Unexpectedly providerName is null.");
        }

        
        CdmiAttributeProvider attributeValueProvider = 
                        cdmiAttributeProviderRegistry.getProvider(providerName);
        if (attributeValueProvider == null) {
          throw new RuntimeException(
              "Couldn't find attribute value provider named " + providerName
          );
        }
       
        
        if (attributeValueProvider != null) {
          metadataProvidedAsObj = 
              attributeValueProvider.attributeValue(
                  objectPath, key, (String)metadataProvidedAsObj
              ); 
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
