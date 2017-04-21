/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;


import com.jcraft.jsch.JSch;

import org.indigo.cdmi.backend.ObjectStoreBackend;
import org.indigo.cdmi.backend.capattrs.CapabilityAssociationTime;
import org.indigo.cdmi.backend.capattrs.CapabilityEcho;
import org.indigo.cdmi.backend.capattrs.CdmiAttributeProvider;
import org.indigo.cdmi.backend.capattrs.CdmiAttributeProviderRegistry;
import org.indigo.cdmi.backend.capattrs.CdmiAttributeProviderRegistryImpl;
import org.indigo.cdmi.backend.exports.ExportAttributeProvider;
import org.indigo.cdmi.backend.exports.ExportAttributeProviderRegistry;
import org.indigo.cdmi.backend.exports.ExportAttributeProviderRegistryImpl;
import org.indigo.cdmi.backend.exports.ExportPatternUrl;
import org.indigo.cdmi.backend.exports.ExportsManager;
import org.indigo.cdmi.backend.exports.ExportsManagerImpl;
import org.indigo.cdmi.backend.radosgw.BackendConfiguration;
import org.indigo.cdmi.backend.radosgw.BackendGateway;

import org.indigo.cdmi.backend.radosgw.GatewayResponseTranslator;
//import org.indigo.cdmi.backend.radosgw.JSchAliveRemoteExecutor;
import org.indigo.cdmi.backend.radosgw.ObjectPathTranslator;
import org.indigo.cdmi.backend.s3.MinioS3ClientBuilder;
import org.indigo.cdmi.backend.s3.MinioS3ClientBuilderImpl;
import org.indigo.cdmi.backend.s3.MinioS3Gateway;
import org.indigo.cdmi.backend.s3.S3ConnectionPropertiesDefaultProvider;
import org.indigo.cdmi.backend.s3.S3ConnectionPropertiesProvider;
import org.indigo.cdmi.backend.s3.S3Facade;
import org.indigo.cdmi.backend.s3.S3FacadeImpl;
import org.indigo.cdmi.backend.s3.S3Gateway;
import org.indigo.cdmi.spi.StorageBackend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class ObjectStorageBackendModule extends AbstractModule {

  private static final Logger log = LoggerFactory.getLogger(ObjectStorageBackendModule.class); 
  
  private static final String PARAMETER_PATH_TRANSLATOR_INSTANCE_FQCN 
      = "objectstore.object-path-translator";
  
  private static final String PARAMETER_GATEWAY_RESPONSE_TRANSLATOR_INSTANCE_FQCN 
      = "objectstore.gateway-response-translator";
  
  private static final String PARAMETER_BACKEND_GATEWAY_FQCN 
      = "objectstore.backend-gateway";
  
  public static final String PARAMETER_BACKEND_GATEWAY_PROVIDER_FQCN 
      = "objectstore.backend-gateway-provider";
  
  
  
  private BackendConfiguration backendConfiguration = null;
  
  
  /**
   * Constructor. 
   */
  public ObjectStorageBackendModule(BackendConfiguration backendConfiguration) {
    this.backendConfiguration = backendConfiguration;
  }
  
  
  @SuppressWarnings("unchecked")
  @Override
  protected void configure() {

    log.info("configure()");
    
    String storageBackendFqcn = this.backendConfiguration.get(PARAMETER_BACKEND_GATEWAY_FQCN);
    try {
      
      Class<? extends BackendGateway> gatewayClazz 
          = (Class<? extends BackendGateway>) Class.forName(storageBackendFqcn);
      
      bind(BackendGateway.class).to(gatewayClazz);
    
    } catch (ClassNotFoundException e) {
      
      log.error("Could not load class {}; reason: {}", storageBackendFqcn, e);
      throw new RuntimeException(e);
    
    } // try{}

    //bind(ObjectPathTranslator.class).to(S3PathTranslator.class);
    
    //bind(RemoteExecutor.class).to(JSchAliveRemoteExecutor.class);
    
    bind(StorageBackend.class).to(ObjectStoreBackend.class);

    bind(JSch.class).toProvider(JSchProvider.class);
    
    bind(S3ConnectionPropertiesProvider.class).to(S3ConnectionPropertiesDefaultProvider.class);
    
    bind(S3Gateway.class).to(MinioS3Gateway.class);
    
    bind(S3Facade.class).to(S3FacadeImpl.class);
    
    bind(MinioS3ClientBuilder.class).to(MinioS3ClientBuilderImpl.class);
    

    Multibinder<CdmiAttributeProvider> cdmiAttributeProviderMultibinder = 
                      Multibinder.newSetBinder(binder(), CdmiAttributeProvider.class);

    cdmiAttributeProviderMultibinder.addBinding().to(CapabilityAssociationTime.class);
    cdmiAttributeProviderMultibinder.addBinding().to(CapabilityEcho.class);
    
    bind(CdmiAttributeProviderRegistry.class).to(CdmiAttributeProviderRegistryImpl.class);
    
    Multibinder<ExportAttributeProvider> exportAttributeProviders =
                    Multibinder.newSetBinder(binder(), ExportAttributeProvider.class);
    
    exportAttributeProviders.addBinding().to(ExportPatternUrl.class);
    
    bind(ExportAttributeProviderRegistry.class).to(ExportAttributeProviderRegistryImpl.class);
    
    bind(ExportsManager.class).to(ExportsManagerImpl.class);
    
  } // configure()

  
  @Provides
  BackendConfiguration provideBackendConfiguration() {
    
    log.info("provideBackendConfiguration()");
    
    return this.backendConfiguration;
  
  } // provideBackendConfiguration()
  
  
  @Provides
  GatewayResponseTranslator provideGatewayResponseTranslator(
                  BackendConfiguration configuration, 
                  CdmiAttributeProviderRegistry cdmiAttributeProviderRegistry) {
    
    log.info("provideGatewayResponseTranslator(BackendConfiguration)");
    
    String translatorClass = configuration.get(
        ObjectStorageBackendModule.PARAMETER_GATEWAY_RESPONSE_TRANSLATOR_INSTANCE_FQCN);

    if (translatorClass == null) {
      log.error(
          "Cannot create GatewayResponseTranslator. Parameter {} is not defined in "
          + "configuration resources",
          ObjectStorageBackendModule.PARAMETER_GATEWAY_RESPONSE_TRANSLATOR_INSTANCE_FQCN);
      throw new RuntimeException("Cannot create GatewayResponseTranslator. Parameter "
          + ObjectStorageBackendModule.PARAMETER_GATEWAY_RESPONSE_TRANSLATOR_INSTANCE_FQCN
          + " is not defined in configuration resources");
    }
    log.info("Actual GatewayResponseTranslator: {}", translatorClass);


    try {
      
      Class<?> clazz = Class.forName(translatorClass);
      //return (GatewayResponseTranslator) clazz.newInstance();
      Constructor<?> cons = clazz.getConstructor(CdmiAttributeProviderRegistry.class);
      return (GatewayResponseTranslator) cons.newInstance(cdmiAttributeProviderRegistry);
      
    } catch (ClassNotFoundException | InstantiationException 
        | NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
      
      log.error("Failed to create instance of {} class; exception: {};", translatorClass, ex);
      throw new RuntimeException(ex);
    
    }

  } // provideGatewayResponseTranslator()

  
  @Provides
  ObjectPathTranslator provideObjectPathTranslator(BackendConfiguration configuration) {
    
    log.info("provideObjectPathTranslator(BackendConfiguration)");
    
    final String translatorClass = configuration.get(
        ObjectStorageBackendModule.PARAMETER_PATH_TRANSLATOR_INSTANCE_FQCN);
    
    if (translatorClass == null) {
      log.error("Cannot create ObjectPathTranslator. Parameter {} is not defined in configuration "
          + "resources", ObjectStorageBackendModule.PARAMETER_PATH_TRANSLATOR_INSTANCE_FQCN);
      
      throw new RuntimeException("Cannot create ObjectPathTranslator. Parameter " 
          + ObjectStorageBackendModule.PARAMETER_PATH_TRANSLATOR_INSTANCE_FQCN 
          + " is not defined in configuration resources");
    
    }

    log.info("Actual ObjectPathTranslator: {}", translatorClass);

    try {
      
      Class<?> clazz = Class.forName(translatorClass);
      return (ObjectPathTranslator) clazz.newInstance();
    
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
      
      log.error("Failed to create instance of {} class; exception: {};", translatorClass, ex);
      throw new RuntimeException(ex);
    
    }

  } // provideObjectPathTranslator()
  
} // end of ObjectStorageBackendModule class
