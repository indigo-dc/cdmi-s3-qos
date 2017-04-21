package org.indigo.cdmi.backend.s3;

import java.util.List;
import java.util.Properties;


public interface S3Facade {

  String PROPERTY_CREATION_TIME = "creation-time";

  /**
   * 
   * @param path
   * @return
   */
  boolean isContainer(String path); // isContainer()

  /**
   * 
   * @param path
   * @return
   */
  List<String> getChildren(String path); // getChildern()

  Properties getObjectProperties(String path); // getObjectProperties()

  /*
   * it will use S3Gateway.listBuckets() to find out the bucket which matches the request
   */
  Properties getContainerProperties(String path);

}
