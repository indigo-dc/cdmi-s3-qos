package org.indigo.cdmi.backend.s3;

import org.indigo.cdmi.BackEndException;
import org.indigo.cdmi.backend.radosgw.BackendConfiguration;
import org.indigo.cdmi.backend.radosgw.DefaultBackendConfiguration;
import org.indigo.cdmi.backend.radosgw.config.ConfigurationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides S3ConnectionPropertis relevant for user which is 
 * currently logged to CDMI server.
 * 
 * This is first, proof of concept version, so all required properties 
 * are being read from configuration. 
 * 
 * Finally, the credentials for S3 connections will be obtained from some kind of external service
 * basing on the Access Token read from SecurityContextHolder.
 * 
 * @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class S3ConnectionPropertiesDefaultProvider implements S3ConnectionPropertiesProvider {

  private static final Logger log = 
      LoggerFactory.getLogger(S3ConnectionPropertiesDefaultProvider.class);
  
  public static final String CONF_S3_ENDPOINT = "objectstore.s3.endpoint";
  public static final String CONF_S3_ACCESSKEY = "objectstore.s3.access-key";
  public static final String CONF_S3_SECRETKEY = "objectstore.s3.secret-key";

  
  /**
   * default configuration source, to be used by below created default AbstractModule
   */
  //BackendConfiguration gatewayConfiguration = DefaultBackendConfiguration.getInstance();
  BackendConfiguration gatewayConfiguration = ConfigurationContext.getConfiguration();
  
  /**
   * 
   * @return
   */
  public S3ConnectionProperties getConnectionProperties() {
    
    String endpoint = gatewayConfiguration.get(CONF_S3_ENDPOINT);
    if (null == endpoint) {
      log.error("Couldn't find S3 endpoint in configuration file.");
      throw new RuntimeException("S3 backed misconfigured.");
    }  
    
    String accessKey = gatewayConfiguration.get(CONF_S3_ACCESSKEY);
    if (null == accessKey) {
      log.error("Couldn't find S3 access key in configuration file.");
      throw new RuntimeException("S3 backend misconfiguration");
    }
    
    String secretKey = gatewayConfiguration.get(CONF_S3_SECRETKEY);
    if (null == secretKey) {
      log.error("Couldn't find S3 secret key in configuration file.");
      throw new RuntimeException("S3 backend misconfiguration");
    }
        
    return new S3ConnectionProperties(endpoint, accessKey, secretKey);
    
  } // getConnectionProperties()
  
  
  
  
} // end of S3ConnectionPropertiesProvider
