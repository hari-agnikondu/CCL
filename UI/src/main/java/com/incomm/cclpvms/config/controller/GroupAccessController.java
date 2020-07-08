package com.incomm.cclpvms.config.controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.GroupAccess;
import com.incomm.cclpvms.config.model.GroupAccessDTO;
import com.incomm.cclpvms.config.model.Partner;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.GroupAccessService;
import com.incomm.cclpvms.config.service.PartnerService;
import com.incomm.cclpvms.config.service.ProductService;
import com.incomm.cclpvms.config.validator.ValidationGroupAccessImpl;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.Util;

/**
 * 
 * @author 
 *
 */

@Controller
@RequestMapping("/config/groupAccessId")
public class GroupAccessController {
	Logger logger = LogManager.getLogger(GroupAccessController.class);

	public static final String STATUS_MESSAGE_EDIT = "statusMessageEdit";
	
	public static final String PRODUCT_MAP="productMap";
	
	public static final String GROUP_ACCESS_PARTNER_LIST="groupAccessPartnersList";

	@Autowired
	GroupAccessService groupAccessService;
	@Autowired
	PartnerService partnerService;
	@Autowired
	ProductService productService;
	@Autowired
	ValidationGroupAccessImpl validationGroupAccess;

	
	@PreAuthorize("hasRole('SEARCH_GROUPACCESS_PRODUCT')")
	@RequestMapping("/groupAccessIdConfig")
	public ModelAndView groupAccessIdConfiguration() {
		logger.debug(CCLPConstants.ENTER);
		logger.debug(CCLPConstants.EXIT);
		return new ModelAndView(CCLPConstants.GROUP_ACCESS_ID_CONFIG,CCLPConstants.GROUP_ACCESS_ID_SEARCH, new GroupAccess());
	}


	/**
	 * This Method Search for the Group Access Id based on Group Access Name/Product Entered. 
	 * An empty search will return all the Available Group Access Ids.
	 */
	@PreAuthorize("hasRole('SEARCH_GROUPACCESS_PRODUCT')")
	@RequestMapping("/searchGroupAccessId")
	public ModelAndView searchGroupAccessId(@Validated(GroupAccess.ValidationStepOne.class)@ModelAttribute("groupAccessIdSearch") GroupAccess groupAccess,BindingResult bindingResult) throws ServiceException  {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();

		if(bindingResult.hasErrors())
		{
			mav.setViewName(CCLPConstants.GROUP_ACCESS_ID_CONFIG);
			return mav;
		}

		mav.addObject("groupAccessIdsList",groupAccessService.getGroupAccessIds(groupAccess));
		mav.addObject("showGrid", "true");
		mav.setViewName(CCLPConstants.GROUP_ACCESS_ID_CONFIG);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	/**
	 * 
	 * This Method is to display the Add Group Access Id Screen jsp display
	 */

	@PreAuthorize("hasRole('ADD_GROUPACCESS')")
	@RequestMapping("/addGroupAccessId")
	public ModelAndView addGroupAccessConfiguration( @ModelAttribute(CCLPConstants.ADD_GROUP_ACCESS_ID) GroupAccess groupAccess,HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		List<GroupAccessDTO>groupAccessNames =null;		
		List<Partner>  partnerList = null;

		partnerList = partnerService.getAllPartners();		
		List<String> selectedPartnerList=groupAccess.getSelectedPartnerList();

		ModelAndView mav=new ModelAndView(CCLPConstants.ADD_GROUP_ACCESS_ID);
		groupAccessNames=groupAccessService.getGroupAccess();

		if(groupAccessNames!=null){
			mav.addObject(CCLPConstants.GROUP_ACCESS_MAP, groupAccessNames.stream().collect(Collectors.toMap(GroupAccessDTO::getGroupAccessId, GroupAccessDTO::getGroupAccessName)));
		}
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));

		mav.addObject(CCLPConstants.PARTNER_LIST, partnerList);
		mav.addObject(CCLPConstants.SELECTED_PARTNER_LIST, selectedPartnerList);
		mav.addObject(CCLPConstants.ADD_ON_LOAD_FLAG,"true");
		mav.addObject(CCLPConstants.ADD_GROUP_ACCESS_ID, new GroupAccess());
		mav.addObject(CCLPConstants.EDIT_GROUP_ACCESS_ID, new GroupAccess());
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/**
	 * Save for added submit
	 */
	@PreAuthorize("hasRole('ADD_GROUPACCESS')")
	@RequestMapping(value = "/saveGroupAccess") 
	public ModelAndView saveGroupAccess( @ModelAttribute(CCLPConstants.ADD_GROUP_ACCESS_ID) GroupAccess groupAccess,BindingResult bindingResult,HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav=new ModelAndView(CCLPConstants.ADD_GROUP_ACCESS_ID);	
		List<Partner>  partnerList = null;
		partnerList = partnerService.getAllPartners();		
		List<String> selectedPartnerList=groupAccess.getSelectedPartnerList();

		validationGroupAccess.validate(groupAccess, bindingResult);
		if(bindingResult.hasErrors())
		{
			List<GroupAccessDTO> groupAccessNames=groupAccessService.getGroupAccess();

			if(groupAccessNames!=null){
				mav.addObject(CCLPConstants.GROUP_ACCESS_MAP, groupAccessNames.stream().collect(Collectors.toMap(GroupAccessDTO::getGroupAccessId, GroupAccessDTO::getGroupAccessName)));
			}
			mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
			mav.addObject(CCLPConstants.PARTNER_LIST, partnerList);
			mav.addObject(CCLPConstants.SELECTED_PARTNER_LIST, selectedPartnerList);
			mav.addObject(CCLPConstants.ADD_GROUP_ACCESS_ID,groupAccess);
			mav.addObject(CCLPConstants.ADD_ON_LOAD_FLAG,"true");
			mav.addObject(CCLPConstants.EDIT_GROUP_ACCESS_ID, new GroupAccess());
			mav.setViewName(CCLPConstants.ADD_GROUP_ACCESS_ID);
			return mav;
		}

		ResponseDTO responseDTO=groupAccessService.addGroupAccess(groupAccess);
		if(ResponseMessages.SUCCESS.equals(responseDTO.getCode()))
		{
			mav.setViewName(CCLPConstants.CONFIG_VIEW);
			mav.addObject(CCLPConstants.STATUS,responseDTO.getMessage());

		}
		else
		{
			List<GroupAccessDTO> groupAccessNames=groupAccessService.getGroupAccess();

			if(groupAccessNames!=null){
				mav.addObject(CCLPConstants.GROUP_ACCESS_MAP, groupAccessNames.stream().collect(Collectors.toMap(GroupAccessDTO::getGroupAccessId, GroupAccessDTO::getGroupAccessName)));
			}
			
			mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
			
			mav.addObject(CCLPConstants.ADD_GROUP_ACCESS_ID,groupAccess);			
			mav.addObject(CCLPConstants.PARTNER_LIST, partnerList);
			mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getMessage());
			mav.addObject(CCLPConstants.ADD_ON_LOAD_FLAG,"true");
			mav.addObject(CCLPConstants.EDIT_GROUP_ACCESS_ID, new GroupAccess());
			mav.setViewName(CCLPConstants.ADD_GROUP_ACCESS_ID);			
			List<Partner> selectedParnters=new LinkedList<>();
			for (Iterator<Partner> iterator = partnerList.iterator(); iterator
					.hasNext();) {
				Partner partner =  iterator.next();
				if(selectedParnters.size()==selectedPartnerList.size()){
					break;
				}
				if(selectedPartnerList.contains(String.valueOf(partner.getPartnerId()))){
					selectedParnters.add(partner);
				}
			}
			mav.addObject("partnerIds",selectedParnters);
		}


		logger.debug(CCLPConstants.EXIT);
		return mav; 
	}
	/**
	 * update groupaccess Name submit
	 */
	@PreAuthorize("hasRole('EDIT_GROUPACCESS')")
	@RequestMapping(value = "/updateGroupAccess") 
	public ModelAndView updateGroupAccess( @ModelAttribute(CCLPConstants.EDIT_GROUP_ACCESS_ID) GroupAccess groupAccess,BindingResult bindingResult,HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		List<GroupAccessDTO>groupAccessNames =null;	
		ModelAndView mav=new ModelAndView(CCLPConstants.ADD_GROUP_ACCESS_ID);	
		List<Partner>  partnerList = null;
		partnerList = partnerService.getAllPartners();		
		List<String> selectedPartnerList=groupAccess.getSelectedPartnerList();
		groupAccessNames=groupAccessService.getGroupAccess();

		if(groupAccess.getGroupAccessId()!=null && groupAccess.getGroupAccessId() == -1)
		{
			bindingResult.rejectValue("groupAccessId", "messageNotEmpty.editGroupAccessId.groupAccessName", "Please Select Group Access Name");

		} 
		if(groupAccess.getSelectedPartnerList().isEmpty()) 
		{
			bindingResult.rejectValue(CCLPConstants.PARTNER_LIST, "messageNotEmpty.addGroupAccessId.selectedPartnerListEmpty", "Updation fails, Partner list should not be empty");
		}
		
		if(bindingResult.hasErrors()) {
			
				if(groupAccessNames!=null){
					mav.addObject(CCLPConstants.GROUP_ACCESS_MAP, groupAccessNames.stream().collect(Collectors.toMap(GroupAccessDTO::getGroupAccessId, GroupAccessDTO::getGroupAccessName)));
				}
				
				mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
				mav.addObject(CCLPConstants.PARTNER_LIST, partnerList);
				mav.addObject(CCLPConstants.SELECTED_PARTNER_LIST, selectedPartnerList);
				mav.addObject(CCLPConstants.ADD_GROUP_ACCESS_ID, new GroupAccess());
				mav.addObject(CCLPConstants.EDIT_GROUP_ACCESS_ID,groupAccess);
				logger.debug(CCLPConstants.EXIT);
				return mav;
		}
		else {
			ResponseEntity<ResponseDTO> responseDTO=groupAccessService.updateGroupAccess(groupAccess);
			if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode()))

			{
				mav.setViewName(CCLPConstants.CONFIG_VIEW);
				mav.addObject(CCLPConstants.STATUS,responseDTO.getBody().getMessage());

			}
			else
			{
				mav.addObject(CCLPConstants.EDIT_GROUP_ACCESS_ID,groupAccess);
				if(groupAccessNames!=null){
					mav.addObject(CCLPConstants.GROUP_ACCESS_MAP, groupAccessNames.stream().collect(Collectors.toMap(GroupAccessDTO::getGroupAccessId, GroupAccessDTO::getGroupAccessName)));
				}
				mav.addObject(CCLPConstants.PARTNER_LIST, partnerList);
				mav.addObject(CCLPConstants.SELECTED_PARTNER_LIST, selectedPartnerList);		
				mav.addObject(STATUS_MESSAGE_EDIT,responseDTO.getBody().getMessage());
				mav.addObject(CCLPConstants.ADD_GROUP_ACCESS_ID, new GroupAccess());
				mav.addObject(CCLPConstants.ADD_ON_LOAD_FLAG,"checked");
				mav.setViewName(CCLPConstants.ADD_GROUP_ACCESS_ID);		
				mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
			}
			logger.debug(CCLPConstants.EXIT);
			return mav; 
		}
		
	}
	
	/**
	 * This Method is to display the Add  Access Id Screen product show screen
	 */
	@PreAuthorize("hasRole('ADD_ASSIGNGROUPACCESS_PRODUCT')")
	@RequestMapping("/assignGroupAccessIdToProduct")
	public ModelAndView assignGroupAccessIdToProduct( @ModelAttribute("assignGroupAccessId") GroupAccess groupAccess,HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		List<GroupAccessDTO>groupAccessNames =null;
		ModelAndView mav=new ModelAndView();
		
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
		groupAccessNames=groupAccessService.getGroupAccess();
		mav.addObject(CCLPConstants.GROUP_ACCESS_MAP,groupAccessNames);		
		mav.setViewName(CCLPConstants.ASSIGN_GROUP_ACCESS_ID);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/**
	 * save assign group access to product
	 */
	@PreAuthorize("hasRole('ADD_ASSIGNGROUPACCESS_PRODUCT')")
	@RequestMapping(value = "/saveAssignGroupProduct") 
	public ModelAndView saveAssignGroupProduct( @ModelAttribute("assignGroupAccessId") GroupAccess groupAccess,BindingResult bindingResult,HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();
				
		if(groupAccess.getGroupAccessId()!=null && groupAccess.getGroupAccessId() == -1)
		{
			bindingResult.rejectValue("groupAccessId", "messageNotEmpty.assignGroupAccessId.groupAccessName", "Please Select Group Access Name");

		}
		if(groupAccess.getProductId()!=null && groupAccess.getProductId() == -1)
		{
			bindingResult.rejectValue("productId", "messageNotEmpty.assignGroupAccessId.productName", "Please Select Product Name");

		}
		
		
		if(bindingResult.hasErrors())
		{	
			
			mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
			mav.addObject(CCLPConstants.GROUP_ACCESS_MAP,groupAccessService.getGroupAccess());		
			mav.setViewName("assignGroupAccessId");
			logger.debug(CCLPConstants.EXIT);
			return mav;
		}

				
		ResponseDTO responseDTO=groupAccessService.addAssignGroupAccessIdToProduct(groupAccess);
		if(ResponseMessages.SUCCESS.equals(responseDTO.getCode()))
		{
			mav.setViewName(CCLPConstants.CONFIG_VIEW);
			mav.addObject(CCLPConstants.STATUS,responseDTO.getMessage());

		}
		else
		{	
			
			mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
			mav.addObject(CCLPConstants.GROUP_ACCESS_MAP,groupAccessService.getGroupAccess());	
			mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getMessage());
			mav.setViewName("assignGroupAccessId");
		}
		
		return mav; 

	}
	/**
	 * assign product
	 */
	@PreAuthorize("hasRole('EDIT_ASSIGNGROUPACCESS_PRODUCT')")
	@RequestMapping("/editAssignGroupAccess")
	public ModelAndView editAssignGroupAccessToProduct( @ModelAttribute("groupAccessIdSearch") GroupAccess groupAccess,HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav=new ModelAndView("editAssignGroupAccess");
		
		mav.addObject(PRODUCT_MAP,groupAccessService.getProductsByAccessId(groupAccess.getGroupAccessId()));  
		List<GroupAccessDTO> groupAccessNames=	groupAccessService.getGroupAccess();
		if(groupAccessNames!=null){
			mav.addObject(CCLPConstants.GROUP_ACCESS_MAP, groupAccessNames.stream().collect(Collectors.toMap(GroupAccessDTO::getGroupAccessId, GroupAccessDTO::getGroupAccessName)));
		}
		mav.addObject(GROUP_ACCESS_PARTNER_LIST, groupAccessService.getGroupAccessProductsByAccessIdAndProductId(groupAccess.getGroupAccessId(),groupAccess.getProductId()));			
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	/**
	 * editUpdateGroupAccess
	 */
	@PreAuthorize("hasRole('EDIT_ASSIGNGROUPACCESS_PRODUCT')")
	@RequestMapping("/updateAssignGroupAccess")
	public ModelAndView updateAssignGroupAccessToProduct( @ModelAttribute("groupAccessIdSearch") GroupAccess groupAccess,HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView("editAssignGroupAccess");	
		ResponseDTO responseDTO=groupAccessService.updateAssignGroupAccessToProduct(groupAccess);
		
		if(ResponseMessages.SUCCESS.equals(responseDTO.getCode()))
		{
			mav.setViewName(CCLPConstants.CONFIG_VIEW);
			mav.addObject(CCLPConstants.STATUS,responseDTO.getMessage());
		}
		else
		{
			mav.addObject("groupAccessIdSearch",groupAccess);
			mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getMessage());
			mav.addObject(PRODUCT_MAP,groupAccessService.getProductsByAccessId(groupAccess.getGroupAccessId()));          
			List<GroupAccessDTO> groupAccessNames=	groupAccessService.getGroupAccess();
			if(groupAccessNames!=null){
				mav.addObject(CCLPConstants.GROUP_ACCESS_MAP, groupAccessNames.stream().collect(Collectors.toMap(GroupAccessDTO::getGroupAccessId, GroupAccessDTO::getGroupAccessName)));
			}
			mav.addObject(GROUP_ACCESS_PARTNER_LIST, groupAccessService.getGroupAccessProductsByAccessIdAndProductId(groupAccess.getGroupAccessId(),groupAccess.getProductId()));
		}				

		return mav; 


	}


	/**
	 * This Method calls when Delete operation performs for the cardrange from search page.
	 */
	@PreAuthorize("hasRole('DELETE_GROUPACCESS_PRODUCT')")
	@RequestMapping("/deleteGroupAccess")
	public ModelAndView deleteCardRange(@ModelAttribute("groupAccessIdSearch") GroupAccess groupAccess,HttpServletRequest req)throws ServiceException  {
		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav=new ModelAndView();

		ResponseEntity<ResponseDTO> responseDTO=groupAccessService.deleteGroupAccess(groupAccess.getGroupAccessId(),groupAccess.getProductId());

		mav.setViewName(CCLPConstants.CONFIG_VIEW);

		if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){

			mav.addObject(CCLPConstants.STATUS,responseDTO.getBody().getMessage());
		}
		else{
			mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getBody().getMessage());
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	/**
	 * View Screen for GROUP ACCESS TO PRODUCT
	 */
	
	@PreAuthorize("hasRole('VIEW_ASSIGNGROUPACCESS_PRODUCT')")
	@RequestMapping("/viewAssignGroupAccess")
	public ModelAndView viewAssignGroupAccessToProduct( @ModelAttribute("groupAccessIdSearch") GroupAccess groupAccess,HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav=new ModelAndView("viewAssignGroupAccess");
		
		mav.addObject(PRODUCT_MAP,groupAccessService.getProductsByAccessId(groupAccess.getGroupAccessId()));  
		List<GroupAccessDTO> groupAccessNames=	groupAccessService.getGroupAccess();
		if(groupAccessNames!=null){
			mav.addObject(CCLPConstants.GROUP_ACCESS_MAP, groupAccessNames.stream().collect(Collectors.toMap(GroupAccessDTO::getGroupAccessId, GroupAccessDTO::getGroupAccessName)));
		}
		mav.addObject(GROUP_ACCESS_PARTNER_LIST, groupAccessService.getGroupAccessProductsByAccessIdAndProductId(groupAccess.getGroupAccessId(),groupAccess.getProductId()));			
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}


}

