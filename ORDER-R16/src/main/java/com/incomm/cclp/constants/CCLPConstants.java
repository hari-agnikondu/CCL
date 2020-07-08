package com.incomm.cclp.constants;

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
	
	public static final String GROUP_NAME="GroupName";
	
	
	
	//POST-BACK-JOB
	
	
			public static final String  ORDER_ORDPOST_BACK_URL = "postBackURL";
			public static final String  ORDER_ID	= "orderId";
			public static final String  API_NAME	="apiName" ;
			public static final String  SEQ_NO="seqNo";
			public static final String  REQ_MESSAGE="reqMsg";
			public static final String  REQ_HEADER="reqHeader";
			public static final String  RES_HEADER="resHeader";
			public static final String  RES_CODE="responseCode";
			public static final String  RES_MSG="responseMsg";
			public static final String  RES_COUNT="responseCount";
			public static final String  RES_FLAG="responseFlag";
			public static final String  POST_BACK_STATUS="postBackStatus";
			public static final String  POST_BACK_URL="postBackUrl";
			public static final String  PARTNER_ID="partnerId";
			public static final String ORDER_STATUS ="orderStatus";
			public static final String SHIPPING_METHOD ="shippingMethod";
			public static final String LINE_ITEM_ID="lineItemId";
			public static final String LINE_ITEM_ORDER_STATUS="lineItemOrderStatus";
			public static final String RETURN_FILE_MSG="returnFileMsg";
			public static final String REJECT_CODE="rejectCode";
			public static final String REJECT_REASON="rejectReason";
			
			public static final String PROXY_NUMBER="proxyNumber";
			public static final String SERIAL_NUMBER="serialNumber";
			public static final String CARD_STATUS="cardStatus";
			public static final String PIN="pin";
			public static final String PROXY_PIN_ENCR="proxyPinEncr";
			public static final String TRACKING_NBR="trackingNumber";
			public static final String SHIPPING_DATE="shippingDate";
			public static final String CUSTOMER_CODE="customerCode";
			public static final String CARDNUMBER="cardNumber";
			public static final String CARD_NUM_HASH="cardNumHash";
			public static final String EXPIRY_DATE="expirationDate";
			public static final String CARD_NUM_ENCR="cardNumEncr";
			public static final String PRINTER_RESPONSE="printerResponse";
			public static final String RESP_CODE="respCode";
			
			public static final String ORDER_LINE_ITEMDTLS = "lineItem";
			
			public static final String CARDS = "cards";
			public static final String LINE_ITEM = "lineItem";

			public static final String ORDER = "order";

			public static final String POSTBACK_APINAME = "POSTBACK_APINAME";
			public static final String POSTBACK_JOB_ID = "PostBackJob";
			
			public static final String CCF_ORDER_LINE_ITEM_ID = "LINE_ITEM_ID";
			public static final String PACKAGE_ID = "package_id";

			public static final String POSTBACK_STATUS="POSTBACK_STATUS";

			public static final String X_INCFS_DATE = "x-incfs-date";

			public static final String X_INCFS_IP = "x-incfs-ip";

			public static final String CARD_NUM_INVENTORY_GEN_ID = "CardNumberInventoryGenJob";

			public static final String CARD_NUM_INVENTORY_INITIAL_TASKLET = "initialStepCardInventory";

			public static final String CARD_NUM_INVENTORY_FINAL_TASKLET = "finalStepCardInventory";

			public static final String AUTO_REPL_INVENTORY_ID = "AutoReplenishmentInventoryGenJob";

			public static final String ORDER_LESS_NO_OF_CARDS_FOR_THIS_LOCATION_ID ="Order Less No Of Cards For This Location ID";
			
			public static final String AUTO_REPLENISHMENT_OF_INVENTORY_GEN_ID = "AutoReplenishmentInventoryGenJob";

			public static final String AUTO_REPLENISH_INVENTORY_INITIAL_TASKLET = "initialStepAutoReplenishmentInventory";

			public static final String AUTO_REPLENISH_INVENTORY_FINAL_TASKLET = "finalStepAutoReplenishmentInventory";

			public static final String ORDER_CONNECTION_ERROR ="Unable to Connect to CCLP-Order Service";
					
				public static final String CCF_B2B_REPL_GENERATION_JOB_ID = "B2BREPLCCFGenerationJob";
			
			
			//B2B REPLACEMENT
			
			public static final String CCF_B2B_REP_INITIAL_TASKLET = "initialStepB2BREPCCF";
			public static final String CCF_B2B_REP_FINAL_TASKLET = "finalStepB2BREPCCF";

			public static final String CCF_REP_ADDRESS_ONE = "shipToAddress1";

			public static final String CCF_REP_ADDRESS_TWO = "shipToAddress2";

			public static final String CCF_REP_ORDER_STATUS_INPROGRESS = "CCF-REPL-IN-PROGRESS";

			public static final String CCF_REPL_ORDER_STATUS_CCF_GENERATED = "CCF-REPL-GENERATED";
			public static final String SHIP_TO_ADDRESS1 = "SHIPTOADDRESS1";
			public static final String SHIP_TO_ADDRESS2 = "SHIPTOADDRESS2";
			
			
			
			public static final String POST_BACK_RESP_MSG="postbackResp";
			
			public static final String EXPIRED_PRODUCT_CARD_STATUS = "20";
	
	
}
