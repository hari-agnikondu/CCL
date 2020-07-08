package com.incomm.cclpvms.config.validator;

import org.springframework.validation.BindingResult;

import com.incomm.cclpvms.config.model.CardRange;


public interface ValidationMethods {
	
	public void chkCardRange(CardRange cardrange, BindingResult bindingResult);
}
