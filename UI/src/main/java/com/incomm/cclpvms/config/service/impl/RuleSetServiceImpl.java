package com.incomm.cclpvms.config.service.impl;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.RuleSetDTO;
import com.incomm.cclpvms.config.model.Ruleset;
import com.incomm.cclpvms.config.service.RuleSetService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class RuleSetServiceImpl implements RuleSetService {
	
	private static final Logger logger = LogManager.getLogger(RuleSetServiceImpl.class);
	private static final String RULESET_BASE_URL = "ruleSet";

	@Autowired
	private RestTemplate restTemplate;

	@Value("${CONFIG_BASE_URL}")
	String CONFIG_BASE_URL;

	@Value("${INS_USER}")
	long userId;

	
	
	/**
	 * Gets the rules list represented as a map with ruleid as key and 
	 * ruleid:ruleName as value.
	 * 
	 * @return the Map with the result.
	 */
	@Override
	public Map<Long, String> getRule() throws ServiceException {
		Map<Long,String> rulesMap=null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "rules", 
					ResponseDTO.class);
			if(responseEntity!=null && responseEntity.getBody()!=null){
				if (!ResponseMessages.SUCCESS.equals(responseEntity.getBody().getCode())) {
					throw new ServiceException(responseEntity.getBody().getResult());
				}
				List<Ruleset> ruleDtoList= new ModelMapper().map(responseEntity.getBody().getData(),
						new TypeToken<List<Ruleset>>() {}.getType());
				if(ruleDtoList!=null){
					rulesMap=ruleDtoList.stream().collect(Collectors.toMap(
							Ruleset::getRuleId, Ruleset::getRuleName));
				}
			}
		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in rule List()"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return rulesMap;
	}

	/**
	 * Gets the rules list represented as a map with rulesetid as key and 
	 * ruleid:ruleSetName as value.
	 * 
	 * @return the Map with the result.
	 */
	@Override
	public Map<Long, String> getRuleSet() throws ServiceException {
		Map<Long,String> rulesMap=null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + RULESET_BASE_URL, 
					ResponseDTO.class);
			if(responseEntity!=null && responseEntity.getBody()!=null){
				if (!ResponseMessages.SUCCESS.equals(responseEntity.getBody().getCode())) {
					throw new ServiceException(responseEntity.getBody().getResult());
				}
				List<Ruleset> ruleDtoList= new ModelMapper().map(responseEntity.getBody().getData(),
						new TypeToken<List<Ruleset>>() {}.getType());
				if(ruleDtoList!=null){
					rulesMap=ruleDtoList.stream().collect(Collectors.toMap(
							Ruleset::getRuleSetId, Ruleset::getRuleSetName));
				}
			}
		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in rulesetDtoList()"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return rulesMap;
	}

	@Override
	public ResponseDTO createRuleSet(RuleSetDTO ruleSetDto) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(CONFIG_BASE_URL + RULESET_BASE_URL,
					ruleSetDto, ResponseDTO.class);
			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.info("status of createRuleSet " + httpStatus);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in createRuleSet()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);

		return responseBody;
	}

	@Override
	public ResponseDTO updateRuleSet(RuleSetDTO ruleSetDto) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + RULESET_BASE_URL,
					HttpMethod.PUT, new HttpEntity<RuleSetDTO>(ruleSetDto), ResponseDTO.class);

			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.info("status of updateRuleSet " + httpStatus);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in updateRuleSet()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);

		return responseBody;
	}

	@Override
	public String getRuleDetails(long ruleSetId) throws ServiceException {
		logger.debug("ENTER");
		ResponseDTO responseBody = null;
		ResponseEntity<ResponseDTO> responseEntity = restTemplate
				.getForEntity(CONFIG_BASE_URL + RULESET_BASE_URL + "/" + ruleSetId, ResponseDTO.class);
		responseBody = responseEntity.getBody();
		String jsonArrray = responseBody.getData().toString();
		logger.info("ruleDetails output  " + jsonArrray);
		return jsonArrray;
	}
	
}
