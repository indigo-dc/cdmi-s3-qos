package org.indigo.cdmi.backend.radosgw;

public class DummyRemoteExecutor implements RemoteExecutor {

  @Override
  public String execute(String cmd) throws RemoteExecutorException {
    return null;
  }

}
