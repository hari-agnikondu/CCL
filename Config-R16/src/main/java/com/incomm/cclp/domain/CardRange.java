package com.incomm.cclp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the CARD_RANGE database table.
 * 
 */
@Entity
@Audited
@Table(name="CARD_RANGE")
@NamedQuery(name="CardRange.findAll", query="SELECT c FROM CardRange c")
public class CardRange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CARD_RANGE_CARD_RANGE_ID_SEQ", sequenceName="CARD_RANGE_CARD_RANGE_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARD_RANGE_CARD_RANGE_ID_SEQ")
	@Column(name="CARD_RANGE_ID")
	private long cardRangeId;
	
	@Column(name="PREFIX")
	private String prefix;
	
	@Column(name="START_CARD_NBR")
	private String startCardNbr;

	@Column(name="END_CARD_NBR")
	private String endCardNbr;
	
	@Column(name="CARD_LENGTH")
	private BigDecimal cardLength;

	@Column(name="NETWORK")
	private String network;
	
	@Column(name="IS_CHECK_DIGIT_REQUIRED")
	private String isCheckDigitRequired;
	
	//bi-directional many-to-one association to Issuer
	@ManyToOne
	@JoinColumn(name="ISSUER_ID")
	private Issuer issuer;
	

	@ManyToOne
	@JoinColumn(name="INS_USER", updatable = false)
	@JsonIgnore
	private ClpUser insUpdclpUser;
	
	@CreationTimestamp
	/*@Temporal(TemporalType.DATE )*/
	@Column(name="INS_DATE",updatable = false )
	private Date insDate;
	

	@ManyToOne
	@JoinColumn(name="LAST_UPD_USER")
	@JsonIgnore
	private ClpUser lastUpdclpUser;
	
	@UpdateTimestamp
	/*@Temporal(TemporalType.DATE)*/
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;
	
	@Column(name="CARD_INVENTORY_TYPE")
	private String cardInventory;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="CHECKER_REMARKS")
	private String checkerDesc;


	public CardRange(long cardRangeId) {
		this.cardRangeId = cardRangeId;
	}

	public long getCardRangeId() {
		return this.cardRangeId;
	}

	public void setCardRangeId(long cardRangeId) {
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
	
	public ClpUser getInsUpdclpUser() {
		return insUpdclpUser;
	}

	public void setInsUpdclpUser(ClpUser insUpdclpUser) {
		this.insUpdclpUser = insUpdclpUser;
	}

	public ClpUser getLastUpdclpUser() {
		return lastUpdclpUser;
	}

	public void setLastUpdclpUser(ClpUser lastUpdclpUser) {
		this.lastUpdclpUser = lastUpdclpUser;
	}


	@Override
	public String toString() {
		return "CardRange [cardRangeId=" + cardRangeId + ", prefix=" + prefix
				+ ", startCardNbr=" + startCardNbr + ", endCardNbr="
				+ endCardNbr + ", cardLength=" + cardLength + ", network="
				+ network + ", isCheckDigitRequired=" + isCheckDigitRequired
				+ ", issuer=" + issuer + ", insUpdclpUser=" + insUpdclpUser
				+ ", insDate=" + insDate + ", lastUpdclpUser=" + lastUpdclpUser
				+ ", lastUpdDate=" + lastUpdDate + ", cardInventory="
				+ cardInventory + ", status=" + status + ", checkerDesc="
				+ checkerDesc + "]";
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