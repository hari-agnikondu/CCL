package com.incomm.cclp.service;

import java.sql.Connection;

import com.incomm.cclp.dto.ValueDTO;

public interface SpilRedemptionLockRevService {

	String[] invoke(ValueDTO valueDto, Connection con);

}
