package org.indigo.cdmi.backend.s3;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* (non-Javadoc)
 * 
 * Fake S3Gateway implementaion 
 */
class FakeS3Gateway implements S3Gateway {

  private static final Logger log = LoggerFactory.getLogger(FakeS3Gateway.class);
  
  @Override
  public boolean bucketExists(String bucketName) {
    return false;
  }

  @Override
  public S3ObjectInfo getObjectInfo(String bucketName, String objectName) {
    S3ObjectInfo s3ObjectInfo = new S3ObjectInfo("bucketName", "file.bin", new Date(), false);
    return s3ObjectInfo;
  }

  @Override
  public List<S3BucketInfo> listBuckets() {
    
    List<S3BucketInfo> bucketsList = new ArrayList<>();

    S3BucketInfo bucketInfo = new S3BucketInfo("any", new Date());
    bucketsList.add(bucketInfo);
    
    return bucketsList;
    
  }

  
  @Override
  public List<S3ObjectInfo> listObjects(String bucketName, String prefix) {
    
    List<S3ObjectInfo> objectsList = new ArrayList<>();
  
    log.error("PREFIX: {}", prefix);
    
    if(prefix.equals("file.txt")) {
      S3ObjectInfo objectInfo = new S3ObjectInfo("any", "file.txt", new Date(), false);
      objectsList.add(objectInfo);
    } else if (bucketName.equals("any")) {
      S3ObjectInfo objectInfo = new S3ObjectInfo("any", "/", new Date(), true);
      objectsList.add(objectInfo);
      S3ObjectInfo objectInfo1 = new S3ObjectInfo("any", "raz", new Date(), true);
      objectsList.add(objectInfo1);
      S3ObjectInfo objectInfo2 = new S3ObjectInfo("any", "dwa", new Date(), true);
      objectsList.add(objectInfo2);
    } else {
      S3ObjectInfo objectInfo = new S3ObjectInfo("any", "/", new Date(), true);
      objectsList.add(objectInfo);
    }
    
    return objectsList;
 
  }
  
} // end of FakeS3Gateway


/* (non-Javadoc)
 * 
 * S3FacadeImpl test
 */
public class S3FacadeImplTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void test() {
    S3Facade s3Facade = new S3FacadeImpl(new FakeS3Gateway());
  }

  @Test(expected=IllegalArgumentException.class)
  public void testIsContainerWithNullArg() {
    S3Facade s3Facade = new S3FacadeImpl(new FakeS3Gateway());
    s3Facade.isContainer(null);
  }
  
  
  @Test
  public void testIsContainer() {
    S3Gateway fakeS3Gateway = new FakeS3Gateway();
    S3Facade s3Facade = new S3FacadeImpl(fakeS3Gateway);
    boolean isBucketContainerTest = s3Facade.isContainer("/bucketName");
    assertTrue(isBucketContainerTest);
    
    boolean isEmptyPathContainerTest = s3Facade.isContainer("");
    assertTrue(isEmptyPathContainerTest);
    
    boolean isRootContainerTest = s3Facade.isContainer("/");
    assertTrue(isRootContainerTest);
    
    boolean isPathContainerTest = s3Facade.isContainer("/bucketName/folder");
    assertTrue(isPathContainerTest);
    

    boolean isFileContainerTest = s3Facade.isContainer("/bucketName/file.txt");
    assertFalse(isFileContainerTest);

    
    //String path = "/bucket/path";
    //String bucket = S3Utils.getBucketNameFromPath(path);
    //String prefixPart = S3Utils.getPrefixFromPath(path);
    //fakeS3Gateway.listObjects(bucket, prefixPart);
    
  }
  
  
  @Test
  public void testGetChildren() {
    
    S3Gateway fakeS3Gateway = new FakeS3Gateway();
    S3Facade s3Facade = new S3FacadeImpl(fakeS3Gateway);
    List<String> bucktetsList = s3Facade.getChildren("/");
    assertFalse(bucktetsList.isEmpty());

    List<String> bucktetsList1 = s3Facade.getChildren("");
    assertFalse(bucktetsList1.isEmpty());

    List<String> bucktetsList2 = s3Facade.getChildren("/any");
    assertFalse(bucktetsList2.isEmpty());
    
  }
  

  @Test
  public void getObjectProperties() {
    
    S3Gateway fakeS3Gateway = new FakeS3Gateway();
    S3Facade s3Facade = new S3FacadeImpl(fakeS3Gateway);
    Properties properties = s3Facade.getObjectProperties("/bucketName/file.bin");
  
  }

  
} // end of S3FacadeImplTest
