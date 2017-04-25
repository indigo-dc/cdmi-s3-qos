package org.indigo.cdmi.backend.s3;

import java.util.Date;

public class S3BucketInfo {
  
  private final String name;
  private final Date creationDate;
  
  /**
   * Constructor. 
   * 
   * @param name Name of underlying bucket.
   * @param creationDate Creation date of underlying bucket.
   */
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
