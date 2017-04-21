package org.indigo.cdmi.backend.capattrs;



public interface CdmiAttributeProviderRegistry {

  /**
   * 
   * @param providerName
   * @param provider
   * @return
   */
  CdmiAttributeProvider register(String providerName, CdmiAttributeProvider provider);

  /**
   * 
   * @param providerName
   * @return
   */
  CdmiAttributeProvider getProvider(String providerName);

}
