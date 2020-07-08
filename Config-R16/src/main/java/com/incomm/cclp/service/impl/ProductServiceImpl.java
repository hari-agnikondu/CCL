/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.AlertDAO;
import com.incomm.cclp.dao.CardRangeDAO;
import com.incomm.cclp.dao.IssuerDAO;
import com.incomm.cclp.dao.PackageDAO;
import com.incomm.cclp.dao.PartnerDAO;
import com.incomm.cclp.dao.ProductAlertDAO;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.dao.ProgramIDDAO;
import com.incomm.cclp.dao.PurseDAO;
import com.incomm.cclp.domain.Alert;
import com.incomm.cclp.domain.CardRange;
import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.PackageDefinition;
import com.incomm.cclp.domain.Product;
import com.incomm.cclp.domain.ProductAlert;
import com.incomm.cclp.domain.ProductCardRange;
import com.incomm.cclp.domain.ProductCurrency;
import com.incomm.cclp.domain.ProductPackage;
import com.incomm.cclp.domain.ProductPurse;
import com.incomm.cclp.domain.ProductRuleSet;
import com.incomm.cclp.domain.ProductRuleSetID;
import com.incomm.cclp.domain.Purse;
import com.incomm.cclp.dto.CurrencyCodeDTO;
import com.incomm.cclp.dto.ProductDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.AttributeDefinitionService;
import com.incomm.cclp.service.CacheService;
import com.incomm.cclp.service.GroupAccessService;
import com.incomm.cclp.service.ProductService;
import com.incomm.cclp.service.RedemptionDelayService;
import com.incomm.cclp.util.Util;


@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

	// the Product dao.
	@Autowired
	ProductDAO productDao;

	@Autowired
	PurseDAO purseDao;

	@Autowired
	CardRangeDAO cardRangeDao;

	@Autowired
	PackageDAO packageDao;

	@Autowired
	IssuerDAO issuerDao;

	@Autowired
	PartnerDAO partnerDao;

	@Autowired
	ProgramIDDAO programDao;

	@Autowired
	AlertDAO alertDao;

	@Autowired
	ProductAlertDAO productAlertDao;

	@Autowired
	LocalCacheServiceImpl localCacheServiceImpl;

	@Autowired
	AttributeDefinitionService attributeDefinitionService;

	@Autowired
	DistributedCacheServiceImpl distributedCacheService;

	@Autowired
	GroupAccessService groupAccessService;

	@Autowired
	RedemptionDelayService redemptionDelayService;

	@Autowired
	CacheService cacheService;
	
	@Override
	@Transactional
	public void createProduct(ProductDTO productDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Product parentProduct = null;

		List<ProductDTO> products = getProductsByName(productDto.getProductName());

		List<ProductDTO> existingproducts = products.stream()
				.filter(product -> product.getProductName().equalsIgnoreCase(productDto.getProductName()))
				.collect(Collectors.toList());

		if (existingproducts != null && !existingproducts.isEmpty()) {
			logger.error("Product already exits");
			throw new ServiceException(ResponseMessages.ERR_PRODUCT_EXISTS);
		}

		Product product = constructProduct(productDto);

		Map<String, Map<String, Object>> attributesMap2 = productDto.getAttributesMap();
		attributesMap2.get(CCLPConstants.PRODUCT_TAB_MAINTENANCE_FEES);

		// Set attributes
		Map<String, Map<String, Object>> attributesMapTmp = getAttributesToCreateProduct(productDto.getAttributesMap());
		Map<String, Map<String, Object>> attributesMap = new HashMap<>();
		attributesMap.putAll(attributesMapTmp);

		// Adding Parent Product attributes to new product by Hari (Copy Products)
		if (productDto.getParentProductId() != null && productDto.getParentProductId() >= 0) {
			parentProduct = productDao.getProductById(productDto.getParentProductId());
			List<ProductPurse> parentProductPurse = parentProduct.getListProductPurse();
			List<ProductPurse> productPurseList = product.getListProductPurse();
			if(!CollectionUtils.isEmpty(parentProductPurse) && !CollectionUtils.isEmpty(productPurseList)) {
				productPurseList.stream().forEach(pp -> {
					pp.setAttributes(parentProductPurse.stream().filter(p -> p.getPurse().getPurseId() == pp.getPurse().getPurseId()).findAny().orElse(pp).getAttributes());
				});
			}
			product.setListProductPurse(productPurseList);
			String parentAttributesString = parentProduct.getAttributes();
			Map<String, Map<String, Object>> parentAttributesMap = Util.convertJsonToHashMap(parentAttributesString);
			attributesMap.put(CCLPConstants.PRODUCT_TAB_GENERAL, parentAttributesMap.get(CCLPConstants.PRODUCT_TAB_GENERAL));
			attributesMap.put(CCLPConstants.PRODUCT_TAB_PIN, parentAttributesMap.get(CCLPConstants.PRODUCT_TAB_PIN));
			attributesMap.put(CCLPConstants.PRODUCT_TAB_CVV, parentAttributesMap.get(CCLPConstants.PRODUCT_TAB_CVV));
			attributesMap.put(CCLPConstants.ALERTS, parentAttributesMap.get(CCLPConstants.ALERTS));
			attributesMap.put(CCLPConstants.PRODUCT_TAB_CARD_STATUS, parentAttributesMap.get(CCLPConstants.PRODUCT_TAB_CARD_STATUS));
			attributesMap.put(CCLPConstants.PRODUCT_TAB_MAINTENANCE_FEES, parentAttributesMap.get(CCLPConstants.PRODUCT_TAB_MAINTENANCE_FEES));

		}
		attributesMap = Util.removeEmptyValuefromMapOfMap(attributesMap);
		product.setAttributes(Util.convertHashMapToJson(attributesMap));
		product.setParentProductId(null);

		productDao.createProduct(product);

		if (!attributesMap.isEmpty()) {
			addIssuerPartnerToCache(product, attributesMap);
		}
		/**
		 * Adding product attributes to cache with product id as key
		 */
		
		distributedCacheService.getOrAddProductAttributesCache(product.getProductId(), attributesMap);
		cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(product.getProductId()));
		updatePurseAttributesBulk(product.getProductId(),product);
		
		logger.info("Record created for :" + product.getProductId());

		productDto.setProductId(product.getProductId());
		logger.info(CCLPConstants.EXIT);
	}

	private Product constructProduct(ProductDTO productDto) {
		logger.info(CCLPConstants.ENTER);
		Product product = new Product();
		product.setProductId(productDto.getProductId());
		product.setProductName(productDto.getProductName());
		product.setDescription(productDto.getDescription());
		product.setIsActive(productDto.getIsActive());
		product.setParentProductId(productDto.getParentProductId());
		product.setInsUser(productDto.getInsUser());
		product.setInsDate(productDto.getInsDate());
		product.setLastUpdUser(productDto.getLastUpdUser());
		product.setLastUpdDate(productDto.getLastUpdDate());
		product.setProductShortName(productDto.getProductShortName());
		Map<String, Map<String, Object>> attributeMap = productDto.getAttributesMap() ;
		Map<String,Object> productMap = attributeMap.get("Product");
		
		String internationalSupport = (String) productMap.get("internationalSupported");
		logger.debug("internationalSupport: {}",internationalSupport);
		// Set issuer
		product.setIssuer(issuerDao.getIssuerById(productDto.getIssuerId()));

		// Set partner
		product.setPartner(partnerDao.getPartnerById(productDto.getPartnerId()));
		product.setProgramID(programDao.getProgramByID(productDto.getProgramId()));

		// Add package definitions
		List<String> packageDefinitions = productDto.getPackageIds();
		if (!CollectionUtils.isEmpty(packageDefinitions)) {
			List<ProductPackage> listOfProductPackage = getProductPackageList(product, packageDefinitions);
			product.setListOfProductPackage(listOfProductPackage);
		}

		// Add product card range
		List<String> cardRanges = productDto.getCardRanges();
		if (!CollectionUtils.isEmpty(cardRanges)) {
			List<ProductCardRange> listProductCardRange = getProductCardRangeList(product, cardRanges);
			product.setListProductCardRange(listProductCardRange);
		}

		// set product purse
		List<String> purses = productDto.getSupportedPurse();
		List<ProductPurse> productPurses = productDto.getListProductPurse();
		if (!CollectionUtils.isEmpty(purses)) {
			String defaultPurseType = getDefaultPurseTypeFromProductAttributes(productDto.getAttributesMap());
			List<ProductPurse> listProductPurse = getProductPurseList(product, purses, defaultPurseType,productPurses);
			product.setListProductPurse(listProductPurse);
		}

		// set product Currency
		if ("Enable".equalsIgnoreCase(internationalSupport)) {
			List<String> partnerCurrency = productDto.getPartnerCurrency();
			if (!CollectionUtils.isEmpty(partnerCurrency)) {
				List<ProductCurrency> listProductCurrency = getPartnerCurrencyList(product, partnerCurrency);
				product.setListProductCurrency(listProductCurrency);
			}
		}else {
			product.setListProductCurrency(new ArrayList<ProductCurrency>());
		}
		logger.info(CCLPConstants.EXIT);
		return product;
	}

	private List<ProductCurrency> getPartnerCurrencyList(Product product, List<String> partnerCurrency) {
		logger.info(CCLPConstants.ENTER);
		List<ProductCurrency> listOfProductCurrency = new ArrayList<>(0);

		partnerCurrency.stream().forEach(partnerCurrCode -> {
			CurrencyCode currencyCode = purseDao.getCurrencyByID(partnerCurrCode);

				ProductCurrency productCurrency = new ProductCurrency();
				productCurrency.setProduct(product);
				productCurrency.setCurrencyCode(currencyCode);
				productCurrency.setInsUser(product.getInsUser());
				productCurrency.setInsDate(product.getInsDate());
				productCurrency.setLastUpdUser(product.getLastUpdUser());
				productCurrency.setLastUpdDate(product.getLastUpdDate());
				
				listOfProductCurrency.add(productCurrency);
		});
		logger.info(CCLPConstants.EXIT);
		return listOfProductCurrency;
	}

	private String getDefaultPurseTypeFromProductAttributes(Map<String, Map<String, Object>> productAttributes) {
		logger.info(CCLPConstants.ENTER);
		String defaultPurseType = "";
		if (!CollectionUtils.isEmpty(productAttributes) && productAttributes.containsKey(CCLPConstants.PRODUCT)) {
			Map<String, Object> productAttribute = productAttributes.get(CCLPConstants.PRODUCT);
			if (productAttribute.containsKey("defaultPurse")) {
				Object purseType = productAttribute.get("defaultPurse");
				if (!Objects.isNull(purseType)) {
					defaultPurseType = purseType.toString();
					logger.debug("defaultPurseType: {}",defaultPurseType);
				}
			}
		}
		logger.info(CCLPConstants.EXIT);
		return defaultPurseType;
	}

	private Map<String, Map<String, Object>> getAttributesToCreateProduct(
			Map<String, Map<String, Object>> productAttributesMap) {
		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String, Object>> defaultAttributes = null;
		defaultAttributes = localCacheServiceImpl.getAttributeDefinition(null);
		Map<String, Map<String, Object>> defaultAttributesTemp = new HashMap<>();
		logger.debug("LOCAL Cache Attribute value : " + defaultAttributes.toString());

		// add to local cache if cache empty
		if (CollectionUtils.isEmpty(defaultAttributes)) {
			logger.info("LOCAL Cache is empty : " + defaultAttributes);

			defaultAttributes = attributeDefinitionService.getAllAttributeDefinitions();

			localCacheServiceImpl.getAttributeDefinition(defaultAttributes);
		}

		if (!CollectionUtils.isEmpty(defaultAttributes)) {
			logger.info("Copying attribute to default attribute map");
			defaultAttributesTemp.putAll(defaultAttributes);
			defaultAttributesTemp.remove(CCLPConstants.PRODUCT_TAB_LIMITS);
			defaultAttributesTemp.remove(CCLPConstants.PRODUCT_TAB_PURSE);
			defaultAttributesTemp.remove(CCLPConstants.PRODUCT_TAB_TRANSACTION_FEES);
			defaultAttributesTemp.remove(CCLPConstants.PRODUCT_TAB_MONTHLY_FEE_CAP);
			// Copy Product attributes to default attribute map
			Map<String, Object> productAttribute = null;
			if (!CollectionUtils.isEmpty(productAttributesMap)
					&& productAttributesMap.containsKey(CCLPConstants.PRODUCT)) {
				productAttribute = productAttributesMap.get(CCLPConstants.PRODUCT);
				updateProductAttributes(defaultAttributesTemp, productAttribute, CCLPConstants.PRODUCT);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new HashMap<>(defaultAttributesTemp);
	}
	@Override
	public  Map<String, Map<String, Object>> getAllAttributesToCreateProduct() {
		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String, Object>> defaultAttributes = null;
		Map<String, Map<String, Object>> defaultAttributesTemp = new HashMap<>();
		defaultAttributes = localCacheServiceImpl.getAttributeDefinition(null);

		// add to local cache if cache empty
		if (CollectionUtils.isEmpty(defaultAttributes)) {
			logger.info("LOCAL Cache is empty : " + defaultAttributes);

			defaultAttributes = attributeDefinitionService.getAllAttributeDefinitions();

			localCacheServiceImpl.getAttributeDefinition(defaultAttributes);
		}
		defaultAttributesTemp.putAll(defaultAttributes);
		defaultAttributesTemp.remove(CCLPConstants.PRODUCT_TAB_LIMITS);
		defaultAttributesTemp.remove(CCLPConstants.PRODUCT_TAB_PURSE);
		defaultAttributesTemp.remove(CCLPConstants.PRODUCT_TAB_TRANSACTION_FEES);
		defaultAttributesTemp.remove(CCLPConstants.PRODUCT_TAB_MONTHLY_FEE_CAP);
		logger.info(CCLPConstants.EXIT);
		return new HashMap<>(defaultAttributesTemp);
	}
	
	@Override
	public Map<String, Map<String, Object>> getAllAttributesToCreateProductPurse() {
		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String, Object>> defaultAttributes = null;
		Map<String, Map<String, Object>> defaultAttributesTemp = new HashMap<>();
		defaultAttributes = localCacheServiceImpl.getAttributeDefinition(null);

		logger.debug("LOCAL Cache Attribute value : " + defaultAttributes);

		// add to local cache if cache empty
		if (CollectionUtils.isEmpty(defaultAttributes)) {
			logger.info("LOCAL Cache is empty : " + defaultAttributes);

			defaultAttributes = attributeDefinitionService.getAllAttributeDefinitions();
			
			localCacheServiceImpl.getAttributeDefinition(defaultAttributes);
		}
		
		defaultAttributesTemp.putAll(defaultAttributes);
		
		Map<String, Map<String, Object>> attributesMap = new HashMap<>();
		attributesMap.put(CCLPConstants.PRODUCT_TAB_PURSE, new HashMap<>(defaultAttributesTemp.get(CCLPConstants.PRODUCT_TAB_PURSE)));
		attributesMap.put(CCLPConstants.PRODUCT_TAB_LIMITS,new HashMap<>(defaultAttributesTemp.get(CCLPConstants.PRODUCT_TAB_LIMITS)));
		attributesMap.put(CCLPConstants.PRODUCT_TAB_TRANSACTION_FEES, new HashMap<>(defaultAttributesTemp.get(CCLPConstants.PRODUCT_TAB_TRANSACTION_FEES)));
		attributesMap.put(CCLPConstants.PRODUCT_TAB_MONTHLY_FEE_CAP, new HashMap<>(defaultAttributesTemp.get(CCLPConstants.PRODUCT_TAB_MONTHLY_FEE_CAP)));

		
		logger.info(CCLPConstants.EXIT);
		return attributesMap;
	}
	private void updateProductAttributes(Map<String, Map<String, Object>> constructAttributes,
			Map<String, Object> productAttributes, String attributeGroupName) {
		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String,Object>> defaultAttributes = getAllAttributesToCreateProduct();
		
		if (defaultAttributes.containsKey(attributeGroupName)) {
			
			 Map<String, Object> updatedProductAttributes = Util.updateValuesToMap(defaultAttributes.get(attributeGroupName), productAttributes);
			 updatedProductAttributes =  Util.removeEmptyValuefromMap(updatedProductAttributes);
			 constructAttributes.put(attributeGroupName, updatedProductAttributes);
		} else {
			logger.info("Adding product attributes");
			constructAttributes.put(attributeGroupName, productAttributes);
		}
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public List<ProductDTO> getAllProducts() {
		return productDTOs(productDao.getAllProducts());

	}

	public List<ProductDTO> productDTOs(List<Product> productList) {
		logger.info(CCLPConstants.ENTER);
		logger.debug("productList: {}",productList.toString());
		List<ProductDTO> productDtoList = new ArrayList<>();
		if (productList != null) {
			productDtoList = productList.stream()
					.map(product -> new ProductDTO(product.getProductId(), product.getProductName(),
							product.getDescription(),
							Util.convertJsonToHashMap(product.getAttributes()),
							product.getIsActive(),
							product.getParentProduct() != null ? product.getParentProduct().getProductId() : null,
							product.getParentProduct() != null ? product.getParentProduct().getProductName() : null,
							product.getIssuer() != null ? product.getIssuer().getIssuerId() : null,
							product.getIssuer() != null ? product.getIssuer().getIssuerName() : null,
							product.getPartner() != null ? product.getPartner().getPartnerId() : null,
							product.getPartner() != null ? product.getPartner().getPartnerName() : null,
							product.getInsUser(), product.getInsDate(), product.getLastUpdUser(),
							product.getLastUpdDate(), product.getProductShortName(),
							product.getProgramID() != null ? product.getProgramID().getPrgmID() : null))
					.collect(Collectors.toList());
		}
		logger.info(CCLPConstants.EXIT);
		return productDtoList;
	}

	@Override
	public void updateProduct(ProductDTO productDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		List<ProductDTO> products = getProductsByName(productDto.getProductName());

		Long noExistingProducts = products.stream()
				.filter(product -> product.getProductName().equalsIgnoreCase(productDto.getProductName())
						&& (!productDto.getProductId().equals(product.getProductId())))
				.count();

		if (noExistingProducts > 0) {
			logger.info("Product Already exists");
			throw new ServiceException(CCLPConstants.PRODUCT_U + ResponseMessages.ALREADY_EXISTS);
		}

		Product existingproduct = productDao.getProductById(productDto.getProductId());

		if (Objects.isNull(existingproduct)) {
			logger.info("Product does not exists");
			throw new ServiceException(CCLPConstants.PRODUCT_U + ResponseMessages.DOESNOT_EXISTS);
		}
		productDto.setListProductPurse(existingproduct.getListProductPurse());

		try {
			Product product = constructProduct(productDto);

			// Set attributes
			Map<String, Map<String, Object>> attributesMap = getAttributesToUpdateProduct(existingproduct,
					productDto.getAttributesMap());
			product.setAttributes(Util.convertHashMapToJson(attributesMap));

			if (!CollectionUtils.isEmpty(existingproduct.getListProductAlert())) {
				product.setListProductAlert(existingproduct.getListProductAlert());
			} else {
				product.setListProductAlert(new ArrayList<ProductAlert>());
			}

			productDao.updateProduct(product);

			/*
			 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
			 * existing product attributes on productId starts venkateshgaddam 29032018
			 */
			addNewAttribToExistingProdAttrib(product, product.getProductId(), attributesMap);
			/*
			 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
			 * existing product attributes on productId ends venkateshgaddam 29032018
			 */
			/**
			 * Update product attributes to cache with product id as key
			 */

			distributedCacheService.updateProductAttributesCache(product.getProductId(), attributesMap);
			cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(product.getProductId()));
			updatePurseAttributesBulk(product.getProductId(),product);
			
		} catch (Exception e) {
			logger.info("Error occured while updating product" + e);
			throw new ServiceException(ResponseMessages.PRODUCT_UPDATE_FAIL);
		}

		logger.info(CCLPConstants.EXIT);
	}

	private Map<String, Map<String, Object>> getAttributesToUpdateProduct(Product existingProduct,
			Map<String, Map<String, Object>> productAttributesMap) {
		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String, Object>> existingAttributesmap = null;
		String existingAttributesJson = existingProduct.getAttributes();
		if (!Objects.isNull(existingAttributesJson)) {
			existingAttributesmap = Util.convertJsonToHashMap(existingAttributesJson);

			if (!CollectionUtils.isEmpty(existingAttributesmap)) {
				Map<String, Object> productAttribute = null;
				if (productAttributesMap.containsKey(CCLPConstants.PRODUCT)) {
					productAttribute = productAttributesMap.get(CCLPConstants.PRODUCT);
					updateProductAttributes(existingAttributesmap, productAttribute, CCLPConstants.PRODUCT);
				}
			}
		} else {
			existingAttributesmap = productAttributesMap;
		}

		logger.info("Final attribute Map :" + existingAttributesmap.toString());
		logger.info(CCLPConstants.EXIT);
		return existingAttributesmap;
	}

	private List<ProductPurse> getProductPurseList(Product product, List<String> purses, String defaultPurseType,List<ProductPurse> productPurses) {
		logger.info(CCLPConstants.ENTER);
		List<ProductPurse> listOfProductPurse = new ArrayList<>(0);
		int purseOrder = 0;
	
		Map<String, ProductPurse> productPurseMap = new HashMap<>();
		if(!CollectionUtils.isEmpty(productPurses)) {
		for(ProductPurse pp: productPurses) {
			productPurseMap.put(String.valueOf(pp.getPurse().getPurseId()), pp);
		}
		}
		for(String id:purses) {
			if(!CollectionUtils.isEmpty(productPurses) && productPurseMap.containsKey(id)) {
				ProductPurse productPurse = productPurseMap.get(id);
				productPurse.setPurseOrder(++purseOrder);
				
				if (!Objects.isNull(defaultPurseType) && defaultPurseType.equals(id)) {
					productPurse.setIsDefault("Y");
				} else {

					productPurse.setIsDefault("N");
				}
				listOfProductPurse.add(productPurse);
			}else {
				Purse purse = purseDao.getPurseById(Long.parseLong(id));
				if (!Objects.isNull(purse)) {
					ProductPurse productPurse = new ProductPurse();
					productPurse.setProduct(product);
					productPurse.setInsUser(product.getInsUser());
					productPurse.setInsDate(product.getInsDate());
					productPurse.setLastUpdUser(product.getLastUpdUser());
					productPurse.setLastUpdDate(product.getLastUpdDate());
					productPurse.setPurse(purse);
					productPurse.setPurseOrder(++purseOrder);
					productPurse.setAttributes(Util.convertHashMapToJson(Util.removeEmptyValuefromMapOfMap(getAllAttributesToCreateProductPurse())));
					if (!Objects.isNull(defaultPurseType) && defaultPurseType.equals(id)) {
						productPurse.setIsDefault("Y");
					} else {

						productPurse.setIsDefault("N");
					}
					listOfProductPurse.add(productPurse);
				}
			}
		}
		logger.info(CCLPConstants.EXIT);
		return listOfProductPurse;
	}

	private List<ProductCardRange> getProductCardRangeList(Product product, List<String> cardRanges) {
		logger.info(CCLPConstants.ENTER);
		List<ProductCardRange> listOfProductCardRange = new ArrayList<>(0);
		Long cardRangeOrder = (long) 0;

		for (String cardRangeId : cardRanges) {
			CardRange cardRange = cardRangeDao.getCardRangeById(cardRangeId);
			if (!Objects.isNull(cardRange)) {
				cardRangeOrder = cardRangeOrder + 1;
				ProductCardRange productCardRange = new ProductCardRange();
				productCardRange.setProduct(product);
				productCardRange.setCardRange(cardRange);
				productCardRange.setCardRangeOrder(cardRangeOrder); // Setting card order
				productCardRange.setInsUser(product.getInsUser());
				productCardRange.setInsDate(product.getInsDate());
				productCardRange.setLastUpdUser(product.getLastUpdUser());
				productCardRange.setLastUpdDate(product.getLastUpdDate());

				listOfProductCardRange.add(productCardRange);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return listOfProductCardRange;
	}

	private List<ProductPackage> getProductPackageList(Product product, List<String> packageDefinitions) {
		logger.info(CCLPConstants.ENTER);
		List<ProductPackage> listOfProductPackage = new ArrayList<>();

		packageDefinitions.stream().forEach(id -> {
			PackageDefinition packageDefinition = packageDao.getPackageDefinitionById(id);

			if (!Objects.isNull(packageDefinition)) {
				ProductPackage productPackage = new ProductPackage();
				productPackage.setProduct(product);
				productPackage.setPackageDefinition(packageDefinition);
				productPackage.setInsUser(product.getInsUser());
				productPackage.setInsDate(product.getInsDate());
				productPackage.setLastUpdUser(product.getLastUpdUser());
				productPackage.setLastUpdDate(product.getLastUpdDate());

				listOfProductPackage.add(productPackage);
			}
		});
		logger.info(CCLPConstants.EXIT);
		return listOfProductPackage;
	}

	@Override
	public ProductDTO getProductById(long productId) throws IOException {
		logger.info(CCLPConstants.ENTER);
		ProductDTO productDto = new ProductDTO();
		Product product = productDao.getProductById(productId);
		if (!Objects.isNull(product)) {
			productDto = new ProductDTO(product.getProductId(), product.getProductName(), product.getDescription(),
					Util.updateValuesToMapOfMap(getAllAttributesToCreateProduct(), Util.jsonToMap(product.getAttributes())), 
					product.getIsActive(), product.getProductShortName(),
					getListOfCardRange(product.getListProductCardRange()),
					getListOfSupporedPurse(product.getListProductPurse()),
					getListOfProductPurse(product.getListProductPurse()),
					getListOfPartnerCurrency(product.getListProductCurrency()),
					/* product.getPackageDefinitions() */getListOfPackageDefinition(product.getListOfProductPackage()),
					product.getParentProduct() != null ? product.getParentProduct().getProductId() : null,
					product.getParentProduct() != null ? product.getParentProduct().getProductName() : null,
					product.getIssuer() != null ? product.getIssuer().getIssuerId() : null,
					product.getIssuer() != null ? product.getIssuer().getIssuerName() : null,
					product.getPartner() != null ? product.getPartner().getPartnerId() : null,
					product.getPartner() != null ? product.getPartner().getPartnerName() : null, product.getInsUser(),
					product.getInsDate(), product.getLastUpdUser(), product.getLastUpdDate(),
					product.getProgramID() != null ? product.getProgramID().getPrgmID() : null);
		}
		logger.info(CCLPConstants.EXIT);
		return productDto;
	}


	private List<CurrencyCode> getListOfPartnerCurrency(List<ProductCurrency> listOfCurrency) {
		logger.info(CCLPConstants.ENTER);
		List<CurrencyCode> listOfCurrencyCode = new ArrayList<>(0);
		if (!CollectionUtils.isEmpty(listOfCurrency)) {
			listOfCurrency.forEach(productCurrency -> listOfCurrencyCode.add(productCurrency.getCurrencyCode()));
		}
		logger.info(CCLPConstants.EXIT);
		return listOfCurrencyCode;
	}

	/**
	 * Used to get all product details
	 * 
	 * @param productId
	 * @return ProductDTO
	 * @throws Exception
	 */
	public ProductDTO getProductDetailsById(long productId) {
		logger.info(CCLPConstants.ENTER);
		ProductDTO productDto = new ProductDTO();
		Product product = productDao.getProductById(productId);
		if (!Objects.isNull(product)) {
			productDto = new ProductDTO(product.getProductId(), product.getProductName(), product.getDescription(),
					Util.convertHashMapToJson(Util.updateValuesToMapOfMap(getAllAttributesToCreateProduct(), Util.convertJsonToHashMap(product.getAttributes()))), 
					null, product.getIsActive(), product.getProductShortName(), null, null,
					null, null, product.getParentProduct() != null ? product.getParentProduct().getProductId() : null,
					product.getParentProduct() != null ? product.getParentProduct().getProductName() : null,
					product.getIssuer() != null ? product.getIssuer().getIssuerId() : null,
					product.getIssuer() != null ? product.getIssuer().getIssuerName() : null,
					product.getPartner() != null ? product.getPartner().getPartnerId() : null,
					product.getPartner() != null ? product.getPartner().getPartnerName() : null, product.getInsUser(),
					product.getInsDate(), product.getLastUpdUser(), product.getLastUpdDate());
		}
		logger.info(CCLPConstants.EXIT);
		return productDto;
	}

	private List<CardRange> getListOfCardRange(List<ProductCardRange> productCardRanges) {
		logger.info(CCLPConstants.ENTER);
		List<CardRange> listOfCardRange = new ArrayList<>(0);
		if (!CollectionUtils.isEmpty(productCardRanges)) {
			productCardRanges.forEach(productCardRange -> listOfCardRange.add(productCardRange.getCardRange()));
		}
		logger.info(CCLPConstants.EXIT);
		return listOfCardRange;
	}

	private List<PackageDefinition> getListOfPackageDefinition(List<ProductPackage> listOfProductPackage) {
		logger.info(CCLPConstants.ENTER);
		List<PackageDefinition> listOfPackageDefinition = new ArrayList<>();
		if (!CollectionUtils.isEmpty(listOfProductPackage)) {
			listOfProductPackage
					.forEach(productPackage -> listOfPackageDefinition.add(productPackage.getPackageDefinition()));
		}
		logger.info(CCLPConstants.EXIT);
		return listOfPackageDefinition;
	}

	private List<Purse> getListOfSupporedPurse(List<ProductPurse> listProductPurse) {
		logger.info(CCLPConstants.ENTER);
		List<Purse> listOfSupporedPurse = new ArrayList<>(0);
		
				Collections.sort(listProductPurse);
		if (!CollectionUtils.isEmpty(listProductPurse)) {

			listProductPurse.forEach(productPurse -> listOfSupporedPurse.add(productPurse.getPurse()));
		}
		logger.info(CCLPConstants.EXIT);
		return listOfSupporedPurse;
	}
	private List<ProductPurse> getListOfProductPurse(List<ProductPurse> listProductPurse) {
		logger.info(CCLPConstants.ENTER);
		
		if (!CollectionUtils.isEmpty(listProductPurse)) {

			listProductPurse.forEach(productPurse -> {
				
					String attributesStr = Util.convertHashMapToJson(Util.updateValuesToMapOfMap(getAllAttributesToCreateProductPurse(), Util.convertJsonToHashMap(productPurse.getAttributes())));
					productPurse.setAttributes(attributesStr);
				productPurse.setProduct(null);
			});
		}
		logger.info(CCLPConstants.EXIT);
		return listProductPurse;
	}
	@Override
	public List<ProductDTO> getProductsByName(String productName) {

		return productDTOs(productDao.getProductsByName(productName));
	}

	@Override
	public int countAndDeleteProductById(long productDto) {

		return 0;
	}

	@Override
	public int countOfProductById(long productDto) {

		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, List> getCCFlist() {

		return productDao.getCCFlist();

	}

	public Map<Long, String> getParentProducts() {
		logger.info(CCLPConstants.ENTER);
		Map<Long, String> parentProductsMap = null;

		List<Product> parentProductsList = productDao.getParentProducts();
		if (parentProductsList != null) {
			parentProductsMap = parentProductsList.stream()
					.collect(Collectors.toMap(Product::getProductId, Product::getProductName));
		}
		logger.info(CCLPConstants.EXIT);
		return parentProductsMap;

	}

	public Map<String, Object> getProductGeneral(Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ProductDTO productDTO = null;
		Map<String, Object> generalMap = new HashMap<>();
		try {
			productDTO = getProductDetailsById(productId);

			ProductRuleSet productRuleSet = productDao.getRuleSetByProductId(productId);
			if (productRuleSet != null) {
				generalMap.put("ruleSetId", productRuleSet.getId().getRuleSetId());
			}
			Map<String, Map<String, Object>> productAttributes = Util.jsonToMap(productDTO.getAttributes());
			generalMap.put("generalAttributes", productAttributes.get("General"));
			generalMap.put("partnerId", productDTO.getPartnerId());

		} catch (IOException e) {
			logger.info("Error while parsing attributes " + e);
			throw new ServiceException(ResponseMessages.ERR_WHILE_PARSING_ATTRIBUTES);
		}
		logger.info(CCLPConstants.EXIT);
		return generalMap;

	}

	@Transactional
	public void updateProductGeneral(Map<String, Object> productMap, Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> productGeneralMap = (Map<String, Object>) productMap.get("generalMap");
			ProductDTO existingProductDTO = getProductDetailsById(productId);
			Map<String, Map<String, Object>> productAttributes = Util.jsonToMap(existingProductDTO.getAttributes());
			productAttributes.put("General", productGeneralMap);
			productAttributes = Util.removeEmptyValuefromMapOfMap(productAttributes);
			String productAttributesString = new ObjectMapper().writeValueAsString(productAttributes);
			ProductDTO productDto = new ProductDTO();
			productDto.setAttributes(productAttributesString);
			productDto.setProductId(productId);

			int updateCnt = productDao.updateProductAttributes(productDto);
			if (updateCnt == 0) {
				logger.error("Error while updating general attributes");
				throw new ServiceException(ResponseMessages.ERR_WHILE_UPDATING_GENERAL_ATTRIBUTES);
			}
			/*
			 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
			 * existing product attributes on productId starts venkateshgaddam 29032018
			 */
			addNewAttribToExistingProdAttrib(existingProductDTO, existingProductDTO.getProductId(), productAttributes);
			/*
			 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
			 * existing product attributes on productId ends venkateshgaddam 29032018
			 */
			/**
			 * Update product attributes to cache with product id as key
			 */
			
			distributedCacheService.updateProductAttributesCache(productId, productAttributes);
			cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(productId));
		} catch (IOException e) {
			logger.error("Error while parsing attributes " + e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_WHILE_PARSING_ATTRIBUTES);
		}
		logger.info(CCLPConstants.EXIT);
	}

	/*
	 * Product Limits and Fees are Started here
	 * 
	 * This Method will update the Fees or Limits Attributes based on the request we
	 * are receiving
	 */

	@Override
	public int updatePurseFeeLimitAttributes(Map<String, Object> inputAttributes, Long productId, Long purseId,
			String attributesGroup) {

		logger.info(CCLPConstants.ENTER);

		int count = 0;
		Map<String, Map<String, Object>> productPurseAttributes = new HashMap<>();

		ProductPurse productPurse = productDao.getProductPurseAttributes(productId, purseId);

		if (!Util.isEmpty(productPurse.getAttributes())) {
			try {
				productPurseAttributes = Util.jsonToMap(productPurse.getAttributes());
			} catch (Exception e) {
				logger.error(e.getMessage());
				return 0;
			}
				 Map<String,Object> defaultAttributes = Util.updateValuesToMap(getAllAttributesToCreateProductPurse().get(attributesGroup), inputAttributes);
				defaultAttributes = Util.removeEmptyValuefromMap(defaultAttributes);
				logger.info("Adding attributes.. ");
				productPurseAttributes.put(attributesGroup, defaultAttributes);
			
		} else {
			/// un Reachable code
			logger.debug("ProductAttribute is empty, put all the inputAttributes");
			productPurseAttributes.put(attributesGroup, inputAttributes);
		}
		String attributesJsonResp = null;
		try {
			attributesJsonResp = Util.mapToJson(productPurseAttributes);
		} catch (Exception e) {
			logger.error("Error while parsing productPurseAttributes Map " + e.getMessage());
			return 0;
		}
		logger.debug("Put resulted product attributes into table productId: {}, attributes: {}", productId,
				attributesJsonResp);

		try {
			
			// Updating the productPurse attribute added by VinothS
			count = productDao.updateProductPurseAttributes(attributesJsonResp, productId, purseId);

			if (count == 1) {
				logger.info("Upated product attributes successfully for productID: {} purseId: {}", productId, purseId);
				
				/**
				 * Update Purse attributes to cache with product id and Purse Id as key
				 */
				distributedCacheService.updateProductPurseAttributesCache(String.valueOf(productId + "_" + purseId),
							productPurseAttributes);
				cacheService.updateCache(CCLPConstants.PRODUCTPURSE_CACHE,String.valueOf(productId + "_" + purseId));
			}

		} catch (Exception e) {
			logger.error("Error while updating the Product and Purse attributes " + e.getMessage());
			return 0;
		}

		logger.info(CCLPConstants.EXIT);

		return count;
	}
	/*
	 * Product Limits and Fees are Started here
	 * 
	 * This Method will update the Fees or Limits Attributes based on the request we
	 * are receiving
	 */

	@Override
	public int updateMaintenanceFeeAttributes(Map<String, Object> inputAttributes, Long productId, String attributesGroup) {

		logger.info(CCLPConstants.ENTER);

		int count = 0;
		Map<String, Map<String, Object>> productAttributes = new HashMap<>();

		Product product = productDao.getAttributes(productId);
		if (product == null) {
			logger.error("Failed to get {} attributes for productId: {}", attributesGroup, productId);
			return 0;
		} else if (!Util.isEmpty(product.getAttributes())) {
			try {
				productAttributes = Util.jsonToMap(product.getAttributes());
			} catch (Exception e) {
				logger.error(e.getMessage());
				return 0;
			}

			Map<String, Object> attributes = productAttributes.get(attributesGroup);

				logger.info("Adding attributes.. ");
				logger.debug("{} attributes to be stored in table Attributes -> ", attributesGroup, attributes);
				

				 Map<String,Object> updatedAttributes = Util.updateValuesToMap(getAllAttributesToCreateProduct().get(attributesGroup), inputAttributes);
				 updatedAttributes = Util.removeEmptyValuefromMap(updatedAttributes);
				productAttributes.put(attributesGroup, updatedAttributes);
			
		} else {
			logger.debug("ProductAttribute is empty, put all the inputAttributes");
			productAttributes.put(attributesGroup, inputAttributes);
		}
		String attributesJsonResp = null;
		try {
			attributesJsonResp = Util.mapToJson(productAttributes);
		} catch (Exception e) {
			logger.error("Error while parsing Map " + e.getMessage());
			return 0;
		}
		logger.debug("Put resulted product attributes into table productId: {}, attributes: {}", productId,
				attributesJsonResp);

		try {
			count = productDao.updateAttributes(attributesJsonResp, productId);

			if (count == 1) {
				logger.info("Upated product attributes successfully for productID: {}",productId);
				/*
				 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
				 * existing product attributes on productId starts venkateshgaddam 29032018
				 */
				addNewAttribToExistingProdAttrib(product, product.getProductId(), productAttributes);
				/*
				 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
				 * existing product attributes on productId ends venkateshgaddam 29032018
				 */
				/**
				 * Update product attributes to cache with product id as key
				 */

				distributedCacheService.updateProductAttributesCache(productId, productAttributes);
				cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(productId));
			}

		} catch (Exception e) {
			logger.error("Error while updating the Product attributes " + e.getMessage());
			return 0;
		}

		logger.info(CCLPConstants.EXIT);

		return count;
	}
	
	/*
	 * This method will retrieve all the attributes from present and parent products
	 * 
	 */
	@Override
	public Map<String, Object> getProductFeeLimitsById(Long productId,Long parentProductId,Long purseId, String attributesGroup) throws IOException {

		logger.info(CCLPConstants.ENTER);
		logger.debug("productId: {}, parentProductId: {},purseId: {}",productId,parentProductId,purseId);
		Map<String, Map<String, Object>> productAttributesMap = new HashMap<>();
		Map<String, Object> productAttributes = null;
		ProductPurse productPurseListDtls;
		Product product = productDao.getAttributes(productId);	
		List<ProductPurse> productPurseList = product.getListProductPurse();		
		if(purseId != -1 && parentProductId <= 0) {
			 productPurseListDtls = productPurseList.stream().filter(p -> purseId.equals(p.getPurse().getPurseId())).findAny()
					.orElse(new ProductPurse());
		}else {
			if(parentProductId != 0) {
				Product parentproduct = productDao.getAttributes(parentProductId);
				List<ProductPurse> parentProductPurseList = parentproduct.getListProductPurse();
				productPurseListDtls = productPurseList.stream().filter(p -> "Y".equals(p.getIsDefault())).findAny()
						.orElse(new ProductPurse());
				long defaultPurseId;
				if(purseId != -1) {
					defaultPurseId = purseId;
				}else {
					 defaultPurseId = productPurseListDtls.getPurse().getPurseId();//main product default purse id
				}
				
				
				productPurseListDtls =  parentProductPurseList.stream().filter(p -> defaultPurseId == p.getPurse().getPurseId()).findAny()
						.orElse(new ProductPurse());
			}else {
			 productPurseListDtls = productPurseList.stream().filter(p -> "Y".equals(p.getIsDefault())).findAny()
					.orElse(new ProductPurse());
			}
		}
			
		if (!Util.isEmpty(productPurseListDtls.getAttributes())) {
			try {
				productAttributesMap = Util.jsonToMap(productPurseListDtls.getAttributes());
			} catch (Exception e) {
				logger.error("Error while parsing JSON" + e.getMessage());
			}

			productAttributes = productAttributesMap.get(attributesGroup);
			productAttributes = Util.updateValuesToMap(getAllAttributesToCreateProductPurse().get(attributesGroup),productAttributes);
			if (!CollectionUtils.isEmpty(productAttributes)) {
				logger.debug("Successfully retrieved {} attributes from table productAttributes: {}", attributesGroup,
						productAttributes);
				logger.info(CCLPConstants.EXIT);
				return productAttributes;
			} else {
				logger.debug("failed to retrieve {} attributes from table", attributesGroup);
				logger.info(CCLPConstants.EXIT);
				return null;
			}
		} else {
			logger.info("Failed to retrive attributes from table");
			logger.info(CCLPConstants.EXIT);
			return new HashMap<>(getAllAttributesToCreateProductPurse().get(attributesGroup));
		}
		
	}
	
	/*
	 * This method will retrieve all the attributes from present and parent products
	 * 
	 */
	@Override
	public Map<String, Object> getProductFeeLimitsById(Long productId, String attributesGroup) throws IOException {

		logger.info(CCLPConstants.ENTER);
		logger.debug("productId: {}",productId);
		Map<String, Map<String, Object>> productAttributesMap = new HashMap<>();
		Map<String, Object> productAttributes = null;

		Product product = productDao.getAttributes(productId);
		
			
		if (!Util.isEmpty(product.getAttributes())) {
			try {
				productAttributesMap = Util.jsonToMap(product.getAttributes());
			} catch (Exception e) {
				logger.error("Error while parsing JSON" + e.getMessage());
			}

			productAttributes = productAttributesMap.get(attributesGroup);
			productAttributes = Util.updateValuesToMap(getAllAttributesToCreateProduct().get(attributesGroup),productAttributes);
			if (!CollectionUtils.isEmpty(productAttributes)) {
				logger.debug("Successfully retrieved {} attributes from table productAttributes: {}", attributesGroup,
						productAttributes);
				logger.info(CCLPConstants.EXIT);
				return productAttributes;
			} else {
				logger.debug("failed to retrieve {} attributes from table", attributesGroup);
				logger.info(CCLPConstants.EXIT);
				return null;
			}
		} else {
			logger.info("Failed to retrive attributes from table");
			logger.info(CCLPConstants.EXIT);
			return null;
		}
		
	}


	@Override
	public Map<Object, Object> getAllParentProducts() {

		logger.info(CCLPConstants.ENTER);
		Map<Object, Object> parentProductList = new HashMap<>();

		List<Product> productList = productDao.getAllParentProducts();
		if (!productList.isEmpty())
			productList.forEach(p -> parentProductList.put(p.getProductId(), p.getProductName()));
		logger.info(CCLPConstants.EXIT);
		return parentProductList;
	}

	@Override
	public Map<Object, Object> getAllRetailProducts() {

		logger.info(CCLPConstants.ENTER);
		Map<Object, Object> retailProductList = new HashMap<>();

		List<Product> productList = productDao.getAllRetailProducts();
		if (!productList.isEmpty())
			productList.forEach(p -> retailProductList.put(p.getProductId(), p.getProductName()));
		logger.info(CCLPConstants.EXIT);
		return retailProductList;
	}

	@Override
	public List<Object[]> getTransactionList() {

		return productDao.getTransactionList();

	}

	@Transactional
	public void updateProductRuleSet(Map<String, Object> productMap, Long productId) {
		logger.info(CCLPConstants.ENTER);
		Integer ruleSetId = (Integer) productMap.get("ruleSetId");
		logger.debug("ruleSetId: {}",ruleSetId);
		if (ruleSetId != null && ruleSetId > 0) {
			logger.info("Upadting RuleSet..");
			Integer userId = (Integer) productMap.get("userId");
			ProductRuleSet productRuleSet = new ProductRuleSet();
			productRuleSet.setId(new ProductRuleSetID(Long.valueOf(ruleSetId), productId));
			productRuleSet.setInsUser(Long.valueOf(userId));
			productRuleSet.setLastUpdUser(Long.valueOf(userId));
			productDao.deleteProductRuleSet(productRuleSet);
			productDao.updateProductRuleSet(productRuleSet);
		} else {
			logger.info("Deleting ruleSet..");
			productDao.deleteProductRuleSetByProductId(productId);
		}
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public Map<String, Object> getProductCvv(Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, Object> cvvMap = new HashMap<>();
		try {

			ProductDTO productDTO = getProductDetailsById(productId);
			Map<String, Map<String, Object>> productAttributes = Util.jsonToMap(productDTO.getAttributes());
			cvvMap.put("cvvAttributes", productAttributes.get("CVV"));
		} catch (IOException e) {
			logger.info("Error while parsing attributes");
			logger.error(e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_WHILE_PARSING_ATTRIBUTES);
		}
		logger.info(CCLPConstants.EXIT);
		return cvvMap;
	}

	@Transactional
	public void updateProductCvv(Map<String, Object> productCvvMap, Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		try {

			ProductDTO existingProductDTO = getProductDetailsById(productId);
			@SuppressWarnings("unchecked")
			Map<String, Object> cvvAttributesMap = (Map<String, Object>) productCvvMap.get("cvvAttributes");
			Map<String, Map<String, Object>> productAttributes = Util.jsonToMap(existingProductDTO.getAttributes());
			productAttributes.put("CVV", cvvAttributesMap);
			//Remove empty Values
			productAttributes = Util.removeEmptyValuefromMapOfMap(productAttributes);
			String productAttributesString = new ObjectMapper().writeValueAsString(productAttributes);
			ProductDTO productDto = new ProductDTO();
			productDto.setAttributes(productAttributesString);
			productDto.setProductId(productId);

			int updateCnt = productDao.updateProductAttributes(productDto);
			if (updateCnt == 0) {
				logger.info("Error while updating CVV attributes");
				throw new ServiceException(ResponseMessages.ERR_WHILE_UPDATING_CVV_ATTRIBUTES);
			}
			/*
			 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
			 * existing product attributes on productId starts venkateshgaddam 29032018
			 */
			addNewAttribToExistingProdAttrib(existingProductDTO, existingProductDTO.getProductId(), productAttributes);
			/*
			 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
			 * existing product attributes on productId ends venkateshgaddam 29032018
			 */
			/**
			 * Update product attributes to cache with product id as key
			 */

			distributedCacheService.updateProductAttributesCache(productId, productAttributes);
			cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(productId));
		} catch (IOException e) {
			logger.error("Error while parsing attributes: {}",e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_WHILE_PARSING_ATTRIBUTES);
		}
		logger.info(CCLPConstants.EXIT);
	}

	/**
	 * Getting the card status attributes
	 * 
	 * @param productId
	 * @return card status attributes as Map
	 * @throws ServiceException
	 */
	public Map<String, Object> getCardStatusAttributes(Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.debug("productId: {}",productId);
		ProductDTO productDTO = null;
		Map<String, Object> attributesMap = new HashMap<>();
		try {
			productDTO = getProductDetailsById(productId);

			Map<String, Map<String, Object>> productAttributes = Util.jsonToMap(productDTO.getAttributes());
			attributesMap.put("cardStatusAttributes", productAttributes.get("Card Status"));

		} catch (Exception e) {
			logger.error("Error while parsing attributes: {}",e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_WHILE_PARSING_ATTRIBUTES);
		}
		logger.info(CCLPConstants.EXIT);
		return attributesMap;

	}

	/**
	 * Updating card status attributes
	 * 
	 * @param productMap
	 * @param productId
	 * @throws ServiceException
	 */
	@Transactional
	public void updateCardStatusAttributes(Map<String, Object> productCardStatMap, Long productId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.debug("productId: {}",productId);
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> cardStatusMap = (Map<String, Object>) productCardStatMap.get("cardStatusAttributes");
			ProductDTO existingProductDTO = getProductDetailsById(productId);
			Map<String, Map<String, Object>> productAttributes = Util.jsonToMap(existingProductDTO.getAttributes());
			productAttributes.put("Card Status", cardStatusMap);
			productAttributes = Util.removeEmptyValuefromMapOfMap(productAttributes);
			String productAttributesString = new ObjectMapper().writeValueAsString(productAttributes);
			ProductDTO productDto = new ProductDTO();
			productDto.setAttributes(productAttributesString);
			productDto.setProductId(productId);

			int updateCnt = productDao.updateProductAttributes(productDto);
			if (updateCnt == 0) {
				logger.info("Error while updating CardStatus Attributes");
				throw new ServiceException(ResponseMessages.ERR_WHILE_UPDATING_CARD_STATUS_ATTRIBUTES);
			}
			/*
			 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
			 * existing product attributes on productId starts venkateshgaddam 29032018
			 */
			addNewAttribToExistingProdAttrib(existingProductDTO, existingProductDTO.getProductId(), productAttributes);
			/*
			 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
			 * existing product attributes on productId ends venkateshgaddam 29032018
			 */
			/**
			 * Update product attributes to cache with product id as key
			 */

			distributedCacheService.updateProductAttributesCache(productId, productAttributes);
			cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(productId));
		} catch (Exception e) {
			logger.error("Error while parsing attributes: {}",e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_WHILE_PARSING_ATTRIBUTES);
		}
		logger.info(CCLPConstants.EXIT);
	}

	// alert messages

	public List<Alert> getProductAlertMessages() {

		return productDao.getProductAlertMessages();

	}

	/**
	 * Getting the pin attributes
	 * 
	 * @param productId
	 * @return pin Attributes as Map
	 * @throws ServiceException
	 */
	public Map<String, Object> getProductPin(Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, Object> pinMap = new HashMap<>();
		try {

			ProductDTO productDTO = getProductDetailsById(productId);
			Map<String, Map<String, Object>> productAttributes = Util.jsonToMap(productDTO.getAttributes());
			pinMap.put("pinAttributes", productAttributes.get("PIN"));
		} catch (IOException e) {
			logger.error("Error while parsing attributes",e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_WHILE_PARSING_ATTRIBUTES);
		}
		logger.info(CCLPConstants.EXIT);
		return pinMap;
	}

	/**
	 * Updating Pin attributes
	 * 
	 * @param productPinMap
	 * @param productId
	 * @throws ServiceException
	 */
	@Transactional
	public void updateProductPinAttributes(Map<String, Object> productPinMap, Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		try {

			ProductDTO existingProductDTO = getProductDetailsById(productId);
			Map<String, Map<String, Object>> productAttributes = Util.jsonToMap(existingProductDTO.getAttributes());
			productAttributes.put("PIN", productPinMap);
			productAttributes = Util.removeEmptyValuefromMapOfMap(productAttributes);
			String productAttributesString = new ObjectMapper().writeValueAsString(productAttributes);
			ProductDTO productDto = new ProductDTO();
			productDto.setAttributes(productAttributesString);
			productDto.setProductId(productId);

			int updateCnt = productDao.updateProductAttributes(productDto);
			if (updateCnt == 0) {
				logger.info("Error while updating PIN attributes");
				throw new ServiceException(ResponseMessages.ERR_WHILE_UPDATING_PIN_ATTRIBUTES);
			}
			/*
			 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
			 * existing product attributes on productId starts venkateshgaddam 29032018
			 */
			addNewAttribToExistingProdAttrib(existingProductDTO, existingProductDTO.getProductId(), productAttributes);
			/*
			 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
			 * existing product attributes on productId ends venkateshgaddam 29032018
			 */
			/**
			 * Update product attributes to cache with product id as key
			 */
			distributedCacheService.updateProductAttributesCache(productId, productAttributes);
			cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(productId));
		} catch (IOException e) {
			logger.error("Error while parsing attributes: {}",e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_WHILE_PARSING_ATTRIBUTES);
		}
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public void updateAlertAttributes(Map<String, Object> inAlertAttributes, Long productId) throws ServiceException {

		logger.info(CCLPConstants.ENTER);

		String alertLanguage = "";

		if (CollectionUtils.isEmpty(inAlertAttributes)) {
			logger.info("Error Alert Attribute is empty");
			throw new ServiceException(ResponseMessages.ERR_PRODUCT_ALERT_ATTRIBUTE_EMPTY);
		}

		Product product = productDao.getProductById(productId);

		if (Objects.isNull(product)) {
			logger.error("Failed to get the product for productId: {}", productId);
			throw new ServiceException(CCLPConstants.PRODUCT_U + ResponseMessages.DOESNOT_EXISTS);
		}

		if (inAlertAttributes.containsKey(CCLPConstants.ALERT_LANGUAGE)) {
			alertLanguage = inAlertAttributes.get(CCLPConstants.ALERT_LANGUAGE).toString();
		}

		if (Util.isEmpty(alertLanguage)) {
			logger.error("Error Alert Language Attribute is empty");
			throw new ServiceException(ResponseMessages.ERR_PRODUCT_ALERT_LANGUAGE_ATTRIBUTE_EMPTY);
		}

		try {
			Map<String, Object> alertAttributes = new HashMap<>();

			alertAttributes.put("alertFromEmail", inAlertAttributes.get("alertFromEmail"));
			alertAttributes.put("alertAppName", inAlertAttributes.get("alertAppName"));
			alertAttributes.put("alertAppNotifyType", inAlertAttributes.get("alertAppNotifyType"));
			alertAttributes.put("alertSMSCode", inAlertAttributes.get("alertSMSCode"));
			alertAttributes.put("alertMinBalance", inAlertAttributes.get("alertMinBalance"));
			alertAttributes.put("alertCardStatus", inAlertAttributes.get("alertCardStatus"));
			alertAttributes.put("alertInactivityPeriod", inAlertAttributes.get("alertInactivityPeriod"));
			alertAttributes.put(CCLPConstants.ALERT_LANGUAGE, inAlertAttributes.get(CCLPConstants.ALERT_LANGUAGE));
			alertAttributes.put("alertOptIn", inAlertAttributes.get("alertOptIn"));

			Map<String, Object> enabledAlertSmsAttributes = inAlertAttributes.entrySet().stream()
					.filter(entry -> entry.getKey().startsWith(CCLPConstants.ALERT_MODE_SMS)
							&& (!Objects.isNull(entry.getValue())) && entry.getValue().equals("SMS"))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			Map<String, Object> enabledAlertEmailAttributes = inAlertAttributes.entrySet().stream()
					.filter(entry -> entry.getKey().startsWith(CCLPConstants.ALERT_MODE_EMAIL)
							&& (!Objects.isNull(entry.getValue())) && entry.getValue().equals("Email"))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			if (!CollectionUtils.isEmpty(product.getListProductAlert())) {
				product.getListProductAlert().clear();
			}

			List<Alert> alerts = alertDao.getAllAlerts();

			if (!CollectionUtils.isEmpty(alerts)) {
				logger.info("Adding alerts..");
				for (Alert alert : alerts) {
					String smsTemplate = "";
					String emailSubject = "";
					String emailBody = "";

					if (inAlertAttributes.containsKey(CCLPConstants.ALERT_MODE_SMS + alert.getAlertShortName()))
						alertAttributes.put(CCLPConstants.ALERT_MODE_SMS + alert.getAlertShortName(),
								inAlertAttributes.get(CCLPConstants.ALERT_MODE_SMS + alert.getAlertShortName()));

					if (inAlertAttributes.containsKey(CCLPConstants.ALERT_MODE_EMAIL + alert.getAlertShortName()))
						alertAttributes.put(CCLPConstants.ALERT_MODE_EMAIL + alert.getAlertShortName(),
								inAlertAttributes.get(CCLPConstants.ALERT_MODE_EMAIL + alert.getAlertShortName()));

					if (enabledAlertSmsAttributes.containsKey(CCLPConstants.ALERT_MODE_SMS + alert.getAlertShortName())
							&& inAlertAttributes.containsKey(CCLPConstants.ALERT_SMS + alert.getAlertShortName())) {
						smsTemplate = inAlertAttributes.get(CCLPConstants.ALERT_SMS + alert.getAlertShortName())
								.toString();

					}

					if (enabledAlertEmailAttributes
							.containsKey(CCLPConstants.ALERT_MODE_EMAIL + alert.getAlertShortName())) {
						if (inAlertAttributes.containsKey(CCLPConstants.ALERT_EMAIL_SUB + alert.getAlertShortName())) {
							emailSubject = inAlertAttributes
									.get(CCLPConstants.ALERT_EMAIL_SUB + alert.getAlertShortName()).toString();
						}

						if (inAlertAttributes.containsKey(CCLPConstants.ALERT_EMAIL_BODY + alert.getAlertShortName())) {
							emailBody = inAlertAttributes
									.get(CCLPConstants.ALERT_EMAIL_BODY + alert.getAlertShortName()).toString();
						}
					}

					if (!Util.isEmpty(smsTemplate) || !Util.isEmpty(emailSubject) || !Util.isEmpty(emailBody)) {
						logger.info("Adding alert list..");
						ProductAlert productAlert = new ProductAlert();
						productAlert.setProduct(product);
						productAlert.setAlert(alert);
						productAlert.setLanguageCode(alertLanguage);
						productAlert.setInsDate(new Date());
						productAlert.setLastUpdDate(new Date());
						productAlert.setInsUser(product.getInsUser());
						productAlert.setLastUpdUser(product.getLastUpdUser());
						productAlert.setSmsTemplate(smsTemplate);
						productAlert.setEmailSubject(emailSubject);
						productAlert.setEmailBody(emailBody);

						product.getListProductAlert().add(productAlert);
					}
				}
			}

			// Get existing attributes
			String existingAttributes = product.getAttributes();
			Map<String, Map<String, Object>> attributes = new HashMap<>();

			if (!Util.isEmpty(existingAttributes)) {
				attributes = Util.convertJsonToHashMap(existingAttributes);

				if (!CollectionUtils.isEmpty(attributes)) {
					updateProductAttributes(attributes, alertAttributes, CCLPConstants.ALERTS);
				}

			} else {
				attributes.put(CCLPConstants.ALERTS, inAlertAttributes);
			}

			// Set attributes
			product.setAttributes(Util.convertHashMapToJson(attributes));

			productDao.updateProduct(product);
			/*
			 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
			 * existing product attributes on productId starts venkateshgaddam 29032018
			 */
			addNewAttribToExistingProdAttrib(product, product.getProductId(), attributes);
			/*
			 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
			 * existing product attributes on productId ends venkateshgaddam 29032018
			 */
			/**
			 * Update product attributes to cache with product id as key
			 */
			
			distributedCacheService.updateProductAttributesCache(productId, attributes);
			cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(productId));
		} catch (Exception e) {
			logger.error("Error occured while try to update alerts: {}", e.getMessage());
			throw new ServiceException(ResponseMessages.FAILED_PRODUCT_ALERT_UPDATE);
		}

		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public Map<String, Object> getAlertAttributesByProductId(Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Map<String, Object> alertAttributesMap = null;

		Product product = productDao.getProductById(productId);
		if (Objects.isNull(product)) {
			logger.error("Failed to get product details for productId: {}", productId);
			throw new ServiceException(CCLPConstants.PRODUCT_U + ResponseMessages.DOESNOT_EXISTS);
		}

		String existingAttributes = product.getAttributes();
		if (Util.isEmpty(existingAttributes)) {
			logger.error("Attributes for productId: {} is Empty", productId);
			throw new ServiceException(ResponseMessages.EMPTY_PRODUCT_ATTRIBUTE_RETRIEVE);
		}

		Map<String, Map<String, Object>> existingAttributesMap = null;
		existingAttributesMap = Util.convertJsonToHashMap(existingAttributes);

		if (!existingAttributesMap.containsKey(CCLPConstants.ALERTS)) {
			logger.info("Alerts not present in existing attributes");
			logger.info(CCLPConstants.EXIT);
			return null;
		}

		alertAttributesMap = existingAttributesMap.get(CCLPConstants.ALERTS);
		alertAttributesMap = Util.updateValuesToMap(getAllAttributesToCreateProduct().get(CCLPConstants.ALERTS), alertAttributesMap);

		logger.info("Alert Attributes for Product Id" + productId + " :>>: " + alertAttributesMap);

		if (!CollectionUtils.isEmpty(alertAttributesMap)) {
			Map<String, Object> enabledAlertSmsAttributes = alertAttributesMap.entrySet().stream()
					.filter(entry -> entry.getKey().startsWith(CCLPConstants.ALERT_MODE_SMS)
							&& (!Objects.isNull(entry.getValue())) && entry.getValue().equals("SMS"))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			Map<String, Object> enabledAlertEmailAttributes = alertAttributesMap.entrySet().stream()
					.filter(entry -> entry.getKey().startsWith(CCLPConstants.ALERT_MODE_EMAIL)
							&& (!Objects.isNull(entry.getValue())) && entry.getValue().equals("Email"))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			List<ProductAlert> listOfProductAlert = product.getListProductAlert();
			if (!CollectionUtils.isEmpty(listOfProductAlert)) {
				for (ProductAlert productAlert : listOfProductAlert) {
					String alertShortName = productAlert.getAlert().getAlertShortName();
					if (!Util.isEmpty(alertShortName)) {
						if (enabledAlertSmsAttributes.containsKey(CCLPConstants.ALERT_MODE_SMS + alertShortName)) {
							String smsTemplate = productAlert.getSmsTemplate();
							if (!Util.isEmpty(smsTemplate)) {
								alertAttributesMap.put(CCLPConstants.ALERT_SMS + alertShortName, smsTemplate);
							} else {
								alertAttributesMap.put(CCLPConstants.ALERT_SMS + alertShortName, "");
							}
						}

						if (enabledAlertEmailAttributes.containsKey(CCLPConstants.ALERT_MODE_EMAIL + alertShortName)) {
							String emailSubject = productAlert.getEmailSubject();
							if (!Util.isEmpty(emailSubject)) {
								alertAttributesMap.put(CCLPConstants.ALERT_EMAIL_SUB + alertShortName, emailSubject);
							} else {
								alertAttributesMap.put(CCLPConstants.ALERT_EMAIL_SUB + alertShortName, "");
							}

							String emailBody = productAlert.getEmailBody();
							if (!Util.isEmpty(emailBody)) {
								alertAttributesMap.put(CCLPConstants.ALERT_EMAIL_BODY + alertShortName, emailBody);
							} else {
								alertAttributesMap.put(CCLPConstants.ALERT_EMAIL_BODY + alertShortName, "");
							}
						}
					}
				}
			}
		}
		logger.info("Successfully retrieved Alert attributes from table productAttributes: {}", alertAttributesMap);
		logger.info(CCLPConstants.EXIT);
		return alertAttributesMap;
	}

	@Override
	public int updateProductAttributesByGroupName(Map<String, Object> inAttributes, Long productId, String groupName)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		int count = 0;

		Map<String, Map<String, Object>> productAttributes = new HashMap<>();

		Product product = productDao.getProductById(productId);
		if (Objects.isNull(product)) {
			logger.error("Failed to get product for this productId: {}", productId);
			throw new ServiceException(CCLPConstants.PRODUCT_U + ResponseMessages.DOESNOT_EXISTS);
		}

		String existingAttributes = product.getAttributes();

		if (Util.isEmpty(existingAttributes)) {
			logger.info("Pan Expiry Attributes for productId: {} is Empty", productId);

			productAttributes.put(groupName, inAttributes);
		} else {
			Map<String, Map<String, Object>> attributes = null;
			attributes = Util.convertJsonToHashMap(existingAttributes);

			if (!CollectionUtils.isEmpty(attributes)) {
				updateProductAttributes(attributes, inAttributes, groupName);
			}

			String strAttributes = Util.convertHashMapToJson(attributes);

			try {
				count = productDao.updateProductAttributes(strAttributes, productId);
			} catch (Exception e) {
				logger.error("Error occurred while updating pan expiry attributes, {}", e.getMessage());

				throw new ServiceException(ResponseMessages.ERR_PRODUCT_PANEXPIRY_UPDATE,
						ResponseMessages.ERR_PRODUCT_PANEXPIRY_UPDATE);
			}

			if (count == 1) {
				/*
				 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
				 * existing product attributes on productId starts venkateshgaddam 29032018
				 */
				addNewAttribToExistingProdAttrib(product, product.getProductId(), attributes);
				/*
				 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
				 * existing product attributes on productId ends venkateshgaddam 29032018
				 */
				/**
				 * Update product attributes to cache with product id as key
				 */
				distributedCacheService.updateProductAttributesCache(productId, attributes);
				cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(productId));
				logger.info("Successfully updated Pan Expiry Attributes to Product '{}'", product.getProductName());
			}
		}

		logger.info(CCLPConstants.EXIT);

		return count;
	}

	@Override
	public Map<String, Object> getProductAttributesByIdAndGroupname(Long productId, String attributesGroup)
			throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String, Object>> existingAttributesMap = null;
		Map<String, Object> productAttributes = null;

		Product product = productDao.getProductById(productId);
		if (Objects.isNull(product)) {
			logger.error("Failed to get product attributes for productId: {}", productId);
			throw new ServiceException((CCLPConstants.PRODUCT_U + ResponseMessages.DOESNOT_EXISTS),
					ResponseMessages.DOESNOT_EXISTS);
		}

		String existingAttributes = product.getAttributes();
		if (Util.isEmpty(existingAttributes)) {
			logger.info("Attributes for productId: {} is Empty", productId);
			throw new ServiceException(ResponseMessages.EMPTY_PRODUCT_ATTRIBUTE_RETRIEVE,
					ResponseMessages.DOESNOT_EXISTS);
		}

		existingAttributesMap = Util.convertJsonToHashMap(existingAttributes);

		if (!existingAttributesMap.containsKey(attributesGroup)) {
			logger.error("Pan Expiry Attributes for productId: {} is Empty/ not exist", productId);
			logger.info(CCLPConstants.EXIT);
			return null;
		}

		productAttributes = existingAttributesMap.get(attributesGroup);
		productAttributes = Util.updateValuesToMap(getAllAttributesToCreateProduct().get(attributesGroup), productAttributes);
		logger.info(CCLPConstants.EXIT);

		return productAttributes;
	}

	@Override
	public List<ProductDTO> getProductsByNameForCopy(String productName) {

		return productDTOs(productDao.getProductsByNameForCopy(productName));
	}

	@Override
	public void updProductPurseAttributesCacheById(Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Product product = productDao.getProductById(productId);
		if (Objects.isNull(product)) {
			logger.error("Failed to get product details for productId: {}", productId);
			throw new ServiceException((CCLPConstants.PRODUCT_U + ResponseMessages.DOESNOT_EXISTS),
					ResponseMessages.DOESNOT_EXISTS);
		}
		try {
			Map<String, Map<String, Object>> existingAttributesMap = null;
			String existingAttributes = product.getAttributes();

			if (!Util.isEmpty(existingAttributes)) {
				existingAttributesMap = Util.convertJsonToHashMap(existingAttributes);
				/*
				 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
				 * existing product attributes on productId starts venkateshgaddam 29032018
				 */
				addNewAttribToExistingProdAttrib(product, product.getProductId(), existingAttributesMap);
				/*
				 * Adding SupportedPurses,PartyType,RedemptionDelay into cache based on
				 * productId ends venkateshgaddam 29032018
				 */
				distributedCacheService.updateProductAttributesCache(productId, existingAttributesMap);
				cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(productId));
				updatePurseAttributesBulk(productId, product);
			} else {
				logger.info("Product attributes for productId: {} is empty.", productId);
			}
		} catch (Exception e) {
			logger.error("Exception occured while trying to update the cache for productId: {}", e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_PRODUCT_CACHE_UPDTAE,
					ResponseMessages.ERR_PRODUCT_CACHE_UPDTAE);
		}
		logger.info(CCLPConstants.EXIT);
	}

	public void updateAllProductAndPurseAttributesCache() throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		List<Product> products = productDao.getAllProducts();
		Map<String, List<Object>> supportedPurseMap = getSupportedPurse(0l);
		Map<String, List<Object>> redMapList = redemptionDelayService.getRedeemMerchantsByProductId(0l);
		Map<String, List<Object>> productCurrencyList =  getInternationalCurrency(0l);
		Map<String, Map<String, Object>> grpAccessProdsMap = groupAccessService.getGroupAccessProdsDetails(0l);
		if (!CollectionUtils.isEmpty(products)) {
			products.stream().forEach(p -> {
				try {
					Map<String, Map<String, Object>> existingAttributesMap = new HashMap<>();
					String existingAttributes = p.getAttributes();
					if (!Util.isEmpty(existingAttributes)) {
						existingAttributesMap = Util.convertJsonToHashMap(existingAttributes);
						/*
						 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
						 * existing product attributes on productId starts venkateshgaddam 29032018
						 */
						addNewAttribToExistingProdAttrib(p, supportedPurseMap, redMapList, grpAccessProdsMap, productCurrencyList,
								existingAttributesMap);
						/*
						 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
						 * existing product attributes on productId ends venkateshgaddam 29032018
						 */
						
						distributedCacheService.updateProductAttributesCache(p.getProductId(),
									existingAttributesMap);
						cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(p.getProductId()));
						updatePurseAttributesBulk(p.getProductId(),p); // Update Purse attributes into Cache
					} else {
						logger.info("Product attributes for productId {} and existing attributes map is empty",
								p.getProductId());

					}
				} catch (Exception e) {
					logger.error("Exception occured while trying to update the cache for productId {}: {}",
							p.getProductId(), e.getMessage());
				}

			});
		}
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public Map<Long, String> getProductsWithSamePartnerId(Long partnerId,Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<Long, String> productsWithSamePartnerIdMap = null;
		
		List<Object[]> parentProductsList = null;
 
		parentProductsList = productDao.getProductsWithSamePartnerId(productId);

		productsWithSamePartnerIdMap = parentProductsList.stream()
				.collect(Collectors.toMap(a -> ((Number) (a[0])).longValue(), a -> (String) a[1]));
		logger.info(CCLPConstants.EXIT);
		return productsWithSamePartnerIdMap;

	}

	@Override
	public List<Object[]> getTransactionListByChannelName(String channelName) throws ServiceException {
		return productDao.getTransactionListByChannelName(channelName);
	}

	@Override
	public List<Object[]> getTransactionListByChannelNameTransactionName(String channelName,
			String transactionShortName) throws ServiceException {
		return productDao.getTransactionListByChannelNameTxnName(channelName, transactionShortName);
	}

	@Override
	public Map<Double, String> buildPdfMetaData() {
		logger.info(CCLPConstants.ENTER);
		Map<Double, String> attributeOrderMap = localCacheServiceImpl.getAttributeDefinitionByOrder(null);
		// HardCoded attributes
		Map<Double, String> productAdditionalAttr = new TreeMap<>();
		productAdditionalAttr.put(1.01, "Product|productName|Product Name");
		productAdditionalAttr.put(1.02, "Product|productShortName|Product Short Name");
		productAdditionalAttr.put(1.03, "Product|description|Product Description");
		productAdditionalAttr.put(1.04, "Product|partnerName|Partner Name");
		productAdditionalAttr.put(1.041, "Product|programId|Program ID Name");
		productAdditionalAttr.put(1.05, "Product|issuerName|Issuer Name");
		productAdditionalAttr.put(1.06, "Product|cardRanges|Card Range");
		productAdditionalAttr.put(1.262, "Product|partnerCurrency|Supported Currencies");

		productAdditionalAttr.put(1.21, "Product|supportedPurse|Supported Purse(s)");
		productAdditionalAttr.put(1.22, "Product|packageIds|Package ID");
		productAdditionalAttr.put(2.13, "General|ruleSetId|Rule Set");
		attributeOrderMap.putAll(productAdditionalAttr);
		logger.info(CCLPConstants.EXIT);
		return attributeOrderMap;
	}

	@Override
	public List<String> updateProductAttributesByProgramId(Map<String, Object> inputAttributes, Long programId)
			throws IOException {
		logger.info(CCLPConstants.ENTER);
		List<String> errorList = new ArrayList<>();
		List<Product> products = productDao.getProductsByProgramId(programId);
		if (!CollectionUtils.isEmpty(products)) {
			Iterator<Product> itr = products.iterator();
			while (itr.hasNext()) {
				Product prod = itr.next();
				String attributes = prod.getAttributes();
				Map<String, Map<String, Object>> prodAttributes = Util.jsonToMap(attributes);
				prodAttributes = Util.updateValuesToMapOfMap(getAllAttributesToCreateProduct(), prodAttributes);
				Map<String, Object> limitAttributes = prodAttributes.get("Limits");
				logger.info("Limits...");
				inputAttributes.entrySet().stream().forEach(p -> {
					String newKey = null;

					if (limitAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.MIN_AMT_PER_TX)) {
						newKey = p.getKey().replace(CCLPConstants.MIN_AMT_PER_TX, CCLPConstants.MAX_AMT_PER_TX);
						logger.debug("minAmtPerTx newKey: {}",newKey);
						Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(highValue.toString()) >= 0 && p.getValue() != ""
								&& Integer.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0)
							limitAttributes.put(p.getKey(), p.getValue());
						else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add(
									"Maximum amount per transaction is lessthan Minimum amount per transaction for Product : "
											+ prod.getProductName());
					}
					if (limitAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.MAX_AMT_PER_TX)) {
						newKey = p.getKey().replace(CCLPConstants.MAX_AMT_PER_TX, CCLPConstants.MIN_AMT_PER_TX);
						logger.debug("maxAmtPerTx newKey: {}",newKey);
						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(value.toString()) == 0)
							limitAttributes.put(p.getKey(), p.getValue());
						else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add(
									"Minimum amount per transaction is greater than Maximum amount per transaction for Product : "
											+ prod.getProductName());
					}

					if (limitAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.DAILY_MAX_AMT)) {
						newKey = p.getKey().replace(CCLPConstants.DAILY_MAX_AMT, CCLPConstants.WEEKLY_MAX_AMT);
						logger.debug("dailyMaxAmt newKey: {}",newKey);
						Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0)
							limitAttributes.put(p.getKey(), p.getValue());
						else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Weekly max Amount is less than Daily Max Amount for Product : "
									+ prod.getProductName());
					}

					if (limitAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.DAILY_MAX_COUNT)) {
						newKey = p.getKey().replace(CCLPConstants.DAILY_MAX_COUNT, CCLPConstants.WEEKLY_MAX_COUNT);
						logger.debug("dailyMaxCount newKey: {}",newKey);
						Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0)
							limitAttributes.put(p.getKey(), p.getValue());
						else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Weekly max Count is less than Daily Max Count for Product : "
									+ prod.getProductName());
					}

					if (limitAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.WEEKLY_MAX_AMT)) {
						newKey = p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT, CCLPConstants.DAILY_MAX_AMT);
						logger.debug("weeklyMaxAmt newKey: {}",newKey);
						Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;

						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0) {
							String newKey2 = p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT,
									CCLPConstants.MONTHLY_MAX_AMT);
							Object value2 = (limitAttributes.get(newKey2) != "" && limitAttributes.get(newKey2) != null)
									? limitAttributes.get(newKey2)
									: 0;
							if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != "" && p.getValue() != null)
								limitAttributes.put(p.getKey(), p.getValue());
							else
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						} else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Daily Max Amount is greater than Weekly Max Amount for Product : "
									+ prod.getProductName());
					}

					if (limitAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.WEEKLY_MAX_AMT)) {
						newKey = p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT, CCLPConstants.MONTHLY_MAX_AMT);
						logger.debug("weeklyMaxAmt newKey: {}",newKey);
						Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0) {
							String newKey2 = p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT,
									CCLPConstants.DAILY_MAX_AMT);
							Object value2 = (limitAttributes.get(newKey2) != "" && limitAttributes.get(newKey2) != null)
									? limitAttributes.get(newKey2)
									: 0;
							if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != "" && p.getValue() != null)
								limitAttributes.put(p.getKey(), p.getValue());
							else
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						} else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Weekly Max Amount is greater than Monthly Max Amount for Product : "
									+ prod.getProductName());
					}

					if (limitAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.WEEKLY_MAX_COUNT)) {
						newKey = p.getKey().replace(CCLPConstants.WEEKLY_MAX_COUNT, CCLPConstants.DAILY_MAX_COUNT);
						logger.debug("weeklyMaxCount newKey: {}",newKey);
						Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;

						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0) {
							String newKey2 = p.getKey().replace(CCLPConstants.WEEKLY_MAX_COUNT,
									CCLPConstants.MONTHLY_MAX_COUNT);
							Object value2 = (limitAttributes.get(newKey2) != "" && limitAttributes.get(newKey2) != null)
									? limitAttributes.get(newKey2)
									: 0;
							if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != "" && p.getValue() != null)
								limitAttributes.put(p.getKey(), p.getValue());
							else
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						} else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Daily Max Count is greater than weekly Max Count for Product : "
									+ prod.getProductName());
					}

					if (limitAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.WEEKLY_MAX_COUNT)) {
						newKey = p.getKey().replace(CCLPConstants.WEEKLY_MAX_COUNT, CCLPConstants.MONTHLY_MAX_COUNT);
						logger.debug("weeklyMaxCount newKey: {}",newKey);
						Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0) {
							String newKey2 = p.getKey().replace(CCLPConstants.WEEKLY_MAX_COUNT,
									CCLPConstants.DAILY_MAX_COUNT);
							Object value2 = (limitAttributes.get(newKey2) != "" && limitAttributes.get(newKey2) != null)
									? limitAttributes.get(newKey2)
									: 0;
							if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != "" && p.getValue() != null)
								limitAttributes.put(p.getKey(), p.getValue());
							else
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						} else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Weekly Max Count is greater than Monthly Max Count for Product : "
									+ prod.getProductName());
					}

					if (limitAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.MONTHLY_MAX_AMT)) {
						newKey = p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT, CCLPConstants.WEEKLY_MAX_AMT);
						logger.debug("monthlyMaxAmt newKey: {}",newKey);
						Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0) {
							String newKey2 = p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT,
									CCLPConstants.YEARLY_MAX_AMT);
							Object value2 = (limitAttributes.get(newKey2) != "" && limitAttributes.get(newKey2) != null)
									? limitAttributes.get(newKey2)
									: 0;
							if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != "" && p.getValue() != null)
								limitAttributes.put(p.getKey(), p.getValue());
							else
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						} else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Weekly Max Amount is greater than Monthly Max Amount for Product : "
									+ prod.getProductName());
					}

					if (limitAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.MONTHLY_MAX_AMT)) {
						newKey = p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT, CCLPConstants.YEARLY_MAX_AMT);
						logger.debug("monthlyMaxAmt newKey: {}",newKey);
						Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0) {
							String newKey2 = p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT,
									CCLPConstants.WEEKLY_MAX_AMT);
							Object value2 = (limitAttributes.get(newKey2) != "" && limitAttributes.get(newKey2) != null)
									? limitAttributes.get(newKey2)
									: 0;
							if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != "" && p.getValue() != null)
								limitAttributes.put(p.getKey(), p.getValue());
							else
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						} else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Monthly Max Amount is greater than Yearly Max Amount for Product : "
									+ prod.getProductName());
					}

					if (limitAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.MONTHLY_MAX_COUNT)) {
						newKey = p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT, CCLPConstants.YEARLY_MAX_COUNT);
						logger.debug("monthlyMaxCount newKey: {}",newKey);
						Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0) {
							String newKey2 = p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT,
									CCLPConstants.WEEKLY_MAX_COUNT);
							Object value2 = (limitAttributes.get(newKey2) != "" && limitAttributes.get(newKey2) != null)
									? limitAttributes.get(newKey2)
									: 0;
							if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != "" && p.getValue() != null)
								limitAttributes.put(p.getKey(), p.getValue());
							else
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						} else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Monthly Max Count is greater than Yearly max Count for Product : "
									+ prod.getProductName());
					}

					if (limitAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.MONTHLY_MAX_COUNT)) {
						newKey = p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT, CCLPConstants.WEEKLY_MAX_COUNT);
						logger.debug("monthlyMaxCount newKey: {}",newKey);
						Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;

						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0) {
							String newKey2 = p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT,
									CCLPConstants.YEARLY_MAX_COUNT);
							Object value2 = (limitAttributes.get(newKey2) != "" && limitAttributes.get(newKey2) != null)
									? limitAttributes.get(newKey2)
									: 0;
							if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != "" && p.getValue() != null)
								limitAttributes.put(p.getKey(), p.getValue());
							else
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						} else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Weekly Max Count is greater than Monthly Max Count for Product : "
									+ prod.getProductName());
					}

					if (limitAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.YEARLY_MAX_AMT)) {
						newKey = p.getKey().replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.MONTHLY_MAX_AMT);
						logger.debug("yearlyMaxAmt newKey: {}",newKey);
						Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;

						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0)
							limitAttributes.put(p.getKey(), p.getValue());
						else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Monthly Max Amount is greater than Yearly Max Amount for Product : "
									+ prod.getProductName());
					}

					if (limitAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.YEARLY_MAX_COUNT)) {
						newKey = p.getKey().replace(CCLPConstants.YEARLY_MAX_COUNT, CCLPConstants.MONTHLY_MAX_COUNT);
						logger.debug("yearlyMaxCount newKey: {}",newKey);
						Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
								? limitAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0)
							limitAttributes.put(p.getKey(), p.getValue());
						else if (p.getValue() == null || p.getValue() == "")
							limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
							limitAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Monthly Max Count is greater than Yearly Max Count for Product : "
									+ prod.getProductName());
					}

				});
				prodAttributes.put("Limits", limitAttributes);

				Map<String, Object> tranFeeAttributes = prodAttributes.get(CCLPConstants.PRODUCT_TAB_TRANSACTION_FEES);
				logger.info("Transaction Fees...");
				inputAttributes.entrySet().stream().forEach(p -> {
					String newKey = null;
					if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FREE_COUNT)) {
						newKey = p.getKey().replace(CCLPConstants.FREE_COUNT, CCLPConstants.MAX_COUNT);
						logger.debug("freeCount newKey: {}",newKey);
						Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (tranFeeAttributes.get(newKey) != "" && tranFeeAttributes.get(newKey) != null)
								? tranFeeAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
							tranFeeAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0)
							tranFeeAttributes.put(p.getKey(), p.getValue());
						else if (p.getValue() == null || p.getValue() == "")
							tranFeeAttributes.put(p.getKey(), tranFeeAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
							tranFeeAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add(
									"Maximum Count is less than Free Count for Product : " + prod.getProductName());
					}

					if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.MAX_COUNT)) {
						newKey = p.getKey().replace(CCLPConstants.MAX_COUNT, CCLPConstants.FREE_COUNT);
						logger.debug("maxCount newKey: {}",newKey);
						Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (tranFeeAttributes.get(newKey) != "" && tranFeeAttributes.get(newKey) != null)
								? tranFeeAttributes.get(newKey)
								: 0;
						if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
							tranFeeAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0)
							tranFeeAttributes.put(p.getKey(), p.getValue());
						else if (p.getValue() == null || p.getValue() == "")
							tranFeeAttributes.put(p.getKey(), tranFeeAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
							tranFeeAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add(
									"Free Count is greater than Maximum Count for Product : " + prod.getProductName());
					}
					if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEE_DESC)
							&& (p.getValue() != null || p.getValue() != "")) {
						tranFeeAttributes.put(p.getKey(), p.getValue());
					}

					if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEE_AMT)
							&& (p.getValue() != null || p.getValue() != "")) {
						tranFeeAttributes.put(p.getKey(), p.getValue());
					}

					if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEE_CONDITION)
							&& (p.getValue() != null || p.getValue() != "")) {

						tranFeeAttributes.put(p.getKey(), p.getValue());
					}

					if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEE_PERCENT)
							&& (p.getValue() != null || p.getValue() != "")) {
						tranFeeAttributes.put(p.getKey(), p.getValue());
					}

					if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.MIN_FEE_AMT)
							&& (p.getValue() != null || p.getValue() != "")) {
						tranFeeAttributes.put(p.getKey(), p.getValue());
					}

					if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FREE_COUNT_FREQ)
							&& (p.getValue() != null || p.getValue() != "")) {
						tranFeeAttributes.put(p.getKey(), p.getValue());
					}

					if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.MAX_COUNT_FREQ)
							&& (p.getValue() != null || p.getValue() != "")) {
						tranFeeAttributes.put(p.getKey(), p.getValue());
					}
					if (tranFeeAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.MONTHLY_FEECAP_AVAIL)
							&& (p.getValue() != null || p.getValue() != "")) {
						tranFeeAttributes.put(p.getKey(), p.getValue());
					}

				});
				prodAttributes.put(CCLPConstants.PRODUCT_TAB_TRANSACTION_FEES, tranFeeAttributes);

				Map<String, Object> maintenanceFeeAttributes = prodAttributes.get(CCLPConstants.PRODUCT_TAB_MAINTENANCE_FEES);
				logger.debug("Maintenance Fees...");
				inputAttributes.entrySet().stream().forEach(p -> {
					String newKey = null;
					if (maintenanceFeeAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.FREE_COUNT)) {
						newKey = p.getKey().replace(CCLPConstants.FREE_COUNT, CCLPConstants.MAX_COUNT);
						Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (maintenanceFeeAttributes.get(newKey) != ""
								&& maintenanceFeeAttributes.get(newKey) != null) ? maintenanceFeeAttributes.get(newKey)
										: 0;
						if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
							maintenanceFeeAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0)
							maintenanceFeeAttributes.put(p.getKey(), p.getValue());
						else if (p.getValue() == null || p.getValue() == "")
							maintenanceFeeAttributes.put(p.getKey(), maintenanceFeeAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
							maintenanceFeeAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Max Count is less than Free Count for Product : " + prod.getProductName());
					}

					if (maintenanceFeeAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.MAX_COUNT)) {
						newKey = p.getKey().replace(CCLPConstants.MAX_COUNT, CCLPConstants.FREE_COUNT);
						Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
								? inputAttributes.get(newKey)
								: 0;
						Object value = (maintenanceFeeAttributes.get(newKey) != ""
								&& maintenanceFeeAttributes.get(newKey) != null) ? maintenanceFeeAttributes.get(newKey)
										: 0;
						if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != ""
								&& Integer.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
							maintenanceFeeAttributes.put(p.getKey(), p.getValue());
						else if (Integer.parseInt(value.toString()) == 0)
							maintenanceFeeAttributes.put(p.getKey(), p.getValue());
						else if (p.getValue() == null || p.getValue() == "")
							maintenanceFeeAttributes.put(p.getKey(), maintenanceFeeAttributes.get(p.getKey()));
						else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
							maintenanceFeeAttributes.put(p.getKey(), p.getValue());
						else
							errorList.add("Free Count greater than Max Count for Product : " + prod.getProductName());

					}

					if (maintenanceFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEE_DESC)
							&& (p.getValue() != null || p.getValue() != "")) {

						maintenanceFeeAttributes.put(p.getKey(), p.getValue());
					}

					if (maintenanceFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEE_AMT)
							&& (p.getValue() != null || p.getValue() != "")) {

						maintenanceFeeAttributes.put(p.getKey(), p.getValue());
					}

					if (maintenanceFeeAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.FIRST_MONTH_FEE_ASS_DAYS)
							&& (p.getValue() != null || p.getValue() != "")) {

						maintenanceFeeAttributes.put(p.getKey(), p.getValue());
					}

					if (maintenanceFeeAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.CAP_FEE_AMT)
							&& (p.getValue() != null || p.getValue() != "")) {

						maintenanceFeeAttributes.put(p.getKey(), p.getValue());
					}
					if (maintenanceFeeAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.MONTHLY_FEECAP_AVAIL)
							&& (p.getValue() != null || p.getValue() != "")) {
						maintenanceFeeAttributes.put(p.getKey(), p.getValue());
					}

					if (maintenanceFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.CLAWBACK)
							&& (p.getValue() != null || p.getValue() != "")) {

						maintenanceFeeAttributes.put(p.getKey(), p.getValue());
					}
					if (maintenanceFeeAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.CLAWBACK_COUNT)
							&& (p.getValue() != null || p.getValue() != "")) {

						maintenanceFeeAttributes.put(p.getKey(), p.getValue());
					}

					if (maintenanceFeeAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.CLAWBACK_OPTION)
							&& (p.getValue() != null || p.getValue() != "")) {

						maintenanceFeeAttributes.put(p.getKey(), p.getValue());
					}
					if (maintenanceFeeAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.CLAW_BACK_MAX_AMT)
							&& (p.getValue() != null || p.getValue() != "")) {

						maintenanceFeeAttributes.put(p.getKey(), p.getValue());
					}

					if (maintenanceFeeAttributes.containsKey(p.getKey())
							&& p.getKey().endsWith(CCLPConstants.FEE_CAP_AVAIL)
							&& (p.getValue() != null || p.getValue() != "")) {

						maintenanceFeeAttributes.put(p.getKey(), p.getValue());
					}

				});
				prodAttributes.put(CCLPConstants.PRODUCT_TAB_MAINTENANCE_FEES, maintenanceFeeAttributes);

				Map<String, Object> feeCapAttributes = prodAttributes.get("Monthly Fee Cap");
				inputAttributes.entrySet().stream().forEach(p -> {
					if (feeCapAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEE_DESC)
							&& (p.getValue() != null || p.getValue() != "")) {

						feeCapAttributes.put(p.getKey(), p.getValue());
					}
					if (feeCapAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.ASSESSMENT_DATE)
							&& (p.getValue() != null || p.getValue() != "")) {

						feeCapAttributes.put(p.getKey(), p.getValue());
					}
					if (feeCapAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEECAP_AMT)
							&& (p.getValue() != null || p.getValue() != "")) {

						feeCapAttributes.put(p.getKey(), p.getValue());
					}
					if (feeCapAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.TIME_PERIOD)
							&& (p.getValue() != null || p.getValue() != "")) {

						feeCapAttributes.put(p.getKey(), p.getValue());
					}

				});
				prodAttributes.put("Monthly Fee Cap", feeCapAttributes);
				prodAttributes = Util.removeEmptyValuefromMapOfMap(prodAttributes);
				String attributesString = Util.mapToJson(prodAttributes);
				productDao.updateAttributes(attributesString, prod.getProductId());

			}
		}
		logger.info("Error List is : " + errorList);
		logger.info(CCLPConstants.EXIT);
		return errorList;
	}

	@Override
	public Long getProductIdByUPC(String upc) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Long productId = null;

		try {
			productId = Long.parseLong(productDao.getProductIdByUPC(upc).toString());
		} catch (EmptyResultDataAccessException e) {
			logger.info("Exception occured while get Product Id by UPC: {}", e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_UPC_NOT_EXIST, ResponseMessages.ERR_UPC_NOT_EXIST);
		} catch (Exception e) {
			logger.info("Exception occured while get Product Id by UPC: {}", e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_UPC_EXC, ResponseMessages.ERR_UPC_EXC);
		}

		logger.info(CCLPConstants.EXIT);
		return productId;
	}

	@Override
	public List<String> getPackagesIdByProductId(Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		List<String> packages = null;
		Product product = productDao.getProductById(productId);
		if (Objects.isNull(product)) {
			logger.error("Failed to get product details for productId: {}", productId);
			throw new ServiceException((CCLPConstants.PRODUCT_U + ResponseMessages.DOESNOT_EXISTS),
					ResponseMessages.DOESNOT_EXISTS);
		}

		try {
			List<ProductPackage> listOfProductPackages = product.getListOfProductPackage();
			if (!CollectionUtils.isEmpty(listOfProductPackages)) {
				packages = listOfProductPackages.stream().map(out -> out.getPackageDefinition().getPackageId())
						.collect(Collectors.toList());
			}
		} catch (Exception e) {
			logger.info("Exception occured while get Package Ids by Product Id: {}", e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_PRODUCT_PACKAGE, ResponseMessages.ERR_PRODUCT_PACKAGE);
		}

		logger.info(CCLPConstants.EXIT);
		return packages;
	}

	/**
	 * This method will give list of supportedPurses based on productId
	 * 
	 * @author venkateshgaddam
	 * @param productId
	 * @return Map<String, List<Object>>
	 */
	@Override
	public Map<String, List<Object>> getSupportedPurse(Long productId) {
		logger.info(CCLPConstants.ENTER);
		Map<String, List<Object>> supportedPurseMap = new HashMap<>();
		try {
			List<Object[]> supportedPurseList = productDao.getSupportedPurse(productId);
			if (supportedPurseList != null && !supportedPurseList.isEmpty()) {
				supportedPurseList.stream().forEach(supportedPurse -> {
					if (supportedPurseMap.get(supportedPurse[0] + "") == null) {
						supportedPurseMap.put(supportedPurse[0] + "", new ArrayList<>());
						supportedPurseMap.get(supportedPurse[0] + "").add(supportedPurse);
					} else {
						supportedPurseMap.get(supportedPurse[0] + "").add(supportedPurse);
					}
				});
			}
		} catch (Exception e) {
			logger.info("Exception in getGroupAccessProdsDetails:" + e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return supportedPurseMap;
	}

	/**
	 * 
	 * @author venkateshgaddam
	 * @param product
	 * @param attributesMap
	 */
	private void addSupportedPurseToCache(Map<String, Map<String, Object>> attributesMap,
			List<Object> supportedPurseList) {
		logger.info(CCLPConstants.ENTER);
		try {
			if (attributesMap.containsKey("Product")) {
				Map<String, Object> prodAtribs = attributesMap.get("Product");
				if (!CollectionUtils.isEmpty(prodAtribs)) {
					prodAtribs.put(CCLPConstants.SUPPORT_PURSES, supportedPurseList);
					attributesMap.put("Product", prodAtribs);
				}
			}
		} catch (Exception e) {
			logger.info("Exception  occured while updating addCurrencyCodesToCache : " + e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
	}
	
	
	/**
	 * Adding Party Type in attributesMap
	 * 
	 * @param productId,
	 *            attributesMap
	 * @author venkateshgaddam
	 */
	private void addPartyTypeToCache(long productId, Map<String, Map<String, Object>> attributesMap) {
		logger.info(CCLPConstants.ENTER);
		try {
			Map<String, Map<String, Object>> grpAccessProdsMap = groupAccessService
					.getGroupAccessProdsDetails(productId);
			if (!CollectionUtils.isEmpty(grpAccessProdsMap)) {
				attributesMap.put(CCLPConstants.PARTY_TYPE, grpAccessProdsMap.get(String.valueOf(productId)));
			}
		} catch (Exception e) {
			logger.info("Exception occured while updating party Type to Cache : " + e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
	}

	public void getRedemptionDelayConfig(long productId, Map<String, Map<String, Object>> attributesMap) {
		logger.info(CCLPConstants.ENTER);
		try {
			if (!CollectionUtils.isEmpty(attributesMap) && productId > 0 && attributesMap.containsKey("Product")) {

					Map<String, Object> prodAtribs = attributesMap.get("Product");
					prodAtribs.put(CCLPConstants.REDEMPTION_DELAY,
							redemptionDelayService.getRedeemMerchantsByProductId(productId));
					attributesMap.put("Product", prodAtribs);
			}
		} catch (Exception e) {
			logger.info("Exception while invoking getRedemptionDelayConfig with productId:" + productId);
			logger.error(e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);

	}
	
	public void getInternationalCurrency(long productId, Map<String, Map<String, Object>> attributesMap) {
		logger.info(CCLPConstants.ENTER);
		try {
			if (!CollectionUtils.isEmpty(attributesMap) && productId > 0 && attributesMap.containsKey("Product")) {

					Map<String, Object> prodAtribs = attributesMap.get("Product");
					prodAtribs.put(CCLPConstants.PRODUCT_CURRENCY,
							getInternationalCurrency(productId));
					attributesMap.put("Product", prodAtribs);
			}
		} catch (Exception e) {
			logger.info("Exception while invoking getRedemptionDelayConfig with productId:" + productId);
			logger.error(e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
	}
	
	@Override
	public Map<String, List<Object>> getInternationalCurrency(Long productId) {
		logger.info(CCLPConstants.ENTER);
		Map<String, List<Object>> internationalCurrencyMap = new HashMap<>();
		try {
			List<Object[]> internationalCurrencyList = productDao.getInternationalCurrencyByProductId(productId);
		
			if (internationalCurrencyList != null && !CollectionUtils.isEmpty(internationalCurrencyList)) {
				for (Iterator<Object[]> iterator = internationalCurrencyList.iterator(); iterator.hasNext();) {
					Object[] objects =  iterator.next();
					
					if(internationalCurrencyMap.get(objects[0])==null  ){
						internationalCurrencyMap.put(objects[0]+"", new ArrayList<>());
						internationalCurrencyMap.get(objects[0]+"").add(objects);
					}else{
						internationalCurrencyMap.get(objects[0]+"").add(objects);
						
					}
					
				}
			}
		} catch (Exception e) {
			logger.info("Exception in getProductCurrencyDetails:" + e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return internationalCurrencyMap;
	}
	

	public Map<String, List<Object>> getRedemptionMapList(long prodcutId, Map<String, List<Object>> redmMap) {
		logger.info(CCLPConstants.ENTER);
		Map<String, List<Object>> mapListTemp = new HashMap<>();
		try {
			String inputProd = String.valueOf(prodcutId);
			redmMap.forEach((key, value) -> {
				String getProductID = key.split("_")[0];
				if (!Util.isEmpty(inputProd) && inputProd.equals(getProductID)) {
					mapListTemp.put(key, value);
				}
			});
		} catch (Exception e) {
			logger.info("Exception while invoking getRedemptionMapList with productId:" + prodcutId);
			logger.error(e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return mapListTemp;

	}

	/**
	 * Getting ProductAttributes based on productID
	 * 
	 * @param productId
	 * @throws ServiceException
	 * @author venkateshgaddam
	 */
	@Override
	public String getOrUpdateProductAttributes(Long productId,Long purseId,String source) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Product product = productDao.getProductById(productId);
		if (Objects.isNull(product)) {
			logger.error("Failed to get product details for productId: {} ", productId);
			throw new ServiceException(ResponseMessages.ERR_PRODUCT_NULL, ResponseMessages.ERR_PRODUCT_NULL);
		}
		try {
			Map<String, Map<String, Object>> prodAttributes = Util.convertJsonToHashMap(product.getAttributes());
			if (!CollectionUtils.isEmpty(prodAttributes)) {
				/*
				 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
				 * existing product attributes on productId starts venkateshgaddam 29042019
				 */
				addNewAttribToExistingProdAttrib(product, productId, prodAttributes);
				/*
				 * Adding SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache into
				 * existing product attributes on productId ends venkateshgaddam 29042019
				 */
				if (source.equalsIgnoreCase(CCLPConstants.FROM_CACHE)) {
					updateCacheValueAsynchronously(prodAttributes, CCLPConstants.PRODUCT_ATTRIBUTUES, productId, 0);
				}

			} else {
				logger.info("Product attributes for productId: {} is empty.", productId);
				throw new ServiceException(ResponseMessages.ERR_ATTRIBUTES_MAP_ISEMPTY,
						ResponseMessages.ERR_ATTRIBUTES_MAP_ISEMPTY);
			}
			
			if (purseId == 0) {
				purseId =  Long.parseLong(String.valueOf(prodAttributes.get("Product").get("defaultPurse")));
			}
			
				Map<String, Map<String, Object>> productPurseAttributes = null;
				ProductPurse productPurse = getPurseDetailsById(productId, purseId);
				if (productPurse != null) {
					productPurseAttributes = Util.convertJsonToHashMap(productPurse.getAttributes());
					if (source.equalsIgnoreCase(CCLPConstants.FROM_CACHE)) {
						updateCacheValueAsynchronously(productPurseAttributes, CCLPConstants.PRODUCT_PURSE_ATTRIBUTUES,
								productId, purseId);
					}
				}
				prodAttributes.putAll(productPurseAttributes);
				logger.info(CCLPConstants.EXIT);
				
				return Util.convertHashMapToJson(prodAttributes);
			

		} catch (Exception e) {
			logger.error("Exception occured while trying to get attributes for product Id: {}", e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_ATTRIBUTES_MAP_ISEMPTY,
					ResponseMessages.ERR_ATTRIBUTES_MAP_ISEMPTY);
		}
		
	}

	/**
	 * Adding issuerId,partnerId to cache.
	 * 
	 * @param productObj
	 * @param attributesMap
	 * @author venkateshgaddam
	 */
	private void addIssuerPartnerToCache(Object productObj, Map<String, Map<String, Object>> attributesMap) {
		logger.info(CCLPConstants.ENTER);
		try {
			if (attributesMap.containsKey("Product")) {
				Map<String, Object> prodAttribs = attributesMap.get("Product");
				if (!CollectionUtils.isEmpty(prodAttribs) && productObj != null) {
					if (productObj instanceof Product) {
						Product product = (Product) productObj;
						prodAttribs.put(CCLPConstants.ISSUERID,
								product.getIssuer() != null ? product.getIssuer().getIssuerId() : null);
						prodAttribs.put(CCLPConstants.PARTNER_ID,
								product.getPartner() != null ? product.getPartner().getPartnerId() : null);

					} else if (productObj instanceof ProductDTO) {
						ProductDTO productDto = (ProductDTO) productObj;
						prodAttribs.put(CCLPConstants.ISSUERID, productDto.getIssuerId());
						prodAttribs.put(CCLPConstants.PARTNER_ID, productDto.getPartnerId());

					}
					attributesMap.put("Product", prodAttribs);
				}
			}
		} catch (Exception e) {
			logger.info("Exception  occured while updating party Type to Cache : " + e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
	}

	/**
	 * Adding New Attributes to existing DB product Attributes New Attributes
	 * like(SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache) to
	 * existing DB product Attributes.
	 * 
	 * @param productObj
	 * @param productId
	 * @param prodAttributes
	 * @author venkateshgaddam
	 */
	private void addNewAttribToExistingProdAttrib(Object productObj, long productId,
			Map<String, Map<String, Object>> prodAttributes) {
		logger.info(CCLPConstants.ENTER);
		addIssuerPartnerToCache(productObj, prodAttributes);
		addPartyTypeToCache(productId, prodAttributes);		
		getRedemptionDelayConfig(productId, prodAttributes);
		Map<String, List<Object>> supportedPurseMap = getSupportedPurse(productId);
		if (!CollectionUtils.isEmpty(supportedPurseMap) && supportedPurseMap.containsKey(String.valueOf(productId))) {
			addSupportedPurseToCache(prodAttributes, supportedPurseMap.get(String.valueOf(productId)));
		}
		
		getInternationalCurrency(productId, prodAttributes);
		logger.info(CCLPConstants.EXIT);
	}// addNewAttribToExistingProdAttrib

	

	/**
	 * Adding New Attributes to existing DB product Attributes New Attributes
	 * like(SupportedPurses,PartyType,RedemptionDelay,addIssuerPartnerToCache) to
	 * existing DB product Attributes.
	 * 
	 * @param productObj
	 * @param productId
	 * @param prodAttributes
	 * @author venkateshgaddam
	 */
	private void addNewAttribToExistingProdAttrib(Product p, Map<String, List<Object>> supportedPurseMap,
			Map<String, List<Object>> redMapList, Map<String, Map<String, Object>> grpAccessProdsMap,   Map<String, List<Object>> productCurrencyMap,
			Map<String, Map<String, Object>> existingAttributesMap) {
		logger.info(CCLPConstants.ENTER);
		if (p != null) {
			addIssuerPartnerToCache(p, existingAttributesMap);
			if (!CollectionUtils.isEmpty(grpAccessProdsMap)
					&& grpAccessProdsMap.containsKey(String.valueOf(p.getProductId() + ""))) {
				existingAttributesMap.put(CCLPConstants.PARTY_TYPE,
						grpAccessProdsMap.get(String.valueOf(p.getProductId() + "")));
			}
			if (!CollectionUtils.isEmpty(supportedPurseMap)
					&& supportedPurseMap.containsKey(String.valueOf(p.getProductId()))) {
				addSupportedPurseToCache(existingAttributesMap,
						supportedPurseMap.get(String.valueOf(p.getProductId())));
			}
			
			if (!CollectionUtils.isEmpty(productCurrencyMap)) {
				Map<String, List<Object>> productCurrencyList = getInternationalCurrency(p.getProductId(), productCurrencyMap);
				if (existingAttributesMap.containsKey("Product")) {
					Map<String, Object> prodAtribs = existingAttributesMap.get("Product");
					prodAtribs.put(CCLPConstants.PRODUCT_CURRENCY, productCurrencyList);
					existingAttributesMap.put("Product", prodAtribs);
				}

			}
			
			if (!CollectionUtils.isEmpty(redMapList)) {
				Map<String, List<Object>> productIDList = getRedemptionMapList(p.getProductId(), redMapList);
				if (existingAttributesMap.containsKey("Product")) {
					Map<String, Object> prodAtribs = existingAttributesMap.get("Product");
					prodAtribs.put(CCLPConstants.REDEMPTION_DELAY, productIDList);
					existingAttributesMap.put("Product", prodAtribs);
				}

			}
		}
		logger.info(CCLPConstants.EXIT);
	}// addNewAttribToExistingProdAttrib

	@Override
	public void updateProductAttributesValueInCache() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ObjectMapper mapper = new ObjectMapper();

		List<BigDecimal> productList = productDao.getDistinctProductId();

		try {
			if (!productList.isEmpty()) {
				for (BigDecimal productIdOut : productList) {

					Long productId = productIdOut.longValue();

					List<Object[]> prodAttributesUpdate = productDao.getprodAttributesUpdate(productId);
					String productAttributesJson = null;

					if (!prodAttributesUpdate.isEmpty()) {
						
						Product product = productDao.getProductById(productId);
						if (Objects.isNull(product)) {
							logger.error("Failed to get product for this productId: {}", productId);
							throw new ServiceException(CCLPConstants.PRODUCT_U + ResponseMessages.DOESNOT_EXISTS);
						}

						String existingAttributes = product.getAttributes();
						if (!Objects.isNull(existingAttributes)) {
							Map<String, Map<String, Object>> productAttributes =  Util.updateValuesToMapOfMap(getAllAttributesToCreateProduct(),  Util.jsonToMap(existingAttributes));
							prodAttributesUpdate.forEach(attributeListOut -> {

								Map<String, Object> innerAttribute =  productAttributes
										.get(attributeListOut[0]);
								innerAttribute.put((String) attributeListOut[1], attributeListOut[2]);

							});
							Map<String, Map<String, Object>> productAttributesMap = Util.removeEmptyValuefromMapOfMap(productAttributes);
							productAttributesJson = mapper.writeValueAsString(productAttributesMap);
							ProductDTO productDto = new ProductDTO();
							productDto.setAttributes(productAttributesJson);
							productDto.setProductId(productId);

							int updateCnt = productDao.updateProductAttributes(productDto);
							if (updateCnt == 0) {
								logger.error("Error while updating general attributes");
								throw new ServiceException(ResponseMessages.ERR_WHILE_UPDATING_GENERAL_ATTRIBUTES);
							}

							int prodUpdateCnt = productDao.updateProdAttributesFlag(productId);
							if (prodUpdateCnt == 0) {
								logger.error("Error while updating product attribute flag");
								throw new ServiceException(ResponseMessages.PRODUCT_UPDATE_FAIL);

							}
							addNewAttribToExistingProdAttrib(product, product.getProductId(), productAttributesMap);
							/**
							 * Update product attributes to cache with product id as key
							 */
							distributedCacheService.updateProductAttributesCache(productId, productAttributesMap);
							cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(productId));
						} else {
							logger.info(" prodAttributesUpdate  is Empty");

						}

					} else {
						logger.info("Error occured while try to update updateProductAttributesBulk: {}");

					}

				}
			} else {
				logger.info(" prodAttributesUpdate  is Empty");

			}
		} catch (Exception e) {
			logger.error("Error occured while try to update updateProductAttributesBulk: {}", e.getMessage());
			throw new ServiceException(ResponseMessages.EMPTY_PRODUCT_ATTRIBUTE_RETRIEVE);
		}
		logger.info(CCLPConstants.EXIT);
	}
	
	
	/*
	 * This method is used to update the product purse attributes into cache
	 */
	public void updatePurseAttributesBulk(Long productId, Product product) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		try {
			for (ProductPurse pp : product.getListProductPurse()) {
				logger.info("Product Purse Iteration List PurseId:"+pp.getPurse().getPurseId());
				Map<String, Map<String, Object>> productPurseAttributes = Util.convertJsonToHashMap(pp.getAttributes());
				String productPurseId = String.valueOf(productId + "_" + pp.getPurse().getPurseId());
				productPurseAttributes = Util.removeEmptyValuefromMapOfMap(productPurseAttributes);
				
				distributedCacheService.updateProductPurseAttributesCache(productPurseId, productPurseAttributes);
				cacheService.updateCache(CCLPConstants.PRODUCTPURSE_CACHE,String.valueOf(productPurseId));
			}
		} catch (Exception e) {
			logger.error("Error occured while try to update updateProductPurseAttributesBulk: {}", e.getMessage());
			throw new ServiceException(ResponseMessages.EMPTY_PRODUCT_ATTRIBUTE_RETRIEVE);
		}
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public Map<String, List<Object>> getInternationalCurrency(Long productId,Map<String, List<Object>> productCurrencyMap) {
		logger.info(CCLPConstants.ENTER);
		Map<String, List<Object>> mapListTemp = new HashMap<>();
		try {
			logger.info("INTL productId:" + productId);
			String inputProd = String.valueOf(productId);
			productCurrencyMap.forEach((key, value) -> {
				String getProductID = key.split("_")[0];
				if (!Util.isEmpty(inputProd) && inputProd.equals(getProductID)) {
					mapListTemp.put(key, value);
				}
			});
		} catch (Exception e) {
			logger.info("Exception while invoking getRedemptionMapList with productId:" + productId);
			logger.error(e.getMessage());
		}
		logger.info("INTL mapListTemp:" + mapListTemp);
		logger.info(CCLPConstants.EXIT);
		return mapListTemp;
	}
	

	@Override
	public Map<String, Object> getPartnerCurrency(Long partnerId) {
		logger.info(CCLPConstants.ENTER);
		Map<String, Object> supportedCurrencyMap = new HashMap<>();
		try {
			List<Object[]> supportedCurrencyList = productDao.getPartnerCurrency(partnerId);
			if (supportedCurrencyList != null && !supportedCurrencyList.isEmpty()) {
				supportedCurrencyList.stream().forEach(
						supportedCurrency -> supportedCurrencyMap.put(supportedCurrency[0] + "", supportedCurrency[1]));

			}
		} catch (Exception e) {
			logger.info("Exception in getGroupAccessProdsDetails:" + e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return supportedCurrencyMap;
	}

	@Override
	public List<CurrencyCodeDTO> getPartnerCurrencyCodes(Long partnerId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		List<CurrencyCodeDTO> currecyCodeDtoList = new ArrayList<>();
		List<Object[]> currencyCodeList = productDao.getPartnerCurrencyCodes(partnerId);
		if (currencyCodeList == null) {
			logger.info("Currency code list is null");
			throw new ServiceException(ResponseMessages.ERR_CURRENCYCODE_NULL);
		}

		currencyCodeList.stream().forEach(currencyCode -> {
			CurrencyCodeDTO dto = new CurrencyCodeDTO();
			dto = new CurrencyCodeDTO(currencyCode[0].toString(), currencyCode[1].toString(),
					currencyCode[2].toString(), currencyCode[3].toString());
			currecyCodeDtoList.add(dto);
		});
		logger.info(CCLPConstants.EXIT);
		return currecyCodeDtoList;

	}
		
		 
	/**
	 * Getting the pin attributes
	 * 
	 * @param productId
	 * @return pin Attributes as Map
	 * @throws ServiceException
	 */
	public List<Purse> getPurseByProductId(Long productId, Long parentProductId ,String attributeGroup) throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		List<Purse> purseList = null;

		 purseList = productDao.getPurseByProductId(productId,parentProductId,attributeGroup);
		logger.info(CCLPConstants.EXIT);
		return purseList;
	}


	@Override
	public void updateProductPurse(Map<String, Object> productPurseMap, Long productId, Long purseId)
			throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		try {
			ProductPurse productPurse = getPurseDetailsById(productId, purseId);
			Map<String, Map<String, Object>> productPurseAttributes = Util.jsonToMap(productPurse.getAttributes());
			productPurseMap = Util.removeEmptyValuefromMap(productPurseMap);
			productPurseAttributes.put("Purse", productPurseMap);
			String productPurseAttributesString = new ObjectMapper().writeValueAsString(productPurseAttributes);

			int updateCnt = productDao.updateProductPurseAttributes(productPurseAttributesString, productId, purseId);
			if (updateCnt == 0) {
				logger.error("Error while updating Product Purse Parameters");
				throw new ServiceException(ResponseMessages.ERR_WHILE_UPDATING_PIN_ATTRIBUTES);
			}
		
			distributedCacheService.updateProductPurseAttributesCache(String.valueOf(productId + "_" + purseId),
					productPurseAttributes);
			cacheService.updateCache(CCLPConstants.PRODUCTPURSE_CACHE, String.valueOf(productId + "_" + purseId));
		} catch (IOException e) {
			logger.error("Error while parsing attributes: {}", e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_WHILE_PARSING_ATTRIBUTES);
		}
		logger.info(CCLPConstants.EXIT);

	}

	public ProductPurse getPurseDetailsById(long productId, long purseId) {
		logger.info(CCLPConstants.ENTER);

		ProductPurse productPurse = productDao.getProductPurseById(productId, purseId);
		if (Objects.isNull(productPurse)) {
			productPurse = new ProductPurse();
		}
		logger.info(CCLPConstants.EXIT);
		return productPurse;
	}

	@Override
	public Map<String, Object> getProductPurse(Long productId, Long purseId, String source)
			throws ServiceException, IOException {

		logger.info(CCLPConstants.ENTER);
		Map<String, Object> productPurseMap = null;
		Map<String, Object> purseTypeMap = new HashMap<>();

		ProductPurse productPurse = getPurseDetailsById(productId, purseId);
		if (productPurse != null) {

			Map<String, Map<String, Object>> productPurseAttributes = Util.jsonToMap(productPurse.getAttributes());
			productPurseMap = productPurseAttributes.get("Purse");
			if (CCLPConstants.FROM_CACHE.equalsIgnoreCase(source))
			{
				updateCacheValueAsynchronously(productPurseAttributes, CCLPConstants.PRODUCT_PURSE_ATTRIBUTUES,
						productId, purseId);
			}
			purseTypeMap.put("purseTypeName", productPurse.getPurse().getPurseType().getPurseTypeName());
			purseTypeMap.put("minorUnits", productPurse.getPurse().getCurrencyCode().getMinorUnits());
		}
		productPurseMap = Util.updateValuesToMap(getAllAttributesToCreateProductPurse().get("Purse"), productPurseMap);
		productPurseMap.putAll(purseTypeMap);
		logger.info(CCLPConstants.EXIT);
		return productPurseMap;
	}

	/* To make a call asynchronously in a new thread to set the product purse value into Cache.
	 * 
	 */
	public void updateCacheValueAsynchronously(Map<String, Map<String, Object>> attributes, String attributesFlag,
			long productId, long purseId) {
		logger.info(CCLPConstants.ENTER);
		try {
			if (CCLPConstants.PRODUCT_ATTRIBUTUES.equalsIgnoreCase(attributesFlag)) {
				logger.info("Set product attributes Cache");
				distributedCacheService.updateProductAttributesCache(productId, attributes);
				cacheService.updateCache(CCLPConstants.PRODUCT_CACHE,String.valueOf(productId));
			} else if (CCLPConstants.PRODUCT_PURSE_ATTRIBUTUES.equalsIgnoreCase(attributesFlag)) {
				logger.info("Set product Purse attrubutes Cache");
				distributedCacheService.updateProductPurseAttributesCache(String.valueOf(productId + "_" + purseId),
						attributes);
				cacheService.updateCache(CCLPConstants.PRODUCTPURSE_CACHE,String.valueOf(productId + "_" + purseId));
			}
		} catch (Exception ex) {
			logger.error("Error Occured while setting the produt/purse attributues"+ex.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
	}
	
	
	/*/
	 * This method is used to update the purse Attributes value in Product Purse table
	 */
	@Override
	public void updatePurseAttributesValueInCache() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		try {
			List<BigDecimal> productPurseIdList = productDao.getDistinctProductPurseId();
			if (!productPurseIdList.isEmpty()) {
				for (BigDecimal productIdOut : productPurseIdList) {
					Long productId = productIdOut.longValue();
					List<Object[]> prodPurseAttributesUpdate = productDao.getprodPurseAttributesUpdate(productId);
					updatePurseAttributues(prodPurseAttributesUpdate, productId);
				}

			} else {
				logger.info(" productPurseIdList  is Empty");
			}

		} catch (Exception ex) {
			logger.error("Error occured while try to update updatePurseAttributesValueInCache: {}", ex.getMessage());
			throw new ServiceException(ResponseMessages.EMPTY_PRODUCT_ATTRIBUTE_RETRIEVE);
		}
		logger.info(CCLPConstants.EXIT);
	}
	
	public void updatePurseAttributues(List<Object[]> prodPurseAttributesUpdate, Long productId)
			throws ServiceException {
		try {
			if (!prodPurseAttributesUpdate.isEmpty()) {
					prodPurseAttributesUpdate.forEach(attributeListOut -> {
					String purseAttributes = "";
					Long purseId = Long.parseLong(String.valueOf(attributeListOut[0]));
					String productpurse = productId+"_"+purseId;
					logger.info("productId-purseId" + productpurse);
					ProductPurse productPurse = productDao.getProductPurseAttributes(productId, purseId);
					Map<String, Map<String, Object>> productPurseAttributes = new HashMap<>();
					if (!Util.isEmpty(productPurse.getAttributes())) {
						try {
							productPurseAttributes = Util.jsonToMap(productPurse.getAttributes());
						} catch (Exception e) {
							logger.error("Error While processing Product Purse attributues" + e.getMessage());
						}
					}
					Map<String, Object> innerAttribute = productPurseAttributes.get(attributeListOut[1]);
					if(innerAttribute != null) {
					innerAttribute.put((String) attributeListOut[2], attributeListOut[3]);
					innerAttribute = Util.updateValuesToMap(getAllAttributesToCreateProductPurse().get(attributeListOut[1]), innerAttribute);
					innerAttribute = Util.removeEmptyValuefromMap(innerAttribute);
					}
					
					purseAttributes = convertMaptoString(productPurseAttributes);

					productDao.updateProductPurseAttributes(purseAttributes, productId, purseId);
					int count = productDao.updateProdPurseAttributesFlag(productId, purseId,
							(String) attributeListOut[1], (String) attributeListOut[2]);
					if (count == 0) {
						logger.error("Error while updating product attribute flag");
					}
					distributedCacheService.updateProductPurseAttributesCache(productpurse, productPurseAttributes);
					cacheService.updateCache(CCLPConstants.PRODUCTPURSE_CACHE,String.valueOf(productpurse));
				});
			}
		} catch (Exception ex) {

			logger.error("Error occured while try to update updatePurseAttributesValueInCache: {}", ex.getMessage());
			throw new ServiceException(ResponseMessages.EMPTY_PRODUCT_ATTRIBUTE_RETRIEVE);
		}
	}
	
	public String convertMaptoString(Map<String, Map<String, Object>> productPurseAttributes) {
		ObjectMapper mapper = new ObjectMapper();
		String purseAttributes = "";
		try {
			purseAttributes = mapper.writeValueAsString(productPurseAttributes);
		} catch (JsonProcessingException e) {
			logger.info(" Parsing Exception while converting into String");
}
		return purseAttributes;
	}
	@Override
	public void removeNullFromAttributes() throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		List<Product> productList = productDao.getAllProducts();
		logger.info("Product List size is {}", productList.size());
		try {
			if (!CollectionUtils.isEmpty(productList)) {
				for (Product product : productList) {

					String productAttributesJson = null;
					Map<String, Map<String, Object>> productAttributes;
					productAttributesJson = product.getAttributes();

					if (!Util.isEmpty(productAttributesJson)) {
						productAttributes = Util.jsonToMap(productAttributesJson);
					}else {
						continue;
					}
					productAttributes.remove(CCLPConstants.PRODUCT_TAB_LIMITS);
					productAttributes.remove(CCLPConstants.PRODUCT_TAB_MONTHLY_FEE_CAP);
					productAttributes.remove(CCLPConstants.PRODUCT_TAB_TRANSACTION_FEES);
					productAttributes.remove(CCLPConstants.PRODUCT_TAB_PURSE);
					productAttributes = Util.removeEmptyValuefromMapOfMap(productAttributes);
					
					productDao.updateProductAttributes(Util.convertHashMapToJson(productAttributes), product.getProductId());
					removeNullFromProductPurse(product);
				}
			} else {
				logger.info(" Product List is Empty");

			}
		} catch (Exception e) {
			logger.error("Error occured while try to update updateProductAttributesBulk: {}", e.getMessage());
			throw new ServiceException(ResponseMessages.EMPTY_PRODUCT_ATTRIBUTE_RETRIEVE);
		}
		logger.info(CCLPConstants.EXIT);

	}
	
	public void removeNullFromProductPurse(Product product) throws ServiceException {
		List<ProductPurse> productPurses = product.getListProductPurse();
		try {
			if (!CollectionUtils.isEmpty(productPurses)) {

				for (ProductPurse productPurse : productPurses) {

					String purseAttributesJson = null;
					Map<String, Map<String, Object>> purseAttributes = null;
					purseAttributesJson = productPurse.getAttributes();
					if (!Util.isEmpty(purseAttributesJson)) {
						purseAttributes = Util.jsonToMap(purseAttributesJson);
					}else {
						purseAttributes = getAllAttributesToCreateProductPurse();
					}
						
					purseAttributes = Util.removeEmptyValuefromMapOfMap(purseAttributes);
					productDao.updateProductPurseAttributes(Util.convertHashMapToJson(purseAttributes),
							product.getProductId(), productPurse.getPurse().getPurseId());

				}
			}
		} catch (Exception e) {
			logger.error("Error occured while try to update updateProductAttributesBulk: {}", e.getMessage());
			throw new ServiceException(ResponseMessages.EMPTY_PRODUCT_ATTRIBUTE_RETRIEVE);
		}
	}
	
	@Override
	public void updateBumpUpConfig() throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		List<Product> productList = productDao.getAllProducts();
		logger.info("Product List size is {}", productList.size());
		try {
			if (!CollectionUtils.isEmpty(productList)) {
				for (Product product : productList) {

					String productAttributesJson = null;
					Map<String, Map<String, Object>> productAttributes;
					productAttributesJson = product.getAttributes();

					if (!Util.isEmpty(productAttributesJson)) {
						productAttributes = Util.jsonToMap(productAttributesJson);
					} else {
						continue;
					}
					if (CollectionUtils.isEmpty(productAttributes)) {
						continue;
					}

					Map<String, Object> generalAttributes = productAttributes.get(CCLPConstants.PRODUCT_TAB_GENERAL);
					if (!CollectionUtils.isEmpty(generalAttributes)) {

						String usdBumpPump = String.valueOf(generalAttributes.get(CCLPConstants.USD_ONE_BUMP_PUMP_TXN));
						if (Util.isEmpty(usdBumpPump) || "null".equalsIgnoreCase(usdBumpPump)) {
							generalAttributes.put(CCLPConstants.USD_ONE_BUMP_PUMP_TXN, CCLPConstants.FALSE);
						} else {
							generalAttributes.put(CCLPConstants.USD_ONE_BUMP_PUMP_TXN, usdBumpPump);
						}
						productAttributes.put(CCLPConstants.PRODUCT_TAB_GENERAL, generalAttributes);
					}
					productDao.updateProductAttributes(Util.convertHashMapToJson(productAttributes), product.getProductId());
				}
			} else {
				logger.info(" Product List is Empty");

			}
		} catch (Exception e) {
			logger.error("Error occured while try to update split updateBumpUpConfig: {}", e);
			throw new ServiceException(ResponseMessages.EMPTY_PRODUCT_ATTRIBUTE_RETRIEVE);
		}
		logger.info(CCLPConstants.EXIT);

	}
	
}
