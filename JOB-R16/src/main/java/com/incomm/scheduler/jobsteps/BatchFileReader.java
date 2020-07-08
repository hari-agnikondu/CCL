package com.incomm.scheduler.jobsteps;


import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

import com.incomm.scheduler.model.CnShipmentFile;


public class BatchFileReader {
	
	private BatchFileReader() {
		
	}
	@StepScope
	@Bean
	public static FlatFileItemReader<CnShipmentFile> reader(String directoryPath,String fileName)  {
		FlatFileItemReader<CnShipmentFile> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(directoryPath + fileName));

		reader.setLineMapper(new DefaultLineMapper<CnShipmentFile>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "magic_Number", "status", "carrier", "date", "tracking_Number",
								"merchant_ID", "merchant_Name", "storelocationID", "batch_Number", "case_Number",
								"pallet_Number", "serial_Number", "ship_To", "street_Address1", "street_Address2",
								"city", "state", "zip", "dCMS_ID", "prod_ID", "order_ID", "parent_Serial_Number" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<CnShipmentFile>() {
					{
						setTargetType(CnShipmentFile.class);
					}
				});
			}
		});
		
		return reader;
	}
	

}
