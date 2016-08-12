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
 * Implementaion of {@link ObjectPathTranslator} interface. 
 * Translates CDMI paths to S3 paths as exposed by RADOS GW server.
 * 
 * @author @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class S3PathTranslator implements ObjectPathTranslator {

  /**
   * @see ObjectPathTranslator#translate(String).
   */
  @Override
  public String translate(String path) {

    return path;

  } // translate()

} // end of S3PathTranslator class
