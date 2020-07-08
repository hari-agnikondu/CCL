/**
 * 
 */
package com.incomm.cclp.transaction.bean;

import java.io.Serializable;

/**
 * @author ard
 *
 */
public class RuleResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6712379254326844019L;

	/**
	 * Rule Id
	 */
	private int ruleId;

	/**
	 * Rule Name
	 */
	private String ruleName;

	/**
	 * Rule Result populated by rule engine
	 */
	private boolean ruleResults;

	/**
	 * For future use
	 */
	private String ruleOutputType;

	private String ruleEvaluateRslt;

	public String getRuleEvaluateRslt() {
		return ruleEvaluateRslt;
	}

	public void setRuleEvaluateRslt(String ruleEvaluateRslt) {
		this.ruleEvaluateRslt = ruleEvaluateRslt;
	}

	/**
	 * Rule output returned by rule engine based on "THEN" condition
	 */
	private String ruleOutput;

	/**
	 * @return the ruleId
	 */
	public int getRuleId() {
		return ruleId;
	}

	/**
	 * @param ruleId the ruleId to set
	 */
	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * @return the ruleResult
	 */
	public boolean isRuleResults() {
		return ruleResults;
	}

	/**
	 * @param ruleResult the ruleResult to set
	 */
	public void setRuleResults(boolean ruleResult) {
		this.ruleResults = ruleResult;
	}

	/**
	 * @return the ruleOutput
	 */
	public String getRuleOutput() {
		return ruleOutput;
	}

	/**
	 * @param ruleOutput the ruleOutput to set
	 */
	public void setRuleOutput(String ruleOutput) {
		this.ruleOutput = ruleOutput;
	}

	/**
	 * @return the ruleOutputType
	 */
	public String getRuleOutputType() {
		return ruleOutputType;
	}

	/**
	 * @param ruleOutputType the ruleOutputType to set
	 */
	public void setRuleOutputType(String ruleOutputType) {
		this.ruleOutputType = ruleOutputType;
	}

}
