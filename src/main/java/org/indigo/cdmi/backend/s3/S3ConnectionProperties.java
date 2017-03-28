package org.indigo.cdmi.backend.s3;

public class S3ConnectionProperties {

  private final String endpoint;
  
  private final String accessKey;
  
  private final String secretKey;
  
  
  /**
   * 
   * @param endpoint
   * @param accessKey
   * @param secretKey
   */
  public S3ConnectionProperties(String endpoint, String accessKey, String secretKey) {

    this.endpoint = endpoint;
    
    this.accessKey = accessKey;
    
    this.secretKey = secretKey;
    
  } // 


  public String getEndpoint() {
    return endpoint;
  }


  public String getAccessKey() {
    return accessKey;
  }


  public String getSecretKey() {
    return secretKey;
  }
    
  
} // end of S3ConnectionProperties
