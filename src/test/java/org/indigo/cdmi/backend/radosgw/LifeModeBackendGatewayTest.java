package org.indigo.cdmi.backend.radosgw;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class LifeModeBackendGatewayTest {

  private static final String REMOTE_COMMAND_OUTPUT_ALL_PROFILES = "remote-command-output-for-all-profiles";
  private static final String REMOTE_COMMAND_OUTPUT_PATH_PROFILE = "remote-command-output-for-path-profile";
  
  private LifeModeBackendGateway gateway = null;
  

  @Test(expected=IllegalArgumentException.class)
  public void testNullConfiguration() {
  
    gateway = new LifeModeBackendGateway(null, null);
  
  }

  @Test(expected=RuntimeException.class)
  public void noRemoteExecutorFactoryConfiguration() {
    
    BackendConfiguration  backendConfig = Mockito.mock(BackendConfiguration.class);
    
    when(backendConfig.getProperties()).thenReturn(new Properties());
    
    gateway = new LifeModeBackendGateway(backendConfig, null);
    
  }
  
  @Test
  public void testGetAllProfiles() {
    
    BackendConfiguration  backendConfig = Mockito.mock(BackendConfiguration.class);
    when(backendConfig.get(LifeModeBackendGateway.CONF_SSH_COMMAND_GET_BUCKET_PROFILE)).thenReturn("mock-command");
    when(backendConfig.get(LifeModeBackendGateway.CONF_SSH_COMMAND_GET_PROFILES)).thenReturn("mock-command");
    
    RemoteExecutor remoteExecutor = Mockito.mock(RemoteExecutor.class);
    try {
      when(remoteExecutor.execute(anyString())).thenReturn(REMOTE_COMMAND_OUTPUT_ALL_PROFILES);
    } catch (RemoteExecutorException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    
    gateway = new LifeModeBackendGateway(backendConfig, remoteExecutor);
    
    String allProfilesResposne = gateway.getAllProfiles();
    assertEquals(allProfilesResposne, REMOTE_COMMAND_OUTPUT_ALL_PROFILES);
    
    
  } // testGetAllProfiles()

  
  @Test(expected=RuntimeException.class)
  public void testGetAllProfilesWithRemoteExecutorException() throws Exception {

    BackendConfiguration  backendConfig = Mockito.mock(BackendConfiguration.class);
    when(backendConfig.get(LifeModeBackendGateway.CONF_SSH_COMMAND_GET_BUCKET_PROFILE)).thenReturn("mock-command");
    when(backendConfig.get(LifeModeBackendGateway.CONF_SSH_COMMAND_GET_PROFILES)).thenReturn("mock-command");
    
    RemoteExecutor remoteExecutor = Mockito.mock(RemoteExecutor.class);
    doThrow(new RemoteExecutorException("mock exception")).when(remoteExecutor).execute(anyString());
      
    gateway = new LifeModeBackendGateway(backendConfig, remoteExecutor);
    
    gateway.getAllProfiles();

    
  }
  

  @Test
  public void testGetPathProfile() {
    
    BackendConfiguration  backendConfig = Mockito.mock(BackendConfiguration.class);
    when(backendConfig.get(LifeModeBackendGateway.CONF_SSH_COMMAND_GET_BUCKET_PROFILE)).thenReturn("mock-command");
    when(backendConfig.get(LifeModeBackendGateway.CONF_SSH_COMMAND_GET_PROFILES)).thenReturn("mock-command");
    
    RemoteExecutor remoteExecutor = Mockito.mock(RemoteExecutor.class);
    try {
      when(remoteExecutor.execute(anyString())).thenReturn(REMOTE_COMMAND_OUTPUT_PATH_PROFILE);
    } catch (RemoteExecutorException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    
    gateway = new LifeModeBackendGateway(backendConfig, remoteExecutor);
    
    String pathProfileResposne = gateway.getPathProfile("/any/path");
    assertEquals(pathProfileResposne, REMOTE_COMMAND_OUTPUT_PATH_PROFILE);
    
    
  } // testGetPathProfile()


  @Test(expected=RuntimeException.class)
  public void testPathProfileWithRemoteExecutorException() throws Exception {

    BackendConfiguration  backendConfig = Mockito.mock(BackendConfiguration.class);
    when(backendConfig.get(LifeModeBackendGateway.CONF_SSH_COMMAND_GET_BUCKET_PROFILE)).thenReturn("mock-command");
    when(backendConfig.get(LifeModeBackendGateway.CONF_SSH_COMMAND_GET_PROFILES)).thenReturn("mock-command");
    
    RemoteExecutor remoteExecutor = Mockito.mock(RemoteExecutor.class);
    doThrow(new RemoteExecutorException("mock exception")).when(remoteExecutor).execute(anyString());
      
    gateway = new LifeModeBackendGateway(backendConfig, remoteExecutor);
    
    gateway.getPathProfile("/any/path");

    
  } // testPathProfileWithRemoteExecutorException()

} // end of LifeModeBackendGatewayTest class
