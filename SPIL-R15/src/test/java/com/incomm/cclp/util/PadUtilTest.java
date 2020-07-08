package com.incomm.cclp.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PadUtilTest {
		
	@Test
	public void padRight_withEmptyInput() {
		String strInput = null;
		int length = 4;
		char paddingVal = '*';
		String expected = "****";
		
		String response = PadUtil.padRight(strInput, length, paddingVal);
		
		assertEquals(expected, response);
	}
	
	@Test
	public void padRight_withLengthGreaterThanInputLength() {
		String strInput = "12345678";
		int length = 10;
		char paddingVal = '*';
		String expected = "12345678**";
		
		String response = PadUtil.padRight(strInput, length, paddingVal);
		
		assertEquals(expected, response);
	}
	
	@Test
	public void padRight_withLengthEqualsToInputLength() {
		String strInput = "12345678";
		int length = 8;
		char paddingVal = '*';
		String expected = "12345678";
		
		String response = PadUtil.padRight(strInput, length, paddingVal);
		
		assertEquals(expected, response);
	}
	
	@Test
	public void padRight_withLengthLessThanInputLength() {
		String strInput = "12345678";
		int length = 5;
		char paddingVal = '*';
		String expected = "12345678";
		
		String response = PadUtil.padRight(strInput, length, paddingVal);
		
		assertEquals(expected, response);
	}

}
