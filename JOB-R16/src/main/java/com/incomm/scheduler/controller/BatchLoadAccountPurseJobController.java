package com.incomm.scheduler.controller;

import java.util.concurrent.Executors;

import javax.batch.operations.JobRestartException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.scheduler.constants.APIConstants;
import com.incomm.scheduler.constants.ResponseMessages;
import com.incomm.scheduler.controller.mapper.ResourceValidatorMapper;
import com.incomm.scheduler.controller.resource.CreateBatchPurseLoadJobRequestResource;
import com.incomm.scheduler.dto.ResponseDTO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.service.AccountPurseJobService;
import com.incomm.scheduler.utils.JobConstants;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("productPurse")
public class BatchLoadAccountPurseJobController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("loadAccountPurseBatchJob")
	private Job loadAccountPurseBatchJob;

	@Autowired
	private AccountPurseJobService accountPurseJobService;

	@PostMapping(value = "triggerJob")
	public ResponseEntity<ResponseDTO> triggerJob(
			@RequestHeader(value = APIConstants.API_HEADER_DELIVERY_CHANNEL, required = true) String channelName, //
			@RequestHeader(value = APIConstants.API_HEADER_USERNAME, required = false) String userName) {
		log.info("triggering the load account purse batch job for user:{}, channel:{}", userName, channelName);

		long timeBeforeTransaction = System.currentTimeMillis();
		ResponseDTO response;

		try {

			JobParameters jobParameters = new JobParametersBuilder().addLong(JobConstants.DATETIME, System.currentTimeMillis())
				.addString(JobConstants.JOBNAME, JobConstants.BATCH_LOAD_ACCOUNT_PURSE_JOB_ID)
				.toJobParameters();

			Executors.newSingleThreadExecutor()
				.submit(() -> jobLauncher.run(loadAccountPurseBatchJob, jobParameters));

			response = ResponseDTO.builder()
				.responseCode("0")
				.message("Job Triggerred successfully.")
				.build();

			long timeTaken = System.currentTimeMillis() - timeBeforeTransaction;
			log.info("TIme taken to trigger the load account purse batch job : {}", timeTaken);

		} catch (JobRestartException exception) {
			log.warn(exception.getMessage(), exception);
			response = ResponseDTO.builder()
				.responseCode(ResponseMessages.FAILURE)
				.message(exception.getMessage())
				.build();
		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
			response = ResponseDTO.builder()
				.responseCode(ResponseMessages.FAILURE)
				.message(exception.getMessage())
				.build();
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "createPurseLoadJob")
	public ResponseEntity<ResponseDTO> createLoadAcccountPurseBatchJob(
			@RequestHeader(value = APIConstants.API_HEADER_DELIVERY_CHANNEL, required = false) String channelName, //
			@RequestHeader(value = APIConstants.API_HEADER_USERNAME, required = false) String userName, //
			@RequestHeader(value = APIConstants.API_HEADER_CORRELATION_ID, required = false) String correlationId, //
			@RequestBody CreateBatchPurseLoadJobRequestResource request) {

		ResponseDTO response;

		log.info("Request received for channel:{}, correlationId:{}, createLoadAcccountPurseBatchJob: {}", channelName, correlationId,
				request.toString());

		try {
			Long batchId = this.accountPurseJobService
				.createPurseLoadJobRequest(ResourceValidatorMapper.map(correlationId, userName, request));

			response = ResponseDTO.builder()
				.message("job request with batchId:" + batchId + " created.")
				.code("0")
				.data(batchId)
				.build();
			log.info("Response:{}", response.toString());

			return ResponseEntity.ok()
				.body(response);

		} catch (ServiceException e) {
			log.error("Service Exception occured while processing request:{}", e.toString());
			response = ResponseDTO.builder()
				.message(e.getMessage())
				.code("400")
				.build();

			return ResponseEntity.badRequest()
				.body(response);

		} catch (Exception e) {
			log.error("Error occured while processing request:{}", e.getMessage(), e);
			response = ResponseDTO.builder()
				.message("Server Error")
				.code("500")
				.build();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(response);
		}

	}
	
	@PostMapping(value = "autoJob")
	public void autoJob(){
		 this.accountPurseJobService
				.autoPurseJob();
	}
	

}
