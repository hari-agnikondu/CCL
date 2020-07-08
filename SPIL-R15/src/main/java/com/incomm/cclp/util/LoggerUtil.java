package com.incomm.cclp.util;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.incomm.cclp.constants.LoggerConstants;
import com.incomm.cclp.constants.ValueObjectKeys;

import uk.org.simonsite.log4j.appender.TimeAndSizeRollingAppender;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class LoggerUtil extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final String supportfilesize;

	private final String supportfiledatepattern;

	private final int supportfilemaxroll;

	private final String serverId;

	private final Logger logger = LogManager.getLogger(this.getClass());

	private final org.apache.log4j.Logger loggerWS;

	private final org.apache.log4j.varia.LevelRangeFilter filterclass = new org.apache.log4j.varia.LevelRangeFilter();

	@Autowired
	public LoggerUtil(@Value("${cclp.supportlog.supportFilePath}") String supportFilePath,
			@Value("${cclp.supportlog.supportFileName}") String supportFileName,
			@Value("${cclp.supportlog.supportFileSize}") String supportfilesize,
			@Value("${cclp.supportlog.supportfiledatepattern}") String supportfiledatepattern,
			@Value("${cclp.supportlog.supportfilemaxroll}") int supportfilemaxroll) {
		loggerWS = org.apache.log4j.Logger.getLogger(this.getClass());
		this.supportfilesize = supportfilesize;
		this.supportfiledatepattern = supportfiledatepattern;
		this.supportfilemaxroll = supportfilemaxroll;
		String absolutefilepath = supportFilePath + supportFileName;
		serverId = getServerName();
		addAppenders(absolutefilepath);
	}

	public void addAppenders(String path) {

		try {
			loggerWS.removeAllAppenders();
			PatternLayout layout = new PatternLayout();
			TimeAndSizeRollingAppender appender = new TimeAndSizeRollingAppender(layout, path);
			appender.setDatePattern(supportfiledatepattern);
			appender.setMaxFileSize(supportfilesize);
			appender.setMaxRollFileCount(supportfilemaxroll);
			filterclass.setLevelMax(Level.WARN);
			filterclass.setLevelMin(Level.WARN);
			appender.addFilter(filterclass);
			appender.activateOptions();
			loggerWS.setAdditivity(false);
			loggerWS.addAppender(appender);

		} catch (Exception e) {
			logger.error("Exception occured in addAppenders(): " + e.getMessage(), e);
		}
	}

	public String getServerName() {
		String localHost = "";
		try {
			localHost = InetAddress.getLocalHost()
				.getHostName();
		} catch (Exception ex) {
			logger.error("Exception occured while getting local host: " + ex.getMessage(), ex);
		}
		return localHost;

	}

	/*
	 * This method will print the request and response of txns in to support.log file in the format specified in support
	 * properties file.- vms.log.supportKey
	 */
	public void logSupport(String msgTyp, String method, String payLoad, String processTime, String daoProcessTime,
			Map<String, String> valueObj) {

		String errMsg = "";
		String errCode = "";
		String respCode = "";
		String respMsg = "";
		String threadId = "";
		String intg = "";
		String delchnlDesc = "";
		String tranDesc = "";
		String delchnlTranDesc = "";
		String categoryDesc = "";
		String packageId = "";
		String vendor = "";
		String daoProcTime = "";
		String responseCode = "";
		String reqIp = "";
		String txnid = "";
		String supportKey = "";
		String msgType = "";

		try {
			supportKey = LoggerConstants.SUPPORT_KEY;
			threadId = serverId + Util.padLeft(String.valueOf(Thread.currentThread()
				.getId()), 10, "0");
			responseCode = String.valueOf(valueObj.get(ValueObjectKeys.RESP_CODE));
			txnid = String.valueOf(valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
			msgType = String.valueOf(valueObj.get(ValueObjectKeys.MSGTYPE));

			// 1.If its response trim spaces and new line chars
			if (LoggerConstants.PAYLOAD_MSGTYPE_RESPONSE.equals(msgTyp)) {

				if (payLoad != null && !("".equals(payLoad))) {
					payLoad = LoggerConstants.RAW_XML + payLoad;

					// retrieve response code
					if (payLoad.contains("<RespCode>")) {
						respCode = payLoad.substring(payLoad.indexOf("<RespCode>") + 10, payLoad.indexOf("</RespCode>"));
					}

					if (respCode == null || "".equals(respCode)) {
						respCode = responseCode;
					}

					respMsg = String.valueOf(valueObj.get(ValueObjectKeys.RESP_MSG));

					// Assign errorcode and Msg
					if (!"00".equals(respCode)) {
						errCode = respCode;
						errMsg = respMsg;
					}

				} else {
					respMsg = LoggerConstants.ERR_PAYLOAD_NOT_PRESENT;
					errMsg = respMsg;
				}

				// set Dao process time
				daoProcTime = daoProcessTime;
			}

			// 2.Get Time Zone
			String ts = Util.getDateWithTimeZone(LoggerConstants.DATE_TIMEZONE_FORMAT);

			// Get transaction Description
			delchnlDesc = String.valueOf(valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME));
			tranDesc = String.valueOf(valueObj.get(ValueObjectKeys.TRANSACTIONDESC));
			if (delchnlDesc != null && tranDesc != null) {
				if ("0400".equals(msgType)) {
					delchnlTranDesc = delchnlDesc + "-" + tranDesc + "-" + "Reversal";
				} else {
					delchnlTranDesc = delchnlDesc + "-" + tranDesc;
				}
			}

			// 3.Add values in Map
			Map<String, String> param = new HashMap<>();
			param.put("ts", ts);
			param.put("thread", Util.returnBlank(threadId));
			param.put("logHost", Util.returnBlank(serverId));
			param.put("reqIp", Util.returnBlank(reqIp));
			param.put("txnid", Util.returnBlank(txnid));
			param.put("msgTyp", Util.returnBlank(msgTyp));
			param.put("method", Util.returnBlank(method));
			param.put("payLoad", Util.returnBlank(payLoad));
			param.put("errMsg", Util.returnBlank(errMsg));
			param.put("errCode", Util.returnBlank(errCode));
			param.put("processTime", Util.returnBlank(processTime));
			param.put("respCode", Util.returnBlank(respCode));
			param.put("respMsg", Util.returnBlank(respMsg));
			param.put("intg", Util.returnBlank(intg));
			param.put("delchnl_tranDesc", Util.returnBlank(delchnlTranDesc));
			param.put("dao_processTime", Util.returnBlank(daoProcTime));
			param.put("categoryDesc", Util.returnBlank(categoryDesc));
			param.put("packageId", Util.returnBlank(packageId));
			param.put("vendor", Util.returnBlank(vendor));

			// 4.Iterate Map & substitute key with values
			for (Entry<String, String> entry : param.entrySet()) {
				supportKey = supportKey.replace("(" + entry.getKey() + ")", entry.getValue());
			}

			// 5.Write to support log
			loggerWS.warn(supportKey);

		} catch (Exception ex) {
			logger.error("Exception occured in writing Support Log: " + ex.getMessage(), ex);
		}

	}

}
