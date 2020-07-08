/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author skocherla
 */
@Entity
@Table(name = "CARD_STATUS")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "CardStatus.findAll", query = "SELECT c FROM CardStatus c"),
		@NamedQuery(name = "CardStatus.findByStatusCode", query = "SELECT c FROM CardStatus c WHERE c.statusCode = :statusCode"),
		@NamedQuery(name = "CardStatus.findByStatusDesc", query = "SELECT c FROM CardStatus c WHERE c.statusDesc = :statusDesc"),
		@NamedQuery(name = "CardStatus.findByInsUser", query = "SELECT c FROM CardStatus c WHERE c.insUser = :insUser"),
		@NamedQuery(name = "CardStatus.findByInsDate", query = "SELECT c FROM CardStatus c WHERE c.insDate = :insDate"),
		@NamedQuery(name = "CardStatus.findByLastUpdUser", query = "SELECT c FROM CardStatus c WHERE c.lastUpdUser = :lastUpdUser"),
		@NamedQuery(name = "CardStatus.findByLastUpdDate", query = "SELECT c FROM CardStatus c WHERE c.lastUpdDate = :lastUpdDate"),
		@NamedQuery(name = "CardStatus.findByCipfailActflag", query = "SELECT c FROM CardStatus c WHERE c.cipfailActflag = :cipfailActflag"),
		@NamedQuery(name = "CardStatus.findByTransactionCode", query = "SELECT c FROM CardStatus c WHERE c.transactionCode = :transactionCode"),
		@NamedQuery(name = "CardStatus.findByPrmcardStatus", query = "SELECT c FROM CardStatus c WHERE c.prmcardStatus = :prmcardStatus"),
		@NamedQuery(name = "CardStatus.findByPassivechkFlag", query = "SELECT c FROM CardStatus c WHERE c.passivechkFlag = :passivechkFlag"),
		@NamedQuery(name = "CardStatus.findByCvvplusStatus", query = "SELECT c FROM CardStatus c WHERE c.cvvplusStatus = :cvvplusStatus"),
		@NamedQuery(name = "CardStatus.findByTokenStatus", query = "SELECT c FROM CardStatus c WHERE c.tokenStatus = :tokenStatus"),
		@NamedQuery(name = "CardStatus.findBySpilResponseId", query = "SELECT c FROM CardStatus c WHERE c.spilResponseId = :spilResponseId"),
		@NamedQuery(name = "CardStatus.findBySpilResponseMsg", query = "SELECT c FROM CardStatus c WHERE c.spilResponseMsg = :spilResponseMsg"),
		@NamedQuery(name = "CardStatus.findByConsumedStatus", query = "SELECT c FROM CardStatus c WHERE c.consumedStatus = :consumedStatus") })
public class CardStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2)
	@Column(name = "STATUS_CODE")
	private String statusCode;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "STATUS_DESC")
	private String statusDesc;
	@Column(name = "INS_USER")
	private BigInteger insUser;
	@Column(name = "INS_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insDate;
	@Column(name = "LAST_UPD_USER")
	private BigInteger lastUpdUser;
	@Column(name = "LAST_UPD_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdDate;
	@Size(max = 1)
	@Column(name = "CIPFAIL_ACTFLAG")
	private String cipfailActflag;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2)
	@Column(name = "TRANSACTION_CODE")
	private String transactionCode;
	@Size(max = 2)
	@Column(name = "PRMCARD_STATUS")
	private String prmcardStatus;
	@Size(max = 1)
	@Column(name = "PASSIVECHK_FLAG")
	private String passivechkFlag;
	@Size(max = 10)
	@Column(name = "CVVPLUS_STATUS")
	private String cvvplusStatus;
	@Size(max = 10)
	@Column(name = "TOKEN_STATUS")
	private String tokenStatus;
	@Column(name = "SPIL_RESPONSE_ID")
	private Integer spilResponseId;
	@Size(max = 500)
	@Column(name = "SPIL_RESPONSE_MSG")
	private String spilResponseMsg;
	@Size(max = 20)
	@Column(name = "CONSUMED_STATUS")
	private String consumedStatus;

	public CardStatus() {
	}

	public CardStatus(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public CardStatus(String transactionCode, String statusCode, String statusDesc) {
		this.transactionCode = transactionCode;
		this.statusCode = statusCode;
		this.statusDesc = statusDesc;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public BigInteger getInsUser() {
		return insUser;
	}

	public void setInsUser(BigInteger insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public BigInteger getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(BigInteger lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public String getCipfailActflag() {
		return cipfailActflag;
	}

	public void setCipfailActflag(String cipfailActflag) {
		this.cipfailActflag = cipfailActflag;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getPrmcardStatus() {
		return prmcardStatus;
	}

	public void setPrmcardStatus(String prmcardStatus) {
		this.prmcardStatus = prmcardStatus;
	}

	public String getPassivechkFlag() {
		return passivechkFlag;
	}

	public void setPassivechkFlag(String passivechkFlag) {
		this.passivechkFlag = passivechkFlag;
	}

	public String getCvvplusStatus() {
		return cvvplusStatus;
	}

	public void setCvvplusStatus(String cvvplusStatus) {
		this.cvvplusStatus = cvvplusStatus;
	}

	public String getTokenStatus() {
		return tokenStatus;
	}

	public void setTokenStatus(String tokenStatus) {
		this.tokenStatus = tokenStatus;
	}

	public Integer getSpilResponseId() {
		return spilResponseId;
	}

	public void setSpilResponseId(Integer spilResponseId) {
		this.spilResponseId = spilResponseId;
	}

	public String getSpilResponseMsg() {
		return spilResponseMsg;
	}

	public void setSpilResponseMsg(String spilResponseMsg) {
		this.spilResponseMsg = spilResponseMsg;
	}

	public String getConsumedStatus() {
		return consumedStatus;
	}

	public void setConsumedStatus(String consumedStatus) {
		this.consumedStatus = consumedStatus;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (transactionCode != null ? transactionCode.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof CardStatus)) {
			return false;
		}
		CardStatus other = (CardStatus) object;
		if ((this.transactionCode == null && other.transactionCode != null)
				|| (this.transactionCode != null && !this.transactionCode.equals(other.transactionCode))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.CardStatus[ transactionCode=" + transactionCode + " ]";
	}

}
