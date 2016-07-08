/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.indigo.cdmi.BackEndException;
import org.indigo.cdmi.BackendCapability;
import org.indigo.cdmi.CdmiObjectStatus;
import org.indigo.cdmi.backend.radosgw.BackendGateway;
import org.indigo.cdmi.backend.radosgw.BackendGatewayFactory;
import org.indigo.cdmi.backend.radosgw.GatewayResponseTranslator;
import org.indigo.cdmi.backend.radosgw.GatewayResponseTranslatorFactory;
import org.indigo.cdmi.backend.radosgw.ObjectPathTranslator;
import org.indigo.cdmi.backend.radosgw.ObjectPathTranslatorFactory;
import org.indigo.cdmi.spi.StorageBackend;

public class ObjectStoreBackend implements StorageBackend {

	public static final String CONF_BACKEND_GATEWAY = "objectstore.backend-gateway";
	
	private static final Logger log = LoggerFactory.getLogger(ObjectStoreBackend.class);

	
	private BackendGateway backendGateway 					= null;
	private GatewayResponseTranslator responseTranslator 	= null;
	private ObjectPathTranslator pathTranslator 			= null;
	
	
	/**
	 * CONSTRUCTOR
	 * Instantiate implementation of BackendBateway and GatewayResponseTranslator
	 */
	public ObjectStoreBackend() {
		
		/*
		 * Instantiate BackendGateway
		 */
		backendGateway = BackendGatewayFactory.create();
		
		/*
		 * Instantiate GatewayResponseTranslator
		 */
		responseTranslator = GatewayResponseTranslatorFactory.create();
		
				
		/*
		 * Instantiate ObjectPathTranslator
		 */
		pathTranslator = ObjectPathTranslatorFactory.create();
		
	} // ObjectStoreBackend()
	
	
	/**
	 * @see  StorageBackend#getCapabilities()
	 */
	@Override
	public List<BackendCapability> getCapabilities() throws BackEndException {
		
		try {
			
			List<BackendCapability> backendCapabilities = new ArrayList<BackendCapability>();
			
			/*
			 * get all back-end profiles form BackendGateway
			 */
			log.info("Quering backed for all supported profiles.");
			String gatewayResponse = backendGateway.getAllProfiles();
			log.info("Response from backednd gateway: {}", gatewayResponse);
			
			log.info("Calling response translator to get list of BackendCapabilitiy(ies)");
			backendCapabilities = responseTranslator.getBackendCapabilitiesList(gatewayResponse);
			
			log.info("All backend capabilities: {}", backendCapabilities);
			
			return backendCapabilities;
		
		
		} catch(Exception ex) {
			
			log.error("Failed to get capability of underlying object store: {}", ex);
			throw new BackEndException("Failed to get capability of underlying object store.", ex);
		
		} // try{}
		
		
	} // getCapabilities()

	
	/**
	 * @see StorageBackend#updateCdmiObject(String, String);
	 */
	@Override
	public void updateCdmiObject(String path, String targetCapabilitiesUri) throws BackEndException {
		
		throw new UnsupportedOperationException("Not supported operation.");
		
		//try {
		//	
		//} catch (Exception ex) {
		//	
		//	log.error("Failed to update CDMI object {} to capability {}: {}", path, targetCapabilitiesUri, ex);
		//	throw new BackEndException(String.format("Failed to update CDMI object %s to capability %s", path, targetCapabilitiesUri), ex);
		//	
		//} // try{}
		
	} // updateCdmiObject()

	
	/**
	 * @see StorageBackend#getCurrentStatus(String)
	 */
	@Override
	public CdmiObjectStatus getCurrentStatus(String path) throws BackEndException {
	
		try {
			
			/*
			 * translate CDMI path to the form comply with underlying object storage technology 
			 */
			String objectPath = pathTranslator.translate(path);
			
			String gatewayResponse = backendGateway.getPathProfile(objectPath);
			
			log.debug("gatewayResponse: {}", gatewayResponse);
			
			CdmiObjectStatus cdmiObjectStatus = responseTranslator.getCdmiObjectStatus(gatewayResponse);
			
			return cdmiObjectStatus;
		
		} catch(Exception e) {
		
			log.error("Failed to get status of CDMI object {}: {}", path, e);
			throw new BackEndException(String.format("Failed to get status of CDMI object %s: %s", path, e), e);
			
		} // try{}
		
	
	} // getCurrentStatus()

} // end of ObjectStoreBackend class
