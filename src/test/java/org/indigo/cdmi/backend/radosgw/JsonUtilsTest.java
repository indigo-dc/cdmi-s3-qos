package org.indigo.cdmi.backend.radosgw;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.json.JSONArray;

public class JsonUtilsTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void testEmptyConstructorJustToGetRidOfMisleadingJaCoCoReports() {
    new JsonUtils();
  }

  @Test
  public void createJsonArrayFromFile() {
    JSONArray jsonArray = JsonUtils.createJsonArrayFromFile("test/array.json");
    assertEquals(3, jsonArray.length());
  }
  
  
  @Test(expected=RuntimeException.class)
  public void createJsonArrayFromNonExistingFile() {
    JsonUtils.createJsonArrayFromFile("any-non-existing-path");
  }
  
  
  @Test(expected=RuntimeException.class)
  public void testFileWithWrongEncoding() {
    JsonUtils.createJsonArrayFromFile("test/encoding-exception.json");
  }
  
  
}
