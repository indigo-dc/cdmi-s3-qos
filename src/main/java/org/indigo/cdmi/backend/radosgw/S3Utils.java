/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

/**
 * Set of auxiliary methods to deal with S3 related stuff.
 * 
 * @author @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class S3Utils {

  /**
   * Translates CDMI path to bucket name of underlying S3 object.
   * 
   * @param path CDMI path received from CDMI server.
   * 
   * @return Name of bucket which contains the related CDMI object.
   */
  public static String getBucketNameFromPath(String path) {
    if (path == null) {
      throw new IllegalArgumentException("path cannot be null");
    }

    if (path.length() == 0) {
      throw new IllegalArgumentException("path cannot be empty string");
    }

    int startIndex = 0;
    int endIndex = 0;
    int pathLength = path.length();

    /*
     * skip leading '/' characters
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
