package org.indigo.cdmi.backend.s3;

import java.util.Date;

public class S3BucketInfo {
  
  final private String name;
  final private Date creationDate;
  
  
  public S3BucketInfo(String name, Date creationDate) {
    super();
    this.name = name;
    this.creationDate = creationDate;
  }


  public String getName() {
    return name;
  }


  public Date getCreationDate() {
    return creationDate;
  }  
 
  
  @Override
  public String toString() {
    
    return this.name + "; " + this.creationDate;
  }
  
}
