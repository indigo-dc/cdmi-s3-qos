package org.indigo.cdmi.backend;

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
  
    log.info("testFacory()");
    
    ObjectStoreBackendFactory factory = new ObjectStoreBackendFactory();

    ObjectStorageBackendTestsModule injectorModule = new ObjectStorageBackendTestsModule();
    factory.setInjectorModule(injectorModule);
    
    String description = factory.getDescription();
    assertNotNull(description);
    
    String type = factory.getType();
    assertNotNull(type);
    assertEquals(
        String.format("The type of StorageBackend has to be \"%s\" and not \"%s\"", expectedStorageBackendType, type), 
        type, 
        expectedStorageBackendType
     );
  
    StorageBackend storageBackend = factory.createStorageBackend(null);
    assertNotNull(storageBackend);
    
  } // testFactory()

    
  @Test(expected=NullPointerException.class)
  public void testCreateStorageBackendWithNullInjectorModule() {
    
    ObjectStoreBackendFactory factory = new ObjectStoreBackendFactory();
    
    ReflectionTestUtils.setField(factory, "storageBackendInjectorModule", null);
    
    factory.createStorageBackend(null);
    
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testSetInjectorModuleToNull() {
    ObjectStoreBackendFactory factory = new ObjectStoreBackendFactory();
    factory.setInjectorModule(null);
  }
  
  
} // end of ObjectStoreBackendFactoryTest
