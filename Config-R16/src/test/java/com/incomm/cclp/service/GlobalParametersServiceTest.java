package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.dao.AttributeDefinitionDAO;
import com.incomm.cclp.domain.AttributeDefinition;
import com.incomm.cclp.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GlobalParametersServiceTest {

	@Autowired
	GlobalParametersService globalParametersService;

	@Autowired
	AttributeDefinitionDAO attributeDefinitionDao;

	@Test
	public void testgetGlobalParameters() throws ServiceException {

		/* createMultipleAttributeDefinitions(); */

		assertEquals(8, globalParametersService.getGlobalParameters().size());

	}

	@Test
	public void testgetGlobalParameters_with_noGlobalParametersGroup() throws ServiceException {

		AttributeDefinition attributeDefinition = new AttributeDefinition();
		attributeDefinition.setAttributeGroup("Product");
		attributeDefinition.setAttributeName("b2bUpc");
		attributeDefinition.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition);

		assertEquals(0, globalParametersService.getGlobalParameters().size());
	}

	@Test
	public void testupdateGlobalParameters() throws ServiceException {

		createMultipleAttributeDefinitions();

		Map<String, Object> globalParameters = new HashMap<String, Object>();
		globalParameters.put("customerPasswordLength", "5");
		globalParameters.put("allowableWrongLogins", "4");

		globalParametersService.updateGlobalParameters(globalParameters);

		assertEquals("5", attributeDefinitionDao.getAttributeDefinitonByAttributeName("customerPasswordLength")
				.getAttributeValue());
		assertEquals("4", attributeDefinitionDao.getAttributeDefinitonByAttributeName("allowableWrongLogins")
				.getAttributeValue());
		assertEquals("", attributeDefinitionDao.getAttributeDefinitonByAttributeName("passwordChangeInterval")
				.getAttributeValue());

	}

	public void createMultipleAttributeDefinitions() {
		AttributeDefinition attributeDefinition = new AttributeDefinition();
		attributeDefinition.setAttributeGroup("Global Parameters");
		attributeDefinition.setAttributeName("customerPasswordLength");
		attributeDefinition.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition);

		AttributeDefinition attributeDefinition1 = new AttributeDefinition();
		attributeDefinition1.setAttributeGroup("Global Parameters");
		attributeDefinition1.setAttributeName("allowableWrongLogins");
		attributeDefinition1.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition1);

		AttributeDefinition attributeDefinition2 = new AttributeDefinition();
		attributeDefinition2.setAttributeGroup("Global Parameters");
		attributeDefinition2.setAttributeName("passwordChangeInterval");
		attributeDefinition2.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition2);

		AttributeDefinition attributeDefinition3 = new AttributeDefinition();
		attributeDefinition3.setAttributeGroup("Global Parameters");
		attributeDefinition3.setAttributeName("passwordLength");
		attributeDefinition3.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition3);

		AttributeDefinition attributeDefinition4 = new AttributeDefinition();
		attributeDefinition4.setAttributeGroup("Global Parameters");
		attributeDefinition4.setAttributeName("previousPasswords");
		attributeDefinition4.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition4);

		AttributeDefinition attributeDefinition5 = new AttributeDefinition();
		attributeDefinition5.setAttributeGroup("Global Parameters");
		attributeDefinition5.setAttributeName("maskingCharValue");
		attributeDefinition5.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition5);

		AttributeDefinition attributeDefinition6 = new AttributeDefinition();
		attributeDefinition6.setAttributeGroup("Global Parameters");
		attributeDefinition6.setAttributeName("hsm");
		attributeDefinition6.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition6);

		AttributeDefinition attributeDefinition7 = new AttributeDefinition();
		attributeDefinition7.setAttributeGroup("Global Parameters");
		attributeDefinition7.setAttributeName("dateFormat");
		attributeDefinition7.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition7);

		AttributeDefinition attributeDefinition8 = new AttributeDefinition();
		attributeDefinition8.setAttributeGroup("Product");
		attributeDefinition8.setAttributeName("limitSupported");
		attributeDefinition8.setAttributeValue("Y");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition8);
	}

}
