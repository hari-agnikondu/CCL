package com.incomm.cclp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.service.PingService;
import com.incomm.cclp.transaction.constants.GeneralConstants;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/ping")
@Api(value = "Spil Transactions")
public class PingController {

	private static final Logger logger = LogManager.getLogger(PingController.class);

	@Autowired
	PingService pingService;

	@GetMapping(produces = "application/json")
	public ResponseEntity<String> getServerDetails(@RequestHeader MultiValueMap<String, String> reqHeaders,
			@RequestParam("functionCode") Integer functionCode) {
		logger.debug(GeneralConstants.ENTER);

		ResponseEntity<String> responseMsg = pingService.getServerDetails(reqHeaders, functionCode);

		logger.debug(GeneralConstants.EXIT);
		return responseMsg;

	}

}
