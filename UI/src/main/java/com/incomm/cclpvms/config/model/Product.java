package com.incomm.cclpvms.config.model;

import java.util.List;
import java.util.Map;

import com.incomm.cclpvms.config.validator.FieldValidation;
import com.incomm.cclpvms.config.validator.LimitMap;
import com.incomm.cclpvms.config.validator.MaintenanceFee;
import com.incomm.cclpvms.config.validator.MonthlyFeeCapFee;
import com.incomm.cclpvms.config.validator.TransactionFee;

public class Product {

	
	@FieldValidation(notEmpty = true, max = 100, messageNotEmpty = "{messageNotEmpty.product.productName}", messageLength = "{messageLength.product.productName}", messagePattern = "{messagepattern.product.productName}", groups = ValidationStepOne.class)
	private String productName;

	private String partnerName;
	private Long programId;
	private String parentProductName;
	private String issuerName;
	private String action;
	@FieldValidation(notEmpty = false, max = 255, messageNotEmpty = "messageNotEmpty.product.productdesc}", 
			messageLength = "{messageLength.product.productdesc}", groups = ValidationStepTwo.class)
	private String description;
	
	private String isActive;
	@FieldValidation(notEmpty = true, pattern = "^[A-Za-z0-9 ]+$", min = 2, max = 50, 
			messageNotEmpty = "{messageNotEmpty.product.productShortName}", messageLength = "{messageLength.product.productShortName}",

	messagePattern = "{messagepattern.product.productShortName}", groups = ValidationStepTwo.class)
	private String productShortName;

	private  List<String> cardRanges;
	private List<Object> cardRangeObject;
	
	private List<String> packageId;
	private List<Object> packageIdObject;

	private List<String> packageUpdate;
	
	public List<String> getPackageUpdate() {
		return packageUpdate;
	}

	public void setPackageUpdate(List<String> packageUpdate) {
		this.packageUpdate = packageUpdate;
	}
	private List<String> supportedPurse;
	private List<String> partnerCurrency;
	private List<String> partnerCurrencyUpdate;
	private List<Object> supportedPurseObject;
	private List<Object> listProductPurseObject;
	private String upc;
	

	private Map<String, Map<String,String>> attributes ;
	
	
	private Long issuerId;
	private Long partnerId;
	private Long parentProductId;
	public List<String> getSupportedPurseUpdate() {
		return supportedPurseUpdate;
	}

	public void setSupportedPurseUpdate(List<String> supportedPurseUpdate) {
		this.supportedPurseUpdate = supportedPurseUpdate;
	}
	private	Map<String,Object> limitsMap;
	@LimitMap(groups=Limitmap.class)
	@MaintenanceFee(groups=maintenanceFee.class)
	@MonthlyFeeCapFee(groups=monthlyFeeCapFee.class)
	@TransactionFee(groups=transactionFee.class)
	private Map<String, Object> productAttributes ;
	private Long productId;
	
	private Long ruleSetId;
	private Long purseId;
	
	

	private  List<String> cardRangesUpdate;
	private  List<String> supportedPurseUpdate;

	public List<String> getCardRangesUpdate() {
		return cardRangesUpdate;
	}

	public void setCardRangesUpdate(List<String> cardRangesUpdate) {
		this.cardRangesUpdate = cardRangesUpdate;
	}
	private String [] txnTypeId;
   
	private String [] activationId;
	
	
	 public String[] getTxnTypeId() {
			return txnTypeId;
		}

		public void setTxnTypeId(String[] txnTypeId) {
			this.txnTypeId = txnTypeId;
		}

		public String[] getActivationId() {
			return activationId;
		}

		public void setActivationId(String[] activationId) {
			this.activationId = activationId;
		}
	

	public List<String> getCardRanges() {
		return cardRanges;
	}

	public void setCardRanges(List<String> cardRanges) {
		this.cardRanges = cardRanges;
	}

	public List<String> getPackageId() {
		return packageId;
	}

	public void setPackageId(List<String> packageId) {
		this.packageId = packageId;
	}

	public List<String> getSupportedPurse() {
		return supportedPurse;
	}

	public void setSupportedPurse(List<String> supportedPurse) {
		this.supportedPurse = supportedPurse;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(Long issuerId) {
		this.issuerId = issuerId;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}
	
		public String getProductShortName() {
		return productShortName;
	}

	
	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}

	private Map<String,String> prodAttributes;
	
	public Map<String, String> getProdAttributes() {
		return prodAttributes;
	}

	public void setProdAttributes(Map<String, String> prodAttributes) {
		this.prodAttributes = prodAttributes;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}


	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getParentProductName() {
		return parentProductName;
	}

	public void setParentProductName(String parentProductName) {
		this.parentProductName = parentProductName;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, Map<String, String>> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Map<String, String>> attributes) {
		this.attributes = attributes;
	}


	public Long getParentProductId() {
		return parentProductId;
	}

	public void setParentProductId(Long parentProductId) {
		this.parentProductId = parentProductId;
	}
	public Map<String, Object> getLimitsMap() {
		return limitsMap;
	}

	public void setLimitsMap(Map<String, Object> limitsMap) {
		this.limitsMap = limitsMap;
	}

	private String copyAllCheck;
	
	public String getCopyAllCheck() {
		return copyAllCheck;
	}

	public void setCopyAllCheck(String copyAllCheck) {
		this.copyAllCheck = copyAllCheck;
	}
	
	
	
	
	
	
	
	public Product(String productName, String partnerName, String parentProductName, String issuerName, String action,
			String description, String isActive, String productShortName, List<String> cardRanges, List<String> packageId,
			List<String> supportedPurse, String upc, Map<String, Map<String, String>> attributes, String issuerId,
			String partnerId, Long parentProductId, Map<String, Object> productAttributes, Long productId,
			Map<String, String> prodAttributes, List<Object> cardRangeObject, List<Object> packageIdObject,
			List<Object> supportedPurseObject,Long programId,List<String> partnerCurrency,List<Object> productPurseObj) {
		super();
		this.productName = productName;
		this.partnerName = partnerName;
		this.parentProductName = parentProductName;
		this.issuerName = issuerName;
		this.action = action;
		this.description = description;
		this.isActive = isActive;
		this.productShortName = productShortName;
		this.cardRanges = cardRanges;
		this.packageId = packageId;
		this.supportedPurse = supportedPurse;
		this.upc = upc;
		this.attributes = attributes;
		this.issuerId = Long.parseLong(issuerId);
		this.partnerId = Long.parseLong(partnerId);
		this.parentProductId = parentProductId;
		this.productAttributes = productAttributes;
		this.productId = productId;
		this.prodAttributes = prodAttributes;
		this.cardRangeObject = cardRangeObject;
		this.packageIdObject = packageIdObject;
		this.supportedPurseObject = supportedPurseObject;
		this.programId = programId;
		this.partnerCurrency = partnerCurrency;
		this.listProductPurseObject=productPurseObj;
		
	}

	public Product() {
	}

	public Long getRuleSetId() {
		return ruleSetId;
	}

	public void setRuleSetId(Long ruleSetId) {
		this.ruleSetId = ruleSetId;
	}


	public Map<String, Object> getProductAttributes() {
		return productAttributes;
	}

	public void setProductAttributes(Map<String, Object> productAttributes) {
		this.productAttributes = productAttributes;
	}



	public interface ValidationStepOne {


	}

	public interface ValidationStepTwo {

	}

	@Override
	public String toString() {
		return "Product [productName=" + productName + ", attributes=" + attributes + ", productAttributes="
				+ productAttributes + ", prodAttributes=" + prodAttributes + "]";
	}

	public Product(Map <String,Object> productAttributes) {
		
		this.productAttributes=productAttributes;
		
	}

	public List<Object> getCardRangeObject() {
		return cardRangeObject;
	}

	public void setCardRangeObject(List<Object> cardRangeObject) {
		this.cardRangeObject = cardRangeObject;
	}

	public List<Object> getSupportedPurseObject() {
		return supportedPurseObject;
	}

	public void setSupportedPurseObject(List<Object> supportedPurseObject) {
		this.supportedPurseObject = supportedPurseObject;
	}

	public List<Object> getPackageIdObject() {
		return packageIdObject;
	}

	public void setPackageIdObject(List<Object> packageIdObject) {
		this.packageIdObject = packageIdObject;
	}

	public List<String> getPartnerCurrency() {
		return partnerCurrency;
	}

	public void setPartnerCurrency(List<String> partnerCurrency) {
		this.partnerCurrency = partnerCurrency;
	}

	public List<String> getPartnerCurrencyUpdate() {
		return partnerCurrencyUpdate;
	}

	public void setPartnerCurrencyUpdate(List<String> partnerCurrencyUpdate) {
		this.partnerCurrencyUpdate = partnerCurrencyUpdate;
	}

	public Long getPurseId() {
		return purseId;
	}

	public void setPurseId(Long purseId) {
		this.purseId = purseId;
	}

	public List<Object> getListProductPurseObject() {
		return listProductPurseObject;
	}

	public void setListProductPurseObject(List<Object> listProductPurseObject) {
		this.listProductPurseObject = listProductPurseObject;
	}
	
}
