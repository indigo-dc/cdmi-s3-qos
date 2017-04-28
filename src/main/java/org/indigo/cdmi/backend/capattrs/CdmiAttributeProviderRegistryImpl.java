package org.indigo.cdmi.backend.capattrs;


import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Default implementation of {@link CdmiAttributeProviderRegistry}.
 * 
 * @author Gracjan Jankowski
 */
@Singleton
public class CdmiAttributeProviderRegistryImpl implements CdmiAttributeProviderRegistry {

  
  private ConcurrentMap<String, CdmiAttributeProvider> providersMap = new ConcurrentHashMap<>();
  
  
  public CdmiAttributeProviderRegistryImpl() {
    
  }
  
  
  /**
   * Constructor.
   * 
   * @param providersSet Set of to be registered {@link CdmiAttributeProvider}s 
   *        (used by Guice DI manager). 
   */
  @Inject
  public CdmiAttributeProviderRegistryImpl(Set<CdmiAttributeProvider> providersSet) {
    
    providersSet.forEach(provider -> this.register(provider.getProviderName(), provider));
    
  } // constructor()
  
  
  /* (non-Javadoc)
   * @see org.indigo.cdmi.backend.capattrs.CdmiAttributeProviderRegistry#register(
   *        java.lang.String, org.indigo.cdmi.backend.capattrs.CdmiAttributeProvider)
   */
  @Override
  public CdmiAttributeProvider register(String providerName, CdmiAttributeProvider provider) {
    
    return providersMap.put(providerName, provider);
    
  } // getProvider()
  
  
  /* (non-Javadoc)
   * @see org.indigo.cdmi.backend.capattrs.CdmiAttributeProviderRegistry
   *        #getProvider(java.lang.String)
   */
  @Override
  public CdmiAttributeProvider getProvider(String providerName) {
    
    return providersMap.get(providerName);
  
  } // getProvider()
  
  
} // end of CdmiAttributeProviderRegistry class
