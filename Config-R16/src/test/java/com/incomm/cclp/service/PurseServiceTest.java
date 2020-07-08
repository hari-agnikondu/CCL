/**
 * 
 */
package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.MasterDao;
import com.incomm.cclp.dao.PurseDAO;
import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.PurseType;
import com.incomm.cclp.dto.CurrencyCodeDTO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.util.ValidationService;


/**
 * @author abutani
 *
 */


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PurseServiceTest {

	@Autowired
	PurseService purseService;
	
	@Autowired
	IssuerService issuerService;
	
	@Autowired
	MasterDao masterdao;
	
	@Autowired
	PurseDAO pursedao;


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
	

	
	@Test
	public void testValidatePurse_With_NullObject() {

		PurseDTO purseDto = null;
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PURSE_NULL;

		try {
			ValidationService.validatePurse(purseDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}

	@Test
	public void testValidatePurse_With_NullPurseValues() {

		PurseDTO purseDto = new PurseDTO();
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PURSETYPEID_NULL +  
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PURSE_NULL +
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_INSUSER_NULL ;
		try {
			ValidationService.validatePurse(purseDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidatePurse_With_CurrencyPurseType_NullCurrencyId() {

		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseId(Long.valueOf(1));
		purseDto.setPurseTypeId(1);
		purseDto.setInsUser(1);
		purseDto.setExtPurseId("TestCurrencyPurse");
		purseDto.setLastUpdUser(1);
		/*No CuurencyID Present*/
		
		String expectedMsg = 
				ResponseMessages.VALIDATION_HEADER_MSG + ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CURRENCYCODE_NULL;
		try {
			ValidationService.validatePurse(purseDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidatePurse_With_CurrencyPurseType_NullExtPurseId() {

		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseId(Long.valueOf(1));
		purseDto.setPurseTypeId(1);
		purseDto.setCurrencyTypeID("840");
		purseDto.setInsUser(1);
		purseDto.setLastUpdUser(1);
		
		String expectedMsg = 
				ResponseMessages.VALIDATION_HEADER_MSG + ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PURSE_NULL;
		try {
			ValidationService.validatePurse(purseDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidatePurse_With_CurrencyPurseType_NullCurrencyCode() {

		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseId(Long.valueOf(2));
		purseDto.setPurseTypeId(1);
		purseDto.setDescription("Null CurrencyCode");
		purseDto.setInsUser(1);
		purseDto.setExtPurseId("TestCurrencyPurse");
		purseDto.setLastUpdUser(1);
		/*No CuurencyID Present*/
		
		String expectedMsg = 
				ResponseMessages.VALIDATION_HEADER_MSG + ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CURRENCYCODE_NULL;
		try {
			ValidationService.validatePurse(purseDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidatePurse_With_CurrencyPurseType_EmptyCurrencyCode() {

		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseId(Long.valueOf(3));
		purseDto.setPurseTypeId(1);
		purseDto.setDescription("Empty CurrencyCode");
		purseDto.setCurrencyTypeID("");
		purseDto.setInsUser(1);
		purseDto.setLastUpdUser(1);
		purseDto.setExtPurseId("DollarPurse");
		purseDto.setHotCardThreshold(0);
		String expectedMsg = 
				ResponseMessages.VALIDATION_HEADER_MSG + ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CURRENCYCODE_NULL;
		try {
			ValidationService.validatePurse(purseDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidatePurse_With_CurrencyPurseType_EmptyUpc() {

		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseId(Long.valueOf(4));
		purseDto.setPurseTypeId(3);
		purseDto.setDescription("Empty UPC");
		purseDto.setUpc("");
		purseDto.setInsUser(1);
		purseDto.setExtPurseId("Test_Empty_UPC");
		purseDto.setHotCardThreshold(0);
		purseDto.setLastUpdUser(1);
		
		String expectedMsg = 
				ResponseMessages.VALIDATION_HEADER_MSG + ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_UPC_NULL;
		try {
			ValidationService.validatePurse(purseDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
//	@Test
//	public void testValidatePurse_With_CurrencyPurseType_NullUpc() {
//
//		PurseDTO purseDto = new PurseDTO();
//		purseDto.setPurseId(Long.valueOf(5));
//		purseDto.setPurseTypeId(3);
//		purseDto.setDescription("Empty UPC");
//		purseDto.setInsUser(1);
//		purseDto.setExtPurseId("TestCurrencyPurse");
//		purseDto.setLastUpdUser(1);
//		/*No UPC*/
//		
//		String expectedMsg = 
//				ResponseMessages.VALIDATION_HEADER_MSG + ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_UPC_NULL;
//		try {
//			ValidationService.validatePurse(purseDto, true);
//		} catch (ServiceException se) {
//
//			assertEquals(expectedMsg, se.getMessage());
//		}
//	}
	
	@Test
	public void testValidatePurse_With_CurrencyPurseType_ZeroPurseType() {

		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseId(Long.valueOf(6));
		purseDto.setPurseTypeId(0);
		purseDto.setDescription("Zero Purse Type");
		purseDto.setInsUser(1);
		purseDto.setExtPurseId("TestCurrencyPurse");
		purseDto.setLastUpdUser(1);
		
		String expectedMsg = 
				ResponseMessages.VALIDATION_HEADER_MSG + ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PURSETYPEID_NULL;
		try {
			ValidationService.validatePurse(purseDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	

	
	@Test
	public void testValidatePurse_With_InvalidInsUser() {
		
		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseId(Long.valueOf(1));
		purseDto.setPurseTypeId(1);
		purseDto.setCurrencyTypeID("840");
		purseDto.setDescription("Zero Purse Type");
		purseDto.setExtPurseId("TestCurrencyPurse");
		purseDto.setInsUser(-1);
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_INSUSER_NULL;	
		try {
			ValidationService.validatePurse(purseDto, true);
		} catch (ServiceException se) {
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	
	@Test
	public void testValidatePurse_With_LoyaltyPurseType_ValueCurrency() {
		
		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseId(Long.valueOf(1));
		purseDto.setPurseTypeId(2);
		purseDto.setDescription("Loyalty with currency Id");
		purseDto.setCurrencyTypeID("840");
		purseDto.setExtPurseId("TestCurrencyPurse");
		purseDto.setInsUser(1);
		purseDto.setLastUpdUser(1);
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_LOYALTY_NULL;	
		try {
			ValidationService.validatePurse(purseDto, true);
		} catch (ServiceException se) {
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidatePurse_With_LoyaltyPurseType_ValueUPC() {
		
		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseId(Long.valueOf(1));
		purseDto.setPurseTypeId(2);
		purseDto.setDescription("Loyalty with UPC Value");
		purseDto.setUpc("546798769454");
		purseDto.setExtPurseId("TestCurrencyPurse");
		purseDto.setInsUser(1);
		purseDto.setLastUpdUser(1);
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_LOYALTY_NULL;	
		try {
			ValidationService.validatePurse(purseDto, true);
		} catch (ServiceException se) {
			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	
	@Test
	public void testValidatePurse_With_CreatePurse() throws ServiceException {
		
		PurseType pursetype = new PurseType();
			pursetype.setPurseTypeName("UPC");
			masterdao.createPurseType(pursetype);
		
		PurseDTO purseDto = new PurseDTO();
		
		purseDto.setPurseTypeId(pursetype.getPurseTypeId());
		purseDto.setDescription("Insert New Purse value");
		purseDto.setUpc("546798769454");
		purseDto.setInsUser(1);
		purseDto.setLastUpdUser(1);
		purseDto.setExtPurseId("TestCurrencyPurse");
		purseDto.setHotCardThreshold(0);
		purseService.createPurse(purseDto);
		
		PurseDTO DTO = purseService.getPurseById(purseDto.getPurseId());
		
		assertEquals(purseDto.getUpc(), DTO.getUpc());
	}
	
	@Test
	public void testValidatePurse_With_CurrencyCreatePurse()throws ServiceException {

		PurseType pursetype = new PurseType();
		pursetype.setPurseTypeName("CURRENCY");
		masterdao.createPurseType(pursetype);
		CurrencyCodeDTO currencyCodeDTO=new CurrencyCodeDTO();
		currencyCodeDTO.setCurrencyTypeID("356");
		currencyCodeDTO.setCurrCodeAlpha("IND");
		CurrencyCode cc=new ModelMapper().map(currencyCodeDTO, CurrencyCode.class);
		masterdao.createCurrency(cc);
		

		PurseDTO purseDto = new PurseDTO();

		purseDto.setPurseTypeId(pursetype.getPurseTypeId());
		purseDto.setDescription("Insert New Purse value");
		purseDto.setInsUser(1);
		purseDto.setLastUpdUser(1);
		purseDto.setCurrencyTypeID(cc.getCurrencyTypeID());
		purseDto.setExtPurseId("TestCurrencyPurse");
		purseDto.setHotCardThreshold(0);
		purseService.createPurse(purseDto);
		
		assertEquals("356",purseDto.getCurrencyTypeID());
	}

	@Test
	public void testValidatePurse_With_UpdatePurse() throws ServiceException {
		
		
		CurrencyCode currencyCodeDTO1=new CurrencyCode();
		currencyCodeDTO1.setCurrencyTypeID("567");
		currencyCodeDTO1.setCurrCodeAlpha("USD");
		currencyCodeDTO1.setCurrencyDesc("US Dollar");
		currencyCodeDTO1.setMinorUnits("2");
		CurrencyCode cc1=new ModelMapper().map(currencyCodeDTO1, CurrencyCode.class);
		masterdao.createCurrency(cc1);
		
		
		CurrencyCode currencyCodeDTO2=new CurrencyCode();
		currencyCodeDTO2.setCurrencyTypeID("356");
		currencyCodeDTO2.setCurrCodeAlpha("INR");
		currencyCodeDTO2.setCurrencyDesc("Indian Rupees");
		currencyCodeDTO2.setMinorUnits("2");
		CurrencyCode cc2=new ModelMapper().map(currencyCodeDTO2, CurrencyCode.class);
		masterdao.createCurrency(cc2);
		
		
		PurseType pursetype = new PurseType();
			pursetype.setPurseTypeName("CURRENCY");
			masterdao.createPurseType(pursetype);
		
		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseTypeId(pursetype.getPurseTypeId());
		purseDto.setDescription("Create Purse value");
		purseDto.setCurrencyTypeID(cc1.getCurrencyTypeID());
		purseDto.setInsUser(1);
		purseDto.setLastUpdUser(1);
		purseDto.setExtPurseId("TestCurrencyPurse");
		purseDto.setHotCardThreshold(0);
		purseService.createPurse(purseDto);
		
		
		PurseDTO purseDTO = purseService.getPurseById(purseDto.getPurseId());
		purseDTO.setCurrencyTypeID("356");
		purseService.updatePurseDetails(purseDTO);
		
		PurseDTO DTO = purseService.getPurseById(purseDTO.getPurseId());
		assertEquals("356", DTO.getCurrencyTypeID());
	}
	
	@Test
	public void testValidatePurse_With_DeletePurse() throws ServiceException {
		
		CurrencyCode currencycode = new CurrencyCode();
		currencycode.setCurrencyTypeID("001");
		currencycode.setCurrCodeAlpha("USD");
		currencycode.setCurrencyDesc("US Dollar");
		currencycode.setMinorUnits("2");
		masterdao.createCurrency(currencycode);
		PurseType pursetype = new PurseType();
		pursetype.setPurseTypeName("CURRENCY");
		masterdao.createPurseType(pursetype);
		
		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseTypeId(pursetype.getPurseTypeId());
		purseDto.setDescription("Delete Purse value");
		purseDto.setCurrencyTypeID(currencycode.getCurrencyTypeID());
		purseDto.setInsUser(1);
		purseDto.setLastUpdUser(1);
		purseDto.setExtPurseId("TestCurrencyPurse");
		purseDto.setHotCardThreshold(0);
		purseService.createPurse(purseDto);

		try{
			purseService.deletePurseDetails(purseDto.getPurseId());
			purseService.getPurseById(purseDto.getPurseId());
		}
		catch(Exception e)
		{
			if(e instanceof ServiceException){
				assertEquals(ResponseMessages.ERR_PURSE_NOT_EXIST, e.getMessage());
			}
			if(e instanceof DataIntegrityViolationException)
			{
				assertEquals(ResponseMessages.ERR_PRODUCT_PURSE_MAPPING, e.getMessage());
				
			}
		}
	}
	
	@Test
	public void testValidatePurse_With_CreateMultiplePurse() throws ServiceException {
		
	 	PurseType pursetype = new PurseType();
		pursetype.setPurseTypeName("UPC");
		masterdao.createPurseType(pursetype);
	
		PurseDTO purseDto = new PurseDTO();
	
		purseDto.setPurseTypeId(pursetype.getPurseTypeId());
		purseDto.setDescription("Insert New Purse value");
		purseDto.setUpc("546798769454");
		purseDto.setInsUser(1);
		purseDto.setLastUpdUser(1);
		purseDto.setExtPurseId("TestCurrencyPurse");
		purseDto.setHotCardThreshold(0);
		purseService.createPurse(purseDto);
	
	
		PurseType pursetype1 = new PurseType();
		pursetype1.setPurseTypeName("UPC");
		masterdao.createPurseType(pursetype1);
	
		PurseDTO purseDto1 = new PurseDTO();
	
		purseDto1.setPurseTypeId(pursetype.getPurseTypeId());
		purseDto1.setDescription("Insert New Purse value");
		purseDto1.setUpc("546798769454");
		purseDto1.setInsUser(1);
		purseDto1.setLastUpdUser(1);
		purseDto1.setExtPurseId("TestCurrencyPurse1");
		purseDto1.setHotCardThreshold(0);
		purseService.createPurse(purseDto1);
		
		PurseType pursetype2 = new PurseType();
		pursetype2.setPurseTypeName("UPC");
		masterdao.createPurseType(pursetype2);
	
		PurseDTO purseDto2 = new PurseDTO();
	
		purseDto2.setPurseTypeId(pursetype.getPurseTypeId());
		purseDto2.setDescription("Insert New Purse value");
		purseDto2.setUpc("546798769454");
		purseDto2.setInsUser(1);
		purseDto2.setLastUpdUser(1);
		purseDto2.setExtPurseId("TestCurrencyPurse2");
		purseDto2.setHotCardThreshold(0);
		purseService.createPurse(purseDto2);
		
	
		
		assertEquals(3, purseService.getPursesBypurseTypePurseExtID(Long.valueOf(-1), "*","*").size());
		
	}
	
	
	@Test
	public void testValidatePurse_With_GetAllPurse() throws ServiceException {
		
		PurseType pursetype = new PurseType();
		pursetype.setPurseTypeName("UPC");
		masterdao.createPurseType(pursetype);
		
		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseTypeId(pursetype.getPurseTypeId());
		purseDto.setDescription("Get All Purses");
		purseDto.setUpc("546798769454");
		purseDto.setInsUser(1);
		purseDto.setLastUpdUser(1);
		purseDto.setExtPurseId("TestCurrencyPurse");
		purseDto.setHotCardThreshold(0);
		purseService.createPurse(purseDto);
		
		assertEquals(true,(purseService.getPursesBypurseTypePurseExtID(Long.valueOf(-1), "*","*").size()>0));
	}


	@Test
	public void testValidatePurse_With_GetByPurseAndUpc() {
		
		PurseDTO purseDto = new PurseDTO();
		purseDto.setPurseId(Long.valueOf(18));
		purseDto.setPurseTypeId(3);
		purseDto.setDescription("Insert New Purse value");
		purseDto.setUpc("546798769454");
		purseDto.setInsUser(1);
		purseDto.setLastUpdUser(1);
		
		try{
			purseService.createPurse(purseDto);
		}
		catch (Exception e)
		{
			if(e instanceof ConstraintViolationException)
			{
				throw new RuntimeException("Record Error for Constraint Violation Successful");
			}
		}
		
	}
	

	

}
