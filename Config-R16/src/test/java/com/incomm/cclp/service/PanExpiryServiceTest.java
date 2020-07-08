package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.dao.AttributeDefinitionDAO;
import com.incomm.cclp.domain.AttributeDefinition;
import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.ProductDTO;
import com.incomm.cclp.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PanExpiryServiceTest {

	@Autowired
	AttributeDefinitionDAO attributeDefinitionDao;

	@Autowired
	IssuerService issuerService;

	@Autowired
	PartnerService partnerService;

	@Autowired
	ProductService productService;

	IssuerDTO issuerDto;

	PartnerDTO partnerDto;

	ProductDTO productDto;

	@Before
	public void setUp() throws Exception {
		issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("ABC Bank");

		partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("Paytm");

		productDto = new ProductDTO();
		productDto.setProductName("Birthday Cards");

		createMultipleAttributeDefinitions();

	}

	@Test
	public void testGetPanExpiryAttributesByIdAndGroupname() throws ServiceException {
		issuerService.createIssuer(issuerDto);
		partnerService.createPartner(partnerDto);

		productDto.setIssuerId(issuerDto.getIssuerId());
		productDto.setPartnerId(partnerDto.getPartnerId());
		productService.createProduct(productDto);

		assertEquals(8, productService.getProductAttributesByIdAndGroupname(productDto.getProductId(), "PAN Expiry").size());
	}

	@Test
	public void testupdateProductAttributesByGroupName() throws ServiceException {
		issuerService.createIssuer(issuerDto);
		partnerService.createPartner(partnerDto);

		productDto.setIssuerId(issuerDto.getIssuerId());
		productDto.setPartnerId(partnerDto.getPartnerId());
		productService.createProduct(productDto);

		Map<String, Object> inAttributes = new HashMap<>();
		inAttributes.put("June", 5);
		inAttributes.put("April", 10);
		assertEquals(1, productService.updateProductAttributesByGroupName(inAttributes, productDto.getProductId(),
				"PAN Expiry"));
	}

	public void createMultipleAttributeDefinitions() {
		AttributeDefinition attributeDefinition = new AttributeDefinition();
		attributeDefinition.setAttributeGroup("PAN Expiry");
		attributeDefinition.setAttributeName("January");
		attributeDefinition.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition);

		AttributeDefinition attributeDefinition1 = new AttributeDefinition();
		attributeDefinition1.setAttributeGroup("PAN Expiry");
		attributeDefinition1.setAttributeName("Feburary");
		attributeDefinition1.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition1);

		AttributeDefinition attributeDefinition3 = new AttributeDefinition();
		attributeDefinition3.setAttributeGroup("PAN Expiry");
		attributeDefinition3.setAttributeName("April");
		attributeDefinition3.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition3);

		AttributeDefinition attributeDefinition4 = new AttributeDefinition();
		attributeDefinition4.setAttributeGroup("PAN Expiry");
		attributeDefinition4.setAttributeName("May");
		attributeDefinition4.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition4);

		AttributeDefinition attributeDefinition5 = new AttributeDefinition();
		attributeDefinition5.setAttributeGroup("PAN Expiry");
		attributeDefinition5.setAttributeName("June");
		attributeDefinition5.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition5);

		AttributeDefinition attributeDefinition2 = new AttributeDefinition();
		attributeDefinition2.setAttributeGroup("PAN Expiry");
		attributeDefinition2.setAttributeName("March");
		attributeDefinition2.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition2);

		AttributeDefinition attributeDefinition6 = new AttributeDefinition();
		attributeDefinition6.setAttributeGroup("PAN Expiry");
		attributeDefinition6.setAttributeName("July");
		attributeDefinition6.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition6);

		AttributeDefinition attributeDefinition7 = new AttributeDefinition();
		attributeDefinition7.setAttributeGroup("PAN Expiry");
		attributeDefinition7.setAttributeName("August");
		attributeDefinition7.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition7);
	}

}
