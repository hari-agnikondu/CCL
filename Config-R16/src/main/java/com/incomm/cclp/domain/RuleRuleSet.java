package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "RULE_RULESET")
public class RuleRuleSet implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RuleRuleSetId primaryKey = new RuleRuleSetId();

	@Column(name = "INS_USER",updatable=false)
	private long insUser;

	@Column(name="INS_DATE",updatable=false)
	private Date insDate;

	@Column(name = "LAST_UPD_USER")
	private long lastUpdUser;

	@Column(name = "LAST_UPD_DATE")
	private Date lastUpdDate;

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
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
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
		this.lastUpdDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public RuleRuleSetId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(RuleRuleSetId primaryKey) {
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
		if (!(obj instanceof RuleRuleSet))
			return false;
		RuleRuleSet other = (RuleRuleSet) obj;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}

}
