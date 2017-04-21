package org.indigo.cdmi.backend.exports;

import java.util.Map;

import org.json.JSONObject;


public interface ExportsManager {

  Map<String, Object> getExports(String path);

}
