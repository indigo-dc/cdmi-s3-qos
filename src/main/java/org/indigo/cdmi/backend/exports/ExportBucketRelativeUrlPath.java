package org.indigo.cdmi.backend.exports;

import org.apache.commons.lang3.StringUtils;

public class ExportBucketRelativeUrlPath implements ExportAttributeProvider {

  public static final String EXPORT_BUCKET_RELATIVE_URL_PATH = "export-bucket-relative-url";
  
  
  @Override
  public String getProviderName() {
  
    return EXPORT_BUCKET_RELATIVE_URL_PATH;

  } // getProviderName()

  
  @Override
  public String attributeValue(String path, String attributePlaceholder) {
    
    String trimedPlaceholder = attributePlaceholder.trim();
    
    String rawPlaceholder = trimedPlaceholder.substring(1, trimedPlaceholder.length() - 1);
    
    String[] args = rawPlaceholder.split("\\s+");
    
    String baseAddr = args[1];
   
    boolean processingLeadingSlashes = true;
    int endOfBucketNameIndex = 0;
    for (int index = 0; index < path.length(); index++) {
    
      char currentChar = path.charAt(index);
      
      if ((processingLeadingSlashes) && (currentChar == '/')) {
        continue;
      }
      
      if ((processingLeadingSlashes) && (currentChar != '/')) {
        processingLeadingSlashes = false;
        endOfBucketNameIndex = index;
        continue;
      }
      
      if (currentChar != '/') {
        endOfBucketNameIndex = index;
        continue;
      } else {
        break;
      }
            
    } // for()
    
    
    /*
     * if endOfBucketNameIndex points to the end of path then remove closing slash (if present) 
     */
    if (processingLeadingSlashes == false && (endOfBucketNameIndex + 1) < path.length()) {
      //path = path.substring(endOfBucketNameIndex + 1, path.length() - 1);
      path = StringUtils.removeEnd(path, "/");
    }
    
    return baseAddr + path;

  } // attributeValue()

} // end of ExportBucketUrl class
