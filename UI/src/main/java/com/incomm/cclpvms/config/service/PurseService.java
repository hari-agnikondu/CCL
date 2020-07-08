package com.incomm.cclpvms.config.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.incomm.cclpvms.config.model.Purse;
import com.incomm.cclpvms.config.model.PurseDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;


public interface PurseService {

	public List<PurseDTO> getAllPurses() throws ServiceException ;
	
	public List<Purse> getPurses(Purse purse) throws ServiceException ;
	
	public Purse getPurseById(Long purseId) throws ServiceException ;
	
	public ResponseEntity<ResponseDTO> updatePurseDetails(Purse purse) throws ServiceException ;
	
	public ResponseEntity<ResponseDTO> savePurseDetails(Purse purse) throws ServiceException;
	
	public Map<Long,String> getPurseTypeList() throws ServiceException ;
	
	public Map<String,String> getCurrencyCodeList() throws ServiceException ;
	
	public ResponseEntity<ResponseDTO> deletePurseDetails(Purse purse) throws ServiceException;
	
		public List<String> getPurseByIds(List<Long> purseId)throws ServiceException;	

}
