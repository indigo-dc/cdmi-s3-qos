package org.indigo.cdmi.backend.radosgw;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class S3UtilsTest {

	private static final Logger log = LoggerFactory.getLogger(S3UtilsTest.class);
	
	//private static final String getBundleName = "getBundleName"; 
	
	
	
	/**
	 * 
	 */
	@Test
	public void testGetBundleNameFromPath() {
	
		//DummyBackendGateway dummyBackendGateway = new DummyBackendGateway();
		
		String computedBundleName = null;

		try {

			//computedBundleName = ReflectionTestUtils.invokeMethod(dummyBackendGateway, getBundleName, "/");
			computedBundleName = S3Utils.getBucketNameFromPath("/");
			fail("Expected IllegalArgumentException has been not thrown.");
			
		} catch (IllegalArgumentException ex) {
			
			log.info("Expected exception: {}", ex.getClass());
			
		}


		try {

			computedBundleName = S3Utils.getBucketNameFromPath("//");
			fail("Expected IllegalArgumentException has been not thrown.");
			
		} catch (IllegalArgumentException ex) {
			
			log.info("Expected exception: {}", ex.getClass());
			
		}


		try {

			computedBundleName = S3Utils.getBucketNameFromPath("///");
			fail("Expected IllegalArgumentException has been not thrown.");
			
		} catch (IllegalArgumentException ex) {
			
			log.info("Expected exception: {}", ex.getClass());
			
		}

		
		computedBundleName = S3Utils.getBucketNameFromPath("n");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");


		computedBundleName = S3Utils.getBucketNameFromPath("/n");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");

		computedBundleName = S3Utils.getBucketNameFromPath("//n");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");

		
		computedBundleName = S3Utils.getBucketNameFromPath("n/");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");

		computedBundleName = S3Utils.getBucketNameFromPath("n//");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");

		
		computedBundleName = S3Utils.getBucketNameFromPath("/n/");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");


		computedBundleName = S3Utils.getBucketNameFromPath("//n//");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");

		computedBundleName = S3Utils.getBucketNameFromPath("n/m");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");
		

		computedBundleName = S3Utils.getBucketNameFromPath("n/m/");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");


		computedBundleName = S3Utils.getBucketNameFromPath("n//m/");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");

		computedBundleName = S3Utils.getBucketNameFromPath("n//m");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");

		computedBundleName = S3Utils.getBucketNameFromPath("/n//m/");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");


		computedBundleName = S3Utils.getBucketNameFromPath("//n//m/");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");

		computedBundleName = S3Utils.getBucketNameFromPath("//n/m/");
		log.info("Expected bundle name: {}; computed bundle name: {}", "n", computedBundleName);
		assertEquals(computedBundleName, "n");

		
		computedBundleName = S3Utils.getBucketNameFromPath("bundle-name");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");
		
		computedBundleName = S3Utils.getBucketNameFromPath("/bundle-name");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");
		
		computedBundleName = S3Utils.getBucketNameFromPath("/bundle-name/");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");
		
		computedBundleName = S3Utils.getBucketNameFromPath("bundle-name/");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");
		
		computedBundleName = S3Utils.getBucketNameFromPath("bundle-name//");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");

		computedBundleName = S3Utils.getBucketNameFromPath("bundle-name///");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");
		
		computedBundleName = S3Utils.getBucketNameFromPath("//bundle-name");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");

		computedBundleName = S3Utils.getBucketNameFromPath("///bundle-name");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");

		computedBundleName = S3Utils.getBucketNameFromPath("///bundle-name////");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");

		computedBundleName = S3Utils.getBucketNameFromPath("/bundle-name/part1");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");

		computedBundleName = S3Utils.getBucketNameFromPath("/bundle-name/part1/");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");

		computedBundleName = S3Utils.getBucketNameFromPath("/bundle-name/part1/part2");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");

		computedBundleName = S3Utils.getBucketNameFromPath("/bundle-name/part1/part2/");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");

		computedBundleName = S3Utils.getBucketNameFromPath("bundle-name/part1/part2/");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");

		computedBundleName = S3Utils.getBucketNameFromPath("///bundle-name////part1/part2");
		log.info("Expected bundle name: {}; computed bundle name: {}", "bundle-name", computedBundleName);
		assertEquals(computedBundleName, "bundle-name");
		
		try {

			computedBundleName = S3Utils.getBucketNameFromPath("");
			fail("Expected IllegalArgumentException has been not thrown.");
			
		} catch (IllegalArgumentException ex) {
			
			log.info("Expected exception: {}", ex.getClass());
			
		}
		
	
		
	} // testGetBundleName()
	
	
	
} // end of DummyBackendGatewayTest class
