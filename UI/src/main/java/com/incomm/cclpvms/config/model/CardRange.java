package com.incomm.cclpvms.config.model;

import com.incomm.cclpvms.config.validator.CheckForSearch;
import com.incomm.cclpvms.config.validator.FieldValidation;

public class CardRange {
	
	@CheckForSearch(pattern="^[A-Za-z0-9 ,&.;'_-]+$", max = 100,  messageLength="{messageLength.Cardrange.IssuerName}",messagepattern="{messagepattern.Cardrange.IssuerName}",groups=ValidateSearch.class)
	private String issuerName;
	private long issuerId;
	@CheckForSearch(pattern="^[0-9]+$", messageLength="{messageLength.Cardrange.Prefix}",messagepattern="{messagepattern.Cardrange.Prefix}",groups=ValidateSearch.class)
	@FieldValidation(notEmpty = false,pattern="^[0-9]+$", min = 6, max = 20,  messageNotEmpty="{messageNotEmpty.Cardrange.Prefix}",messageLength="{messageLength.Cardrange.Prefix}",messagePattern="{messagepattern.Cardrange.Prefix}")
	private String prefix;
	@FieldValidation(notEmpty = false,pattern="^[0-9]+$", min=2, max = 2,  messageNotEmpty="{messageNotEmpty.Cardrange.Cardlength}",messageLength="{messageLength.Cardrange.Cardlength}",messagePattern="{messagepattern.Cardrange.Cardlength}")
	private Integer cardLength;
	@FieldValidation(notEmpty = false,pattern="^[0-9]+$", min = 5, max = 20,  messageNotEmpty="{messageNotEmpty.Cardrange.StartCardRange}",messageLength="{messageLength.Cardrange.StartCardRange}",messagePattern="{messagepattern.Cardrange.StartCardRange}")
	private String startCardNbr;
	@FieldValidation(notEmpty = false,pattern="^[0-9]+$", min = 5, max = 20,  messageNotEmpty="{messageNotEmpty.Cardrange.EndCardRange}",messageLength="{messageLength.Cardrange.EndCardRange}",messagePattern="{messagepattern.Cardrange.EndCardRange}")
	private String endCardNbr;
	private String isCheckDigitRequired;
	private String cardInventory;
	private String availableInventory;
	private long cardRangeId;
	
	private String status;
	private String action;
	//@FieldValidation(notEmpty = true,pattern="^[A-Za-z]+$",  messageLength="{messageLength.Cardrange.Network}",messagePattern="{messagepattern.Cardrange.Network}")
	private String network;
	
	private String checkerDesc;
	
	public CardRange() {
		
	}
	

	public CardRange(String issuerName, String prefix, Integer cardLength, String startCardNbr, String endCardNbr,
			String isCheckDigitRequired, String cardInventory, String availableInventory, String status, String action,
			String network) {
		super();
		this.issuerName = issuerName;
		this.prefix = prefix;
		this.cardLength = cardLength;
		this.startCardNbr = startCardNbr;
		this.endCardNbr = endCardNbr;
		this.isCheckDigitRequired = isCheckDigitRequired;
		this.cardInventory = cardInventory;
		this.availableInventory = availableInventory;
		this.status = status;
		this.action = action;
		this.network = network;
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

	public String getIsCheckDigitRequired() {
		return isCheckDigitRequired;
	}

	public void setIsCheckDigitRequired(String isCheckDigitRequired) {
		this.isCheckDigitRequired = isCheckDigitRequired;
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
	public Integer getCardLength() {
		return cardLength;
	}
	public void setCardLength(Integer cardLength) {
		this.cardLength = cardLength;
	}


	public String getAvailableInventory() {
		return availableInventory;
	}


	public void setAvailableInventory(String availableInventory) {
		this.availableInventory = availableInventory;
	}



	public String getAction() {
		return action;
	}



	public void setAction(String action) {
		this.action = action;
	}



	public String getNetwork() {
		return network;
	}



	public void setNetwork(String network) {
		this.network = network;
	}


	public long getIssuerId() {
		return issuerId;
	}


	public void setIssuerId(long issuerId) {
		this.issuerId = issuerId;
	}


	

	public long getCardRangeId() {
		return cardRangeId;
	}


	public void setCardRangeId(long cardRangeId) {
		this.cardRangeId = cardRangeId;
	}



	public String getCheckerDesc() {
		return checkerDesc;
	}


	public void setCheckerDesc(String checkerDesc) {
		this.checkerDesc = checkerDesc;
	}


	@Override
	public String toString() {
		return "CardRangeModel [issuerName=" + issuerName + ", issuerId="
				+ issuerId + ", prefix=" + prefix + ", cardLength="
				+ cardLength + ", startCardNbr=" + startCardNbr
				+ ", endCardNbr=" + endCardNbr + ", isCheckDigitRequired="
				+ isCheckDigitRequired + ", cardInventory=" + cardInventory
				+ ", availableInventory=" + availableInventory
				+ ", cardRangeId=" + cardRangeId + ", status=" + status
				+ ", action=" + action + ", network=" + network
				+ ", checkerDesc=" + checkerDesc + "]";
	}
	
	public interface ValidateSearch {

		// validation group marker interface

	}
	

}
