package org.indigo.cdmi.backend.capattrs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CapabilityEchoTest {

  private static final Logger log = LoggerFactory.getLogger(CapabilityEchoTest.class);
  
  private CdmiAttributeProvider attributeProvider = new CapabilityEcho();
  
  @Before
  public void setUp() throws Exception {
    
  }

  @Test
  public void testProviderName() {

    String name = attributeProvider.getProviderName();
    assertEquals(name, CapabilityEcho.PROVIDER_NAME);
  
  } // test

  
  @Test
  public void testAttributeValue() {
    
    String attributeValue = 
        attributeProvider.attributeValue("/", "any-name", "{" + CapabilityEcho.PROVIDER_NAME +" hello}");
    
    log.error("ATTRIBUTE_VALUE:{}", attributeValue);
    
  }
  
} // end of CapabilityEchoTest class
