package com.incomm.cclp.fsapi.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.OrderDAO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
import com.incomm.cclp.fsapi.bean.FsApiValidationDetail;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.CardStatusInquiryDAO;
import com.incomm.cclp.fsapi.dao.OrderProcessDAO;
import com.incomm.cclp.fsapi.dao.ProductDAO;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.ProductService;
import com.incomm.cclp.fsapi.service.TransactionService;
import com.incomm.cclp.util.Util;


@Component
public class OrderValidator {

	@Autowired 
	OrderDAO orderDao;

	@Autowired
	ProductService productService;

	@Autowired
	ProductDAO productDao;

	@Autowired 
	OrderProcessDAO orderProcessDAO;

	@Autowired
	CardStatusInquiryDAO cardStatusInquiryDao;

	@Autowired
	TransactionService transactionService;
	
	@Autowired
	CommonService commonService;

	private static final Logger logger = LogManager.getLogger(OrderValidator.class);

	@SuppressWarnings("unchecked")
	public void validate(final Map<String, Object> valuHashMap /*, final Connection conn
			,final OrderService orderService*/) throws ServiceException {
		final Map<String, Object> tempValuHashMap = valuHashMap;
		final List<Map<String, Object>> lineItemList = (List<Map<String, Object>>) tempValuHashMap
				.get(FSAPIConstants.ORDER_LINE_ITEM);
		final Map<String, String> packIdMap = new HashMap<>();
		final Map<String, Long> productIdMap = new HashMap<>();
		final Set<String> productIdSet = new HashSet<>();
		Long totalCount = 0L;
		final String partnerId = String.valueOf(tempValuHashMap.get(FSAPIConstants.ORDER_PARTNERID));
		final List<ErrorMsgBean> errList = (List<ErrorMsgBean>) tempValuHashMap.get(FSAPIConstants.ORRDER_ERROR_LIST);
		final String orderId = String.valueOf(valuHashMap.get(FSAPIConstants.ORDER_ORDER_ID));
		final Set<String> lineItemIdSet = new HashSet<>();
		String fulfillVndr = "";
		logger.info(CCLPConstants.ENTER);

		for (final Map<String, Object> temp : lineItemList) {
			final List<ErrorMsgBean> lineItemErrList =new LinkedList<>();
			temp.put(FSAPIConstants.LINEITEM_ERR_LIST, lineItemErrList);

		}

		checkProdAndPackId(tempValuHashMap);
		final List<Map<String, Object>> lineItemList1 = (List<Map<String, Object>>) tempValuHashMap.get(FSAPIConstants.ORDER_LINE_ITEM);
		for (final Map<String, Object> temp : lineItemList1) {
			final String packageId = String.valueOf(temp.get(FSAPIConstants.ORDER_PACKAGE_ID));
			final String productId = String.valueOf(temp.get(FSAPIConstants.ORDER_PRODUCT_ID));
			packIdMap.put(packageId.toUpperCase(), productId);

		}

		logger.debug("Check fulfillment type");
		Map<String,Object> packageMap = checkFulFillmentVendorWithkeyPair(packIdMap.keySet(),errList);
		Boolean isBulkOrIND=(Boolean)packageMap.get(FSAPIConstants.VENDOR_FULFILLMENT_TYPE);
		
		fulfillVndr=packageMap.get(FSAPIConstants.FULFILLMENT_TYPE)+"";
		valuHashMap.put(FSAPIConstants.FULFILLMENT_TYPE,fulfillVndr);
		if(isBulkOrIND==true) {
			checkDeliveryChannel(tempValuHashMap, errList);
		}


		if (!errList.isEmpty()) {

			for (Map<String, Object> tempMap : lineItemList) {	
				final List<ErrorMsgBean> lineItemErrList =new LinkedList<>();

				final ErrorMsgBean errorBean = new ErrorMsgBean();
				errorBean.setKey(FSAPIConstants.ORDER_REJECT_STATUS);
				errorBean.setErrorMsg(FSAPIConstants.ORDER_CHANNEL_EERMSG);
				errorBean.setRespCode(B2BResponseCode.INVALID_FIELD);
				lineItemErrList.add(errorBean);
				tempMap.put(FSAPIConstants.LINEITEM_ERR_LIST, lineItemErrList);

				/*tempMap.put(FSAPIConstants.ORDER_LINE_ITEMSTATUS, FSAPIConstants.ORDER_REJECT_STATUS);
					tempMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.INVALID_FIELD);
					tempMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, FSAPIConstants.ORDER_CHANNEL_EERMSG);
					tempMap.put(FSAPIConstants.ORDER_LINE_ITEMERRMSG, FSAPIConstants.ORDER_CHANNEL_EERMSG);*/
				/*tempMap.put(key, value)*/
			}	
		} else {



			for (final Map<String, Object> temp : lineItemList) {

				String quantity = String.valueOf(temp.get(FSAPIConstants.ORDER_QUANTITY));
				final String productId = String.valueOf(temp.get(FSAPIConstants.ORDER_PRODUCT_ID));
				final String lineItemId = String.valueOf(temp.get(FSAPIConstants.ORDER_LINE_ITEM_ID));
				final String statusCode = String.valueOf(temp.get(FSAPIConstants.LINEITEM_RESPONSE_CODE));
				 List<ErrorMsgBean> lineItemErrList=(List<ErrorMsgBean>) temp.get(FSAPIConstants.LINEITEM_ERR_LIST);
				//if(!(B2BResponseCode.REQUIRED_REQUIRED_PROPERTY.equals(statusCode) || B2BResponseCode.INVALID_FIELD.equals(statusCode))){
					
					if (lineItemId != null) {
						lineItemIdSet.add(lineItemId.toUpperCase());
					}

					if(temp.get(FSAPIConstants.ERROR_MSG)!=null && temp.get(FSAPIConstants.ERROR_MSG)!=FSAPIConstants.ORDER_EMPTY_STRING){
						logger.error("Error found in line item: {}, error: {}",lineItemId,String.valueOf(temp.get(FSAPIConstants.ERROR_MSG)));
						// List<ErrorMsgBean> lineItemErrList=(List<ErrorMsgBean>) temp.get(FSAPIConstants.LINEITEM_ERR_LIST);
						final ErrorMsgBean errorBean = new ErrorMsgBean();
						errorBean.setKey(FSAPIConstants.ORDER_SHIPPING_METHOD);
						errorBean.setErrorMsg(String.valueOf(temp.get(FSAPIConstants.ERROR_MSG)));
						errorBean.setRespCode(String.valueOf(temp.get(FSAPIConstants.LINEITEM_RESPONSE_CODE)));
						//errList.add(errorBean);
						lineItemErrList.add(errorBean);
					}
					
					if(!Util.isEmpty(productId)){
						productIdSet.add(productId);
					Long quan=0l;
					try {
						quan=Long.valueOf(quantity);
						//if(FSAPIConstants.ORDER_VENDORTYPE_IND.equalsIgnoreCase(fulfillVndr)) {
							totalCount = totalCount +quan;
						//}
					} catch (Exception ex) {
						logger.info("",ex);
						quantity = FSAPIConstants.ORDER_ZERO;
					}

					if(quan==0){
						logger.error("Quantity for this line item is zero");
						// List<ErrorMsgBean> lineItemErrList=(List<ErrorMsgBean>) temp.get(FSAPIConstants.LINEITEM_ERR_LIST);
						 ErrorMsgBean errorBean = new ErrorMsgBean();
						errorBean.setKey(FSAPIConstants.ORDER_QUANTITY);
						errorBean.setErrorMsg(B2BResponseMessage.QUANTITY_SHOULD_NOT_BE_ZERO);
						errorBean.setRespCode(B2BResponseCode.REQUIRED_QUANTITY_ZERO);
						lineItemErrList.add(errorBean);
					}

					if (!Util.isEmpty(productId) && productIdMap.get(productId) == null) {
						productIdMap.put(productId, Long.valueOf(quantity));
					} else {
						final Long tempValue = Long.valueOf(quantity) + productIdMap.get(productId);
						productIdMap.put(productId, tempValue);
					}
										
				}else{
					logger.error("product is null- in line item"+lineItemId);
				}
				}

				if("".equalsIgnoreCase(fulfillVndr)) {
					fulfillVndr=FSAPIConstants.ORDER_VENDORTYPE_IND;
				}
				checkProductId(tempValuHashMap);


				if(lineItemList.size()!=lineItemIdSet.size()){
					valuHashMap.put(FSAPIConstants.ORDER_DUPLICATE_LINEITEM, FSAPIConstants.Y);			
					for (Map<String, Object> tempMap : lineItemList) {									
						tempMap.put(FSAPIConstants.ORDER_LINE_ITEMSTATUS, FSAPIConstants.ORDER_REJECT_STATUS);
						tempMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.DUPLICATE_ORDER_LINE_ITEM);
						//tempMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, B2BResponseMessage.INVALID_LINE_ITEM_ID);
						tempMap.put(FSAPIConstants.ORDER_LINE_ITEMERRMSG, FSAPIConstants.ORDER_LINEITEM_DUPLICATE_ERRMSG);

					}	
				}

				if (orderDao.b2bDuplicateOrderCheck(orderId,partnerId)  != 0) {
					valuHashMap.put(FSAPIConstants.ORDER_DUPLICATE_CHECK, FSAPIConstants.Y);
					for (Map<String, Object> tempMap : lineItemList) {									
						tempMap.put(FSAPIConstants.ORDER_LINE_ITEMSTATUS, FSAPIConstants.ORDER_REJECT_STATUS);
						tempMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.DUPLICATE_ORDER);
						//tempMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, B2BResponseMessage.INVALID_LINE_ITEM_ID);
						tempMap.put(FSAPIConstants.ORDER_LINE_ITEMERRMSG, FSAPIConstants.ORDER_DUPLICATE_ERRMSG);
					}	
				} else {
					valuHashMap.put(FSAPIConstants.ORDER_DUPLICATE_CHECK, FSAPIConstants.N);
				}

				boolean vFlag=checkVirtualProduct(productIdSet);
				if(!vFlag){
					final String shippingMethod = String.valueOf(valuHashMap.get(FSAPIConstants.ORDER_SHIPPING_METHOD));
					final Map<String, FsApiValidationDetail> validationDtlMap = transactionService.getFsapiValidationDetailByApiKey(FSAPIConstants.ORDER_API+FSAPIConstants.POST);
					FsApiValidationDetail shippingMtohdVal=validationDtlMap.get(FSAPIConstants.ORDER_SHIPPING_METHOD);
					String shippingMthd=FSAPIConstants.ORDER_EMPTY_STRING;
					if(shippingMtohdVal!=null){
						shippingMthd=shippingMtohdVal.getRegexExpression();
					}
					boolean shiipingValFlag=false;
					try{
						shiipingValFlag=shippingMethod.matches(shippingMthd);
					}catch(Exception ex){
						logger.debug("OrderValidator  shippingMethod "+shippingMthd);
						logger.error("OrderValidator  shippingMethod ",ex);
					}
					if(!shiipingValFlag){
						logger.error(FSAPIConstants.ORDER_SHIPPING_METHOD_ERRMSG);
						
						final ErrorMsgBean errorBean = new ErrorMsgBean();
						errorBean.setKey(FSAPIConstants.ORDER_SHIPPING_METHOD);
						errorBean.setErrorMsg(FSAPIConstants.ORDER_SHIPPING_METHOD_ERRMSG);
						errorBean.setRespCode(B2BResponseCode.INVALID_SHIPPINGMETHOD);
						errList.add(errorBean);
					}   
				}


				checkActivationCode(tempValuHashMap);

				checkProductType(tempValuHashMap);


				checkPartnerId(tempValuHashMap);

				if (!packIdMap.isEmpty()) {
					for (final Map<String, Object> temp : lineItemList) {
						 String productId = String.valueOf(temp.get(FSAPIConstants.ORDER_PRODUCT_ID));
						 String packageId = String.valueOf(temp.get(FSAPIConstants.ORDER_PACKAGE_ID));
						 fulfillVndr=packageMap.get(packageId)+"";

					long count = 9;
					totalCount=productIdMap.get(productId);
					 List<ErrorMsgBean> lineItemErrList=(List<ErrorMsgBean>) temp.get(FSAPIConstants.LINEITEM_ERR_LIST);
					if (FSAPIConstants.ORDER_VENDORTYPE_IND.equalsIgnoreCase(fulfillVndr) && totalCount > count) {
						logger.error("invalid quantity");
						final ErrorMsgBean errorBean = new ErrorMsgBean();
						errorBean.setKey(FSAPIConstants.ORDER_QUANTITY);
						errorBean.setErrorMsg(B2BResponseMessage.QUANTITY_SHOULD_NOT_EXCEED_BE_ZERO);
						errorBean.setRespCode(B2BResponseCode.REQUIRED_QUANTITY_EXCEED);
						lineItemErrList.add(errorBean);
					}
				}
				}
				if (!productIdSet.isEmpty()) {

					checkFulFillmentOption(tempValuHashMap,errList);
				}
				final String acceptPartialOrders=String.valueOf(tempValuHashMap.get(FSAPIConstants.ORDER_ACCEPT_PARTIAL_ORDERS));
				if (totalCount > 0) {
					checkPanCount(productIdMap, lineItemList,acceptPartialOrders);
				}
				checkSerialCount(productIdMap, lineItemList,acceptPartialOrders);
		//	}
		}
	}

	private void checkDeliveryChannel(Map<String, Object> tempValuHashMap, List<ErrorMsgBean> errList) {

		String channel = String.valueOf(tempValuHashMap.get(FSAPIConstants.CHANNEL));
		String delChannel  =  productDao.getChannels();
		delChannel = Util.convertAsString(delChannel);
		String[] delArray = delChannel.split(",");
		List<String> delList = Arrays.asList(delArray);

		if (!(Util.isEmpty(delChannel))) {
			if (!(delList.contains(channel))) {
				logger.error(FSAPIConstants.ORDER_FULFILMENT_TYPEERRMSG);
				logger.error(FSAPIConstants.ORDER_FULFILMENT_TYPEERRMSG);
				final ErrorMsgBean tempBean = new ErrorMsgBean();
				tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
				tempBean.setErrorMsg(FSAPIConstants.DELIVERY_CHANNEL_SUPPORT+":"+channel);
				tempBean.setRespCode(B2BResponseCode.INVALID_FIELD);
				errList.add(tempBean);

			}
		} else {
			logger.error(FSAPIConstants.ORDER_FULFILMENT_TYPEERRMSG);
			final ErrorMsgBean tempBean = new ErrorMsgBean();
			tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
			tempBean.setErrorMsg(FSAPIConstants.DELIVERY_CHANNEL_SUPPORT+":"+channel);
			tempBean.setRespCode(B2BResponseCode.INVALID_FIELD);
			errList.add(tempBean);
		}

	}


	private boolean checkVirtualProduct(Set<String> productIdSet) {
		List<String> productTypes;
		productTypes = productDao.checkVirtualProduct(productIdSet);
		boolean virtualProd = false;
		if(productTypes.contains("Virtual")) {
			virtualProd = true;
		}
		return virtualProd;
	}


	private void checkProductId(Map<String, Object> tempValuHashMap) {
		logger.info(CCLPConstants.ENTER);

		@SuppressWarnings("unchecked")
		final List<Map<String, Object>> lineItemList = (List<Map<String, Object>>) tempValuHashMap
		.get(FSAPIConstants.ORDER_LINE_ITEM);

		for (final Map<String, Object> temp : lineItemList) {

			String productId = String.valueOf(temp.get(FSAPIConstants.ORDER_PRODUCT_ID));

			if (!Util.isEmpty(productId) && productDao.checkProductByProductId(productId) == 1) {

				logger.debug("Product exist");
				
				boolean isValidProd = commonService.checkProductValidity(productId);
				if(!isValidProd) {
					logger.error("Expired Product");
					final ErrorMsgBean tempBean = new ErrorMsgBean();
					tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
					tempBean.setErrorMsg(FSAPIConstants.EXPIRED_PRODUCT);
					tempBean.setRespCode(B2BResponseCode.EXPIRED_PRODUCT);
					@SuppressWarnings("unchecked")
					final List<ErrorMsgBean> lineItemErrList = (List<ErrorMsgBean>) temp
					.get(FSAPIConstants.LINEITEM_ERR_LIST);
					lineItemErrList.add(tempBean);
				}

			}else {

				logger.error("Product does not exist");

				final ErrorMsgBean tempBean = new ErrorMsgBean();
				tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
				tempBean.setErrorMsg(FSAPIConstants.INVALID_PRODUCT);
				tempBean.setRespCode(B2BResponseCode.INVALID_PRODUCT_TYPE);
				@SuppressWarnings("unchecked")
				final List<ErrorMsgBean> lineItemErrList = (List<ErrorMsgBean>) temp
				.get(FSAPIConstants.LINEITEM_ERR_LIST);
				lineItemErrList.add(tempBean);
			}



		}
		logger.info(CCLPConstants.EXIT);

	}


	private void checkFulFillmentOption(Map<String, Object> tempValuHashMap, List<ErrorMsgBean> errList) {

		logger.info(CCLPConstants.ENTER);
		try {
			logger.info("--------checkDenom---------");

			@SuppressWarnings("unchecked")
			final List<Map<String, Object>> lineItemList = (List<Map<String, Object>>) tempValuHashMap
			.get(FSAPIConstants.ORDER_LINE_ITEM);

			for (final Map<String, Object> temp : lineItemList) {

				if (!APIHelper.isEmpty(String.valueOf(temp.get(FSAPIConstants.FUNDING_OPTION)))) {
					if (FSAPIConstants.CARD_ACTIVATION
							.equalsIgnoreCase(String.valueOf(temp.get(FSAPIConstants.FUNDING_OPTION)))
							&& (APIHelper.isEmpty(String.valueOf(temp.get(FSAPIConstants.INITIAL_LOAD_OPTION))))) {
						logger.error(FSAPIConstants.MANDATORY_FIELD_FAILURE + FSAPIConstants.COLON_SEPARATOR
								+ FSAPIConstants.INITIAL_LOAD_OPTION);
						final ErrorMsgBean tempBean = new ErrorMsgBean();
						tempBean.setKey(FSAPIConstants.INITIAL_LOAD_OPTION);
						tempBean.setErrorMsg(FSAPIConstants.MANDATORY_FIELD_FAILURE + FSAPIConstants.COLON_SEPARATOR
								+ FSAPIConstants.INITIAL_LOAD_OPTION);
						tempBean.setRespCode(B2BResponseCode.INVALID_FIELD);
						errList.add(tempBean);
					}
					temp.put(FSAPIConstants.FUNDING_OVERRIDE, "Y");
					if (FSAPIConstants.ORDER_FULFILLMENT
							.equalsIgnoreCase(String.valueOf(temp.get(FSAPIConstants.FUNDING_OPTION)))) {
						temp.put(FSAPIConstants.FUNDING_OPTION, FSAPIConstants.ORDER_FULFILLMENT);
						temp.put(FSAPIConstants.INITIAL_LOAD_OPTION, null);
					} else if (FSAPIConstants.CARD_ACTIVATION
							.equalsIgnoreCase(String.valueOf(temp.get(FSAPIConstants.FUNDING_OPTION)))) {
						temp.put(FSAPIConstants.FUNDING_OPTION, FSAPIConstants.CARD_ACTIVATION);
						if (FSAPIConstants.ORDER_AMOUNT
								.equalsIgnoreCase(String.valueOf(temp.get(FSAPIConstants.INITIAL_LOAD_OPTION)))) {
							temp.put(FSAPIConstants.INITIAL_LOAD_OPTION, FSAPIConstants.ORDER_AMOUNT);
						} else if (FSAPIConstants.CARD_ACTIVATION_AMOUNT
								.equalsIgnoreCase(String.valueOf(temp.get(FSAPIConstants.INITIAL_LOAD_OPTION)))) {
							temp.put(FSAPIConstants.INITIAL_LOAD_OPTION, FSAPIConstants.CARD_ACTIVATION_AMOUNT);
						}
					}
				}

				if (APIHelper.isEmpty(String.valueOf(temp.get(FSAPIConstants.FUNDING_OPTION)))) {


					String productId = String.valueOf(temp.get(FSAPIConstants.ORDER_PRODUCT_ID));

					Map<String, Map<String, Object>> attributes = productService.getProductAttributes(productId);


					Map<String, Object> productAttributes = attributes.get(ValueObjectKeys.PRODUCT);

					String fundingOption = String.valueOf(productAttributes.get("b2bProductFunding"));

					String initialLoadOption = String.valueOf(productAttributes.get("b2bSourceOfFunding"));

					if(!Util.isEmpty(fundingOption)) {
						temp.put(FSAPIConstants.FUNDING_OPTION, fundingOption);
					}

					if(!Util.isEmpty(initialLoadOption)) {
						temp.put(FSAPIConstants.INITIAL_LOAD_OPTION, initialLoadOption);
					}

					// need to get the attributes from product

					logger.debug("Funding options set from product");

				}

				if (!(String.valueOf(temp.get(FSAPIConstants.FUNDING_OPTION))
						.equalsIgnoreCase(FSAPIConstants.CARD_ACTIVATION)
						&& String.valueOf(temp.get(FSAPIConstants.INITIAL_LOAD_OPTION))
						.equalsIgnoreCase(FSAPIConstants.CARD_ACTIVATION_AMOUNT))) {
					logger.debug("initial load option: {} and funding option: {}",
							String.valueOf(temp.get(FSAPIConstants.INITIAL_LOAD_OPTION)),
							String.valueOf(temp.get(FSAPIConstants.FUNDING_OPTION)));
					checkDenomination(temp);
				}

			}
		} catch (Exception e) {
			logger.error("Exception while loading checkDenom()" + e);

		}
		logger.info(CCLPConstants.EXIT);
	}


	private void checkSerialCount(Map<String, Long> productIdMap, List<Map<String, Object>> lineItemList,
			String acceptPartialOrders) throws ServiceException {

		String partialFlag = FSAPIConstants.ORDER_FSAPI_FALSE;
		logger.info(CCLPConstants.ENTER);
		try {

			if (acceptPartialOrders != null && (FSAPIConstants.ORDER_FSAPI_TRUE.equalsIgnoreCase(acceptPartialOrders)
					|| FSAPIConstants.ORDER_FSAPI_TRUE1.equals(acceptPartialOrders))) {
				partialFlag = FSAPIConstants.ORDER_FSAPI_TRUE;
			}

			logger.info("--------checkSerialCount---------");
			for (final Entry<String, Long> temp : productIdMap.entrySet()) {

				final String key = temp.getKey();
				final Long value = temp.getValue();
				int availableSerial = orderProcessDAO.checkSerialCount(key, value);

				if (availableSerial < 1 && FSAPIConstants.ORDER_FSAPI_TRUE.equalsIgnoreCase(partialFlag)) {
					Long tCount = 0L;
					for (final Map<String, Object> tempMap : lineItemList) {
						String productId = String.valueOf(tempMap.get(FSAPIConstants.ORDER_PRODUCT_ID));
						if (productId.equals(key)) {

							final String qty = String.valueOf(tempMap.get(FSAPIConstants.ORDER_QUANTITY));
							Long count = 0L;
							try {
								count = Long.parseLong(qty);
							} catch (Exception ex) {
								count = Long.parseLong(FSAPIConstants.ORDER_ZERO);
							}
							tCount = count + tCount;
							logger.debug("checkSerialCount::::" + productId + "value:::" + count);

							int availableSerialLineItem = orderProcessDAO.checkSerialCount(productId, tCount);

							if (availableSerialLineItem < 1) {
								tCount = tCount - count;
								final ErrorMsgBean tempBean = new ErrorMsgBean();
								logger.error("Serial number not available for lineItem: {}",
										String.valueOf(tempMap.get(FSAPIConstants.LINE_ITEMID)));
								tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
								tempBean.setErrorMsg(FSAPIConstants.ORDER_SERIALNOCHK_ERRMSG);
								tempBean.setRespCode(B2BResponseCode.REQUIRED_SERIAL_NUMBERS_ARE_NOT_AVAILABLE);
								@SuppressWarnings("unchecked")
								final List<ErrorMsgBean> lineItemErrList = (List<ErrorMsgBean>) tempMap
								.get(FSAPIConstants.LINEITEM_ERR_LIST);
								lineItemErrList.add(tempBean);
							}
						}
					}
				} else if (availableSerial < 1) {
					logger.error("Serial number not avaliable for {}", temp.getKey());
					for (final Map<String, Object> tempMap : lineItemList) {
						String pId = String.valueOf(tempMap.get(FSAPIConstants.ORDER_PRODUCT_ID));
						if (key.equalsIgnoreCase(pId)) {
							logger.error(FSAPIConstants.ORDER_SERIALNOCHK_ERRMSG);
							final ErrorMsgBean tempBean = new ErrorMsgBean();
							tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
							tempBean.setErrorMsg(FSAPIConstants.ORDER_SERIALNOCHK_ERRMSG);
							tempBean.setRespCode(B2BResponseCode.REQUIRED_SERIAL_NUMBERS_ARE_NOT_AVAILABLE);
							@SuppressWarnings("unchecked")
							final List<ErrorMsgBean> lineItemErrList = (List<ErrorMsgBean>) tempMap
							.get(FSAPIConstants.LINEITEM_ERR_LIST);
							lineItemErrList.add(tempBean);
						}
					}
				} else {
					logger.error("Pan check Pass available inventory" + availableSerial);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in serial count check" + e);
			throw new ServiceException(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR,
					B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);

		}
	}


	private void checkPanCount(Map<String, Long> productIdMap, List<Map<String, Object>> lineItemList,
			String acceptPartialOrders) throws ServiceException {

		String partialFlag = FSAPIConstants.ORDER_FSAPI_FALSE;
		logger.info(CCLPConstants.ENTER);
		try {

			if (acceptPartialOrders != null && (FSAPIConstants.ORDER_FSAPI_TRUE.equalsIgnoreCase(acceptPartialOrders)
					|| FSAPIConstants.ORDER_FSAPI_TRUE1.equals(acceptPartialOrders))) {
				partialFlag = FSAPIConstants.ORDER_FSAPI_TRUE;
			}
			for (final Entry<String, Long> temp : productIdMap.entrySet()) {
				final String key = temp.getKey();
				final Long value = temp.getValue();

				Long availablePan = orderProcessDAO.checkPanCount(key);
				if (availablePan < value && FSAPIConstants.ORDER_FSAPI_TRUE.equalsIgnoreCase(partialFlag)) {
					Long tCount = 0L;
					for (final Map<String, Object> tempMap : lineItemList) {
						String productId = String.valueOf(tempMap.get(FSAPIConstants.ORDER_PRODUCT_ID));
						if(!Util.isEmpty(productId)){
						if (productId.equals(key)) {
							final String qty = String.valueOf(tempMap.get(FSAPIConstants.ORDER_QUANTITY));
							Long count = 0L;
							try {
								count = Long.parseLong(qty);
							} catch (Exception ex) {
								logger.info("",ex);
								count = Long.parseLong(FSAPIConstants.ORDER_ZERO);
							}
							logger.debug("checkPanCount::::" + productId + "value:::" + count);
							tCount = count + tCount;
							if (availablePan < tCount) {

								tCount = tCount - count;
								logger.error("Pan not available for lineItem: {}",
										String.valueOf(tempMap.get(FSAPIConstants.LINE_ITEMID)));
								final ErrorMsgBean tempBean = new ErrorMsgBean();
								tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
								tempBean.setErrorMsg(FSAPIConstants.ORDER_PANCHK_ERRMSG);
								tempBean.setRespCode(B2BResponseCode.REQUIRED_CARD_NUMBERS_NOT_AVAILABLE);
								@SuppressWarnings("unchecked")
								final List<ErrorMsgBean> lineItemErrList = (List<ErrorMsgBean>) tempMap
								.get(FSAPIConstants.LINEITEM_ERR_LIST);
								lineItemErrList.add(tempBean);
							}
						}
					}	

					}
				} else if (availablePan < value) {
					logger.error("pan not avaliable for {}", temp.getKey());
					for (final Map<String, Object> tempMap : lineItemList) {
						String pId = String.valueOf(tempMap.get(FSAPIConstants.ORDER_PRODUCT_ID));
						if (key.equalsIgnoreCase(pId)) {
							logger.error(FSAPIConstants.ORDER_PANCHK_ERRMSG);
							final ErrorMsgBean tempBean = new ErrorMsgBean();
							tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
							tempBean.setErrorMsg(FSAPIConstants.ORDER_PANCHK_ERRMSG);
							tempBean.setRespCode(B2BResponseCode.REQUIRED_CARD_NUMBERS_NOT_AVAILABLE);
							@SuppressWarnings("unchecked")
							final List<ErrorMsgBean> lineItemErrList = (List<ErrorMsgBean>) tempMap
							.get(FSAPIConstants.LINEITEM_ERR_LIST);
							lineItemErrList.add(tempBean);
						}
					}
				} else {
					logger.error("Pan check Pass available inventory" + availablePan);
				}

			}
		} catch (Exception e) {
			logger.error("Exception while check pan availability" + e);
			throw new ServiceException(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR,
					B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);

		}
		logger.info(CCLPConstants.EXIT);
	}


	private void checkDenomination(Map<String, Object> temp) throws ServiceException {
		String denominationType = "";
		boolean isAmtMatched = false;

		BigDecimal txnAmount = new BigDecimal(String.valueOf(temp.get(FSAPIConstants.DENOMINATION)));

		final String productId = String.valueOf(temp.get(FSAPIConstants.ORDER_PRODUCT_ID));
		Map<String, Map<String, Object>> attributes = productService.getProductAttributes(productId);

		logger.info(CCLPConstants.ENTER);
		if(!CollectionUtils.isEmpty(attributes)) {

			Map<String, Object> productAttributes = attributes.get(ValueObjectKeys.PRODUCT);

			if (!CollectionUtils.isEmpty(productAttributes) && productAttributes.containsKey(FSAPIConstants.DENOMINATIONTYPE)
					&& !Objects.isNull(productAttributes.get(FSAPIConstants.DENOMINATIONTYPE))) {

				denominationType = String.valueOf(productAttributes.get(FSAPIConstants.DENOMINATIONTYPE));

				logger.debug("Product level denomination type : {}",denominationType);
				if ("Fixed".equalsIgnoreCase(denominationType)){
					BigDecimal fixedValue = new BigDecimal(productAttributes.get("denomFixed").toString());
					if (txnAmount.compareTo(fixedValue) == 0) {
						isAmtMatched = true;
					}
				} else if ("Variable".equalsIgnoreCase(denominationType)) {
					BigDecimal minValue = new BigDecimal(productAttributes.get("denomVarMin").toString());
					BigDecimal maxValue = new BigDecimal(productAttributes.get("denomVarMax").toString());

					int res1;
					res1 = txnAmount.compareTo(minValue);
					int res2;
					res2 = txnAmount.compareTo(maxValue);

					if (res1 >=0  && res2 <= 0) {
						isAmtMatched = true;
					}
				} else if ("Select".equalsIgnoreCase(denominationType)) {
					String selAmount = productAttributes.get("denomSelect").toString();
					String[] selectedAmounts = selAmount.split(",");
					List<String> listAmount = new ArrayList<>(Arrays.asList(selectedAmounts));

					for (String strAmount : listAmount) {
						BigDecimal amountVal = new BigDecimal(strAmount);
						if (txnAmount.compareTo(amountVal) == 0) {
							isAmtMatched = true;
						}
					}
				}
				if (isAmtMatched) {
					logger.debug("Denomination check passed successfully");
				} else {
					logger.error("Invalid input amount");
					@SuppressWarnings("unchecked")
					final List<ErrorMsgBean> lineItemErrList=(List<ErrorMsgBean>) temp.get(FSAPIConstants.LINEITEM_ERR_LIST);
					final ErrorMsgBean temps = new ErrorMsgBean();
					temps.setErrorMsg(FSAPIConstants.DENOMERRMSG);
					temps.setKey(FSAPIConstants.DENOMINATION);
					temps.setRespCode(B2BResponseCode.INVALID_DENOMINATION);
					lineItemErrList.add(temps);
				}
			}
		}else {
			logger.error("Product does not exist to check denomination");
		}
		logger.info(CCLPConstants.EXIT);
	}


	private String checkFulFillmentVendor(Set<String> packageIdList,
			List<ErrorMsgBean> errList) {
		String fulfillType = "";

		logger.info(CCLPConstants.ENTER);
		List<String> fulfillTypeList = productDao.checkFulFillmentVendor(packageIdList);

		if (!CollectionUtils.isEmpty(fulfillTypeList)) {
			if(fulfillTypeList.contains((FSAPIConstants.ORDER_VENDORTYPE_IND)) && 
					fulfillTypeList.contains((FSAPIConstants.ORDER_VENDORTYPE_BLK))) {
				fulfillType = "";
			}
			else if (fulfillTypeList.contains((FSAPIConstants.ORDER_VENDORTYPE_IND))) {
				fulfillType = FSAPIConstants.ORDER_VENDORTYPE_IND;
			} else if(fulfillTypeList.contains((FSAPIConstants.ORDER_VENDORTYPE_BLK))){
				fulfillType = FSAPIConstants.ORDER_VENDORTYPE_BLK;
			}else {
				fulfillType = "";
			}
			logger.debug("fulfillmentType: {}", fulfillType);
		} else {
			logger.error("Product Type is not valid");
			final ErrorMsgBean tempBean = new ErrorMsgBean();
			tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
			tempBean.setErrorMsg(B2BResponseMessage.INVALID_PRODUCT_TYPE);
			tempBean.setRespCode(B2BResponseCode.INVALID_PRODUCT_TYPE);
			errList.add(tempBean);
		}

		logger.info(CCLPConstants.EXIT);
		return fulfillType;

	}
	private Map<String,Object>  checkFulFillmentVendorWithkeyPair(Set<String> packageIdList,
			List<ErrorMsgBean> errList) {
		String fulfillType = "";
		Map<String,Object> fulfillmentMap = new HashMap<>();
		List<String> compareList = new ArrayList<>();
		Boolean isBulkOrIND=false;
		logger.info(CCLPConstants.ENTER);
		List<Map<String, Object>> fulfillTypeList = productDao.checkFulFillmentVendorWithkeypair(packageIdList);
		
		if (!Objects.isNull(fulfillTypeList)) {
			
			fulfillTypeList.forEach(map -> {
				fulfillmentMap.put(Util.convertAsString(map.get("package_id")), map.get("attribute_value"));
			});
			
		}
		
		
		if(!Objects.isNull(fulfillmentMap) && !fulfillmentMap.isEmpty()){
			
			for(Entry e:fulfillmentMap.entrySet()){
				
				compareList.add(Util.convertAsString(e.getValue()));
			}
		}
	

		if (!CollectionUtils.isEmpty(fulfillTypeList)) {
			if(compareList.contains((FSAPIConstants.ORDER_VENDORTYPE_IND)) && 
					compareList.contains((FSAPIConstants.ORDER_VENDORTYPE_BLK))) {
				
				isBulkOrIND = true;
				fulfillType = "";
			}
			else if (compareList.contains((FSAPIConstants.ORDER_VENDORTYPE_IND))) {
				fulfillType = FSAPIConstants.ORDER_VENDORTYPE_IND;
			} else if(compareList.contains((FSAPIConstants.ORDER_VENDORTYPE_BLK))){
				fulfillType = FSAPIConstants.ORDER_VENDORTYPE_BLK;
			}else {
				fulfillType = "";
			}
			
			fulfillmentMap.put(FSAPIConstants.VENDOR_FULFILLMENT_TYPE, isBulkOrIND);
			fulfillmentMap.put(FSAPIConstants.FULFILLMENT_TYPE,fulfillType);
			
			logger.debug("fulfillmentType: {}", fulfillType);
		} else {
			logger.error("packages are  not valid");
		/*	final ErrorMsgBean tempBean = new ErrorMsgBean();
			tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
			tempBean.setErrorMsg(B2BResponseMessage.INVALID_PRODUCT_TYPE);
			tempBean.setRespCode(B2BResponseCode.INVALID_PRODUCT_TYPE);
			errList.add(tempBean);*/
		}

		logger.info(CCLPConstants.EXIT);
		return fulfillmentMap;

	}
	@SuppressWarnings("unchecked")
	private void checkPartnerId(Map<String, Object> tempValuHashMap) {

		final List<Map<String, Object>> lineItemList = (List<Map<String, Object>>) tempValuHashMap
				.get(FSAPIConstants.ORDER_LINE_ITEM);
		String partnerId = String.valueOf(tempValuHashMap.get(FSAPIConstants.ORDER_PARTNERID));
		for (final Map<String, Object> temp : lineItemList) {

			final String productId = String.valueOf(temp.get(FSAPIConstants.ORDER_PRODUCT_ID));
			if(!Util.isEmpty(productId)){
			int prodPartnerIdcount = productDao.checkPartnerId(productId, partnerId);
			if (prodPartnerIdcount == 0) {
				logger.error(FSAPIConstants.ORDER_PARTNERID_ERRMSG + ":"
						+tempValuHashMap.get(FSAPIConstants.ORDER_PARTNERID) + ":Product ID:"
						+ temp.get(FSAPIConstants.ORDER_PRODUCT_ID));
				final List<ErrorMsgBean> lineItemErrList = (List<ErrorMsgBean>) temp
						.get(FSAPIConstants.LINEITEM_ERR_LIST);
				final ErrorMsgBean tempBean = new ErrorMsgBean();
				tempBean.setKey(String.valueOf(tempValuHashMap.get(FSAPIConstants.ORDER_PARTNERID)));
				tempBean.setErrorMsg(FSAPIConstants.ORDER_PARTNERID_ERRMSG + ":"
						+tempValuHashMap.get(FSAPIConstants.ORDER_PARTNERID) + ":Product ID:"
						+ temp.get(FSAPIConstants.ORDER_PRODUCT_ID));
				tempBean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
				lineItemErrList.add(tempBean);
			}
		}
		}

	}


	private void checkProductType(Map<String, Object> tempValuHashMap) throws ServiceException {
		@SuppressWarnings("unchecked")
		final List<Map<String, Object>> lineItemList1 = (List<Map<String, Object>>) tempValuHashMap.get(FSAPIConstants.ORDER_LINE_ITEM);
		for (final Map<String, Object> temp : lineItemList1) {
			String productType = null;
			String purseId = null;
			final String productId = String.valueOf(temp.get(FSAPIConstants.ORDER_PRODUCT_ID));
			Map<String, Map<String, Object>> attributes;
			if(!Util.isEmpty(productId)){
			 attributes = productService.getProductAttributes(productId);

			if(!CollectionUtils.isEmpty(attributes)) {

				Map<String, Object> productAttributes = attributes.get(ValueObjectKeys.PRODUCT);

				if(!CollectionUtils.isEmpty(productAttributes)) {
					productType = String.valueOf(productAttributes.get("productType"));
					purseId = String.valueOf(productAttributes.get("defaultPurse"));
				}else {
					logger.error("Error while getting product type");
				}
				
				if(!Util.isEmpty(purseId))
					temp.put(ValueObjectKeys.PURSE_ID, purseId);

				if(!Util.isEmpty(productType) && productType != null && productType.equalsIgnoreCase("b2b")) {

					tempValuHashMap.put(FSAPIConstants.ORDER_TYPE, "B2B");
					logger.debug("productType : "+productType);
				}
				else {
					logger.error(FSAPIConstants.ORDER_PRODUCTID_ERRMSG + ":productId:"
							+temp.get(FSAPIConstants.ORDER_PRODUCT_ID));
					@SuppressWarnings("unchecked")
					final List<ErrorMsgBean> lineItemErrList=(List<ErrorMsgBean>) temp.get(FSAPIConstants.LINEITEM_ERR_LIST);
					final ErrorMsgBean tempBean = new ErrorMsgBean();
					tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
					tempBean.setErrorMsg(FSAPIConstants.ORDER_PRODUCTID_ERRMSG + ":productId:"
							+temp.get(FSAPIConstants.ORDER_PRODUCT_ID));
					tempBean.setRespCode(B2BResponseCode.INVALID_PRODUCT_TYPE);
					lineItemErrList.add(tempBean);
				}
			}else {
				logger.error("product does not exist to check product type");
			}
		}
		}


	}


	@SuppressWarnings("unchecked")
	private void checkProdAndPackId(Map<String, Object> tempValuHashMap) throws ServiceException {


		final List<Map<String, Object>> lineItemList = (List<Map<String, Object>>) tempValuHashMap.get(FSAPIConstants.ORDER_LINE_ITEM);
		String defaultPackageId = null;
		List<Map<String,Object>> packageAttributes = null;
		logger.info(CCLPConstants.ENTER);
		for (final Map<String, Object> temp : lineItemList) {

			String packageId=String.valueOf(temp.get(FSAPIConstants.ORDER_PACKAGE_ID));
			final String productId = String.valueOf(temp.get(FSAPIConstants.ORDER_PRODUCT_ID));
			if(!(Util.isEmpty(packageId)) && !(Util.isEmpty(productId))) {
				int prodPackIdcount = productDao.checkProductPackageId(productId,packageId);
				if(prodPackIdcount == 0) {
					logger.error("Invalid ProductID and Package ID Combination: productId: {}, packageId: {} ",productId,String.valueOf(temp.get(FSAPIConstants.ORDER_PACKAGE_ID)));
					final List<ErrorMsgBean> lineItemErrList=(List<ErrorMsgBean>) temp.get(FSAPIConstants.LINEITEM_ERR_LIST);
					final ErrorMsgBean tempBean = new ErrorMsgBean();
					tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
					tempBean.setErrorMsg(FSAPIConstants.ORDER_PRODPACK_ERRMSG + ":productId"
							+ temp.get(FSAPIConstants.ORDER_PRODUCT_ID) + ":packageId"
							+ temp.get(FSAPIConstants.ORDER_PACKAGE_ID));
					tempBean.setRespCode(B2BResponseCode.INVALID_PRODUCTID_AND_PACKAGE_ID_COMBINATION);
					lineItemErrList.add(tempBean);
				}else {
					
					//added for getting embossLine and embossLine1
					
					packageAttributes= productDao.getPackageAttributes(packageId);
					if(!Objects.isNull(packageAttributes) && !packageAttributes.isEmpty()) {
						packageAttributes.forEach(attrMap ->{
							if(attrMap.containsKey("ATTRIBUTE_NAME") && attrMap.containsKey("ATTRIBUTE_VALUE")) {
								if(Util.isEmpty(String.valueOf(temp.get(FSAPIConstants.ORDER_EMBOSSED_LINE))) && attrMap.get("ATTRIBUTE_NAME").equals("embossLine3"))
									temp.put(FSAPIConstants.ORDER_EMBOSSED_LINE, attrMap.get("ATTRIBUTE_VALUE"));
								if(Util.isEmpty(String.valueOf(temp.get(FSAPIConstants.ORDER_EMBOSSED_LINE1))) && attrMap.get("ATTRIBUTE_NAME").equals("embossLine4"))
									temp.put(FSAPIConstants.ORDER_EMBOSSED_LINE1, attrMap.get("ATTRIBUTE_VALUE"));
							}
							
						});
					
					}
				}
			}else {

				logger.info("Package Id not present in input, check default packageId");
				Map<String, Map<String, Object>> attributes=null;
				if(!(Util.isEmpty(productId)))
				attributes = productService.getProductAttributes(productId);

				if(!CollectionUtils.isEmpty(attributes)) {

					Map<String, Object> productAttributes = attributes.get(ValueObjectKeys.PRODUCT);

					if(!CollectionUtils.isEmpty(productAttributes)) {
						defaultPackageId = String.valueOf(productAttributes.get("defaultPackage"));

						if(!Util.isEmpty(defaultPackageId)) {

							temp.put(FSAPIConstants.ORDER_PACKAGE_ID, defaultPackageId);
							
							//added for getting embossLine and embossLine1
							packageAttributes= productDao.getPackageAttributes(defaultPackageId);
							if(!Objects.isNull(packageAttributes) && !packageAttributes.isEmpty()) {
								packageAttributes.forEach(attrMap ->{
									if(attrMap.containsKey("ATTRIBUTE_NAME") && attrMap.containsKey("ATTRIBUTE_VALUE")) {
										if(Util.isEmpty(String.valueOf(temp.get(FSAPIConstants.ORDER_EMBOSSED_LINE))) && attrMap.get("ATTRIBUTE_NAME").equals("embossLine3"))
											temp.put(FSAPIConstants.ORDER_EMBOSSED_LINE, attrMap.get("ATTRIBUTE_VALUE"));
										if(Util.isEmpty(String.valueOf(temp.get(FSAPIConstants.ORDER_EMBOSSED_LINE1))) && attrMap.get("ATTRIBUTE_NAME").equals("embossLine4"))
											temp.put(FSAPIConstants.ORDER_EMBOSSED_LINE1, attrMap.get("ATTRIBUTE_VALUE"));
									}
									
								});
							
							}
							
							
							//added for getting embossLine and embossLine1
							packageAttributes= productDao.getPackageAttributes(defaultPackageId);
							if(!Objects.isNull(packageAttributes) && !packageAttributes.isEmpty()) {
								packageAttributes.forEach(attrMap ->{
									if(attrMap.containsKey("ATTRIBUTE_NAME") && attrMap.containsKey("ATTRIBUTE_VALUE")) {
										if(Util.isEmpty(String.valueOf(temp.get(FSAPIConstants.ORDER_EMBOSSED_LINE))) && attrMap.get("ATTRIBUTE_NAME").equals("embossLine3"))
											temp.put(FSAPIConstants.ORDER_EMBOSSED_LINE, attrMap.get("ATTRIBUTE_VALUE"));
										if(Util.isEmpty(String.valueOf(temp.get(FSAPIConstants.ORDER_EMBOSSED_LINE1))) && attrMap.get("ATTRIBUTE_NAME").equals("embossLine4"))
											temp.put(FSAPIConstants.ORDER_EMBOSSED_LINE1, attrMap.get("ATTRIBUTE_VALUE"));
									}
									
								});
							
							}
							
							logger.debug("Default packageId: {}",defaultPackageId);
						}else {
							logger.error("Invalid ProductID and Package ID Combination: productId: {}, packageId is null ",productId);
							final List<ErrorMsgBean> lineItemErrList = (List<ErrorMsgBean>) temp
									.get(FSAPIConstants.LINEITEM_ERR_LIST);
							final ErrorMsgBean tempBean = new ErrorMsgBean();
							tempBean.setKey(FSAPIConstants.ORDER_PRODUCT_ID);
							tempBean.setErrorMsg(FSAPIConstants.ORDER_PRODPACK_ERRMSG + ":productId"
									+ temp.get(FSAPIConstants.ORDER_PRODUCT_ID) + ":packageId is null");
							tempBean.setRespCode(B2BResponseCode.INVALID_PRODUCTID_AND_PACKAGE_ID_COMBINATION);
							lineItemErrList.add(tempBean);
						}

					}else {
						logger.error("Error while getting default packageId");
					}
				}else {
					logger.error("Product does not exist to check product and package id");
				}
			}

		}
		logger.info(CCLPConstants.EXIT);
	}
	@SuppressWarnings("unchecked")
	private void checkActivationCode(Map<String, Object> tempValuHashMap) throws ServiceException {
		final List<Map<String, Object>> lineItemList1 = (List<Map<String, Object>>) tempValuHashMap
				.get(FSAPIConstants.ORDER_LINE_ITEM);
		for (final Map<String, Object> temp : lineItemList1) {
			String activationCode = null;
			final String productId = String.valueOf(temp.get(FSAPIConstants.ORDER_PRODUCT_ID));
			if(!Util.isEmpty(productId)){
			final String actCode = String.valueOf(tempValuHashMap.get(FSAPIConstants.ORDER_ACTIVATION_CODE));
			Map<String, Map<String, Object>> attributes = productService.getProductAttributes(productId);

			if (!CollectionUtils.isEmpty(attributes)) {

				Map<String, Object> generalAttributes = attributes.get("General");

				if (!CollectionUtils.isEmpty(generalAttributes)) {
					activationCode = String.valueOf(generalAttributes.get("activationCode"));
				} else {
					logger.error("Error while getting activation code");
				}

				if (!Util.isEmpty(activationCode) && activationCode != null && activationCode.equalsIgnoreCase("true")) {
					if (!Util.isEmpty(actCode)) {
						logger.debug("Activation code : " + actCode);
					} else {
						logger.error("Invalid Activation code field ");
						final List<ErrorMsgBean> lineItemErrList = (List<ErrorMsgBean>) temp
								.get(FSAPIConstants.LINEITEM_ERR_LIST);
						final ErrorMsgBean errorBean = new ErrorMsgBean();
						errorBean.setKey(FSAPIConstants.ORDER_ACTIVATION_CODE);
						errorBean.setErrorMsg(FSAPIConstants.ORDER_ACTIVATION_CODERESPMSG);
						errorBean.setRespCode(B2BResponseCode.INVALID_FIELD);
						lineItemErrList.add(errorBean);
					}
				}
			} else {

				logger.error("Product does not exist to check actinvation code");

			}
		}
		}
	}


	public void checkOrderId(String orderId, String partnerId, List<ErrorMsgBean> errorList) {

		if (orderDao.b2bCheckOrderIDandPartnerID(orderId, partnerId) <= 0) {
			logger.error(FSAPIConstants.ORDER_IDERRMSG);
			final ErrorMsgBean tempBean = new ErrorMsgBean();
			tempBean.setKey(FSAPIConstants.ORDER_ORDER_ID);
			tempBean.setErrorMsg(B2BResponseMessage.INVALID_ORDER_ID_PARTNER_ID_COMBINATION);
			tempBean.setRespCode(B2BResponseCode.INVALID_ORDER_ID_PARTNER_ID_COMBINATION);
			errorList.add(tempBean);
		}

	}


	public void checkLineItemIds(String orderId, String partnerId, Map<String, Object> valueHashMap,
			List<ErrorMsgBean> errorList) {

		@SuppressWarnings("unchecked")
		HashSet<String> lineItemIdSet = (HashSet<String>) valueHashMap.get(FSAPIConstants.LINEITEMID);

		Set<String> tempSet = new HashSet<>(orderDao.b2bCheckLineItemID(orderId, partnerId));

		if (lineItemIdSet == null || lineItemIdSet.isEmpty()) {
			valueHashMap.put(FSAPIConstants.LINEITEMID, tempSet);
		} else {
			if (!Util.setComparision(tempSet, lineItemIdSet)) {
				logger.error(FSAPIConstants.ORDER_LINEITEM_ERRMSG);
				final ErrorMsgBean tempBean = new ErrorMsgBean();
				tempBean.setKey(FSAPIConstants.ORDER_LINE_ITEMS);
				tempBean.setErrorMsg(FSAPIConstants.ORDER_LINEITEM_ERRMSG);
				tempBean.setRespCode(B2BResponseCode.INVALID_LINE_ITEM_ID);
				errorList.add(tempBean);
			}
		}

	}


	public void getOrderStatus(Map<String, Object> valueHashMap) {

		orderProcessDAO.b2bCheckOrderStatus(valueHashMap);
	}

	public void getLineItemOrderStatus(Map<String, Object> valueHashMap) {

		orderProcessDAO.getLineItemOrderStatus(valueHashMap);
	}


	public List<String> csvToList(final String lineItemIds) {
		List<String> lineItemList = new ArrayList<>();
		if (lineItemIds != null) {
			lineItemList = Arrays.asList(lineItemIds.split("\\s*,\\s*"));
		}
		return lineItemList;
	}

	public Set<String> csvToSet(final String lineItemIds) {
		Set<String> lineItemSet = new HashSet<>();
		if (lineItemIds != null) {
			lineItemSet = new HashSet<>(Arrays.asList(lineItemIds.split(FSAPIConstants.COMMA_SEPARATOR)));
		}
		return lineItemSet;
	}

	@SuppressWarnings("unchecked")
	public void getlineItemDtls(final Map<String, Object> valueHashMap, final String apiName) throws ServiceException {
		Map<String, String> lineItemStatus = null;
		final List<Map<String, Object>> tempItemList = new LinkedList<>();
		try {
			lineItemStatus = (Map<String, String>) valueHashMap.get(FSAPIConstants.ORDER_LINE_ITEMDTLS);
			if (lineItemStatus != null) {
				for (final Entry<String, String> temp : lineItemStatus.entrySet()) {
					final Map<String, Object> tempLineItemMap = new HashMap<>();
					String key = temp.getKey();
					String value = temp.getValue();
					String respCode = "";
					String respMsg = "";
					String lstatus = "";
					if (value != null) {
						String[] respArr = value.split(":");
						if (respArr.length == 3) {
							lstatus = respArr[0];
							respMsg = respArr[1];
							respCode = respArr[2];
						}

						if (respCode != null && !"null".equalsIgnoreCase(respCode) && !"".equalsIgnoreCase(respCode)) {
							tempLineItemMap.put(FSAPIConstants.ORDER_LINE_ITEM_ID, key);
							tempLineItemMap.put(FSAPIConstants.LINEITEM_RESPONSE_CODE, respCode);
							tempLineItemMap.put(FSAPIConstants.LINEITEM_RESPONSE_MSG, respMsg);

							tempLineItemMap.put(FSAPIConstants.LINEITEM_STATUS, lstatus);

							List<Map<String, String>> tempList = new LinkedList<>();
							orderProcessDAO.getLineItemDtls(key, valueHashMap, tempList, apiName);
							tempLineItemMap.put(FSAPIConstants.CARDS, tempList);
							tempItemList.add(tempLineItemMap);

						} else {
							tempLineItemMap.put(FSAPIConstants.ORDER_LINE_ITEM_ID, key);
							tempLineItemMap.put(FSAPIConstants.LINEITEM_RESPONSE_CODE,
									FSAPIConstants.ORDER_REJECT_STATUS.equalsIgnoreCase(respMsg)
									? FSAPIConstants.ORDER_RESPONSE_REJRESCODE
											: FSAPIConstants.SUCCESS_RESP_CODE);
							tempLineItemMap.put(FSAPIConstants.LINEITEM_RESPONSE_MSG, respMsg);
							if (tempLineItemMap.get(FSAPIConstants.LINEITEM_RESPONSE_CODE).equals("00"))
								tempLineItemMap.put(FSAPIConstants.LINEITEM_STATUS, lstatus);
							else
								tempLineItemMap.put(FSAPIConstants.LINEITEM_STATUS,
										FSAPIConstants.INVALID_LINEITEM_STATUS);
							List<Map<String, String>> tempList = new LinkedList<>();
							List<Map<String, String>> tempList1 = orderProcessDAO.getLineItemDtls(key, valueHashMap,
									tempList, apiName);
							tempLineItemMap.put(FSAPIConstants.CARDS, tempList1);
							tempItemList.add(tempLineItemMap);
						}
					}

				}
			}
		} catch (Exception ex) {
			logger.info("Exception whileGETTING CARD ARRAY DETAILS {} ", ex);
			logger.error("Exception while GETTING CARD ARRAY DETAILS {} ", ex.getMessage());
		}
		valueHashMap.put(FSAPIConstants.ORDER_LINE_ITEMDTLS, tempItemList);

		if (FSAPIConstants.ORDER_STATUS_API.equalsIgnoreCase(apiName)
				|| FSAPIConstants.ORDER_API.equalsIgnoreCase(apiName)) {

			Map<String, Map<String, String>> resFields = transactionService
					.getFsapiDetailByApiKey(FSAPIConstants.ORDER_STATUS_API + FSAPIConstants.GET).getResSubTagFields();

			for (final Map<String, Object> temp : tempItemList) {
				List<Map<String, String>> tempCardArry = (List<Map<String, String>>) temp.get(FSAPIConstants.CARDS);
				List<Map<String, String>> tempCardArry1 = new ArrayList<>();
				for (Map<String, String> tempCrd : tempCardArry) {
					Map<String, String> tempMap = new HashMap<>();
					if (resFields != null && resFields.get(FSAPIConstants.CARDS) != null) {
						for (Entry<String, String> resp : resFields.get(FSAPIConstants.CARDS).entrySet()) {
							tempMap.put(resp.getValue(), tempCrd.get(resp.getKey()));
						}
					}
					tempCardArry1.add(tempMap);
				}
				temp.put(FSAPIConstants.CARDS, tempCardArry1);
			}
		}
	}

	public void proxyorserialRangeValiation(Map<String, Object> valuHashMap, List<ErrorMsgBean>   errorList){

		Long  start=Long.parseLong((String)valuHashMap.get(FSAPIConstants.CARDSTAT_START));
		Long  end=Long.parseLong((String)valuHashMap.get(FSAPIConstants.CARDSTAT_END));
		ErrorMsgBean errorBean=new ErrorMsgBean();
		if(start>end){
			logger.error(B2BResponseMessage.INVALID_FIELD);
			errorBean.setErrorMsg(B2BResponseMessage.INVALID_FIELD);
			errorBean.setRespCode(B2BResponseCode.INVALID_FIELD);
			errorList.add(errorBean);
		}
	}

	public void validateOrderDetails(Map<String, Object> valuHashMap, List<ErrorMsgBean>   errorList){

		cardStatusInquiryDao.checkOrderId(valuHashMap, errorList);	

	}


	public void checkPartnerId(Map<String, Object> valueHashMap,
			List<ErrorMsgBean> errorList) {
		cardStatusInquiryDao.checkPartnerId(valueHashMap, errorList);	

	}

	public void conditionalMandatoryValidation(Map<String, Object> valuHashMap, List<String> subtag, List<ErrorMsgBean>   errorList) {

		for (String s : subtag) {
			String value = String.valueOf(valuHashMap.get(s));
			if (value == null || FSAPIConstants.ORDER_EMPTY_STRING.equals(value) || "null".equalsIgnoreCase(value)) {
				logger.error(B2BResponseMessage.INVALID_FIELD + ":" + s);
				ErrorMsgBean tempErrBean = new ErrorMsgBean();
				tempErrBean.setErrorMsg(B2BResponseMessage.INVALID_FIELD + ":" + s);
				tempErrBean.setKey(s);
				tempErrBean.setRespCode(B2BResponseCode.INVALID_FIELD);
				errorList.add(tempErrBean);
			}

		}
	}


	public void checkOrderIdForActivation(String orderId, String partnerId, List<ErrorMsgBean> errorList) {

		if (orderDao.b2bCheckOrderIDandPartnerID(orderId,partnerId)  <= 0) {
			final ErrorMsgBean tempBean = new ErrorMsgBean();
			tempBean.setKey(FSAPIConstants.ORDER_ORDER_ID);
			tempBean.setErrorMsg(B2BResponseMessage.INVALID_ORDER_ID_PARTNER_ID_COMBINATION);
			tempBean.setRespCode(B2BResponseCode.INVALID_ORDER_ID_PARTNER_ID_COMBINATION);
			errorList.add(tempBean);
		}

	}


	public void checkLineItemIdsForActivation(String orderId, String partnerId, Set<String> lineItemIdSet, List<ErrorMsgBean> errorList) {

		Set<String> tempSet = new HashSet<>(orderDao.b2bCheckLineItemID(orderId, partnerId));
		if (!Util.setComparision(tempSet, lineItemIdSet)) {
			logger.error(FSAPIConstants.ORDER_LINEITEM_ERRMSG);
			final ErrorMsgBean tempBean = new ErrorMsgBean();
			tempBean.setKey(FSAPIConstants.ORDER_LINE_ITEMS);
			tempBean.setErrorMsg(FSAPIConstants.ORDER_LINEITEM_ERRMSG);
			tempBean.setRespCode(B2BResponseCode.INVALID_LINE_ITEM_ID);
			errorList.add(tempBean);
		}

	}
	@SuppressWarnings("unchecked")
	public void getLineItemDtls(final Map<String, Object> valueHashMap, final String apiName) throws ServiceException {
		Map<String, String> lineItemStatus = null;
		final List<Map<String, Object>> tempItemList = new LinkedList<>();
		try {
			lineItemStatus = (Map<String, String>) valueHashMap.get(FSAPIConstants.ORDER_LINE_ITEMDTLS);
			if (lineItemStatus != null) {
				for (final Entry<String, String> temp : lineItemStatus.entrySet()) {
					final Map<String, Object> tempLineItemMap = new HashMap<>();
					String key = temp.getKey();
					String value = temp.getValue();
					String respCode = "";
					String respMsg = "";
					String lstatus = "";
					if (value != null) {
						String[] respArr = value.split(":");
						if (respArr.length == 3) {
							lstatus = respArr[0];
							respMsg = respArr[1];
							respCode = respArr[2];
						}

						if (respCode != null && !"null".equalsIgnoreCase(respCode) && !"".equalsIgnoreCase(respCode)) {
							tempLineItemMap.put(FSAPIConstants.ORDER_LINE_ITEM_ID, key);
							tempLineItemMap.put(FSAPIConstants.LINEITEM_RESPONSE_CODE, respCode);
							tempLineItemMap.put(FSAPIConstants.LINEITEM_RESPONSE_MSG, respMsg);

							tempLineItemMap.put(FSAPIConstants.ORDER_RESPONSE_STATUS, lstatus);

							List<Map<String, String>> tempList = new LinkedList<>();
							orderProcessDAO.getLineItemDtls(key, valueHashMap, tempList, apiName);
							tempLineItemMap.put(FSAPIConstants.CARDS, tempList);
							tempItemList.add(tempLineItemMap);

						} else {
							tempLineItemMap.put(FSAPIConstants.ORDER_LINE_ITEM_ID, key);
							tempLineItemMap.put(FSAPIConstants.LINEITEM_RESPONSE_CODE,
									FSAPIConstants.ORDER_REJECT_STATUS.equalsIgnoreCase(respMsg)
									? FSAPIConstants.ORDER_RESPONSE_REJRESCODE
											: FSAPIConstants.SUCCESS_RESP_CODE);
							tempLineItemMap.put(FSAPIConstants.LINEITEM_RESPONSE_MSG, respMsg);
							if (tempLineItemMap.get(FSAPIConstants.LINEITEM_RESPONSE_CODE).equals("00"))
								tempLineItemMap.put(FSAPIConstants.ORDER_RESPONSE_STATUS, lstatus);
							else
								tempLineItemMap.put(FSAPIConstants.ORDER_RESPONSE_STATUS,
										FSAPIConstants.INVALID_LINEITEM_STATUS);
							List<Map<String, String>> tempList = new LinkedList<>();
							List<Map<String, String>> tempList1 = orderProcessDAO.getLineItemDtls(key, valueHashMap,
									tempList, apiName);
							tempLineItemMap.put(FSAPIConstants.CARDS, tempList1);
							tempItemList.add(tempLineItemMap);
						}
					}

				}
			}
		} catch (Exception ex) {
			logger.info("Exception whileGETTING CARD ARRAY DETAILS {} ", ex);
			logger.error("Exception while GETTING CARD ARRAY DETAILS {} ", ex.getMessage());
		}
		valueHashMap.put(FSAPIConstants.ORDER_LINE_ITEMDTLS, tempItemList);
	}


}
