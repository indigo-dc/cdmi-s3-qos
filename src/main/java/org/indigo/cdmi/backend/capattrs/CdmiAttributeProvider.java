package org.indigo.cdmi.backend.capattrs;

public interface CdmiAttributeProvider {

  String getProviderName();
  
  String attributeValue(String objectPath, String attributeName, String attributePattern);

} // CdmiAttributeProvider
