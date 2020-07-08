/**
 * 
 */
package com.incomm.cclp.transaction.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;

/**
 * @author ard
 *
 */
@EqualsAndHashCode
public class Rule implements Comparable<Rule>, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Rule Id
	 */
	private int ruleId;
	/**
	 * Rule Name
	 */
	private String ruleName;
	/**
	 * Rule Priority assigned at rule set levvel
	 */
	private int rulePriority;
	/**
	 * Rule Expression
	 */
	private String ruleExpression;
	/**
	 * Rule Action type
	 */
	private String ruleActionType;
	/**
	 * THEN condition value
	 */
	private String thenConditionValue;
	/**
	 * THEN condition method
	 */
	private String thenConditionMethod;

	public String getRuleTransactionType() {
		return ruleTransactionType;
	}

	public void setRuleTransactionType(String ruleTransactionType) {
		this.ruleTransactionType = ruleTransactionType;
	}

	/**
	 * THEN condition param
	 */
	private String thenConditionInParam;
	private String ruleTransactionType;
	private Map<String, String> ruleDetails = new HashMap<>();
	private Map<String, String> ruleEvaluateRslt = new HashMap<>();

	public Map<String, String> getRuleEvaluateRslt() {
		return ruleEvaluateRslt;
	}

	public void setRuleEvaluateRslt(Map<String, String> ruleEvaluateRslt) {
		this.ruleEvaluateRslt = ruleEvaluateRslt;
	}

	public Map<String, String> getRuleDetails() {
		return ruleDetails;
	}

	public void setRuleDetails(Map<String, String> ruleDetails) {
		this.ruleDetails = ruleDetails;
	}

	/**
	 * 
	 */
	public Rule() {

	}

	/**
	 * @param ruleId
	 * @param ruleName
	 * @param rulePriority
	 * @param ruleExpression
	 * @param ruleActionType
	 * @param thenConditionValue
	 * @param thenConditionMethod
	 * @param thenConditionInParam
	 */
	public Rule(int ruleId, String ruleName, int rulePriority, String ruleExpression, String ruleActionType, String thenConditionValue,
			String thenConditionMethod, String thenConditionInParam, Map<String, String> ruleDetails, String ruleTransactionType) {

		this.ruleId = ruleId;
		this.ruleName = ruleName;
		this.rulePriority = rulePriority;
		this.ruleExpression = ruleExpression;
		this.ruleActionType = ruleActionType;
		this.thenConditionValue = thenConditionValue;
		this.thenConditionMethod = thenConditionMethod;
		this.thenConditionInParam = thenConditionInParam;
		this.ruleDetails = ruleDetails;
		this.ruleTransactionType = ruleTransactionType;
	}

	public Rule(int ruleId, String ruleName, String ruleExpression, String ruleActionType, Map<String, String> ruleDetails,
			String ruleTransactionType) {

		this.ruleId = ruleId;
		this.ruleName = ruleName;
		this.ruleExpression = ruleExpression;
		this.ruleActionType = ruleActionType;
		this.ruleDetails = ruleDetails;
		this.ruleTransactionType = ruleTransactionType;
	}

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
	 * @return the rulePriority
	 */
	public int getRulePriority() {
		return rulePriority;
	}

	/**
	 * @param rulePriority the rulePriority to set
	 */
	public void setRulePriority(int rulePriority) {
		this.rulePriority = rulePriority;
	}

	/**
	 * @return the ruleExpression
	 */
	public String getRuleExpression() {
		return ruleExpression;
	}

	/**
	 * @param ruleExpression the ruleExpression to set
	 */
	public void setRuleExpression(String ruleExpression) {
		this.ruleExpression = ruleExpression;
	}

	/**
	 * @return the ruleActionType
	 */
	public String getRuleActionType() {
		return ruleActionType;
	}

	/**
	 * @param ruleActionType the ruleActionType to set
	 */
	public void setRuleActionType(String ruleActionType) {
		this.ruleActionType = ruleActionType;
	}

	/**
	 * @return the thenConditionValue
	 */
	public String getThenConditionValue() {
		return thenConditionValue;
	}

	/**
	 * @param thenConditionValue the thenConditionValue to set
	 */
	public void setThenConditionValue(String thenConditionValue) {
		this.thenConditionValue = thenConditionValue;
	}

	/**
	 * @return the thenConditionMethod
	 */
	public String getThenConditionMethod() {
		return thenConditionMethod;
	}

	/**
	 * @param thenConditionMethod the thenConditionMethod to set
	 */
	public void setThenConditionMethod(String thenConditionMethod) {
		this.thenConditionMethod = thenConditionMethod;
	}

	/**
	 * @return the thenConditionInParam
	 */
	public String getThenConditionInParam() {
		return thenConditionInParam;
	}

	/**
	 * @param thenConditionInParam the thenConditionInParam to set
	 */
	public void setThenConditionInParam(String thenConditionInParam) {
		this.thenConditionInParam = thenConditionInParam;
	}

	@Override
	public int compareTo(Rule rule) {
		return rule.rulePriority - this.rulePriority;
	}

	public String toString() {
		return String.format("(%d)", rulePriority);
	}

}
