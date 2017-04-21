package org.indigo.cdmi.backend.s3;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;


public interface MinioS3ClientBuilder {

  /**
   * 
   * @param connectionProperties
   * @return
   * @throws InvalidEndpointException
   * @throws InvalidPortException
   */
  MinioClient buildMinioClient(S3ConnectionProperties connectionProperties)
      throws InvalidEndpointException, InvalidPortException; // buildMinioClient()

}
