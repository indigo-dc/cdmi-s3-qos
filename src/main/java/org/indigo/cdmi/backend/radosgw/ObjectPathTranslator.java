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
 *  Interface to translate CDMI objects paths seen by CDMI server to 
 *  paths seen by underlying RADOS GW.
 *  
 *  @author @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public interface ObjectPathTranslator {

  /**
   * Translates CDMI path to RADOS GW path.
   * 
   * @param path Path to CDMI object as it is seen by CDMI object.
   * @return Path to object as it is seen by RADOS GW server.
   */
  public String translate(String path);

} // end of ObjectPathTranslator
