package com.incomm.scheduler.dao;

import java.util.List;
import java.util.Optional;

import com.incomm.scheduler.model.PartnerProductInfo;

public interface PartnerDAO {

	public Optional<PartnerProductInfo> getPartnerProductInfo(long partnerId, long productId, String purseName);
	
	public List<PartnerProductInfo> getProductPurseInfo();

	void updatePurseAttributes(String attributes, long productId, long purseId);

}
