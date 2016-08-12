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
import org.indigo.cdmi.backend.radosgw.FixedModeBackendGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * NOTE: No longer used. Now, in AbstractModule::configure() I bind 
 * directly to FixedModeBackendGateway. 
 * 
 * @author Gracjan Jankowski
 */
public class FixedModeBackendBackendProvider implements Provider<FixedModeBackendGateway> {

  private static final Logger log = LoggerFactory.getLogger(FixedModeBackendBackendProvider.class);
  
  private final BackendConfiguration configuration;
  
  
  @Inject
  public FixedModeBackendBackendProvider(BackendConfiguration configuration) {
    this.configuration = configuration;
  }
  
  @Override
  public FixedModeBackendGateway get() {
  
    log.info("get()");
   
    return new FixedModeBackendGateway(configuration);
     
  
  }

} // end of FixedModeBackendProvider
