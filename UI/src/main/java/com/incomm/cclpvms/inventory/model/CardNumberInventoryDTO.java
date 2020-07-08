package com.incomm.cclpvms.inventory.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CardNumberInventoryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long cardRangeId;

	private String issuerName;

	private String prefix;

	private String startCardNbr;

	private String endCardNbr;

	private BigDecimal cardLength;

	private String cardInventory;

	private String status;

	public CardNumberInventoryDTO() {

	}

	public Long getCardRangeId() {
		return cardRangeId;
	}

	public void setCardRangeId(Long cardRangeId) {
		this.cardRangeId = cardRangeId;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getStartCardNbr() {
		return startCardNbr;
	}

	public void setStartCardNbr(String startCardNbr) {
		this.startCardNbr = startCardNbr;
	}

	public String getEndCardNbr() {
		return endCardNbr;
	}

	public void setEndCardNbr(String endCardNbr) {
		this.endCardNbr = endCardNbr;
	}

	public BigDecimal getCardLength() {
		return cardLength;
	}

	public void setCardLength(BigDecimal cardLength) {
		this.cardLength = cardLength;
	}

	public String getCardInventory() {
		return cardInventory;
	}

	public void setCardInventory(String cardInventory) {
		this.cardInventory = cardInventory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
