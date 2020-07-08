package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.ProgramIDDTO;
import com.incomm.cclp.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProgramIdServiceTest {

	@Autowired
	ProgramIDService programIdService;

	@Autowired
	PartnerService partnerService;

	@Test
	public void testCreateProgramIdWithValidPartnerId() throws ServiceException {

		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("PHEONYX MARKET VENDORS");
		partnerDto.setPartnerDesc("Shop anything u want");
		partnerDto.setIsActive("Y");
		partnerDto.setMdmId("shdfsaihsgf1287");
		partnerService.createPartner(partnerDto);

		ProgramIDDTO programIdDto = new ProgramIDDTO();
		programIdDto.setProgramIDName("Program_id_one");
		programIdDto.setPartnerId(partnerDto.getPartnerId());
		programIdDto.setPartnerName(partnerDto.getPartnerName());
		programIdDto.setDescription("");
		programIdDto.setInsUser(1L);
		programIdDto.setLastUpdUser(1L);
		programIdService.createProgramID(programIdDto);

		assertEquals(programIdDto.getProgramIDName(),
				programIdService.getProgramByID(programIdDto.getProgramID()).getProgramIDName());

	}

	@Test
	public void testCreateProgramIdWithInvalidPartnerId() throws ServiceException {

		ProgramIDDTO programIdDto = new ProgramIDDTO();
		programIdDto.setProgramIDName("Program_id_one");
		programIdDto.setPartnerId(673243874652634L);
		programIdDto.setPartnerName("synergy dfdnf");
		programIdDto.setDescription("");

		try {
			programIdService.createProgramID(programIdDto);
		} catch (ServiceException se) {
			assertEquals(ResponseMessages.PROGRAM_ID_INSERT_FAIL, se.getMessage());
		}

	}
	
	
	@Test
	public void testGetAllProgramIdsWithEmptyData()  {
		
		assertEquals(0, programIdService.getAllProgramIDs().size());
	}
	
	@Test
	public void testGetAllProgramIdsWithValidData() throws ServiceException  {
		
		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("PHEONYX MARKET VENDORS");
		partnerDto.setPartnerDesc("Shop anything u want");
		partnerDto.setIsActive("Y");
		partnerDto.setMdmId("shdfsaihsgf1287");
		partnerService.createPartner(partnerDto);

		ProgramIDDTO programIdDto = new ProgramIDDTO();
		programIdDto.setProgramIDName("Program_id_one");
		programIdDto.setPartnerId(partnerDto.getPartnerId());
		programIdDto.setPartnerName(partnerDto.getPartnerName());
		programIdDto.setDescription("");
		programIdDto.setInsUser(1L);
		programIdDto.setLastUpdUser(1L);
		programIdService.createProgramID(programIdDto);
		
		assertEquals(1, programIdService.getAllProgramIDs().size());
	}

	@Test
	public void testUpdateProgramIdWithValidData() throws ServiceException {

		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("PHEONYX MARKET VENDORS");
		partnerDto.setPartnerDesc("Shop anything u want");
		partnerDto.setIsActive("Y");
		partnerDto.setMdmId("shdfsaihsgf1287");
		partnerService.createPartner(partnerDto);

		ProgramIDDTO programIdDto = new ProgramIDDTO();
		programIdDto.setProgramIDName("Program_id_one");
		programIdDto.setPartnerId(partnerDto.getPartnerId());
		programIdDto.setPartnerName(partnerDto.getPartnerName());
		programIdDto.setDescription("");
		programIdDto.setInsUser(1L);
		programIdDto.setLastUpdUser(1L);
		programIdService.createProgramID(programIdDto);

		programIdDto.setDescription("hiii");

		programIdService.updateProgramID(programIdDto);
		assertEquals(programIdDto.getProgramIDName(),
				programIdService.getProgramByID(programIdDto.getProgramID()).getProgramIDName());

	}

	@Test
	public void testDeleteProgramId() throws ServiceException {

		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("PHEONYX MARKET VENDORS");
		partnerDto.setPartnerDesc("Shop anything u want");
		partnerDto.setIsActive("Y");
		partnerDto.setMdmId("shdfsaihsgf1287");
		partnerService.createPartner(partnerDto);

		ProgramIDDTO programIdDto = new ProgramIDDTO();
		programIdDto.setProgramIDName("Program_id_one");
		programIdDto.setPartnerId(partnerDto.getPartnerId());
		programIdDto.setPartnerName(partnerDto.getPartnerName());
		programIdDto.setDescription("");
		programIdDto.setInsUser(1L);
		programIdDto.setLastUpdUser(1L);
		programIdService.createProgramID(programIdDto);

		try {
			programIdService.deleteProgramIDById(programIdDto.getProgramID());
		} catch (ServiceException se) {
			assertEquals(ResponseMessages.FAIL_PROGRAM_DELETE, se.getMessage());
		}
		

	}
	
	@Test
	public void testDeleteProgramIdWithInvalidProgramId() throws ServiceException {

		try {
			programIdService.deleteProgramIDById(75675L);
		} catch (ServiceException se) {
			assertEquals("PROGRAMID_ERR" + ResponseMessages.DOESNOT_EXISTS, se.getMessage());
		}
		

	}
	

	@Test
	public void testGetAllProgramIDsByName() throws ServiceException {

		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("PHEONYX MARKET VENDORS");
		partnerDto.setPartnerDesc("Shop anything u want");
		partnerDto.setIsActive("Y");
		partnerDto.setMdmId("shdfsaihsgf1287");
		partnerService.createPartner(partnerDto);

		ProgramIDDTO programIdDto = new ProgramIDDTO();
		programIdDto.setProgramIDName("Program_id_one");
		programIdDto.setPartnerId(partnerDto.getPartnerId());
		programIdDto.setPartnerName(partnerDto.getPartnerName());
		programIdDto.setDescription("");
		programIdDto.setInsUser(1L);
		programIdDto.setLastUpdUser(1L);
		programIdService.createProgramID(programIdDto);

		assertNotEquals(0, programIdService.getAllProgramIDsByName(programIdDto.getProgramIDName()).size());

	}

	@Test
	public void testGetAllProgramIDsWithInvalidProgramIdName() throws ServiceException {

		assertEquals(0, programIdService.getAllProgramIDsByName("Program_id_one").size());

	}
	
	@Test
	public void testUpdateProgramIdWithInvalidData() throws ServiceException {

		ProgramIDDTO programIdDto = new ProgramIDDTO();
		programIdDto.setProgramID(123L);
		programIdDto.setProgramIDName("ProgramIdOne");
		programIdDto.setDescription("hiii");
		try {
			programIdService.updateProgramID(programIdDto);
		} catch (ServiceException se) {
			assertEquals(ResponseMessages.ERR_PROGRAM_ID_EXISTS, se.getMessage());
		}
	}

	@Test
	public void testUpdateProgramIdWithInvaliProgramIdName() throws ServiceException {

		PartnerDTO partnerDto = new PartnerDTO();
		partnerDto.setPartnerName("PHEONYX MARKET VENDORS");
		partnerDto.setPartnerDesc("Shop anything u want");
		partnerDto.setIsActive("Y");
		partnerDto.setMdmId("shdfsaihsgf1287");
		partnerService.createPartner(partnerDto);

		ProgramIDDTO programIdDto = new ProgramIDDTO();
		programIdDto.setProgramIDName("Program_id_one");
		programIdDto.setPartnerId(partnerDto.getPartnerId());
		programIdDto.setPartnerName(partnerDto.getPartnerName());
		programIdDto.setDescription("");
		programIdDto.setInsUser(1L);
		programIdDto.setLastUpdUser(1L);
		programIdService.createProgramID(programIdDto);
		programIdDto.setProgramIDName("New ProgramId");
		programIdDto.setDescription("hiii");
		try {
			programIdService.updateProgramID(programIdDto);
		} catch (ServiceException se) {
			assertEquals(ResponseMessages.PROGRAM_ID_UPDATE_FAIL, se.getMessage());
		}

	}
	
	

}
