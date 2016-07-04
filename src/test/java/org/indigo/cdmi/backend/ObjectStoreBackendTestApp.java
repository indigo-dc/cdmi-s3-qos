/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend;

import org.indigo.cdmi.BackEndException;
import org.indigo.cdmi.CdmiObjectStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectStoreBackendTestApp {

	private static final Logger log = LoggerFactory.getLogger(ObjectStoreBackendTestApp.class);
	
	public static void main(String[] args) {
		ObjectStoreBackend objectStoreBackend = new ObjectStoreBackend();

		//log.debug("Before objectStoreBackend.getCapabilities();");
		//objectStoreBackend.getCapabilities();
		//log.debug("After objectStoreBackend.getCapabilities();");

		
		String path = "standard/subdir";
		CdmiObjectStatus cdmiObjectStatus = null;
		try {
			cdmiObjectStatus = objectStoreBackend.getCurrentStatus(path);
		} catch (BackEndException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("Object status for path: {} is {}", path, cdmiObjectStatus);
		
		
	}

}
