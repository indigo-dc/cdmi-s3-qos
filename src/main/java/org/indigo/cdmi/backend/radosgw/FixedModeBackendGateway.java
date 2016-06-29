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
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
//import org.json.
import org.slf4j.LoggerFactory;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * 
 * This class is meant only for internal testing purposes.
 * 
 * @author gracjan
 */
public class FixedModeBackendGateway implements BackendGateway {

	private static final Logger log = LoggerFactory.getLogger(FixedModeBackendGateway.class);
	
	private static final String PARAMETER_ALL_PROFILES_FILE = "objectstore.backend-gateway.fixed-mode.all-profiles-file";
	private static final String PARAMETER_PATHS_PROFILES_FILE = "objectstore.backend-gateway.fixed-mode.paths-profiles-file";
	
	private GatewayConfiguration config = GatewayConfiguration.getInstance();
	
	private String allProfilesFilePath 		= null;
	private String pathsProfilesFilePath 	= null;
	
	
	/**
	 * 
	 */
	public FixedModeBackendGateway() {
		
		allProfilesFilePath = config.get(PARAMETER_ALL_PROFILES_FILE);
		if(allProfilesFilePath == null) {
			//to avoid logs pollution it will be logged on ObjectStoreBacked's method level (entry point to SPI)
			//log.error("Cannot instatiate DummyBackendGateway. Could not find {} parameter in conifguration resources.", PARAMETER_ALL_PROFILES_FILE);
			throw new RuntimeException("Cannot instatiate FixedModeBackendGateway. Could not find " + PARAMETER_ALL_PROFILES_FILE + " parameter in conifguration resources.");
		}
		
		pathsProfilesFilePath = config.get(PARAMETER_PATHS_PROFILES_FILE);
		if(allProfilesFilePath == null) {
			//to avoid logs pollution it will be logged on ObjectStoreBacked's method level (entry point to SPI)
			//log.error("Cannot instatiate DummyBackendGateway. Could not find {} parameter in conifguration resources.", PARAMETER_ALL_PROFILES_FILE);
			throw new RuntimeException("Cannot instatiate FixedModeBackendGateway. Could not find " + PARAMETER_PATHS_PROFILES_FILE + " parameter in conifguration resources.");
		}
		
		
		
	} // FixedModeBackendGateway()
	
	
	@Override
	public String getAllProfiles() {
		
		/*
		 * Open (as InputStream) file with definition of all available profiles
		 */
		InputStream fis = null;
		try {
			fis = new FileInputStream(allProfilesFilePath);
		} catch (FileNotFoundException e) {
			//to avoid logs pollution it will be logged on ObjectStoreBacked's method level (entry point to SPI)
			//log.error("Failed to open {} file.", allProfilesFilePath);
			throw new RuntimeException(String.format("Failed to open %s file.", allProfilesFilePath), e);
		}
		
		
		/*
		 * wrap BufferedReader around InputStream, 
		 * it will allow to read data from InputStream line by line
		 */
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			try {fis.close();} catch (IOException ex) {}
			throw new RuntimeException(String.format("File with definition of all available profiles %s is encoded in unsupported format", allProfilesFilePath), e);
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
		
			throw new RuntimeException(String.format("Failed to read definition of all available profiles from %s", allProfilesFilePath), e);
		
		} finally {
			
			try {fis.close();} catch (IOException ex) {}
		
		}
		
		return stringBuilder.toString();
		
	} // getAllProfiles()

	
	/**
	 * @see BackendGateway#getPathProfile(String)
	 */
	@Override
	public String getPathProfile(String path) {
	
		/*
		 * get bucket name
		 */
		String bucketName = S3Utils.getBucketNameFromPath(path);
		log.debug("Bucket name: {}", bucketName);
		
		
		/*
		 * create (read from file) JSONObject with bucket=>profile map
		 */
		JSONObject bucketsMap = null;
		try {
			bucketsMap = JSONUtils.createJSONObjectFromFile(pathsProfilesFilePath);
		} catch (Exception e) {
			throw new RuntimeException("Failed to read buckets map from file " + pathsProfilesFilePath);
		}
		log.debug("Buckets map: {}", bucketsMap);
		
		/*
		 * create (read from file) JSONArray with all profiles
		 */
		JSONArray profilesArray = null;
		try {
			profilesArray = JSONUtils.createJSONArrayFromFile(allProfilesFilePath);			
		} catch (Exception e) {
			throw new RuntimeException("Failed to read all profiles array from file " + pathsProfilesFilePath);
		}
		log.debug("All (available/configured) profiles: {}", profilesArray);
				
		/*
		 * get profile name
		 */
		String profileName = null;
		try {
			profileName = bucketsMap.getString(bucketName);	
		} catch(JSONException e) {
			throw new RuntimeException("Bunket of name " + bucketName + " is not mapped to profile in " + pathsProfilesFilePath + " file");
		} catch (Exception e) {
			throw new RuntimeException("Failed to map bucket of name " + bucketName + " to profile name.");
		}
		log.debug("Profile name for bucket {} is {}", bucketName, profileName);
		
		
		/*
		 * find profile of given name (iterate through all profiles)
		 */
		JSONObject wantedObject = null;
		for(int i=0; i<profilesArray.length(); i++) {
			JSONObject currJSONObject = profilesArray.getJSONObject(i);
			String currentProfileName = currJSONObject.getString("name");
			log.debug("Processing profile: {}", currJSONObject);
			log.debug("Processing profile's name: {}", currentProfileName);
			if(profileName.equals(currentProfileName)) {
				log.debug("*****************************************************");
				wantedObject = currJSONObject;
				break;
			}
		}
		
		log.debug("wanted object: {}", wantedObject );
		
		if(wantedObject == null) {
			throw new RuntimeException("Failed to find profile of name " + profileName + ". Probably there is no such profile in file " + allProfilesFilePath);
		}
		
		return wantedObject.toString();
	
	} // getPathProfile()

	
} // end of FixedModeBackendGateway class
