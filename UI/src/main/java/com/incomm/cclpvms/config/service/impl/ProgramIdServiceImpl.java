package com.incomm.cclpvms.config.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.incomm.cclpvms.config.model.ProgramID;
import com.incomm.cclpvms.config.model.ProgramIDDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.ProgramIdService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class ProgramIdServiceImpl implements ProgramIdService {
	private static final Logger logger = LogManager.getLogger(ProgramIdService.class.getName());

	@Autowired
	RestTemplate restTemplate;

	@Value("${CONFIG_BASE_URL}") String configBaseUrl;

	
	
	@Override
	public List<ProgramIDDTO> getAllProgramIds() throws ServiceException {
		ResponseDTO responseBody = null;
		List<ProgramIDDTO> programIDDTO = null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl +"/programid",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call : " + responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				throw new ServiceException(responseBody.getResult());
			}
			programIDDTO = (List<ProgramIDDTO>) responseBody.getData();

		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllprogramIDDTO()");
			throw new ServiceException("Failed to process.. Please try again later.");
		}

		return programIDDTO;
	}



	@Override
	public List<ProgramIDDTO> getAllProgramIdsByName(String programIdName) throws ServiceException {
		
		ResponseDTO responseBody = null;
		List<ProgramIDDTO> programIDDTO = null;
		try {

			UriComponentsBuilder builder = UriComponentsBuilder
					.fromUriString(configBaseUrl + "/programid/search")
					.queryParam("programIDName", programIdName);

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call : " + responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				throw new ServiceException(responseBody.getResult());
			}
			programIDDTO = (List<ProgramIDDTO>) responseBody.getData();

		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllprogramIDByName()");
			throw new ServiceException("Failed to process... Please try again later.");
		}

		return programIDDTO;
	}



	@Override
	public ProgramID getProgramId(Long programId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		String tempurl = "";
		tempurl = "programid/" + programId;
		String idSearchUrl = tempurl;
		ResponseDTO responseBody = null;
		ProgramID programID = null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + idSearchUrl,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			Map<String, Object> merchantobj = null;
			merchantobj = (Map<String, Object>) responseBody.getData();
		
			String programIDName = (String) merchantobj.get("programIDName");
			
			Long partnerID = Long.parseLong( ( merchantobj.get("partnerId").toString()));
			String partnerName = (String) merchantobj.get("partnerName");
			String description = (String) merchantobj.get("description");
			programID = new ProgramID( programId, programIDName, partnerID,partnerName,description);

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getMerchantById()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return programID;
	}



	@Override
	public ResponseDTO createProgramID(ProgramIDDTO programIDDTO) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(configBaseUrl + "/programid/", programIDDTO,
					ResponseDTO.class);
			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.info("status of CreateIssuer " + httpStatus);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in createIssuer()"+e);
			throw new ServiceException("Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}



	@Override
	public ResponseDTO udpateProgramId(ProgramID programIdForm) throws ServiceException {
		ResponseDTO responseBody = null;
		logger.debug("ENTER");
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(configBaseUrl + "/programid/",
					HttpMethod.PUT, new HttpEntity<ProgramIDDTO>(constructObj(programIdForm)), ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException ex) {
			logger.error("Error while creating update Package, {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug("RETURN");
		return responseBody;
	}
	
	private ProgramIDDTO constructObj(ProgramID programIdForm){
		ProgramIDDTO programIDDTO = new ProgramIDDTO();
		programIDDTO.setProgramID(programIdForm.getProgramID());
		programIDDTO.setProgramIDName(programIdForm.getProgramIDName());
		programIDDTO.setDescription(programIdForm.getDescription());
		programIDDTO.setInsDate(new Date());
		programIDDTO.setInsUser(programIdForm.getInsUser());
		programIDDTO.setLastUpdUser(programIdForm.getLastUpdUser());
		programIDDTO.setLastUpdDate(new Date());
		logger.debug("programIDDTO:" + programIDDTO.toString());
		return programIDDTO;
	}



	@Override
	public ResponseEntity<ResponseDTO> deleteProgramIddetails(ProgramID programIdForm) throws ServiceException {
		ResponseEntity<ResponseDTO> responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			 responseDTO = restTemplate.exchange(configBaseUrl+"programid/"+programIdForm.getProgramID(),
					HttpMethod.DELETE,null, ResponseDTO.class);
		}catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in deletePurseDetails()"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return responseDTO;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramIDDTO> getProgramIdsByPartnerId(Long partnerId) throws ServiceException {
		
		logger.debug(CCLPConstants.ENTER);
		List<ProgramIDDTO>	programIDDTO= new ArrayList<>();
		
		try {
			String programIdsByPartnerIdURL ="/getProgramIdDtlsByPartnerId/";
			logger.debug("Calling '{}' service to search Card Range '{}'",configBaseUrl,partnerId);
			
			ResponseDTO responseDTO=restTemplate.getForObject(configBaseUrl +"programid"+ programIdsByPartnerIdURL + partnerId, ResponseDTO.class);
			if(responseDTO!=null && responseDTO.getData()!=null){
				programIDDTO = (List<ProgramIDDTO>) responseDTO.getData();
				
			}

		} 
		catch (RestClientException e) {
			logger.error("RestClientException in getCardRangeById:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return programIDDTO;
		
	}
	
}

