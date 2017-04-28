package org.indigo.cdmi.backend.s3;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class S3ObjectInfoTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void test() {
    
    Date creationDate = new Date();
    boolean isDir = true;
    S3ObjectInfo objectInfo = new S3ObjectInfo("bucketName", "objectName", creationDate, isDir);
    
    String objectInfoAsString = objectInfo.toString();
    assertNotNull(objectInfoAsString);

    boolean isDirResult = objectInfo.isDir();
    assertTrue(isDirResult);
    
    String bucketName = objectInfo.getBucketName();
    assertEquals("bucketName", bucketName);
    
    String objectName = objectInfo.getObjectName();
    assertEquals("objectName", objectName);
    
    Date returnedCreationDate = objectInfo.getCreationDate();
    assertEquals(creationDate, returnedCreationDate);
    
    
  }

}
