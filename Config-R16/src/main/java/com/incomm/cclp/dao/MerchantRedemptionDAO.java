package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.MerchantProductRedemptionId;
import com.incomm.cclp.domain.MerchantRedemption;
import com.incomm.cclp.domain.RedemptionDelay;

public interface MerchantRedemptionDAO {
	
	public void createMerchant(MerchantRedemption merchant) ;

	public List<MerchantRedemption> getAllMerchants();

	public List<MerchantRedemption> getMerchantsByName(String merchantName);

	public MerchantRedemption getMerchantById(String merchantId);

	public RedemptionDelay getMerchantProductById(MerchantProductRedemptionId merchantProductRedemptionId);

	

}
