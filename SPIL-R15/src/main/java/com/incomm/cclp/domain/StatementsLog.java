/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Persistable;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dto.StatementLog;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 *
 * @author skocherla
 */
@Builder(builderClassName = "Builder", toBuilder = true)
@AllArgsConstructor
@Entity
@Table(name = "STATEMENTS_LOG")
@XmlRootElement

@NamedNativeQueries({
		@NamedNativeQuery(name = "StatementsLog.getLastFeeTransactions", query = QueryConstants.GET_LAST_FEE_TXN, resultSetMapping = "StatementsLogInfoResult"),
		@NamedNativeQuery(name = "StatementsLog.getAllStatementLogs", query = QueryConstants.GET_ALL_STATEMENTS_LOG, resultSetMapping = "StatementLogResult") })

@SqlResultSetMapping(name = "StatementsLogInfoResult", classes = { @ConstructorResult(targetClass = StatementsLogInfo.class, columns = {
		@ColumnResult(name = "txnDesc", type = String.class), @ColumnResult(name = "txnAmount", type = Double.class),
		@ColumnResult(name = "accountPurseId", type = BigInteger.class), @ColumnResult(name = "purseId", type = Long.class) }) })

@SqlResultSetMapping(name = "StatementLogResult", classes = { @ConstructorResult(targetClass = StatementLog.class, columns = {
		@ColumnResult(name = "transactionDescription", type = String.class),
		@ColumnResult(name = "transactionAmount", type = BigDecimal.class), @ColumnResult(name = "operationType", type = String.class),
		@ColumnResult(name = "accountPurseId", type = Long.class), @ColumnResult(name = "purseId", type = Long.class),
		@ColumnResult(name = "toPurseId", type = Long.class) }) })

@NamedQueries({ @NamedQuery(name = "StatementsLog.findAll", query = "SELECT s FROM StatementsLog s"),
		@NamedQuery(name = "StatementsLog.findByCardNumHash", query = "SELECT s FROM StatementsLog s WHERE s.cardNumHash = :cardNumHash"),
		@NamedQuery(name = "StatementsLog.findByCardNumEncr", query = "SELECT s FROM StatementsLog s WHERE s.cardNumEncr = :cardNumEncr"),
		@NamedQuery(name = "StatementsLog.findByOpeningBalance", query = "SELECT s FROM StatementsLog s WHERE s.openingBalance = :openingBalance"),
		@NamedQuery(name = "StatementsLog.findByClosingBalance", query = "SELECT s FROM StatementsLog s WHERE s.closingBalance = :closingBalance"),
		@NamedQuery(name = "StatementsLog.findByTransactionAmount", query = "SELECT s FROM StatementsLog s WHERE s.transactionAmount = :transactionAmount"),
		@NamedQuery(name = "StatementsLog.findByCreditDebitFlag", query = "SELECT s FROM StatementsLog s WHERE s.creditDebitFlag = :creditDebitFlag"),
		@NamedQuery(name = "StatementsLog.findByTransactionNarration", query = "SELECT s FROM StatementsLog s WHERE s.transactionNarration = :transactionNarration"),
		@NamedQuery(name = "StatementsLog.findByLastUpdDate", query = "SELECT s FROM StatementsLog s WHERE s.lastUpdDate = :lastUpdDate"),
		@NamedQuery(name = "StatementsLog.findByInsDate", query = "SELECT s FROM StatementsLog s WHERE s.insDate = :insDate"),
		@NamedQuery(name = "StatementsLog.findByRrn", query = "SELECT s FROM StatementsLog s WHERE s.rrn = :rrn"),
		@NamedQuery(name = "StatementsLog.findByTransactionDate", query = "SELECT s FROM StatementsLog s WHERE s.transactionDate = :transactionDate"),
		@NamedQuery(name = "StatementsLog.findByTransactionTime", query = "SELECT s FROM StatementsLog s WHERE s.transactionTime = :transactionTime"),
		@NamedQuery(name = "StatementsLog.findByFeeFlag", query = "SELECT s FROM StatementsLog s WHERE s.feeFlag = :feeFlag"),
		@NamedQuery(name = "StatementsLog.findByDeliveryChannel", query = "SELECT s FROM StatementsLog s WHERE s.deliveryChannel = :deliveryChannel"),
		@NamedQuery(name = "StatementsLog.findByTransactionCode", query = "SELECT s FROM StatementsLog s WHERE s.transactionCode = :transactionCode"),
		@NamedQuery(name = "StatementsLog.findByAccountId", query = "SELECT s FROM StatementsLog s WHERE s.accountId = :accountId"),
		@NamedQuery(name = "StatementsLog.findByToAccountId", query = "SELECT s FROM StatementsLog s WHERE s.toAccountId = :toAccountId"),
		@NamedQuery(name = "StatementsLog.findByMerchantName", query = "SELECT s FROM StatementsLog s WHERE s.merchantName = :merchantName"),
		@NamedQuery(name = "StatementsLog.findByMerchantCity", query = "SELECT s FROM StatementsLog s WHERE s.merchantCity = :merchantCity"),
		@NamedQuery(name = "StatementsLog.findByMerchantState", query = "SELECT s FROM StatementsLog s WHERE s.merchantState = :merchantState"),
		@NamedQuery(name = "StatementsLog.findByCardLast4digit", query = "SELECT s FROM StatementsLog s WHERE s.cardLast4digit = :cardLast4digit"),
		@NamedQuery(name = "StatementsLog.findByProductId", query = "SELECT s FROM StatementsLog s WHERE s.productId = :productId"),
		@NamedQuery(name = "StatementsLog.findByRecordSeq", query = "SELECT s FROM StatementsLog s WHERE s.statementsLogPK.recordSeq = :recordSeq"),
		@NamedQuery(name = "StatementsLog.findByPurseId", query = "SELECT s FROM StatementsLog s WHERE s.purseId = :purseId"),
		@NamedQuery(name = "StatementsLog.findByToPurseId", query = "SELECT s FROM StatementsLog s WHERE s.toPurseId = :toPurseId"),
		@NamedQuery(name = "StatementsLog.findByTransactionSqid", query = "SELECT s FROM StatementsLog s WHERE s.statementsLogPK.transactionSqid = :transactionSqid"),
		@NamedQuery(name = "StatementsLog.findByBusinessDate", query = "SELECT s FROM StatementsLog s WHERE s.businessDate = :businessDate"),
		@NamedQuery(name = "StatementsLog.findByStoreId", query = "SELECT s FROM StatementsLog s WHERE s.storeId = :storeId"),
		@NamedQuery(name = "StatementsLog.findByAuthId", query = "SELECT s FROM StatementsLog s WHERE s.authId = :authId"),
		@NamedQuery(name = "StatementsLog.findByInsTimeStamp", query = "SELECT s FROM StatementsLog s WHERE s.insTimeStamp = :insTimeStamp"),
		@NamedQuery(name = "StatementsLog.findBySourceDescription", query = "SELECT s FROM StatementsLog s WHERE s.sourceDescription = :sourceDescription") })
public class StatementsLog implements Persistable<StatementsLogPK> {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected StatementsLogPK statementsLogPK;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 90)
	@Column(name = "CARD_NUM_HASH")
	private String cardNumHash;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "CARD_NUM_ENCR")
	private String cardNumEncr;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce
	// field validation
	@Basic(optional = false)
	@NotNull
	@Column(name = "OPENING_BALANCE")
	private double openingBalance;
	@Basic(optional = false)
	@NotNull
	@Column(name = "CLOSING_BALANCE")
	private double closingBalance;
	@Basic(optional = false)
	@NotNull
	@Column(name = "TRANSACTION_AMOUNT")
	private double transactionAmount;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2)
	@Column(name = "CREDIT_DEBIT_FLAG")
	private String creditDebitFlag;
	@Size(max = 300)
	@Column(name = "TRANSACTION_NARRATION")
	private String transactionNarration;
	@Column(name = "LAST_UPD_DATE")
//    @Temporal(TemporalType.TIMESTAMP)
	private String lastUpdDate;
	@Basic(optional = false)
	@NotNull
	@Column(name = "INS_DATE")
//    @Temporal(TemporalType.TIMESTAMP)
	private String insDate;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "RRN")
	private String rrn;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 8)
	@Column(name = "TRANSACTION_DATE")
	private String transactionDate;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "TRANSACTION_TIME")
	private String transactionTime;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 1)
	@Column(name = "FEE_FLAG")
	private String feeFlag;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2)
	@Column(name = "DELIVERY_CHANNEL")
	private String deliveryChannel;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 3)
	@Column(name = "TRANSACTION_CODE")
	private String transactionCode;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ACCOUNT_ID")
	private BigInteger accountId;
	@Column(name = "TO_ACCOUNT_ID")
	private BigInteger toAccountId;
	@Size(max = 50)
	@Column(name = "MERCHANT_NAME")
	private String merchantName;
	@Size(max = 50)
	@Column(name = "MERCHANT_CITY")
	private String merchantCity;
	@Size(max = 30)
	@Column(name = "MERCHANT_STATE")
	private String merchantState;
	@Size(max = 4)
	@Column(name = "CARD_LAST4DIGIT")
	private String cardLast4digit;
	@Basic(optional = false)
	@NotNull
	@Column(name = "PRODUCT_ID")
	private BigInteger productId;
	@Column(name = "PURSE_ID")
	private BigInteger purseId;
	@Column(name = "TO_PURSE_ID")
	private BigInteger toPurseId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "BUSINESS_DATE")
//    @Temporal(TemporalType.TIMESTAMP)
	private String businessDate;
	@Size(max = 64)
	@Column(name = "STORE_ID")
	private String storeId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 14)
	@Column(name = "AUTH_ID")
	private String authId;
	@Column(name = "INS_TIME_STAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insTimeStamp;
	@Size(max = 10)
	@Column(name = "SOURCE_DESCRIPTION", columnDefinition = "String default 'CCLP'")
	private String sourceDescription;
	@Column(name = "ACCOUNT_PURSE_ID")
	private long accountPurseId;
	@Column(name = "AVAIL_OPENING_BALANCE")
	private double availOpenBal;
	@Column(name = "AVAIL_CLOSING_BALANCE")
	private double availcloseBal;

	@Transient
	private boolean update;

	public StatementsLog() {
	}

	public StatementsLog(StatementsLogPK statementsLogPK) {
		this.statementsLogPK = statementsLogPK;
	}

	public StatementsLog(StatementsLogPK statementsLogPK, String cardNumHash, String cardNumEncr, double openingBalance,
			double closingBalance, double transactionAmount, String creditDebitFlag, String insDate, String rrn, String transactionDate,
			String transactionTime, String feeFlag, String deliveryChannel, String transactionCode, BigInteger accountId,
			BigInteger productId, String businessDate, String authId) {
		this.statementsLogPK = statementsLogPK;
		this.cardNumHash = cardNumHash;
		this.cardNumEncr = cardNumEncr;
		this.openingBalance = openingBalance;
		this.closingBalance = closingBalance;
		this.transactionAmount = transactionAmount;
		this.creditDebitFlag = creditDebitFlag;
		this.insDate = insDate;
		this.rrn = rrn;
		this.transactionDate = transactionDate;
		this.transactionTime = transactionTime;
		this.feeFlag = feeFlag;
		this.deliveryChannel = deliveryChannel;
		this.transactionCode = transactionCode;
		this.accountId = accountId;
		this.productId = productId;
		this.businessDate = businessDate;
		this.authId = authId;
	}

	public StatementsLog(BigInteger recordSeq, BigDecimal transactionSqid) {
		this.statementsLogPK = new StatementsLogPK(recordSeq, transactionSqid);
	}

	public StatementsLogPK getStatementsLogPK() {
		return statementsLogPK;
	}

	public void setStatementsLogPK(StatementsLogPK statementsLogPK) {
		this.statementsLogPK = statementsLogPK;
	}

	public String getCardNumHash() {
		return cardNumHash;
	}

	public void setCardNumHash(String cardNumHash) {
		this.cardNumHash = cardNumHash;
	}

	public String getCardNumEncr() {
		return cardNumEncr;
	}

	public void setCardNumEncr(String cardNumEncr) {
		this.cardNumEncr = cardNumEncr;
	}

	public double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getCreditDebitFlag() {
		return creditDebitFlag;
	}

	public void setCreditDebitFlag(String creditDebitFlag) {
		this.creditDebitFlag = creditDebitFlag;
	}

	public String getTransactionNarration() {
		return transactionNarration;
	}

	public void setTransactionNarration(String transactionNarration) {
		this.transactionNarration = transactionNarration;
	}

	public String getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(String lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public String getInsDate() {
		return insDate;
	}

	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getFeeFlag() {
		return feeFlag;
	}

	public void setFeeFlag(String feeFlag) {
		this.feeFlag = feeFlag;
	}

	public String getDeliveryChannel() {
		return deliveryChannel;
	}

	public void setDeliveryChannel(String deliveryChannel) {
		this.deliveryChannel = deliveryChannel;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public BigInteger getAccountId() {
		return accountId;
	}

	public void setAccountId(BigInteger accountId) {
		this.accountId = accountId;
	}

	public BigInteger getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(BigInteger toAccountId) {
		this.toAccountId = toAccountId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantCity() {
		return merchantCity;
	}

	public void setMerchantCity(String merchantCity) {
		this.merchantCity = merchantCity;
	}

	public String getMerchantState() {
		return merchantState;
	}

	public void setMerchantState(String merchantState) {
		this.merchantState = merchantState;
	}

	public String getCardLast4digit() {
		return cardLast4digit;
	}

	public void setCardLast4digit(String cardLast4digit) {
		this.cardLast4digit = cardLast4digit;
	}

	public BigInteger getProductId() {
		return productId;
	}

	public void setProductId(BigInteger productId) {
		this.productId = productId;
	}

	public BigInteger getPurseId() {
		return purseId;
	}

	public void setPurseId(BigInteger purseId) {
		this.purseId = purseId;
	}

	public BigInteger getToPurseId() {
		return toPurseId;
	}

	public void setToPurseId(BigInteger toPurseId) {
		this.toPurseId = toPurseId;
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public Date getInsTimeStamp() {
		return insTimeStamp;
	}

	public void setInsTimeStamp(Date insTimeStamp) {
		this.insTimeStamp = insTimeStamp;
	}

	public String getSourceDescription() {
		return sourceDescription;
	}

	public void setSourceDescription(String sourceDescription) {
		this.sourceDescription = sourceDescription;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (statementsLogPK != null ? statementsLogPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof StatementsLog)) {
			return false;
		}
		StatementsLog other = (StatementsLog) object;
		if ((this.statementsLogPK == null && other.statementsLogPK != null)
				|| (this.statementsLogPK != null && !this.statementsLogPK.equals(other.statementsLogPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.cclp.StatementsLog[ statementsLogPK=" + statementsLogPK + " ]";
	}

	@Override
	public StatementsLogPK getId() {
		return getStatementsLogPK();
	}

	public long getAccountPurseId() {
		return accountPurseId;
	}

	public void setAccountPurseId(long accountPurseId) {
		this.accountPurseId = accountPurseId;
	}

	@Override
	public boolean isNew() {
		return !this.update;
	}

	public boolean isUpdate() {
		return this.update;
	}

	public boolean setUpdate(boolean update) {
		return this.update = update;
	}

	public double getAvailOpenBal() {
		return availOpenBal;
	}

	public void setAvailOpenBal(double availOpenBal) {
		this.availOpenBal = availOpenBal;
	}

	public double getAvailcloseBal() {
		return availcloseBal;
	}

	public void setAvailcloseBal(double availcloseBal) {
		this.availcloseBal = availcloseBal;
	}

}
