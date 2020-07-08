package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;




/**
 * The persistent class for the RULESET database table.
 * 
 */
@Entity
@Audited
@Table(name = "RULESET")
public class RuleSet implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="RULESET_ID")
	private Long ruleSetId;		

	@Column(name="RULESET_NAME")
	private String ruleSetName;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, 
			mappedBy = "primaryKey.ruleSetId", orphanRemoval = true)
	private List<RuleRuleSet> ruleRuleSetMapping;

	@Column(name="INS_USER",updatable=false)
	private Long insUser;

	@CreationTimestamp
	@Column(name="INS_DATE",updatable=false)
	private Date insDate;

	@Column(name="LAST_UPD_USER")
	private Long lastUpdUser;

	@UpdateTimestamp
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;

	public Long getRuleSetId() {
		return ruleSetId;
	}

	public void setRuleSetId(Long ruleSetId) {
		this.ruleSetId = ruleSetId;
	}

	public String getRuleSetName() {
		return ruleSetName;
	}

	public void setRuleSetName(String ruleSetName) {
		this.ruleSetName = ruleSetName;
	}

	public List<RuleRuleSet> getRuleRuleSetMapping() {
		return ruleRuleSetMapping;
	}

	public void setRuleRuleSetMapping(List<RuleRuleSet> ruleRuleSetMapping) {
		this.ruleRuleSetMapping = ruleRuleSetMapping;
	}

	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public Long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = new java.sql.Date(new java.util.Date().getTime());
	}

	@Override
	public String toString() {
		return "RuleSet [ruleSetId=" + ruleSetId + ", ruleSetName=" + ruleSetName + 
				", insUser=" + insUser + ", insDate=" + insDate + ", lastUpdUser=" + 
				lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ ", ruleRuleSetMapping=" + ruleRuleSetMapping + "]";
	}

}