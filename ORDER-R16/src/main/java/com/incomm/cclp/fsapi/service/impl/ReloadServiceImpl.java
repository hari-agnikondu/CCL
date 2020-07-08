package com.incomm.cclp.fsapi.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
import com.incomm.cclp.fsapi.dao.ReloadDAO;

@Service
public class ReloadServiceImpl  {
	@Autowired
	ReloadDAO reloadDAO;
	public void insertReloadmast(Map<String,Object> tempValueMap) throws ServiceException{
		reloadDAO.insertReloadmast(tempValueMap);
	}
	public void insertReloadDtls(Map<String,Object> tempValueMap) throws ServiceException{
		reloadDAO.insertReloadDtls(tempValueMap);
	}
	public  void getOrderID(Map<String, Object> valuHashMap){
		reloadDAO.getOrderID(valuHashMap);
	}
	public void validateProxySerial(Map<String,Object> valueMap,List<ErrorMsgBean> errorList)throws ServiceException{
		reloadDAO.validateProxySerial(valueMap,errorList);
	}
	public  void partnerIdValidation(Map<String,Object> valueMap,List<ErrorMsgBean> errorList) throws ServiceException{
		reloadDAO.partnerIdValidation(valueMap,errorList);
	}


}
