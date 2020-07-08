package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;


@Entity
@Audited
@Table(name = "PACKAGE_ATTRIBUTES")
public class PackageAttributes implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    private PackageAtrributesId primaryKey = new PackageAtrributesId();
	

	@Column(name="ATTRIBUTE_VALUE")
	private String attributeValue;
	
	@Column(name="INS_USER")
	private long insUser;
	
	@Column(name="INS_DATE")
	private Date insDate;
	
	@Column(name="LAST_UPD_USER")
	private long lupdUser;
	
	@Column(name="LAST_UPD_DATE")
	private Date lupdDate;

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public long getLupdUser() {
		return lupdUser;
	}

	public void setLupdUser(long lupdUser) {
		this.lupdUser = lupdUser;
	}

	public Date getLupdDate() {
		return lupdDate;
	}

	public void setLupdDate(Date lupdDate) {
		this.lupdDate = lupdDate;
	}
	

	public PackageAtrributesId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(PackageAtrributesId primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PackageAttributes))
			return false;
		PackageAttributes other = (PackageAttributes) obj;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PackageAttributes [attributeValue=" + attributeValue + "]";
	}
	
	
}
