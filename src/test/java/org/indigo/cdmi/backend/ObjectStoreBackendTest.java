/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend;

import org.indigo.cdmi.BackEndException;
import org.indigo.cdmi.BackendCapability;
import org.indigo.cdmi.CdmiObjectStatus;
import org.indigo.cdmi.backend.capattrs.CdmiAttributeProviderRegistry;
import org.indigo.cdmi.backend.exports.ExportAttributeProviderRegistry;
import org.indigo.cdmi.backend.exports.ExportsManagerImpl;
import org.indigo.cdmi.backend.radosgw.BackendConfiguration;
import org.indigo.cdmi.backend.radosgw.BackendGateway;
import org.indigo.cdmi.backend.radosgw.FixedModeBackendGateway;
import org.indigo.cdmi.backend.radosgw.JsonResponseTranlator;
import org.indigo.cdmi.backend.s3.MinioS3ClientBuilder;
import org.indigo.cdmi.backend.s3.MinioS3Gateway;
import org.indigo.cdmi.backend.s3.S3ConnectionPropertiesDefaultProvider;
import org.indigo.cdmi.backend.s3.S3Facade;
import org.indigo.cdmi.backend.s3.S3PathTranslator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class ObjectStoreBackendTest {

  private static final Logger log = LoggerFactory.getLogger(ObjectStoreBackendTest.class);
  private BackendConfiguration backendConfiguration;

  
  
  @Before
  public void setUp() {
    
    log.info("setUp()");
    
    backendConfiguration = Mockito.mock(BackendConfiguration.class);
    
    when(
        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_ALL_PROFILES_FILE)
    ).thenReturn(
        "config/fixed-mode/all-profiles.json"
    );

    when(
        //backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PATHS_PROFILES_FILE)
        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PROFILES_MAP_FILE)
    ).thenReturn(
        //"config/fixed-mode/buckets-profiles.json"
        "config/fixed-mode/profiles-map.json"
    );

    
  } // setUp()
  
  
  @Test
  public void testStandardPath() throws Exception {
    
    log.info("testStandardPath()");

    ObjectStoreBackend objectStoreBackend = new ObjectStoreBackend(
        new FixedModeBackendGateway(backendConfiguration, new S3Facade(new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesDefaultProvider()))), 
        new JsonResponseTranlator(new CdmiAttributeProviderRegistry()), 
        new S3PathTranslator(),
        new S3Facade(new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesDefaultProvider())),
        new ExportsManagerImpl(new ExportAttributeProviderRegistry())
    );

    List<BackendCapability> list = objectStoreBackend.getCapabilities();
    assertNotNull(list);
    assertTrue(!list.isEmpty());
      
    CdmiObjectStatus cdmiObjectStatus1 = objectStoreBackend.getCurrentStatus("/golden");
    assertNotNull(cdmiObjectStatus1);
    
    
  } // test()
  

//  @Test(expected=BackEndException.class)
//  public void testStatusOfNonExistingPath() throws Exception {
//
//    ObjectStoreBackend objectStoreBackend = new ObjectStoreBackend(
//        new FixedModeBackendGateway(backendConfiguration, new S3Facade(new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesDefaultProvider()))), 
//        new JsonResponseTranlator(), 
//        new S3PathTranslator(),
//        new S3Facade(new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesDefaultProvider()))
//    );
//    
//    objectStoreBackend.getCurrentStatus("/non-existing-path");    
//  
//  }

  
  
  @Test(expected=BackEndException.class)
  public void testUpdateCdmiObjectWithException() throws Exception{    
    
    ObjectStoreBackend objectStoreBackend = new ObjectStoreBackend(
        new FixedModeBackendGateway(backendConfiguration, new S3Facade(new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesDefaultProvider()))), 
        new JsonResponseTranlator(new CdmiAttributeProviderRegistry()), 
        new S3PathTranslator(),
        new S3Facade(new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesDefaultProvider())),
        new ExportsManagerImpl(new ExportAttributeProviderRegistry())
    );
    
    objectStoreBackend.updateCdmiObject("", "");    
    
  } // testGetCapabilitiesWithException()
  
  
  @Test(expected=BackEndException.class)
  public void testGetCapabilitiesWithException() throws Exception {
    
    BackendGateway backendGateway = Mockito.mock(BackendGateway.class);
    
    when(backendGateway.getAllProfiles()).thenThrow(new RuntimeException());
    
    ObjectStoreBackend objectStoreBackend = new ObjectStoreBackend(
        backendGateway, 
        new JsonResponseTranlator(new CdmiAttributeProviderRegistry()), 
        new S3PathTranslator(),
        new S3Facade(new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesDefaultProvider())),
        new ExportsManagerImpl(new ExportAttributeProviderRegistry())
    );

    objectStoreBackend.getCapabilities();
    
  }

} // end of ObjectStoreBackendTest class
