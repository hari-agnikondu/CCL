package com.incomm.scheduler.dao;

import java.util.List;
import java.util.Map;

import com.incomm.scheduler.model.PostBackLogDtl;

public interface PostBackDAO {

	public List<PostBackLogDtl> getPostBackReqTableDetails();

	public Map<String, Object>   getOrderStatus(Map<String, Object> valuHashMap);
	public List<Map<String, Object>>  getlineItemDtls(Map<String, Object> orderStatusMap);

	public void updatePostBackStatus(Map<String, Object> valueObj);
	
			  
	
	

}
