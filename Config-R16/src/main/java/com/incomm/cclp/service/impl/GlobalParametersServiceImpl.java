package com.incomm.cclp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.dao.AttributeDefinitionDAO;
import com.incomm.cclp.domain.AttributeDefinition;
import com.incomm.cclp.service.GlobalParametersService;

@Service
public class GlobalParametersServiceImpl implements GlobalParametersService {

	private static final Logger logger = LogManager.getLogger(GlobalParametersServiceImpl.class);

	@Autowired
	private AttributeDefinitionDAO attributeDefinitionDao;

	public static final String ATTRIBUTES_GROUP_GLOBAL_PARAMETERS = "Global Parameters";

	@Override
	public Map<String, Object> getGlobalParameters() {
		logger.info(CCLPConstants.ENTER);

		Map<String, Object> globalParameters = new HashMap<>();

		List<AttributeDefinition> globalParameterList = attributeDefinitionDao
				.getAttributeDefinitionsByGroupName(ATTRIBUTES_GROUP_GLOBAL_PARAMETERS);

		logger.debug("Retrived attribute defenitions : {}", globalParameterList);

		if (!CollectionUtils.isEmpty(globalParameterList)) {
			globalParameterList.stream().forEach(entry -> 
				globalParameters.put(entry.getAttributeName(), entry.getAttributeValue())
			);
		}

		logger.debug("Retrived Global Parameters : {}", globalParameters);
		logger.info(CCLPConstants.EXIT);

		return globalParameters;
	}

	@Override
	public void updateGlobalParameters(Map<String, Object> globalParameters) {
		logger.info(CCLPConstants.ENTER);

		globalParameters.entrySet().stream().forEach(globalParameter -> {
			AttributeDefinition attributeDefinition = new AttributeDefinition();
			attributeDefinition.setAttributeName(globalParameter.getKey());
			attributeDefinition.setAttributeValue(globalParameter.getValue().toString());
			attributeDefinition.setAttributeGroup(ATTRIBUTES_GROUP_GLOBAL_PARAMETERS);
			attributeDefinitionDao.updateAttributeDefinition(attributeDefinition);

		});

		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public void updateThreadProperties(int chunkSize, int threadPoolSize, int maxThreadPoolSize) {
		logger.info(CCLPConstants.ENTER);
		attributeDefinitionDao.updateAttributeDefinition(chunkSize,threadPoolSize,maxThreadPoolSize);
		logger.info(CCLPConstants.EXIT);
	}

}
