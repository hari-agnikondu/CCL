package com.incomm.cclp.account.rest.controller;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.account.application.command.UpdateAccountPurseCommand;
import com.incomm.cclp.account.application.command.UpdatePurseStatusCommand;
import com.incomm.cclp.account.application.command.UpdateRolloverAccountPurseCommand;
import com.incomm.cclp.account.application.service.AccountApplicationService;
import com.incomm.cclp.account.domain.view.AccountPurseView;
import com.incomm.cclp.account.domain.view.PurseView;
import com.incomm.cclp.account.rest.mapper.CommandFactory;
import com.incomm.cclp.account.rest.mapper.ResourceMapper;
import com.incomm.cclp.account.rest.resources.UpdateAccountPurseRequestResource;
import com.incomm.cclp.account.rest.resources.UpdateAccountPurseResponseResource;
import com.incomm.cclp.account.rest.resources.UpdatePurseStatusRequestResource;
import com.incomm.cclp.account.rest.resources.UpdatePurseStatusResponseResource;
import com.incomm.cclp.constants.APIConstants;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class AccountRestController {

	@Autowired
	AccountApplicationService applicationService;

	@Autowired
	CommandFactory commandFactory;

	@PostMapping(value = "/purses/update", produces = "application/json", consumes = "application/json")
	public ResponseEntity<UpdateAccountPurseResponseResource> loadAccountPurse( //
			@RequestHeader(value = APIConstants.API_HEADER_DELIVERY_CHANNEL, required = false) String channelName, //
			@RequestHeader(value = APIConstants.API_HEADER_USERNAME, required = false) String userName, //
			@RequestHeader(value = APIConstants.API_HEADER_CORRELATION_ID, required = false) String correlationId, //
			@RequestBody UpdateAccountPurseRequestResource requestResource) {

		ThreadContext.put("RRN", correlationId);
		AccountPurseView view = null;

		log.info("Purse update request received for correlation Id: {}", correlationId);
		log.info("request information: channel:{}, userName:{}, correlationId:{}, request:{}", channelName, userName, correlationId,
				requestResource.toString());

		if ("autoRollover".equals(requestResource.getAction())) {
			UpdateRolloverAccountPurseCommand command = commandFactory.getUpdateRolloverAccountPurseCommand(correlationId, channelName,
					userName, requestResource);
			view = applicationService.execute(command);

		} else {
			UpdateAccountPurseCommand command = commandFactory.getUpdateAccountPurseCommand(correlationId, channelName, userName,
					requestResource);
			view = applicationService.execute(command);
		}

		ThreadContext.clearAll();

		return this.getResponseEntity(ResourceMapper.mapUpdateAccountPurseResponse(view), correlationId);

	}

	@PostMapping(value = "/purses/updateStatus", produces = "application/json")
	public ResponseEntity<UpdatePurseStatusResponseResource> updatePurseStatus( //
			@RequestHeader(value = APIConstants.API_HEADER_DELIVERY_CHANNEL, required = false) String channelName, //
			@RequestHeader(value = APIConstants.API_HEADER_USERNAME, required = false) String userName, //
			@RequestHeader(value = APIConstants.API_HEADER_CORRELATION_ID, required = false) String correlationId, //
			@RequestBody UpdatePurseStatusRequestResource requestResource) {

		ThreadContext.put("RRN", correlationId);

		log.info("Purse Unload request received for correlation Id: {}", correlationId);
		log.info("request information: channel:{}, userName:{}, correlationId:{}, request:{}", channelName, userName, correlationId,
				requestResource.toString());

		UpdatePurseStatusCommand command = commandFactory.getUpdatePurseStatusCommand(correlationId, channelName, userName,
				requestResource);

		PurseView view = applicationService.execute(command);

		ThreadContext.clearAll();

		return this.getResponseEntity(ResourceMapper.mapUpdatePurseStatusResponse(view), correlationId);

	}

	private <T> ResponseEntity<T> getResponseEntity(T resource, String correlationId) {

		HttpHeaders headers = ResourceMapper.map(APIConstants.API_HEADER_CORRELATION_ID, correlationId);
		return ResponseEntity.ok()
			.headers(headers)
			.body(resource);
	}

}
