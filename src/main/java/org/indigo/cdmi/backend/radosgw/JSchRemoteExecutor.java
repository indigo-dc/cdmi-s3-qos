/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JSchRemoteExecutor implements RemoteExecutor {

	private static final Logger log = LoggerFactory.getLogger(JSchRemoteExecutor.class);
	
	public static final String CONF_SSH_HOST                 = "objectstore.ssh-gateway.host";
	public static final String CONF_SSH_PORT                 = "objectstore.ssh-gateway.port";
	public static final String CONF_SSH_USER                 = "objectstore.ssh-gateway.user";
	public static final String CONF_SSH_PASSWORD             = "objectstore.ssh-gateway.password";
	public static final String CONF_SSH_KEY_PATH             = "objectstore.ssh-gateway.key-path";
	public static final String CONF_SSH_KEY_PASSWORD         = "objectstore.ssh-gateway.key-password";
	
	
	private String sshHost 				 = null;
	private String sshPort 				 = null;
	private String sshUser 				 = null;
	private String sshPassword 			 = null;
	private String sshKeyPath   		 = null;
	private String sshKeyPassword 		 = null;
	
	private int sshPortAsInt 			 = 0;

	
	/**
	 * @see RemoteExecutor#configure(Properties) 
	 */
	@Override
	public void configure(Properties config) {
		
		boolean wrongConfiguration = false;
		
		/*
		 * read configuration
		 */
		this.sshHost = config.getProperty(CONF_SSH_HOST);
		this.sshPort = config.getProperty(CONF_SSH_PORT, "22");
		this.sshUser = config.getProperty(CONF_SSH_USER);
		this.sshPassword = config.getProperty(CONF_SSH_PASSWORD);
		this.sshKeyPath = config.getProperty(CONF_SSH_KEY_PATH);
		this.sshKeyPassword = config.getProperty(CONF_SSH_KEY_PASSWORD);
		
		log.info("Host: {}", this.sshHost);
		log.info("Port: {}", this.sshPort);
		

		/*
		 * check if required configuration has been provided and if required convert string parameter to final types
		 */
		if(this.sshHost == null) {
			log.error("There is no {} property in passed config parameter.", CONF_SSH_HOST);
			wrongConfiguration = true;
		}
		
		if(this.sshPort == null) {
			log.error("There is no {} property in passed config parameters.", CONF_SSH_PORT);
			wrongConfiguration = true;
		}

		try {
			this.sshPortAsInt = Integer.parseInt(this.sshPort);
		} catch(NumberFormatException e) {
			log.error("The value of {} property in config parameters must be proper TCP port number.", CONF_SSH_PORT);
			wrongConfiguration = true;
		}
		
		if(this.sshUser == null) {
			log.error("There is no {} property in passed config parameter.", CONF_SSH_USER);
			wrongConfiguration = true;
		}
		
		if(this.sshPassword == null && this.sshKeyPath == null) {
			log.error("There is no {} or {} property in passed config parameter.", CONF_SSH_PASSWORD, CONF_SSH_KEY_PATH);
			wrongConfiguration = true;
		}
		
		if(wrongConfiguration) {
			throw new RuntimeException("JSchRemoteExecutor is not properly configured.");
		}
		
	} // configure()

	
	/**
	 * @see RemoteExecutor#execute(String)
	 */
	@Override
	public String execute(String cmd) throws RemoteExecutorException {
	
		/*
		 * 
		 */
		String retrivedJSON = null;
		
		/*
		 * main object to operate with ssh functionality
		 */
		JSch jsch=new JSch();
		
		Session sshSession = null;
		Channel execChannel = null;
		
		try {
			/*
			 * create session object
			 */
			sshSession = jsch.getSession(this.sshUser, this.sshHost, this.sshPortAsInt);
			
			/*
			 * configure client
			 */
			Properties sshConfig = new Properties();			
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			
			/*
			 * configure credentials
			 */
			if(this.sshPassword != null) {
				
				sshSession.setPassword(this.sshPassword);
			
			} else if(this.sshKeyPath != null) {
			
				if(this.sshKeyPassword != null) {
					jsch.addIdentity(this.sshKeyPath, this.sshKeyPassword);	
				} else {
					jsch.addIdentity(this.sshKeyPath);
				}
				
			} else {

				log.error("No enough credentials to establish connection to ssh server.");
				throw new RuntimeException("No enough credentials to establish connection to ssh server.");
			
			}
			
			/*
			 * connect to server
			 */
			sshSession.connect();
			
			/*
			 * open exec channel
			 */
			execChannel = sshSession.openChannel("exec");
			((ChannelExec) execChannel).setCommand(cmd);
			//((ChannelExec) execChannel).setCommand("/bin/cat /home/staszek/profiles.json");
			log.debug("Remote command \"{}\" issued", cmd);
			
			/*
			 * disable input stream for exec channel and intercept error stream
			 */
			execChannel.setInputStream(null);
			//((ChannelExec) execChannel).setErrStream(System.err);


			/*
			 * get InputStream which will expose output of executed command 
			 */
			InputStream in = execChannel.getInputStream();
			//InputStream in = execChannel.getExtInputStream();
			
			/*
			 * execute remote command
			 */
			execChannel.connect();

			/*
			 * read output form remote command
			 */
			
			StringBuffer execOutputBuffer = new StringBuffer();			
			byte[] readBuffer = new byte[4096];
			
			while(true) {
				log.debug("In stdout reading loop.");
				while(in.available() > 0) {
					int i=in.read(readBuffer, 0, readBuffer.length);
					if(i<0) break;
					String readString = new String(readBuffer, 0, i); 
					execOutputBuffer.append(readString);
				}
				if(execChannel.isClosed()) {
					if(in.available() > 0) continue;
					int exitStatus = execChannel.getExitStatus();
					//System.out.println("exit-status: " + exitStatus);
					if(exitStatus != 0) {
						throw new RuntimeException("Remote command has returned failure status " + exitStatus);
					}
					break;
				}
				
				// yield CPU for a while
				try {Thread.sleep(100);} catch (Exception e) {}
				
			} // while(true)
			
			retrivedJSON = execOutputBuffer.toString();
			
			execChannel.disconnect();
			sshSession.disconnect();

			
		} catch (IOException | JSchException | RuntimeException e) {
			
			if(execChannel != null) execChannel.disconnect();
			if(sshSession != null) sshSession.disconnect();
			
			log.debug("Failed to execute command " + cmd + " because of " + e, e);
			
			throw new RuntimeException("Failed to execute command " + cmd + " because of: " + e, e);
			
		} catch (Exception e) {

			if(execChannel != null) execChannel.disconnect();
			if(sshSession != null) sshSession.disconnect();
			
			log.debug("Failed to execute command " + cmd + " because of " + e, e);
			
			throw new RuntimeException("Failed to execute command " + cmd + " because of: " + e, e);

			
		} // try{}

		log.info("JSON read from ssh server: {}", retrivedJSON);
		
		return retrivedJSON;
		
	
	} // execute()

	
} // end of SshRemoteExecutor
