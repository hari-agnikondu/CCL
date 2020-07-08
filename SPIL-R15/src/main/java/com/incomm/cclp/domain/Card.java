/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.incomm.cclp.constants.QueryConstants;

/**
 *
 * @author skocherla
 */
@Entity
@Table(name = "CARD")
@XmlRootElement

@NamedNativeQueries({ @NamedNativeQuery(name = "Card.getDamagedCardCount", query = QueryConstants.GET_DAMAGED_CARD_COUNT),
		@NamedNativeQuery(name = "Card.getReplacedCardStatus", query = QueryConstants.GET_REPLACED_CARD_STATUS, resultSetMapping = "CardInfoResult") })
@SqlResultSetMapping(name = "CardInfoResult", classes = { @ConstructorResult(targetClass = CardInfo.class, columns = {
		@ColumnResult(name = "oldCardStatus", type = String.class), @ColumnResult(name = "cardNumHash", type = String.class) }) })

@NamedQueries({ @NamedQuery(name = "Card.findAll", query = "SELECT c FROM Card c"),
		@NamedQuery(name = "Card.findByCardNumHash", query = "SELECT c FROM Card c WHERE c.cardNumHash = :cardNumHash"),
		@NamedQuery(name = "Card.findByCardNumEncr", query = "SELECT c FROM Card c WHERE c.cardNumEncr = :cardNumEncr"),
		@NamedQuery(name = "Card.findByCardNumMask", query = "SELECT c FROM Card c WHERE c.cardNumMask = :cardNumMask"),
		@NamedQuery(name = "Card.findBySerialNumber", query = "SELECT c FROM Card c WHERE c.serialNumber = :serialNumber"),
		@NamedQuery(name = "Card.findByProductId", query = "SELECT c FROM Card c WHERE c.productId = :productId"),
		@NamedQuery(name = "Card.findByCardRangeId", query = "SELECT c FROM Card c WHERE c.cardRangeId = :cardRangeId"),
		@NamedQuery(name = "Card.findByProxyNumber", query = "SELECT c FROM Card c WHERE c.proxyNumber = :proxyNumber"),
		@NamedQuery(name = "Card.findByCardStatus", query = "SELECT c FROM Card c WHERE c.cardStatus = :cardStatus"),
		@NamedQuery(name = "Card.findByExpiryDate", query = "SELECT c FROM Card c WHERE c.expiryDate = :expiryDate"),
		@NamedQuery(name = "Card.findByPanGenerationDate", query = "SELECT c FROM Card c WHERE c.panGenerationDate = :panGenerationDate"),
		@NamedQuery(name = "Card.findByDateOfActivation", query = "SELECT c FROM Card c WHERE c.dateOfActivation = :dateOfActivation"),
		@NamedQuery(name = "Card.findByInsUser", query = "SELECT c FROM Card c WHERE c.insUser = :insUser"),
		@NamedQuery(name = "Card.findByInsDate", query = "SELECT c FROM Card c WHERE c.insDate = :insDate"),
		@NamedQuery(name = "Card.findByLastUpdUser", query = "SELECT c FROM Card c WHERE c.lastUpdUser = :lastUpdUser"),
		@NamedQuery(name = "Card.findByLastUpdDate", query = "SELECT c FROM Card c WHERE c.lastUpdDate = :lastUpdDate"),
		@NamedQuery(name = "Card.findByPinOff", query = "SELECT c FROM Card c WHERE c.pinOff = :pinOff"),
		@NamedQuery(name = "Card.findByDispName", query = "SELECT c FROM Card c WHERE c.dispName = :dispName"),
		@NamedQuery(name = "Card.findByNextBillDate", query = "SELECT c FROM Card c WHERE c.nextBillDate = :nextBillDate"),
		@NamedQuery(name = "Card.findByNextMbDate", query = "SELECT c FROM Card c WHERE c.nextMbDate = :nextMbDate"),
		@NamedQuery(name = "Card.findByIssueFlag", query = "SELECT c FROM Card c WHERE c.issueFlag = :issueFlag"),
		@NamedQuery(name = "Card.findByIpinOffset", query = "SELECT c FROM Card c WHERE c.ipinOffset = :ipinOffset"),
		@NamedQuery(name = "Card.findByFirsttimeTopup", query = "SELECT c FROM Card c WHERE c.firsttimeTopup = :firsttimeTopup"),
		@NamedQuery(name = "Card.findByStartercardFlag", query = "SELECT c FROM Card c WHERE c.startercardFlag = :startercardFlag"),
		@NamedQuery(name = "Card.findByInactiveFeecalcDate", query = "SELECT c FROM Card c WHERE c.inactiveFeecalcDate = :inactiveFeecalcDate"),
		@NamedQuery(name = "Card.findByPrflCode", query = "SELECT c FROM Card c WHERE c.prflCode = :prflCode"),
		@NamedQuery(name = "Card.findByPrflLevl", query = "SELECT c FROM Card c WHERE c.prflLevl = :prflLevl"),
		@NamedQuery(name = "Card.findByReplFlag", query = "SELECT c FROM Card c WHERE c.replFlag = :replFlag"),
		@NamedQuery(name = "Card.findByOldCardstat", query = "SELECT c FROM Card c WHERE c.oldCardstat = :oldCardstat"),
		@NamedQuery(name = "Card.findByLastTxndate", query = "SELECT c FROM Card c WHERE c.lastTxndate = :lastTxndate"),
		@NamedQuery(name = "Card.findByCardpackId", query = "SELECT c FROM Card c WHERE c.cardpackId = :cardpackId"),
		@NamedQuery(name = "Card.findByCvvplusRegFlag", query = "SELECT c FROM Card c WHERE c.cvvplusRegFlag = :cvvplusRegFlag"),
		@NamedQuery(name = "Card.findByCvvplusActiveFlag", query = "SELECT c FROM Card c WHERE c.cvvplusActiveFlag = :cvvplusActiveFlag"),
		@NamedQuery(name = "Card.findByActivationCode", query = "SELECT c FROM Card c WHERE c.activationCode = :activationCode"),
		@NamedQuery(name = "Card.findByReplaceExprydt", query = "SELECT c FROM Card c WHERE c.replaceExprydt = :replaceExprydt"),
		@NamedQuery(name = "Card.findByMerchantId", query = "SELECT c FROM Card c WHERE c.merchantId = :merchantId"),
		@NamedQuery(name = "Card.findByMerchantName", query = "SELECT c FROM Card c WHERE c.merchantName = :merchantName"),
		@NamedQuery(name = "Card.findByStoreId", query = "SELECT c FROM Card c WHERE c.storeId = :storeId"),
		@NamedQuery(name = "Card.findByTerminalId", query = "SELECT c FROM Card c WHERE c.terminalId = :terminalId"),
		@NamedQuery(name = "Card.findByIpAddress", query = "SELECT c FROM Card c WHERE c.ipAddress = :ipAddress"),
		@NamedQuery(name = "Card.findByUrl", query = "SELECT c FROM Card c WHERE c.url = :url"),
		@NamedQuery(name = "Card.findByUserIdentifyType", query = "SELECT c FROM Card c WHERE c.userIdentifyType = :userIdentifyType"),
		@NamedQuery(name = "Card.findByMerchantBillable", query = "SELECT c FROM Card c WHERE c.merchantBillable = :merchantBillable"),
		@NamedQuery(name = "Card.findByReplaceTerminalId", query = "SELECT c FROM Card c WHERE c.replaceTerminalId = :replaceTerminalId"),
		@NamedQuery(name = "Card.findByReplaceMerchantId", query = "SELECT c FROM Card c WHERE c.replaceMerchantId = :replaceMerchantId"),
		@NamedQuery(name = "Card.findByReplaceLocationId", query = "SELECT c FROM Card c WHERE c.replaceLocationId = :replaceLocationId"),
		@NamedQuery(name = "Card.findByBillAddr", query = "SELECT c FROM Card c WHERE c.billAddr = :billAddr"),
		@NamedQuery(name = "Card.findByMbrNumb", query = "SELECT c FROM Card c WHERE c.mbrNumb = :mbrNumb"),
		@NamedQuery(name = "Card.findByIsRedeemed", query = "SELECT c FROM Card c WHERE c.isRedeemed = :isRedeemed"),
		@NamedQuery(name = "Card.findByLimitProfile", query = "SELECT c FROM Card c WHERE c.limitProfile = :limitProfile"),
		@NamedQuery(name = "Card.findByFeeProfile", query = "SELECT c FROM Card c WHERE c.feeProfile = :feeProfile"),
		@NamedQuery(name = "Card.findByCardId", query = "SELECT c FROM Card c WHERE c.cardId = :cardId"),
		@NamedQuery(name = "Card.findByPinFlag", query = "SELECT c FROM Card c WHERE c.pinFlag = :pinFlag"),
		@NamedQuery(name = "Card.findByNextWbDate", query = "SELECT c FROM Card c WHERE c.nextWbDate = :nextWbDate"),
		@NamedQuery(name = "Card.findByInUse", query = "SELECT c FROM Card c WHERE c.inUse = :inUse"),
		@NamedQuery(name = "Card.findByWrongLoginCnt", query = "SELECT c FROM Card c WHERE c.wrongLoginCnt = :wrongLoginCnt"),
		@NamedQuery(name = "Card.findByPingenDate", query = "SELECT c FROM Card c WHERE c.pingenDate = :pingenDate"),
		@NamedQuery(name = "Card.findByPinCnt", query = "SELECT c FROM Card c WHERE c.pinCnt = :pinCnt"),
		@NamedQuery(name = "Card.findByPinRetryTime", query = "SELECT c FROM Card c WHERE c.pinRetryTime = :pinRetryTime") })
public class Card implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "CARD_NUM_HASH")
	private String cardNumHash;
	@Size(max = 100)
	@Column(name = "CARD_NUM_ENCR")
	private String cardNumEncr;
	@Size(max = 100)
	@Column(name = "CARD_NUM_MASK")
	private String cardNumMask;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "SERIAL_NUMBER")
	private String serialNumber;
	@Basic(optional = false)
	@NotNull
	@Column(name = "PRODUCT_ID")
	private BigInteger productId;
	@Column(name = "CARD_RANGE_ID")
	private BigInteger cardRangeId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 19)
	@Column(name = "PROXY_NUMBER")
	private String proxyNumber;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2)
	@Column(name = "CARD_STATUS")
	private String cardStatus;
	@Column(name = "EXPIRY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDate;
	@Column(name = "PAN_GENERATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date panGenerationDate;
	@Column(name = "DATE_OF_ACTIVATION", nullable = true)
//    @Temporal(TemporalType.TIMESTAMP)
	private Date dateOfActivation;
	@Column(name = "INS_USER")
	private BigInteger insUser;
	@Column(name = "INS_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insDate;
	@Column(name = "LAST_UPD_USER")
	private BigInteger lastUpdUser;
	@Column(name = "LAST_UPD_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdDate;
	@Size(max = 10)
	@Column(name = "PIN_OFF")
	private String pinOff;
	@Size(max = 2000)
	@Column(name = "DISP_NAME")
	private String dispName;
	@Column(name = "NEXT_BILL_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date nextBillDate;
	@Column(name = "NEXT_MB_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date nextMbDate;
	@Size(max = 1)
	@Column(name = "ISSUE_FLAG")
	private String issueFlag;
	@Size(max = 100)
	@Column(name = "IPIN_OFFSET")
	private String ipinOffset;
	@Size(max = 1)
	@Column(name = "FIRSTTIME_TOPUP")
	private String firsttimeTopup;
	@Size(max = 1)
	@Column(name = "STARTERCARD_FLAG")
	private String startercardFlag;
	@Column(name = "INACTIVE_FEECALC_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date inactiveFeecalcDate;
	@Size(max = 10)
	@Column(name = "PRFL_CODE")
	private String prflCode;
	@Column(name = "PRFL_LEVL")
	private Short prflLevl;
	@Column(name = "REPL_FLAG")
	private Short replFlag;
	@Size(max = 2)
	@Column(name = "OLD_CARDSTAT")
	private String oldCardstat;
	@Column(name = "LAST_TXNDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastTxndate;
	@Column(name = "CARDPACK_ID")
	private Integer cardpackId;
	@Size(max = 20)
	@Column(name = "CVVPLUS_REG_FLAG")
	private String cvvplusRegFlag;
	@Size(max = 20)
	@Column(name = "CVVPLUS_ACTIVE_FLAG")
	private String cvvplusActiveFlag;
	@Size(max = 20)
	@Column(name = "ACTIVATION_CODE")
	private String activationCode;
	@Column(name = "REPLACE_EXPRYDT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date replaceExprydt;
	@Size(max = 50)
	@Column(name = "MERCHANT_ID")
	private String merchantId;
	@Size(max = 50)
	@Column(name = "MERCHANT_NAME")
	private String merchantName;
	@Size(max = 100)
	@Column(name = "STORE_ID")
	private String storeId;
	@Size(max = 50)
	@Column(name = "TERMINAL_ID")
	private String terminalId;
	@Size(max = 50)
	@Column(name = "IP_ADDRESS")
	private String ipAddress;
	@Size(max = 200)
	@Column(name = "URL")
	private String url;
	@Size(max = 20)
	@Column(name = "USER_IDENTIFY_TYPE")
	private String userIdentifyType;
	@Size(max = 20)
	@Column(name = "MERCHANT_BILLABLE")
	private String merchantBillable;
	@Size(max = 50)
	@Column(name = "REPLACE_TERMINAL_ID")
	private String replaceTerminalId;
	@Size(max = 50)
	@Column(name = "REPLACE_MERCHANT_ID")
	private String replaceMerchantId;
	@Size(max = 50)
	@Column(name = "REPLACE_LOCATION_ID")
	private String replaceLocationId;
	@Size(max = 20)
	@Column(name = "BILL_ADDR")
	private String billAddr;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "MBR_NUMB")
	private String mbrNumb;
	@Lob
	@Column(name = "ATTRIBUTES")
	private String attributes;
	@Lob
	@Column(name = "USAGE_LIMIT")
	private String usageLimit;
	@Size(max = 3)
	@Column(name = "IS_REDEEMED")
	private String isRedeemed;
	@Size(max = 20)
	@Column(name = "LIMIT_PROFILE")
	private String limitProfile;
	@Size(max = 20)
	@Column(name = "FEE_PROFILE")
	private String feeProfile;
	@Lob
	@Column(name = "USAGE_FEE")
	private String usageFee;
	@Size(max = 20)
	@Column(name = "CARD_ID")
	private String cardId;
	@Size(max = 1)
	@Column(name = "PIN_FLAG")
	private String pinFlag;
	@Column(name = "NEXT_WB_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date nextWbDate;
	@Size(max = 1)
	@Column(name = "IN_USE")
	private String inUse;
	@Basic(optional = false)
	@NotNull
	@Column(name = "WRONG_LOGIN_CNT")
	private BigInteger wrongLoginCnt;
	@Column(name = "PINGEN_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pingenDate;
	@Basic(optional = false)
	@NotNull
	@Column(name = "PIN_CNT")
	private BigInteger pinCnt;
	@Column(name = "PIN_RETRY_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pinRetryTime;
	@JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID")
	@ManyToOne(optional = false)
	private Account accountId;
	@JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
	@ManyToOne
	private Address addressId;
	@JoinColumn(name = "CUSTOMER_CODE", referencedColumnName = "CUSTOMER_CODE")
	@ManyToOne
	private CustomerProfile customerCode;

	public Card() {
	}

	public Card(String cardNumHash) {
		this.cardNumHash = cardNumHash;
	}

	public Card(String cardNumHash, String serialNumber, BigInteger productId, String proxyNumber, String cardStatus, String mbrNumb,
			BigInteger wrongLoginCnt, BigInteger pinCnt) {
		this.cardNumHash = cardNumHash;
		this.serialNumber = serialNumber;
		this.productId = productId;
		this.proxyNumber = proxyNumber;
		this.cardStatus = cardStatus;
		this.mbrNumb = mbrNumb;
		this.wrongLoginCnt = wrongLoginCnt;
		this.pinCnt = pinCnt;
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

	public String getCardNumMask() {
		return cardNumMask;
	}

	public void setCardNumMask(String cardNumMask) {
		this.cardNumMask = cardNumMask;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public BigInteger getProductId() {
		return productId;
	}

	public void setProductId(BigInteger productId) {
		this.productId = productId;
	}

	public BigInteger getCardRangeId() {
		return cardRangeId;
	}

	public void setCardRangeId(BigInteger cardRangeId) {
		this.cardRangeId = cardRangeId;
	}

	public String getProxyNumber() {
		return proxyNumber;
	}

	public void setProxyNumber(String proxyNumber) {
		this.proxyNumber = proxyNumber;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getPanGenerationDate() {
		return panGenerationDate;
	}

	public void setPanGenerationDate(Date panGenerationDate) {
		this.panGenerationDate = panGenerationDate;
	}

	public Date getDateOfActivation() {
		return dateOfActivation;
	}

	public void setDateOfActivation(Date dateOfActivation) {
		this.dateOfActivation = dateOfActivation;
	}

	public BigInteger getInsUser() {
		return insUser;
	}

	public void setInsUser(BigInteger insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public BigInteger getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(BigInteger lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public String getPinOff() {
		return pinOff;
	}

	public void setPinOff(String pinOff) {
		this.pinOff = pinOff;
	}

	public String getDispName() {
		return dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	public Date getNextBillDate() {
		return nextBillDate;
	}

	public void setNextBillDate(Date nextBillDate) {
		this.nextBillDate = nextBillDate;
	}

	public Date getNextMbDate() {
		return nextMbDate;
	}

	public void setNextMbDate(Date nextMbDate) {
		this.nextMbDate = nextMbDate;
	}

	public String getIssueFlag() {
		return issueFlag;
	}

	public void setIssueFlag(String issueFlag) {
		this.issueFlag = issueFlag;
	}

	public String getIpinOffset() {
		return ipinOffset;
	}

	public void setIpinOffset(String ipinOffset) {
		this.ipinOffset = ipinOffset;
	}

	public String getFirsttimeTopup() {
		return firsttimeTopup;
	}

	public void setFirsttimeTopup(String firsttimeTopup) {
		this.firsttimeTopup = firsttimeTopup;
	}

	public String getStartercardFlag() {
		return startercardFlag;
	}

	public void setStartercardFlag(String startercardFlag) {
		this.startercardFlag = startercardFlag;
	}

	public Date getInactiveFeecalcDate() {
		return inactiveFeecalcDate;
	}

	public void setInactiveFeecalcDate(Date inactiveFeecalcDate) {
		this.inactiveFeecalcDate = inactiveFeecalcDate;
	}

	public String getPrflCode() {
		return prflCode;
	}

	public void setPrflCode(String prflCode) {
		this.prflCode = prflCode;
	}

	public Short getPrflLevl() {
		return prflLevl;
	}

	public void setPrflLevl(Short prflLevl) {
		this.prflLevl = prflLevl;
	}

	public Short getReplFlag() {
		return replFlag;
	}

	public void setReplFlag(Short replFlag) {
		this.replFlag = replFlag;
	}

	public String getOldCardstat() {
		return oldCardstat;
	}

	public void setOldCardstat(String oldCardstat) {
		this.oldCardstat = oldCardstat;
	}

	public Date getLastTxndate() {
		return lastTxndate;
	}

	public void setLastTxndate(Date lastTxndate) {
		this.lastTxndate = lastTxndate;
	}

	public Integer getCardpackId() {
		return cardpackId;
	}

	public void setCardpackId(Integer cardpackId) {
		this.cardpackId = cardpackId;
	}

	public String getCvvplusRegFlag() {
		return cvvplusRegFlag;
	}

	public void setCvvplusRegFlag(String cvvplusRegFlag) {
		this.cvvplusRegFlag = cvvplusRegFlag;
	}

	public String getCvvplusActiveFlag() {
		return cvvplusActiveFlag;
	}

	public void setCvvplusActiveFlag(String cvvplusActiveFlag) {
		this.cvvplusActiveFlag = cvvplusActiveFlag;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public Date getReplaceExprydt() {
		return replaceExprydt;
	}

	public void setReplaceExprydt(Date replaceExprydt) {
		this.replaceExprydt = replaceExprydt;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserIdentifyType() {
		return userIdentifyType;
	}

	public void setUserIdentifyType(String userIdentifyType) {
		this.userIdentifyType = userIdentifyType;
	}

	public String getMerchantBillable() {
		return merchantBillable;
	}

	public void setMerchantBillable(String merchantBillable) {
		this.merchantBillable = merchantBillable;
	}

	public String getReplaceTerminalId() {
		return replaceTerminalId;
	}

	public void setReplaceTerminalId(String replaceTerminalId) {
		this.replaceTerminalId = replaceTerminalId;
	}

	public String getReplaceMerchantId() {
		return replaceMerchantId;
	}

	public void setReplaceMerchantId(String replaceMerchantId) {
		this.replaceMerchantId = replaceMerchantId;
	}

	public String getReplaceLocationId() {
		return replaceLocationId;
	}

	public void setReplaceLocationId(String replaceLocationId) {
		this.replaceLocationId = replaceLocationId;
	}

	public String getBillAddr() {
		return billAddr;
	}

	public void setBillAddr(String billAddr) {
		this.billAddr = billAddr;
	}

	public String getMbrNumb() {
		return mbrNumb;
	}

	public void setMbrNumb(String mbrNumb) {
		this.mbrNumb = mbrNumb;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getUsageLimit() {
		return usageLimit;
	}

	public void setUsageLimit(String usageLimit) {
		this.usageLimit = usageLimit;
	}

	public String getIsRedeemed() {
		return isRedeemed;
	}

	public void setIsRedeemed(String isRedeemed) {
		this.isRedeemed = isRedeemed;
	}

	public String getLimitProfile() {
		return limitProfile;
	}

	public void setLimitProfile(String limitProfile) {
		this.limitProfile = limitProfile;
	}

	public String getFeeProfile() {
		return feeProfile;
	}

	public void setFeeProfile(String feeProfile) {
		this.feeProfile = feeProfile;
	}

	public String getUsageFee() {
		return usageFee;
	}

	public void setUsageFee(String usageFee) {
		this.usageFee = usageFee;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getPinFlag() {
		return pinFlag;
	}

	public void setPinFlag(String pinFlag) {
		this.pinFlag = pinFlag;
	}

	public Date getNextWbDate() {
		return nextWbDate;
	}

	public void setNextWbDate(Date nextWbDate) {
		this.nextWbDate = nextWbDate;
	}

	public String getInUse() {
		return inUse;
	}

	public void setInUse(String inUse) {
		this.inUse = inUse;
	}

	public BigInteger getWrongLoginCnt() {
		return wrongLoginCnt;
	}

	public void setWrongLoginCnt(BigInteger wrongLoginCnt) {
		this.wrongLoginCnt = wrongLoginCnt;
	}

	public Date getPingenDate() {
		return pingenDate;
	}

	public void setPingenDate(Date pingenDate) {
		this.pingenDate = pingenDate;
	}

	public BigInteger getPinCnt() {
		return pinCnt;
	}

	public void setPinCnt(BigInteger pinCnt) {
		this.pinCnt = pinCnt;
	}

	public Date getPinRetryTime() {
		return pinRetryTime;
	}

	public void setPinRetryTime(Date pinRetryTime) {
		this.pinRetryTime = pinRetryTime;
	}

	public Account getAccountId() {
		return accountId;
	}

	public void setAccountId(Account accountId) {
		this.accountId = accountId;
	}

	public Address getAddressId() {
		return addressId;
	}

	public void setAddressId(Address addressId) {
		this.addressId = addressId;
	}

	public CustomerProfile getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(CustomerProfile customerCode) {
		this.customerCode = customerCode;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (cardNumHash != null ? cardNumHash.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Card)) {
			return false;
		}
		Card other = (Card) object;
		if ((this.cardNumHash == null && other.cardNumHash != null)
				|| (this.cardNumHash != null && !this.cardNumHash.equals(other.cardNumHash))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.Card[ cardNumHash=" + cardNumHash + " ]";
	}

}
