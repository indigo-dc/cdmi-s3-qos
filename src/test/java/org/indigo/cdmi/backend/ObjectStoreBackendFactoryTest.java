package org.indigo.cdmi.backend;

import org.indigo.cdmi.backend.radosgw.BackendConfiguration;
import org.indigo.cdmi.backend.radosgw.DefaultBackendConfiguration;
import org.indigo.cdmi.backend.radosgw.config.ConfigurationContext;
import org.indigo.cdmi.backend.radosgw.di.ObjectStorageBackendTestsModule;
import org.indigo.cdmi.spi.StorageBackend;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

public class ObjectStoreBackendFactoryTest {

  private static final Logger log = LoggerFactory.getLogger(ObjectStoreBackendFactoryTest.class);
  private static final String expectedStorageBackendType = "radosgw";
  
  @Test
  public void testFactory() {
    
    BackendConfiguration defaultConfiguration = DefaultBackendConfiguration.getInstance();
    ConfigurationContext.registerConfiguration(defaultConfiguration);
    
    ObjectStorageBackendTestsModule injectorModule = new ObjectStorageBackendTestsModule();
    ObjectStoreBackendFactory factory = new ObjectStoreBackendFactory();
    factory.setInjectorModule(injectorModule);

  }

} // end of ObjectStoreBackendFactoryTest
