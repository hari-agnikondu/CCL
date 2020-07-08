package com.incomm.scheduler.model;

import java.io.Serializable;

public class PostBackLogDtl implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String orderId;
	private String apiName;
	private String seqNo;
	private String  reqMsg;
	private String  reqHeader;
	private String  resHeader;
	private String  responseCode;
	private String  responseMsg;
	private String  responseCount;
	private String  responseFlag;
	private String  postBackStatus;
	private String  postBackUrl;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getReqMsg() {
		return reqMsg;
	}
	public void setReqMsg(String reqMsg) {
		this.reqMsg = reqMsg;
	}
	public String getReqHeader() {
		return reqHeader;
	}
	public void setReqHeader(String reqHeader) {
		this.reqHeader = reqHeader;
	}
	public String getResHeader() {
		return resHeader;
	}
	public void setResHeader(String resHeader) {
		this.resHeader = resHeader;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public String getResponseCount() {
		return responseCount;
	}
	public void setResponseCount(String responseCount) {
		this.responseCount = responseCount;
	}
	public String getResponseFlag() {
		return responseFlag;
	}
	public void setResponseFlag(String responseFlag) {
		this.responseFlag = responseFlag;
	}
	public String getPostBackStatus() {
		return postBackStatus;
	}
	public void setPostBackStatus(String postBackStatus) {
		this.postBackStatus = postBackStatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPostBackUrl() {
		return postBackUrl;
	}
	public void setPostBackUrl(String postBackUrl) {
		this.postBackUrl = postBackUrl;
	}
	
	
	
}
