package com.incomm.cclpvms.config.validator;

import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

public class ValidateMaintenanceFee implements ConstraintValidator<MaintenanceFee, Map<String, Object>>{
	
	

	private static final Logger logger = LogManager.getLogger(ValidateMaintenanceFee.class);
	private boolean errorFlag = true;
    public void initialize(MaintenanceFee chk) {
		
        
	}
	public boolean isValid(Map<String, Object> value, ConstraintValidatorContext context) {

		value.entrySet().stream().filter(p -> p.getValue() == null).forEach(map -> map.setValue(""));

		value.entrySet().stream().forEach(p -> {

			if (p.getKey().contains("feeDesc")) {

				if (p.getValue().toString().length() > 100) {
					logger.debug(
							"Monthly Fee Description should contain minimum 1 characters and maximum 100 characters");
					context.buildConstraintViolationWithTemplate("{monthlyFeeCap_feeDesc.length}").addPropertyNode(null)
							.inIterable().atKey(p.getKey()).addConstraintViolation();
					errorFlag = false;

				}
			} else if (p.getKey().contains("clawbackCount")) {
				if (value.get(p.getKey().replace("clawbackCount", "clawback")).toString().equals("true")) {

					if (p.getValue().toString().equals("")) {
						logger.debug("clawback count cant be empty " + p);
						context.buildConstraintViolationWithTemplate("{product.clawbackCnt.empty}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;

					} else if (!isValidCount(p.getValue())) {
						logger.debug("Invalid ClawbackCount " + p);
						context.buildConstraintViolationWithTemplate("{product.clawbackCnt.invalid}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;

					}
				}

			} else if (p.getKey().contains("clawbackMaxAmt")) {
				if (value.get(p.getKey().replace("clawbackMaxAmt", "clawback")).toString().equals("true")) {
					if (p.getValue().equals("")) {
						logger.debug("clawback Max Amount should not be empty " + p);
						context.buildConstraintViolationWithTemplate("{product.clawbackMaxFeeAmt.invalid}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;

					} else if (!isValidAmount(p.getValue()) || p.getValue().toString().length() > 10) {
						logger.debug("monthly fee amont not valid " + p);
						context.buildConstraintViolationWithTemplate("{product.clawbackMaxFeeAmt.invalid}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;

					} else if (p.getValue().toString().contains(".")
							&& p.getValue().toString().split("\\.")[1].length() > 3) {
						logger.debug("monthly fee amound decimal digit should not exceed 3 " + p);
						context.buildConstraintViolationWithTemplate("{product.clawbackMaxFeeAmt.invalid}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;

					}
				}
			} else if (p.getKey().contains("firstMonthFeeAssessedDays")) {
				if (!p.getValue().toString().isEmpty()) {
					if (!isValidNumber(p.getValue())) {
						logger.debug("first Month fee assessed days is not a valid number");
						context.buildConstraintViolationWithTemplate("{maintenanceFee.firstFeeAccessDays.invalid}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;

					} else if (p.getValue().toString().length() > 3) {
						logger.debug("first month fee assessed days length cant exceed 3 digits");
						context.buildConstraintViolationWithTemplate("{maintenanceFee.firstFeeAccessDays.invalid}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;

					}
				}
			} else if (p.getKey().contains("feeAmt")) {
				if (p.getValue().toString().length() > 10) {
					logger.debug("monthly fee amont not valid " + p);
					context.buildConstraintViolationWithTemplate("{product.feeAmt.invalid}").addPropertyNode(null)
							.inIterable().atKey(p.getKey()).addConstraintViolation();
					errorFlag = false;

				} else if (p.getValue().toString().contains(".")
						&& p.getValue().toString().split("\\.")[1].length() > 3) {
					logger.debug("monthly fee amound decimal digit should not exceed 3 " + p);
					context.buildConstraintViolationWithTemplate("{product.feeAmt.invalid}").addPropertyNode(null)
							.inIterable().atKey(p.getKey()).addConstraintViolation();
					errorFlag = false;

				}
			} else if (p.getKey().contains("freeCount")) {

				if (!p.getValue().toString().equals("") && !isValidCount(p.getValue())) {
					logger.debug("Invalid freee count " + p);
					context.buildConstraintViolationWithTemplate("{maintenanceFee.freeCnt.invalid}")
							.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
					errorFlag = false;

				}
			} else if ((p.getKey().contains("maxCoun")) 
				&& (!p.getValue().toString().equals("") && !isValidCount(p.getValue()))) {
					logger.debug("Invalid maxcount " + p);
					context.buildConstraintViolationWithTemplate("{maintenanceFee.maxCnt.invalid}")
							.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
					errorFlag = false;

			}

		});

		return errorFlag;
	}
	
	static boolean isValidCount(Object count) {
		String regex = "^[0-9]+$";
		return (count.toString().matches(regex) && count.toString().length() < 7)? true: false; 
	}

	static boolean isValidAmount(Object amount) {

		String regex = "^[0-9.]+$";
		if (!amount.toString().matches(regex) || amount.toString().endsWith(".")
				|| StringUtils.countOccurrencesOf(amount.toString(), ".") > 1) {
			logger.debug(" Not a valid number " + amount);
		
			return false;
		}
		return true;

	}

	static boolean isValidNumber(Object number) {
		String regex = "^[0-9]+$";
		return (number.toString().matches(regex)) ? true : false;
	}

}
