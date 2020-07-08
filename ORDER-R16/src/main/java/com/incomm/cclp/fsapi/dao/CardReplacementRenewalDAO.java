package com.incomm.cclp.fsapi.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
@Repository
public interface CardReplacementRenewalDAO {
	
	public void saveCardReplacementOrder(Map<String, Object> valuHashMap) throws ServiceException;
	
	public void validateProxySerial(Map<String,Object> valueMap,List<ErrorMsgBean> errorList)throws SQLException;
	
	

}
