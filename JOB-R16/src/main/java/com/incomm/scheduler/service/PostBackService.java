package com.incomm.scheduler.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface PostBackService {

	public List<Map<String, Object>> postbackLogDetails();
	public String postBackJob();
	


}
