package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "PRODUCT_PURSE")
@AssociationOverride(name = "primaryKey.product", joinColumns = @JoinColumn(name = "PRODUCT_ID"))
@AssociationOverride(name = "primaryKey.purse", joinColumns = @JoinColumn(name = "PURSE_ID"))
public class ProductPurse implements Serializable,Comparable<ProductPurse> {

	private static final long serialVersionUID = 1L;

	// composite-id key
	private ProductPurseId primaryKey = new ProductPurseId();

	private String isDefault;

	private long insUser;

	private Date insDate;

	private long lastUpdUser;

	private Date lastUpdDate;
	
	private Integer purseOrder;
	
	@Lob
	@Column(name="ATTRIBUTES")
	private String attributes;

	public ProductPurse() {
		super();
	}

	@EmbeddedId
	public ProductPurseId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(ProductPurseId primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "IS_DEFAULT")
	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	@Column(name = "INS_USER", updatable = false)
	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "INS_DATE", updatable = false)
	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	@Column(name = "LAST_UPD_USER")
	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPD_DATE")
	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}
	
	@Column(name = "PURSE_ORDER")
	public Integer getPurseOrder() {
		return purseOrder;
	}

	public void setPurseOrder(Integer purseOrder) {
		this.purseOrder = purseOrder;
	}

	@Column(name = "ATTRIBUTES")
	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	@Transient
	public Product getProduct() {
		return getPrimaryKey().getProduct();
	}

	public void setProduct(Product product) {
		getPrimaryKey().setProduct(product);
	}

	@Transient
	public Purse getPurse() {
		return getPrimaryKey().getPurse();
	}

	public void setPurse(Purse purse) {
		getPrimaryKey().setPurse(purse);
	}

	@Override
	public boolean equals(Object obj) {		
		if (this == obj) return true;
		 
		else if (obj == null  )
            return false;
		else if(getClass() != obj.getClass())
			return false;
		else {
        ProductPurse that = (ProductPurse) obj;
        return Objects.equals(getPrimaryKey(), that.getPrimaryKey());
		}
	}

	@Override
	public int hashCode() {
		return (getPrimaryKey() != null ? getPrimaryKey().hashCode() : 0);
	}

	@Override
	public int compareTo(ProductPurse o) {
		
		return this.purseOrder-o.getPurseOrder();
	}
	@Override
	public String toString() {
		return "ProductPurse [primaryKey=" + primaryKey + ", isDefault=" + isDefault + ", purseOrder=" + purseOrder
				+ ", insUser=" + insUser + ", insDate=" + insDate
				+ ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate + ", extPurseId=" 
				+ "]";
	}
}
