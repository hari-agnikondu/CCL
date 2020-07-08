package com.incomm.cclp.service;

import java.sql.Connection;

import com.incomm.cclp.dto.ValueDTO;

public interface SpilCreditReversalService {

	public String[] invoke(ValueDTO valueDto, Connection con);
}
