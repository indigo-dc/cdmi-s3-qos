package org.indigo.cdmi.backend.radosgw;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Properties;

import org.indigo.cdmi.backend.s3.S3Facade;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* (non-Javadoc)
 * 
 */
class S3FacadeStub implements S3Facade {

  @Override
  public boolean isContainer(String path) {
    
    if (path.equals("/standard")) {
      return true;
    }
    
    return false;
  }

  @Override
  public List<String> getChildren(String path) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Properties getObjectProperties(String path) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Properties getContainerProperties(String path) {
    // TODO Auto-generated method stub
    return null;
  }
  
} // end of S3FacadeStub


/* (non-Javadoc)
 * 
 */
//@RunWith(Suite.class)
public class FixedModeBackendGatewayTest {
  
  
  private BackendConfiguration configuration = null;
  private S3Facade s3Facade = null;
  private BackendGateway backendGateway = null; 
  
  @Before
  public void setUp() {
    
    configuration = DefaultBackendConfiguration.getInstance("test");
    s3Facade = new S3FacadeStub();
    
  }
  
  @Test
  public void testConstructor() {
    backendGateway = new FixedModeBackendGateway(configuration, s3Facade);
  }
  
  
  @Test(expected=IllegalArgumentException.class)
  public void testConstructorWithNullBackendConfiguration() {
    backendGateway = new FixedModeBackendGateway(null, s3Facade);
  }

  
  @Test(expected=IllegalArgumentException.class)
  public void testConstructorWithNullS3Facade() {
    backendGateway = new FixedModeBackendGateway(configuration, null);
  }

  
  @Test(expected=RuntimeException.class)
  public void testConstructorWhenNoMapFileIsPointedInConfiguration() {
    configuration = DefaultBackendConfiguration.getInstance("no-profiles-map-test");
    backendGateway = new FixedModeBackendGateway(configuration, s3Facade);
  }
  
  
  @Test(expected=RuntimeException.class)
  public void testConstructorWhenNoAllProfilesDefinitionIsPointedInConfiguration() {
    configuration = DefaultBackendConfiguration.getInstance("no-all-profiles-test");
    backendGateway = new FixedModeBackendGateway(configuration, s3Facade);
  }

  
  @Test
  public void testGetAllProfiles() {
   
    backendGateway = new FixedModeBackendGateway(configuration, s3Facade);
    String allProfilesAsString = backendGateway.getAllProfiles();
    assertNotNull(allProfilesAsString);
    
  }
  
  @Test(expected=RuntimeException.class)
  public void testGetAllProfilesWithWronAllProfilesPath() {
    
    configuration = DefaultBackendConfiguration.getInstance("test-wrong-all-profiles-path");
    backendGateway = new FixedModeBackendGateway(configuration, s3Facade);
    backendGateway.getAllProfiles();
    
  }
  

  @Test
  public void testGetPathProfile() {
   
    backendGateway = new FixedModeBackendGateway(configuration, s3Facade);
    String profileAsString = backendGateway.getPathProfile("/standard");
    assertNotNull(profileAsString);
  
  }


  @Test
  public void testGetRootPathProfile() {
   
    backendGateway = new FixedModeBackendGateway(configuration, s3Facade);
    String profileAsString = backendGateway.getPathProfile("/");
    assertNotNull(profileAsString);
    
  }

  
  
//  private static final Logger log = LoggerFactory.getLogger(FixedModeBackendGateway.class);
//  
//  @Mock
//  private BackendConfiguration backendConfiguration;
//  
//  @Before
//  public void setUp() throws Exception {
//    
//    log.info("setUp()");    
//    
//    MockitoAnnotations.initMocks(this);
//    
//  } // setUp()
//  
//
//  @Test
//  public void testNoExceptionPath() {
//    
//
//    when(
//        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_ALL_PROFILES_FILE)
//    ).thenReturn(
//        "config/fixed-mode/all-profiles.json"
//    );
//
//    when(
//        //backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PATHS_PROFILES_FILE)
//        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PROFILES_MAP_FILE)
//    ).thenReturn(
//        //"config/fixed-mode/buckets-profiles.json"
//        "config/fixed-mode/profiles-map.json"
//    );  
//
//    
//    FixedModeBackendGateway fixedModeBackendGateway = new FixedModeBackendGateway(this.backendConfiguration, new S3FacadeImpl(new MinioS3Gateway(new MinioS3ClientBuilderImpl(), new S3ConnectionPropertiesDefaultProvider())));
//    
//    /*
//     * get all profiles
//     */
//    String allProfilesString = fixedModeBackendGateway.getAllProfiles();
//    log.info("All profiles in JSON format: {}", allProfilesString);
//    
//    /*
//     * iterate through profiles and change 
//     */
//    String pathProfileName = fixedModeBackendGateway.getPathProfile("/golden");
//    assertNotNull(pathProfileName);
//    log.info("Profile name for path \"/golden\" is {}", pathProfileName);
//    
//    
//  } // testNoExceptionPath()
//
//  
//  @Test(expected = RuntimeException.class)
//  public void testNoAllProfilesFile() {
//    
//
//    when(
//        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_ALL_PROFILES_FILE)
//    ).thenReturn(
//        "just-no-existing-path"
//    );
//
//    when(
//        //backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PATHS_PROFILES_FILE)
//        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PROFILES_MAP_FILE)
//    ).thenReturn(
//        //"config/fixed-mode/buckets-profiles.json"
//        "config/fixed-mode/profiles-map.json"
//    );  
//
//    
//    FixedModeBackendGateway fixedModeBackendGateway = new FixedModeBackendGateway(this.backendConfiguration, new S3FacadeImpl(new MinioS3Gateway(new MinioS3ClientBuilderImpl(), new S3ConnectionPropertiesDefaultProvider())));
//    
//    /*
//     * get all profiles
//     */
//    fixedModeBackendGateway.getAllProfiles();
//    
//    
//  } // testNoAllProfilesFile()
//
//
//  @Test(expected = RuntimeException.class)
//  public void testNoPathsProfilesFile() {
//    
//
//    when(
//        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_ALL_PROFILES_FILE)
//    ).thenReturn(
//        "config/fixed-mode/all-profiles.json"
//    );
//
//    when(
//        //backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PATHS_PROFILES_FILE)
//        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PROFILES_MAP_FILE)
//    ).thenReturn(
//        "just-no-existing-path"
//    );  
//
//    
//    FixedModeBackendGateway fixedModeBackendGateway = new FixedModeBackendGateway(this.backendConfiguration, new S3FacadeImpl(new MinioS3Gateway(new MinioS3ClientBuilderImpl(), new S3ConnectionPropertiesDefaultProvider())));
//    
//    /*
//     * get all profiles
//     */
//    fixedModeBackendGateway.getPathProfile("/golden");
//    
//    
//  } // testNoAllProfilesFile()
//
//  
  
} // FixedModeBackendGatewayTest()
