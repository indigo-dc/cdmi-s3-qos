package org.indigo.cdmi.backend.s3;

import static org.junit.Assert.*;

import org.indigo.cdmi.backend.radosgw.DefaultBackendConfiguration;
import org.indigo.cdmi.backend.radosgw.config.ConfigurationContext;
import org.junit.Before;
import org.junit.Test;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

public class MinioS3ClientBuilderTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void testEmptyConstructor() {
    
    MinioS3ClientBuilder minioS3ClientBuilder = new MinioS3ClientBuilderImpl();
    
  }

  
  @Test
  public void testBuildMinioClient() throws InvalidEndpointException, InvalidPortException {
    
    ConfigurationContext.registerConfiguration(DefaultBackendConfiguration.getInstance("minioclient"));
    MinioS3ClientBuilder minioS3ClientBuilder = new MinioS3ClientBuilderImpl();
    
    S3ConnectionPropertiesDefaultProvider connectionPropertiesDefaultProvider = new S3ConnectionPropertiesDefaultProvider();
    
    S3ConnectionProperties connectionProperties = connectionPropertiesDefaultProvider.getConnectionProperties();
    
    MinioClient minioClient = minioS3ClientBuilder.buildMinioClient(connectionProperties);
    assertNotNull(minioClient);
    
  }

  
}
