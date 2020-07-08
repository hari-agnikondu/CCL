package com.incomm.cclpvms.setup.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.setup.model.JobScheduler;
import com.incomm.cclpvms.setup.model.SwitchOverScheduler;
import com.incomm.cclpvms.setup.service.JobSchedulerService;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class JobSchedulerServiceImpl implements JobSchedulerService {

	// the logger
	private static final Logger logger = LogManager.getLogger(JobSchedulerServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate ;
	
	@Value("${CONFIG_BASE_URL}") String CONFIG_BASE_URL;
	

	@SuppressWarnings("unchecked")
	@Override
	public Map<Long,String> getAllJobs() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		Map<Long,String> sortedJobMap = null;
		
		try {
			logger.debug("Calling '{}' service to get all jobs",CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/jobScheduler/jobsList",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.error("Failed to Fetch jobs from config srvice");
				throw new ServiceException(responseBody.getMessage() );
			}
			Map<Long,String> jobMap = (Map<Long,String>) responseBody.getData();
			sortedJobMap = jobMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
			          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		} catch (RestClientException e) {
			logger.error("Exception while fetching records ",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return sortedJobMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getUserMailDetail() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		List<Object[]> userMailList = null;
		try {
			logger.debug("Calling '{}' service to get user's mail details",CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/jobScheduler/userMailList",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase("SUCCESS")) {
				logger.error("Failed to Fetch mail list from config srvice");
				throw new ServiceException(responseBody.getMessage() );
			}
			
		  userMailList = (List<Object[]>) responseBody.getData();
			
		} catch (RestClientException e) {
			logger.error("Exception while Getting records {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return userMailList;
	}	
	
	public ResponseDTO updateSchedulerConfig(JobScheduler scheduleJobDTO) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<JobScheduler> requestEntity = new HttpEntity<>(scheduleJobDTO, headers);
			logger.debug("Calling '{}' service to update SchedulerConfig",CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + "/jobScheduler", HttpMethod.PUT,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating SchedulerConfig {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}


	public ResponseDTO getSchedulerConfigByJobId(long jobId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		logger.debug("Calling '{}' service for get SchedulerConfig '{}'",CONFIG_BASE_URL,jobId);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/jobScheduler/{jobId}",ResponseDTO.class, jobId);
		ResponseDTO responseDTO = responseEntity.getBody();
		logger.info("SchedulerConfig information "+responseEntity.getBody());
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	
	}
	
	@SuppressWarnings("unchecked")	
	@Override
	public List<SwitchOverScheduler> getAllServers() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		ObjectMapper objectMapper = new ObjectMapper();
		List<SwitchOverScheduler> switchOverSchedulerList = new ArrayList<>();
		try {
			logger.debug("Calling '{}' service to get all Servers", CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(CONFIG_BASE_URL + "/jobScheduler/serversList", ResponseDTO.class);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase("SUCCESS")) {
				logger.error("Failed to Fetch servers List from config srvice");
				throw new ServiceException(responseBody.getMessage());
			}

			List<Object> servers = (List<Object>) responseBody.getData();
			if (servers != null) {
				Iterator<Object> itr = servers.iterator();
				while (itr.hasNext()) {
					switchOverSchedulerList.add(objectMapper.convertValue(itr.next(), SwitchOverScheduler.class));
				}
			}
			Collections.sort(switchOverSchedulerList);
		} catch (RestClientException e) {
			logger.error("Exception while fetching records {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return switchOverSchedulerList;
	}

	@Override
	public ResponseDTO updateSwitchOverScheduler(SwitchOverScheduler sosDTO) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<SwitchOverScheduler> requestEntity = new HttpEntity<>(sosDTO, headers);
			logger.debug("Calling '{}' service to update SchedulerConfig", CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					CONFIG_BASE_URL + "/jobScheduler/switchOverScheduler", HttpMethod.PUT, requestEntity,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating SchedulerConfig {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<List<Object>> getRunningJobs() throws ServiceException {
		
		ResponseDTO responseBody = null;
		List<List<Object>> schedulerServiceJobs = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/jobScheduler/schedulerServiceJobs",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				logger.error("Failed to Fetch scheduler service jobs list from config srvice");
				throw new ServiceException(
						responseBody.getMessage() != null ? responseBody.getMessage() : ResourceBundleUtil.getMessage(ResponseMessages.ERR_JOB_SCHEDULER_FETCH));
			}
			List<Object> schedulerServiceJobsDtos = (List<Object>) responseBody.getData();
			if (schedulerServiceJobsDtos != null) {
				Iterator<Object> itr = schedulerServiceJobsDtos.iterator();
				while (itr.hasNext()) {
					schedulerServiceJobs.add(objectMapper.convertValue(itr.next(), List.class));
				} 
			}
		} catch (RestClientException e) {
			logger.error("Exception while fetching records {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return schedulerServiceJobs;
	}
}
