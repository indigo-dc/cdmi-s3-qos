package org.indigo.cdmi.backend.radosgw;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.indigo.cdmi.backend.s3.MinioS3ClientBuilder;
import org.indigo.cdmi.backend.s3.MinioS3Gateway;
import org.indigo.cdmi.backend.s3.S3ConnectionPropertiesDefaultProvider;
import org.indigo.cdmi.backend.s3.S3Facade;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedModeBackendGatewayTest {

  private static final Logger log = LoggerFactory.getLogger(FixedModeBackendGateway.class);
  
  @Mock
  private BackendConfiguration backendConfiguration;
  
  @Before
  public void setUp() throws Exception {
    
    log.info("setUp()");    
    
    MockitoAnnotations.initMocks(this);
    
  } // setUp()
  

  @Test
  public void testNoExceptionPath() {
    

    when(
        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_ALL_PROFILES_FILE)
    ).thenReturn(
        "config/fixed-mode/all-profiles.json"
    );

    when(
        //backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PATHS_PROFILES_FILE)
        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PROFILES_MAP_FILE)
    ).thenReturn(
        //"config/fixed-mode/buckets-profiles.json"
        "config/fixed-mode/profiles-map.json"
    );  

    
    FixedModeBackendGateway fixedModeBackendGateway = new FixedModeBackendGateway(this.backendConfiguration, new S3Facade(new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesDefaultProvider())));
    
    /*
     * get all profiles
     */
    String allProfilesString = fixedModeBackendGateway.getAllProfiles();
    log.info("All profiles in JSON format: {}", allProfilesString);
    
    /*
     * iterate through profiles and change 
     */
    String pathProfileName = fixedModeBackendGateway.getPathProfile("/golden");
    assertNotNull(pathProfileName);
    log.info("Profile name for path \"/golden\" is {}", pathProfileName);
    
    
  } // testNoExceptionPath()

  
  @Test(expected = RuntimeException.class)
  public void testNoAllProfilesFile() {
    

    when(
        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_ALL_PROFILES_FILE)
    ).thenReturn(
        "just-no-existing-path"
    );

    when(
        //backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PATHS_PROFILES_FILE)
        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PROFILES_MAP_FILE)
    ).thenReturn(
        //"config/fixed-mode/buckets-profiles.json"
        "config/fixed-mode/profiles-map.json"
    );  

    
    FixedModeBackendGateway fixedModeBackendGateway = new FixedModeBackendGateway(this.backendConfiguration, new S3Facade(new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesDefaultProvider())));
    
    /*
     * get all profiles
     */
    fixedModeBackendGateway.getAllProfiles();
    
    
  } // testNoAllProfilesFile()


  @Test(expected = RuntimeException.class)
  public void testNoPathsProfilesFile() {
    

    when(
        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_ALL_PROFILES_FILE)
    ).thenReturn(
        "config/fixed-mode/all-profiles.json"
    );

    when(
        //backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PATHS_PROFILES_FILE)
        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PROFILES_MAP_FILE)
    ).thenReturn(
        "just-no-existing-path"
    );  

    
    FixedModeBackendGateway fixedModeBackendGateway = new FixedModeBackendGateway(this.backendConfiguration, new S3Facade(new MinioS3Gateway(new MinioS3ClientBuilder(), new S3ConnectionPropertiesDefaultProvider())));
    
    /*
     * get all profiles
     */
    fixedModeBackendGateway.getPathProfile("/golden");
    
    
  } // testNoAllProfilesFile()

  
  
} // FixedModeBackendGatewayTest()
