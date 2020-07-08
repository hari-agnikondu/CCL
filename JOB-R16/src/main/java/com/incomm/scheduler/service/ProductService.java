package com.incomm.scheduler.service;

import java.util.Map;

public interface ProductService {

	public Map<String, Map<String, Object>> getProductAttributes(String productId, String purseId);
}
