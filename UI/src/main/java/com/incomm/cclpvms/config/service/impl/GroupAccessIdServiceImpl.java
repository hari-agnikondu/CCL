package com.incomm.cclpvms.config.service.impl;


import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.incomm.cclpvms.config.model.GroupAccess;
import com.incomm.cclpvms.config.model.GroupAccessDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.GroupAccessService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class GroupAccessIdServiceImpl implements GroupAccessService {
	
	Logger logger = LogManager.getLogger(GroupAccessIdServiceImpl.class);
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${CONFIG_BASE_URL}") 
	String config_base_url;
	
	String groupAccessBaseURL="/groupAccess";
	
	@Autowired
	SessionService sessionService;
	/** 
	 * This Method retrieves Group Access ID's  based on the input from the user.
	 */

	public List<GroupAccess> getGroupAccessIds(GroupAccess groupAccess)throws ServiceException{
		List<GroupAccess> groupAccessList=null;
		String tempurl=config_base_url + "/groupAccess";
		logger.debug(CCLPConstants.ENTER);

		try {
			logger.debug("Calling '{}' service for Search Card Range",config_base_url);
			String groupAccessName=null; 
			if(groupAccess.getGroupAccessName()==null || groupAccess.getGroupAccessName().trim().isEmpty()){
				groupAccessName="*";
			}
			else{
				groupAccessName=groupAccess.getGroupAccessName();
			}
			String productName=null; 
			if(groupAccess.getProductName()==null || groupAccess.getProductName().trim().isEmpty()){
				productName="*";
			}
			else{
				productName=groupAccess.getProductName();
			}
			
			
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromUriString(tempurl+"/search")
					.queryParam("groupAccessName",groupAccessName)
					.queryParam("productName", productName);

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
					ResponseDTO.class);			  
			ResponseDTO responseBody  = responseEntity.getBody();
			if(responseBody!=null && responseBody.getData()!=null) {
				ModelMapper mm = new ModelMapper();
				groupAccessList= mm.map(responseBody.getData(),new TypeToken<List<GroupAccess>>() {}.getType());
				
			}
			logger.debug(CCLPConstants.EXIT);
			return groupAccessList;
		} 
		catch (RestClientException e) {
			logger.error("RestClientException in getGroupAccessIds:"+e);
			throw new ServiceException(ResponseMessages.SERVER_DOWN);
		}

	}
	
	/** 
	 * This Method retrieves Group AccessNames List.
	 */
			
	public List<GroupAccessDTO> getGroupAccess() throws ServiceException {			
		logger.debug(CCLPConstants.ENTER);
		
		List<GroupAccessDTO> groupAccessNameList=null;
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(config_base_url + groupAccessBaseURL,
				ResponseDTO.class);			  
		ResponseDTO responseBody  = responseEntity.getBody();
		
		if(responseBody!=null && responseBody.getData()!=null) {	
			ModelMapper mm = new ModelMapper();
			groupAccessNameList= mm.map(responseBody.getData(),new TypeToken<List<GroupAccessDTO>>() {}.getType());		
		}
		
		logger.debug(CCLPConstants.EXIT);
		return groupAccessNameList;
	
	}
	
	/** 
	 * This Method retrieves GroupAccessProducts Based on  AccessId And ProductId.
	 */
	public List<GroupAccessDTO> getGroupAccessProductsByAccessIdAndProductId(Long groupAccessId,Long productId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		
		List<GroupAccessDTO> groupAccessNameList=null;
		ModelMapper mm = new ModelMapper();
		ResponseDTO responseDTO=restTemplate.getForObject(config_base_url +groupAccessBaseURL+ "/"+groupAccessId+ "/"+productId, ResponseDTO.class);
		if(responseDTO!=null && responseDTO.getData()!=null){
		groupAccessNameList= mm.map(responseDTO.getData(),new TypeToken<List<GroupAccessDTO>>() {}.getType());
		}

	logger.debug(CCLPConstants.EXIT);
	return groupAccessNameList;
}
	/** 
	 * This Method retrieves GroupAccessPartners Based on GroupAccessID
	 */
public List<GroupAccessDTO> getGroupAccessPartnersByAccessID(Long groupAccessId) throws ServiceException {		
	List<GroupAccessDTO> groupAccessPartnersList=null;

	logger.debug(CCLPConstants.ENTER);

	ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(config_base_url + groupAccessBaseURL+ "/"+groupAccessId+"/partners",
			ResponseDTO.class);			  
	ResponseDTO responseBody  = responseEntity.getBody();
	
	if(responseBody!=null && responseBody.getData()!=null) {
		
		ModelMapper mm = new ModelMapper();
	
		groupAccessPartnersList= mm.map(responseBody.getData(),new TypeToken<List<GroupAccessDTO>>() {}.getType());
		
	}
	logger.debug(CCLPConstants.EXIT);
	return groupAccessPartnersList;

}	
	/**
	 * This Service calls the restTemplate to addGroupAccess into the database.
	 */
	public ResponseDTO addGroupAccess(GroupAccess groupAccess) throws ServiceException  {
		ResponseDTO responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			logger.debug("Calling '{}' service to create new addGroupAccess",config_base_url);
			ModelMapper mm = new ModelMapper();
			GroupAccessDTO groupAccessDTO = mm.map(groupAccess, GroupAccessDTO.class);	
			groupAccessDTO.setPartnerList(groupAccess.getSelectedPartnerList());
			groupAccessDTO.setInsUser(sessionService.getUserId());
			groupAccessDTO.setLastUpdUser(sessionService.getUserId());
			responseDTO=restTemplate.postForObject(config_base_url + groupAccessBaseURL+ "/partners",groupAccessDTO, ResponseDTO.class); 
			
		} 
		catch (RestClientException e) {
			logger.error("Error while creating new GroupAccess:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}
	
	
	
	/**
	 * This Service Method calls the restTemplate to update the Card range details entered by the user.
	 */
	public ResponseEntity<ResponseDTO> updateGroupAccess(GroupAccess groupAccess) throws ServiceException{
		ResponseEntity<ResponseDTO> responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			ModelMapper mm = new ModelMapper();
			GroupAccessDTO groupAccessDTO = mm.map(groupAccess, GroupAccessDTO.class);
			groupAccessDTO.setPartnerList(groupAccess.getSelectedPartnerList());
			groupAccessDTO.setGroupAccessId(groupAccess.getGroupAccessId());
			groupAccessDTO.setGroupAccessName(groupAccess.getGroupAccessName());
			groupAccessDTO.setInsUser(sessionService.getUserId());
			groupAccessDTO.setLastUpdUser(sessionService.getUserId());
	
			logger.debug("Calling '{}' service to update Card Range",config_base_url);
			
			 responseDTO = restTemplate.exchange(config_base_url + groupAccessBaseURL+ "/partners",
					HttpMethod.PUT,new HttpEntity<GroupAccessDTO>(groupAccessDTO), ResponseDTO.class);
			 
		}
		catch (RestClientException e) {
			logger.error("RestClientException in updateGroupAccess:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 

		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
		
	}

	/**
	 * This Service calls the restTemplate addAssignGroupAccessIdToProduct into the database.
	 */

	@Override
	public ResponseDTO addAssignGroupAccessIdToProduct(GroupAccess groupAccess) throws ServiceException {
	
		ResponseDTO responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			logger.debug("Calling '{}' service to create new GroupAccessIdToProduct",config_base_url);

			ModelMapper mm = new ModelMapper();
			GroupAccessDTO groupAccessDTO = mm.map(groupAccess, GroupAccessDTO.class);	
			groupAccessDTO.setInsUser(sessionService.getUserId());
			groupAccessDTO.setLastUpdUser(sessionService.getUserId());
			responseDTO=restTemplate.postForObject(config_base_url + groupAccessBaseURL+ "/products", groupAccessDTO, ResponseDTO.class); 
			
		} 
		catch (RestClientException e) {
			logger.error("Error while creating new AssignGroupAccessIdToProduct:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}

	/** 
	 * This Method used to updateGroupAccessProducts
	 */
	@Override
	public ResponseDTO updateAssignGroupAccessToProduct(GroupAccess groupAccess) throws ServiceException {
		
		ResponseDTO responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			logger.debug("Calling '{}' service to create new addGroupAccess",config_base_url);			
			ModelMapper mm = new ModelMapper();
			GroupAccessDTO groupAccessDTO = mm.map(groupAccess, GroupAccessDTO.class);	
			groupAccessDTO.setPartnerList(groupAccess.getSelectedPartnerList());
			groupAccessDTO.setInsUser(sessionService.getUserId());
			groupAccessDTO.setLastUpdUser(sessionService.getUserId());
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(config_base_url + groupAccessBaseURL+ "/products",
						HttpMethod.PUT,new HttpEntity<GroupAccessDTO>(groupAccessDTO), ResponseDTO.class);
			responseDTO=responseEntity.getBody();		
		} 
		catch (RestClientException e) {
			logger.error("Error while creating new GroupAccess:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}

	/** 
	 * This Method used to get the ProductsBy AccessId
	 */
	public Map<String,String>  getProductsByAccessId(Long groupAccessId) throws ServiceException {		

		ModelMapper mm = new ModelMapper();
		ResponseDTO responseBody = null;
		Map<String, String> productMap = null;

		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(config_base_url + groupAccessBaseURL+ "/"+groupAccessId+"/products",
					ResponseDTO.class);	
			responseBody = responseEntity.getBody();

			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				@SuppressWarnings("unchecked")
				Map<String, String> parentProductMap = mm.map(responseBody.getData(), Map.class);
				productMap = parentProductMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
						.collect(Collectors.toMap(e -> e.getKey(), e -> e.getKey()+":"+e.getValue(),(e1, e2) -> e1,
                                LinkedHashMap::new));
				logger.debug(parentProductMap);
			} else
				logger.error("Failed to Fetch LimitAttributes from config srvice");
		} catch (RestClientException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return productMap;

	}
	
	
	/** 
	 * This Method used to get the GroupAccessPartners ByAccessIDAndProductId
	 */
public List<GroupAccessDTO> getGroupAccessPartnersByAccessIDAndProductId(Long groupAccessId,Long productId) throws ServiceException {		
	List<GroupAccessDTO> groupAccessNameList=null;

	logger.debug(CCLPConstants.ENTER);

	ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(config_base_url + groupAccessBaseURL+ "/"+groupAccessId+"/partners"+"/"+productId,
			ResponseDTO.class);			  
	ResponseDTO responseBody  = responseEntity.getBody();
	
	if(responseBody!=null && responseBody.getData()!=null) {
		
		ModelMapper mm = new ModelMapper();
	
		groupAccessNameList= mm.map(responseBody.getData(),new TypeToken<List<GroupAccessDTO>>() {}.getType());
		
	}
	logger.debug(CCLPConstants.EXIT);
	return groupAccessNameList;

}
	

/**
 *This Method Delete the card range by the selection.
 */
public ResponseEntity<ResponseDTO> deleteGroupAccess(Long groupAccessId, Long productId) throws ServiceException {
	ResponseEntity<ResponseDTO> responseDTO=null;
	logger.debug(CCLPConstants.ENTER);
	
	try {
		GroupAccessDTO groupAccessDTO=new GroupAccessDTO();
		groupAccessDTO.setGroupAccessId(groupAccessId);
		groupAccessDTO.setProductId(productId);
		 responseDTO = restTemplate.exchange(config_base_url + groupAccessBaseURL + "/"+groupAccessId+"/"+productId,HttpMethod.DELETE,new HttpEntity<GroupAccessDTO>(groupAccessDTO), ResponseDTO.class);
	}//try
	catch (RestClientException e) {
		logger.error("RestClientException in deleteCardRange:"+e);
		throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
	} 
	logger.debug(CCLPConstants.EXIT);
	return responseDTO;
}

/** 
 * This Method used to get the  GroupAccessProducts ByAccessId
 */
		
public List<GroupAccessDTO> getGroupAccessProductsByAccessId(Long groupAccessId) throws ServiceException {
	logger.debug(CCLPConstants.ENTER);

	List<GroupAccessDTO> groupAccessNameList=null;
	ModelMapper mm = new ModelMapper();
	ResponseDTO responseDTO=restTemplate.getForObject(config_base_url +groupAccessBaseURL+"/"+groupAccessId+"/groupAccessProducts", ResponseDTO.class);
	if(responseDTO!=null && responseDTO.getData()!=null){
		groupAccessNameList= mm.map(responseDTO.getData(),new TypeToken<List<GroupAccessDTO>>() {}.getType());
	}
	logger.debug(CCLPConstants.EXIT);
	return groupAccessNameList;
}



}