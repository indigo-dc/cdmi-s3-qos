/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


/**
 * Provides auxiliary methods to operate on JSON objects and Strings representing JSON objects.
 * 
 * @author @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class JsonUtils {


  /**
   * Reads file pointed by path and returns the content of the file in String format.
   * (The assumption is that the file contains UTF-8 encoded JSON object.)
   * 
   * @param path Path to file with UTF-8 encoded JSON object.
   * 
   * @return String with value representing content of file pointed by path.
   */
  private static String fileToString(String path) {

    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(path);
    } catch (FileNotFoundException ex) {
      throw new RuntimeException(String.format("Failed to open %s file.", path), ex);
    }

    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    } catch (UnsupportedEncodingException ex1) {
      try {
        inputStream.close();
      } catch (IOException ex2) {
        // empty, on purpose
      }
      throw new RuntimeException(String.format(
          "File with definition of all available profiles %s is encoded in unsupported format",
          inputStream), ex1);
    }

    /*
     * each line read will be added to that buffer
     */
    StringBuffer stringBuilder = new StringBuffer();

    /*
     * iterate through all available lines
     */
    try {

      String line = bufferedReader.readLine();

      while (line != null) {
        stringBuilder.append(line);
        line = bufferedReader.readLine();
      }

    } catch (IOException ex) {

      throw new RuntimeException(
          String.format("Failed to read definition of all available profiles from %s", path), ex);

    } finally {

      try {
        inputStream.close();
      } catch (IOException ex1) {
        // empty, on purpose
      }

    }

    return stringBuilder.toString();

  } // fileToString


  /**
   * Converts JSON object, from the file pointed by path parameter, to JSONObject.
   * 
   * @param path Path to UTF-8 encoded file with JSON.
   * 
   * @return JSONObject created from file pointed by path parameter.
   */
  public static JSONObject createJsonObjectFromFile(String path) {

    String jsonObjectAsString = fileToString(path);

    JSONObject rv = new JSONObject(jsonObjectAsString);

    return rv;

  } // createJsonObjectFromFile


  /**
   * Converts JSON object, from the file pointed by path parameter, to JSONArrsy. 
   * 
   * @param path Path to UTF-8 encoded file with JSON (exactly, with JSON array).
   * 
   * @return JSONArray created from file pointed by path parameter.
   */
  public static JSONArray createJsonArrayFromFile(String path) {

    String jsonArrayAsString = fileToString(path);

    JSONArray rv = new JSONArray(jsonArrayAsString);

    return rv;

  } // createJsonArrayFromFile()

} // end of JsonUtils class
