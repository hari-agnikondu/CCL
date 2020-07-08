package com.incomm.cclpvms.common.controller;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {


	/**
	 * Handles the login from the Login page.
	 * 
	 */ 
	@RequestMapping("/clpLogin")
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName("clpLogin");

		return model;
	}
	

	@RequestMapping("/")
	public ModelAndView welcome(Map<String, Object> model) {
		ModelAndView model1 = new ModelAndView("home");
		 UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 if(userDetails.getAuthorities().isEmpty()) {
		    model1.addObject("error","User Not Authorized, Please check your system admin");
		    model1.setViewName("clpLogin");
		 return model1;
		 }
	return model1;
	}
	
	
}

