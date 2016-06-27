/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for objects which implement GatewayResponseTranslator interface.
 * The FQCN of the class which will be created is read from 
 * "objectstore.gateway-response-translator" configuration parameter.
 * 
 * The value of this parameter is read from default instance of GatewayConfiguration class.
 * 
 * @author Gracjan Jankowski
 */
public class GatewayResponseTranslatorFactory {

	private static final Logger log = LoggerFactory.getLogger(GatewayResponseTranslatorFactory.class);
	
	private static final String PARAMETER_INSTANCE_FQCN = "objectstore.gateway-response-translator";
	
	
	/**
	 * 
	 * @return Newly created instance of object which implements GatewayResponseTranslator interface.
	 * The being created object is chosen basing on value of "objectstore.gateway-response-translator" read
	 * from default instance of GatewayConfiguration
	 */
	public static GatewayResponseTranslator create() {
		
		GatewayConfiguration config = GatewayConfiguration.getInstance();
		
		String translatorClass = config.get(GatewayResponseTranslatorFactory.PARAMETER_INSTANCE_FQCN);
		
		if(translatorClass == null) {
			log.error("Cannot create GatewayResponseTranslator. Parameter {} is not defined in configuration resources", GatewayResponseTranslatorFactory.PARAMETER_INSTANCE_FQCN);
			throw new RuntimeException("Cannot create GatewayResponseTranslator. Parameter " + GatewayResponseTranslatorFactory.PARAMETER_INSTANCE_FQCN + " is not defined in configuration resources");
		}
		log.info("Actual GatewayResponseTranslator: {}", translatorClass);

		
		try {
			Class<?> clazz = Class.forName(translatorClass);
			return (GatewayResponseTranslator) clazz.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			log.error("Failed to create instance of {} class; exception: {};", translatorClass, e);
			throw new RuntimeException(e);
		}

		
	} // create()
	
}
