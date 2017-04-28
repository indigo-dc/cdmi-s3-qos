package org.indigo.cdmi.backend.radosgw.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ConfigurationContextTest {

  @Before
  public void setUp() throws Exception {}

  @Test(expected=IllegalStateException.class)
  public void test() {
    ConfigurationContext.registerConfiguration(null);
    ConfigurationContext.getConfiguration();
  }

}
