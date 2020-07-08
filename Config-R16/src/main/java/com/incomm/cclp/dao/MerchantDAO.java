/**
 * 
 */
package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.Merchant;
import com.incomm.cclp.domain.MerchantProduct;
import com.incomm.cclp.domain.MerchantProductId;

public interface MerchantDAO {

	public void createMerchant(Merchant merchant);

	public List<Merchant> getAllMerchants();

	public List<Merchant> getMerchantsByName(String merchantName);

	public void updateMerchant(Merchant merchant);

	public Merchant getMerchantById(Long merchantId);

	public void deleteMerchant(Merchant merchant);

	public void assignProductToMerchant(MerchantProduct merchantProduct);

	public void removeMerchantProductMapping(MerchantProduct merchantProduct);

	public MerchantProduct getMerchantProductById(MerchantProductId merchantProductId);

	public List<MerchantProduct> getMerchantProducts(String merchantName, String productName);

	public List<MerchantProduct> getMerchantProductsById(Long merchantId, Long productId);
}
