package org.indigo.cdmi.backend.s3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class S3PathTranslatorTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void test() {
    S3PathTranslator s3PathTranslator = new S3PathTranslator();
    String translatedPath = s3PathTranslator.translate("/any/path");
    assertEquals("/any/path", translatedPath);
  }

}
