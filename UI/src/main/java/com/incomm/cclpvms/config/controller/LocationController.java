package com.incomm.cclpvms.config.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.Location;
import com.incomm.cclpvms.config.model.Merchant;
import com.incomm.cclpvms.config.model.MerchantDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.LocationService;
import com.incomm.cclpvms.config.service.MerchantService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.MerchantException;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.Util;

@Controller
@RequestMapping("/config/location")
public class LocationController {

	@Autowired
	MerchantService merchantService;
	
	@Autowired
	LocationService locationService;

	Logger logger = LogManager.getLogger(LocationController.class);

	private static final String LOCATION_CONFIG = "locationConfig";
	private static final String LOCATION_SEARCH = "locationSearch";
	private static final String LOCATION_LIST = "locationList";
	private static final String ADD_LOCATION = "addLocation";
	private static final String MERCHANT_LIST = "merchantList";
	private static final String EDIT_LOCATION = "editLocation";
	private static final String CONFIG_VIEW = "forward:/config/location/locationConfig";
	private static final String COUNTRY_MAP = "countryMap";
	private static final String COUNTRY_LIST ="countryList";
	private static final String STATE_LIST ="stateList";
	private static final String MERCHANTOBJ ="merchantId_obj";
	private static final String VIEW_LOCATION = "viewLocation";
	
	
	
	/**
	 * This method going to display Location search screen.
	 * @return
	 */
	
	@PreAuthorize("hasRole('SEARCH_LOCATION')")
	@RequestMapping("/locationConfig")
	public ModelAndView locationConfiguration() {

		logger.debug(CCLPConstants.ENTER);
		logger.debug(CCLPConstants.EXIT);
		return new ModelAndView(LOCATION_CONFIG, LOCATION_SEARCH, new Location());

	}
	/**
	 * 
	 *This method going to search the locations.
	 * @return
	 * @throws ServiceException
	 */
	@PreAuthorize("hasRole('SEARCH_LOCATION')")
	@RequestMapping("/searchLocation")
	public ModelAndView searchLocation( @Validated(Merchant.SearchMerchantScreen.class)
			@ModelAttribute(LOCATION_SEARCH) Location location)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		mav.setViewName(LOCATION_CONFIG);
		mav.addObject(LOCATION_LIST, locationService.getAllLocations(location));
		mav.addObject(LOCATION_SEARCH, location);
		mav.addObject("showGrid", "true");
		logger.debug(CCLPConstants.EXIT);
		return mav;

	}
	
	/**
	 * This Method is to display the Add Location.
	 */
	@PreAuthorize("hasRole('ADD_LOCATION')")
	@RequestMapping("/addLocation")
	public ModelAndView addLocationConfiguration( @ModelAttribute(ADD_LOCATION) Location location,HttpServletRequest req) throws Exception {
		logger.debug(CCLPConstants.ENTER);
	
		List<MerchantDTO> merchantList=merchantService.getAllMerchants();		
		ModelAndView mav=new ModelAndView(ADD_LOCATION);
		
		mav.addObject(MERCHANT_LIST, merchantList);
		mav.addObject(COUNTRY_MAP,locationService.getAllCountries());

		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
		
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	/**
	 * This method is used for save the location details.
	 * @param locationConfig
	 * @param bindingResult
	 * @param req
	 * @return
	 * @throws ServiceException
	 * @throws MerchantException
	 */
	@PreAuthorize("hasRole('ADD_LOCATION')")
	@RequestMapping(value = "/saveLocation") 
	public ModelAndView saveLocation(@Valid @ModelAttribute(ADD_LOCATION) Location locationConfig,BindingResult bindingResult,HttpServletRequest req) throws Exception{
		logger.debug(CCLPConstants.ENTER);
			
		ModelAndView mav=new ModelAndView(ADD_LOCATION);	
		mav.addObject(MERCHANT_LIST,merchantService.getAllMerchants());	
		
		
		if(bindingResult.hasErrors())
		{
			mav.addObject(MERCHANTOBJ,locationConfig.getMerchantId());
			
			mav.addObject(ADD_LOCATION,locationConfig);
			mav.addObject(COUNTRY_MAP,locationService.getAllCountries());
			mav.addObject("stateMap",locationService.listStates(locationConfig.getCountryCodeID()));
			mav.addObject(MERCHANT_LIST,merchantService.getAllMerchants());	
			mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
			logger.debug(CCLPConstants.EXIT);
			return mav;
		}		
		ResponseDTO responseDTO= locationService.addLocation(locationConfig);
		if(ResponseMessages.SUCCESS.equals(responseDTO.getCode()))			
		{
			mav.setViewName(CONFIG_VIEW);
			mav.addObject(CCLPConstants.STATUS,responseDTO.getMessage());
		}
		else
		{
			locationConfig.setMerchantId(locationConfig.getMerchantId());
			mav.addObject(MERCHANTOBJ,locationConfig.getMerchantId());
			mav.addObject(ADD_LOCATION,locationConfig);
			mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getMessage());			
			mav.addObject(MERCHANT_LIST,merchantService.getAllMerchants());
			mav.addObject(COUNTRY_MAP,locationService.getAllCountries());
			mav.addObject("stateMap",locationService.listStates(locationConfig.getCountryCodeID()));
			mav.addObject(MERCHANT_LIST,merchantService.getAllMerchants());	
			mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
			mav.setViewName(ADD_LOCATION);
						
		}
		logger.debug(CCLPConstants.EXIT);
		return mav; 
	}

	
	/**
	 * This method is to display the edit Location screen to the user.
	 */
	@PreAuthorize("hasRole('EDIT_LOCATION')")
	@RequestMapping("/editLocation")
	public ModelAndView editLocation(@ModelAttribute(LOCATION_SEARCH) Location location,HttpServletRequest req) throws Exception {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=null;
		Location locationServ=locationService.getLocationById(location.getLocationId());
		if(locationServ!=null)
		{
			mav=new ModelAndView(EDIT_LOCATION,EDIT_LOCATION,locationServ);
		
			mav.addObject(MERCHANTOBJ,locationServ.getMerchantId());
			mav.addObject(MERCHANT_LIST,merchantService.getAllMerchants());	
			mav.addObject(COUNTRY_LIST,locationService.getAllCountries());
			mav.addObject(STATE_LIST,locationService.listStates(locationServ.getCountryCodeID()));
			mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
		}
		else
		{
			mav=new ModelAndView(LOCATION_CONFIG,LOCATION_CONFIG, new Location());
			mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
			logger.debug(CCLPConstants.EXIT);
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

/**
 * This Methods Updates the Location details.
 */
@PreAuthorize("hasRole('EDIT_LOCATION')")
@RequestMapping(value = "/updateLocation") 
public ModelAndView updateLocation(@ModelAttribute("editLocation") Location location, BindingResult bindingResult,HttpServletRequest req) throws Exception {
	logger.debug(CCLPConstants.ENTER);
	ModelAndView mav=new ModelAndView();
	
	if(bindingResult.hasErrors())
	{
		mav.addObject(EDIT_LOCATION,location);
		mav.addObject(MERCHANT_LIST,merchantService.getAllMerchants());
		mav.addObject(COUNTRY_LIST,locationService.getAllCountries());
		mav.addObject(MERCHANTOBJ,location.getMerchantId());
		mav.addObject(STATE_LIST,locationService.listStates(location.getCountryCodeID()));
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
		mav.setViewName(EDIT_LOCATION);
	}

		ResponseEntity<ResponseDTO> responseDTO=locationService.updateLocation(location);
		if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){
			mav.setViewName(CONFIG_VIEW);
			mav.addObject(CCLPConstants.STATUS,responseDTO.getBody().getMessage()); 
		}
		else
		{
			mav.addObject(EDIT_LOCATION,location);
			mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getBody().getMessage());
			mav.addObject(MERCHANT_LIST,merchantService.getAllMerchants());
			mav.addObject(COUNTRY_LIST,locationService.getAllCountries());
			mav.addObject(MERCHANTOBJ,location.getMerchantId());
			mav.addObject(STATE_LIST,locationService.listStates(location.getCountryCodeID()));
			mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
			mav.setViewName(EDIT_LOCATION);
		}
		mav.addObject(MERCHANT_LIST,merchantService.getAllMerchants());
	
	logger.debug(CCLPConstants.EXIT);
	return mav; 
	
}
/**
 * This method is used to delete the location details.
 * @param location
 * @return
 * @throws ServiceException
 */
@PreAuthorize("hasRole('DELETE_LOCATION')")
@RequestMapping(value = "/deleteLocation") 
public ModelAndView deleteLocation(@ModelAttribute(LOCATION_SEARCH) Location location) throws ServiceException {
	logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();
		ResponseEntity<ResponseDTO> responseDTO=locationService.deleteLocations(location.getLocationId());
		if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){
			mav.setViewName(CONFIG_VIEW);
			mav.addObject(CCLPConstants.STATUS,responseDTO.getBody().getMessage());
		}
		else{
			mav.setViewName(CONFIG_VIEW);
			mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getBody().getMessage());
		}
	
	logger.debug(CCLPConstants.EXIT);
	return mav; 
	
}
/**
 * View Location Screen
 * @param exception
 * @return
 * @throws ServiceException 
 * @throws MerchantException 
 */

@PreAuthorize("hasRole('VIEW_LOCATION')")
@RequestMapping("/viewLocation")
public ModelAndView viewLocation(@ModelAttribute(LOCATION_SEARCH) Location location,HttpServletRequest req) throws ServiceException, MerchantException  {
	logger.debug(CCLPConstants.ENTER);
	ModelAndView mav=null;
	Location locationServ=locationService.getLocationById(location.getLocationId());
	if(locationServ!=null)
	{
		mav=new ModelAndView(VIEW_LOCATION,VIEW_LOCATION,locationServ);
	
		mav.addObject(MERCHANTOBJ,locationServ.getMerchantId());
		mav.addObject(MERCHANT_LIST,merchantService.getAllMerchants());	
		mav.addObject(COUNTRY_LIST,locationService.getAllCountries());
		mav.addObject(STATE_LIST,locationService.listStates(locationServ.getCountryCodeID()));
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
	}
	else
	{
		mav=new ModelAndView(LOCATION_CONFIG,LOCATION_CONFIG, new Location());
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
		logger.debug(CCLPConstants.EXIT);
	}
	logger.debug(CCLPConstants.EXIT);
	return mav;
}




@ExceptionHandler(Exception.class)
public ModelAndView exceptionHandler(Exception exception) {

	logger.info("exceptionHandler Method Starts Here");
	String errMessage = ResponseMessages.SERVER_DOWN;
	ModelAndView mav = new ModelAndView();
	if (exception instanceof ServiceException)
		errMessage = exception.getMessage();

	mav.setViewName("serviceError");
	mav.addObject(CCLPConstants.STATUS_MESSAGE, errMessage);

	logger.info("exceptionHandler Method Ends Here");
	return mav;
}

}

