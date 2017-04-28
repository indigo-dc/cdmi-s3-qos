package org.indigo.cdmi.backend.exports;

import static org.junit.Assert.*;

import java.util.Map;

import org.indigo.cdmi.backend.radosgw.DefaultBackendConfiguration;
import org.indigo.cdmi.backend.radosgw.config.ConfigurationContext;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/* (non-Javadoc)
 * 
 */
class FakeExportAttributeProvider implements ExportAttributeProvider {

  @Override
  public String getProviderName() {
    // TODO Auto-generated method stub
    return "export-pattern-url";
  }

  @Override
  public String attributeValue(String objectPath, String attributePlaceholder) {
    return "out-of-interest";
  }
  
}


/* (non-Javadoc)
 * ExportAttributeProviderRegistry stub for tests purposes
 */
class ExportAttributeProviderRegistryStub implements ExportAttributeProviderRegistry {

  @Override
  public ExportAttributeProvider register(String providerName, ExportAttributeProvider provider) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ExportAttributeProvider getProvider(String providerName) {
    // TODO Auto-generated method stub
    return new FakeExportAttributeProvider();
  }
  
} // end of ExportAttributeProviderRegistryStub



/* (not-Javadoc)
 *  Main test case for ExportsManagerImpl class
 */
public class ExportsManagerImplTest {

  private static final Logger log = LoggerFactory.getLogger(ExportsManagerImplTest.class); 
  
  private ExportsManagerImpl exportsManager = null;
  
  @Before
  public void setUp() throws Exception {

    ConfigurationContext.registerConfiguration(DefaultBackendConfiguration.getInstance("test"));

    exportsManager = new ExportsManagerImpl(new ExportAttributeProviderRegistryStub());
    
  } // setUp()

  
  @Test
  public void testS3ExportForRootPath() {
    Map<String, Object> exports = exportsManager.getExports("/");
    Object exportsJsonObject = exports.get("s3");
    assertNotNull(exportsJsonObject);
  } // test()

  @Test
  public void testS3ExportForStandardPath() {
    Map<String, Object> exports = exportsManager.getExports("/standard");
    Object exportsJsonObject = exports.get("s3");
    assertNotNull(exportsJsonObject);
  }
  
  
  @Test
  public void testS3ExportWithWrongExportCommand() {
    Map<String, Object> exports = exportsManager.getExports("/standard_with_wrong_export_command");
    Object exportsJsonObject = exports.get("s3");
    assertNotNull(exportsJsonObject);
  }
  
  
  @Test
  public void testS3ExportWithEmptyCommand() {
    Map<String, Object> exports = exportsManager.getExports("/standard_with_empty_command");
    Object exportsJsonObject = exports.get("s3");
    assertNotNull(exportsJsonObject);
  }
  

  @Test
  public void testS3ExportWithNoOpeningCurlyBracketCommand() {
    Map<String, Object> exports = exportsManager.getExports("/standard_with_no_opening_curly_bracket_command");
    Object exportsJsonObject = exports.get("s3");
    assertNotNull(exportsJsonObject);
  }

  @Test
  public void testS3ExportWithNoClosingCurlyBracketCommand() {
    Map<String, Object> exports = exportsManager.getExports("/standard_with_no_closing_curly_bracket_command");
    Object exportsJsonObject = exports.get("s3");
    assertNotNull(exportsJsonObject);
  }

  @Test
  public void noExportsCase() {

    ConfigurationContext.registerConfiguration(DefaultBackendConfiguration.getInstance("noexports"));
    exportsManager = new ExportsManagerImpl(new ExportAttributeProviderRegistryStub());

    Map<String, Object> exports = exportsManager.getExports("/standard");
    assertTrue(exports.isEmpty());
    
  }
  
  
  @Test(expected=RuntimeException.class)
  public void noRegisteredAttributeProvider() {

    exportsManager = new ExportsManagerImpl(new ExportAttributeProviderRegistry() {
      
      @Override
      public ExportAttributeProvider register(String providerName, ExportAttributeProvider provider) {
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public ExportAttributeProvider getProvider(String providerName) {
        // TODO Auto-generated method stub
        return null;
      }
    
    });

    Map<String, Object> exports = exportsManager.getExports("/standard");
    assertTrue(exports.isEmpty());
    
  } // end of test
  

  @Test
  public void exportsFileWithEmbeddedArrays() {

    ConfigurationContext.registerConfiguration(DefaultBackendConfiguration.getInstance("exports-with-array"));
    exportsManager = new ExportsManagerImpl(new ExportAttributeProviderRegistryStub());

    Map<String, Object> exports = exportsManager.getExports("/standard");
    assertTrue(!exports.isEmpty());
    
  }
  
  
  
} // end of test
