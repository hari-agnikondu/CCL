package com.incomm.cclp.fsapi.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.fsapi.bean.ErrorMsgBean;

@Repository
public interface VirtualCardValidationDAO {

	public Map<String, Object> virtualCardValidation(Map<String, Object> valuMap,List<ErrorMsgBean> errorList);
	
	public void logAPIRequestDtls(Map<String, Object> tempValuHashMap,String respMsg);
}
