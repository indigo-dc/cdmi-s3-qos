/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;


/**
 * Defines interface which allows to read output from command executed on remote node.
 * 
 * @author Gracjan Jankowski
 */
public interface RemoteExecutor {

  /**
   * Remotely executes the "cmd" command.
   * 
   * @param cmd Command to be executed remotely.
   * @return String read from standard output of the "cmd" command.
   * @throws RemoteExecutorException Indicates that "cmd" couldn't be executed. Reason should 
   *     be read from exception's message.
   */
  public String execute(String cmd) throws RemoteExecutorException;

} // end of RemoteExecutor class
