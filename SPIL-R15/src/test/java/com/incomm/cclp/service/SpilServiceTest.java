package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.gpp.security.APISecurityException;
import com.incomm.cclp.service.impl.SpilServiceImpl;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//@Sql({"classpath:data.sql"})
//@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode=TransactionMode.ISOLATED), scripts = "classpath:data.sql")
//@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode=TransactionMode.ISOLATED), scripts = "classpath:deletemetadata.sql")
//@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:deletemetadata.sql")


public class SpilServiceTest {

	
	@Autowired
	SpilServiceImpl spilServiceImpl;
	
	private static final String SUCCESS_RESPONSE_CODE ="00";
	// @Test
	public void testCallSPILTransaction_with_respCode_Check() throws ServiceException, SQLException {
		
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589512</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>6009004120297973</Track1><Track2>6009004120297973</Track2>\r\n" + 
				"<SPNumber>6009004120297973</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10043",respCode);	
			
}
	// @Test
	public void testCallSPILTransaction_with_spNumber_Check() throws ServiceException, SQLException {
		
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589512</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>6009004120297973</Track1><Track2>6009004120297973</Track2>\r\n" + 
				"<SPNumber>6009004120297973</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
				
		String spNumber = txnResponse.substring(txnResponse.indexOf("<SPNumber>")+10,txnResponse.indexOf("</SPNumber>"));
		
		
		assertEquals("6009004120297973",spNumber);	

			
}
	
	// @Test
	public void testCallSPILTransaction_with_MandatoryField_Empty_Amount_Check() throws ServiceException, SQLException {
		
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589512</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>6009004120297973</Track1><Track2>6009004120297973</Track2>\r\n" + 
				"<SPNumber>6009004120297973</SPNumber><Fee>0.0</Fee><Value><Money><Amount></Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);		
		String respMsg = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));	
		
		assertEquals("10010",respMsg);	


			
}
	
	// @Test
	public void testCallSPILTransaction_Authenticated_Check() throws ServiceException, APISecurityException {
			
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589512</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>6009004120297973</Track1><Track2>6009004120297973</Track2>\r\n" + 
				"<SPNumber>6009004120297973</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";		
	
		String userID = xmlMsg.substring(xmlMsg.indexOf("<UserID>")+8,xmlMsg.indexOf("</UserID>"));
		byte[] password = xmlMsg.substring(xmlMsg.indexOf("<Password>")+10,xmlMsg.indexOf("</Password>")).getBytes();
		
		spilServiceImpl.isAuthenticated(userID,password,null);		
		assertEquals(true,spilServiceImpl.isAuthenticated(userID,password,null));	


			
}
	
	// @Test
	public void testCallSPILTransaction_Authentication_Invalid_Password_Check() throws ServiceException, SQLException {
			
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589512</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>6009004120297973</Track1><Track2>6009004120297973</Track2>\r\n" + 
				"<SPNumber>6009004120297973</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123476</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";		
	
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);		
		String respMsg = txnResponse.substring(txnResponse.indexOf("<RespMsg>")+9,txnResponse.indexOf("</RespMsg>"));	
		assertEquals("Invalid UserID or Password",respMsg);			
}
	
	// @Test
	public void testReloadTransaction_with_success() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325772142</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835223</Track2>\r\n" + 
				"<SPNumber>5241810402835223</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);		
}
	
	// @Test
	public void testReloadTransaction_with_duplicate_rrn() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589520</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String xmlMsg1="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589520</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse1 =spilServiceImpl.callSPILTransaction(xmlMsg1);
		
		String respCode = txnResponse1.substring(txnResponse1.indexOf("<RespCode>")+10,txnResponse1.indexOf("</RespCode>"));
		
		assertEquals("10085",respCode);		
}
	
/*	// @Test
	public void testReloadTransaction_beyond_maxCardBal() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589514</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>1178801000000005</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>105.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(respCode,"10070");		
}*/
	
	// @Test
	public void testReloadTransaction_beyond_maxTxnAmount() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32581019</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>105.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10180",respCode);		
}
	
	// @Test
	public void testReloadTransaction_below_minTxnAmount() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32581119</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>5.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10181",respCode);		
}
	
	// @Test
	public void testReloadTransaction_txn_product_currency_mismatch() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32522019</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>INR</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>INR</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10081",respCode);		
}
	
	// @Test
	public void testReloadTransaction_no_amount_tag() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589515</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10010",respCode);		
}
	
	// @Test
	public void testReloadTransaction_no_amount_value() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32586340</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10181",respCode);		
}
	
	// @Test
	public void testReloadTransaction_card_status_check_fail() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589517</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835226</Track1><Track2>1178801000000005</Track2>\r\n" + 
				"<SPNumber>5241810402835226</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>INR</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10050",respCode);		
}
	
	// @Test
	public void testReload_reversal_transaction_success() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32141518</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);
		
		/*Reversal Txn starts*/
		
		if(SUCCESS_RESPONSE_CODE.equals(respCode))
		{
		
		String xmlMsgRev="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>rechargereversal</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32141518</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponseRev =spilServiceImpl.callSPILTransaction(xmlMsgRev);
		
		String respCodeRev = txnResponseRev.substring(txnResponseRev.indexOf("<RespCode>")+10,txnResponseRev.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCodeRev);
		}
}
	
	// @Test
	public void testReload_reversal_transaction_no_amount_tag() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589521</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);
		
		/*Reversal Txn starts*/
		
		if(SUCCESS_RESPONSE_CODE.equals(respCode))
		{
		
		String xmlMsgRev="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>rechargereversal</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589521</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponseRev =spilServiceImpl.callSPILTransaction(xmlMsgRev);
		
		String respCodeRev = txnResponseRev.substring(txnResponseRev.indexOf("<RespCode>")+10,txnResponseRev.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCodeRev);
		}
}
	
	// @Test
	public void testReload_reversal_transaction_already_reversed() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>recharge</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589522</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);
		
		/*Reversal Txn starts*/
		
		respCode = SUCCESS_RESPONSE_CODE;
		
		if(SUCCESS_RESPONSE_CODE.equals(respCode))
		{
		
		String xmlMsgRev="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>rechargereversal</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589522</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		spilServiceImpl.callSPILTransaction(xmlMsgRev);
		
		String xmlMsgRev1="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>rechargereversal</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589521</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>5241810402835227</Track1><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponseRev1 =spilServiceImpl.callSPILTransaction(xmlMsgRev1);
		
		String respCodeRev = txnResponseRev1.substring(txnResponseRev1.indexOf("<RespCode>")+10,txnResponseRev1.indexOf("</RespCode>"));
		
		/*Already Reversed Txn should return the Original Reversal Txn*/
		assertEquals(SUCCESS_RESPONSE_CODE,respCodeRev);
		}
}
	
	//Intial Amount is 100$
	
	// @Test
	public void testValIns_Transaction_with_success() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325895</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);		
}
	
	// @Test
	public void testValIns_Transaction_with_no_upc() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325895</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10010",respCode);		
}
	
	// @Test
	public void testValIns_Transaction_with_card_status_check() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325895</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835226</Track2>\r\n" + 
				"<SPNumber>5241810402835226</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10050",respCode);		
}
	
//	// @Test
//	public void testValIns_Transaction_beyond_maxCardBal() throws ServiceException, SQLException {
//		
//		//Max Balance in general is 150 
//		
//		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
//				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325896</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
//				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
//				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
//				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>30.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
//				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
//		
//		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
//		
//		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
//		
//		assertEquals(respCode,"10070");		
//}
	
	// @Test
	public void testValIns_Transaction_no_amount_tag() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325897</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10010",respCode);		
}
	
	// @Test
	public void testValIns_Transaction_maxTxnAmount() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589844</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>105.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10180",respCode);		
}
	
	// @Test
	public void testValIns_Transaction_minTxnAmount() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589433</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>5.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10181",respCode);		
}
	
	// @Test
	public void testValIns_Transaction_txn_product_currency_mismatch() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589433</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>INR</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>INR</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10081",respCode);		
}
	
	// @Test
	public void testValIns_Transaction_local_txn_currency_mismatch() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325899</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>INR</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10081",respCode);		
}
	
	// @Test
	public void testValIns_reversal_transaction_success() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325900</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		System.out.println(respCode);
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);
		
		/*Reversal Txn starts*/
		
		if(SUCCESS_RESPONSE_CODE.equals(respCode))
		{
		
			String xmlMsgRev="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325900</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
					"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
					"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponseRev =spilServiceImpl.callSPILTransaction(xmlMsgRev);
		
		String respCodeRev = txnResponseRev.substring(txnResponseRev.indexOf("<RespCode>")+10,txnResponseRev.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCodeRev);
		}
}
	
	// @Test
	public void testValIns_reversal_transaction_no_amount_tag() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325901</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);
		
		/*Reversal Txn starts*/
		
		if(SUCCESS_RESPONSE_CODE.equals(respCode))
		{
		
			String xmlMsgRev="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325901</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
					"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
					"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponseRev =spilServiceImpl.callSPILTransaction(xmlMsgRev);
		
		String respCodeRev = txnResponseRev.substring(txnResponseRev.indexOf("<RespCode>")+10,txnResponseRev.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCodeRev);
		}
}
	
	// @Test
	public void testValIns_reversal_transaction_already_reversed() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325902</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);
		
		/*Reversal Txn starts*/
		
		respCode = SUCCESS_RESPONSE_CODE;
		
		if(SUCCESS_RESPONSE_CODE.equals(respCode))
		{
		
			String xmlMsgRev="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325902</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
					"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
					"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		spilServiceImpl.callSPILTransaction(xmlMsgRev);
		
		String xmlMsgRev1="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>valins</MsgType><DateTimeInfo><Date>20130121</Date><Time>121924</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325902</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012403</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value><UPC>1234567890</UPC></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name>\r\n" + 
				"<Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponseRev1 =spilServiceImpl.callSPILTransaction(xmlMsgRev1);
		
		String respCodeRev = txnResponseRev1.substring(txnResponseRev1.indexOf("<RespCode>")+10,txnResponseRev1.indexOf("</RespCode>"));
		
		/*Already Reversed Txn should return the Original Reversal Txn*/
		assertEquals(SUCCESS_RESPONSE_CODE,respCodeRev);
		}
}
	
	// @Test
	public void testBalTransfer_Transaction_with_success() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>baltransfer</MsgType><DateTimeInfo><Date>20130120</Date><Time>121917</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32522869</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID><TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012387</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount>\r\n" + 
					"<CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name>\r\n" + 
					"<Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);		
}
	
	// @Test
	public void testBalTransfer_Txn_source_and_target_card_same() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>baltransfer</MsgType><DateTimeInfo><Date>20130120</Date><Time>121917</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325222100</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID><TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012387</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835227</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount>\r\n" + 
					"<CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name>\r\n" + 
					"<Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);	
		
		//This case should be declined, Need to change the assertValue.
}
	
	// @Test
	public void testBalTransfer_Txn_invalid_card() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>baltransfer</MsgType><DateTimeInfo><Date>20130120</Date><Time>121917</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32532125</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID><TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012387</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>52418104028352609555</SPNumber><TargetCardNumber>524181040283550</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount>\r\n" + 
					"<CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name>\r\n" + 
					"<Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10010",respCode);	
		
}
	
	// @Test
	public void testBalTransfer_minTxn_amount() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>baltransfer</MsgType><DateTimeInfo><Date>20130120</Date><Time>121917</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325327333</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID><TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012387</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>0</Amount>\r\n" + 
					"<CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name>\r\n" + 
					"<Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10181",respCode);	
		
}
	
	// @Test
	public void testBalTransfer_maxTxn_amount() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>baltransfer</MsgType><DateTimeInfo><Date>20130120</Date><Time>121917</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325345125</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID><TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012387</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>105.0</Amount>\r\n" + 
					"<CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name>\r\n" + 
					"<Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10180",respCode);	
		
}
	
	// @Test
	public void testBalTransfer_txn_product_currency_mismatch() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>baltransfer</MsgType><DateTimeInfo><Date>20130120</Date><Time>121917</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>325345125</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID><TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012387</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>INR</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount>\r\n" + 
					"<CurrencyCode>INR</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name>\r\n" + 
					"<Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10081",respCode);	
}
	
	// @Test
	public void testBalTransfer_already_closed_card() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>baltransfer</MsgType><DateTimeInfo><Date>20130120</Date><Time>121917</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>322211125</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID><TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012387</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount>\r\n" + 
					"<CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name>\r\n" + 
					"<Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10050",respCode);	
}
	
	// @Test
	public void testBalTransfer_reversal_transaction_with_success() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>baltransfer</MsgType><DateTimeInfo><Date>20130120</Date><Time>121917</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32522870</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID><TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012387</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount>\r\n" + 
					"<CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name>\r\n" + 
					"<Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);
		
		if(SUCCESS_RESPONSE_CODE.equals(respCode))
		{

			String xmlMsg1="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>baltransferreversal</MsgType><DateTimeInfo><Date>20130120</Date><Time>121917</Time>\r\n" + 
						"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32522870</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID><TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1>Address2>Suite M100</Address2>\r\n" + 
						"<City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo><MerchRefNum>012387</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode>\r\n" + 
						"<ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>;6009004120201975</Track1><Track2>5241810402835227</Track2><SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber>\r\n" + 
						"<Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value>\r\n" + 
						"</Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		String txnResponse1 =spilServiceImpl.callSPILTransaction(xmlMsg1);
		
		String respCode1 = txnResponse1.substring(txnResponse1.indexOf("<RespCode>")+10,txnResponse1.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode1);
		}
}
	
	// @Test
	public void testBalTransfer_reversal_already_transaction() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>baltransfer</MsgType><DateTimeInfo><Date>20130120</Date><Time>121917</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32523370</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID><TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012387</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount>\r\n" + 
					"<CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name>\r\n" + 
					"<Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);
		
		if(SUCCESS_RESPONSE_CODE.equals(respCode))
		{

			String xmlMsg1="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>baltransferreversal</MsgType><DateTimeInfo><Date>20130120</Date><Time>121917</Time>\r\n" + 
						"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32523370</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID><TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1>Address2>Suite M100</Address2>\r\n" + 
						"<City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo><MerchRefNum>012387</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode>\r\n" + 
						"<ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>;6009004120201975</Track1><Track2>5241810402835227</Track2><SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber>\r\n" + 
						"<Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value>\r\n" + 
						"</Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";
		
		spilServiceImpl.callSPILTransaction(xmlMsg1);
		
		String xmlMsg2="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>baltransferreversal</MsgType><DateTimeInfo><Date>20130120</Date><Time>121917</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32523370</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID><TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1>Address2>Suite M100</Address2>\r\n" + 
				"<City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo><MerchRefNum>012387</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode>\r\n" + 
				"<ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>;6009004120201975</Track1><Track2>5241810402835227</Track2><SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber>\r\n" + 
				"<Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value>\r\n" + 
				"</Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>"+" ";

		String txnResponse2 =spilServiceImpl.callSPILTransaction(xmlMsg2);
		
		String respCode1 = txnResponse2.substring(txnResponse2.indexOf("<RespCode>")+10,txnResponse2.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode1);
		}
}
	
	// @Test
	public void testCardCardTransfer_Transaction_with_success() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>cardcardtransfer</MsgType><DateTimeInfo><Date>20130121</Date><Time>122002</Time>\r\n" + 
					"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589578</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
					"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
					"<MerchRefNum>012502</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode>\r\n" + 
					"<ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
					"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product>\r\n" + 
					"<AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
						" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);		
}
	
	// @Test
	public void testCardCardTransfer_Txn_source_and_target_card_same() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>cardcardtransfer</MsgType><DateTimeInfo><Date>20130121</Date><Time>122002</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589568</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012502</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode>\r\n" + 
				"<ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835227</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product>\r\n" + 
				"<AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode);	
		
		//This case should be declined, Need to change the assertValue.
}
	
	// @Test
	public void testCardCardTransfer_Txn_invalid_card() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>cardcardtransfer</MsgType><DateTimeInfo><Date>20130121</Date><Time>122002</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589588</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012502</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode>\r\n" + 
				"<ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227454523223</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product>\r\n" + 
				"<AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10010",respCode);	
		
}
	
	// @Test
	public void testCardCardTransfer_minTxn_amount() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>cardcardtransfer</MsgType><DateTimeInfo><Date>20130121</Date><Time>122002</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589278</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012502</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode>\r\n" + 
				"<ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>5.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product>\r\n" + 
				"<AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10181",respCode);	
		
}
	
	// @Test
	public void testCardCardTransfer_maxTxn_amount() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>cardcardtransfer</MsgType><DateTimeInfo><Date>20130121</Date><Time>122002</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589579</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012502</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode>\r\n" + 
				"<ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>105.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product>\r\n" + 
				"<AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10180",respCode);	
		
}
	
	// @Test
	public void testCardCardTransfer_txn_product_currency_mismatch() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>cardcardtransfer</MsgType><DateTimeInfo><Date>20130121</Date><Time>122002</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32229578</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012502</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>INR</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode>\r\n" + 
				"<ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>INR</CurrencyCode></Money></Value></Product>\r\n" + 
				"<AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
		
		String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
		
		String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
		
		assertEquals("10081",respCode);	
}
	
	// @Test
	public void testCardCardTransfer_reversal_transaction_with_success() throws ServiceException, SQLException {
		
		String xmlMsg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>cardcardtransfer</MsgType><DateTimeInfo><Date>20130121</Date><Time>122002</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589578</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012502</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo><EntryMode>030</EntryMode>\r\n" + 
				"<ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track2>5241810402835227</Track2>\r\n" + 
				"<SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money><Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product>\r\n" + 
				"<AuthInfo><UserID>CMSAuth</UserID><Password>123456</Password></AuthInfo><Extension><Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
					" ";
	
	String txnResponse =spilServiceImpl.callSPILTransaction(xmlMsg);
	
	String respCode = txnResponse.substring(txnResponse.indexOf("<RespCode>")+10,txnResponse.indexOf("</RespCode>"));
	
	assertEquals(SUCCESS_RESPONSE_CODE,respCode);	
	
	if(SUCCESS_RESPONSE_CODE.equals(respCode))
		
	{
		
		String xmlMsg1="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ServiceProviderTxn><Version>3.1</Version><Request><MsgType>cardcardtransferrev</MsgType><DateTimeInfo><Date>20130121</Date><Time>122004</Time>\r\n" + 
				"<TimeZone>EST</TimeZone></DateTimeInfo><IncommRefNum>32589704</IncommRefNum><Origin><MerchName>InCommTest</MerchName><StoreInfo><StoreID>3631</StoreID>\r\n" + 
				"<TermID>CLV36300</TermID><StoreLoc><Address1>25 William St</Address1><Address2>Suite M100</Address2><City>Atlanta</City><State>GA</State><Zip>30303</Zip></StoreLoc></StoreInfo>\r\n" + 
				"<MerchRefNum>012504</MerchRefNum><LocaleInfo><CountryCode>US</CountryCode><CurrencyCode>USD</CurrencyCode><Language>EN</Language></LocaleInfo><POSInfo>\r\n" + 
				"<EntryMode>030</EntryMode><ConditionCode>00</ConditionCode></POSInfo><SourceInfo>POS</SourceInfo></Origin><Product><Track1>;6009004120322342</Track1>\r\n" + 
				"<Track2>5241810402835227</Track2><SPNumber>5241810402835227</SPNumber><TargetCardNumber>5241810402835228</TargetCardNumber><Fee>0.0</Fee><Value><Money>\r\n" + 
				"<Amount>25.0</Amount><CurrencyCode>USD</CurrencyCode></Money></Value></Product><AuthInfo><UserID>User1</UserID><Password>123456</Password></AuthInfo><Extension>\r\n" + 
				"<Name>Description</Name><Value>InComm XML Interface Spec</Value></Extension><Extension><Name>Address</Name><Value>Washington, D.C.</Value></Extension></Request></ServiceProviderTxn>\r\n" + 
				" ";
		
		String txnResponse1 =spilServiceImpl.callSPILTransaction(xmlMsg1);
		
		String respCode1= txnResponse1.substring(txnResponse1.indexOf("<RespCode>")+10,txnResponse1.indexOf("</RespCode>"));
		
		assertEquals(SUCCESS_RESPONSE_CODE,respCode1);		
	}
}
	
	
}
