package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.text.ParseException;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.exception.ServiceException;

/**
 * ValidationService class provides all the validations for Input DTO's.
 * 
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PartnerServiceTest {

	@Autowired
	PartnerService partnerService;

	/*Search all the existing Partners*/
	@Test
	public void getAllPartnersList() throws ServiceException {

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("Harikrishna");
		Dto.setPartnerDesc(" Java Developer");
		Dto.setIsActive("Y");
		Dto.setMdmId("123");
		partnerService.createPartner(Dto);
	
		assertEquals(1, partnerService.getAllPartners().size());
	}

	/* Search for Existing partner With Name( Either Partial or Full Name)*/
	@Test
	public void searchExistingParnterByName() throws ServiceException {

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("Ulagan");
		Dto.setPartnerDesc(" Java Developer");
		Dto.setIsActive("Y");
		Dto.setMdmId("123");

		partnerService.createPartner(Dto);
	
		String name = "Ulagan";

		assertEquals(1, partnerService.getPartnerByName(name).size());

	}

	/* Search for Non-Existing Partner with name*/
	@Test
	public void searchNonExistingParnterByName() throws ServiceException {

		String name = "asdf";
		assertEquals(0, partnerService.getPartnerByName(name).size());

	}

	/* Searching for Existing Partner BY ID*/
	@Test
	public void searchExistingPartnerById() throws ServiceException {

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("Rathna");
		Dto.setPartnerDesc(" DB Developer");
		Dto.setIsActive("Y");
		Dto.setMdmId("143");
		partnerService.createPartner(Dto);
		
		Long Id = Dto.getPartnerId();

		assertNotNull(partnerService.getPartnerById(Id));
	}

	/* Search for Non Existing Partner BY ID ( PartnerID does not exists in DB)*/
	@Test
	public void searchNonExistingParnterById() throws ServiceException {

		Long Id = (long) 123;
		assertNull(partnerService.getPartnerById(Id));

	}

	/* Inserting new partner to the Table*/
	@Test
	public void testCreatePartner_First_Partner() throws ParseException, ServiceException {

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("farahan");
		Dto.setPartnerDesc(" Java Developer");
		Dto.setIsActive("Y");
		Dto.setMdmId("123");
		partnerService.createPartner(Dto);

		assertEquals(1, partnerService.getPartnerByName("farahan").size());
	}

	// Inserting Duplicate Partner to the Table
	@Test
	public void testCreatePartner_Duplicate_Partner() throws ParseException, ServiceException {

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("vinoth");
		Dto.setPartnerDesc(" Java Developer");
		Dto.setIsActive("Y");
		Dto.setMdmId("123");

		partnerService.createPartner(Dto);
		
		try {
			partnerService.createPartner(Dto);
		} catch (ServiceException se) {
			assertEquals(ResponseMessages.ERR_PARTNER_EXISTS, se.getMessage());
		}
		
		
	}

	/* Update the Partner With Existing Partner Id*/
	@Test
	public void updatePartner_with_existingPartner() throws ServiceException {

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("Hari");
		Dto.setPartnerDesc(" Java Tester");
		Dto.setIsActive("Y");
		Dto.setMdmId("123");

		partnerService.createPartner(Dto);

		Dto.setPartnerDesc("Full Stack Developer");
		try {
			partnerService.updatePartner(Dto);
		}catch (ServiceException se) {
				assertEquals(ResponseMessages.ERR_PARTNER_EXISTS, se.getMessage());
		}

	}
	
	/*Update partner with already existing partnerName*/
	@Test
	public void updatePartner_with_Already_existingPartnerName() throws ServiceException {

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("HariKrishna");
		Dto.setPartnerDesc(" Java Developer");
		Dto.setIsActive("Y");
		Dto.setMdmId("12345");

		partnerService.createPartner(Dto);

		PartnerDTO Dto2 = new PartnerDTO();
		Dto2.setPartnerName("Hari");
		Dto2.setPartnerDesc(" Java Developer");
		Dto2.setIsActive("Y");
		Dto2.setMdmId("12345");

		partnerService.createPartner(Dto2);
		
		Dto.setPartnerName("Hari");
		Dto.setPartnerDesc("Full Stack Developer");
		try {
			partnerService.updatePartner(Dto);
		}catch (ServiceException se) {
				assertEquals(ResponseMessages.ERR_PARTNER_EXISTS, se.getMessage());
		}
	}
	

	/*Update partner with already existing partnerName*/
	@Test
	public void updatePartner_among_Already_existingPartners() throws ServiceException {

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("HariKrishna");
		Dto.setPartnerDesc(" Java Developer");
		Dto.setIsActive("Y");
		Dto.setMdmId("12345");

		partnerService.createPartner(Dto);

		PartnerDTO Dto2 = new PartnerDTO();
		Dto2.setPartnerName("Hari");
		Dto2.setPartnerDesc(" Java Developer");
		Dto2.setIsActive("Y");
		Dto2.setMdmId("12345");

		partnerService.createPartner(Dto2);
		
		Dto.setPartnerName("Farahan");
		Dto.setPartnerDesc("Full Stack Developer");
		try {
			partnerService.updatePartner(Dto);
		}catch (ServiceException se) {
				assertEquals(ResponseMessages.ERR_PARTNER_EXISTS, se.getMessage());
		}
	}
	
	/*Update the empty Existing partner with some other values*/
	@Test
	public void updatePartner_with_Empty_existingPartner() throws ServiceException {

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("");
		Dto.setPartnerDesc("");
		Dto.setIsActive("");
		Dto.setMdmId("");

		partnerService.createPartner(Dto);

		Dto.setPartnerDesc("Full Stack Developer");
		try {
			partnerService.updatePartner(Dto);
		}catch (ServiceException se) {
				assertEquals(ResponseMessages.ERR_PARTNER_EXISTS, se.getMessage());
		}

	}
	
	/*Update the null partner with some other values*/
	@Test
	public void updatePartner_with_null_partner() throws ServiceException {

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("Ranga");
		Dto.setPartnerDesc("Software Tester");
		Dto.setIsActive("Y");
		Dto.setMdmId("12345");

		partnerService.createPartner(Dto);

		PartnerDTO Dto2 = new PartnerDTO();
		
		Dto2.setPartnerDesc("Full Stack Developer");
		try {
			partnerService.updatePartner(Dto2);
		}catch (ServiceException se) {
			System.out.println(se.getMessage());
			System.out.println(ResponseMessages.ERR_PARTNER_EXISTS);
				assertEquals(ResponseMessages.ERR_PARTNER_EXISTS, se.getMessage());
		}

	}
	

	/* Deleting the Partner With Existing PartnerId*/
	@Test
	public void deletePartner_With_PartnerId() throws ServiceException {

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("karthik");
		Dto.setPartnerDesc(" DB Developer");
		Dto.setIsActive("Y");
		Dto.setMdmId("13");
		partnerService.createPartner(Dto);

		Long Id =Dto.getPartnerId();

		partnerService.deletePartner(Id);

		assertEquals(partnerService.getAllPartners().size(),0);
	}

	/* Trying to Delete the Partner which Does not Exists in DB*/
	@Test
	public void deleteNonExistingPartner_With_partnerId()  {

		Long Id = (long) 1234;
		try {
			partnerService.deletePartner(Id);
		}catch (ServiceException se) {
			assertEquals(se.getMessage(),ResponseMessages.FAIL_PARTNER_DELETE);
		}
	}
	
	/* Trying to Delete the Partner which Does not Exists in DB*/
	@Test
	public void deleteNonExistingPartner_With_Zero_partnerId()  {

		long id = 0;
		try {
			partnerService.deletePartner(id);
		}catch (ServiceException se) {
			assertEquals(se.getMessage(),ResponseMessages.FAIL_PARTNER_DELETE);
		}
	}

}
