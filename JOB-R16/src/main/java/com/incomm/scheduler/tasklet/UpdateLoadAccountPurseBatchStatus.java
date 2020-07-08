package com.incomm.scheduler.tasklet;

import java.util.List;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.BatchLoadAccountPurseDAO;
import com.incomm.scheduler.model.BatchLoadAccountPurse;
import com.incomm.scheduler.service.AccountPurseJobService;

public class UpdateLoadAccountPurseBatchStatus {
	
    private AccountPurseJobService accountPurseJobService;

	private BatchLoadAccountPurseDAO batchUpdateRequestDAO;

	public UpdateLoadAccountPurseBatchStatus(AccountPurseJobService accountPurseJobService) {
		super();
		this.accountPurseJobService = accountPurseJobService;
	}
	
	public UpdateLoadAccountPurseBatchStatus(BatchLoadAccountPurseDAO batchUpdateRequestDAO) {
		super();
		this.batchUpdateRequestDAO = batchUpdateRequestDAO;
	}

	public void updateBatchRequestStatus(List<BatchLoadAccountPurse> requests) {
		requests.stream()
			.forEach(request -> batchUpdateRequestDAO.updateAccountPurseUpdateRequest(request.getBatchId(),
					CCLPConstants.JOB_STATUS_COMPLETED));
	}
	
	public void autoPurseJobRequest() {
		
		accountPurseJobService.autoPurseJob();
	}

}
