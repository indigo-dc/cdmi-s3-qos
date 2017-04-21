/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

import java.util.Properties;

/**
 * Interface which defines how to access configuration parameters. 
 * See also {@link DefaultBackendConfiguration}.
 *  
 * @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public interface BackendConfiguration {

  static String EXPORTS_CONFIG_FILE_PATH = "objectstore.exports.config-file";
  
  /**
   * 
   * @param parameter Name of configuration parameter.
   * @return Value of configuration parameter.
   */
  String get(String parameter);

  /**
   * 
   * @param parameter Name of configuration parameter.
   * @param defaultValue Default value to be returned in case the requested parameter is not
   *        configured explicitly
   * @return Value of configuration parameter (possibly the default one).
   */
  String get(String parameter, String defaultValue); 

  /**
   * 
   * @return The Properties object which holds all configuration parameters and values 
   *        managed by instance of this interface.
   */
  Properties getProperties();

} // end of BackendConfiguration interface
