package org.indigo.cdmi.backend.exports;

public interface ExportAttributeProvider {

  String getProviderName();
  
  String attributeValue(String objectPath, String attributePlaceholder);
  
} // ExportAttributeProvider()
