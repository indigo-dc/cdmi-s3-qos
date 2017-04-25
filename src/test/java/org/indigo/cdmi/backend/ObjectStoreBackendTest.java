/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend;

import org.indigo.cdmi.BackendCapability;
import org.indigo.cdmi.CdmiObjectStatus;
import org.indigo.cdmi.backend.exports.ExportsManager;
import org.indigo.cdmi.backend.radosgw.BackendGateway;
import org.indigo.cdmi.backend.radosgw.GatewayResponseTranslator;
import org.indigo.cdmi.backend.radosgw.ObjectPathTranslator;
import org.indigo.cdmi.backend.s3.S3Facade;
import org.indigo.cdmi.spi.StorageBackend;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;


/* (non-Javadoc)
 * Stub of BackendGateway interface.
 */
class BackendGatewayStub implements BackendGateway {

  @Override
  public String getAllProfiles() {
    return null;
  }

  @Override
  public String getPathProfile(String path) {
    return null;
  }
  
} // end of BackendGateway class


/* (non-Javadoc)
 * Stub of GatewayResponseTranslator interface
 */
class GatewayResponseTranslatorStub implements GatewayResponseTranslator {

  @Override
  public List<BackendCapability> getBackendCapabilitiesList(String gatewayResponse) {
    return null;
  }

  @Override
  public CdmiObjectStatus getCdmiObjectStatus(String objectPath, String gatewayResponse,
      boolean isContainer) {
    return null;
  }
  
} // end of GatewayResponseTranslatorStub class


/* (not-Javadoc)
 * Stub of ObjectPathTranslator class
 */
class ObjectPathTranslatorStub implements ObjectPathTranslator {

  @Override
  public String translate(String path) {
    return null;
  }
  
} // end of ObjectPathTranslatorStub


/* (non-Javadoc)
 * Stub of S3Facade interface.
 */
class S3FacadeStub implements S3Facade {

  @Override
  public boolean isContainer(String path) {
    return false;
  }

  @Override
  public List<String> getChildren(String path) {
    return null;
  }

  @Override
  public Properties getObjectProperties(String path) {
    return null;
  }

  @Override
  public Properties getContainerProperties(String path) {
    return null;
  }
  
} // end of S3FacadeStub class


/* (non-Javadoc)
 * ExportsManager stub.
 * 
 */
class ExportsManagerStub implements ExportsManager {

  @Override
  public Map<String, Object> getExports(String path) {
    return null;
  }
  
} // end of ExportsManagerStub


/* (non-Javadoc)
 * 
 * Actual tests.
 */
public class ObjectStoreBackendTest {
  
  private static final Logger log = LoggerFactory.getLogger(ObjectStoreBackendTest.class);
  
  @Test
  public void test() {
    
    StorageBackend storageBackend = new ObjectStoreBackend(
        new BackendGatewayStub(), 
        new GatewayResponseTranslatorStub(), 
        new ObjectPathTranslatorStub(), 
        new S3FacadeStub(), 
        new ExportsManagerStub()
    );
    assertNotNull(storageBackend);
    
  } // test()
  
} // end of ObjectStoreBackendTest class
