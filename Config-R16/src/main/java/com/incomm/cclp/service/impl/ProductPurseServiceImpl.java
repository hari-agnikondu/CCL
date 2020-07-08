package com.incomm.cclp.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.dao.ProductPurseDao;
import com.incomm.cclp.domain.Product;
import com.incomm.cclp.domain.ProductPurse;
import com.incomm.cclp.service.ProductPurseService;
import com.incomm.cclp.service.ProductService;
import com.incomm.cclp.util.Util;

@Service
public class ProductPurseServiceImpl implements ProductPurseService {

	private static final Logger logger = LogManager.getLogger(ProductPurseServiceImpl.class);

	@Autowired
	ProductDAO productDao;

	@Autowired
	ProductPurseDao productPurseDao;

	@Autowired
	ProductService productService;
	
	@Autowired
	DistributedCacheServiceImpl distributedCacheService;

	@Override
	public List<String> updateProductPurseLimitAttributesByProgramId(Map<String, Object> inputAttributes,
			Long programId, Long purseId) throws IOException {
		logger.info(CCLPConstants.ENTER);
		List<String> errorList = new ArrayList<>();
		List<Product> products = productDao.getProductsByProgramId(programId);
		if (!CollectionUtils.isEmpty(products)) {
			Iterator<Product> itr = products.iterator();
			while (itr.hasNext()) {
				Product prod = itr.next();
				// Check attributes at productPurse level
				ProductPurse productPurse = productPurseDao.getProdPurseAttributesByProdPurseId(prod.getProductId(),
						purseId);
				if(productPurse!=null) {
				String attributes = productPurse.getAttributes();
				Map<String, Map<String, Object>> prodPurseAttributes = Util.jsonToMap(attributes);
				prodPurseAttributes = Util.updateValuesToMapOfMap(productService.getAllAttributesToCreateProductPurse(), prodPurseAttributes);
				logger.info("Limits...");

				if (prodPurseAttributes != null) {
					Map<String, Object> limitAttributes = prodPurseAttributes.get("Limits");
					
					inputAttributes.entrySet().stream().forEach(p -> {
						String newKey = null;

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.MIN_AMT_PER_TX)) {
							newKey = p.getKey().replace(CCLPConstants.MIN_AMT_PER_TX, CCLPConstants.MAX_AMT_PER_TX);
							logger.debug("minAmtPerTx newKey: {}", newKey);
							Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;
							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(highValue.toString()) >= 0 && p.getValue() != "" && Integer
									.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0)
								limitAttributes.put(p.getKey(), p.getValue());
							else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add(
										"Maximum amount per transaction is lessthan Minimum amount per transaction for Product : "
												+ prod.getProductName());
						}
						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.MAX_AMT_PER_TX)) {
							newKey = p.getKey().replace(CCLPConstants.MAX_AMT_PER_TX, CCLPConstants.MIN_AMT_PER_TX);
							logger.debug("maxAmtPerTx newKey: {}", newKey);
							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(value.toString()) == 0)
								limitAttributes.put(p.getKey(), p.getValue());
							else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add(
										"Minimum amount per transaction is greater than Maximum amount per transaction for Product : "
												+ prod.getProductName());
						}

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.DAILY_MAX_AMT)) {
							newKey = p.getKey().replace(CCLPConstants.DAILY_MAX_AMT, CCLPConstants.WEEKLY_MAX_AMT);
							logger.debug("dailyMaxAmt newKey: {}", newKey);
							Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;
							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0)
								limitAttributes.put(p.getKey(), p.getValue());
							else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Weekly max Amount is less than Daily Max Amount for Product : "
										+ prod.getProductName());
						}

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.DAILY_MAX_COUNT)) {
							newKey = p.getKey().replace(CCLPConstants.DAILY_MAX_COUNT, CCLPConstants.WEEKLY_MAX_COUNT);
							logger.debug("dailyMaxCount newKey: {}", newKey);
							Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;
							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0)
								limitAttributes.put(p.getKey(), p.getValue());
							else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Weekly max Count is less than Daily Max Count for Product : "
										+ prod.getProductName());
						}

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.WEEKLY_MAX_AMT)) {
							newKey = p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT, CCLPConstants.DAILY_MAX_AMT);
							logger.debug("weeklyMaxAmt newKey: {}", newKey);
							Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;

							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0) {
								String newKey2 = p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT,
										CCLPConstants.MONTHLY_MAX_AMT);
								Object value2 = (limitAttributes.get(newKey2) != ""
										&& limitAttributes.get(newKey2) != null) ? limitAttributes.get(newKey2) : 0;
								if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != ""
										&& p.getValue() != null)
									limitAttributes.put(p.getKey(), p.getValue());
								else
									limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							} else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Daily Max Amount is greater than Weekly Max Amount for Product : "
										+ prod.getProductName());
						}

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.WEEKLY_MAX_AMT)) {
							newKey = p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT, CCLPConstants.MONTHLY_MAX_AMT);
							logger.debug("weeklyMaxAmt newKey: {}", newKey);
							Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;
							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0) {
								String newKey2 = p.getKey().replace(CCLPConstants.WEEKLY_MAX_AMT,
										CCLPConstants.DAILY_MAX_AMT);
								Object value2 = (limitAttributes.get(newKey2) != ""
										&& limitAttributes.get(newKey2) != null) ? limitAttributes.get(newKey2) : 0;
								if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != ""
										&& p.getValue() != null)
									limitAttributes.put(p.getKey(), p.getValue());
								else
									limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							} else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Weekly Max Amount is greater than Monthly Max Amount for Product : "
										+ prod.getProductName());
						}

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.WEEKLY_MAX_COUNT)) {
							newKey = p.getKey().replace(CCLPConstants.WEEKLY_MAX_COUNT, CCLPConstants.DAILY_MAX_COUNT);
							logger.debug("weeklyMaxCount newKey: {}", newKey);
							Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;

							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0) {
								String newKey2 = p.getKey().replace(CCLPConstants.WEEKLY_MAX_COUNT,
										CCLPConstants.MONTHLY_MAX_COUNT);
								Object value2 = (limitAttributes.get(newKey2) != ""
										&& limitAttributes.get(newKey2) != null) ? limitAttributes.get(newKey2) : 0;
								if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != ""
										&& p.getValue() != null)
									limitAttributes.put(p.getKey(), p.getValue());
								else
									limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							} else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Daily Max Count is greater than weekly Max Count for Product : "
										+ prod.getProductName());
						}

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.WEEKLY_MAX_COUNT)) {
							newKey = p.getKey().replace(CCLPConstants.WEEKLY_MAX_COUNT,
									CCLPConstants.MONTHLY_MAX_COUNT);
							logger.debug("weeklyMaxCount newKey: {}", newKey);
							Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;
							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0) {
								String newKey2 = p.getKey().replace(CCLPConstants.WEEKLY_MAX_COUNT,
										CCLPConstants.DAILY_MAX_COUNT);
								Object value2 = (limitAttributes.get(newKey2) != ""
										&& limitAttributes.get(newKey2) != null) ? limitAttributes.get(newKey2) : 0;
								if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != ""
										&& p.getValue() != null)
									limitAttributes.put(p.getKey(), p.getValue());
								else
									limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							} else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Weekly Max Count is greater than Monthly Max Count for Product : "
										+ prod.getProductName());
						}

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.MONTHLY_MAX_AMT)) {
							newKey = p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT, CCLPConstants.WEEKLY_MAX_AMT);
							logger.debug("monthlyMaxAmt newKey: {}", newKey);
							Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;
							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0) {
								String newKey2 = p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT,
										CCLPConstants.YEARLY_MAX_AMT);
								Object value2 = (limitAttributes.get(newKey2) != ""
										&& limitAttributes.get(newKey2) != null) ? limitAttributes.get(newKey2) : 0;
								if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != ""
										&& p.getValue() != null)
									limitAttributes.put(p.getKey(), p.getValue());
								else
									limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							} else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Weekly Max Amount is greater than Monthly Max Amount for Product : "
										+ prod.getProductName());
						}

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.MONTHLY_MAX_AMT)) {
							newKey = p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT, CCLPConstants.YEARLY_MAX_AMT);
							logger.debug("monthlyMaxAmt newKey: {}", newKey);
							Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;
							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0) {
								String newKey2 = p.getKey().replace(CCLPConstants.MONTHLY_MAX_AMT,
										CCLPConstants.WEEKLY_MAX_AMT);
								Object value2 = (limitAttributes.get(newKey2) != ""
										&& limitAttributes.get(newKey2) != null) ? limitAttributes.get(newKey2) : 0;
								if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != ""
										&& p.getValue() != null)
									limitAttributes.put(p.getKey(), p.getValue());
								else
									limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							} else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Monthly Max Amount is greater than Yearly Max Amount for Product : "
										+ prod.getProductName());
						}

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.MONTHLY_MAX_COUNT)) {
							newKey = p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT,
									CCLPConstants.YEARLY_MAX_COUNT);
							logger.debug("monthlyMaxCount newKey: {}", newKey);
							Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;
							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0) {
								String newKey2 = p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT,
										CCLPConstants.WEEKLY_MAX_COUNT);
								Object value2 = (limitAttributes.get(newKey2) != ""
										&& limitAttributes.get(newKey2) != null) ? limitAttributes.get(newKey2) : 0;
								if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != ""
										&& p.getValue() != null)
									limitAttributes.put(p.getKey(), p.getValue());
								else
									limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							} else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Monthly Max Count is greater than Yearly max Count for Product : "
										+ prod.getProductName());
						}

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.MONTHLY_MAX_COUNT)) {
							newKey = p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT,
									CCLPConstants.WEEKLY_MAX_COUNT);
							logger.debug("monthlyMaxCount newKey: {}", newKey);
							Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;

							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0) {
								String newKey2 = p.getKey().replace(CCLPConstants.MONTHLY_MAX_COUNT,
										CCLPConstants.YEARLY_MAX_COUNT);
								Object value2 = (limitAttributes.get(newKey2) != ""
										&& limitAttributes.get(newKey2) != null) ? limitAttributes.get(newKey2) : 0;
								if (Integer.parseInt(value2.toString()) == 0 && p.getValue() != ""
										&& p.getValue() != null)
									limitAttributes.put(p.getKey(), p.getValue());
								else
									limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							} else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Weekly Max Count is greater than Monthly Max Count for Product : "
										+ prod.getProductName());
						}

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.YEARLY_MAX_AMT)) {
							newKey = p.getKey().replace(CCLPConstants.YEARLY_MAX_AMT, CCLPConstants.MONTHLY_MAX_AMT);
							logger.debug("yearlyMaxAmt newKey: {}", newKey);
							Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;

							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0)
								limitAttributes.put(p.getKey(), p.getValue());
							else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Monthly Max Amount is greater than Yearly Max Amount for Product : "
										+ prod.getProductName());
						}

						if (limitAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.YEARLY_MAX_COUNT)) {
							newKey = p.getKey().replace(CCLPConstants.YEARLY_MAX_COUNT,
									CCLPConstants.MONTHLY_MAX_COUNT);
							logger.debug("yearlyMaxCount newKey: {}", newKey);
							Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;
							Object value = (limitAttributes.get(newKey) != "" && limitAttributes.get(newKey) != null)
									? limitAttributes.get(newKey)
									: 0;
							if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0)
								limitAttributes.put(p.getKey(), p.getValue());
							else if (p.getValue() == null || p.getValue() == "")
								limitAttributes.put(p.getKey(), limitAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
								limitAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Monthly Max Count is greater than Yearly Max Count for Product : "
										+ prod.getProductName());
						}

					});
					prodPurseAttributes.put("Limits", limitAttributes);
					prodPurseAttributes = Util.removeEmptyValuefromMapOfMap(prodPurseAttributes);
					String attributesString = Util.mapToJson(prodPurseAttributes);
					productPurseDao.updateAttributes(attributesString, prod.getProductId(), purseId);
					distributedCacheService.updateProductPurseAttributesCache(String.valueOf(prod.getProductId()+"_"+purseId), prodPurseAttributes);

				}
				}
			}
		} else {
			logger.info("No products found for the programId {}", programId);
			errorList.add("No products found for the programId : " + programId);
		}
		logger.info("Error List is : " + errorList);
		logger.info(CCLPConstants.EXIT);
		return errorList;
	}

	@Override
	public List<String> updateProductPurseMonthlyCapAttributesByProgramId(Map<String, Object> inputAttributes,
			Long programId, Long purseId) throws IOException {

		logger.info(CCLPConstants.ENTER);
		List<String> errorList = new ArrayList<>();
		List<Product> products = productDao.getProductsByProgramId(programId);
		if (!CollectionUtils.isEmpty(products)) {
			Iterator<Product> itr = products.iterator();
			while (itr.hasNext()) {
				Product prod = itr.next();
				// Check attributes at productPurse level
				ProductPurse productPurse = productPurseDao.getProdPurseAttributesByProdPurseId(prod.getProductId(),
						purseId);
				// Check if default purse, call update attributes of productService.
				if(productPurse!=null) {
				String attributes = productPurse.getAttributes();
				Map<String, Map<String, Object>> prodPurseAttributes = Util.jsonToMap(attributes);
				prodPurseAttributes = Util.updateValuesToMapOfMap(productService.getAllAttributesToCreateProductPurse(), prodPurseAttributes);
				logger.info("MonthlyCap...");

				if (prodPurseAttributes != null) {
					Map<String, Object> feeCapAttributes = prodPurseAttributes.get("Monthly Fee Cap");
					inputAttributes.entrySet().stream().forEach(p -> {
						if (feeCapAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEE_DESC)
								&& (p.getValue() != null || p.getValue() != "")) {

							feeCapAttributes.put(p.getKey(), p.getValue());
						}
						if (feeCapAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.ASSESSMENT_DATE)
								&& (p.getValue() != null || p.getValue() != "")) {

							feeCapAttributes.put(p.getKey(), p.getValue());
						}
						if (feeCapAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEECAP_AMT)
								&& (p.getValue() != null || p.getValue() != "")) {

							feeCapAttributes.put(p.getKey(), p.getValue());
						}
						if (feeCapAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.TIME_PERIOD)
								&& (p.getValue() != null || p.getValue() != "")) {

							feeCapAttributes.put(p.getKey(), p.getValue());
						}

					});
					prodPurseAttributes.put("Monthly Fee Cap", feeCapAttributes);
					prodPurseAttributes = Util.removeEmptyValuefromMapOfMap(prodPurseAttributes);
					String attributesString = Util.mapToJson(prodPurseAttributes);
					productPurseDao.updateAttributes(attributesString, prod.getProductId(), purseId);
					distributedCacheService.updateProductPurseAttributesCache(String.valueOf(prod.getProductId()+"_"+purseId), prodPurseAttributes);
				}
				}
			}
		} else {
			logger.info("No products found for the programId {}", programId);
			errorList.add("No products found for the programId : " + programId);
		}
		logger.info("Error List is : " + errorList);
		logger.info(CCLPConstants.EXIT);
		return errorList;
	}

	@Override
	public List<String> updateProductPurseTxnFeeAttributesByProgramId(Map<String, Object> inputAttributes,
			Long programId, Long purseId) throws IOException {

		logger.info(CCLPConstants.ENTER);
		List<String> errorList = new ArrayList<>();
		List<Product> products = productDao.getProductsByProgramId(programId);
		if (!CollectionUtils.isEmpty(products)) {
			Iterator<Product> itr = products.iterator();
			while (itr.hasNext()) {
				Product prod = itr.next();
				// Check attributes at productPurse level
				ProductPurse productPurse = productPurseDao.getProdPurseAttributesByProdPurseId(prod.getProductId(),
						purseId);
				// Check if default purse, call update attributes of productService.
				if(productPurse!=null) {
				String attributes = productPurse.getAttributes();
				Map<String, Map<String, Object>> prodPurseAttributes = Util.jsonToMap(attributes);
				prodPurseAttributes = Util.updateValuesToMapOfMap(productService.getAllAttributesToCreateProductPurse(), prodPurseAttributes);
				logger.info("TxnFee...");

				if (prodPurseAttributes != null) {
					Map<String, Object> tranFeeAttributes = prodPurseAttributes.get("Transaction Fees");
					logger.info("Transaction Fees...");
					inputAttributes.entrySet().stream().forEach(p -> {
						String newKey = null;
						if (tranFeeAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.FREE_COUNT)) {
							newKey = p.getKey().replace(CCLPConstants.FREE_COUNT, CCLPConstants.MAX_COUNT);
							logger.debug("freeCount newKey: {}", newKey);
							Object highValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;
							Object value = (tranFeeAttributes.get(newKey) != ""
									&& tranFeeAttributes.get(newKey) != null) ? tranFeeAttributes.get(newKey) : 0;
							if (Integer.parseInt(highValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(highValue.toString()) >= Integer.parseInt(p.getValue().toString()))
								tranFeeAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0)
								tranFeeAttributes.put(p.getKey(), p.getValue());
							else if (p.getValue() == null || p.getValue() == "")
								tranFeeAttributes.put(p.getKey(), tranFeeAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) >= Integer.parseInt(p.getValue().toString()))
								tranFeeAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add(
										"Maximum Count is less than Free Count for Product : " + prod.getProductName());
						}

						if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.MAX_COUNT)) {
							newKey = p.getKey().replace(CCLPConstants.MAX_COUNT, CCLPConstants.FREE_COUNT);
							logger.debug("maxCount newKey: {}", newKey);
							Object lowValue = inputAttributes.get(newKey) != "" && inputAttributes.get(newKey) != null
									? inputAttributes.get(newKey)
									: 0;
							Object value = (tranFeeAttributes.get(newKey) != ""
									&& tranFeeAttributes.get(newKey) != null) ? tranFeeAttributes.get(newKey) : 0;
							if (Integer.parseInt(lowValue.toString()) > 0 && p.getValue() != "" && Integer
									.parseInt(lowValue.toString()) <= Integer.parseInt(p.getValue().toString()))
								tranFeeAttributes.put(p.getKey(), p.getValue());
							else if (Integer.parseInt(value.toString()) == 0)
								tranFeeAttributes.put(p.getKey(), p.getValue());
							else if (p.getValue() == null || p.getValue() == "")
								tranFeeAttributes.put(p.getKey(), tranFeeAttributes.get(p.getKey()));
							else if (Integer.parseInt(value.toString()) <= Integer.parseInt(p.getValue().toString()))
								tranFeeAttributes.put(p.getKey(), p.getValue());
							else
								errorList.add("Free Count is greater than Maximum Count for Product : "
										+ prod.getProductName());
						}
						if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEE_DESC)
								&& (p.getValue() != null || p.getValue() != "")) {
							tranFeeAttributes.put(p.getKey(), p.getValue());
						}

						if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEE_AMT)
								&& (p.getValue() != null || p.getValue() != "")) {
							tranFeeAttributes.put(p.getKey(), p.getValue());
						}

						if (tranFeeAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.FEE_CONDITION)
								&& (p.getValue() != null || p.getValue() != "")) {

							tranFeeAttributes.put(p.getKey(), p.getValue());
						}

						if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.FEE_PERCENT)
								&& (p.getValue() != null || p.getValue() != "")) {
							tranFeeAttributes.put(p.getKey(), p.getValue());
						}

						if (tranFeeAttributes.containsKey(p.getKey()) && p.getKey().endsWith(CCLPConstants.MIN_FEE_AMT)
								&& (p.getValue() != null || p.getValue() != "")) {
							tranFeeAttributes.put(p.getKey(), p.getValue());
						}

						if (tranFeeAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.FREE_COUNT_FREQ)
								&& (p.getValue() != null || p.getValue() != "")) {
							tranFeeAttributes.put(p.getKey(), p.getValue());
						}

						if (tranFeeAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.MAX_COUNT_FREQ)
								&& (p.getValue() != null || p.getValue() != "")) {
							tranFeeAttributes.put(p.getKey(), p.getValue());
						}
						if (tranFeeAttributes.containsKey(p.getKey())
								&& p.getKey().endsWith(CCLPConstants.MONTHLY_FEECAP_AVAIL)
								&& (p.getValue() != null || p.getValue() != "")) {
							tranFeeAttributes.put(p.getKey(), p.getValue());
						}

					});
					prodPurseAttributes.put("Transaction Fees", tranFeeAttributes);
					prodPurseAttributes = Util.removeEmptyValuefromMapOfMap(prodPurseAttributes);
					String attributesString = Util.mapToJson(prodPurseAttributes);
					productPurseDao.updateAttributes(attributesString, prod.getProductId(), purseId);
					distributedCacheService.updateProductPurseAttributesCache(String.valueOf(prod.getProductId()+"_"+purseId), prodPurseAttributes);
				}
				}
			}
		} else {
			logger.info("No products found for the programId {}", programId);
			errorList.add("No products found for the programId : " + programId);
		}
		logger.info("Error List is : " + errorList);
		logger.info(CCLPConstants.EXIT);
		return errorList;
	}

}
