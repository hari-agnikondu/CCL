/**
 * 
 */
package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

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
import com.incomm.cclp.dto.CardRangeDTO;
import com.incomm.cclp.dto.ClpUserDTO;
import com.incomm.cclp.dto.IssuerDTO;
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
public class CardRangeServiceTest {

	@Autowired
	CardRangeService cardRangeService;
	
	@Autowired
	IssuerService issuerService;
	
	@Autowired
	UserService userService;


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
	public void testValidateCardRange_With_NullCardRange() {

		CardRangeDTO cardRangeDto = null;
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER +
				ResponseMessages.ERR_CARDRANGE_NULL;


		try {
			ValidationService.validateCardRange(cardRangeDto, false);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}

	@Test
	public void testValidateCardRange_With_NullCardRangeValues() {

		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_ID + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PREFIX_NULL + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_START_CARDRANGE+
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_END_CARDRANGE+
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_CHECKDIGIT+
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_INVENTORY+
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_INS_USER;
		try {
			ValidationService.validateCardRange(cardRangeDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}


	@Test
	public void testValidateCardRange_With_Values() {

		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setCardRangeId(1);
		cardRangeDto.setIssuerId(1);
		cardRangeDto.setLastUpdUser(1);
		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("456789");

		cardRangeDto.setStartCardNbr("456789");
		cardRangeDto.setEndCardNbr("456790");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG ; 

		try {
			ValidationService.validateCardRange(cardRangeDto, false);
		} catch (ServiceException se) {
			assertEquals(expectedMsg, se.getMessage());
		}
	}


	@Test
	public void testValidateCardRange_With_EmptyPrefix() {

		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setCardRangeId(1);
		cardRangeDto.setIssuerId(1);
		cardRangeDto.setLastUpdUser(1);

		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix(" ");

		cardRangeDto.setStartCardNbr("456789");
		cardRangeDto.setEndCardNbr("456790");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");

		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PREFIX_NULL;	

		try {
			ValidationService.validateCardRange(cardRangeDto, false);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}


	@Test
	public void testValidateCardRange_With_EmptyStartCardRange() {


		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setCardRangeId(1);
		cardRangeDto.setIssuerId(1);
		cardRangeDto.setLastUpdUser(1);

		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("112345");

		cardRangeDto.setStartCardNbr(" ");
		cardRangeDto.setEndCardNbr("456790");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");

		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_START_CARDRANGE;	
		System.out.println("expectedMsg"+expectedMsg);
		try {
			ValidationService.validateCardRange(cardRangeDto, false);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	@Test
	public void testValidateCardRange_With_EmptyEndCardRange() {

		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setIssuerId(1);
		cardRangeDto.setLastUpdUser(1);
		cardRangeDto.setCardRangeId(2);
		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("112345");

		cardRangeDto.setStartCardNbr("456789");
		cardRangeDto.setEndCardNbr(" ");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");

		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_END_CARDRANGE;	
		System.out.println("expectedMsg"+expectedMsg);
		try {
			ValidationService.validateCardRange(cardRangeDto, false);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	@Test
	public void testValidateCardRange_With_EmptyCheckDigit() {

		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setIssuerId(1);
		cardRangeDto.setLastUpdUser(1);
		cardRangeDto.setCardRangeId(2);
		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("112345");

		cardRangeDto.setStartCardNbr("456789");
		cardRangeDto.setEndCardNbr("456790");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired(" ");

		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_CHECKDIGIT;	
		System.out.println("expectedMsg"+expectedMsg);
		try {
			ValidationService.validateCardRange(cardRangeDto, false);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	@Test
	public void testValidateCardRange_With_EmptyInventory() {

		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setIssuerId(1);
		cardRangeDto.setLastUpdUser(1);
		cardRangeDto.setCardRangeId(2);
		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("112345");

		cardRangeDto.setStartCardNbr("456789");
		cardRangeDto.setEndCardNbr("456790");
		cardRangeDto.setCardInventory(" ");
		cardRangeDto.setIsCheckDigitRequired("Y");

		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_INVENTORY;	
		try {
			ValidationService.validateCardRange(cardRangeDto, false);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	@Test
	public void testValidateCardRange_With_InvalidInsUser() {

		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setIssuerId(1);
		cardRangeDto.setLastUpdUser(1);
		cardRangeDto.setCardRangeId(2);
		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("112345");

		cardRangeDto.setStartCardNbr("456789");
		cardRangeDto.setEndCardNbr("456790");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");
		cardRangeDto.setInsUser(-1);

		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_INS_USER;	
		try {
			ValidationService.validateCardRange(cardRangeDto, true);
		} catch (ServiceException se) {
			assertEquals(expectedMsg, se.getMessage());
		}
	}

	/*@Test
	public void testUpdate_Operation_ForCardRangeUpdate() throws ServiceException  {
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("true");
		issuerService.createIssuer(issuerDto);
		
		ClpUserDTO user = new ClpUserDTO();
		
		user.setUserLoginId("admin_user");
		user.setUserName("ADMIN USER");
		user.setUserEmail("admin@incomm.com");
		user.setUserStatus("NEW");
		
		userService.createUser(user);
		
		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setIssuerId(issuerDto.getIssuerId());

		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("111345");

		cardRangeDto.setStartCardNbr("456789");
		cardRangeDto.setEndCardNbr("456790");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");
		cardRangeDto.setStatus("NEW");

		cardRangeDto.setInsUser(user.getUserId());
		cardRangeDto.setLastUpdUser(user.getUserId());

		cardRangeService.createCardRange(cardRangeDto);
		
		CardRangeDTO cardRangeDto1  = new CardRangeDTO();
		cardRangeDto1.setCardRangeId(cardRangeDto.getCardRangeId());
		cardRangeDto1.setIssuerId(issuerDto.getIssuerId());
		cardRangeDto1.setCardLength(new BigDecimal("12"));
		cardRangeDto1.setPrefix("123456");

		cardRangeDto1.setStartCardNbr("456789");
		cardRangeDto1.setEndCardNbr("456790");
		cardRangeDto1.setCardInventory("Y");
		cardRangeDto1.setIsCheckDigitRequired("Y");
		cardRangeDto1.setStatus("NEW");
		cardRangeDto1.setInsUser(user.getUserId());
		cardRangeDto1.setLastUpdUser(user.getUserId());

		cardRangeService.updateCardRange(cardRangeDto1);

		CardRangeDTO cardRangeDTO=cardRangeService.getCardRangeById(String.valueOf(cardRangeDto.getCardRangeId()));

		assertEquals("123456", cardRangeDTO.getPrefix());
	}*/

	@Test
	public void testDeleteCardRange() throws ServiceException {
		try {
			
			IssuerDTO issuerDto = new IssuerDTO();
			issuerDto.setIssuerName("Wells Fargo");
			issuerDto.setIsActive("true");
			issuerService.createIssuer(issuerDto);
			
			ClpUserDTO user = new ClpUserDTO();
			
			user.setUserLoginId("admin_user");
			user.setUserName("ADMIN USER");
			user.setUserEmail("admin@incomm.com");
			user.setUserStatus("NEW");
			
			userService.createUser(user);
			
			CardRangeDTO cardRangeDto  = new CardRangeDTO();
			cardRangeDto.setIssuerId(issuerDto.getIssuerId());

			cardRangeDto.setCardLength(new BigDecimal("12"));
			cardRangeDto.setPrefix("111345");

			cardRangeDto.setStartCardNbr("456789");
			cardRangeDto.setEndCardNbr("456790");
			cardRangeDto.setCardInventory("Y");
			cardRangeDto.setIsCheckDigitRequired("Y");
			cardRangeDto.setStatus("NEW");

			cardRangeDto.setInsUser(user.getUserId());
			cardRangeDto.setLastUpdUser(user.getUserId());

			cardRangeService.createCardRange(cardRangeDto);
			
			CardRangeDTO cardRangeDto1  = new CardRangeDTO();
			cardRangeDto1.setCardRangeId(cardRangeDto.getCardRangeId());
			cardRangeService.deleteCardRange(cardRangeDto);
			
			try {
				cardRangeService.getCardRangeById(String.valueOf(cardRangeDto.getCardRangeId()));
			} catch (Exception e) {
				if(e instanceof javax.persistence.NoResultException ){
					throw new RuntimeException("Record Delete succesfully");
				}
			}
		} catch (Exception e) {
			assertEquals("Record Delete succesfully",e.getMessage());
		}
	}

	@Test
	public void testUpdate_Operation_ForCardRangeUpdate_AlreadyExist() throws ServiceException  {

		try {
			
			IssuerDTO issuerDto = new IssuerDTO();
			issuerDto.setIssuerName("Wells Fargo");
			issuerDto.setIsActive("true");
			issuerService.createIssuer(issuerDto);
		
			ClpUserDTO user = new ClpUserDTO();
			
			user.setUserLoginId("admin_user");
			user.setUserName("ADMIN USER");
			user.setUserEmail("admin@incomm.com");
			user.setUserStatus("NEW");
			
			userService.createUser(user);
			
			CardRangeDTO cardRangeDto  = new CardRangeDTO();
			cardRangeDto.setIssuerId(issuerDto.getIssuerId());

			cardRangeDto.setCardLength(new BigDecimal("12"));
			cardRangeDto.setPrefix("111345");

			cardRangeDto.setStartCardNbr("456789");
			cardRangeDto.setEndCardNbr("456790");
			cardRangeDto.setCardInventory("Y");
			cardRangeDto.setIsCheckDigitRequired("Y");
			cardRangeDto.setStatus("NEW");

			cardRangeDto.setInsUser(user.getUserId());
			cardRangeDto.setLastUpdUser(user.getUserId());

			cardRangeService.createCardRange(cardRangeDto);
			
			CardRangeDTO cardRangeDto2  = new CardRangeDTO();
			cardRangeDto2.setIssuerId(issuerDto.getIssuerId());

			cardRangeDto2.setCardLength(new BigDecimal("12"));
			cardRangeDto2.setPrefix("111345");

			cardRangeDto2.setStartCardNbr("456756");
			cardRangeDto2.setEndCardNbr("456757");
			cardRangeDto2.setCardInventory("Y");
			cardRangeDto2.setIsCheckDigitRequired("Y");
			cardRangeDto2.setStatus("NEW");

			cardRangeDto2.setInsUser(user.getUserId());
			cardRangeDto2.setLastUpdUser(user.getUserId());

			cardRangeService.createCardRange(cardRangeDto2);
			
			CardRangeDTO cardRangeDto1  = new CardRangeDTO();
			cardRangeDto1.setCardRangeId(cardRangeDto2.getCardRangeId());
			cardRangeDto1.setIssuerId(issuerDto.getIssuerId());

			cardRangeDto1.setCardLength(new BigDecimal("12"));
			cardRangeDto1.setPrefix("111345");

			cardRangeDto1.setStartCardNbr("456789");
			cardRangeDto1.setEndCardNbr("456790");
			cardRangeDto1.setCardInventory("Y");
			cardRangeDto1.setIsCheckDigitRequired("Y");
			cardRangeDto1.setStatus("NEW");
			
			cardRangeDto1.setLastUpdUser(user.getUserId());

			cardRangeService.updateCardRange(cardRangeDto1);
		} catch (Exception e) {
			assertEquals(e.getMessage(),ResponseMessages.ERR_CARDRANGE_EXISTS);
		}

	}




	@Test
	public void testValidateForCardRangeCreate() throws ServiceException  {
		try{
			
			IssuerDTO issuerDto = new IssuerDTO();
			issuerDto.setIssuerName("Wells Fargo");
			issuerDto.setIsActive("true");
			issuerService.createIssuer(issuerDto);
			
			CardRangeDTO cardRangeDto  = new CardRangeDTO();
			cardRangeDto.setIssuerId(issuerDto.getIssuerId());

			cardRangeDto.setCardLength(new BigDecimal("12"));
			cardRangeDto.setPrefix("111345");

			cardRangeDto.setStartCardNbr("456789");
			cardRangeDto.setEndCardNbr("456790");
			cardRangeDto.setCardInventory("Y");
			cardRangeDto.setIsCheckDigitRequired("Y");
			cardRangeDto.setStatus("NEW");

			cardRangeDto.setInsUser(1);

			cardRangeService.createCardRange(cardRangeDto);
			assertEquals("111345",cardRangeService.getCardRangeById(String.valueOf(cardRangeDto.getCardRangeId())).getPrefix());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void createMultipleCardRanges()throws ServiceException{
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("true");
		issuerService.createIssuer(issuerDto);
		
		ClpUserDTO user = new ClpUserDTO();
		
		user.setUserLoginId("Develop_user");
		user.setUserName("DEVELOP USER");
		user.setUserEmail("admin@incomm.com");
		user.setUserStatus("APPROVE");
		
		userService.createUser(user);
		
		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setIssuerId(issuerDto.getIssuerId());

		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("111345");

		cardRangeDto.setStartCardNbr("456789");
		cardRangeDto.setEndCardNbr("456790");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");
		cardRangeDto.setStatus("NEW");

		cardRangeDto.setInsUser(user.getUserId());
		cardRangeDto.setLastUpdUser(user.getUserId());

		cardRangeService.createCardRange(cardRangeDto);
		
		CardRangeDTO cardRangeDto1  = new CardRangeDTO();
		
		cardRangeDto1.setIssuerId(issuerDto.getIssuerId());
		cardRangeDto1.setCardLength(new BigDecimal("12"));
		cardRangeDto1.setPrefix("567893");

		cardRangeDto1.setStartCardNbr("234323");
		cardRangeDto1.setEndCardNbr("234326");
		cardRangeDto1.setCardInventory("Y");
		cardRangeDto1.setIsCheckDigitRequired("Y");
		cardRangeDto1.setStatus("NEW");

		cardRangeDto1.setInsUser(user.getUserId());
		cardRangeDto1.setLastUpdUser(user.getUserId());

		cardRangeService.createCardRange(cardRangeDto1);
		
	}
	@Test
	public void test_cardRangeAlreadyExistFor_create() throws ServiceException {
		
		try {
			createMultipleCardRanges();
			
			CardRangeDTO cardRangeDto  = new CardRangeDTO();
			cardRangeDto.setIssuerId(1);
	
			cardRangeDto.setCardLength(new BigDecimal("12"));
			cardRangeDto.setPrefix("111345");

			cardRangeDto.setStartCardNbr("456789");
			cardRangeDto.setEndCardNbr("456790");
			cardRangeDto.setCardInventory("Y");
			cardRangeDto.setIsCheckDigitRequired("Y");
			cardRangeDto.setStatus("NEW");

			cardRangeDto.setInsUser(1);
			cardRangeDto.setLastUpdUser(1);

			cardRangeService.createCardRange(cardRangeDto);
		} catch (Exception e) {
			assertEquals(e.getMessage(),ResponseMessages.ERR_CARDRANGE_EXISTS);
		}
		
	}
	
	
	
	
	@Test
	public void testValidate_CardRange_Already_Approved() throws ServiceException  {

		try {
			CardRangeDTO cardRangeDto  = new CardRangeDTO();
			cardRangeDto.setIssuerId(1);

			cardRangeDto.setCardLength(new BigDecimal("12"));
			cardRangeDto.setPrefix("111345");

			cardRangeDto.setStartCardNbr("456789");
			cardRangeDto.setEndCardNbr("456790");
			cardRangeDto.setCardInventory("Y");
			cardRangeDto.setIsCheckDigitRequired("Y");
			cardRangeDto.setStatus("NEW");

			cardRangeDto.setInsUser(1);

			cardRangeService.createCardRange(cardRangeDto);
			
			
			CardRangeDTO cardRangeDto1  = new CardRangeDTO();
			cardRangeDto1.setCardRangeId(cardRangeDto.getCardRangeId());
			cardRangeDto1.setIsCheckDigitRequired("Y");

			cardRangeDto1.setStatus("APPROVED");
			cardRangeDto1.setLastUpdUser(1);
			cardRangeDto1.setCheckerDesc("Approved for valid");
			

			cardRangeService.changeCardRangeStatus(cardRangeDto1.getCardRangeId(), cardRangeDto1.getStatus(), cardRangeDto1.getCheckerDesc(),cardRangeDto1.getLastUpdUser());

			cardRangeService.changeCardRangeStatus(cardRangeDto1.getCardRangeId(), cardRangeDto1.getStatus(), cardRangeDto1.getCheckerDesc(),cardRangeDto1.getLastUpdUser());
			

		} catch (Exception e) {

			assertEquals(e.getMessage(),ResponseMessages.ERR_CARDRANGE_APPROVED);

		}

	}
	
	
	
	@Test
	public void testValidate_CardRange_Already_Rejected() throws ServiceException  {

		try {
			CardRangeDTO cardRangeDto  = new CardRangeDTO();
			cardRangeDto.setIssuerId(1);

			cardRangeDto.setCardLength(new BigDecimal("12"));
			cardRangeDto.setPrefix("111345");

			cardRangeDto.setStartCardNbr("456789");
			cardRangeDto.setEndCardNbr("456790");
			cardRangeDto.setCardInventory("Y");
			cardRangeDto.setIsCheckDigitRequired("Y");
			cardRangeDto.setStatus("NEW");

			cardRangeDto.setInsUser(1);

			cardRangeService.createCardRange(cardRangeDto);
			
			
			CardRangeDTO cardRangeDto1  = new CardRangeDTO();
			cardRangeDto1.setCardRangeId(cardRangeDto.getCardRangeId());
			cardRangeDto1.setIsCheckDigitRequired("Y");

			cardRangeDto1.setStatus("REJECTED");
			cardRangeDto1.setLastUpdUser(1);
			cardRangeDto1.setCheckerDesc("Reject");

			cardRangeService.changeCardRangeStatus(cardRangeDto1.getCardRangeId(), cardRangeDto1.getStatus(), cardRangeDto1.getCheckerDesc(),cardRangeDto1.getLastUpdUser());

			cardRangeService.changeCardRangeStatus(cardRangeDto1.getCardRangeId(), cardRangeDto1.getStatus(), cardRangeDto1.getCheckerDesc(),cardRangeDto1.getLastUpdUser());
			

		} catch (Exception e) {

			assertEquals(e.getMessage(),ResponseMessages.ERR_CARDRANGE_REJECTED);

		}

	}
	
	@Test
	public void testGetCardRanges() throws ServiceException{
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("true");
		issuerService.createIssuer(issuerDto);
		
		ClpUserDTO user = new ClpUserDTO();
		
		user.setUserLoginId("admin_user");
		user.setUserName("ADMIN USER");
		user.setUserEmail("admin@incomm.com");
		user.setUserStatus("NEW");
		
		userService.createUser(user);
		
		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setIssuerId(issuerDto.getIssuerId());

		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("111345");

		cardRangeDto.setStartCardNbr("00089");
		cardRangeDto.setEndCardNbr("000090");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");
		cardRangeDto.setStatus("NEW");

		cardRangeDto.setInsUser(user.getUserId());
		cardRangeDto.setLastUpdUser(user.getUserId());

		cardRangeService.createCardRange(cardRangeDto);
		
		
		assertEquals(true,(cardRangeService.getCardRanges().size()>0));
		
	}
	
	@Test
	public void testGetCardRangeByIssuerNameAndPrefix()  throws ServiceException{

		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("Y");
		issuerService.createIssuer(issuerDto);
		
		ClpUserDTO user = new ClpUserDTO();
		
		user.setUserLoginId("admin_user");
		user.setUserName("ADMIN USER");
		user.setUserEmail("admin@incomm.com");
		user.setUserStatus("NEW");
		
		userService.createUser(user);
		
		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setIssuerId(issuerDto.getIssuerId());

		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("111345");

		cardRangeDto.setStartCardNbr("00089");
		cardRangeDto.setEndCardNbr("000090");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");
		cardRangeDto.setStatus("NEW");

		cardRangeDto.setInsUser(user.getUserId());
		cardRangeDto.setLastUpdUser(user.getUserId());

		cardRangeService.createCardRange(cardRangeDto);
		
		assertEquals(true,cardRangeService.getCardRangeByIssuerNameAndPrefix("", "").size()>0);
		
		
	}
	@Test
	public void testGetIssuers()  throws ServiceException{
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("Y");
		issuerService.createIssuer(issuerDto);
		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setIssuerId(1);

		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("111345");

		cardRangeDto.setStartCardNbr("00089");
		cardRangeDto.setEndCardNbr("000090");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");
		cardRangeDto.setStatus("NEW");

		cardRangeDto.setInsUser(1);

		cardRangeService.createCardRange(cardRangeDto);
		assertEquals(true,(cardRangeService.getIssuers().size()>0));
	}
	@Test
	public void testGetExistCardRanges()  throws ServiceException{
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("Y");
		issuerService.createIssuer(issuerDto);
		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setIssuerId(1);

		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("111345");

		cardRangeDto.setStartCardNbr("00089");
		cardRangeDto.setEndCardNbr("000090");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");
		cardRangeDto.setStatus("NEW");

		cardRangeDto.setInsUser(1);

		cardRangeService.createCardRange(cardRangeDto);
		
		assertEquals(true,(cardRangeService.getExistCardRanges(cardRangeDto)!=null?true:false));
	}
	
	@Test
	public void testGetAllCardRanges() throws ServiceException{
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("Y");
		issuerService.createIssuer(issuerDto);
		
		ClpUserDTO user = new ClpUserDTO();
		
		user.setUserLoginId("admin_user");
		user.setUserName("ADMIN USER");
		user.setUserEmail("admin@incomm.com");
		user.setUserStatus("NEW");
		
		userService.createUser(user);
		
		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setIssuerId(issuerDto.getIssuerId());

		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("111345");

		cardRangeDto.setStartCardNbr("00089");
		cardRangeDto.setEndCardNbr("000090");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");
		cardRangeDto.setStatus("NEW");

		cardRangeDto.setInsUser(user.getUserId());
		cardRangeDto.setLastUpdUser(user.getUserId());

		cardRangeService.createCardRange(cardRangeDto);
		
		assertEquals(true,(cardRangeService.getAllCardRanges().size()>0));
	
	}
	
	public void testGetCardRangesByIssuerId() throws ServiceException {
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("Wells Fargo");
		issuerDto.setIsActive("Y");
		issuerService.createIssuer(issuerDto);
		CardRangeDTO cardRangeDto  = new CardRangeDTO();
		cardRangeDto.setIssuerId(issuerDto.getIssuerId());

		cardRangeDto.setCardLength(new BigDecimal("12"));
		cardRangeDto.setPrefix("111345");

		cardRangeDto.setStartCardNbr("00089");
		cardRangeDto.setEndCardNbr("000090");
		cardRangeDto.setCardInventory("Y");
		cardRangeDto.setIsCheckDigitRequired("Y");
		cardRangeDto.setStatus("NEW");

		cardRangeDto.setInsUser(1);

		cardRangeService.createCardRange(cardRangeDto);
		
		assertEquals(true,(cardRangeService.getCardRangesByIssuerId(cardRangeDto.getIssuerId()).size()>0));
	}

}
