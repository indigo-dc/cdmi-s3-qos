/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Creates objects which implement RemoteExecutor interface.
 *  
 * @author Gracjan Jankowski
 */
public class RemoteExecutorFactory {

	private static final Logger log = LoggerFactory.getLogger(RemoteExecutorFactory.class);
	

	private static final String CONF_REMOTE_EXECUTOR = "objectstore.remote-executor";
	
	
	/**
	 * 
	 * @param fqcn
	 * @param executorConfiguration
	 * @return
	 */
	public static RemoteExecutor create(String fqcn, Properties executorConfiguration) {
		
		RemoteExecutor rv = null;
		
		try {
			
			Class<?> clazz = Class.forName(fqcn);
			
			rv = (RemoteExecutor) clazz.newInstance();
			rv.configure(executorConfiguration);
			return rv;
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			log.error("Failed to create instance of {} class; exception: {};", fqcn, e);
			throw new RuntimeException(e);
		} catch (Exception e) {
			log.error("Failed to configure instance {} object.", rv);
			throw new RuntimeException(e);
		}

	} // create()

	
	/**
	 * 
	 * @param factoryConfiguration
	 * @param executorConfiguration
	 * @return
	 */
	public static RemoteExecutor create(Properties factoryConfiguration, Properties executorConfiguration) {
		
		String executorClass = factoryConfiguration.getProperty(CONF_REMOTE_EXECUTOR);
		if(executorClass == null) {
			log.error("Cannot create RemoteExecutor. There is no {} property in passed factoryConfiguration.", CONF_REMOTE_EXECUTOR);
			throw new RuntimeException("Cannot create RemoteExecutor. There is no " + CONF_REMOTE_EXECUTOR + " property in passed factoryConfiguration");
		}
		log.info("Class configured to be RemoteExecutor: {}", executorClass);
		
		return RemoteExecutorFactory.create(executorClass, executorConfiguration);
		
	} // create()
	
	
} // end of RemoteExecutorFactory class
