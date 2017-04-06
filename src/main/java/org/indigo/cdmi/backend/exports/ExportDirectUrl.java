package org.indigo.cdmi.backend.exports;

public class ExportDirectUrl implements ExportAttributeProvider {

  public static final String EXPORT_DIRECT_URL = "export-direct-url"; 
  
  @Override
  public String getProviderName() {
    return EXPORT_DIRECT_URL;
  }

  @Override
  public String attributeValue(String path, String attributePlaceholder) {
    
    String trimedPlaceholder = attributePlaceholder.trim();
    
    String rawPlaceholder = trimedPlaceholder.substring(1, trimedPlaceholder.length() - 1);
    
    String[] args = rawPlaceholder.split("\\s+");
    
    String baseAddr = args[1];
    
    return baseAddr + path;
  
  }

}
