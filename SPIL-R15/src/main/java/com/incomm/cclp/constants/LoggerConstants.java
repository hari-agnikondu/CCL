package com.incomm.cclp.constants;

public class LoggerConstants {

	private LoggerConstants() {
		throw new IllegalStateException("Logger Constants class");
	}

	public static final String RAW_XML = "rawxml=";

	public static final String ERR_PAYLOAD_NOT_PRESENT = "ERROR IN RESPONSE MESSAGE";

	public static final String PAYLOAD_MSGTYPE_RESPONSE = "RES";

	public static final String PAYLOAD_MSGTYPE_REQUEST = "REQ";

	public static final String MASKED_RESPONSE_MSG = "****************************";

	public static final String DATE_TIMEZONE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSZ";

	public static final String SUPPORT_KEY = "TS=(ts),THREAD=(thread),LOG-SRC=CCLP,LOG-HOST=(logHost),REQ-SRC-IP=(reqIp),TXN-ID=(txnid),MSG-TYPE=(msgTyp),METHOD=(method),MSG-FMT=XML,PAYLOAD=[(payLoad)],OPT-KEYS=,ERR-MSG=(errMsg),ERR-CODE=(errCode),PROCESS-TIME=(processTime),RESP-CODE=(respCode),RESP-MSG=(respMsg),SERIAL-NBR=,INTERCHANGE=(intg),TRAN-DESC=(delchnl_tranDesc),DAO-PROCESS-TIME=(dao_processTime),PROD-DESC=(categoryDesc),PACKAGE-ID=(packageId),VENDOR-ID=(vendor)";

	public static final String CLASS_METHOD_FULLNAME = "com.incomm.cclp.service.impl.SpilServiceImpl.callSPILTransaction";

	public static final String ENTER = "ENTER";

	public static final String EXIT = "EXIT";

	public static final String PING_CLASS_METHOD = "com.incomm.cclp.service.impl.PingServiceImpl.getServerDetails";

}
