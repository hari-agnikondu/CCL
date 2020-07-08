package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.MerchantRedemptionDAO;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.dao.RedemptionDelayDAO;
import com.incomm.cclp.domain.MerchantRedemption;
import com.incomm.cclp.domain.Product;
import com.incomm.cclp.domain.RedemptionDelay;
import com.incomm.cclp.dto.RedemptionDelayDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RedemptionDelayService;

@Service
public class RedemptionDelayServiceImpl implements RedemptionDelayService{
	
	@Autowired
	RedemptionDelayDAO redemptionDelayDAO;
	
	@Autowired
	MerchantRedemptionDAO merchantRedemptionDAO;
	
	@Autowired
	ProductDAO productDAO;
	
	@Autowired
	DistributedCacheServiceImpl distributedCacheService;
	
	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	@Transactional
	public void createRedemptionDelay(RedemptionDelayDTO redemptionDelayDTO) throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		logger.info(redemptionDelayDTO.toString());

		Long productId = redemptionDelayDTO.getProductId();
		String merchantId = redemptionDelayDTO.getMerchantId();

		MerchantRedemption merchant = merchantRedemptionDAO.getMerchantById(merchantId);
		if (Objects.isNull(merchant)) {

			logger.error("Merchant record with id '{}' does not exist", merchantId);
			throw new ServiceException(("MER_" + ResponseMessages.DOESNOT_EXISTS), ResponseMessages.DOESNOT_EXISTS);
		}

		Product product = productDAO.getProductById(productId);
		if (Objects.isNull(product)) {

			logger.error("Product record with id '{}' does not exist", productId);
			throw new ServiceException(("PRODUCT_" + ResponseMessages.DOESNOT_EXISTS), ResponseMessages.DOESNOT_EXISTS);
		}

		List<Object[]> redemDelay = redemptionDelayDAO.getMerchantProductById(productId, merchantId);
		if (!CollectionUtils.isEmpty(redemDelay)) {
			try {
				redemptionDelayDAO.deleteRedemptionDelay(productId, merchantId);
			} catch (Exception e) {
				logger.error("Error while removing Merchant Product mapping recored : {}", e.getMessage());
				throw new ServiceException(ResponseMessages.FAIL_REDEMPTION_DELAY_DELETE,
						ResponseMessages.FAIL_REDEMPTION_DELAY_DELETE);
			}
		}

		if (CollectionUtils.isEmpty(redemptionDelayDTO.getOperationsList())) {
			logger.error("Time not configured");
			throw new ServiceException(ResponseMessages.TIME_NOT_CONFIGURED);
		} else {
			insertRedemptionDelayData(merchant, product, redemptionDelayDTO);
		}
		logger.info(CCLPConstants.EXIT);

	}
	
	
	private void insertRedemptionDelayData(MerchantRedemption merchant, Product product, RedemptionDelayDTO redemptionDelayDTO) {
		logger.info(CCLPConstants.ENTER);
		String startTimeDisplay = "";
		String endTimeDisplay="";
		Long redemptionDelayTime = null;
		List<String> operationsList = redemptionDelayDTO.getOperationsList();
		
		for(int i=0;i<operationsList.size();i++) {
			RedemptionDelay redemptionDelay= new RedemptionDelay();
			redemptionDelay.setProduct(product);
			redemptionDelay.setMerchant(merchant);		
			
			String[] val=operationsList.get(i).split("-");
			startTimeDisplay = val[0];
			endTimeDisplay = val[1];
			redemptionDelayTime = Long.parseLong(val[2]);
			
			redemptionDelay.setInsDate(new Date());
			redemptionDelay.setLastUpdDate(new Date());
			redemptionDelay.setInsUser(redemptionDelayDTO.getInsUser());
			redemptionDelay.setLastUpdUser(redemptionDelayDTO.getLastUpdUser());
			redemptionDelay.setStartTimeDisplay(startTimeDisplay);
			redemptionDelay.setEndTimeDisplay(endTimeDisplay);
			redemptionDelay.setRedemptionDelayTime(redemptionDelayTime);
	
		
			redemptionDelayDAO.createRedemptionDelay(redemptionDelay);
			
			/*update the redemption delay configuration into hazel cast*/
			enableConfigInHazelCacheByProducId(product.getProductId());
		
			logger.info(CCLPConstants.EXIT);
		}
	
	}

	@Override
	public String getOverlapRedemptionDetails(String previousValue, String currentValue) {
		logger.info(CCLPConstants.ENTER);
		String overLapResult = "";
		overLapResult = redemptionDelayDAO.getOverLapDetails(previousValue,currentValue);
		logger.info(CCLPConstants.EXIT);
		return overLapResult;
		
	}

	
	public List<Object> getRedeemMerchantProductIdData(Long productId, String merchantId) {
		
		return (redemptionDelayDAO.getMerchantProductByIdData(productId, merchantId));
	}
	

	@Override
	public List<RedemptionDelayDTO> getRedeemMerchantProductIddetails(Long productId, String merchantId) {
	
		return redemptionDelayDTOs(redemptionDelayDAO.getMerchantProductById(productId, merchantId));
	}
	
	public List<RedemptionDelayDTO> redemptionDelayDTOs(List<Object[]> redemptionList) {
		logger.info(CCLPConstants.ENTER);
		List<RedemptionDelayDTO> redemptionDtoList = new ArrayList<>();
		if (redemptionList != null) {

			for (Iterator<Object[]> iterator = redemptionList.iterator(); iterator.hasNext();) {
				Object[] objects = iterator.next();
				RedemptionDelayDTO delayDTO = new RedemptionDelayDTO();
				delayDTO.setStartTimeDisplay(objects[0] + "");
				delayDTO.setEndTimeDisplay(objects[1] + "");
				delayDTO.setRedemptionDelayTime(objects[2] != null ? Long.valueOf(objects[2] + "") : null);
				redemptionDtoList.add(delayDTO);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return redemptionDtoList;
	}
	
	@Override
	public Map<String, List<Object>> getRedeemMerchantsByProductId(Long productId) {
		logger.info(CCLPConstants.ENTER);
		Map<String, List<Object>> redemptionDealyMap=new HashMap<>();
		try{
		List<Object[]> redemptionList =redemptionDelayDAO.getMerchantsbyProductId(productId);
		
		
		if (redemptionList != null && !CollectionUtils.isEmpty(redemptionList)) {
			for (Iterator<Object[]> iterator = redemptionList.iterator(); iterator.hasNext();) {
				Object[] objects = iterator.next();
				/*delayMerchant.setProductId(objects[1] + "");
				delayMerchant.setMerchantName(objects[2] + "");
				delayMerchant.setMerchantId(objects[3] + "");
				delayMerchant.setStartTime(objects[4] + "");
				delayMerchant.setEndTime(objects[5] + "");
				delayMerchant.setDelayMins(objects[6] != null ?objects[2] +""  : null);*/
				if(redemptionDealyMap.get(objects[0])==null  ){
					redemptionDealyMap.put(objects[0]+"", new ArrayList<>());
					redemptionDealyMap.get(objects[0]+"").add(objects);
				}else{
					redemptionDealyMap.get(objects[0]+"").add(objects);
					
				}
				
			}
		}
		
		}catch(Exception e){
			logger.info("Exception while invoke process of enableConfigInHazelCacheByProducId  with productid:"+productId);
		}
		logger.info(CCLPConstants.EXIT);
		return redemptionDealyMap;
	}


	@Override
	public Map<String, Map<String, Object>> enableConfigInHazelCacheByProducId(Long productId) {
		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String, Object>>  attributeMap=null;
		try{

			attributeMap=distributedCacheService.getOrAddProductAttributesCache(productId, null);
			if(!CollectionUtils.isEmpty(attributeMap)){

				if(attributeMap.containsKey("Product")) {
					Map<String,Object>prodAtribs=attributeMap.get("Product");
					prodAtribs.put(CCLPConstants.REDEMPTION_DELAY,getRedeemMerchantsByProductId(productId));
					attributeMap.put("Product",prodAtribs);
					distributedCacheService.updateProductAttributesCache(productId, attributeMap);
				}
			}

		}catch(Exception e){
			logger.info("Exception while invoke process of enableConfigInHazelCacheByProducId  with productid:"+productId);
		}
		logger.info(CCLPConstants.EXIT);
		return attributeMap;
	}
	
	

	

}
