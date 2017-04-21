package org.indigo.cdmi.backend.exports;


public interface ExportAttributeProviderRegistry {

  /**
   * Registers new {@link ExportAttributeProvider} provider.
   * 
   * @param providerName Name used to register the provider. The name acts as key. 
   *        Later, the same name has to be used in order to retrieve the registered provider. 
   * 
   * @param provider The being registered provider.
   * 
   * @return The precious provider registered with the same name or null if there was no 
   *        provider with the same name.
   */
  ExportAttributeProvider register(String providerName, ExportAttributeProvider provider);

  /**
   * Gives provider registered with {@code providerName} name.
   * 
   * @param providerName
   * 
   * @return {@link ExportAttributeProvider} registered with 
   *        {@code providerName} name or null if there is no such provider.
   */
  ExportAttributeProvider getProvider(String providerName);

} // end of interface
