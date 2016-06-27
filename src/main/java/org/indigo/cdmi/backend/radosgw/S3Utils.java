package org.indigo.cdmi.backend.radosgw;

/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

public class S3Utils {

	public static String getBucketNameFromPath(String path) {
		if(path == null) {
			throw new IllegalArgumentException("path cannot be null");
		}
		
		if(path.length() == 0) {
			throw new IllegalArgumentException("path cannot be empty string");
		}
		
		int startIndex = 0;
		int endIndex   = 0;
		int pathLength = path.length();
		
		/*
		 * skip leading '/' characters
		 */
		while(path.charAt(startIndex) == '/' && startIndex < pathLength-1) {
			startIndex++;
		}
		
		/*
		 * if start index is last character in path then invalid path
		 */
		if((startIndex == (pathLength-1)) && (path.charAt(startIndex) == '/')) throw new IllegalArgumentException("Invalid path. The path argument contains only '/' signs.");
		
		/*
		 * look for next '/' character (that is determine the value of endIndex)
		 */
		endIndex = path.indexOf('/', startIndex+1);
		
		/*
		 * if no another '/' character found then the endIndex is at the end of the path string 
		 */
		if(endIndex == -1) {
			endIndex = pathLength;
		}
		
		/*
		 * return substring denoted by startIndex and endIndex
		 */
		return path.substring(startIndex, endIndex);

	}
	
} // end of S3Utils class
