package com.incomm.cclpvms.config.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long productId;
	private Long issuerId;
	private Long partnerId;
	private String productName;
	private String partnerName;
	private Long programId;

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	private String parentProductName;
	private String issuerName;

	private String productShortName;

	private List<String> cardRanges;
	private List<String> partnerCurrency;

	private List<String> packageId;

	private List<String> supportedPurse;

	private String upc;

	private String isActive;

	private String description;
	private Long insUser;

	private Date insDate;

	private Long lastUpdUser;

	private Date lastUpdDate;

	private Map<String, String> prodAttributes;

	private Map<String, Map<String, String>> attributesMap;

	private String attributes;

	private Long parentProductId;
	private String[] txnTypeId;

	private String[] activationId;

	public String[] getTxnTypeId() {
		return txnTypeId;
	}

	private List<String> packageUpdate;

	public List<String> getPackageUpdate() {
		return packageUpdate;
	}

	public void setPackageUpdate(List<String> packageUpdate) {
		this.packageUpdate = packageUpdate;
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

	public List<String> getCardRangesUpdate() {
		return cardRangesUpdate;
	}

	public void setCardRangesUpdate(List<String> cardRangesUpdate) {
		this.cardRangesUpdate = cardRangesUpdate;
	}

	public List<String> getSupportedPurseUpdate() {
		return supportedPurseUpdate;
	}

	public void setSupportedPurseUpdate(List<String> supportedPurseUpdate) {
		this.supportedPurseUpdate = supportedPurseUpdate;
	}

	private List<String> cardRangesUpdate;
	private List<String> supportedPurseUpdate;

	public Long getParentProductId() {
		return parentProductId;
	}

	public void setParentProductId(Long parentProductId) {
		this.parentProductId = parentProductId;
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

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getProductShortName() {
		return productShortName;
	}

	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public ProductDTO() {
	}

	public Map<String, String> getProdAttributes() {
		return prodAttributes;
	}

	public void setProdAttributes(Map<String, String> prodAttributes) {
		this.prodAttributes = prodAttributes;
	}

	public Map<String, Map<String, String>> getAttributesMap() {
		return attributesMap;
	}

	public void setAttributesMap(Map<String, Map<String, String>> attributesMap) {
		this.attributesMap = attributesMap;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

	public Long getInsUser() {
		return insUser;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public Long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public List<String> getPartnerCurrency() {
		return partnerCurrency;
	}

	public void setPartnerCurrency(List<String> partnerCurrency) {
		this.partnerCurrency = partnerCurrency;
	}


	
	@Override
	public String toString() {
		return "ProductDTO [productId=" + productId + ", issuerId=" + issuerId + ", partnerId=" + partnerId
				+ ", productName=" + productName + ", partnerName=" + partnerName + ", parentProductName="
				+ parentProductName + ", issuerName=" + issuerName + ", productShortName=" + productShortName
				+ ", cardRanges=" + cardRanges + ", packageId=" + packageId + ", supportedPurse=" + supportedPurse
				+ ", upc=" + upc + ", isActive=" + isActive + ", description=" + description + ", insUser=" + insUser
				+ ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ ", prodAttributes=" + prodAttributes + ", attributesMap=" + attributesMap + ", attributes="
				+ attributes + "]";
	}

	public ProductDTO(Long productId, Long issuerId, Long partnerId, String productName, String partnerName,
			String parentProductName, String issuerName, String productShortName, List<String> cardRanges,
			List<String> packageId, List<String> supportedPurse, String upc, String isActive, String description,
			Long insUser, Date insDate, Long lastUpdUser, Date lastUpdDate, Map<String, String> prodAttributes,
			Map<String, Map<String, String>> attributes) {
		super();
		this.productId = productId;
		this.issuerId = issuerId;
		this.partnerId = partnerId;
		this.productName = productName;
		this.partnerName = partnerName;
		this.parentProductName = parentProductName;
		this.issuerName = issuerName;
		this.productShortName = productShortName;
		this.cardRanges = cardRanges;
		this.packageId = packageId;
		this.supportedPurse = supportedPurse;
		this.upc = upc;
		this.isActive = isActive;
		this.description = description;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
		this.prodAttributes = prodAttributes;
		this.attributesMap = attributesMap;
	}

	private String copyAllCheck;

	public String getCopyAllCheck() {
		return copyAllCheck;
	}

	public void setCopyAllCheck(String copyAllCheck) {
		this.copyAllCheck = copyAllCheck;
	}

}
