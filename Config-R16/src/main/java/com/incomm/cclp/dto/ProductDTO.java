package com.incomm.cclp.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Transient;

import com.incomm.cclp.domain.CardRange;
import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.PackageDefinition;
import com.incomm.cclp.domain.ProductPurse;
import com.incomm.cclp.domain.Purse;

/**
 * @author svinoth
 *
 */
public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long productId;

	private String productName;

	private String description;

	private String attributes;

	private Map<String, Map<String, Object>> attributesMap;

	private String isActive;
	
	private String productShortName;

	private List<String> cardRanges;
	
	private List<String> partnerCurrency;

	private List<String> packageIds;

	private List<String> supportedPurse;

	private String upc;

	private Long parentProductId;

	private String parentProductName;

	private Long issuerId;

	private String issuerName;

	private Long partnerId;

	private String partnerName;
	
	private Long programId;

	private Long insUser;

	private Date insDate;

	private Long lastUpdUser;

	private Date lastUpdDate;

	private Map<String, String> productAttributes;

	private Map<String, Object> generalAttributes;
	
	
	
	/**
	 * by ulagan*/
	
	private List<Purse> listOfPurse;
	
	


	private List<PackageDefinition> listOfPackageDefinition;
	
	private List<CardRange> listOfCardRange;
	
	private List<CurrencyCode> listOfCurrencyCode;
	
	@Transient
	private List<ProductPurse> listProductPurse;
	/**
	 * *by ulagan*/
	
	
	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	
	public List<Purse> getListOfPurse() {
		return listOfPurse;
	}

	public void setListOfPurse(List<Purse> listOfPurse) {
		this.listOfPurse = listOfPurse;
	}

	public List<PackageDefinition> getListOfPackageDefinition() {
		return listOfPackageDefinition;
	}

	public void setListOfPackageDefinition(List<PackageDefinition> listOfPackageDefinition) {
		this.listOfPackageDefinition = listOfPackageDefinition;
	}

	public List<CardRange> getListOfCardRange() {
		return listOfCardRange;
	}

	public void setListOfCardRange(List<CardRange> listOfCardRange) {
		this.listOfCardRange = listOfCardRange;
	}

	public void setPackageIds(List<String> packageIds) {
		this.packageIds = packageIds;
	}
	
	
	

	public List<String> getCardRanges() {
		return cardRanges;
	}

	public void setCardRanges(List<String> cardRanges) {
		this.cardRanges = cardRanges;
	}

	public List<String> getPackageIds() {
		return packageIds;
	}

	public void setPackageId(List<String> packageIds) {
		this.packageIds = packageIds;
	}

	public List<String> getSupportedPurse() {
		return supportedPurse;
	}

	public void setSupportedPurse(List<String> supportedPurse) {
		this.supportedPurse = supportedPurse;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getProductShortName() {
		return productShortName;
	}

	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}

	public Map<String, String> getProductAttributes() {
		return productAttributes;
	}

	public void setProductAttributes(Map<String, String> productAttributes) {
		this.productAttributes = productAttributes;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public ProductDTO() {
		super();
	}

	public ProductDTO(Long productId, String productName, String description, String attributes, String isActive,
			Long parentProductId, String parentProductName, Long issuerId, String issuerName, Long partnerId,
			String partnerName, Long insUser, Date insDate, Long lastUpdUser, Date lastUpdDate,Long programId) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.description = description;
		this.attributes = attributes;
		this.isActive = isActive;
		this.parentProductId = parentProductId;
		this.parentProductName = parentProductName;
		this.issuerId = issuerId;
		this.issuerName = issuerName;
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
		this.programId = programId;
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

	public Long getParentProductId() {
		return parentProductId;
	}

	public void setParentProductId(Long parentProductId) {
		this.parentProductId = parentProductId;
	}

	public String getParentProductName() {
		return parentProductName;
	}

	public void setParentProductName(String parentProductName) {
		this.parentProductName = parentProductName;
	}

	public Long getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(Long issuerId) {
		this.issuerId = issuerId;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public Long getInsUser() {
		return insUser;
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

	public ProductDTO(Long productId, String productName, String description, String attributes, String isActive,
			Long parentProductId, String parentProductName, Long issuerId, String issuerName, Long partnerId,
			String partnerName, Long insUser, Date insDate, Long lastUpdUser, Date lastUpdDate,
			String productShortName,Long programId) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.description = description;
		this.attributes = attributes;
		this.isActive = isActive;
		this.parentProductId = parentProductId;
		this.parentProductName = parentProductName;
		this.issuerId = issuerId;
		this.issuerName = issuerName;
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
	
		this.productShortName = productShortName;
		this.programId = programId;
	}

	public ProductDTO(Long productId, String productName, String description,
			Map<String, Map<String, Object>> attributesMap, String isActive, Long parentProductId,
			String parentProductName, Long issuerId, String issuerName, Long partnerId, String partnerName,
			Long insUser, Date insDate, Long lastUpdUser, Date lastUpdDate, String productShortName) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.description = description;
		this.attributesMap = attributesMap;
		this.isActive = isActive;
		this.parentProductId = parentProductId;
		this.parentProductName = parentProductName;
		this.issuerId = issuerId;
		this.issuerName = issuerName;
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
		this.productShortName = productShortName;
	}

	@Override
	public String toString() {
		return "ProductDTO [productId=" + productId + ", productName=" + productName + ", description=" + description
				+ ",attributes=" + attributes + ", isActive=" + isActive + ", parentProductId=" + parentProductId
				+ ", parentProductName=" + parentProductName + ", issuerId=" + issuerId + ", issuerName=" + issuerName
				+ ", partnerId=" + partnerId + ", partnerName=" + partnerName + ", insUser=" + insUser + ", insDate="
				+ insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate + "]";
	}

	public Map<String, Object> getGeneralAttributes() {
		return generalAttributes;
	}

	public void setGeneralAttributes(Map<String, Object> generalAttributes) {
		this.generalAttributes = generalAttributes;
	}

	public Map<String, Map<String, Object>> getAttributesMap() {
		return attributesMap;
	}

	public void setAttributesMap(Map<String, Map<String, Object>> attributesMap) {
		this.attributesMap = attributesMap;
	}
	public List<String> getPartnerCurrency() {
		return partnerCurrency;
	}

	public void setPartnerCurrency(List<String> partnerCurrency) {
		this.partnerCurrency = partnerCurrency;
	}
	
	
	/**
	 * by ulagan*/
	
	
	public ProductDTO(Long productId, String productName, String description,
			Map<String, Map<String, Object>> attributesMap, String isActive, String productShortName,
			List<CardRange> listOfCardRange, List<Purse> listOfPurse,List<ProductPurse> listProductPurse,List<CurrencyCode> listOfCurrencyCode, List<PackageDefinition> listOfPackageDefinition,
			Long parentProductId, String parentProductName, Long issuerId, String issuerName,
			Long partnerId, String partnerName, Long insUser, Date insDate, Long lastUpdUser, Date lastUpdDate,Long programId) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.description = description;
		this.attributesMap = attributesMap;
		this.isActive = isActive;
		this.productShortName = productShortName;
		this.listOfCardRange = listOfCardRange;
		this.listOfPurse = listOfPurse;
		this.listOfCurrencyCode = listOfCurrencyCode;
		this.listOfPackageDefinition = listOfPackageDefinition;
		this.parentProductId = parentProductId;
		this.parentProductName = parentProductName;
		this.issuerId = issuerId;
		this.issuerName = issuerName;
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
		this.programId=programId;
		this.listProductPurse=listProductPurse;
	}

	/**
	 * For general and cvv and card status
	 * @param productId
	 * @param productName
	 * @param description
	 * @param attributes
	 * @param attributesMap
	 * @param isActive
	 * @param productShortName
	 * @param cardRanges
	 * @param packageIds
	 * @param supportedPurse
	 * @param upc
	 * @param parentProductId
	 * @param parentProductName
	 * @param issuerId
	 * @param issuerName
	 * @param partnerId
	 * @param partnerName
	 * @param insUser
	 * @param insDate
	 * @param lastUpdUser
	 * @param lastUpdDate
	 */
	public ProductDTO(Long productId, String productName, String description,
			String attributes, Map<String, Map<String, Object>> attributesMap,
			String isActive, String productShortName, List<String> cardRanges,
			List<String> packageIds, List<String> supportedPurse, String upc,
			Long parentProductId, String parentProductName, Long issuerId,
			String issuerName, Long partnerId, String partnerName,
			Long insUser, Date insDate, Long lastUpdUser, Date lastUpdDate) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.description = description;
		this.attributes = attributes;
		this.attributesMap = attributesMap;
		this.isActive = isActive;
		this.productShortName = productShortName;
		this.cardRanges = cardRanges;
		this.packageIds = packageIds;
		this.supportedPurse = supportedPurse;
		this.upc = upc;
		this.parentProductId = parentProductId;
		this.parentProductName = parentProductName;
		this.issuerId = issuerId;
		this.issuerName = issuerName;
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
	}

	public ProductDTO(Long productId, String productName, String description,
			Map<String, Map<String, Object>> attributesMap, String isActive, Long parentProductId, String parentProductName,
			Long issuerId, String issuerName, Long partnerId, String partnerName, Long insUser, Date insDate, Long lastUpdUser,
			Date lastUpdDate, String productShortName, Long programId) {
		this.productId = productId;
		this.productName = productName;
		this.description = description;
		this.attributesMap = attributesMap;
		this.isActive = isActive;
		this.parentProductId = parentProductId;
		this.parentProductName = parentProductName;
		this.issuerId = issuerId;
		this.issuerName = issuerName;
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
		this.productShortName = productShortName;
		this.programId = programId;
	}
	
	public List<CurrencyCode> getListOfCurrencyCode() {
		return listOfCurrencyCode;
	}

	public void setListOfCurrencyCode(List<CurrencyCode> listOfCurrencyCode) {
		this.listOfCurrencyCode = listOfCurrencyCode;
	}

	public List<ProductPurse> getListProductPurse() {
		return listProductPurse;
	}

	public void setListProductPurse(List<ProductPurse> listProductPurse) {
		this.listProductPurse = listProductPurse;
	}
	

}
