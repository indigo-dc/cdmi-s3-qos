/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw.di;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.indigo.cdmi.backend.radosgw.BackendConfiguration;
import org.indigo.cdmi.backend.radosgw.LifeModeBackendGateway;
import org.indigo.cdmi.backend.radosgw.RemoteExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NOTE: No longer used. Now, in AbstractModule::configure() I bind 
 * directly to LifeModeBackendGateway. 
 * 
 * @author Gracjan Jankowski
 */
public class LifeModeBackendGatewayProvider implements Provider<LifeModeBackendGateway> {

  private static final Logger log = LoggerFactory.getLogger(LifeModeBackendGatewayProvider.class);
  
  private final BackendConfiguration configuration;
  private final RemoteExecutor remoteExecutor;
  
  /**
   * Constructor. 
   */
  @Inject
  public LifeModeBackendGatewayProvider(BackendConfiguration configuration, 
      RemoteExecutor remoteExecutor) {
  
    log.info("LifeModeBackendGatewayProvider(BackendConfiguration)");
    
    this.configuration = configuration;
    this.remoteExecutor = remoteExecutor;
  
  }
  
  
  @Override
  public LifeModeBackendGateway get() {
    
    log.debug("get()");
    
    return new LifeModeBackendGateway(this.configuration, this.remoteExecutor);
  
  }

} // end of LifeModeBackendGatewayProvider class
