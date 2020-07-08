package com.incomm.scheduler.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The persistent class for the CARD_RANGE database table.
 * 
 */
@Entity
@Table(name = "clp_configuration.CARD_RANGE")
public class CardRange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CARD_RANGE_ID")
	private Long cardRangeId;

	@Column(name = "PREFIX")
	private String prefix;

	@Column(name = "START_CARD_NBR")
	private String startCardNbr;

	@Column(name = "END_CARD_NBR")
	private String endCardNbr;

	@Column(name = "CARD_LENGTH")
	private BigDecimal cardLength;

	@Column(name = "NETWORK")
	private String network;

	@Column(name = "IS_CHECK_DIGIT_REQUIRED")
	private String isCheckDigitRequired;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ISSUER_ID")
	private Issuer issuer;

	@Column(name = "INS_USER", updatable = false)
	private Long insUser;

	@CreationTimestamp
	@Column(name = "INS_DATE", updatable = false)
	private Date insDate;

	@Column(name = "LAST_UPD_USER")
	private Long lastUpdUser;

	@UpdateTimestamp
	@Column(name = "LAST_UPD_DATE")
	private Date lastUpdDate;

	@Column(name = "CARD_INVENTORY_TYPE")
	private String cardInventory;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CHECKER_REMARKS")
	private String checkerDesc;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "cardRange")
	private CardRangeInventory cardRangeInventory;

	public CardRangeInventory getCardRangeInventory() {
		return cardRangeInventory;
	}

	public void setCardRangeInventory(CardRangeInventory cardRangeInventory) {
		this.cardRangeInventory = cardRangeInventory;
	}

	public CardRange(Long cardRangeId) {
		this.cardRangeId = cardRangeId;
	}

	public Long getCardRangeId() {
		return this.cardRangeId;
	}

	public void setCardRangeId(Long cardRangeId) {
		this.cardRangeId = cardRangeId;
	}

	public BigDecimal getCardLength() {
		return this.cardLength;
	}

	public void setCardLength(BigDecimal cardLength) {
		this.cardLength = cardLength;
	}

	public String getEndCardNbr() {
		return this.endCardNbr;
	}

	public void setEndCardNbr(String endCardNbr) {
		this.endCardNbr = endCardNbr;
	}

	public Date getInsDate() {
		return this.insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public String getIsCheckDigitRequired() {
		return this.isCheckDigitRequired;
	}

	public void setIsCheckDigitRequired(String isCheckDigitRequired) {
		this.isCheckDigitRequired = isCheckDigitRequired;
	}

	public Date getLastUpdDate() {
		return this.lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public String getNetwork() {
		return this.network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getStartCardNbr() {
		return this.startCardNbr;
	}

	public void setStartCardNbr(String startCardNbr) {
		this.startCardNbr = startCardNbr;
	}

	public Issuer getIssuer() {
		return this.issuer;
	}

	public void setIssuer(Issuer issuer) {
		this.issuer = issuer;
	}

	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	public Long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
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

	@Override
	public String toString() {
		return "CardRange [cardRangeId=" + cardRangeId + ", prefix=" + prefix + ", startCardNbr=" + startCardNbr
				+ ", endCardNbr=" + endCardNbr + ", cardLength=" + cardLength + ", network=" + network
				+ ", isCheckDigitRequired=" + isCheckDigitRequired + ", issuer=" + issuer + ", insUser=" + insUser
				+ ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ ", cardInventory=" + cardInventory + ", status=" + status + "]";
	}

	public String getCheckerDesc() {
		return checkerDesc;
	}

	public void setCheckerDesc(String checkerDesc) {
		this.checkerDesc = checkerDesc;
	}

	public CardRange() {

	}

}