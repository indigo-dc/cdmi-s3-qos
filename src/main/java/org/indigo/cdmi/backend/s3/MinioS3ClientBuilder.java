package org.indigo.cdmi.backend.s3;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

/**
 * 
 * @author gracjan
 *
 */
public class MinioS3ClientBuilder {

  /**
   * 
   * @param connectionProperties
   * @return
   * @throws InvalidEndpointException
   * @throws InvalidPortException
   */
  public MinioClient buildMinioClient(S3ConnectionProperties connectionProperties) 
      throws InvalidEndpointException, InvalidPortException {
  
    return new MinioClient(
        connectionProperties.getEndpoint(), 
        connectionProperties.getAccessKey(), 
        connectionProperties.getSecretKey());
  
  } // buildMinioClient()
  

} // end of MinioS3ClientBuilder class
