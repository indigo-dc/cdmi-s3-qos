package org.indigo.cdmi.backend.s3;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class S3BucketInfoTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void test() {
    
    Date creationDate = new Date();
    
    S3BucketInfo bucketInfo = new S3BucketInfo("name", creationDate);
    
    String bucketInfoAsString = bucketInfo.toString();
    assertNotNull(bucketInfoAsString);
    
    Date returnedCreationDate = bucketInfo.getCreationDate();
    assertEquals(creationDate, returnedCreationDate);
    
    String returnedBucketName = bucketInfo.getName();
    assertEquals("name", returnedBucketName);
    
  }

}
