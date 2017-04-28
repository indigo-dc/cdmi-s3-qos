package org.indigo.cdmi.backend.s3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class S3UtilsTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void testGetBucketName() {
    
    String bucketName = S3Utils.getBucketNameFromPath("/bucketName/and/any/path");
    assertEquals("bucketName", bucketName);
    
  }


  @Test(expected=IllegalArgumentException.class)
  public void testGetBucketNameWithNullArgument() {
    
    S3Utils.getBucketNameFromPath(null);
    
  }

  
  @Test(expected=IllegalArgumentException.class)
  public void testGetBucketNameWithEmptyArgument() {
    
    S3Utils.getBucketNameFromPath("");
    
  }


  
  @Test(expected=IllegalArgumentException.class)
  public void testGetBucketNameWithInvalidPath() {
    
    S3Utils.getBucketNameFromPath("//");
    
  }

  
  
  @Test
  public void testGetPrefixFromPath() {
    
    String prefixPath = S3Utils.getPrefixFromPath("/bucketName/and/any/path");
    assertEquals("and/any/path", prefixPath);
    
  }


  @Test
  public void testGetPrefixFromPathWithoutPrefixPart() {
    
    String prefixPath = S3Utils.getPrefixFromPath("/bucketName");
    assertEquals("", prefixPath);
    
  }


  @Test
  public void testGetPrefixFromPathWithoutPrefixPartButWithClosingSlash() {
    
    String prefixPath = S3Utils.getPrefixFromPath("/bucketName/");
    assertEquals("", prefixPath);
    
  }

  
  
  @Test(expected=IllegalArgumentException.class)
  public void testGetPrefixFromPathWithNullArgument() {
    
    S3Utils.getPrefixFromPath(null);
    
  }


  @Test(expected=IllegalArgumentException.class)
  public void testGetPrefixFromPathWithEmptyArgument() {
    
    S3Utils.getPrefixFromPath("");
    
  }

  
}
