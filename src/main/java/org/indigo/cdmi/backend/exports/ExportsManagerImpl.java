package org.indigo.cdmi.backend.exports;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.indigo.cdmi.backend.radosgw.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * 
 * @author gracjan
 *
 */
public class ExportsManagerImpl implements ExportsManager {

  static public final String EXPORTS_CONFIG_FILE_PATH = "config/fixed-mode/exports.json";
  
  static private final Logger log = LoggerFactory.getLogger(ExportsManagerImpl.class);
    

  private final ExportAttributeProviderRegistry exportAttributeProviderRegistry;
  
  
  
  @Inject
  public ExportsManagerImpl(ExportAttributeProviderRegistry exportAttributeProviderRegistry) {
    
    this.exportAttributeProviderRegistry = exportAttributeProviderRegistry;
    
  }
  
  
  /**
   * 
   * @param object
   */
  private void processValues(Object object, String path) {
  
    if (object instanceof JSONObject) {

      processJsonObject((JSONObject) object, path);
    
    } else if (object instanceof JSONArray) {
    
      processJsonArray((JSONArray)object, path);
      
    } else {
      
      throw new IllegalArgumentException("No support for argument of type " + object.getClass().getName());
    
    } // if()
    
  } // processValue()

  
  
  private String extractProviderName(String fullCommand) {
    
    int inputStrLength = fullCommand.length();
    
    if (inputStrLength <= 3) return "";
    
    return fullCommand.trim().substring(1, inputStrLength-1).split(" ")[0];
    
  } // extractAttributeProviderNamme

  
  
  /**
   * 
   * @param fullCommand
   * @param path
   * @return
   */
  private String processLeafCommand(String fullCommand, String path) {
  
    String providerName = extractProviderName(fullCommand);
    
    ExportAttributeProvider provider = exportAttributeProviderRegistry.getProvider(providerName);
    if(provider == null) {
      
      throw new RuntimeException("Failed to find export attribute provider named " + providerName);
      
    } // if(provider == null)
    
    String calculatedValue = provider.attributeValue(path, fullCommand);
    
    return calculatedValue;
  
  } // processLeafCommand()
  
  
  /**
   * 
   * @param value
   */
  private void processObjectLeaf(JSONObject jsonObject, String key, String value, String path) {
    
    String trimedValue = value.trim(); 
    
    if (trimedValue.charAt(0) == '{' && trimedValue.charAt(trimedValue.length() - 1) == '}') {
      
      String calculatedValue = processLeafCommand(trimedValue, path);
      jsonObject.put(key, calculatedValue);  
      
    }
    
  } // processObjectString()
  
  
  /**
   * 
   * @param jsonArray
   * @param index
   * @param value
   */
  private void processArrayLeaf(JSONArray jsonArray, int index, String value, String path) {
    
    String trimedValue = value.trim(); 
    
    if (trimedValue.charAt(0) == '{' && trimedValue.charAt(trimedValue.length() - 1) == '}') {
      
      String calculatedValue = processLeafCommand(trimedValue, path);
      jsonArray.put(index, calculatedValue);  
      
    }
    
  } // prprocessArrayLeaf()
  
  
  /**
   * 
   * @param jsonObject
   */
  private void processJsonObject(JSONObject jsonObject, String path) {
    
  
    Iterator<String> keys = jsonObject.keys();
    while (keys.hasNext()) {
      
      String key = keys.next();
      
      Object value = jsonObject.get(key);
      
      if(value instanceof String) {
        processObjectLeaf(jsonObject, key, (String) value, path);      
      } else {
        processValues(value, path);
      }
      
    } // while()
    
    return;
  
  } // processJsonObject()
  
  
  /**
   * 
   */
  private void processJsonArray(JSONArray jsonArray, String path) {
    
    Map<Integer, String> leafsToBeProcessed = new HashMap<>();
    for (int index = 0; index < jsonArray.length(); index++) {

      Object value = jsonArray.get(index);
      
      if (value instanceof String) {
        leafsToBeProcessed.put(index, (String) value);
      } else {
        processValues(value, path);
      }
            
    } // for()
    
    
    Set<Integer> indexes = leafsToBeProcessed.keySet();
    for (Integer index : indexes) {
      
      processArrayLeaf(jsonArray, index, leafsToBeProcessed.get(index), path);
      
    } // for()
    
    
    return;
    
  } // processJsonArray()
  
  
  /* (non-Javadoc)
   * @see org.indigo.cdmi.backend.exports.ExportsManager#getExports(java.lang.String)
   */
  @Override
  public Map<String, Object> getExports(String path) {
        
    Map<String, Object> outputExportsMap = new HashMap<>();
    
    /*
     * first find matching mappings from exports.json
     */
    
    JSONObject exportsMappings = JsonUtils.createJsonObjectFromFile(EXPORTS_CONFIG_FILE_PATH);
    
    Iterator<String> entryPointPaths = exportsMappings.keys();
    
    JSONObject exports = null;
    String longestMatchedPath = null;
    
    while (entryPointPaths.hasNext()) {
      
      String currEntryPathKey = entryPointPaths.next();
      JSONObject currExportsObject = exportsMappings.getJSONObject(currEntryPathKey);
      
      if (path.startsWith(currEntryPathKey)) {
        
        if (exports == null) {
          exports = currExportsObject;
          longestMatchedPath = currEntryPathKey;
          continue;
        } // if(exports)
      
        if (currEntryPathKey.length() > longestMatchedPath.length()) {
          exports = currExportsObject;
          longestMatchedPath = currEntryPathKey;
          continue;
        }
        
      } // if()
      
    } // while()
    
    
    /*
     * if no matchings have been found then return empty map
     */
    if (exports == null) {
      return outputExportsMap;
    }
       
    
    /*
     * Walk through the description and replace placeholders with actual values
     */
    processValues(exports, path);

    
    /*
     * prepare output payload 
     */
    Iterator<String> foundExportsKeys = exports.keys();
    while (foundExportsKeys.hasNext()) {
      
      
      String key = foundExportsKeys.next();
      Object value = exports.get(key); 
      
      outputExportsMap.put(key, value);
      
    } // while()
    
    
    return outputExportsMap;
  
  } // getExports()
  
  
} // end of ExportsManager class
