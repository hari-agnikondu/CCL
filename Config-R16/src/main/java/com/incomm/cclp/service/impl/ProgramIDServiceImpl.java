package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.GeneralConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.PartnerDAO;
import com.incomm.cclp.dao.ProgramIDDAO;
import com.incomm.cclp.domain.ProgramID;
import com.incomm.cclp.dto.ProgramIDDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.ProgramIDService;

@Service
public class ProgramIDServiceImpl implements ProgramIDService{

	@Autowired
	private ProgramIDDAO programIDDAO;
	
	@Autowired
	PartnerDAO partnerDao;
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Override
	public List<ProgramIDDTO> getAllProgramIDs() {

		return programIDDTOs(programIDDAO.getAllProgramIDs());
		
	}

	
	@Override
	public List<ProgramIDDTO> getAllProgramIDsByName(String programIDName) {
		
		return programIDDTOs(programIDDAO.getAllProgramIDsByName(programIDName));
	}
	
	private List<ProgramIDDTO> programIDDTOs(List<ProgramID> programIDsList) {
		logger.info(CCLPConstants.ENTER);
		List<ProgramIDDTO> programDTOsList = new ArrayList<>();
		if(!Objects.isNull(programIDsList)) {
			programDTOsList=programIDsList.stream()
					.map(programID -> new ProgramIDDTO(
							programID.getPrgmID(),
							programID.getProgramIDName(),
							programID.getPartner().getPartnerId(),
							programID.getPartner().getPartnerName(),
							programID.getDescription(), 
							programID.getInsUser(),
							programID.getInsDate(),
							programID.getLastUpdUser(),programID.getLastUpdDate())
							).collect(Collectors.toList());
		}
		logger.info(CCLPConstants.EXIT);
		return programDTOsList;
	}


	@Override
	@Transactional
	public void createProgramID(ProgramIDDTO programIDDTO) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info(programIDDTO.toString());
		try {
		List<ProgramIDDTO> programIDs = getProgramIDByName(programIDDTO.getProgramIDName());
		if(!Objects.isNull(programIDs)){
		
			List<ProgramIDDTO> programIDList = programIDs.stream()
					.filter(product -> product.getProgramIDName().equalsIgnoreCase(programIDDTO.getProgramIDName()))
					.collect(Collectors.toList());
		
			if(!Objects.isNull(programIDList) && !programIDList.isEmpty()) {
				throw new ServiceException(ResponseMessages.ERR_PROGRAM_ID_EXISTS);
			}
		}
			ProgramID programID = constructProgramID(programIDDTO);
			programIDDAO.createProgramID(programID);
			programIDDTO.setProgramID(programID.getPrgmID());
			
		}catch(ServiceException se) {
			logger.error("Exception se {}",se);
			throw se;
		}catch(Exception e) {
			
			logger.info("Error occured while inserting program id"+ e);
			throw new ServiceException(ResponseMessages.PROGRAM_ID_INSERT_FAIL);	
		}
		
		logger.info("Record created for :" + programIDDTO.getProgramID());

		programIDDTO.setProgramID(programIDDTO.getProgramID());
		logger.info(CCLPConstants.EXIT);
	}


	private ProgramID constructProgramID(ProgramIDDTO programIDDTO) {
		logger.info(CCLPConstants.ENTER);
		ProgramID programID = new ProgramID();
		programID.setPrgmID(programIDDTO.getProgramID());
		programID.setProgramIDName(programIDDTO.getProgramIDName());
		programID.setDescription(programIDDTO.getDescription());
		programID.setPartner(partnerDao.getPartnerById(programIDDTO.getPartnerId()));
		programID.setInsUser(programIDDTO.getInsUser());
		programID.setInsDate(programIDDTO.getInsDate());
		programID.setLastUpdUser(programIDDTO.getLastUpdUser());
		programID.setLastUpdDate(programIDDTO.getLastUpdDate());
		logger.info(CCLPConstants.EXIT);
		return programID;
	}


	private List<ProgramIDDTO> getProgramIDByName(String programIDName) {
		
		return programIDDTOs(programIDDAO.getProgramIDByName(programIDName));
	}


	@Override
	public ProgramIDDTO getProgramByID(Long programId) {
		logger.info(CCLPConstants.ENTER);
		ProgramIDDTO programIDDTO = null;
		ProgramID programID = programIDDAO.getProgramByID(programId);
		if(!Objects.isNull(programID)) {
			
			programIDDTO = new ProgramIDDTO(programID.getPrgmID(),
					programID.getProgramIDName(), programID.getPartner().getPartnerId(),
					programID.getPartner().getPartnerName(),programID.getDescription(), 
					programID.getInsUser(),programID.getInsDate(),
					programID.getLastUpdUser(),programID.getLastUpdDate());
			
		}
		logger.info(CCLPConstants.EXIT);
		return programIDDTO;
		
	}


	@Override
	@Transactional
	public void updateProgramID(ProgramIDDTO programIDDTO) throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		
		
		ProgramID existingProgramID = programIDDAO.getProgramByID(programIDDTO.getProgramID());
		if(Objects.isNull(existingProgramID)) {
			logger.debug("PROGRAM ID name '{}' does not exist in table", programIDDTO.getProgramIDName());
			throw new ServiceException(ResponseMessages.ERR_PROGRAM_ID_EXISTS);
		}
		
		List<ProgramIDDTO> programIDs = getProgramIDByName(programIDDTO.getProgramIDName());
		Long noOfExistingProgramIDs = programIDs.stream()
				.filter(programID -> programID.getProgramIDName().equalsIgnoreCase(programIDDTO.getProgramIDName())
						&& (!programID.getProgramID().equals(programIDDTO.getProgramID())))
				.count();
		
		if(noOfExistingProgramIDs > 0) {
			logger.error("Program ID already exists");
			throw new ServiceException("PROGRAMID_" + ResponseMessages.ALREADY_EXISTS);
		}		
		
		Long isMatched = programIDs.stream()
				.filter(programID -> programID.getProgramIDName().equalsIgnoreCase(programIDDTO.getProgramIDName())
						&& (programID.getProgramID().equals(programIDDTO.getProgramID())))
				.count();
		
		if(isMatched != 1) {
			logger.error("Error while updating program id");
			throw new ServiceException(ResponseMessages.PROGRAM_ID_UPDATE_FAIL);
		}
		
		try {
			
			/**ProgramID programID = constructProgramID(programIDDTO);*/
			existingProgramID.setDescription(programIDDTO.getDescription());
			existingProgramID.setLastUpdDate(new Date());
			existingProgramID.setLastUpdUser(programIDDTO.getLastUpdUser());
			programIDDAO.updateProduct(existingProgramID);
			
		}catch(Exception e) {
			logger.info("Error occured while updating program id"+ e);
			throw new ServiceException(ResponseMessages.PROGRAM_ID_UPDATE_FAIL);	
		}
		
		logger.info("Record updated for :" + programIDDTO.getProgramID());
		logger.info(CCLPConstants.EXIT);
		
	}


	@Override
	public void deleteProgramIDById(Long programID) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.debug("inside deleteProgramIDById with data : " + programID);

		ProgramID programId = programIDDAO.getProgramByID(programID);
		if (Objects.isNull(programId)) {
			logger.debug("programId record not exists with programID : {}", programID);
			throw new ServiceException(("PROGRAMID_ERR" + ResponseMessages.DOESNOT_EXISTS), ResponseMessages.DOESNOT_EXISTS);
		}
		
		
		try {
			logger.info("deleting program..");
			programIDDAO.deleteProgramID(programId);
		} catch (Exception e) {
			logger.error("Error occured while deleting programId record", e.getMessage());

			throw new ServiceException(ResponseMessages.FAIL_PROGRAM_DELETE, ResponseMessages.FAIL_PROGRAM_DELETE);
		}

		logger.info("Record deleted successfully");

		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public List<ProgramIDDTO> getProgramIdsByPartnerId(Long partnerId) {
		
		return programIDDTOs(programIDDAO.getProgramIdsByPartnerId(partnerId));
	}
	
	

}
