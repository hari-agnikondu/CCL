package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.RedemptionDelayDAO;
import com.incomm.cclp.domain.RedemptionDelay;



@Repository
public class RedemptionDelayDAOImpl implements RedemptionDelayDAO{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public void createRedemptionDelay(RedemptionDelay redemptionDelay) {
		
		em.createNativeQuery(QueryConstants.ADD_REDEMPTION_DELAY)
		.setParameter(1, redemptionDelay.getPrimaryKey().getProduct().getProductId())
		.setParameter(2, redemptionDelay.getPrimaryKey().getMerchant().getMerchantId())
		.setParameter(3, redemptionDelay.getStartTimeDisplay())
		.setParameter(4, redemptionDelay.getEndTimeDisplay())
		.setParameter(5, redemptionDelay.getRedemptionDelayTime())
		.setParameter(6, redemptionDelay.getInsUser())
		.setParameter(7, redemptionDelay.getInsDate())
		.setParameter(8, redemptionDelay.getLastUpdUser())
		.setParameter(9, redemptionDelay.getLastUpdDate())
		
		.executeUpdate();
	}

	@Override
	public void deleteRedemptionDelay(Long productId, String merchantId) {

		em.createQuery("delete from RedemptionDelay redem where redem.primaryKey.product.productId =:productId and"
				+ " redem.primaryKey.merchant.merchantId =:merchantId")
		.setParameter(CCLPConstants.PRODUCT_ID, productId).setParameter(CCLPConstants.MERCHANT_ID, merchantId)
		.executeUpdate();
	}



	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String getOverLapDetails(String previousValue, String currentValue) {
		String overlap="select redemptiondelay.fn_check_overlaps('"+previousValue+"','"+currentValue+"') from dual";

		List<String> data = em.createNativeQuery(overlap).getResultList();
		return data.get(0);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getMerchantProductById(Long productId, String merchantId) {
		
		return  em.createNativeQuery("select START_TIME_DISPLAY,END_TIME_DISPLAY,REDEMPTION_DELAY_TIME from Redemption_Delay redemptionDelay where redemptionDelay.product_Id=:productId and "
				+ " redemptionDelay.merchant_Id=:merchantId "+"order by redemptionDelay.start_Time_Display")
		.setParameter(CCLPConstants.PRODUCT_ID, productId).setParameter(CCLPConstants.MERCHANT_ID, merchantId)
		.getResultList();

	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getMerchantProductByIdData(Long productId, String merchantId) {
		return  em.createNativeQuery("select START_TIME_DISPLAY,END_TIME_DISPLAY,REDEMPTION_DELAY_TIME from Redemption_Delay redemptionDelay where redemptionDelay.product_Id=:productId and "
				+ " redemptionDelay.merchant_Id=:merchantId")
		.setParameter(CCLPConstants.PRODUCT_ID, productId).setParameter(CCLPConstants.MERCHANT_ID, merchantId)
		.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getMerchantsbyProductId(Long productId) {

		if(productId>0){
			return em.createNativeQuery(QueryConstants.GET_REDEMPTION_DELAY+ " AND RD.PRODUCT_ID=:productId").setParameter(CCLPConstants.PRODUCT_ID, productId).getResultList();
		}else{
			return em.createNativeQuery(QueryConstants.GET_REDEMPTION_DELAY).getResultList();
		}
	}


}
