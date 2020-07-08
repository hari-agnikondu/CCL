package com.incomm.cclpvms.config.validator;


import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.incomm.cclpvms.config.model.Product;

/*
 * This service class performs validation checks for customized field level validation.
 */


@Component
@PropertySource("classpath:serverValidation.properties")
public class ValidateProductImpl implements Validator{
	
	private String patternsuffix="pattern";
	private String maxLengthsuffix = "max";
	boolean errorflag = false;
	
	@Autowired
	Environment env;

	public boolean supports(Class<?> clazz) 
	{
		
		return Product.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) 
	{
		String propName = "";
		String pattern = "";
		String maxLength = "";
		int maxLength1 = 0;
		
		
		Map<String,Object> generalMap = null;
		
		Product product = (Product)target;
		
		generalMap = product.getProductAttributes();

		for(String key:generalMap.keySet())
		{
			String value = (String) generalMap.get(key);
			
			propName = key + "." + patternsuffix;
			maxLength = key + "." + maxLengthsuffix;
			if(env.getProperty(propName)!=null)
				{
				pattern = env.getProperty(propName);
				}
			
			
			if(key.equals("defaultCardStatus"))
			{
			
				if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
					{
					errors.rejectValue("productAttributes['defaultCardStatus']", "messagepattern.general.defaultCardStatus", "Default Status should be Numeric");
					errorflag = true;
					}
			}
			if(key.equals("serviceCode"))
			{
				if( env.getProperty(maxLength)!=null)
				{
				maxLength1 = Integer.parseInt(env.getProperty(maxLength));
				}
				
				
				 if((value!=null && !value.trim().isEmpty())  && (value.length()>maxLength1))
					{
					errors.rejectValue("productAttributes['serviceCode']", "messageLength.general.serviceCode", "Service Code should be Max of 3 Characters");
					errorflag = true;
					}
				 else if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
					{
					errors.rejectValue("productAttributes['serviceCode']", "messagepattern.general.serviceCode", "Service Code should be Numeric");
					errorflag = true;
					}
			}
			
			else if(key.equals("roboHelpUrl"))
			{
				if( env.getProperty(maxLength)!=null)
				{
				maxLength1 = Integer.parseInt(env.getProperty(maxLength));
				}

				if((value!=null && !value.trim().isEmpty()) && value.length()>maxLength1)
					{
					errors.rejectValue("productAttributes['roboHelpUrl']", "messageLength.general.roboHelpUrl", "URL for ROBO help Should be Max of 500 Characters");
					errorflag = true;
					}
				else if(value!=null && !value.trim().isEmpty())
				{
					URL url;
					try 
					{
						url = new URL(value);
						url.toURI();
						
					} 
					catch (MalformedURLException e)
					{
						errors.rejectValue("productAttributes['roboHelpUrl']", "messagepattern.general.roboHelpUrl", "URL for ROBO help is Invalid");
						errorflag = true;
					} 
					catch (URISyntaxException e) 
					{
						errors.rejectValue("productAttributes['roboHelpUrl']", "messagepattern.general.roboHelpUrl", "URL for ROBO help is Invalid");
						errorflag = true;
					}
					 
				}
			}
			
			else if(key.equals("emailIdStatement"))
			{
				if( env.getProperty(maxLength)!=null)
				{
				maxLength1 = Integer.parseInt(env.getProperty(maxLength));
				}
				
				  Pattern validEmailAddressRegex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
				 
				 if((value!=null && !value.trim().isEmpty())  && (value.length()>maxLength1))
					{
					errors.rejectValue("productAttributes['emailIdStatement']", "messageLength.general.emailIdStatement", "Email ID for Statement Should be Max of 50 Characters");
					errorflag = true;
					}
				 else if((value!=null && !value.trim().isEmpty()) && !(validEmailAddressRegex.matcher(value).matches())  ) 
					{
					errors.rejectValue("productAttributes['emailIdStatement']", "messagepattern.general.emailIdStatement", "Email ID for Statement is Invalid");
					errorflag = true;
					}
			}
			
			else if(key.equals("statementFooter"))
			{
			
				if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
					{
					errors.rejectValue("productAttributes['statementFooter']", "messagepattern.general.statementFooter", "Statement Footer should be Alphabets");
					errorflag = true;
					}
			}
			
			else if(key.equals("customerCareNbr"))
			{
				if( env.getProperty(maxLength)!=null)
				{
				maxLength1 = Integer.parseInt(env.getProperty(maxLength));
				}
				
				if((value!=null && !value.trim().isEmpty())  && (value.length()>maxLength1))
					{
					errors.rejectValue("productAttributes['customerCareNbr']", "messageLength.general.customerCareNbr", "Customer Care Number should be Max of 20 Characters");
					errorflag = true;
					}
				else if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
					{
					errors.rejectValue("productAttributes['customerCareNbr']", "messagepattern.general.customerCareNbr", "Customer Care Number should be Numeric");
					errorflag = true;
					}	
			}
			
			else if(key.equals("cardRenewReplaceProd"))
			{
			
				if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
					{
					errors.rejectValue("productAttributes['cardRenewReplaceProd']", "messagepattern.general.cardRenewReplaceProd", "Card Renewal/Replacement Product Option should be Alphabets");
					errorflag = true;
					}
			}
			else if(key.equals("cardExpiryPendingDays"))
			{
				if( env.getProperty(maxLength)!=null)
				{
				maxLength1 = Integer.parseInt(env.getProperty(maxLength));
				}
				
				if((value!=null && !value.trim().isEmpty())  && (value.length()>maxLength1))
				{
				errors.rejectValue("productAttributes['cardExpiryPendingDays']", "messageLength.general.cardExpiryPendingDays", "Card Expiry Pending Period length should be Max of 3 Characters");
				errorflag = true;
				}
				
				else if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
					{
					errors.rejectValue("productAttributes['cardExpiryPendingDays']", "messagepattern.general.cardExpiryPendingDays", "Card Expiry Pending Period should be Numeric");
					errorflag = true;
					}
			}

			else if(key.equals("maxCardsPerCust"))
			{
				if( env.getProperty(maxLength)!=null)
					{
					maxLength1 = Integer.parseInt(env.getProperty(maxLength));
					}
				if((value!=null && !value.trim().isEmpty())  && (value.length()>maxLength1))
				{
				errors.rejectValue("productAttributes['maxCardsPerCust']", "messageLength.general.maxCardsPerCust", "Maximum number of Cards length should be Max of 2 characters");
				errorflag = true;
				}
				
				else if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
					{
					errors.rejectValue("productAttributes['maxCardsPerCust']", "messagepattern.general.maxCardsPerCust", "Maximum number of Cards should be Numeric");
					errorflag = true;
					}
				 
			}
			
			else if(key.equals("preAuthExpiryPeriod"))
			{
			
				if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
					{
					errors.rejectValue("productAttributes['preAuthExpiryPeriod']", "messagepattern.general.preAuthExpiryPeriod", "Pre Auth Expiry Period should be Numeric");
					errorflag = true;
					}
			}
			
			else if(key.equals("activationCode"))
			{
			
				if((value!=null && !value.trim().isEmpty()) && (!("true".equals(value))&& !("false".equals(value))))
					{
					errors.rejectValue("productAttributes['activationCode']", "messagepattern.general.activationCode", "Activation Code value should be 'YES' or 'NO'");
					errorflag = true;
					}
			}
			
			else if(key.equals("txnCountRecentStatement"))
			{
				if( env.getProperty(maxLength)!=null)
				{
				maxLength1 = Integer.parseInt(env.getProperty(maxLength));
				}
				
				if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
					{
					errors.rejectValue("productAttributes['txnCountRecentStatement']", "messagepattern.general.txnCountRecentStatement", "Txn Count for Recent Statement should be Numeric");
					errorflag = true;
					}
				else if((value!=null && !value.trim().isEmpty()) && ((Integer.parseInt(value) < 1) || (Integer.parseInt(value) > 10)))
					{
					errors.rejectValue("productAttributes['txnCountRecentStatement']", "messageValue.general.txnCountRecentStatement", "Txn Count for Recent Statement should be between 1 to 10");
					errorflag = true;
					}
			}
			
			else if("invalidPinAttempts".equals(key))
			{
				
				if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
					{
					errors.rejectValue("productAttributes['invalidPinAttempts']", "messagepattern.general.invalidPinAttempts", "Invalid PIN Attempts should be Numeric");
					errorflag = true;
					}
				else if((value!=null && !value.trim().isEmpty()) && (value.length()>2))
					{
					errors.rejectValue("productAttributes['invalidPinAttempts']", "messageValue.general.invalidPinAttempts", "Invalid PIN Attempts should be between 1 to 2 Characters");
					errorflag = true;
					}
			}
			
			else if("dormancyFeePeriod".equals(key)) {
				if ((value != null && !value.trim().isEmpty()) && !(value).matches(pattern)) {
					errors.rejectValue("productAttributes['dormancyFeePeriod']", "messagepattern.general.dormancyFeePeriod",
							"Dormancy Fee Period should be Numeric");
					errorflag = true;
				} else if ((value != null && !value.trim().isEmpty()) && (value.length() > 3)) {
					errors.rejectValue("productAttributes['dormancyFeePeriod']", "messageValue.general.dormancyFeePeriod",
							"Dormancy Fee Period should not be greater than 3 digits");
					errorflag = true;
				}
			}
			
			else if("maxCardBalance".equals(key)) {
				
				if((value==null) || value.trim().isEmpty())
				{
				errors.rejectValue("productAttributes['maxCardBalance']", "messageNotEmpty.general.maxCardBalance", "Please enter Maximum Card Balance");
				errorflag = true;
				}		
				
				else if( !(value.trim().isEmpty()) && !(value.matches("^[0-9]{1,11}(?:\\.[0-9]{1,3})?$"))) {
					errors.rejectValue("productAttributes['maxCardBalance']", "messagepattern.general.maxCardBalance","Maximum Card Balance should be Numeric");
					errorflag = true;
				}

			} else if ("chwAuthType".equals(key)) {
				if (!Objects.isNull(value) && value.equals("-1")) {
					errors.rejectValue("productAttributes['chwAuthType']",
							"messageUnselect.general.chwUserAuthentication", "Please select CHW User Authentication");
					errorflag = true;
				}

			} else if ("ivrAuthType".equals(key)) {
				if (!Objects.isNull(value) && value.equals("-1")) {
					errors.rejectValue("productAttributes['ivrAuthType']",
							"messageUnselect.general.ivrUserAuthentication", "Please select IVR User Authentication");
					errorflag = true;
				}

			} else if ("cvkA".equals(key)) {
				if ((value == null) || value.trim().isEmpty()) {
					errors.rejectValue("productAttributes['cvkA']", "messageNotEmpty.cvv.cvkA", "Please enter CVK_A");
					errorflag = true;
				} else if ((value != null) && !(value).matches(pattern)) {
					errors.rejectValue("productAttributes['cvkA']", "messagepattern.cvv.cvkA",
							"CVK_A should be AlphaNumeric");
					errorflag = true;
				}
			}
			else if("cvkB".equals(key))
			{
				if((value==null) || value.trim().isEmpty())
				{
				errors.rejectValue("productAttributes['cvkB']", "messageNotEmpty.cvv.cvkB", "Please enter CVK_B");
				errorflag = true;
				}
				else if((value!=null) && !(value).matches(pattern))
				{
				errors.rejectValue("productAttributes['cvkB']", "messagepattern.cvv.cvkB", "CVK_B should be AlphaNumeric");
				errorflag = true;
				}
			} 
			else if("cvkKeySpecifier".equals(key))
			{
				if((value==null) || value.trim().isEmpty())
				{
				errors.rejectValue("productAttributes['cvkKeySpecifier']", "messageNotEmpty.cvv.cvkKeySpecifier", "Please enter CVK Key Specifier");
				errorflag = true;
				}
				else if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
				{
				errors.rejectValue("productAttributes['cvkKeySpecifier']", "messagepattern.cvv.cvkKeySpecifier", "CVK Key Specifier should be AlphaNumeric");
				errorflag = true;
				}
			}
			
			
			else if("cvkIndex".equals(key))
			{
				if((value==null) || value.trim().isEmpty())
				{
				errors.rejectValue("productAttributes['cvkIndex']", "messageNotEmpty.cvv.cvkIndex", "Please enter CVK Index");
				errorflag = true;
				}
				else if((value!=null && !value.trim().isEmpty()) && !value.matches(pattern))
				{
				errors.rejectValue("productAttributes['cvkIndex']", "messagepattern.cvv.cvkIndex", "CVK Index should be AlphaNumeric");
				errorflag = true;
				}
			}
			else if("pinLength".equals(key)) {
			
				
				
				if( env.getProperty(maxLength)!=null)
				{
				maxLength1 = Integer.parseInt(env.getProperty(maxLength));
				}
				if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
				{
					errors.rejectValue("productAttributes['pinLength']", "messagepattern.pin.pinLength", "Pin Length should be Numeric");
				
				errorflag = true;
				}
				
				else if((value!=null && !value.trim().isEmpty())  && (value.length()>maxLength1))
					{
					errors.rejectValue("productAttributes['pinLength']", "messageLength.pin.pinLength", "Pin Length should be Maximum of 2 characters");
					errorflag = true;
					}
			}
			
	
			
			else if("pvk".equals(key)) {
				
				
				if( env.getProperty(maxLength)!=null)
				{
				maxLength1 = Integer.parseInt(env.getProperty(maxLength));
				}
				if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
				{
					errors.rejectValue("productAttributes['pvk']", "messagepattern.pin.pvk", "PVK should allow  Only Hexadecimal Values ie 0-9, a-f, A-F");
					
				
				errorflag = true;
				}
				
				else if((value!=null && !value.trim().isEmpty())  && (value.length()!=maxLength1)) {
					errors.rejectValue("productAttributes['pvk']", "messageLength.pin.pvk", "PVK should be  16 characters long");
					
					errorflag = true;
				}
			}
			
			else if("maximumPINLength".equals(key)) {
	
				if( env.getProperty(maxLength)!=null)
				{
				maxLength1 = Integer.parseInt(env.getProperty(maxLength));
				}
				if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
				{
				errors.rejectValue("productAttributes['maximumPINLength']", "messagepattern.pin.maximumPINLength", "Maximum PIN Length should be Numeric");
				errorflag = true;
				}
				
				else if((value!=null && !value.trim().isEmpty())  && (value.length()>maxLength1))
				{
					
				errors.rejectValue("productAttributes['maximumPINLength']", "messageLength.pin.maximumPINLength", "Maximum PIN Length should be Maximum of 2 characters");
				errorflag = true;
				}
			}
			
			else if("checkLength".equals(key)) {
				
				if( env.getProperty(maxLength)!=null)
				{
				maxLength1 = Integer.parseInt(env.getProperty(maxLength));
				}
				if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
				{
				errors.rejectValue("productAttributes['checkLength']", "messagepattern.pin.checkLength", "Check Length should be Numeric");
				errorflag = true;
				}
				
				else if((value!=null && !value.trim().isEmpty())  && (value.length()>maxLength1))
				{
					
				errors.rejectValue("productAttributes['checkLength']", "messageLength.pin.checkLength", "Check Length should be Maximum of 2 characters");
				errorflag = true;
				}
			}
else if("panOffset".equals(key)) {
				
				if( env.getProperty(maxLength)!=null)
				{
				maxLength1 = Integer.parseInt(env.getProperty(maxLength));
				}
				if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
				{
				errors.rejectValue("productAttributes['panOffset']", "messagepattern.pin.panOffset", "Pan Offset should be Numeric");
				errorflag = true;
				}
				
				else if((value!=null && !value.trim().isEmpty())  && (value.length()>maxLength1))
				{
					
				errors.rejectValue("productAttributes['panOffset']", "messageLength.pin.panOffset", "Pan Offset should be Maximum of 2 characters");
				errorflag = true;
				}
			}
			
else if("panVerifyLength".equals(key)) {
	
	if( env.getProperty(maxLength)!=null)
	{
	maxLength1 = Integer.parseInt(env.getProperty(maxLength));
	}
	if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
	{
	errors.rejectValue("productAttributes['panVerifyLength']", "messagepattern.pin.panVerifyLength", "PAN Verify Length should be Numeric");
	errorflag = true;
	}
	
	else if((value!=null && !value.trim().isEmpty())  && (value.length()>maxLength1))
	{
		
	errors.rejectValue("productAttributes['panVerifyLength']", "messageLength.pin.panVerifyLength", "PAN Verify Length should be Maximum of 2 characters");
	errorflag = true;
	}
}
			
else if("pinBlockFormat".equals(key)) {
	
	if( env.getProperty(maxLength)!=null)
	{
	maxLength1 = Integer.parseInt(env.getProperty(maxLength));
	}
	if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
	{
	errors.rejectValue("productAttributes['pinBlockFormat']", "messagepattern.pin.pinBlockFormat", "PIN Block Format should be Numeric");
	errorflag = true;
	}
	
	else if((value!=null && !value.trim().isEmpty())  && (value.length()>maxLength1))
	{
		
	errors.rejectValue("productAttributes['pinBlockFormat']", "messageLength.pin.pinBlockFormat", "PIN Block Format should be Maximum of 2 characters");
	errorflag = true;
	}
}
else if("decimalisationTable".equals(key)) {
	
	if( env.getProperty(maxLength)!=null)
	{
	maxLength1 = Integer.parseInt(env.getProperty(maxLength));
	}
	if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
	{
	errors.rejectValue("productAttributes['decimalisationTable']", "messagepattern.pin.decimalisationTable", "Decimalisation Table should allow  Only Hexadecimal Values ie 0-9, a-f, A-F");
	errorflag = true;
	}
	
	else if((value!=null && !value.trim().isEmpty())  && (value.length()!=maxLength1))
	{
		
	errors.rejectValue("productAttributes['decimalisationTable']", "messageLength.pin.decimalisationTable", "Decimalisation Table should be 16 characters");
	errorflag = true;
	}
}
			
else if("padCharacter".equals(key)) {
	
	if( env.getProperty(maxLength)!=null)
	{
	maxLength1 = Integer.parseInt(env.getProperty(maxLength));
	}
	if((value!=null && !value.trim().isEmpty()) && !(value).matches(pattern))
	{
	errors.rejectValue("productAttributes['padCharacter']", "messagepattern.pin.padCharacter", "PAD Character should allow  Only Hexadecimal Value ie 0-9, a-f, A-F");
	errorflag = true;
	}
	
	else if((value!=null && !value.trim().isEmpty())  && (value.length()>maxLength1))
	{
		
	errors.rejectValue("productAttributes['padCharacter']", "messageLength.pin.padCharacter", "PAD Character should be 1 characters");
	errorflag = true;
	}
}
			
			else if ("maxPurseBalance".equals(key)) {
				String maxPurseBalance = null;
				if (value != null && value.length() > 0) {
					maxPurseBalance = value;
					if (!maxPurseBalance.matches("^[0-9]{1,11}(?:\\.[0-9]{1,3})?$")) {
						errors.rejectValue("productAttributes['maxPurseBalance']",
								"messagepattern.purse.maxPurseBalance",
								"Maximum Purse Balance should be Numeric with 3 Decimals");
						errorflag = true;

					} else if (Double.parseDouble(maxPurseBalance) <= 0) {
						errors.rejectValue("productAttributes['maxPurseBalance']",
								"messagepattern.purse.maxPurseBalance.zero",
								"Maximum Purse Balance should be greater than zero");
						errorflag = true;
					}

				}
			}
			
		}
		
		if (errorflag)
		{
			return;
		}
		
		
	}

}
