package com.incomm.cclpvms.inventory.model;

import com.incomm.cclpvms.config.model.CardRange.ValidateSearch;
import com.incomm.cclpvms.config.validator.CheckForSearch;
import com.incomm.cclpvms.config.validator.FieldValidation;

public class CardNumberInventory {

	@CheckForSearch(pattern = "^[A-Za-z0-9 ,&.;'_-]+$", max = 100, messageLength = "{messageLength.Cardrange.IssuerName}", messagepattern = "{messagepattern.Cardrange.IssuerName}", groups = ValidateSearch.class)
	private String issuerName;
	private long issuerId;
	@CheckForSearch(pattern = "^[0-9]+$", messageLength = "{messageLength.Cardrange.Prefix}", messagepattern = "{messagepattern.Cardrange.Prefix}", groups = ValidateSearch.class)
	@FieldValidation(notEmpty = false, pattern = "^[0-9]+$", min = 6, max = 20, messageNotEmpty = "{messageNotEmpty.Cardrange.Prefix}", messageLength = "{messageLength.Cardrange.Prefix}", messagePattern = "{messagepattern.Cardrange.Prefix}")
	private String prefix;
	@FieldValidation(notEmpty = false, pattern = "^[0-9]+$", min = 2, max = 2, messageNotEmpty = "{messageNotEmpty.Cardrange.Cardlength}", messageLength = "{messageLength.Cardrange.Cardlength}", messagePattern = "{messagepattern.Cardrange.Cardlength}")
	private Integer cardLength;
	@FieldValidation(notEmpty = false, pattern = "^[0-9]+$", min = 5, max = 20, messageNotEmpty = "{messageNotEmpty.Cardrange.StartCardRange}", messageLength = "{messageLength.Cardrange.StartCardRange}", messagePattern = "{messagepattern.Cardrange.StartCardRange}")
	private String startCardNbr;
	@FieldValidation(notEmpty = false, pattern = "^[0-9]+$", min = 5, max = 20, messageNotEmpty = "{messageNotEmpty.Cardrange.EndCardRange}", messageLength = "{messageLength.Cardrange.EndCardRange}", messagePattern = "{messagepattern.Cardrange.EndCardRange}")
	private String endCardNbr;
	private String availableInventory;

	public String getAvailableInventory() {
		return availableInventory;
	}

	public void setAvailableInventory(String availableInventory) {
		this.availableInventory = availableInventory;
	}

	public String getIsCheckDigitRequired() {
		return isCheckDigitRequired;
	}

	public void setIsCheckDigitRequired(String isCheckDigitRequired) {
		this.isCheckDigitRequired = isCheckDigitRequired;
	}

	private String cardInventory;
	private long cardRangeId;
	private String isCheckDigitRequired;

	public long getCardRangeId() {
		return cardRangeId;
	}

	public void setCardRangeId(long cardRangeId) {
		this.cardRangeId = cardRangeId;
	}

	private String status;
	private String action;
	// @FieldValidation(notEmpty = true,pattern="^[A-Za-z]+$",
	// messageLength="{messageLength.Cardrange.Network}",messagePattern="{messagepattern.Cardrange.Network}")

	public CardNumberInventory(String issuerName, String prefix, Integer cardLength, String startCardNbr,
			String endCardNbr, String cardInventory, String status, Long cardRangeId) {
		super();
		this.issuerName = issuerName;
		this.prefix = prefix;
		this.cardLength = cardLength;
		this.startCardNbr = startCardNbr;
		this.endCardNbr = endCardNbr;
		this.cardInventory = cardInventory;
		this.status = status;
		this.cardRangeId= cardRangeId;
	}

	public CardNumberInventory() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public long getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(long issuerId) {
		this.issuerId = issuerId;
	}

	public String getCardInventory() {
		return cardInventory;
	}

	public void setCardInventory(String cardInventory) {
		this.cardInventory = cardInventory;
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

	public Integer getCardLength() {
		return cardLength;
	}

	public void setCardLength(Integer cardLength) {
		this.cardLength = cardLength;
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

}
