package org.indigo.cdmi.backend.s3;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;


public interface MinioS3ClientBuilder {

  /**
   * Using passed connectionProperties creates instance of MinioClient.
   * 
   * @param connectionProperties Contains connection properties required to create MinioClient.
   * 
   * @return Instance of MinioClient related with S3 server and account 
   *        determined by {@code connectionProperties}. 
   * 
   * @throws InvalidEndpointException One of exceptions potentially 
   *        being thrown by MinioClient constructor.
   * @throws InvalidPortException One of exceptions potentially being 
   *        thrown by MinioClient constructor.
   */
  MinioClient buildMinioClient(S3ConnectionProperties connectionProperties)
      throws InvalidEndpointException, InvalidPortException; 

}
