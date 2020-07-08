package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class MerchantProductRedemptionId implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	@ManyToOne(/*fetch = FetchType.LAZY*/)
	@JoinColumn(name = "MERCHANT_ID", referencedColumnName = "MERCHANT_ID")
	MerchantRedemption merchant;
	
	@ManyToOne(/*fetch = FetchType.LAZY*/)
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
	Product product;

	public MerchantRedemption getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantRedemption merchant) {
		this.merchant = merchant;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
