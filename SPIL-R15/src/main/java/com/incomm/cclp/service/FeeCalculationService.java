package com.incomm.cclp.service;

import java.util.Map;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface FeeCalculationService {

	public void feeCalculation(ValueDTO valueDto) throws ServiceException;

	public void calculateFee(Map<String, String> valueObj, Map<String, Object> prodDelchnlTxnFeeMap, String delchnlTxn)
			throws ServiceException;

	public void calcMonthlyFeeCap(Map<String, String> valueObj, Map<String, Object> monthlyFeeCap, String delchnlTxn, ValueDTO valueDto,
			Map<String, Object> productDelchnlTxnFeeMap) throws ServiceException;
}
