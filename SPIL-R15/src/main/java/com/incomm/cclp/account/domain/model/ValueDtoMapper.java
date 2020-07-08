package com.incomm.cclp.account.domain.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.ccl.common.util.Mapper;
import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.application.command.UpdateAccountPurseCommand;
import com.incomm.cclp.account.application.command.UpdatePurseStatusCommand;
import com.incomm.cclp.account.application.command.UpdateRolloverAccountPurseCommand;
import com.incomm.cclp.account.util.DateTimeFormatType;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dto.AccountPurseUsageDto;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.service.CardDetailsService;
import com.incomm.cclp.service.ProductService;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;

@Service
public class ValueDtoMapper {

	@Autowired
	private ProductService productService;

	@Autowired
	private CardDetailsService cardDetailsService;

	public ValueDTO createValueDto(Long productId, Long purseId, AccountPurseUsageDto accountPurseUsageDto) {

		ValueDTO valueDto = new ValueDTO();
		Map<String, String> valueObjectMap = new HashMap<>(50);
		valueDto.setValueObj(valueObjectMap);

		valueDto.setUsageFee(accountPurseUsageDto == null ? null : accountPurseUsageDto.getUsageFees());
		valueDto.setUsageLimit(accountPurseUsageDto == null ? null : accountPurseUsageDto.getUsageLimits());

		valueDto.setProductAttributes(productService.getProductAttributes(Long.toString(productId), Long.toString(purseId)));

		cardDetailsService.doPopulateSupportedPurseDtls(valueDto);

		this.populateTransactionDateTime(valueObjectMap);
		if (accountPurseUsageDto != null) {
			valueDto.getValueObj()
				.put(ValueObjectKeys.LAST_TXN_DATE,
						String.valueOf(accountPurseUsageDto.getLastTransactionDate() == null ? LocalDateTime.now()
								: accountPurseUsageDto.getLastTransactionDate()));
		} else {
			valueDto.getValueObj()
				.put(ValueObjectKeys.LAST_TXN_DATE, String.valueOf(LocalDateTime.now()));
		}

		return valueDto;
	}

	public void populateProductPurseInfo(ValueDTO valueDto, ProductPurse productPurse) {

		String purseTypeId = productPurse.getPurseType() == null ? ""
				: Integer.toString(productPurse.getPurseType()
					.getPurseTypeId());

		valueDto.getValueObj()
			.put(ValueObjectKeys.PURSE_TYPE_ID, purseTypeId);
		valueDto.getValueObj()
			.put(ValueObjectKeys.PURSE_ID, Long.toString(productPurse.getPurseId()));

		this.cardDetailsService.doPopulateSupportedPurseDtls(valueDto);

	}

	public void populateMessageTypeBeanInfo(Map<String, String> valueObjectMap, SpilStartupMsgTypeBean msgTypeBean) {

		valueObjectMap.put(ValueObjectKeys.MSGTYPE, msgTypeBean.getMsgType());
		valueObjectMap.put(ValueObjectKeys.DELIVERYCHNL, msgTypeBean.getDeliveryChannel());
		valueObjectMap.put(ValueObjectKeys.TRANS_CODE, msgTypeBean.getTxnCode());
		valueObjectMap.put(ValueObjectKeys.MEMBERNO, "000");
		valueObjectMap.put(ValueObjectKeys.PARTY_SUPPORTED, msgTypeBean.getPartySupported());
		valueObjectMap.put(ValueObjectKeys.AUTH_JAVA_CLASS_NAME, msgTypeBean.getAuthJavaClass());
		valueObjectMap.put(ValueObjectKeys.IS_FINANCIAL, msgTypeBean.getIsFinacial());
		valueObjectMap.put(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, msgTypeBean.getCreditDebitIndicator());
		valueObjectMap.put(ValueObjectKeys.SPIL_MSG_TYPE, msgTypeBean.getSpilMsgType());
		valueObjectMap.put(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME, msgTypeBean.getChannelShortName());
		valueObjectMap.put(ValueObjectKeys.PASSIVE_SUPPORT_FLAG, msgTypeBean.getPassiveSupported());
		valueObjectMap.put(ValueObjectKeys.TRANSACTIONDESC, msgTypeBean.getTransactionDesc());
		valueObjectMap.put(ValueObjectKeys.TRANSACTIONSHORTNAME, msgTypeBean.getTransactionShortName());

	}

	public void populateCardInfo(Map<String, String> valueObjectMap, CardEntity cardEntity) {
		valueObjectMap.put(ValueObjectKeys.PRODUCT_ID, Long.toString(cardEntity.getProductId()));

		valueObjectMap.put(ValueObjectKeys.CARD_ACCOUNT_ID, Long.toString(cardEntity.getAccountId()));
		valueObjectMap.put(ValueObjectKeys.CARD_NUM_HASH, cardEntity.getCardNumberHash());
		valueObjectMap.put(ValueObjectKeys.PRFL_ID, cardEntity.getProfileCode());
		valueObjectMap.put(ValueObjectKeys.CARD_NUMBER, cardEntity.getCardNumber());
		valueObjectMap.put(ValueObjectKeys.CUSTOMER_CODE, String.valueOf(cardEntity.getCustomerCode()));
		valueObjectMap.put(ValueObjectKeys.CARD_SERIAL_NUMBER, cardEntity.getSerialNumber());
		valueObjectMap.put(ValueObjectKeys.PROXY_NUMBER, cardEntity.getProxyNumber());
		valueObjectMap.put(ValueObjectKeys.CARD_CARDSTAT, cardEntity.getCardStatus()
			.getStatusCode());

		valueObjectMap.put(ValueObjectKeys.CARD_EXPDATE, cardEntity.getExpiryDate());
		valueObjectMap.put(ValueObjectKeys.CARD_ACTIVATION_DATE,
				DateTimeUtil.convert(cardEntity.getActivationDate(), DateTimeFormatType.YYYY_MM_DD_WITH_HYPHEN));
		valueObjectMap.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, Mapper.mapToYN(cardEntity.getFirstTimeTopUp()));
		valueObjectMap.put(ValueObjectKeys.OLD_CARD_STATUS, cardEntity.getOldCardStatus()
			.isPresent()
					? cardEntity.getOldCardStatus()
						.get()
						.getStatusCode()
					: null);

		valueObjectMap.put(ValueObjectKeys.LAST_TXN_DATE,
				DateTimeUtil.convert(cardEntity.getLastTransactionDate(), DateTimeFormatType.YYYY_MM_DD_WITH_HYPHEN));
		valueObjectMap.put(ValueObjectKeys.IS_REDEEMED, cardEntity.getIsRedeemed());
		valueObjectMap.put(ValueObjectKeys.ISSUER_ID, String.valueOf(cardEntity.getIssuerId()));
		valueObjectMap.put(ValueObjectKeys.PARTNER_ID, String.valueOf(cardEntity.getPartnerId()));
		valueObjectMap.put(ValueObjectKeys.REPL_FLAG, String.valueOf(cardEntity.getReplFlag()));
		valueObjectMap.put(ValueObjectKeys.CARD_NUM_ENCR, cardEntity.getCardNumberEncrypted());
		valueObjectMap.put(ValueObjectKeys.LAST_4DIGIT, cardEntity.getLastFourDigit());
		valueObjectMap.put(ValueObjectKeys.DIGITAL_PIN, cardEntity.getDigitalPin());
		valueObjectMap.put(ValueObjectKeys.DB_SYSDATE, String.valueOf(cardEntity.getDbSysDate()));

	}

	public void populateTransactionInfo(Map<String, String> valueObjectMap, UpdateAccountPurseCommand command) {

		this.populateTransactionInfo(valueObjectMap, command.getTransactionInfo());

		valueObjectMap.put(ValueObjectKeys.PURAUTHREQ_SKUCODE, command.getSkuCode());

		valueObjectMap.put(ValueObjectKeys.SPIL_TRAN_AMT, command.getTransactionAmount()
			.toString());
		valueObjectMap.put(ValueObjectKeys.PURAUTHREQ_TRAN_AMT, command.getTransactionAmount()
			.toString());
		valueObjectMap.put(ValueObjectKeys.PURSE_NAME, command.getPurseName());

		valueObjectMap.put(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY, command.getCurrency());

		valueObjectMap.put(ValueObjectKeys.SKIP_CURRENCY_CHECK_IF_NULL, "true");

		valueObjectMap.put(ValueObjectKeys.ACCOUNT_PURSE_ID, command.getAccountPurseId() == null ? null
				: command.getAccountPurseId()
					.toString());
		valueObjectMap.put(ValueObjectKeys.EFFECTIVE_DATE, command.getEffectiveDate() == null ? null
				: command.getEffectiveDate()
					.toString());
		valueObjectMap.put(ValueObjectKeys.EXPIRY_DATE, command.getExpiryDate() == null ? null
				: command.getExpiryDate()
					.toString());
		valueObjectMap.put(ValueObjectKeys.CURRENCY, command.getCurrency());
		valueObjectMap.put(ValueObjectKeys.ORIGINAL_MSGTYPE, command.getActionType()
			.getTransactionShortName());

	}

	public void populateTransactionInfo(Map<String, String> valueObjectMap, TransactionInfo transactionInfo) {

		valueObjectMap.put(ValueObjectKeys.DELIVERYCHNL, transactionInfo.getDeliveryChannelType()
			.getChannelCode());
		valueObjectMap.put(ValueObjectKeys.SPNUMBER, transactionInfo.getSpNumber());

		valueObjectMap.put(ValueObjectKeys.SPIL_STORE_ID, transactionInfo.getStoreId());
		valueObjectMap.put(ValueObjectKeys.SPIL_TERM_ID, transactionInfo.getTerminalId());

		valueObjectMap.put(ValueObjectKeys.MDM_ID, transactionInfo.getMdmId());
		valueObjectMap.put(ValueObjectKeys.INCOM_REF_NUMBER, transactionInfo.getCorrelationId());

	}

	public void populateTransactionInfo(Map<String, String> valueObjectMap, UpdatePurseStatusCommand command) {
		this.populateTransactionInfo(valueObjectMap, command.getTransactionInfo());

		valueObjectMap.put(ValueObjectKeys.PURSE_NAME, command.getPurseName());
		valueObjectMap.put(ValueObjectKeys.ORIGINAL_MSGTYPE, UpdatePurseStatusCommand.TRANSACTION_SHORT_CODE);
		valueObjectMap.put(ValueObjectKeys.SPIL_TRAN_AMT, "0");

	}

	public void populateTransactionDateTime(Map<String, String> valueObjectMap) {
		LocalDateTime currentDateTime = DateTimeUtil.currentDateTime();

		valueObjectMap.put(ValueObjectKeys.SPIL_TRAN_DATE, DateTimeUtil.convert(currentDateTime, DateTimeFormatType.YYYYMMDD));
		valueObjectMap.put(ValueObjectKeys.SPIL_TRAN_TIME, DateTimeUtil.convert(currentDateTime, DateTimeFormatType.HHMMSS));

	}

	public void populateTransactionInfo(Map<String, String> valueObjectMap, UpdateRolloverAccountPurseCommand command) {
		this.populateTransactionInfo(valueObjectMap, command.getTransactionInfo());

		valueObjectMap.put(ValueObjectKeys.PURAUTHREQ_SKUCODE, command.getSkuCode());

		valueObjectMap.put(ValueObjectKeys.SPIL_TRAN_AMT, command.getTransactionAmount()
			.toString());
		valueObjectMap.put(ValueObjectKeys.PURAUTHREQ_TRAN_AMT, command.getTransactionAmount()
			.toString());
		valueObjectMap.put(ValueObjectKeys.PURSE_NAME, command.getPurseName());

		valueObjectMap.put(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY, command.getCurrency());

		valueObjectMap.put(ValueObjectKeys.SKIP_CURRENCY_CHECK_IF_NULL, "true");

		valueObjectMap.put(ValueObjectKeys.ACCOUNT_PURSE_ID, command.getAccountPurseId() == null ? null
				: command.getAccountPurseId()
					.toString());
		valueObjectMap.put(ValueObjectKeys.EFFECTIVE_DATE, command.getEffectiveDate() == null ? null
				: command.getEffectiveDate()
					.toString());
		valueObjectMap.put(ValueObjectKeys.EXPIRY_DATE, command.getExpiryDate() == null ? null
				: command.getExpiryDate()
					.toString());
		valueObjectMap.put(ValueObjectKeys.CURRENCY, command.getCurrency());
		valueObjectMap.put(ValueObjectKeys.ORIGINAL_MSGTYPE, command.getActionType()
			.getTransactionShortName());

	}

}
