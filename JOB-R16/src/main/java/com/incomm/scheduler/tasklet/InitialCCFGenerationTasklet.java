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
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.CCFGenerationDAO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.model.CCFFormatDtl;
import com.incomm.scheduler.utils.AsyncServiceCall;
import com.incomm.scheduler.utils.HSMCommandBuilder;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.ResponseMessages;
import com.incomm.scheduler.utils.Util;
 
@Component
//@PropertySource("classpath:application.properties")
public class InitialCCFGenerationTasklet {
 
    
	@Value("${CCF_DIR_PATH}") String psDirPath;
	@Value("${CCF_NEW_DIR_PATH}") String psNewDirPath;
	
	@Value("${CCF_HSM_IPADDRESS}") String hsmIpAddress;
	@Value("${CCF_HSM_PORT}") int hsmPort;
	
	@Autowired
	AsyncServiceCall asyncServiceCall;
	
	private static final Logger logger = LogManager.getLogger(InitialCCFGenerationTasklet.class);
	
    HSMCommandBuilder hsmCommandBuilder = new HSMCommandBuilder();
    
    @Autowired
    CCFGenerationDAO ccfGenerationDao;
 
    public void setCCFGenerationDAOObj(CCFGenerationDAO ccfGenerationDao){
    	 this.ccfGenerationDao = ccfGenerationDao;
    }
 
    private FileWriter moFileWriter = null;
 
    String ccfGenerationVersion = null;
 
    public void generateCcfFile(String orderType) throws IOException, ServiceException, ParseException {
		logger.info(CCLPConstants.ENTER);
        Map<String, Object> valueObj = new HashMap<>();
        Map<String, String> fileCreationCheck = new HashMap<>();
        Map<String, String> copyToArchiveMap = null;
        List<Map<String, String>> copyToArchiveList = new ArrayList<>();
        Boolean isSuccess =true;
        Map<String, Object> mftApiKeyValMap = new HashMap<>();
		List<Map<String, Object>> mftApiKeyValList = ccfGenerationDao.getGlobalParams();

		mftApiKeyValList.stream().forEach(mftApiKeyVal -> mftApiKeyValMap
				.put(String.valueOf(mftApiKeyVal.get("ATTRIBUTE_NAME")), mftApiKeyVal.get("ATTRIBUTE_VALUE")));
		
		String isOutbound = String.valueOf(mftApiKeyValMap.get("mftOutbound"));
        
        List<String> processedOrderAndPackageId = new ArrayList<>();
        
        List<String> separateCCForders = ccfGenerationDao.getAllOrdersToGenerateSeparateCCF(orderType);
		logger.info("Orders to generate Seperate CCF file : "+separateCCForders.toString());
        if(separateCCForders != null || !CollectionUtils.isEmpty(separateCCForders)) {
        	List<String> processedOrderAndFulfillment = new ArrayList<>();
        	Iterator<?> separateCCFiterator = separateCCForders.iterator();
        	while(separateCCFiterator.hasNext()) 
        		CCFforIndividualCCFflag(separateCCFiterator.next(),orderType,copyToArchiveList,processedOrderAndFulfillment);
        }
        
        valueObj.put("orderType", orderType);
        List<String> orders  = ccfGenerationDao.getAllOrdersToGenerateCCF(orderType);
        logger.info("orders to generate CCF" + orders);
        if(orders == null) {
 			logger.error(JobConstants.NO_ORDER_TO_PROCESS);
        	throw new ServiceException(JobConstants.NO_ORDER_TO_PROCESS);
        }
        Iterator<?> iterator = orders.iterator();
        while (iterator.hasNext()) {
           Map<?, ?> map = (Map<?, ?>) iterator.next();
            String order = String.valueOf( map.get(JobConstants.CCF_ORDER_ID));
            String orderLineItem = String.valueOf(map.get(JobConstants.CCF_ORDER_LINE_ITEM_ID));
          
        	String packageIdTmp = String.valueOf(map.get(JobConstants.PACKAGE_ID));
            String productIdTmp = String.valueOf(map.get(JobConstants.CCF_PRODUCT_ID));
            if(processedOrderAndPackageId.contains(order+orderLineItem+packageIdTmp+productIdTmp) ) {
                logger.info("Line item {} has already processed  ",orderLineItem);
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
                 	if(!fileCreationCheck.containsKey(vendorId)) {
                
                 			createFolder(vendorId, isOutbound);
                            String psFileName = this.getCCFFileName(vendorId,order,orderLineItem);
							logger.info("FileName"+psFileName);
                            final List<StringBuilder> listofData = writeData(psFileName,vendorId, null, valueObj, orderLineItem);//replaced order with null --by hari
                 
                 
                            if (listofData != null && !listofData.isEmpty()) {
                               	valueObj.put(JobConstants.CCF_HEADER_FILE_NUMBER, "1234");
                                moFileWriter = this.getPhysicalCCFFile(vendorId, psFileName, isOutbound);
                                logger.info("Writting CCF FIle0");
                                
                                writeDetail(listofData);
                                moFileWriter.close();
                                
                                this.renamePhysicalCCFFile(vendorId, psFileName, isOutbound);
                                logger.info("CCF Generation completed");
                                 
                               
                            }
                            else {
                            	isSuccess = false;
                            }
                            copyToArchiveMap= new HashMap<>();
                            copyToArchiveMap.put("psDirPath", psDirPath);
                            copyToArchiveMap.put("vendorId", vendorId);
                            copyToArchiveMap.put("psFileName", psFileName);
                            copyToArchiveMap.put("psNewDirPath", psNewDirPath);
                            copyToArchiveList.add(copyToArchiveMap);
                            /*copyToArchive(new File(psDirPath),vendorId, psFileName, new File(psNewDirPath));*/
                            
                            fileCreationCheck.put(vendorId, "Y");
                            if ("enable".equalsIgnoreCase(isOutbound)) {
                            	
                            	int i = psFileName.lastIndexOf('.');
                                String name = psFileName.substring(0,i) +".csv";
                                logger.info("call mft outbound for vendor: {}, file: {}",vendorId,name);
                            asyncServiceCall.processOutboundFile(vendorId+":"+name);
                            }
                 	}
                 	if(isSuccess)
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
					logger.error("Error occured while copying file" + e.getMessage());
				}

			});
		}

		logger.info(CCLPConstants.EXIT);
	}
 
	public void copyToArchive(File rootDir, String vendorId, String psFileName, File newRootDir) throws IOException {

		logger.info(CCLPConstants.ENTER);
		String psStrPath = "";
		String newPsStrPath = "";

		psStrPath = new StringBuilder().append(rootDir).append("/").append(getVendorName(vendorId)).append("/")
				.toString();
		newPsStrPath = new StringBuilder().append(newRootDir).append("/").append(getVendorName(vendorId)).toString();
		int i = psFileName.lastIndexOf('.');
	    String name =psFileName.substring(0,i);
	    logger.info("FileName:"+name+" psStrPath: "+psStrPath+" newPsStrPath: "+newPsStrPath);
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

	private String getCCFFileName(String vendorId, String order,String oderLineItem) throws ServiceException {
    	Map<String, Object> procedureResponse = ccfGenerationDao.getCCFFileName(vendorId, order,oderLineItem);
    	
    	if(!String.valueOf(procedureResponse.get("error")).equalsIgnoreCase("ok")) {
    		throw new ServiceException(JobConstants.ERR_FILE_NAME);
		}
    	
    	return String.valueOf(procedureResponse.get("embfname"));
	}

	private void updateOrderStatus(String orderId, String orderStatus,String orderLineItem) {
        ccfGenerationDao.updateOrderStatus(orderId, orderStatus,orderLineItem);
        
    }
 
    private List<StringBuilder> writeData(String psFileName,String vendorId, String orderId, Map<String, Object> valueObj1,
			String orderLineItem) throws ServiceException, ParseException {

    	logger.info(CCLPConstants.ENTER);
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
		StringBuilder headerWithDetailSb = null;
		StringBuilder alldetailsSb = new StringBuilder();
		Map<String, Object> oldOrderValueObj = null;
		int recordCount = 0;
		String currentOrderId = "";
		int grossRecCount = 0;

		int cardRecordCount = 1;
		String panLength = null;
		final StringBuilder tempSb = new StringBuilder();

		final List<StringBuilder> dataList = new LinkedList<>();
	
		try {
			String orderType = Util.convertAsString(valueObj1.get("orderType"));
			if (orderType.equalsIgnoreCase("RTL")) {
				orderType = "RETAIL";
			} else {
				orderType = "B2B";
			}
			List<Map<String, Object>> ccfDetails;
			// List<Map<String, Object>>
			if (orderId == null || Util.isEmpty(orderId))
				ccfDetails = ccfGenerationDao.getAllOrdersToGenerateCCFF(vendorId, orderType);
			else
				ccfDetails = ccfGenerationDao.getAllOrdersToGenerateSeparateCFF(vendorId, orderType, orderId);

			for (Iterator<Map<String, Object>> iterator = ccfDetails.iterator(); iterator.hasNext();) {

				Map<String, Object> valueObj = iterator.next();
				String orderID = Util.convertAsString(valueObj.get(JobConstants.CHILD_ORDER_ID));

				valueObj.put(JobConstants.CCF_CARD_RECORD_COUNT, String.valueOf(cardRecordCount));

				msPan = String.valueOf(valueObj.get(JobConstants.CCF_PAN));
				msProdCode = String.valueOf(valueObj.get(JobConstants.CCF_PRODUCT_ID));
				logger.debug("ProductId for order {}", msProdCode);
				srvCode = ccfGenerationDao.getServiceCode(msProdCode);

				valueObj.put(JobConstants.CCF_SRV_CODE, srvCode);

				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
				java.util.Date date = sdf1.parse(String.valueOf(valueObj.get(JobConstants.CCF_EXPIRY_DATE)));
				msExpiryDate = new java.sql.Date(date.getTime());
				valueObj.put(JobConstants.CCF_EXPIRY_DATE, msExpiryDate);
				hashPancode = String.valueOf(valueObj.get(JobConstants.CCF_CARD_NUM_HASH));

				msProxyNumber = String.valueOf(valueObj.get(JobConstants.CCF_PROXY_NUMBER));

				valueObj.put(JobConstants.CCF_PROXY_NUMBER, msProxyNumber != null ? msProxyNumber : " ");

				if (panLength == null || (" ".equals(panLength))) {
					panLength = msPan != null ? String.valueOf(msPan.length()) : " ";
				}

				if (!Objects.isNull(valueObj.get(JobConstants.CHILD_ORDER_ID))) {
					valueObj1.put(JobConstants.CHILD_ORDER_ID, valueObj.get(JobConstants.CHILD_ORDER_ID));
				}

				if (!Objects.isNull(valueObj.get("PARENTORDERID"))) {
					valueObj1.put("parentOrderId", valueObj.get("PARENTORDERID"));
				}
				if (!Objects.isNull(valueObj.get("ORDERTYPE"))) {
					valueObj1.put("orderType", valueObj.get("ORDERTYPE"));
				} else {
					valueObj1.put("orderType", "");
				}

				if (!Objects.isNull(valueObj.get(JobConstants.SHIP_TO_ADDRESS1))) {
					valueObj1.put(JobConstants.CCF_REP_ADDRESS_ONE, valueObj.get(JobConstants.SHIP_TO_ADDRESS1));
				}

				if (!Objects.isNull(valueObj.get(JobConstants.SHIP_TO_ADDRESS2))) {
					valueObj1.put(JobConstants.CCF_REP_ADDRESS_TWO, valueObj.get(JobConstants.SHIP_TO_ADDRESS2));
				}
				if (!Objects.isNull(valueObj.get("CITY")) || !Objects.isNull(valueObj.get("STATE"))
						|| !Objects.isNull(valueObj.get("POSTALCODE"))) {
					valueObj1.put("shipToCityStatePostalcode", valueObj.get("CITY") + JobConstants.COMMA_SEPARATOR
							+ valueObj.get("STATE") + JobConstants.COMMA_SEPARATOR + valueObj.get("POSTALCODE"));
				}
				if (!Objects.isNull(valueObj.get("COUNTRY"))) {
					valueObj1.put("countryName", valueObj.get("COUNTRY"));
				}
				if (!Objects.isNull(valueObj.get("SHIPTOCOMPANYNAME"))) {
					valueObj1.put("companyName", valueObj.get("SHIPTOCOMPANYNAME"));
				}
				if (!Objects.isNull(valueObj.get("SHIPTONAME"))) {
					String shipname = valueObj.get("SHIPTONAME") + "";
					if (shipname.length() < 26) {
						valueObj1.put("shipToName", shipname);
					} else {
						valueObj1.put("shipToName", shipname.substring(0, 25));
					}
				}
				if (!Objects.isNull(valueObj.get("PROGRAMID"))) {
					valueObj1.put("programId", valueObj.get("PROGRAMID"));
				}
				if (!Objects.isNull(valueObj.get("PROGRAMID"))) {
					valueObj1.put("programId", valueObj.get("PROGRAMID"));
				}
				if (!Objects.isNull(valueObj.get("EMBNAME"))) {
					valueObj1.put("embName", valueObj.get("EMBNAME"));
				}
				if (!Objects.isNull(valueObj.get("EMBNAMETWO"))) {
					valueObj1.put("embNameTwo", valueObj.get("EMBNAMETWO"));
				}
				ccfGenerationVersion = ccfGenerationDao.getCCFFormatVersion(msProdCode);

				valueObj.put(JobConstants.CCF_VERSION_ID, ccfGenerationVersion);

				psExpYY = padLeft(String.valueOf(getYear(msExpiryDate)), 2, '0');
				if (psExpYY != null) {
					psExpYY = psExpYY.substring(psExpYY.length() - 2);
				}
				psExpMM = padLeft(String.valueOf(getMonth(msExpiryDate)), 2, '0');

				msProdCode = String.valueOf(valueObj.get(JobConstants.CCF_PRODUCT_ID));
				valueObj.put(JobConstants.CCF_FULL_NAME, String.valueOf(valueObj.get(JobConstants.DISP_NAME)));
				valueObj.put(JobConstants.CCF_EMB_NAME, String.valueOf(valueObj.get(JobConstants.DISP_NAME)));
				valueObj.put(JobConstants.CCF_EMB_NAME_TWO, JobConstants.EMPTY_STRING);

				List<?> address = ccfGenerationDao.getAddressDetails(hashPancode);

				for (Object addressDetail : address) {

					tempSb.delete(0, tempSb.length());

					Map m = (Map) addressDetail;
					addressone = m.get(JobConstants.CCF_ADDRESS_ONE) != null
							? String.valueOf(m.get(JobConstants.CCF_ADDRESS_ONE))
							: JobConstants.EMPTY_STRING;

					addresstwo = m.get(JobConstants.CCF_ADDRESS_TWO) != null
							? String.valueOf(m.get(JobConstants.CCF_ADDRESS_TWO))
							: JobConstants.EMPTY_STRING;

					addrcity = m.get(JobConstants.CCF_ADDRESS_CITY) != null
							? String.valueOf(m.get(JobConstants.CCF_ADDRESS_CITY))
							: JobConstants.EMPTY_STRING;
					cityStateZip = addrcity;
					stateCode = m.get(JobConstants.CCF_STATE_CODE) != null
							? String.valueOf(m.get(JobConstants.CCF_STATE_CODE))
							: JobConstants.EMPTY_STRING;
					cityStateZip = tempSb.append(cityStateZip).append(JobConstants.COMMA_SEPARATOR).append(stateCode)
							.toString();
					tempSb.delete(0, tempSb.length());
					pinCode = m.get(JobConstants.CCF_PIN_CODE) != null
							? String.valueOf(m.get(JobConstants.CCF_PIN_CODE))
							: JobConstants.EMPTY_STRING;
					cityStateZip = tempSb.append(cityStateZip).append(JobConstants.COMMA_SEPARATOR).append(pinCode)
							.toString();

					valueObj.put(JobConstants.CCF_ADDRESS_ONE, addressone);
					valueObj.put(JobConstants.CCF_ADDRESS_TWO, addresstwo);
					valueObj.put(JobConstants.CCF_CITY_STATE, cityStateZip);

				}

				String cvvSupported = ccfGenerationDao.getCvvSupportedFlag(msProdCode);
				if (cvvSupported.equalsIgnoreCase(JobConstants.CCF_ENABLE)) {
					logger.debug("CVV support is enabled for this product");
					tempSb.delete(0, tempSb.length());
					final String cvk = ccfGenerationDao.getCVK(msProdCode);
					logger.debug("CVK: {}", cvk);
					tempSb.delete(0, tempSb.length());

					String cvv1 = hsmCommandBuilder.genCVV(padRight(msPan, msPan.length(), JobConstants.EMPTY_SPACE),
							psExpYY + psExpMM, srvCode, cvk, hsmIpAddress, hsmPort);
					valueObj.put(JobConstants.CCF_CVV_1, cvv1);
					valueObj.put(JobConstants.FIVE_DIGIT_CSC, cvv1);

					String cvv2 = hsmCommandBuilder.genCVV(padRight(msPan, msPan.length(), JobConstants.EMPTY_SPACE),
							psExpMM + psExpYY, JobConstants.TRIPLE_ZERO, cvk, hsmIpAddress, hsmPort);

					valueObj.put(JobConstants.CCF_CVV_2, cvv2);
					valueObj.put(JobConstants.THREE_DIGIT_CSC, cvv2);

				}
				logger.info("Cards Record count so far is {}", cardRecordCount);
				cardRecordCount++;

				valueObj1.put(JobConstants.CCF_HEADER_FILE_NUMBER, "1234");
				valueObj1.put(JobConstants.CCF_PAN_LENGTH, panLength);

				if (currentOrderId.isEmpty()) {
					currentOrderId = orderID;
					oldOrderValueObj = new HashMap<>();
					oldOrderValueObj.putAll(valueObj1);
				}
				StringBuilder trailerSb = new StringBuilder();
				StringBuilder detailSB = new StringBuilder();
				ccfFormation(valueObj, valueObj.get(JobConstants.CCF_VERSION_ID).toString(), JobConstants.CCF_DTL_PART,
						detailSB);

				String endValue = JobConstants.CCF_SLASH_N;
				detailSB.append(endValue);
				if (!currentOrderId.isEmpty() && !currentOrderId.equals(orderID)) {
					currentOrderId = orderID;
					grossRecCount = recordCount + 2;
					oldOrderValueObj.put(JobConstants.CCF_HEADER_GROSS_RECCOUNT, String.valueOf(grossRecCount));
					oldOrderValueObj.put(JobConstants.CCF_TRAILER_GROSS_RECCOUNT, String.valueOf(grossRecCount));
					headerWithDetailSb = new StringBuilder();
					ccfFormation(oldOrderValueObj, ccfGenerationVersion, JobConstants.CCF_HEADER_PART,
							headerWithDetailSb);
					headerWithDetailSb.append(JobConstants.CCF_SLASH_N);
					ccfFormation(oldOrderValueObj, ccfGenerationVersion, JobConstants.CCF_TRAILER_PART, trailerSb);
					trailerSb.append(JobConstants.CCF_SLASH_N);
					headerWithDetailSb.append(alldetailsSb);
					headerWithDetailSb.append(trailerSb);
					dataList.add(headerWithDetailSb);
					alldetailsSb.setLength(0);
					recordCount=0;
					oldOrderValueObj=new HashMap<>();
					oldOrderValueObj.putAll(valueObj1);

				}
				alldetailsSb.append(detailSB);
				recordCount++;
				if (!iterator.hasNext()) {
					grossRecCount = recordCount + 2;
					oldOrderValueObj.put(JobConstants.CCF_HEADER_GROSS_RECCOUNT, String.valueOf(grossRecCount));
					oldOrderValueObj.put(JobConstants.CCF_TRAILER_GROSS_RECCOUNT, String.valueOf(grossRecCount));
					headerWithDetailSb = new StringBuilder();
					ccfFormation(oldOrderValueObj, ccfGenerationVersion, JobConstants.CCF_HEADER_PART,
							headerWithDetailSb);
					headerWithDetailSb.append(JobConstants.CCF_SLASH_N);
					ccfFormation(oldOrderValueObj, ccfGenerationVersion, JobConstants.CCF_TRAILER_PART, trailerSb);
					trailerSb.append(JobConstants.CCF_SLASH_N);
					headerWithDetailSb.append(alldetailsSb);
					headerWithDetailSb.append(trailerSb);
					dataList.add(headerWithDetailSb);
				}

				int i = psFileName.lastIndexOf('.');
				String name = psFileName.substring(0, i);
				String fileName = name + ".csv";
				updateCardStatus(hashPancode, fileName);

			}
			logger.info("cards has been successfully generated for ccf file: {}", psFileName);
		} catch (Exception e) {
			logger.error(JobConstants.ERROR_WRITE_DETAILS + ": " + e.getMessage());
			throw new ServiceException(JobConstants.ERROR_WRITE_DETAILS);
		}

		logger.info(CCLPConstants.EXIT);
		return dataList;
	}
 
    private void updateCardStatus(String hashPancode, String psFileName) {
        ccfGenerationDao.updateCardStatus(hashPancode, psFileName, JobConstants.CCF_ORDER_STATUS_CCF_GENERATED);
        ccfGenerationDao.updateCard(hashPancode,JobConstants.PRINTERSENT);
        
    }
 
    /**
     * 
     * @param valueObj
     * @param versionIdCCF
     * @param listofData
     * @throws ServiceException 
     */
    private void writeDetail(List<StringBuilder> listofData) throws ServiceException {
 
        try {
            for (StringBuilder tempdtl : listofData) {
                if (tempdtl.toString().length() != 1) {
                    moFileWriter.write(tempdtl.toString());
                }
            }
        } catch (Exception e) {
        	logger.error("Error while writing the data in file"+e.getMessage());
            throw new ServiceException(JobConstants.ERROR_WRITE_DETAILS);
        }
    }
 
	private void createFolder(String vendorId, String isOutbound) throws ServiceException {
		  
		logger.info(CCLPConstants.ENTER);
		File poDir = null;

		poDir = new File(psDirPath);

		try {
			if (!poDir.exists()) {
				poDir.mkdirs();
			}
		} catch (Exception e) {
			logger.error(JobConstants.CCF_ERROR_CREATE_FOLDER+" : "+e.getMessage());
			throw new ServiceException(JobConstants.CCF_ERROR_CREATE_FOLDER);
		}
		if ("enable".equalsIgnoreCase(isOutbound))
			poDir = new File(new StringBuilder().append(psDirPath).append("MFT").append(JobConstants.CCF_FORWARD_SLASH)
					.append(getVendorName(vendorId)).append(JobConstants.CCF_FORWARD_SLASH).toString());
		else
			poDir = new File(new StringBuilder().append(psDirPath).append(getVendorName(vendorId))
					.append(JobConstants.CCF_FORWARD_SLASH).toString());
		try {
			if (!poDir.exists()) {
				poDir.mkdirs();
			}
		} catch (Exception e) {
			logger.error(JobConstants.CCF_ERROR_CREATE_FOLDER+" : "+e.getMessage());
			throw new ServiceException(JobConstants.CCF_ERROR_CREATE_FOLDER);
		}
		logger.info(CCLPConstants.EXIT);
	}
 
    private String getVendorName(String vendorId) {
    	String vendorName ="";
    	vendorName = ccfGenerationDao.getVendorNameForId(vendorId);
		return vendorName;
	}

	public FileWriter getPhysicalCCFFile(final String vendorId, final String msFileName, String isOutbound) throws IOException, ServiceException {
		
		logger.info(CCLPConstants.ENTER);
        File poDir = null;
        String psFilePath = "";
        String psStrPath = "";
        poDir = new File(psDirPath);
        try {
            if (!poDir.exists()) {
                poDir.mkdirs();
            }
        } catch (Exception e) {
        	logger.error(JobConstants.ERROR_CCF_FILE_CREATE +":" +e.getMessage());
            throw new ServiceException(JobConstants.ERROR_CCF_FILE_CREATE);
        }
        if ("enable".equalsIgnoreCase(isOutbound)) {
        psStrPath = new StringBuilder().append(psDirPath).append("MFT").append(JobConstants.CCF_FORWARD_SLASH)
        							   .append(getVendorName(vendorId)).append("/").toString();
        }else {
        	psStrPath = new StringBuilder().append(psDirPath)
					   .append(getVendorName(vendorId)).append("/").toString();
        }
 
        File f = new File(psStrPath);
 
        if (!f.isDirectory()) {
            f.mkdir();
        }
 
        psFilePath = psStrPath + msFileName;
        logger.info(CCLPConstants.EXIT);
        return new FileWriter(psFilePath, true);
    }
	
	public void renamePhysicalCCFFile(final String vendorId, final String msFileName, String isOutbound) throws ServiceException {
       
		 logger.info(CCLPConstants.ENTER);
		File poDir = null;
        String psStrPath = "";
        poDir = new File(psDirPath);
 
        try {
            if (!poDir.exists()) {
                logger.debug("Directiry Does not exists");
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
        	 logger.debug("FileName renamed unsuccesful"+name);
        }
        logger.info(CCLPConstants.EXIT);
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
        
    	String tempValue = "";
        final List<CCFFormatDtl> ccfFormat = loadCCFVersionDtls(ccfFormatVersion, dtlPart);
 
        if (ccfFormat != null) {
            for (CCFFormatDtl tempCCFFormatDtl : ccfFormat) {
 
                if (!Util.isEmptyString(tempCCFFormatDtl.getDefaultValue())) {
                    valuSb.append(tempCCFFormatDtl.getDefaultValue());
                } else if ("H_GENERATE_DATE".equalsIgnoreCase(tempCCFFormatDtl.getTitleName())) {
                    final SimpleDateFormat sdf = new SimpleDateFormat(tempCCFFormatDtl.getFormat());
                    tempValue = sdf.format(new java.sql.Date(new java.util.Date().getTime()));
                    valuSb.append(tempValue);
                } else {
                    tempValue = String.valueOf( tempValueHashMap.get(tempCCFFormatDtl.getValueKey()));
                    if(Util.isEmptyString(tempValue)){
                    	tempValue="";
                    }
                    final String tempVal = "L".equals(tempCCFFormatDtl.getFillerSide())
                            ? padLeft(tempValue, Integer.parseInt(tempCCFFormatDtl.getDataLength()),
                                    tempCCFFormatDtl.getFiller().charAt(0))
                            : padRight(tempValue, Integer.parseInt(tempCCFFormatDtl.getDataLength()),
                                    tempCCFFormatDtl.getFiller().charAt(0), false);
                    valuSb.append(tempVal);
 
                }
 
            }
        } else {
        	logger.info("Error while getting ccf format detail");
        }
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
            return str.substring(0, str.length() + needed);
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
 
        HashMap<String, List<CCFFormatDtl>> ccfVersionDtls = new HashMap<>();
        List<CCFFormatDtl> ccfFormatDtlList = new ArrayList<>();
        try {
            List ccfVersionDetails = ccfGenerationDao.getCcfVersionDetails(version, recType);
            ccfVersionDtls.clear();
            for (Object ccfVersionDetail : ccfVersionDetails) {
                CCFFormatDtl temp = new CCFFormatDtl();
 
                Map m = (Map) ccfVersionDetail;
 
                temp.setTitleName(String.valueOf(m.get("v5")));
                temp.setRecordType(String.valueOf( m.get("v2")));
                temp.setRecordType(String.valueOf( m.get("v2")));
                temp.setDataLength(String.valueOf( m.get("v7")));
                temp.setDefaultValue(String.valueOf( m.get("v6")));
                temp.setFormat(String.valueOf( m.get("v8")));
                temp.setFiller(String.valueOf( m.get("v9")));
                temp.setFillerSide(String.valueOf( m.get("v10")));
                temp.setValueKey(String.valueOf( m.get("v4")));
                temp.setFormatType(String.valueOf( m.get("v3")));
                final String versionType = String.valueOf( m.get("v1"));
                ccfFormatDtlList = ccfVersionDtls.get(versionType);
 
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
                	logger.debug("Missing record type");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ccfFormatDtlList;
    }
 
    public static int getYear(java.sql.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
 
    public static int getMonth(java.sql.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // since calenadar class gives month as 0-jan 1-feb,we add 1 before returning
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
        final StringBuilder headerSb = new StringBuilder();
        try {
            ccfFormation(valueObj, ccfGenerationVersion, JobConstants.CCF_HEADER_PART, headerSb);
            headerSb.append(JobConstants.CCF_SLASH_N);
            moFileWriter.append(headerSb.toString());
        } catch (Exception e) {
        	logger.error(e.getMessage());
        }
    }
 
    private void writeTrailer(final Map<String, Object> valueObj) {
 
        final StringBuilder trailerSb = new StringBuilder();
        try {
            ccfFormation(valueObj, ccfGenerationVersion, JobConstants.CCF_TRAILER_PART, trailerSb);
            trailerSb.append(JobConstants.CCF_SLASH_N);
            moFileWriter.append(trailerSb.toString());
 
        } catch (Exception e) {
        	logger.error(e.getMessage());
        }
 
    }
    
	private void CCFforIndividualCCFflag(Object object, String orderType, List<Map<String, String>> copyToArchiveList,List<String> processedOrderAndFulfillment)
			throws ServiceException, IOException, ParseException {

		logger.info(CCLPConstants.ENTER);
		
		Map<String, Object> valueObj = new HashMap<>();
		valueObj.put("orderType", orderType);
		Map<String, String> fileCreationCheck = new HashMap<>();
		Map<String, String> copyToArchiveMap = null;
		Boolean isSuccess = true;
		Map<String, Object> mftApiKeyValMap = new HashMap<>();
		List<Map<String, Object>> mftApiKeyValList = ccfGenerationDao.getGlobalParams();

		mftApiKeyValList.stream().forEach(mftApiKeyVal -> mftApiKeyValMap
				.put(String.valueOf(mftApiKeyVal.get("ATTRIBUTE_NAME")), mftApiKeyVal.get("ATTRIBUTE_VALUE")));
		
		String isOutbound = String.valueOf(mftApiKeyValMap.get("mftOutbound"));
	

		Map<?, ?> map = (Map<?, ?>) object;
		String order = String.valueOf(map.get(JobConstants.CCF_ORDER_ID));
		String orderLineItem = String.valueOf(map.get(JobConstants.CCF_ORDER_LINE_ITEM_ID));

		String packageId = String.valueOf(map.get(JobConstants.PACKAGE_ID));

			logger.info("Card Generation Started for orderId: " + order + " orderLineItem:" + orderLineItem);
			/**
			 * updateOrderStatus(order,JobConstants.CCF_ORDER_STATUS_INPROGRESS,orderLineItem);
			 **/

			List<String> vendorIds = ccfGenerationDao.getAllVendorsLinkedToOrderPackageId(order, packageId);
			if (vendorIds == null) {
				logger.error(JobConstants.NO_VENDOR_LINKED_TO_ORDER);
				throw new ServiceException(JobConstants.NO_VENDOR_LINKED_TO_ORDER);
			}

			Iterator<?> vendorIterator = vendorIds.iterator();
			while (vendorIterator.hasNext()) {

				Map<?, ?> vendorMap = (Map<?, ?>) vendorIterator.next();
				String vendorId = String.valueOf(vendorMap.get(JobConstants.CCF_FULFILLMENT_VENDOR_ID));
				
				if (processedOrderAndFulfillment.contains(order + vendorId)) {
					logger.info("Line item {} has already processed  ", orderLineItem);
					updateOrderStatus(order, JobConstants.CCF_ORDER_STATUS_CCF_GENERATED, orderLineItem);
				} else {
					if (!fileCreationCheck.containsKey(vendorId)) {

					createFolder(vendorId, isOutbound);
					String psFileName = this.getCCFFileName(vendorId, order, orderLineItem);
					final List<StringBuilder> listofData = writeData(psFileName, vendorId, order, valueObj,
							orderLineItem);

					if (listofData != null && !listofData.isEmpty()) {
						valueObj.put(JobConstants.CCF_HEADER_FILE_NUMBER, "1234");
						moFileWriter = this.getPhysicalCCFFile(vendorId, psFileName, isOutbound);
						logger.info("Writting CCF FIle0");

						writeDetail(listofData);
						moFileWriter.close();

						this.renamePhysicalCCFFile(vendorId, psFileName, isOutbound);
						logger.info("CCF Generation completed");

					} else {
						isSuccess = false;
					}
					copyToArchiveMap = new HashMap<>();
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
						asyncServiceCall.processOutboundFile(vendorId+":"+psFileName);
					}

				}
				if (isSuccess)
					updateOrderStatus(order, JobConstants.CCF_ORDER_STATUS_CCF_GENERATED, orderLineItem);
				processedOrderAndFulfillment.add(order + vendorId);
			}
		}	
		logger.info(CCLPConstants.EXIT);
	}
    
}