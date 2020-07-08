package com.incomm.scheduler.jobsteps;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.scheduler.dao.BulkTransactionDAO;
import com.incomm.scheduler.dto.ResponseDTO;
import com.incomm.scheduler.model.BulkTransactionRequestFile;
import com.incomm.scheduler.model.BulkTransactionResponseFile;

@Component
public class BulkTransactionFileProcessor implements ItemProcessor<BulkTransactionRequestFile, BulkTransactionResponseFile>
{
	private static final Logger logger = LogManager.getLogger(BulkTransactionFileProcessor.class);
	
	
	@Autowired
	BulkTransactionDAO bulkTransactiondao;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${SPIL_BATCH_URL}")
	private String spilUrl;

	private Set<String> seenRec = new HashSet<String>();

	
	@Override
	public BulkTransactionResponseFile process(BulkTransactionRequestFile items) throws Exception {
		
		long time = System.currentTimeMillis();
		BulkTransactionResponseFile response = null;
		ObjectMapper om = new ObjectMapper();
		try {
		
		String request  = om.writeValueAsString(items);
		
		logger.info("Request: "+items.toString());
		
		String batchIdRef = items.getBatchId()+items.getSourceReferenceNumber();
		
		if(seenRec.contains(batchIdRef)){logger.info("batchIdRef: "+batchIdRef);
			BulkTransactionResponseFile responseTemp = new BulkTransactionResponseFile();
			responseTemp.setBatchId(items.getBatchId());
			responseTemp.setSourceReferenceNumber(items.getSourceReferenceNumber());
			responseTemp.setRecordNum(items.getRecordNum());
			responseTemp.setSpNumber(items.getSpNumber());
			responseTemp.setAmount(items.getAmount());
			responseTemp.setMdmId(items.getMdmId());
			responseTemp.setStoreId(items.getStoreId());
			responseTemp.setTerminalId(items.getTerminalId());
			responseTemp.setAvailableBalance("0");
			responseTemp.setResponseCode("10085");
			responseTemp.setResponseMessage("Duplicate RRN/Request");
			return responseTemp;
		}else {
			seenRec.add(batchIdRef);
		}
		
		ResponseDTO responseDto =restTemplate.postForObject(spilUrl+"/batchProcess", request, ResponseDTO.class);
			
		Object respObj = responseDto.getData();
		
		if(respObj != null) {
			response = om.convertValue(respObj, BulkTransactionResponseFile.class);
			response.setBatchId(items.getBatchId());
		}
		logger.info("Response: "+response.getFileRecode());
		}catch(RestClientException re) {
			logger.error("Exception while fetching records {}",re);
		}catch(Exception e) {
			logger.error("Exception while fetching records {}",e);
			BulkTransactionResponseFile responseExc = new BulkTransactionResponseFile();
			responseExc.setBatchId(items.getBatchId());
			responseExc.setSourceReferenceNumber(items.getSourceReferenceNumber());
			responseExc.setRecordNum(items.getRecordNum());
			responseExc.setSpNumber(items.getSpNumber());
			responseExc.setAmount(items.getAmount());
			responseExc.setMdmId(items.getMdmId());
			responseExc.setStoreId(items.getStoreId());
			responseExc.setTerminalId(items.getTerminalId());
			responseExc.setAvailableBalance("0");
			responseExc.setResponseCode("10029");
			responseExc.setResponseMessage("System Error");
			return responseExc;
		}
		time = System.currentTimeMillis() - time;
		logger.info("Time taken for process: "+time);
		
		return response;
	}

	

}
