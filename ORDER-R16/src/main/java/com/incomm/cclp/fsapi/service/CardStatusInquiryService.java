package com.incomm.cclp.fsapi.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface CardStatusInquiryService {

	public ResponseEntity<Object> cardStatusInquiry(Map<String, Object> valueObj);

}
