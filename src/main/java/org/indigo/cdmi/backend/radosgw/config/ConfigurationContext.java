package org.indigo.cdmi.backend.radosgw.config;

import org.indigo.cdmi.backend.radosgw.BackendConfiguration;
import org.indigo.cdmi.backend.radosgw.DefaultBackendConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationContext {

  private static final Logger log = LoggerFactory.getLogger(ConfigurationContext.class);
  
  private static BackendConfiguration configuration = DefaultBackendConfiguration.getInstance();
  
  /**
   * Register implementation of {@link BackendConfiguration} interface.
   * 
   * @param configuration Actual implementation of BackendConfiguration interface.
   * @return null or previously registered BackendConfiguration implementation if any
   */
  public static BackendConfiguration registerConfiguration(BackendConfiguration configuration) {
    
    BackendConfiguration previousConfiguration = ConfigurationContext.configuration;
    
    ConfigurationContext.configuration = configuration;
    
    return previousConfiguration;
    
  } // registerConfiguration()
  
  
  /**
   * Gives access to registered {@link BackendConfiguration}.
   * 
   * @return BackendConfiguration which has been registered with help of 
   *        {@link #registerConfiguration(BackendConfiguration)} method or default one if 
   *        there was no registration
   */
  public static BackendConfiguration getConfiguration() {
  
    if (ConfigurationContext.configuration == null) {
      throw new IllegalStateException(
          "No BackendConfiguration has been registered yes. "
          + "Use ConfigurationContext.registerConfiguration() method"
      );
    }
    
    return ConfigurationContext.configuration;
  
  } // getConfiguration()
  
  
  
} // end of ConfigurationContext class
