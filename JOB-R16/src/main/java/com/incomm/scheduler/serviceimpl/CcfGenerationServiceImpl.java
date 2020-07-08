package com.incomm.scheduler.serviceimpl;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.CCFGenerationDAO;
import com.incomm.scheduler.dao.CCFRowCallbackHandler;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.model.CCFFormatDtl;
import com.incomm.scheduler.notification.NotificationService;
import com.incomm.scheduler.service.CcfGenerationService;
import com.incomm.scheduler.utils.AsyncServiceCall;
import com.incomm.scheduler.utils.HSMCommandBuilder;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.ResponseMessages;
 
@Service
public class CcfGenerationServiceImpl implements CcfGenerationService {
 
    
	@Value("${CCF_DIR_PATH}") String psDirPath;
	@Value("${CCF_HSM_IPADDRESS}") String hsmIpAddress;
	@Value("${CCF_HSM_PORT}") int hsmPort;
	@Value("${CCF_NEW_DIR_PATH}") String psNewDirPath;

	
	
	private static final Logger logger = LogManager.getLogger(CcfGenerationServiceImpl.class);
	
    HSMCommandBuilder hsmCommandBuilder = new HSMCommandBuilder();
    
    @Autowired
    CCFGenerationDAO ccfGenerationDao;
    
	@Autowired
	NotificationService notificationService;
 
	@Autowired
	AsyncServiceCall asyncServiceCall;
	
    private FileWriter moFileWriter = null;
 
    String ccfGenerationVersion = "";
    
    private static final String CCF_SUBJECT ="CCF Generation Status";
 
    @Override
    @Async
	public void generateCcfFile(String[] manualOrders,
			String userId) /* throws IOException, ServiceException, ParseException */ {

		logger.info(CCLPConstants.ENTER);
		String orderStatus = null;
		Map<String, Object> valueObj = new HashMap<>();
		List<String> orders = new ArrayList<>();

		Map<String, Object> mftApiKeyValMap = new HashMap<>();
		List<Map<String, Object>> mftApiKeyValList = ccfGenerationDao.getGlobalParams();

		mftApiKeyValList.stream().forEach(mftApiKeyVal -> mftApiKeyValMap
				.put(String.valueOf(mftApiKeyVal.get("ATTRIBUTE_NAME")), mftApiKeyVal.get("ATTRIBUTE_VALUE")));
		
		String isOutbound = String.valueOf(mftApiKeyValMap.get("mftOutbound"));
		logger.info("Mft outbound : {}", isOutbound);
		String orderStatusUpd = "";
		for (String manualOrder : manualOrders) {
			String[] orderAndPartnerId = manualOrder.split(JobConstants.CCF_TILT_SEPARATOR);
			orders.add(orderAndPartnerId[0]);
		}
		

		try {

			for (String order : orders) {
				int insUser = 1;
				userId = getUserId(order);
				orderStatus = JobConstants.CCF_ORDER_STATUS_INPROGRESS;
				String vendorId = "";
				String psFileName = "";
				logger.info("Card Generation Started for orderId: " + order);
				updateOrderStatus(order, orderStatus);
				orderStatusUpd = order;
				//Get ccf format version
				ccfGenerationVersion = ccfGenerationDao.getCCFFormatVersionByOrderId(order);
				List<String> vendorIds = ccfGenerationDao.getAllVendorsLinkedToOrder(order);

				Iterator<?> vendorIterator = vendorIds.iterator();
				while (vendorIterator.hasNext()) {
					Map<?, ?> vendorMap = (Map<?, ?>) vendorIterator.next();
					vendorId = (String) vendorMap.get(JobConstants.CCF_FULFILLMENT_VENDOR_ID);

					createFolder(vendorId, isOutbound);
					logger.info("Creating Ccf file for vendor: {} and order {}", vendorId, order);
					psFileName = this.getCCFFileName(vendorId, order);
					logger.debug("CCF file name: {}", psFileName);

					Map<String, Object> count_panLength = ccfGenerationDao.getCountAllOrdersToGenerateCCF(order,
							vendorId);
					Long count = ((BigDecimal) count_panLength.get("cnt")).longValue();
					int panLength = ((BigDecimal) count_panLength.get("panLength")).intValue();

					int grossRecCount = 0;

					if (count != 0) {

						String headerFileNumber = getHeaderFileNumber(insUser);
						valueObj = ccfGenerationDao.getOrderInfo(order, vendorId);
						valueObj.put(JobConstants.CCF_HEADER_FILE_NUMBER, headerFileNumber);
						moFileWriter = this.getPhysicalCCFFile(vendorId, psFileName, isOutbound);
						logger.info("Writting CCF File... " + moFileWriter);
						grossRecCount = count.intValue() + 2;
						valueObj.put(JobConstants.CCF_HEADER_GROSS_RECCOUNT, String.valueOf(grossRecCount));
						valueObj.put(JobConstants.CCF_TRAILER_GROSS_RECCOUNT, String.valueOf(grossRecCount));
						valueObj.put(JobConstants.CCF_PAN_LENGTH, String.valueOf(panLength));

						writeHeader(valueObj);
						writeData(psFileName, vendorId, order, valueObj);
						writeTrailer(valueObj);

						moFileWriter.close();
						this.renamePhysicalCCFFile(vendorId, psFileName, isOutbound);
						logger.info("Card Generation completed");

					}
					logger.info("Updating Order status ");
					updateCardStatus(order, psFileName);
					updateOrderStatus(order, JobConstants.CCF_ORDER_STATUS_CCF_GENERATED);
					String fileName = copyToArchive(vendorId, psFileName, new File(psNewDirPath), isOutbound);

					DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
					Date date = new Date();

					String productName = getProductNameByOrderId(order);
					String message = new StringBuilder()
							.append("CCF Files Generated Successfully for Order number - " + order + "\r\n")
							.append("Order date - " + dateFormat.format(date).toUpperCase() + "\r\n")
							.append("Product - " + productName + "\r\n")
							.append("Order Type - " + String.valueOf(valueObj.get("orderType")) + "\r\n")
							.append("CCF file name - " + fileName + "\r\n").toString();

					notificationService.sendEmailNotification(userId, CCF_SUBJECT, message);
					if ("enable".equalsIgnoreCase(isOutbound)) {
						logger.info("Processing vendor: {}, file: {}", vendorId, fileName);
						asyncServiceCall.processOutboundFile(vendorId + ":" + fileName);
					}
				}
			}
		} catch (IOException e) {
			logger.error("IOException occured while process CCF Generation: {} ", e.getMessage());
			updateOrderStatus(orderStatusUpd, JobConstants.ORDER_STATUS_PROCESSED);
			notificationService.sendEmailNotification(userId, CCF_SUBJECT,
					"CCF Files Generation Failed for OrderID -" + orderStatusUpd);
		} catch (ServiceException e) {
			logger.error("ServiceException occured while process CCF Generation:  {} ", e.getMessage());
			updateOrderStatus(orderStatusUpd, JobConstants.ORDER_STATUS_PROCESSED);
			notificationService.sendEmailNotification(userId, CCF_SUBJECT,
					"CCF Files Generation Failed for OrderID -" + orderStatusUpd);
		} catch (ParseException e) {
			logger.error("ParseException occured while process CCF Generation:  {} ", e.getMessage());
			updateOrderStatus(orderStatusUpd, JobConstants.ORDER_STATUS_PROCESSED);
			notificationService.sendEmailNotification(userId, CCF_SUBJECT,
					"CCF Files Generation Failed for OrderID -" + orderStatusUpd);
		}
		logger.info(CCLPConstants.EXIT);
	}
    
	 private void updateCardStatus(String orderId, String psFileName) {

	    	int i = psFileName.lastIndexOf('.');
	    	 String name =psFileName.substring(0,i); 
	         String FileName = name+".csv";
	        ccfGenerationDao.updateCardStatus(orderId, FileName, JobConstants.CCF_ORDER_STATUS_CCF_GENERATED, JobConstants.PRINTERSENT);
	        
	    }
 
    private String getUserId(String order) {
		
    	String userId=null;
    	try {
    		userId = ccfGenerationDao.getUserId(order);
        }catch(Exception e) {
        		logger.error("Exception while getting productid based on Order id"+e.getMessage());
        }
    	return userId;
	}

	private String getProductNameByOrderId(String order) {
    	String productId = null;
    	try {
    	productId = ccfGenerationDao.getProductNameByOrderId(order);
    	}catch(Exception e) {
    		logger.error("Exception while getting productid based on Order id"+e.getMessage());
    	}
		return productId;
    	
	}

	private String getHeaderFileNumber(int insUser) throws ServiceException {
    	Map<String, Object> procedureResponse = ccfGenerationDao.getHeaderFileNumber(insUser);
    	logger.info("Generating header file number");
    	if(!String.valueOf(procedureResponse.get("errmsg")).equalsIgnoreCase(JobConstants.CCF_OK)) {
    		throw new ServiceException(ResponseMessages.CCF_ERR_FILE_NUMBER);
		}
    	String headerFileNumber = String.valueOf(procedureResponse.get(JobConstants.CCF_HEADER_FILE_NO));
    	logger.info("Header file numebr generated headerFileNumebr={}",headerFileNumber);
		return headerFileNumber;
	}

	private String getCCFFileName(String vendorId, String order) throws ServiceException {
    	Map<String, Object> procedureResponse = ccfGenerationDao.getCCFFileName(vendorId, order);
    	
    	if(!String.valueOf(procedureResponse.get(JobConstants.CCF_ERROR)).equalsIgnoreCase(JobConstants.CCF_OK)) {
    		throw new ServiceException(ResponseMessages.CCF_ERR_FILE_NAME);
		}
    	
    	String embFileName = String.valueOf(procedureResponse.get(JobConstants.CCF_EMB_FILE_NAME));
    	logger.info("CCF File name generated embFileName={}",embFileName);
		return embFileName;
	}

	private void updateOrderStatus(String orderId, String orderStatus) {
        ccfGenerationDao.updateCardStatus(orderId, orderStatus);
        
    }
 
 
	private List<StringBuilder> writeData(String psFileName,String vendorId, String orderId, Map<String, Object> valueObj1
			) throws  ServiceException, ParseException {

		logger.info(CCLPConstants.ENTER);		
		final List<StringBuilder> dataList = new LinkedList<>();
		try {
			CCFRowCallbackHandler handler = new CCFRowCallbackHandler();
			handler.setPsFileName(psFileName);
			handler.setValueObj1(valueObj1);
			handler.setFileWriter(moFileWriter);
			handler.setCcfGenerationDao(ccfGenerationDao);
			handler.setHsmIpAddress(hsmIpAddress);
			handler.setHsmPort(hsmPort);
			ccfGenerationDao.getAllOrdersToGenerateCCF(orderId, vendorId,handler);

		}catch(Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(JobConstants.ERROR_WRITE_DETAILS);
		}
		
		logger.info(CCLPConstants.EXIT);
		return dataList;
	}
 
   
	private void createFolder(String vendorId, String isOutbound) throws ServiceException {
		logger.info(CCLPConstants.ENTER +": "+ psDirPath);
		File poDir = null;

		poDir = new File(psDirPath);

		try {
			if (!poDir.exists()) {
				poDir.mkdirs();
				logger.debug("Creating directory: {}", poDir);
			}
		} catch (Exception e) {
			logger.error(ResponseMessages.CCF_ERROR_CREATE_FOLDER + e.getMessage());
			throw new ServiceException(ResponseMessages.CCF_ERROR_CREATE_FOLDER);
		}

		if ("enable".equalsIgnoreCase(isOutbound))
			poDir = new File(new StringBuilder().append(psDirPath).append("MFT").append(JobConstants.CCF_FORWARD_SLASH)
					.append(getVendorName(vendorId)).append(JobConstants.CCF_FORWARD_SLASH).toString());
		else
			poDir = new File(new StringBuilder().append(psDirPath).append(getVendorName(vendorId))
					.append(JobConstants.CCF_FORWARD_SLASH).toString());
		try {
			if (!poDir.exists()) {
				logger.debug("Creating vendor folder {} ", getVendorName(vendorId));
				poDir.mkdirs();
			}
		} catch (Exception e) {
			logger.error(ResponseMessages.CCF_ERROR_CREATE_FOLDER + e.getMessage());
			throw new ServiceException(ResponseMessages.CCF_ERROR_CREATE_FOLDER);
		}
		logger.info(CCLPConstants.EXIT);

	}
 
	public String getVendorName(String vendorId) {
		logger.info(CCLPConstants.ENTER);
		String vendorName = "";
		vendorName = ccfGenerationDao.getVendorNameForId(vendorId);
		logger.debug("vendor Name {} for vendor ID {} ", vendorName, vendorId);
		logger.info(CCLPConstants.EXIT);
		return vendorName;
	}
    
    
    public String copyToArchive(String vendorId,String psFileName, File newRootDir,String isOutbound) throws IOException {
		
    	logger.info(CCLPConstants.ENTER);
    	String psStrPath = "";
		String newPsStrPath = "";
		String rootDirStr = psDirPath;
		
		if ("enable".equalsIgnoreCase(isOutbound)) {
			rootDirStr = rootDirStr + "MFT"+ JobConstants.CCF_FORWARD_SLASH;
		}
		File rootDir = new File(rootDirStr);
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
		String fileName = name+".csv";
		// copying the file to a new location
		
		if (FileCopyUtils.copy(file, new File(copiedDir))>0) {
			logger.info("File Successfully Copied");
		} else {
			logger.info("Failed to Copy the file");
			
		}
		logger.info(CCLPConstants.EXIT);
		return fileName;
	}

	public FileWriter getPhysicalCCFFile(final String vendorId, final String msFileName, String isOutbound)
			throws IOException, ServiceException {
		
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
			logger.error(e.getMessage());
			throw new ServiceException(ResponseMessages.CCF_ERROR_CCF_FILE_CREATE);
		}

		if ("enable".equalsIgnoreCase(isOutbound)) {
			psStrPath = new StringBuilder().append(psDirPath).append("MFT").append(JobConstants.CCF_FORWARD_SLASH)
					.append(getVendorName(vendorId)).append("/").toString();

		} else {
			psStrPath = new StringBuilder().append(psDirPath).append(getVendorName(vendorId)).append("/").toString();
		}

		File f = new File(psStrPath);
		if (!f.isDirectory()) {
			f.mkdir();
		}

		psFilePath = psStrPath + msFileName;
		logger.info("creating physical ccf file {} ", psFilePath);
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
        	 logger.info("FileName renamed unsuccesfully"+name);
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
        
    	logger.info(CCLPConstants.ENTER);
    	String tempValue = "";
        logger.debug("Writing {} Data",dtlPart);
        
        final List<CCFFormatDtl> ccfFormat = loadCCFVersionDtls(ccfFormatVersion, dtlPart);
        
        if (ccfFormat != null) {
            for (CCFFormatDtl tempCCFFormatDtl : ccfFormat) {
 
                if (tempCCFFormatDtl.getDefaultValue() != null) {
                    valuSb.append(tempCCFFormatDtl.getDefaultValue());
                } else if ("H_GENERATE_DATE".equalsIgnoreCase(tempCCFFormatDtl.getTitleName())) {
                    final SimpleDateFormat sdf = new SimpleDateFormat(tempCCFFormatDtl.getFormat());
                    tempValue = sdf.format(new java.sql.Date(new java.util.Date().getTime()));
                    valuSb.append(tempValue);
                } else {
                    tempValue = (String) tempValueHashMap.get(tempCCFFormatDtl.getValueKey());
 
                    final String tempVal = "L".equals(tempCCFFormatDtl.getFillerSide())
                            ? padLeft(tempValue, Integer.parseInt(tempCCFFormatDtl.getDataLength()),
                                    tempCCFFormatDtl.getFiller().charAt(0))
                            : padRight(tempValue, Integer.parseInt(tempCCFFormatDtl.getDataLength()),
                                    tempCCFFormatDtl.getFiller().charAt(0), false);
                    valuSb.append(tempVal);
 
                }
 
            }
        } else {
        	logger.error("Failed to get CCF Formation details");
        }
        logger.info(CCLPConstants.EXIT);
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
            return str.substring(0, str.length()  + needed);
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
    	
    	logger.info(CCLPConstants.ENTER);
        HashMap<String, List<CCFFormatDtl>> ccfVersionDtls = new HashMap<>();
        List<CCFFormatDtl> ccfFormatDtlList = new ArrayList<>();
        try {
            List ccfVersionDetails = ccfGenerationDao.getCcfVersionDetails(version, recType);
            ccfVersionDtls.clear();
            for (Object ccfVersionDetail : ccfVersionDetails) {
                CCFFormatDtl temp = new CCFFormatDtl();
 
                Map m = (Map) ccfVersionDetail;
 
                temp.setTitleName((String) m.get("v5"));
                temp.setRecordType((String) m.get("v2"));
                temp.setDataLength((String) m.get("v7"));
                temp.setDefaultValue((String) m.get("v6"));
                temp.setFormat((String) m.get("v8"));
                temp.setFiller((String) m.get("v9"));
                temp.setFillerSide((String) m.get("v10"));
                temp.setValueKey((String) m.get("v4"));
                temp.setFormatType((String) m.get("v3"));
                final String versionType = (String) m.get("v1");
                ccfFormatDtlList = ccfVersionDtls.get(versionType);
 
                if (ccfFormatDtlList == null) {
                    ccfVersionDtls.put((String) m.get("v1"), new LinkedList<CCFFormatDtl>());
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
        logger.info(CCLPConstants.EXIT);
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
    	logger.info(CCLPConstants.ENTER);
        final StringBuilder headerSb = new StringBuilder();
        try {
            ccfFormation(valueObj, ccfGenerationVersion, JobConstants.CCF_HEADER_PART, headerSb);
            headerSb.append(JobConstants.CCF_SLASH_N);
           
            moFileWriter.append(headerSb.toString());
        } catch (Exception e) {
           logger.error("error writing ccf header ",e.getMessage());
        }
        logger.info(CCLPConstants.EXIT);
    }
 
    private void writeTrailer(final Map<String, Object> valueObj) {
    	logger.info(CCLPConstants.ENTER);
        final StringBuilder trailerSb = new StringBuilder();
        try {
            ccfFormation(valueObj, ccfGenerationVersion, JobConstants.CCF_TRAILER_PART, trailerSb);
            trailerSb.append(JobConstants.CCF_SLASH_N);
        
            moFileWriter.append(trailerSb.toString());
 
        } catch (Exception e) {
        	logger.error(e.getMessage());
        }
        logger.info(CCLPConstants.EXIT);
 
    }

	@Override
	public List<Map<String, Object>> getCCFFilesToDelete(String ccfFileDelGap) {
		
		logger.info(CCLPConstants.ENTER);	 
		List<Map<String, Object>> ccfFiles = ccfGenerationDao.getCCFFilesToDelete(ccfFileDelGap);
		logger.info(CCLPConstants.EXIT);
		return ccfFiles;
	}

    
}