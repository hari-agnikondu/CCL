package com.incomm.cclpvms.config.model;

import java.io.Serializable;
import java.util.Date;

public class RuleSetDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long ruleSetId;		

	private String ruleSetName;

	private Long insUser;

	private Date insDate;

	private Long lastUpdUser;

	private Date lastUpdDate;
	
	private String ruleSetNametxt;
	
	private String operationList;
	
	private String deletedValues;

	public String getDeletedValues() {
		return deletedValues;
	}

	public void setDeletedValues(String deletedValues) {
		this.deletedValues = deletedValues;
	}

	public String getRuleSetNametxt() {
		return ruleSetNametxt;
	}

	public void setRuleSetNametxt(String ruleSetNametxt) {
		this.ruleSetNametxt = ruleSetNametxt;
	}

	public String getOperationList() {
		return operationList;
	}

	public void setOperationList(String operationList) {
		this.operationList = operationList;
	}

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
		this.insDate = insDate;
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
		this.lastUpdDate = lastUpdDate;
	}


}