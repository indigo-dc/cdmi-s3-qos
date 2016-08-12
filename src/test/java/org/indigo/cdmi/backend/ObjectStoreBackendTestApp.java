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
import org.indigo.cdmi.CdmiObjectStatus;
import org.indigo.cdmi.backend.radosgw.di.ObjectStorageBackendTestsModule;
import org.indigo.cdmi.spi.StorageBackend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ObjectStoreBackendTestApp {

  private static final Logger log = LoggerFactory.getLogger(ObjectStoreBackendTestApp.class);

  /**
   * Entry point for "manual" test.
   * 
   */
  public static void main(String[] args) {
    
    
    ObjectStorageBackendTestsModule injectorModule = new ObjectStorageBackendTestsModule();
    
    Injector injector = Guice.createInjector(injectorModule);
    
    ObjectStoreBackend objectStoreBackend = (ObjectStoreBackend) injector.getInstance(StorageBackend.class);


    String path = "standard/subdir";
    CdmiObjectStatus cdmiObjectStatus = null;
    try {
      cdmiObjectStatus = objectStoreBackend.getCurrentStatus(path);
    } catch (BackEndException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    log.debug("Object status for path: {} is {}", path, cdmiObjectStatus);

  }

}
