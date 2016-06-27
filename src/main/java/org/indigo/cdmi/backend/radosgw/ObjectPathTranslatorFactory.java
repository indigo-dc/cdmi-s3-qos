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

public class ObjectPathTranslatorFactory {
	
	private static final Logger log = LoggerFactory.getLogger(ObjectPathTranslator.class); 
	
	private static final String PARAMETER_INSTANCE_FQCN = "objectstore.object-path-translator";

	
	/**
	 * 
	 * @return
	 */
	public static ObjectPathTranslator create() {
		
		GatewayConfiguration config = GatewayConfiguration.getInstance();
		
		String translatorClass = config.get(PARAMETER_INSTANCE_FQCN);

		if(translatorClass == null) {
			log.error("Cannot create ObjectPathTranslator. Parameter {} is not defined in configuration resources", PARAMETER_INSTANCE_FQCN);
			throw new RuntimeException("Cannot create ObjectPathTranslator. Parameter " + PARAMETER_INSTANCE_FQCN + " is not defined in configuration resources");
		}
		log.info("Actual ObjectPathTranslator: {}", translatorClass);

		try {
			Class<?> clazz = Class.forName(translatorClass);
			return (ObjectPathTranslator) clazz.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			log.error("Failed to create instance of {} class; exception: {};", translatorClass, e);
			throw new RuntimeException(e);
		}

	} // create()
	
} // end of class
