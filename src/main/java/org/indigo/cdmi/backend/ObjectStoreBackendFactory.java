/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.indigo.cdmi.backend.radosgw.BackendConfiguration;
import org.indigo.cdmi.backend.radosgw.DefaultBackendConfiguration;
import org.indigo.cdmi.backend.radosgw.di.ObjectStorageBackendModule;

import org.indigo.cdmi.spi.StorageBackend;
import org.indigo.cdmi.spi.StorageBackendFactory;


import java.util.Map;

/**
 * Main factory of service implemented by this project.
 * Interface of this factory is defined cdmi-spi (https://github.com/indigo-dc/cdmi-spi).
 * 
 * @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class ObjectStoreBackendFactory implements StorageBackendFactory {

  private final String type = "radosgw";
  private final String description = "Radosgw and S3 based back-end implementation";
  
  private AbstractModule storageBackendInjectorModule = null;
  private boolean newInjectorRequired = true;
    
  private Injector injector = null;
  
  /**
   * This method is meant mainly for testing purposes. It allows for providing custom 
   * Implementation of {@link AbstractModule} which is responsible for tiding up all manageable
   * dependencies.
   * 
   * <p>In production, by default {@link ObjectStorageBackendModule} is used as AbstractModule.
   *      
   * <p>Dependency injection management is base on Guice project (https://github.com/google/guice)
   *   
   * @param module Instance of AbstractModule which defines dependency injection rules. 
   * 
   * @return previous AbstractModule
   */
  public AbstractModule setInjectorModule(AbstractModule module) {
    
    if (null == module) {
      throw new IllegalArgumentException("module parameter cannot be null");
    }
    
    AbstractModule oldModule = storageBackendInjectorModule;
    
    this.storageBackendInjectorModule = module;
    
    /*
     * set to true after new injector module is set, it indicates that injector has to be 
     * recreated because new dependency injections has been provided
     */
    newInjectorRequired = true;
    
    return oldModule;
  
  } // setInjectorModule()
  
  
  /**
   * This constructor cannot have parameters because it is to be created by SPI mechanism.
   */
  public ObjectStoreBackendFactory() {
    
    /*
     * default configuration source, to be used by below created default AbstractModule
     */
    BackendConfiguration gatewayConfiguration = DefaultBackendConfiguration.getInstance();
    
    /*
     * default AbstractModule with defined dependency injection rules
     */
    this.storageBackendInjectorModule = new ObjectStorageBackendModule(gatewayConfiguration);
    
    /*
     * indicate that dependencies defined by the above Abstract module have not yet been used
     * to create injector object
     */
    newInjectorRequired = true;
    
  } // ObjectStoreBackendFactory()
  
  
  @Override
  public StorageBackend createStorageBackend(Map<String, String> properties)
      throws IllegalArgumentException {
   
  
    /*
     * if there is no injector of if newInjectorRequired flag is set then create new injector
     */
    if (injector == null || newInjectorRequired) {
      
      
      /*
       * check it just in case, 
       * one default storageBackendInjectorModule is created by constructor 
       * so below condition should never been met 
       */
      if (null == this.storageBackendInjectorModule) {
        
        throw new NullPointerException("storageBackendInjectorModule cannot be null");
      
      }
      
      /*
       * create new injector, use AbstractModule from storageBackendInjectorModule field
       */
      injector = Guice.createInjector(this.storageBackendInjectorModule);
      
      /*
       * remember that current AbstractModule has been used to create a specific injectror 
       * and until new AbstractModule is set there is not need to create new instance of injector
       */
      newInjectorRequired = false;
    
    }
    
    return injector.getInstance(StorageBackend.class);
    
  } // createStorageBackend()
  
  
  /**
   * Returns description of this factory.
   *  
   * @see ObjectStoreBackendFactory#getDescription()
   */
  @Override
  public String getDescription() {
    return description;
  }
  
  /**
   * Returns type of this factory.
   * 
   * @see ObjectStoreBackendFactory#getType()
   */
  @Override
  public String getType() {
    return type;
  }
  
} // end of ObjectStoreBackendFactory class
