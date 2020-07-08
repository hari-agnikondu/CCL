package com.incomm.cclp.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.LoggerConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.CommonValidationsDAO;

@Repository
public class CommonValidationsDAOImpl extends JdbcDaoSupport implements CommonValidationsDAO {

	@Autowired
	DataSource dataSource;

	private static final Logger logger = LogManager.getLogger(BalanceTransferDAOImpl.class);

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Override
	public String getCvvSupportedFlag(String msProdCode) {

		logger.debug(LoggerConstants.ENTER);
		String cvvSupported = null;
		try {
			logger.info("CVV supported flag query {}, values(productId: {})", QueryConstants.GET_CVV_SUPPORTED_FLAG, msProdCode);
			cvvSupported = getJdbcTemplate().queryForObject(QueryConstants.GET_CVV_SUPPORTED_FLAG, new Object[] { msProdCode },
					String.class);
		} catch (Exception e) {
			return cvvSupported;
		}
		logger.info("CVV supported flag query response: {}", cvvSupported);
		logger.debug(LoggerConstants.EXIT);
		return cvvSupported;
	}

	public String getTransShortNameForTransCode(String tranCode) {

		logger.debug(LoggerConstants.ENTER);
		String txnShortName = null;

		logger.info("get txn short name query: {}, parameters( tranCode: {})", QueryConstants.GET_TRANSACTION_SHORT_NAME, tranCode);
		txnShortName = (getJdbcTemplate().queryForObject(QueryConstants.GET_TRANSACTION_SHORT_NAME, new Object[] { tranCode },
				String.class));
		logger.info("get txn short name response: {})", txnShortName);
		logger.debug(LoggerConstants.EXIT);
		return txnShortName;

	}

	@Override
	public String getProductAttributesForProductID(String productId) {
		logger.debug(LoggerConstants.ENTER);
		String productAttributes = null;
		logger.info("get product attributes Query: {}", productId);
		productAttributes = getJdbcTemplate().queryForObject(QueryConstants.GET_PRODUCT_ATTRIBUTES, new Object[] { productId },
				String.class);
		logger.info("sp number check response: {}", productId);
		logger.debug(LoggerConstants.EXIT);
		return productAttributes;

	}

	@Override
	public String getCVK(String productId) {
		logger.debug(LoggerConstants.ENTER);
		String cvk = null;
		logger.info("get cvk Query: {}, parameter (productId: {})", QueryConstants.GET_CVK, productId);
		cvk = getJdbcTemplate().queryForObject(QueryConstants.GET_CVK, new Object[] { productId }, String.class);
		logger.info("get cvk response: {}", cvk);
		logger.debug(LoggerConstants.EXIT);
		return cvk;
	}

	@Override
	public String expiryDateCheck(Map<String, String> valueObj) {
		logger.debug(LoggerConstants.ENTER);
		String expDate = null;

		try {
			logger.info("expiry date check  Query: {}, parameters(cardNumber:{})", QueryConstants.GET_EXPIRY_DATE,
					valueObj.get(ValueObjectKeys.CARD_NUM_HASH));
			expDate = getJdbcTemplate().queryForObject(QueryConstants.GET_EXPIRY_DATE,
					new Object[] { valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID), valueObj.get(ValueObjectKeys.CARD_NUM_HASH) },
					String.class);
		} catch (Exception e) {
			logger.debug(LoggerConstants.EXIT);
			return expDate;
		}
		logger.info("expiry date check response: {}", expDate);
		logger.debug(LoggerConstants.EXIT);
		return expDate;
	}

	@Override
	public String getPassiveCheckFlag(String channelCode) {
		logger.debug(LoggerConstants.ENTER);
		String passiveFlag = null;
		logger.info("get passive check flag Query: {}, parameter(channelCode:{})", QueryConstants.GET_PASSIVE_CHECK_FLAG, channelCode);
		passiveFlag = getJdbcTemplate().queryForObject(QueryConstants.GET_PASSIVE_CHECK_FLAG, new Object[] { channelCode }, String.class);
		logger.info("get passive check flag response: {}", passiveFlag);
		logger.debug(LoggerConstants.EXIT);

		return passiveFlag;
	}

	@Override
	public int updatePassiveStatus(String cardActiveStatus, String cardNumHash, String passiveCard) {
		logger.debug(LoggerConstants.ENTER);
		int passiveUpdate;
		logger.info("update passive status Query: {}, parameter(cardActiveStatus:{},cardNo:{},passiveCard:{})",
				QueryConstants.UPDATE_PASSIVE_STATUS, cardActiveStatus, cardNumHash, passiveCard);
		passiveUpdate = getJdbcTemplate().update(QueryConstants.UPDATE_PASSIVE_STATUS, cardActiveStatus, cardNumHash, passiveCard);
		logger.info("update passive response: {}", passiveUpdate);
		logger.debug(LoggerConstants.EXIT);
		return passiveUpdate;
	}

	@Override
	public List<String> getCurrencyCodeByProductId(String productId) {
		logger.debug(LoggerConstants.ENTER);
		List<String> currencyCodes;
		logger.info("get currency codes Query: {},parameters(productId:{})", QueryConstants.CHECK_CURRECNY_CODE, productId);
		currencyCodes = getJdbcTemplate().queryForList(QueryConstants.CHECK_CURRECNY_CODE, String.class, productId);
		logger.info("get product attributes Query: {}", productId);
		logger.debug(LoggerConstants.EXIT);
		return currencyCodes;

	}

	@Override
	public String getDeliveryChannelShortName(String channelCode) {
		logger.debug(LoggerConstants.ENTER);
		String delChannelShortName = null;
		logger.info("get delivery channel short name Query: {}", QueryConstants.GET_DEL_CHANNEL_SHORTNAME, channelCode);
		delChannelShortName = getJdbcTemplate().queryForObject(QueryConstants.GET_DEL_CHANNEL_SHORTNAME, new Object[] { channelCode },
				String.class);
		logger.info("get delivery channel short name response: {}", delChannelShortName);
		logger.debug(LoggerConstants.EXIT);
		return delChannelShortName;
	}

	@Override
	public String getCardLimitAttributes(String cardNumHash, String productId) {
		logger.debug(LoggerConstants.ENTER);
		String limitAttributes = null;
		logger.info("get card limits Query: {}, parameters(cardNo:{},productId:{})", QueryConstants.GET_CARD_LIMIT_ATTRIBUTES, cardNumHash,
				productId);
		limitAttributes = getJdbcTemplate().queryForObject(QueryConstants.GET_CARD_LIMIT_ATTRIBUTES,
				new Object[] { productId, cardNumHash }, String.class);
		logger.info("get card limits response: {}", limitAttributes);
		logger.debug(LoggerConstants.EXIT);
		return limitAttributes;

	}

	@Override
	public java.util.Date getLastTransactionDateFromCard(String cardNumHash) {
		logger.debug(LoggerConstants.ENTER);
		java.util.Date lastTxnDate;
		logger.info("get last txn date Query: {},parameter(cardNo:{})", QueryConstants.GET_LAST_TRANSACTION_DATE, cardNumHash);
		lastTxnDate = getJdbcTemplate().queryForObject(QueryConstants.GET_LAST_TRANSACTION_DATE, new Object[] { cardNumHash },
				java.util.Date.class);
		logger.info("get last txn date response: {}", lastTxnDate);
		logger.debug(LoggerConstants.EXIT);
		return lastTxnDate;
	}

	@Override
	public int updateCardLimitAttributs(String attributesJsonResp, String cardNumHash, String productID) {
		logger.debug(LoggerConstants.ENTER);
		int updateRes;
		logger.info("update card limits Query: {},parameters(attributes:{},cardNo:{},product:{})",
				QueryConstants.UPDATE_CARD_LIMIT_ATTRIBUTES, attributesJsonResp, cardNumHash, productID);
		updateRes = getJdbcTemplate().update(QueryConstants.UPDATE_CARD_LIMIT_ATTRIBUTES, attributesJsonResp, cardNumHash, productID);
		logger.info("update limit response: {}", updateRes);
		logger.debug(LoggerConstants.EXIT);
		return updateRes;
	}

	@Override
	public int updateCardUsageLimits(String cardAttributesMap, String cardNumHash, String productId) {
		logger.debug(LoggerConstants.ENTER);
		int usageLimit;
		logger.info("update usage Limits Query:{}, parameters(attributes:{},cardNo:{},productId:{})",
				QueryConstants.UPDATE_CARD_LIMIT_ATTRIBUTES, cardAttributesMap, cardNumHash, productId);
		usageLimit = getJdbcTemplate().update(QueryConstants.UPDATE_CARD_LIMIT_ATTRIBUTES, cardAttributesMap, cardNumHash, productId);
		logger.info("update usage limits response: {}", usageLimit);
		logger.debug(LoggerConstants.EXIT);
		return usageLimit;
	}

	@Override
	public String getCardAttributes(String profileId) {
		logger.debug(LoggerConstants.ENTER);
		String cardAttributes = null;
		logger.info("get card atrributes Query: {},parameters(profileId:{})", QueryConstants.GET_CARD_ATTRIBUTES, profileId);
		cardAttributes = getJdbcTemplate().queryForObject(QueryConstants.GET_CARD_ATTRIBUTES, new Object[] { profileId }, String.class);
		logger.info("get card attributes response: {}", cardAttributes);
		logger.debug(LoggerConstants.EXIT);
		return cardAttributes;
	}

	@Override
	public List<Map<String, Object>> getproductfunding(String pan) {
		logger.debug(LoggerConstants.ENTER);
		return getJdbcTemplate().queryForList(QueryConstants.GET_PRODUCT_FUNDING, pan);

	}

	@Override
	public int updateRedemptionDelayFlag(String accountId) {

		return getJdbcTemplate().update(QueryConstants.UPDATE_REDEMPTION_DELAY_FLAG, accountId);
	}

	@Override
	public int insertDelayedLoad(String accountId, String deliveryChannel, String txnCode, String rrn, String txnAmount,
			String delayAmount) {

		return getJdbcTemplate().update(QueryConstants.INSERT_DELAY_LOAD, accountId, deliveryChannel, txnCode, rrn, txnAmount, delayAmount);

	}

	public int updateDelayedLoadReversal(String accountId, String deliveryChannel, String txnCode, String rrn, String txnAmount) {

		return getJdbcTemplate().update(QueryConstants.UPDATE_DELAY_LOAD_REVERSAL, accountId, rrn, deliveryChannel, txnCode, txnAmount);

	}

	public String redemptionDelayCheck(String redemptionDelayCheck) throws SQLException {

		String redemCount = "false";
		logger.info("redemptionDelayCheckQuery: {}", redemptionDelayCheck);

		try {
			if (redemptionDelayCheck != null && !redemptionDelayCheck.isEmpty()) {
				redemCount = getJdbcTemplate().queryForObject(redemptionDelayCheck, String.class);

			}
			if (redemCount != null && !redemCount.isEmpty() && !"0".equals(redemCount)) {

				return redemCount;
			} else {
				redemCount = "false";
			}
		}

		catch (EmptyResultDataAccessException e) {

			logger.info("no redemption configured");
			return redemCount;
		}

		return redemCount;

	}

	@Override
	public int getDelayedAmount(String accountId) {

		logger.debug(LoggerConstants.ENTER);
		int delayedAmount = 0;
		try {
			logger.info("Get Delayed Amount Query {}, values(accountId: {})", QueryConstants.GET_DELAYED_AMOUNT, accountId);
			delayedAmount = getJdbcTemplate().queryForObject(QueryConstants.GET_DELAYED_AMOUNT, new Object[] { accountId }, Integer.class);
		} catch (Exception e) {
			return delayedAmount;
		}
		logger.info("Get Delayed Amount Query response: {}", delayedAmount);
		logger.debug(LoggerConstants.EXIT);
		return delayedAmount;
	}

	public int updateDelayedAmount(String accountId) {

		return getJdbcTemplate().update(QueryConstants.UPDATE_DELAYED_AMOUNT, accountId);

	}

}
