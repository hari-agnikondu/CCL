/**
 * 
 */
package com.incomm.cclp.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.incomm.cclp.dto.PackageDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.PackageService;
import com.incomm.cclp.util.ResponseBuilder;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/packages")
@Api(value="package")
public class PackageController {

	
	@Autowired
	private PackageService packageService;
	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;
	
	// the logger
	private static final Logger logger = LogManager.getLogger(PackageController.class);
	
	
	@RequestMapping(value = "/packageIdList", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getPackageIdList() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> shipmentList = packageService.getPackageIdList();
		responseDto = responseBuilder.buildSuccessResponse(shipmentList, "PAC_LIST_" + ResponseMessages.SUCCESS,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fulFillmentList", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getFulFillmentList() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> shipmentList = packageService.getfulFillmentList();
		responseDto = responseBuilder.buildSuccessResponse(shipmentList, "PAC_LIST_" + ResponseMessages.SUCCESS,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createPackage(@RequestBody PackageDTO packageDTO)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		int cnt = packageService.checkduplicatePackageId(packageDTO.getPackageId());
		if (cnt > 0) {
			responseDto = responseBuilder.buildFailureResponse((CCLPConstants.PAC_ERR + ResponseMessages.ALREADY_EXISTS),
					ResponseMessages.ALREADY_EXISTS);
		} else {
			logger.info("Creating package..");
			packageService.createPackage(packageDTO);
			responseDto = responseBuilder.buildSuccessResponse(null, ("PAC_ADD_" + ResponseMessages.SUCCESS),
					ResponseMessages.SUCCESS);
		}
		responseDto.setMessage(ResponseBuilder.fillPlaceHolder("packageId",
				packageDTO.getPackageId() + "~" + packageDTO.getDescription(),
				responseDto.getMessage()));
		
		logger.info(GeneralConstants.LOG_RESPONSE_CODE + responseDto.getResponseCode()
				+ GeneralConstants.LOG_RESPONSE_MESSAGE + responseDto.getMessage());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updatePackage(@RequestBody PackageDTO packageDTO)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		packageService.updatePackage(packageDTO);
		responseDto = responseBuilder.buildSuccessResponse(null, ("PAC_UPD_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);
		responseDto.setMessage(ResponseBuilder.fillPlaceHolder("packageId",
				packageDTO.getPackageId() + "~" + packageDTO.getDescription(),
				responseDto.getMessage()));
		logger.info(GeneralConstants.LOG_RESPONSE_CODE + responseDto.getResponseCode()
				+ GeneralConstants.LOG_RESPONSE_MESSAGE + responseDto.getMessage());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllPackages() {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<PackageDTO> packageDtos = packageService.getAllPackages();
		if (packageDtos.isEmpty()) {
			responseDto = responseBuilder.buildFailureResponse(CCLPConstants.PAC_ERR + ResponseMessages.DOESNOT_EXISTS,
					ResponseMessages.DOESNOT_EXISTS);
			logger.debug("Record Doesn't exist: " + responseDto);
		} else {
			responseDto = responseBuilder.buildSuccessResponse(packageDtos, "PAC_RETRIVE_" + ResponseMessages.SUCCESS,
					ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getPackagesByName(@RequestParam("packageName") String packageName)
			{
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<PackageDTO> packageDtos = packageService.getAllPackagesByName(packageName);
		if (packageDtos.isEmpty()) {
			responseDto = responseBuilder.buildFailureResponse(CCLPConstants.PAC_ERR+ResponseMessages.DOESNOT_EXISTS,
					ResponseMessages.DOESNOT_EXISTS);
			logger.info("Record not exist: " + responseDto);
		} else {
			responseDto = responseBuilder.buildSuccessResponse(packageDtos, "PAC_RETRIVE_" + ResponseMessages.SUCCESS,
					ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
		}
	
	@RequestMapping(value = "/{packageId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getPackageIdDtls(@PathVariable("packageId") String packageId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		logger.info("packageId: " + packageId);

		if (packageId ==null||(packageId.isEmpty())) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_FULFILLMENT_ID,
					ResponseMessages.ERR_FULFILLMENT_ID);
		} else {
			PackageDTO packageDto = packageService.getPackageIdDtls(packageId);
			if (packageDto == null) {
				responseDto = responseBuilder.buildFailureResponse(CCLPConstants.PAC_ERR+ResponseMessages.DOESNOT_EXISTS,
						ResponseMessages.DOESNOT_EXISTS);
				logger.info("Record Does not exist: " + packageId);
			} else {
				responseDto = responseBuilder.buildSuccessResponse(packageDto,
						"FUL_RETRIVE_" + ResponseMessages.SUCCESS, ResponseMessages.SUCCESS);
				logger.debug("Record for FulFillment: {} has been retrieved successfully {}", packageDto,
						packageDto.toString());
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPackageDataById/{packageIds}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getPackageDataById(@PathVariable("packageIds") List<String> packageIds) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		
		
		List<String> packageList= new ArrayList<>();
		
		for(int i=0;i<packageIds.size();i++)
		{
			String id="";
			id=packageIds.get(i).replaceAll("\\[", "");
			id=id.replaceAll("\\]", "");
			
			packageList.add(id);
		}
		
		
		List<String> packageData = packageService.getPackageByIds(packageList);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(packageData, 
				("PRS_RETRIVE_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);		
		
		responseDto.setMessage(responseDto.getMessage());
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
}
