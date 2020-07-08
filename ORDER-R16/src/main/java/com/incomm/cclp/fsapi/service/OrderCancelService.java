package com.incomm.cclp.fsapi.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface OrderCancelService {

	public ResponseEntity<Object> orderCancelService(Map<String, Object> valueObj,Map<String, String> headerMap);

}
