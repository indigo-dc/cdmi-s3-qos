/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


/**
 * This class is meant only for internal testing purposes. 
 * It implements BackendGateway interface, but instead of communicating with RADOS GW 
 * (as instacjes of BackendGateway interface are supposed to do), 
 * it mimics RADOS GW server responses basing on data read from dedicated 
 * configuration files.
 * 
 * @author Gracjan Jankowski (gracjan@man.poznan.pl)
 */
public class FixedModeBackendGateway implements BackendGateway {

  private static final Logger log = LoggerFactory.getLogger(FixedModeBackendGateway.class);

  public static final String PARAMETER_ALL_PROFILES_FILE =
      "objectstore.backend-gateway.fixed-mode.all-profiles-file";
  
  public static final String PARAMETER_PATHS_PROFILES_FILE =
      "objectstore.backend-gateway.fixed-mode.paths-profiles-file";

  private String allProfilesFilePath = null;
  private String pathsProfilesFilePath = null;


  /**
   * Constructor. 
   */
  @Inject
  public FixedModeBackendGateway(BackendConfiguration backendConfiguration) {

    allProfilesFilePath = backendConfiguration.get(PARAMETER_ALL_PROFILES_FILE);
    if (allProfilesFilePath == null) {
      throw new RuntimeException("Cannot instatiate FixedModeBackendGateway. Could not find "
          + PARAMETER_ALL_PROFILES_FILE + " parameter in conifguration resources.");
    }

    pathsProfilesFilePath = backendConfiguration.get(PARAMETER_PATHS_PROFILES_FILE);
    if (allProfilesFilePath == null) {
      throw new RuntimeException("Cannot instatiate FixedModeBackendGateway. Could not find "
          + PARAMETER_PATHS_PROFILES_FILE + " parameter in conifguration resources.");
    }

  } // FixedModeBackendGateway()


  /**
   * @see BackendGateway#getAllProfiles().
   */
  @Override
  public String getAllProfiles() {

    /*
     * Open (as InputStream) file with definition of all available profiles
     */
    InputStream fis = null;
    try {
      fis = new FileInputStream(allProfilesFilePath);
    } catch (FileNotFoundException ex) {
      // to avoid logs pollution it will be logged on ObjectStoreBacked's method level (entry point
      // to SPI)
      // log.error("Failed to open {} file.", allProfilesFilePath);
      throw new RuntimeException(String.format("Failed to open %s file.", allProfilesFilePath), ex);
    }


    /*
     * wrap BufferedReader around InputStream, it will allow to read data from InputStream line by
     * line
     */
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
    } catch (UnsupportedEncodingException ex) {
      try {
        fis.close();
      } catch (IOException ex1) {
        // ignore
      }
      throw new RuntimeException(String.format(
          "File with definition of all available profiles %s is encoded in unsupported format",
          allProfilesFilePath), ex);
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

      throw new RuntimeException(String.format(
          "Failed to read definition of all available profiles from %s", allProfilesFilePath), ex);

    } finally {

      try {
        fis.close();
      } catch (IOException ex) {
        // ignore
      }

    }

    return stringBuilder.toString();

  } // getAllProfiles()


  /**
   * Returns QoS profile assigned to given path.
   * 
   * @see BackendGateway#getPathProfile(String)
   */
  @Override
  public String getPathProfile(String path) {

    log.debug("getPathProfile({})", path);
    
    /*
     * get bucket name
     */
    String bucketName = "/";
    if (!path.equals("/")) {
          
      bucketName = S3Utils.getBucketNameFromPath(path);
    
    } // path.equals("/")
    log.debug("Bucket name: {}", bucketName);


    /*
     * create (read from file) JSONObject with bucket=>profile map
     */
    JSONObject bucketsMap = null;
    try {
      bucketsMap = JsonUtils.createJsonObjectFromFile(pathsProfilesFilePath);
    } catch (Exception ex) {
      throw new RuntimeException("Failed to read buckets map from file " + pathsProfilesFilePath);
    }
    log.debug("Buckets map: {}", bucketsMap);

    /*
     * create (read from file) JSONArray with all profiles
     */
    JSONArray profilesArray = null;
    try {
      profilesArray = JsonUtils.createJsonArrayFromFile(allProfilesFilePath);
    } catch (Exception ex) {
      throw new RuntimeException(
          "Failed to read all profiles array from file " + pathsProfilesFilePath);
    }
    log.debug("All (available/configured) profiles: {}", profilesArray);

    /*
     * get profile name
     */
    String profileName = null;
    try {
      profileName = bucketsMap.getString(bucketName);
    } catch (JSONException ex) {
      throw new RuntimeException("Bunket of name " + bucketName + " is not mapped to profile in "
          + pathsProfilesFilePath + " file");
    } catch (Exception ex) {
      throw new RuntimeException(
          "Failed to map bucket of name " + bucketName + " to profile name.");
    }
    log.debug("Profile name for bucket {} is {}", bucketName, profileName);


    /*
     * find profile of given name (iterate through all profiles)
     */
    JSONObject wantedObject = null;
    for (int i = 0; i < profilesArray.length(); i++) {
      JSONObject currJsonObject = profilesArray.getJSONObject(i);
      String currentProfileName = currJsonObject.getString("name");
      log.debug("Processing profile: {}", currJsonObject);
      log.debug("Processing profile's name: {}", currentProfileName);
      if (profileName.equals(currentProfileName)) {
        wantedObject = currJsonObject;
        break;
      }
    }

    log.debug("wanted object: {}", wantedObject);

    if (wantedObject == null) {
      throw new RuntimeException("Failed to find profile of name " + profileName
          + ". Probably there is no such profile in file " + allProfilesFilePath);
    }

    return wantedObject.toString();

  } // getPathProfile()


} // end of FixedModeBackendGateway class
