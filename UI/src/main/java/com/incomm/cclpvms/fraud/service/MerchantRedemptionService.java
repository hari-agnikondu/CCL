package com.incomm.cclpvms.fraud.service;

import java.util.List;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.MerchantException;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.fraud.model.MerchantRedemptionDTO;

public interface MerchantRedemptionService {
	public ResponseDTO createRedemptionMerchant(MerchantRedemptionDTO merchantRedemptionDTO)  throws ServiceException;
	
	public List<MerchantRedemptionDTO> getAllRedemptionMerchants() throws ServiceException, MerchantException;
}
