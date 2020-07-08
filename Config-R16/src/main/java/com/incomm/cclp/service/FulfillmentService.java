package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.FulfillmentDTO;
import com.incomm.cclp.exception.ServiceException;

/**
 * @author Raja
 */
public interface FulfillmentService {

	public void createFulfillment(FulfillmentDTO fullfillmentDto) throws ServiceException;

	public void updateFulfillment(FulfillmentDTO fullfillmentDto) throws ServiceException;

	public FulfillmentDTO getFulfillmentById(long fulfillmentSEQID) throws ServiceException;

	public List<FulfillmentDTO> getAllFulfillments() throws ServiceException;

	public List<FulfillmentDTO> getFulfillmentByName(String fulFillmentName) throws ServiceException;

	public int chkDuplicateByID(String fulfillmentID); 

	public void deleteFulfillment(long fulfillmentSEQID) throws ServiceException;
	
	public int checkPackageIdMap(String fulfillmentID);
}
