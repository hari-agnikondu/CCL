package com.incomm.cclpvms.config.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.incomm.cclpvms.config.model.CardRange;

/*
 * This service class performs validation checks for customized field level validation.
 */


@Service
public class ValidationMethodsImpl implements ValidationMethods{
	
public void chkCardRange(CardRange cardrange, BindingResult bindingResult) {
		
		Long startcardrange = null;
		Long endcardrange = null;
		int cardLength = 0;
		String checkdigit = "";
		int cardLengthconstitute = 0;
		
		if(cardrange.getCardLength()!=null && 0!=cardrange.getCardLength())
		{
			cardLength=cardrange.getCardLength();
		}
		
		if(cardrange.getIsCheckDigitRequired()!=null && cardrange.getIsCheckDigitRequired()!="")
		{
			checkdigit=cardrange.getIsCheckDigitRequired();
		}
		
		if(checkdigit != null && checkdigit.equals("Y"))
		   {
		   cardLengthconstitute = (cardrange.getPrefix().length())+(cardrange.getStartCardNbr().length())+1;
		   }
	   else
		   {
		   cardLengthconstitute = (cardrange.getPrefix().length())+(cardrange.getStartCardNbr().length());
		   }
		
		if((cardLength!=0)&& ((cardLength<12)||(cardLength>21)))
		   {
			   bindingResult.rejectValue("cardLength", "messageLength.Cardrange.cardFixedLength", "error");
		   }
		else if((cardLength!=0)&&(cardLength>cardLengthconstitute || cardLength<cardLengthconstitute))
		   {
			   bindingResult.rejectValue("cardLength", "messageLength.Cardrange.CardLengthnotMatch", "error");
		   }

		if(cardrange.getStartCardNbr()!=null&&cardrange.getStartCardNbr()!="")
		{
		startcardrange = Long.parseLong(cardrange.getStartCardNbr());
		}
		
		if(cardrange.getEndCardNbr()!=null&&cardrange.getEndCardNbr()!="")
		{
		endcardrange = Long.parseLong(cardrange.getEndCardNbr());
		}
		
		if(cardrange.getStartCardNbr().length()!= cardrange.getEndCardNbr().length())
			{
				bindingResult.rejectValue("startCardNbr", "messageLength.Cardrange.StartEndCardRange", "error");
				bindingResult.rejectValue("endCardNbr", "messageLength.Cardrange.StartEndCardRange", "error");
			}
		else if(startcardrange!=null && endcardrange !=null && endcardrange<=startcardrange)
			{
				bindingResult.rejectValue("endCardNbr", "messageLength.Cardrange.StartRangeGreater", "error");
			}
	   
		
	}


}
