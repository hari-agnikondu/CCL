package com.incomm.cclpvms.config.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.incomm.cclpvms.config.model.Alert;
import com.incomm.cclpvms.config.model.CurrencyCodeDTO;
import com.incomm.cclpvms.config.model.DeliveryChannel;
import com.incomm.cclpvms.config.model.Product;
import com.incomm.cclpvms.config.model.ProductDTO;
import com.incomm.cclpvms.config.model.PurseDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ProductException;
import com.incomm.cclpvms.exception.ServiceException;

public interface ProductService {
	public ResponseDTO createProduct(ProductDTO productdto) throws ServiceException;

	public List<ProductDTO> getAllProducts() throws ServiceException, ProductException;

	public ResponseDTO updateProduct(ProductDTO productDTO) throws ServiceException;

	public ResponseDTO deleteProduct(Long productId) throws ServiceException;

	public Product getProductById(Long productId) throws ServiceException;

	public List<ProductDTO> getProductsByName(String productName) throws ServiceException, ProductException;

	public ResponseDTO getProductCount(Long productId) throws ServiceException;

	@SuppressWarnings("rawtypes")
	public Map<String, List> getCCFList() throws ServiceException, ProductException;

	public ResponseEntity<ResponseDTO> updateGeneralTab(Product product) throws ServiceException;

	public Product getGeneralTabByProductId(long productId) throws ServiceException;

	public Map<Long, String> getParentProducts() throws ServiceException;

	public Map<Long, String> getRuleSetMetaData() throws ServiceException;

	public ResponseDTO addLimits(Map<String, Object> limitAttributes, long productId,long purseId) throws ServiceException;

	public List<Object> getDeliveryChnlTxns() throws ServiceException;

	public Map<Object, String> getAllParentProducts() throws ServiceException;

	Map<Object, String> getAllRetailProducts() throws ServiceException;

	public Product getCVVTabByProductId(Long productId) throws ServiceException;

	public ResponseEntity<ResponseDTO> updateCVVTab(Map<String, Object> productAttributes, Long productId)
			throws ServiceException;

	public List<String> getCardStatus() throws ServiceException;

	public ResponseDTO getTxnFees(long productId,long parentProductId, long purseId) throws ServiceException;

	ResponseDTO addTxnFees(Map<String, Object> txnFeeAttributes, long productId, long purseId) throws ServiceException;

	public Product getTxnBasedOnCardStatus(Long productId) throws ServiceException;

	public ResponseEntity<ResponseDTO> saveTxnOnCardStatById(Map<String, Object> productAttributes, Long productId)
			throws ServiceException;

	public List<Alert> getMessages() throws ServiceException;

	public ResponseDTO addMaintenanceFee(Map<String, Object> monthlyFeeCapAttributes, long productId)
			throws ServiceException;

	public ResponseDTO getMaintenanceFee(long parseLong) throws ServiceException;

	public Product getPinTabByProductId(Long productId) throws ServiceException;

	public ResponseEntity<ResponseDTO> updatePinTab(Map<String, Object> generalAttributes, Long productId)
			throws ServiceException;

	public ResponseEntity<ResponseDTO> updateAlertsTab(Map<String, String> alertAttributes, Long productId)
			throws ServiceException;

	public ResponseDTO getAlertsTab(Long productId) throws ServiceException;

	public ResponseDTO getPanExpiryDetails(Long productId) throws ServiceException;

	public List<ProductDTO> getProductsByNameForCopy(String productName) throws ServiceException, ProductException;

	public Map<String, String> getCardStatusList() throws ServiceException;

	public Map<Long, String> getProductListwithSamePartner(Long partnerId, Long productId) throws ServiceException;

	Map<Double, String> generatePdfDocument() throws ServiceException;

	List<DeliveryChannel> getDelChnlTxns() throws ServiceException;

	String getSerialNumberResponse(long productId, String upc, long serialNumberQuantity) throws ServiceException;

	public List<Object> getDeliveryChnlTxnsByChnlCode(String channelcode) throws ServiceException;

	public List<Object> getDeliveryChnlTxnsByChnlCodeTxnName(String delchanl, String transactn) throws ServiceException;

	public ResponseDTO addAttributesByProgramID(Map<String, Object> productAttributes, Long programID)
			throws ServiceException;

	public Map<String, Map<String, String>> getAuthTypeMetaData() throws ServiceException;

	Map<String, Object> getPartnerCurrencies(long partnerId) throws ServiceException;

	public List<CurrencyCodeDTO> getCurrencyCodeList(Long partnerId) throws ServiceException;

	public ResponseEntity<ResponseDTO> updatePurse(Map<String, Object> productAttributes, Long productId,Long purseId) throws ServiceException;

	public Map<String, Object> getProductPurseAttributes(Long productId, Long purseId) throws ServiceException;

	ResponseDTO getMonthlyFeeCap(long productId, long parentProductId,long purseId) throws ServiceException;

	ResponseDTO addMonthlyFeeCap(Map<String, Object> monthlyFeeCapAttributes, long productId, long purseId)
			throws ServiceException;

	ResponseDTO getLimits(long productId,long parentProductId, long purseId) throws ServiceException;

	List<PurseDTO> getPurseByProductId(Long productId, Long parentProductId, String attributeGroup) throws ServiceException;


	

	

}
