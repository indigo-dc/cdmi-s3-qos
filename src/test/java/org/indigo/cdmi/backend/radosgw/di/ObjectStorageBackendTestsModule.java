package org.indigo.cdmi.backend.radosgw.di;

import static org.mockito.Mockito.when;

import org.indigo.cdmi.backend.ObjectStoreBackend;
import org.indigo.cdmi.backend.radosgw.BackendConfiguration;
import org.indigo.cdmi.backend.radosgw.BackendGateway;
import org.indigo.cdmi.backend.radosgw.FixedModeBackendGateway;
import org.indigo.cdmi.backend.radosgw.GatewayResponseTranslator;
import org.indigo.cdmi.backend.radosgw.JsonResponseTranlator;
import org.indigo.cdmi.backend.radosgw.ObjectPathTranslator;
import org.indigo.cdmi.backend.s3.MinioS3ClientBuilderImpl;
import org.indigo.cdmi.backend.s3.MinioS3Gateway;
import org.indigo.cdmi.backend.s3.S3ConnectionPropertiesDefaultProvider;
import org.indigo.cdmi.backend.s3.S3ConnectionPropertiesProvider;
import org.indigo.cdmi.backend.s3.S3FacadeImpl;
import org.indigo.cdmi.backend.s3.S3Gateway;
import org.indigo.cdmi.backend.s3.S3PathTranslator;
import org.indigo.cdmi.spi.StorageBackend;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jcraft.jsch.JSch;


public class ObjectStorageBackendTestsModule extends AbstractModule {

  @Mock
  private BackendConfiguration backendConfiguration;
  
  
  
  /**
   * Constructor() 
   */
  public ObjectStorageBackendTestsModule() {
    
    MockitoAnnotations.initMocks(this);
  
    /*
     * parameters required by FixedModeBackendGateway
     */
    when(
        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_ALL_PROFILES_FILE)
    ).thenReturn(
        "config/fixed-mode/all-profiles.json"
    );

    when(
        //backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PATHS_PROFILES_FILE)
        backendConfiguration.get(FixedModeBackendGateway.PARAMETER_PROFILES_MAP_FILE)
    ).thenReturn(
        "config/fixed-mode/buckets-profiles.json"
    );

  
    
    
    
  } // ObjectStorageBackendTestsModule()
  
  
  @Override
  protected void configure() {
    
    // StorageBackend.class
    bind(StorageBackend.class).to(ObjectStoreBackend.class);
    
    // BackendGateway.class
    bind(BackendGateway.class).to(FixedModeBackendGateway.class);
    
    // GatewayResponseTranslator.class 
    bind(GatewayResponseTranslator.class).to(JsonResponseTranlator.class);
    
    // ObjectPathTranslator.class
    bind(ObjectPathTranslator.class).to(S3PathTranslator.class);

    //bind(RemoteExecutor.class).to(JSchAliveRemoteExecutor.class);

    bind(JSch.class).toProvider(JSchProvider.class);

    bind(S3FacadeImpl.class);

    bind(S3Gateway.class).to(MinioS3Gateway.class);
    
    bind(S3ConnectionPropertiesProvider.class).to(S3ConnectionPropertiesDefaultProvider.class);
    
    bind(MinioS3ClientBuilderImpl.class);
    
  } // configure

  
  @Provides
  BackendConfiguration provideBackendConfiguration() {
    return this.backendConfiguration;
  }
  
  
} // end of ObjectStorageBackendTestModule
