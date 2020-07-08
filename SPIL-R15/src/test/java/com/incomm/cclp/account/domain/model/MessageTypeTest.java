package com.incomm.cclp.account.domain.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MessageTypeTest {

	@Test
	public void testMessageTypeResolution() {
		assertTrue(MessageType.byMessageTypeCode("0200")
			.isPresent());
		assertTrue(MessageType.byMessageTypeCode("0400")
			.isPresent());

		assertFalse(MessageType.byMessageTypeCode("INVALID")
			.isPresent());
	}

}
