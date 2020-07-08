/**
 * 
 *//*
package com.incomm.cclp.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.ResponseDTO;

*//**
 * @author abutani
 *
 *//*
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ResponseBuilderTest {
	
	@Autowired
	ResponseBuilder responseBuilder;
	
	@Autowired
	LocaleHandler localeHandler;

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
	public void test_buildSuccessResponse() {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerId(1);
		issuerDto.setIssuerName("BOA");
		List<IssuerDTO> issuerDtos = new ArrayList<IssuerDTO>();
		issuerDtos.add(issuerDto);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(
				issuerDtos, ResponseMessages.SUCCESS_ISSUER_RETRIEVE);

		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.SUCCESS_RESPONSE),
				responseDto.getResult());
		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.SUCCESS_ISSUER_RETRIEVE),
				responseDto.getMessage());
		
		
	}
	
	@Test
	public void test_buildSuccessResponse_With_NullData() {

		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(
				null, ResponseMessages.SUCCESS_ISSUER_CREATE);

		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.SUCCESS_RESPONSE),
				responseDto.getResult());
		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.SUCCESS_ISSUER_CREATE),
				responseDto.getMessage());
		
		
	}
	
	@Test
	public void test_buildSuccessResponse_With_NullMessage() {

		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerId(1);
		issuerDto.setIssuerName("BOA");
		List<IssuerDTO> issuerDtos = new ArrayList<IssuerDTO>();
		issuerDtos.add(issuerDto);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(
				issuerDtos, null);

		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.SUCCESS_RESPONSE),
				responseDto.getResult());
		assertEquals(null, responseDto.getMessage());
		
		
	}
	
	
	@Test
	public void test_buildFailureResponse() {
		
		
		ResponseDTO responseDto = responseBuilder.buildFailureResponse(
				ResponseMessages.ERR_ISSUER_EXISTS);
		
		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.FAILURE_RESPONSE),
				responseDto.getResult());
		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.ERR_ISSUER_EXISTS),
				responseDto.getMessage());
		
		
	}
	
	@Test
	public void test_buildFailureResponse_With_NullMessage() {
		
		
		ResponseDTO responseDto = responseBuilder.buildFailureResponse(
				null);
		
		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.FAILURE_RESPONSE),
				responseDto.getResult());
		assertEquals(null, responseDto.getMessage());
		
		
	}
	
	 Partner Response Testcase 
	
	@Test
	public void testPartner_buildSuccessResponse() {
		
		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerId(1);
		partnerDto.setPartnerName("BOA");
		List<PartnerDTO> partnerDtos = new ArrayList<PartnerDTO>();
		partnerDtos.add(partnerDto);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(
				partnerDtos, ResponseMessages.SUCCESS_PARTNER_RETRIEVE);

		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.SUCCESS_RESPONSE),
				responseDto.getResult());
		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.SUCCESS_PARTNER_RETRIEVE),
				responseDto.getMessage());
		
		
	}
	
	@Test
	public void testPartner_buildSuccessResponse_With_NullData() {

		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(
				null, ResponseMessages.SUCCESS_PARTNER_CREATE);

		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.SUCCESS_RESPONSE),
				responseDto.getResult());
		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.SUCCESS_PARTNER_CREATE),
				responseDto.getMessage());
		
		
	}
	
	@Test
	public void testPartner_buildSuccessResponse_With_NullMessage() {

		
		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerId(1);
		partnerDto.setPartnerName("BOA");
		List<PartnerDTO> partnerDtos = new ArrayList<PartnerDTO>();
		partnerDtos.add(partnerDto);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(
				partnerDtos, null);

		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.SUCCESS_RESPONSE),
				responseDto.getResult());
		assertEquals(null, responseDto.getMessage());
		
		
	}
	
	
	@Test
	public void testPartner_buildFailureResponse() {
		
		
		ResponseDTO responseDto = responseBuilder.buildFailureResponse(
				ResponseMessages.ERR_PARTNER_EXISTS);
		
		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.FAILURE_RESPONSE),
				responseDto.getResult());
		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.ERR_PARTNER_EXISTS),
				responseDto.getMessage());
		
		
	}
	
	@Test
	public void testPartner_buildFailureResponse_With_NullMessage() {
		
		
		ResponseDTO responseDto = responseBuilder.buildFailureResponse(
				null);
		
		assertEquals(localeHandler.getLocalizedMessage(ResponseMessages.FAILURE_RESPONSE),
				responseDto.getResult());
		assertEquals(null, responseDto.getMessage());
		
		
	}

}
*/