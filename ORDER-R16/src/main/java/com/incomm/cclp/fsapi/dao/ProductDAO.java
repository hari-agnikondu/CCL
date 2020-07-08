package com.incomm.cclp.fsapi.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.incomm.cclp.exception.ServiceException;

public interface ProductDAO {
	
	String getProductAttributesByProductId(String productId);

	int checkProductByProductId(String productId);

	int checkProductPackageId(String productId, String packageId);

	int checkPartnerId(String productId, String partnerId);

	List<String> checkFulFillmentVendor(Set<String> packageIdList);
	
	List<Map<String, Object>> checkFulFillmentVendorWithkeypair(Set<String> packageIdList);

	List<String> checkVirtualProduct(Set<String> productIdSet);


	
	String getChannels();

	List<Map<String, Object>> getPackageAttributes(String packageId);

	String getBusinessDate(String productid) throws ServiceException;

	int checkProductvalidity(String productId);

	void updateCardStatus(String valueOf, String expiredProductCardStatus);

}
