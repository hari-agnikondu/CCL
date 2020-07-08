package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the PRODUCT database table.
 * 
 */
@Entity
@Table(name="clp_configuration.PRODUCT")//added by Hari, because we are accessing the table from other schema without synonym
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PRODUCT_SEQ_GEN", sequenceName="PRODUCT_PRODUCT_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCT_SEQ_GEN")	
	@Column(name="PRODUCT_ID")
	private Long productId;		

	@Column(name="PRODUCT_NAME")
	private String productName;

	@Column(name="DESCRIPTION")
	private String description;

	@Lob
	@Column(name="ATTRIBUTES")
	private String attributes;

	@Column(name="IS_ACTIVE")
	private String isActive;

	@Column(name="PARENT_PRODUCT_ID")
	private Long parentProductId;

	@OneToOne(optional=false ,fetch=FetchType.LAZY)
	@JoinColumn(name="PARENT_PRODUCT_ID",referencedColumnName="PRODUCT_ID" ,insertable=false,updatable=false)
	private Product parentProduct;

	@ManyToOne(optional=false)
	@JoinColumn(name="ISSUER_ID",referencedColumnName="ISSUER_ID")
	private Issuer issuer;

	@ManyToOne(optional=false)
	@JoinColumn(name="PARTNER_ID" ,referencedColumnName="PARTNER_ID")
	private Partner partner;

	@Column(name="INS_USER")
	private Long insUser;

	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE")
	private Date insDate;

	@Column(name="LAST_UPD_USER")
	private Long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;

	@Column(name="PRODUCT_SHORT_NAME")
	private String productShortName;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.product", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, orphanRemoval = true)
	private List<ProductPackage> listOfProductPackage;

	public List<ProductPackage> getListOfProductPackage() {
		return listOfProductPackage;
	}

	public void setListOfProductPackage(List<ProductPackage> listOfProductPackage) {
		this.listOfProductPackage = listOfProductPackage;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.product", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, orphanRemoval = true)
	private List<ProductCardRange> listProductCardRange;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.product", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, orphanRemoval = true)
	private List<ProductPurse> listProductPurse;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
	private List<ProductAlert> listProductAlert;

	public List<ProductAlert> getListProductAlert() {
		return listProductAlert;
	}

	public void setListProductAlert(List<ProductAlert> listProductAlert) {
		this.listProductAlert = listProductAlert;
	}

	public List<ProductCardRange> getListProductCardRange() {
		return listProductCardRange;
	}

	public void setListProductCardRange(List<ProductCardRange> listProductCardRange) {
		this.listProductCardRange = listProductCardRange;
	}

	public List<ProductPurse> getListProductPurse() {
		return listProductPurse;
	}

	public void setListProductPurse(List<ProductPurse> listProductPurse) {
		this.listProductPurse = listProductPurse;
	}

	public String getProductShortName() {
		return productShortName;
	}

	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
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

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
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

	public Product getParentProduct() {
		return parentProduct;
	}


	public Issuer getIssuer() {
		return issuer;
	}

	public void setIssuer(Issuer issuer) {
		this.issuer = issuer;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
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
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
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

	public Product() {

	}
	public Product(Long productId, String productName, String description, String attributes, String isActive,
			Long parentProductId, Product parentProduct, Issuer issuer, Partner partner, Long insUser, Date insDate,
			Long lastUpdUser, Date lastUpdDate, String productShortName) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.description = description;
		this.attributes = attributes;
		this.isActive = isActive;
		this.parentProductId = parentProductId;
		this.parentProduct = parentProduct;
		this.issuer = issuer;
		this.partner = partner;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
		this.productShortName = productShortName;

	}
	
	public Product(Long productId, String productName) {
		this.productId = productId;
		this.productName = productName;
	}

	public Product(Long productId, String productName, String description, String attributes, String isActive,
			Long parentProductId, Product parentProduct, Issuer issuer, Partner partner, Long insUser, Date insDate,
			Long lastUpdUser, Date lastUpdDate, String productShortName, List<ProductPackage> listOfProductPackage,
			List<ProductCardRange> listProductCardRange, List<ProductPurse> listProductPurse) {
		this.productId = productId;
		this.productName = productName;
		this.description = description;
		this.attributes = attributes;
		this.isActive = isActive;
		this.parentProductId = parentProductId;
		this.parentProduct = parentProduct;
		this.issuer = issuer;
		this.partner = partner;
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
		this.productShortName = productShortName;
		this.listOfProductPackage = listOfProductPackage;
		this.listProductCardRange = listProductCardRange;
		this.listProductPurse = listProductPurse;
	}

}