package org.indigo.cdmi.backend.capattrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CapabilityEcho implements CdmiAttributeProvider {

  
  public static final String PROVIDER_NAME = "cdmi_capability_echo";
  
  private static Logger log = LoggerFactory.getLogger(CapabilityEcho.class);
  

  
  @Override
  public String getProviderName() {
    // TODO Auto-generated method stub
    return PROVIDER_NAME;
  }

  @Override
  public String attributeValue(String objectPath, String attributeName, String attributePattern) {
    
    String trimedPattern = attributePattern.trim();
    return trimedPattern.substring(1, trimedPattern.length()-1).substring(PROVIDER_NAME.length());
  
  }

}
