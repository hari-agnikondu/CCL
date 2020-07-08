package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.PreAuthVerificationConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.RuleConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RuleEngineService;
import com.incomm.cclp.service.RuleSetCacheService;
import com.incomm.cclp.transaction.bean.Rule;
import com.incomm.cclp.transaction.bean.RuleResult;
import com.incomm.cclp.transaction.bean.RuleSet;
import com.incomm.cclp.transaction.bean.RuleSetResult;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.service.impl.LocalCacheServiceImpl;
import com.incomm.cclp.util.Util;

@Service
public class RuleEngineServiceImpl implements RuleEngineService {

	@Autowired
	RuleSetCacheService ruleSetCacheService;

	@Autowired
	ProductDAO productDao;

	@Autowired
	LocalCacheServiceImpl localCacheService;

	private static final Logger logger = LogManager.getLogger(RuleEngineServiceImpl.class);

	@Override
	public void callTransactionFilter(ValueDTO valueDto) throws ServiceException {
		logger.info(GeneralConstants.ENTER);
		Map<String, String> valObject = valueDto.getValueObj();
		List<RuleResult> lst = null;
		String ruleproductId = valObject.get(ValueObjectKeys.PRODUCT_ID);

		String ruleSetId = String.valueOf(productDao.getProductRuleSet(ruleproductId));

		if (Util.isEmpty(ruleSetId) || ruleSetId.equalsIgnoreCase("0")) {
			logger.info("Rule not enabled for this product");
			return;
		}

		RuleSet rs = loadRuleSet(ruleSetId);
		String declineMsg = "";
		String declineRsn = "";
		String transactionIdentifier = productDao.getTransactionIdentifier(
				String.valueOf(valObject.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE)),
				String.valueOf(valObject.get(ValueObjectKeys.TRANS_CODE)), String.valueOf(valObject.get(ValueObjectKeys.MSGTYPE)));

		valObject.put(ValueObjectKeys.TRANSACTION_IDENTIFIER, transactionIdentifier);
		if (rs != null && transactionIdentifier != null && !transactionIdentifier.equals("0")) {
			long startTime = System.currentTimeMillis();
			RuleSetResult result = executeRules(rs.getRules(), valObject);
			lst = result.getRules();
			for (RuleResult set : lst) {
				logger.info(set.getRuleId() + ":" + set.getRuleName() + " --> " + set.isRuleResults());
				declineMsg = RuleConstants.DECLINED_BY_TRANSACTION_FILTER + " " + set.getRuleName() + "-" + set.getRuleId();
				declineRsn = set.getRuleEvaluateRslt();
			}
			logger.info("RuleSet Result   :" + result.isRuleSetResults());
			logger.info("RuleSet Response :" + result.getRuleSetResponse());
			long endTime = System.currentTimeMillis();
			logger.info("Time taken callTransactionFilter :" + (endTime - startTime) + " ms");
			if (!result.isRuleSetResults())
				throw new ServiceException(declineMsg, declineRsn, ResponseCodes.DECLINED_BY_TRANSACTION_FILTER);
		} else
			logger.info("TransactionFilter skipped");
		logger.info(GeneralConstants.EXIT);
	}

	private RuleSet loadRuleSet(String ruleSetId) throws ServiceException {

		return ruleSetCacheService.getRuleSet(ruleSetId);

	}

	public RuleSetResult executeRules(List<Rule> cln, Map<String, String> obj) {

		Rule rule = null;
		RuleSetResult result = new RuleSetResult();
		RuleResult ruleResult = null;
		boolean rslt = true;
		String nullParam = "";
		List<RuleResult> ruleList = new ArrayList<>();
		int i = 0;
		int collectionSize = cln.size();
		String transactionType = "";

		String transactionIdentifier = String.valueOf(obj.get(ValueObjectKeys.TRANSACTION_IDENTIFIER));
		transactionIdentifier = (transactionIdentifier == null ? "0" : transactionIdentifier);
		boolean skipcheck = false;
		boolean[] array = new boolean[collectionSize];

		if (collectionSize > 0) {
			Collections.sort((List<Rule>) cln);
			Collections.reverse((List<Rule>) cln);
		}
		boolean conditionalCase = false;
		Iterator<Rule> itr = cln.iterator();
		while (itr.hasNext()) {
			skipcheck = false;
			rule = itr.next();
			ruleResult = new RuleResult();
			transactionType = rule.getRuleTransactionType();
			logger.debug("Rule :" + rule.getRuleId() + " - " + rule.getRuleName() + " TranType :" + transactionType + " Action Type :"
					+ rule.getRuleActionType());
			if (transactionIdentifier.equals("0") || (("," + transactionType + ",").indexOf("," + transactionIdentifier + ",") == -1)) {
				skipcheck = true;
				array[i] = true;
			} else {
				array[i] = evaluateRule(rule.getRuleExpression(), obj, rule.getRuleDetails(), rule.getRuleEvaluateRslt());
				nullParam = ((HashMap<String, String>) obj).get(ValueObjectKeys.NULL_PARAM);
			}

			ruleResult.setRuleId(rule.getRuleId());
			ruleResult.setRuleName(rule.getRuleName());
			ruleResult.setRuleResults(array[i]);
			if (rule.getRuleEvaluateRslt() != null && !rule.getRuleEvaluateRslt()
				.isEmpty()) {
				final StringBuilder tempSb = new StringBuilder();
				tempSb.append("RuleID:")
					.append(rule.getRuleId())
					.append("-")
					.append(rule.getRuleName())
					.append(":")
					.append(System.getProperty(ValueObjectKeys.LINE_SEPARATOR));
				if ((String) ((HashMap<String, String>) obj).get(ValueObjectKeys.GLOBAL_RULE_ID) != null
						&& ("Y".equals((String) ((HashMap<String, String>) obj).get(ValueObjectKeys.GLOBAL_RULE_FLAG)))) {
					String globRulId = ((HashMap<String, String>) obj).get(ValueObjectKeys.GLOBAL_RULE_ID);
					String globRulName = ((HashMap<String, String>) obj).get(ValueObjectKeys.GLOBAL_RULE_NAME);
					tempSb.append("MerchantRuleID:")
						.append(globRulId)
						.append("-")
						.append(globRulName)
						.append(":")
						.append(System.getProperty(ValueObjectKeys.LINE_SEPARATOR));
				}
				for (String key : rule.getRuleEvaluateRslt()
					.keySet()) {
					String ruleNames = rule.getRuleDetails()
						.get(key);
					String ruleRslt = rule.getRuleEvaluateRslt()
						.get(key);
					String[] ruleName = ruleNames.split(" ");

					logger.debug("rule.getRuleDetails() :" + ruleNames);
					logger.debug("rule.getRuleEvaluateRslt() :" + ruleRslt);
					logger.debug("rule.getRuleActionType() :" + rule.getRuleActionType());
					if (ruleName != null) {

						logger.debug("ruleRslt :" + ruleRslt);
						if (((RuleConstants.DECLINE_IF_TRUE.equalsIgnoreCase(rule.getRuleActionType()))
								|| (RuleConstants.CONDITIONAL_IF_TRUE.equalsIgnoreCase(rule.getRuleActionType())))
								&& "true".equalsIgnoreCase(ruleRslt)) {
							try {
								if (RuleConstants.BLACKLIST_PAN.equals(ruleName[0])) {
									tempSb.append(RuleConstants.BLACKLIST_PAN)
										.append(";")
										.append(System.getProperty(ValueObjectKeys.LINE_SEPARATOR));
								} else {
									tempSb.append(rule.getRuleDetails()
										.get(key))
										.append(";")
										.append(System.getProperty(ValueObjectKeys.LINE_SEPARATOR));
								}

							} catch (Exception ex) {
								logger.error("Error while getting Block List " + ex.getMessage(), ex);
							}
						} else if (((RuleConstants.DECLINE_IF_FALSE.equalsIgnoreCase(rule.getRuleActionType()))
								|| (RuleConstants.CONDITIONAL_IF_FALSE.equalsIgnoreCase(rule.getRuleActionType())))
								&& "false".equalsIgnoreCase(ruleRslt)) {
							try {
								if (RuleConstants.BLACKLIST_PAN.equals(ruleName[0])) {
									tempSb.append(RuleConstants.BLACKLIST_PAN)
										.append(";")
										.append(System.getProperty(ValueObjectKeys.LINE_SEPARATOR));
								} else {
									tempSb.append(rule.getRuleDetails()
										.get(key))
										.append(";")
										.append(System.getProperty(ValueObjectKeys.LINE_SEPARATOR));
								}

							} catch (Exception ex) {
								logger.error("Error occured in RuleEngineServiceImpl: " + ex.getMessage(), ex);
							}
						}
					}
				}
				ruleResult.setRuleEvaluateRslt(tempSb.toString());
			}
			if (nullParam.equals("")) {
				if (!skipcheck && ((RuleConstants.CONDITIONAL_IF_TRUE.equals(rule.getRuleActionType()) && array[i])
						|| (RuleConstants.CONDITIONAL_IF_FALSE.equals(rule.getRuleActionType()) && !array[i]))) {
					conditionalCase = true;
				}
				if (!skipcheck && ((array[i] && rule.getRuleActionType()
					.equals(RuleConstants.DECLINE_IF_TRUE)) || (!array[i]
							&& rule.getRuleActionType()
								.equals(RuleConstants.DECLINE_IF_FALSE)))) {
					result.setRuleSetResponse("89");
					rslt = false;
					ruleList.add(ruleResult);
					break;
				}
			} else {
				logger.debug("Decline check skipped - NULL_PARAM :" + nullParam);
			}
			ruleList.add(ruleResult);

			i++;

		}

		if (result.getRuleSetResponse() == null && rslt) {
			result.setRuleSetResponse("00");
			obj.put(ValueObjectKeys.TOKEN_RULE_RESPONSE, PreAuthVerificationConstants.RULE_RESPONSE_SUCCESS);
			if (conditionalCase) {
				obj.put(ValueObjectKeys.TOKEN_RULE_RESPONSE, PreAuthVerificationConstants.RULE_RESPONSE_CONDITIONAL);
			}
		} else {
			result.setRuleSetResponse("89");
			obj.put(ValueObjectKeys.TOKEN_RULE_RESPONSE, PreAuthVerificationConstants.RULE_RESPONSE_FAILURE);
		}

		result.setRules(ruleList);
		result.setRuleSetResults(rslt);

		return result;

	}

	/**
	 * @param ruleExpression
	 * @param obj
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private boolean evaluateRule(String ruleExpression, Object obj, Map<String, String> hm, Map<String, String> map) {

		String token = null;
		boolean bool1;
		boolean bool2;
		Stack<String> operands = new Stack<>();
		Stack<String> operators = new Stack<>();
		Stack<Boolean> booleans = new Stack<>();
		int innerCnt = 0;
		StringTokenizer tokens = new StringTokenizer(ruleExpression, " ");

		while (tokens.hasMoreTokens()) {
			token = tokens.nextToken();
			if ("AND".equals(token)) {
				operators.push("&&");
				innerCnt++;
			} else if ("OR".equals(token)) {
				operators.push("||");
				innerCnt++;
			} else if (token.equals("(")) {
				innerCnt = 0;
			} else if (token.equals(")")) {
				if (!CollectionUtils.isEmpty(operators))
					stackOperation1(operands, operators, booleans, obj, innerCnt, map, token);
				innerCnt = 0;
			} else {
				Stack<String> operands1 = new Stack<>();
				Stack<String> operators1 = new Stack<>();
				logger.debug("Filter :" + hm.get(token));
				String[] tarr = getOper(hm.get(token));
				operands1.push(tarr[0]);
				operands1.push(tarr[2]);
				operators1.push(tarr[1]);
				stackOperation1(operands1, operators1, booleans, obj, 0, map, token);
			}

		}

		if (booleans.size() != 1) {
			String op = "";
			int opsize = operators.size();
			for (int k = 0; k < opsize; k++) {
				bool2 = booleans.pop();
				bool1 = booleans.pop();
				op = operators.pop();
				logger.debug(bool1 + " " + op + " " + bool2 + " - " + ("&&".equals(op) ? (bool1 && bool2) : (bool1 || bool2)));
				if ("&&".equals(op))
					booleans.push((bool1 && bool2));
				else
					booleans.push((bool1 || bool2));
			}

		}

		return booleans.pop();

	}

	private void stackOperation1(Stack<String> operands, Stack<String> operators, Stack<Boolean> booleans, Object obj, int cnt,
			Map<String, String> map, String token) {
		String operand1 = null;
		String operand2 = null;
		String oper = null;

		boolean bool1;
		boolean bool2;
		boolean firstloop = true;

		oper = operators.pop();

		if (Arrays.asList(RuleConstants.EXCLUDE_LOGICAL_OPR)
			.contains(oper)) {
			operand2 = operands.pop();
			operand1 = operands.pop();
			boolean tempFlag = evaluateExpression(operand1, operand2, oper, obj);
			booleans.push(tempFlag);
			logger.debug(ValueObjectKeys.EVAL_EXP + (String) ((HashMap<?, ?>) obj).get(ValueObjectKeys.EVAL_EXP));
			map.put(token, String.valueOf(tempFlag));

		} else if ("&&".equals(oper) || "||".equals(oper)) {
			while (cnt >= 0) {
				if (firstloop)
					firstloop = false;
				else
					oper = operators.pop();
				cnt--;
				if (cnt == 0)
					cnt = -1;
				bool2 = booleans.pop();
				bool1 = booleans.pop();
				logger.debug(bool1 + " " + oper + " " + bool2 + " " + ("&&".equals(oper) ? (bool1 && bool2) : (bool1 || bool2)));
				if ("&&".equals(oper))
					booleans.push((bool1 && bool2));
				else
					booleans.push((bool1 || bool2));
			}
		} else
			throw new IllegalArgumentException("Illegal operation (Unknown Character)");

	}

	public String[] getOper(String filter) {
		String filter1 = (filter == null ? "" : filter);
		StringBuilder sbfilter = new StringBuilder(filter1);
		String[] farr = filter1.split(" ");
		String operand1 = "";
		String operand2 = "";
		String operator = "";
		if (farr.length == 3) {
			operand1 = farr[0];
			operator = farr[1];
			operand2 = farr[2];
		} else if (farr.length > 3) {
			int s1 = sbfilter.indexOf(" ");
			int s11 = s1 + 1;
			int s2 = sbfilter.indexOf(" ", s11);
			int s22 = s2 + 1;
			operand1 = sbfilter.substring(0, s1);
			operand2 = sbfilter.substring(s22, sbfilter.toString()
				.length());
			operator = sbfilter.substring(s11, s2);
		} else if (farr.length == 2) {
			operand1 = farr[0];
			operator = farr[1];
		}
		return new String[] { operand1, operator, operand2 };
	}

	/**
	 * @param oper1
	 * @param oper2
	 * @param bool
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private boolean evaluateExpression(String oper1, String oper2, String bool, Object obj) {
		boolean result = true;
		String leftTerm = null;
		String rightTerm = null;
		String operator = null;
		Map<String, String> ruleParameters = null;
		Map<String, String> ruleParametersMapping = null;

		ruleParameters = localCacheService.getRuleParameters(ruleParameters);
		String ruleParamType = ruleParameters.get(oper1);

		ruleParamType = (ruleParamType == null ? "2" : ruleParamType); // 2-String
		String delchnl = (String) ((HashMap<?, ?>) obj).get(ValueObjectKeys.DELIVERYCHNL);
		String trancde = (String) ((HashMap<?, ?>) obj).get(ValueObjectKeys.TRANS_CODE);

		ruleParametersMapping = localCacheService.getRuleParametersMapping(ruleParametersMapping);
		String key = ruleParametersMapping.get(oper1 + delchnl + trancde);
		key = (key == null ? ruleParametersMapping.get(oper1 + delchnl + "NA") : key);
		leftTerm = (String) ((HashMap<?, ?>) obj).get(key);
		rightTerm = oper2;
		operator = bool;
		((HashMap<String, String>) obj).put(ValueObjectKeys.EVAL_EXP, "");

		if ((String) ((HashMap<?, ?>) obj).get(ValueObjectKeys.GLOBAL_RULE_FLAG) != null
				&& ("Y".equals((String) ((HashMap<?, ?>) obj).get(ValueObjectKeys.GLOBAL_RULE_FLAG)))
				&& RuleConstants.MERCHANT_ID_NAME_KEY.equalsIgnoreCase(key)) {
			leftTerm = (String) ((HashMap<?, ?>) obj).get(ValueObjectKeys.CARD_ACCEPT_ID_CODE_DE42);
			rightTerm = (String) ((HashMap<?, ?>) obj).get(ValueObjectKeys.MERCHANT_NAME);
		}

		if (leftTerm == null && !(RuleConstants.RULE_PARAM_TYPE_05.equals(ruleParamType))) {
			((HashMap<String, String>) obj).put(ValueObjectKeys.NULL_PARAM, oper1);
			return result;

		} else {
			((HashMap<String, String>) obj).put(ValueObjectKeys.NULL_PARAM, "");
		}

		if (ruleParamType.equals("1")) // 1-Numeric
		{
			return evaluateNumericExpression(leftTerm, operator, rightTerm, obj, key);
		} else if (ruleParamType.equals("2")) // 2-String

		{
			return evaluateStringExpression(leftTerm, operator, rightTerm, obj, key);
		} else if (ruleParamType.equals("3") || ruleParamType.equals("4")) // 3-Date 4-Time
		{
			return evaluateDateExpression(oper1, ruleParamType, leftTerm, operator, rightTerm, obj, key);
		} else if (RuleConstants.RULE_PARAM_TYPE_05.equals(ruleParamType)) {
			return evaluateExpression(leftTerm, operator, rightTerm, obj, key);
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	private boolean evaluateExpression(final String leftTerm, final String operator, final String rightTerm, final Object mapobj,
			final String key) {

		boolean result = false;

		((HashMap<String, String>) mapobj).put(ValueObjectKeys.EVAL_EXP,
				"(" + key + ") " + leftTerm + " " + operator + " " + rightTerm + " = " + result);
		return result;

	}

	@SuppressWarnings("unchecked")
	private boolean evaluateNumericExpression(String leftTerm, String operator, String rightTerm, Object obj, String mapobj) {
		boolean result = false;
		if (operator.equals(RuleConstants.EQUALS_TO)) {
			try {
				result = leftTerm != null && !"".equals(leftTerm) && rightTerm != null && !"".equals(rightTerm)
						&& (new Double(leftTerm)).doubleValue() == (new Double(rightTerm)).doubleValue();
			} catch (NumberFormatException ex1) {
				throw new NumberFormatException("Numeric type is expected: '" + leftTerm + "','" + rightTerm + "'");
			}
		} else if (operator.equals(RuleConstants.NOT_EQUALS_TO)) {
			try {
				result = leftTerm != null && !"".equals(leftTerm) && rightTerm != null && !"".equals(rightTerm)
						&& (new Double(leftTerm)).doubleValue() != (new Double(rightTerm)).doubleValue();
			} catch (NumberFormatException ex1) {
				throw new NumberFormatException("Numeric type expecting : '" + leftTerm + "','" + rightTerm + "'");
			}
		} else if (operator.equals(RuleConstants.LESS_THAN)) {
			try {
				result = leftTerm != null && !"".equals(leftTerm) && rightTerm != null && !"".equals(rightTerm)
						&& (new Double(leftTerm)).doubleValue() < (new Double(rightTerm)).doubleValue();
			} catch (NumberFormatException ex1) {
				throw new NumberFormatException("Numeric type is expecting: '" + leftTerm + "','" + rightTerm + "'");
			}
		} else if (operator.equals(RuleConstants.LESS_OR_EQUALS_TO)) {
			try {
				result = leftTerm != null && !"".equals(leftTerm) && rightTerm != null && !"".equals(rightTerm)
						&& (new Double(leftTerm)).doubleValue() <= (new Double(rightTerm)).doubleValue();
			} catch (NumberFormatException ex2) {
				throw new NumberFormatException("Numeric type expected here: '" + leftTerm + "','" + rightTerm + "'");
			}
		} else if (operator.equals(RuleConstants.GREATER_THAN)) {
			try {
				result = leftTerm != null && !"".equals(leftTerm) && rightTerm != null && !"".equals(rightTerm)
						&& (new Double(leftTerm)).doubleValue() > (new Double(rightTerm)).doubleValue();
			} catch (NumberFormatException ex3) {
				throw new NumberFormatException("Numeric type is expecting here: '" + leftTerm + "','" + leftTerm + "'");
			}
		} else if (operator.equals(RuleConstants.GREATER_OR_EQUALS_TO)) {
			try {
				result = leftTerm != null && !"".equals(leftTerm) && rightTerm != null && !"".equals(rightTerm)
						&& (new Double(leftTerm)).doubleValue() >= (new Double(rightTerm)).doubleValue();
			} catch (NumberFormatException ex4) {
				throw new NumberFormatException("Numeric type expected here: '" + leftTerm + "','" + rightTerm + "'");
			}
		}
		((HashMap<String, Object>) obj).put(ValueObjectKeys.EVAL_EXP,
				"(" + mapobj + ") " + leftTerm + " " + operator + " " + rightTerm + " = " + result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private boolean evaluateStringExpression(String leftTerm, String operator, String rightTerm, Object obj, String mapobj) {
		boolean result = false;
		if (operator.equals(RuleConstants.EQUALS_TO)) {

			result = leftTerm == null && rightTerm == null || leftTerm != null && rightTerm != null && leftTerm.equalsIgnoreCase(rightTerm);
		} else if (operator.equals(RuleConstants.NOT_EQUALS_TO)) {
			result = leftTerm == null && rightTerm != null || leftTerm != null && rightTerm == null
					|| leftTerm != null && rightTerm != null && !leftTerm.equalsIgnoreCase(rightTerm);
		} else if (operator.equals(RuleConstants.CONTAINS)) {
			result = leftTerm == null && rightTerm == null || leftTerm != null && rightTerm != null && leftTerm.toUpperCase()
				.indexOf(rightTerm.toUpperCase()) != -1;
		} else if (operator.equals(RuleConstants.NOT_CONTAINS)) {
			result = leftTerm == null && rightTerm == null || leftTerm != null && rightTerm != null && leftTerm.toUpperCase()
				.indexOf(rightTerm.toUpperCase()) == -1;
		} else if (operator.equals(RuleConstants.START_WITH)) {
			result = leftTerm == null && rightTerm == null || leftTerm != null && rightTerm != null && leftTerm.toUpperCase()
				.startsWith(rightTerm.toUpperCase());
		} else if (operator.equals(RuleConstants.END_WITH)) {
			result = leftTerm == null && rightTerm == null || leftTerm != null && rightTerm != null && leftTerm.toUpperCase()
				.endsWith(rightTerm.toUpperCase());
		} else if (operator.equals(RuleConstants.PATTERN_MATCHES)) {

			result = leftTerm == null && rightTerm == null || leftTerm != null && rightTerm != null && Pattern.matches(rightTerm, leftTerm);
		} else if (operator.equals(RuleConstants.PATTERN_MATCH_FIND)) {

			result = leftTerm == null && rightTerm == null || leftTerm != null && rightTerm != null && Pattern.compile(rightTerm)
				.matcher(leftTerm)
				.find();

		} else if (operator.equals(RuleConstants.IS_NOT_NULL)) {

			result = leftTerm != null && !leftTerm.equals("");
		} else if (operator.equals(RuleConstants.IS_NULL)) {

			result = leftTerm == null || leftTerm.equals("");
		}

		((HashMap<String, Object>) obj).put(ValueObjectKeys.EVAL_EXP,
				"(" + mapobj + ") " + leftTerm + " " + operator + " " + rightTerm + " = " + result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private boolean evaluateDateExpression(String param, String paramType, String leftTerm, String operator, String rightTerm, Object obj,
			String mapobj) {
		boolean result = false;
		int year = 0;
		int month = 0;
		int day = 0;
		int hh = 0;
		int mm = 0;
		int ss = 0;
		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		String[] rightTermArr = (rightTerm == null ? "" : rightTerm).split(",");

		try {
			if (paramType.equals("3")) { // Added to avoid date check fails in random case
				calendar.set(Calendar.HOUR_OF_DAY, 00);
				calendar.set(Calendar.MINUTE, 00);
				calendar.set(Calendar.SECOND, 00);
				calendar.set(Calendar.MILLISECOND, 0);

				calendar1.set(Calendar.HOUR_OF_DAY, 00);
				calendar1.set(Calendar.MINUTE, 00);
				calendar1.set(Calendar.SECOND, 00);
				calendar1.set(Calendar.MILLISECOND, 0);

				calendar2.set(Calendar.HOUR_OF_DAY, 00);
				calendar2.set(Calendar.MINUTE, 00);
				calendar2.set(Calendar.SECOND, 00);
				calendar2.set(Calendar.MILLISECOND, 0);
			}
			if (operator.equals(RuleConstants.IS_NOT_NULL)) {
				result = leftTerm != null && !leftTerm.equals("");
			} else if (operator.equals(RuleConstants.IS_NULL)) {
				result = leftTerm == null || leftTerm.equals("");
			} else {
				String leftTerm1 = (leftTerm == null ? "" : leftTerm);
				if (param.equals("SYSTEM_DATE") || param.equals("SYSTEM_TIME"))
					leftTerm1 = "";// server date time
				if (paramType.equals("3") && leftTerm1.length() == 8) {
					year = Integer.parseInt(leftTerm1.substring(0, 4));
					month = Integer.parseInt(leftTerm1.substring(4, 6));
					day = Integer.parseInt(leftTerm1.substring(6, 8));
					calendar.set(year, month - 1, day);
				} else if (paramType.equals("4") && leftTerm1.length() == 6) {
					hh = Integer.parseInt(leftTerm1.substring(0, 2));
					mm = Integer.parseInt(leftTerm1.substring(2, 4));
					calendar.set(Calendar.HOUR_OF_DAY, hh);
					calendar.set(Calendar.MINUTE, mm);
					calendar.set(Calendar.SECOND, ss);
					calendar.set(Calendar.MILLISECOND, 0);
				}

				if (paramType.equals("3") && rightTermArr[0].length() == 10) {
					year = Integer.parseInt(rightTermArr[0].split("/")[2]);
					month = Integer.parseInt(rightTermArr[0].split("/")[0]);
					day = Integer.parseInt(rightTermArr[0].split("/")[1]);
					calendar1.set(year, month - 1, day);
				} else if (paramType.equals("4") && rightTermArr[0].length() == 5) {
					hh = Integer.parseInt(rightTermArr[0].split(":")[0]);
					mm = Integer.parseInt(rightTermArr[0].split(":")[1]);
					calendar1.set(Calendar.HOUR_OF_DAY, hh);
					calendar1.set(Calendar.MINUTE, mm);
					calendar1.set(Calendar.SECOND, ss);
					calendar1.set(Calendar.MILLISECOND, 0);
				}

				if (operator.equals(RuleConstants.BETWEEN) || operator.equals(RuleConstants.NOT_BETWEEN)) {
					if (paramType.equals("3") && rightTermArr[1] != null && rightTermArr[1].length() == 10) {
						year = Integer.parseInt(rightTermArr[1].split("/")[2]);
						month = Integer.parseInt(rightTermArr[1].split("/")[0]);
						day = Integer.parseInt(rightTermArr[1].split("/")[1]);
						calendar2.set(year, month - 1, day);
					} else if (paramType.equals("4") && rightTermArr[1] != null && rightTermArr[1].length() == 5) {
						hh = Integer.parseInt(rightTermArr[1].split(":")[0]);
						mm = Integer.parseInt(rightTermArr[1].split(":")[1]);
						calendar2.set(Calendar.HOUR_OF_DAY, hh);
						calendar2.set(Calendar.MINUTE, mm);
						calendar2.set(Calendar.SECOND, ss);
						calendar2.set(Calendar.MILLISECOND, 0);
					}
				}

				if (operator.equals(RuleConstants.EQUALS_TO)) {
					result = calendar.equals(calendar1);
				} else if (operator.equals(RuleConstants.NOT_EQUALS_TO)) {
					result = !calendar.equals(calendar1);
				} else if (operator.equals(RuleConstants.LESS_THAN)) {
					result = calendar.before(calendar1);
				} else if (operator.equals(RuleConstants.LESS_OR_EQUALS_TO)) {
					result = calendar.before(calendar1) || calendar.equals(calendar1);
				} else if (operator.equals(RuleConstants.GREATER_THAN)) {
					result = calendar.after(calendar1);
				} else if (operator.equals(RuleConstants.GREATER_OR_EQUALS_TO)) {
					result = calendar.after(calendar1) || calendar.equals(calendar1);
				} else if (operator.equals(RuleConstants.BETWEEN)) {
					result = calendar.after(calendar1) && calendar.before(calendar2);
				} else if (operator.equals(RuleConstants.NOT_BETWEEN)) {
					result = !(calendar.after(calendar1) && calendar.before(calendar2));
				}

			}
		} catch (Exception e) {
			logger.error("Error occured in RuleEngineServiceImpl: " + e.getMessage(), e);
		}
		((HashMap<String, Object>) obj).put(ValueObjectKeys.EVAL_EXP,
				"(" + mapobj + ") " + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DATE) + " "
						+ calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) + " " + operator + " " + rightTerm + " = "
						+ result);
		return result;
	}

}
