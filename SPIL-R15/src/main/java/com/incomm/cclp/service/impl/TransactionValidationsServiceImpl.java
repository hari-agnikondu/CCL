package com.incomm.cclp.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.incomm.cclp.service.FeeCalculationService;
import com.incomm.cclp.service.TransactionValidationsService;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;
import com.incomm.cclp.util.Util;

@Service
public class TransactionValidationsServiceImpl implements TransactionValidationsService {

	private static final Logger logger = LogManager.getLogger(TransactionValidationsServiceImpl.class);

	@Autowired
	CommonValidationsDAO commondao;

	@Autowired
	FeeCalculationService feeCalculationService;

	private static final String MONTHLY_FEE_CAP_AVAIL = "_monthlyFeeCapAvail";

	@Override
	public void validateProductDenomCheck(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		String denominationType = "";
		boolean isAmtMatched = false;
		BigDecimal txnAmount = new BigDecimal(valueDto.getValueObj()
			.get(ValueObjectKeys.SPIL_TRAN_AMT));
		String pan = valueDto.getValueObj()
			.get(ValueObjectKeys.CARD_NUM_HASH);
		Map<String, Object> productAttributes = valueDto.getProductAttributes()
			.get(ValueObjectKeys.PRODUCT);
		String productType = productAttributes.get(ValueObjectKeys.PRODUCT_TYPE)
			.toString();
		String b2bproductfunding = "";
		String b2bsourcefunding = "";
		BigDecimal denomAmt = null;
		String formFactor = productAttributes.get(ValueObjectKeys.FORM_FACTOR)
			.toString();
		if (valueDto.getAccountPurseDto() == null) {
			logger.debug("Denomination check for default purse");
			if ("Digital".equalsIgnoreCase(formFactor) || "Physical".equalsIgnoreCase(formFactor)) {
				List<Map<String, Object>> productDetail = commondao.getproductfunding(pan);
				logger.debug("productDetails" + productDetail);
				for (Object productDetails : productDetail) {
					Map m = (Map) productDetails;
					String merchantID = (String) m.get(ValueObjectKeys.MERCHANT_ID);
					String locationID = (String) m.get(ValueObjectKeys.LOCATION_ID);
					valueDto.getValueObj()
						.put(ValueObjectKeys.MERCHANT_ID, merchantID);
					valueDto.getValueObj()
						.put(ValueObjectKeys.LOCATION_ID, locationID);
				}
			}
			if (productType.equalsIgnoreCase("B2B")) {
				List<Map<String, Object>> productfund = commondao.getproductfunding(pan);
				logger.debug("productfund" + productfund);
				for (Object fundingdetails : productfund) {
					Map m = (Map) fundingdetails;

					b2bproductfunding = (String) m.get(ValueObjectKeys.B2B_PRODUCT_FUNDING);
					b2bsourcefunding = (String) m.get(ValueObjectKeys.B2B_SOURCE_FUNDING);
					denomAmt = new BigDecimal(
							(String) m.get(ValueObjectKeys.ORDER_DENOM) != null ? (String) m.get(ValueObjectKeys.ORDER_DENOM) : "0.00");

				}
			}
			String firstTimeTopupFlag = Objects.isNull(valueDto.getValueObj()
				.get(ValueObjectKeys.FIRST_TIMETOPUP_FLAG)) ? "N"
						: valueDto.getValueObj()
							.get(ValueObjectKeys.FIRST_TIMETOPUP_FLAG);
			if (productType.equalsIgnoreCase("Retail") || (productType.equalsIgnoreCase("B2B") && !Objects.isNull(b2bsourcefunding)
					&& !Objects.isNull(b2bproductfunding) && b2bproductfunding.equalsIgnoreCase("CARD_ACTIVATION")
					&& b2bsourcefunding.equalsIgnoreCase("CARD_ACTIVATION_AMOUNT"))) {

				if (firstTimeTopupFlag.equalsIgnoreCase("N") && !CollectionUtils.isEmpty(productAttributes)
						&& productAttributes.containsKey(ValueObjectKeys.DENOMINATION_TYPE)
						&& !Objects.isNull(productAttributes.get(ValueObjectKeys.DENOMINATION_TYPE))) {

					denominationType = productAttributes.get(ValueObjectKeys.DENOMINATION_TYPE)
						.toString();

					if (denominationType.equalsIgnoreCase("Fixed")) {

						BigDecimal fixedValue = new BigDecimal(productAttributes.get("denomFixed")
							.toString());
						if (txnAmount.compareTo(fixedValue) == 0) {
							isAmtMatched = true;
						}

					} else if (denominationType.equalsIgnoreCase("Variable")) {
						BigDecimal minValue = new BigDecimal(productAttributes.get("denomVarMin")
							.toString());
						BigDecimal maxValue = new BigDecimal(productAttributes.get("denomVarMax")
							.toString());

						int res1;
						res1 = txnAmount.compareTo(minValue);
						int res2;
						res2 = txnAmount.compareTo(maxValue);

						if (res1 >= 0 && res2 <= 0) {
							isAmtMatched = true;
						}

					} else if (denominationType.equalsIgnoreCase("Select")) {
						String selAmount = productAttributes.get("denomSelect")
							.toString();
						String[] selectedAmounts = selAmount.split(",");
						List<String> listAmount = new ArrayList<>(Arrays.asList(selectedAmounts));

						for (String strAmount : listAmount) {
							BigDecimal amountVal = new BigDecimal(strAmount);
							if (txnAmount.compareTo(amountVal) == 0) {
								isAmtMatched = true;
							}
						}

					}

					if (isAmtMatched) {
						logger.debug("Denomination check passed successfully");
					} else {
						logger.error("Invalid input amount");
						throw new ServiceException(SpilExceptionMessages.INVALID_AMOUNT, ResponseCodes.INVALID_AMOUNT);
					}
				}
			} else if (productType.equalsIgnoreCase("B2B") && !Objects.isNull(b2bsourcefunding) && !Objects.isNull(b2bproductfunding)
					&& b2bproductfunding.equalsIgnoreCase("CARD_ACTIVATION") && b2bsourcefunding.equalsIgnoreCase("ORDER_AMOUNT")) {

				if (txnAmount.compareTo(denomAmt) == 0) {
					logger.debug("Order Amount Denomination check passed successfully");
				} else {
					logger.error("Invalid input amount");
					throw new ServiceException(SpilExceptionMessages.INVALID_AMOUNT, ResponseCodes.INVALID_AMOUNT);
				}

			} else if (productType.equalsIgnoreCase("B2B") && !Objects.isNull(b2bproductfunding)
					&& b2bproductfunding.equalsIgnoreCase("ORDER_FULFILLMENT")) {

				valueDto.getValueObj()
					.put(ValueObjectKeys.B2B_PRODUCT_FUNDING, b2bproductfunding);
			} else {
				logger.error("Invalid input amount");
				throw new ServiceException(SpilExceptionMessages.INVALID_AMOUNT, ResponseCodes.INVALID_AMOUNT);

			}
		}

		logger.debug(GeneralConstants.EXIT);
	}

	@Override
	public void cardAlreadyRedeemedCheck(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		String isRedeemed = valueDto.getValueObj()
			.get(ValueObjectKeys.IS_REDEEMED);
		String transCode = valueDto.getValueObj()
			.get(ValueObjectKeys.TRANS_CODE);

		if (isRedeemed.equalsIgnoreCase("N") && transCode.equalsIgnoreCase("010")) {
			logger.error("Card already not redeemed, So Store Credit Transaction was failed");
			throw new ServiceException(SpilExceptionMessages.CARD_NOT_REDEEMED, ResponseCodes.INVALID_REQUEST);
		} else if (isRedeemed.equalsIgnoreCase("Y") && transCode.equalsIgnoreCase("003")) {
			logger.error("Card already redeemed, so not able to Deactivation");
			throw new ServiceException(SpilExceptionMessages.CARD_IS_REDEEMED, ResponseCodes.CARD_IS_REDEEMED);
		} else {
			logger.debug("Card already redeemed check successfully passed");
		}

		logger.debug(GeneralConstants.EXIT);
	}

	@Override
	public void cardAlreadyDeactivatedCheck(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		String cardStatus = valueDto.getValueObj()
			.get(ValueObjectKeys.CARD_CARDSTAT);
		String transactionCode = valueDto.getValueObj()
			.get(ValueObjectKeys.TRANS_CODE);
		String msgType = valueDto.getValueObj()
			.get(ValueObjectKeys.MSGTYPE);

		if (transactionCode.equalsIgnoreCase("003") && msgType.equals("0200") && cardStatus.equals("0")) {
			logger.error("Card already Deactivated");
			throw new ServiceException(SpilExceptionMessages.INACTIVE_CARD, ResponseCodes.INACTIVE_CARD);
		}

		logger.debug(GeneralConstants.EXIT);

	}

	@Override
	public void reloadApplicableCheck(ValueDTO valueDto) throws ServiceException {

		logger.debug(GeneralConstants.ENTER);

		Map<String, Object> productAttributes = null;
		if (!CollectionUtils.isEmpty(valueDto.getProductAttributes())) {

			productAttributes = valueDto.getProductAttributes()
				.get(ValueObjectKeys.PRODUCT);

			if (!CollectionUtils.isEmpty(productAttributes) && productAttributes.containsKey(ValueObjectKeys.RELOADABLE_FLAG)
					&& !Objects.isNull(productAttributes.get(ValueObjectKeys.RELOADABLE_FLAG))
					&& productAttributes.get(ValueObjectKeys.RELOADABLE_FLAG)
						.equals(ValueObjectKeys.DISABLE)) {

				logger.error("Reloadable is not applicable for this transaction");
				throw new ServiceException(SpilExceptionMessages.RELOADABLE_CHECK, ResponseCodes.RELOADABLE_CHECK);

			} else {
				logger.debug("Reloadable Flag check is Successfull ");
			}
		}
		logger.debug(GeneralConstants.EXIT);
	}

	/**
	 * partialAuthCheck method provide partial Auth functionality based on screen configuration and fee will be
	 * calculated for the available balance.
	 * 
	 * @author venkateshgaddam
	 */
	public void partialAuthCheck(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		if (TransactionConstant.TRUE.equalsIgnoreCase(valueDto.getValueObj()
			.get(ValueObjectKeys.PARTIAL_AUTH_IND)) && valueDto.getValueObj()
				.containsKey("feeCheckFlag")) {
			double avilAmt = valueDto.getValueObj()
				.get(ValueObjectKeys.AVAIL_BALANCE) != null ? Double.parseDouble(
						valueDto.getValueObj()
							.get(ValueObjectKeys.AVAIL_BALANCE))
						: 0;
			double txnFee = 0D;
			double tranAmt = 0D;
			double delayedAmt = 0D;
			if (!Util.isEmpty(valueDto.getValueObj()
				.get(ValueObjectKeys.SPIL_TRAN_AMT))) {
				tranAmt = Double.valueOf(valueDto.getValueObj()
					.get(ValueObjectKeys.SPIL_TRAN_AMT));
			}
			if (!Util.isEmpty(valueDto.getValueObj()
				.get(ValueObjectKeys.PROD_TXN_FEE_AMT))) {
				txnFee = Double.valueOf(valueDto.getValueObj()
					.get(ValueObjectKeys.PROD_TXN_FEE_AMT));
			}
			if (!Util.isEmpty(valueDto.getValueObj()
				.get(ValueObjectKeys.REDEMPTION_DELAY_TRAN_AMOUNT))) {
				delayedAmt = Double.valueOf(valueDto.getValueObj()
					.get(ValueObjectKeys.REDEMPTION_DELAY_TRAN_AMOUNT));
			}
			avilAmt = avilAmt - delayedAmt;

			if (tranAmt + txnFee > avilAmt) {
				String delchnlTxn = valueDto.getValueObj()
					.get(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME) + "_"
						+ valueDto.getValueObj()
							.get(ValueObjectKeys.ORGINAL_TXN_SHORT_NAME);
				Map<String, Map<String, Object>> productAttributesMap = valueDto.getProductAttributes();
				Map<String, Object> transactionFee = productAttributesMap.get(ValueObjectKeys.TRANSACTION_FEES);
				Map<String, Object> productDelchnlTxnFeeMap = new HashMap<>();
				transactionFee.entrySet()
					.stream()
					.filter(p -> p.getKey()
						.startsWith(delchnlTxn))
					.forEach(map -> productDelchnlTxnFeeMap.put(map.getKey(), map.getValue()));
				valueDto.getValueObj()
					.put(ValueObjectKeys.PARTIAL_AUTH_INDICATOR_AVAL_FLAG, TransactionConstant.TRUE);

				if (!Util.isEmpty(productDelchnlTxnFeeMap.get(delchnlTxn + MONTHLY_FEE_CAP_AVAIL) + "")
						&& "true".equalsIgnoreCase(productDelchnlTxnFeeMap.get(delchnlTxn + MONTHLY_FEE_CAP_AVAIL) + "")) {
					feeCalculationService.calcMonthlyFeeCap(valueDto.getValueObj(),
							productAttributesMap.get(ValueObjectKeys.MONTHLY_FEE_CAP), delchnlTxn, valueDto, productDelchnlTxnFeeMap);
				} else {
					feeCalculationService.calculateFee(valueDto.getValueObj(), productDelchnlTxnFeeMap, delchnlTxn);
				}
			}
		}
		logger.debug(GeneralConstants.EXIT);
	}
}
