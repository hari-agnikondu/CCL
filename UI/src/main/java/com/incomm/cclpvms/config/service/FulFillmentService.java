package com.incomm.cclpvms.config.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclpvms.config.model.FulFillment;
import com.incomm.cclpvms.config.model.FulfillmentDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface FulFillmentService {
	
	public ResponseDTO createFulfillment(FulFillment fulFillment) throws ServiceException;

	public ResponseDTO updateFulfillment(FulFillment fulFillment) throws ServiceException;

	public ResponseDTO deleteFulfillment(long fulFillmentSEQID,String fulfillmentID) throws ServiceException;

	public List<FulfillmentDTO> getAllFulFillment() throws ServiceException;

	public Map<String, String> getShipmentAttList() throws ServiceException;

	public List<FulfillmentDTO> getAllFulFillmentByName(String fulFillmentName) throws ServiceException;

	public FulFillment getFulfillmentById(long fulFillmentID) throws ServiceException;
}
