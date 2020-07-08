/**
 * 
 */
package com.incomm.cclpvms.config.model;

import com.incomm.cclpvms.config.model.Issuer.ValidationStepOne;
import com.incomm.cclpvms.config.model.Issuer.SearchScreen;
import com.incomm.cclpvms.config.validator.EmptyValidation;
import com.incomm.cclpvms.config.validator.FieldValidation;

/**
 * @author abutani
 *
 */
public class Rule {

	private long ruleId;

	@EmptyValidation(notEmpty = false, pattern = "^[A-Za-z0-9 ,&.;'_-]+$", min = 0, max = 100,  messageLength = "{messageLength.rule.ruleName}",
			messagePattern = "{messagepattern.rule.ruleName}", 
			groups = SearchScreen.class)

	@FieldValidation(notEmpty = true, pattern = "^[A-Za-z0-9 ,&.;'_-]+$",min=2,max = 100,
	messageNotEmpty = "{messageNotEmpty.rule.ruleName}", 
	messageLength = "{messageLength.rule.ruleName}",
	startsWithSpace = "{startsWithSpace.ruleName.message}",
	groups = ValidationStepOne.class)

	private String ruleName;

	private String[] txnTypeId;

	private String action;

	private String jsonReq;

	private String filters;

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

	public String[] getTxnTypeId() {
		return txnTypeId;
	}

	public void setTxnTypeId(String[] txnTypeId) {
		this.txnTypeId = txnTypeId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getJsonReq() {
		return jsonReq;
	}

	public void setJsonReq(String jsonReq) {
		this.jsonReq = jsonReq;
	}

	public String getDisplayRuleName()
	{
		return this.getRuleId() + ":" + this.getRuleName();
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getTxnTypeIds()
	{
		String txnTypes = "";
		StringBuilder sb = new StringBuilder();

		if (this.txnTypeId != null)
		{
			for (String txnType: txnTypeId)
			{
				sb.append(txnType + ",");
			}

			if (sb.toString().length() > 1)
				txnTypes = sb.toString().substring(0, sb.toString().length()-1);
		}

		return txnTypes;
	}
	
	public void setTxnTypeIds(String txnTypeIds)
	{
		this.txnTypeId = txnTypeIds.split(",");

	}
}
