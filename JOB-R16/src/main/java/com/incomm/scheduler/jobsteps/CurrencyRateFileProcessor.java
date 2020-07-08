package com.incomm.scheduler.jobsteps;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.constants.FSAPIConstants;
import com.incomm.scheduler.dao.CurrencyRateUploadDAO;


@Component
public class CurrencyRateFileProcessor implements ItemProcessor<String,String>
{
	private static final Logger logger = LogManager.getLogger(CurrencyRateFileProcessor.class);
	
	
	@Autowired
	CurrencyRateUploadDAO currencyRateUploadDao;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${SPIL_BASE_URL}")
	private String spilUrl;

	long count = 0;
	
	@Override
	public String process(String batchIdFileName) throws Exception {
		
		logger.info(CCLPConstants.ENTER);
		logger.info("Processor input: "+batchIdFileName);
		JSONArray addJsonArray = new JSONArray();
		JSONArray deleteJsonArray = new JSONArray();
		ObjectMapper objectMapper = new ObjectMapper();
		String attributesJsonResp = null;
		String[] batchIdFile=batchIdFileName.split(":");
		String batchId = batchIdFile[0];
		String fileMdmId = batchIdFile[1].split("_")[0];
		
		try {
		List<Map<String,Object>> recordList = currencyRateUploadDao.getRecordsToProcess(batchId);
		logger.info("Batch Ids: "+batchId);
		
		attributesJsonResp = objectMapper.writeValueAsString(recordList);
		
		JSONArray jsonArray = new JSONArray(attributesJsonResp);
		logger.debug("ADD Json from db: "+attributesJsonResp);
		int length = jsonArray.length();
		
		for(int i=0; i< length;i++) {
			boolean flag = false;
			String errorMsg = "";
			JSONObject jsonRecord = jsonArray.getJSONObject(i);
			String recMdmId = String.valueOf(jsonRecord.get("MDMID"));
			String txnCurrency = String.valueOf(jsonRecord.get("transactionCurrency"));
			String issuingCurrency = String.valueOf(jsonRecord.get("issuingCurrency"));
			double convRate = Double.parseDouble(String.valueOf(jsonRecord.get("exchangeRate")));
			String recordNum = String.valueOf(jsonRecord.get("recordNumber"));
			String action = String.valueOf(jsonRecord.get("ACTION"));
			jsonRecord.remove("ACTION");
			jsonRecord.remove("MDMID");
			if(!recMdmId.equals(fileMdmId) ) {
				flag = true;
				errorMsg="Record mdm id not matching with file name mdmId";
			}
			else if(currencyRateUploadDao.checkCurrencyCode(txnCurrency) < 1) {
				flag = true;
				errorMsg="Invalid Transaction Currency";
			}else if(currencyRateUploadDao.checkCurrencyCode(issuingCurrency) < 1) {
				flag = true;
				errorMsg="Invalid issuing Currency";
			}else if(issuingCurrency.equalsIgnoreCase(txnCurrency)) {
				flag = true;
				errorMsg="Issuing Currency and Txn Currency should not be same";
			}
			else if(convRate < 0.01 || convRate > 100000) {
				flag = true;
				errorMsg="Invalid Currency conversion Rate";
				
			}else {
				if(action.equalsIgnoreCase("add")) {
					addJsonArray.put(jsonRecord);
				}else if(action.equalsIgnoreCase("Delete")) {
					deleteJsonArray.put(jsonRecord);
				}else {
					flag = true;
					errorMsg="Invalid Action";
				}
				
			}
			
			if(flag) {
				currencyRateUploadDao.updateResponse("E",errorMsg,batchId,recordNum);
				
			}else {
				logger.info("Field level validation success");
			}
			
		}
    
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(FSAPIConstants.CURRENCY_RATE_HEADER_CHANNEL, FSAPIConstants.CURRENCY_RATE_HEADER_VAL_CHANNEL);
		headers.add(FSAPIConstants.CURRENCY_RATE_HEADER_USER, FSAPIConstants.CURRENCY_RATE_HEADER_VAL_USER);
		
		logger.info("Final ADD req to spil :"+addJsonArray.toString());
		if(addJsonArray.length()!=0) {
			JSONObject finalObj = new JSONObject();
			finalObj.put(FSAPIConstants.CURRENCY_CONV_ARRAY_NAME, addJsonArray);
			
			HttpEntity<String> requestEntity = new HttpEntity<>(finalObj.toString(), headers);
			String addResponse =restTemplate.postForObject(spilUrl+"/currencyConversion/{mdmId}", requestEntity, String.class,fileMdmId);
			JSONObject resp = new JSONObject(addResponse);
			if(!resp.get(FSAPIConstants.ORDER_RESPONSE_CODE).equals("0")) {
				currencyRateUploadDao.updateResponseByAction("E",String.valueOf(resp.get("responseMessage")),batchId,"Add");
			}else {
				if(resp.has("currencyConversion")) {
				JSONArray respArray = (JSONArray) resp.get("currencyConversion");
				for(int i=0;i<respArray.length();i++) {
					JSONObject indvResp = respArray.getJSONObject(i);
					if(indvResp.get("responseCode").equals("0")) {
						currencyRateUploadDao.updateResponse("Y",String.valueOf(indvResp.get("responseMessage")),batchId,String.valueOf(indvResp.get("recordNumber")));
					}else {
						currencyRateUploadDao.updateResponse("E",String.valueOf(indvResp.get("responseMessage")),batchId,String.valueOf(indvResp.get("recordNumber")));
					}
				}
			}
			}
			
			logger.info("Add request response for MdmId: {} and response: {}",fileMdmId,addResponse);
		}
		
		logger.info("Final DELETE req to spil :"+deleteJsonArray.toString());
		if(deleteJsonArray.length()!=0) {
			JSONObject finalObj = new JSONObject();
			finalObj.put("currencyConversion", deleteJsonArray);
			HttpEntity<String> requestEntity = new HttpEntity<>(finalObj.toString(), headers);
			ResponseEntity<String> deleteResponse = restTemplate.exchange(spilUrl+"/currencyConversion/{mdmId}", HttpMethod.DELETE,requestEntity, String.class,fileMdmId);
			String deleteResp = deleteResponse.getBody();
			JSONObject resp = new JSONObject(deleteResp);
			if(!resp.get("responseCode").equals("0")) {
				currencyRateUploadDao.updateResponseByAction("E",String.valueOf(resp.get("responseMessage")),batchId,"Delete");
			}else {
				if(resp.has("currencyConversion")) {
				JSONArray respArray = (JSONArray) resp.get("currencyConversion");
				
				for(int i=0;i<respArray.length();i++) {
					JSONObject indvResp = respArray.getJSONObject(i);
					if(indvResp.get("responseCode").equals("0")) {
						currencyRateUploadDao.updateResponse("Y",String.valueOf(indvResp.get("responseMessage")),batchId,String.valueOf(indvResp.get("recordNumber")));
					}else {
						currencyRateUploadDao.updateResponse("E",String.valueOf(indvResp.get("responseMessage")),batchId,String.valueOf(indvResp.get("recordNumber")));
					}
				}
				}
			}
			
			logger.info("Delete request response for MdmId: {} and response: {}",fileMdmId,deleteResp);
		}
		}catch(Exception e) {
			logger.error("Exception in file processing"+e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return null;
	}

	

}
