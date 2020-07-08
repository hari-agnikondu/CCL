
package com.incomm.cclp.transaction.dao;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.transaction.bean.CardStatus;
import com.incomm.cclp.transaction.bean.RuleParameter;
import com.incomm.cclp.transaction.bean.RuleParameterMapping;
import com.incomm.cclp.transaction.bean.SpilResponseCode;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;
import com.incomm.cclp.transaction.bean.TransactionAuthDefinition;
import com.incomm.cclp.transaction.bean.TransactionInputValidation;
import com.incomm.cclp.transaction.bean.Transactions;

public interface TransactionDAO {

	public List<TransactionInputValidation> getTransactionInputValidations(String deliveryChannel);

	public List<SpilStartupMsgTypeBean> getSpilMessageType(String deliveryChannel);

	public List<TransactionAuthDefinition> getAllTransactionAuthDefinitions();

	public List<SpilResponseCode> getSpilResponseCodes(String deliveryChannel);

	public List<CardStatus> getAllCardStatus();

	List<RuleParameter> getAllRuleParameters();

	List<RuleParameterMapping> getAllRuleParametersMapping();

	public Map<String, String> getTxnLogDtls(Map<String, String> valueobj);

	public List<Transactions> getSpilTransactionCodes(String deliveryChannel);

	public String getTransactionLogRequestInfo(String rrn, String deliveryChannel, String messageType, String mdmId, String responseCode);

}
