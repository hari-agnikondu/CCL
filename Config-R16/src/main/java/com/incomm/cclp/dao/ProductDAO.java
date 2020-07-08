
package com.incomm.cclp.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.incomm.cclp.domain.Alert;
import com.incomm.cclp.domain.Product;
import com.incomm.cclp.domain.ProductPurse;
import com.incomm.cclp.domain.ProductRuleSet;
import com.incomm.cclp.domain.Purse;
import com.incomm.cclp.dto.ProductDTO;

public interface ProductDAO {

	public void createProduct(Product product);

	public List<Product> getAllProducts();

	public void updateProduct(Product product);

	public void deleteProduct(Product product);

	public Product getProductById(long productId);

	public List<Product> getProductsByName(String productName);

	public int countAndDeleteProductById(long product);

	public int countOfproductById(long product);

	@SuppressWarnings("rawtypes")
	public Map<String, List> getCCFlist();

	public List<Product> getParentProducts();

	public int updateProductAttributes(ProductDTO productDTO);

	public List<Object[]> getTransactionList();

	public List<Product> getAllParentProducts();

	public List<Product> getAllRetailProducts();

	public List<Product> getChildProductList(long productId);

	public List<Product> getParentProductList(long productId);

	public ProductRuleSet getRuleSetByProductId(long product);

	public void updateProductRuleSet(ProductRuleSet productRuleSet);

	public int deleteProductRuleSet(ProductRuleSet productRuleSet);

	public int updateAttributes(String productAttributes, Long productId);

	public Product getAttributes(Long productId);

	public List<Alert> getProductAlertMessages();

	public int updateProductAttributes(String attributes, Long productId);

	public List<Product> getProductsByNameForCopy(String productName);

	public List<Object[]> getProductsWithSamePartnerId(Long productId);

	public List<Object[]> getTransactionListByChannelName(String channelName);

	public List<Object[]> getTransactionListByChannelNameTxnName(String channelName, String transactionShortName);

	public List<Product> getProductsByProgramId(Long programId);

	public Object getProductIdByUPC(String upc);

	public int deleteProductRuleSetByProductId(Long productId);

	public List<Object[]> getSupportedPurse(Long productId);

	public List<Object[]> getprodAttributesUpdate(Long productId);

	int updateProdAttributesFlag(Long productId);

	List<BigDecimal> getDistinctProductId();

	public List<Object[]> getInternationalCurrencyByProductId(long productId);

	public List<Object[]> getPartnerCurrency(Long partnerId);

	public List<Object[]> getPartnerCurrencyCodes(Long partnerId);

	public ProductPurse getProductPurseById(long productId, long purseId);

	public List<Purse> getPurseByProductId(Long productId, Long parentProductId,String attributeGroup);

	ProductPurse getProductPurseAttributes(Long productId, Long purseId);

	int updateProductPurseAttributes(String productAttributes, Long productId, Long purseId);

	List<BigDecimal> getDistinctProductPurseId();
	
	public List<Object[]> getprodPurseAttributesUpdate(Long productId);
	
	int updateProdPurseAttributesFlag(Long productId,long purseId,String attributeGroup,String attributeKey);

}
