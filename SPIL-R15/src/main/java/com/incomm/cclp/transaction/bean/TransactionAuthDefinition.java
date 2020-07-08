package com.incomm.cclp.transaction.bean;

public class TransactionAuthDefinition {

	private String transactionCode;

	private String msgType;

	private String processKey;

	private String processType;

	private Long executionOrder;

	private String validationType;

	private String channelCode;

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public Long getExecutionOrder() {
		return executionOrder;
	}

	public void setExecutionOrder(Long executionOrder) {
		this.executionOrder = executionOrder;
	}

	public String getValidationType() {
		return validationType;
	}

	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}

	public TransactionAuthDefinition() {
		// Constructor
	}

	@Override
	public String toString() {
		return "TransactionAuthDefinition [transactionCode=" + transactionCode + ", msgType=" + msgType + ", processKey=" + processKey
				+ ", processType=" + processType + ", executionOrder=" + executionOrder + ", validationType=" + validationType + "]";
	}

}
