package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.CommonValidationsDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RedemptionDelayService;
import com.incomm.cclp.transaction.bean.RedemMerchantConfig;
import com.incomm.cclp.transaction.bean.Transactions;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;
import com.incomm.cclp.transaction.service.impl.LocalCacheServiceImpl;
import com.incomm.cclp.util.Util;

@Service
public class RedemptionDelayServiceImpl implements RedemptionDelayService {

	@Autowired
	CommonValidationsDAO commonValidationsDao;

	@Autowired
	LocalCacheServiceImpl localCacheService;

	private static final Logger logger = LogManager.getLogger(RedemptionDelayServiceImpl.class);

	@Override
	public void redemptionDelayCheck(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		String productId = "";
		String merchantId = "";
		String accountId = "";
		String rrn = "";
		String deliveryChannel = "";
		String txnAmount = "";
		String txnCode = "";

		String delayAmount = "";
		int insDelayCount = 0;
		int delayFlagUpdCount = 0;
		String txnType = "";
		try {

			Map<String, Transactions> tnxMap = localCacheService.getTransactionMapping(null);

			if (valueDto.getValueObj() != null && valueDto.getValueObj()
				.containsKey(ValueObjectKeys.REDEMPTION_DELAY_CHECK_AVAIL)
					&& !valueDto.getValueObj()
						.containsKey(ValueObjectKeys.PURAUTHREQ_PURSE_NAME)) {

				txnType = valueDto.getValueObj()
					.get(ValueObjectKeys.MSG_TYPE);

				productId = Util.convertAsString(valueDto.getValueObj()
					.get(ValueObjectKeys.SPIL_PROD_ID));

				merchantId = Util.convertAsString(valueDto.getValueObj()
					.get(ValueObjectKeys.SPIL_MERCHANT_NAME));

				txnAmount = Util.convertAsString(valueDto.getValueObj()
					.get(ValueObjectKeys.SPIL_TRAN_AMT));

				accountId = Util.convertAsString(valueDto.getValueObj()
					.get(ValueObjectKeys.CARD_ACCOUNT_ID));

				rrn = Util.convertAsString(valueDto.getValueObj()
					.get(ValueObjectKeys.INCOM_REF_NUMBER));

				deliveryChannel = Util.convertAsString(valueDto.getValueObj()
					.get(ValueObjectKeys.DELIVERYCHNL));

				txnCode = Util.convertAsString(valueDto.getValueObj()
					.get(ValueObjectKeys.TRANS_CODE));

				if (!productId.isEmpty() && !merchantId.isEmpty() && !txnAmount.isEmpty() && !accountId.isEmpty() && !rrn.isEmpty()
						&& !deliveryChannel.isEmpty() && !txnCode.isEmpty()) {

					/**
					 * 002 010 011 012 014
					 */

					if ((txnCode.equalsIgnoreCase(tnxMap.get(TransactionConstant.ACT)
						.getTransactionCode()) || txnCode.equalsIgnoreCase(
								tnxMap.get(TransactionConstant.STORE_CREDIT)
									.getTransactionCode())
							|| txnCode.equalsIgnoreCase(tnxMap.get(TransactionConstant.CREDIT)
								.getTransactionCode())
							|| txnCode.equalsIgnoreCase(tnxMap.get(TransactionConstant.RECHARGE)
								.getTransactionCode())
							|| txnCode.equalsIgnoreCase(tnxMap.get(TransactionConstant.VALINS)
								.getTransactionCode())
							|| txnCode.equalsIgnoreCase(tnxMap.get(TransactionConstant.PREVALINS)
								.getTransactionCode())
							|| txnCode.equalsIgnoreCase(tnxMap.get(TransactionConstant.SALE_ACTIVE_CODE)
								.getTransactionCode()))
							&& txnType.equalsIgnoreCase(TransactionConstant.MSG_TYPE_POSITIVE)) {

						delayAmount = commonValidationsDao.redemptionDelayCheck(formRedemptionDelayQuery(valueDto));

						if (delayAmount != null && delayAmount.equalsIgnoreCase("false")) {
							logger.info("redemption not configured");
						}

						else if (delayAmount != null && !delayAmount.equalsIgnoreCase("false")) {

							insDelayCount = commonValidationsDao.insertDelayedLoad(accountId, deliveryChannel, txnCode, rrn, txnAmount,
									delayAmount);

							if (insDelayCount > 0) {
								delayFlagUpdCount = commonValidationsDao.updateRedemptionDelayFlag(accountId);

								if (delayFlagUpdCount < 1) {
									throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);

								}
								logger.info(delayFlagUpdCount);

							} else {
								throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);

							}
						}
					}

					else if ((txnCode.equalsIgnoreCase(tnxMap.get(TransactionConstant.ACT)
						.getTransactionCode()) || txnCode.equalsIgnoreCase(
								tnxMap.get(TransactionConstant.STORE_CREDIT)
									.getTransactionCode())
							|| txnCode.equalsIgnoreCase(tnxMap.get(TransactionConstant.CREDIT)
								.getTransactionCode())
							|| txnCode.equalsIgnoreCase(tnxMap.get(TransactionConstant.RECHARGE)
								.getTransactionCode())
							|| txnCode.equalsIgnoreCase(tnxMap.get(TransactionConstant.VALINS)
								.getTransactionCode())
							|| txnCode.equalsIgnoreCase(tnxMap.get(TransactionConstant.SALE_ACTIVE_CODE)
								.getTransactionCode()))
							&& txnType.equalsIgnoreCase(TransactionConstant.MSG_TYPE_REVERSAL))

					/** for reversals */
					{
						commonValidationsDao.updateDelayedLoadReversal(accountId, rrn, deliveryChannel, txnCode, txnAmount);

					}

				} else {
					throw new ServiceException(SpilExceptionMessages.REDEMPTION_DELAY_ERROR, ResponseCodes.SYSTEM_ERROR);
				}

			}
			/*
			 * else { throw new ServiceException(SpilExceptionMessages.REDEMPTION_DELAY_ERROR,
			 * ResponseCodes.SYSTEM_ERROR);
			 * 
			 * }
			 */

		}

		catch (Exception e) {
			logger.error("Exception in redemptionDelayCheck: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.REDEMPTION_DELAY_ERROR, ResponseCodes.SYSTEM_ERROR);

		}

		logger.debug(GeneralConstants.EXIT);
	}

	/**
	 * @MethodDescription Method to fetch redemption Delay Config Details
	 * @param productAttributes
	 * @param valueObj
	 * @return List<RedemMerchantConfig>
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public List<RedemMerchantConfig> redemptionDelayConfig(ValueDTO valueDto) {

		List<RedemMerchantConfig> redemptionDelayMapping = new ArrayList<>();

		@SuppressWarnings("unchecked")
		Map<String, List<Object>> redemptionConfigMap = (Map<String, List<Object>>) valueDto.getProductAttributes()
			.get("Product")
			.get("REDEMPTION_CONFIG");

		if (redemptionConfigMap != null && !redemptionConfigMap.isEmpty()) {
			List<Object> redemptionConfigList = redemptionConfigMap.get(valueDto.getValueObj()
				.get(ValueObjectKeys.PRODUCT_ID) + "_"
					+ (valueDto.getValueObj()
						.get(ValueObjectKeys.SPIL_MERCHANT_NAME)
						.toUpperCase()));
			if (!CollectionUtils.isEmpty(redemptionConfigList)) {
				for (Iterator iterator = redemptionConfigList.iterator(); iterator.hasNext();) {
					Object[] object = null;
					Object obj = iterator.next();
					if (obj instanceof Collection) {
						object = ((List) obj).toArray();

					} else {
						object = (Object[]) obj;
					}
					RedemMerchantConfig redemptionDelay = new RedemMerchantConfig();
					redemptionDelay.setProductId(object[1] + "");
					redemptionDelay.setMerchantId(object[2] + "");
					redemptionDelay.setMerchantName(object[3] + "");
					redemptionDelay.setStartTime(object[4] + "");
					redemptionDelay.setEndTime(object[5] + "");
					redemptionDelay.setDelayMins(object[6] + "");
					redemptionDelayMapping.add(redemptionDelay);

				}
			}
		}

		return redemptionDelayMapping;

	}

	public String formRedemptionDelayQuery(ValueDTO valueDto) {

		StringBuilder redemptionConfigQuery = new StringBuilder();

		List<RedemMerchantConfig> redemptionDelayConfig = redemptionDelayConfig(valueDto);
		if (redemptionDelayConfig != null && !redemptionDelayConfig.isEmpty()) {
			redemptionConfigQuery.append("SELECT  CASE ");
			for (RedemMerchantConfig redemMerchantConfig : redemptionDelayConfig) {

				redemptionConfigQuery.append("  WHEN  (" + redemMerchantConfig.getStartTime() + " <= TO_CHAR (SYSDATE, 'hh24miss')  AND "
						+ redemMerchantConfig.getEndTime() + " >= TO_CHAR (SYSDATE, 'hh24miss') ) THEN  '"
						+ redemMerchantConfig.getDelayMins() + "' ");

			}
			return String.valueOf(redemptionConfigQuery + " ELSE '0' END FROM DUAL");

		}

		return null;
	}
}
