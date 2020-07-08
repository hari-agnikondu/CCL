package com.incomm.cclp.account.domain.model;

import static com.incomm.cclp.account.util.CodeUtil.hasOneElementOnly;
import static com.incomm.cclp.account.util.CodeUtil.isEmpty;
import static com.incomm.cclp.account.util.CodeUtil.mapToBigInteger;
import static com.incomm.cclp.account.util.CodeUtil.trimToLength;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.incomm.cclp.account.application.command.AccountTransactionCommand;
import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.application.command.UpdateAccountPurseCommand;
import com.incomm.cclp.account.application.command.UpdatePurseStatusCommand;
import com.incomm.cclp.account.application.command.UpdateRolloverAccountPurseCommand;
import com.incomm.cclp.account.util.DateTimeFormatType;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.domain.TransactionLog;
import com.incomm.cclp.domain.TransactionLog.Builder;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;
import com.incomm.cclp.transaction.constants.GeneralConstants;

public class TransactionLogBuilderHelper {

	private final TransactionLog.Builder builder;

	public static TransactionLog getFailedTransaction(UpdatePurseStatusCommand command, TransactionResponseInfo responseInfo) {

		return TransactionLogBuilderHelper.from(null)
			.cardEntity(responseInfo.getCardEntity())
			.command(command)
			.response(responseInfo.getResponseCode(), responseInfo.getResponseMessage(), "F")
			.build();
	}

	private TransactionLogBuilderHelper(Builder builder) {
		super();
		this.builder = builder;

		this.populateDefaultFields();
	}

	public static TransactionLogBuilderHelper from(BigDecimal transactionId) {
		return new TransactionLogBuilderHelper(TransactionLog.builder()
			.transactionSqid(transactionId));
	}

	public static TransactionLogBuilderHelper from(BigDecimal transactionId, String authorizationId) {
		return new TransactionLogBuilderHelper(TransactionLog.builder()
			.transactionSqid(transactionId)
			.authId(authorizationId));
	}

	public TransactionLog build() {
		return builder.build();
	}

	public TransactionLogBuilderHelper response(String responseCode, String respsonseMessage, String transactionStatus) {

		this.builder.responseId(responseCode)
			.errorMsg(respsonseMessage)
			.transactionStatus(transactionStatus);
		return this;
	}

	public TransactionLogBuilderHelper requestXml(String requestXml) {
		this.builder.requestXml(requestXml);
		return this;
	}

	public TransactionLogBuilderHelper accountAggregate(AccountAggregate aggregate) {

		cardEntity(aggregate.getCardEntity());
		populateSpilStartupMsgTypeBean(aggregate.getTransactionConfiguration());
		return this;
	}

	public TransactionLogBuilderHelper command(UpdateAccountPurseCommand command) {
		populateTransactionInfo(command.getTransactionInfo());

		builder.purseTxnAmount(command.getTransactionAmount()
			.doubleValue())
			.purseName(command.getPurseName())
			.tranCurr(command.getCurrency())
			.orgnlTranCurr(command.getCurrency())
			.accountPurseId(command.getAccountPurseId());

		return this;
	}

	public TransactionLogBuilderHelper command(AccountTransactionCommand command) {
		populateTransactionInfo(command.getTransactionInfo());
		TransactionInfo transactionInfo = command.getTransactionInfo();

		builder.transactionAmount(command.getTransactionAmount()
			.doubleValue())
			.purseTxnAmount(command.getTransactionAmount()
				.doubleValue())
			.purseName(command.getPurseName()
				.orElse(null))
			.tranCurr(command.getTransactionCurrency()
				.orElse(null))
			.orgnlTranCurr(command.getTransactionCurrency()
				.orElse(null))
			.orgnlTranAmt(command.getTransactionAmount()
				.doubleValue())
			.accountNumber(command.getValueDTO()
				.getValueObj()
				.get(ValueObjectKeys.ACCOUNT_NUMBER))
			.isFinancial(command.getValueDTO()
				.getValueObj()
				.get(ValueObjectKeys.IS_FINANCIAL))
			.transactionCode(transactionInfo.getTransactionType()
				.getTransactionCode())
			.transactionDesc(transactionInfo.getTransactionType()
				.getTransactionDescription())
			.crDrFlag(transactionInfo.getTransactionType()
				.getOperationType()
				.getFlag());

		return this;
	}

	public TransactionLogBuilderHelper cardEntity(CardEntity cardEntity) {

		builder.accountId(mapToBigInteger(cardEntity.getAccountId()))
			.accountNumber(cardEntity.getAccountNumber())
			.productId(mapToBigInteger(cardEntity.getProductId()))
			.partnerId(mapToBigInteger(cardEntity.getPartnerId()))
			.customerId(mapToBigInteger(cardEntity.getCustomerCode()))
			.issuerId(mapToBigInteger(cardEntity.getIssuerId()))
			.cardNumber(cardEntity.getCardNumberHash())
			.customerCardNbrEncr(cardEntity.getCardNumberEncrypted())
			.proxyNumber(cardEntity.getProxyNumber())
			.expirationDate(cardEntity.getExpiryDate())
			.cardStatus(cardEntity.getCardStatus()
				.getStatusCode());

		return this;
	}

	public TransactionLogBuilderHelper command(UpdatePurseStatusCommand command) {
		populateTransactionInfo(command.getTransactionInfo());

		builder.purseName(command.getPurseName());

		return this;
	}

	public TransactionLogBuilderHelper accountPurseUpdate(AccountPurseUpdateNew accountPurseUpdate) {
		this.builder.purseId(mapToBigInteger(accountPurseUpdate.getAccountPurseKey()
			.getPurseId()))
			.accountPurseId(accountPurseUpdate.getAccountPurseKey()
				.getAccountPurseId())
			.openingLedgerBalance(accountPurseUpdate.getPreviousLedgerBalance()
				.doubleValue())
			.openingAvailableBalance(accountPurseUpdate.getPreviousAvailableBalance()
				.doubleValue())
			.ledgerBalance(accountPurseUpdate.getNewLedgerBalance()
				.doubleValue())
			.accountBalance(accountPurseUpdate.getNewAvailableBalance()
				.doubleValue());

		return this;
	}

	public TransactionLogBuilderHelper accountPurseUpdates(List<AccountPurseUpdateNew> accountPurseUpdates) {

		if (isEmpty(accountPurseUpdates)) {
			return this;
		}

		if (hasOneElementOnly(accountPurseUpdates)) {
			return this.accountPurseUpdate(accountPurseUpdates.get(0));
		}

		this.builder.openingLedgerBalance(0)
			.openingAvailableBalance(0)
			.ledgerBalance(0)
			.accountBalance(0);

		return this;
	}

	public TransactionLogBuilderHelper authorizedAmount(BigDecimal authAmount) {
		this.builder.authAmount(authAmount.doubleValue());
		return this;
	}

	public TransactionLogBuilderHelper transactionFee(BigDecimal transactionFee) {
		this.builder.tranfeeAmount(transactionFee == null ? 0 : transactionFee.doubleValue());
		return this;
	}

	public TransactionLogBuilderHelper businessDate(String businessDate) {
		builder.businessDate(businessDate);
		return this;
	}

	public TransactionLogBuilderHelper accountPurseUpdate(AccountPurseUpdate accountPurseUpdate, BigDecimal transactionFee) {
		this.builder.purseId(mapToBigInteger(accountPurseUpdate.getAccountPurseKey()
			.getPurseId()))
			.accountPurseId(accountPurseUpdate.getAccountPurseKey()
				.getAccountPurseId())
			.openingLedgerBalance(accountPurseUpdate.getPreviousLedgerBalance()
				.doubleValue())
			.openingAvailableBalance(accountPurseUpdate.getPreviousAvailableBalance()
				.doubleValue())
			.authAmount(accountPurseUpdate.getAuthorizedAmount()
				.doubleValue())
			.ledgerBalance(accountPurseUpdate.getNewLedgerBalance()
				.doubleValue())
			.accountBalance(accountPurseUpdate.getNewAvailableBalance()
				.doubleValue())
			.tranfeeAmount(transactionFee.doubleValue())
			.transactionAmount(accountPurseUpdate.getTransactionAmount()
				.doubleValue());

		return this;
	}

	public TransactionLogBuilderHelper purseUpdate(PurseUpdate purseUpdate, BigDecimal transactionFee) {
		this.builder.remark(purseUpdate.getNewStatus()
			.name())
			.tranfeeAmount(transactionFee.doubleValue());

		return this;
	}

	public TransactionLogBuilderHelper purseUpdateTransactionInfo(PurseUpdateTransactionInfo previousTransactionInfo) {
		builder.purseName(previousTransactionInfo.getPurseName())
			.accountPurseId(previousTransactionInfo.getAccountPurseId())
			.purseTxnAmount(previousTransactionInfo.getTransactionAmount()
				.doubleValue())
			.skuCode(previousTransactionInfo.getSkuCode())
			.purseCurrency(previousTransactionInfo.getCurrency());
		return this;
	}

	private void populateSpilStartupMsgTypeBean(SpilStartupMsgTypeBean msgTypeBean) {
		builder.transactionCode(msgTypeBean.getTxnCode())
			.transactionDesc(msgTypeBean.getTransactionDesc())
			.isFinancial(msgTypeBean.getIsFinacial())
			.crDrFlag(msgTypeBean.getCreditDebitIndicator());
	}

	private void populateTransactionInfo(TransactionInfo transactionInfo) {
		builder.rrn(transactionInfo.getCorrelationId())
			.correlationId(trimToLength(transactionInfo.getCorrelationId(), 20))
			.deliveryChannel(transactionInfo.getDeliveryChannelType()
				.getChannelCode())
			.msgType(transactionInfo.getMessageType()
				.getMessageTypeCode())
			.tranReverseFlag(transactionInfo.getMessageType() == MessageType.ORIGINAL ? GeneralConstants.NO : GeneralConstants.YES)
			.spilUpc(transactionInfo.getUpc())
			.mdmId(transactionInfo.getMdmId())
			.terminalId(transactionInfo.getTerminalId())
			.storeId(transactionInfo.getStoreId());
	}

	private void populateDefaultFields() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		builder.insTimeStamp(DateTimeUtil.map(currentDateTime))
			.lastUpdDate(DateTimeUtil.map(currentDateTime))
			.insDate(DateTimeUtil.map(currentDateTime))
			.transactionDate(DateTimeUtil.convert(currentDateTime, DateTimeFormatType.YYYYMMDD))
			.transactionTime(DateTimeUtil.convert(currentDateTime, DateTimeFormatType.HHMMSS));

	}

	public TransactionLogBuilderHelper businessDateField(String businessDate) {
		this.builder.businessDate(businessDate);
		return this;
	}

	public TransactionLogBuilderHelper command(UpdateRolloverAccountPurseCommand command) {
		populateTransactionInfo(command.getTransactionInfo());

		builder.transactionAmount(command.getTransactionAmount()
			.doubleValue())
			.purseTxnAmount(command.getTransactionAmount()
				.doubleValue())
			.purseName(command.getPurseName())
			.tranCurr(command.getCurrency())
			.orgnlTranCurr(command.getCurrency())
			.accountPurseId(command.getAccountPurseId());

		return this;
	}

}
