package org.indigo.cdmi.backend.radosgw;

import static org.junit.Assert.*;

import java.util.Properties;

import org.indigo.cdmi.backend.radosgw.BackendConfiguration;
import org.indigo.cdmi.backend.radosgw.DefaultBackendConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBackendConfigurationTest {
  
  private static final Logger log = LoggerFactory.getLogger(DefaultBackendConfigurationTest.class);
  private static final String DEFAULT_VALUE = "default-value";
  
  @Before
  public void setUp(){
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testIllegalConstructorArgument() {
    DefaultBackendConfiguration.getInstance(null);
  }
  
  
  @Test
  public void testDefaultConfiguration() {
    
    log.info("testDefaultConfiguration()");
    BackendConfiguration configuration = DefaultBackendConfiguration.getInstance();
    assertNotNull(configuration);
    
    /*
     * second call to getInstance should return the same object as previous call
     */
    BackendConfiguration configuration2 = DefaultBackendConfiguration.getInstance();
    assertNotNull(configuration2);
    
    assertSame(configuration, configuration2);
    
    
    //String value = null;
    //value = configuration.get(ObjectStoreBackend.CONF_BACKEND_GATEWAY);
    //assertNotNull(value);
    
//    value = configuration.get(GatewayResponseTranslatorFactory.PARAMETER_INSTANCE_FQCN);
//    assertNotNull(value);
    

    Properties props = configuration.getProperties();
    assertNotNull(props);
    
    String defaultValue = configuration.get("non-existing-parameter", DEFAULT_VALUE);
    assertNotNull(defaultValue);
    
    assertEquals(defaultValue, DEFAULT_VALUE);
    
    
    BackendConfiguration customConfiguration1 = DefaultBackendConfiguration.getInstance("custom1");
    assertNotNull(customConfiguration1);
    
    BackendConfiguration customConfiguration2 = DefaultBackendConfiguration.getInstance("custom2");
    assertNotNull(customConfiguration2);
    
    assertNotSame(customConfiguration1, customConfiguration2);
    
    BackendConfiguration customConfiguration1a = DefaultBackendConfiguration.getInstance("custom1");
    assertNotNull(customConfiguration1a);
    
    BackendConfiguration customConfiguration2a = DefaultBackendConfiguration.getInstance("custom2");
    assertNotNull(customConfiguration2a);
    
    assertSame(customConfiguration1, customConfiguration1a);
    
    assertSame(customConfiguration2, customConfiguration2a);
    
  } // testDefaultConfiguration()

  
  @Test(expected = RuntimeException.class)
  public void nonExistingConfiguration() {
    DefaultBackendConfiguration.getInstance("non-existing");
  }
  
  
} // end of DefaultBackendConfigurationTests class
