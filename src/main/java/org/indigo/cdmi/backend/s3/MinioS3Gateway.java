package org.indigo.cdmi.backend.s3;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

public class MinioS3Gateway implements S3Gateway {

  private final static Logger log = LoggerFactory.getLogger(MinioS3Gateway.class);
  
  private final S3ConnectionPropertiesProvider connectionPropertiesProvider; 
  private final MinioS3ClientBuilder clientBuilder;
  
  
  /**
   * 
   * @param connectionPropertiesProvider
   */
  @Inject
  public MinioS3Gateway(
      MinioS3ClientBuilder clientBuilder, 
      S3ConnectionPropertiesProvider connectionPropertiesProvider) {

    this.clientBuilder = clientBuilder;
    this.connectionPropertiesProvider = connectionPropertiesProvider;

  } // constructor
  
  
  @Override
  public boolean bucketExists(String bucketName) {
    
    try {

      S3ConnectionProperties connectionProperties = 
                  connectionPropertiesProvider.getConnectionProperties();
  
      MinioClient minioClient = this.clientBuilder.buildMinioClient(connectionProperties);

      return minioClient.bucketExists(bucketName);
      
    } catch (Exception ex) {
      
      log.error(
          "Failed to check if bucket of name " + bucketName + " exists; " + ex.getMessage(), 
          ex);
      
      throw new RuntimeException("Failed to check if bucket of name " + bucketName + " exists.");
      
    } // try
        
  } // bucketExists()

  
  
  @Override
  public S3ObjectInfo getObjectInfo(String bucketName, String objectName) {
    
    try {

      S3ConnectionProperties connectionProperties = 
          connectionPropertiesProvider.getConnectionProperties();

      MinioClient minioClient = this.clientBuilder.buildMinioClient(connectionProperties);
            
      ObjectStat objectStat = minioClient.statObject(bucketName, objectName);

      
      return new S3ObjectInfo(
          objectStat.bucketName(), objectStat.name(), objectStat.createdTime(), false);
      
    } catch (Exception ex) {
      
      log.error(
          "Failed to obtain object info for object " + objectName + " in bucket "
          + bucketName + "; " + ex.getMessage(), ex
      );
      
      throw new RuntimeException(
          "Failed to obtain object info for object " + objectName + " in bucket " + bucketName);
      
    } // try{}
    
  } 


  @Override
  public List<S3BucketInfo> listBuckets() {

    try {

      S3ConnectionProperties connectionProperties = 
          connectionPropertiesProvider.getConnectionProperties();

      MinioClient minioClient = this.clientBuilder.buildMinioClient(connectionProperties);

      List<Bucket> bucketList = minioClient.listBuckets();
      
      return bucketList.stream()
          .map(bucket -> new S3BucketInfo(bucket.name(), bucket.creationDate()))
          .collect(Collectors.toList());
      
    } catch (Exception ex) {
      
      log.error("Failed to get list of buckets; " + ex.getMessage(), ex);
      
      throw new RuntimeException("Failed to get list of buckets");
      
    } // try{}

  } // listBuckets()

  
  @Override
  public List<S3ObjectInfo> listObjects(String bucketName, String prefix) {

    try {
      
      S3ConnectionProperties connectionProperties = 
          connectionPropertiesProvider.getConnectionProperties();

      MinioClient minioClient = this.clientBuilder.buildMinioClient(connectionProperties);

      
      Iterable<Result<Item>> objects = minioClient.listObjects(bucketName, prefix, false);
      
      List<S3ObjectInfo> returnValue = StreamSupport
          .stream(objects.spliterator(), false)
          .filter(result -> {
            try {
              result.get();
              return true;
            } catch (Exception e) {
              log.error(String.format(
                  "Error while readig list of S3 objects in bucket %s with prefix %s; reason: %s", 
                  bucketName, prefix, e.getMessage()), e);
              return false;
            } // try{}
          })
          .map(s3obj -> {
            
            Item s3Item = null;
            S3ObjectInfo obInfo = null;
            try {
              s3Item = s3obj.get();
                            
              Date lastModificationDate = s3Item.isDir() ? null : s3Item.lastModified();
              obInfo = new S3ObjectInfo(
                  bucketName, s3Item.objectName().substring(prefix.length()),
                  lastModificationDate, s3Item.isDir());
            } catch (Exception e) {
              log.error(String.format("Failed to convert Item %s to S3ObjectInfo", s3Item),e);
              
              throw new RuntimeException(
                  "Failed to build S3ObjectInfo from respone received from server.", e);
            }  // try{}
            
            return obInfo;
          }
          ).collect(Collectors.toList());
      
      
      return returnValue;
      
    } catch (Exception ex) {
      
      log.error(
          "Failed to get list of object from bucket " + bucketName 
          + " with prefix " + prefix + "; " + ex.getMessage(), ex
      );
      
      throw new RuntimeException(
          "Failed to get list of object from bucket " + bucketName + " with prefix " + prefix);
      
    } // try{}
  
  } // listObjects()

} // end of MinioS3Gateway class
