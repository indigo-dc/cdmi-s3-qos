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

/**
 * 
 * Defines interface which allows to read output from command executed on remote node.
 * 
 * @author Gracjan Jankowski
 */
public interface RemoteExecutor {
	
	/**
	 * 
	 * @param cmd Command to be executed remotely.
	 * @return String read from standard output of the "cmd" command.
	 * @throws RemoteExecutorException
	 */
	public String execute(String cmd) throws RemoteExecutorException;
	

	/**
	 * 
	 * Configure given implementation of RemoteExecutor. 
	 * For example, specific credentials can be passed through properties parameter.
	 * 
	 * @param properties Parameters to be used in order to configure given instance of RemoteExecutor
	 */
	public void configure(Properties properties);
	
}
