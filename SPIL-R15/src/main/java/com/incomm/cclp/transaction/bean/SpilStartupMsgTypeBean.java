package com.incomm.cclp.transaction.bean;

/**
 * This Class represents to load the values on Server Startup
 * 
 * @author Venkatesh Gaddam
 *
 */

public class SpilStartupMsgTypeBean {

	private String deliveryChannel;
	private String txnCode;
	private String msgType;
	private String spilMsgType;
	private String partySupported;
	private String authJavaClass;
	private String isFinacial;
	private String creditDebitIndicator;
	private String channelShortName;
	private String passiveSupported;
	private String transactionDesc;
	private String transactionShortName;

	public String getTransactionShortName() {
		return transactionShortName;
	}

	public void setTransactionShortName(String transactionShortName) {
		this.transactionShortName = transactionShortName;
	}

	public String getTransactionDesc() {
		return transactionDesc;
	}

	public void setTransactionDesc(String transactionDesc) {
		this.transactionDesc = transactionDesc;
	}

	public String getDeliveryChannel() {
		return deliveryChannel;
	}

	public void setDeliveryChannel(String deliveryChannel) {
		this.deliveryChannel = deliveryChannel;
	}

	public String getTxnCode() {
		return txnCode;
	}

	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getSpilMsgType() {
		return spilMsgType;
	}

	public void setSpilMsgType(String spilMsgType) {
		this.spilMsgType = spilMsgType;
	}

	public String getPartySupported() {
		return partySupported;
	}

	public void setPartySupported(String partySupported) {
		this.partySupported = partySupported;
	}

	public String getAuthJavaClass() {
		return authJavaClass;
	}

	public void setAuthJavaClass(String authJavaClass) {
		this.authJavaClass = authJavaClass;
	}

	public String getIsFinacial() {
		return isFinacial;
	}

	public void setIsFinacial(String isFinacial) {
		this.isFinacial = isFinacial;
	}

	public String getCreditDebitIndicator() {
		return creditDebitIndicator;
	}

	public void setCreditDebitIndicator(String creditDebitIndicator) {
		this.creditDebitIndicator = creditDebitIndicator;
	}

	public String getChannelShortName() {
		return channelShortName;
	}

	public void setChannelShortName(String channelShortName) {
		this.channelShortName = channelShortName;
	}

	public String getPassiveSupported() {
		return passiveSupported;
	}

	public void setPassiveSupported(String passiveSupported) {
		this.passiveSupported = passiveSupported;
	}

	@Override
	public String toString() {
		return "SpilStartupMsgTypeBean [deliveryChannel=" + deliveryChannel + ", txnCode=" + txnCode + ", msgType=" + msgType
				+ ", spilMsgType=" + spilMsgType + ", partySupported=" + partySupported + ", authJavaClass=" + authJavaClass
				+ ", isFinacial=" + isFinacial + ", creditDebitIndicator=" + creditDebitIndicator + ", channelShortName=" + channelShortName
				+ ", passiveSupported=" + passiveSupported + ", transactionShortName=" + transactionShortName + "]";
	}

}
