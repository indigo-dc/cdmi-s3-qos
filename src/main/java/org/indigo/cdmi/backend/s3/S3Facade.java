package org.indigo.cdmi.backend.s3;

import java.util.List;
import java.util.Properties;


public interface S3Facade {

  String PROPERTY_CREATION_TIME = "creation-time";

  /**
   * Checks if passed {@code path} points to container 
   * (if not then the assumption is that it points to data-object).
   * 
   * @param path Path to be examined if points to container or not.
   * @return true if passed {@code path} is container else false
   */
  boolean isContainer(String path); 

  /**
   * Returns list of children elements of path determined py {@code path} argument.
   * 
   * @param path Path of object which children elements has to be returned.
   * 
   * @return List of String(s) with names of children elements of given {@code path}.
   */
  List<String> getChildren(String path);

  /**
   * Retrieves properties associated with object pointed to by {@code path}.
   *  
   * @param path Path to object.
   * @return Properties object with properties of object specified by {@code path}.
   */
  Properties getObjectProperties(String path);

  /**
   * It will use S3Gateway.listBuckets() to find out the bucket which matches the request.
   */
  Properties getContainerProperties(String path);

}
