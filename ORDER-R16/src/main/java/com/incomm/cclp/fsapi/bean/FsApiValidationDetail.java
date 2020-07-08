package com.incomm.cclp.fsapi.bean;

import java.util.List;

public class FsApiValidationDetail {

	private String apiName;

	private String fieldName;

	private String parentTag;

	private String dataType;

	private String fieldValues;

	private String regexExpression;

	private String validationType;

	private String validationErrMsg;

	private String respCode;

	private List<String> subTagList;
	
	private String subTag;

	private String subTagField;

	private String requestMethod;

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getParentTag() {
		return parentTag;
	}

	public void setParentTag(String parentTag) {
		this.parentTag = parentTag;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(String fieldValues) {
		this.fieldValues = fieldValues;
	}

	public String getRegexExpression() {
		return regexExpression;
	}

	public void setRegexExpression(String regexExpression) {
		this.regexExpression = regexExpression;
	}

	public String getValidationType() {
		return validationType;
	}

	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}

	public String getValidationErrMsg() {
		return validationErrMsg;
	}

	public void setValidationErrMsg(String validationErrMsg) {
		this.validationErrMsg = validationErrMsg;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getSubTag() {
		return subTag;
	}

	public void setSubTag(String subTag) {
		this.subTag = subTag;
	}
	public String getSubTagField() {
		return subTagField;
	}

	public void setSubTagField(String subTagField) {
		this.subTagField = subTagField;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public List<String> getSubTagList() {
		return subTagList;
	}

	public void setSubTagList(List<String> subTagList) {
		this.subTagList = subTagList;
	}

	@Override
	public String toString() {
		return "FsApiValidationDetail [apiName=" + apiName + ", fieldName=" + fieldName + ", parentTag=" + parentTag
				+ ", dataType=" + dataType + ", fieldValues=" + fieldValues + ", regexExpression=" + regexExpression
				+ ", validationType=" + validationType + ", validationErrMsg=" + validationErrMsg + ", respCode="
				+ respCode + ", subTagList=" + subTagList + ", subTagField=" + subTagField + ", requestMethod="
				+ requestMethod + "]";
	}
	
}
