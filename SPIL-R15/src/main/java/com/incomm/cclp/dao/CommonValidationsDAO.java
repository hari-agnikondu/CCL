package com.incomm.cclp.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CommonValidationsDAO {

	String getCvvSupportedFlag(String msProdCode);

	String getCVK(String msProdCode);

	String expiryDateCheck(Map<String, String> valueObj);

	String getTransShortNameForTransCode(String string);

	String getProductAttributesForProductID(String string);

	String getPassiveCheckFlag(String channelCode);

	int updatePassiveStatus(String cardActiveStatus, String cardNumHash, String passiveCard);

	String getDeliveryChannelShortName(String channelCode);

	String getCardLimitAttributes(String cardNumHash, String productId);

	java.util.Date getLastTransactionDateFromCard(String cardNumHash);

	int updateCardLimitAttributs(String attributesJsonResp, String cardNumHash, String productID);

	public int updateCardUsageLimits(String cardAttributesMap, String cardNumHash, String productId);

	public List<String> getCurrencyCodeByProductId(String productId);

	public String getCardAttributes(String profileId);

	List<Map<String, Object>> getproductfunding(String pan);

	public String redemptionDelayCheck(String formRedemptionDelayQuery) throws SQLException;

	public int insertDelayedLoad(String accountId, String deliveryChannel, String txnCode, String rrn, String txnAmount,
			String delayAmount);

	public int updateRedemptionDelayFlag(String accountId);

	public int updateDelayedLoadReversal(String accountId, String deliveryChannel, String txnCode, String rrn, String txnAmount);

	public int getDelayedAmount(String accountId);

	public int updateDelayedAmount(String accountId);

}
