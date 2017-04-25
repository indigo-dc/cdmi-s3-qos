package org.indigo.cdmi.backend.s3;

import java.util.Date;

public class S3ObjectInfo {


  private final String bucketName;
  private final String objectName;
  private final Date creationDate;
  private final boolean isDir;
  
  /**
   * Constructor.
   * 
   * @param bucketName Bucket name.
   * @param objectName Object name (without bucket part).
   * @param creationDate Creation date of underlying object.
   * @param isDir Specifies is underlying object is dir or not. 
   */
  public S3ObjectInfo(String bucketName, String objectName, Date creationDate, boolean isDir) {
    super();
    this.bucketName = bucketName;
    this.objectName = objectName;
    this.creationDate = creationDate;
    this.isDir = isDir;
  }

  public String getBucketName() {
    return bucketName;
  }

  public String getObjectName() {
    return objectName;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  
  public boolean isDir() {
    return this.isDir;
  }
  
  @Override
  public String toString() {
    return this.objectName;
  }
  
  
  
} // end of S3ObjectInfo class
