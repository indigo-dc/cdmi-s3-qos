package org.indigo.cdmi.backend.s3;

public class S3ConnectionPropertiesTestingProvider implements S3ConnectionPropertiesProvider {

  int port;
  
  public S3ConnectionPropertiesTestingProvider(int port) {
    this.port = port;
  }
  
  @Override
  public S3ConnectionProperties getConnectionProperties() {
    return new S3ConnectionProperties("http://localhost:" + String.valueOf(this.port), "", "");
  }

}
