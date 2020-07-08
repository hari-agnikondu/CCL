package com.incomm.cclp.service.impl;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hazelcast.util.CollectionUtil;
import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.CustomerProfileDAO;
import com.incomm.cclp.dto.CustomerProfileDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.AttributeDefinitionService;
import com.incomm.cclp.service.CustomerProfileService;
import com.incomm.cclp.util.Util;

@Service
public class CustomerProfileServiceImpl implements CustomerProfileService {

	
	private static final Logger logger = LogManager.getLogger(CustomerProfileServiceImpl.class);
	
	@Autowired
	LocalCacheServiceImpl localCacheServiceImpl;
	
	@Autowired
	AttributeDefinitionService attributeDefinitionService;
	
	@Autowired
	CustomerProfileDAO customerProfileDao;
	
	@Override
	public void createCustomerProfile(CustomerProfileDTO customerProfileDto) throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		logger.info(customerProfileDto.toString());
		
		int prflCodeCnt = 0;
		String proxyNumber = customerProfileDto.getProxyNumber();
		//check profile code already exist
		if(proxyNumber != null) {
			
			 prflCodeCnt = customerProfileDao.checkExistingProfileCode(proxyNumber);
			
		}
		if(prflCodeCnt > 0) {
			logger.error("Customer profile already exists");
			 throw new ServiceException(ResponseMessages.ERR_CUSTOMER_PROFILE_EXISTS,ResponseMessages.ALREADY_EXISTS);
		}
		
		// Set attributes
		Map<String, Map<String, Object>> attributesMap = getAttributesToCreateCustomerProfile(customerProfileDto.getAttributesMap());
				
		String attributes = Util.convertHashMapToJson(attributesMap);
				
		int count = customerProfileDao.createCustomerProfile(customerProfileDto,attributes);
		if(count <=0) {
			logger.error("Error while creating customer profile");
			throw new ServiceException(ResponseMessages.ERR_CUSTOMER_PROFILE_CREATE,ResponseMessages.DOESNOT_EXISTS);
		}
		logger.info(CCLPConstants.EXIT);
	}

	private Map<String, Map<String, Object>> getAttributesToCreateCustomerProfile(
			Map<String, Map<String, Object>> cardAttributesMap) {
		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String, Object>> defaultAttributes = null;
		defaultAttributes = localCacheServiceImpl.getCardAttributeDefinition(null);

		logger.debug("LOCAL Cache Attribute value : " + defaultAttributes);

		// add to local cache if cache empty
		if (CollectionUtils.isEmpty(defaultAttributes)) {
			logger.info("LOCAL Cache is empty : " + defaultAttributes);

			defaultAttributes = attributeDefinitionService.getAllCardAttributeDefinitions();
			
			localCacheServiceImpl.getCardAttributeDefinition(defaultAttributes);
		}

		if (!CollectionUtils.isEmpty(defaultAttributes)) {
			// Copy Product attributes to default attribute map
			Map<String, Object> cardAttribute = null;
			if (!CollectionUtils.isEmpty(cardAttributesMap) && cardAttributesMap.containsKey("Card")) {
				cardAttribute = cardAttributesMap.get("Card");
				updateProductAttributes(defaultAttributes, cardAttribute, "Card");
			}
		}
		logger.info("Final attribute Map :" + defaultAttributes.toString());
		logger.info(CCLPConstants.EXIT);
		return defaultAttributes;
	}
	
	
	private void updateProductAttributes(Map<String, Map<String, Object>> defaultAttributes,
			Map<String, Object> cardAttributes, String attributeGroupName) {
		logger.info(CCLPConstants.ENTER);
		if (defaultAttributes.containsKey(attributeGroupName)) {
			
			Map<String, Object> defaultCardAttributes = defaultAttributes.get(attributeGroupName);
			if (!defaultCardAttributes.isEmpty() && !cardAttributes.isEmpty()) {
				for (Map.Entry<String, Object> entry : cardAttributes.entrySet()) {
					if (defaultCardAttributes.containsKey(entry.getKey())) {
						defaultCardAttributes.replace(entry.getKey(), entry.getValue());
					}
				}
			}
			defaultAttributes.replace(attributeGroupName, defaultCardAttributes);
		} else {
			defaultAttributes.put(attributeGroupName, cardAttributes);
		}
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public CustomerProfileDTO getCustomerProfileById(Long profileId) {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper(); 
		@SuppressWarnings("unchecked")
		List<Object> customerProfile = mm.map(customerProfileDao.getCustomerProfileById(profileId),List.class);
		String attributesStr = "";
		Map<String, Map<String, Object>> attributesMap;
		
		logger.info("customer profile details: {}",customerProfile);
		
		CustomerProfileDTO customerProfileDto = new CustomerProfileDTO();
		
		customerProfileDto.setCardNumber(String.valueOf(customerProfile.get(0)));
		customerProfileDto.setProxyNumber(String.valueOf(customerProfile.get(1)));
		customerProfileDto.setAccountNumber(String.valueOf(customerProfile.get(2)));
		
		
		 Clob clob =(Clob) customerProfile.get(3);
	      // materialize CLOB onto client
	      
		try {
			attributesStr = clob.getSubString(1, (int) clob.length());
		} catch (SQLException e) {
			logger.error("Error Occured : {}",e.getMessage());
		}
				
		logger.info("customer profile : {}",attributesStr);
		
		attributesMap = Util.convertJsonToHashMap(attributesStr);
		customerProfileDto.setAttributesMap(attributesMap);
		logger.info(CCLPConstants.EXIT);
		return customerProfileDto;

		
	}

	/*This Function retrieves all customer profiles for SEARCH Screen*/
	@Override
	public List<CustomerProfileDTO> getCustomerProfiles(String accountNumber, String cardNumber, String proxyNumber) {
		logger.info(CCLPConstants.ENTER);
		List<Object[]> customerProfiles = customerProfileDao.getCustomerProfiles(accountNumber,cardNumber,proxyNumber);
		
		List<CustomerProfileDTO> customerProfileList= new ArrayList<>();
		
		if(!CollectionUtil.isEmpty(customerProfiles)) {
			logger.debug("Customer profiles : {}",customerProfiles);
			for(Object[] custPrflObj : customerProfiles) {
				CustomerProfileDTO tempCustPrfl = new CustomerProfileDTO();
				if(custPrflObj[0] != null ) {
				tempCustPrfl.setProfileId(Long.parseLong(String.valueOf(custPrflObj[0])));
				}
				
				tempCustPrfl.setAccountNumber(String.valueOf(custPrflObj[1]));
				tempCustPrfl.setProxyNumber(String.valueOf(custPrflObj[2]));
				tempCustPrfl.setCardNumber(String.valueOf(custPrflObj[3]));
				customerProfileList.add(tempCustPrfl);
			}
			
		}
		logger.info(CCLPConstants.EXIT);
		return customerProfileList;
		
		
	}
	
	@Override
	public void deleteCustomerProfileById(Long profileId) throws ServiceException {
	
		logger.info(CCLPConstants.ENTER);
		int deleteCount = customerProfileDao.deleteCustomerProfileById(profileId);
		if(deleteCount<=0)
			throw new ServiceException(ResponseMessages.CUSTOMER_PROFILE_DELETE_FAIL,ResponseMessages.FAILURE );
		
		logger.info("Number of Customer Profiles deleted {} " , deleteCount);
		logger.info(CCLPConstants.EXIT);
	}
	


	
		/*
		 * Product Limits and Fees are Started here
		 * 
		 * This Method will update the Fees or Limits Attributes based on the request we are receiving
		 */

		@Override
		public int updateFeeLimitAttributes(Map<String, Object> inputAttributes, Long profileId, String attributesGroup) {

			logger.info(CCLPConstants.ENTER);
			
			int count = 0;
			ModelMapper mm = new ModelMapper(); 
			
			@SuppressWarnings("unchecked")
			List<Object> customerProfile = mm.map(customerProfileDao.getCustomerProfileById(profileId),List.class);
			String attributesStr = "";
			Map<String, Map<String, Object>> cardAttributes = new HashMap<>();
			
			if ((customerProfile.get(3) != null)) {
			 Clob clob =(Clob) customerProfile.get(3);
		      // materialize CLOB onto client
		      
			try {
				attributesStr = clob.getSubString(1, (int) clob.length());
			} catch (SQLException e) {
				logger.error("Error Occured : {}",e.getMessage());
			}
					
			logger.info("customer profile is : {}",attributesStr);
			
			cardAttributes = Util.convertJsonToHashMap(attributesStr);
			
	
				Map<String, Object> attributes = cardAttributes.get(attributesGroup);

				if (CollectionUtils.isEmpty(attributes)) {

					logger.debug("{} attributes in table is empty, " + "copy attributes to table inputAttributes -> {}",
							attributesGroup, inputAttributes);
					cardAttributes.put(attributesGroup, inputAttributes);
					
				} else {		
					
					logger.debug("{} attributes to be stored in table Attributes -> ",attributesGroup,attributes);
					cardAttributes.put(attributesGroup, attributes);
					inputAttributes.entrySet().stream().filter(p ->p.getValue() == null)
					   .forEach(map -> map.setValue(""));
				
					
					/******* update the Attributes in db with inputAttributes ******/
					attributes.entrySet().stream().forEach(p ->	
						p.setValue(inputAttributes.entrySet().stream().filter(e -> e.getKey().equals(p.getKey()))
								.map(Map.Entry::getValue).findAny().orElse(p.getValue()))
					);
				}
			} else {
				logger.info("ProductAttribute is empty, put all the inputAttributes");
				cardAttributes.put(attributesGroup, inputAttributes);
			}
			String attributesJsonResp=null;
			try {
				attributesJsonResp = Util.mapToJson(cardAttributes);
			} catch (Exception e) {
				logger.error(e);
				logger.info(CCLPConstants.EXIT);
				return 0;
			}
			logger.debug("Put resulted card attributes into table profileId: {}, attributes: {}", profileId,
					attributesJsonResp);
			
			try {
				count = customerProfileDao.updateCardAttributes(attributesJsonResp, profileId);
				
				if (count == 1) {
					/**
					 * Update product attributes to cache with product id as key
					 */
					logger.debug("distributedCacheService.updateProductAttributes");
					
				}
				
			} catch (Exception e) {
				logger.error(e);
				logger.info(CCLPConstants.EXIT);
				return 0;
			}
			
			logger.info(CCLPConstants.EXIT);
		
			return count;
		}

		/*
		 *This method will retrieve all the attributes from present and parent products
		 * 
		 */
		@Override
		public Map<String, Object> getCardFeeLimitsById(Long profileId,String attributesGroup) {
				
			logger.debug(CCLPConstants.ENTER);
			ModelMapper mm = new ModelMapper(); 
			
			@SuppressWarnings("unchecked")
			List<Object> customerProfile = mm.map(customerProfileDao.getCustomerProfileById(profileId),List.class);
			String attributesStr = "";
			Map<String, Map<String, Object>> cardAttributes = null;
			
			if ((customerProfile.get(3) != null)) {
			 Clob clob =(Clob) customerProfile.get(3);
		      // materialize CLOB onto client
		      
			try {
				attributesStr = clob.getSubString(1, (int) clob.length());
			} catch (SQLException e) {
				logger.error("Error Occured : {} ",e.getMessage());
			}
					
			logger.info("customer profile : {}",attributesStr);
			
			cardAttributes = Util.convertJsonToHashMap(attributesStr);
			
	
				Map<String, Object> attributes = cardAttributes.get(attributesGroup);

			    if(!CollectionUtils.isEmpty(attributes)) {
			    	logger.debug("Successfully retrieved {} attributes from table productAttributes: {}",attributesGroup,attributes);
			    	logger.info(CCLPConstants.EXIT);
			    	return attributes; 
			    }else {
			    	logger.debug("failed to retrieve {} attributes from table",attributesGroup);
			    	logger.info(CCLPConstants.EXIT);
			    	return null;
			    }	
			}else {
				logger.info("Failed to retrive attributes from table");
				logger.info(CCLPConstants.EXIT);
				return null;
			}
		}

		@Override
		public Map<String, Object> getCardFeeLimitsByType(String type, String value, String attributeGrp) throws ServiceException {

			logger.info(CCLPConstants.ENTER);
			Map<String, Object> attributes = null;
			Map<String, Map<String, Object>> cardAttributes = null;
			String attributesStr = "";
			Object attributesObj = customerProfileDao.getAttributesByType(type,value,attributeGrp);
			
			
			 Clob clob =(Clob) attributesObj;
		      // materialize CLOB onto client
		      
			try {
				if(clob != null ) {
					attributesStr = clob.getSubString(1, (int) clob.length());
				}else {
					logger.error("{} Profile not found for {}: {}",attributeGrp,type,value);
					throw new ServiceException(ResponseMessages.ERR_NO_ATTRIBUTES_FOUND,ResponseMessages.DOESNOT_EXISTS);
				}
				
			} catch (SQLException e) {
				logger.error("Error Occured : {} ",e.getMessage());
			}
			
			logger.debug("Attributes Map " + attributesStr);
			
			cardAttributes = Util.convertJsonToHashMap(attributesStr);
			
			attributes = cardAttributes.get(attributeGrp);
			logger.info(CCLPConstants.EXIT);		
			return attributes;
		}
		
		@Override
		public List<Object[]> getCardsByAccountNumber(String accountNumber) {
		
			return customerProfileDao.getCardsByAccountNumber(accountNumber);	
			
		}
		
	}
	
