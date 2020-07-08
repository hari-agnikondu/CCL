package com.incomm.cclp.dao;

import com.incomm.cclp.domain.ProductPurse;

public interface ProductPurseDao {

	public int updateAttributes(String productAttributes, Long productId, Long purseId);

	public ProductPurse getProdPurseAttributesByProdPurseId(Long productId, Long purseId);
	
}
