package com.incomm.cclpvms.config.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.DeliveryChannel;
import com.incomm.cclpvms.config.model.PackageID;
import com.incomm.cclpvms.config.model.Partner;
import com.incomm.cclpvms.config.model.ProgramID;
import com.incomm.cclpvms.config.model.ProgramIDDTO;
import com.incomm.cclpvms.config.model.PurseDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.PartnerService;
import com.incomm.cclpvms.config.service.ProductPurseService;
import com.incomm.cclpvms.config.service.ProductService;
import com.incomm.cclpvms.config.service.ProgramIdService;
import com.incomm.cclpvms.config.validator.ValidateProgramID;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.ClpSession;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;

@Controller
@RequestMapping("/config/programId")
public class ProgramIDController {
	private static final Logger logger = LogManager.getLogger(ProgramIDController.class);
	
	@Autowired
	public PartnerService partnerService;
	
	@Autowired
	public ProgramIdService programIdService;
	
	@Autowired
	public ValidateProgramID validateProgramId;
	
	
	@Autowired
	public ProductService productService;
	
	@Autowired
	public ProductPurseService productPurseService;
	
	@Value("${INS_USER}")
	public long insUser;
	@Autowired
	ClpSession clpSession;
	
	private static final String SUCCESS_STATUS = "successstatus";
	private static final String FAIL_STATUS = "failstatus";
	private static final String SHOW_GRID = "showGrid";
	private static final String SRCH_TYPE = "searchType";
	private static final String PROGRAM_ID_FORM = "programIdForm";
	private static final String PROGRAM_ID_CONFIG = "programIdConfig";
	private static final String PROGRAM_ID_TABLE_LIST = "programIDTableList";
	private static final String PROGRAM_ID = "programID";
	private static final String PARTNER_LIST = "partnerNameList";
	private static final String PROGRAM_ID_LIST = "programIDList";
	private static final String EDIT_PRGM_ID = "editprogramId";
	private static final String EDIT_PROGRAM_ID = "editProgramId";
	private static final String VIEW_PROGRAM_ID = "viewProgramId";
	private static final String DELETE_BOX = "deleteBox";
	private static final String ADD_PROGRAM_ID = "addProgramId"; 
	private static final String SEARCH_TYPE = "SearchType";
	private static final String UPDATE_PROGRAM_ID = "updateProgramId";
	
	private static final String TXNMAP2="txnMap2";
	private static final String TXNMAP="txnMap";
	private static final String TXNDETAILSBYCHNLNAME="txnDtlsByChnlName";
	private static final String TXNDTLSBASEDONCHNLNAME="txnDtlsBasedonChnlName";
	private static final String TXNFLAGFORCHNLNAME="transactionFlagForChnlName";
	private static final String PRG_NAME="progrmName";
	private static final String PRG_ID="progrmId";
	private static final String DELIVERY_CHNL_ID="deliveyChnlId";
	private static final String DEL_CHNL_KEY_VAL="delChnlkeyval";
	private static final String DELCHNL2="delChnl2";
	private static final String SHOW_ERR_MSG="showErrorMessage";
	
	
	
	@PreAuthorize("hasRole('SEARCH_PROGRAMID')")
	@RequestMapping("/programIdConfig")
	public ModelAndView programIdConfiguration(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
	
		logger.debug(CCLPConstants.ENTER);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject(PROGRAM_ID_FORM, new ProgramID());
		mav.addObject(DELETE_BOX, new ProgramID());
		mav.setViewName(PROGRAM_ID_CONFIG);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('ADD_PROGRAMID')")
	@RequestMapping("/showAddProgramId")
	public ModelAndView showAddProgramID(Map<String, Object> model) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		List<Partner>  partnerList = null;
		Map<Long, String> partnerMap=null;
		mav.addObject("ProgramID", new ProgramID());
		mav.addObject(PROGRAM_ID_FORM, new ProgramID());
		partnerList = partnerService.getAllPartners();
		if(partnerList!=null){
			partnerMap = partnerList.stream().collect(Collectors.toMap(Partner::getPartnerId, Partner::getPartnerName));
		}
		mav.addObject(PARTNER_LIST, partnerMap);
		mav.setViewName(ADD_PROGRAM_ID);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('SEARCH_PROGRAMID')")
	@RequestMapping("/showAllProgramIDs")
	public ModelAndView showAllProgramIDs(Map<String, Object> model,
			@ModelAttribute(PROGRAM_ID_FORM) ProgramID programIdForm, BindingResult bindingresult,
			HttpServletRequest request) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		String searchType="";
		 if(request.getParameter(SRCH_TYPE)==null) {
			 searchType="";
		 }else {
		 searchType = request.getParameter(SRCH_TYPE);
		 }
		 mav.addObject(SEARCH_TYPE, searchType);
		model.put(PROGRAM_ID_TABLE_LIST, programIdService.getAllProgramIds());
		mav.addObject(PROGRAM_ID_FORM, programIdForm);
		mav.addObject(DELETE_BOX, new ProgramID());
		mav.addObject(SHOW_GRID, "true");
		mav.setViewName(PROGRAM_ID_CONFIG);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('SEARCH_PROGRAMID')")
	@RequestMapping("/showAllProgramIDsByName")
	public ModelAndView showAllProgramIDsByName(Map<String, Object> model,
			@ModelAttribute(PROGRAM_ID_FORM) ProgramID programIdForm, BindingResult bindingresult,
			HttpServletRequest request) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		String searchType="";
		 if(request.getParameter(SRCH_TYPE)==null) {
			 searchType="";
		 }else {
		 searchType = request.getParameter(SRCH_TYPE);
		 }
		 mav.addObject(SEARCH_TYPE, searchType);
		model.put(PROGRAM_ID_TABLE_LIST, programIdService.getAllProgramIdsByName(programIdForm.getProgramIDName().trim()));
		mav.addObject(PROGRAM_ID_FORM, programIdForm);
		mav.addObject(DELETE_BOX, new ProgramID());
		mav.addObject(SHOW_GRID, "true");
		mav.setViewName(PROGRAM_ID_CONFIG);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	
	/**
	 * SHOW EDIT PROGRAM ID
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	
	@PreAuthorize("hasRole('EDIT_PROGRAMID')")
	@RequestMapping("/showEditProductID")
	public ModelAndView showEditProductID(HttpServletRequest request) throws ServiceException {
		logger.info("****showEditProductID Method ********");
		Long programId ;
		ProgramID programID = null;
		ModelAndView mav = new ModelAndView();
		try {
			programId =Long.parseLong(request.getParameter(PROGRAM_ID));
			mav.setViewName(EDIT_PROGRAM_ID);
			logger.debug("programID:: " + programId);
			
			programID = programIdService.getProgramId(programId);
			logger.info("****packageID OBJ ********" + programId);
			if(programID!=null) {
			mav.addObject(PROGRAM_ID_FORM, programID);
			
			mav.addObject(EDIT_PRGM_ID, request.getParameter(PROGRAM_ID));
			}
			mav.addObject("confirmBox", new ProgramID());
		} catch (NumberFormatException e) {
			mav.setViewName(EDIT_PROGRAM_ID);
			mav.addObject(PROGRAM_ID_FORM, programID);
			mav.addObject(PROGRAM_ID_FORM, new PackageID());
			logger.error("NumberFormatException Occured While Editing in showEditProductID()" + e);
		}
		logger.info("****EXIT showEditProductID Method ********");
		return mav;
	}
	/**
	 * ADD PROGRAM ID
	 * @param model
	 * @param programIdForm
	 * @param bindingResult
	 * @param request
	 * @return
	 * @throws ServiceException
	 * @throws Program ID Exception
	 */
	
	@PreAuthorize("hasRole('ADD_PROGRAMID')")
	@RequestMapping("/addProgramId")
	public ModelAndView addProgramId(Map<String, Object> model,
			 @ModelAttribute(PROGRAM_ID_FORM) ProgramID programIdForm,
			BindingResult bindingResult,HttpServletRequest request)
					throws ServiceException {

		
		ModelAndView mav = new ModelAndView();
		List<Partner>  partnerList = null;
		Map<Long, String> partnerMap=null;
		ProgramIDDTO programIDDTO = new ProgramIDDTO();
		ResponseDTO responseDTO = null;
		String programIDName ="";
		String description="";
		Long insUser = clpSession.getUserId();
		String message = "";
		partnerList = partnerService.getAllPartners();
		if(partnerList!=null){
			partnerMap = partnerList.stream().collect(Collectors.toMap(Partner::getPartnerId, Partner::getPartnerName));
		}
		validateProgramId.validate(programIdForm, bindingResult);

			if (bindingResult.hasErrors()) {
				mav.addObject(PARTNER_LIST, partnerMap);
				mav.setViewName(ADD_PROGRAM_ID);
				return mav;

			} 
			else
			{
				
				if (Util.isEmpty(programIdForm.getProgramIDName())) {
					programIDName="";
				}else {
					programIDName = programIdForm.getProgramIDName().trim();
				}
				
				
				
				if (Util.isEmpty(programIdForm.getDescription())) {
					description="";
				}else {
					description = programIdForm.getDescription();
				}
								
					

			
		 
			programIDDTO.setProgramIDName(programIDName);
			programIDDTO.setPartnerId(programIdForm.getPartnerId());
			programIDDTO.setInsUser(insUser);
			programIDDTO.setInsDate(new Date());
			programIDDTO.setLastUpdDate(new Date());
			programIDDTO.setLastUpdUser(insUser);
			programIDDTO.setDescription(description);
		
			mav.addObject(PARTNER_LIST, partnerMap);

				logger.debug("Before Calling merchantService.createMerchant");
				responseDTO = programIdService.createProgramID(programIDDTO);
				logger.debug("after Calling issuerservice.createIssuer");
				logger.info("Message from createIssuer method: " + responseDTO.getMessage());

				logger.debug("CCLP-VMS Response Code : "+responseDTO.getCode()+" Error Message : "+responseDTO.getMessage());
				if (responseDTO.getCode().equalsIgnoreCase("000")) 
				{ 
					message=responseDTO.getMessage(); 
					mav.addObject(SUCCESS_STATUS, message);
					mav.setViewName(PROGRAM_ID_CONFIG);
					mav.addObject(PROGRAM_ID_FORM,  new ProgramIDDTO());
					mav.addObject(DELETE_BOX, new ProgramID());
					
				} else {
					message=responseDTO.getMessage(); 
					mav.addObject(FAIL_STATUS, message);
					mav.setViewName(ADD_PROGRAM_ID);
					mav.addObject(PARTNER_LIST, partnerMap);
					mav.addObject(PROGRAM_ID_FORM, programIDDTO);
				}	
			}
			logger.debug("EXIT");
		return mav;
	}
	/**
	 * UPDATE PROGRAM ID
	 * @param programIdForm
	 * @param result
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	
	
	@PreAuthorize("hasRole('EDIT_PROGRAMID')")
	@RequestMapping(value = "/updateProgramId")
	public ModelAndView updateProgramId( @ModelAttribute(PROGRAM_ID_FORM) ProgramID programIdForm,
			BindingResult result, HttpServletRequest request) throws ServiceException {
		logger.info("****update programId Method ********");
		ModelAndView mav = new ModelAndView();
		ResponseDTO responseDTO = null;
		String programid=null ;
		Long insUser=clpSession.getUserId();
		String message = "";
		mav.addObject("confirmBox", new ProgramID());

		if (request.getParameter(EDIT_PRGM_ID) != null
				&& !request.getParameter(EDIT_PRGM_ID).equalsIgnoreCase("0")) {
			programid = request.getParameter(EDIT_PRGM_ID);
		}

		programIdForm.setProgramID(Long.parseLong(programid));
		
		programIdForm.setLastUpdUser(insUser);

		responseDTO = programIdService.udpateProgramId(programIdForm);
		logger.debug("RESPONSE CODE: " + responseDTO.getCode() + " Error Message : " + responseDTO.getMessage());
		message = responseDTO.getMessage();
		if (ResponseMessages.SUCCESS.equals(responseDTO.getCode().trim())) {
			mav.addObject(SUCCESS_STATUS, message);
			mav.setViewName(PROGRAM_ID_CONFIG);
			mav.addObject(PROGRAM_ID_FORM, new ProgramID());

		} else {
			
			mav.addObject(FAIL_STATUS, message);
			mav.setViewName(EDIT_PROGRAM_ID);
			mav.addObject(PROGRAM_ID_FORM, programIdForm);
		}

		mav.addObject(DELETE_BOX, new PackageID());
		logger.info("**** EXIT addPackage ********");
		return mav;
	}
	
	
	@PreAuthorize("hasRole('DELETE_PROGRAMID')")
	@RequestMapping("/deleteProgramID")
	public ModelAndView deleteProgramID( @ModelAttribute(PROGRAM_ID_FORM) ProgramID programIdForm, HttpServletRequest request) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		List<ProgramIDDTO> programIdDTOList = null;
		ProgramIDDTO programDTOobj = new ProgramIDDTO();
		String id ="";
		String searchedName="";
		/**String deletedName="";*/
		String searchType="";
		String message="";
		ProgramID program = new ProgramID();
		ModelAndView mav = new ModelAndView();
		Long programId=null;
	
			mav.setViewName(PROGRAM_ID_CONFIG);
			 id = request.getParameter(PROGRAM_ID);
			 searchedName = request.getParameter("searchedName");
			 /**if(request.getParameter("deletedName")!=null)
			 {
			 deletedName=request.getParameter("deletedName");
			 }*/
			 logger.info("id and SearchedName : "+id +"  "+searchedName);
			if (searchedName == null || searchedName.isEmpty()) {
				searchedName = request.getParameter("retrievedName");
			}
			mav.addObject("SearchedName", searchedName);
			searchType = request.getParameter(SRCH_TYPE);
			mav.addObject(SEARCH_TYPE, searchType);
			logger.info("SearchType : "+searchType);
			programId = Long.valueOf(id);
			programDTOobj.setProgramID(programId);
		
	
				logger.debug("Before Calling issuerservice.deleteIssuer()");
				ResponseEntity<ResponseDTO> responsedTO=programIdService.deleteProgramIddetails(programIdForm);
				logger.debug("After Calling issuerservice.deleteIssuer()");

				if (responsedTO.getBody().getCode().equalsIgnoreCase("000")) { 
					message=responsedTO.getBody().getMessage();
					mav.addObject(SUCCESS_STATUS, message);
					mav.setViewName(PROGRAM_ID_CONFIG);
					mav.addObject(DELETE_BOX, new ProgramID());
				}	
				
			
				else
				{
					message=responsedTO.getBody().getMessage();
					mav.addObject(FAIL_STATUS, message);
					mav.setViewName(PROGRAM_ID_CONFIG);
					mav.addObject(DELETE_BOX, new ProgramID());
				}
			
				if (searchType != null && searchedName!=null && 
						!searchedName.isEmpty() && searchType.equalsIgnoreCase("byName")) {
					logger.info("Before Calling merchantService.getMerchantsByName() with name "+searchedName);
					programIdDTOList = programIdService.getAllProgramIdsByName(searchedName);
					logger.debug("After Calling merchantService.getMerchantsByName()");
				} else {
					logger.debug("Before Calling merchantService.getAllMerchants()");
					programIdDTOList = programIdService.getAllProgramIds();
					logger.debug("After Calling merchantService.getAllMerchants()");
				}

				mav.addObject(PROGRAM_ID_TABLE_LIST, programIdDTOList);
				mav.addObject(SHOW_GRID, "true");

				

				program.setProgramIDName(searchedName);
				mav.addObject(PROGRAM_ID_FORM, new ProgramIDDTO());
				mav.addObject(DELETE_BOX, new ProgramID());

			logger.debug("EXIT");
	
			return mav;
			
	}
	
	@PreAuthorize("hasRole('VIEW_PROGRAMID')")
	@RequestMapping("/showViewProgramID")
	public ModelAndView showViewProgramID(HttpServletRequest request) throws ServiceException {
		logger.info("****showEditProductID Method ********");
		Long programId ;
		ProgramID programID = null;
		ModelAndView mav = new ModelAndView();
		try {
			programId =Long.parseLong(request.getParameter(PROGRAM_ID));
			mav.setViewName(VIEW_PROGRAM_ID);
			logger.debug("programID in view Screen:: " + programId);
			
			programID = programIdService.getProgramId(programId);
			logger.info("****packageID OBJect ********" + programId);
			if(programID!=null) {
			mav.addObject(PROGRAM_ID_FORM, programID);
			
			mav.addObject(EDIT_PRGM_ID, request.getParameter(PROGRAM_ID));
			}
			mav.addObject("confirmBox", new ProgramID());
		} catch (NumberFormatException e) {
			mav.setViewName(VIEW_PROGRAM_ID);
			mav.addObject(PROGRAM_ID_FORM, programID);
			mav.addObject(PROGRAM_ID_FORM, new PackageID());
			logger.error("NumberFormatException Occured While Viewing showViewProgramID()" + e);
		}
		logger.info("****EXIT showViewProgramID Method ********");
		return mav;
	}
	@RequestMapping("/showUpdateProgramId")
	public ModelAndView showUpdateProgramId(Map<String, Object> model,HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		
		List<ProgramIDDTO>  programIdDropDown = programIdService.getAllProgramIds();
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
		mav.addObject(PROGRAM_ID_LIST, programIdDropDown);
		List<Object> txnDtls=productService.getDeliveryChnlTxns();
		logger.debug(txnDtls);
	
		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST,txnDtls.get(0));
		mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
		//mav.addObject(CCLPConstants.PURSES, purseList);
		
		mav.addObject(PROGRAM_ID_FORM, new ProgramID());
		mav.setViewName(UPDATE_PROGRAM_ID);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	
	@RequestMapping("/saveLimitAttributesByProgramId")
	public ModelAndView saveLimits(
	@ModelAttribute("programIdForm") ProgramID programIdForm,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView(UPDATE_PROGRAM_ID);
		logger.debug(CCLPConstants.ENTER);
		logger.debug("programIdForm in save Limits "+programIdForm);
		Long pid=null;
		String pname=null;
		String lf=null;
		String delchanl=null;
		String transactn=null;
		Long purseId=null;

		
		if(programIdForm.getLimitsFees()!=null) {
			 lf=programIdForm.getLimitsFees();
		}
		if(programIdForm.getTransaction()!=null) {
			transactn=programIdForm.getTransaction();
		}
		if(request.getParameter(PRG_ID)!=null && request.getParameter(PRG_ID)!="" && !request.getParameter(PRG_ID).isEmpty() ) {
		pid=Long.parseLong(request.getParameter(PRG_ID));
		}
		if(request.getParameter(PRG_NAME)!=null && request.getParameter(PRG_NAME)!="")
		pname=request.getParameter(PRG_NAME);
		if(programIdForm.getProgramIDName()!=null && programIdForm.getProgramIDName().contains("~")) {
		String[] p=	programIdForm.getProgramIDName().split("~");
		pid=Long.parseLong(p[0]);
		pname=p[1];
		}
		purseId=Long.parseLong(request.getParameter("purseList"));
		
		delchanl=request.getParameter(DELIVERY_CHNL_ID);
		programIdForm.setProgramID(pid);
		programIdForm.setProgramIDName(pname);
		programIdForm.setPurseList(String.valueOf(purseId));
		
		List<ProgramIDDTO>  programIdDropDown = programIdService.getAllProgramIds();
		
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(request));
		mav.addObject(PROGRAM_ID_LIST, programIdDropDown);
	
		
		List<Object> txnDtls=productService.getDeliveryChnlTxns();
		
		List<Object> txnDtlsByChnlName=productService.getDeliveryChnlTxnsByChnlCode(delchanl);
	
		List<Object> txnDtlsByChnldata=(List<Object>) txnDtlsByChnlName.get(0);
		DeliveryChannel delChnl=(DeliveryChannel) txnDtlsByChnldata.get(0);
		Map<String,String> txnMap=delChnl.getTransactionMap();
				
		List<Object> txnDtlsBasedonChnlName=productService.getDeliveryChnlTxnsByChnlCodeTxnName(delchanl,transactn);
		
		List deldata= (List) txnDtlsBasedonChnlName.get(0);
		DeliveryChannel delChnl2=(DeliveryChannel) deldata.get(0);
		Map<String,String> txnMap2=delChnl2.getTransactionMap();
		List<PurseDTO>  purseIdDropDown = partnerService.getProgramIDPartnerPurses(pid);

		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST,txnDtls.get(0));
		
		mav.addObject(DELIVERY_CHNL_ID,delchanl);
		mav.addObject(TXNDETAILSBYCHNLNAME, txnDtlsByChnlName);
		mav.addObject(TXNMAP, txnMap);
		mav.addObject(TXNDTLSBASEDONCHNLNAME, txnDtlsBasedonChnlName.get(0));
		mav.addObject(TXNMAP2, txnMap2);
		mav.addObject(TXNFLAGFORCHNLNAME, txnDtlsBasedonChnlName.get(1));
		mav.addObject(DELCHNL2, delChnl2);
		mav.addObject(CCLPConstants.PROGRAM_PARTNER_PURSE_LIST,purseIdDropDown);
		String str1=txnMap2.keySet().toString().replace("[", "");
		
		str1=str1.replace("]","");
		
		mav.addObject(DEL_CHNL_KEY_VAL,delChnl2.getDeliveryChnlShortName()+"_"+str1);
		
		if(lf!=null && !lf.isEmpty() && lf.equalsIgnoreCase("Limits"))
		{
		mav.addObject("showLimits", "true");
		}
		
		logger.debug("Attributes to be updated for {} and purseId {}",lf,purseId);
		ResponseDTO responseDto = productPurseService.addLimitAttributesByProgramID(programIdForm.getProductAttributes(),programIdForm.getProgramID(),purseId);
		
		if(responseDto.getResult()!=null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
			logger.info("Limit record '{}' has been updated successfully");
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,  ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {
			
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.FAILURE);
			
			if (responseDto.getMessage() != null) {
				List<String> errorMessage= (List<String>) responseDto.getData();
			
			if(responseDto.getData()!=null) {
				mav.addObject(SHOW_ERR_MSG, "true");
				String[] err= errorMessage.toArray(new String[0]);

					
				mav.addObject(CCLPConstants.STATUS_MESSAGE, err);
				
			}
			}
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,   ResourceBundleUtil.getMessage(ResponseMessages.PRODUCT_MONTHLYFEECAP_UPDATE_FAIL));
		}

	
	

	mav.addObject(PROGRAM_ID_FORM, programIdForm);	
	mav.setViewName(UPDATE_PROGRAM_ID);
	logger.debug(CCLPConstants.EXIT);
	return mav;

		
		
	}
	
	
@RequestMapping("/saveTxnFeeAttributesByProgramId")
	
	public ModelAndView saveTxnFee(
	@ModelAttribute("programIdForm") ProgramID programIdForm,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView(UPDATE_PROGRAM_ID);
		logger.debug(CCLPConstants.ENTER);
		logger.debug("programId form in save txn fee "+programIdForm);
		Long pid=null;
		String pname=null;
		String lf=null;
		String delchanl=null;
		String transactn=null;
		Long purseId=null;
		
		if(programIdForm.getLimitsFees()!=null) {
			 lf=programIdForm.getLimitsFees();
		}
		if(programIdForm.getTransaction()!=null) {
			transactn=programIdForm.getTransaction();
		}
		if(request.getParameter(PRG_ID)!=null && request.getParameter(PRG_ID)!="" &&  !request.getParameter(PRG_ID).isEmpty()) {
		pid=Long.parseLong(request.getParameter(PRG_ID));
		}
		if(request.getParameter(PRG_NAME)!=null && request.getParameter(PRG_NAME)!="")
		pname=request.getParameter(PRG_NAME);
		if(programIdForm.getProgramIDName()!=null && programIdForm.getProgramIDName().contains("~")) {
		String[] p=	programIdForm.getProgramIDName().split("~");
		pid=Long.parseLong(p[0]);
		pname=p[1];
		}
		purseId=Long.parseLong(request.getParameter("purseList"));
		delchanl=request.getParameter(DELIVERY_CHNL_ID);
		programIdForm.setProgramID(pid);
		programIdForm.setProgramIDName(pname);
		programIdForm.setPurseList(String.valueOf(purseId));
		List<ProgramIDDTO>  programIdDropDown = programIdService.getAllProgramIds();
		
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(request));
		mav.addObject(PROGRAM_ID_LIST, programIdDropDown);
	
		
		List<Object> txnDtls=productService.getDeliveryChnlTxns();
		
		List<Object> txnDtlsByChnlName=productService.getDeliveryChnlTxnsByChnlCode(delchanl);
	
		List<Object> txnDtlsByChnldata=(List<Object>) txnDtlsByChnlName.get(0);
		DeliveryChannel delChnl=(DeliveryChannel) txnDtlsByChnldata.get(0);
		Map<String,String> txnMap=delChnl.getTransactionMap();
		
		
		List<Object> txnDtlsBasedonChnlName=productService.getDeliveryChnlTxnsByChnlCodeTxnName(delchanl,transactn);
		
		List deldata= (List) txnDtlsBasedonChnlName.get(0);
		DeliveryChannel delChnl2=(DeliveryChannel) deldata.get(0);
		Map<String,String> txnMap2=delChnl2.getTransactionMap();
		List<PurseDTO>  purseIdDropDown = partnerService.getProgramIDPartnerPurses(pid);
		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST,txnDtls.get(0));
		
		mav.addObject(DELIVERY_CHNL_ID,delchanl);
		mav.addObject(TXNDETAILSBYCHNLNAME, txnDtlsByChnlName);
		mav.addObject(TXNMAP, txnMap);
		mav.addObject(TXNDTLSBASEDONCHNLNAME, txnDtlsBasedonChnlName.get(0));
		mav.addObject(TXNMAP2, txnMap2);
		mav.addObject(TXNFLAGFORCHNLNAME, txnDtlsBasedonChnlName.get(1));
		mav.addObject(DELCHNL2, delChnl2);
		mav.addObject(CCLPConstants.PROGRAM_PARTNER_PURSE_LIST,purseIdDropDown);
		String str1=txnMap2.keySet().toString().replace("[", "");
		
		str1=str1.replace("]","");
		
		mav.addObject(DEL_CHNL_KEY_VAL,delChnl2.getDeliveryChnlShortName()+"_"+str1);
		
		
		 if(lf!=null && !lf.isEmpty() && lf.equalsIgnoreCase("Transaction Fees"))
		{
		mav.addObject("showTxnFee", "true");	
		}
		 logger.debug("Attributes to be updated for {} and purseId {}",lf,purseId);
		//ResponseDTO responseDto = productService.addAttributesByProgramID(programIdForm.getProductAttributes(),programIdForm.getProgramID());
		 ResponseDTO responseDto = productPurseService.addTxnFeeAttributesByProgramID(programIdForm.getProductAttributes(),programIdForm.getProgramID(),purseId);
		if(responseDto.getResult()!=null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
			logger.info("Save Txn Fee record by program ID  '{}' has been updated successfully");
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,  ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {
			
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.FAILURE);
			
			if (responseDto.getMessage() != null) {
				List<String> errorMessage= (List<String>) responseDto.getData();
			
			if(responseDto.getData()!=null) {
				mav.addObject(SHOW_ERR_MSG, "true");
				String[] err= errorMessage.toArray(new String[0]);

					
				mav.addObject(CCLPConstants.STATUS_MESSAGE, err);
				
			}
			}
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,   ResourceBundleUtil.getMessage(ResponseMessages.PRODUCT_MONTHLYFEECAP_UPDATE_FAIL));
		}

	
	mav.addObject(PROGRAM_ID_FORM, programIdForm);	
	mav.setViewName(UPDATE_PROGRAM_ID);
	logger.debug(CCLPConstants.EXIT);
	return mav;

		
		
	}

@RequestMapping("/saveMaintenanceFeeAttributesByProgramId")

public ModelAndView saveMaintenanceFee(
@ModelAttribute("programIdForm") ProgramID programIdForm,HttpServletRequest request) throws ServiceException {
	ModelAndView mav = new ModelAndView(UPDATE_PROGRAM_ID);
	logger.debug(CCLPConstants.ENTER);
	logger.debug("programId form in save maintenance fee "+programIdForm);
	Long pid=null;
	String pname=null;
	String lf=null;
	String delchanl=null;
	String transactn=null;
	Long purseId=null;
	
	if(programIdForm.getLimitsFees()!=null) {
		 lf=programIdForm.getLimitsFees();
	}
	if(programIdForm.getTransaction()!=null) {
		transactn=programIdForm.getTransaction();
	}
	if(request.getParameter(PRG_ID)!=null && request.getParameter(PRG_ID)!="" && !request.getParameter(PRG_ID).isEmpty() ) {
	pid=Long.parseLong(request.getParameter(PRG_ID));
	}
	if(request.getParameter(PRG_NAME)!=null && request.getParameter(PRG_NAME)!="")
	pname=request.getParameter(PRG_NAME);
	if(programIdForm.getProgramIDName()!=null && programIdForm.getProgramIDName().contains("~")) {
	String[] p=	programIdForm.getProgramIDName().split("~");
	pid=Long.parseLong(p[0]);
	pname=p[1];
	}
	purseId=Long.parseLong(request.getParameter("purseList"));
	delchanl=request.getParameter(DELIVERY_CHNL_ID);
	programIdForm.setProgramID(pid);
	programIdForm.setProgramIDName(pname);
	programIdForm.setPurseList(String.valueOf(purseId));
	List<ProgramIDDTO>  programIdDropDown = programIdService.getAllProgramIds();
	
	mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(request));
	mav.addObject(PROGRAM_ID_LIST, programIdDropDown);

	
	List<Object> txnDtls=productService.getDeliveryChnlTxns();
	
	List<Object> txnDtlsByChnlName=productService.getDeliveryChnlTxnsByChnlCode(delchanl);

	List<Object> txnDtlsByChnldata=(List<Object>) txnDtlsByChnlName.get(0);
	DeliveryChannel delChnl=(DeliveryChannel) txnDtlsByChnldata.get(0);
	Map<String,String> txnMap=delChnl.getTransactionMap();
	
	
	List<Object> txnDtlsBasedonChnlName=productService.getDeliveryChnlTxnsByChnlCodeTxnName(delchanl,transactn);
	
	List deldata= (List) txnDtlsBasedonChnlName.get(0);
	DeliveryChannel delChnl2=(DeliveryChannel) deldata.get(0);
	Map<String,String> txnMap2=delChnl2.getTransactionMap();
	List<PurseDTO>  purseIdDropDown = partnerService.getProgramIDPartnerPurses(pid);
	mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST,txnDtls.get(0));
	
	mav.addObject(DELIVERY_CHNL_ID,delchanl);
	mav.addObject(TXNDETAILSBYCHNLNAME, txnDtlsByChnlName);
	mav.addObject(TXNMAP, txnMap);
	mav.addObject(TXNDTLSBASEDONCHNLNAME, txnDtlsBasedonChnlName.get(0));
	mav.addObject(TXNMAP2, txnMap2);
	mav.addObject(TXNFLAGFORCHNLNAME, txnDtlsBasedonChnlName.get(1));
	mav.addObject(DELCHNL2, delChnl2);
	mav.addObject(CCLPConstants.PROGRAM_PARTNER_PURSE_LIST,purseIdDropDown);
	
	String str1=txnMap2.keySet().toString().replace("[", "");
	
	str1=str1.replace("]","");
	
	mav.addObject(DEL_CHNL_KEY_VAL,delChnl2.getDeliveryChnlShortName()+"_"+str1);
	
	
		if (lf != null && !lf.isEmpty() && lf.equalsIgnoreCase("Maintenance Fees")) {

		if (transactn != null && transactn.equalsIgnoreCase("weeklyfee")) {

			mav.addObject("showWeeklyFee", "true");
		} else if (transactn != null && transactn.equalsIgnoreCase("dormancyfee")) {

			mav.addObject("showDormancyFee", "true");
		}

		else if (transactn != null && transactn.equalsIgnoreCase("annualfee")) {

			mav.addObject("showAnnualFee", "true");
		}

		else if (transactn != null && transactn.equalsIgnoreCase("monthlyfee")) {

			mav.addObject("showMonthlyFee", "true");
		}
		mav.addObject("showMntFee", "true");
	}
	
	ResponseDTO responseDto = productService.addAttributesByProgramID(programIdForm.getProductAttributes(),programIdForm.getProgramID());
	
	if(responseDto.getResult()!=null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
		logger.info("Txn Fee record '{}' has been updated successfully");
		mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
		if (responseDto.getMessage() != null)
			mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
		else
			mav.addObject(CCLPConstants.STATUS_MESSAGE,  ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
	} else {
		
		mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.FAILURE);
		
		if (responseDto.getMessage() != null) {
			List<String> errorMessage= (List<String>) responseDto.getData();
		
		if(responseDto.getData()!=null) {
			mav.addObject(SHOW_ERR_MSG, "true");
			String[] err= errorMessage.toArray(new String[0]);

				
			mav.addObject(CCLPConstants.STATUS_MESSAGE, err);
			
		}
		}
		else
			mav.addObject(CCLPConstants.STATUS_MESSAGE,   ResourceBundleUtil.getMessage(ResponseMessages.PRODUCT_MONTHLYFEECAP_UPDATE_FAIL));
	}


mav.addObject(PROGRAM_ID_FORM, programIdForm);	
mav.setViewName(UPDATE_PROGRAM_ID);
logger.debug(CCLPConstants.EXIT);
return mav;

	
	
}
	/**
	 * save monthly fee cap
	 * @param exception
	 * @return
	 */
	
	@RequestMapping("/showAttributesOfMonthlyFeeCap")
	public ModelAndView saveAttributesOfMonthlyFeeCap( @ModelAttribute("programIdForm") ProgramID programIdForm,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView(UPDATE_PROGRAM_ID);
		logger.debug(CCLPConstants.ENTER);
		logger.debug("productForm "+programIdForm);
		Long pid=null;
		List<String> dateStr=new ArrayList<>();
		
		
		for (int n=1;n<=31;n++)
			
		{
			dateStr.add(String.valueOf(n));	
		}
			
		mav.addObject("dateStr", dateStr);
		
		String splitter=null;
		if(programIdForm.getProgramIDName()!=null && programIdForm.getProgramIDName()!="-1") {
		splitter=programIdForm.getProgramIDName();
		String[] programIdDatatoSplit=splitter.split("~");
		if (programIdDatatoSplit[0] != null && !programIdDatatoSplit[0].equalsIgnoreCase("-1") ) {
			programIdForm.setProgramID(Long.parseLong(programIdDatatoSplit[0]));
			programIdForm.setProgramIDName(programIdDatatoSplit[1]);
			String[] p=	programIdForm.getProgramIDName().split("~");
			pid=Long.parseLong(programIdDatatoSplit[0]);
		}
		}
		
		List<ProgramIDDTO>  programIdDropDown = programIdService.getAllProgramIds();
		List<PurseDTO>  purseIdDropDown = partnerService.getProgramIDPartnerPurses(pid);
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(request));
		mav.addObject(PROGRAM_ID_LIST, programIdDropDown);
		mav.addObject(CCLPConstants.PROGRAM_PARTNER_PURSE_LIST,purseIdDropDown);
		List<Object> txnDtls=productService.getDeliveryChnlTxns();
		logger.debug(txnDtls);
		

		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST,txnDtls.get(0));
	
		mav.addObject("ShowMonthlyFeeCap",true);
		mav.addObject("productForm", programIdForm);
		
		
		
		
		return mav;
	}
	
	
	
	@RequestMapping("/saveMonthlyFeeCapAttributesByProgramId")
	public ModelAndView saveMonthlyFeeCap( @ModelAttribute("programIdForm") ProgramID programIdForm,HttpServletRequest request) throws ServiceException {
		ModelAndView mav = new ModelAndView(UPDATE_PROGRAM_ID);
		logger.debug(CCLPConstants.ENTER);
		String splitter=null;
		logger.debug("programIdForm "+programIdForm);
		Long purseId=Long.parseLong(request.getParameter("purseList"));
		if(programIdForm.getProgramIDName()!=null && !programIdForm.getProgramIDName().equalsIgnoreCase("-1")) {
		splitter=programIdForm.getProgramIDName();
		String[] programIdDatatoSplit=splitter.split("~");
		if (programIdDatatoSplit[0] != null && !programIdForm.getProgramIDName().equalsIgnoreCase("-1")) {
			programIdForm.setProgramID(Long.parseLong(programIdDatatoSplit[0]));
			programIdForm.setProgramIDName(programIdDatatoSplit[1]);
			programIdForm.setPurseList(String.valueOf(purseId));
		}
		}
		
		
		List<String> dateStr=new ArrayList<>();
		
		
		for (int n=1;n<=31;n++)
			
		{
			dateStr.add(String.valueOf(n));	
		}
			
		mav.addObject("dateStr", dateStr);
		List<ProgramIDDTO>  programIdDropDown = programIdService.getAllProgramIds();
		List<PurseDTO>  purseIdDropDown = partnerService.getProgramIDPartnerPurses(programIdForm.getProgramID());
		
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(request));
		mav.addObject(PROGRAM_ID_LIST, programIdDropDown);
		mav.addObject(CCLPConstants.PROGRAM_PARTNER_PURSE_LIST,purseIdDropDown);
		ResponseDTO responseDto = productPurseService.addMonthlyCapAttributesByProgramID(programIdForm.getProductAttributes(),programIdForm.getProgramID(),purseId);
		//ResponseDTO responseDto = productService.addAttributesByProgramID(programIdForm.getProductAttributes(),programIdForm.getProgramID());
		
		if(responseDto.getResult()!=null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
			logger.info("save monthly fee cap Fee record '{}' has been updated successfully");
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,  ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {
			
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.FAILURE);
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,   ResourceBundleUtil.getMessage(ResponseMessages.PRODUCT_MONTHLYFEECAP_UPDATE_FAIL));
		}
		
		List<Object> txnDtls=productService.getDeliveryChnlTxns();
		logger.debug(txnDtls);
		

		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST,txnDtls.get(0));

	mav.addObject("ShowMonthlyFeeCap",true);
	mav.addObject(PROGRAM_ID_FORM, programIdForm);	
	mav.setViewName(UPDATE_PROGRAM_ID);
	logger.debug(CCLPConstants.EXIT);
	return mav;
	}
	
	
	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug("Exception for PROGRAMID starts here");
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException || 
				exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject(FAIL_STATUS, errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug("exception for PROGRAM ID ends here");

		return mav;
	}
	
	
	@RequestMapping("/goUpdateAttributesByProgramId")
	public ModelAndView goUpdateProgramIdAttributes(Map<String, Object> model, HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		Long pid=null;
		String desc=null;
		String lf=null;
		String delchanl=null;
		String transactn=null;
		String pname=null;
		String purse=null;
		ProgramID programId=new ProgramID();
		
		List<ProgramIDDTO>  programIdDropDown = programIdService.getAllProgramIds();
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
		mav.addObject(PROGRAM_ID_LIST, programIdDropDown);
		List<Object> txnDtls=productService.getDeliveryChnlTxns();
		
		
		pid=Long.parseLong(req.getParameter(PRG_ID));
		pname=req.getParameter(PRG_NAME);
		desc=req.getParameter("descId");
		lf=req.getParameter("LfId");
		delchanl=req.getParameter(DELIVERY_CHNL_ID);
		transactn=req.getParameter("txnId");
		purse=req.getParameter("pursId");
		
		programId.setProgramID(pid);
		programId.setProgramIDName(pname);
		programId.setDescription(desc);
		programId.setLimitsFees(lf);
		programId.setDeliveryChannelList(delchanl);
		programId.setTransaction(transactn);
		programId.setPurseList(purse);
		
		
		String delchnflag="";
		
		delchnflag=req.getParameter("delchn");
		
		String selTxnflag="";
		
		selTxnflag=req.getParameter("seltxn");
		String selLimitFeesName=req.getParameter("selLimitFeesNameFlag") != null?req.getParameter("selLimitFeesNameFlag"):"";
		
		if(delchnflag!=null && delchnflag.equalsIgnoreCase("true"))
		{	
		List<Object> txnDtlsByChnlName=productService.getDeliveryChnlTxnsByChnlCode(delchanl);
		
		List<Object> txnDtlsByChnldata=(List<Object>) txnDtlsByChnlName.get(0);
		DeliveryChannel delChnl=(DeliveryChannel) txnDtlsByChnldata.get(0);
		Map<String,String> txnMap=delChnl.getTransactionMap();
		//Get purse list
		List<PurseDTO>  purseIdDropDown = partnerService.getProgramIDPartnerPurses(pid);
		
		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST,txnDtls.get(0));
		mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
		mav.addObject(TXNDETAILSBYCHNLNAME, txnDtlsByChnlName);
		mav.addObject(TXNMAP, txnMap);
		mav.addObject(CCLPConstants.PROGRAM_PARTNER_PURSE_LIST,purseIdDropDown);
		
		mav.addObject(PROGRAM_ID_FORM, programId);
		}
		
		if(selLimitFeesName!=null && selLimitFeesName.equalsIgnoreCase("true"))
		{	
				
		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST,txnDtls.get(0));
		mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
		
		if(delchnflag!=null && delchnflag.equalsIgnoreCase("true")){
		List<Object> txnDtlsByChnlName=productService.getDeliveryChnlTxnsByChnlCode(delchanl);
		List<Object> txnDtlsByChnldata=(List<Object>) txnDtlsByChnlName.get(0);
		DeliveryChannel delChnl=(DeliveryChannel) txnDtlsByChnldata.get(0);
		Map<String,String> txnMap=delChnl.getTransactionMap();
		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST,txnDtls.get(0));
		mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
		mav.addObject(TXNDETAILSBYCHNLNAME, txnDtlsByChnlName);
		mav.addObject(TXNMAP, txnMap);
		}
		
		//Get purse list
		List<PurseDTO>  purseIdDropDown = partnerService.getProgramIDPartnerPurses(pid);
		mav.addObject(CCLPConstants.PROGRAM_PARTNER_PURSE_LIST,purseIdDropDown);
		
		mav.addObject(PROGRAM_ID_FORM, programId);
		}
		
		if(selTxnflag!=null && selTxnflag.equalsIgnoreCase("true"))
		{
			List<Object> txnDtlsByChnlName=productService.getDeliveryChnlTxnsByChnlCode(delchanl);
			
			List<Object> txnDtlsByChnldata=(List<Object>) txnDtlsByChnlName.get(0);
			DeliveryChannel delChnl=(DeliveryChannel) txnDtlsByChnldata.get(0);
			Map<String,String> txnMap=delChnl.getTransactionMap();
			
			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST,txnDtls.get(0));
			List<Object> txnDtlsBasedonChnlName=productService.getDeliveryChnlTxnsByChnlCodeTxnName(delchanl,transactn);
			
			List deldata= (List) txnDtlsBasedonChnlName.get(0);
			DeliveryChannel delChnl2=(DeliveryChannel) deldata.get(0);
			Map<String,String> txnMap2=delChnl2.getTransactionMap();
			//Get purse list
			List<PurseDTO>  purseIdDropDown = partnerService.getProgramIDPartnerPurses(pid);
	
			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST,txnDtls.get(0));
			
			mav.addObject(PRG_ID,pid);
			mav.addObject(PRG_NAME,pname);
			mav.addObject(DELIVERY_CHNL_ID,delchanl);
			mav.addObject(TXNDETAILSBYCHNLNAME, txnDtlsByChnlName);
			mav.addObject(TXNMAP, txnMap);
			mav.addObject(TXNDTLSBASEDONCHNLNAME, txnDtlsBasedonChnlName.get(0));
			mav.addObject(TXNMAP2, txnMap2);
			mav.addObject(CCLPConstants.PROGRAM_PARTNER_PURSE_LIST,purseIdDropDown);
						
			mav.addObject(TXNFLAGFORCHNLNAME, txnDtlsBasedonChnlName.get(1));
			
			mav.addObject(PROGRAM_ID_FORM, programId);
			mav.addObject(DELCHNL2, delChnl2);
			
			String str1=txnMap2.keySet().toString().replace("[", "");
			
			str1=str1.replace("]","");
			
			mav.addObject(DEL_CHNL_KEY_VAL,delChnl2.getDeliveryChnlShortName()+"_"+str1);
			
			if(lf!=null && !lf.isEmpty() && lf.equalsIgnoreCase("Limits"))
			{
			mav.addObject("showLimits", "true");
			}
			else if(lf!=null && !lf.isEmpty() && lf.equalsIgnoreCase("Transaction Fees"))
			{
			mav.addObject("showTxnFee", "true");	
			}
			else if (lf != null && !lf.isEmpty() && lf.equalsIgnoreCase("Maintenance Fees")) {

				if (transactn != null && transactn.equalsIgnoreCase("weeklyfee")) {

					mav.addObject("showWeeklyFee", "true");
				} else if (transactn != null && transactn.equalsIgnoreCase("dormancyfee")) {

					mav.addObject("showDormancyFee", "true");
				}

				else if (transactn != null && transactn.equalsIgnoreCase("annualfee")) {

					mav.addObject("showAnnualFee", "true");
				}

				else if (transactn != null && transactn.equalsIgnoreCase("monthlyfee")) {

					mav.addObject("showMonthlyFee", "true");
				}
				mav.addObject("showMntFee", "true");
			}
			
			
		}
		mav.addObject(PROGRAM_ID_FORM, programId);	
		mav.setViewName(UPDATE_PROGRAM_ID);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

}
