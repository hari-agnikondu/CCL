package com.incomm.cclp.fsapi.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.FsApiTransaction;
import com.incomm.cclp.fsapi.constants.APIConstants;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.dao.ProductDAO;
import com.incomm.cclp.fsapi.helper.APIHelper;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.TransactionService;

@Service
public class CommonServiceImpl implements CommonService{
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	ProductDAO productDao;

	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Override
	public void getBusinessTime(Map<String, Object> tempValueMap) {
		logger.debug("--------ENTER getBusinessTime---------");
		final SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
		final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
		final SimpleDateFormat formatter2 = new SimpleDateFormat("HHmmss");
		final SimpleDateFormat formatter3 = new SimpleDateFormat("z");
		final SimpleDateFormat formatter4 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
		    final Date date = formatter.parse(String.valueOf(tempValueMap.get(ValueObjectKeys.BUSINESS_DATE_TIME)));
		    tempValueMap.put(ValueObjectKeys.BUSINESS_DATE, formatter1.format(date));
		    tempValueMap.put(ValueObjectKeys.BUSINESS_TIME, formatter2.format(date));
		    tempValueMap.put(ValueObjectKeys.BUSINESSTIMEZONE, formatter3.format(date));
		    tempValueMap.put(ValueObjectKeys.DATE_TIME, formatter4.format(date));
		} catch (ParseException e) {
		    final Date tempDate = new Date();
		    tempValueMap.put(ValueObjectKeys.BUSINESS_DATE, formatter1.format(tempDate));
		    tempValueMap.put(ValueObjectKeys.BUSINESS_TIME, formatter2.format(tempDate));
		    tempValueMap.put(ValueObjectKeys.BUSINESSTIMEZONE, formatter3.format(tempDate));
		    tempValueMap.put(ValueObjectKeys.DATE_TIME, formatter4.format(tempDate));
		}
		logger.debug("--------EXIT getBusinessTime---------");

	}

	@Override
	public void getDelchannelTranCode(Map<String, Object> valuHashMap, String apiName, String reqType)
			throws ServiceException {

		logger.info("--------ENTER getDelchannelTranCode---------");
		try {
			String actionVal = String.valueOf(valuHashMap.get(APIConstants.ACTION));

			logger.debug("DELIVERY actionVal   : " + actionVal);
			if (!APIHelper.emptyCheck(actionVal)) {
				actionVal = "";
			} else {
				actionVal = actionVal.toUpperCase();
				valuHashMap.replace(APIConstants.ACTION, actionVal);
			}

			logger.debug("DELIVERY CHANNEL DESC : " + valuHashMap.get(APIConstants.API_CHANNEL));
			logger.debug("API Name : " + apiName);

			String channl = String.valueOf(valuHashMap.get(APIConstants.API_CHANNEL));

			logger.debug("Channel name : " + channl);
			logger.debug("Channel name(HOST) : " + channl);

			final String key = channl + ":" + apiName + ":" + reqType + ":" + actionVal;
			FsApiTransaction fsapiTran = transactionService.getFsapiTransactionDetailByApiKey(key);
			if (!Objects.isNull(fsapiTran)) {
				valuHashMap.put(ValueObjectKeys.CHANNEL_CODE, fsapiTran.getChannelCode());
				valuHashMap.put(ValueObjectKeys.MSGTYPE, fsapiTran.getMsgType());
				valuHashMap.put(ValueObjectKeys.TRANS_CODE, fsapiTran.getTranCode());
				valuHashMap.put(ValueObjectKeys.DAO_CLASSNAME, fsapiTran.getDaoCName());
				valuHashMap.put(ValueObjectKeys.VALIDATION_CLASSNAME, fsapiTran.getValidationName());
				valuHashMap.put(ValueObjectKeys.VERIFICATION_CLASSNAME, fsapiTran.getVerifyCName());
				if (APIConstants.N.equalsIgnoreCase(fsapiTran.getReversalTransaction())) {
					valuHashMap.put(ValueObjectKeys.REVERSAL_CODE, APIConstants.NORMAL_TXN_RVSLCODE);
					valuHashMap.put(ValueObjectKeys.TXN_MODE, "0");
				} else {
					valuHashMap.put(ValueObjectKeys.REVERSAL_CODE, APIConstants.REVERSAL_TXN_RVSLCODE);
					valuHashMap.put(ValueObjectKeys.TXN_MODE, "1");
					valuHashMap.put(ValueObjectKeys.APIREVERSALFLAG, APIConstants.Y);

				}

				valuHashMap.put(ValueObjectKeys.LOG_EXEMPTION, fsapiTran.getLogExemption());
				valuHashMap.put(ValueObjectKeys.IS_FINANCIAL, fsapiTran.getIsFinancial());
				valuHashMap.put(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, fsapiTran.getCreditDebitIndicator());
				valuHashMap.put(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME, fsapiTran.getChannelShortName());
				valuHashMap.put(ValueObjectKeys.PASSIVE_SUPPORT_FLAG, fsapiTran.getPassiveSupported());
				valuHashMap.put(ValueObjectKeys.TRANSACTIONDESC, fsapiTran.getTransactionDesc());
				valuHashMap.put(ValueObjectKeys.REVERSAL_FLAG, fsapiTran.getReversalTransaction());
				valuHashMap.put(ValueObjectKeys.DELIVERYCHNL, fsapiTran.getChannelDescritption());
				valuHashMap.put(ValueObjectKeys.MEMBERNO, "000");
				valuHashMap.put(ValueObjectKeys.ORIGINAL_MSGTYPE, fsapiTran.getTransactionShortName());

				logger.info("DELIVERY CHANNEL AND TRAN CODE DESC : " + fsapiTran.getChannelDescritption() + " : "
						+ fsapiTran.getTranCode());
			} else {
				throw new ServiceException(B2BResponseMessage.API_DELIVERYCHNL_NOTFOUND,
						B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			}
		} catch (ServiceException ex) {
			logger.error("Exception", ex);
			throw ex;
		} catch (Exception ex) {
			logger.error("Exception", ex);
			throw new ServiceException(B2BResponseMessage.API_DELIVERYCHNL_NOTFOUND, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}

		logger.info("--------EXIT getDelchannelTranCode---------");
	}
	
@Override
public boolean checkProductValidity(String productId) {
		
		int count = productDao.checkProductvalidity(productId);
		if(count == 1)
			return true;
		else
			return false;
	}

}
