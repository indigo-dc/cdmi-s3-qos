package org.indigo.cdmi.backend.capattrs;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class FakeCdmiAttributeProvider implements CdmiAttributeProvider {

  public static final String PROVIDER_NAME = "fake-provider";
  
  @Override
  public String getProviderName() {
    return PROVIDER_NAME;
  }

  @Override
  public String attributeValue(String objectPath, String attributeName, String attributePattern) {
    
    return "calculated-attribute-value";
  
  }
  
} // end of FakeCdmiAttributeProvider



public class CdmiAttributeProviderRegistryImplTest {

  private static final Logger log = LoggerFactory.getLogger(CdmiAttributeProviderRegistryImplTest.class);
  
  
  @Before
  public void setUp() throws Exception {}

  
  @Test
  public void testEmptyConstructor() {
    
    CdmiAttributeProviderRegistryImpl attrProviderRegistry = new CdmiAttributeProviderRegistryImpl();
    CdmiAttributeProvider attrProvider = attrProviderRegistry.getProvider("any");
    assertNull(attrProvider);
  
    
  }
  
  
  @Test
  public void testParametrizedConstructorWithEmptyProvidersSet() {
    CdmiAttributeProviderRegistryImpl attrProviderRegistry = new CdmiAttributeProviderRegistryImpl(new HashSet<>());
    CdmiAttributeProvider attrProvider = attrProviderRegistry.getProvider("any");
    assertNull(attrProvider);
    
  }
  
  
  @Test
  public void testParametrizedConstructorWithSomeProvidersInIt() {
    
    Set<CdmiAttributeProvider> providersSet = new HashSet<>();
    providersSet.add(new FakeCdmiAttributeProvider());
    CdmiAttributeProviderRegistryImpl attrProviderRegistry = new CdmiAttributeProviderRegistryImpl(providersSet);
    CdmiAttributeProvider attrProvider = attrProviderRegistry.getProvider("any");
    assertNull(attrProvider);
    
  }

  
  @Test
  public void testGetProvider() {

    Set<CdmiAttributeProvider> providersSet = new HashSet<>();
    providersSet.add(new FakeCdmiAttributeProvider());
    CdmiAttributeProviderRegistryImpl attrProviderRegistry = new CdmiAttributeProviderRegistryImpl(providersSet);

    CdmiAttributeProvider attrProvider = attrProviderRegistry.getProvider(FakeCdmiAttributeProvider.PROVIDER_NAME);
    assertTrue(attrProvider instanceof FakeCdmiAttributeProvider);
    
  }
  
  
} // end of test class
