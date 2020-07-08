/**
 * 
 */
package com.incomm.cclp.transaction.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author ard
 *
 */
public class RuleSetResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6041472543693957209L;

	/**
	 * The final result after executing a ruleset containing multiple rule expressions
	 */
	private boolean ruleSetResults;

	/**
	 * Ruleset response code representing either 00 or 89 00 - success 89 - failure and txn to be declined
	 */
	private String ruleSetResponse;

	/**
	 * List containing the entire rule result of a ruleset
	 */
	private List<RuleResult> rules;

	/**
	 * @return the ruleSetResult
	 */
	public boolean isRuleSetResults() {
		return ruleSetResults;
	}

	/**
	 * @param ruleSetResult the ruleSetResult to set
	 */
	public void setRuleSetResults(boolean ruleSetResult) {
		this.ruleSetResults = ruleSetResult;
	}

	/**
	 * @return the ruleSetResponse
	 */
	public String getRuleSetResponse() {
		return ruleSetResponse;
	}

	/**
	 * @param ruleSetResponse the ruleSetResponse to set
	 */
	public void setRuleSetResponse(String ruleSetResponse) {
		this.ruleSetResponse = ruleSetResponse;
	}

	/**
	 * @return the rules
	 */
	public List<RuleResult> getRules() {
		return rules;
	}

	/**
	 * @param rules the rules to set
	 */
	public void setRules(List<RuleResult> rules) {
		this.rules = rules;
	}

}
