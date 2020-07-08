package com.incomm.scheduler.dao;

import java.util.List;
import java.util.Map;

public interface B2BREPLCCFGenerationDAO {
	
	public	List<String> getAllOrdersToGenerateCCF(String orderType);

	public List<Map<String, Object>> getAllOrdersToGenerateCCF(String orderId, String vendorId);

	public List<Object> getCcfVersionDetails(String version,String recType);
	
	public String getCCFFormatVersion(String productId);

	public String getServiceCode(String msProdCode);

	public void updateCardStatus(String hashPancode, String psFileName, String status);

	public void updateCardStatus(String orderId, String orderStatus);

	public List<Object> getAddressDetails(String hashPancode);
	
	public String getCVK(String msProdCode);

	public String getCvvSupportedFlag(String productId);

	public List<String> getAllVendorsLinkedToOrder(String orderId);

	public String getVendorNameForId(String vendorId);

	public Map<String, Object> getCCFFileName(String vendorId, String order);

	public Map<String, Object> getHeaderFileNumber(int insUser);

	public List<Map<String, Object>> cnFileList();
	
	public void updateOrderStatus(String orderId, String orderStatus,String orderLineItem);
	
	public List<String> getAllVendorsLinkedToOrder(String orderId,String orderLineItem);
	public Map<String, Object> getCCFFileName(String vendorId, String order,String orderLineItem);
	public List<Map<String, Object>> getAllOrdersToGenerateCCF(String orderId, String vendorId,String orderLineItem);

	public List<String> getAllB2BReplOrdersToGenerateCCF(String orderType);

	public List<String> getAllReplacementVendorsLinkedToOrder(String orderId, String orderLineItem);
	
	public String getReplacementCCFFormatVersion(String productId);
	public void updateReplacementCard(String hashPancode, String printersent);

}
