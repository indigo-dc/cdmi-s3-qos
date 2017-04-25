package org.indigo.cdmi.backend.s3;

import javax.xml.ws.Endpoint;

public class S3ConnectionProperties {

  private final String endpoint;
  
  private final String accessKey;
  
  private final String secretKey;
  
  
  /**
   * Constructor.
   * 
   * @param endpoint S3 server endpoint (as required by MinioClient constructor).
   * @param accessKey S3 access key to be used to authorize against S3 server 
   *        referred by {@code endpoint}
   * @param secretKey S3 secret key to be used to authenticate against S3 
   *        server referred by {@code endpoint}
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
