package org.indigo.cdmi.backend.s3;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.xmlpull.v1.XmlPullParserException;

import static org.junit.Assert.*;


import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

/* (non-Javadoc)
 * 
 */
class FakeS3ConnectionPropertiesProvider implements S3ConnectionPropertiesProvider {

  @Override
  public S3ConnectionProperties getConnectionProperties() {
      return new S3ConnectionProperties("endpoint", "accessKey", "secretKey");
  }
  
} // end of FakeS3ConnectionPropertiesProvider


/* (non-Javadoc)
 * 
 */
class FakeMinioS3ClientBuilder implements MinioS3ClientBuilder {

  
  @Override
  public MinioClient buildMinioClient(S3ConnectionProperties connectionProperties)
      throws InvalidEndpointException, InvalidPortException {
  
    
    MinioClient minioClientMock = PowerMockito.mock(MinioClient.class); 
    
    try {
      when(minioClientMock.bucketExists("any")).thenReturn(true);
    } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException
        | InsufficientDataException | NoResponseException | ErrorResponseException
        | InternalException | IOException | XmlPullParserException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    try {
      when(minioClientMock.bucketExists("throwException")).thenThrow(new RuntimeException("Faked exception for bucketExists()."));
    } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException
        | InsufficientDataException | NoResponseException | ErrorResponseException
        | InternalException | IOException | XmlPullParserException e) {
      e.printStackTrace();
    }
    
    
    try {
      when(minioClientMock.statObject("bucket2", "dummy-object")).thenReturn(new ObjectStat("bucket2", "dummy-object", new Date(), 100, "etag", "text"));
    } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException
        | InsufficientDataException | NoResponseException | ErrorResponseException
        | InternalException | IOException | XmlPullParserException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    

    try {
      when(minioClientMock.statObject("bucket2", "dummy-object-with-exception")).thenThrow(new RuntimeException("Faked exception for statObject() method"));
    } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException
        | InsufficientDataException | NoResponseException | ErrorResponseException
        | InternalException | IOException | XmlPullParserException e) {
      e.printStackTrace();
    }

    
    //S3BucketInfo standardBucket = new S3BucketInfo("standard", new Date());
    //S3BucketInfo silverBucket = new S3BucketInfo("silver", new Date());
    //S3BucketInfo goldenBucket = new S3BucketInfo("golden", new Date());
    //List<S3BucketInfo> bucketsList = Arrays.asList(standardBucket, silverBucket, goldenBucket);
    
    Bucket standardBucket = mock(Bucket.class);
    when(standardBucket.name()).thenReturn("standard");
    when(standardBucket.creationDate()).thenReturn(new Date());

    Bucket silverBucket = mock(Bucket.class);
    when(silverBucket.name()).thenReturn("silver");
    when(silverBucket.creationDate()).thenReturn(new Date());
    
    Bucket goldenBucket = mock(Bucket.class);
    when(goldenBucket.name()).thenReturn("golden");
    when(goldenBucket.creationDate()).thenReturn(new Date());

    List<Bucket> bucketsList = Arrays.asList(standardBucket, silverBucket, goldenBucket);
    
    try {
      when(minioClientMock.listBuckets()).thenReturn(bucketsList);
    } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException
        | InsufficientDataException | NoResponseException | ErrorResponseException
        | InternalException | IOException | XmlPullParserException e) {
      e.printStackTrace();
    }


    List<Result<Item>> objectsList = new ArrayList<>();
      
    Item item1 = PowerMockito.mock(Item.class);
    //Item item1 = new Item("file1", false);
    when(item1.objectName()).thenReturn("dummy-folder/file1");
    when(item1.isDir()).thenReturn(false);
    when(item1.lastModified()).thenReturn(new Date());
    objectsList.add(new Result<Item>(item1, null));
    
    Item item2 = PowerMockito.mock(Item.class);
    //Item item2 = new Item("file2", false);
    when(item2.objectName()).thenReturn("dummy-folder/file2");
    when(item2.isDir()).thenReturn(false);      
    when(item2.lastModified()).thenReturn(new Date());
    objectsList.add(new Result<Item>(item2, null));
    
    Item item3 = PowerMockito.mock(Item.class);
    //Item item3 = new Item("dir", true);
    when(item3.objectName()).thenReturn("dummy-folder/dir");
    when(item3.isDir()).thenReturn(false);            
    when(item3.lastModified()).thenReturn(new Date());
    objectsList.add(new Result<Item>(item3, null));
      
    
    when(minioClientMock.listObjects("bucket", "dummy-folder", false)).thenReturn(objectsList);
    
    when(
        minioClientMock.listObjects("bucket", "dummy-folder-with-exception", false))
        .thenThrow(new RuntimeException("Faked exception for listObjects()")
    );
    
    
    return minioClientMock;

  } // buildMinioClient()
  
} // end of FakeMinioS3ClientBuilder


/*
 * 
 */
class FakeMinioS3ClientBuilderForMinioClientThrowingExceptionOnListBuckets implements MinioS3ClientBuilder {

  @Override
  public MinioClient buildMinioClient(S3ConnectionProperties connectionProperties)
      throws InvalidEndpointException, InvalidPortException {
    
    MinioClient minioClientMock = PowerMockito.mock(MinioClient.class);
    
    try {
      when(minioClientMock.listBuckets()).thenThrow(new RuntimeException("Faked exception for listBuckets()"));
    } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException
        | InsufficientDataException | NoResponseException | ErrorResponseException
        | InternalException | IOException | XmlPullParserException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return minioClientMock;
  }
  
} // end of FakeMinioS3ClientBuilderForMinioClientThrowingExceptionOnListBuckets

/**
 * 
 * @author Gracjan Jankowski
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MinioClient.class, Item.class})
public class MinioS3GatewayTest {

  private MinioS3Gateway minioS3Gateway = null;
  
  @Before
  public void setUp() {
    
    minioS3Gateway = new MinioS3Gateway(new FakeMinioS3ClientBuilder(), new FakeS3ConnectionPropertiesProvider());
    
  } // setUp()
  
  
  @Test
  public void testBucketExistsAny() {
    
    minioS3Gateway.bucketExists("any");
    
  } // test()
  

  @Test(expected=RuntimeException.class)
  public void testBucketExistsThrowException() {
    
    minioS3Gateway.bucketExists("throwException");
    
  } // test()

  
  @Test
  public void testListBuckets() {
    
    List<S3BucketInfo> bucketInfosList = minioS3Gateway.listBuckets();
    assertFalse(bucketInfosList.isEmpty());
      
  } // test()
  
  
  @Test(expected=RuntimeException.class)
  public void testListBucketsWithException() {
    
    minioS3Gateway = new MinioS3Gateway(
        new FakeMinioS3ClientBuilderForMinioClientThrowingExceptionOnListBuckets(), 
        new FakeS3ConnectionPropertiesProvider()
    );
    
    minioS3Gateway.listBuckets();
    
  } // test()
  
  

  @Test(expected=RuntimeException.class)
  public void testGetObjectInfoWithException() {
  
    minioS3Gateway.getObjectInfo("bucket2", "dummy-object-with-exception");
    
  } // test()

  @Test
  public void testGetObjectInfo() {
  
    
    S3ObjectInfo objInfo = minioS3Gateway.getObjectInfo("bucket2", "dummy-object");
    assertEquals("bucket2", objInfo.getBucketName());
    assertEquals("dummy-object", objInfo.getObjectName());

    //assertThat(objInfo, allOf(is("bucket2")));
    
  } // test()
  
  @Test
  public void testListObjects() {                           
    List<S3ObjectInfo> objectsList = minioS3Gateway.listObjects("bucket", "dummy-folder");
    assertFalse(objectsList.isEmpty());
  }
  

  
  @Test(expected=RuntimeException.class)
  public void testListObjectsWithException() {
    minioS3Gateway.listObjects("bucket", "dummy-folder-with-exception");
  }

  
} // end of MinioS3GatewayTest
