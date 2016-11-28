/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

/**
 * Defines methods required by SPI implementation to communicate with back-end object storage.
 * 
 * @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public interface BackendGateway {

  /**
   * Returns String as it is returned by command line tool invoked to 
   * obtain information about profiles available on given object store.
   * 
   * <p>The output of this method is meant to be passed to 
   * {@link GatewayResponseTranslator#getBackendCapabilitiesList(String)}
   * 
   * @return string representing standard output of command run to get information about profiles
   *     provided by underlying object storage
   */
  public String getAllProfiles();


  /**
   * Returns String in JSON format, which represents QoS profile assigned to given path.
   * 
   * @param path Path to CDMI object.
   */
  public String getPathProfile(String path);

} // end of interface
