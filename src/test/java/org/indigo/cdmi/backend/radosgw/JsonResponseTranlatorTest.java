package org.indigo.cdmi.backend.radosgw;

import static org.junit.Assert.*;

import java.util.List;

import org.indigo.cdmi.BackendCapability;
import org.indigo.cdmi.CdmiObjectStatus;
import org.indigo.cdmi.backend.capattrs.CdmiAttributeProvider;
import org.indigo.cdmi.backend.capattrs.CdmiAttributeProviderRegistry;
import org.junit.Before;
import org.junit.Test;


class StubCdmiAttributeProviderRegistry implements CdmiAttributeProviderRegistry {

  @Override
  public CdmiAttributeProvider register(String providerName, CdmiAttributeProvider provider) {
    return null;
  }

  @Override
  public CdmiAttributeProvider getProvider(String providerName) {
    
    return new CdmiAttributeProvider() {
      
      @Override
      public String getProviderName() {
        return "cdmi_capability_association_time";
      }
      
      @Override
      public String attributeValue(String objectPath, String attributeName, String attributePattern) {
        return "calculated-value";
      }
    };
  
  }
  
}


public class JsonResponseTranlatorTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void testConstructor() {
    new JsonResponseTranlator(new StubCdmiAttributeProviderRegistry());
  }

  
  @Test
  public void testCdmiObjectStatus() {
    
    JsonResponseTranlator translator = new JsonResponseTranlator(new StubCdmiAttributeProviderRegistry());
    String objectPath = "/standard/file1.jpg";
    
    String gatewayResponse = "{\"metadata\":{\"cdmi_capability_lifetime\":\"P20Y\",\"cdmi_data_redundancy\":\"1\",\"cdmi_latency\":\"3000\",\"cdmi_geographic_placement\":[\"PL\"],\"cdmi_data_storage_lifetime\":\"P20Y\",\"cdmi_capability_lifetime_action\":\"delete\",\"cdmi_throughput\":\"100000\"},\"metadata_provided\":{\"cdmi_throughput_provided\":\"90000\",\"cdmi_capability_lifetime_provided\":\"P20Y\",\"cdmi_data_redundancy_provided\":\"1\",\"cdmi_geographic_placement_provided\":[\"PL\"],\"cdmi_latency_provided\":\"1000\",\"cdmi_capability_association_time_provided\":\"{cdmi_capability_association_time}\",\"cdmi_data_storage_lifetime_provided\":\"P20Y\",\"cdmi_capability_lifetime_action_provided\":\"delete\"},\"capabilities\":{\"cdmi_capability_lifetime\":\"true\",\"cdmi_capability_association_time\":\"true\",\"cdmi_data_redundancy\":\"true\",\"cdmi_geographic_placement\":\"true\",\"cdmi_latency\":\"true\",\"cdmi_capabilities_exact_inherit\":\"true\",\"cdmi_data_storage_lifetime\":\"true\",\"cdmi_capabilities_templates\":\"true\",\"cdmi_capability_lifetime_action\":\"true\",\"cdmi_throughput\":\"true\"},\"name\":\"DataobjectProfile1\",\"type\":\"dataobject\"}";
    
    CdmiObjectStatus objStat = translator.getCdmiObjectStatus(objectPath, gatewayResponse, false);
    assertNotNull(objStat);
    
  } // testCdmiObjectStatus()
  
  
  @Test
  public void testCdmiContainerStatus() {

    JsonResponseTranlator translator = new JsonResponseTranlator(new StubCdmiAttributeProviderRegistry());
    String objectPath = "/standard";
    String gatewayResponse = "{\"metadata\":{\"cdmi_default_dataobject_capability_class\":\"/cdmi_capabilities/dataobject/DataobjectProfile1\",\"cdmi_capability_lifetime\":\"P20Y\",\"cdmi_data_redundancy\":\"1\",\"cdmi_location\":[\"/standard\"],\"cdmi_latency\":\"3000\",\"cdmi_geographic_placement\":[\"DE\"],\"cdmi_data_storage_lifetime\":\"P20Y\",\"cdmi_capability_lifetime_action\":\"delete\",\"cdmi_throughput\":\"100000\"},\"metadata_provided\":{\"cdmi_default_dataobject_capability_class\":\"/cdmi_capabilities/dataobject/DataobjectProfile1\",\"cdmi_capability_lifetime\":\"P20Y\",\"cdmi_location\":[\"/standard\"],\"cdmi_throughput_provided\":\"90000\",\"cdmi_data_redundancy_provided\":\"1\",\"cdmi_geographic_placement_provided\":[\"DE\"],\"cdmi_latency_provided\":\"1000\",\"cdmi_data_storage_lifetime\":\"P20Y\",\"cdmi_capability_lifetime_action\":\"delete\"},\"capabilities\":{\"cdmi_capability_lifetime\":\"true\",\"cdmi_capability_association_time\":\"true\",\"cdmi_default_dataobject_capability_class\":\"true\",\"cdmi_data_redundancy\":\"true\",\"cdmi_location\":\"true\",\"cdmi_geographic_placement\":\"true\",\"cdmi_latency\":\"true\",\"cdmi_capabilities_exact_inherit\":\"true\",\"cdmi_data_storage_lifetime\":\"true\",\"cdmi_capabilities_templates\":\"true\",\"cdmi_capability_lifetime_action\":\"true\",\"cdmi_throughput\":\"true\"},\"name\":\"ContainerProfile1\",\"type\":\"container\"}";
    
    CdmiObjectStatus objStat = translator.getCdmiObjectStatus(objectPath, gatewayResponse, true);
    assertNotNull(objStat);
    
  }
  
  @Test
  public void testGetBackentCapabilitiesList() {
    
    String fakedGatewayResponse = JsonUtils.fileToString("test/backend-capabilities-response.json");
    JsonResponseTranlator translator = new JsonResponseTranlator(new StubCdmiAttributeProviderRegistry());
    List<BackendCapability> capabilitiesList = translator.getBackendCapabilitiesList(fakedGatewayResponse);
    assertFalse(capabilitiesList.isEmpty());
  
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testGetBackentCapabilitiesListWithNullAsGatewayResponse() {
    
    String fakedGatewayResponse = null;
    JsonResponseTranlator translator = new JsonResponseTranlator(new StubCdmiAttributeProviderRegistry());
    translator.getBackendCapabilitiesList(fakedGatewayResponse);
  
  }
  
  
  
}
