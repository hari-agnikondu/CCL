package com.incomm.cclpvms.config.model;

import com.incomm.cclpvms.config.model.Issuer.ValidationStepOne;
import com.incomm.cclpvms.config.validator.FieldValidation;

public class Ruleset {

	private long ruleSetId;
	private String ruleSetName;
	private long ruleId;
	private String ruleName;
	
	@FieldValidation(notEmpty = true, pattern = "^[A-Za-z0-9 ,&_-]+$",min=2,max = 50,
			messageNotEmpty = "{messageNotEmpty.rule.ruleName}", 
			messageLength = "{messageLength.rule.ruleName}",
			startsWithSpace = "{startsWithSpace.ruleName.message}",groups = ValidationStepOne.class)
	private String ruleSetNametxt;
	private String operationList;
	
	private String deletedValues;

	public String getDeletedValues() {
		return deletedValues;
	}

	public void setDeletedValues(String deletedValues) {
		this.deletedValues = deletedValues;
	}

	public String getOperationList() {
		return operationList;
	}

	public void setOperationList(String operationList) {
		this.operationList = operationList;
	}

	public long getRuleSetId() {
		return ruleSetId;
	}

	public void setRuleSetId(long ruleSetId) {
		this.ruleSetId = ruleSetId;
	}

	public String getRuleSetName() {
		return ruleSetName;
	}

	public void setRuleSetName(String ruleSetName) {
		this.ruleSetName = ruleSetName;
	}

	public long getRuleId() {
		return ruleId;
	}

	public void setRuleId(long ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleSetNametxt() {
		return ruleSetNametxt;
	}

	public void setRuleSetNametxt(String ruleSetNametxt) {
		this.ruleSetNametxt = ruleSetNametxt;
	}

}
