package org.indigo.cdmi.backend.exports;

import java.util.Map;


public interface ExportsManager {

  Map<String, Object> getExports(String path);

}
