package com.incomm.cclp.fsapi.bean;

import java.util.Map;


public class FsApiDetail {
	private String apiName;

	private String apiUrl;
	private String subTagRespField;
	private String subTagReqField;
	private String requestMetod;

	private Map<String, String> reqFields;
	
	private Map<String, String> resFields;
	
	private Map<String, Map<String, String>> resSubTagFields;
	
	private Map<String, Map<String, String>> reqSubTagFields;

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getRequestMetod() {
		return requestMetod;
	}

	public void setRequestMetod(String requestMetod) {
		this.requestMetod = requestMetod;
	}

	public Map<String, String> getReqFields() {
		return reqFields;
	}

	public void setReqFields(Map<String, String> reqFields) {
		this.reqFields = reqFields;
	}

	public Map<String, String> getResFields() {
		return resFields;
	}

	public void setResFields(Map<String, String> resFields) {
		this.resFields = resFields;
	}

	public Map<String, Map<String, String>> getResSubTagFields() {
		return resSubTagFields;
	}

	public void setResSubTagFields(Map<String, Map<String, String>> resSubTagFields) {
		this.resSubTagFields = resSubTagFields;
	}

	public Map<String, Map<String, String>> getReqSubTagFields() {
		return reqSubTagFields;
	}

	public void setReqSubTagFields(Map<String, Map<String, String>> reqSubTagFields) {
		this.reqSubTagFields = reqSubTagFields;
	}

	public String getSubTagRespField() {
		return subTagRespField;
	}
	public void setSubTagRespField(String subTagRespField) {
		this.subTagRespField = subTagRespField;
	}
	public String getSubTagReqField() {
		return subTagReqField;
	}
	public void setSubTagReqField(String subTagReqField) {
		this.subTagReqField = subTagReqField;
	}
	@Override
	public String toString() {
		return "FsApiDetail [apiName=" + apiName + ", apiUrl=" + apiUrl + ", requestMetod=" + requestMetod
				+ ", reqFields=" + reqFields + ", resFields=" + resFields + ", resSubTagFields=" + resSubTagFields
				+ ", reqSubTagFields=" + reqSubTagFields + "]";
	}

}
