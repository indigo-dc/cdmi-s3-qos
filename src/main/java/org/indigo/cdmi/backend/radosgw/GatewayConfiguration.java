/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This object reads configuration files and exposes the configuration to the rest of library.
 * 
 * The default values of configuration parameters are defined in file objectstore.properties read from CLASSPATH 
 * (see src/main/resources/objectstore.propertis)
 * 
 * New parameters or parameter which override the default values can be defined in file 
 * objectstore.properties read from current working directory. 
 * 
 * The format of configuration files is the same as required by {@link Properties#load(InputStream)}
 * 
 * @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class GatewayConfiguration {

	private static final Logger log = LoggerFactory.getLogger(GatewayConfiguration.class);
	
	private static Map<String, GatewayConfiguration> configsMap = new HashMap<>();
	
	private Properties configurationProperties = new Properties();
	
	
	/**
	 * private constructor
	 */
	private GatewayConfiguration(String configName) {
		
		/*
		 * determine name of file with properties
		 */
		String propertiesFileName = null;
		
		StringBuffer propertiesFileNameBuilder = new StringBuffer();
		
		propertiesFileNameBuilder.append("config/objectstore");
		
		if(!configName.equals("")) {
			propertiesFileNameBuilder.append("-");
			propertiesFileNameBuilder.append(configName);
		}
		
		propertiesFileNameBuilder.append(".properties");
		
		propertiesFileName = propertiesFileNameBuilder.toString();
		
		/*
		 * read properties form properties file located on CLASSPATH 
		 */
		InputStream configInputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
		if(configInputStream == null) {
			throw new RuntimeException("Failed to find config file");
		}
		
		try {
			configurationProperties.load(configInputStream);
		} catch (IOException e) {
			log.error("Failed to load configuration from resource {}; message: {}, stacktrace: {}", propertiesFileName, e.getMessage(), e.getStackTrace());
			throw new RuntimeException(String.format("Failed to load configuration from resource: %s", propertiesFileName), e);
		} finally {
			try {
				configInputStream.close();
			} catch (IOException e) {
				log.error("Failed to close resource input stream {}; exception: e", propertiesFileName, e);
				throw new RuntimeException(e);
			}
		}
		
		
		/*
		 * read properties from file located in current working directory
		 */
		InputStream fis = null;
		try {
			fis = new FileInputStream(propertiesFileName);
		} catch (FileNotFoundException e) {
			log.warn("There is no configuration file {} in current working directory", propertiesFileName);
		}
		
		/*
		 * if local configuration exists then read it into configurationProperties
		 */
		try {
			
			if(fis!=null) {
				configurationProperties.load(fis);
			}
		
		} catch (IOException e) {
			
			if(fis!=null) try {fis.close();} catch (Exception ex) {}
			log.error("Failed to read local configuration file {} from current working directory", propertiesFileName);
			throw new RuntimeException("Failed to read local configuration file " + propertiesFileName, e);
		
		} finally {
			
			try {if(fis!=null)fis.close();}catch(IOException e){}
			
		} // try{}
		
	} // GatewayConfiguration(String)
	
	
	/**
	 * 
	 * @param parameter Name of configuration parameter.
	 * @return Value of configuration parameter.
	 */
	public String get(String parameter) {
		return configurationProperties.getProperty(parameter);
	}
	
	/**
	 * 
	 * @param parameter Name of configuration parameter.
	 * @param defaultValue Default value to be returned in case the requested parameter is not configured explicitly 
	 * @return Value of configuration parameter (possibly the default one).
	 */
	public String get(String parameter, String defaultValue) {
		return configurationProperties.getProperty(parameter, defaultValue);
	} // get(String, String)
	
	
	/**
	 * 
	 * @return The underlying Properties object which holds configuration parameters and values.
	 */
	public Properties getProperties() {
		return configurationProperties;
	}
	
	
	/**
	 * 
	 * Creates and / or returns singleton instance of GatewayConfiguration object of given name.
	 * It can be many objects of GatewayConfiguration type, but each has to have distinguish name.
	 * The name designates individual instance of GatewayConfiguration object.
	 * 
	 * @param configName Name of requested GatewayConfiguration object
	 * @return The singleton instance of GatewayConfiguration of given name.
	 */
	synchronized public static GatewayConfiguration getInstance(String configName) {
		
		if (configName == null) {
			throw new IllegalArgumentException("Argumetn configName cannot be null");
		}
		
		GatewayConfiguration config = configsMap.get(configName);
		if(config != null) {
			return config;
		}
		
		config = new GatewayConfiguration(configName);
		configsMap.put(configName, config);
		
		return config;
	
	} // getInstance(String)
	

	/**
	 * 
	 * @return Default GatewayConfiguration object (with no name, that is with name denoted by empty string).
	 */
	public static GatewayConfiguration getInstance() {
		return getInstance("");
	} // getInstance()

		
} // 
