package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.FulFillmentVendor;

public interface FulfillmentDAO {

	public void createFulfillment(FulFillmentVendor fulFillmentVendor);

	public void updateFulfillment(FulFillmentVendor fulFillmentVendor);

	public FulFillmentVendor getFulfillmentById(long fulfillmentSEQID);

	public List<FulFillmentVendor> getAllFulfillments();

	public List<FulFillmentVendor> getFulfillmentByName(String fulFillmentName);

	public int chkDuplicateByID(String fulfillmentID);

	public int deleteFulfillment(long fulfillmentID);
	
	public int checkPackageIdMap(String fulfillmentID);

}
