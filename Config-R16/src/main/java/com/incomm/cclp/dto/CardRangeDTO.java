/**
 * 
 */
package com.incomm.cclp.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class CardRangeDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private long cardRangeId;
	
	private String prefix;
	
	private String startCardNbr;
	
	private String endCardNbr;

	private BigDecimal cardLength;
	
	private String network;

	private String isCheckDigitRequired;

	private long insUser;
	
	private String insUserName;

	private Date insDate;

	private long lastUpdUser;
	
	private String updUserName;

	private Date lastUpdDate;

	private String issuerName;

	private long issuerId;

	private String cardInventory;

	private String status;
	
	private String checkerDesc;
	
	/*To create an object of CardRangeDTO with default constuctor*/
	public CardRangeDTO(){
		
	}
	public CardRangeDTO(long cardRangeId, String prefix, String startCardNbr,
			String endCardNbr, BigDecimal cardLength, String network,
			String isCheckDigitRequired, String issuerName,
			long issuerId, String cardInventory, String status,long userLoginId, String insUserName,
			long updUser, String upduserName) {
		super();
		this.cardRangeId = cardRangeId;
		this.prefix = prefix;
		this.startCardNbr = startCardNbr;
		this.endCardNbr = endCardNbr;
		this.cardLength = cardLength;
		this.network = network;
		this.isCheckDigitRequired = isCheckDigitRequired;
		this.issuerName = issuerName;
		this.issuerId = issuerId;
		this.cardInventory = cardInventory;
		this.status = status;
		this.insUser = userLoginId;
		this.insUserName = insUserName;
		this.updUserName = upduserName;
		this.lastUpdUser = updUser;
		
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
	
	public long getCardRangeId() {
		return cardRangeId;
	}

	public void setCardRangeId(long cardRangeId) {
		this.cardRangeId = cardRangeId;
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

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getIsCheckDigitRequired() {
		return isCheckDigitRequired;
	}

	public void setIsCheckDigitRequired(String isCheckDigitRequired) {
		this.isCheckDigitRequired = isCheckDigitRequired;
	}

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public long getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(long issuerId) {
		this.issuerId = issuerId;
	}
	
	public String getCheckerDesc() {
		return checkerDesc;
	}
	public void setCheckerDesc(String checkerDesc) {
		this.checkerDesc = checkerDesc;
	}

	public String getInsUserName() {
		return insUserName;
	}
	public void setInsUserName(String insUserName) {
		this.insUserName = insUserName;
	}
	public String getUpdUserName() {
		return updUserName;
	}
	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}
	@Override
	public String toString() {
		return "CardRangeDTO [cardRangeId=" + cardRangeId + ", prefix="
				+ prefix + ", startCardNbr=" + startCardNbr + ", endCardNbr="
				+ endCardNbr + ", cardLength=" + cardLength + ", network="
				+ network + ", isCheckDigitRequired=" + isCheckDigitRequired
				+ ", insUser=" + insUser + ", insUserName=" + insUserName
				+ ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser
				+ ", updUserName=" + updUserName + ", lastUpdDate="
				+ lastUpdDate + ", issuerName=" + issuerName + ", issuerId="
				+ issuerId + ", cardInventory=" + cardInventory + ", status="
				+ status + ", checkerDesc=" + checkerDesc + "]";
	}
	
	
	


}
