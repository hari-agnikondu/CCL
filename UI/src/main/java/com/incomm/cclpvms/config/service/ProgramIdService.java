package com.incomm.cclpvms.config.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.incomm.cclpvms.config.model.ProgramID;
import com.incomm.cclpvms.config.model.ProgramIDDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface ProgramIdService {

	public List<ProgramIDDTO> getAllProgramIds() throws ServiceException;

	
	public List<ProgramIDDTO> getAllProgramIdsByName(String programIdName) throws ServiceException;

	public ProgramID getProgramId(Long programId) throws ServiceException;


	public ResponseDTO createProgramID(ProgramIDDTO programIDDTO) throws ServiceException;


	public ResponseDTO udpateProgramId(ProgramID programIdForm) throws ServiceException;


	public ResponseEntity<ResponseDTO> deleteProgramIddetails(ProgramID programIdForm)throws ServiceException;


	public List<ProgramIDDTO> getProgramIdsByPartnerId(Long partnerId) throws ServiceException;
	
}
