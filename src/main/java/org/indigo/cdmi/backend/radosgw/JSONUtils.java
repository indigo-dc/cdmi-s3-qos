/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.indigo.cdmi.backend.radosgw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author Gracjan Jankowski
 *
 */
public class JSONUtils {

	
	/**
	 * 
	 * @param path
	 * @return
	 */
	private static String fileToString(String path) {
		
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			//to avoid logs pollution it will be logged on ObjectStoreBacked's method level (entry point to SPI)
			//log.error("Failed to open {} file.", allProfilesFilePath);
			throw new RuntimeException(String.format("Failed to open %s file.", path), e);
		}
		
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			try {inputStream.close();} catch (IOException ex) {}
			throw new RuntimeException(String.format("File with definition of all available profiles %s is encoded in unsupported format", inputStream), e);
		}
		
		/*
		 * each line read will be added to that buffer
		 */
		StringBuffer stringBuilder = new StringBuffer();
		
		/*
		 * iterate through all available lines
		 */
		try {
			
			String line = bufferedReader.readLine();
				
			while(line != null) {
				stringBuilder.append(line);
				line = bufferedReader.readLine();
			}
		
		} catch (IOException e) {
		
			throw new RuntimeException(String.format("Failed to read definition of all available profiles from %s", path), e);
		
		} finally {
			
			try {inputStream.close();} catch (IOException ex) {}
		
		}
		
		return stringBuilder.toString();
	
	} // fileToString
	
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static JSONObject createJSONObjectFromFile(String path) {
		
		String jsonObjectAsString = fileToString(path);

		JSONObject rv = new JSONObject(jsonObjectAsString);

		return rv;
		
	} // createJSONObjectFromFile
	
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static JSONArray createJSONArrayFromFile(String path) {
	
		String jsonArrayAsString = fileToString(path);
		
		JSONArray rv = new JSONArray(jsonArrayAsString);
	
		return rv;
		
	} // createJSONArrayFromFile()
	
}
