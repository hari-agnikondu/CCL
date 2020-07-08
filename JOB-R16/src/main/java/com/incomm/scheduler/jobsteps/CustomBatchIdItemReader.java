package com.incomm.scheduler.jobsteps;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * @author Dinesh Rajput
 *
 */
public class CustomBatchIdItemReader implements ItemReader<String>{
 
 private List<String> batchIdList;
 private int count = 0;
 
 @Override
 public String read() throws Exception, UnexpectedInputException,
   ParseException {
  if(count < batchIdList.size()){
   return batchIdList.get(count++);
  }else{
   return null;
  }
 }
 public List<String> getBatchIdList() {
  return batchIdList;
 }
 public void setBatchIdList(List<String> batchIdList) {
  this.batchIdList = batchIdList;
 }
 
}