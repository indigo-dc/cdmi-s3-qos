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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Default implementation of {@link BackendConfiguration} interface.
 * This object reads configuration files and exposes the configuration 
 * through interface defined by BackendConfiguration.
 * 
 * <p>The default values of configuration parameters are defined in file objectstore.properties read
 * from CLASSPATH (see src/main/resources/objectstore.propertis)
 * 
 * <p>New parameters or parameters which override the default ones can be defined in file
 * config/objectstore.properties read from current working directory.
 * 
 * <p>The format of configuration files is the same as required by 
 * {@link Properties#load(InputStream)}.
 * 
 * @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class DefaultBackendConfiguration implements BackendConfiguration {

  private static final Logger log = LoggerFactory.getLogger(DefaultBackendConfiguration.class);

  private static Map<String, DefaultBackendConfiguration> configsMap = new HashMap<>();

  private Properties configurationProperties = new Properties();


  /**
   * Private constructor.
   */
  private DefaultBackendConfiguration(String configName) {

    /*
     * determine name of file with properties
     */
    StringBuffer inJarPropertiesFileNameBuilder = new StringBuffer();

    inJarPropertiesFileNameBuilder.append("objectstore");

    if (!configName.equals("")) {
      inJarPropertiesFileNameBuilder.append("-");
      inJarPropertiesFileNameBuilder.append(configName);
    }

    inJarPropertiesFileNameBuilder.append(".properties");
    
    String inJarPropertiesFileName = inJarPropertiesFileNameBuilder.toString();

    /*
     * read properties form properties file located on CLASSPATH
     * (the assumption is that it is default configuration file placed within 
     * the same jar file as this class)
     */
    InputStream configInputStream =
        getClass().getClassLoader().getResourceAsStream(inJarPropertiesFileName);
    if (configInputStream == null) {
      throw new RuntimeException("Failed to find required config file on CLASSPATH. "
          + "Could not open " + inJarPropertiesFileName);
    }

    try {
      configurationProperties.load(configInputStream);
      log.info("Default configuration from {} is read.", inJarPropertiesFileName);
      
    } catch (IOException ex) {
      throw new RuntimeException(
          String.format("Failed to load configuration from resource: %s", inJarPropertiesFileName),
          ex);
    } finally {
      try {
        configInputStream.close();
      } catch (IOException ex) {
        throw new RuntimeException("Filed to close configuration file " 
             + inJarPropertiesFileName, ex);
      }
    } // try{}


    /*
     * read properties from file located in config directory
     */

    StringBuffer outJarPropertiesFileNameBuilder = new StringBuffer();

    outJarPropertiesFileNameBuilder.append("config/objectstore");

    if (!configName.equals("")) {
      outJarPropertiesFileNameBuilder.append("-");
      outJarPropertiesFileNameBuilder.append(configName);
    }

    outJarPropertiesFileNameBuilder.append(".properties");

    String outJarPropertiesFileName = outJarPropertiesFileNameBuilder.toString();


    InputStream fis = null;
    try {
      fis = new FileInputStream(outJarPropertiesFileName);
    } catch (FileNotFoundException ex) {
      // IGNORE
      // configuration file in config dir simply can be not present
    }

    /*
     * if local configuration exists then read it into configurationProperties
     */
    try {

      if (fis != null) {
        configurationProperties.load(fis);
        log.info("Default configuration from {} is read.", outJarPropertiesFileName);
      }

    } catch (IOException ex) {

      if (fis != null) {
        try {
          fis.close();
        } catch (Exception ex1) {
          // ignore
        }
      }
      throw new RuntimeException(
          "Failed to read local configuration file " + outJarPropertiesFileName, ex);

    } finally {

      try {
        if (fis != null) {
          fis.close(); 
        }  
      } catch (IOException ex) {
        throw new RuntimeException("Filed to close configuration file " 
            + outJarPropertiesFileName, ex);
      }

    } // try{}

  } // GatewayConfiguration(String)


  /**
   * @see org.indigo.cdmi.backend.radosgw.GatewayConfiguration#get(java.lang.String)
   */
  @Override
  public String get(String parameter) {
    return configurationProperties.getProperty(parameter);
  }

  /**
   * @see org.indigo.cdmi.backend.radosgw.GatewayConfiguration#get(
   *    java.lang.String, java.lang.String)
   */
  @Override
  public String get(String parameter, String defaultValue) {
    return configurationProperties.getProperty(parameter, defaultValue);
  } // get(String, String)


  /**
   * @see org.indigo.cdmi.backend.radosgw.GatewayConfiguration#getProperties()
   */
  @Override
  public Properties getProperties() {
    return configurationProperties;
  }


  /**
   * Creates and / or returns singleton instance of GatewayConfiguration object of given name. It
   * can be many objects of GatewayConfiguration type, but each has to have distinguish name. The
   * name designates individual instance of GatewayConfiguration object.
   * 
   * @param configName Name of requested GatewayConfiguration object
   * @return The singleton instance of GatewayConfiguration of given name.
   */
  public static synchronized DefaultBackendConfiguration getInstance(String configName) {

    if (configName == null) {
      throw new IllegalArgumentException("Argument configName cannot be null");
    }

    DefaultBackendConfiguration config = configsMap.get(configName);
    if (config != null) {
      return config;
    }

    config = new DefaultBackendConfiguration(configName);
    configsMap.put(configName, config);

    return config;

  } // getInstance(String)


  /**
   * @return Default GatewayConfiguration object (with no name, that is with name denoted by empty
   *         string).
   */
  public static DefaultBackendConfiguration getInstance() {
    return getInstance("");
  } // getInstance()


} // end of DefaultBackendConfiguration class
