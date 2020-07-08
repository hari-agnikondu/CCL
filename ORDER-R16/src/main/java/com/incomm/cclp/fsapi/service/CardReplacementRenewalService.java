package com.incomm.cclp.fsapi.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.incomm.cclp.exception.ServiceException;

@Service
public interface CardReplacementRenewalService {
	
	public ResponseEntity<Object> cardReplacementRenewalProcess(Map<String, String> reqHeaders, String replacementReq, Map<String, Object> valueObj) throws ServiceException;
	
}
