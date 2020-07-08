/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.math3.util.Precision;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;
import com.incomm.cclp.util.Util;

/**
 *
 * @author skocherla
 */
public class RequestInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = 2196093867173972284L;

	private String cardNum;
	private String cardNumHash;
	private String cardNumEncr;
	private double openingBalance;
	private double openingLedgerBalance;
	private double openingAvailBalance;
	private double closingBalance;
	private double closingLedgerBalance;
	private double closingAvailBalance;
	private double txnAmount;
	private double txnFee;
	private double totalAmount;
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
	private String lastTxnDate;
	private String partialPreAuthInd;
	private String feeFlg;
	private String freeFeeFlg;
	private String firstTimeTopupFlag;
	private String maxFeeFlg;
	private String prodFeeCondition;
	private double prodMinFeeAmount;
	private double prodPercentFeeAmount;
	private double prodFlatFeeAmount;
	private double prodTxnFeeAmount;
	private double maxCardBal;
	private double redemDelayTranAmount;
	private double feeAmount;
	private double authorizedAmount;
	private String productFeeCond;
	private String upc;
	private String channel;
	private String msgType;
	private String txnCode;
	private String txnType;
	private String orgnlTranCurr;
	private BigInteger accountId;
	private BigInteger toAccountId;
	private String accountNumber;
	private String merchantName;
	private String merchantCity;
	private String merchantState;
	private String merchantRefNum;
	private String merchantId;
	private String locationId;
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
	private String cashierId;
	private String cardStatus;
	private String oldCardStatus;
	private String partySupported;
	private String expiryDate;
	private String respCode;
	private short reversalCode;
	private String errorMsg;
	private String txnReversalFlag;
	private String productFunding;
	private java.sql.Date sysDate;
	private String serialNumber;
	private String digitalPin;
	private String orgnlTxnDate;
	private String orgnlTxnTime;
	private String orgnlCardNumHash;
	private String orgnlRrn;
	private String orgnlTerminalId;
	private double orgnlTxnFee;
	private double orgnlTxnAmt;
	private double orgnAuthAmt;
	private long accountPurseId;
	private int purseTypeId;
	private String purseType;
	// Currency Conversion Fields
	private double reqTxnAmt;
	private double reqAuthAmt;
	private String tranCurr;
	private BigDecimal convRate;

	private String storeDbId;

	public String getStoreDbId() {
		return storeDbId;
	}

	public void setStoreDbId(String storeDbId) {
		this.storeDbId = storeDbId;
	}

	private ValueDTO valueDTO;
	int scale;

	// Multipurse fields
	AccountPurseBalance purAuthReq;
	private String purseName;
	double maxPurseBalance;
	private BigInteger defaultPurseId;
	boolean newPurse = false;

	String sourceName;
	private double availOpeningBalance;
	private double availClosingBalance;

	public RequestInfo(int roundoffdigits) {
		this.errorMsg = GeneralConstants.OK;
		this.respCode = ResponseCodes.SUCCESS;
		this.scale = roundoffdigits;
	}

	public RequestInfo() {
		this.errorMsg = GeneralConstants.OK;
		this.respCode = ResponseCodes.SUCCESS;
	}

	public double getReqTxnAmt() {
		return reqTxnAmt;
	}

	public void setReqTxnAmt(double reqTxnAmt) {
		this.reqTxnAmt = Precision.round(reqTxnAmt, scale);
	}

	public void setReqTxnAmt(String reqTxnAmt) {
		if (reqTxnAmt != null) {
			this.reqTxnAmt = Precision.round(Double.parseDouble(reqTxnAmt), scale);
		} else {
			this.reqTxnAmt = 0;
		}
	}

	public double getReqAuthAmt() {
		return reqAuthAmt;
	}

	public void setReqAuthAmt(double reqAuthAmt) {
		this.reqAuthAmt = Precision.round(reqAuthAmt, scale);
	}

	public void setReqAuthAmt(String reqAuthAmt) {
		if (reqAuthAmt != null) {
			this.reqAuthAmt = Precision.round(Double.parseDouble(reqAuthAmt), scale);
		} else {
			this.reqAuthAmt = 0;
		}
	}

	public BigDecimal getConvRate() {
		return convRate;
	}

	public String getOrgnlTranCurr() {
		return orgnlTranCurr;
	}

	public void setOrgnlTranCurr(String orgnlTranCurr) {
		this.orgnlTranCurr = orgnlTranCurr;
	}

	public void setConvRate(BigDecimal convRate) {
		this.convRate = convRate;
	}

	public void setConvRate(String convRate) {
		if (convRate != null) {
			this.convRate = new BigDecimal(convRate);
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public RequestInfo getClone() throws ServiceException {
		RequestInfo reqInfo = null;
		try {
			reqInfo = (RequestInfo) this.clone();
		} catch (CloneNotSupportedException e) {
			throw new ServiceException("Internal System Error", ResponseCodes.SYSTEM_ERROR);
		}
		return reqInfo;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + Objects.hashCode(this.cardNumHash);
		hash = 79 * hash + Objects.hashCode(this.accountId);
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
		final RequestInfo other = (RequestInfo) obj;
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

	public double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = Precision.round(openingBalance, scale);
	}

	public double getOpeningLedgerBalance() {
		return openingLedgerBalance;
	}

	public void setOpeningLedgerBalance(double openingLedgerBalance) {
		this.openingLedgerBalance = Precision.round(openingLedgerBalance, scale);
	}

	public double getOpeningAvailBalance() {
		return openingAvailBalance;
	}

	public void setOpeningAvailBalance(double openingAvailBalance) {
		this.openingAvailBalance = Precision.round(openingAvailBalance, scale);
	}

	public double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(double closingBalance) {
		this.closingBalance = Precision.round(closingBalance, scale);
	}

	public double getClosingLedgerBalance() {
		return closingLedgerBalance;
	}

	public void setClosingLedgerBalance(double closingLedgerBalance) {
		this.closingLedgerBalance = Precision.round(closingLedgerBalance, scale);
	}

	public double getClosingAvailBalance() {
		return closingAvailBalance;
	}

	public void setClosingAvailBalance(double closingAvailBalance) {
		this.closingAvailBalance = Precision.round(closingAvailBalance, scale);
	}

	public double getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(double txnAmount) {
		this.txnAmount = Precision.round(txnAmount, scale);
	}

	public void setTxnAmount(String txnAmount) {
		if (txnAmount != null) {
			this.txnAmount = Precision.round(Double.parseDouble(txnAmount), scale);
		} else {
			this.txnAmount = 0;
		}
	}

	public double getTxnFee() {
		return txnFee;
	}

	public void setTxnFee(double txnFee) {
		this.txnFee = Precision.round(txnFee, scale);
	}

	public void setTxnFee(String txnFee) {
		if (txnFee != null) {
			this.txnFee = Precision.round(Double.parseDouble(txnFee), scale);
		} else {
			this.txnFee = 0;
		}
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = Precision.round(totalAmount, scale);
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

	public String getLastTxnDate() {
		return lastTxnDate;
	}

	public void setLastTxnDate(String lastTxnDate) {
		this.lastTxnDate = lastTxnDate;
	}

	public void setLastTxnDate(Date lastTxnDate) {
		if (lastTxnDate != null) {
			this.lastTxnDate = String.valueOf(lastTxnDate);
		} else {
			this.lastTxnDate = "";
		}
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
		switch ((partialPreAuthInd != null) ? partialPreAuthInd : "") {
		case "true":
			this.partialPreAuthInd = "Y";
			break;
		case "false":
			this.partialPreAuthInd = "N";
			break;
		default:
			this.partialPreAuthInd = "";
			break;
		}
	}

	public boolean isPartialAuth() {
		if ("true".equalsIgnoreCase(partialPreAuthInd))
			return true;
		return false;
	}

	public String getFeeFlg() {
		return feeFlg;
	}

	public void setFeeFlg(String feeFlg) {
		this.feeFlg = feeFlg;
	}

	public String getFreeFeeFlg() {
		return freeFeeFlg;
	}

	public void setFreeFeeFlg(String freeFeeFlg) {
		this.freeFeeFlg = freeFeeFlg;
	}

	public String getMaxFeeFlg() {
		return maxFeeFlg;
	}

	public void setFirstTimeTopupFlag(String firstTimeTopupFlag) {
		this.firstTimeTopupFlag = firstTimeTopupFlag;
	}

	public String getFirstTimeTopupFlag() {
		return firstTimeTopupFlag;
	}

	public void setMaxFeeFlg(String maxFeeFlg) {
		this.maxFeeFlg = maxFeeFlg;
	}

	public String getProdFeeCondition() {
		return prodFeeCondition;
	}

	public void setProdFeeCondition(String prodFeeCondition) {
		this.prodFeeCondition = prodFeeCondition;
	}

	public double getProdMinFeeAmount() {
		return prodMinFeeAmount;
	}

	public void setProdMinFeeAmount(double prodMinFeeAmount) {
		this.prodMinFeeAmount = Precision.round(prodMinFeeAmount, scale);
	}

	public void setProdMinFeeAmount(String prodMinFeeAmount) {
		if (prodMinFeeAmount != null) {
			this.prodMinFeeAmount = Precision.round(Double.parseDouble(prodMinFeeAmount), scale);
		} else {
			this.prodMinFeeAmount = 0;
		}

	}

	public double getAuthorizedAmount() {
		return authorizedAmount;
	}

	public void setAuthorizedAmount(double authorizedAmount) {
		this.authorizedAmount = Precision.round(authorizedAmount, scale);
	}

	public String getProductFeeCond() {
		return productFeeCond;
	}

	public void setProductFeeCond(String productFeeCond) {
		this.productFeeCond = productFeeCond;
	}

	public double getProdPercentFeeAmount() {
		return prodPercentFeeAmount;
	}

	public void setProdPercentFeeAmount(double prodPercentFeeAmount) {
		this.prodPercentFeeAmount = Precision.round(prodPercentFeeAmount, scale);
	}

	public void setProdPercentFeeAmount(String prodPercentFeeAmount) {
		if (prodPercentFeeAmount != null) {
			this.prodPercentFeeAmount = Precision.round(Double.parseDouble(prodPercentFeeAmount), scale);
		} else {
			this.prodPercentFeeAmount = 0;
		}
	}

	public double getProdFlatFeeAmount() {
		return prodFlatFeeAmount;
	}

	public void setProdFlatFeeAmount(double prodFlatFeeAmount) {
		this.prodFlatFeeAmount = Precision.round(prodFlatFeeAmount, scale);
	}

	public void setProdFlatFeeAmount(String prodFlatFeeAmount) {
		if (prodFlatFeeAmount != null) {
			this.prodFlatFeeAmount = Precision.round(Double.parseDouble(prodFlatFeeAmount), scale);
		} else {
			this.prodFlatFeeAmount = 0;
		}
	}

	public double getProdTxnFeeAmount() {
		return prodTxnFeeAmount;
	}

	public void setProdTxnFeeAmount(double prodTxnFeeAmount) {
		this.prodTxnFeeAmount = Precision.round(prodTxnFeeAmount, scale);
	}

	public void setProdTxnFeeAmount(String prodTxnFeeAmount) {
		if (prodTxnFeeAmount != null) {
			this.prodTxnFeeAmount = Precision.round(Double.parseDouble(prodTxnFeeAmount), scale);
		} else {
			this.prodTxnFeeAmount = 0;
		}
	}

	public double getMaxCardBal() {
		return maxCardBal;
	}

	public void setMaxCardBal(double maxCardBal) {
		this.maxCardBal = Precision.round(maxCardBal, scale);
	}

	public void setMaxCardBal(String maxCardBal) {
		if (maxCardBal != null && "".equals(maxCardBal)) {
			this.maxCardBal = Precision.round(Double.parseDouble(maxCardBal), scale);
		} else {
			this.maxCardBal = 0;
		}

	}

	public double getRedemDelayTranAmount() {
		return redemDelayTranAmount;
	}

	public void setRedemDelayTranAmount(double redemDelayTranAmount) {
		this.redemDelayTranAmount = Precision.round(redemDelayTranAmount, scale);
	}

	public void setRedemDelayTranAmount(String redemDelayTranAmount) {
		if (redemDelayTranAmount != null) {
			this.redemDelayTranAmount = Precision.round(Double.parseDouble(redemDelayTranAmount), scale);
		} else {
			this.redemDelayTranAmount = 0;
		}

	}

	public double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(double feeAmount) {
		this.feeAmount = Precision.round(feeAmount, scale);
	}

	public void setFeeAmount(String feeAmount) {
		if (feeAmount != null) {
			this.feeAmount = Precision.round(Double.parseDouble(feeAmount), scale);
		} else {
			this.feeAmount = 0;
		}
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

	public String getTranCurr() {
		return tranCurr;
	}

	public void setTranCurr(String tranCurr) {
		this.tranCurr = tranCurr;
	}

	public BigInteger getAccountId() {
		return accountId;
	}

	public void setAccountId(BigInteger accountId) {
		this.accountId = accountId;
	}

	public void setAccountId(String accountId) {
		if (accountId != null) {
			this.accountId = new BigInteger(accountId);
		} else {
			this.accountId = BigInteger.ZERO;
		}
	}

	public BigInteger getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(BigInteger toAccountId) {
		this.toAccountId = toAccountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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

	public void setProductId(String productId) {
		if (productId != null) {
			this.productId = new BigInteger(productId);
		} else {
			this.productId = BigInteger.ZERO;
		}
	}

	public BigInteger getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(BigInteger issuerId) {
		this.issuerId = issuerId;
	}

	public void setIssuerId(String issuerId) {
		if (issuerId != null) {
			this.issuerId = new BigInteger(issuerId);
		} else {
			this.issuerId = BigInteger.ZERO;
		}
	}

	public BigInteger getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(BigInteger partnerId) {
		this.partnerId = partnerId;
	}

	public void setPartnerId(String partnerId) {
		if (partnerId != null) {
			this.partnerId = new BigInteger(partnerId);
		} else {
			this.partnerId = BigInteger.ZERO;
		}
	}

	public BigInteger getMdmId() {
		return mdmId;
	}

	public void setMdmId(BigInteger mdmId) {
		this.mdmId = mdmId;
	}

	public void setMdmId(String mdmId) {
		if (mdmId != null) {
			this.mdmId = new BigInteger(mdmId);
		} else {
			this.mdmId = BigInteger.ZERO;
		}
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

	public String getCashierId() {
		return cashierId;
	}

	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
	}

	public String getOldCardStatus() {
		return oldCardStatus;
	}

	public void setOldCardStatus(String oldCardStatus) {
		this.oldCardStatus = oldCardStatus;
	}

	public String getPartySupported() {
		return partySupported;
	}

	public void setPartySupported(String partySupported) {
		this.partySupported = partySupported;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
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

	public void setReversalCode(String code) {
		this.reversalCode = Short.valueOf(code);
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public ValueDTO getValueDTO() {
		return valueDTO;
	}

	public void setValueDTO(ValueDTO valueDTO) {
		this.valueDTO = valueDTO;
	}

	public String getTxnReversalFlag() {
		return txnReversalFlag;
	}

	public void setTxnReversalFlag(String txnReversalFlag) {
		this.txnReversalFlag = txnReversalFlag;
	}

	public String getProductFunding() {
		return productFunding;
	}

	public void setProductFunding(String productFunding) {
		this.productFunding = productFunding;
	}

	public java.sql.Date getSysDate() {
		return sysDate;
	}

	public void setSysDate(String sysDate) {
		if (sysDate != null) {
			this.sysDate = Util.formatDate(ValueObjectKeys.DATE_FORMAT, sysDate);
		} else {
			this.sysDate = new java.sql.Date(System.currentTimeMillis());
		}
	}

	public boolean isFirstTimeTopUp() {
		if ("Y".equalsIgnoreCase(firstTimeTopupFlag)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCardStatusConsumed() {
		if (TransactionConstant.CONSUMED.equalsIgnoreCase(cardStatus)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCredit() {
		if (TransactionConstant.CREDIT_CARD.equalsIgnoreCase(creditDebitFlg)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isDebit() {
		if (TransactionConstant.DEBIT_CARD.equalsIgnoreCase(creditDebitFlg)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isFinancial() {
		if (!TransactionConstant.NONFINANCIAL_CARD.equalsIgnoreCase(creditDebitFlg)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isOrderFulfillment() {
		boolean isOrderFulfillment = false;
		if ("ORDER_FULFILLMENT".equalsIgnoreCase(productFunding)) {
			isOrderFulfillment = true;
		}
		return isOrderFulfillment;

	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setSysDate(java.sql.Date sysDate) {
		this.sysDate = sysDate;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getDigitalPin() {
		return digitalPin;
	}

	public void setDigitalPin(String digitalPin) {
		this.digitalPin = digitalPin;
	}

	public String getOrgnlTxnDate() {
		return orgnlTxnDate;
	}

	public void setOrgnlTxnDate(String orgnlTxnDate) {
		this.orgnlTxnDate = orgnlTxnDate;
	}

	public String getOrgnlTxnTime() {
		return orgnlTxnTime;
	}

	public void setOrgnlTxnTime(String orgnlTxnTime) {
		this.orgnlTxnTime = orgnlTxnTime;
	}

	public String getOrgnlCardNumHash() {
		return orgnlCardNumHash;
	}

	public void setOrgnlCardNumHash(String orgnlCardNumHash) {
		this.orgnlCardNumHash = orgnlCardNumHash;
	}

	public String getOrgnlRrn() {
		return orgnlRrn;
	}

	public void setOrgnlRrn(String orgnlRrn) {
		this.orgnlRrn = orgnlRrn;
	}

	public String getOrgnlTerminalId() {
		return orgnlTerminalId;
	}

	public void setOrgnlTerminalId(String orgnlTerminalId) {
		this.orgnlTerminalId = orgnlTerminalId;
	}

	public double getOrgnlTxnFee() {
		return orgnlTxnFee;
	}

	public void setOrgnlTxnFee(double orgnlTxnFee) {
		this.orgnlTxnFee = Precision.round(orgnlTxnFee, scale);
	}

	public double getOrgnlTxnAmt() {
		return orgnlTxnAmt;
	}

	public void setOrgnlTxnAmt(double orgnlTxnAmt) {
		this.orgnlTxnAmt = Precision.round(orgnlTxnAmt, scale);
	}

	public double getOrgnAuthAmt() {
		return orgnAuthAmt;
	}

	public void setOrgnAuthAmt(double orgnAuthAmt) {
		this.orgnAuthAmt = Precision.round(orgnAuthAmt, scale);

	}

	public AccountPurseBalance getPurAuthReq() {
		return purAuthReq;
	}

	public void setPurAuthReq(AccountPurseBalance purAuthReq) {
		this.purAuthReq = purAuthReq;
	}

	public long getAccountPurseId() {
		return accountPurseId;
	}

	public void setAccountPurseId(long accountPurseId) {
		this.accountPurseId = accountPurseId;
	}

	public int getPurseTypeId() {
		return purseTypeId;
	}

	public void setPurseTypeId(int purseTypeId) {
		this.purseTypeId = purseTypeId;
	}

	public String getPurseType() {
		return purseType;
	}

	public void setPurseType(String purseType) {
		this.purseType = purseType;
	}

	public double getMaxPurseBalance() {
		return maxPurseBalance;
	}

	public void setMaxPurseBalance(double maxPurseBalance) {
		this.maxPurseBalance = Precision.round(maxPurseBalance, scale);
	}

	public void setMaxPurseBalance(Object maxPurseBalance) {
		if (maxPurseBalance != null && !"".equals(maxPurseBalance.toString())) {
			this.maxPurseBalance = Precision.round(Double.parseDouble(maxPurseBalance.toString()), scale);
		} else if (this.purAuthReq != null) {
			this.maxPurseBalance = Double.NaN;
		} else {
			this.maxPurseBalance = 0;
		}
	}

	public String getPurseName() {
		return purseName;
	}

	public void setPurseName(String purseName) {
		this.purseName = purseName;
	}

	public void setNewPurse(boolean newPurse) {
		this.newPurse = newPurse;
	}

	public boolean isNewPurse() {
		return this.newPurse;
	}

	public boolean isExtPurse() {
		boolean newPurse = true;
		if (this.purAuthReq == null)
			newPurse = false;

		return newPurse;
	}

	public BigInteger getDefaultPurseId() {
		return defaultPurseId;
	}

	public void setDefaultPurseId(BigInteger defaultPurseId) {
		this.defaultPurseId = defaultPurseId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public double getAvailOpeningBalance() {
		return availOpeningBalance;
	}

	public void setAvailOpeningBalance(double availOpeningBalance) {
		this.availOpeningBalance = availOpeningBalance;
	}

	public double getAvailClosingBalance() {
		return availClosingBalance;
	}

	public void setAvailClosingBalance(double availClosingBalance) {
		this.availClosingBalance = availClosingBalance;
	}

	@Override
	public String toString() {
		return "RequestInfo [cardNum=" + cardNum + ", cardNumHash=" + cardNumHash + ", cardNumEncr=" + cardNumEncr + ", openingBalance="
				+ openingBalance + ", openingLedgerBalance=" + openingLedgerBalance + ", openingAvailBalance=" + openingAvailBalance
				+ ", closingBalance=" + closingBalance + ", closingLedgerBalance=" + closingLedgerBalance + ", closingAvailBalance="
				+ closingAvailBalance + ", txnAmount=" + txnAmount + ", txnFee=" + txnFee + ", totalAmount=" + totalAmount
				+ ", creditDebitFlg=" + creditDebitFlg + ", txnNarration=" + txnNarration + ", txnDesc=" + txnDesc + ", lastUpdDate="
				+ lastUpdDate + ", insDate=" + insDate + ", rrn=" + rrn + ", authId=" + authId + ", txnDate=" + txnDate + ", txnTime="
				+ txnTime + ", txnTimeZone=" + txnTimeZone + ", lastTxnDate=" + lastTxnDate + ", partialPreAuthInd=" + partialPreAuthInd
				+ ", feeFlg=" + feeFlg + ", freeFeeFlg=" + freeFeeFlg + ", firstTimeTopupFlag=" + firstTimeTopupFlag + ", maxFeeFlg="
				+ maxFeeFlg + ", prodFeeCondition=" + prodFeeCondition + ", prodMinFeeAmount=" + prodMinFeeAmount
				+ ", prodPercentFeeAmount=" + prodPercentFeeAmount + ", prodFlatFeeAmount=" + prodFlatFeeAmount + ", prodTxnFeeAmount="
				+ prodTxnFeeAmount + ", maxCardBal=" + maxCardBal + ", redemDelayTranAmount=" + redemDelayTranAmount + ", feeAmount="
				+ feeAmount + ", authorizedAmount=" + authorizedAmount + ", productFeeCond=" + productFeeCond + ", upc=" + upc
				+ ", channel=" + channel + ", msgType=" + msgType + ", txnCode=" + txnCode + ", txnType=" + txnType + ", orgnlTranCurr="
				+ orgnlTranCurr + ", accountId=" + accountId + ", toAccountId=" + toAccountId + ", accountNumber=" + accountNumber
				+ ", merchantName=" + merchantName + ", merchantCity=" + merchantCity + ", merchantState=" + merchantState
				+ ", merchantRefNum=" + merchantRefNum + ", merchantId=" + merchantId + ", locationId=" + locationId + ", localeCountry="
				+ localeCountry + ", localeLanguage=" + localeLanguage + ", localeCurrency=" + localeCurrency + ", cardLast4digits="
				+ cardLast4digits + ", recordSeq=" + recordSeq + ", productId=" + productId + ", issuerId=" + issuerId + ", partnerId="
				+ partnerId + ", mdmId=" + mdmId + ", purseId=" + purseId + ", toPurseId=" + toPurseId + ", txnSeqId=" + txnSeqId
				+ ", proxyNumber=" + proxyNumber + ", businessDate=" + businessDate + ", posEntryMode=" + posEntryMode
				+ ", posConditionCode=" + posConditionCode + ", storeId=" + storeId + ", storeAddress1=" + storeAddress1
				+ ", storeAddress2=" + storeAddress2 + ", storeCity=" + storeCity + ", storeState=" + storeState + ", terminalId="
				+ terminalId + ", cashierId=" + cashierId + ", cardStatus=" + cardStatus + ", oldCardStatus=" + oldCardStatus
				+ ", partySupported=" + partySupported + ", expiryDate=" + expiryDate + ", respCode=" + respCode + ", reversalCode="
				+ reversalCode + ", errorMsg=" + errorMsg + ", txnReversalFlag=" + txnReversalFlag + ", productFunding=" + productFunding
				+ ", sysDate=" + sysDate + ", serialNumber=" + serialNumber + ", digitalPin=" + digitalPin + ", orgnlTxnDate="
				+ orgnlTxnDate + ", orgnlTxnTime=" + orgnlTxnTime + ", orgnlCardNumHash=" + orgnlCardNumHash + ", orgnlRrn=" + orgnlRrn
				+ ", orgnlTerminalId=" + orgnlTerminalId + ", orgnlTxnFee=" + orgnlTxnFee + ", orgnlTxnAmt=" + orgnlTxnAmt
				+ ", orgnAuthAmt=" + orgnAuthAmt + ", accountPurseId=" + accountPurseId + ", purseTypeId=" + purseTypeId + ", purseType="
				+ purseType + ", reqTxnAmt=" + reqTxnAmt + ", reqAuthAmt=" + reqAuthAmt + ", tranCurr=" + tranCurr + ", convRate="
				+ convRate + ", valueDTO=" + valueDTO + ", scale=" + scale + ", purAuthReq=" + purAuthReq + ", purseName=" + purseName
				+ ", maxPurseBalance=" + maxPurseBalance + ", defaultPurseId=" + defaultPurseId + ", newPurse=" + newPurse + ", sourceName="
				+ sourceName + "]";
	}

}
