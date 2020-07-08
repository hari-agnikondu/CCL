package com.incomm.cclp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.controller.BlockListController;
import com.incomm.cclp.dao.AttributeDefinitionDAO;
import com.incomm.cclp.domain.AttributeDefinition;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.AttributeDefinitionService;

@Service
@Component
public class AttributeDefinitionServiceImpl implements AttributeDefinitionService {

	@Autowired
	private AttributeDefinitionDAO attributeDefinitionDao;
	
	private static final Logger logger = LogManager.getLogger(AttributeDefinitionServiceImpl.class);

	@Override
	public Map<String, Map<String, Object>> getAllAttributeDefinitions() {
		logger.info(CCLPConstants.ENTER);
		 List<AttributeDefinition> attributeDefinitionList = attributeDefinitionDao.getAllAttributeDefinitions();
		 logger.info(CCLPConstants.EXIT);
		return convertToHashMap(attributeDefinitionList);
	}

	@Override
	public Map<Double, String> getAttributeDefinitionsByOrder() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		List<AttributeDefinition> attributeDefinitionList = attributeDefinitionDao.getAllAttributeDefinitions();
		Map<Double, String> attrributeOrder = new TreeMap<Double, String>();

		attributeDefinitionList.stream().forEach(attrDefOut -> {
			if (attrDefOut.getAttributeOrder() != null) {
				attrributeOrder.put(Double.valueOf(attrDefOut.getAttributeOrder()),
						String.valueOf(attrDefOut.getAttributeGroup() + "|" + attrDefOut.getAttributeName() + "|"
								+ attrDefOut.getDescription()));
			}

		});
		attrributeOrder.put(8.01,
				"Card Status|Card Status|Card Status");
		attrributeOrder.put(9.01,
				"Maintenance Fees|Maintenance Fees|Maintenance Fees");
		attrributeOrder.put(11.01,
				"ProductPurse|ProductPurse|ProductPurse");
		logger.info(CCLPConstants.EXIT);
		return attrributeOrder;

	}

	@Override
	public Map<String, Map<String, Object>> getAllCardAttributeDefinitions() {
		logger.info(CCLPConstants.ENTER);
		List<AttributeDefinition> attributeDefinitionList = attributeDefinitionDao.getAllCardAttributeDefinitions();
		logger.info(CCLPConstants.EXIT);
		return convertToHashMap(attributeDefinitionList);
	}
	
	Map<String, Map<String, Object>> convertToHashMap(List<AttributeDefinition> attributeDefinitionList) {
		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String, Object>> attributeMap = new HashMap<>();
		if (!attributeDefinitionList.isEmpty()) {

			attributeDefinitionList.forEach(attributeDef -> {

				String attributeGroup = attributeDef.getAttributeGroup();

				Map<String, Object> groupMap = null;
				if (!attributeMap.containsKey(attributeGroup)) { // Check whether group name is not present
					groupMap = new HashMap<>();
					attributeMap.put(attributeGroup, groupMap);
				} else { // If group name is present
					groupMap = attributeMap.get(attributeGroup);
				}

				groupMap.put(attributeDef.getAttributeName(), attributeDef.getAttributeValue());

			});
		}
		logger.info(CCLPConstants.EXIT);
		return attributeMap;
	}

}
