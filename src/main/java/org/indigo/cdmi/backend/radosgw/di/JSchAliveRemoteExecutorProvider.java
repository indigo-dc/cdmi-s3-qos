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

import com.jcraft.jsch.JSch;

import org.indigo.cdmi.backend.radosgw.BackendConfiguration;
import org.indigo.cdmi.backend.radosgw.JSchAliveRemoteExecutor;


public class JSchAliveRemoteExecutorProvider implements Provider<JSchAliveRemoteExecutor> {

  
  private BackendConfiguration backendConfiguration;
  private Provider<JSch> jschProvider;
  
  /**
   * Constructor.
   */
  @Inject
  public JSchAliveRemoteExecutorProvider(BackendConfiguration backendConfiguration, 
      Provider<JSch> jschProvider) {
    
    this.backendConfiguration = backendConfiguration;
    this.jschProvider = jschProvider;
  
  } // constructor
  
  @Override
  public JSchAliveRemoteExecutor get() {
    
    JSchAliveRemoteExecutor remoteExecutor = 
        new JSchAliveRemoteExecutor(this.jschProvider, this.backendConfiguration);
    
    return remoteExecutor;
  }

} // end of JSchAliveRemoteExecutorProvider
