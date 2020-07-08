package com.incomm.cclp.fsapi.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;

public interface CardStatusInquiryDAO {

	List<String> getListOfProxyOrSerial(Map<String, Object> valuHashMap);

	void getyyyyMMdd(Map<String, Object> tempValueMap, Date date);

	void gethhmmss(Map<String, Object> tempValueMap, java.util.Date date);

	void processCardStatusInquiry(Map<String, Object> valuHashMap,
			List<ErrorMsgBean> errorList) throws ServiceException;

	void checkOrderId(Map<String, Object> valuHashMap,
			List<ErrorMsgBean> errorList);

	Map<String, Object> getCardDetls(Map<String, Object> valuHashMap)
			throws SQLException;

	void checkPartnerId(Map<String, Object> valueHashMap,
			List<ErrorMsgBean> errorList);

	void processCardStatus(Map<String, Object> valueHashMap,
			List<ErrorMsgBean> errorList);

	void processCardStatusList(Map<String, Object> valueHashMap,
			List<ErrorMsgBean> errorList);

}
