package com.incomm.cclp.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void convertMapToJsonTest() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("productId", 31);
		attributes.put("rate", 34);
		String result = "{\"productId\":31,\"rate\":34}";
		String responseMsg = Util.convertMapToJson(attributes);
		assertEquals(responseMsg, result);
	}

	@Test
	public void convertMapToJsonEmptyMapTest() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		String result = null;
		String responseMsg = Util.convertMapToJson(attributes);
		assertEquals(responseMsg, result);
	}

}
