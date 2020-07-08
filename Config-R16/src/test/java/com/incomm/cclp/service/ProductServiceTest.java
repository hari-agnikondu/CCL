package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.ProductDTO;
import com.incomm.cclp.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductServiceTest {

	@Autowired
	IssuerService issuerService;

	@Autowired
	PartnerService partnerService;

	@Autowired
	ProductService productService;
	
	// the Product dao.
	@Autowired
	ProductDAO productDao;
	
	/*@Test
	public void testGetAllProducts() throws ServiceException{

		createMultipleProducts();

		assertEquals(2, productService.getAllProducts().size());
	}

	@Test
	public void testGetProductsByName() throws ServiceException{

		createMultipleProducts();

		assertEquals(1, productService.getProductsByName("Ha").size());
	}

	private void createMultipleProducts() throws ServiceException{
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("Y");
		issuerService.createIssuer(issuerDto);

		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("World travels");
		partnerDto.setPartnerDesc("Travel anywhere in the world");
		partnerDto.setIsActive("Y");
		partnerDto.setMdmId("123");
		partnerService.createPartner(partnerDto);

		ProductDTO productDto = new ProductDTO();
		productDto.setProductName("Happy World");
		productDto.setDescription("Travel to world");
		productDto.setAttributes(null);
		productDto.setIsActive("Y");
		productDto.setParentProductId(null);
		productDto.setParentProductName(null);
		productDto.setIssuerId(issuerDto.getIssuerId());
		productDto.setIssuerName(issuerDto.getIssuerName());
		productDto.setPartnerId(partnerDto.getPartnerId());
		productDto.setPartnerName(partnerDto.getPartnerName());		
		productService.createProduct(productDto);

		ProductDTO productDto1 = new ProductDTO();
		productDto1.setProductName("My zone");
		productDto1.setDescription("Spend money every where");
		productDto1.setAttributes(null);
		productDto1.setIsActive("Y");
		productDto1.setParentProductId(null);
		productDto1.setParentProductName(null);
		productDto1.setIssuerId(issuerDto.getIssuerId());
		productDto1.setIssuerName(issuerDto.getIssuerName());
		productDto1.setPartnerId(partnerDto.getPartnerId());
		productDto1.setPartnerName(partnerDto.getPartnerName());		
		productService.createProduct(productDto1);
	}
*/

	/*General Tab Test cases Starts*/
/*	@Test
	public void getVaid_ParentProductsMap() {
		Map<Long,String> parentProductMap=productService.getParentProducts();
		assertEquals(true,parentProductMap.size()>0);
	}*/
	
	@Test
	public void getProductGeneralDetails_with_valid_productID() throws ServiceException  {
		try {
			IssuerDTO issuerDto = new IssuerDTO();
			issuerDto.setIssuerName("Wells Fargo");
			issuerDto.setIsActive("Y");
			issuerService.createIssuer(issuerDto);
			
			PartnerDTO partnerDto = new PartnerDTO();
			partnerDto.setPartnerName("World travels");
			partnerDto.setPartnerDesc("Travel anywhere in the world");
			partnerDto.setIsActive("Y");
			partnerDto.setMdmId("123");
			partnerService.createPartner(partnerDto);
			
			Map<String,Object> attributes=new HashMap<String, Object>();
			attributes.put("activationCode", "Y");
			attributes.put("cardExpiryPendingDays", "20");
			attributes.put("cardRenewReplaceProd", "Renewal");
			attributes.put("customerCareNbr", "1002334");
			attributes.put("defaultCardStatus", "1");
			attributes.put("emailIdStatement", "a@incomm.com");
			
			attributes.put("maxCardsPerCust", "100");
			
			attributes.put("preAuthExpiryPeriod", "20");
			attributes.put("roboHelpUrl", "http://roboURL");
			attributes.put("serviceCode", "100");
			attributes.put("statementFooter", "20");
			attributes.put("preAuthExpiryPeriod", "@copy rights 2018");
			attributes.put("txnCountRecentStatement", "200");
			
			Map<String, Map<String, Object>> productAttributes=new HashMap<>();
			productAttributes.put("General", attributes);
			
			ProductDTO productDto = new ProductDTO();
			productDto.setProductName("Happy World");
			productDto.setDescription("Travel to world");
			productDto.setAttributes(new ObjectMapper().writeValueAsString(productAttributes));
			productDto.setIsActive("Y");
			productDto.setParentProductId(null);
			productDto.setParentProductName(null);
			productDto.setIssuerId(issuerDto.getIssuerId());
			productDto.setIssuerName(issuerDto.getIssuerName());
			productDto.setPartnerId(partnerDto.getPartnerId());
			productDto.setPartnerName(partnerDto.getPartnerName());		
			productService.createProduct(productDto);
			
			ProductDTO existingProductDto=productService.getProductById(productDto.getProductId());
			Map<String, Map<String, Object>> productAttributes1=new ObjectMapper().readValue(existingProductDto.getAttributes(),
					new TypeReference<Map<String, Map<String, Object>>>(){});
			Map<String,Object> generalDetails=productAttributes1.get("General");
			assertEquals(true,generalDetails.size()>1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateProductGeneralDetails_with_valid_productID_Fail_case() throws ServiceException  {
		try {
			IssuerDTO issuerDto = new IssuerDTO();
			issuerDto.setIssuerName("Wells Fargo");
			issuerDto.setIsActive("Y");
			issuerService.createIssuer(issuerDto);
			
			PartnerDTO partnerDto = new PartnerDTO();
			partnerDto.setPartnerName("World travels");
			partnerDto.setPartnerDesc("Travel anywhere in the world");
			partnerDto.setIsActive("Y");
			partnerDto.setMdmId("123");
			partnerService.createPartner(partnerDto);
			
			Map<String,Object> attributes=new HashMap<String, Object>();
			attributes.put("activationCode", "Y");
			attributes.put("cardExpiryPendingDays", "20");
			attributes.put("cardRenewReplaceProd", "Renewal");
			attributes.put("customerCareNbr", "1002334");
			attributes.put("defaultCardStatus", "1");
			attributes.put("emailIdStatement", "a@incomm.com");
			
			attributes.put("maxCardsPerCust", "100");
			
			attributes.put("preAuthExpiryPeriod", "20");
			attributes.put("roboHelpUrl", "http://roboURL");
			attributes.put("serviceCode", "100");
			attributes.put("statementFooter", "20");
			attributes.put("preAuthExpiryPeriod", "@copy rights 2018");
			attributes.put("txnCountRecentStatement", "200");
			
			Map<String, Map<String, Object>> productAttributes=new HashMap<>();
			productAttributes.put("General", attributes);
			
			ProductDTO productDto = new ProductDTO();
			productDto.setProductName("Happy World");
			productDto.setDescription("Travel to world");
			productDto.setAttributes(new ObjectMapper().writeValueAsString(productAttributes));
			productDto.setIsActive("Y");
			productDto.setParentProductId(null);
			productDto.setParentProductName(null);
			productDto.setIssuerId(issuerDto.getIssuerId());
			productDto.setIssuerName(issuerDto.getIssuerName());
			productDto.setPartnerId(partnerDto.getPartnerId());
			productDto.setPartnerName(partnerDto.getPartnerName());		
			productService.createProduct(productDto);
			
			Map<String,Object> genAttributes=new HashMap<String, Object>();
			genAttributes.put("activationCode", "Y");
			genAttributes.put("cardExpiryPendingDays", "20");
			genAttributes.put("cardRenewReplaceProd", "Renewal");
			genAttributes.put("customerCareNbr", "1002334");
			genAttributes.put("defaultCardStatus", "1");
			genAttributes.put("emailIdStatement", "a@incomm.com");
			
			genAttributes.put("maxCardsPerCust", "100");
			
			genAttributes.put("preAuthExpiryPeriod", "20");
			genAttributes.put("roboHelpUrl", "http://roboURL123");
			genAttributes.put("serviceCode", "100");
			genAttributes.put("statementFooter", "20");
			genAttributes.put("preAuthExpiryPeriod", "@copy rights 2018");
			genAttributes.put("txnCountRecentStatement", "200");
			
			ProductDTO existingProductDto=productService.getProductById(productDto.getProductId());
			Map<String, Map<String, Object>> productAttributes1=new ObjectMapper().readValue(existingProductDto.getAttributes(),
					new TypeReference<Map<String, Map<String, Object>>>(){});
			productAttributes1.put("General", genAttributes);
			String productAttributesString=	new ObjectMapper().writeValueAsString(productAttributes1);
			ProductDTO productDto1=new ProductDTO();
			productDto1.setAttributes(productAttributesString);
			productDto1.setProductId(existingProductDto.getProductId());
			assertEquals(0,productDao.updateProductAttributes(productDto1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*General Tab Test cases Ends */
	
	@Test
	public void getProductCvvDetails_with_valid_productID() throws ServiceException  {
		try {
			IssuerDTO issuerDto = new IssuerDTO();
			issuerDto.setIssuerName("Wells Fargo");
			issuerDto.setIsActive("Y");
			issuerService.createIssuer(issuerDto);
			
			PartnerDTO partnerDto = new PartnerDTO();
			partnerDto.setPartnerName("World travels");
			partnerDto.setPartnerDesc("Travel anywhere in the world");
			partnerDto.setIsActive("Y");
			partnerDto.setMdmId("123");
			partnerService.createPartner(partnerDto);
			
			Map<String,Object> attributes=new HashMap<String, Object>();
			attributes.put("embossApplicable", "Y");
			attributes.put("cvkFormat", "HostStroed");
			attributes.put("cardVerifyType", "CVV");
			attributes.put("cvkKeySpecifier", "cvkKeySpecifierChecking");
			attributes.put("cvkA", "cvkAChecking");
			attributes.put("cvkB", "cvkBChecking");
			
			
			
			Map<String, Map<String, Object>> productAttributes=new HashMap<>();
			productAttributes.put("CVV", attributes);
			
			ProductDTO productDto = new ProductDTO();
			productDto.setProductName("Happy World");
			productDto.setDescription("Travel to world");
			productDto.setAttributes(new ObjectMapper().writeValueAsString(productAttributes));
			productDto.setIsActive("Y");
			productDto.setParentProductId(null);
			productDto.setParentProductName(null);
			productDto.setIssuerId(issuerDto.getIssuerId());
			productDto.setIssuerName(issuerDto.getIssuerName());
			productDto.setPartnerId(partnerDto.getPartnerId());
			productDto.setPartnerName(partnerDto.getPartnerName());		
			productService.createProduct(productDto);
			
			ProductDTO existingProductDto=productService.getProductById(productDto.getProductId());
			Map<String, Map<String, Object>> productAttributes1=new ObjectMapper().readValue(existingProductDto.getAttributes(),
					new TypeReference<Map<String, Map<String, Object>>>(){});
			Map<String,Object> cvvDetails=productAttributes1.get("CVV");
			assertEquals(true,cvvDetails.size()>1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateProductCvvDetails_with_valid_productID_Fail_case() throws ServiceException  {
		try {
			IssuerDTO issuerDto = new IssuerDTO();
			issuerDto.setIssuerName("Wells Fargo");
			issuerDto.setIsActive("Y");
			issuerService.createIssuer(issuerDto);
			
			PartnerDTO partnerDto = new PartnerDTO();
			partnerDto.setPartnerName("World travels");
			partnerDto.setPartnerDesc("Travel anywhere in the world");
			partnerDto.setIsActive("Y");
			partnerDto.setMdmId("123");
			partnerService.createPartner(partnerDto);
			
			Map<String,Object> attributes=new HashMap<String, Object>();
			attributes.put("embossApplicable", "Y");
			attributes.put("cvkFormat", "HostStroed");
			attributes.put("cardVerifyType", "CVV");
			attributes.put("cvkKeySpecifier", "cvkKeySpecifierChecking");
			attributes.put("cvkA", "cvkAChecking");
			attributes.put("cvkB", "cvkBChecking");
			
			Map<String, Map<String, Object>> productAttributes=new HashMap<>();
			productAttributes.put("CVV", attributes);
			
			ProductDTO productDto = new ProductDTO();
			productDto.setProductName("Happy World");
			productDto.setDescription("Travel to world");
			productDto.setAttributes(new ObjectMapper().writeValueAsString(productAttributes));
			productDto.setIsActive("Y");
			productDto.setParentProductId(null);
			productDto.setParentProductName(null);
			productDto.setIssuerId(issuerDto.getIssuerId());
			productDto.setIssuerName(issuerDto.getIssuerName());
			productDto.setPartnerId(partnerDto.getPartnerId());
			productDto.setPartnerName(partnerDto.getPartnerName());		
			productService.createProduct(productDto);
			
			Map<String,Object> genAttributes=new HashMap<String, Object>();
			attributes.put("embossApplicable", "Y");
			attributes.put("cvkFormat", "HostStroed11111");
			attributes.put("cardVerifyType", "CVV1111111");
			attributes.put("cvkKeySpecifier", "cvkKeySpecifierChecking111111");
			attributes.put("cvkA", "cvkAChecking11111111");
			attributes.put("cvkB", "cvkBChecking111111");
			
			ProductDTO existingProductDto=productService.getProductById(productDto.getProductId());
			Map<String, Map<String, Object>> productAttributes1=new ObjectMapper().readValue(existingProductDto.getAttributes(),
					new TypeReference<Map<String, Map<String, Object>>>(){});
			productAttributes1.put("CVV", genAttributes);
			String productAttributesString=	new ObjectMapper().writeValueAsString(productAttributes1);
			ProductDTO productDto1=new ProductDTO();
			productDto1.setAttributes(productAttributesString);
			productDto1.setProductId(existingProductDto.getProductId());
			assertEquals(0,productDao.updateProductAttributes(productDto1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
	


