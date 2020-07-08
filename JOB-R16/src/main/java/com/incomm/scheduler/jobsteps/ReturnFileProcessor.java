package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.incomm.scheduler.dao.SchedulerJobDAO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.model.ReturnFile;

@Component
public class ReturnFileProcessor implements ItemProcessor<ReturnFile, ReturnFile>
{
	private static final Logger logger = LogManager.getLogger(ReturnFileProcessor.class);
	
	SchedulerJobDAO schedulerJobDAO;
	
	private List<String> fileName;
	
	public List<String> getFileName() {
		return fileName;
	}

	public void setFileName(List<String> fileName) {
		this.fileName = fileName;
	}
	
	public ReturnFileProcessor(SchedulerJobDAO schedulerJobDAO) {
		this.schedulerJobDAO = schedulerJobDAO;
	}

	public ReturnFileProcessor() {
		super();
	}

	@Override
	public ReturnFile process(ReturnFile items) throws Exception {
		try {
			if(items.getCustomerDesc()==null || items.getCustomerDesc().length()>100)
			{
				throw new ServiceException("Invalid Customer Description");
			}
			if(items.getShipSuffixnum().length()>200)
			{
				throw new ServiceException("Invalid Ship Suffix Number");
			}
			if(items.getParentOrderId()==null || items.getParentOrderId().length()>50)
			{
				throw new ServiceException("Invalid Parent Order Id");
			}
			if(items.getChildOrderId()==null || items.getChildOrderId().length()>100)
			{
				throw new ServiceException("Invalid Child Order Id");
			}
			if(items.getSerialNumber()==null || items.getSerialNumber().length()>40)
			{
				throw new ServiceException("Invalid Serial Number");
			}
			if(items.getRejectCode().length()>10)
			{
				throw new ServiceException("Invalid Reject Code");
			}
			if(items.getRejectReason().length()>100)
			{
				throw new ServiceException("Invalid Reject Reason");
			}
			if(items.getCardType().length()>50)
			{
				throw new ServiceException("Invalid Card Type");
			}
			if(items.getClientOrderId().length()>20)
			{
				throw new ServiceException("Invalid Client Order Id");
			}
				
			if(items.getFileName()==null || items.getFileName().length()==0 ||items.getFileName().length()>200)
			{
				throw new ServiceException("Invalid File Name");
			}
		
		} catch (ServiceException e) {
			logger.error("Log:"+items.getFileName()+"RETURN FILE UPLOAD "+items.getFileRecode()+" "+e.getMessage());
			schedulerJobDAO.updateErrorLog(items.getFileName(),"RETURN FILE UPLOAD",items.getFileRecode(),e.getMessage());
			return null;
		}
		return items;
	}
	

}
