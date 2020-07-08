/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author skocherla
 */
public class AuthRequest implements Serializable {
	private String cardNum;
	private String cardNumHash;
	private String cardNumEncr;
	private BigDecimal openingBalance;
	private BigDecimal openingLedgerBalance;
	private BigDecimal openingAvailBalance;
	private BigDecimal closingBalance;
	private BigDecimal closingLedgerBalance; // ??
	private BigDecimal closingAvailBalance;
	private BigDecimal txnAmount;
	private BigDecimal txnFee;
	private BigDecimal totalAmount;
	private String creditDebitFlg;
	private String txnNarration;
	private String txnDesc;
	private Timestamp lastUpdDate;
	private Timestamp insDate;
	private String rrn;
	private String authId;
	private String txnDate;
	private String txnTime;
	private String txnTimeZone;
	private String partialPreAuthInd;
	private String feeFlg;
	private BigDecimal feeAmount;
	private String upc;
	private String channel;
	private String msgType;
	private String txnCode;
	private String txnType; // is_financial( txn table)
	private String currCode;
	private BigInteger accoundId;
	private BigInteger toAccountId;
	private String merchantName;
	private String merchantCity;
	private String merchantState;
	private String merchantRefNum;
	private String localeCountry;
	private String localeLanguage;
	private String localeCurrency;
	private String cardLast4digits;
	private BigDecimal recordSeq;
	private BigInteger productId;
	private BigInteger issuerId;
	private BigInteger partnerId;
	private BigInteger mdmId;
	private BigInteger purseId;
	private BigInteger toPurseId;
	private BigDecimal txnSeqId;
	private String proxyNumber;
	private Date businessDate;
	private String posEntryMode;
	private String posConditionCode;
	private String storeId;
	private String storeAddress1;
	private String storeAddress2;
	private String storeCity;
	private String storeState;
	private String terminalId;
	private String cardStatus;
	private String respCode;
	private short reversalCode;
	private String errorMsg;

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + Objects.hashCode(this.cardNumHash);
		hash = 79 * hash + Objects.hashCode(this.accoundId);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AuthRequest other = (AuthRequest) obj;
		return true;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
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

	public BigDecimal getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(BigDecimal openingBalance) {
		this.openingBalance = openingBalance;
	}

	public BigDecimal getOpeningLedgerBalance() {
		return openingLedgerBalance;
	}

	public void setOpeningLedgerBalance(BigDecimal openingLedgerBalance) {
		this.openingLedgerBalance = openingLedgerBalance;
	}

	public BigDecimal getOpeningAvailBalance() {
		return openingAvailBalance;
	}

	public void setOpeningAvailBalance(BigDecimal openingAvailBalance) {
		this.openingAvailBalance = openingAvailBalance;
	}

	public BigDecimal getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(BigDecimal closingBalance) {
		this.closingBalance = closingBalance;
	}

	public BigDecimal getClosingLedgerBalance() {
		return closingLedgerBalance;
	}

	public void setClosingLedgerBalance(BigDecimal closingLedgerBalance) {
		this.closingLedgerBalance = closingLedgerBalance;
	}

	public BigDecimal getClosingAvailBalance() {
		return closingAvailBalance;
	}

	public void setClosingAvailBalance(BigDecimal closingAvailBalance) {
		this.closingAvailBalance = closingAvailBalance;
	}

	public BigDecimal getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(BigDecimal txnAmount) {
		this.txnAmount = txnAmount;
	}

	public BigDecimal getTxnFee() {
		return txnFee;
	}

	public void setTxnFee(BigDecimal txnFee) {
		this.txnFee = txnFee;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCreditDebitFlg() {
		return creditDebitFlg;
	}

	public void setCreditDebitFlg(String creditDebitFlg) {
		this.creditDebitFlg = creditDebitFlg;
	}

	public String getTxnNarration() {
		return txnNarration;
	}

	public void setTxnNarration(String txnNarration) {
		this.txnNarration = txnNarration;
	}

	public String getTxnDesc() {
		return txnDesc;
	}

	public void setTxnDesc(String txnDesc) {
		this.txnDesc = txnDesc;
	}

	public Timestamp getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Timestamp lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public Timestamp getInsDate() {
		return insDate;
	}

	public void setInsDate(Timestamp insDate) {
		this.insDate = insDate;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public String getTxnTimeZone() {
		return txnTimeZone;
	}

	public void setTxnTimeZone(String txnTimeZone) {
		this.txnTimeZone = txnTimeZone;
	}

	public String getPartialPreAuthInd() {
		return partialPreAuthInd;
	}

	public void setPartialPreAuthInd(String partialPreAuthInd) {
		this.partialPreAuthInd = partialPreAuthInd;
	}

	public String getFeeFlg() {
		return feeFlg;
	}

	public void setFeeFlg(String feeFlg) {
		this.feeFlg = feeFlg;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getTxnCode() {
		return txnCode;
	}

	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	public BigInteger getAccoundId() {
		return accoundId;
	}

	public void setAccoundId(BigInteger accoundId) {
		this.accoundId = accoundId;
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

	public String getMerchantRefNum() {
		return merchantRefNum;
	}

	public void setMerchantRefNum(String merchantRefNum) {
		this.merchantRefNum = merchantRefNum;
	}

	public String getLocaleCountry() {
		return localeCountry;
	}

	public void setLocaleCountry(String localeCountry) {
		this.localeCountry = localeCountry;
	}

	public String getLocaleLanguage() {
		return localeLanguage;
	}

	public void setLocaleLanguage(String localeLanguage) {
		this.localeLanguage = localeLanguage;
	}

	public String getLocaleCurrency() {
		return localeCurrency;
	}

	public void setLocaleCurrency(String localeCurrency) {
		this.localeCurrency = localeCurrency;
	}

	public String getCardLast4digits() {
		return cardLast4digits;
	}

	public void setCardLast4digits(String cardLast4digits) {
		this.cardLast4digits = cardLast4digits;
	}

	public BigDecimal getRecordSeq() {
		return recordSeq;
	}

	public void setRecordSeq(BigDecimal recordSeq) {
		this.recordSeq = recordSeq;
	}

	public BigInteger getPurseId() {
		return purseId;
	}

	public void setPurseId(BigInteger purseId) {
		this.purseId = purseId;
	}

	public BigInteger getProductId() {
		return productId;
	}

	public void setProductId(BigInteger productId) {
		this.productId = productId;
	}

	public BigInteger getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(BigInteger issuerId) {
		this.issuerId = issuerId;
	}

	public BigInteger getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(BigInteger partnerId) {
		this.partnerId = partnerId;
	}

	public BigInteger getMdmId() {
		return mdmId;
	}

	public void setMdmId(BigInteger mdmId) {
		this.mdmId = mdmId;
	}

	public BigInteger getToPurseId() {
		return toPurseId;
	}

	public void setToPurseId(BigInteger toPurseId) {
		this.toPurseId = toPurseId;
	}

	public BigDecimal getTxnSeqId() {
		return txnSeqId;
	}

	public void setTxnSeqId(BigDecimal txnSeqId) {
		this.txnSeqId = txnSeqId;
	}

	public String getProxyNumber() {
		return proxyNumber;
	}

	public void setProxyNumber(String proxyNumber) {
		this.proxyNumber = proxyNumber;
	}

	public Date getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}

	public String getPosEntryMode() {
		return posEntryMode;
	}

	public void setPosEntryMode(String posEntryMode) {
		this.posEntryMode = posEntryMode;
	}

	public String getPosConditionCode() {
		return posConditionCode;
	}

	public void setPosConditionCode(String posConditionCode) {
		this.posConditionCode = posConditionCode;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreAddress1() {
		return storeAddress1;
	}

	public void setStoreAddress1(String storeAddress1) {
		this.storeAddress1 = storeAddress1;
	}

	public String getStoreAddress2() {
		return storeAddress2;
	}

	public void setStoreAddress2(String storeAddress2) {
		this.storeAddress2 = storeAddress2;
	}

	public String getStoreCity() {
		return storeCity;
	}

	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}

	public String getStoreState() {
		return storeState;
	}

	public void setStoreState(String storeState) {
		this.storeState = storeState;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public short getReversalCode() {
		return reversalCode;
	}

	public void setReversalCode(short reversalCode) {
		this.reversalCode = reversalCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
