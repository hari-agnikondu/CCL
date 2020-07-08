package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.RedemptionDelay;


public interface RedemptionDelayDAO {

	public void createRedemptionDelay(RedemptionDelay redemptionDelay) ;


	public List<Object[]> getMerchantProductById(Long productId, String merchantId);

	public String getOverLapDetails(String previousValue, String currentValue);

	void deleteRedemptionDelay(Long productId, String merchantId);
	
	
	public List<Object> getMerchantProductByIdData(Long productId, String merchantId);
	
	public List<Object[]> getMerchantsbyProductId(Long productId);


}
