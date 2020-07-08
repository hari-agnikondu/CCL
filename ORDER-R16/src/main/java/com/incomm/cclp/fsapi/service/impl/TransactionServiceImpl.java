package com.incomm.cclp.fsapi.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.CSSResponseCode;
import com.incomm.cclp.fsapi.bean.CardStatusBean;
import com.incomm.cclp.fsapi.bean.FsApiDetail;
import com.incomm.cclp.fsapi.bean.FsApiMaster;
import com.incomm.cclp.fsapi.bean.FsApiTransaction;
import com.incomm.cclp.fsapi.bean.FsApiValidationDetail;
import com.incomm.cclp.fsapi.constants.APIConstants;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.dao.TransactionDao;
import com.incomm.cclp.fsapi.service.TransactionService;
import com.incomm.cclp.util.Util;
import com.incomm.cclp.fsapi.service.impl.LocalCacheServiceImpl;

@Service
public class TransactionServiceImpl implements TransactionService {

	Logger logger = LogManager.getLogger(TransactionServiceImpl.class);

	@Autowired
	TransactionDao transactionDao;

	@Autowired
	LocalCacheServiceImpl localCacheService;

	@Override
	public Map<String, FsApiMaster> getFsapiMasterDetails() {

		logger.debug(CCLPConstants.ENTER);

		List<FsApiMaster> listOfFsapiMasterdetails = transactionDao.getAllFsapiMastDetails();

		logger.debug("Retrived fsapi details records : {}" + listOfFsapiMasterdetails);

		Map<String, FsApiMaster> mapFsapiDetails = new HashMap<>();

		if (!CollectionUtils.isEmpty(listOfFsapiMasterdetails)) {
			listOfFsapiMasterdetails.stream().forEach(fsapiDetails -> {
				String fsapiKey = (fsapiDetails.getApiName() + APIConstants.COLON + fsapiDetails.getReqMethod());
				mapFsapiDetails.put(fsapiKey, fsapiDetails);
			});
		}

		logger.debug("fsapi master table details records maped based on fsapi Key: {}", mapFsapiDetails);

		logger.debug(CCLPConstants.EXIT);

		return mapFsapiDetails;
	}
	@Override
	public CSSResponseCode getCSSResponseCodeByRespId(String channelCode, String respId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		CSSResponseCode respCode = null;
		Map<String, CSSResponseCode> mapCSSRespCode = null;

		mapCSSRespCode = localCacheService.getAllCSSResponseDetails(mapCSSRespCode);

		if (CollectionUtils.isEmpty(mapCSSRespCode)
				|| (!CollectionUtils.isEmpty(mapCSSRespCode) && !mapCSSRespCode.containsKey(channelCode+"_"+respId))) {
			mapCSSRespCode = getAllCSSResponseCodeDetails();
		}

		if (!CollectionUtils.isEmpty(mapCSSRespCode) && mapCSSRespCode.containsKey(channelCode+"_"+respId)) {
			
				respCode = mapCSSRespCode.get(channelCode+"_"+respId);

			
		}
		
		if (Objects.isNull(respCode)) {
			logger.error("Actual Response details missing for resp id :{} and channel code: {}", respId, channelCode);
			
			throw new ServiceException(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}

		return respCode;
	}
	@Override
	public FsApiMaster getFsapiMasterDetailByApiKey(String key) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		FsApiMaster fsapimasterdetails = null;
		Map<String, FsApiMaster> mapfsapimasterdetails = null;
		mapfsapimasterdetails = localCacheService.getFsapiMasterDetails(mapfsapimasterdetails);

		if (CollectionUtils.isEmpty(mapfsapimasterdetails)
				|| (!CollectionUtils.isEmpty(mapfsapimasterdetails) && !mapfsapimasterdetails.containsKey(key))) {
			logger.debug("Transaction Auth validations from LOCAL Cache is empty : {}", mapfsapimasterdetails);

			mapfsapimasterdetails = getFsapiMasterDetails();

			localCacheService.updateOrGetFsapiMasterDetails(mapfsapimasterdetails);
		}

		if (!CollectionUtils.isEmpty(mapfsapimasterdetails)) {
			logger.debug("Transaction Auth validations from LOCAL Cache : {}", mapfsapimasterdetails);

			if (mapfsapimasterdetails.containsKey(key)) {

				logger.debug("Transaction Auth validations for mapping key exist");

				fsapimasterdetails = mapfsapimasterdetails.get(key);

				logger.debug("Transaction Auth validations : {}", fsapimasterdetails);
			}
		}
		
		if (Objects.isNull(fsapimasterdetails)) {
			logger.error("FS API Maser details missing for Key: " + key);
			
			throw new ServiceException(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}

		logger.debug(CCLPConstants.EXIT);

		return fsapimasterdetails;
	}

	@Override
	public Map<String, FsApiDetail> getFsapiDetails() {
		logger.debug(CCLPConstants.ENTER);

		List<Map<String, Object>> listOfFsapidetail = transactionDao.getAllFsapiDetails();

		logger.debug("Retrived fsapi details records : {}" + listOfFsapidetail);

		Map<String, FsApiDetail> mapFsapiDetail = new HashMap<>();

		if (!CollectionUtils.isEmpty(listOfFsapidetail)) {
			listOfFsapidetail.stream().forEach(fsapiDetails -> {
				FsApiDetail fsapiDtls = new FsApiDetail();

				Map<String, String> reqMap = new HashMap<>();
				Map<String, String> resMap = new HashMap<>();
				Map<String, Map<String, String>> subResMap = new HashMap<>();
				Map<String, Map<String, String>> subReqMap = new HashMap<>();

				fsapiDtls.setApiName(String.valueOf(fsapiDetails.get("API_NAME")));
				fsapiDtls.setApiUrl(String.valueOf(fsapiDetails.get("API_URL")));
				fsapiDtls.setRequestMetod(String.valueOf(fsapiDetails.get("REQUEST_METHOD")));

				String reqTemp = String.valueOf(fsapiDetails.get("REQ_FIELDS"));
				if (!Util.isEmpty(reqTemp)) {
					String[] reqFiled = reqTemp.split("~");
					for (int i = 0; i < reqFiled.length; i++) {
						String[] valTemp = reqFiled[i].split(":");
						reqMap.put(valTemp[0], valTemp[1]);
					}
					if (!CollectionUtils.isEmpty(reqMap))
						fsapiDtls.setReqFields(reqMap);
				}

				String resTemp = String.valueOf(fsapiDetails.get("RES_FIELDS"));
				if (!Util.isEmpty(resTemp)) {
					String[] resFiled = resTemp.split("~");
					for (int i = 0; i < resFiled.length; i++) {
						String[] valTemp = resFiled[i].split(":");
						resMap.put(valTemp[0], valTemp[1]);
					}
					if (!CollectionUtils.isEmpty(resMap))
						fsapiDtls.setResFields(resMap);
				}

				String subResTemp = String.valueOf(fsapiDetails.get("SUBTAG_RESPFIELD"));
				if (!Util.isEmpty(subResTemp)) {
					String[] subResTempArr = subResTemp.split("~");
					for (int i = 0; i < subResTempArr.length; i++) {
						Map<String, String> subResMap1 = new HashMap<>();
						if (!Util.isEmpty(subResTempArr[i])) {
							String[] valTemps = subResTempArr[i].split(",");
							for (int j = 1; j < valTemps.length; j++) {
								String[] valTemp = valTemps[j].split(":");
								subResMap1.put(valTemp[0], valTemp[1]);

							}
							logger.info(" subResMap1   " + i + "  " + subResMap1);
							subResMap.put(valTemps[0], subResMap1);
						}
					}

					if (!CollectionUtils.isEmpty(subResMap))
						fsapiDtls.setResSubTagFields(subResMap);
				}

				String subReqTemp = String.valueOf(fsapiDetails.get("SUBTAG_REQFIELD"));
				if (!Util.isEmpty(subReqTemp)) {
					String[] subReqTempArr = subReqTemp.split("~");
					for (int i = 0; i < subReqTempArr.length; i++) {
						Map<String, String> subReqMap1 = new HashMap<>();
						if (!Util.isEmpty(subReqTempArr[i])) {
							String[] valTemps = subReqTempArr[i].split(",");
							for (int j = 1; j < valTemps.length; j++) {
								String[] valTemp = valTemps[j].split(":");
								subReqMap1.put(valTemp[0], valTemp[1]);
							}
							subReqMap.put(valTemps[0], subReqMap1);
						}
					}

					if (!CollectionUtils.isEmpty(subReqMap))
						fsapiDtls.setReqSubTagFields(subReqMap);
				}

				String fsapiKey = (fsapiDtls.getApiName() + APIConstants.COLON + fsapiDtls.getRequestMetod());
				mapFsapiDetail.put(fsapiKey, fsapiDtls);
			});
		}

		logger.debug("fsapi details from fspidetail table records maped based on fsapi Key: {}", mapFsapiDetail);

		logger.debug(CCLPConstants.EXIT);

		return mapFsapiDetail;
	}

	@Override
	public Map<String, Map<String, FsApiValidationDetail>> getFsapiValidationDetails() {
		logger.debug(CCLPConstants.ENTER);

		Map<String, Map<String, FsApiValidationDetail>> fsapiValidDtlsmap = new HashMap<>();

		List<Map<String, Object>> listOfFsapiValidationdetail = transactionDao.getAllFsapiValidationDetails();

		logger.debug("Retrived fsapi details records : {}" + listOfFsapiValidationdetail);

		if (!CollectionUtils.isEmpty(listOfFsapiValidationdetail)) {
			listOfFsapiValidationdetail.stream().forEach(fsapiDetails -> {
				
				FsApiValidationDetail fsapiValidationDtls = new FsApiValidationDetail();
				fsapiValidationDtls.setApiName(String.valueOf(fsapiDetails.get("API_NAME")));
				fsapiValidationDtls.setRequestMethod(String.valueOf(fsapiDetails.get("REQUEST_METHOD")));
				fsapiValidationDtls.setDataType(String.valueOf(fsapiDetails.get("DATA_TYPE")));
				fsapiValidationDtls.setFieldValues(String.valueOf(fsapiDetails.get("FIELD_VALUES")));
				fsapiValidationDtls.setFieldName(String.valueOf(fsapiDetails.get("FIELD_NAME")));
				fsapiValidationDtls.setParentTag(String.valueOf(fsapiDetails.get("PARENT_TAG")));
				fsapiValidationDtls.setRegexExpression(String.valueOf(fsapiDetails.get("REGEX_EXPRESSION")));
				fsapiValidationDtls.setValidationType(String.valueOf(fsapiDetails.get("VALIDATION_TYPE")));
				fsapiValidationDtls.setValidationErrMsg(String.valueOf(fsapiDetails.get("VALIDATION_ERRMSG")));
				fsapiValidationDtls.setRespCode(String.valueOf(fsapiDetails.get("RESPONSE_CODE")));
				fsapiValidationDtls.setSubTagField(String.valueOf(fsapiDetails.get("SUB_TAGFIELD")));
				String tempSub = String.valueOf(fsapiDetails.get("SUB_TAG"));

				if (!Util.isEmpty(tempSub)) {
					fsapiValidationDtls.setSubTagList(Arrays.asList(tempSub.split("\\s*,\\s*")));
				} else {
					fsapiValidationDtls.setSubTagList(new ArrayList<String>());
				}

				Map<String, FsApiValidationDetail> dtlMap = fsapiValidDtlsmap.get(
						fsapiValidationDtls.getApiName() + APIConstants.COLON + fsapiValidationDtls.getRequestMethod());
				if (!CollectionUtils.isEmpty(dtlMap)) {
					dtlMap.put(fsapiValidationDtls.getFieldName(), fsapiValidationDtls);
					fsapiValidDtlsmap.put(fsapiValidationDtls.getApiName() + APIConstants.COLON
							+ fsapiValidationDtls.getRequestMethod(), dtlMap);
				} else {
					dtlMap = new HashMap<>();
					dtlMap.put(fsapiValidationDtls.getFieldName(), fsapiValidationDtls);
					fsapiValidDtlsmap.put(fsapiValidationDtls.getApiName() + APIConstants.COLON
							+ fsapiValidationDtls.getRequestMethod(), dtlMap);
				}
			});
		}

		logger.debug("fsapi validation details  table records maped based on fsapi Key: {}", fsapiValidDtlsmap);

		logger.debug(CCLPConstants.EXIT);

		return fsapiValidDtlsmap;
	}

	@Override
	public Map<String, FsApiTransaction> getFsapiTransactionDetails() {
		logger.debug(CCLPConstants.ENTER);

		List<FsApiTransaction> listOfFsapiTransactioDetail = transactionDao.getAllFsapiTransactionDetails();

		logger.debug("Retrived fsapi transaction details records : {}" + listOfFsapiTransactioDetail);

		Map<String, FsApiTransaction> mapFsapiTransactionDetail = new HashMap<>();

		if (!CollectionUtils.isEmpty(listOfFsapiTransactioDetail)) {
			listOfFsapiTransactioDetail.stream().forEach(fsapiTranDetails -> {
				String actionName = Util.isEmpty(fsapiTranDetails.getActionName()) ? ""
						: fsapiTranDetails.getActionName();
				String fsapiKey = (fsapiTranDetails.getChannelDescritption() + APIConstants.COLON
						+ fsapiTranDetails.getRequestType() + APIConstants.COLON + fsapiTranDetails.getRequestMethod()
						+ APIConstants.COLON + actionName);
				mapFsapiTransactionDetail.put(fsapiKey.trim(), fsapiTranDetails);
			});
		}

		logger.debug("fsapi validation details  table records maped based on fsapi Key: {}", mapFsapiTransactionDetail);

		logger.debug(CCLPConstants.EXIT);

		return mapFsapiTransactionDetail;
	}

	@Override
	public FsApiDetail getFsapiDetailByApiKey(String key) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		FsApiDetail fsapidetails = null;
		Map<String, FsApiDetail> mapfsapidetails = null;
		mapfsapidetails = localCacheService.getFsapiDetails(mapfsapidetails);

		if (CollectionUtils.isEmpty(mapfsapidetails)
				|| (!CollectionUtils.isEmpty(mapfsapidetails) && !mapfsapidetails.containsKey(key))) {
			logger.debug("mapfsapidetails from LOCAL Cache is empty : {}", mapfsapidetails);

			mapfsapidetails = getFsapiDetails();

			localCacheService.updateOrGetFsapiDetails(mapfsapidetails);
		}

		if (!CollectionUtils.isEmpty(mapfsapidetails)) {
			logger.debug("mapfsapidetails from LOCAL Cache : {}", mapfsapidetails);

			if (mapfsapidetails.containsKey(key)) {

				logger.debug("Transaction Auth validations for mapping key exist");

				// added by nawaz 18062018
				final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
				fsapidetails = mapper.convertValue(mapfsapidetails.get(key), FsApiDetail.class);

				logger.debug("Transaction Auth validations : {}", fsapidetails);
			}
		}
		
		if (Objects.isNull(fsapidetails)) {
			logger.error("FS API Details data missing for Key: " + key);
			
			throw new ServiceException(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}

		logger.debug(CCLPConstants.EXIT);

		return fsapidetails;
	}

	@Override
	public Map<String, FsApiValidationDetail> getFsapiValidationDetailByApiKey(String key) {
		logger.debug(CCLPConstants.ENTER);

		Map<String, FsApiValidationDetail> mapfsapiValidationDtls = null;

		Map<String, Map<String, FsApiValidationDetail>> mapfsapiValidationdetails = null;
		mapfsapiValidationdetails = localCacheService.getFsapiValidationDetails(mapfsapiValidationdetails);

		if (CollectionUtils.isEmpty(mapfsapiValidationdetails)) {
			logger.debug("mapfsapiValidationdetails from LOCAL Cache is empty : {}", mapfsapiValidationdetails);

			mapfsapiValidationdetails = getFsapiValidationDetails();

			localCacheService.updateOrGetFsapiValidationDetails(mapfsapiValidationdetails);
		}

		if (!CollectionUtils.isEmpty(mapfsapiValidationdetails)) {
			logger.debug("mapfsapidetails from LOCAL Cache : {}", mapfsapiValidationdetails);

			if (mapfsapiValidationdetails.containsKey(key)) {

				logger.debug("mapfsapidetails for mapping key exist");

				mapfsapiValidationDtls = mapfsapiValidationdetails.get(key);

				logger.debug("mapfsapidetails : {}", mapfsapiValidationDtls);
			}
		}

		logger.debug(CCLPConstants.EXIT);

		return mapfsapiValidationDtls;
	}
	
	@Override
	public Map<String, CSSResponseCode> getAllCSSResponseCodeDetails() {
		logger.debug(CCLPConstants.ENTER);

		List<CSSResponseCode> cssRespCodes = transactionDao.getCssResponseCodes();

		Map<String, CSSResponseCode> mapCSSRespCode = new HashMap<>();

		// Needs to discuss where key will be channel_code-internal resp code
		if (!CollectionUtils.isEmpty(cssRespCodes)) {
			cssRespCodes.stream().forEach(respCode -> 
				// key needs to define
				mapCSSRespCode.put(respCode.getChannelCode() + "_" + respCode.getResponseCode(), respCode)
			);

		}

		logger.debug(CCLPConstants.EXIT);

		return mapCSSRespCode;
	}

	@Override
	public FsApiTransaction getFsapiTransactionDetailByApiKey(String key) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		FsApiTransaction fsapiTransaction = new FsApiTransaction();
		Map<String, FsApiTransaction> mapfsapiTransaction = null;
		mapfsapiTransaction = localCacheService.getFsapiTransactionDetails(mapfsapiTransaction);

		if (CollectionUtils.isEmpty(mapfsapiTransaction)|| (!CollectionUtils.isEmpty(mapfsapiTransaction) && !mapfsapiTransaction.containsKey(key))) {
			logger.debug("fsapiTransaction from LOCAL Cache is empty : {}", mapfsapiTransaction);

			mapfsapiTransaction = getFsapiTransactionDetails();

			localCacheService.updateOrGetFsapiTransactionDetails(mapfsapiTransaction);
		}

		if (!CollectionUtils.isEmpty(mapfsapiTransaction)) {
			logger.debug("fsapiTransaction from LOCAL Cache : {}", mapfsapiTransaction);

			if (mapfsapiTransaction.containsKey(key)) {

				logger.debug("fsapiTransaction for mapping key exist");

				fsapiTransaction = mapfsapiTransaction.get(key);

				logger.debug("fsapiTransaction : {}", fsapiTransaction);
			}
		}
		
		if (Objects.isNull(fsapiTransaction)) {
			logger.error("FS API Transaction data missing for Key: " + key);
			
			throw new ServiceException(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}

		logger.debug(CCLPConstants.EXIT);

		return fsapiTransaction;
	}

	@Override
	public Map<String, String> getAllCardStatus() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		Map<String, String> cardStatusDefs = new HashMap<>();

		try {
			List<CardStatusBean> listCardStatusDef = transactionDao.getAllCardStatus();
			if (!CollectionUtils.isEmpty(listCardStatusDef)) {
				listCardStatusDef.stream().forEach(cardStatus -> 
					cardStatusDefs.put(cardStatus.getStatusCode(), cardStatus.getStatusDesc())
				);
			}
		} catch (Exception e) {
			logger.error("Exception occured while get card status definitions: " + e.getMessage());
			throw new ServiceException(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}

		logger.debug(CCLPConstants.EXIT);

		return cardStatusDefs;
	}



}
