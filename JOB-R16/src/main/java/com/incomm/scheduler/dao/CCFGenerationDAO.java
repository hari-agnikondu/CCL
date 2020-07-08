package com.incomm.scheduler.dao;

import java.util.List;
import java.util.Map;

public interface CCFGenerationDAO {
	
	List<String> getAllOrdersToGenerateCCF(String orderType);

	void getAllOrdersToGenerateCCF(String orderId, String vendorId,CCFRowCallbackHandler handler);
	
	Map<String,Object> getCountAllOrdersToGenerateCCF(String orderId, String vendorId);
	
	Map<String,Object> getOrderInfo(String orderId, String vendorId);

	List<Object> getCcfVersionDetails(String version,String recType);
	
	public String getCCFFormatVersion(String productId);

	String getServiceCode(String msProdCode);

	void updateCardStatus(String hashPancode, String psFileName, String status);

	void updateCardStatus(String orderId, String psFileName, String orderStatus, String cardStatus);
	
	void updateCardStatus(String orderId, String orderStatus);

	List<Object> getAddressDetails(String hashPancode);

	
	String getCVK(String msProdCode);

	String getCvvSupportedFlag(String productId);

	List<String> getAllVendorsLinkedToOrder(String orderId);

	String getVendorNameForId(String vendorId);

	Map<String, Object> getCCFFileName(String vendorId, String order);

	Map<String, Object> getHeaderFileNumber(int insUser);

	List<Map<String, Object>> cnFileList();
	
	public void updateOrderStatus(String orderId, String orderStatus,String orderLineItem);
	
	List<String> getAllVendorsLinkedToOrder(String orderId,String orderLineItem);
	Map<String, Object> getCCFFileName(String vendorId, String order,String orderLineItem);
	List<Map<String, Object>> getAllOrdersToGenerateCCF(String orderId, String vendorId,String orderLineItem);

	void updateCard(String hashPancode, String printersent);

	List<Map<String, Object>> getCCFFilesToDelete(String ccfFileDelGap);

	String getProductNameByOrderId(String order);

	String getUserId(String order);

	

	List<Map<String, Object>> getAllOrdersToGenerateCCFF(String vendorId,String orderType);

	List<String> getAllOrdersList(String orderType);

	List<String> getAllOrdersToGenerateSeparateCCF(String orderType);

	List<Map<String, Object>> getAllOrdersToGenerateSeparateCFF(String vendorId, String orderType, String orderId);
	
	List<Map<String, Object>> getGlobalParams();

	List<String> getAllVendorsLinkedToOrderPackageId(String order, String packageId);

	public String getCCFFormatVersionByOrderId(String orderId);
	
	
}
