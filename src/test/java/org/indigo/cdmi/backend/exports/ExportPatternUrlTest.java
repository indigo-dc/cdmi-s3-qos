package org.indigo.cdmi.backend.exports;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportPatternUrlTest {

  private final static Logger log = LoggerFactory.getLogger(ExportPatternUrlTest.class);
  
  private ExportPatternUrl exportPatternUrl;

  @Before
  public void setUp() throws Exception {

    exportPatternUrl = new ExportPatternUrl();
    
  }

  
  @Test(expected=IllegalArgumentException.class)
  public void attributeValue_objectPathIsNull_IllegalArgumentException() {
    
    exportPatternUrl.attributeValue(null, "out-of-interest");
    
  }


  @Test(expected=IllegalArgumentException.class)
  public void attributeValue_providerArgsIsNull_IllegalArgumentException() {
    
    exportPatternUrl.attributeValue("out-of-interest", null);
    
  }

  @Test
  public void getProviderName_returnsAssumedValue() {
    
    assertEquals(
        exportPatternUrl.getProviderName(), 
        ExportPatternUrl.EXPORT_PATTERN_URL);
   
  }
  
  @Test
  public void attributeValue_anyPathFixedUrl_returnsFixedUrl() {
    
    // Expected result
    String expectedReturnValue = "http://any.addr.net:8888/bucket-name";
    
    // Input arguments
    String objectPath = "/any/path";
    String providerArgs = "{" + ExportPatternUrl.EXPORT_PATTERN_URL + " http://any.addr.net:8888/bucket-name}";
    
    // Test call
    String returnedVaue = exportPatternUrl.attributeValue(objectPath, providerArgs);

    log.debug("expecterReturnValue: {}; returnedValue: {}", expectedReturnValue, returnedVaue);

    // Result check
    assertEquals(expectedReturnValue, returnedVaue);
    
  } // end of test


  @Test
  public void attributeValue_anyPathAndPatternWithFullPathPlaceholderAtTheEnd_returnsUrlWithFullPath() {
    
    // Expected result
    String expectedReturnValue = "http://any.addr.net:8888/raz/dwa/trzy";
    
    // Input arguments
    String providerArgs = "{" + ExportPatternUrl.EXPORT_PATTERN_URL + " http://any.addr.net:8888/{full-path}}";
    String objectPath = "/raz/dwa/trzy";
    
    // Test call
    String returnedVaue = exportPatternUrl.attributeValue(objectPath, providerArgs);
    log.debug("expecterReturnValue: {}; returnedValue: {}", expectedReturnValue, returnedVaue);
    
    // Result check
    assertEquals(expectedReturnValue, returnedVaue);
    
  } // end of test

  
  @Test
  public void attributeValue_anyPathFixedHostUrl_returnsHostUrl() {
    
    // Expected results
    String expectedReturnValue = "http://any.addr.net:8888";
    
    // Input arguments
    String providerArgs = "{" + ExportPatternUrl.EXPORT_PATTERN_URL + " " + expectedReturnValue + "}";
    String objectPath = "/raz/dwa/trzy";
    
    // Test call
    String returnedVaue = exportPatternUrl.attributeValue(objectPath, providerArgs);
    log.debug("expecterReturnValue: {}; returnedValue: {}", expectedReturnValue, returnedVaue);
    
    // Result check
    assertEquals(expectedReturnValue, returnedVaue);
    
  }
 
 
  @Test
  public void attributeValue_slashAsPathFixedUrlAsPattern_returnsFixedUrl() {
    
    // Expected result
    String expectedReturnValue = "http://any.addr.net:8888/bucket-name";
    
    // Input arguments
    String providerArgs = "{" + ExportPatternUrl.EXPORT_PATTERN_URL + " " + expectedReturnValue + "}";
    String objectPath = "/";
        
    // Test call
    String returnedVaue = exportPatternUrl.attributeValue(objectPath, providerArgs);
    log.debug("expecterReturnValue: {}; returnedValue: {}", expectedReturnValue, returnedVaue);
    
    // Result check
    assertEquals(expectedReturnValue, returnedVaue);
    
  } // end of test
  
  
  @Test
  public void attributeValue_anyPathAsObjectPathAndsimpleFixedHostUrlWithoutAnyPathAsPattern_returnsFixedHostUrlWithoutAnyPath() {
    
    // Expected result
    String expectedReturnValue = "http://any.addr.net:8888";
    
    // Input arguments
    String providerArgs = "{" + ExportPatternUrl.EXPORT_PATTERN_URL + " " + expectedReturnValue + "/}";
    String objectPath = "/any/path";
    
    // Test call
    String returnedVaue = exportPatternUrl.attributeValue(objectPath, providerArgs);
    log.debug("expecterReturnValue: {}; returnedValue: {}", expectedReturnValue, returnedVaue);

    // Result check
    assertEquals(expectedReturnValue, returnedVaue);
    
  } // end of test
  

  @Test
  public void attributeValue_longObjectPathAndUrlWithBucketAsPathInPattern_returnsUrlWithBucketAsPath() {
    
    // Expected result
    String expectedReturnValue = "http://any.addr.net:8888/some-bucket";
    
    // Input arguments
    String providerArgs = "{" + ExportPatternUrl.EXPORT_PATTERN_URL + " http://any.addr.net:8888/{bucket}}";
    String objectPath = "/some-bucket/any/object/path";
    
    // Test call
    String returnedVaue = exportPatternUrl.attributeValue(objectPath, providerArgs);
    log.debug("expecterReturnValue: {}; returnedValue: {}", expectedReturnValue, returnedVaue);
    
    // Result check
    assertEquals(expectedReturnValue, returnedVaue);
    
  } // end of Path
  
  
  @Test
  public void attributeValue_longObjectPathPatternWithBucketNameAsHostname_returnsUrlWithBucketAsHostname() {
    
    // Expected result
    String expectedReturnValue = "http://some-bucket.any.addr.net:8888";
    
    // Input arguments
    String providerArgs = "{" + ExportPatternUrl.EXPORT_PATTERN_URL + " http://{bucket}.any.addr.net:8888}";
    String objectPath = "/some-bucket/any/object/path";
    
    // Test call
    String returnedVaue = exportPatternUrl.attributeValue(objectPath, providerArgs);
    log.debug("expecterReturnValue: {}; returnedValue: {}", expectedReturnValue, returnedVaue);
    
    // Result check
    assertEquals(expectedReturnValue, returnedVaue);
        
  } // end of test

  
  
  @Test
  public void attributeValue_longObjectPathPatternWithRelativePathAtTheEnd_returnsUrlWithRelativePath() {
  
    // Expected result
    String expectedReturnValue = "http://any.addr.net:8888/relative/path";
    
    // Input arguments 
    String providerArgs = "{" + ExportPatternUrl.EXPORT_PATTERN_URL + " http://any.addr.net:8888/{relative-path}}";
    String objectPath = "/some-bucket/relative/path";
    
    // Test call
    String returnedVaue = exportPatternUrl.attributeValue(objectPath, providerArgs);
    log.debug("expecterReturnValue: {}; returnedValue: {}", expectedReturnValue, returnedVaue);
    
    // Check result
    assertEquals(expectedReturnValue, returnedVaue);
        
  } // end of test

  
} // end od ExportPatternUrlTest class
