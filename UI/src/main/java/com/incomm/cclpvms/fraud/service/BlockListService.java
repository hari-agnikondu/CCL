package com.incomm.cclpvms.fraud.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.fraud.model.BlockList;

public interface BlockListService {

	public Map<String,String> getAllDelivaryChannels()  throws ServiceException ;
	public ResponseDTO getBlockedListByDelChnlCode(String delChnlCode)  throws ServiceException ;
	public ResponseDTO getAllBlockedList()  throws ServiceException;
	public ResponseDTO deleteBlockList(Object[] deleteDelCode);
	public List<List<Object>> getDeliveryChnlTxns() throws ServiceException;
	public ResponseDTO createBlockList(BlockList blockList) throws ServiceException;
	
	
	
}
