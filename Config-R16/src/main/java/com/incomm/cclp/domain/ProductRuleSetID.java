package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * The persistent class for the ProductRuleSetID database table.
 * 
 */
@Embeddable
public class ProductRuleSetID implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="RULESET_ID")
	private Long ruleSetId;		

	@Column(name="PRODUCT_ID")
	private Long productId;
	
	public ProductRuleSetID(){
		super();
	}
	
	public ProductRuleSetID(Long ruleSetId, Long productId) {
		
		this.ruleSetId = ruleSetId;
		this.productId = productId;
	}

	public Long getRuleSetId() {
		return ruleSetId;
	}

	public void setRuleSetId(Long ruleSetId) {
		this.ruleSetId = ruleSetId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

}