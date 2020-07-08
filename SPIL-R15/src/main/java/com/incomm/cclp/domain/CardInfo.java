package com.incomm.cclp.domain;

public class CardInfo {
	String oldCardStatus;
	String cardNumHash;

	public CardInfo() {

	}

	public CardInfo(String oldCardStatus, String cardNumHash) {
		this.oldCardStatus = oldCardStatus;
		this.cardNumHash = cardNumHash;
	}

	public String getOldCardStatus() {
		return oldCardStatus;
	}

	public void setOldCardStatus(String oldCardStatus) {
		this.oldCardStatus = oldCardStatus;
	}

	public String getCardNumHash() {
		return cardNumHash;
	}

	public void setCardNumHash(String cardNumHash) {
		this.cardNumHash = cardNumHash;
	}

}
