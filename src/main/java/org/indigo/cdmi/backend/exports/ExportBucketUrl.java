package org.indigo.cdmi.backend.exports;

public class ExportBucketUrl implements ExportAttributeProvider {

  public static final String EXPORT_BUCKET_RELATIVE_URL_PATH = "export-bucket-url";
  
  @Override
  public String getProviderName() {
    return EXPORT_BUCKET_RELATIVE_URL_PATH;
  }

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
    
    if (processingLeadingSlashes == false) {
      path = path.substring(0, endOfBucketNameIndex + 1);
    }
    
    
    return baseAddr + path;
  
  
  }

}
