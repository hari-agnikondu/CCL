package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 * The persistent class for the GroupAccessProductID database table.
 * 
 */
@Embeddable
public class GroupAccessProductID implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManyToOne
	private GroupAccess groupAccess;		
	@ManyToOne
	private Product product;
	
	public GroupAccessProductID(){
		super();
	}

	public GroupAccess getGroupAccess() {
		return groupAccess;
	}

	public void setGroupAccess(GroupAccess groupAccess) {
		this.groupAccess = groupAccess;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


}