package com.incomm.scheduler.model;

import java.io.Serializable;


public class DailyBalance implements Serializable {

	private static final long serialVersionUID = 1L;


	private String cardNumber;
	private String productId;
	private String accountId;
	private String cardHash;
	
	private String moblieNo;
	private String email;
	private String currencyCode;
	private String processStatus;
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public String getCardHash() {
		return cardHash;
	}
	public void setCardHash(String cardHash) {
		this.cardHash = cardHash;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getMoblieNo() {
		return moblieNo;
	}
	public void setMoblieNo(String moblieNo) {
		this.moblieNo = moblieNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(String status) {
		this.processStatus = status;
	}
	@Override
	public String toString() {
		return "DailyBalance [cardNumber=" + cardNumber + ", productId=" + productId + ", accountId=" + accountId
				+ ", cardHash=" + cardHash + ", moblieNo=" + moblieNo + ", email=" + email + ", currencyCode="
				+ currencyCode + ", processStatus=" + processStatus + "]";
	}
	
	
	
	
}
