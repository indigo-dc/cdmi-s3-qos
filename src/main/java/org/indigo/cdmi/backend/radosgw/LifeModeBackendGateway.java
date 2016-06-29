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
 * 
 * @author Gracjan Jankowski
 *
 */
public class LifeModeBackendGateway implements BackendGateway {

	private static final Logger log = LoggerFactory.getLogger(LifeModeBackendGateway.class); 
	
	private GatewayConfiguration config = GatewayConfiguration.getInstance();
	
	public static final String CONF_SSH_COMMAND_GET_PROFILES = "objectstore.ssh-gateway.get-profiles-command";
	public static final String CONF_SSH_COMMAND_GET_BUCKET_PROFILE = "objectstore.ssh-gateway.get-bucket-profile-command";
	
	private String sshCommandGetProfiles = null;
	private String sshCommandGetBucketProfile = null;
	
	
	private RemoteExecutor remoteExecutor = null;
	
	
	/**
	 * Read configuration file, instantiate auxiliary objects
	 */
	public LifeModeBackendGateway() {
	
		
		remoteExecutor = RemoteExecutorFactory.create(config.getProperties(), config.getProperties());
		
		boolean wrongConfiguration = false;
		
		/*
		 * read configuration
		 */
		this.sshCommandGetProfiles = config.get(CONF_SSH_COMMAND_GET_PROFILES);		
		log.info("Command to get profiles: {}", this.sshCommandGetProfiles);
		
		this.sshCommandGetBucketProfile = config.get(CONF_SSH_COMMAND_GET_BUCKET_PROFILE);
		log.info("Command to get bucket profile: {}", this.sshCommandGetBucketProfile);
		
		/*
		 * check if required configuration has been provided and if required convert string parameter to final types
		 */
		if(this.sshCommandGetProfiles == null) {
			log.error("There is no {} parameter in configuration files.", LifeModeBackendGateway.CONF_SSH_COMMAND_GET_PROFILES);
			wrongConfiguration = true;
		}
		
		if(wrongConfiguration) {
			throw new RuntimeException("SshBackendGateway is not properly configured. Correct or add objectstore.properties file to application's current working directory.");
		}
		
		
	} // LifeModeBackedGateway()
	
	
	/**
	 * @see BackendGateway#getAllProfiles() 
	 */
	@Override
	public String getAllProfiles() {

		String executorAnswer = null;
		
		
		try {
		
			executorAnswer = remoteExecutor.execute(this.sshCommandGetProfiles);
		
		} catch (RemoteExecutorException e) {
		
			log.error("Failed to remote execute command {} via remote executor {}; error: {}", this.sshCommandGetProfiles, remoteExecutor, e);
			throw new RuntimeException("Failed to remote execute command " + this.sshCommandGetProfiles + " via remote executor " + remoteExecutor, e);
		
		}
		
		return executorAnswer;
	
	} // getAllProfiles()


	
	/**
	 * @see BackendGateway#getPathProfile(String)
	 */
	@Override
	public String getPathProfile(String path) {
		
		String executorAnswer = null;
		
		/*
		 * get bundle name
		 */
		String bundleName = null;
		try {
			bundleName = S3Utils.getBucketNameFromPath(path);
			log.debug("Related bundle name: {}", bundleName);
		} catch (Exception e) {
			throw new RuntimeException("Failed to get name of bundle for path " + path, e);
		}
		
		
		try {
			String sshCommand = this.sshCommandGetBucketProfile + " " + bundleName;
			log.debug("ssh command to get path profile: {}", sshCommand);
			executorAnswer = remoteExecutor.execute(sshCommand);
		} catch (RemoteExecutorException e) {
			throw new RuntimeException("Failed to get profile of bundle " + bundleName, e);
		} 
		
		
		return executorAnswer;
		
	} // getPathProfile

	
} // end of SshBackedGateway class
