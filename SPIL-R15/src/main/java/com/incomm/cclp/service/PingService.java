package com.incomm.cclp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public interface PingService {

	ResponseEntity<String> getServerDetails(MultiValueMap<String, String> reqHeaders, int functionCode);

}
