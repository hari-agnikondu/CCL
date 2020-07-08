package com.incomm.cclpvms.config.service;

import java.util.List;

import com.incomm.cclpvms.config.model.CurrencyCode;
import com.incomm.cclpvms.config.model.CurrencyCodeDTO;
import com.incomm.cclpvms.config.model.Partner;
import com.incomm.cclpvms.config.model.PurseDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface PartnerService {

	public ResponseDTO createPartner(Partner partner) throws ServiceException;

	public List<Partner> getAllPartners() throws ServiceException;

	public ResponseDTO updatePartner(Partner partner) throws ServiceException;

	public ResponseDTO deletePartner(long partnerId);

	public List<Partner> getPartnersByName(String partnerName);

	public Partner getPartnerByPartnerId(long partnerId) throws ServiceException;
	
	public List<CurrencyCodeDTO> getCurrencyCodes() throws ServiceException;
	public List<PurseDTO> getPurses() throws ServiceException;

	public List<PurseDTO> getAllSupportedPurses(Long partnerId) throws ServiceException;
	
	public List<PurseDTO> getProgramIDPartnerPurses(long programId) throws ServiceException;
		
}
