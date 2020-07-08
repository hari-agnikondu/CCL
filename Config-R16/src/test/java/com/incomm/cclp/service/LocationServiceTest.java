/**
 * 
 */
package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

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
import com.incomm.cclp.dao.MasterDao;
import com.incomm.cclp.dao.impl.MetaDataDAOImpl;
import com.incomm.cclp.dto.CountryCodeDTO;
import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.dto.LocationDTO;
import com.incomm.cclp.dto.MerchantDTO;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.ProductDTO;
import com.incomm.cclp.dto.StateCodeDTO;
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
public class LocationServiceTest {

	@Autowired
	LocationService locationService;
	
	@Autowired
	MasterService masterService;
	
	@Autowired
	MasterDao masterdao;
	
	@Autowired
	MerchantService merchantService;
	
	@Autowired
	MetaDataDAOImpl metadata;

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
	public void testValidateLocation_With_NullObject() {

		LocationDTO locationDto = null;
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_LOCATION_NULL;

		try {
			ValidationService.validateLocation(locationDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}

	@Test
	public void testValidateLocation_With_NullLocationValues() {

		LocationDTO locationDto = new LocationDTO();
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_MERCHANT_ID +
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ADDRESSONE_NULL +
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CITY_NULL +
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ZIP_NULL +
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_STATE_NULL + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_COUNTRY_NULL +
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_LOCATION_INS_USER;
		try {
			ValidationService.validateLocation(locationDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidateLocation_With_NullMerchantId() {

		LocationDTO locationDto = new LocationDTO();
		
		MerchantDTO merchantDto = new MerchantDTO();
		
		StateCodeDTO stateCodeDto = new StateCodeDTO();
		
		CountryCodeDTO countryCodeDto = new CountryCodeDTO();
		
		merchantDto.setMerchantName("ABC Pvt Ltd");
		merchantDto.setMdmId("ABC0002");
		
		countryCodeDto.setCountryCodeID((long) 3);
		countryCodeDto.setCountryName("USA");
		
		stateCodeDto.setStateCodeID((long) 2);
		stateCodeDto.setStateName("FLORIDA");
		stateCodeDto.setCountryCode((long) 3);;
		
		countryCodeDto.setCountryCodeID(stateCodeDto.getCountryCode());
		countryCodeDto.setCountryName("USA");
		
		locationDto.setLocationName("Chennai");
		locationDto.setAddressOne("Texas");
		locationDto.setCity("Dallas");
		locationDto.setCountryCodeID(countryCodeDto.getCountryCodeID());
		locationDto.setCountryName(countryCodeDto.getCountryName());
		locationDto.setMerchantName(merchantDto.getMerchantName());
		locationDto.setLastUpdUser((long) 1);
		locationDto.setInsUser((long) 1);
		locationDto.setStateCodeID(stateCodeDto.getStateCodeID());
		locationDto.setStateName(stateCodeDto.getStateName());
		locationDto.setZip("15023");
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_MERCHANT_ID;
		try {
			ValidationService.validateLocation(locationDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidateLocation_With_NullMerchantName() {

		LocationDTO locationDto = new LocationDTO();
		
		MerchantDTO merchantDto = new MerchantDTO();
		
		StateCodeDTO stateCodeDto = new StateCodeDTO();
		
		CountryCodeDTO countryCodeDto = new CountryCodeDTO();
		
		merchantDto.setMerchantId((long) 3);
		merchantDto.setMerchantName("ABC Pvt Ltd");
		merchantDto.setMdmId("ABC0002");
		
		countryCodeDto.setCountryCodeID((long) 3);
		countryCodeDto.setCountryName("USA");
		
		stateCodeDto.setStateCodeID((long) 2);
		stateCodeDto.setStateName("FLORIDA");
		stateCodeDto.setCountryCode((long) 3);;
		
		countryCodeDto.setCountryCodeID(stateCodeDto.getCountryCode());
		countryCodeDto.setCountryName("USA");
		
		locationDto.setLocationName("Chennai");
		locationDto.setAddressOne("Texas");
		locationDto.setCity("Dallas");
		locationDto.setCountryCodeID(countryCodeDto.getCountryCodeID());
		locationDto.setCountryName(countryCodeDto.getCountryName());
		locationDto.setLastUpdUser((long) 1);
		locationDto.setInsUser((long) 1);
		locationDto.setStateCodeID(stateCodeDto.getStateCodeID());
		locationDto.setStateName(stateCodeDto.getStateName());
		locationDto.setZip("15023");
		locationDto.setMerchantId(merchantDto.getMerchantId());
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_MERCHANT_NAME;
		try {
			ValidationService.validateLocation(locationDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	
	@Test
	public void testValidateLocation_With_NullCityName() {

		LocationDTO locationDto = new LocationDTO();
		
		MerchantDTO merchantDto = new MerchantDTO();
		
		StateCodeDTO stateCodeDto = new StateCodeDTO();
		
		CountryCodeDTO countryCodeDto = new CountryCodeDTO();
		
		merchantDto.setMerchantId((long) 3);
		merchantDto.setMerchantName("ABC Pvt Ltd");
		merchantDto.setMdmId("ABC0002");
		
		countryCodeDto.setCountryCodeID((long) 3);
		countryCodeDto.setCountryName("USA");
		
		stateCodeDto.setStateCodeID((long) 2);
		stateCodeDto.setStateName("FLORIDA");
		stateCodeDto.setCountryCode((long) 3);;
		
		countryCodeDto.setCountryCodeID(stateCodeDto.getCountryCode());
		countryCodeDto.setCountryName("USA");
		
		locationDto.setLocationName("Chennai");
		locationDto.setAddressOne("Texas");
		locationDto.setCountryCodeID(countryCodeDto.getCountryCodeID());
		locationDto.setCountryName(countryCodeDto.getCountryName());
		locationDto.setLastUpdUser((long) 1);
		locationDto.setInsUser((long) 1);
		locationDto.setStateCodeID(stateCodeDto.getStateCodeID());
		locationDto.setStateName(stateCodeDto.getStateName());
		locationDto.setZip("15023");
		locationDto.setMerchantName(merchantDto.getMerchantName());
		locationDto.setMerchantId(merchantDto.getMerchantId());
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CITY_NULL;
		try {
			ValidationService.validateLocation(locationDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}

	@Test
	public void testValidateLocation_With_NullZip() {

		LocationDTO locationDto = new LocationDTO();
		
		MerchantDTO merchantDto = new MerchantDTO();
		
		StateCodeDTO stateCodeDto = new StateCodeDTO();
		
		CountryCodeDTO countryCodeDto = new CountryCodeDTO();
		
		merchantDto.setMerchantId((long) 3);
		merchantDto.setMerchantName("ABC Pvt Ltd");
		merchantDto.setMdmId("ABC0002");
		
		countryCodeDto.setCountryCodeID((long) 3);
		countryCodeDto.setCountryName("USA");
		
		stateCodeDto.setStateCodeID((long) 2);
		stateCodeDto.setStateName("FLORIDA");
		stateCodeDto.setCountryCode((long) 3);;
		
		countryCodeDto.setCountryCodeID(stateCodeDto.getCountryCode());
		countryCodeDto.setCountryName("USA");
		
		locationDto.setLocationName("Chennai");
		locationDto.setAddressOne("Texas");
		locationDto.setCountryCodeID(countryCodeDto.getCountryCodeID());
		locationDto.setCountryName(countryCodeDto.getCountryName());
		locationDto.setLastUpdUser((long) 1);
		locationDto.setInsUser((long) 1);
		locationDto.setCity("Dallas");
		locationDto.setStateCodeID(stateCodeDto.getStateCodeID());
		locationDto.setStateName(stateCodeDto.getStateName());
		locationDto.setMerchantName(merchantDto.getMerchantName());
		locationDto.setMerchantId(merchantDto.getMerchantId());
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ZIP_NULL;
		try {
			ValidationService.validateLocation(locationDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidateLocation_With_NullStateCodeId() {

		LocationDTO locationDto = new LocationDTO();
		
		MerchantDTO merchantDto = new MerchantDTO();
		
		StateCodeDTO stateCodeDto = new StateCodeDTO();
		
		CountryCodeDTO countryCodeDto = new CountryCodeDTO();
		
		merchantDto.setMerchantId((long) 3);
		merchantDto.setMerchantName("ABC Pvt Ltd");
		merchantDto.setMdmId("ABC0002");
		
		countryCodeDto.setCountryCodeID((long) 3);
		countryCodeDto.setCountryName("USA");
		
		stateCodeDto.setStateCodeID((long) 2);
		stateCodeDto.setStateName("FLORIDA");
		stateCodeDto.setCountryCode((long) 3);;
		
		countryCodeDto.setCountryCodeID(stateCodeDto.getCountryCode());
		countryCodeDto.setCountryName("USA");
		
		locationDto.setLocationName("Chennai");
		locationDto.setAddressOne("Texas");
		locationDto.setLastUpdUser((long) 1);
		locationDto.setInsUser((long) 1);
		locationDto.setCity("Dallas");
		locationDto.setStateCodeID(stateCodeDto.getStateCodeID());
		locationDto.setStateName(stateCodeDto.getStateName());
		locationDto.setMerchantName(merchantDto.getMerchantName());
		locationDto.setMerchantId(merchantDto.getMerchantId());
		locationDto.setZip("15023");
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_COUNTRY_NULL;
		try {
			ValidationService.validateLocation(locationDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidateLocation_With_NullCountryCodeId() {

		LocationDTO locationDto = new LocationDTO();
		
		MerchantDTO merchantDto = new MerchantDTO();
		
		StateCodeDTO stateCodeDto = new StateCodeDTO();
		
		CountryCodeDTO countryCodeDto = new CountryCodeDTO();
		
		merchantDto.setMerchantId((long) 3);
		merchantDto.setMerchantName("ABC Pvt Ltd");
		merchantDto.setMdmId("ABC0002");
		
		countryCodeDto.setCountryCodeID((long) 3);
		countryCodeDto.setCountryName("USA");
		
		stateCodeDto.setStateCodeID((long) 2);
		stateCodeDto.setStateName("FLORIDA");
		stateCodeDto.setCountryCode((long) 3);;
		
		countryCodeDto.setCountryCodeID(stateCodeDto.getCountryCode());
		countryCodeDto.setCountryName("USA");
		
		locationDto.setLocationName("Chennai");
		locationDto.setAddressOne("Texas");
		locationDto.setCountryCodeID(countryCodeDto.getCountryCodeID());
		locationDto.setCountryName(countryCodeDto.getCountryName());
		locationDto.setLastUpdUser((long) 1);
		locationDto.setInsUser((long) 1);
		locationDto.setCity("Dallas");
		locationDto.setMerchantName(merchantDto.getMerchantName());
		locationDto.setMerchantId(merchantDto.getMerchantId());
		locationDto.setZip("15023");
		
		String expectedMsg = ResponseMessages.VALIDATION_HEADER_MSG + 
				ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_STATE_NULL;
		try {
			ValidationService.validateLocation(locationDto, true);
		} catch (ServiceException se) {

			assertEquals(expectedMsg, se.getMessage());
		}
	}
	
	@Test
	public void testValidateLocation_With_CreateLocation() {

		LocationDTO locationDto = new LocationDTO();
		
		MerchantDTO merchantDto = new MerchantDTO();
		
		merchantDto.setMerchantName("ABC Pvt Ltd");
		merchantDto.setMdmId("ABC0002");
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("ABC Bank");
		
		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("Paytm");
		
		ProductDTO productDto = new ProductDTO();
		productDto.setProductName("Birthday Cards");
		
		metadata.createCurrency();
		metadata.createCountry();
		metadata.createState();
		
		try {
			merchantService.createMerchant(merchantDto);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		
		locationDto.setLocationName("Chennai");
		locationDto.setAddressOne("Texas");
		locationDto.setCity("Dallas");
		locationDto.setCountryCodeID((long) 3);
		locationDto.setCountryName("CANADA");
		locationDto.setMerchantName(merchantDto.getMerchantName());
		locationDto.setLastUpdUser((long) 1);
		locationDto.setInsUser((long) 1);
		locationDto.setStateCodeID((long) 1);
		locationDto.setStateName("NEW YORK");
		locationDto.setZip("15023");
		locationDto.setMerchantId(merchantDto.getMerchantId());
		
		try {
			
			locationService.createLocation(locationDto);
			assertEquals(1, locationService.getLocationByMerchantName(merchantDto.getMerchantName()).size());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testValidateLocation_With_UpdateLocation() {
		
		LocationDTO locationDto = new LocationDTO();
		
		MerchantDTO merchantDto = new MerchantDTO();
		
		merchantDto.setMerchantName("McDonald");
		merchantDto.setMdmId("ABC0002");
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("ABC Bank");
		
		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("Paytm");
		
		ProductDTO productDto = new ProductDTO();
		productDto.setProductName("Birthday Cards");
		
		metadata.createCurrency();
		metadata.createCountry();
		metadata.createState();
		try {
			merchantService.createMerchant(merchantDto);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		
		locationDto.setLocationName("Chennai");
		locationDto.setAddressOne("Texas");
		locationDto.setCity("Dallas");
		locationDto.setCountryCodeID((long) 3);
		locationDto.setCountryName("CANADA");
		locationDto.setMerchantName(merchantDto.getMerchantName());
		locationDto.setLastUpdUser((long) 1);
		locationDto.setInsUser((long) 1);
		locationDto.setStateCodeID((long) 1);
		locationDto.setStateName("NEW YORK");
		locationDto.setZip("15023");
		locationDto.setMerchantId(merchantDto.getMerchantId());
		
		try {
			
			locationService.createLocation(locationDto);
			
			List<LocationDTO> locationDTOList =  locationService.getLocationByMerchantName(merchantDto.getMerchantName());
			
			locationDTOList.stream().forEach(locationDTO->{
			
					if(1==locationDTO.getLocationId())
					{
						locationDTO.setCity("Salem");
						try {
							locationService.updateLocation(locationDTO);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}}
					);
			assertEquals(1, locationService.getLocationByMerchantName(merchantDto.getMerchantName()).size());
			
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testValidateLocation_With_getAllLocations() {
		
		LocationDTO locationDto = new LocationDTO();
		
		LocationDTO locationDto1 = new LocationDTO();
		
		MerchantDTO merchantDto = new MerchantDTO();
		MerchantDTO merchantDto1 = new MerchantDTO();
		
		merchantDto.setMerchantName("Pothys");
		merchantDto.setMdmId("ABC0002");
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("ABC Bank");
		
		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("Paytm");
		
		ProductDTO productDto = new ProductDTO();
		productDto.setProductName("Birthday Cards");
		
		metadata.createCurrency();
		metadata.createCountry();
		metadata.createState();
		
		try {
			merchantService.createMerchant(merchantDto);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		
		locationDto.setLocationName("Chennai");
		locationDto.setAddressOne("Texas");
		locationDto.setCity("Dallas");
		locationDto.setCountryCodeID((long) 3);
		locationDto.setCountryName("CANADA");
		locationDto.setMerchantName(merchantDto.getMerchantName());
		locationDto.setLastUpdUser((long) 1);
		locationDto.setInsUser((long) 1);
		locationDto.setStateCodeID((long) 1);
		locationDto.setStateName("NEW YORK");
		locationDto.setZip("15023");
		locationDto.setMerchantId(merchantDto.getMerchantId());
		
		merchantDto1.setMerchantName("StarBucks");
		merchantDto1.setMdmId("ABC0002");
		
		try {
			merchantService.createMerchant(merchantDto1);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		
		locationDto1.setLocationName("Africa");
		locationDto1.setAddressOne("Texas");
		locationDto1.setCity("Dallas");
		locationDto1.setCountryCodeID((long) 3);
		locationDto1.setCountryName("CANADA");
		locationDto1.setMerchantName(merchantDto1.getMerchantName());
		locationDto1.setLastUpdUser((long) 1);
		locationDto1.setInsUser((long) 1);
		locationDto1.setStateCodeID((long) 1);
		locationDto1.setStateName("NEW YORK");
		locationDto1.setZip("15023");
		locationDto1.setMerchantId(merchantDto1.getMerchantId());
		
		try {
			locationService.createLocation(locationDto);
			locationService.createLocation(locationDto1);
			
			assertEquals(2, locationService.getAllLocations().size());
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testValidateLocation_With_getLocationByMerchantName() {
		
		LocationDTO locationDto = new LocationDTO();
		
		MerchantDTO mrchDTO = new MerchantDTO();
		
		mrchDTO.setMerchantName("KFC1");
		mrchDTO.setMdmId("ABC0002");
		
		IssuerDTO issuerDto = new IssuerDTO();
		issuerDto.setIssuerName("ABC Bank");
		
		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("Paytm");
		
		ProductDTO productDto = new ProductDTO();
		productDto.setProductName("Birthday Cards");
		
		metadata.createCurrency();
		metadata.createCountry();
		metadata.createState();
		
		try {
			merchantService.createMerchant(mrchDTO);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		
		locationDto.setLocationName("Andhra");
		locationDto.setAddressOne("Texas");
		locationDto.setCity("Dallas");
		locationDto.setCountryCodeID((long) 3);
		locationDto.setCountryName("CANADA");
		locationDto.setMerchantName(mrchDTO.getMerchantName());
		locationDto.setLastUpdUser((long) 1);
		locationDto.setInsUser((long) 1);
		locationDto.setStateCodeID((long) 1);
		locationDto.setStateName("NEW YORK");
		locationDto.setZip("15023");
		locationDto.setMerchantId(mrchDTO.getMerchantId());
		
		try {
			locationService.createLocation(locationDto);
			
			assertEquals(1, locationService.getLocationByMerchantName(mrchDTO.getMerchantName()).size());
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}  

}
