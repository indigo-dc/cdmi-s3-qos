package org.indigo.cdmi.backend.capattrs;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.inject.Inject;
import com.google.inject.Singleton;


/**
 * 
 * @author gracjan
 *
 */
@Singleton
public class CdmiAttributeProviderRegistry {

  
  private ConcurrentMap<String, CdmiAttributeProvider> providersMap = new ConcurrentHashMap<>();
  
  
  public CdmiAttributeProviderRegistry() {
    
  }
  
  
  @Inject
  public CdmiAttributeProviderRegistry(Set<CdmiAttributeProvider> providersSet) {
    
    providersSet.forEach(provider -> this.register(provider.getProviderName(), provider));
    
  } // constructor()
  
  
  /**
   * 
   * @param providerName
   * @param provider
   * @return
   */
  public CdmiAttributeProvider register(String providerName, CdmiAttributeProvider provider) {
    
    return providersMap.put(providerName, provider);
    
  } // getProvider()
  
  
  /**
   * 
   * @param providerName
   * @return
   */
  public CdmiAttributeProvider getProvider(String providerName) {
    
    return providersMap.get(providerName);
  
  } // getProvider()
  
  
} // end of CdmiAttributeProviderRegistry class
