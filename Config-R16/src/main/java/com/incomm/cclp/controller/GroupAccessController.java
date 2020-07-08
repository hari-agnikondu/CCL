
package com.incomm.cclp.controller;

import io.swagger.annotations.Api;

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
import com.incomm.cclp.dto.GroupAccessDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.GroupAccessService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;
/**
 * Group Access Controller provides all the REST operations for Group Access. 
 */
@RestController
@RequestMapping("/groupAccess")
@Api(value="Group Access")
public class GroupAccessController {

	// the GroupAccessService service.
	@Autowired
	private GroupAccessService groupAccessService;

	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;


	// the logger
	private static final Logger logger = LogManager.getLogger(GroupAccessController.class);

	/**
	 * Getting Group Access details By Group Access name and Product Name
	 * 
	 * */
	@RequestMapping(value="/search",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getGroupAccessProducts(@RequestParam("groupAccessName") String groupAccessName
			,@RequestParam("productName") String productName){
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto =null;
		if (Util.isEmpty(groupAccessName)) {
			logger.info("Group access name is empty");
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_GROUP_ACCESS_NAME_NULL,ResponseMessages.DOESNOT_EXISTS);
		}
		else if(Util.isEmpty(productName)){
			logger.info("Group access name does not exists");
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_PRODUCT_NAME_NULL,ResponseMessages.DOESNOT_EXISTS);
		}
		else{
			List<GroupAccessDTO> groupAccessList=groupAccessService.getGroupAccessProducts(groupAccessName,productName);
			logger.info("Successfully retrieved group access details");
			responseDto = responseBuilder.buildSuccessResponse(groupAccessList,
					ResponseMessages.GROUP_ACCESS_RETRIEVE_000,"");
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Getting Group Access details By Group Access name and Product Name
	 * 
	 * */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getGroupAccess(){
		logger.info(CCLPConstants.ENTER);
			//Here * will give all records in group access
			List<GroupAccessDTO> groupAccessList=groupAccessService.getGroupAccess("*");
			ResponseDTO responseDto = responseBuilder.buildSuccessResponse(groupAccessList,
					ResponseMessages.GROUP_ACCESS_RETRIEVE_000,"");
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Getting Group Access partners list
	 * 
	 * */
	@RequestMapping(value="/partners",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getGroupAccessPartners(){
		logger.info(CCLPConstants.ENTER);
			List<GroupAccessDTO> groupAccessList=groupAccessService.getGroupAccessPartners();
			ResponseDTO responseDto = responseBuilder.buildSuccessResponse(groupAccessList,
					ResponseMessages.GROUP_ACCESS_RETRIEVE_000,"");
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Getting Group Access partners list by Group Access Id
	 * 
	 * */
	@RequestMapping(value="/{groupAccessId}/partners",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getGroupAccessPartnersByAccessId(@PathVariable("groupAccessId") Long groupAccessId)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
			List<GroupAccessDTO> groupAccessList=groupAccessService.getGroupAccessPartnersByAccessId(groupAccessId);
			ResponseDTO responseDto = responseBuilder.buildSuccessResponse(groupAccessList,
					ResponseMessages.GROUP_ACCESS_RETRIEVE_000,"");
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Create Group Access
	 * 
	 * */
	@RequestMapping(value="/partners",method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createGroupAccess(@RequestBody GroupAccessDTO groupAccessDTO) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
			groupAccessService.createGroupAccess(groupAccessDTO);
			ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
					ResponseMessages.GROUP_ACCESS_ADD_000,"");
			responseDto = respMessage(responseDto,groupAccessDTO,false); 
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Create Group Access Products
	 * 
	 * */
	@RequestMapping(value="/products",method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createGroupAccessProducts(@RequestBody GroupAccessDTO groupAccessDTO)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
			groupAccessService.createGroupAccessProducts(groupAccessDTO);
			ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
					ResponseMessages.GROUP_ACCESS_PRODUCT_ADD_000,"");
			responseDto = respMessage(responseDto,groupAccessDTO,true); 
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Update Group Access
	 * 
	 * */
	@RequestMapping(value="/partners",method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateGroupAccess(@RequestBody GroupAccessDTO groupAccessDTO)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		responseDto=groupAccessService.updateGroupAccess(groupAccessDTO);
		if(responseDto==null){
			 responseDto = responseBuilder.buildSuccessResponse(null,ResponseMessages.GROUP_ACCESS_UPDATED_000,"");
				responseDto = respMessage(responseDto,groupAccessDTO,false); 
		}
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	

	/**
	 * Update Group Access Products
	 * 
	 * */
	@RequestMapping(value="/products",method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateGroupAccessProducts(@RequestBody GroupAccessDTO groupAccessDTO)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		
			groupAccessService.updateGroupAccessProducts(groupAccessDTO);
			ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
					ResponseMessages.GROUP_ACCESS_PRODUCT_UPDATED_000,"");
			responseDto = respMessage(responseDto,groupAccessDTO,true);
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Delete Group Access Products
	 * 
	 * */
	@RequestMapping(value="/{groupAccessId}/{productId}",method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteGroupAccessProducts(@PathVariable("groupAccessId") Long groupAccessId,@PathVariable("productId") Long productId)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto =null;
		if (groupAccessId<=0) {
			logger.info("Group access ID cannot be negative or 0");
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_GROUP_ACCESS_ID_NULL,ResponseMessages.DOESNOT_EXISTS);
		}
		else if(productId<=0){
			logger.info("Group access ID does not exists");
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_PRODUCT_ID_NULL,ResponseMessages.DOESNOT_EXISTS);
		}
		else{
			GroupAccessDTO groupAccessDTO=	groupAccessService.deleteGroupAccessProducts(groupAccessId,productId);
			logger.info("group access deleted for id {}",groupAccessId);
			responseDto = responseBuilder.buildSuccessResponse(null,
					ResponseMessages.GROUP_ACCESS_PRODUCT_DELETE_000,""); 
			responseDto = respMessage(responseDto,groupAccessDTO,true);
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	

	/**
	 * Getting Group access products by accessId and product id
	 * 
	 * */
	@RequestMapping(value="/{groupAccessId}/{productId}",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getGroupAccessProductsByAccessIdAndProductId(@PathVariable("groupAccessId") Long groupAccessId,@PathVariable("productId") Long productId) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto =null;
		if (groupAccessId<=0) {
			logger.info("Group access ID cannot be negative or 0");
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_GROUP_ACCESS_ID_NULL,ResponseMessages.DOESNOT_EXISTS);
		}
		else if(productId<=0){
			logger.info("product ID cannot be negative or 0");
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_PRODUCT_ID_NULL,ResponseMessages.DOESNOT_EXISTS);
		}
		else{
			List<GroupAccessDTO> groupAccessDTOList=groupAccessService.getGroupAccessProductsByAccessAndProductId(groupAccessId,productId);
			responseDto = responseBuilder.buildSuccessResponse(groupAccessDTOList,
					ResponseMessages.GROUP_ACCESS_RETRIEVE_000,"");
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Building response message name 
	 * @param responseDto
	 * @param groupAccessDTO
	 * @return ResponseDTO
	 */
	public ResponseDTO respMessage(ResponseDTO responseDto, GroupAccessDTO groupAccessDTO,boolean isGroupAccessProduct)
	{
		logger.info(CCLPConstants.ENTER);
		String str="";
		if(groupAccessDTO.getGroupAccessName()!=null){
			str+=groupAccessDTO.getGroupAccessName();
		}
		Map<String,String> valuesMap = new HashMap<>();
		if(isGroupAccessProduct){
			valuesMap.put("ProductName", groupAccessDTO.getProductName());
		}
		
		valuesMap.put("GroupAccessName", str);
		responseDto.setMessage(new StrSubstitutor(valuesMap).replace(responseDto.getMessage()));
		logger.info(CCLPConstants.EXIT);
		return responseDto;
	} 
	
	

	/**
	 * Getting Products by AccessID
	 * 
	 * */
	@RequestMapping(value="/{groupAccessId}/products",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProductsByAccessId(@PathVariable("groupAccessId") Long groupAccessId) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto =null;
		if (groupAccessId<=0) {
			logger.info("Group access ID cannot be negative or 0");
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_GROUP_ACCESS_ID_NULL,ResponseMessages.DOESNOT_EXISTS);
		}
		else{
			 responseDto = responseBuilder.buildSuccessResponse(groupAccessService.getProductsByAccessId(groupAccessId),
					ResponseMessages.GROUP_ACCESS_RETRIEVE_000,"");
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Getting Group Access partners list by Group Access Id and ProductId
	 * 
	 * */
	@RequestMapping(value="/{groupAccessId}/partners/{productId}",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getGroupAccessPartnersByAccessIdAndProductId(@PathVariable("groupAccessId") Long groupAccessId,@PathVariable("productId") Long productId)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
			List<GroupAccessDTO> groupAccessList=groupAccessService.getGroupAccessPartnersByAccessIdAndProductId(groupAccessId,productId);
			ResponseDTO responseDto = responseBuilder.buildSuccessResponse(groupAccessList,
					ResponseMessages.GROUP_ACCESS_RETRIEVE_000,"");
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Getting Group Access Product list by Group Access Id
	 * 
	 * */
	@RequestMapping(value="/{groupAccessId}/groupAccessProducts",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getGroupAccessProductsByAccessId(@PathVariable("groupAccessId") Long groupAccessId)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
			List<GroupAccessDTO> groupAccessList=groupAccessService.getGroupAccessProductsByAccessId(groupAccessId);
			ResponseDTO responseDto = responseBuilder.buildSuccessResponse(groupAccessList,
					ResponseMessages.GROUP_ACCESS_RETRIEVE_000,"");
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	

}
