package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.incomm.scheduler.dao.SchedulerJobDAO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.model.SerialNumberFile;

@Component
public class SerialNumberFileProcessor implements ItemProcessor<SerialNumberFile, SerialNumberFile>
{
	private static final Logger logger = LogManager.getLogger(SerialNumberFileProcessor.class);
	
	SchedulerJobDAO schedulerJobDAO;
	
	private List<String> fileName;
	
	public List<String> getFileName() {
		return fileName;
	}

	public void setFileName(List<String> fileName) {
		this.fileName = fileName;
	}
	
	public SerialNumberFileProcessor(SchedulerJobDAO schedulerJobDAO) {
		this.schedulerJobDAO = schedulerJobDAO;
	}

	public SerialNumberFileProcessor() {
		super();
	}

	@Override
	public SerialNumberFile process(SerialNumberFile items) throws Exception {
			try {
			if(items.getProductId()==null || items.getProductId()<0)
			{
				throw new ServiceException("Invalid Product Id");
			}
			if(items.getSerialNumber()==null||items.getSerialNumber().length()>40)
			{
				throw new ServiceException("Invalid Serial Number");
			}
			if(items.getVan16()==null || items.getVan16().length()>50)
			{
				throw new ServiceException("Invalid Van16");
			}
		
		} catch (ServiceException e) {
			logger.error("Log:"+items.getFileName()+"Serial FILE UPLOAD "+items.getFileRecode()+" "+e.getMessage());
			schedulerJobDAO.updateErrorLog(items.getFileName(),"Serial FILE UPLOAD",items.getFileRecode(),e.getMessage());
			return null;
		}  
		return items;
	}
	

}
