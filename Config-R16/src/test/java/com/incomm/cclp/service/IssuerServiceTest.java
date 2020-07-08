
package com.incomm.cclp.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.IssuerService;

/**
 * @author abutani
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class IssuerServiceTest {

	@Autowired
	IssuerService issuerService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.cclp.service.impl.IssuerServiceImpl#createIssuer(com.cclp.dto.IssuerDTO)}.
	 * @throws ServiceException 
	 */
	@Test
	public void testCreateIssuer_With_ActiveFlag() throws ServiceException {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("true");
		
		issuerService.createIssuer(issuerDto);
		
		assertEquals(1, issuerService.getIssuersByName("Wells Fargo").size());
	}
	
	
	/**
	 * Test method for {@link com.cclp.service.impl.IssuerServiceImpl#createIssuer(com.cclp.dto.IssuerDTO)}.
	 * @throws ServiceException 
	 */
	@Test
	public void testCreateIssuer_Without_ActiveFlag() throws ServiceException {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerService.createIssuer(issuerDto);
		
		assertEquals(1, issuerService.getIssuersByName("Wells Fargo").size());
	}
	
	
	
	/**
	 * Test method for {@link com.cclp.service.impl.IssuerServiceImpl#createIssuer(com.cclp.dto.IssuerDTO)}.
	 * @throws ServiceException 
	 */
	@Test
	public void testCreateIssuer_Duplicate_Issuer() {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		
		IssuerDTO issuerDto1 = new IssuerDTO();
		issuerDto1.setIssuerName("Wells Fargo");
		
		try 
		{
			issuerService.createIssuer(issuerDto);
		} 
		catch (ServiceException e) {
		}
		
		String errorMsg = "ISS_"+ResponseMessages.ALREADY_EXISTS;
		try 
		{
			issuerService.createIssuer(issuerDto1);
		} 
		catch (ServiceException se) {
			
			assertEquals(errorMsg, se.getMessage());
		}
		
	}
	
	
	/**
	 * Test method for {@link com.cclp.service.impl.IssuerServiceImpl#createIssuer(com.cclp.dto.IssuerDTO)}.
	 * @throws ServiceException 
	 */
	@Test
	public void testCreateIssuer_Duplicate_Issuer_With_PartialNameMatch() {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo Bank");
		
		IssuerDTO issuerDto1 = new IssuerDTO();
		issuerDto1.setIssuerName("Wells Fargo");
		String errorMsg = "ISS_"+ResponseMessages.ALREADY_EXISTS;
		try 
		{
			issuerService.createIssuer(issuerDto);
		} 
		catch (ServiceException e) {
		}
		
		try 
		{
			issuerService.createIssuer(issuerDto1);
			
		} 
		catch (ServiceException se) {
			
			assertNotEquals(errorMsg, se.getMessage());
		}
		
	}
	
		
	@Test
	public void testGetAllIssuers() throws ServiceException {
		
		createMultipleIssuers();
		
		assertEquals(4, issuerService.getAllIssuers().size());
		
	}
	
	@Test
	public void testGetIssuersByName() throws ServiceException {
		
		createMultipleIssuers();
		
		assertEquals(2, issuerService.getIssuersByName("ells").size());
		
	}
	
	@Test
	public void testGetIssuersByName_WithNoMatch() throws ServiceException {
		
		createMultipleIssuers();
		
		assertEquals(0, issuerService.getIssuersByName("zo").size());
		
	}
	
	@Test
	public void testGetAllIssuers_VerifyRetrivedNames() throws ServiceException {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("true");
		issuerService.createIssuer(issuerDto);
		
		IssuerDTO issuerDto1 = new IssuerDTO();
		issuerDto1.setIssuerName("Bank Of America");
		issuerDto.setIsActive("true");;
		issuerService.createIssuer(issuerDto1);
		
		assertEquals(2, issuerService.getAllIssuers().size());
		
		assertEquals("Wells Fargo", issuerService.getIssuerById(issuerDto.getIssuerId()).
				getIssuerName());
		
		assertEquals("Bank Of America", issuerService.getIssuerById(issuerDto1.getIssuerId()).
				getIssuerName());
	}
	
	@Test
	public void testGetIssuerById() throws ServiceException {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Bank Of America");
		issuerDto.setIsActive("true");
		
		issuerService.createIssuer(issuerDto);
		
		assertEquals("Bank Of America", issuerService.getIssuerById(issuerDto.getIssuerId()).
				getIssuerName());
		
	}
	
	
 	/*@Test
	public void testGetIssuerById_With_NullIssuer() throws ServiceException {
		
		createMultipleIssuers();
		
		assertEquals(null, issuerService.getIssuerById(511));
		
	} 
	*/
	@Test
	public void testUpdateIssuer() throws ServiceException {
		
		createMultipleIssuers();
		
		IssuerDTO issuerDto = new IssuerDTO();
		
		issuerDto.setIssuerId(2);
		issuerDto.setIssuerName("AIG");
		issuerDto.setIsActive("true");
		
		issuerService.updateIssuer(issuerDto);;
		
		assertEquals(1, issuerService.getIssuersByName("AIG").size());
		
	}
	
	@Test
	public void testDeleteIssuer() throws ServiceException {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerId(1);
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("true");
		
		issuerService.deleteIssuer(issuerDto);
		
		assertEquals(issuerService.getAllIssuers().size(), 0);
		
	}
	
	@Test
	public void testDeleteIssuer_ExistingIssuer() throws ServiceException {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("true");
		
		issuerService.createIssuer(issuerDto);
		
		issuerService.deleteIssuer(issuerDto);
		
		assertEquals(issuerService.getAllIssuers().size(), 0);
		
	}
	
	private void createMultipleIssuers() throws ServiceException
	{
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("true");
		issuerService.createIssuer(issuerDto);
		
		IssuerDTO issuerDto1 = new IssuerDTO();
		issuerDto1.setIssuerName("Bank Of America");
		issuerDto.setIsActive("true");;
		issuerService.createIssuer(issuerDto1);
		
		IssuerDTO issuerDto2 = new IssuerDTO();
		issuerDto2.setIssuerName("Fells Wargo");
		issuerDto2.setIsActive("true");
		issuerService.createIssuer(issuerDto2);
		
		IssuerDTO issuerDto3 = new IssuerDTO();
		issuerDto3.setIssuerName("Chase");
		issuerDto3.setIsActive("false");
		issuerService.createIssuer(issuerDto3);
		
	}
	
	
@Test
	public void testUpdateIssuer_Duplicate_Issuer() {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("true");
		
		IssuerDTO issuerDto1 = new IssuerDTO();
		issuerDto1.setIssuerName("Bank Of America");
		issuerDto.setIsActive("true");;
		String errorMsg = "ISS_"+ResponseMessages.ALREADY_EXISTS;
		try
		{
			issuerService.createIssuer(issuerDto);
			issuerService.createIssuer(issuerDto1);
		}
		catch (ServiceException se) {
		}
		
		try
		{
			issuerDto1.setIssuerName("Wells Fargo");
			
			issuerService.updateIssuer(issuerDto1);
		}
		catch (ServiceException se) {
			
			assertEquals(errorMsg, se.getMessage());
		}
		
		assertEquals("Bank Of America", 
				issuerService.getIssuerById(issuerDto1.getIssuerId()).getIssuerName());
	}

}
