package org.indigo.cdmi.backend.capattrs;

import com.google.inject.Inject;

import org.indigo.cdmi.backend.s3.S3Facade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class CapabilityAssociationTime implements CdmiAttributeProvider {

  public static final String PROVIDER_NAME = "cdmi_capability_association_time";
  
  private static Logger log = LoggerFactory.getLogger(CapabilityAssociationTime.class);
  
  private final S3Facade s3Facade;
  
  
  /**
   * 
   */
  @Inject
  public CapabilityAssociationTime(S3Facade s3Facade) {
  
    this.s3Facade = s3Facade;
    
  } // end of constructor
  
  
  @Override
  public String attributeValue(String objectPath, String attributeName, String attributePattern) {
  
    Properties objProperties = s3Facade.getObjectProperties(objectPath);
    
    return objProperties.get(S3Facade.PROPERTY_CREATION_TIME).toString();
    
  } // attributeValue()


  @Override
  public String getProviderName() {
      return PROVIDER_NAME;
  }


} // end of CapabilityAassociationTime class
