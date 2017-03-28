/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.s3;

import java.util.List;

/**
 * Provides access to underlying S3 server (list of objects, objects' properties).
 * 
 * @author @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public interface S3Gateway {

  public boolean bucketExists(String bucketName);
  
  public S3ObjectInfo getObjectInfo(String bucketName, String objectName);  
  
  public List<S3BucketInfo> listBuckets();
  
  public List<S3ObjectInfo> listObjects(String bucketName, String prefix);
  
} // end of S3Gateway class
