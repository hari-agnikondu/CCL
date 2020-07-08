package com.incomm.cclpvms.config.service;


import java.util.List;

import com.incomm.cclpvms.config.model.RedemptionDelayDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface RedemptionService {
	
	public ResponseDTO createRedemption(RedemptionDelayDTO redemptionDelaydto)  throws ServiceException;

	public  String getOverlapDelayDetails(String previousValue, String currentValue) throws ServiceException ;

	public List<RedemptionDelayDTO> getRedemptionDelayByMerchantIdProductId(Long productId, String merchantId) throws ServiceException;

}
