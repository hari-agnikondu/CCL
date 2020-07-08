package com.incomm.scheduler.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.incomm.scheduler.model.DailyBalance;

@Service
public interface DailyBalanceAlertDAO {

	public List<DailyBalance> getDailyBalanceDetails();

	public void updateDailyBalalertmsg(Map<String, String> valueObj);

	public String  callProcedure(Long issuerId);




}
