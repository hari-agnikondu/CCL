package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.scheduler.dao.SchedulerJobDAO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.model.ResponseFile;

@Component
public class ResponseFileProcessor implements ItemProcessor<ResponseFile, ResponseFile>
{
	private static final Logger logger = LogManager.getLogger(ResponseFileProcessor.class);
	@Autowired
	SchedulerJobDAO schedulerJobDAO;
	
	private List<String> fileName;
	
	public ResponseFileProcessor(SchedulerJobDAO schedulerJobDAO) {
		this.schedulerJobDAO = schedulerJobDAO;
	}

	public List<String> getFileName() {
		return fileName;
	}

	public void setFileName(List<String> fileName) {
		this.fileName = fileName;
	}

	@Override
	public ResponseFile process(ResponseFile items) throws Exception {
		try {
			if(items.getMagicNumber()==null || items.getMagicNumber().length() == 0 || items.getMagicNumber().length() > 20)
			{
				throw new ServiceException("Invalid Magic Number");
			}
			if(items.getStatus().length()>1)
			{
				throw new ServiceException("Invalid Status");
			}
			if(items.getCarrier().length()>100)
			{
				throw new ServiceException("Invalid Carrier");
			}
			if(items.getTrackingNumber().length()==0 ||  items.getTrackingNumber().length()>100)
			{
				throw new ServiceException("Invalid Tracking Number");
			}
			if(items.getMerchantID().length()>8)
			{
				throw new ServiceException("Invalid Merchant_ID");
			}
			if(items.getMerchantName().length()>50)
			{
				throw new ServiceException("Invalid Merchant_Name");
			}
			if(items.getStorelocationID().length()>15)
			{
				throw new ServiceException("Invalid StoreLocationID");
			}
			if(items.getBatchNumber().length()>20)
			{
				throw new ServiceException("Invalid Batch_Number");
			}
			if(items.getCaseNumber().length()>20)
			{
				throw new ServiceException("Invalid Case_Number");
			}
			if(items.getPalletNumber().length()>20)
			{
				throw new ServiceException("Invalid Pallet_Number");
			}
			if(items.getSerialNumber()==null || items.getSerialNumber().length()==0 ||  items.getSerialNumber().length()>15)
			{
				throw new ServiceException("Invalid Serial_Number");
			}
			if(items.getShipTo().length()>100)
			{
				throw new ServiceException("Invalid Ship_To");
			}
			if(items.getShipTo().length()>100)
			{
				throw new ServiceException("Invalid Ship_To");
			}
			if(items.getStreetAddress1().length()>50)
			{
				throw new ServiceException("Invalid street_Address1");
			}
			if(items.getStreetAddress2().length()>50)
			{
				throw new ServiceException("Invalid street_Address2");
			}
			if(items.getCity().length()>25)
			{
				throw new ServiceException("Invalid city");
			}
			if(items.getState().length()>5)
			{
				throw new ServiceException("Invalid State");
			}
			if(items.getZip().length()>15)
			{
				throw new ServiceException("Invalid Zip");
			}
			if(items.getdCMSID().length()>10)
			{
				throw new ServiceException("Invalid Zip");
			}
			if(items.getProdID().length()>10)
			{
				throw new ServiceException("Invalid Prod_ID");
			}
			if(items.getOrderID()==null || items.getOrderID().length()==0 ||  items.getOrderID().length()>10)
			{
				throw new ServiceException("Invalid Partner_Id");
			}
			if(items.getFileName()==null || items.getFileName().length()==0 ||  items.getFileName().length()>200)
			{
				throw new ServiceException("Invalid FileName");
			}
		} catch (ServiceException e) {
			logger.error("Log:"+items.getFileName()+"RESPONSE FILE UPLOAD "+items.getFileRecode()+" "+e.getMessage());
			schedulerJobDAO.updateErrorLog(items.getFileName(),"RESPONSE FILE UPLOAD",items.getFileRecode(),e.getMessage());
			return null;
		}
		return items;
	}
	

}
