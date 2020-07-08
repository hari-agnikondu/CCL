/**
 * 
 *//*
package com.incomm.cclp.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.exception.ServiceException;

*//**
 * @author abutani
 *
 *//*
public class ValidationServiceTest {

	*//**
	 * @throws java.lang.Exception
	 *//*
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	*//**
	 * @throws java.lang.Exception
	 *//*
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	*//**
	 * @throws java.lang.Exception
	 *//*
	@Before
	public void setUp() throws Exception {
	}

	*//**
	 * @throws java.lang.Exception
	 *//*
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidateIssuer_With_NullIssuer() {
		
		IssuerDTO issuerDto = null;
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER +
				ResponseMessages.ERR_ISSUER_NULL;

		try {
			ValidationService.validateIssuer(issuerDto, false);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}

	@Test
	public void testValidateIssuer_With_NullIssuerValues() {
		
		IssuerDTO issuerDto = new IssuerDTO();
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_NAME + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_INS_USER + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_UPD_USER;		

		try {
			ValidationService.validateIssuer(issuerDto, true);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	
	@Test
	public void testValidateIssuer_With_NullIssuerName() {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerId(2);
		issuerDto.setInsUser(1);
		issuerDto.setLastUpdUser(1);
		issuerDto.setIsActive("true");
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_NAME + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_UPD_USER;	

		try {
			ValidationService.validateIssuer(issuerDto, false);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	
	@Test
	public void testValidateIssuer_With_EmptyIssuerName() {
		
		IssuerDTO issuerDto = new IssuerDTO();
		
		issuerDto.setIssuerId(2);
		issuerDto.setIssuerName(" ");
		issuerDto.setInsUser(1);
		issuerDto.setLastUpdUser(1);
		issuerDto.setIsActive(true);
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_NAME;	

		try {
			ValidationService.validateIssuer(issuerDto, false);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidateIssuer_With_InvalidInsUser() {
		
		IssuerDTO issuerDto = new IssuerDTO();
		
		issuerDto.setIssuerId(2);
		issuerDto.setIssuerName("BOA");
		issuerDto.setInsUser(-1);
		issuerDto.setLastUpdUser(1);
		issuerDto.setIsActive(true);
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_INS_USER;	

		try {
			ValidationService.validateIssuer(issuerDto, false);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidateIssuer_With_NullIssuerValues_for_Update() {
		
		IssuerDTO issuerDto = new IssuerDTO();
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_ID + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_NAME + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_INS_USER + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_UPD_USER;		

		try {
			ValidationService.validateIssuer(issuerDto, false);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidateIssuer_With_NullIssuerValues_And_ValidIssuerId_for_Update() {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerId(1);
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_NAME + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_INS_USER + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_UPD_USER;		

		try {
			ValidationService.validateIssuer(issuerDto, true);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidateIssuer_With_ValidValues() {
		
		IssuerDTO issuerDto = new IssuerDTO();
		
		issuerDto.setIssuerId(2);
		issuerDto.setIssuerName("BOA");
		issuerDto.setInsUser(1);
		issuerDto.setLastUpdUser(1);
		issuerDto.setIsActive(true);

		try {
			ValidationService.validateIssuer(issuerDto, false);
		} catch (ServiceException se) {
			assertEquals("", se.getMessage());
		}
	}
	
	 Partner Test Case 
	
	@Test
	public void testValidatePartner_With_NullPartner() {
		
		PartnerDTO partnerDto = null;
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER +
				ResponseMessages.ERR_PARTNER_NULL;

		try {
			ValidationService.validatePartner(partnerDto, false);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}

	@Test
	public void testValidatePartner_With_NullPartnerValues() {
		
		PartnerDTO partnerDto = new PartnerDTO();
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PARTNER_NAME;		

		try {
			ValidationService.validatePartner(partnerDto, true);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	
	@Test
	public void testValidatePartner_With_NullPartnerName() {
		
		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerId(2);
		partnerDto.setInsUser(1);
		partnerDto.setLastUpdUser(1);
		partnerDto.setIsActive("Y");
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PARTNER_NAME;	

		try {
			ValidationService.validatePartner(partnerDto, false);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	
	@Test
	public void testValidatePartner_With_EmptyPartnerName() {
		
		PartnerDTO partnerDto = new PartnerDTO();
		
		partnerDto.setPartnerId(2);
		partnerDto.setPartnerName(" ");
		partnerDto.setInsUser(1);
		partnerDto.setLastUpdUser(1);
		partnerDto.setIsActive("Y");
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PARTNER_NAME;	

		try {
			ValidationService.validatePartner(partnerDto, false);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidatePartner_With_InvalidInsUser() {
		
		PartnerDTO partnerDto = new PartnerDTO();
		
		partnerDto.setPartnerId(2);
		partnerDto.setPartnerName("BOA");
		partnerDto.setInsUser(-1);
		partnerDto.setLastUpdUser(1);
		partnerDto.setIsActive("Y");
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PARTNER_INS_USER;	

		try {
			ValidationService.validatePartner(partnerDto, false);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidatePartner_With_NullPartnerValues_for_Update() {
		
		PartnerDTO partnerDto = new PartnerDTO();
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PARTNER_ID + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PARTNER_NAME;	

		try {
			ValidationService.validatePartner(partnerDto, false);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidatePartner_With_NullPartnerValues_And_ValidPartnerId_for_Update() {
		
		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerId(1);
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PARTNER_NAME;		

		try {
			ValidationService.validatePartner(partnerDto, true);
		} catch (ServiceException se) {
			
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidatePartner_With_ValidValues() {
		
		PartnerDTO partnerDto = new PartnerDTO();
		
		partnerDto.setPartnerId(2);
		partnerDto.setPartnerName("BOA");
		partnerDto.setInsUser(1);
		partnerDto.setLastUpdUser(1);
		partnerDto.setIsActive("Y");

		try {
			ValidationService.validatePartner(partnerDto, false);
		} catch (ServiceException se) {
			assertEquals("", se.getMessage());
		}
	}
	
	
}
*/