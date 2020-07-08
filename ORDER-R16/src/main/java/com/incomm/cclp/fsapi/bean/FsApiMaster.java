package com.incomm.cclp.fsapi.bean;

public class FsApiMaster {

	private String apiName;

	private String reqEncrypt;

	private String resEncrypt;

	private String userIdentifyType;

	private String partnerValid;

	private String custTypeValid;
	
	private String reqMethod;
	
	private String reqParse;
	
	private String validationBypass;
	
	private String apiDesc;

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getReqEncrypt() {
		return reqEncrypt;
	}

	public void setReqEncrypt(String reqEncrypt) {
		this.reqEncrypt = reqEncrypt;
	}

	public String getResEncrypt() {
		return resEncrypt;
	}

	public void setResEncrypt(String resEncrypt) {
		this.resEncrypt = resEncrypt;
	}

	public String getUserIdentifyType() {
		return userIdentifyType;
	}

	public void setUserIdentifyType(String userIdentifyType) {
		this.userIdentifyType = userIdentifyType;
	}

	public String getPartnerValid() {
		return partnerValid;
	}

	public void setPartnerValid(String partnerValid) {
		this.partnerValid = partnerValid;
	}

	public String getCustTypeValid() {
		return custTypeValid;
	}

	public void setCustTypeValid(String custTypeValid) {
		this.custTypeValid = custTypeValid;
	}

	public String getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(String reqMethod) {
		this.reqMethod = reqMethod;
	}

	public String getReqParse() {
		return reqParse;
	}

	public void setReqParse(String reqParse) {
		this.reqParse = reqParse;
	}

	public String getValidationBypass() {
		return validationBypass;
	}

	public void setValidationBypass(String validationBypass) {
		this.validationBypass = validationBypass;
	}

	public String getApiDesc() {
		return apiDesc;
	}

	public void setApiDesc(String apiDesc) {
		this.apiDesc = apiDesc;
	}

	@Override
	public String toString() {
		return "FaspiDetails [apiName=" + apiName + ", reqEncrypt=" + reqEncrypt + ", resEncrypt=" + resEncrypt
				+ ", userIdentifyType=" + userIdentifyType + ", partnerValid=" + partnerValid + ", custTypeValid="
				+ custTypeValid + ", reqMethod=" + reqMethod + ", reqParse=" + reqParse + ", validationBypass="
				+ validationBypass + ", apiDesc=" + apiDesc + "]";
	}

}
