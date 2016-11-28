/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

import org.indigo.cdmi.BackendCapability;
import org.indigo.cdmi.CdmiObjectStatus;
import org.indigo.cdmi.spi.StorageBackend;

import java.util.List;

/**
 * Defines methods used by this SPI implementation to translate response obtained from given
 * {@link BackendGateway} into data formats used by {@link StorageBackend} interface.
 * 
 * @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public interface GatewayResponseTranslator {

  /**
   * Translates response returned by {@link BackendGateway#getAllProfiles()} into 
   * list of BackendCapability types.
   * 
   * @param gatewayResponse The response in String format as it was returned by
   *        {@link BackendGateway#getAllProfiles()}
   * 
   * @return The list of BackendCapability objects (to be used as return value of
   *         {@link StorageBackend#getCapabilities()}).
   */
  public List<BackendCapability> getBackendCapabilitiesList(String gatewayResponse);

  
  /**
   * Translates gatewayResponse from String format to CdmiObjectStatus.
   * 
   * @param gatewayResponse The response in String format as it was returned by
   *        {@link BackendGateway#getPathProfile(String)}
   *        
   * @return CdmiObjectStatus object build on base of passed gatewayResponse 
   */
  public CdmiObjectStatus getCdmiObjectStatus(String gatewayResponse);

} // end of GatewayResponseTranslator interface
