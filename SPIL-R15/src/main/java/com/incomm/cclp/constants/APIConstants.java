package com.incomm.cclp.constants;

public class APIConstants {

	/**
	 * Constants class should not be instantiated.
	 */
	private APIConstants() {
		throw new IllegalStateException("APIConstants class");
	}

	public static final String SERIAL_NUMBER = "SerialNumber";
	public static final String PRODUCT_FORM_FACTOR_DIGITAL = "Digital";
	public static final String PACKAGE_ID = "packageID";

	public static final String API_HEADER_CORRELATION_ID = "x-incfs-correlationid";
	public static final String API_HEADER_USERNAME = "x-incfs-username";
	public static final String API_HEADER_DELIVERY_CHANNEL = "x-incfs-channel";

}
