package org.indigo.cdmi.backend.capattrs;



public interface CdmiAttributeProviderRegistry {

  /**
   * Registers implementation of CdmiAttributeProvider.
   * 
   * @param providerName Name associated with the being registered {@code provider}.
   * @param provider Actual implementation of being registered {@link CdmiAttributeProvider}.
   * @return null if there was not provider with {@code providerName}, otherwise previous provider. 
   */
  CdmiAttributeProvider register(String providerName, CdmiAttributeProvider provider);

  /**
   * Gives access to registered {@link CdmiAttributeProvider} implementations (called providers).
   * @param providerName Name of requested provider. The same name had to be used with 
   *        {@link CdmiAttributeProviderRegistry#register(String, CdmiAttributeProvider)} method.
   * @return Provider registered under {@code providerName} name or null if there is no 
   *        such provider registered.
   */
  CdmiAttributeProvider getProvider(String providerName);

}
