package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class ProductPackageId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Product product;
	
	private PackageDefinition packageDefinition;

	@ManyToOne
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne
	public PackageDefinition getPackageDefinition() {
		return packageDefinition;
	}

	public void setPackageDefinition(PackageDefinition packageDefinition) {
		this.packageDefinition = packageDefinition;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ProductPackageId that = (ProductPackageId) obj;

        return Objects.equals(product, that.product) &&
        		Objects.equals(packageDefinition, that.packageDefinition);
    }
	
	@Override
	public int hashCode() {
		return Objects.hash(product, packageDefinition);
    }
	
}
