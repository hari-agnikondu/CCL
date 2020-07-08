package com.incomm.cclp.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.envers.Audited;

import java.util.Date;

/**
 * The persistent class for the "RULE" database table.
 * 
 */
@Entity
@Audited
@Table(name = "RULE")
@NamedQuery(name = "Rule.findAll", query = "SELECT r FROM Rule r")
public class Rule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "RULE_SEQ_GEN", sequenceName = "RULE_RULE_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RULE_SEQ_GEN")
	@Column(name = "RULE_ID")
	private long ruleId;

	@Column(name = "ACTION_TYPE")
	private String actionType;

	@Temporal(TemporalType.DATE)
	@Column(name = "INS_DATE", updatable = false)
	private Date insDate;

	@Column(name = "INS_USER", updatable = false)
	private long insUser;

	@Lob
	@Column(name = "JSON_REQ")
	private String jsonReq;

	@Column(name = "JSONREQ_FLAG")
	private String jsonReqFlag;

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPD_DATE")
	private Date lastUpdDate;

	@Column(name = "LAST_UPD_USER")
	private long lastUpdUser;

	@Column(name = "RULE_EXP")
	private String ruleExp;

	@Column(name = "RULE_NAME")
	private String ruleName;

	@Column(name = "TRANSACTION_TYPE")
	private String txnTypeId;

	public Rule() {
	}

	public long getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(long ruleId) {
		this.ruleId = ruleId;
	}

	public String getActionType() {
		return this.actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getInsDate() {
		return this.insDate;
	}

	public void setInsDate(Date insDate) {
	
		this.insDate = new java.sql.Date(new java.util.Date().getTime()); 
				
	}

	public long getInsUser() {
		return this.insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public String getJsonReq() {
		return this.jsonReq;
	}

	public void setJsonReq(String jsonReq) {
		this.jsonReq = jsonReq;
	}

	public String getJsonReqFlag() {
		return this.jsonReqFlag;
	}

	public void setJsonReqFlag(String jsonReqFlag) {
		this.jsonReqFlag = jsonReqFlag;
	}

	public Date getLastUpdDate() {
		return this.lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate; 
				
	}

	public long getLastUpdUser() {
		return this.lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public String getRuleExp() {
		return this.ruleExp;
	}

	public void setRuleExp(String ruleExp) {
		this.ruleExp = ruleExp;
	}

	public String getRuleName() {
		return this.ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getTxnTypeId() {
		return txnTypeId;
	}

	public void setTxnTypeId(String txnTypeId) {
		this.txnTypeId = txnTypeId;
	}

	public Rule(long ruleId, String actionType, Date insDate, long insUser, String jsonReq, String jsonReqFlag,
			Date lastUpdDate, long lastUpdUser, String ruleExp, String ruleName, String txnTypeId) {
		this.ruleId = ruleId;
		this.actionType = actionType;
		this.insDate = insDate;
		this.insUser = insUser;
		this.jsonReq = jsonReq;
		this.jsonReqFlag = jsonReqFlag;
		this.lastUpdDate = lastUpdDate;
		this.lastUpdUser = lastUpdUser;
		this.ruleExp = ruleExp;
		this.ruleName = ruleName;
		this.txnTypeId = txnTypeId;
	}
	
	
}