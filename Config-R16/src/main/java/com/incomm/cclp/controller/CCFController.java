package com.incomm.cclp.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.GeneralConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.domain.CCFConfDetail;
import com.incomm.cclp.dto.CCFConfigReq;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CCFService;
import com.incomm.cclp.util.ResponseBuilder;

import io.swagger.annotations.Api;

/**
 * @author Raja
 */
@RestController
@RequestMapping("/ccfConfigs")
@Api(value = "ccfconfig")
public class CCFController {

	private static final Logger logger = LogManager.getLogger(CCFController.class);

	@Autowired
	private CCFService ccfService;

	@Autowired
	private ResponseBuilder responseBuilder;
	

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createCCFConfig(@RequestBody CCFConfigReq request) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ccfService.createCCFConfig(request);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, ("CCF_ADD_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);
		responseDto.setMessage(ResponseBuilder.fillPlaceHolder("versionID", request.getVersionID(), responseDto.getMessage()));
		logger.info(GeneralConstants.LOG_RESPONSE_CODE + responseDto.getResponseCode()
				+ GeneralConstants.LOG_RESPONSE_MESSAGE + responseDto.getMessage());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateCCFConfig(@RequestBody CCFConfigReq request) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ccfService.updateCCFConfig(request);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, ("CCF_UPD_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);
		responseDto.setMessage(ResponseBuilder.fillPlaceHolder("versionID", request.getVersionID(), responseDto.getMessage()));
		logger.info(GeneralConstants.LOG_RESPONSE_CODE + responseDto.getResponseCode()
				+ GeneralConstants.LOG_RESPONSE_MESSAGE + responseDto.getMessage());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	

	@RequestMapping(value = "/ccfConfigDetails", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCCFVersionDtls(@RequestParam("CCFVersionID") String versionID)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<CCFConfDetail> ccfParamList = ccfService.getCCFVersionDtls(versionID);
		responseDto = responseBuilder.buildSuccessResponse(ccfParamList, ResponseMessages.SUCCESS_CCF_PARAM_RETRIEVE,
				ResponseMessages.SUCCESS);
		logger.info(GeneralConstants.LOG_RESPONSE_CODE + responseDto.getResponseCode()
		+ GeneralConstants.LOG_RESPONSE_MESSAGE + responseDto.getMessage());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/ccfParams", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCCFParam() throws ServiceException, JSONException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> ccfParamList = ccfService.getCCFParam();
		JSONArray jsonParamArray = new JSONArray();
		String outputJsonArray = "";

		for (Object[] result : ccfParamList) {
			JSONObject jObj = new JSONObject();
			jObj.put("paramSection", result[0]);
			jObj.put("paramId", result[1]);
			jObj.put("paramDesc", result[2]);
			jsonParamArray.put(jObj);
		}
		outputJsonArray = jsonParamArray.toString();
		responseDto = responseBuilder.buildSuccessResponse(outputJsonArray, ResponseMessages.SUCCESS_CCF_PARAM_RETRIEVE,
				ResponseMessages.SUCCESS);
		logger.info(GeneralConstants.LOG_RESPONSE_CODE + responseDto.getResponseCode()
		+ GeneralConstants.LOG_RESPONSE_MESSAGE + responseDto.getMessage());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/ccfKeys", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCCFMappingKeys() throws ServiceException, JSONException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> ccfParamList = ccfService.getCCFMappingKeys();
		JSONArray jsonParamArray = new JSONArray();
		String outputJsonArray = "";

		for (Object[] result : ccfParamList) {
			JSONObject jObj = new JSONObject();
			jObj.put("key", result[0]);
			jObj.put("value", result[1]);
			jsonParamArray.put(jObj);
		}
		outputJsonArray = jsonParamArray.toString();
		responseDto = responseBuilder.buildSuccessResponse(outputJsonArray, ResponseMessages.SUCCESS_CCF_PARAM_RETRIEVE,
				ResponseMessages.SUCCESS);
		logger.info(GeneralConstants.LOG_RESPONSE_CODE + responseDto.getResponseCode()
		+ GeneralConstants.LOG_RESPONSE_MESSAGE + responseDto.getMessage());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/ccfList", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getccfDetails() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		@SuppressWarnings("rawtypes")
		Map<String, List> data = ccfService.getCCFlist();
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(data,
				ResponseMessages.SUCCESS_CCF_PARAM_RETRIEVE, ResponseMessages.SUCCESS);
		logger.info(GeneralConstants.LOG_RESPONSE_CODE + responseDto.getResponseCode()
		+ GeneralConstants.LOG_RESPONSE_MESSAGE + responseDto.getMessage());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}


}
