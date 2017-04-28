package org.indigo.cdmi.backend.capattrs;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.indigo.cdmi.backend.s3.S3Facade;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class S3FacadeStub implements S3Facade {

  @Override
  public boolean isContainer(String path) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public List<String> getChildren(String path) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Properties getObjectProperties(String path) {
    
    Properties objProperties = new Properties();
    objProperties.put(S3Facade.PROPERTY_CREATION_TIME, new Date());
    
    return objProperties;
  }

  @Override
  public Properties getContainerProperties(String path) {
    // TODO Auto-generated method stub
    return null;
  }
  
}

public class CapabilityAssociationTimeTest {

  private static final Logger log = LoggerFactory.getLogger(CapabilityAssociationTime.class);
  
  private CdmiAttributeProvider cdmiAttributeProvider = null;
  
  @Before
  public void setUp() throws Exception {
    
    cdmiAttributeProvider = new CapabilityAssociationTime(new S3FacadeStub());
    
  }

  
  @Test
  public void testAttributeValue() {
    
    String attributeValue = cdmiAttributeProvider.attributeValue("objectPath", "attributeName", "attributePattern");
    assertNotNull(attributeValue);
        
  }

  @Test
  public void testGetProviderName() {
  
    String providerName = cdmiAttributeProvider.getProviderName();
    assertEquals(CapabilityAssociationTime.PROVIDER_NAME, providerName);
    
  }
  
  
} // end of class
