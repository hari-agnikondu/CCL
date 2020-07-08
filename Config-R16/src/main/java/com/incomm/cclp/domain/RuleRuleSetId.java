package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RuleRuleSetId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "RULESET_ID")
	private long ruleSetId;

	@Column(name = "RULE_ID")
	private long ruleID;

	public long getRuleID() {
		return ruleID;
	}

	public void setRuleID(long ruleID) {
		this.ruleID = ruleID;
	}

	public RuleRuleSetId() {

	}

	public RuleRuleSetId(long ruleSetID, long ruleID) {
		this.ruleSetId = ruleSetID;
		this.ruleID = ruleID;
	}

	public long getRuleSetId() {
		return ruleSetId;
	}

	public void setRuleSetId(long ruleSetId) {
		this.ruleSetId = ruleSetId;
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
 
        if (obj == null || getClass() != obj.getClass()) 
            return false;
 
        RuleRuleSetId that = (RuleRuleSetId) obj;
        return Objects.equals(ruleSetId, that.ruleSetId) && 
               Objects.equals(ruleID, that.ruleID);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(ruleSetId, ruleID);
    }
	
}
