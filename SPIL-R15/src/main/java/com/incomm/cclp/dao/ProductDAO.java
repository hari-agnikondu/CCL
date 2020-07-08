package com.incomm.cclp.dao;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.account.domain.model.ProductPurse;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.exception.ServiceException;

public interface ProductDAO {

	String getProductAttributesByProductId(String productId);

	int checkProductByProductId(String productId);

	int checkRuleSetById(String ruleSetId);

	List<Map<String, Object>> getRuleSetById(String ruleSetId);

	List<Map<String, Object>> getRuleDetailsById(int ruleId);

	String getTransactionIdentifier(String deliveryChannelCode, String transCode, String msgType);

	int getProductRuleSet(String productId);

	int checkProductValidity(String productId);

	void updateCardStatus(String cardNumHash, String cardStatus) throws ServiceException;

	String getPartnerDetails(String productId, String partnerName);

	String getProductDefaultCurr(String purseId);

	String getMdmId(String partnerId);

	PurseDTO getpurseDto(String purseName);

	List<ProductPurse> getPursesByProductId(long productId);

}
