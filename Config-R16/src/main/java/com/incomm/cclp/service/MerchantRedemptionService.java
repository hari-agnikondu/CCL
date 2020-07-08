package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.MerchantRedemptionDTO;
import com.incomm.cclp.exception.ServiceException;

public interface MerchantRedemptionService {
	public void createMerchant(MerchantRedemptionDTO merchantRedemptionDto) throws ServiceException;

	public List<MerchantRedemptionDTO> getAllMerchants();

	public List<MerchantRedemptionDTO> getMerchantsByName(String merchantName);
	
	

}
