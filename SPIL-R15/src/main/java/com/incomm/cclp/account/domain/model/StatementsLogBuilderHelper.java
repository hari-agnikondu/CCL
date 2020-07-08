package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.incomm.cclp.account.application.command.AccountTransactionCommand;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.domain.StatementsLog;
import com.incomm.cclp.domain.StatementsLogPK;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.constants.GeneralConstants;

public class StatementsLogBuilderHelper {
	private StatementsLog.Builder builder;

	private StatementsLogBuilderHelper() {
		this.builder = StatementsLog.builder();
	}

	public StatementsLog build() {
		return builder.build();
	}

	public static StatementsLogBuilderHelper from() {
		return new StatementsLogBuilderHelper();
	}

	public StatementsLogBuilderHelper primaryKey(BigDecimal transactionLogId, String authSeqId, BigDecimal statementLogId) {
		StatementsLogPK statementsLogPK = new StatementsLogPK();
		statementsLogPK.setRecordSeq(statementLogId.toBigInteger());
		statementsLogPK.setTransactionSqid(transactionLogId);
		builder.statementsLogPK(statementsLogPK)
			.authId(authSeqId);
		return this;

	}

	public StatementsLogBuilderHelper statementLog(LedgerEntry ledgerEntry, AccountTransactionCommand command) {
		String feeFlag = GeneralConstants.NO;
		String transactionDescription = command.getTransactionInfo()
			.getTransactionType()
			.getTransactionDescription();
		if (ledgerEntry.getLedgerEntryType() == LedgerEntryType.TRANSACTION_AMOUNT) {
			feeFlag = GeneralConstants.NO;
		} else if (ledgerEntry.getLedgerEntryType() == LedgerEntryType.FLAT_FEE) {
			feeFlag = GeneralConstants.YES;
			transactionDescription = transactionDescription + " Fee";
		} else if (ledgerEntry.getLedgerEntryType() == LedgerEntryType.PERCENT_FEE) {
			feeFlag = GeneralConstants.YES;
			transactionDescription = transactionDescription + " Percent Fee";
		}

		builder.openingBalance(ledgerEntry.getPreviousLedgerBalance()
			.doubleValue())
			.closingBalance(ledgerEntry.getNewLedgerBalance()
				.doubleValue())
			.transactionAmount(ledgerEntry.getTransactionAmount()
				.doubleValue())
			.feeFlag(feeFlag)
			.creditDebitFlag(ledgerEntry.getOperationType()
				.getFlag())
			.merchantCity(command.getValueDTO()
				.getValueObj()
				.get(ValueObjectKeys.SPIL_CITY))
			.merchantState(command.getValueDTO()
				.getValueObj()
				.get(ValueObjectKeys.SPIL_STATE))
			.accountPurseId(ledgerEntry.getAccountPurseKey()
				.getAccountPurseId())
			.availOpenBal(ledgerEntry.getPreviousAvailableBalance()
				.doubleValue())
			.availcloseBal(ledgerEntry.getNewAvailableBalance()
				.doubleValue())
			.transactionNarration(transactionDescription);
		return this;
	}

	public StatementsLogBuilderHelper accountTransactionCommand(AccountTransactionCommand command) {
		createCommonData(command);
		createMerchantData(command);

		builder.transactionAmount(command.getTransactionAmount()
			.doubleValue())
			.transactionCode(command.getValueDTO()
				.getValueObj()
				.get(ValueObjectKeys.TRANS_CODE))
			.feeFlag(command.getValueDTO()
				.getValueObj()
				.get(ValueObjectKeys.FREE_FEE_FLAG))
			.deliveryChannel(command.getTransactionInfo()
				.getDeliveryChannelType()
				.getChannelCode())
			.storeId(command.getTransactionInfo()
				.getStoreId());
		return this;

	}

	private StatementsLogBuilderHelper createCommonData(AccountTransactionCommand command) {
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yy");
		String finalString = newFormat.format(new Date());
		long time = System.currentTimeMillis();
		builder.lastUpdDate(finalString)
			.insDate(finalString)
			.insTimeStamp(new Timestamp(time))
			.transactionDate(command.getValueDTO()
				.getValueObj()
				.get(ValueObjectKeys.SPIL_TRAN_DATE))
			.transactionTime(command.getValueDTO()
				.getValueObj()
				.get(ValueObjectKeys.SPIL_TRAN_TIME))
			.rrn(command.getValueDTO()
				.getValueObj()
				.get(ValueObjectKeys.INCOM_REF_NUMBER))
			.productId(new BigInteger(command.getValueDTO()
				.getValueObj()
				.get(ValueObjectKeys.SPIL_PROD_ID)))
			.businessDate(cutOverTime(command.getValueDTO()
				.getProductAttributes()));
		return this;
	}

	private void createMerchantData(AccountTransactionCommand command) {
		builder.merchantName(command.getValueDTO()
			.getValueObj()
			.get(ValueObjectKeys.SPIL_MERCHANT_NAME))
			.merchantCity(command.getValueDTO()
				.getValueObj()
				.get(ValueObjectKeys.MERCHANT_CITY));
	}

	public StatementsLogBuilderHelper cardEntity(CardEntity cardEntity) {
		builder.cardNumHash(cardEntity.getCardNumberHash())
			.cardNumEncr(cardEntity.getCardNumberEncrypted())
			.cardLast4digit(cardEntity.getLastFourDigit());
		return this;
	}

	public StatementsLogBuilderHelper accountPurseKey(AccountPurseKey accountPurseKey) {

		builder.purseId(BigInteger.valueOf(accountPurseKey.getPurseId()))
			.toPurseId(BigInteger.valueOf(accountPurseKey.getPurseId()))
			.accountPurseId(accountPurseKey.getAccountPurseId())
			.accountId(BigInteger.valueOf(accountPurseKey.getAccountId()))
			.toAccountId(BigInteger.valueOf(accountPurseKey.getAccountId()));
		return this;
	}

	private String cutOverTime(Map<String, Map<String, Object>> productAttributes) {
		try {
			Date date = Calendar.getInstance()
				.getTime();
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-YY");
			String cutOffDate = dateFormat.format(date);

			DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyhh:mm:ss");
			Date cutOffDateTime = dateTimeFormat.parse(cutOffDate.concat(productAttributes.get("Product")
				.get("cutOverTime")
				.toString()));

			Date businessDate = null;
			date = dateTimeFormat.parse(dateTimeFormat.format(date));

			if (date.compareTo(cutOffDateTime) < 0)
				businessDate = cutOffDateTime;
			else {
				Calendar c = Calendar.getInstance();
				c.setTime(cutOffDateTime);
				c.add(Calendar.DATE, 1);
				businessDate = c.getTime();
			}

			return new SimpleDateFormat("dd-MMM-yy").format(businessDate)
				.toUpperCase();
		} catch (Exception e) {
			throw new ServiceException(SpilExceptionMessages.ERROR_PARSING_DATE);
		}
	}

}
