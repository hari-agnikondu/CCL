package com.incomm.cclpvms.config.service;

import java.util.List;

import com.incomm.cclpvms.config.model.Issuer;
import com.incomm.cclpvms.config.model.IssuerDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.IssuerException;
import com.incomm.cclpvms.exception.ServiceException;



public interface IssuerService {
	
	public ResponseDTO createIssuer(IssuerDTO issuerdto)  throws ServiceException;
	
	public List<IssuerDTO> getAllIssuers() throws ServiceException, IssuerException;
	
	public ResponseDTO updateIssuer(IssuerDTO issuerdto) throws ServiceException;
	
	public ResponseDTO deleteIssuer(long issuerId) throws ServiceException;
	
	public Issuer getIssuerById(long issuerId) throws ServiceException;
	
	public List<IssuerDTO> getIssuersByName(String issuerName) throws ServiceException;
	
	public ResponseDTO getCardRangeCount(long issuerId) throws ServiceException ;
}
