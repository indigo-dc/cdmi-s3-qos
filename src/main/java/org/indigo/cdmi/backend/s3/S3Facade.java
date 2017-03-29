package org.indigo.cdmi.backend.s3;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * Uses S3Gateway to  provide CDMI server with more abstract / general interface to underlying S3 server. 
 * 
 * @author gracjan
 */
public class S3Facade {
  
  private final static Logger log = LoggerFactory.getLogger(S3Facade.class);
  
  private final S3Gateway s3Gateway;
  
  @Inject
  public S3Facade(S3Gateway s3Gateway) {
    
    this.s3Gateway = s3Gateway;
    
  } // constructor
  
  
  /**
   * 
   * @param path
   * @return
   */
  public boolean isContainer(String path) {
    
    log.debug("isContainer({})", path);
    
    
    if (path == null) {
      throw new IllegalArgumentException("path cannot be null");
    }
        
    /*
     * empty path or 'root' path is considered to be an container 
     */
    if (path.equals("")) {
      log.debug("Path: {} is empty so it is considered to be a container (root container)");
      return true;
    }
    
    /*
     * if path ends with slash ('/') then assume that path designates 
     * a container
     */
    if (StringUtils.endsWith(path, "/")) {
      log.debug("Path: {} ends with slash so it is considered to be a container.", path);
      return true;
    }
    
    
    /*
     * First split path up into bucket and suffix part
     */
    String bucketName = S3Utils.getBucketNameFromPath(path);    
    log.debug("bucketName: {}", bucketName);
    
    String prefixPart = S3Utils.getPrefixFromPath(path);
    log.debug("prefixPart: {}", prefixPart);
    
    
    /*
     * if prefix part is empty then the path represents just a bucket which 
     * from CDMI point of view is considered as an container
     */
    if (prefixPart.length() == 0) {
      return true;
    }
    
    /*
     * Try to list objects which live in determined bucket and suffix 
     */
    List<S3ObjectInfo> s3Objects = s3Gateway.listObjects(bucketName, prefixPart);
    
    boolean isContainer = false;
       
    for (S3ObjectInfo s3ObjectInfo : s3Objects) {
      
      if (s3ObjectInfo.getObjectName().equals("/")) {
        isContainer = true;
        break;
      }
      
    }
    
    //s3Objects.forEach(System.out::println);
        
    return isContainer;
  
  } // isContainer()

  
  /**
   * 
   * @return
   */
  private List<String> getBucketsList() {
    
    // get info about buckets present on back-end
    List<S3BucketInfo> bucketsInfoList = s3Gateway.listBuckets();
    
    // convert information from back-end to list of strings with names of buckets
    return bucketsInfoList
           .stream()
           .map((element) -> element.getName() + "/")
           .collect(Collectors.toList());
     
  }
  
  
  /**
   * 
   * @param bucketName
   * @param prefixPart
   * @return
   */
  private List<String> getObjectsList(String bucketName, String prefixPart) {
    
    List<S3ObjectInfo> s3Objects = s3Gateway.listObjects(bucketName, prefixPart);
    
    return s3Objects
              .stream()
              .filter(it -> !it.getObjectName().equals("/"))
              .map(it -> it.getObjectName())
              .collect(Collectors.toList());
    
  } // getObjectsList()
  
  
  /**
   * 
   * @param path
   * @return
   */
  public List<String> getChildren(String path) {
    
    log.debug("getChildern({})", path);
    
    if (path.equals("/") || path.equals("")) {
      return getBucketsList();
    }
    
    // 1. determine bucket name
    String bucketName = S3Utils.getBucketNameFromPath(path);
    log.debug("bucketName: {}", bucketName);

    // 2. determine prefix
    String prefixPart = S3Utils.getPrefixFromPath(path);
    log.debug("prefixPart: {}", prefixPart);
    
    
    // 3. Try to list objects; QUESTION: how to check if path points to the container and not to a file,
    // I have to check what s3Gateway.listObjects() will return for such a path
    
    return getObjectsList(bucketName, prefixPart);
    
  } // getChildern()
  
  
  public Properties getObjectProperties(String path) {
    return null;
  }
  
  /*
   * it will use S3Gateway.listBuckets() to find out the bucket which matches the request
   */
  public Properties getContainerProperties(String path) {
    return null;
  }
  
  
} // end of S3Adapter
