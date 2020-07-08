/**
 * 
 */
package com.incomm.cclp.dto;

import java.util.Date;
import java.util.Map;

/**
 * @author abutani
 *
 */
public class RuleDTO {
	
	private long ruleId;
	
	private String ruleName;
	
	private String txnTypeId;
	
	private String actionType;

	private String jsonReq;
	
	private String ruleExp;

	private long insUser;
	
	private Date insDate;
	
	private long lastUpdUser;
	
	private Date lastUpdDate;
	
	private Map<String, String> ruleDetails;

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

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public Map<String, String> getRuleDetails() {
		return ruleDetails;
	}

	public void setRuleDetails(Map<String, String> ruleDetails) {
		this.ruleDetails = ruleDetails;
	}

}
