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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.BlockListDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.BlockListService;
import com.incomm.cclp.util.ResponseBuilder;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/blockLists")
@Api(value = "blockLists")
public class BlockListController {

	@Autowired
	private ResponseBuilder responseBuilder;
	// the logger
	private static final Logger logger = LogManager.getLogger(BlockListController.class);

	@Autowired
	BlockListService blockListService;

	/**
	 * Gets all Delivery channel list from table.
	 *
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/deliveryChannels", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> deliveryChannels() {
		logger.info(CCLPConstants.ENTER);
		List<Object[]> deliveryChannels = null;
		ResponseDTO responseDto = null;
		deliveryChannels = blockListService.getDeliveryChannelList();
		if (CollectionUtils.isEmpty(deliveryChannels)) {
			logger.info("Failed to get deliveryChannelsList");
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_DELIVERYCHANNEL_RETRIEVE,
					ResponseMessages.FAILURE);
		} else {

			logger.info("Successfully retrieved deliveryChannel list");
			responseDto = responseBuilder.buildSuccessResponse(deliveryChannels,
					ResponseMessages.SUCCESS_DELIVERYCHANNEL_RETRIEVE, ResponseMessages.SUCCESS);
		}

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Add new Block List record in table.
	 *
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createBlockList(@RequestBody BlockListDTO blockListDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();

		logger.debug("Creating new BlockList in tatble {}", blockListDto.toString());
		blockListService.createBlockList(blockListDto);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
				("BLOCKLIST_ADD_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		valuesMap.put("InstrumentId", blockListDto.getInstrumentId());
		String templateString = "";

		templateString = responseDto.getMessage();

		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);

		responseDto.setMessage(resolvedString);

		logger.info("BlockList created successfully");
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Get all Block List record from table.
	 *
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllBlockList() {
		logger.info(CCLPConstants.ENTER);
		List<BlockListDTO> partnerDtos = blockListService.getAllBlockList();
		logger.info("Performing full serach for BlockList");
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(partnerDtos,
				ResponseMessages.ALL_SUCCESS_BLOCKLIST_RETRIEVE, ResponseMessages.SUCCESS);
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Gets all Block List for a given delivery channel.
	 *
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(value = "/{channelCode}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getBlockListById(@PathVariable("channelCode") String channelCode) {
		logger.info(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		if (Long.parseLong(channelCode) <= 0) {
			logger.info("channel code is negative: {}", channelCode);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_CHANNEL_CODE,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			List<BlockListDTO> blockListDto = blockListService.getBlockListById(channelCode);
			if (CollectionUtils.isEmpty(blockListDto)) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_BLOCKLIST_RETRIEVE,
						ResponseMessages.DOESNOT_EXISTS);
				logger.info("Failed to fetch Block List for channel code {}", channelCode);
			} else {
				responseDto = responseBuilder.buildSuccessResponse(blockListDto,
						("BLOCKLIST_RETRIVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
				logger.info("Block List record for channel code: {} has retrieved successfully", channelCode);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Delete all Block List Records from table.
	 *
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteBlockList(@RequestBody List<Object> instrumentIds)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;
		blockListService.deleteBlockList(instrumentIds);
		responseDto = responseBuilder.buildSuccessResponse(null, ("BLOCKLIST_DELETE_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
