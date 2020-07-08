package com.incomm.cclp.service.impl;

/**
 * FeeCalculationServiceImpl provides the fee calculation based on screen configuration.
 * @author venkateshgaddam
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.CommonValidationsDAO;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.FeeCalculationService;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;
import com.incomm.cclp.util.Util;

@Service
public class FeeCalculationServiceImpl implements FeeCalculationService {

	private static final Logger logger = LogManager.getLogger(FeeCalculationServiceImpl.class);

	@Autowired
	CommonValidationsDAO commonValidationsDao;

	private static final String FREE_COUNT_TAG = "_freeCount";
	private static final String MAX_COUNT_TAG = "_maxCount";
	private static final String FEE_PERCENT_TAG = "_feePercent";
	private static final String MIN_FEE_AMT_TAG = "_minFeeAmt";
	private static final String FEE_CONDITION_TAG = "_feeCondition";
	private static final String FEE_AMT_TAG = "_feeAmt";

	private static final String MONTHLY_FEE_CAP_TIME_PERIOD = "monthlyFeeCap_timePeriod";

	private static final String LAST_TXN_DATE = "_lastTxnDate";

	private static final String DB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	private static final String MONTHLY_FEE_CAP_AVAIL = "_monthlyFeeCapAvail";

	@Autowired
	SpilDAO spilDao;

	/**
	 * feeCalculation method does fee calculation.
	 * 
	 * @author venkateshgaddam
	 */
	@Override
	public void feeCalculation(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		try {
			Map<String, String> valueObj = valueDto.getValueObj();
			Map<String, Map<String, Object>> productAttributesMap = valueDto.getProductAttributes();
			String txnShortName = commonValidationsDao.getTransShortNameForTransCode(valueObj.get(ValueObjectKeys.TRANS_CODE));
			valueObj.put(ValueObjectKeys.ORIGINAL_TRANSACTION_SHORT_NAME, txnShortName);
			String delchnlTxn = valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME) + "_"
					+ valueObj.get(ValueObjectKeys.ORIGINAL_TRANSACTION_SHORT_NAME);
			Map<String, Object> transactionFee = productAttributesMap.get(ValueObjectKeys.TRANSACTION_FEES);
			if (!CollectionUtils.isEmpty(transactionFee) && transactionFee != null) {
				Map<String, Object> productDelchnlTxnFeeMap = new HashMap<>();
				transactionFee.entrySet()
					.stream()
					.filter(p -> p.getKey()
						.startsWith(delchnlTxn))
					.forEach(map -> productDelchnlTxnFeeMap.put(map.getKey(), map.getValue()));

				Map<String, Object> usageFeeMainAttr = valueDto.getUsageFee();
				if (CollectionUtils.isEmpty(usageFeeMainAttr)) {
					// If UsageFee is null/empty
					usageFeeMainAttr = setCardUsageFee(transactionFee);
					valueDto.setUsageFee(usageFeeMainAttr);
					valueDto.getValueObj()
						.put(ValueObjectKeys.UPDATE_USAGE_FEE, "YES");
				}
				// Map<String, Object> usageFees = usageFeeMainAttr.get(ValueObjectKeys.FEES);

				if (!productDelchnlTxnFeeMap.isEmpty()) {
					if (TransactionConstant.MSG_TYPE_POSITIVE.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
						// check free count
						if (productDelchnlTxnFeeMap.get(delchnlTxn + "_freeCountFreq") != null
								&& !Util.isEmpty(productDelchnlTxnFeeMap.get(delchnlTxn + FREE_COUNT_TAG) + "")
								&& Integer.parseInt(productDelchnlTxnFeeMap.get(delchnlTxn + FREE_COUNT_TAG) + "") > 0) {
							feeFreeMaxCalc(usageFeeMainAttr, valueObj, delchnlTxn, valueDto, "_freeCountFreq", FREE_COUNT_TAG,
									productDelchnlTxnFeeMap);
						}
						if (valueObj.get(ValueObjectKeys.FREE_FEE_FLAG) != null
								&& "Y".equals(valueObj.get(ValueObjectKeys.FREE_FEE_FLAG))) {
							setDefaultTxnFeeDetails(valueObj);
							return;
						}
						// check max exceeded count
						if ("N".equals(valueObj.get(ValueObjectKeys.FREE_FEE_FLAG))
								&& productDelchnlTxnFeeMap.get(delchnlTxn + "_maxCountFreq") != null
								&& !Util.isEmpty(productDelchnlTxnFeeMap.get(delchnlTxn + MAX_COUNT_TAG) + "")
								&& Integer.parseInt(productDelchnlTxnFeeMap.get(delchnlTxn + MAX_COUNT_TAG) + "") > 0) {
							feeFreeMaxCalc(usageFeeMainAttr, valueObj, delchnlTxn, valueDto, "_maxCountFreq", MAX_COUNT_TAG,
									productDelchnlTxnFeeMap);
							if (valueObj.get(ValueObjectKeys.MAX_FEE_FLAG) != null
									&& "Y".equals(valueObj.get(ValueObjectKeys.MAX_FEE_FLAG))) {
								setDefaultTxnFeeDetails(valueObj);
							} else {
								if (!Util.isEmpty(productDelchnlTxnFeeMap.get(delchnlTxn + MONTHLY_FEE_CAP_AVAIL) + "")
										&& "true".equalsIgnoreCase(productDelchnlTxnFeeMap.get(delchnlTxn + MONTHLY_FEE_CAP_AVAIL) + "")) {
									calcMonthlyFeeCap(valueObj, productAttributesMap.get(ValueObjectKeys.MONTHLY_FEE_CAP), delchnlTxn,
											valueDto, productDelchnlTxnFeeMap);
								} else {
									calculateFee(valueObj, productDelchnlTxnFeeMap, delchnlTxn);
								}
							}
						} // if
						else {
							if (!Util.isEmpty(productDelchnlTxnFeeMap.get(delchnlTxn + MONTHLY_FEE_CAP_AVAIL) + "")
									&& "true".equalsIgnoreCase(productDelchnlTxnFeeMap.get(delchnlTxn + MONTHLY_FEE_CAP_AVAIL) + "")) {
								calcMonthlyFeeCap(valueObj, productAttributesMap.get(ValueObjectKeys.MONTHLY_FEE_CAP), delchnlTxn, valueDto,
										productDelchnlTxnFeeMap);
							} else {
								calculateFee(valueObj, productDelchnlTxnFeeMap, delchnlTxn);
							}
						}
					} else {
						// Fee monthly fee cap and Free/Max count Reversal
						if (valueObj.get(ValueObjectKeys.ORGNL_FREE_FEE_FLAG) != null
								&& "Y".equals(valueObj.get(ValueObjectKeys.ORGNL_FREE_FEE_FLAG))) {
							reverseCardUsageFee(valueDto, delchnlTxn + FREE_COUNT_TAG);
						}
						boolean isMonthlyFeeCap = false;
						double feeAmt;
						if (valueObj.get(ValueObjectKeys.ORGNL_MAX_FEE_FLAG) != null
								&& "N".equals(valueObj.get(ValueObjectKeys.ORGNL_MAX_FEE_FLAG))) {
							reverseCardUsageFee(valueDto, delchnlTxn + MAX_COUNT_TAG);
							feeAmt = valueDto.getValueObj()
								.get(ValueObjectKeys.ORGNL_TRAN_FEE_AMNT) != null ? Double.parseDouble(
										valueDto.getValueObj()
											.get(ValueObjectKeys.ORGNL_TRAN_FEE_AMNT))
										: 0.0;
							if (feeAmt > 0) {
								isMonthlyFeeCap = true;
							}
						}
						if ((valueObj.get(ValueObjectKeys.ORGNL_MAX_FEE_FLAG) == null || isMonthlyFeeCap)
								&& (!Util.isEmpty(productDelchnlTxnFeeMap.get(delchnlTxn + MONTHLY_FEE_CAP_AVAIL) + "")
										&& "true".equalsIgnoreCase(productDelchnlTxnFeeMap.get(delchnlTxn + MONTHLY_FEE_CAP_AVAIL) + ""))) {
							calcMonthlyFeeCap(valueObj, productAttributesMap.get(ValueObjectKeys.MONTHLY_FEE_CAP), delchnlTxn, valueDto,
									productDelchnlTxnFeeMap);
						}
					}

				}
			}
		} catch (ServiceException e) {
			logger.error("ServiceException Occured while calculating fee: " + e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception Occured while calculating fee: " + e.getMessage(), e);
			throw new ServiceException("Exception Occured while calculating fee", ResponseCodes.SYSTEM_ERROR);
		}
		logger.debug(GeneralConstants.EXIT);
	}

	/**
	 * feeFreeMaxCalc method does free/Max fee calculation.
	 * 
	 * @param productDeltxnFee
	 * @param valueObj
	 * @param delchnlTxn
	 * @param valueDto
	 * @param countFreqTag
	 * @param countTag
	 * @return txnCountFlag
	 */
	public void feeFreeMaxCalc(Map<String, Object> productDeltxnFee, Map<String, String> valueObj, String delchnlTxn, ValueDTO valueDto,
			String countFreqTag, String countTag, Map<String, Object> productDelchnlTxnFeeMap) {
		int currCnt = productDeltxnFee.get(delchnlTxn + countTag) != null
				? Integer.parseInt(productDeltxnFee.get(delchnlTxn + countTag) + "")
				: 0;
		if (valueObj.get(ValueObjectKeys.LAST_TXN_DATE) == null) {
			feeReset(0, valueDto, delchnlTxn, countTag, countFreqTag, productDelchnlTxnFeeMap.get(delchnlTxn + countFreqTag) + "");
			setTxnFeeFlag(countTag, valueObj, false);
			return;
		}
		LocalDate now = null;
		LocalDate calForLstTxnDt = null;
		try {
			now = getLocalDate(valueObj.get(ValueObjectKeys.DB_SYSDATE), DB_DATE_FORMAT);
			calForLstTxnDt = getLocalDate(valueObj.get(ValueObjectKeys.LAST_TXN_DATE), DB_DATE_FORMAT);
			if (calForLstTxnDt != null && now.isAfter(calForLstTxnDt) && productDelchnlTxnFeeMap.get(delchnlTxn + countFreqTag) != null) {
				if ("D".equals(productDelchnlTxnFeeMap.get(delchnlTxn + countFreqTag))) {
					currCnt = 0;
				} else if ("W".equals(productDelchnlTxnFeeMap.get(delchnlTxn + countFreqTag))) {
					calForLstTxnDt = calForLstTxnDt.with(DayOfWeek.SUNDAY);
					if ((DayOfWeek.SUNDAY.equals(now.getDayOfWeek()) || now.isAfter(calForLstTxnDt))) {
						currCnt = 0;
					}
				} else if ("BW".equals(productDelchnlTxnFeeMap.get(delchnlTxn + countFreqTag))) {
					LocalDate biWeeklyDt = calForLstTxnDt.plusDays(7);
					biWeeklyDt = calForLstTxnDt.with(DayOfWeek.SUNDAY);
					while (biWeeklyDt.isBefore(calForLstTxnDt)) {
						if (biWeeklyDt.isAfter(calForLstTxnDt)) {
							break;
						}
						biWeeklyDt = biWeeklyDt.plusDays(14);
					}
					if (now.isAfter(biWeeklyDt) || now.isEqual(biWeeklyDt)) {
						currCnt = 0;
					}
				} else if ("M".equals(productDelchnlTxnFeeMap.get(delchnlTxn + countFreqTag))
						&& (calForLstTxnDt.getDayOfMonth() == 1 || now.isAfter(calForLstTxnDt)))
					currCnt = 0;

				else if ("BM".equals(productDelchnlTxnFeeMap.get(delchnlTxn + countFreqTag))) {
					LocalDate add15ToCurrDt = now.plusDays(15);
					if ((now.isAfter(calForLstTxnDt) || add15ToCurrDt.isAfter(calForLstTxnDt)) && now.isAfter(calForLstTxnDt)) {
						currCnt = 0;
					}
				} else if ("Y".equals(productDelchnlTxnFeeMap.get(delchnlTxn + countFreqTag))
						&& (now.getDayOfMonth() == 1 && now.getMonthValue() == 1) || now.isAfter(calForLstTxnDt))
					currCnt = 0;
			}

			if (currCnt == 0) {
				feeReset(1, valueDto, delchnlTxn, countTag, countFreqTag, productDelchnlTxnFeeMap.get(delchnlTxn + countFreqTag) + "");
				setTxnFeeFlag(countTag, valueObj, false);
				return;
			}
			if (productDelchnlTxnFeeMap.get(delchnlTxn + countTag) != null
					&& currCnt >= Integer.parseInt(productDelchnlTxnFeeMap.get(delchnlTxn + countTag) + "")) {
				setTxnFeeFlag(countTag, valueObj, true);
			} else {
				feeReset(2, valueDto, delchnlTxn, countTag, countFreqTag, productDelchnlTxnFeeMap.get(delchnlTxn + countFreqTag) + "");
				setTxnFeeFlag(countTag, valueObj, false);
			}
		} catch (ParseException e) {
			logger.error("Error Occured in feeFreeMaxCalc: " + e.getMessage(), e);
			setTxnFeeFlag(countTag, valueObj, false);
		} catch (Exception e) {
			logger.error("Error Occured in feeFreeMaxCalc: " + e.getMessage(), e);
			setTxnFeeFlag(countTag, valueObj, false);
		}
	}

	/**
	 * feeReset used to update the usageFee Attribute.
	 * 
	 * @param resetFlag
	 * @param valueDto
	 * @param delchnlTxnWithTag
	 */
	public void feeReset(int resetFlag, ValueDTO valueDto, String delchnlTxn, String countTag, String countFrqTag, String currentFrqTag) {

		Map<String, Object> usageFeeMainAttr = valueDto.getUsageFee();

		if (usageFeeMainAttr != null && !usageFeeMainAttr.isEmpty()) {
			// Map<String, Object> usageFees = usageFeeMainAttr.get(ValueObjectKeys.FEES);
			if (!Util.isEmpty(usageFeeMainAttr.get(delchnlTxn + countTag) + "")) {
				if (resetFlag == 2) {
					int finalValue = Integer.parseInt(usageFeeMainAttr.get(delchnlTxn + countTag) + "");
					finalValue = finalValue + 1;
					usageFeeMainAttr.put(delchnlTxn + countTag, finalValue);
					usageFeeMainAttr.put(delchnlTxn + countFrqTag, currentFrqTag);
				} else if (resetFlag == 0 || resetFlag == 1) {
					usageFeeMainAttr.put(delchnlTxn + countTag, 1);
					usageFeeMainAttr.put(delchnlTxn + countFrqTag, currentFrqTag);
				}
			}
			// updating the usageFee
			updateUsageFee(valueDto, usageFeeMainAttr);
		}
	}

	/**
	 * Setting the Card Usage Fee values as product Fee at product level when CardUsageFee is null(first Transaction)
	 * 
	 * @param productFee
	 * @return cardUsageAttributesMap
	 */
	private Map<String, Object> setCardUsageFee(Map<String, Object> productFee) {
		logger.debug("set Card_Usage Fee : ENTER");
		// Map<String, Map<String, Object>> cardUsageAttributesMap = new HashMap<>();
		Map<String, Object> cardUsageFee = new HashMap<>();
		productFee.entrySet()
			.stream()
			.forEach(p -> {
				if (p.getKey()
					.endsWith("Count")) {
					cardUsageFee.put(p.getKey(), GeneralConstants.ZERO);
				} else {
					cardUsageFee.put(p.getKey(), p.getValue());
				}
			});
		// cardUsageAttributesMap.put(ValueObjectKeys.FEES, cardUsageFee);
		logger.debug("EXIT");
		return cardUsageFee;
	}

	/**
	 * reverseCardUsageFee method will revert the freeCount/MaxCount to previous
	 * 
	 * @param valueDto
	 * @param delchnlTxnWithTag
	 */
	private void reverseCardUsageFee(ValueDTO valueDto, String delchnlTxnWithTag) {
		Map<String, Object> usageFeeMainAttr = valueDto.getUsageFee();
		if (usageFeeMainAttr != null && !usageFeeMainAttr.isEmpty()) {
			// Map<String, Object> usageFees = usageFeeMainAttr.get(ValueObjectKeys.FEES);

			if (!Util.isEmpty(usageFeeMainAttr.get(delchnlTxnWithTag) + "")) {
				int finalValue = Integer.parseInt(String.valueOf(usageFeeMainAttr.get(delchnlTxnWithTag)));
				if (finalValue > 0) {
					finalValue = finalValue - 1;
					usageFeeMainAttr.put(delchnlTxnWithTag, finalValue);
				}
			}
			// updating the usageFee
			updateUsageFee(valueDto, usageFeeMainAttr);
		}
	}

	/**
	 * Setting the Free Fee flag as N means the txn is not a free transaction Setting the Max Fee flag as Y means the
	 * Max fee count has reached
	 * 
	 * @param countTag,valueObj,flag
	 */
	public void setTxnFeeFlag(String countTag, Map<String, String> valueObj, boolean flag) {
		if (flag) {
			if (FREE_COUNT_TAG.equals(countTag)) {
				valueObj.put(ValueObjectKeys.FREE_FEE_FLAG, "N");// N means not a free transaction
			} else if (MAX_COUNT_TAG.equals(countTag)) {
				valueObj.put(ValueObjectKeys.MAX_FEE_FLAG, "Y"); // Y means max Fee count reached
			}
		} else {
			if (FREE_COUNT_TAG.equals(countTag)) {
				valueObj.put(ValueObjectKeys.FREE_FEE_FLAG, "Y");
			} else if (MAX_COUNT_TAG.equals(countTag)) {
				valueObj.put(ValueObjectKeys.MAX_FEE_FLAG, "N");
			}
		}

	}

	/**
	 * Setting the default value if transaction is free
	 * 
	 * @param valueObj
	 * @param prodDelchnlTxnFeeMap
	 */
	public void setDefaultTxnFeeDetails(Map<String, String> valueObj) {
		valueObj.put(ValueObjectKeys.PROD_TXN_FEE_AMT, GeneralConstants.ZERO);
		valueObj.put(ValueObjectKeys.PROD_FLAT_FEE_AMT, GeneralConstants.ZERO);
		valueObj.put(ValueObjectKeys.PROD_PERCENT_FEE_AMT, GeneralConstants.ZERO);
		valueObj.put(ValueObjectKeys.PROD_MIN_FEE_AMT, GeneralConstants.ZERO);
	}

	/**
	 * Fee Calculation
	 * 
	 * @param valueObj
	 * @param prodDelchnlTxnFeeMap
	 * @param delchnlTxn
	 * @throws ServiceException
	 */
	public void calculateFee(Map<String, String> valueObj, Map<String, Object> prodDelchnlTxnFeeMap, String delchnlTxn)
			throws ServiceException {
		Double tranFeeAmt = 0D;
		Double tranAmt = 0D;
		Double tranFeePercent = 0D;
		Double feeAmt = 0D;
		Double minFeeAmt = 0D;
		try {
			if (!Util.isEmpty(valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT))
					&& !valueObj.containsKey(ValueObjectKeys.PARTIAL_AUTH_INDICATOR_AVAL_FLAG)) {
				tranAmt = Double.valueOf(valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT));
			}
			if (!Util.isEmpty(valueObj.get(ValueObjectKeys.AVAIL_BALANCE))
					&& valueObj.containsKey(ValueObjectKeys.PARTIAL_AUTH_INDICATOR_AVAL_FLAG)) {
				double delayedAmt = 0D;
				if (!Util.isEmpty(valueObj.get(ValueObjectKeys.REDEMPTION_DELAY_TRAN_AMOUNT))) {
					delayedAmt = Double.valueOf(valueObj.get(ValueObjectKeys.REDEMPTION_DELAY_TRAN_AMOUNT));
				}
				tranAmt = Double.valueOf(valueObj.get(ValueObjectKeys.AVAIL_BALANCE));
				tranAmt = tranAmt - delayedAmt;
			}
			if (!Util.isEmpty(prodDelchnlTxnFeeMap.get(delchnlTxn + FEE_PERCENT_TAG) + "")) {
				tranFeePercent = Double.valueOf(prodDelchnlTxnFeeMap.get(delchnlTxn + FEE_PERCENT_TAG)
					.toString());
			}
			if (!Util.isEmpty(prodDelchnlTxnFeeMap.get(delchnlTxn + FEE_AMT_TAG) + "")) {
				feeAmt = Double.valueOf(prodDelchnlTxnFeeMap.get(delchnlTxn + FEE_AMT_TAG)
					.toString());
			}
			if (!Util.isEmpty(prodDelchnlTxnFeeMap.get(delchnlTxn + MIN_FEE_AMT_TAG) + "")) {
				minFeeAmt = Double.valueOf(prodDelchnlTxnFeeMap.get(delchnlTxn + MIN_FEE_AMT_TAG)
					.toString());
			}
			if ("A".equals(String.valueOf(prodDelchnlTxnFeeMap.get(delchnlTxn + FEE_CONDITION_TAG)))) {
				tranFeeAmt = tranAmt * (tranFeePercent / 100);
				tranFeePercent = tranFeeAmt;
				tranFeeAmt = tranFeeAmt + feeAmt;
				if (tranFeeAmt < minFeeAmt) {
					tranFeeAmt = minFeeAmt;
					valueObj.put(ValueObjectKeys.PROD_FEE_CONDITION, "M");
				} else {
					valueObj.put(ValueObjectKeys.PROD_FEE_CONDITION, "A");
				}
			} else if ("O".equals(String.valueOf(prodDelchnlTxnFeeMap.get(delchnlTxn + FEE_CONDITION_TAG)))) {
				Double percentFee = tranAmt * (tranFeePercent / 100);
				if (percentFee > feeAmt) {
					tranFeeAmt = percentFee;
					tranFeePercent = percentFee;
				} else {
					tranFeeAmt = feeAmt;
				}
				valueObj.put(ValueObjectKeys.PROD_FEE_CONDITION, prodDelchnlTxnFeeMap.get(delchnlTxn + FEE_CONDITION_TAG) + "");
			} else if ("N".equals(String.valueOf(prodDelchnlTxnFeeMap.get(delchnlTxn + FEE_CONDITION_TAG)))) {
				tranFeeAmt = feeAmt;
				valueObj.put(ValueObjectKeys.PROD_FEE_CONDITION, prodDelchnlTxnFeeMap.get(delchnlTxn + FEE_CONDITION_TAG) + "");
			}
			valueObj.put(ValueObjectKeys.PROD_TXN_FEE_AMT, tranFeeAmt + "");
			valueObj.put(ValueObjectKeys.PROD_FLAT_FEE_AMT, feeAmt + "");
			valueObj.put(ValueObjectKeys.PROD_PERCENT_FEE_AMT, tranFeePercent + "");
			valueObj.put(ValueObjectKeys.PROD_MIN_FEE_AMT, minFeeAmt + "");
			// This feeCheckFlag is required for partial Auth check.
			valueObj.put("feeCheckFlag", "true");
		} catch (Exception e) {
			logger.error("Error Occured in calculateFee:" + e.getMessage(), e);
			throw new ServiceException("Exception Occured while calculateFee", ResponseCodes.SYSTEM_ERROR);
		}

	}

	/**
	 * Converting the DB date to LocalDate
	 * 
	 * @param valObj
	 * @return LocalDate
	 * @throws ParseException
	 */
	public LocalDate getLocalDate(String valObj, String format) throws ParseException {
		return new java.sql.Date(new SimpleDateFormat(format).parse(valObj)
			.getTime()).toLocalDate();
	}

	/**
	 * Calculating monthly Fee cap based on configuration
	 * 
	 * @param valueObj,monthlyFeeCap,delchnlTxn,valueDto,productDelchnlTxnFeeMap
	 * @throws ServiceException
	 * @author venkateshgaddam
	 */
	public void calcMonthlyFeeCap(Map<String, String> valueObj, Map<String, Object> monthlyFeeCap, String delchnlTxn, ValueDTO valueDto,
			Map<String, Object> productDelchnlTxnFeeMap) throws ServiceException {
		try {
			double monthlyFeeCapAmt = monthlyFeeCap.get("monthlyFeeCap_feeCapAmt") != null
					? Double.parseDouble(monthlyFeeCap.get("monthlyFeeCap_feeCapAmt")
						.toString())
					: 0.0;
			LocalDate now = getLocalDate(valueObj.get(ValueObjectKeys.DB_SYSDATE), DB_DATE_FORMAT);
			if (!Util.isEmpty(monthlyFeeCap.get(MONTHLY_FEE_CAP_TIME_PERIOD) + "")
					&& "CM".equals(monthlyFeeCap.get(MONTHLY_FEE_CAP_TIME_PERIOD) + "")) {
				LocalDate config = LocalDate.of(now.getYear(), now.getMonth(), now.with(TemporalAdjusters.firstDayOfMonth())
					.getDayOfMonth());
				LocalDate configPlusOne = config.plusMonths(1);
				configPlusOne = configPlusOne.minusDays(1);
				setCalMonthlyFeeCapToValObj(valueObj, delchnlTxn, valueDto, productDelchnlTxnFeeMap, configPlusOne, monthlyFeeCapAmt, now,
						config);
			} else if (!Util.isEmpty(monthlyFeeCap.get(MONTHLY_FEE_CAP_TIME_PERIOD) + "")
					&& "SM".equals(monthlyFeeCap.get(MONTHLY_FEE_CAP_TIME_PERIOD) + "")) {
				LocalDate config = getConfigDate(now, monthlyFeeCap);
				LocalDate configPlusOne = config.plusMonths(1);
				configPlusOne = configPlusOne.minusDays(1);
				setCalMonthlyFeeCapToValObj(valueObj, delchnlTxn, valueDto, productDelchnlTxnFeeMap, configPlusOne, monthlyFeeCapAmt, now,
						config);
			}

		} catch (Exception e) {
			logger.error("Error Occured in calcMonthlyFeeCap: " + e.getMessage(), e);
			throw new ServiceException("Exception Occured while doing monthly Fee cap", ResponseCodes.SYSTEM_ERROR);
		}

	}

	public LocalDate getLastTxnDate(String delchnlTxn, LocalDate configPlusOne, Map<String, Object> usageFees) {
		LocalDate lastTxnDate = null;
		try {
			if (Util.isEmpty(usageFees.get(delchnlTxn + LAST_TXN_DATE) + "")) {
				usageFees.put(delchnlTxn + LAST_TXN_DATE, configPlusOne.toString());
				lastTxnDate = configPlusOne;
			} else {
				lastTxnDate = getLocalDate(usageFees.get(delchnlTxn + LAST_TXN_DATE) + "", "yyyy-MM-dd");
			}
		} catch (ParseException e) {
			lastTxnDate = configPlusOne;
		}
		return lastTxnDate;
	}

	/**
	 * Getting the formatted assessmentDate
	 * 
	 * @param now,monthlyFeeCap
	 * @return LocalDate
	 * @author venkateshgaddam
	 */
	public LocalDate getConfigDate(LocalDate now, Map<String, Object> monthlyFeeCap) {
		LocalDate config = null;
		try {
			config = LocalDate.of(now.getYear(), now.getMonth(),
					Integer.parseInt(monthlyFeeCap.get(ValueObjectKeys.MONTHLY_FEE_CAP_ASSESSMENT_DATE) + ""));
		} catch (DateTimeException e) {
			logger.info("ConfigDate is beyond the current date" + e);
			config = LocalDate.of(now.getYear(), now.getMonth(), now.with(TemporalAdjusters.lastDayOfMonth())
				.getDayOfMonth());
		}
		return config;
	}

	/**
	 * Updating the usageFee
	 * 
	 * @param valueDto
	 * @param usageFees
	 */
	public void updateUsageFee(ValueDTO valueDto, Map<String, Object> usageFees) {
		// Map<String, Map<String, Object>> finalMap = new HashMap<>();
		// finalMap.put(ValueObjectKeys.FEES, usageFees);
		valueDto.setUsageFee(usageFees);
		valueDto.getValueObj()
			.put(ValueObjectKeys.UPDATE_USAGE_FEE, "YES");
	}

	/**
	 * Setting the calculated MonthlyFeeCap to the valueObject
	 */
	public void setCalMonthlyFeeCapToValObj(Map<String, String> valueObj, String delchnlTxn, ValueDTO valueDto,
			Map<String, Object> productDelchnlTxnFeeMap, LocalDate configPlusOne, double monthlyFeeCapAmt, LocalDate now,
			LocalDate config) {
		boolean isFeeCap = false;
		Map<String, String> monthlyFeeCapDtls = spilDao.getMonthlyFeeCapDetails(valueObj, java.sql.Date.valueOf(configPlusOne));
		if (CollectionUtils.isEmpty(monthlyFeeCapDtls)) {
			spilDao.newInsertMonthlyFeeCap(valueObj, java.sql.Date.valueOf(configPlusOne), monthlyFeeCapAmt);
		}
		double currFeeCapAmt = monthlyFeeCapDtls.get(ValueObjectKeys.FEE_ACCRUED) != null
				? Double.parseDouble(monthlyFeeCapDtls.get(ValueObjectKeys.FEE_ACCRUED))
				: 0.0;
		if (!(now.isBefore(config) || now.isAfter(configPlusOne))
				&& TransactionConstant.MSG_TYPE_POSITIVE.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
			if (currFeeCapAmt < monthlyFeeCapAmt) {
				// Calculating fee
				calculateFee(valueObj, productDelchnlTxnFeeMap, delchnlTxn);
				double calcMonthFeeAmt = monthlyFeeCapAmt - currFeeCapAmt;
				double prdTxnFee = valueObj.get(ValueObjectKeys.PROD_TXN_FEE_AMT) != null
						? Double.parseDouble(valueObj.get(ValueObjectKeys.PROD_TXN_FEE_AMT))
						: 0.0;
				if (calcMonthFeeAmt < prdTxnFee) {
					currFeeCapAmt += calcMonthFeeAmt;
					valueObj.put(ValueObjectKeys.PROD_TXN_FEE_AMT, String.valueOf(calcMonthFeeAmt));
				} else {
					currFeeCapAmt += prdTxnFee;
				}
				isFeeCap = true;
			} else {
				setDefaultTxnFeeDetails(valueObj);
			}
		} else {
			if (TransactionConstant.MSG_TYPE_POSITIVE.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
				// Calculating fee
				calculateFee(valueObj, productDelchnlTxnFeeMap, delchnlTxn);
				double prdTxnFee = valueObj.get(ValueObjectKeys.PROD_TXN_FEE_AMT) != null
						? Double.parseDouble(valueObj.get(ValueObjectKeys.PROD_TXN_FEE_AMT))
						: 0.0;
				currFeeCapAmt += prdTxnFee;
				isFeeCap = true;
			} else {
				double feeAmt = valueDto.getValueObj()
					.get(ValueObjectKeys.ORGNL_TRAN_FEE_AMNT) != null ? Double.parseDouble(
							valueDto.getValueObj()
								.get(ValueObjectKeys.ORGNL_TRAN_FEE_AMNT))
							: 0.0;
				if (currFeeCapAmt > 0) {
					currFeeCapAmt = currFeeCapAmt - feeAmt;
					isFeeCap = true;
				}
			}
		}
		if (isFeeCap) {
			valueObj.put(ValueObjectKeys.USAGE_FEE_PERIOD, configPlusOne.toString());
			valueObj.put(ValueObjectKeys.CURRENT_FEE_ACCRUED, Double.toString(currFeeCapAmt));
			valueObj.put(ValueObjectKeys.PRODUCT_MONTHLY_FEE_CAP, String.valueOf(monthlyFeeCapAmt));
		}
	}

}
