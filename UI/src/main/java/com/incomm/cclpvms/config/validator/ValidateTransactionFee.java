package com.incomm.cclpvms.config.validator;

import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import com.incomm.cclpvms.constants.CCLPConstants;

public class ValidateTransactionFee implements ConstraintValidator<TransactionFee, Map<String, Object>>{

	private static final Logger logger = LogManager.getLogger(ValidateTransactionFee.class);
	private boolean errorFlag = true;
public void initialize(TransactionFee chk) {
		
        
	}
	public boolean isValid(Map<String, Object> value, ConstraintValidatorContext context) {

		value.entrySet().stream().filter(p ->p.getValue() == null)
		   .forEach(map -> map.setValue(""));
		value.entrySet().stream().forEach(p -> {

			if (p.getKey().contains("feeDesc")) {
				 if (p.getValue().toString().length() > 100) {
					logger.debug("Fee Description should contain maximum 100 characters "+p);
					context.buildConstraintViolationWithTemplate( "{product.feeDesc.length}" )
			        .addPropertyNode( null ).inIterable().atKey(p.getKey())
			        .addConstraintViolation();
					  errorFlag = false;
				}

			} else if (p.getKey().contains("clawbackCount")) {
				if (value.get(p.getKey().replace("clawbackCount", "clawback")).toString().equals("true")) {

					if (p.getValue().toString().equals("")) {
						logger.debug("clawback count cant be empty "+p);
						context.buildConstraintViolationWithTemplate( "{product.clawbackCnt.empty}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;
					} else if (!isValidCount(p.getValue())) {
						logger.debug("Invalid ClawbackCount "+p);
						context.buildConstraintViolationWithTemplate( "{product.clawbackCnt.invalid}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;
					}
				}
			} else if (p.getKey().contains("feeAmt")) {
				String prefix = p.getKey().substring(0,p.getKey().lastIndexOf('_'));
			
				if (p.getValue().equals("")) {
					if((value.get(prefix+"_clawbackCount")!=null && !value.get(prefix+"_clawbackCount").equals(""))||(value.get(prefix+"_freeCount")!=null && !value.get(prefix+"_freeCount").equals("")) || 
							(value.get(prefix+"_maxCount")!=null && !value.get(prefix+"_maxCount").equals("")) ||(value.get(prefix+"_feeCondition")!=null && !value.get(prefix+"_feeCondition").equals("N")) ) {
					logger.debug("fee amount should not be empty "+p);
					context.buildConstraintViolationWithTemplate( "{product.feeAmt.empty}" )
			        .addPropertyNode( null ).inIterable().atKey(p.getKey())
			        .addConstraintViolation();
					  errorFlag = false;
					}
				} else if(p.getValue()!=null && !p.getValue().equals("")) {
						
					if (!isValidAmount(p.getValue()) || p.getValue().toString().length() > 10) {
						logger.debug("fee amont not valid "+p);
						context.buildConstraintViolationWithTemplate( "{product.feeAmt.invalid}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;
					} else if (Double.valueOf((String) p.getValue()) == 0) {
						logger.debug("fee amount should not be zero "+p);
						context.buildConstraintViolationWithTemplate( "{product.feeAmt.zero}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;
					} else if (p.getValue().toString().contains(".")
							&& p.getValue().toString().split("\\.")[1].length() > 3) {
						logger.debug("fee amound decimal digit should not exceed 3 "+p);
						context.buildConstraintViolationWithTemplate( "{product.feeAmt.invalid}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;
					}
				}
			} else if (p.getKey().contains(CCLPConstants.FEE_PERCENT)) {

				if (value.get(p.getKey().replace(CCLPConstants.FEE_PERCENT, CCLPConstants.FEE_CONDITION)).toString().equalsIgnoreCase("A") || value
						.get(p.getKey().replace(CCLPConstants.FEE_PERCENT, CCLPConstants.FEE_CONDITION)).toString().equalsIgnoreCase("O")) {
					if (p.getValue().equals("")) {
						logger.debug("fee percent should not be empty "+p);
						context.buildConstraintViolationWithTemplate( "{product.feePerc.empty}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;
					} else if (!isValidAmount(p.getValue())) {
						logger.debug("fee percentage not a valid number "+p);
						context.buildConstraintViolationWithTemplate( "{product.feePerc.zero}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;
					} else if (p.getValue().toString().length() > 6) {
						logger.debug("Percent Fee Amount length Should Not Be greater than 6 digits "+p);
						context.buildConstraintViolationWithTemplate( "{product.feePerc.zero}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;
					} else if (Double.valueOf((String) p.getValue()) == 0) {
						logger.debug("Invalid Percent Fee Amount "+p);
						context.buildConstraintViolationWithTemplate( "{product.feePerc.zero}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;
					} else if (Double.valueOf((String) p.getValue()) > 100) {
						logger.debug("Percentage Fee should Not be greater than 100 "+p);
						context.buildConstraintViolationWithTemplate( "{product.feePerc.greater}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;
					}else if((value.get(p.getKey().replace(CCLPConstants.FEE_PERCENT, CCLPConstants.FEE_CONDITION)).toString().equalsIgnoreCase("A")) 
						&& (p.getValue().toString().contains(".") && p.getValue().toString().split("\\.")[1].length() > 3) ){
							logger.debug("fee percent decimal digit should not exceed 3 "+p);
							context.buildConstraintViolationWithTemplate( "{product.feePerc.zero}" )
					        .addPropertyNode( null ).inIterable().atKey(p.getKey())
					        .addConstraintViolation();
							  errorFlag = false;
					}

				}
				

			} else if (p.getKey().contains(CCLPConstants.MIN_FEE_AMT)) {
				if (value.get(p.getKey().replace(CCLPConstants.MIN_FEE_AMT, CCLPConstants.FEE_CONDITION)).toString().equals("A")) {
					if (p.getValue().equals("")) {
						logger.debug("Enter the Min Fee Amt "+p);
						context.buildConstraintViolationWithTemplate( "{product.minFeeAmt.empty}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;
					} else if (!isValidAmount(p.getValue()) || p.getValue().toString().length() > 10
							|| (p.getValue().toString().contains(".")
									&& p.getValue().toString().split("\\.")[1].length() > 3)) {
						logger.debug("min fee amt is not valid "+p);
						context.buildConstraintViolationWithTemplate( "{product.minFeeAmt.invalid}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;
					} else if (Double.valueOf((String) p.getValue()) < Double
							.valueOf((String) value.get(p.getKey().replace(CCLPConstants.MIN_FEE_AMT, "feeAmt")))) {
						logger.debug("Min Fee Amt Should be > than Fee Amt "+p);
						context.buildConstraintViolationWithTemplate( "{product.minFeeAmt.greater}" )
				        .addPropertyNode( null ).inIterable().atKey(p.getKey())
				        .addConstraintViolation();
						  errorFlag = false;

					}
				}
			} else if (p.getKey().endsWith("freeCount")) {
				if (!p.getValue().toString().equals("") && !isValidCount(p.getValue())) {
					logger.debug("Invalid freee count "+p);
					context.buildConstraintViolationWithTemplate( "{product.freeCnt.invalid}" )
			        .addPropertyNode( null ).inIterable().atKey(p.getKey())
			        .addConstraintViolation();
					  errorFlag = false;
				}
			} else if ((p.getKey().endsWith("maxCount")) 
				&& (!p.getValue().toString().equals("") && !isValidCount(p.getValue()))) {
					logger.debug("Invalid maxcount "+p);
					context.buildConstraintViolationWithTemplate( "{product.maxCnt.invalid}" )
			        .addPropertyNode( null ).inIterable().atKey(p.getKey())
			        .addConstraintViolation();
					  errorFlag = false;

			}

		});

	
		return errorFlag;
	}

	static boolean isValidCount(Object count) {
		String regex = "^[0-9]+$";
		return (count.toString().matches(regex) && count.toString().length() < 7) ? true : false;
	}

	static boolean isValidAmount(Object amount) {

		String regex = "^[0-9.]+$";
		if (!amount.toString().matches(regex) || amount.toString().endsWith(".")
				|| StringUtils.countOccurrencesOf(amount.toString(), ".") > 1) {
			logger.debug("Not a valid number " + amount);
			return false;
		}
		return true;

	}

}
