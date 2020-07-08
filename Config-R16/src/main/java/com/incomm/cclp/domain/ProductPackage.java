package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table (name="PRODUCT_PACKAGE")
@AssociationOverride(name = "primaryKey.product", joinColumns = @JoinColumn(name = "PRODUCT_ID"))
@AssociationOverride(name = "primaryKey.packageDefinition", joinColumns = @JoinColumn(name = "PACKAGE_ID"))
public class ProductPackage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ProductPackageId primaryKey = new ProductPackageId();
	
	private long insUser;

	private Date insDate;

	private long lastUpdUser;

	private Date lastUpdDate;

	@EmbeddedId
	public ProductPackageId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(ProductPackageId primaryKey) {
		this.primaryKey = primaryKey;
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
	
	@Transient
	public Product getProduct() {
		return getPrimaryKey().getProduct();
	}

	public void setProduct(Product product) {
		getPrimaryKey().setProduct(product);
	}
	
	@Transient
	public PackageDefinition getPackageDefinition() {
		return getPrimaryKey().getPackageDefinition();
	}
	
	public void setPackageDefinition(PackageDefinition packageDefinition) {
		getPrimaryKey().setPackageDefinition(packageDefinition);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;
		else {
			ProductPackage that = (ProductPackage) obj;
			return Objects.equals(getPrimaryKey(), that.getPrimaryKey());
		}
	}

	@Override
	public int hashCode() {
		return (getPrimaryKey() != null ? getPrimaryKey().hashCode() : 0);
	}
}
