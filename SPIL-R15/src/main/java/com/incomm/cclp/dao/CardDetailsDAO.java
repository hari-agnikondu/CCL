package com.incomm.cclp.dao;

import java.time.LocalDateTime;
import java.util.Map;

import com.incomm.cclp.account.domain.model.SpNumberType;
import com.incomm.cclp.domain.CardDetail;

public interface CardDetailsDAO {

	public Map<String, Object> getCardDetailsBySPNumber(String spNumber);

	public Map<String, Object> getCardDetailsByCustomerPref(String spNumber, String targetCardNumber);

	public String getCardNoByProductId(String string);

	public String getCardNoByRefNum(String rrn, String txnCode, String txnDate);

	public void updateCardStatus(String spNumber, String cardStatus);

	public Map<String, Object> getAccountPurseUsageDetails(String accountId, String purseId);

	public CardDetail getCardDetails(SpNumberType spNumberType, String spNumber);

	public String getCardNumberByCustId(String customerId);

	public Map<String, Object> getCardNumberFromBalTransferDetails(String rrn, String targetCardNumberInRequest);

	public int updateCard(String cardNumberHash, String newCardStatus, String oldCardStatus, LocalDateTime activationDate,
			Boolean firstTimeTopup);

}
