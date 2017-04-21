package org.indigo.cdmi.backend.exports;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class ExportAttributeProviderRegistryImpl implements ExportAttributeProviderRegistry {

  private ConcurrentMap<String, ExportAttributeProvider> providersMap = new ConcurrentHashMap<>();

  
  /**
   * Empty, default constructor used to create registry which initially has 
   * no any provider registered. 
   */
  public ExportAttributeProviderRegistryImpl() { }

  /**
   * Constructor.
   * 
   * @param providersSet set of {@link ExportAttributeProvider} classes to be injected to 
   *        this registry
   */
  @Inject
  public ExportAttributeProviderRegistryImpl(Set<ExportAttributeProvider> providersSet) {
    
    if (providersSet != null) {
      providersSet.forEach(provider -> this.register(provider.getProviderName(), provider));  
    }
    
  } // constructor()

  @Override
  public ExportAttributeProvider register(String providerName, ExportAttributeProvider provider) {
    
    return providersMap.put(providerName, provider);
    
  } // getProvider()
  
  @Override
  public ExportAttributeProvider getProvider(String providerName) {
    
    return providersMap.get(providerName);
  
  } // getProvider()
  
  
} // end of ExportAttributeProviderRegistry class
