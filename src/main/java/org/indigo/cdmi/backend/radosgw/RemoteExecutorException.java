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
 * Exception to be thrown by RemoteExecutor.execute() method in case of any problems.
 * Details about the error should be included in exception message.
 * 
 *  @author @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class RemoteExecutorException extends Exception {

  private static final long serialVersionUID = 1L;

  public RemoteExecutorException() {
    super();
  }

  public RemoteExecutorException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public RemoteExecutorException(String message, Throwable cause) {
    super(message, cause);
  }

  public RemoteExecutorException(String message) {
    super(message);
  }

  public RemoteExecutorException(Throwable cause) {
    super(cause);
  }

  
} // end of RemoteExecutorException
