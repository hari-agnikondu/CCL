package com.incomm.cclp.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.account.util.DateTimeFormatType;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.CommonValidationsDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.LimitValidationService;
import com.incomm.cclp.util.Util;

@Service
public class LimitValidationServiceImpl implements LimitValidationService {

	@Autowired
	CommonValidationsDAO commonValidationsDao;

	@Autowired
	CacheManager cacheManager;

	private static final Logger logger = LogManager.getLogger(LimitValidationServiceImpl.class);

	/*
	 * Getting the Limits of the card and product
	 */
	@Override

	public void validateLimits(ValueDTO valueDto) throws ServiceException {

		logger.debug("Validating Limit Attributes : ENTER");

		logger.info("Limit validation...");

		Map<String, String> valueObj = valueDto.getValueObj();

		Map<String, Map<String, Object>> productAttributesMap = null;
		Map<String, Object> cardUsageAttributesMap = valueDto.getUsageLimit();

		if (!Util.isEmpty(valueObj.get(ValueObjectKeys.PRFL_ID))) {
			String customerProfileAttributesStr = commonValidationsDao
				.getCardAttributes(String.valueOf(valueObj.get(ValueObjectKeys.PRFL_ID)));

			try {
				productAttributesMap = Util.jsonToMap(customerProfileAttributesStr);
			} catch (IOException e) {
				logger.error("Error while converting to HashMap" + e.getMessage(), e);
				throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
			}
		} else {
			productAttributesMap = valueDto.getProductAttributes();
		}

		String channelShortName = commonValidationsDao.getDeliveryChannelShortName(valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME, channelShortName);
		String txnShortName = commonValidationsDao.getTransShortNameForTransCode(valueObj.get(ValueObjectKeys.TRANS_CODE));
		valueObj.put(ValueObjectKeys.ORIGINAL_TRANSACTION_SHORT_NAME, txnShortName);
		String delchnlTxn = valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME) + "_"
				+ valueObj.get(ValueObjectKeys.ORIGINAL_TRANSACTION_SHORT_NAME);

		Map<String, Object> productLimits = productAttributesMap.get(ValueObjectKeys.LIMITS);

		if (!CollectionUtils.isEmpty(productLimits) && productLimits != null) {
			if (cardUsageAttributesMap == null || CollectionUtils.isEmpty(cardUsageAttributesMap)) {
				logger.debug("This is a first transaction... set limit attributes");
				cardUsageAttributesMap = setCardUsageLimit(productLimits);
				valueDto.setUsageLimit(cardUsageAttributesMap);
			} else {
				Map<String, Object> productLimitsdelchnlTxn = new HashMap<>();
				productLimits.entrySet()
					.stream()
					.filter(p -> p.getKey()
						.startsWith(delchnlTxn))
					.forEach(map -> productLimitsdelchnlTxn.put(map.getKey(), map.getValue()));
				Map<String, Object> cardUsageMapLimit = cardUsageAttributesMap;

				productLimitsdelchnlTxn.entrySet()
					.stream()
					.forEach(p -> {
						if (Util.isEmpty(String.valueOf(p.getValue()))) {
							cardUsageMapLimit.put(p.getKey(), p.getValue());
						} else if (Util.isEmpty(String.valueOf(cardUsageMapLimit.get(p.getKey())))) {
							cardUsageMapLimit.put(p.getKey(), "0");
						}

					});

				valueDto.setUsageLimit(cardUsageMapLimit);
				resetLimits(valueDto);
			}

			Map<String, Object> productDelchnlTxnLimitMap = new HashMap<>();
			productLimits.entrySet()
				.stream()
				.filter(p -> p.getKey()
					.startsWith(delchnlTxn))
				.forEach(map -> productDelchnlTxnLimitMap.put(map.getKey(), map.getValue()));

			Map<String, Object> cardUsageLimits = cardUsageAttributesMap;

			Map<String, Object> cardDelchnlTxnLimitMap = cardUsageLimits.entrySet()
				.stream()
				.filter(p -> (p.getKey()
					.startsWith(delchnlTxn) && p.getValue() != null))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			logger.debug("validating limit attributes for {}", delchnlTxn);

			if (valueObj.get(ValueObjectKeys.MSG_TYPE)
				.equals("0200")) {
				validate(valueObj, productDelchnlTxnLimitMap, cardDelchnlTxnLimitMap, delchnlTxn);
				updateCardLimitAttributes(valueDto);
			} else if (valueObj.get(ValueObjectKeys.MSG_TYPE)
				.equals("0400")) {
				revertCardLimitAttributes(valueDto);
			}
		}
		logger.info("Limit validation success....");
		logger.debug("Validating Limit Attributes : EXIT");
	}

	private Map<String, Object> setCardUsageLimit(Map<String, Object> productLimits) {
		logger.debug("set Card_Usage Limits : ENTER");

		Map<String, Object> cardUsageLimits = new HashMap<>();
		productLimits.entrySet()
			.stream()
			.forEach(p -> {
				if (!Util.isEmpty(String.valueOf(p.getValue()))) {
					cardUsageLimits.put(p.getKey(), "0");
				} else {
					cardUsageLimits.put(p.getKey(), p.getValue());
				}

			});

		logger.debug("EXIT");
		return cardUsageLimits;
	}

	/*
	 * Validating the minimum and maximum amount per transaction Calculating the maximum amount per
	 * daily,weekly,monthly,early calculating the number of transactions per daily,weekly,monthly and yearly basis
	 */
	private void validate(Map<String, String> valueObj, Map<String, Object> productDelchnlTxnLimitMap,
			Map<String, Object> cardDelchnlTxnLimitMap, String delchnlTxn) throws ServiceException {

		double transactionAmount = Double.parseDouble(valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT));
		logger.info("Transaction amount : {}", transactionAmount);
		logger.debug("limit Validation started");

		if (productDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.MIN_AMT_PET_TX)
				&& !Util.isEmpty(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MIN_AMT_PET_TX)))
				&& Double.valueOf(String
					.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MIN_AMT_PET_TX))) > transactionAmount) {
			logger.info(SpilExceptionMessages.LESS_THAN_MIN_AMT_PER_TXN);
			throw new ServiceException(SpilExceptionMessages.LESS_THAN_MIN_AMT_PER_TXN,
					ResponseCodes.TRANSACTION_AMOUNT_IS_LESS_THAN_MINIMUM_PER_TXN_AMOUNT);
		}

		if (productDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.MAX_AMT_PET_TX)
				&& !Util.isEmpty(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MAX_AMT_PET_TX)))
				&& Double.valueOf(String
					.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MAX_AMT_PET_TX))) < transactionAmount) {
			logger.info(SpilExceptionMessages.GREATER_THAN_MAX_AMT_PER_TXN);
			throw new ServiceException(SpilExceptionMessages.GREATER_THAN_MAX_AMT_PER_TXN,
					ResponseCodes.TRANSACTION_AMOUNT_IS_GREATER_THAN_MAXIMUM_PER_TXN_AMOUNT);
		}

		if (productDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_AMT)
				&& !Util.isEmpty(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_AMT)))
				&& cardDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_AMT)
				&& !Util.isEmpty(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_AMT)))
				&& Double.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_AMT))) > 0
				&& Double.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_AMT)))
						+ transactionAmount > Double
							.valueOf(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_AMT)))) {
			logger.info(SpilExceptionMessages.DAILY_MAX_AMT_REACHED);
			throw new ServiceException(SpilExceptionMessages.DAILY_MAX_AMT_REACHED, ResponseCodes.DAILY_MAXIMUM_TRANSACTION_AMOUNT_REACHED);
		}

		if (productDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_AMT)
				&& !Util.isEmpty(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_AMT)))
				&& cardDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_AMT)
				&& !Util.isEmpty(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_AMT)))
				&& Double.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_AMT))) > 0
				&& Double.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_AMT)))
						+ transactionAmount > Double
							.valueOf(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_AMT)))) {
			logger.info(SpilExceptionMessages.WEEKLY_MAX_AMT_REACHED);
			throw new ServiceException(SpilExceptionMessages.WEEKLY_MAX_AMT_REACHED,
					ResponseCodes.WEEKLY_MAXIMUM_TRANSACTION_AMOUNT_REACHED);
		}

		if (productDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_AMT)
				&& !Util.isEmpty(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_AMT)))
				&& cardDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_AMT)
				&& !Util.isEmpty(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_AMT)))
				&& Double.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_AMT))) > 0
				&& Double.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_AMT)))
						+ transactionAmount > Double
							.valueOf(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_AMT)))) {
			logger.info(SpilExceptionMessages.MONTHLY_MAX_AMT_REACHED);
			throw new ServiceException(SpilExceptionMessages.MONTHLY_MAX_AMT_REACHED,
					ResponseCodes.MONTHLY_MAXIMUM_TRANSACTION_AMOUNT_REACHED);
		}

		if (productDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_AMT)
				&& !Util.isEmpty(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_AMT)))
				&& cardDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_AMT)
				&& !Util.isEmpty(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_AMT)))
				&& Double.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_AMT))) > 0
				&& Double.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_AMT)))
						+ transactionAmount > Double
							.valueOf(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_AMT)))) {
			logger.info(SpilExceptionMessages.YEARLY_MAX_AMT_REACHED);
			throw new ServiceException(SpilExceptionMessages.YEARLY_MAX_AMT_REACHED,
					ResponseCodes.YEARLY_MAXIMUM_TRANSACTION_AMOUNT_REACHED);
		}

		if (productDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_COUNT)
				&& !Util.isEmpty(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_COUNT)))
				&& cardDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_COUNT)
				&& !Util.isEmpty(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_COUNT)))
				&& Integer.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_COUNT))) > 0
				&& Integer
					.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_COUNT))) >= Integer
						.valueOf(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.DAILY_MAX_COUNT)))) {
			logger.info(SpilExceptionMessages.DAILY_MAX_CNT_REACHED);
			throw new ServiceException(SpilExceptionMessages.DAILY_MAX_CNT_REACHED, ResponseCodes.DAILY_MAXIMUM_TRANSACTION_COUNT_REACHED);
		}

		if (productDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_COUNT)
				&& !Util.isEmpty(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_COUNT)))
				&& cardDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_COUNT)
				&& !Util.isEmpty(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_COUNT)))
				&& Integer.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_COUNT))) > 0
				&& Integer
					.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_COUNT))) >= Integer
						.valueOf(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.WEEKLY_MAX_COUNT)))) {
			logger.info(SpilExceptionMessages.WEEKLY_MAX_CNT_REACHED);
			throw new ServiceException(SpilExceptionMessages.WEEKLY_MAX_CNT_REACHED,
					ResponseCodes.WEEKLY_MAXIMUM_TRANSACTION_COUNT_REACHED);
		}

		if (productDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_COUNT)
				&& !Util.isEmpty(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_COUNT)))
				&& cardDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_COUNT)
				&& !Util.isEmpty(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_COUNT)))
				&& Integer.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_COUNT))) > 0
				&& Integer
					.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_COUNT))) >= Integer
						.valueOf(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.MONTHLY_MAX_COUNT)))) {
			logger.info(SpilExceptionMessages.MONTHLY_MAX_CNT_REACHED);
			throw new ServiceException(SpilExceptionMessages.MONTHLY_MAX_CNT_REACHED,
					ResponseCodes.MONTHLY_MAXIMUM_TRANSACTION_COUNT_REACHED);
		}

		if (productDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_COUNT)
				&& !Util.isEmpty(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_COUNT)))
				&& cardDelchnlTxnLimitMap.containsKey(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_COUNT)
				&& !Util.isEmpty(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_COUNT)))
				&& Integer.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_COUNT))) > 0
				&& Integer
					.valueOf(String.valueOf(cardDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_COUNT))) >= Integer
						.valueOf(String.valueOf(productDelchnlTxnLimitMap.get(delchnlTxn + "_" + ValueObjectKeys.YEARLY_MAX_COUNT)))) {
			logger.info(SpilExceptionMessages.YEARLY_MAX_CNT_REACHED);
			throw new ServiceException(SpilExceptionMessages.YEARLY_MAX_CNT_REACHED,
					ResponseCodes.YEARLY_MAXIMUM_TRANSACTION_COUNT_REACHED);
		}

	}

	/* reseting the (daily,weekly,monthly,yearly) max count and max amount */
	@Override
	public void resetLimits(ValueDTO valueDto) throws ServiceException {

		logger.debug("Reseting Limit Attributes : ENTER");

		Map<String, String> valueObj = valueDto.getValueObj();

		Map<String, Object> cardUsageLimitsMap = valueDto.getUsageLimit();

		/* reseting limit attribute of card */
		reset(valueObj, cardUsageLimitsMap);

		logger.debug("Reseting Limit Attributes : EXIT");
	}

	private void reset(Map<String, String> valueObj, Map<String, Object> cardLimits) throws ServiceException {

		java.util.Date lastTransDate = null;
		java.util.Date todayDate = null;

		cardLimits.entrySet()
			.stream()
			.filter(p -> p.getValue() == null)
			.forEach(map -> map.setValue("0"));
		String date = new SimpleDateFormat(ValueObjectKeys.DATE_FORMAT).format(new Date());

		try {
			String lastTransactionDate = valueObj.get(ValueObjectKeys.LAST_TXN_DATE);
			lastTransDate = lastTransactionDate == null ? new Date(0L)
					: DateTimeUtil.parseDate(lastTransactionDate, DateTimeFormatType.YYYY_MM_DD_WITH_HYPHEN);

			todayDate = new SimpleDateFormat(ValueObjectKeys.DATE_FORMAT).parse(date);

		} catch (ParseException e) {
			logger.error("Error Occured while try get last transaction date, {}", e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

		logger.info("Last transaction date" + lastTransDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(todayDate);

		Calendar calendarLastTransDate = Calendar.getInstance();
		calendarLastTransDate.setTime(lastTransDate);

		Calendar lastTransDateSunday = Calendar.getInstance();
		lastTransDateSunday.setTime(lastTransDate);
		int weekday = calendarLastTransDate.get(Calendar.DAY_OF_WEEK);
		int days = Calendar.SUNDAY - weekday;
		if (days < 0) {
			days += 7;
		}
		lastTransDateSunday.add(Calendar.DAY_OF_YEAR, days);
		if (!(lastTransDate.equals(todayDate))) {
			if (lastTransDate.before(todayDate)) {
				logger.info("Last Transaction date is lesser then transaction date");

				cardLimits.entrySet()
					.stream()
					.filter(q -> !Util.isEmpty(String.valueOf(q.getValue())))
					.filter(p -> p.getKey()
						.endsWith(ValueObjectKeys.DAILY_MAX_COUNT)
							|| p.getKey()
								.endsWith(ValueObjectKeys.DAILY_MAX_AMT))
					.forEach(q -> cardLimits.put(q.getKey(), "0"));

			}
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || todayDate.after(lastTransDateSunday.getTime())) {
				logger.info("Last Transaction date is lesser then (by week) transaction date");

				cardLimits.entrySet()
					.stream()
					.filter(q -> !Util.isEmpty(String.valueOf(q.getValue())))
					.filter(p -> p.getKey()
						.endsWith(ValueObjectKeys.WEEKLY_MAX_COUNT)
							|| p.getKey()
								.endsWith(ValueObjectKeys.WEEKLY_MAX_AMT))
					.forEach(q -> cardLimits.put(q.getKey(), "0"));

			}
			if ((cal.get(Calendar.DATE) == 1) || cal.get(Calendar.MONTH) > calendarLastTransDate.get(Calendar.MONTH)) {
				logger.info("Last Transaction date is lesser then (by month) transaction date");

				cardLimits.entrySet()
					.stream()
					.filter(q -> !Util.isEmpty(String.valueOf(q.getValue())))
					.filter(p -> p.getKey()
						.endsWith(ValueObjectKeys.MONTHLY_MAX_COUNT)
							|| p.getKey()
								.endsWith(ValueObjectKeys.MONTHLY_MAX_AMT))
					.forEach(q -> cardLimits.put(q.getKey(), "0"));

			}

			if ((cal.get(Calendar.DATE) == 1 && cal.get(Calendar.MONTH) == Calendar.JANUARY)
					|| (cal.get(Calendar.YEAR) > calendarLastTransDate.get(Calendar.YEAR))) {
				logger.info("Last Transaction date is lesser then (by year) transaction date");

				cardLimits.entrySet()
					.stream()
					.filter(q -> !Util.isEmpty(String.valueOf(q.getValue())))
					.filter(p -> p.getKey()
						.endsWith(ValueObjectKeys.YEARLY_MAX_COUNT)
							|| p.getKey()
								.endsWith(ValueObjectKeys.YEARLY_MAX_AMT)
							|| p.getKey()
								.endsWith(ValueObjectKeys.MONTHLY_MAX_COUNT)
							|| p.getKey()
								.endsWith(ValueObjectKeys.MONTHLY_MAX_AMT))
					.forEach(q -> cardLimits.put(q.getKey(), "0"));
			}

		}

	}

	@Override
	public void updateCardLimitAttributes(ValueDTO valueDto) throws ServiceException {

		logger.debug("Update Card Limit Attributes : ENTER");

		Map<String, String> valueObj = valueDto.getValueObj();
		String cardAttributesJsonString = null;

		String delchnlTxn = valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME) + "_"
				+ valueObj.get(ValueObjectKeys.ORIGINAL_TRANSACTION_SHORT_NAME);

		Map<String, Object> cardUsageAttributesMap = valueDto.getUsageLimit();

		Map<String, Object> updateCardLimitsMap = new HashMap<>();

		updateCardLimitsMap.putAll(cardUsageAttributesMap);

		if (cardUsageAttributesMap.size() > 0) {
			cardUsageAttributesMap.forEach((key, value) -> {
				// for Updating the USAGE_LIMIT Count for daily,weekly,monthly,yearly
				if (key.startsWith(delchnlTxn) && key.endsWith("Count") && !Util.isEmpty(String.valueOf(value))) {

					int finalValue = Integer.parseInt(String.valueOf(value));
					value = finalValue + 1;
					updateCardLimitsMap.put(key, value);
				}
				// for Updating the USAGE_LIMIT Amount for daily,weekly,monthly,yearly
				if (key.startsWith(delchnlTxn) && key.endsWith("Amt") && !Util.isEmpty(String.valueOf(value))) {

					double finalValue = Double.parseDouble(value.toString());
					value = finalValue + Double.parseDouble((valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT)));
					updateCardLimitsMap.put(key, value);
				}
			});
		}

		try {
			valueDto.setUsageLimit(updateCardLimitsMap);
			cardAttributesJsonString = Util.convertMapToJson(updateCardLimitsMap);
		} catch (Exception e) {
			logger.error("Error Occured while try to get card details, {}", e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}
		// putting updated limits in valueObj
		valueObj.put(ValueObjectKeys.CARD_USAGE_LIMIT, cardAttributesJsonString);
		logger.debug("EXIT");
	}

	@Override
	public void revertCardLimitAttributes(ValueDTO valueDto) throws ServiceException {

		logger.debug("Revert Card Limit Attributes : ENTER");
		Map<String, String> valueObj = valueDto.getValueObj();
		String cardAttributesJsonString = null;

		String delchnlTxn = valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME) + "_"
				+ valueObj.get(ValueObjectKeys.ORIGINAL_TRANSACTION_SHORT_NAME);

		Map<String, Object> cardUsageAttributesMap = valueDto.getUsageLimit();

		Map<String, Object> updateCardLimitsMap = new HashMap<>();

		updateCardLimitsMap.putAll(cardUsageAttributesMap);

		if (cardUsageAttributesMap.size() > 0) {
			cardUsageAttributesMap.forEach((key, value) -> {
				// for Updating the USAGE_LIMIT Count for daily,weekly,monthly,yearly
				if (key.startsWith(delchnlTxn) && key.endsWith("Count") && !Util.isEmpty(String.valueOf(value))
						&& Integer.parseInt(String.valueOf(value)) > 0) {

					int finalValue = Integer.parseInt(String.valueOf(value));
					value = finalValue - 1;
					updateCardLimitsMap.replace(key, value);
				}
				// for Updating the USAGE_LIMIT Amount for daily,weekly,monthly,yearly
				if (key.startsWith(delchnlTxn) && key.endsWith("Amt") && !Util.isEmpty(String.valueOf(value))
						&& Double.parseDouble(String.valueOf(value)) > 0) {

					double finalValue = Double.parseDouble(String.valueOf(value));
					value = finalValue - Double.parseDouble(String.valueOf(valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT)));
					updateCardLimitsMap.replace(key, value);
				}
			});
		}

		try {
			cardAttributesJsonString = Util.convertMapToJson(updateCardLimitsMap);
		} catch (Exception e) {
			logger.error("Error Occured while try to get card details, {}", e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}
		valueObj.put(ValueObjectKeys.CARD_USAGE_LIMIT, cardAttributesJsonString);
		logger.debug("EXIT");
	}

}
