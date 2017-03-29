package org.indigo.cdmi.backend.s3;

import java.util.List;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.*;



/**
 * 
 * @author gracjan
 *
 */
public class MinioS3GatewayTest {

//  private int port = 0;
//  private MinioS3Gateway minioS3Gateway = null;
//  
//  @Rule
//  public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort().usingFilesUnderClasspath("templates/wiremock"));
//  
//  
//  @Before
//  public void setUp() throws Exception {
//    
//    this.port = wireMockRule.port();
//    this.minioS3Gateway = new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesTestingProvider(this.port));
//    
//  }
//
//  
//  @Test
//  public void testListBuckets() {
//    
//    List<S3BucketInfo> bucketsList = null;
//    try {
//      bucketsList = this.minioS3Gateway.listBuckets();
//    } catch (Exception e) {
//      e.printStackTrace();
//      throw e;
//    }
//
//    assertEquals(bucketsList.size(), 3);
//    
//  } // testListBuckets
//
//  
//  @Test
//  public void testPositiveBucketExists() {
//
//    boolean bucketExists = this.minioS3Gateway.bucketExists("standard");
//    assertEquals(bucketExists, true);
//    
//  } //  // testPositiveBucketExists()
//  
//  
//  @Test
//  public void testNegativeBucketExists() {
//
//    boolean bucketExists = this.minioS3Gateway.bucketExists("nonexisting");  
//    assertEquals(bucketExists, false);    
//  
//  } // testNegativeBucketExists()
//  
//    
//  @Test
//  public void testListObjectsWithEmptyPrefix() {
//    
//    List<S3ObjectInfo> objectsList = this.minioS3Gateway.listObjects("standard", "");
//
//    assertEquals(objectsList.size(), 2);
//    
//    assertEquals(objectsList.get(0).getObjectName(), "file1.txt");
//    assertEquals(objectsList.get(1).getObjectName(), "dir1/");
//    
//  } // testListObjectsWithEmptyPrefix()
//  
//  
//  @Test
//  public void testListObjectsWithNonEmptyPrefix() {
//    
//    List<S3ObjectInfo> objectsList = this.minioS3Gateway.listObjects("standard", "dir1/");
//
//    assertEquals(objectsList.size(), 1);
//    
//    assertEquals(objectsList.get(0).getObjectName(), "file2.txt");
//    
//  } // testListObjectsWithEmptyPrefix()
//  
//
//  @Test
//  public void testExistingObjectGetObjectInfo() {
//    
//    S3ObjectInfo objectInfo1 = this.minioS3Gateway.getObjectInfo("standard", "file1.txt");
//    assertEquals(objectInfo1.getBucketName(), "standard");
//    assertEquals(objectInfo1.getObjectName(), "file1.txt");
//   
//    S3ObjectInfo objectInfo2 = this.minioS3Gateway.getObjectInfo("standard", "dir1/file2.txt");
//    assertEquals(objectInfo2.getBucketName(), "standard");
//    assertEquals(objectInfo2.getObjectName(), "dir1/file2.txt");   
//    
//  } // testExistingObjectGetObjectInfo()
//
//  
//  @Test(expected = RuntimeException.class)
//  public void testNonExistingObjectGetObjectInfo() {
//    
//    this.minioS3Gateway.getObjectInfo("standard", "dir1/");
//    
//     
//  } // testNonExistingObjectGetObjectInfo()
//
//  
  
} // end of MinioS3GatewayTest
