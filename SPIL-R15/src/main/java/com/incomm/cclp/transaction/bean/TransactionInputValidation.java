package com.incomm.cclp.transaction.bean;

public class TransactionInputValidation {

	private String channelCode;
	private String transactionCode;
	private String msgType;
	private String fieldName;
	private String type;
	private String pattern;
	private String parentTag;

	public String getParentTag() {
		return parentTag;
	}

	public void setParentTag(String parentTag) {
		this.parentTag = parentTag;
	}

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

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TransactionInputValidation [channelCode=" + channelCode + ", transactionCode=" + transactionCode + ", msgType=" + msgType
				+ ", fieldName=" + fieldName + ", type=" + type + ", pattern=" + pattern + "]";
	}

}
