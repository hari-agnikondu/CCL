package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.scheduler.dao.SchedulerJobDAO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.model.ShipmentFile;

@Component
public class ShipmentFileProcessor implements ItemProcessor<ShipmentFile, ShipmentFile>
{
	private static final Logger logger = LogManager.getLogger(ShipmentFileProcessor.class);
	@Autowired
	SchedulerJobDAO schedulerJobDAO;
	
	private List<String> fileName;
	
	public ShipmentFileProcessor(SchedulerJobDAO schedulerJobDAO) {
		this.schedulerJobDAO=schedulerJobDAO;
	}

	public List<String> getFileName() {
		return fileName;
	}

	public void setFileName(List<String> fileName) {
		this.fileName = fileName;
	}

	@Override
	public ShipmentFile process(ShipmentFile items) throws Exception {
		try {
			if(items.getCustomerDesc()==null || items.getCustomerDesc().length() > 100)
			{
				throw new ServiceException("Invalid Customer Description");
			}
			if(items.getSourceOneBatch().length()>200)
			{
				throw new ServiceException("Invalid Source One Batch");
			}
			if(items.getParentOrderId()==null||items.getParentOrderId().length()>50)
			{
				throw new ServiceException("Invalid Parent Order ID");
			}
			if(items.getChildOrderId().length()>100)
			{
				throw new ServiceException("Invalid Child Order Id");
			}
			if(items.getFileDate()==null)
			{
				throw new ServiceException("Invalid File date");
			}
			if(items.getSerialNumber()==null||items.getSerialNumber().length()>40)
			{
				throw new ServiceException("Invalid serial Number");
			}
			if(items.getCards().length()>20)
			{
				throw new ServiceException("Invalid cards");
			}
			if(items.getPackageId().length()>20)
			{
				throw new ServiceException("Invalid Package Id");
			}
			if(items.getCardType().length()>50)
			{
				throw new ServiceException("Invalid Card Type");
			}
			if(items.getContactName().length()>4000)
			{
				throw new ServiceException("Invalid Contact Name");
			}
			if(items.getShipto().length()>4000)
			{
				throw new ServiceException("Invalid Ship_To");
			}
			if(items.getAddress1()==null||items.getAddress1().length()>4000)
			{
				throw new ServiceException("Invalid street_Address1");
			}
			if(items.getAddress2().length()>4000)
			{
				throw new ServiceException("Invalid street_Address2");
			}
			if(items.getCity()==null||items.getCity().length()>4000)
			{
				throw new ServiceException("Invalid city");
			}
			if(items.getState()==null||items.getState().length()>4000)
			{
				throw new ServiceException("Invalid State");
			}
			if(items.getZip()==null||items.getZip().length()>4000)
			{
				throw new ServiceException("Invalid Zip");
			}
			if(items.getTrackingNumber().length()>100)
			{
				throw new ServiceException("Invalid Zip");
			}
			if(items.getShipDate()==null)
			{
				throw new ServiceException("Invalid Ship Date");
			}
			if(items.getShipmentId()==null || items.getShipmentId().length()==0 ||  items.getShipmentId().length()>10)
			{
				throw new ServiceException("Invalid Partner_Id");
			}
			if(items.getShipMethod().length()>200)
			{
				throw new ServiceException("Invalid FileName");
			}
		} catch (ServiceException e) {
			logger.error("Log:"+items.getFileName()+"CN FILE UPLOAD "+items.getFileRecode()+" "+e.getMessage());
			schedulerJobDAO.updateErrorLog(items.getFileName(),"CN FILE UPLOAD",items.getFileRecode(),e.getMessage());
			return null;
		}
		return items;
	}
	

}
