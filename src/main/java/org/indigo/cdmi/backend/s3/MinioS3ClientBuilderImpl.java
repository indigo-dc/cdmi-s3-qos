package org.indigo.cdmi.backend.s3;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

/**
 * 
 * @author gracjan
 *
 */
public class MinioS3ClientBuilderImpl implements MinioS3ClientBuilder {

  /* (non-Javadoc)
   * @see org.indigo.cdmi.backend.s3.MinioS3ClientBuilder#buildMinioClient(org.indigo.cdmi.backend.s3.S3ConnectionProperties)
   */
  @Override
  public MinioClient buildMinioClient(S3ConnectionProperties connectionProperties) 
      throws InvalidEndpointException, InvalidPortException {
  
    return new MinioClient(
        connectionProperties.getEndpoint(), 
        connectionProperties.getAccessKey(), 
        connectionProperties.getSecretKey());
  
  } // buildMinioClient()
  

} // end of MinioS3ClientBuilder class
