package com.incomm.cclp.fsapi.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.incomm.cclp.exception.ServiceException;

public interface ProxyNumberActivationService {
		
	public ResponseEntity<Object> proxyNumberActivationProcess(Map<String, String> reqHeaders, Map<String, Object> valueObj) throws ServiceException;
		

	}

