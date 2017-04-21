package org.indigo.cdmi.backend.s3;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.indigo.cdmi.backend.radosgw.DefaultBackendConfiguration;
import org.indigo.cdmi.backend.radosgw.config.ConfigurationContext;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class S3ConnectionPropertiesDefaultProviderTest {

  private static final Logger log = LoggerFactory.getLogger(S3ConnectionPropertiesDefaultProviderTest.class);
  
  @Before
  public void setUp() throws Exception {
    
    ConfigurationContext.registerConfiguration(DefaultBackendConfiguration.getInstance("s3tests"));
    
  }

  @Test
  public void testHappyPath() {
    S3ConnectionPropertiesDefaultProvider connectionPropertiesProvider = new S3ConnectionPropertiesDefaultProvider();
    S3ConnectionProperties connectionProperties = connectionPropertiesProvider.getConnectionProperties();
    assertNotNull(connectionProperties);
    
    String retrivedAccesskey = connectionProperties.getAccessKey();
    assertEquals("any-access-key", retrivedAccesskey);
    
    String retrivedEndPoint = connectionProperties.getEndpoint();
    assertEquals("any-endpoint", retrivedEndPoint);
    
    String retrivedSecretKey = connectionProperties.getSecretKey();
    assertEquals("any-secret-key", retrivedSecretKey);
    
    
  }

  @Test(expected=RuntimeException.class)
  public void testNoEndpoint() {

    ConfigurationContext.registerConfiguration(DefaultBackendConfiguration.getInstance("s3-no-endpoint"));
    
    S3ConnectionPropertiesDefaultProvider connectionPropertiesProvider = new S3ConnectionPropertiesDefaultProvider();
    
    connectionPropertiesProvider.getConnectionProperties();
  
  }
  
  @Test(expected=RuntimeException.class)
  public void testNoAccessKey() {

    ConfigurationContext.registerConfiguration(DefaultBackendConfiguration.getInstance("s3-no-accesskey"));
    
    S3ConnectionPropertiesDefaultProvider connectionPropertiesProvider = new S3ConnectionPropertiesDefaultProvider();
    
    connectionPropertiesProvider.getConnectionProperties();
    
  }

  @Test(expected=RuntimeException.class)
  public void testNoSecretKey() {

    ConfigurationContext.registerConfiguration(DefaultBackendConfiguration.getInstance("s3-no-secretkey"));
    
    S3ConnectionPropertiesDefaultProvider connectionPropertiesProvider = new S3ConnectionPropertiesDefaultProvider();
    
    connectionPropertiesProvider.getConnectionProperties();
    
  }
  

}
