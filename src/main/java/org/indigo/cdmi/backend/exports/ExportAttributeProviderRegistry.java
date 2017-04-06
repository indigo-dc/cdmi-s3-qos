package org.indigo.cdmi.backend.exports;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ExportAttributeProviderRegistry {

  private ConcurrentMap<String, ExportAttributeProvider> providersMap = new ConcurrentHashMap<>();

  
  public ExportAttributeProviderRegistry() {
   
  }
  
  @Inject
  public ExportAttributeProviderRegistry(Set<ExportAttributeProvider> providersSet) {
    
    if(providersSet != null) {
      providersSet.forEach(provider -> this.register(provider.getProviderName(), provider));  
    }
    
    
  } // constructor()


  public ExportAttributeProvider register(String providerName, ExportAttributeProvider provider) {
    
    return providersMap.put(providerName, provider);
    
  } // getProvider()

  
  public ExportAttributeProvider getProvider(String providerName) {
    
    return providersMap.get(providerName);
  
  } // getProvider()
  
  
} // end of ExportAttributeProviderRegistry class
