package com.incomm.cclp.fsapi.bean;

public class FsApiTransaction {

	private String channelCode;

	private String msgType;

	private String tranCode;

	private String daoCName;

	private String requestType;

	private String requestMethod;

	private String validationName;

	private String verifyCName;

	private String reversalTransaction;

	private String actionName;

	private String logExemption;

	private String channelDescritption;

	private String isFinancial;

	private String creditDebitIndicator;

	private String channelShortName;

	private String passiveSupported;

	private String transactionDesc;

	private String transactionShortName;

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getTranCode() {
		return tranCode;
	}

	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

	public String getDaoCName() {
		return daoCName;
	}

	public void setDaoCName(String daoCName) {
		this.daoCName = daoCName;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getValidationName() {
		return validationName;
	}

	public void setValidationName(String validationName) {
		this.validationName = validationName;
	}

	public String getVerifyCName() {
		return verifyCName;
	}

	public void setVerifyCName(String verifyCName) {
		this.verifyCName = verifyCName;
	}

	public String getReversalTransaction() {
		return reversalTransaction;
	}

	public void setReversalTransaction(String reversalTransaction) {
		this.reversalTransaction = reversalTransaction;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getLogExemption() {
		return logExemption;
	}

	public void setLogExemption(String logExemption) {
		this.logExemption = logExemption;
	}

	public String getChannelDescritption() {
		return channelDescritption;
	}

	public void setChannelDescritption(String channelDescritption) {
		this.channelDescritption = channelDescritption;
	}

	public String getIsFinancial() {
		return isFinancial;
	}

	public void setIsFinancial(String isFinancial) {
		this.isFinancial = isFinancial;
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

	public String getTransactionDesc() {
		return transactionDesc;
	}

	public void setTransactionDesc(String transactionDesc) {
		this.transactionDesc = transactionDesc;
	}

	public String getTransactionShortName() {
		return transactionShortName;
	}

	public void setTransactionShortName(String transactionShortName) {
		this.transactionShortName = transactionShortName;
	}

	@Override
	public String toString() {
		return "FsApiTransaction [channelCode=" + channelCode + ", msgType=" + msgType + ", tranCode=" + tranCode
				+ ", daoCName=" + daoCName + ", requestType=" + requestType + ", requestMethod=" + requestMethod
				+ ", validationName=" + validationName + ", verifyCName=" + verifyCName + ", reversalTransaction="
				+ reversalTransaction + ", actionName=" + actionName + ", logExemption=" + logExemption
				+ ", channelDescritption=" + channelDescritption + ", isFinancial=" + isFinancial
				+ ", creditDebitIndicator=" + creditDebitIndicator + ", channelShortName=" + channelShortName
				+ ", passiveSupported=" + passiveSupported + ", transactionDesc=" + transactionDesc
				+ ", transactionShortName=" + transactionShortName + "]";
	}

}
