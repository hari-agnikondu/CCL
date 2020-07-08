package com.incomm.cclpvms.config.validator;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.incomm.cclpvms.config.model.Product;

@Component
@PropertySource("classpath:ValidationMessages.properties")
public class ValidateProductEdit implements Validator {

	private static final String ERROR = "error";

	private static final String DENOM_PATTERN = "^\\d+(\\.\\d{1,3})?$";

	boolean errorflag = false;

	@Autowired
	Environment env;

	public boolean supports(Class<?> clazz) {

		return Product.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {

		Product product = (Product) target;
		String pattern = "";
		String patternAlpha = "^[A-Za-z0-9]+$";
		/**
		 * String patternProdName="^[A-Za-z0-9 ,.;'_-]+$"; String patternAlphawithspace=
		 * "^[A-Za-z0-9 ]+$" ;
		 */

		String productName = product.getProductName();
		String issuerName = product.getIssuerName();
		String partnerName = product.getPartnerName();
		String[] cardRanges = product.getCardRangesUpdate().toArray(new String[0]);
		String[] partnerCurrency = product.getPartnerCurrencyUpdate().toArray(new String[0]);
		String[] supportedPurse = product.getSupportedPurseUpdate().toArray(new String[0]);
		String[] packageId = product.getPackageUpdate().toArray(new String[0]);

		if (productName == "null" || productName.equals("")) {
			errors.rejectValue("productName", "messageNotEmpty.product.productName", ERROR);
			errorflag = true;

		}

		else if (productName.length() < 2 || productName.length() > 100) {
			errors.rejectValue("productName", "messageLength.product.productName", ERROR);
			errorflag = true;

		}

		if (partnerName.equalsIgnoreCase("-1")) {
			errors.rejectValue("partnerName", "messageNotEmpty.product.partnerName", ERROR);
			errorflag = true;

		}
		if (cardRanges == null || cardRanges.length == 0) {

			errors.rejectValue("cardRangesUpdate", "messageNotEmpty.product.cardRanges", ERROR);
			errorflag = true;

		}
		
		if (supportedPurse == null || supportedPurse.length == 0) {

			errors.rejectValue("supportedPurseUpdate", "messageNotEmpty.product.supportedPurse", ERROR);
			errorflag = true;

		}
		if (packageId == null || packageId.length == 0) {

			errors.rejectValue("packageUpdate", "messageNotEmpty.product.packageId", ERROR);
			errorflag = true;

		}

		if (issuerName.equalsIgnoreCase("NONE")) {
			errors.rejectValue("issuerName", "messageNotEmpty.product.issuerName", ERROR);
			errorflag = true;

		}

		Map<String, String> productMap = null;
		String formFactor = null;
		productMap = product.getProdAttributes();
		String internationalSupported= productMap.get("internationalSupported");
		if ("Enable".equalsIgnoreCase(internationalSupported)) {

			if (partnerName.equalsIgnoreCase("-1") && (partnerCurrency == null || partnerCurrency.length == 0)) {
				errors.rejectValue("partnerCurrencyUpdate", "messageNotEmpty.product.selectPartner", ERROR);
				errorflag = true;
			}
			if (partnerCurrency == null || partnerCurrency.length == 0) {
				errors.rejectValue("partnerCurrencyUpdate", "messageNotEmpty.product.partnerCurrency", ERROR);
				errorflag = true;
			}
		}
		for (String key : productMap.keySet()) {
			String value = productMap.get(key);

			formFactor = productMap.get("formFactor");
			pattern = "^[0-9]+$";

			if (key.equals("activeFrom")) {

				if ((value == null) || value.equals("")) {
					errors.rejectValue("prodAttributes['activeFrom']", "messageNotEmpty.product.activeFrom", ERROR);
					errorflag = true;

				}

			}

			else if ((key.equals("digitalInvAutoReplLvl") && "Digital".equalsIgnoreCase(formFactor))
					&& ((value == null) || value.equals(""))) {
				errors.rejectValue("prodAttributes['digitalInvAutoReplLvl']",
						"messageNotEmpty.product.digitalInvAutoReplLvl", ERROR);
				errorflag = true;

			} else if ((key.equals("initialDigitalInvQty") && "Digital".equalsIgnoreCase(formFactor))
					&& ((value == null) || value.equals(""))) {
				errors.rejectValue("prodAttributes['initialDigitalInvQty']",
						"messageNotEmpty.product.initialDigitalInvQty", ERROR);
				errorflag = true;

			} else if ((key.equals("digitalInvAutoReplQty") && "Digital".equalsIgnoreCase(formFactor))
					&& ((value == null) || value.equals(""))) {
				errors.rejectValue("prodAttributes['digitalInvAutoReplQty']",
						"messageNotEmpty.product.digitalInvAutoReplQty", ERROR);
				errorflag = true;

			}

			else if (key.equals("b2bInitSerialNumQty")) {

				if (!(value).isEmpty() && !(value).matches(pattern)) {
					errors.rejectValue("prodAttributes['b2bInitSerialNumQty']",
							"messageNotEmpty.product.b2bInitSerialNumQty", ERROR);
					errorflag = true;

				}
			} else if (key.equals("b2bSerialNumAutoReplenishLevel")) {

				if (!(value).isEmpty() && !(value).matches(pattern)) {
					errors.rejectValue("prodAttributes['b2bSerialNumAutoReplenishLevel']",
							"messageNotEmpty.product.b2bSerialNumAutoReplenishLevel", ERROR);
					errorflag = true;
				}
				/* Add Some more 'Else If' condition if required to check for Max/Min length */
			} else if (key.equals("b2bSerialNumAutoReplenishVal")) {

				if (!(value).isEmpty() && !(value).matches(pattern)) {
					errors.rejectValue("prodAttributes['b2bSerialNumAutoReplenishVal']",
							"messageNotEmpty.product.b2bSerialNumAutoReplenishVal", ERROR);
					errorflag = true;

				}
				/* Add Some more 'Else If' condition if required to check for Max/Min length */
			} else if (key.equals("dcmsId")) {

				if (value.length() > 0 && !(value).matches(patternAlpha)) {
					errors.rejectValue("prodAttributes['dcmsId']", "messageNotEmpty.product.dcmsId", ERROR);
					errorflag = true;

				}

			} else if (key.equals("retailUPC")) {

				if (value.length() > 0 && !(value).matches(patternAlpha)) {

					errors.rejectValue("prodAttributes['retailUPC']", "messageNotEmpty.product.retailUPC", ERROR);
					errorflag = true;

				}

			} else if (key.equals("b2bUpc")) {

				if (value.length() > 0 && !(value).matches(patternAlpha)) {
					errors.rejectValue("prodAttributes['b2bUpc']", "messageNotEmpty.product.b2bUpc", ERROR);
					errorflag = true;

				}

			} else if (key.equals("denominationType")) {

				if (value.equalsIgnoreCase("NONE") || (value == null) || value.equals("")) {
					errors.rejectValue("prodAttributes['denominationType']", "messageNotEmpty.product.denomination",
							ERROR);
					errorflag = true;

				}
			}

			else if (key.equals("denomFixed")) {

				if ((value != null && !value.equals("")) && !(value.matches("^\\d+(\\.\\d{1,10})?$"))) {
					errors.rejectValue("prodAttributes['denomFixed']", "messagepattern.product.denominationFixed",
							ERROR);
					errorflag = true;
				}

			} else if (key.equals("denomSelect")) {
				if (value != null) {
					String[] denomSelectValue = value.split(",");
					for (int i = 0; i < denomSelectValue.length; i++) {
						value = denomSelectValue[i];
						if (value != null && !(value.matches(DENOM_PATTERN))) {
							errors.rejectValue("prodAttributes['denomSelect']", "messagepattern.product.denomSelect",
									ERROR);
							errorflag = true;
						}

					}
				}
			} else if (key.equals("denomVarMax")) {

				if ((!value.isEmpty()) && !(value.matches(DENOM_PATTERN))) {
					errors.rejectValue("prodAttributes['denomVarMax']", "messagepattern.product.denomVarMax", ERROR);
					errorflag = true;
				}

			} else if (key.equals("denomVarMin")) {

				if ((!value.isEmpty()) && !(value.matches(DENOM_PATTERN))) {
					errors.rejectValue("prodAttributes['denomVarMin']", "messagepattern.product.denomVarMin", ERROR);
					errorflag = true;
				}

			}

			else if (key.equals("ccfFormatVersion")) {

				if (value.equalsIgnoreCase("NONE")) {
					errors.rejectValue("prodAttributes['ccfFormatVersion']", "messageNotEmpty.product.ccfFormatVersion",
							ERROR);
					errorflag = true;

				}
			} else if (key.equals("defaultPackage")) {

				if (value.equalsIgnoreCase("NONE") || (value == null) || value.equals("")) {
					errors.rejectValue("prodAttributes['defaultPackage']", "messageNotEmpty.product.defaultPackage",
							ERROR);
					errorflag = true;

				}
			} else if (key.equals("defaultPurse")) {

				if (value.equalsIgnoreCase("NONE") || (value == null) || value.equals("")) {
					errors.rejectValue("prodAttributes['defaultPurse']", "messageNotEmpty.product.defaultPurse", ERROR);
					errorflag = true;

				}
			} else if (key.equals("validityPeriod")) {

				if (value == "null" || value.equals("")) {
					errors.rejectValue("prodAttributes['validityPeriod']", "messageNotEmpty.product.validityPeriod",
							ERROR);
					errorflag = true;

				} else if (!(value.matches(pattern))) {
					errors.rejectValue("prodAttributes['validityPeriod']",
							"messageNotEmpty.product.NumericvalidityPeriod", ERROR);
					errorflag = true;
				}
			}

		}

		if (errorflag) {
			return;
		}
	}
}
