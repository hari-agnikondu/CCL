package com.incomm.cclp.fsapi.controller;


import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.fsapi.service.PostBackService;

@RestController
@Configuration
@RequestMapping("/postBack")
public class PostBackController {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	
	@Autowired
	PostBackService postBackService;
	
	@Value("${PSTBK_ORDER_STATUS_UPDATE}")
	private String pstbkOrderActStatus;
	
	@PostConstruct
	public void loadPostBackUrls() {
		//Here update the 2nd parameter whenever key of "pstbkOrderActStatus" is getting changed
		postBackService.loadPostBackUrl(pstbkOrderActStatus,"PSTBK_ORDER_STATUS_UPDATE");
	}
	
	@RequestMapping(value="/orderStatus",method = RequestMethod.POST)
	public void orderStatus(@RequestBody String payLoad,
			@RequestHeader Map<String, String> headers) {
		logger.info("PostBack Started");
		
		postBackService.postBackStatus(payLoad, headers);
		
	}
}