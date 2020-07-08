package com.incomm.cclpvms.config.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.Rule;
import com.incomm.cclpvms.config.model.RuleDTO;
import com.incomm.cclpvms.config.service.RuleService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class RuleServiceImpl implements RuleService {

	private static final Logger logger = LogManager.getLogger(CCFServiceImpl.class);
	private static final String RULE_BASE_URL = "rules";

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
	public Map<Long,String> getRulesList() throws ServiceException {
		Map<Long,String> rulesMap=null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + RULE_BASE_URL, 
					ResponseDTO.class);
			if(responseEntity!=null && responseEntity.getBody()!=null){
				if (!ResponseMessages.SUCCESS.equals(responseEntity.getBody().getCode())) {
					throw new ServiceException(responseEntity.getBody().getResult());
				}
				List<Rule> ruleDtoList= new ModelMapper().map(responseEntity.getBody().getData(),
						new TypeToken<List<Rule>>() {}.getType());
				if(ruleDtoList!=null){
					rulesMap=ruleDtoList.stream().collect(Collectors.toMap(
							Rule::getRuleId, Rule::getRuleName));
				}
			}
		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in ruleDtoList()"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return rulesMap;
	}


	/**
	 * Creates a new rule
	 * 
	 * @param rule The Rule to save
	 * 
	 */
	public ResponseDTO createRule(RuleDTO ruleDto) throws ServiceException
	{
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(CONFIG_BASE_URL + RULE_BASE_URL, 
					ruleDto,
					ResponseDTO.class);
			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.info("status of CreateRule " + httpStatus);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in createRule()"+e);
			throw new ServiceException("Failed to process,Please try again later");
		}

		logger.debug(CCLPConstants.EXIT);

		return responseBody;
	}

	
	/**
	 * Updates a rule
	 * 
	 * @param rule The Rule to update
	 * 
	 */
	public ResponseDTO updateRule(RuleDTO ruleDto) throws ServiceException
	{
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + RULE_BASE_URL, 
					HttpMethod.PUT, 
					new HttpEntity<RuleDTO>(ruleDto),
					ResponseDTO.class);
			
			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.info("status of UpdateRule " + httpStatus);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in updateRule()"+e);
			throw new ServiceException("Failed to process Please try again later");
		}

		logger.debug(CCLPConstants.EXIT);

		return responseBody;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,String> getRuleConfig() throws ServiceException {
		logger.debug("ENTER getRuleConfig");
		ResponseDTO responseBody = null;

		logger.debug("ENTER getRuleConfig:" + CONFIG_BASE_URL + RULE_BASE_URL);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate
				.getForEntity(CONFIG_BASE_URL + RULE_BASE_URL + "/ruleConfigs", ResponseDTO.class);
		responseBody = responseEntity.getBody();
		return  (HashMap<String, String>) responseBody.getData();

	}

	public List<Rule> getRulesByName(String ruleName) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		List<Rule> rulesList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		String tempurl = "";
		tempurl = RULE_BASE_URL + "/search";
	
		try {



			UriComponentsBuilder builder = UriComponentsBuilder
					.fromUriString(CONFIG_BASE_URL+tempurl)
					.queryParam("ruleName", ruleName);

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
					ResponseDTO.class);


			responseBody = responseEntity.getBody();
			@SuppressWarnings("unchecked")
			List<Object> ruleDtos = (List<Object>) responseBody.getData();
			if (ruleDtos != null) {
				Iterator<Object> itr = ruleDtos.iterator();
				while (itr.hasNext()) {
					RuleDTO ruleDto = objectMapper.convertValue(itr.next(), RuleDTO.class);
					Rule rule = new Rule();
					rule.setRuleId(ruleDto.getRuleId());
					rule.setRuleName(ruleDto.getRuleName());
					rule.setTxnTypeIds(ruleDto.getTxnTypeId());
					rule.setAction(ruleDto.getActionType());
					rule.setJsonReq(ruleDto.getJsonReq());
					rulesList.add(rule);
				} 
			}
			
		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getRulesByName()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		}
		logger.debug(CCLPConstants.EXIT);
		
		return rulesList;
	}
	
	public Rule getRulesById(Long ruleId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		Rule rule = new Rule();
		ObjectMapper objectMapper = new ObjectMapper();
		try {



			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + RULE_BASE_URL + "/{ruleId}",
					ResponseDTO.class, ruleId);


			responseBody = responseEntity.getBody();
			RuleDTO ruleDto = objectMapper.convertValue(responseBody.getData(), RuleDTO.class);
				
			rule.setRuleId(ruleDto.getRuleId());
			rule.setRuleName(ruleDto.getRuleName());
			rule.setTxnTypeIds(ruleDto.getTxnTypeId());
			rule.setAction(ruleDto.getActionType());
			rule.setJsonReq(ruleDto.getJsonReq());
			
			
		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getRulesByName()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		}
		logger.debug(CCLPConstants.EXIT);
		
		return rule;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getTransactionByTxnType(String txnTypeId) throws ServiceException {
		logger.debug("ENTER");
		ResponseDTO responseBody = null;
		List<Object[]> txnList = new ArrayList<>();

		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(
					CONFIG_BASE_URL + RULE_BASE_URL
							+ "/{txnTypeId}/transaction",
					ResponseDTO.class, txnTypeId);

			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call : " + responseBody.getResult());
			if (!responseBody.getResult().equalsIgnoreCase("SUCCESS")) {
				logger.error("Failed to Fetch transaction from config service");
				throw new ServiceException(responseBody.getMessage() );
			}
			txnList = ( List<Object[]>) responseBody.getData();
			logger.debug("cardList"+txnList);
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching transaction ",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return txnList;
	}
}
