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
import com.incomm.cclp.dto.GroupDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.GroupService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.ValidationService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/groups")
@Api(value="group")
public class GroupController {
	
	@Autowired
	private ResponseBuilder responseBuilder;
	
	@Autowired
	private GroupService groupService;
	
	
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	
	/**
	 * To Create group   by using groupDto
	 * @param  groupDto 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> creatGroup(@RequestBody GroupDTO groupDto)throws ServiceException  {
		logger.info(CCLPConstants.ENTER);
		
		logger.info("groupDto:"+groupDto);
		
		Map<String,String> valuesMap = new HashMap<>();
		ValidationService.validateGroup(groupDto, true);
		
		groupService.createGroup(groupDto);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("GROUP_ADD_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		
		String templateString=responseDto.getMessage();

		valuesMap.put(CCLPConstants.GROUP_NAME,groupDto.getGroupName());
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		
		responseDto.setMessage(sub.replace(templateString));
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
		
	}
	/**
	 * To get all groups by using 
	 * @param  emplty 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getGroups(){
		logger.info(CCLPConstants.ENTER);		
		
		List<GroupDTO> groupDtolist=groupService.getGroups();

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(groupDtolist, 
				(CCLPConstants.GROUP_RETRIVE+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); 
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	/**
	 * To update group   by using groupDto
	 * @param  groupDto 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateGroup(@RequestBody GroupDTO groupDto) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		
		logger.info("groupDto:"+groupDto);
		
		Map<String,String> valuesMap = new HashMap<>();
		ValidationService.validateGroup(groupDto, false);
		groupService.updateGroup(groupDto);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("GROUP_UPDATED_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		 valuesMap.put(CCLPConstants.GROUP_NAME,groupDto.getGroupName());
		 String templateString=responseDto.getMessage();
		
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 responseDto.setMessage(sub.replace(templateString));
		 logger.info(CCLPConstants.EXIT);
		
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	/**
	 * To get result by groupName
	 * @param  groupName 
	 * @return the ResponseEntity with the result.
	 *  
	 */
	@RequestMapping(value="/search",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getgroupByName(@RequestParam("groupName") String groupName) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<GroupDTO> groupDtolist = groupService.getGroupByName(groupName);
		responseDto = responseBuilder.buildSuccessResponse(groupDtolist, 
				(CCLPConstants.GROUP_RETRIVE+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); 
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
		
	}
	
	/**
	 * To get result by groupId
	 * @param  groupid 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 *  
	 */
	
	@RequestMapping(value = "/{groupId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getGroupId(@PathVariable("groupId") Long groupId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		GroupDTO grpdto=new GroupDTO();
		grpdto.setGroupId(groupId);
		GroupDTO groupDto = groupService.getGroupId(grpdto);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(groupDto,(CCLPConstants.GROUP_RETRIVE+ResponseMessages.SUCCESS),ResponseMessages.SUCCESS);
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * To get result by groupId
	 * @param  groupid 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 *  
	 */
	@RequestMapping(value = "/{groupId}",method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteGroup(@PathVariable("groupId") Long groupId) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		Map<String,String> valuesMap = new HashMap<>();
		GroupDTO grpdto=new GroupDTO();
		grpdto.setGroupId(groupId);
		GroupDTO groupdto= groupService.deleteGroup(grpdto);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
				("GROUP_DELETE_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); 
		 
		 valuesMap.put(CCLPConstants.GROUP_NAME,groupdto.getGroupName());
		 String templateString=responseDto.getMessage();
			
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 responseDto.setMessage(sub.replace(templateString));
		 logger.info(responseDto.toString());
		 logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * To Approve/Reject result by groupId
	 * @param  groupid 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 *  
	 */
	
	@RequestMapping(value="/{groupId}/status",method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> changeStatus(@RequestBody GroupDTO groupDto ) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		Map<String,String> valuesMap = new HashMap<>();
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
				("GROUP_CHANGESTATUS_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		
		groupService.changeStatus( groupDto.getGroupId(), groupDto.getGroupStatus(),
				 groupDto.getGroupCheckerRemarks(),groupDto.getLastUpdateUser());
		if("APPROVED".equals(groupDto.getGroupStatus())) {
			logger.info("Approved status");
			responseDto = responseBuilder.buildSuccessResponse(null,
					("GROUP_APPROVED_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		}else if("REJECTED".equals(groupDto.getGroupStatus())) {
			logger.info("Rejected status");
			responseDto = responseBuilder.buildSuccessResponse(null,
					("GROUP_REJECTED_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		}
		GroupDTO groupDTO=new GroupDTO();
		groupDTO.setGroupId(groupDto.getGroupId());
		String groupName=groupService.getGroupId(groupDTO).getGroupName();
		 valuesMap.put(CCLPConstants.GROUP_NAME,groupName);
		 String templateString=responseDto.getMessage();
			
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 responseDto.setMessage(sub.replace(templateString));
		
		 logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto,HttpStatus.OK);
	}
	
}
