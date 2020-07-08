package com.incomm.cclpvms.config.model;

import com.incomm.cclpvms.config.validator.FieldValidation;

public class MerchantProduct {


	private Long merchantId;
	private Long productId;
	@FieldValidation(notEmpty = false, pattern = "^[A-Za-z0-9 ,&.;'_-]+$", max = 100, messageNotEmpty = "{messageNotEmpty.merchantProduct.merchantName}", messageLength = "{messageLength.merchantProduct.merchantName}", messagePattern = "{messagepattern.merchantProduct.merchantName}", groups = ValidationMerchantSearch.class)
	private String merchantName;

	@FieldValidation(notEmpty = false, pattern = "^[A-Za-z0-9 ]+$", max = 100, messageNotEmpty = "{messageNotEmpty.merchantProduct.productName}", messageLength = "{messageLength.merchantProduct.productName}", messagePattern = "{messagepattern.merchantProduct.productName}", groups = ValidationMerchantSearch.class)
	private String productName;
	 
	
	
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	
	
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	

	public MerchantProduct() {


	}
	
	
	
	public MerchantProduct(Long merchantId, Long productId, String merchantName, String productName) {
		super();
		this.merchantId = merchantId;
		this.productId = productId;
		this.merchantName = merchantName;
		this.productName = productName;
	}
	@Override
	public String toString() {
		return "MerchantProduct [merchantId=" + merchantId + ", productId=" + productId + ", merchantName="
				+ merchantName + ", productName=" + productName + "]";
	}

	public interface ValidationMerchantSearch {
		 
    }
	


}
