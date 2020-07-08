/**
 * 
 */
package com.incomm.cclp.transaction.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.bean.SpilResponseCode;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;
import com.incomm.cclp.transaction.bean.TransactionAuthDefinition;
import com.incomm.cclp.transaction.bean.TransactionInputValidation;
import com.incomm.cclp.transaction.bean.Transactions;

public interface TransactionService {

	public Map<String, List<TransactionInputValidation>> loadTxnInputValidations(String deliveryChannel) throws ServiceException;

	public List<TransactionInputValidation> getInputValidations(Map<String, String> valueObj) throws ServiceException;

	public Map<String, List<TransactionAuthDefinition>> getAllTransactionAuthDefinitions();

	public Map<String, SpilResponseCode> getSpilResponseCodes(String deliveryChannel) throws ServiceException;

	public SpilResponseCode getSpilResponseCode(String internalRespCode);

	public SpilStartupMsgTypeBean getSpilMessageTypeBean(String spilMsgType) throws ServiceException;

	public Map<String, String> getAllCardStatus() throws ServiceException;

	Map<String, String> getAllRuleParameters() throws ServiceException;

	Map<String, String> getAllRuleParametersMapping() throws ServiceException;

	public void duplicateCheckProductBased(ValueDTO valueDto) throws ServiceException;

	public Map<String, Transactions> getSpilTransactionCodes(String deliveryChannel) throws ServiceException;
}
