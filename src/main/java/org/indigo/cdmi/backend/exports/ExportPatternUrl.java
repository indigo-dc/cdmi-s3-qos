package org.indigo.cdmi.backend.exports;


import org.apache.commons.lang3.StringUtils;
import org.indigo.cdmi.backend.s3.S3Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class used to calculate actual value of {export-pattern-url} placeholder which can be
 * used in export.json file.
 *  
 * @author Gracjan Jankowski
 *
 */
public class ExportPatternUrl implements ExportAttributeProvider {

  private static final Logger log = LoggerFactory.getLogger(ExportPatternUrl.class);
  
  public static final String EXPORT_PATTERN_URL = "export-pattern-url";
  
  @Override
  public String getProviderName() {
    return EXPORT_PATTERN_URL;
  }

  
  @Override
  public String attributeValue(String objectPath, String providerArgs) {
  
    log.debug("attributeValue({},{})", objectPath, providerArgs);
    
    if (objectPath == null) {
      throw new IllegalArgumentException("objectPath argument passed to method is NULL");
    }
    
    if (providerArgs == null) {
      throw new IllegalArgumentException("providerArgs argument passed to method is NULL");
    }
    
    
    String trimedArgs = providerArgs.trim();

    String rawArgs = trimedArgs.substring(1, trimedArgs.length() - 1);
    
    String[] args = rawArgs.split("\\s+");

    String addressPattern = args[1];

    String fullPath = objectPath;
    String bucketName = S3Utils.getBucketNameFromPath(objectPath);
    String relativePath = StringUtils.removeStart(fullPath, "/" + bucketName);
    relativePath = StringUtils.removeStart(relativePath, "/");

    /*
     * replace placeholders from pattern with actual values
     */
    addressPattern = StringUtils.replaceEach(
        addressPattern, 
        new String[] {"{bucket}", "{relative-path}", "{full-path}"}, 
        new String[] {bucketName, relativePath, fullPath}
    );

   
    /*
     * if URL dosen't contain any path segment then remove final slash from this URL
     */
    if ((!addressPattern.endsWith(fullPath)) 
        && (relativePath.length() == 0 || !addressPattern.endsWith(relativePath)) 
        && (bucketName.length() == 0 || !addressPattern.endsWith(bucketName))) {
      
      addressPattern = StringUtils.removeEnd(addressPattern, "/");  
    
    } // if()
    
    
    /*
     * if any value used to substitute placeholder were empty string then 
     * "insane" URL could come up, below this "insane" parts of URL are
     * being corrected
     */
    addressPattern = StringUtils.replace(addressPattern, "://.", "://", 1);
    
    // remove duplicated slashes (//)
    addressPattern = StringUtils.replace(addressPattern, "//", "/");
    addressPattern = StringUtils.replace(addressPattern, ":/", "://", 1);
    
    return addressPattern;

  } // attributeValue()

} // end of ExportsPatternUrl class
