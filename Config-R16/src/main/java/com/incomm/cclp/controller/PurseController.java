/**
 * 
 */
package com.incomm.cclp.controller;

import io.swagger.annotations.Api;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.dto.PurseTypeDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.MasterService;
import com.incomm.cclp.service.PurseService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;
import com.incomm.cclp.util.ValidationService;

/**
 * Purse Controller provides all the REST operations pertaining to Purse.
 * 
 */
@RestController
@RequestMapping("/purses")
@Api(value="purses")
public class PurseController {

	
	@Autowired
	private PurseService purseService;
	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;
	
	@Autowired
	private MasterService masterService;
	
	// the logger
	private static final Logger logger = LogManager.getLogger(PurseController.class);
	
	
	/**
	 * To create a new purse
	 * @param purseDTO
	 * @return ResponseDTO
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createPurse(@RequestBody PurseDTO purseDTO) 
			throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		logger.info("PurseDTO :"+purseDTO);
		
		ValidationService.validatePurse(purseDTO, true);
		purseService.createPurse(purseDTO);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("PRS_ADD_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		PurseDTO purseDto=purseService.getPurseById(purseDTO.getPurseId());
		PurseTypeDTO purseTypeDTO=	masterService.getPurseTypeById(purseDto.getPurseTypeId());
		purseDto.setPurseTypeName(purseTypeDTO.getPurseTypeName());
		responseDto = respMessage(responseDto,purseDto); 
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Gets all Active getAllPackages.
	 *
	 * @return the ResponseEntity with the result.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity getAllPurses() {
		logger.info(CCLPConstants.ENTER);
		
		
		List<PurseDTO> purseDtos = purseService.getAllPurses();
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(purseDtos, 
				(CCLPConstants.PRS_RETRIVE+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);	
		
		
		responseDto.setMessage(responseDto.getMessage());
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity(responseDto, HttpStatus.OK);
	}
	

	/**
	 * To get all purses  by using Currency code and UPC
	 * @param  The Currency code and UPC
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/{currencyCode}/{upc}",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getPursesByCurrencyAndUpcCode(@PathVariable("currencyCode")
	String currencyCode,@PathVariable("upc") String upc)throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		if(Util.isEmpty(currencyCode)){
			logger.info("invalid currency code");
			throw new ServiceException(ResponseMessages.ERR_CURRENCYCODE_NULL);
		}
		if(Util.isEmpty(upc)){
			logger.error("invalid UPC");
			throw new ServiceException(ResponseMessages.ERR_UPC_NULL);
		}
		
		List<PurseDTO> purseList=purseService.getPursesByCurrencyAndUpcCode(currencyCode,upc);
		ResponseDTO	responseDto = responseBuilder.buildSuccessResponse(purseList, 
				(CCLPConstants.PRS_RETRIVE+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * To get purse  by purse Id
	 * @param  The purseId
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/{purseId}",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getPurseDetailsById(@PathVariable("purseId")
	Long purseId)throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		if(purseId<=0){
			logger.error("invalid purse ID");
			throw new ServiceException(ResponseMessages.ERR_PURSEID_INVALID);
		}
		PurseDTO purseDTO=purseService.getPurseById(purseId);
		ResponseDTO	responseDto = responseBuilder.buildSuccessResponse(purseDTO, 
				(CCLPConstants.PRS_RETRIVE+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	
	
	/**
	 * To get purse  by purse Id
	 * @param  The purseId
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updatePurseDetails(@RequestBody	PurseDTO purseDTO)throws ServiceException 
	{
		logger.info(CCLPConstants.ENTER);

		ValidationService.validatePurse(purseDTO, false);
		
		purseService.updatePurseDetails(purseDTO);
		
		ResponseDTO	responseDto = responseBuilder.buildSuccessResponse(null, 
				("PRS_UPDATE_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		
		PurseDTO purseDto=purseService.getPurseById(purseDTO.getPurseId());
		responseDto = respMessage(responseDto,purseDto);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/{purseId}",method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deletePurseDetails(@PathVariable("purseId")	Long purseId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		
		if(purseId<=0){
			logger.error("invalid purse ID");
			throw new ServiceException(ResponseMessages.ERR_PURSEID_INVALID);
		}

		PurseDTO purseDto = purseService.getPurseById(purseId);
		purseService.deletePurseDetails(purseId);
		ResponseDTO	responseDto = responseBuilder.buildSuccessResponse(null,
					("PRS_DELETE_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); 	

			responseDto = respMessage(responseDto,purseDto);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * This methods is to set the name to the place holder 
	 * @param responseDto,purseDTO
	 * @return ResponseDTO
	 */
	public ResponseDTO respMessage(ResponseDTO responseDto, PurseDTO purseDTO)
	{
		logger.info(CCLPConstants.ENTER);
		StringBuilder str=new StringBuilder();
		if(purseDTO.getExtPurseId()!=null && !purseDTO.getExtPurseId().isEmpty())
		{
			str.append(purseDTO.getExtPurseId()+" - ");
		}
		if(purseDTO.getPurseTypeName()!=null){
			str.append(purseDTO.getPurseTypeName()+" ");
		}
		if(purseDTO.getCurrencyTypeID()!=null && !purseDTO.getCurrencyTypeID().isEmpty())
		{
			str.append(purseDTO.getCurrencyTypeID());
		}
		else if(purseDTO.getUpc()!=null && !purseDTO.getUpc().isEmpty())
		{
			str.append(purseDTO.getUpc());
		}
		Map<String,String> valuesMap = new HashMap<>();
		valuesMap.put("PurseName", str.toString());
		responseDto.setMessage(new StrSubstitutor(valuesMap).replace(responseDto.getMessage()));
		logger.info(CCLPConstants.EXIT);
		return responseDto;
	}
	
		@RequestMapping(value = "/getPurseDataById/{purseIds}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getPurseByIds(@PathVariable("purseIds") List<String> purseIds) {
		
		logger.info(CCLPConstants.ENTER);
		
		List<Long> purseList= new ArrayList<>();
		
		for(int i=0;i<purseIds.size();i++)
		{
			String id="";
			id=purseIds.get(i).replaceAll("\\[", "");
			id=id.replaceAll("\\]", "");
			
			purseList.add(Long.parseLong(id));
		}
		
		
		List<String> purseData = purseService.getPurseByIds(purseList);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(purseData, 
				(CCLPConstants.PRS_RETRIVE+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);		
		
		responseDto.setMessage(responseDto.getMessage());
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

		@RequestMapping(value="/{purseType}/{currencyCode}/{purseIdExt}",method = RequestMethod.GET)
		public ResponseEntity<ResponseDTO> getPursesBypurseTypePurseExtID(@PathVariable("purseType")Long purseType,
				@PathVariable("currencyCode")String currencyCode,@PathVariable("purseIdExt") String purseIdExt)throws ServiceException {
			logger.info(CCLPConstants.ENTER);
			if(purseType==null){
				logger.info("invalid purse type");
				throw new ServiceException(ResponseMessages.ERR_PURSETYPEID_NULL);
			}
			if(Util.isEmpty(currencyCode)){
				logger.info("invalid currency code");
				throw new ServiceException(ResponseMessages.ERR_CURRENCYCODE_NULL);
			}
			if(Util.isEmpty(purseIdExt)){
				logger.error("invalid purse");
				throw new ServiceException(ResponseMessages.ERR_PURSE_NULL);
			}
			
			List<PurseDTO> purseList=purseService.getPursesBypurseTypePurseExtID(purseType,currencyCode,purseIdExt);
			ResponseDTO	responseDto = responseBuilder.buildSuccessResponse(purseList, 
					(CCLPConstants.PRS_RETRIVE+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

			logger.info(CCLPConstants.EXIT);
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		}
	
}
