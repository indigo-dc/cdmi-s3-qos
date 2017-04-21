/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.s3;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Set of auxiliary methods to deal with S3 related stuff.
 * 
 * @author @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class S3Utils {

  private final static Logger log = LoggerFactory.getLogger(S3Utils.class);
  
  /**
   * 
   * @param path
   * @return
   */
  public static String getPrefixFromPath(String path) {
    
    log.debug("getPrefixFromPath({})", path);
    
    if (path == null) {
      throw new IllegalArgumentException("path cannot be null");
    }

    if (path.length() == 0) {
      throw new IllegalArgumentException("path cannot be empty string");
    }
    
    String bucketName = getBucketNameFromPath(path);
    log.debug("Bucket name derived form path {} is {}", path, bucketName);

    int bucketIndex = StringUtils.indexOf(path, bucketName);
    log.debug("Bucket index: {}", bucketIndex);
    
    int prefixBeginning = bucketIndex + bucketName.length();
    log.debug("Preffix beginning index: " + prefixBeginning);
    
    // if the first character at prefixBeginning position is '/' then skip it
    if (path.length() > prefixBeginning && path.charAt(prefixBeginning) == '/') {
      prefixBeginning++;
    }
    
    String prefix = path.substring(prefixBeginning);
    
    return prefix;
    
  } // getPrefixFromPath()
  
  
  /**
   * Translates CDMI path to bucket name of underlying S3 object.
   * 
   * @param path CDMI path received from CDMI server.
   * 
   * @return Name of bucket which contains the related CDMI object.
   */
  public static String getBucketNameFromPath(String path) {
    
    log.debug("getBucketFromPath({})", path);
    
    if (path == null) {
      throw new IllegalArgumentException("path cannot be null");
    }

    if (path.length() == 0) {
      throw new IllegalArgumentException("path cannot be empty string");
    }

    if (path.equals("/")) {
      return "";
    }
    
    int startIndex = 0;
    int endIndex = 0;
    int pathLength = path.length();

    /*
     * skip leading '/' characters (it can be a few in a row)
     */
    while (path.charAt(startIndex) == '/' && startIndex < pathLength - 1) {
      startIndex++;
    }

    /*
     * if start index is last character in path then invalid path
     */
    if ((startIndex == (pathLength - 1)) && (path.charAt(startIndex) == '/')) {
      throw new IllegalArgumentException(
          "Invalid path. The path argument contains only '/' signs.");
    } // if()

    /*
     * look for next '/' character (that is determine the value of endIndex)
     */
    endIndex = path.indexOf('/', startIndex + 1);

    /*
     * if no another '/' character found then the endIndex is at the end of the path string
     */
    if (endIndex == -1) {
      endIndex = pathLength;
    }

    /*
     * return substring denoted by startIndex and endIndex
     */
    return path.substring(startIndex, endIndex);

  }

} // end of S3Utils class
