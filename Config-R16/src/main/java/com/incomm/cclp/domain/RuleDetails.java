package com.incomm.cclp.domain;




import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the RULE_DETAILS database table.
 * 
 */
//@Audited
@Entity
@Table(name = "RULE_DETAILS")
//@javax.persistence.NamedNativeQuery(name = "encryptFunction", query = "{? =  call get_chkdig(:ruleFilter) }",  hints = {@javax.persistence.QueryHint(name = "org.hibernate.callable", value = "true") })
public class RuleDetails implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private RuleRuleDetailId primaryKey = new RuleRuleDetailId();
	
	@Column(name="RULE_FILTER")
	private String ruleFilter;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;

	@Column(name="LAST_UPD_USER")
	private long lastUpdUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE",updatable = false)
	private Date insDate;

	@Column(name="INS_USER",updatable = false )
	private long insUser;
 
	
	
	public RuleDetails() {

	}

	public RuleDetails(RuleRuleDetailId primaryKey, String ruleFilter, long lastUpdUser,long insUser) {

		this.primaryKey = primaryKey;
		this.ruleFilter = ruleFilter;
		this.lastUpdUser = lastUpdUser;
		this.insUser = insUser;
		setInsDate();
		setLastUpdDate();
		this.insDate=getInsDate();
		this.lastUpdDate=getLastUpdDate();
	}

	public String getRuleFilter() {
		return ruleFilter;
	}

	public void setRuleFilter(String ruleFilter) {
		this.ruleFilter = ruleFilter;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate() {
		this.lastUpdDate = new java.sql.Date(new java.util.Date().getTime());
	}
	
	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate() {
	
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public RuleRuleDetailId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(RuleRuleDetailId primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RuleDetails))
			return false;
		RuleDetails other = (RuleDetails) obj;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}
}
