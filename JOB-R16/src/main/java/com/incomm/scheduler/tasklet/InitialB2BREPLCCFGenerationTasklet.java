package com.incomm.scheduler.tasklet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.B2BREPLCCFGenerationDAO;
import com.incomm.scheduler.dao.CCFGenerationDAO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.model.CCFFormatDtl;
import com.incomm.scheduler.utils.AsyncServiceCall;
import com.incomm.scheduler.utils.HSMCommandBuilder;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.ResponseMessages;
import com.incomm.scheduler.utils.Util;

@Component
// @PropertySource("classpath:application.properties")
public class InitialB2BREPLCCFGenerationTasklet {
	 @Autowired
	  CCFGenerationDAO ccfGenerationDao;

	

	@Value("${CCF_HSM_IPADDRESS}")
	String hsmIpAddress;
	@Value("${CCF_HSM_PORT}")
	int hsmPort;
	
	@Value("${CCF_DIR_PATH}") String psDirPath;
	
	@Value("${CCF_NEW_DIR_PATH}") String psNewDirPath;

	private static final Logger logger = LogManager.getLogger(InitialB2BREPLCCFGenerationTasklet.class);

	HSMCommandBuilder hsmCommandBuilder = new HSMCommandBuilder();

	@Autowired
	B2BREPLCCFGenerationDAO b2BREPLCCFGenerationDAO;
	
	@Autowired
	AsyncServiceCall asyncServiceCall;


	private FileWriter moFileWriter = null;

	public void setB2BREPLCCFGenerationDAO(B2BREPLCCFGenerationDAO b2breplccfGenerationDAO) {
		b2BREPLCCFGenerationDAO = b2breplccfGenerationDAO;
	}

	private String ccfGenerationVersion = null;

	public void generateCcfFile(String orderType) throws IOException, ServiceException, ParseException {

		logger.info(CCLPConstants.ENTER);
		Map<String, Object> valueObj = new HashMap<>();
		Map<String, String> fileCreationCheck = new HashMap<>();
        Map<String, String> copyToArchiveMap = null;
        List<Map<String, String>> copyToArchiveList = new ArrayList<>();
    	Map<String, Object> mftApiKeyValMap = new HashMap<>();
		List<Map<String, Object>> mftApiKeyValList = ccfGenerationDao.getGlobalParams();

		mftApiKeyValList.stream().forEach(mftApiKeyVal -> mftApiKeyValMap
				.put(String.valueOf(mftApiKeyVal.get("ATTRIBUTE_NAME")), mftApiKeyVal.get("ATTRIBUTE_VALUE")));
		
		String isOutbound = String.valueOf(mftApiKeyValMap.get("mftOutbound"));
        
        List<String> processedOrderAndPackageId = new ArrayList<>();
		
		List<String> orders  = b2BREPLCCFGenerationDAO.getAllB2BReplOrdersToGenerateCCF(orderType);
		
		
		 logger.info("******orders******"+orders);
	        if(orders == null) {
	        	logger.error(JobConstants.NO_ORDER_TO_PROCESS);
	            throw new ServiceException(JobConstants.NO_ORDER_TO_PROCESS);
	        }
	        
        
	        Iterator<?> iterator = orders.iterator();
	        while (iterator.hasNext()) {

	        	Map<?, ?> map = (Map<?, ?>) iterator.next();
	        	String order = String.valueOf( map.get(JobConstants.CCF_ORDER_ID));
	             String orderLineItem = String.valueOf( map.get(JobConstants.CCF_ORDER_LINE_ITEM_ID));
	           	String packageIdTmp = String.valueOf(map.get(JobConstants.PACKAGE_ID));
	            String productIdTmp = String.valueOf(map.get(JobConstants.CCF_PRODUCT_ID));
	            
	            if(processedOrderAndPackageId.contains(order+orderLineItem+packageIdTmp+productIdTmp) ) {
	                logger.debug("Line item {} has already processed  ",orderLineItem);
	                updateOrderStatus(order,JobConstants.CCF_ORDER_STATUS_CCF_GENERATED,orderLineItem);
	            }else {
	            
	                 logger.info("Card Generation Started for orderId: "+order+" orderLineItem:"+orderLineItem);
	                 /**updateOrderStatus(order,JobConstants.CCF_ORDER_STATUS_INPROGRESS,orderLineItem);**/
	                 
	                 List<String> vendorIds  = ccfGenerationDao.getAllVendorsLinkedToOrder(order,orderLineItem);
	                 if(vendorIds == null) {
						 logger.error(JobConstants.NO_VENDOR_LINKED_TO_ORDER);
	                     throw new ServiceException(JobConstants.NO_VENDOR_LINKED_TO_ORDER);
	                 }
				Iterator<?> vendorIterator = vendorIds.iterator();
	            while (vendorIterator.hasNext()) {
	            	Map<?, ?> vendorMap = (Map<?, ?>) vendorIterator.next();
	            	String vendorId = String.valueOf( vendorMap.get(JobConstants.CCF_FULFILLMENT_VENDOR_ID));
	            	
	            	createFolder(vendorId, isOutbound);
	            String psFileName = this.getCCFFileName(vendorId,order,orderLineItem);
	            final List<StringBuilder> listofData = writeData(psFileName,vendorId, order, valueObj,orderLineItem);
	 
	            int grossRecCount = 0;
						if (listofData != null && !listofData.isEmpty()) {
							valueObj.put(JobConstants.CCF_HEADER_FILE_NUMBER, "1234");
							moFileWriter = this.getPhysicalCCFFile(vendorId, psFileName, isOutbound);
							logger.info("Writting CCF FIle0");
							grossRecCount = listofData.size() + 2;
							valueObj.put(JobConstants.CCF_HEADER_GROSS_RECCOUNT, String.valueOf(grossRecCount));
							valueObj.put(JobConstants.CCF_TRAILER_GROSS_RECCOUNT, String.valueOf(grossRecCount));

							writeHeader(valueObj);
							writeDetail(valueObj, listofData);
							writeTrailer(valueObj);
							moFileWriter.close();
							this.renamePhysicalCCFFile(vendorId, psFileName, isOutbound);
							logger.info("CCF Generation completed");

						}
						copyToArchiveMap= new HashMap<>();
                        copyToArchiveMap.put("psDirPath", psDirPath);
                        copyToArchiveMap.put("vendorId", vendorId);
                        copyToArchiveMap.put("psFileName", psFileName);
                        copyToArchiveMap.put("psNewDirPath", psNewDirPath);
                        copyToArchiveList.add(copyToArchiveMap);
                        
                        fileCreationCheck.put(vendorId, "Y");
                        
                        if ("enable".equalsIgnoreCase(isOutbound)) {
                        	int i = psFileName.lastIndexOf('.');
                            String name = psFileName.substring(0,i) +".csv";
                        	logger.info("call mft outbound for vendor: {}, file: {}",vendorId,name);
                        	asyncServiceCall.processOutboundFile(vendorId+":"+name);
            			}
                        
             	
             	updateOrderStatus(order,JobConstants.CCF_ORDER_STATUS_CCF_GENERATED,orderLineItem);
             }
             processedOrderAndPackageId.add(order+orderLineItem+packageIdTmp+productIdTmp);
	        }
	
  }
	if (!Objects.isNull(copyToArchiveList) && !copyToArchiveList.isEmpty()) {
		copyToArchiveList.forEach(copyArchiveMap -> {
			String oldDirPath = copyArchiveMap.get("psDirPath");
			String vendorId = copyArchiveMap.get("vendorId");
			String fileName = copyArchiveMap.get("psFileName");
			String newDirPath = copyArchiveMap.get("psNewDirPath");

		
			if ("enable".equalsIgnoreCase(isOutbound)) {
				oldDirPath = oldDirPath + "MFT"+ JobConstants.CCF_FORWARD_SLASH;
			}
			
			try {
				copyToArchive(new File(oldDirPath), vendorId, fileName, new File(newDirPath));
			} catch (IOException e) {

				logger.error("Error occured while copying file"+e.getMessage());
			}

			});
		}
		logger.info(CCLPConstants.EXIT);
	}

public void copyToArchive(File rootDir, String vendorId,String psFileName, File newRootDir) throws IOException {
	
	logger.info(CCLPConstants.ENTER);
	String psStrPath = "";
	String newPsStrPath = "";
	
	psStrPath = new StringBuilder().append(rootDir).append("/").append(getVendorName(vendorId)).append("/").toString();
	newPsStrPath = new StringBuilder().append(newRootDir).append("/").append(getVendorName(vendorId)).toString();
	int i = psFileName.lastIndexOf('.');
    String name =psFileName.substring(0,i);
    logger.info("FileName"+name);
    File file =new File(psStrPath+File.separator+name+".csv");
    
	File newDir = new File(newPsStrPath + File.separator);

	if (!newDir.exists()) {
		newDir.mkdirs();
	}
	
	String copiedDir = newPsStrPath + File.separator+ name+".csv";
	// copying the file to a new location
	
	if (FileCopyUtils.copy(file, new File(copiedDir))>0) {
		logger.info("File Successfully Copied");
	} else {
		logger.info("Failed to Copy the file");
		
	}
	logger.info(CCLPConstants.EXIT);
}

	private String getCCFFileName(String vendorId, String order, String oderLineItem) throws ServiceException {
		logger.info("Entered in to getCCFFileName");
		Map<String, Object> procedureResponse = b2BREPLCCFGenerationDAO.getCCFFileName(vendorId, order, oderLineItem);

		if (!String.valueOf(procedureResponse.get("error")).equalsIgnoreCase("ok")) {
			logger.error(JobConstants.ERR_FILE_NAME);
			throw new ServiceException(JobConstants.ERR_FILE_NAME);
			
		}

		String embFileName = String.valueOf(procedureResponse.get("embfname"));
		logger.info("procedure response for getCCFFileName" +embFileName);
		logger.info("Exited from getCCFFileName");
		return embFileName;
	}

	private void updateOrderStatus(String orderId, String orderStatus, String orderLineItem) {
		logger.info("Entered in to updateOrderStatus ");
		b2BREPLCCFGenerationDAO.updateOrderStatus(orderId, orderStatus, orderLineItem);

	}

	private List<StringBuilder> writeData(String psFileName, String vendorId, String orderId,
			Map<String, Object> valueObj,  String orderLineItem)
			throws ServiceException, ParseException {
		logger.info("Entered in to writeData in to the Replacement CCF file");

		String srvCode = "";
		String psExpYY = null;
		String psExpMM = null;
		String addressone = null;
		String addresstwo = null;
		String addrcity = null;
		String cityStateZip = null;
		String stateCode = null;
		String pinCode = null;

		String msPan = "";

		java.sql.Date msExpiryDate;
		String msProdCode = "";
		String hashPancode = "";

		String msProxyNumber = "";

		int cardRecordCount = 1;
		String panLength = null;
		final StringBuilder tempSb = new StringBuilder();

		final List<StringBuilder> dataList = new LinkedList<>();

		try {
		List<Map<String, Object>> ccfDetails = b2BREPLCCFGenerationDAO.getAllOrdersToGenerateCCF(orderId, vendorId,
				orderLineItem);
		logger.info("getting the details for Replacement CCF file"+ccfDetails);

		for (Map<String, Object> ccfDtl : ccfDetails) {
			ccfDtl.put(JobConstants.CCF_CARD_RECORD_COUNT, String.valueOf(cardRecordCount));
			logger.info("recordcount"+cardRecordCount);

			msPan = String.valueOf( ccfDtl.get(JobConstants.CCF_PAN));
			msProdCode = String.valueOf(ccfDtl.get(JobConstants.CCF_PRODUCT_ID));

			srvCode = b2BREPLCCFGenerationDAO.getServiceCode(msProdCode);
			ccfDtl.put(JobConstants.CCF_SRV_CODE, srvCode);

			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date = sdf1.parse(String.valueOf(ccfDtl.get(JobConstants.CCF_EXPIRY_DATE)));
			msExpiryDate = new java.sql.Date(date.getTime());
			ccfDtl.put(JobConstants.CCF_EXPIRY_DATE, msExpiryDate);
			hashPancode = String.valueOf(ccfDtl.get(JobConstants.CCF_CARD_NUM_HASH));
			msProxyNumber = String.valueOf( ccfDtl.get(JobConstants.CCF_PROXY_NUMBER));

			ccfDtl.put(JobConstants.CCF_PROXY_NUMBER, msProxyNumber != null ? msProxyNumber : " ");

			if (panLength == null || (" ".equals(panLength))) {
				panLength = msPan != null ? String.valueOf(msPan.length()) : " ";
			}
			if (!Objects.isNull(ccfDtl.get(JobConstants.CHILD_ORDER_ID))) {
					valueObj.put(JobConstants.CHILD_ORDER_ID, ccfDtl.get(JobConstants.CHILD_ORDER_ID));
				}

			if (!Objects.isNull(ccfDtl.get("PARENTORDERID"))) {
				valueObj.put("parentOrderId", ccfDtl.get("PARENTORDERID"));
			}
			if (!Objects.isNull(ccfDtl.get("ORDERTYPE"))) {
				valueObj.put(JobConstants.CCF_ORDER_TYPE, ccfDtl.get("ORDERTYPE"));
			}
			
			if (!Objects.isNull(ccfDtl.get(JobConstants.SHIP_TO_ADDRESS1))) {
				valueObj.put(JobConstants.CCF_REP_ADDRESS_ONE, ccfDtl.get(JobConstants.SHIP_TO_ADDRESS1));
			}
			
			if (!Objects.isNull(ccfDtl.get(JobConstants.SHIP_TO_ADDRESS2))) {
				valueObj.put(JobConstants.CCF_REP_ADDRESS_TWO, ccfDtl.get(JobConstants.SHIP_TO_ADDRESS2));
			}
			if (!Objects.isNull(ccfDtl.get("CITY"))||!Objects.isNull(ccfDtl.get("STATE"))||!Objects.isNull(ccfDtl.get("POSTALCODE"))) {
				valueObj.put("shipToCityStatePostalcode", ccfDtl.get("CITY")+JobConstants.COMMA_SEPARATOR+ccfDtl.get("STATE")+JobConstants.COMMA_SEPARATOR+ccfDtl.get("POSTALCODE"));
			}
			if (!Objects.isNull(ccfDtl.get("COUNTRY"))) {
				valueObj.put("countryName", ccfDtl.get("COUNTRY"));
			}
			if (!Objects.isNull(ccfDtl.get("SHIPTOCOMPANYNAME"))) {
				valueObj.put("companyName", ccfDtl.get("SHIPTOCOMPANYNAME"));
			}

			ccfGenerationVersion = b2BREPLCCFGenerationDAO.getReplacementCCFFormatVersion(msProdCode);
			logger.info("ccfGenerationVersion"+ccfGenerationVersion);

			ccfDtl.put(JobConstants.CCF_VERSION_ID, ccfGenerationVersion);

			psExpYY = padLeft(String.valueOf(getYear(msExpiryDate)), 2, '0');
			if(psExpYY != null) {
			psExpYY = psExpYY.substring(psExpYY.length() - 2);
			}
			psExpMM = padLeft(String.valueOf(getMonth(msExpiryDate)), 2, '0');

			msProdCode = String.valueOf(ccfDtl.get(JobConstants.CCF_PRODUCT_ID));
			ccfDtl.put(JobConstants.CCF_FULL_NAME, (String) ccfDtl.get(JobConstants.DISP_NAME));
			ccfDtl.put(JobConstants.CCF_EMB_NAME, (String) ccfDtl.get(JobConstants.DISP_NAME));
			ccfDtl.put(JobConstants.CCF_EMB_NAME_TWO, JobConstants.EMPTY_STRING);

			List<?> address = b2BREPLCCFGenerationDAO.getAddressDetails(hashPancode);

			 for (Object addressDetail : address) {
	            	
	            	tempSb.delete(0, tempSb.length());
	            	
	                Map m = (Map) addressDetail;
	                addressone = m.get(JobConstants.CCF_ADDRESS_ONE) != null ?   Util.convertAsString( m.get(JobConstants.CCF_ADDRESS_ONE))
	                        : JobConstants.EMPTY_STRING;
	 
	                addresstwo = m.get(JobConstants.CCF_ADDRESS_TWO)!= null ?  Util.convertAsString(m.get(JobConstants.CCF_ADDRESS_TWO))
	                        : JobConstants.EMPTY_STRING;
	 
	                addrcity = m.get(JobConstants.CCF_ADDRESS_CITY) != null ?   Util.convertAsString( m.get(JobConstants.CCF_ADDRESS_CITY))
	                        : JobConstants.EMPTY_STRING;
	                cityStateZip = addrcity;
	                stateCode = m.get(JobConstants.CCF_STATE_CODE) != null ? Util.convertAsString( m.get(JobConstants.CCF_STATE_CODE))
	                        : JobConstants.EMPTY_STRING;
	                cityStateZip = tempSb.append(cityStateZip).append(JobConstants.COMMA_SEPARATOR).append(stateCode)
							.toString();
	                tempSb.delete(0, tempSb.length());
	                pinCode = m.get(JobConstants.CCF_PIN_CODE) != null ?  Util.convertAsString( m.get(JobConstants.CCF_PIN_CODE))
	                        : JobConstants.EMPTY_STRING;
	                cityStateZip = tempSb.append(cityStateZip).append(JobConstants.COMMA_SEPARATOR).append(pinCode)
							.toString();
	                
	                ccfDtl.put(JobConstants.CCF_ADDRESS_ONE, addressone);
	                ccfDtl.put(JobConstants.CCF_ADDRESS_TWO, addresstwo);
	                ccfDtl.put(JobConstants.CCF_CITY_STATE,cityStateZip);
	           
	            }

			String cvvSupported = b2BREPLCCFGenerationDAO.getCvvSupportedFlag(msProdCode);
			if (cvvSupported.equalsIgnoreCase(JobConstants.CCF_ENABLE)) {

				logger.debug("CVV support is enabled for this product");
				tempSb.delete(0, tempSb.length());
				final String cvk = b2BREPLCCFGenerationDAO.getCVK(msProdCode);
				logger.debug("CVK: {}", cvk);
				tempSb.delete(0, tempSb.length());

				String cvv1 = hsmCommandBuilder.genCVV(padRight(msPan, msPan.length(), JobConstants.EMPTY_SPACE),
						psExpYY + psExpMM, srvCode, cvk, hsmIpAddress, hsmPort);
				ccfDtl.put(JobConstants.CCF_CVV_1, cvv1);
				ccfDtl.put(JobConstants.FIVE_DIGIT_CSC, cvv1);
				logger.debug("cvv1: {}", cvv1);
				String cvv2 = hsmCommandBuilder.genCVV(padRight(msPan, msPan.length(), JobConstants.EMPTY_SPACE),
						psExpMM + psExpYY, JobConstants.TRIPLE_ZERO, cvk, hsmIpAddress, hsmPort);
				logger.debug("cvv2: {}", cvv2);
				ccfDtl.put(JobConstants.CCF_CVV_2, cvv2);
				ccfDtl.put(JobConstants.THREE_DIGIT_CSC, cvv2);
				
			}
			cardRecordCount++;
			StringBuilder detailSB = new StringBuilder();
			logger.info("calling ccfFormation");
			ccfFormation(ccfDtl, ccfDtl.get(JobConstants.CCF_VERSION_ID).toString(), JobConstants.CCF_DTL_PART,
					detailSB);

			String endValue = JobConstants.CCF_SLASH_N;
			detailSB.append(endValue);
			dataList.add(detailSB);
			int i = psFileName.lastIndexOf('.');
            String name =psFileName.substring(0,i); 
            String FileName = name+".csv";
            updateCardStatus(hashPancode, FileName);
		}
		}catch(Exception e) {
			logger.error(e.getMessage());
        	throw new ServiceException(JobConstants.ERROR_WRITE_DETAILS);
		}
		valueObj.put(JobConstants.CCF_PAN_LENGTH, panLength);
		logger.info("Exit from getting the details for Replacement CCF file");

		return dataList;
	}

	private void updateCardStatus(String hashPancode, String psFileName) {
		b2BREPLCCFGenerationDAO.updateCardStatus(hashPancode, psFileName, JobConstants.CCF_ORDER_STATUS_CCF_GENERATED);
		b2BREPLCCFGenerationDAO.updateReplacementCard(hashPancode, JobConstants.PRINTERSENT);

	}

	/**
	 * 
	 * @param valueObj
	 * @param versionIdCCF
	 * @param listofData
	 * @throws ServiceException
	 */
	private void writeDetail(Map<String, Object> valueObj, List<StringBuilder> listofData) throws ServiceException {
		logger.info("Entered in to writedata");

		try {
			for (StringBuilder tempdtl : listofData) {
				if (tempdtl.toString().length() != 1) {
					moFileWriter.write(tempdtl.toString());
				}
			}
		} catch (Exception e) {
			logger.error(JobConstants.ERROR_WRITE_DETAILS+e.getMessage());
			throw new ServiceException(JobConstants.ERROR_WRITE_DETAILS);
		}
		logger.info("Exited from writedata");
	}

	private void createFolder(String vendorId, String isOutbound) throws ServiceException {
		logger.info("Entered into create folder");
		File poDir = null;

		poDir = new File(psDirPath);

		try {
			if (!poDir.exists()) {
				poDir.mkdirs();
			}
		} catch (Exception e) {
			logger.error(JobConstants.CCF_ERROR_CREATE_FOLDER + e.getMessage());
			throw new ServiceException(JobConstants.CCF_ERROR_CREATE_FOLDER);
		}

		if ("enable".equalsIgnoreCase(isOutbound))
			poDir = new File(new StringBuilder().append(psDirPath).append("MFT").append(JobConstants.CCF_FORWARD_SLASH)
					.append(getVendorName(vendorId)).append(JobConstants.CCF_FORWARD_SLASH).toString());
		else
			poDir = new File(new StringBuilder().append(psDirPath).append(getVendorName(vendorId))
					.append(JobConstants.CCF_FORWARD_SLASH).toString());
		
		logger.info("poDir" + poDir);
		try {
			if (!poDir.exists()) {
				poDir.mkdirs();
			}
		} catch (Exception e) {
			logger.error(JobConstants.CCF_ERROR_CREATE_FOLDER + e.getMessage());
			throw new ServiceException(JobConstants.CCF_ERROR_CREATE_FOLDER);
		}
		logger.info("Exit from create folder");
	}

	private String getVendorName(String vendorId) {
		logger.info("getting vendor Name based on vendorId");
		String vendorName = "";
		vendorName = b2BREPLCCFGenerationDAO.getVendorNameForId(vendorId);
		logger.info("vendor Name using vendorId"+vendorName);
		return vendorName;
	}

	public FileWriter getPhysicalCCFFile(final String vendorId, final String msFileName, String isOutbound)
			throws IOException, ServiceException {
		logger.info("Entered in to getPhysicalCCFFile");
		File poDir = null;
		String psFilePath = "";
		String psStrPath = "";

		poDir = new File(psDirPath);
		logger.info("Directory Path"+poDir);
		try {
			if (!poDir.exists()) {
				poDir.mkdirs();
			}
		} catch (Exception e) {
			logger.info(JobConstants.ERROR_CCF_FILE_CREATE + e.getMessage());
			throw new ServiceException(JobConstants.ERROR_CCF_FILE_CREATE);
		}

		if ("enable".equalsIgnoreCase(isOutbound)) {
			psStrPath = new StringBuilder().append(psDirPath).append("MFT").append(JobConstants.CCF_FORWARD_SLASH)
					.append(getVendorName(vendorId)).append("/").toString();
		}else {
			psStrPath = new StringBuilder().append(psDirPath).append(getVendorName(vendorId)).append("/").toString();
		}
		
		logger.info("file Path"+psStrPath);

		File f = new File(psStrPath);
		logger.info("file Name"+f);

		if (!f.isDirectory()) {
			f.mkdir();
		}

		psFilePath = psStrPath + msFileName;
	
		logger.info("Exit from getPhysicalCCFFile");
		return new FileWriter(psFilePath, true);
	}

	/**
	 * 
	 * @param tempValueHashMap
	 * @param object
	 * @param dtlPart
	 * @param valuSb
	 */
	private void ccfFormation(Map<String, Object> tempValueHashMap, String ccfFormatVersion, String dtlPart,
			StringBuilder valuSb) {
		logger.info("Entered in to ccfFormation");
		String tempValue = "";
		final List<CCFFormatDtl> ccfFormat = loadCCFVersionDtls(ccfFormatVersion, dtlPart);
		logger.debug("ccfFormat details"+ccfFormat);

		if (ccfFormat != null) {
			for (CCFFormatDtl tempCCFFormatDtl : ccfFormat) {

				if (!Util.isEmptyString(tempCCFFormatDtl.getDefaultValue())) {
					valuSb.append(tempCCFFormatDtl.getDefaultValue());
				} else if ("H_GENERATE_DATE".equalsIgnoreCase(tempCCFFormatDtl.getTitleName())) {
					final SimpleDateFormat sdf = new SimpleDateFormat(tempCCFFormatDtl.getFormat());
					tempValue = sdf.format(new java.sql.Date(new java.util.Date().getTime()));
					valuSb.append(tempValue);
				} else {
					tempValue =  Util.convertAsString(tempValueHashMap.get(tempCCFFormatDtl.getValueKey()));
					logger.debug("tempvalues : "+tempValue);

					final String tempVal = "L".equals(tempCCFFormatDtl.getFillerSide())
							? padLeft(tempValue, Integer.parseInt(tempCCFFormatDtl.getDataLength()),
									tempCCFFormatDtl.getFiller().charAt(0))
							: padRight(tempValue, Integer.parseInt(tempCCFFormatDtl.getDataLength()),
									tempCCFFormatDtl.getFiller().charAt(0), false);
					valuSb.append(tempVal);

				}

			}
		} else {
			logger.error("Error while getting ccf format detail");
		}
		logger.info("Exit from ccfFormation");
	}

	public static String padRight(final String str, final int length, final char c) {
		int needed;

		if (str == null) {
			needed = length;
		} else {
			needed = length - str.length();
		}
		if (needed <= 0) {
			return str;
		}
		final char[] padding = new char[needed];
		java.util.Arrays.fill(padding, c);
		StringBuilder sb = new StringBuilder(length);
		if (str != null) {
			sb.append(str);
		}
		sb.append(padding);
		return sb.toString();
	}

	public static String padRight(final String str, final int length, final char c, final boolean upperCaseFlag) {
	
		int needed;

		if (str == null) {
			needed = length;
		} else {
			needed = length - str.length();
		}
		if (needed <= 0 && str != null) {
			return str.substring(0, str.length());
		}
		final char[] padding = new char[needed];
		java.util.Arrays.fill(padding, c);
		StringBuilder sb = new StringBuilder(length);
		if (str != null) {
			sb.append(str);
		}
		sb.append(padding);
		if (upperCaseFlag) {
			return sb.toString().toUpperCase();
		} else {
			return sb.toString();
		}
		
	}

	private List<CCFFormatDtl> loadCCFVersionDtls(String version, String recType) {
		logger.info("Entered in to LoadCCFVersionDtls");
		HashMap<String, List<CCFFormatDtl>> ccfVersionDtls = new HashMap<>();
		List<CCFFormatDtl> ccfFormatDtlList = new ArrayList<>();
		try {
			List<?> ccfVersionDetails = b2BREPLCCFGenerationDAO.getCcfVersionDetails(version, recType);
			logger.debug("CCFVersionDtls"+ccfVersionDetails);
			ccfVersionDtls.clear();
			for (Object ccfVersionDetail : ccfVersionDetails) {
				CCFFormatDtl temp = new CCFFormatDtl();

				Map m = (Map) ccfVersionDetail;

				temp.setTitleName(String.valueOf( m.get("v5")));
				temp.setRecordType(String.valueOf( m.get("v2")));
				temp.setDataLength(String.valueOf( m.get("v7")));
				temp.setDefaultValue(String.valueOf(m.get("v6")));
				temp.setFormat(String.valueOf( m.get("v8")));
				temp.setFiller(String.valueOf( m.get("v9")));
				temp.setFillerSide(String.valueOf( m.get("v10")));
				temp.setValueKey(String.valueOf( m.get("v4")));
				temp.setFormatType(String.valueOf( m.get("v3")));
				final String versionType = String.valueOf(m.get("v1"));
				ccfFormatDtlList =  ccfVersionDtls.get(versionType);

				if (ccfFormatDtlList == null) {
					ccfVersionDtls.put(String.valueOf( m.get("v1")), new LinkedList<CCFFormatDtl>());
				}
				if ("H".equalsIgnoreCase(temp.getRecordType())) {
					ccfVersionDtls.get(versionType).add(temp);
				} else if ("D".equalsIgnoreCase(temp.getRecordType())) {
					ccfVersionDtls.get(versionType).add(temp);
				} else if ("T".equalsIgnoreCase(temp.getRecordType())) {
					ccfVersionDtls.get(versionType).add(temp);
				}else {
                	logger.error("Missing record type");
                }
			}
		} catch (Exception e) {
			logger.error("Error in getting CCF Version details"+e.getMessage());

		}
		logger.info("Exit loadCCFVersionDtls");
		return ccfFormatDtlList;
	}

	public static int getYear(java.sql.Date date) {
		logger.info("Entered in to getYear");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		logger.info("exit getYear");
		return calendar.get(Calendar.YEAR);
		
		
	}

	public static int getMonth(java.sql.Date date) {
		logger.info("Entered in to getMonth");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// since calenadar class gives month as 0-jan 1-feb,we add 1 before returning
		logger.info("Exit getMonth");
		// month
		return (calendar.get(Calendar.MONTH) + 1);
	}

	public static String padLeft(final String str, final int length, final char c) {
	
		int needed;
		
		if (str == null) {
			needed = length;
		} else {
			needed = length - str.length();
		}

		if (needed <= 0) {
			return str;
		}
		final char[] padding = new char[needed];
		java.util.Arrays.fill(padding, c);
		StringBuilder sb = new StringBuilder(length);
		sb.append(padding);
		if (str != null) {
			sb.append(str);
		}
		return sb.toString();
	}

	private void writeHeader(final Map<String, Object> valueObj) {
		logger.info("Entered in to writeHeader");
		final StringBuilder headerSb = new StringBuilder();
		try {
			ccfFormation(valueObj, ccfGenerationVersion, JobConstants.CCF_HEADER_PART, headerSb);
			headerSb.append(JobConstants.CCF_SLASH_N);
			moFileWriter.append(headerSb.toString());
		} catch (Exception e) {
			logger.error("Exception in writeHeader Method"+e.getMessage());
		}
		logger.info("Exit writeHeader");
	}

	private void writeTrailer(final Map<String, Object> valueObj) {
		logger.info("Entered in to writeTrailer");

		final StringBuilder trailerSb = new StringBuilder();
		try {
			ccfFormation(valueObj, ccfGenerationVersion, JobConstants.CCF_TRAILER_PART, trailerSb);
			trailerSb.append(JobConstants.CCF_SLASH_N);
			moFileWriter.append(trailerSb.toString());

		} catch (Exception e) {
			logger.error("Exception in writeTrailer Method"+e.getMessage());
		}
		logger.info("Exit from  writeTrailer");

	}
	
	public void renamePhysicalCCFFile(final String vendorId, final String msFileName, String isOutbound) throws ServiceException {
        
		logger.info(CCLPConstants.ENTER);
		File poDir = null;
        String psStrPath = "";
		poDir = new File(psDirPath);
        try {
            if (!poDir.exists()) {
                logger.error("Directiry Does not exists");
            }
        } catch (Exception e) {
        	logger.error(e.getMessage());
            throw new ServiceException(ResponseMessages.CCF_ERROR_CCF_FILE_CREATE);
        }
 
        if ("enable".equalsIgnoreCase(isOutbound)) {
        	psStrPath = new StringBuilder().append(psDirPath).append("MFT").append(JobConstants.CCF_FORWARD_SLASH).append(getVendorName(vendorId)).append("/").toString();
        }else {
        	psStrPath = new StringBuilder().append(psDirPath).append(getVendorName(vendorId)).append("/").toString();
        }
        
          int i = msFileName.lastIndexOf('.');
        String name =msFileName.substring(0,i);
        logger.info("FileName"+name);
        boolean success = new File(psStrPath+"/"+msFileName).renameTo(new File(psStrPath+"/"+name+".csv"));
        if (!success) {
        	 logger.debug("FileName renamed unsuccesfully :"+name);
        }
        logger.info(CCLPConstants.EXIT);
	}

}