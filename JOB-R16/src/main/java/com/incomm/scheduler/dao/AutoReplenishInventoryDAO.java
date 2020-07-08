package com.incomm.scheduler.dao;

import java.util.List;

import com.incomm.scheduler.model.AutoReplenishmentInventory;

public interface AutoReplenishInventoryDAO {

	String intiateBatchAutoReplenishInventory();

	public List<AutoReplenishmentInventory> autoReplenishCheck();

	public List getProductDetails(String productID);
	
	
}
