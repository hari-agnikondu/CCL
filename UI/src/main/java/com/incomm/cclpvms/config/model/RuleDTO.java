/**
 * 
 */
package com.incomm.cclpvms.config.model;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author abutani
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleDTO {
	
	private long ruleId;
	
	private String ruleName;
	
	private String txnTypeId;
	
	private String actionType;

	private String jsonReq;
	
	private String ruleExp;

	private long insUser;
	
	private long lastUpdUser;
	
	private HashMap<String, String> ruleDetails;

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public long getRuleId() {
		return ruleId;
	}

	public void setRuleId(long ruleId) {
		this.ruleId = ruleId;
	}

	public String getTxnTypeId() {
		return txnTypeId;
	}

	public void setTxnTypeId(String txnTypeId) {
		this.txnTypeId = txnTypeId;
	}

	
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getJsonReq() {
		return jsonReq;
	}

	public void setJsonReq(String jsonReq) {
		this.jsonReq = jsonReq;
	}

	public String getRuleExp() {
		return ruleExp;
	}

	public void setRuleExp(String ruleExp) {
		this.ruleExp = ruleExp;
	}
	
	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public HashMap<String, String> getRuleDetails() {
		return ruleDetails;
	}

	public void setRuleDetails(HashMap<String, String> ruleDetails) {
		this.ruleDetails = ruleDetails;
	}

	@Override
	public String toString() {
		return "RuleDTO [ruleId=" + ruleId + ", ruleName=" + ruleName + ", txnTypeId=" + txnTypeId + ", actionType="
				+ actionType + ", jsonReq=" + jsonReq + ", ruleExp=" + ruleExp + ", insUser=" + insUser
				+ ", lastUpdUser=" + lastUpdUser + ", ruleDetails=" + ruleDetails + "]";
	}
  
}
