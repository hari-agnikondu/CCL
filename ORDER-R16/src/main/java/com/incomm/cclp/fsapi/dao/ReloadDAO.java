package com.incomm.cclp.fsapi.dao;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;

public interface ReloadDAO {
	
	public  void getOrderID(Map<String, Object> valuHashMap);
	public void insertReloadDtls(Map<String,Object> tempValueMap) throws ServiceException;
	public void insertReloadmast(Map<String,Object> tempValueMap) throws ServiceException;
	public void validateProxySerial(Map<String,Object> valueMap,List<ErrorMsgBean> errorList)throws ServiceException;
	public void partnerIdValidation(Map<String,Object> valueMap,List<ErrorMsgBean> errorList) throws ServiceException;

}
