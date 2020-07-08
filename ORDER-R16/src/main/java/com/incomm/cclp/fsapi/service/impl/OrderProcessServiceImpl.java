package com.incomm.cclp.fsapi.service.impl;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.OrderProcessDAO;
import com.incomm.cclp.fsapi.helper.FSAPIUtils;
import com.incomm.cclp.fsapi.helper.OrderValidator;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.OrderProcessService;


@Service
public class OrderProcessServiceImpl implements OrderProcessService {
	private final Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	OrderValidator orderValidator;
	
	@Autowired
	CommonService commonService;
	@Autowired
	OrderProcessDAO orderProcessDAO;
	@Autowired
	FSAPIUtils  utils;
	
	public void updateCardInfo(Map<String, Object> valObj) {
		try {
			String cardStatusRespCode = null;
			utils.setDefaultValues(valObj);
			utils.getCardInfoWithCard(valObj);
			utils.getDelchannelTranCode(valObj,  FSAPIConstants.ACTIVATION_API);
			utils.getRRN(valObj,  FSAPIConstants.FSAPI_BULKORDER_ACTIVATION_SEQ_NAME);
			utils.getyyyyMMdd(valObj, new java.sql.Date(new java.util.Date().getTime()));
			utils.gethhmmss(valObj, new java.util.Date());
			cardStatusRespCode = utils.validateCardStatusForFinancialTxn(valObj);
			logger.info("cardStatusRespCode :::::::::" + cardStatusRespCode);
			
			//check product validity
			if(!CCLPConstants.EXPIRED_PRODUCT_CARD_STATUS.equals(valObj.get(ValueObjectKeys.CARDSTATUS))
					&& cardStatusRespCode != null && B2BResponseCode.SUCCESS.equals(cardStatusRespCode)){
				utils.checkProductValidity(valObj);
				cardStatusRespCode = String.valueOf(valObj.get(ValueObjectKeys.FSAPIRESPONSECODE));
			}
			String errormsg="";
			String responseCode="";
			if(cardStatusRespCode != null && B2BResponseCode.SUCCESS.equals(cardStatusRespCode) ) 
			{
		    	if((String)valObj.get(ValueObjectKeys.CARDSTATUS)!= null && ("0".equals((String)valObj.get(ValueObjectKeys.CARDSTATUS)) || "INACTIVE".equals((String)valObj.get(ValueObjectKeys.CARDSTATUS)) )) 
		    	{
		    		utils.updateCardInfo(valObj);
		    		valObj.put(ValueObjectKeys.CARDSTATUS,FSAPIConstants.ACTIVE_CARDSTAT);
		    		errormsg=FSAPIConstants.SUCCESS_MSG;
		    		responseCode=FSAPIConstants.SUCCESS_RESPONSE1;
		    		utils.checkFundingOption(valObj);
		    		
		    	} else if((String)valObj.get(ValueObjectKeys.CARDSTATUS)!= null && "1".equals((String)valObj.get(ValueObjectKeys.CARDSTATUS))){
		    		errormsg=FSAPIConstants.CARD_ACTIVATION_ALREADY_DONE;
		    		responseCode=B2BResponseCode.ACTIVATION_ALEREDY_DONE;
		    	}else if((String)valObj.get(ValueObjectKeys.CARDSTATUS)!= null && !"0".equals((String)valObj.get(ValueObjectKeys.CARDSTATUS))){
		    		errormsg=FSAPIConstants.CARD_ACTIVATION_ALREADY_DONE;
		    		responseCode=B2BResponseCode.ACTIVATION_ALEREDY_DONE;
		    	}
			}else{
				valObj.put(ValueObjectKeys.FSAPIRESPONSECODE, cardStatusRespCode);
				errormsg=FSAPIConstants.INVALID_CARD_STATE;
	    		responseCode=B2BResponseCode.INVALID_CARD_STATE;
	    		logger.info("Valid Card Status Check Failed..." );
				
			}
			if(!valObj.containsKey(ValueObjectKeys.TRAN_LOG_UPDATE)){
				utils.logTxnDtls(valObj, errormsg, responseCode);
			}
		} catch (Exception e) {
			
			logger.error("Exception Occured while updating card status details..." + e);
		}

	}



}
