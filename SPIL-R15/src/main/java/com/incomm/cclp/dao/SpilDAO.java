package com.incomm.cclp.dao;

import java.util.Map;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilDAO {

	public String[] transactionLogEntry(Map<String, String> valueObj, String respCode, String respMsg) throws ServiceException;

	public void updateUsageLimits(ValueDTO valueDto) throws ServiceException;

	void updateCardStatus(String cardNumHash, String oldCardStatus, String cardStatus) throws ServiceException;

	public void updateUsageFee(ValueDTO valueDto) throws ServiceException;

	public Map<String, String> getMonthlyFeeCapDetails(Map<String, String> valueObj, java.sql.Date calLastTxnDate);

	public void newInsertMonthlyFeeCap(Map<String, String> valueObj, java.sql.Date calLastTxnDate, Double feeCap) throws ServiceException;

	public void updateMonthlyFeeCap(Map<String, String> valueObj) throws ServiceException;

	public void updateExpiryDate(String expiryDate, String cardNumber);

	public String getPurseBalance(String accountId);

	public int getCountInStatementLog(String cardNumber);

	public int getCountInTransactiontLog(String cardNumber);

	public Map<String, String> getAcccountBalanceAndCurrency(Map<String, String> valueObj);

	public String getServerDate();
}
