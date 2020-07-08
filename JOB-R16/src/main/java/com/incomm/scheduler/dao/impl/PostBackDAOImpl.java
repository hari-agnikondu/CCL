package com.incomm.scheduler.dao.impl;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.scheduler.constants.QueryConstants;
import com.incomm.scheduler.dao.PostBackDAO;
import com.incomm.scheduler.model.PostBackLogDtl;
import com.incomm.scheduler.utils.JobConstants;
/**
 * this class used to get the details from db regarding PostBack 
 * @author sampathkumarl
 *
 */
@Repository
public class PostBackDAOImpl extends JdbcDaoSupport implements PostBackDAO {
	
	private static final Logger log = LogManager.getLogger(PostBackDAOImpl.class);
	
	@Autowired
	public void setDs(@Qualifier("orderDs") DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	/**
	 * 
	 * @see com.incomm.scheduler.dao.getPostBackReqTableDetails#GET_POSTBACK_REQ_DETAILS_QUERY()
	 */

	@Override
	public List<PostBackLogDtl> getPostBackReqTableDetails() {
		return getJdbcTemplate().query(QueryConstants.GET_POSTBACK_REQ_DETAILS_QUERY,  new ResultSetExtractor<List<PostBackLogDtl>>(){

			@Override
			public List<PostBackLogDtl> extractData(ResultSet rs) throws SQLException  {
				List<PostBackLogDtl> postBackLogDtlList = new ArrayList<>();

				while(rs.next()){
					
					PostBackLogDtl pb = new PostBackLogDtl();
					pb.setOrderId(rs.getString(1));
					pb.setApiName(rs.getString(2));
					pb.setSeqNo(rs.getString(3));
					pb.setReqMsg(rs.getString(4));
					pb.setReqHeader(rs.getString(5));
					pb.setResHeader(rs.getString(6));
					pb.setResponseCode(rs.getString(7));
					pb.setResponseMsg(rs.getString(8));
					pb.setResponseCount(rs.getString(9));
					pb.setResponseFlag(rs.getString(10));
					pb.setPostBackStatus(rs.getString(11));
					pb.setPostBackUrl(rs.getString(12));
					
					
					postBackLogDtlList.add(pb);
				}
				
				log.info(" extracted the PostBackLog Details ");
				return postBackLogDtlList;
			}

		});
	}

	/**
	 * this method is used for getting OrderStatus details.
	 */
	@Override
	public Map<String, Object> getOrderStatus(Map<String, Object> valuHashMap) {
		
		return getJdbcTemplate().query(QueryConstants.GET_ORDER_STATUS,  new ResultSetExtractor<Map<String, Object>>(){
		 Map<String, Object> lineItemStatus = new HashMap<>();
			@Override
			public Map<String, Object> extractData(ResultSet rs) throws SQLException {
				while(rs.next()){
					
					Map<String,Object> itemListObject=new HashMap<>();
					valuHashMap.put(JobConstants.ORDER_ID, rs.getString(1));
					valuHashMap.put(JobConstants.ORDER_STATUS, rs.getString(2));
					valuHashMap.put(JobConstants.SHIPPING_METHOD, rs.getString(3));
					
					itemListObject.put(JobConstants.LINE_ITEM_ID, rs.getString(4));
					itemListObject.put(JobConstants.LINE_ITEM_ORDER_STATUS,rs.getString(5));
					itemListObject.put(JobConstants.RETURN_FILE_MSG, rs.getString(6));
					itemListObject.put(JobConstants.RESP_CODE, rs.getString(7));
					
					lineItemStatus.put(rs.getString(4), itemListObject);
				}
				valuHashMap.put(JobConstants.ORDER_LINE_ITEMDTLS,lineItemStatus);
				
				log.info(" extracted the OrderStatus Details ");
				return valuHashMap;
				
			}
			
		
			
			

		},valuHashMap.get(JobConstants.ORDER_ID),valuHashMap.get(JobConstants.PARTNER_ID) );
	}

	/**
	 * this method is used for getting the line Item Details.
	 */
	@Override
	public List<Map<String, Object>> getlineItemDtls(Map<String, Object> valuHashMap) {
		
		List<Map<String, String>> postBackLineItemList =new ArrayList<>();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" AND OL.LINE_ITEM_ID IN (");
		
		ArrayList<Object> array=new ArrayList<>();
		@SuppressWarnings("unchecked")
		Map<String,Object> lineItemMap= (Map<String, Object>) valuHashMap.get(JobConstants.ORDER_LINE_ITEMDTLS);
		 List<Map<String, Object>> lineItemList =new ArrayList<>();
		 
		for(Entry<String, Object> map:lineItemMap.entrySet()) {
				array.add(map.getKey());
	
				getJdbcTemplate().query(QueryConstants.GET_ORDER_LINE_ITEM_DTLS,  new ResultSetExtractor<List<Map<String, Object>>>(){
			
			@SuppressWarnings("unchecked")
			@Override
			public List<Map<String, Object>>  extractData(ResultSet rs) throws SQLException{
				HashMap<String, String> postBackLineItemMap= new HashMap<>();
				HashMap<String, Object> cardsMap= new HashMap<>();

				while(rs.next()){
					postBackLineItemMap.put(JobConstants.PROXY_NUMBER, rs.getString(1));
					postBackLineItemMap.put(JobConstants.SERIAL_NUMBER, rs.getString(2));
					postBackLineItemMap.put(JobConstants.CARD_STATUS, rs.getString(3));
					postBackLineItemMap.put(JobConstants.PIN, rs.getString(4));
					postBackLineItemMap.put(JobConstants.PROXY_PIN_ENCR,rs.getString(5));
					postBackLineItemMap.put(JobConstants.TRACKING_NBR, rs.getString(6));
					postBackLineItemMap.put(JobConstants.SHIPPING_DATE, rs.getString(7));
					postBackLineItemMap.put(JobConstants.CUSTOMER_CODE,rs.getString(8));
					postBackLineItemMap.put(JobConstants.PRODUCT_ID, rs.getString(9));
					postBackLineItemMap.put(JobConstants.CARDNUMBER, rs.getString(10));
					postBackLineItemMap.put(JobConstants.CARD_NUM_HASH, rs.getString(11));
					postBackLineItemMap.put(JobConstants.EXPIRY_DATE, rs.getString(12));
					postBackLineItemMap.put(JobConstants.CARD_NUM_ENCR, rs.getString(13));
					postBackLineItemMap.put(JobConstants.PRINTER_RESPONSE, rs.getString(14));
					
					postBackLineItemList.add(postBackLineItemMap);
					
				}
				cardsMap.put(JobConstants.CARDS, postBackLineItemList);
				cardsMap.putAll((Map<? extends String, ? extends Object>) map.getValue());
				lineItemList.add(cardsMap);
				return lineItemList;
			}

		},valuHashMap.get("orderId"), valuHashMap.get("partnerId"),map.getKey());
		
		
		}
		log.info(" extracted the lineItemList Details ");
		return lineItemList;
		
	}
	
	/**
	 * This method is used to Update the PostBackStatus, retry Count.
	 */
	@Override
	public void updatePostBackStatus(Map<String, Object> valueObj) {
		
		getJdbcTemplate().update(QueryConstants.UPDATE_POSTBACKSTATUS, valueObj.get("POSTBACK_STATUS"),valueObj.get(JobConstants.POST_BACK_RESP_MSG),valueObj.get(JobConstants.SEQ_NO));
		
	}
	
}
