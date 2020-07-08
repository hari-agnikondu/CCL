package com.incomm.cclpvms.config.service.impl;

import java.util.Iterator;
import java.util.ArrayList;
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

import com.incomm.cclpvms.config.model.CurrencyCodeDTO;
import com.incomm.cclpvms.config.model.Purse;
import com.incomm.cclpvms.config.model.PurseDTO;
import com.incomm.cclpvms.config.model.PurseTypeDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.PurseService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.ResourceBundleUtil;


@Service
public class PurseServiceImpl implements PurseService {
	private static final Logger logger = LogManager.getLogger(PurseServiceImpl.class.getName());

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${CONFIG_BASE_URL}") String CONFIG_BASE_URL;
	
	String purseBaseUrl="/purses";
	
	@Autowired
	SessionService sessionService;
	

	public List<PurseDTO> getAllPurses() throws ServiceException {
		logger.info("Inside getAllPurses Method Starts Here");
		String tempurl = "";
		tempurl = "purses/";
		ResponseDTO responseBody = null;
		List<PurseDTO> purseDtos=null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL+tempurl, ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call : "+responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				throw new ServiceException(responseBody.getResult());
			}
			purseDtos = (List<PurseDTO>) responseBody.getData();

		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllPurses()");
			throw new ServiceException("Failed to process. Please try again later.");
		}

		return purseDtos;
	}
	

	public List<Purse> getPurses(Purse purse) throws ServiceException {
		
		List<Purse> purseList=null;
		try {
			Long purseTypeId=null;
			String extPurseId=null;
			String currencyCode=null;
			if (purse.getCurrencyTypeID() == null || purse.getCurrencyTypeID().trim().isEmpty()) {
				currencyCode = "*";
			} else {
				currencyCode = purse.getCurrencyTypeID();
			}
			
			if (purse.getPurseTypeId() == null) {
				purseTypeId=Long.valueOf(-1);
			} else {
				purseTypeId = purse.getPurseTypeId();
			}
			if (purse.getExtPurseId() == null|| purse.getExtPurseId().trim().isEmpty()) {
				extPurseId="*";
			} else {
				extPurseId = purse.getExtPurseId();
			}
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL+purseBaseUrl+"/"+purseTypeId+"/"
			+currencyCode+"/"+extPurseId, ResponseDTO.class);

			if(responseEntity!=null && responseEntity.getBody()!=null){
				ResponseDTO responseDTO = responseEntity.getBody();
				logger.debug("response from Rest Call : "+responseDTO.getResult());
				if (!ResponseMessages.SUCCESS.equals(responseDTO.getCode())) {
					throw new ServiceException(responseDTO.getResult());
				}
				@SuppressWarnings("unchecked")
				List<PurseDTO> purseDtoList=(List<PurseDTO>)responseDTO.getData();
				purseList= new ModelMapper().map(purseDtoList, new TypeToken<List<Purse>>() {}.getType());
			}
			
//			String currencyCode=null; 
//			if(purse.getCurrencyTypeID()==null || purse.getCurrencyTypeID().trim().isEmpty()){
//				currencyCode="*";
//			}
//			else{
//				currencyCode=purse.getCurrencyTypeID();
//			}
//			String upc=null; 
//			if(purse.getUpc()==null || purse.getUpc().trim().isEmpty()){
//				upc="*";
//			}
//			else{
//				upc=purse.getUpc();
//			}
//			
//			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL+purseBaseUrl+"/"+currencyCode+"/"+upc, ResponseDTO.class);
//
//			if(responseEntity!=null && responseEntity.getBody()!=null){
//				ResponseDTO responseDTO = responseEntity.getBody();
//				logger.debug("response from Rest Call : "+responseDTO.getResult());
//				if (!ResponseMessages.SUCCESS.equals(responseDTO.getCode())) {
//					throw new ServiceException(responseDTO.getResult());
//				}
//				@SuppressWarnings("unchecked")
//				List<PurseDTO> purseDtoList=(List<PurseDTO>)responseDTO.getData();
//				purseList= new ModelMapper().map(purseDtoList, new TypeToken<List<Purse>>() {}.getType());
//			}
			
		
		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getPurses()"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}

		return purseList;
	}
	
	@Override
	public Purse getPurseById(Long purseId) throws ServiceException {
		Purse purse=null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL+purseBaseUrl+"/"+purseId, ResponseDTO.class);
			if(responseEntity!=null && responseEntity.getBody()!=null){
				if (!ResponseMessages.SUCCESS.equals(responseEntity.getBody().getCode())) {
					throw new ServiceException(responseEntity.getBody().getResult());
				}
				purse= new ModelMapper().map(responseEntity.getBody().getData(), new TypeToken<Purse>() {}.getType());
			}
		}catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getPurseById()"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		
		return purse;
	}
	
	public ResponseEntity<ResponseDTO> updatePurseDetails(Purse purse) throws ServiceException {
		ResponseEntity<ResponseDTO> responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			ModelMapper mm = new ModelMapper();
			PurseDTO purseDTO = mm.map(purse, PurseDTO.class);
			purseDTO.setLastUpdUser(sessionService.getUserId());
			 responseDTO = restTemplate.exchange(CONFIG_BASE_URL+purseBaseUrl,
					HttpMethod.PUT,new HttpEntity<PurseDTO>(purseDTO), ResponseDTO.class);
		}catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in updatePurseDetails()"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return responseDTO;
	}

	public ResponseEntity<ResponseDTO> savePurseDetails(Purse purse) throws ServiceException {
		ResponseEntity<ResponseDTO> responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			if(purse!=null && purse.getPurseTypeId()==2){
				purse.setCurrencyTypeID("000");
			}if(purse!=null && purse.getPurseTypeId()==3){
				purse.setCurrencyTypeID("001");
			}
			ModelMapper mm = new ModelMapper();
			PurseDTO purseDTO = mm.map(purse, PurseDTO.class);
			purseDTO.setInsUser(sessionService.getUserId());
			purseDTO.setLastUpdUser(sessionService.getUserId());
			 responseDTO = restTemplate.exchange(CONFIG_BASE_URL+purseBaseUrl,
					HttpMethod.POST,new HttpEntity<PurseDTO>(purseDTO), ResponseDTO.class);
		}catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in savePurseDetails()"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return responseDTO;
	}
	
	public Map<Long,String> getPurseTypeList() throws ServiceException {
		Map<Long,String> purseTypeMap=null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL+"/master"+"/purseType", ResponseDTO.class);
			if(responseEntity!=null && responseEntity.getBody()!=null){
				if (!ResponseMessages.SUCCESS.equals(responseEntity.getBody().getCode())) {
					throw new ServiceException(responseEntity.getBody().getResult());
				}
				List<PurseTypeDTO> purseTypeDTOList= new ModelMapper().map(responseEntity.getBody().getData(),
						new TypeToken<List<PurseTypeDTO>>() {}.getType());
				if(purseTypeDTOList!=null){
					purseTypeMap=purseTypeDTOList.stream().collect(Collectors.toMap(PurseTypeDTO::getPurseId, PurseTypeDTO::getPurseType));
				}
			}
		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getPurseTypeList()"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return purseTypeMap;
	}
	public Map<String,String> getCurrencyCodeList() throws ServiceException{

		Map<String,String> currencyCodeMap=new LinkedHashMap<>();
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL+"/master"+"/currencyCode", ResponseDTO.class);
			if(responseEntity!=null && responseEntity.getBody()!=null){
				if (!ResponseMessages.SUCCESS.equals(responseEntity.getBody().getCode())) {
					throw new ServiceException(responseEntity.getBody().getResult());
				}
				List<CurrencyCodeDTO> currencyCodeDTOList= new ModelMapper().map(responseEntity.getBody().getData(),
						new TypeToken<List<CurrencyCodeDTO>>() {}.getType());
				if(currencyCodeDTOList!=null){
					for (Iterator<CurrencyCodeDTO> iterator = currencyCodeDTOList.iterator(); iterator.hasNext();) {
						CurrencyCodeDTO currencyCodeDTO = iterator.next();
						currencyCodeMap.put(currencyCodeDTO.getCurrencyTypeID(), currencyCodeDTO.getCurrCodeAlpha()+":"+currencyCodeDTO.getCurrencyTypeID());
					}
					currencyCodeMap.remove("000");
				}
			}
		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getCurrencyCodeList()"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return currencyCodeMap;
	
	}
	public ResponseEntity<ResponseDTO> deletePurseDetails(Purse purse) throws ServiceException {
		ResponseEntity<ResponseDTO> responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			 responseDTO = restTemplate.exchange(CONFIG_BASE_URL+purseBaseUrl+"/"+purse.getPurseId(),
					HttpMethod.DELETE,null, ResponseDTO.class);
		}catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in deletePurseDetails()"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return responseDTO;
	}

	public List<String> getPurseByIds(List<Long> purseIds) throws ServiceException {

	
		
		List<String> pursesList = new ArrayList<String>();
		
		ResponseDTO responseDTO=null;
		String	purseUrl = "purses/";
		String url="/getPurseDataById/"+purseIds;
		logger.debug(CCLPConstants.ENTER);
		
		try {	
			logger.debug("Calling '{}' service for getting all Card Ranges ",CONFIG_BASE_URL);

		

			responseDTO=restTemplate.getForObject(CONFIG_BASE_URL +purseUrl+url , ResponseDTO.class);
			if(responseDTO!=null && responseDTO.getData()!=null){ 
				
				pursesList=(List<String>) responseDTO.getData();
				
			}
			
		

		} 
		catch (RestClientException e) {
			logger.error("RestClientException in getCardRange:"+e);
			throw new ServiceException(ResponseMessages.SERVER_DOWN);
			
		}
		
		logger.debug(CCLPConstants.EXIT);
		return pursesList;
		
	
	}
	
	
	

}
