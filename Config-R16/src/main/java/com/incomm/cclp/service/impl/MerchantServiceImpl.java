/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.MerchantDAO;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.domain.Merchant;
import com.incomm.cclp.domain.MerchantProduct;
import com.incomm.cclp.domain.Product;
import com.incomm.cclp.dto.MerchantDTO;
import com.incomm.cclp.dto.MerchantProductDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.MerchantService;

@Service
public class MerchantServiceImpl implements MerchantService {

	@Autowired
	MerchantDAO merchantDao;
	
	@Autowired
	ProductDAO productDao;

	private final Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * Create an merchant.
	 * 
	 * @param merchantDto
	 *            The MerchantDTO to be created.
	 * @throws ServiceException
	 */
	@Override
	@Transactional
	public void createMerchant(MerchantDTO merchantDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info(merchantDto.toString());

		List<MerchantDTO> merchants = getMerchantsByName(merchantDto.getMerchantName());
		List<MerchantDTO> existingMerchants = merchants.stream()
				.filter(merchant -> merchant.getMerchantName().equalsIgnoreCase(merchantDto.getMerchantName()))
				.collect(Collectors.toList());
		if (existingMerchants != null && !existingMerchants.isEmpty()) {
			logger.debug("Merchant record already exists with same name: {}", merchantDto.getMerchantName());
			throw new ServiceException("MER_" + ResponseMessages.ALREADY_EXISTS, ResponseMessages.ALREADY_EXISTS);
		}

		ModelMapper mm = new ModelMapper();
		Merchant merchant = mm.map(merchantDto, Merchant.class);
		merchantDao.createMerchant(merchant);

		logger.info("Record created for :" + merchant.getMerchantId());
		logger.info(CCLPConstants.EXIT);

		merchantDto.setMerchantId(merchant.getMerchantId());
	}

	/**
	 * Gets all Merchants.
	 * 
	 * @return the list of all Merchants.
	 */
	@Override
	public List<MerchantDTO> getAllMerchants() {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside getAllMerchants");

		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<MerchantDTO>>() {
		}.getType();

		logger.info(CCLPConstants.EXIT);
		return mm.map(merchantDao.getAllMerchants(), targetListType);
	}

	/**
	 * Gets an merchant by name.
	 * 
	 * @param merchantName
	 *            The issuer name for the Issuer to be retrieved. The issuerName
	 *            parameter can be a partial or complete name.
	 * 
	 * @return the list of all IssuerDTOs for the given name.
	 */
	@Override
	public List<MerchantDTO> getMerchantsByName(String merchantName) {
		logger.info(CCLPConstants.ENTER);
		logger.debug("inside getMerchantsByName with data : " + merchantName);

		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<MerchantDTO>>() {
		}.getType();

		logger.debug("after retrieving data for MerchantName : " + merchantName);
		logger.info(CCLPConstants.EXIT);

		return mm.map(merchantDao.getMerchantsByName(merchantName), targetListType);
	}

	/**
	 * Updates an merchant.
	 * 
	 * @param merchantDto
	 *            The IssuerDTO to be updated.
	 */
	public void updateMerchant(MerchantDTO merchantDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.debug("inside updateMerchant with data :" + merchantDto.toString());

		Merchant existingmerchant = merchantDao.getMerchantById(merchantDto.getMerchantId());
		if (Objects.isNull(existingmerchant)) {
			logger.debug("Record does not exist for merchantId: {}", merchantDto.getMerchantId());
			throw new ServiceException(("MER_" + ResponseMessages.DOESNOT_EXISTS), ResponseMessages.DOESNOT_EXISTS);
		}

		String flag = "true";
		MerchantDTO merchantloc = null;
		List<MerchantDTO> existingMerchants = getMerchantsByName(merchantDto.getMerchantName());
		if (existingMerchants != null && !existingMerchants.isEmpty()) {
			Iterator<MerchantDTO> iterator = existingMerchants.iterator();
			while (iterator.hasNext()) {
				merchantloc = iterator.next();
				if (merchantloc.getMerchantName().equalsIgnoreCase(merchantDto.getMerchantName())
						&& (merchantloc.getMerchantId() != merchantDto.getMerchantId())) {
					
					throw new ServiceException("MER_" + ResponseMessages.ALREADY_EXISTS);
				}

			}

		}

		if (flag.equalsIgnoreCase("true")) {
			ModelMapper mm = new ModelMapper();
			Merchant merchant = mm.map(merchantDto, Merchant.class);
			merchantDao.updateMerchant(merchant);
			logger.info("after updating data for :" + merchant.getMerchantId());
			
		}
		logger.info(CCLPConstants.EXIT);
	}


	/**
	 * Get a Merchant by ID.
	 * 
	 * @param merchantId
	 *            The Merchant id for the Merchant to be retrieved.
	 * 
	 * @return the MerchantDTO.
	 */
	@Override
	public MerchantDTO getMerchantById(Long merchantId) {
		logger.info(CCLPConstants.ENTER);

		Merchant merchant = merchantDao.getMerchantById(merchantId);
		if (Objects.isNull(merchant)) {
			logger.error("Error while fetching record for merchantId: {}", merchantId);
			logger.info(CCLPConstants.EXIT);
			return null;
		}

		ModelMapper mm = new ModelMapper();
		logger.info(CCLPConstants.EXIT);
		return mm.map(merchant, MerchantDTO.class);
	}

	@Override
	public void deleteMerchantById(Long merchantId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.debug("inside deleteMerchantById with data : " , String.valueOf(merchantId));

		Merchant merchant = merchantDao.getMerchantById(merchantId);
		if (Objects.isNull(merchant)) {
			logger.debug("Merchant record not exists with merchantId : {}", merchantId);
			throw new ServiceException(("MER_" + ResponseMessages.DOESNOT_EXISTS), ResponseMessages.DOESNOT_EXISTS);
		}

		try {
			merchantDao.deleteMerchant(merchant);
		} catch (Exception e) {
			logger.error("Error occured while deleting merchant record", e.getMessage());

			throw new ServiceException(ResponseMessages.FAIL_MERCHANT_DELETE, ResponseMessages.FAIL_MERCHANT_DELETE);
		}

		logger.info("Record deleted successfully");

		logger.info(CCLPConstants.EXIT);
	}
	

	@Override
	public void assignProductToMerchant(MerchantProductDTO merchantProductDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info(merchantProductDto.toString());

		Merchant merchant = merchantDao.getMerchantById(merchantProductDto.getMerchantId());

		if (Objects.isNull(merchant)) {
			logger.debug("Merchant record with id '{}' does not exist", merchantProductDto.getMerchantId());

			throw new ServiceException(("MER_" + ResponseMessages.DOESNOT_EXISTS), ResponseMessages.DOESNOT_EXISTS);
		}

		Product product = productDao.getProductById(merchantProductDto.getProductId());
		if (Objects.isNull(product)) {
			logger.debug("Product record with id '{}' does not exist", merchantProductDto.getProductId());

			throw new ServiceException(("PRODUCT_" + ResponseMessages.DOESNOT_EXISTS), ResponseMessages.DOESNOT_EXISTS);
		}

		MerchantProduct merchantProduct = new MerchantProduct();
		merchantProduct.setMerchant(merchant);
		merchantProduct.setProduct(product);
		merchantProduct.setInsDate(merchantProductDto.getInsDate());
		merchantProduct.setLastUpdDate(merchantProductDto.getLastUpdDate());
		merchantProduct.setInsUser(merchantProductDto.getInsUser());
		merchantProduct.setLastUpdUser(merchantProductDto.getLastUpdUser());
		
		MerchantProduct existingMerchantProduct = merchantDao.getMerchantProductById(merchantProduct.getPrimaryKey());
		if (!Objects.isNull(existingMerchantProduct)) {
			logger.debug("MerchantProduct Mapping record does not exist");
			throw new ServiceException(ResponseMessages.MERCHANT_ALREADY_EXISTT, ResponseMessages.ALREADY_EXISTS);
		}

		merchantDao.assignProductToMerchant(merchantProduct);

		logger.info("Merchant '{}' mapped with Product '{}' successfully", merchant.getMerchantName(),
				product.getProductName());

		merchantProductDto.setMerchantName(merchant.getMerchantName());
		merchantProductDto.setProductName(product.getProductName());
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public void removeMerchantProductMapping(MerchantProductDTO merchantProductDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info(merchantProductDto.toString());

		Merchant merchant = new Merchant();
		merchant.setMerchantId(merchantProductDto.getMerchantId());
		Product product = new Product();
		product.setProductId(merchantProductDto.getProductId());
		MerchantProduct merchantProduct = new MerchantProduct();
		merchantProduct.setMerchant(merchant);
		merchantProduct.setProduct(product);

		MerchantProduct existingMerchantProduct = merchantDao.getMerchantProductById(merchantProduct.getPrimaryKey());

		if (Objects.isNull(existingMerchantProduct)) {
			logger.debug("MerchantProduct Mapping record does not exist");
			throw new ServiceException(ResponseMessages.MERCHANT_PRODUCT_NOTEXIST, ResponseMessages.DOESNOT_EXISTS);
		}

		try {
			merchantDao.removeMerchantProductMapping(existingMerchantProduct);
		} catch (Exception e) {
			logger.error("Error while removing Merchant Product mapping recored : {}", e.getMessage());

			throw new ServiceException(ResponseMessages.FAIL_MERCHANT_PRODUCT_DELETE,
					ResponseMessages.FAIL_MERCHANT_PRODUCT_DELETE);

		}

		logger.info("Merchant '{}' mapped with Product '{}' successfully", merchant.getMerchantName(),
				product.getProductName());

		merchantProductDto.setMerchantName(existingMerchantProduct.getMerchant().getMerchantName());
		merchantProductDto.setProductName(existingMerchantProduct.getProduct().getProductName());
		logger.info(CCLPConstants.EXIT);
	}
	
	@Override
	public List<MerchantProductDTO> getMerchantProducts(String merchantName, String productName)
			throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
	
		List<MerchantProductDTO> merchantProductDtos = new ArrayList<>();
		try {
		List<MerchantProduct> merchantProducts = merchantDao.getMerchantProducts(merchantName, productName);
		logger.info("merchant and product name",merchantName, productName);
		
		if (!CollectionUtils.isEmpty(merchantProducts)) {
			merchantProducts.stream().forEach(merchatProduct -> {
				MerchantProductDTO merchantProductDto = new MerchantProductDTO();
				merchantProductDto.setMerchantId(merchatProduct.getMerchant().getMerchantId());
				merchantProductDto.setMerchantName(merchatProduct.getMerchant().getMerchantName());
				merchantProductDto.setProductId(merchatProduct.getProduct().getProductId());
				merchantProductDto.setProductName(merchatProduct.getProduct().getProductName());
				
				merchantProductDtos.add(merchantProductDto);
			});
		}
		}
		catch (Exception e) {
			logger.error("Error while removing Merchant Product  in get Merchant product search mapping recored : {}", e);
			throw new ServiceException(ResponseMessages.MERCHANT_PRODUCT_NOTEXIST, ResponseMessages.DOESNOT_EXISTS);
		
		}
		logger.info(CCLPConstants.EXIT);
		return merchantProductDtos;
	}
		
	
	@Override
	public List<MerchantProductDTO> getMerchantProductsById(Long merchantId, Long productId)
			throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		
		List<MerchantProductDTO> merchantProductDtos = new ArrayList<>();
		List<MerchantProduct> merchantProducts = merchantDao.getMerchantProductsById(merchantId, productId);		
		
		if (!CollectionUtils.isEmpty(merchantProducts)) {
			merchantProducts.stream().forEach(merchatProduct -> {
				MerchantProductDTO merchantProductDto = new MerchantProductDTO();
				merchantProductDto.setMerchantId(merchatProduct.getMerchant().getMerchantId());
				merchantProductDto.setMerchantName(merchatProduct.getMerchant().getMerchantName());
				merchantProductDto.setProductId(merchatProduct.getProduct().getProductId());
				merchantProductDto.setProductName(merchatProduct.getProduct().getProductName());
				
				merchantProductDtos.add(merchantProductDto);
			});
		}
		
		logger.info(CCLPConstants.EXIT);
		
		return merchantProductDtos;
	}
	
}
