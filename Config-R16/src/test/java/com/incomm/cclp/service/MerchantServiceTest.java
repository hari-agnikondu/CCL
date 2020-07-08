package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.domain.Issuer;
import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.dto.MerchantDTO;
import com.incomm.cclp.dto.MerchantProductDTO;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.ProductDTO;
import com.incomm.cclp.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MerchantServiceTest {

	@Autowired
	MerchantService merchantService;
	
	@Autowired
	IssuerService issuerService;
	
	@Autowired
	PartnerService partnerService;
	
	@Autowired
	ProductService productService;

	MerchantDTO merchantDto;
	
	MerchantDTO merchantDto1;
	
	IssuerDTO issuerDto;
	
	PartnerDTO partnerDto;
	
	ProductDTO productDto;
	
	MerchantProductDTO merchantProductDto;
	
	MerchantProductDTO merchantProductDto1;

	@Before
	public void setUp() throws Exception {
		merchantDto = new MerchantDTO();
		merchantDto.setMerchantName("ABC Pvt Ltd");
		merchantDto.setMdmId("ABC0002");
		
		merchantDto1 = new MerchantDTO();
		merchantDto1.setMerchantName("PreFly");
		merchantDto1.setMdmId("ABC0001");
		
		issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("ABC Bank");
		
		partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("Paytm");
		
		productDto = new ProductDTO();
		productDto.setProductName("Birthday Cards");
		
		merchantProductDto = new MerchantProductDTO();
		
		merchantProductDto1 = new MerchantProductDTO();
	}

	@Test
	public void testCreateMerchant() throws ServiceException {
		merchantService.createMerchant(merchantDto);

		assertEquals(1, merchantService.getAllMerchants().size());
	}

	@Test
	public void testCreateMerchant_Duplicate_MerchantName() throws ServiceException {
		merchantService.createMerchant(merchantDto);

		MerchantDTO merchantDto1 = new MerchantDTO();
		merchantDto1.setMerchantName("ABC Pvt Ltd");

		String errorMessage = "MER_" + ResponseMessages.ALREADY_EXISTS;

		try {
			merchantService.createMerchant(merchantDto1);
		} catch (Exception e) {
			assertEquals(errorMessage, e.getMessage());
		}
	}
	
	@Test
	public void testGetAllMerchants() throws ServiceException {		
		merchantService.createMerchant(merchantDto);
		
		assertEquals(1, merchantService.getAllMerchants().size());		
	}
	
	@Test
	public void testGetMerchantById() throws ServiceException {
		merchantService.createMerchant(merchantDto);
		
		assertEquals(merchantDto.getMerchantName(), 
				merchantService.getMerchantById(merchantDto.getMerchantId()).getMerchantName());	
	}
	
	@Test
	public void testGetMerchantByName() throws ServiceException {
		merchantService.createMerchant(merchantDto);
		
		assertEquals(1, merchantService.getMerchantsByName(merchantDto.getMerchantName()).size());	
	}
	
	@Test
	public void testDeleteMerchantById() throws ServiceException {
		merchantService.createMerchant(merchantDto);
		
		merchantService.deleteMerchantById(merchantDto.getMerchantId());
		
		assertEquals(null, merchantService.getMerchantById(merchantDto.getMerchantId()));	
	}
	
	@Test
	public void testUpdateMerchant() throws ServiceException {
		merchantService.createMerchant(merchantDto);
		
		merchantDto.setMerchantName("Incomm");
		
		merchantService.updateMerchant(merchantDto);
		
		assertEquals(1, merchantService.getMerchantsByName("Incomm").size());	
	}
	
	@Test
	public void testAssignProductToMerchant() throws ServiceException {
		merchantService.createMerchant(merchantDto);
		issuerService.createIssuer(issuerDto);
		partnerService.createPartner(partnerDto);
		
		productDto.setIssuerId(issuerDto.getIssuerId());
		productDto.setPartnerId(partnerDto.getPartnerId());
		productService.createProduct(productDto);
		
		merchantProductDto.setMerchantId(merchantDto.getMerchantId());
		merchantProductDto.setProductId(productDto.getProductId());
		merchantService.assignProductToMerchant(merchantProductDto);
		
		assertEquals(1, merchantService.getMerchantProducts("ABC Pvt Ltd", "Birthday Cards").size());	
	}
	
	@Test
	public void testgetMerchantProducts_EmptySearch() throws ServiceException {
		merchantService.createMerchant(merchantDto);
		merchantService.createMerchant(merchantDto1);
		issuerService.createIssuer(issuerDto);
		partnerService.createPartner(partnerDto);
		
		productDto.setIssuerId(issuerDto.getIssuerId());
		productDto.setPartnerId(partnerDto.getPartnerId());
		productService.createProduct(productDto);
		
		merchantProductDto.setMerchantId(merchantDto.getMerchantId());
		merchantProductDto.setProductId(productDto.getProductId());
		merchantService.assignProductToMerchant(merchantProductDto);
		
		merchantProductDto1.setMerchantId(merchantDto1.getMerchantId());
		merchantProductDto1.setProductId(productDto.getProductId());
		merchantService.assignProductToMerchant(merchantProductDto1);
		
		assertEquals(2, merchantService.getMerchantProducts("*", "*").size());	
	}
	
	@Test
	public void testgetMerchantProducts_With_ProductName() throws ServiceException {
		merchantService.createMerchant(merchantDto);
		merchantService.createMerchant(merchantDto1);
		issuerService.createIssuer(issuerDto);
		partnerService.createPartner(partnerDto);
		
		productDto.setIssuerId(issuerDto.getIssuerId());
		productDto.setPartnerId(partnerDto.getPartnerId());
		productService.createProduct(productDto);
		
		merchantProductDto.setMerchantId(merchantDto.getMerchantId());
		merchantProductDto.setProductId(productDto.getProductId());
		merchantService.assignProductToMerchant(merchantProductDto);
		
		merchantProductDto1.setMerchantId(merchantDto1.getMerchantId());
		merchantProductDto1.setProductId(productDto.getProductId());
		merchantService.assignProductToMerchant(merchantProductDto1);
		
		assertEquals(2, merchantService.getMerchantProducts("*", "Birthday").size());	
	}
	
	@Test
	public void testgetMerchantProducts_With_MerchantName() throws ServiceException {
		merchantService.createMerchant(merchantDto);
		merchantService.createMerchant(merchantDto1);
		issuerService.createIssuer(issuerDto);
		partnerService.createPartner(partnerDto);
		
		productDto.setIssuerId(issuerDto.getIssuerId());
		productDto.setPartnerId(partnerDto.getPartnerId());
		productService.createProduct(productDto);
		
		merchantProductDto.setMerchantId(merchantDto.getMerchantId());
		merchantProductDto.setProductId(productDto.getProductId());
		merchantService.assignProductToMerchant(merchantProductDto);
		
		merchantProductDto1.setMerchantId(merchantDto1.getMerchantId());
		merchantProductDto1.setProductId(productDto.getProductId());
		merchantService.assignProductToMerchant(merchantProductDto1);
		
		assertEquals(1, merchantService.getMerchantProducts("PreFly", "*").size());	
	}
	
	@Test
	public void testRemoveMerchantProductMapping() throws ServiceException {
		merchantService.createMerchant(merchantDto);
		merchantService.createMerchant(merchantDto1);
		issuerService.createIssuer(issuerDto);
		partnerService.createPartner(partnerDto);
		
		productDto.setIssuerId(issuerDto.getIssuerId());
		productDto.setPartnerId(partnerDto.getPartnerId());
		productService.createProduct(productDto);
		
		merchantProductDto.setMerchantId(merchantDto.getMerchantId());
		merchantProductDto.setProductId(productDto.getProductId());
		merchantService.assignProductToMerchant(merchantProductDto);
		
		merchantProductDto1.setMerchantId(merchantDto1.getMerchantId());
		merchantProductDto1.setProductId(productDto.getProductId());
		merchantService.assignProductToMerchant(merchantProductDto1);
		
		merchantService.removeMerchantProductMapping(merchantProductDto);
		
		assertEquals(1, merchantService.getMerchantProducts("*", "*").size());	
	}
}
