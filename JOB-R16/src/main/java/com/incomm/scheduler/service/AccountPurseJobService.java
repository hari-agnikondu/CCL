package com.incomm.scheduler.service;

import com.incomm.scheduler.service.command.CreatePurseLoadJobCommand;

public interface AccountPurseJobService {

	public long createPurseLoadJobRequest(CreatePurseLoadJobCommand command);

	void autoPurseJob();
	
}
