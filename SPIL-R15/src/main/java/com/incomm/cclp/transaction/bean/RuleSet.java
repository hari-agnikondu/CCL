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
public class RuleSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * List containing Rule object
	 */
	private List<Rule> rules;

	/**
	 * Placeholder for the logical operator that has to applied on multiple rules. Can be AND(&&) or OR(||).
	 */

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	/**
	 * constructor
	 * 
	 * @param rules
	 * @param ruleSetBooleanOpr
	 */
	public RuleSet(List<Rule> rules) {
		this.rules = rules;

	}

	public RuleSet() {

	}

	/**
	 * @return the rules
	 */
	public List<Rule> getRules() {
		return rules;
	}

	/**
	 * @return the ruleSetBooleanOpr
	 */

}
