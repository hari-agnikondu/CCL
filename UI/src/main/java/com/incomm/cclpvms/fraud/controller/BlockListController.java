package com.incomm.cclpvms.fraud.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.fraud.model.BlockList;
import com.incomm.cclpvms.fraud.service.BlockListService;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Controller
@RequestMapping("/fraud")
public class BlockListController {

	// the logger
	private static final Logger logger = LogManager.getLogger(BlockListController.class);

	@Autowired
	public BlockListService blockListService;

	@Value("${INS_USER}")
	long userId;

	@PreAuthorize("hasRole('SEARCH_BLOCKLIST')")
	@RequestMapping("/blockList")
	public ModelAndView blockList() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView(CCLPConstants.BLOCK_LIST);
		mav.addObject(CCLPConstants.BLOCK_LIST_FORM, new BlockList());
		Map<String, String> deliveryChannelList = blockListService.getAllDelivaryChannels();

		mav.addObject(CCLPConstants.BLOCKLIST_DELIVERY_CHANNEL_LIST, deliveryChannelList);
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}

	@PreAuthorize("hasRole('SEARCH_BLOCKLIST')")
	@RequestMapping("/searchBlockListByDeliveryChannel")
	public ModelAndView searchBlockListByDeliveryChannel(@ModelAttribute("blockListForm") BlockList blockListForm,
			HttpServletRequest request) throws ServiceException {

		logger.debug("ENTER " + blockListForm);

		List<BlockList> blockList = new ArrayList<>();

		ModelAndView mav = new ModelAndView(CCLPConstants.BLOCK_LIST);
		Map<String, String> deliveryChannelList = blockListService.getAllDelivaryChannels();

		mav.addObject(CCLPConstants.BLOCKLIST_DELIVERY_CHANNEL_LIST, deliveryChannelList);

		ResponseDTO responseDTO = null;
		if (blockListForm.getChannelCode() != null && !blockListForm.getChannelCode().equals("-1")) {

			logger.info("Searching for a delivery channel '{}'", blockListForm.getChannelCode());

			responseDTO = blockListService.getBlockedListByDelChnlCode(blockListForm.getChannelCode());
		} else {

			logger.info("There is no delivery channel selected, performing full search");

			responseDTO = blockListService.getAllBlockedList();

		}
		if (responseDTO != null) {
			if (responseDTO.getData() != null && responseDTO.getResult().equalsIgnoreCase("SUCCESS")) {
				logger.debug("Block list fetched successfully. Response from config service is " + responseDTO);
				ObjectMapper objectMapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				List<Object> blockListDtos = (List<Object>) responseDTO.getData();
				if (blockListDtos != null) {
					Iterator<Object> itr = blockListDtos.iterator();
					while (itr.hasNext()) {
						blockList.add(objectMapper.convertValue(itr.next(), BlockList.class));
					}
				}

			}
			mav.addObject("showGrid", "true");
		} else {
			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			mav.addObject(CCLPConstants.STATUS_MESSAGE, ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));

			logger.error("Failed to fetch block list. Response from config service is " + responseDTO);
		}

		mav.addObject(CCLPConstants.BLOCK_LIST, blockList);

		mav.addObject(CCLPConstants.BLOCK_LIST_FORM, blockListForm);

		logger.debug(CCLPConstants.EXIT);

		return mav;
	}

	@PreAuthorize("hasRole('DELETE_BLOCKLIST')")
	@RequestMapping("/deleteFromBlockList")
	public ModelAndView deleteFromBlockList(HttpServletRequest request) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		List<BlockList> blockList = new ArrayList<>();

		ModelAndView mav = new ModelAndView(CCLPConstants.BLOCK_LIST);

		ResponseDTO responseDto = null;
		String searchCode = request.getParameter("search_channelCode");
		logger.info("searchCode " + searchCode + " Delete block list from table {}"
				+ Arrays.toString(request.getParameterValues("checkDelete")));
		String[] deleteDelCode = request.getParameterValues("checkDelete");
		if (deleteDelCode != null && deleteDelCode.length > 0) {
			responseDto = blockListService.deleteBlockList(deleteDelCode);
		}

		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("blockList record '{}' has been deleted successfully", Arrays.toString(deleteDelCode));
				mav.addObject(CCLPConstants.STATUS_FLAG, "success");
			} else {
				logger.error("Failed to delete record for blocklist '{}'", Arrays.toString(deleteDelCode));
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			}
			mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
		}
		Map<String, String> deliveryChannelList = blockListService.getAllDelivaryChannels();

		mav.addObject(CCLPConstants.BLOCKLIST_DELIVERY_CHANNEL_LIST, deliveryChannelList);

		ResponseDTO responseDTO = null;
		if (searchCode != null && !searchCode.equals("-1")) {

			logger.info("Searching for a delivery channel '{}'", searchCode);

			responseDTO = blockListService.getBlockedListByDelChnlCode(searchCode);
		} else {

			logger.info("There is no delivery channel selected, performing full search");

			responseDTO = blockListService.getAllBlockedList();

		}
		if (responseDTO != null) {
			if (responseDTO.getData() != null && responseDTO.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.debug("Block list fetched successfully. Response from config service is " + responseDTO);
				ObjectMapper objectMapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				List<Object> blockListDtos = (List<Object>) responseDTO.getData();
				if (blockListDtos != null) {
					Iterator<Object> itr = blockListDtos.iterator();
					while (itr.hasNext()) {
						blockList.add(objectMapper.convertValue(itr.next(), BlockList.class));
					}
				}

			}
			mav.addObject("showGrid", "true");
		} else {

			logger.error("Failed to fetch block list. Response from config service is " + responseDTO);
		}
		mav.addObject(CCLPConstants.BLOCK_LIST, blockList);
		BlockList blockListForm = new BlockList(searchCode);
		mav.addObject(CCLPConstants.BLOCK_LIST_FORM, blockListForm);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@PreAuthorize("hasRole('ADD_BLOCKLIST')")
	@RequestMapping("/showAddBlockList")
	public ModelAndView showAddBlockList() throws ServiceException {

		ModelAndView mav = new ModelAndView("blockListAdd");

		List<List<Object>> deliveryChannels = blockListService.getDeliveryChnlTxns();
		logger.info(deliveryChannels);
		mav.addObject(CCLPConstants.DELIVERY_CHANNELS, deliveryChannels);
		mav.addObject(CCLPConstants.BLOCK_LIST_FORM, new BlockList());

		return mav;
	}

	@PreAuthorize("hasRole('ADD_BLOCKLIST')")
	@RequestMapping(value = "/addBlockList")
	public ModelAndView addBlockList(@Valid @ModelAttribute("blockListForm") BlockList blockList,
			BindingResult bindingResult) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		blockList.setInsUser(userId);

		ModelAndView mav = new ModelAndView("blockListAdd");
		if (bindingResult.hasErrors()) {
			List<List<Object>> deliveryChannels = blockListService.getDeliveryChnlTxns();
			logger.info(deliveryChannels);
			mav.addObject(CCLPConstants.DELIVERY_CHANNELS, deliveryChannels);
			mav.addObject(CCLPConstants.BLOCK_LIST_FORM, blockList);
			logger.error("Some error occured while binding the BlockList object");
			return mav;
		}

		ResponseDTO responseDto = blockListService.createBlockList(blockList);

		if (responseDto != null) {

			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("BlockList record '{}' has been added successfully", blockList.getInstrumentType());

				mav.addObject(CCLPConstants.STATUS_FLAG, "success");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.setViewName(CCLPConstants.BLOCK_LIST);
				mav.addObject(CCLPConstants.BLOCK_LIST_FORM, new BlockList());
				Map<String, String> deliveryChannelList = blockListService.getAllDelivaryChannels();

				mav.addObject(CCLPConstants.BLOCKLIST_DELIVERY_CHANNEL_LIST, deliveryChannelList);

			} else {
				logger.error("Error while adding BlockList " + responseDto.getMessage());
				mav.addObject("statusFlag", "fail");
				if (responseDto.getMessage() != null)
					mav.addObject("statusMessage", responseDto.getMessage());
				mav.addObject(CCLPConstants.BLOCK_LIST_FORM, blockList);
				List<List<Object>> deliveryChannels = blockListService.getDeliveryChnlTxns();
				logger.info(deliveryChannels);
				mav.addObject(CCLPConstants.DELIVERY_CHANNELS, deliveryChannels);
			}
		}

		logger.debug("EXIT ");
		return mav;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug(CCLPConstants.ENTER);
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException || exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject("statusMessage", errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}

}

