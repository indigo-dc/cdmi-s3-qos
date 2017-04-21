package org.indigo.cdmi.backend.exports;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

public class ExportAttributeProviderRegistryImplTest {

  
  ExportAttributeProviderRegistry exportAttributeProviderRegistry = null;
  
  @Before
  public void setUp() throws Exception {
    exportAttributeProviderRegistry = new ExportAttributeProviderRegistryImpl();
  }

  @Test
  public void register() {
  
    ExportAttributeProvider patternExportAttributeProvider = new ExportPatternUrl();
    exportAttributeProviderRegistry.register(ExportPatternUrl.EXPORT_PATTERN_URL, patternExportAttributeProvider);
    
    ExportAttributeProvider provider = exportAttributeProviderRegistry.getProvider(ExportPatternUrl.EXPORT_PATTERN_URL);
    
    assertSame(patternExportAttributeProvider, provider);
    
  } // end of test

  @Test
  public void getProvider() {
    
    ExportAttributeProvider provider = exportAttributeProviderRegistry.getProvider("any-non-existing-provider");
    assertEquals(null, provider);
    
  }

  @Test
  public void nullAsProvidersSet() {
  
        
    ExportAttributeProviderRegistryImpl registryImpl = new ExportAttributeProviderRegistryImpl(null);

    ExportAttributeProvider patternExportAttributeProvider = new ExportPatternUrl();
    
    registryImpl.register(ExportPatternUrl.EXPORT_PATTERN_URL, patternExportAttributeProvider);
    
    ExportAttributeProvider provider = registryImpl.getProvider(ExportPatternUrl.EXPORT_PATTERN_URL);
    
    assertEquals(provider, patternExportAttributeProvider);
    
  } // end of test
  
  
  @Test
  public void constructorWithArguments() {

    ExportAttributeProvider patternExportAttributeProvider = new ExportPatternUrl();
    
    Set<ExportAttributeProvider> providersSet = Sets.newHashSet(patternExportAttributeProvider);
    
    ExportAttributeProviderRegistryImpl registryImpl = new ExportAttributeProviderRegistryImpl(providersSet);
    
    ExportAttributeProvider provider = registryImpl.getProvider(ExportPatternUrl.EXPORT_PATTERN_URL);
    
    assertEquals(ExportPatternUrl.EXPORT_PATTERN_URL, provider.getProviderName());
  
  } // end of test
  
} // end of ExportAttributeProviderRegistryImplTest class
