package com.incomm.cclp.fsapi.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;

@Repository
public interface SerialNumberActivationDAO {
	
	public Map<String, Object> serialNumberActivation(Map<String, Object> valuMap, List<ErrorMsgBean> errorList) throws ServiceException;


}
