package com.incomm.cclp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.dto.ScheduleJobDTO;
import com.incomm.cclp.dto.SwitchOverSchedulerDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.JobSchedulerService;
import com.incomm.cclp.util.ResponseBuilder;

@RestController
@RequestMapping("/jobScheduler")
public class JobSchedulerController {

	@Autowired
	private JobSchedulerService jobSchedulerService;
	
	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;
	
	// the logger
	private static final Logger logger = LogManager.getLogger(JobSchedulerController.class);
	
	
	@RequestMapping(value = "/{jobId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getSchedulerConfigByJobId(@PathVariable("jobId") long jobId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto=null;
		if (jobId <= 0)
		{
			logger.info("Job Id is negative: {}",jobId);
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.JOBID_NEGATIVE_ORZERO,ResponseMessages.DOESNOT_EXISTS);
		}
		else{
			ScheduleJobDTO scheduleJobDto = jobSchedulerService.getSchedulerConfig(jobId);

			responseDto = responseBuilder.buildSuccessResponse(scheduleJobDto,
				ResponseMessages.SCHEDULERCONFIG_RETRIEVE_SUCCESS,ResponseMessages.SUCCESS);
			
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
		
	}

	@RequestMapping(value="/jobsList", method=RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getJobs() throws ServiceException {
	
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		Map<Long,String> jobMap = jobSchedulerService.getJobs();

		if (jobMap!=null && !jobMap.isEmpty()) {

			logger.debug("Job map fetched successfully, Map -> {}",jobMap);
			responseDto = responseBuilder.buildSuccessResponse(jobMap,
					ResponseMessages.JOB_RETRIEVE_SUCCESS,ResponseMessages.SUCCESS);

		} 
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value="/userMailList", method=RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getUserMailList() {
	
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> userMailList = null;

		try {
			userMailList = jobSchedulerService.getUserMailList();
			responseDto = responseBuilder.buildSuccessResponse(userMailList,"", ResponseMessages.SUCCESS);
		} catch (Exception e) {
			logger.error("error while fetching user mail list : {}",e.getMessage());
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_USER_MAIL_DETAIL_RETRIEVE, ResponseMessages.FAILURE);
			
		}
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping (method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateSchedulerConfigForAJob(@RequestBody ScheduleJobDTO scheduleJobDTO) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		
		logger.debug(scheduleJobDTO);	
		jobSchedulerService.updateSchedulerConfigForAJob(scheduleJobDTO);
		logger.debug("Updating scheduler config for {} as {}",scheduleJobDTO.getJobName(),scheduleJobDTO);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SCHEDULERCONFIG_UPDATE_SUCCESS,ResponseMessages.SUCCESS);
		
		 valuesMap.put("jobName",scheduleJobDTO.getJobName());
		 String templateString = "";

		 templateString=responseDto.getMessage();
		
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 String resolvedString = sub.replace(templateString);
		 
		 responseDto.setMessage(resolvedString);
		
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	

	@RequestMapping(value="/serversList", method=RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getServers() throws ServiceException {
	
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<SwitchOverSchedulerDTO> serversList= jobSchedulerService.getServers();

		if (!serversList.isEmpty()) {

			logger.debug("Servers List fetched successfully, serversList -> {}",serversList);
			responseDto = responseBuilder.buildSuccessResponse(serversList,
					ResponseMessages.SERVERS_RETRIEVE_SUCCESS,ResponseMessages.SUCCESS);
		} 
		else {
			logger.debug("Servers List fetched successfully, serversList -> {}",serversList);
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.SERVERS_DOESNOT_EXIST,ResponseMessages.FAILURE);
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value="/switchOverScheduler",method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateSwitchOverScheduler(@RequestBody SwitchOverSchedulerDTO 		switchOverSchedulerDTO) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		
		logger.debug(switchOverSchedulerDTO);	
		int count = jobSchedulerService.updateSwitchOverScheduler(switchOverSchedulerDTO);

		logger.debug("Updating scheduler config for {} as {}",switchOverSchedulerDTO.getPhysicalServer(),switchOverSchedulerDTO);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(count, ResponseMessages.SWITCHOVERSCHEDULER_UPDATE_SUCCESS,ResponseMessages.SUCCESS);
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value="/schedulerServiceJobs", method=RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllRunningJobs() {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> schedulerServiceJobs = jobSchedulerService.getJobSchedulerServiceJobs();
		if(CollectionUtils.isEmpty(schedulerServiceJobs)) {
			logger.error("Failed to get fee attributes, feeAttribute is empty");
			responseDto = responseBuilder.buildSuccessResponse(schedulerServiceJobs,
					ResponseMessages.FAILED_JOB_SCHEDULER_JOBS_RETRIEVE,ResponseMessages.FAILURE);
		}else {
			
		logger.info("Successfully retrieved scheduler service running jobs");
		 responseDto = responseBuilder.buildSuccessResponse(schedulerServiceJobs, 
				 ResponseMessages.SUCCESS_JOB_SCHEDULER_JOBS_RETRIEVE, ResponseMessages.SUCCESS);
		}
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
}
