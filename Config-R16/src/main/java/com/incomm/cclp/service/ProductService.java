package com.incomm.cclp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.incomm.cclp.domain.Alert;
import com.incomm.cclp.domain.Purse;
import com.incomm.cclp.dto.CurrencyCodeDTO;
import com.incomm.cclp.dto.ProductDTO;
import com.incomm.cclp.exception.ServiceException;

public interface ProductService {
	
	public void createProduct(ProductDTO productDto) throws ServiceException;
	
	public List<ProductDTO> getAllProducts();
	
	public void updateProduct(ProductDTO productDto) throws ServiceException;
	
	public ProductDTO getProductById(long productDto) throws IOException;
	
	public List<ProductDTO> getProductsByName(String productName);
	
	public int countAndDeleteProductById(long productDto);
	
	public int countOfProductById(long productDto);

	@SuppressWarnings("rawtypes")
	public Map<String,List> getCCFlist();
	
	public Map<Long,String> getParentProducts();
	
	public Map<String,Object> getProductGeneral(Long productId) throws ServiceException ;
	public  Map<String, Map<String, Object>> getAllAttributesToCreateProduct();
	public Map<String, Map<String, Object>> getAllAttributesToCreateProductPurse();
	
	public void updateProductGeneral(Map<String,Object> productGeneralMap,Long productId) throws ServiceException;

	public Map<Object, Object> getAllParentProducts();
	
	public Map<Object, Object> getAllRetailProducts();

	public List<Object[]> getTransactionList();
	
	public void updateProductRuleSet(Map<String,Object> productMap,Long productId);
	
	public Map<String,Object> getProductCvv(Long productId)throws ServiceException;

	public void updateProductCvv(Map<String, Object> productCvvMap, Long productId)throws ServiceException;
	
	public Map<String,Object>  getCardStatusAttributes(Long productId) throws ServiceException;
	
	public void updateCardStatusAttributes(Map<String,Object> productMap,Long productId) throws ServiceException;
	
	int updateMaintenanceFeeAttributes(Map<String, Object> inputAttributes, Long productId, String attributesGroup);

	Map<String, Object> getProductFeeLimitsById(Long productId,Long parentProductId, Long purseId,String attributesGroup)
			throws IOException;
	
	Map<String, Object> getProductFeeLimitsById(Long productId, String attributesGroup)
			throws IOException;		
			
	public List<Alert> getProductAlertMessages();
	
	public void updateProductPinAttributes(Map<String, Object> productPinMap, Long productId)throws ServiceException;

	public Map<String, Object> getProductPin(Long productId)throws ServiceException;

	public Map<String, Object> getAlertAttributesByProductId(Long productId) throws ServiceException;

	void updateAlertAttributes(Map<String, Object> inAlertAttributes, Long productId) throws ServiceException;
	
	Map<String, Object> getProductAttributesByIdAndGroupname(Long productId, String attributesGroup) throws ServiceException;

	int updateProductAttributesByGroupName(Map<String, Object> inAttributes, Long productId, String groupName) throws ServiceException;

	public List<ProductDTO> getProductsByNameForCopy(String productName);

	public void updProductPurseAttributesCacheById(Long productId) throws ServiceException;

	public void updateAllProductAndPurseAttributesCache() throws ServiceException;

	public Map<Double, String> buildPdfMetaData();

	public Map<Long, String> getProductsWithSamePartnerId(Long partnerId,Long productId) throws ServiceException;

	public List<String> updateProductAttributesByProgramId(Map<String, Object> inputAttributes, Long programId) throws IOException;

	public List<Object[]> getTransactionListByChannelName(String channelName)throws ServiceException;

	List<Object[]> getTransactionListByChannelNameTransactionName(String channelName, String transactionShortName)
			throws ServiceException;

	public Long getProductIdByUPC(String upc) throws ServiceException;

	public List<String> getPackagesIdByProductId(Long productId) throws ServiceException;
	
	public Map<String, List<Object>> getSupportedPurse(Long productId);
	
	public String getOrUpdateProductAttributes(Long productId,Long purseId,String source) throws ServiceException;
	
	public void updateProductAttributesValueInCache() throws ServiceException;

	public Map<String, Object> getPartnerCurrency(Long partnerId);

	public List<CurrencyCodeDTO> getPartnerCurrencyCodes(Long partnerId) throws ServiceException;

	
	Map<String, List<Object>> getInternationalCurrency(Long productId, Map<String, List<Object>> productCurrencyMapMap);

	Map<String, List<Object>> getInternationalCurrency(Long productId);

	public List<Purse> getPurseByProductId(Long productId, Long parentProductId,String attributeGroup) throws ServiceException;

	public void updateProductPurse(Map<String, Object> productPurseMap, Long productId, Long purseId) throws ServiceException;

	public Map<String, Object> getProductPurse(Long productId, Long purseId,String source) throws ServiceException, IOException;

	int updatePurseFeeLimitAttributes(Map<String, Object> inputAttributes, Long productId, Long purseId,
			String attributesGroup);
	
	public void updatePurseAttributesValueInCache() throws ServiceException;

	void removeNullFromAttributes() throws ServiceException;

	void updateBumpUpConfig() throws ServiceException;
	
	
}

