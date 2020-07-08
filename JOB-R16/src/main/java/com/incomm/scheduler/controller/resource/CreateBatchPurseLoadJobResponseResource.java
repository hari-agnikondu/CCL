package com.incomm.scheduler.controller.resource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBatchPurseLoadJobResponseResource {

	private String responseCode;
	private String responseMessage;

	private long batchId;

}
