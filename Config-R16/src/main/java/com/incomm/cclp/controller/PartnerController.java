package com.incomm.cclp.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.PartnerService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;
import com.incomm.cclp.util.ValidationService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/partners")
@Api(value = "partner")
public class PartnerController {

	@Autowired
	private PartnerService partnerService;

	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;

	// the logger
	private static final Logger logger = LogManager.getLogger(PartnerController.class);

	/**
	 * Creates a partner.
	 * 
	 * @param partnerDto
	 *            The PartnerDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createPartner(@RequestBody PartnerDTO partnerDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();

		partnerDto.setCurrencyList(partnerDto.getCurrencyList());
		partnerDto.setPurseList(partnerDto.getPurseList());
		ValidationService.validatePartner(partnerDto, true);

		logger.debug("Creating new partner in tatble {}", partnerDto.toString());
		partnerService.createPartner(partnerDto);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
				("PARTNER_ADD_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		valuesMap.put(CCLPConstants.PARTNER_NAME, partnerDto.getPartnerName());
		String templateString = "";

		templateString = responseDto.getMessage();

		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);

		responseDto.setMessage(resolvedString);

		logger.info("Partner created successfully");
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Gets all Active Partners.
	 *
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllPartners() {
		logger.info(CCLPConstants.ENTER);
		List<PartnerDTO> partnerDtos = partnerService.getAllPartners();
		logger.info("Performing full serach for partners");
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(partnerDtos,
				ResponseMessages.ALL_SUCCESS_PARTNER_RETRIEVE, ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Updates a partner.
	 * 
	 * @param partnerDto
	 *            The PartnerDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updatePartner(@RequestBody PartnerDTO partnerDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		ValidationService.validatePartner(partnerDto, false);

		logger.debug("Partner to be updated::"+partnerDto.toString());
		partnerService.updatePartner(partnerDto);
		logger.debug("Updating parter details for {} as {}", partnerDto.getPartnerName(), partnerDto.toString());
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
				("PARTNER_UPDATE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		valuesMap.put(CCLPConstants.PARTNER_NAME, partnerDto.getPartnerName());
		String templateString = "";

		templateString = responseDto.getMessage();

		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);

		responseDto.setMessage(resolvedString);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Deletes a partner.
	 * 
	 * @param partnerId
	 *            The id of the Partner to be deleted.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/{partnerId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deletePartner(@PathVariable("partnerId") long partnerId) {
		logger.info(CCLPConstants.ENTER);
		PartnerDTO partner = new PartnerDTO();
		Map<String, String> valuesMap = new HashMap<>();

		ResponseDTO responseDto = null;
		try {
			logger.debug("Deleting partnerID:'{}' from partner table", partnerId);
			partner = partnerService.getPartnerById(partnerId);

			if (partner != null) {
				partnerService.deletePartner(partnerId);
				responseDto = responseBuilder.buildSuccessResponse(null, ("PARTNER_DELETE_" + ResponseMessages.SUCCESS),
						ResponseMessages.SUCCESS);
				valuesMap.put(CCLPConstants.PARTNER_NAME, partner.getPartnerName());
				String templateString = "";
				templateString = responseDto.getMessage();
				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				String resolvedString = sub.replace(templateString);
				responseDto.setMessage(resolvedString);
			} else {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_PARTNER_RETRIEVE,
						ResponseMessages.DOESNOT_EXISTS);
			}

		} catch (ServiceException e) {
			logger.info("Error while deleting the partner");
			partner = partnerService.getPartnerById(partnerId);
			logger.error("Error while deleting partner from table, {}", e);
			responseDto = responseBuilder.buildFailureResponse(e.getMessage(), e.getCode());

			valuesMap.put(CCLPConstants.PARTNER_NAME, partner.getPartnerName());
			String templateString = "";
			templateString = responseDto.getMessage();
			StrSubstitutor sub = new StrSubstitutor(valuesMap);
			String resolvedString = sub.replace(templateString);
			responseDto.setMessage(resolvedString);
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Gets a partner by Partner Id.
	 * 
	 * @param partnerId
	 *            The id of the Partner to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{partnerId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getPartnerById(@PathVariable("partnerId") Long partnerId) {
		logger.info(CCLPConstants.ENTER);
		PartnerDTO partner;
		Map<String, String> valuesMap = new HashMap<>();

		ResponseDTO responseDto = null;

		if (partnerId <= 0) {
			logger.info("Parnter Id is negative: {}", partnerId);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PARTNER_ID,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			PartnerDTO partnerDto = partnerService.getPartnerById(partnerId);
			if (partnerDto == null) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_PARTNER_RETRIEVE,
						ResponseMessages.DOESNOT_EXISTS);
				logger.info("Failed to fetch partner details for partner {}", partnerId);
			} else {
				responseDto = responseBuilder.buildSuccessResponse(partnerDto,
						("PARTNER_RETRIVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
				partner = partnerService.getPartnerById(partnerId);

				valuesMap.put(CCLPConstants.PARTNER_NAME, partner.getPartnerName());
				String templateString = "";

				templateString = responseDto.getMessage();

				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				String resolvedString = sub.replace(templateString);

				responseDto.setMessage(resolvedString);
				logger.debug("Partner record for partnerId: {} has retrieved successfully {}", partnerId,
						partnerDto.toString());
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Gets a partner by name. The name can be a complete or partial Parnter name.
	 * 
	 * @param partnerName
	 *            The name of the Partner to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getPartnerByName(@RequestParam("partnerName") String partnerName) {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		ResponseDTO responseDto = null;

		if (Util.isEmpty(partnerName)) {
			logger.info("Partner name is empty");
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PARTNER_NAME,
					ResponseMessages.DOESNOT_EXISTS);
		} else {

			List<PartnerDTO> partnerDtos = partnerService.getPartnerByName(partnerName);
			if (partnerDtos.isEmpty()) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_PARTNER_RETRIEVE,
						ResponseMessages.DOESNOT_EXISTS);

				valuesMap.put(CCLPConstants.PARTNER_NAME, partnerName);
				String templateString = "";

				templateString = responseDto.getMessage();

				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				String resolvedString = sub.replace(templateString);

				responseDto.setMessage(resolvedString);

			} else {
				responseDto = responseBuilder.buildSuccessResponse(partnerDtos,
						"PARTNER_RETRIVE_" + ResponseMessages.SUCCESS, ResponseMessages.SUCCESS);
				logger.error("Failed to retrive partner details for PartnerName: {}", partnerName);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Gets a partner by Partner Id.
	 * 
	 * @param partnerId
	 *            The id of the Partner to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/getAllSupportedPursesByPartnerId/{partnerId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllSupportedPursesByPartnerId(@PathVariable("partnerId") Long partnerId)
			{
		logger.info(CCLPConstants.ENTER);

		List<PurseDTO> supportedPurseList = partnerService.getAllSupportedPursesByPartnerId(partnerId);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(supportedPurseList,
				(ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
}
