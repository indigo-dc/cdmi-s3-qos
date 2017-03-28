/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;

import org.indigo.cdmi.backend.s3.S3Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * Specific implementation of BackendGateway interface.
 * This implementation utilizes ssh protocol as a way of communication with RADOS server.
 *  
 *  @author @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class LifeModeBackendGateway implements BackendGateway {

  private static final Logger log = LoggerFactory.getLogger(LifeModeBackendGateway.class);

  private BackendConfiguration config = null;
  
  public static final String CONF_SSH_COMMAND_GET_PROFILES =
      "objectstore.ssh-gateway.get-profiles-command";

  public static final String CONF_SSH_COMMAND_GET_BUCKET_PROFILE =
      "objectstore.ssh-gateway.get-bucket-profile-command";

  private String sshCommandGetProfiles      = null;
  private String sshCommandGetBucketProfile = null;


  private RemoteExecutor remoteExecutor = null;

  private final CacheLoader<String, String> responseCacheLoader;
  private final LoadingCache<String, String> backendResponsesCache;
  
  /**
   * Read configuration file, instantiate auxiliary objects.
   */
  @Inject
  public LifeModeBackendGateway(BackendConfiguration backendConfig, RemoteExecutor remoteExecutor) {

    if (null == backendConfig) {
      throw new IllegalArgumentException("backendConfig parameter cannot be null");
    }
    
    this.config = backendConfig;

    this.remoteExecutor = remoteExecutor;
    
    boolean wrongConfiguration = false;

    /*
     * read configuration
     */
    this.sshCommandGetProfiles = config.get(CONF_SSH_COMMAND_GET_PROFILES);
    log.info("Command to get profiles: {}", this.sshCommandGetProfiles);

    
    this.sshCommandGetBucketProfile = config.get(CONF_SSH_COMMAND_GET_BUCKET_PROFILE);    
    log.info("Command to get bucket profile: {}", this.sshCommandGetBucketProfile);

    /*
     * check if required configuration has been provided and if required convert string parameter to
     * final types
     */
    if (this.sshCommandGetProfiles == null) {
      log.error("There is no {} parameter in configuration files.",
          LifeModeBackendGateway.CONF_SSH_COMMAND_GET_PROFILES);
      wrongConfiguration = true;
    }
    
    if (this.sshCommandGetBucketProfile == null) {
      log.error("There is no {} parameter in configuration files.",
          LifeModeBackendGateway.CONF_SSH_COMMAND_GET_BUCKET_PROFILE);
      wrongConfiguration = true;
    }

    if (wrongConfiguration) {
      throw new RuntimeException(
          "LifeModeBackendGateway is not properly configured. Correct or add "
          + "objectstore.properties file to application's current working directory. "
          + "Check if parameters " + CONF_SSH_COMMAND_GET_BUCKET_PROFILE 
          + " and " + CONF_SSH_COMMAND_GET_PROFILES + " have been defined.");
    }


    /*
     * initialize cache loader
     */
    responseCacheLoader = new CacheLoader<String, String>() {

      @Override
      public String load(String bundleName) throws Exception {
      
        String executorAnswer = null;
        
        try {
          String sshCommand = sshCommandGetBucketProfile + " " + bundleName;
          log.debug("ssh command to get path profile: {}", sshCommand);
          executorAnswer = remoteExecutor.execute(sshCommand);
        } catch (Exception ex) {
          throw new Exception("Failed to get profile of bundle " + bundleName, ex);
        }

        return executorAnswer;
      
      } // load()
    
    };
    
    
    /*
     * build responseCache
     */
    backendResponsesCache = CacheBuilder.newBuilder()
        .maximumSize(30)
        .expireAfterAccess(3600, TimeUnit.SECONDS)
        .build(responseCacheLoader);
        
  } // LifeModeBackedGateway()


  /**
   * Returns JSON in String format, which describes all QoS profiles provided by underlying 
   * RADOS gateway.
   *
   * <p>It just connects with RADOS server indicated in configuration and asks the server 
   * for all supported profiles.
   * 
   * @see BackendGateway#getAllProfiles()
   */
  @Override
  public String getAllProfiles() {

    String executorAnswer = null;

    try {

      executorAnswer = remoteExecutor.execute(this.sshCommandGetProfiles);

    } catch (RemoteExecutorException ex) {

      log.error("Failed to remote execute command {} via remote executor {}; error: {}",
          this.sshCommandGetProfiles, remoteExecutor, ex);
      throw new RuntimeException("Failed to remote execute command " + this.sshCommandGetProfiles
          + " via remote executor " + remoteExecutor, ex);

    }

    return executorAnswer;

  } // getAllProfiles()

  
  
  /**
   * Returns String in JSON format which describes QoS profile assigned to CDMI object 
   * determined by path parameter.
   * 
   * @see BackendGateway#getPathProfile(String)
   */
  @Override
  public String getPathProfile(String path) {

    log.debug("getPathProfile({})", path);
    
    /*
     * get bundle name
     */
    String bundleName = "/";
    if (!path.equals("/")) {
      try {
        bundleName = S3Utils.getBucketNameFromPath(path);
        log.debug("Related bundle name: {}", bundleName);
      } catch (Exception ex) {
        throw new RuntimeException("Failed to get name of bundle for path " + path, ex);
      }      
    }

    String backendResponse = null;
    try {
      backendResponse = backendResponsesCache.get(bundleName);
    } catch (ExecutionException e) {
      throw new RuntimeException("Failed to get profile of bundle " + bundleName, e);
    }
    return backendResponse;

  } // getPathProfile

} // end of SshBackedGateway class
