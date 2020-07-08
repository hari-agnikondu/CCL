package com.incomm.cclp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductPurseService {

	public List<String> updateProductPurseLimitAttributesByProgramId(Map<String, Object> inputAttributes, Long programId, Long purseId)
			 throws IOException;

	public List<String> updateProductPurseMonthlyCapAttributesByProgramId(Map<String, Object> inputAttributes,
			Long programId, Long purseId) throws IOException;

	public List<String> updateProductPurseTxnFeeAttributesByProgramId(Map<String, Object> inputAttributes,
			Long programId, Long purseId)throws IOException;

	
	
}
