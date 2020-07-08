package com.incomm.cclpvms.config.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.incomm.cclpvms.config.model.CardRange;
import com.incomm.cclpvms.config.model.CardRangeDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.CardRangeService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class CardRangeServiceImpl implements CardRangeService{

	Logger logger = LogManager.getLogger(CardRangeServiceImpl.class);

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${CONFIG_BASE_URL}") 
	String config_base_url;
	
	@Autowired
	SessionService sessionService;
	
	String cardRangeBaseURL="/cardRanges";

	/*
	 * This Service calls the restTemplate to add the card range into the database.
	 */
	public ResponseDTO addCardRange(CardRange cardRange) throws ServiceException  {
		ResponseDTO responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			logger.debug("Calling '{}' service to create new Card Range",config_base_url);

			ModelMapper mm = new ModelMapper();
			CardRangeDTO cardRangeDTO = mm.map(cardRange, CardRangeDTO.class);
			cardRangeDTO.setStatus(ResponseMessages.NEW_STATUS);
			cardRangeDTO.setInsUser(sessionService.getUserId());
			cardRangeDTO.setLastUpdUser(sessionService.getUserId());
			responseDTO=restTemplate.postForObject(config_base_url + cardRangeBaseURL, cardRangeDTO, ResponseDTO.class); 
			
		} 
		catch (RestClientException e) {
			logger.error("Error while creating new CardRange:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}

	/*
	 * This Service Method calls the restTemplate to update the Card range details entered by the user.
	 */
	public ResponseEntity<ResponseDTO> updateCardRange(CardRange CardRange) throws ServiceException{
		ResponseEntity<ResponseDTO> responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			ModelMapper mm = new ModelMapper();
			CardRangeDTO cardRangeDTO = mm.map(CardRange, CardRangeDTO.class);
			cardRangeDTO.setStatus(ResponseMessages.NEW_STATUS);
			cardRangeDTO.setCheckerDesc("");
			cardRangeDTO.setLastUpdUser(sessionService.getUserId());

			logger.debug("Calling '{}' service to update Card Range",config_base_url);
			
			 responseDTO = restTemplate.exchange(config_base_url + cardRangeBaseURL,
					HttpMethod.PUT,new HttpEntity<CardRangeDTO>(cardRangeDTO), ResponseDTO.class);
		}
		catch (RestClientException e) {
			logger.error("RestClientException in updateCardRange:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 

		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}


	/*
	 * This Method retrieves all the Issuer details.
	 */
	@SuppressWarnings("unchecked")
	public Map<Long,String> getAllIssuers() throws ServiceException{
		Map<Long,String> issuerMap=null;
		Map<Long,String> sortedissuerMap=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			ModelMapper mm = new ModelMapper();

			logger.debug("Calling '{}' service to search all Card Range",config_base_url);
			ResponseDTO responseDTO=restTemplate.getForObject(config_base_url +cardRangeBaseURL+ "/getIssuers", ResponseDTO.class);
			if(responseDTO!=null && responseDTO.getData()!=null){
				issuerMap = mm.map(responseDTO.getData(), Map.class);
				
				 sortedissuerMap = issuerMap.entrySet().stream()
			                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
			                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
			                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
			}
		}
		catch (RestClientException e) {
			logger.error("RestClientException in getAllIssuers:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		
		logger.debug(CCLPConstants.EXIT);
		return sortedissuerMap;
	}

	/* 
	 * This Method retrieves cardrange based on the input from the user.
	 */
	@SuppressWarnings("unchecked")
	public List<CardRangeDTO> getCardRange(CardRange cardRange)throws ServiceException{
		List<CardRangeDTO> cardRangeDtoList=null;
		ResponseDTO responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			ModelMapper mm = new ModelMapper();
			CardRangeDTO cardRangeDTO=mm.map(cardRange, CardRangeDTO.class);
			
			logger.debug("Calling '{}' service for Search Card Range",config_base_url);
			String issuerName=null; 
			if(cardRangeDTO.getIssuerName()==null || cardRangeDTO.getIssuerName().trim().isEmpty()){
				issuerName="*";
			}
			else{
				issuerName=cardRangeDTO.getIssuerName();
			}
			String prefix=null; 
			if(cardRangeDTO.getPrefix()==null || cardRangeDTO.getPrefix().trim().isEmpty()){
				prefix="*";
			}
			else{
				prefix=cardRangeDTO.getPrefix();
			}
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromUriString(config_base_url +cardRangeBaseURL+"/"+prefix+"/search")
					.queryParam("issuerName",issuerName);

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
					ResponseDTO.class);			  
			
			responseDTO=responseEntity.getBody();
			if(responseDTO!=null && responseDTO.getData()!=null){ 
				cardRangeDtoList= mm.map(responseDTO.getData(), List.class);
			}

		} 
		catch (RestClientException e) {
			logger.error("RestClientException in getCardRange:"+e);
			throw new ServiceException(ResponseMessages.SERVER_DOWN);
			
		}
		
		logger.debug(CCLPConstants.EXIT);
		return cardRangeDtoList;
	}

	public CardRange getCardRangeById(String cardRangeId) throws ServiceException {
		CardRange cardRange=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			ModelMapper mm = new ModelMapper();
			
			logger.debug("Calling '{}' service to search Card Range '{}'",config_base_url,cardRangeId);
			
			ResponseDTO responseDTO=restTemplate.getForObject(config_base_url +cardRangeBaseURL+ "/"+cardRangeId, ResponseDTO.class);
			if(responseDTO!=null && responseDTO.getData()!=null){
				CardRangeDTO	cardRangeDTO = mm.map(responseDTO.getData(), CardRangeDTO.class);
				cardRange=mm.map(cardRangeDTO, CardRange.class);
			}

		} 
		catch (RestClientException e) {
			logger.error("RestClientException in getCardRangeById:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return cardRange;
	}

	/*
	 *This Method Delete the card range by the selection.
	 */
	public ResponseEntity<ResponseDTO> deleteCardRange(long cardRangeId,String issuerName) throws ServiceException {
		ResponseEntity<ResponseDTO> responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			CardRangeDTO cardRangeDTO=new CardRangeDTO();
			cardRangeDTO.setCardRangeId(cardRangeId);
			cardRangeDTO.setIssuerName(issuerName);
			
			logger.debug("Calling '{}' service to delete Card Range '{}'",config_base_url,cardRangeId);
			 responseDTO = restTemplate.exchange(config_base_url +cardRangeBaseURL,HttpMethod.DELETE,new HttpEntity<CardRangeDTO>(cardRangeDTO), ResponseDTO.class);
		}//try
		catch (RestClientException e) {
			logger.error("RestClientException in deleteCardRange:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}

	/*
	 * This Method changes the cardrange Status to Approve/Reject.
	 */
	public ResponseEntity<ResponseDTO> changeCardRangeStatus(CardRange cardRange) throws ServiceException {
		ResponseEntity<ResponseDTO> responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			CardRangeDTO cardRangeDTO = new ModelMapper().map(cardRange, CardRangeDTO.class);
			cardRangeDTO.setLastUpdUser(sessionService.getUserId());
			
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromUriString(config_base_url +cardRangeBaseURL+"/"+cardRange.getCardRangeId()+"/status");

			 responseDTO = restTemplate.exchange(builder.build().encode().toUri(),
					HttpMethod.PUT,new HttpEntity<CardRangeDTO>(cardRangeDTO), ResponseDTO.class);

		}//try
		catch (RestClientException e) {
			logger.error("RestClientException in approveCardRange:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}
	
	
	public List<String> getAllCardRanges() throws ServiceException {

		List<String> cardRangesList = new ArrayList<String>();
		
		ResponseDTO responseDTO=null;
		String url="/getAllCardRanges";
		logger.debug(CCLPConstants.ENTER);
		
		try {	
			logger.debug("Calling '{}' service for getting all Card Ranges ",config_base_url);

			responseDTO=restTemplate.getForObject(config_base_url +cardRangeBaseURL+url , ResponseDTO.class);
			if(responseDTO!=null && responseDTO.getData()!=null){ 
				
				cardRangesList=(List<String>) responseDTO.getData();
				
			}
		} 
		catch (RestClientException e) {
			logger.error("RestClientException in getCardRange:"+e);
			throw new ServiceException(ResponseMessages.SERVER_DOWN);
			
		}
		
		logger.debug(CCLPConstants.EXIT);
		return cardRangesList;
	}
	
	/*added by nawaz*/
	
	public List<String> getCardRangebyCardId(List<Long> cardRangeId) throws ServiceException {

		List<String> cardRangesList = new ArrayList<String>();
		
		ResponseDTO responseDTO=null;
		String url="/getCardRangeDataById/"+cardRangeId;
		logger.debug(CCLPConstants.ENTER);
		
		try {	
			logger.debug("Calling '{}' service for getting all Card Ranges ",config_base_url);

			responseDTO=restTemplate.getForObject(config_base_url +cardRangeBaseURL+url , ResponseDTO.class);
			if(responseDTO!=null && responseDTO.getData()!=null){ 
				
				cardRangesList=(List<String>) responseDTO.getData();
				
			}
		} 
		catch (RestClientException e) {
			logger.error("RestClientException in getCardRange:"+e);
			throw new ServiceException(ResponseMessages.SERVER_DOWN);
			
		}
		
		logger.debug(CCLPConstants.EXIT);
		return cardRangesList;
	}
	
	
	
	/*added by nawaz ends*/

/*added by nawaz for gettinng card ranges*/
	public List<CardRangeDTO> getCardRangeByIssuerId(Long issuerId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		List<CardRangeDTO>	cardRangeDTO= new ArrayList<>();
		
		try {
			String cardRangeByIssuerIdurl="/getCardRangesByIssuerId/";
			logger.debug("Calling '{}' service to search Card Range '{}'",config_base_url,issuerId);
			
			ResponseDTO responseDTO=restTemplate.getForObject(config_base_url +"cardRanges"+cardRangeByIssuerIdurl+issuerId, ResponseDTO.class);
			if(responseDTO!=null && responseDTO.getData()!=null){
				cardRangeDTO = (List<CardRangeDTO>) responseDTO.getData();
				
			}

		} 
		catch (RestClientException e) {
			logger.error("RestClientException in getCardRangeById:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return cardRangeDTO;
	}	
	
/*added by nawaz for gettinng card ranges*/
	
	



}

