package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.exception.ServiceException;

public interface PartnerService {

	public void createPartner(PartnerDTO partnerDto) throws ServiceException;

	public List<PartnerDTO> getAllPartners();

	public void updatePartner(PartnerDTO partnerDto) throws ServiceException;

	public void deletePartner(long partnerId) throws ServiceException;

	public PartnerDTO getPartnerById(Long partnerId);

	public List<PartnerDTO> getPartnerByName(String partnerName);

	public List<PurseDTO> getAllSupportedPursesByPartnerId(Long partnerId);

}
