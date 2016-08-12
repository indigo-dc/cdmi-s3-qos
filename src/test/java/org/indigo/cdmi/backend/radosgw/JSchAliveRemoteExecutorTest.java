package org.indigo.cdmi.backend.radosgw;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import static org.mockito.Mockito.*;

public class JSchAliveRemoteExecutorTest {

  private static final Logger log = LoggerFactory.getLogger(JSchAliveRemoteExecutorTest.class);
  
  private JSchAliveRemoteExecutor executor = null;
  BackendConfiguration backendConfiguration = null;
  Properties confProporties = null;
  
  class JSchProvider implements Provider<JSch> {

    @Override
    public JSch get() {
    
      
      JSch mockJSch = Mockito.mock(JSch.class);
      Session mockSession = Mockito.mock(Session.class);
      
      doNothing().when(mockSession).setConfig(any(Properties.class));
      doNothing().when(mockSession).setPassword(anyString());
      try {
        doNothing().when(mockJSch).addIdentity(anyString());
      } catch (JSchException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
        throw new RuntimeException();
      }
      
      try {
        doNothing().when(mockJSch).addIdentity(anyString(), anyString());
      } catch (JSchException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
        throw new RuntimeException();
      }
      
      try {
        doNothing().when(mockSession).connect();
      } catch (JSchException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
        throw new RuntimeException();
      }
      
      Channel mockChannel = Mockito.mock(ChannelExec.class);
      try {
        when(mockSession.openChannel("exec")).thenReturn(mockChannel);
      } catch (JSchException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
        throw new RuntimeException();
      }
      
      doNothing().when((ChannelExec)mockChannel).setCommand(anyString());
      
      doNothing().when(mockChannel).setInputStream(null);
      
      InputStream mockInputStream = Mockito.mock(InputStream.class);
      try {
        when(mockChannel.getInputStream()).thenReturn(mockInputStream);
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
        throw new RuntimeException();
      }
      
      try {
        doNothing().when(mockChannel).connect();
      } catch (JSchException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
        throw new RuntimeException();
      }
      
      try {
        when(mockInputStream.available()).thenReturn(0);
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
        throw new RuntimeException();
      }
      
      when(mockChannel.isClosed()).thenReturn(true);
      when(mockChannel.getExitStatus()).thenReturn(0);
      
      try {
        doNothing().when(mockInputStream).close();
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
        throw new RuntimeException();
      }
      
      
      doNothing().when(mockChannel).disconnect();
      
      
      try {
        when(
            mockJSch.getSession(anyString(), anyString(), anyInt())
        ).thenReturn(
            mockSession
        );
      
      } catch (JSchException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        throw new RuntimeException(e);
      }
      
      return mockJSch;
    
    }
   
  }

  
  @Before
  public void setUp() throws Exception {
    
    log.info("setUp()");
 
    
    backendConfiguration = Mockito.mock(BackendConfiguration.class);
    
    confProporties = new Properties();
    when(backendConfiguration.getProperties()).thenReturn(confProporties);
    
    this.executor = new JSchAliveRemoteExecutor(new JSchProvider(), backendConfiguration);
    
  }
  
  
  @Test(expected=RemoteExecutorException.class)
  public void testExecuteNullCmd() throws Exception {
    
    log.info("testExecuteNullCmd()");
    
    this.executor.execute(null);
  
  }

  
  @Test(expected=RuntimeException.class)
  public void testExecuteWithEmptyConfiguration() throws Exception {
    
    log.info("testExecuteWithEmptyConfiguration()");
    
    this.executor.execute("");
  }

  @Test(expected=IllegalArgumentException.class)
  public void testExecuteWithNullConfiguration() throws Exception {
    
    log.info("testExecuteWithNullConfiguration()");
    
    when(backendConfiguration.getProperties()).thenReturn(null);
    this.executor.execute("");
  }
  
  
  @Test(expected=RuntimeException.class)
  public void testWrongPortNumberFormat() throws Exception {

    log.info("testWrongPortNumberFormat()");
    
    this.confProporties.put(JSchAliveRemoteExecutor.CONF_SSH_PORT, "twenty two");
    this.executor.execute("");
    
  }
  
  @Test
  public void testHappyPath() throws Exception {
    
    this.confProporties.put(JSchAliveRemoteExecutor.CONF_SSH_PORT, "22");
    this.confProporties.put(JSchAliveRemoteExecutor.CONF_SSH_HOST, "mock-test");
    this.confProporties.put(JSchAliveRemoteExecutor.CONF_SSH_USER, "mock-user");
    this.confProporties.put(JSchAliveRemoteExecutor.CONF_SSH_PASSWORD, "mock-password");
         
    String commandOutput = this.executor.execute("");
    assertEquals(commandOutput, "");
    
  } // testHappyPath()

    
}
