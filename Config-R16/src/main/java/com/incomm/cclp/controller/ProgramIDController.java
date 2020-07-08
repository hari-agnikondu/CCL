package com.incomm.cclp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.GeneralConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.ProgramIDDTO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.PartnerService;
import com.incomm.cclp.service.ProgramIDService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.ValidationService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/programid")
@Api(value="programid", description="Operations pertaining to Program ID")
public class ProgramIDController {
	
		@Autowired
		private ResponseBuilder responseBuilder;
		
		@Autowired
		private ProgramIDService programIDService;
		
		@Autowired
		private PartnerService partnerService;
	
	
	private static final Logger logger = LogManager.getLogger(ProgramIDController.class);
	private static final String PROGRAM_ID_NAME = "programIDName";
	private static final String PROGRAM_ID_RETRIEVE = "PROGRAMID_RETRIVE_";
	private static final String PROGRAM_ID_ERR = "PROGRAMID_ERR_";
	
	
	
	/**
	 * Get all program IDs
	 * 
	 * @return the ResponseEntity with the result.
	 */

	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllProgramIds()  {
		
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<ProgramIDDTO> programIDDtos = programIDService.getAllProgramIDs();
	
		if (programIDDtos.isEmpty()) {
			responseDto = responseBuilder.buildFailureResponse(PROGRAM_ID_ERR + ResponseMessages.DOESNOT_EXISTS,
					ResponseMessages.DOESNOT_EXISTS);
			logger.debug("Record Does not exist:: " + responseDto);
		} else {
			responseDto = responseBuilder.buildSuccessResponse(programIDDtos, PROGRAM_ID_RETRIEVE + ResponseMessages.SUCCESS,
					ResponseMessages.SUCCESS);
		}
		
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	

	/**
	 * Gets an Program ID by name. The name can be a complete or partial Program ID Name.
	 * 
	 * @param programIDName
	 *            The name of the programID to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 */
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProgramIDsByName(@RequestParam(PROGRAM_ID_NAME) String programIDName)
			 {
		
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		
		List<ProgramIDDTO> programIDDtos = programIDService.getAllProgramIDsByName(programIDName);
		if(programIDDtos.isEmpty()) {
			responseDto = responseBuilder.buildFailureResponse(PROGRAM_ID_ERR +ResponseMessages.DOESNOT_EXISTS,
					ResponseMessages.DOESNOT_EXISTS);
			logger.debug("Record Does not exist: " + responseDto);
		}else {
			responseDto = responseBuilder.buildSuccessResponse(programIDDtos, PROGRAM_ID_RETRIEVE + ResponseMessages.SUCCESS,
					ResponseMessages.SUCCESS);
		}
		
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
		
	}
	
	
	/**
	 * Creates an Program ID.
	 * 
	 * @RequestBody programIDDTO
	 *            The programIDDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createProgramID(@RequestBody ProgramIDDTO programIDDTO)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info(programIDDTO.toString());
		Map<String, String> valuesMap = new HashMap<>();
		String templateString = "";
		
		ValidationService.validateProgramId(programIDDTO, true);
		
		programIDService.createProgramID(programIDDTO);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,"PRGMID_ADD_"+ResponseMessages.SUCCESS,ResponseMessages.SUCCESS);
		
		valuesMap.put(PROGRAM_ID_NAME, programIDDTO.getProgramIDName());
		templateString = responseDto.getMessage();
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);
		responseDto.setMessage(resolvedString);
		
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/*
	 * Get ProgramID By ID
	 */
	
	@RequestMapping(value = "/{programID}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProgramIdById(@PathVariable("programID") Long programID){
		
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		
		if (Objects.isNull(programID)) {
			
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROGRAM_ID,
					ResponseMessages.ERR_PROGRAM_ID);
		}else {
			ProgramIDDTO programIDDTO = programIDService.getProgramByID(programID);
			
			if(Objects.isNull(programIDDTO)) {
				responseDto = responseBuilder.buildFailureResponse(PROGRAM_ID_ERR +ResponseMessages.DOESNOT_EXISTS,
						ResponseMessages.DOESNOT_EXISTS);
				logger.debug("Record Does not exist: " + programID);
			}
			else {
				
				responseDto = responseBuilder.buildSuccessResponse(programIDDTO, PROGRAM_ID_RETRIEVE + ResponseMessages.SUCCESS, 
						ResponseMessages.SUCCESS);
				logger.debug("Record for Program ID: {} has been retrieved successfully {}", programIDDTO,
						programIDDTO.toString());
			}
		}
		
		
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
		
	}
	
	
	/**
	 * Updates an program ID.
	 * 
	 * @param programIDDTO
	 *            The programIDDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 * @throws Exception 
	 */
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateProgramID(@RequestBody ProgramIDDTO programIDDTO) throws ServiceException{

		logger.info(CCLPConstants.ENTER);
		logger.debug(programIDDTO.toString());
		Map<String, String> valuesMap = new HashMap<>();
		ResponseDTO responseDto = null;
		String templateString = "";
		
		ValidationService.validateProgramId(programIDDTO, false);
		
		programIDService.updateProgramID(programIDDTO);
		
		logger.debug("Updating program id details for {} as {}", programIDDTO.getPartnerName(), programIDDTO.toString());
		responseDto = responseBuilder.buildSuccessResponse(null, ("PROGRAMID_UPD_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);
		
		valuesMap.put(PROGRAM_ID_NAME, programIDDTO.getProgramIDName());
		templateString = responseDto.getMessage();
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);
		responseDto.setMessage(resolvedString);
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto,HttpStatus.OK);
		
	}
	@RequestMapping(value = "/{programID}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteProgramId(@PathVariable("programID") Long programID) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info("programID  to delete :" + programID);
		
		ProgramIDDTO programIDDTO = null;
		Map<String, String> valuesMap = new HashMap<>();
		String templateString = "";	
		String resolvedString = "";		
		ResponseDTO responseDto = null;
		try {
		if (programID <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROGRAM_ID,
					ResponseMessages.ERR_PROGRAM_ID);
		}
		else {
			programIDDTO = programIDService.getProgramByID(programID);
			
			if (Objects.isNull(programIDDTO)) {
				logger.debug("ProgramId record not exists with ProgramId : {}", programID);
				
				responseDto = responseBuilder.buildFailureResponse(("PROGRAMID_ERR" + ResponseMessages.DOESNOT_EXISTS),
						ResponseMessages.DOESNOT_EXISTS);
			}
			else {
				programIDService.deleteProgramIDById(programID);
				
				logger.info("ProgramID '{}' record deleted successfully", programIDDTO.getProgramIDName());
				
				responseDto = responseBuilder.buildSuccessResponse(null, ("PROGRAM_DELETE_" + ResponseMessages.SUCCESS),
						ResponseMessages.SUCCESS);
				valuesMap.put(PROGRAM_ID_NAME, programIDDTO.getProgramIDName().trim());
				templateString = responseDto.getMessage();
				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				resolvedString = sub.replace(templateString);
				responseDto.setMessage(resolvedString);
			}
		}
		}catch(ServiceException se){
			logger.error("Error while deleting Program ID from table, {}", se);
			responseDto = responseBuilder.buildFailureResponse(se.getMessage(),se.getCode());
			
			valuesMap.put(PROGRAM_ID_NAME, programIDDTO.getProgramIDName().trim());
			templateString = responseDto.getMessage();
			StrSubstitutor sub = new StrSubstitutor(valuesMap);
			resolvedString = sub.replace(templateString);
			responseDto.setMessage(resolvedString);
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/getProgramIdDtlsByPartnerId/{partnerId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProgramIdsByPartnerId(@PathVariable("partnerId") Long partnerId) throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		List<ProgramIDDTO> programIdList = programIDService.getProgramIdsByPartnerId(partnerId);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(programIdList,
				(CCLPConstants.CARDRANGE_RETRIEVE+ ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAllSupportedPursesByProgramId/{programId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllSupportedPursesByProgramIdId(@PathVariable("programId") Long programId)
			{
		logger.info(CCLPConstants.ENTER);
		
		ProgramIDDTO programIDDTO = programIDService.getProgramByID(programId);
		List<PurseDTO> supportedPurseList = partnerService.getAllSupportedPursesByPartnerId(programIDDTO.getPartnerId());

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(supportedPurseList,
				(ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
}
