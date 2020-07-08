package com.incomm.cclp.fsapi.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface OrderProcessDAO {

	void saveOrders(Map<String, Object> valuMap) throws SQLException;

	void processOrder(Map<String, Object> valueHashMap);

	void b2bCheckOrderStatus(Map<String, Object> valueHashMap);

	List<Map<String, String>> getLineItemDtls(String key, Map<String, Object> valueHashMap, List<Map<String, String>> tempList,
			String apiName);

	void responsePostBackLogger(String order, String orderId, String reqHeaders, String reqStr, String respCode,
			String respMsg, String respStr, String serverID, long timeTaken, String postBackUrl);

	void reqRespLogger(String apiName, String reqHeaders, String reqStr, String respCode, String respMsg, String respStr,
			String serverID, long timeTaken);

	Long checkPanCount(String key);

	int checkSerialCount(String key, Long value);
	
	public int updatePostBackUrl(String name,String url);
	
	public int deletePostBackUrl();
	public void getLineItemOrderStatus(Map<String, Object> valueHashMap);
	
	int updateMailAlertDetails(String smtpHostAddress, String smtpHostPropKey);

	int deleteMailAlertDetails();

	int getReplacedCardOldStatus(String serialNumber);

}
