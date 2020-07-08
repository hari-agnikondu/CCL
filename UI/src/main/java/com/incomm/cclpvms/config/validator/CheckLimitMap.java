package com.incomm.cclpvms.config.validator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.incomm.cclpvms.config.service.ProductService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.exception.ServiceException;

public class CheckLimitMap implements ConstraintValidator<LimitMap, Map<String, Object>> {
	@Autowired
	ProductService productService;

	private static final Logger logger = LogManager.getLogger(CheckLimitMap.class);
	private boolean errorFlag = true;
	Map<String, String> txnFinancialFlagMap = new HashMap<>();
	
	public void initialize(LimitMap chk) {
		
        
	}

	public boolean isValid(Map<String, Object> value, ConstraintValidatorContext context) {

		txnFinancialFlagMap = financialTxnMap();
		try {
			value.entrySet().stream().filter(p -> p.getKey().contains(CCLPConstants.MIN_AMT_PER_TX)
					|| p.getKey().contains(CCLPConstants.MAX_AMT_PER_TX)).forEach(x -> {

						if (!isFinancialTxn(x.getKey(), txnFinancialFlagMap)) {
							logger.debug("This is not a financial transaction, skip validation for {}", x.getKey());
						}

						/* minimum amount per transaction should not be empty */
						else if (x.getKey().contains(CCLPConstants.MIN_AMT_PER_TX) && x.getValue() != null
								&& !x.getValue().equals("")) {
							if (!isValidnumber(x.getValue())) {
								logger.debug("Entered value is not a valid amount, {} = {}", x.getKey(), x.getValue());
								context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null)
										.inIterable().atKey(x.getKey()).addConstraintViolation();
								errorFlag = false;
							} else if (x.getValue().toString().contains(".")
									&& x.getValue().toString().split("\\.")[1].length() > 3) {
								logger.debug("Decimal digits should not exceed 3 for {} = {}", x.getKey(),
										x.getValue());
								context.buildConstraintViolationWithTemplate("{product.decimal.validation}")
										.addPropertyNode(null).inIterable().atKey(x.getKey()).addConstraintViolation();
								errorFlag = false;
							} else if (x.getValue().toString().length() > 10) {
								logger.debug("Amount length should not exceed 10 digits, {} = {}", x.getKey(),
										x.getValue());
								context.buildConstraintViolationWithTemplate("{product.minAmtPerTxn.invalid}")
										.addPropertyNode(null).inIterable().atKey(x.getKey()).addConstraintViolation();
								errorFlag = false;
							} else if (Double.valueOf((String) x.getValue()) < 0) {
								logger.debug("Amount should not be negative, {} = {}", x.getKey(), x.getValue());
								context.buildConstraintViolationWithTemplate("{product.minAmtPerTxn.invalid}")
										.addPropertyNode(null).inIterable().atKey(x.getKey()).addConstraintViolation();
								errorFlag = false;
							}
						} else if (x.getKey().contains(CCLPConstants.MAX_AMT_PER_TX) && x.getValue() != null
								&& !x.getValue().equals("")) {
							/* maximum amount per transaction should not be empty or negative */
							if (!isValidnumber(x.getValue())) {
								logger.debug("Entered value is not a valid amount, {} = {}", x.getKey(), x.getValue());
								context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null)
										.inIterable().atKey(x.getKey()).addConstraintViolation();
								errorFlag = false;
							} else if (x.getValue().toString().contains(".")
									&& x.getValue().toString().split("\\.")[1].length() > 3) {
								logger.debug("Decimal digits should not exceed 3 for {} = {}", x.getKey(),
										x.getValue());
								context.buildConstraintViolationWithTemplate("{product.decimal.validation}")
										.addPropertyNode(null).inIterable().atKey(x.getKey()).addConstraintViolation();
								errorFlag = false;
							} else if (x.getValue().toString().length() > 10) {
								logger.debug("Amount length should not exceed 10 digits, {} = {}", x.getKey(),
										x.getValue());
								context.buildConstraintViolationWithTemplate("{product.maxAmtPerTxn.invalid}")
										.addPropertyNode(null).inIterable().atKey(x.getKey()).addConstraintViolation();
								errorFlag = false;
							} else if (Double.valueOf((String) x.getValue()) <= 0) {
								logger.debug("Amount should not be negative or zero, {} = {}", x.getKey(),
										x.getValue());
								context.buildConstraintViolationWithTemplate("{product.maxAmtPerTxn.invalid}")
										.addPropertyNode(null).inIterable().atKey(x.getKey()).addConstraintViolation();
								errorFlag = false;
							}
							/* Maximum Amount should be greater than or equal to Minimum Amount */
							else if (!value.get(x.getKey().replace("max", "min")).equals("")
									&& isValidAmount(value.get(x.getKey().replace("max", "min")))
									&& Double.valueOf((String) x.getValue()) < Double
											.valueOf((String) value.get(x.getKey().replace("max", "min")))) {
								logger.debug(
										"Maximum amount should be greater than or equals to minimum amount, {} = {}",
										x.getKey(), x.getValue());
								context.buildConstraintViolationWithTemplate("{product.maxAmtPerTxn.lesser}")
										.addPropertyNode(null).inIterable().atKey(x.getKey()).addConstraintViolation();
								errorFlag = false;
							}
						}
					});

			value.entrySet().stream().filter(p -> !p.getKey().endsWith("_ovrd")).forEach(p -> {

				if (!isFinancialTxn(p.getKey(), txnFinancialFlagMap)) {
					logger.debug("This is not a financial transaction, skip validation for {}", p.getKey());
				} else if (!(p.getKey().contains(CCLPConstants.MAX_AMT_PER_TX)
						|| p.getKey().contains(CCLPConstants.MIN_AMT_PER_TX)) && !p.getValue().equals("")
						&& !isValidnumber(p.getValue())) {
					logger.debug("Entered value is not a valid amount, {}", p);
					context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
							.atKey(p.getKey()).addConstraintViolation();
					errorFlag = false;
				}
				/* Daily transaction validation */
				else if (p.getKey().contains(CCLPConstants.DAILY_MAX_AMT) && !p.getValue().equals("")) {
					if (!isValidnumber(p.getValue())) {
						logger.debug("Entered value is not valid amount" + p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (p.getValue().toString().contains(".")
							&& p.getValue().toString().split("\\.")[1].length() > 3) {
						logger.debug("Decimal digits should not exceed 3 digits for {}", p);
						context.buildConstraintViolationWithTemplate("{product.decimal.validation}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (p.getValue().toString().length() > 10) {
						logger.debug("Amount should not exceed 10 digits {}", p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (!value.get(p.getKey().replace(CCLPConstants.DAILY_MAX_AMT, CCLPConstants.MAX_AMT_PER_TX))
							.equals("")
							&& isValidAmount(value
									.get(p.getKey().replace(CCLPConstants.DAILY_MAX_AMT, CCLPConstants.MAX_AMT_PER_TX)))
							&& Double.valueOf((String) p.getValue()) < Double.valueOf((String) value.get(
									p.getKey().replace(CCLPConstants.DAILY_MAX_AMT, CCLPConstants.MAX_AMT_PER_TX)))) {
						logger.debug(
								"Daily Amount should be greater than or equal to Maximum Amount per transaction " + p);
						context.buildConstraintViolationWithTemplate("{product.dailyMaxAmt.lesser.maxAmtPerTxn}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					}
					/* Weekly transaction validation */
				} else if (p.getKey().contains(CCLPConstants.WEEKLY_MAX_AMT) && !p.getValue().equals("")) {
					if (!isValidnumber(p.getValue())) {
						logger.debug("Entered value is not valid amount, " + p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (p.getValue().toString().contains(".")
							&& p.getValue().toString().split("\\.")[1].length() > 3) {
						logger.debug("Decimal digits should not exceed 3 digits for {}", p);
						context.buildConstraintViolationWithTemplate("{product.decimal.validation}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (p.getValue().toString().length() > 10) {
						logger.debug("Amount length should not exceed 10 digits {}", p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (!value.get(p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT, CCLPConstants.DAILY_MAX_AMT))
							.equals("")) {
						if (isValidAmount(value
								.get(p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT, CCLPConstants.DAILY_MAX_AMT)))
								&& Double.valueOf((String) p.getValue()) < Double.valueOf((String) value.get(p.getKey()
										.replace(CCLPConstants.WEEKLY_MAX_AMT, CCLPConstants.DAILY_MAX_AMT)))) {
							logger.debug(
									"Weekly transaction amount should be greater than or equal to Daily transaction amount "
											+ p);
							context.buildConstraintViolationWithTemplate("{product.weeklyMaxAmt.lesser.dailyMaxAmt}")
									.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
							errorFlag = false;
						}

					} else if ((!value
							.get(p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT, CCLPConstants.MAX_AMT_PER_TX))
							.equals(""))
							&& (isValidAmount(value.get(
									p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT, CCLPConstants.MAX_AMT_PER_TX)))
									&& Double.valueOf((String) p.getValue()) < Double
											.valueOf((String) value.get(p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT,
													CCLPConstants.MAX_AMT_PER_TX))))) {
						logger.debug(
								"Weekly transaction amount should be greater than or equal to maximum amount per transaction "
										+ p);
						context.buildConstraintViolationWithTemplate("{product.weeklyMaxAmt.lesser.maxAmtPerTxn}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					}
				}

				/* Monthly transaction validation */
				else if (p.getKey().contains(CCLPConstants.MONTHLY_MAX_AMT) && !p.getValue().equals("")) {
					if (!isValidnumber(p.getValue())) {
						logger.debug("Entered value is not a valid amount, " + p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (p.getValue().toString().contains(".")
							&& p.getValue().toString().split("\\.")[1].length() > 3) {
						logger.debug("Decimal digits should not exceed 3 for {}", p);
						context.buildConstraintViolationWithTemplate("{product.decimal.validation}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (p.getValue().toString().length() > 10) {
						logger.debug("Amount length should not exceed 10 digits, {}", p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (!value
							.get(p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT, CCLPConstants.WEEKLY_MAX_AMT))
							.equals("")) {
						if (isValidAmount(value
								.get(p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT, CCLPConstants.WEEKLY_MAX_AMT)))
								&& Double.valueOf((String) p.getValue()) < Double.valueOf((String) value.get(p.getKey()
										.replace(CCLPConstants.MONTHLY_MAX_AMT, CCLPConstants.WEEKLY_MAX_AMT)))) {
							logger.debug(
									"Monthly transaction amount should be greater than or equal to Weekly transaction amount "
											+ p);
							context.buildConstraintViolationWithTemplate("{product.monthlyMaxAmt.lesser.weeklyMaxAmt}")
									.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
							errorFlag = false;
						}

					} else if (!value
							.get(p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT, CCLPConstants.DAILY_MAX_AMT))
							.equals("")) {
						if (isValidAmount(value
								.get(p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT, CCLPConstants.DAILY_MAX_AMT)))
								&& Double.valueOf((String) p.getValue()) < Double.valueOf((String) value.get(p.getKey()
										.replace(CCLPConstants.MONTHLY_MAX_AMT, CCLPConstants.DAILY_MAX_AMT)))) {
							logger.debug(
									"Monthly transaction amount should be greater than or equal to Daily transaction amount "
											+ p);
							context.buildConstraintViolationWithTemplate("{product.monthlyMaxAmt.lesser.dailyMaxAmt}")
									.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
							errorFlag = false;
						}

					} else if ((!value
							.get(p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT, CCLPConstants.MAX_AMT_PER_TX))
							.equals(""))
							&& (isValidAmount(value.get(
									p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT, CCLPConstants.MAX_AMT_PER_TX)))
									&& Double.valueOf((String) p.getValue()) < Double.valueOf(
											(String) value.get(p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT,
													CCLPConstants.MAX_AMT_PER_TX))))) {
						logger.debug(
								"Monthly transaction amount should be greater than or equal to maximum amount per transaction "
										+ p);
						context.buildConstraintViolationWithTemplate("{product.monthlyMaxAmt.lesser.maxAmtPerTxn}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;

					}
				}

				/* Yearly transaction validation */
				else if (p.getKey().contains(CCLPConstants.YEARLY_MAX_AMT) && !p.getValue().equals("")) {
					if (!isValidnumber(p.getValue())) {
						logger.debug("Entered value is not a valid amount, " + p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (p.getValue().toString().contains(".")
							&& p.getValue().toString().split("\\.")[1].length() > 3) {
						logger.debug("Decimal digits should not exceed 3 for {}", p);
						context.buildConstraintViolationWithTemplate("{product.decimal.validation}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (p.getValue().toString().length() > 10) {
						logger.debug("Amount length should not exceed 10 digits, {}", p);
						context.buildConstraintViolationWithTemplate("product.NaN").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (!value
							.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.MONTHLY_MAX_AMT))
							.equals("")) {
						if (isValidAmount(value
								.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.MONTHLY_MAX_AMT)))
								&& Double.valueOf((String) p.getValue()) < Double.valueOf((String) value.get(p.getKey()
										.replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.MONTHLY_MAX_AMT)))) {
							logger.debug(
									"Yearly transaction amount should be greater than or equal to monthly transaction amount "
											+ p);
							context.buildConstraintViolationWithTemplate("{product.yearlyMaxAmt.lesser.monthlyMaxAmt}")
									.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
							errorFlag = false;
						}

					} else if (!value
							.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.WEEKLY_MAX_AMT))
							.equals("")) {
						if (isValidAmount(value
								.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.WEEKLY_MAX_AMT)))
								&& Double.valueOf((String) p.getValue()) < Double.valueOf((String) value.get(p.getKey()
										.replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.WEEKLY_MAX_AMT)))) {
							logger.debug(
									"Yearly transaction amount should be greater than or equal to wekkly transaction amount "
											+ p);
							context.buildConstraintViolationWithTemplate("{product.yearlyMaxAmt.lesser.weeklyMaxAmt}")
									.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
							errorFlag = false;
						}

					} else if (!value.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.DAILY_MAX_AMT))
							.equals("")) {
						if (isValidAmount(value
								.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.DAILY_MAX_AMT)))
								&& Double.valueOf((String) p.getValue()) < Double.valueOf((String) value.get(p.getKey()
										.replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.DAILY_MAX_AMT)))) {
							logger.debug(
									"Yearly transaction amount should be greater than or equal to daily transaction amount "
											+ p);
							context.buildConstraintViolationWithTemplate("{product.yearlyMaxAmt.lesser.dailyMaxAmt}")
									.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
							errorFlag = false;
						}

					} else if ((!value
							.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.MAX_AMT_PER_TX))
							.equals(""))
							&& (isValidAmount(value.get(
									p.getKey().replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.MAX_AMT_PER_TX)))
									&& Double.valueOf((String) p.getValue()) < Double
											.valueOf((String) value.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_AMT,
													CCLPConstants.MAX_AMT_PER_TX))))) {
						logger.debug(
								"Yearly transaction amount should be greater than or equal to maximum amount per transaction "
										+ p);
						context.buildConstraintViolationWithTemplate("{product.yearlyMaxAmt.lesser.maxAmtPerTxn}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					}
				}
				/* Daily transaction Count validation */
				else if (p.getKey().contains(CCLPConstants.DAILY_MAX_COUNT) && !p.getValue().equals("")) {
					if (p.getValue().toString().contains(".")) {
						logger.debug("Count value can't be a fraction, " + p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (p.getValue().toString().length() > 4) {
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;

					}

				}
				/* Weekly transaction Count validation */
				else if (p.getKey().contains(CCLPConstants.WEEKLY_MAX_COUNT) && !p.getValue().equals("")) {
					if (p.getValue().toString().contains(".")) {
						logger.debug("Count value can't be a fraction, " + p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (p.getValue().toString().length() > 4) {
						logger.debug("Count value can't exceed 4 digits, " + p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;

					} else if (!value
							.get(p.getKey().replace(CCLPConstants.WEEKLY_MAX_COUNT, CCLPConstants.DAILY_MAX_COUNT))
							.equals("")
							&& isValidCount(value.get(
									p.getKey().replace(CCLPConstants.WEEKLY_MAX_COUNT, CCLPConstants.DAILY_MAX_COUNT)))
							&& Integer.valueOf((String) p.getValue()) < Integer.valueOf((String) value.get(p.getKey()
									.replace(CCLPConstants.WEEKLY_MAX_COUNT, CCLPConstants.DAILY_MAX_COUNT)))) {
						logger.debug("Weekly Count should be greater than or equal to Daily Count " + p);
						context.buildConstraintViolationWithTemplate("{product.weeklyMaxCnt.lesser.dailyMaxCnt}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					}
					/* Monthly transaction Count validation */
				} else if (p.getKey().contains(CCLPConstants.MONTHLY_MAX_COUNT) && !p.getValue().equals("")) {
					if (p.getValue().toString().contains(".")) {
						logger.debug("Count value can't be a fraction" + p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (p.getValue().toString().length() > 4) {
						logger.debug("Count value can't exceed 4 digits" + p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;

					} else if (!value
							.get(p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT, CCLPConstants.WEEKLY_MAX_COUNT))
							.equals("")) {
						if (isValidCount(value.get(
								p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT, CCLPConstants.WEEKLY_MAX_COUNT)))
								&& Integer.valueOf((String) p.getValue()) < Integer
										.valueOf((String) value.get(p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT,
												CCLPConstants.WEEKLY_MAX_COUNT)))) {
							logger.debug("Monthly Count should be greater than or equal to weekly Count " + p);
							context.buildConstraintViolationWithTemplate(
									"{product.monthlyMaxCnt.lesser.weeklylyMaxCnt}").addPropertyNode(null).inIterable()
									.atKey(p.getKey()).addConstraintViolation();
							errorFlag = false;
						}

					} else if ((!value
							.get(p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT, CCLPConstants.DAILY_MAX_COUNT))
							.equals(""))
							&& (isValidCount(value.get(
									p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT, CCLPConstants.DAILY_MAX_COUNT)))
									&& Integer.valueOf((String) p.getValue()) < Integer.valueOf(
											(String) value.get(p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT,
													CCLPConstants.DAILY_MAX_COUNT))))) {
						logger.debug("Monthly Count should be greater than or equal to daily Count " + p);
						context.buildConstraintViolationWithTemplate("{product.monthlyMaxCnt.lesser.dailylyMaxCnt}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					}
				}
				/* Yearly transaction Count validation */
				else if (p.getKey().contains(CCLPConstants.YEARLY_MAX_COUNT) && !p.getValue().equals("")) {
					if (p.getValue().toString().contains(".")) {
						logger.debug("Count value can't be a fraction" + p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					} else if (p.getValue().toString().length() > 4) {
						logger.debug("Count value can't exceed 4 digits" + p);
						context.buildConstraintViolationWithTemplate("{product.NaN}").addPropertyNode(null).inIterable()
								.atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;

					} else if (!value
							.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_COUNT, CCLPConstants.MONTHLY_MAX_COUNT))
							.equals("")) {
						if (isValidCount(value.get(
								p.getKey().replace(CCLPConstants.YEARLY_MAX_COUNT, CCLPConstants.MONTHLY_MAX_COUNT)))
								&& Integer.valueOf((String) p.getValue()) < Integer
										.valueOf((String) value.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_COUNT,
												CCLPConstants.MONTHLY_MAX_COUNT)))) {
							logger.debug("Yearly Count should be greater than or equal to monthly Count " + p);
							context.buildConstraintViolationWithTemplate("{product.yearlyMaxCnt.lesser.monthlyMaxCnt}")
									.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
							errorFlag = false;
						}

					} else if (!value
							.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_COUNT, CCLPConstants.WEEKLY_MAX_COUNT))
							.equals("")) {
						if (isValidCount(value.get(
								p.getKey().replace(CCLPConstants.YEARLY_MAX_COUNT, CCLPConstants.WEEKLY_MAX_COUNT)))
								&& Integer.valueOf((String) p.getValue()) < Integer
										.valueOf((String) value.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_COUNT,
												CCLPConstants.WEEKLY_MAX_COUNT)))) {
							logger.debug("Yearly Count should be greater than or equal to weekly Count " + p);
							context.buildConstraintViolationWithTemplate("{product.yearlyMaxCnt.lesser.weeklylyMaxCnt}")
									.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
							errorFlag = false;
						}

					} else if ((!value
							.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_COUNT, CCLPConstants.DAILY_MAX_COUNT))
							.equals(""))
							&& (isValidCount(value.get(
									p.getKey().replace(CCLPConstants.YEARLY_MAX_COUNT, CCLPConstants.DAILY_MAX_COUNT)))
									&& Integer.valueOf((String) p.getValue()) < Integer.valueOf(
											(String) value.get(p.getKey().replace(CCLPConstants.YEARLY_MAX_COUNT,
													CCLPConstants.DAILY_MAX_COUNT))))) {
						logger.debug("Yearly Count should be greater than or equal to daily Count " + p);
						context.buildConstraintViolationWithTemplate("{product.yearlyMaxCnt.lesser.dailylyMaxCnt}")
								.addPropertyNode(null).inIterable().atKey(p.getKey()).addConstraintViolation();
						errorFlag = false;
					}
				}

			});
		} catch (Exception e) {
			logger.error("Error Occured : ", e.getMessage());
		}
		return errorFlag;
	}

	static boolean isValidAmount(Object amount) {

		String regex = "^[0-9.]+$";
		return (!amount.toString().matches(regex) || amount.toString().endsWith(".")
				|| (StringUtils.countOccurrencesOf(amount.toString(), ".") > 1) || (amount.toString().length() > 10)
				|| (amount.toString().contains(".") && amount.toString().split("\\.")[1].length() > 3)) ? false : true;
	}

	static boolean isValidnumber(Object amount) {

		String regex = "^[0-9.]+$";
		return (!amount.toString().matches(regex) || amount.toString().endsWith(".")
				|| StringUtils.countOccurrencesOf(amount.toString(), ".") > 1) ? false : true;
	
	}

	static boolean isValidCount(Object count) {
		String regex = "^[0-9]+$";
		return (count.toString().matches(regex) && count.toString().length() < 5) ? true: false;
	
	}

	boolean isFinancialTxn(Object txnKey, Map<String, String> txnFinancialFlags) {

		String txnName = txnKey.toString().split("\\_")[1];

		try {
			if (!txnKey.toString().contains("Amt") || txnFinancialFlags.get(txnName).equals("Y")) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Error Occured while checking financial txn : ",e.getMessage());
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> financialTxnMap() {
		List<Object> txnDtls = new ArrayList<>();
		try {
			txnDtls = productService.getDeliveryChnlTxns();
		} catch (ServiceException e) {

			logger.debug(e.getMessage());
		}
		return (Map<String, String>) txnDtls.get(1);
	}

}
