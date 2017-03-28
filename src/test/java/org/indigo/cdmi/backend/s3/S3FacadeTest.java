package org.indigo.cdmi.backend.s3;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.Sets;

public class S3FacadeTest {

  private int port = 0;
  private MinioS3Gateway minioS3Gateway = null;
  
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort().usingFilesUnderClasspath("templates/wiremock"));
  
  
  @Before
  public void setUp() throws Exception {
    
    this.port = wireMockRule.port();
    this.minioS3Gateway = new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesTestingProvider(this.port));
    
  }
  
  @Test
  public void getChildrenTest() {

 
    this.minioS3Gateway = new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesDefaultProvider());
    
    S3Facade s3Facade = new S3Facade(minioS3Gateway);

    System.out.println("---------------------------------------------------");
    System.out.println("---------------------------------------------------");

    
    List<String> children = s3Facade.getChildren("/standard/dir1/");
    children.forEach(System.out::println);
    
    System.out.println("---------------------------------------------------");
    System.out.println("---------------------------------------------------");
    
    
  } // getChildrenTest()
  

  
  
  @Test
  public void isContainerTest() {
    
    
    System.out.println("---------------------------------------------------");
    System.out.println("---------------------------------------------------");
    
    //this.minioS3Gateway = new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesDefaultProvider());
    
    S3Facade s3Facade = new S3Facade(minioS3Gateway);
    
    String path = "standard";
    boolean isContainer = s3Facade.isContainer(path);
  
    assertEquals(isContainer, true);
    
    System.out.println(String.format("isContainer(\"%s\"): %b", path, isContainer));

//    path = "/standard/file1.txt";
//    isContainer = s3Facade.isContainer(path);
//    System.out.println(String.format("isContainer(\"%s\"): %b", path, isContainer));
//
//    
//    path = "/standard/dir1/";
//    isContainer = s3Facade.isContainer(path);
//    System.out.println(String.format("isContainer(\"%s\"): %b", path, isContainer));
//    
//    path = "standard/dir1/";
//    isContainer = s3Facade.isContainer(path);
//    System.out.println(String.format("isContainer(\"%s\"): %b", path, isContainer));
//
//    path = "standard/dir1";
//    isContainer = s3Facade.isContainer(path);
//    System.out.println(String.format("isContainer(\"%s\"): %b", path, isContainer));
//
//
//    path = "/standard/dir1";
//    isContainer = s3Facade.isContainer(path);
//    System.out.println(String.format("isContainer(\"%s\"): %b", path, isContainer));
//
//    
//    path = "/standard/dir1/file2.txt";
//    isContainer = s3Facade.isContainer(path);
//    System.out.println(String.format("isContainer(\"%s\"): %b", path, isContainer));
//
    
//    path = "standard/dir1/nonexisting.txt";
//    isContainer = s3Facade.isContainer(path);
//    System.out.println(String.format("isContainer(\"%s\"): %b", path, isContainer));
    
    
    System.out.println("---------------------------------------------------");
    System.out.println("---------------------------------------------------");

    
  } // isContainerTest()
  
  
  @Test
  public void test() {
    
    S3Facade s3Facade = new S3Facade(minioS3Gateway);
    
    Set<String> fixtureSet = Sets.newHashSet("silver", "golden", "standard");
    
    List<String> bucketsList = s3Facade.getChildren("");
    assertEquals(bucketsList.size(), 3);

    Set<String> response1Set = new HashSet<>();
    CollectionUtils.addAll(response1Set, bucketsList);
    
    assertTrue(SetUtils.isEqualSet(response1Set, fixtureSet));
    
    
    bucketsList = s3Facade.getChildren("/");
    assertEquals(bucketsList.size(), 3);

    Set<String> response2Set = new HashSet<>();
    CollectionUtils.addAll(response2Set, bucketsList);
    
    assertTrue(SetUtils.isEqualSet(response2Set, fixtureSet));
    
  } // test()

}
