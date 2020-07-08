package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RuleRuleDetailId implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "RULE_DETAILS_ID")
	private long ruleDetailsId;

	@Column(name = "RULE_ID")
	private long ruleID;

	public long getRuleID() {
		return ruleID;
	}

	public void setRuleID(long ruleID) {
		this.ruleID = ruleID;
	}

	public RuleRuleDetailId() {

	}

	public RuleRuleDetailId(long ruleDetailsId, long ruleID) {
		this.ruleDetailsId = ruleDetailsId;
		this.ruleID = ruleID;
	}

	public long getRuleDetailsId() {
		return ruleDetailsId;
	}
	
	public void setRuleDetailsId(long ruleDetailsId) {
		this.ruleDetailsId = ruleDetailsId;
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
 
        if (obj == null || getClass() != obj.getClass()) 
            return false;
 
        RuleRuleDetailId that = (RuleRuleDetailId) obj;
        return Objects.equals(ruleDetailsId, that.ruleDetailsId) && 
               Objects.equals(ruleID, that.ruleID);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(ruleDetailsId, ruleID);
    }

}
