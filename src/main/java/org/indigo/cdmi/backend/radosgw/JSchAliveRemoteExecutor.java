/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Executes commands remotely with help of JSch library.
 * Session to SSH server is persistent, that is, it is  not closed between specific remote commands.
 * 
 * @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class JSchAliveRemoteExecutor implements RemoteExecutor {

  private static final Logger log = LoggerFactory.getLogger(JSchAliveRemoteExecutor.class);

  public static final String CONF_SSH_HOST = "objectstore.ssh-gateway.host";
  public static final String CONF_SSH_PORT = "objectstore.ssh-gateway.port";
  public static final String CONF_SSH_USER = "objectstore.ssh-gateway.user";
  public static final String CONF_SSH_PASSWORD = "objectstore.ssh-gateway.password";
  public static final String CONF_SSH_KEY_PATH = "objectstore.ssh-gateway.key-path";
  public static final String CONF_SSH_KEY_PASSWORD = "objectstore.ssh-gateway.key-password";


  private String sshHost = null;
  private String sshPort = null;
  private String sshUser = null;
  private String sshPassword = null;
  private String sshKeyPath = null;
  private String sshKeyPassword = null;

  private int sshPortAsInt = 0;

  private int maxAttempts = 3;

  private JSch jsch = null;

  private BackendConfiguration backendConfiguration;
  
  Session sshSession = null;
  Channel execChannel = null;

  private Provider<JSch> jschProvider;

  private boolean configured = false;
  
  /**
   * Constructor.
   * 
   * @param jschProvider Guise like provider meant to create new instances of JSch objects.
   * @param backendConfiguration Instance of BackendConfiguration with parameters required 
   *        to establish ssh session to RADOS GW server.
   */
  @Inject
  public JSchAliveRemoteExecutor(Provider<JSch> jschProvider, 
      BackendConfiguration backendConfiguration) {

    this.jschProvider = jschProvider;
    this.backendConfiguration = backendConfiguration;
  
  } // constructor
  
  
  /**
   * It is called only during start-up from configure() method and in exception handling clause from
   * execute() method.
   */
  private void setUpChannel() {

    log.info("setUpChannel called");

    /*
     * first try clean after previous channel (if any)
     */
    if (execChannel != null) {
      execChannel.disconnect();
    }
      
    if (sshSession != null) {
      sshSession.disconnect();
    }
      


    try {

      /*
       * now try set-up new channel (from bottom-up)
       */
      jsch = jschProvider.get();
      
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
      if (this.sshPassword != null) {

        sshSession.setPassword(this.sshPassword);

      } else if (this.sshKeyPath != null) {

        if (this.sshKeyPassword != null) {
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


    } catch (Exception ex) {

      log.error("Failed to set-up exec channel to remote server: {}", ex);
      if (sshSession != null) {
        sshSession.disconnect();
      }
        

    } // try{}

  } // setUpChannel()

  /**
   * Configure instance of this object with parameters passed in config.
   * 
   * @see RemoteExecutor#configure(Properties)
   */
  //@Override
  private void configure(Properties config) {

    boolean wrongConfiguration = false;

    
    if (null == config) {
      throw new IllegalArgumentException("Config parameter cannot be null");
    }

    
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
     * check if required configuration has been provided and if required convert string parameter to
     * final types
     */
    if (this.sshHost == null) {
      log.error("There is no {} property in passed config parameter.", CONF_SSH_HOST);
      wrongConfiguration = true;
    }

    if (this.sshPort == null) {
      log.error("There is no {} property in passed config parameters.", CONF_SSH_PORT);
      wrongConfiguration = true;
    }

    try {
      this.sshPortAsInt = Integer.parseInt(this.sshPort);
    } catch (NumberFormatException ex) {
      log.error("The value of {} property in config parameters must be proper TCP port number.",
          CONF_SSH_PORT);
      wrongConfiguration = true;
    }

    if (this.sshUser == null) {
      log.error("There is no {} property in passed config parameter.", CONF_SSH_USER);
      wrongConfiguration = true;
    }

    if (this.sshPassword == null && this.sshKeyPath == null) {
      log.error("There is no {} or {} property in passed config parameter.", CONF_SSH_PASSWORD,
          CONF_SSH_KEY_PATH);
      wrongConfiguration = true;
    }

    if (wrongConfiguration) {
      throw new RuntimeException("JSchRemoteExecutor is not properly configured.");
    }

    setUpChannel();

  } // configure()


  /**
   * Executes cmd command on remote site.
   * 
   * @see RemoteExecutor#execute(String)
   */
  @Override
  public String execute(String cmd) throws RemoteExecutorException {

    log.info("Remote execution of \"{}\" command", cmd);

    String retrivedJson = null;
    boolean done = false;
    int attemptNumber = 0;

    if (cmd == null) {
      throw new RemoteExecutorException("cmd parameter cannot be null");
    }

    
    if (!configured) {
      configure(backendConfiguration.getProperties());
      configured = true;
    }

    
    /*
     * try to execute cmd command for "maxAttemps" times
     */
    while (!done && attemptNumber < maxAttempts) {

      attemptNumber++;

      try {

        execChannel = sshSession.openChannel("exec");
        ((ChannelExec) execChannel).setCommand(cmd);
        log.debug("Remote command \"{}\" issued", cmd);

        /*
         * disable input stream for exec channel and intercept error stream
         */
        execChannel.setInputStream(null);


        /*
         * get InputStream which will expose output of executed command
         */
        InputStream in = execChannel.getInputStream();

        /*
         * execute remote command
         */
        execChannel.connect();

        /*
         * read output form remote command
         */

        StringBuffer execOutputBuffer = new StringBuffer();
        byte[] readBuffer = new byte[4096];

        while (true) {
          log.debug("In stdout reading loop.");
          while (in.available() > 0) {
            int index = in.read(readBuffer, 0, readBuffer.length);
            if (index < 0) {
              break;
            }
            String readString = new String(readBuffer, 0, index);
            execOutputBuffer.append(readString);
          }
          if (execChannel.isClosed()) {
            if (in.available() > 0) {
              continue;
            }
            int exitStatus = execChannel.getExitStatus();
            // System.out.println("exit-status: " + exitStatus);
            if (exitStatus != 0) {
              throw new RuntimeException(
                  "Remote command has returned failure status " + exitStatus);
            }
            break;
          }

          // yield CPU for a while
          try {
            Thread.sleep(100);
          } catch (Exception ex) {
            // ignore
          }

        } // while(true)

        in.close();
        execChannel.disconnect();

        retrivedJson = execOutputBuffer.toString();

        done = true;

      } catch (Exception ex) {

        if (execChannel != null) {
          execChannel.disconnect();
        }
          
        if (sshSession != null) {
          sshSession.disconnect();
        }
          

        log.debug("Failed to execute command " + cmd + " because of " + ex, ex);

        setUpChannel();

      } // try{}

    } // while()

    /*
     * check if command has been executed successfully
     */
    if (!done) {
      throw new RuntimeException("Failed to execute command " + cmd);
    }

    log.info("JSON read from ssh server: {}", retrivedJson);

    return retrivedJson;

  } // execute()

}
