package com.incomm.cclp.fsapi.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.incomm.cclp.exception.ServiceException;

@Service
public interface SerialNumberActivationService {

	public ResponseEntity<Object> serialNumberActivationProcess(Map<String, String> reqHeaders, Map<String, Object> valueObj)
			throws ServiceException;

}
