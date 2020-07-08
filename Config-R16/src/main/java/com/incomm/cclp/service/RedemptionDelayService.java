package com.incomm.cclp.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.dto.RedemptionDelayDTO;
import com.incomm.cclp.exception.ServiceException;

public interface RedemptionDelayService {

	public void createRedemptionDelay(RedemptionDelayDTO redemptionDelayDTO) throws ServiceException;

	public String getOverlapRedemptionDetails(String previousValue, String currentValue);

	public List<RedemptionDelayDTO> getRedeemMerchantProductIddetails(Long productId, String merchantId);
	
	public Map<String, List<Object>> getRedeemMerchantsByProductId(Long productId);
	
	public Map<String, Map<String, Object>>  enableConfigInHazelCacheByProducId(Long productId);
	
	

}
