package com.incomm.scheduler.constants;

public class CCLPConstants {
	/**
	 * Constants class should not be instantiated.
	 */
	private CCLPConstants() {
		throw new IllegalStateException("Constants class");
	}

	public static final String ENTER = "ENTER";

	public static final String EXIT = "EXIT";

	public static final String USER_STATUS_APPROVED = "APPROVED";

	public static final String USER_STATUS_REJECTED = "REJECTED";

	public static final String USER_STATUS_NEW = "NEW";

	public static final String GROUP_NAME = "GroupName";

	public static final String LOCATION_NAME = "LocationName";

	public static final String INVENTORY_STATUS_NEW = "NEW";

	public static final String INVENTORY_STATUS_PAUSED = "PAUSED";

	public static final String INVENTORY_STATUS_STARTED = "STARTED";

	public static final String INVENTORY_STATUS_COMPLETED = "COMPLETED";
	
	public static final String INVENTORY_STATUS_FAILED="FAILED";

	public static final String INVENTORY_STATUS_CANCELLED = "CANCELLED";

	public static final String CARDNUMBER_INVENTORY_ACTION_GENERATE = "GENERATE";

	public static final String CARDNUMBER_INVENTORY_ACTION_RESUME = "RESUME";
	
	public static final String CARDNUMBER_INVENTORY_ACTION_REGENERATE = "REGENERATE";
	
	public static final String JOB_STATUS_COMPLETED = "COMPLETED";
	
	public static final String SUCCESS_RESPONSE_CODE = "000";
	
}
