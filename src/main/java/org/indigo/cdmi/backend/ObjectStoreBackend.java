/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend;

import com.google.inject.Inject;

import org.indigo.cdmi.BackEndException;
import org.indigo.cdmi.BackendCapability;
import org.indigo.cdmi.CdmiObjectStatus;
import org.indigo.cdmi.backend.exports.ExportsManager;
import org.indigo.cdmi.backend.radosgw.BackendGateway;
import org.indigo.cdmi.backend.radosgw.GatewayResponseTranslator;
import org.indigo.cdmi.backend.radosgw.ObjectPathTranslator;
import org.indigo.cdmi.backend.s3.S3Facade;
import org.indigo.cdmi.spi.StorageBackend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Main implementation of StorageBacked interface defined by cdmi-spi 
 * (https://github.com/indigo-dc/cdmi-spi)/
 * 
 * @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class ObjectStoreBackend implements StorageBackend {

  private static final Logger log = LoggerFactory.getLogger(ObjectStoreBackend.class);

  /*
   * dependencies
   */
  private final BackendGateway backendGateway;
  private final GatewayResponseTranslator responseTranslator;
  private final ObjectPathTranslator pathTranslator;
  private final S3Facade s3Facade;
  private final ExportsManager exportsManager;


  /**
   * Constructor with all dependencies of this object exposed in form of constructor parameters.
   * 
   * @param backendGateway Instance of BackendGateway which knows how to communicate 
   *        with RADOS GW server
   * 
   * @param responseTranslator Translates responses got from RADOS GW to data 
   *        structures used by StorageBackend interface
   * 
   * @param pathTranslator Translates paths used by CDMI server to paths used 
   *        by underlying RADOS GW server.
   */
  @Inject
  public ObjectStoreBackend(BackendGateway backendGateway, 
      GatewayResponseTranslator responseTranslator, 
      ObjectPathTranslator pathTranslator,
      S3Facade s3Facade,
      ExportsManager exportsManager) {
    
    /*
     * assign all dependencies to member fields
     */
    this.backendGateway = backendGateway;
    this.responseTranslator = responseTranslator;
    this.pathTranslator = pathTranslator;
    this.s3Facade = s3Facade;
    this.exportsManager = exportsManager;
    
  } // ObjectStoreBackend()

  


  /**
   * Returns all QoS profiles supported by / configured on underlying RADOS gateway.
   * 
   * @see StorageBackend#getCapabilities()
   */
  @Override
  public List<BackendCapability> getCapabilities() throws BackEndException {

    log.debug("getCapabilities()");
    
    try {

      /*
       * get all back-end profiles form BackendGateway
       */
      log.info("Quering backed for all supported profiles.");
      String gatewayResponse = backendGateway.getAllProfiles();
      log.info("Response from backednd gateway: {}", gatewayResponse);

      log.info("Calling response translator to get list of BackendCapabilitiy(ies)");
      List<BackendCapability> backendCapabilities = new ArrayList<BackendCapability>();
      backendCapabilities = responseTranslator.getBackendCapabilitiesList(gatewayResponse);

      log.info("All backend capabilities: {}", backendCapabilities);

      return backendCapabilities;

    } catch (Exception ex) {

      log.error("Failed to get capability of underlying object store: {}", ex);
      throw new BackEndException("Failed to get capabilities of underlying object store.", ex);

    } // try{}

  } // getCapabilities()

  
  /**
   * Operation not supported on RADOS GW based back-end.
   * 
   * @see StorageBackend#updateCdmiObject(String, String);
   */
  @Override
  public void updateCdmiObject(String path, String targetCapabilitiesUri) throws BackEndException {

    // TODO: Determine type of exception that is to be thrown here.
    //throw new UnsupportedOperationException("Not supported operation.");

    throw new BackEndException(
        "updateCdmiObject() is not supported by radosgw cdmi-spi implementaion");
    
  } // updateCdmiObject()

  
  
  /**
   * Returns list of children laying under passed {@code path}.
   *  
   * @return List of strings which determines names of children elements of given path.
   */
  private List<String> getChildrenList(String path) {
    
    return s3Facade.getChildren(path);
    
  } // getChildrenList()
  
  
  /**
   * Returns QoS metrics (meta-data) of given CDMI object denoted by given path.  
   * 
   * @param path CDMI like path to object
   * 
   * @return QoS enriched status of object pointed to by path
   * 
   * @see StorageBackend#getCurrentStatus(String)
   */
  @Override
  public CdmiObjectStatus getCurrentStatus(String path) throws BackEndException {

    log.debug("getCurrentStatus({})", path);
    
    try {
 
      /*
       * translate CDMI path to the form compliant with underlying object storage technology
       */
      String radosPath = pathTranslator.translate(path);
      log.debug("CDMI path: {} maps to rados gw path: {}", path, radosPath);
      
      String gatewayResponse = backendGateway.getPathProfile(radosPath);
      log.debug("Status of object {} as obtained by BackendGateway: {}", path, gatewayResponse);

      boolean isContainer = s3Facade.isContainer(path);
      
      CdmiObjectStatus cdmiObjectStatus = 
          responseTranslator.getCdmiObjectStatus(radosPath, gatewayResponse, isContainer);
      
      log.debug("Status of object {} translated to CdmiObjectStatus format: {}", 
          path, cdmiObjectStatus);
      
      
      /*
       * if path point to the container then prepare children and exports attributes
       */
      if (isContainer) {
      
        List<String> childrenList = getChildrenList(path);
        cdmiObjectStatus.setChildren(childrenList);
      
        Map<String, Object> exports = exportsManager.getExports(radosPath);
        cdmiObjectStatus.setExportAttributes(exports);
      
      }
            
      //Map<String, Object> exportAttributes = new HashMap<>();
      //exportAttributes.put("s3", "http://150.254.186.78:8080");
      //cdmiObjectStatus.setExportAttributes(exportAttributes);
      
      return cdmiObjectStatus;

    } catch (Exception ex) {

      log.error("Failed to get status of CDMI object {}: {}", path, ex);
      throw new BackEndException(
          String.format("Failed to get status of CDMI object %s: %s", path, ex), ex);

    } // try{}

  } // getCurrentStatus()

  
} // end of ObjectStoreBackend class
