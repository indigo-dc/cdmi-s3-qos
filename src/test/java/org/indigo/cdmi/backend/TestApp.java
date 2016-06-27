/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend;

import org.indigo.cdmi.backend.radosgw.BackendGateway;
import org.indigo.cdmi.backend.radosgw.BackendGatewayFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestApp {

	private static final Logger log = LoggerFactory.getLogger(TestApp.class);
	
	public static void main(String[] args) {

		log.debug("Create BackendGateway instance");
		BackendGateway backendGateway = BackendGatewayFactory.create();

		String jsonString = backendGateway.getAllProfiles();
		log.debug("JSON returned by BackendGateway: {}", jsonString);
		
		JSONArray jsonArray = new JSONArray(jsonString);
		log.debug("JSONObject created from JSON string: {}", jsonArray);
		
		for(int i=0; i<jsonArray.length(); i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			log.debug("JSONObject: {}", jsonObject);
		}
		

		String path = "s3-test";
		String pathProfile = backendGateway.getPathProfile(path);
		log.debug("Profile for 's3-test' path: {}", pathProfile);

		
//		String path = "golden";
//		String pathProfile = backendGateway.getPathProfile(path);
//		log.debug("Profile for 'golden' path: {}", pathProfile);
//		
//		path = "silver";
//		pathProfile = backendGateway.getPathProfile(path);
//		log.debug("Profile for 'silver' path: {}", pathProfile);
//
//		path = "standard";
//		pathProfile = backendGateway.getPathProfile(path);
//		log.debug("Profile for 'standard' path: {}", pathProfile);
//		
//		path = "standard/any/path";
//		pathProfile = backendGateway.getPathProfile(path);
//		log.debug("Profile for 'standard' path: {}", pathProfile);
//		
//
//		path = "/standard/any/path";
//		pathProfile = backendGateway.getPathProfile(path);
//		log.debug("Profile for 'standard' path: {}", pathProfile);

		
	} // main()

}
