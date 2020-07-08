package com.incomm.cclp.fsapi.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.incomm.cclp.exception.ServiceException;

@Service
public interface VirtualCardValidationService {
	public ResponseEntity<Object> virtualCardValidationProcess(Map<String, String> reqHeaders, String encryptedString, Map<String, Object> valueObj)
			throws ServiceException;

}
