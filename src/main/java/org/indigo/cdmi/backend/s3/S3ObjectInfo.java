package org.indigo.cdmi.backend.s3;

import java.util.Date;

public class S3ObjectInfo {


  private final String bucketName;
  private final String objectName;
  private final Date creationDate;
  private final boolean isDir;
  
  /**
   * 
   * @param bucketName
   * @param objectName
   * @param creationDate
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
