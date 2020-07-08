package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.ProgramIDDTO;
import com.incomm.cclp.exception.ServiceException;

public interface ProgramIDService {

	public List<ProgramIDDTO> getAllProgramIDs();

	public List<ProgramIDDTO> getAllProgramIDsByName(String programIDName);

	public void createProgramID(ProgramIDDTO programIDDTO) throws ServiceException;

	public ProgramIDDTO getProgramByID(Long programID);

	public void updateProgramID(ProgramIDDTO programIDDTO) throws ServiceException;

	public void deleteProgramIDById(Long programID) throws ServiceException;

	public List<ProgramIDDTO> getProgramIdsByPartnerId(Long partnerId);

		
}

