package com.incomm.cclpvms.config.service.impl;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.incomm.cclpvms.config.model.DeliveryChannel;
import com.incomm.cclpvms.config.model.Product;
import com.incomm.cclpvms.config.model.ProductAlert;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.CronFrequency;
import com.incomm.cclpvms.util.CronSchedule;
import com.incomm.cclpvms.util.CronUtil;
import com.incomm.cclpvms.util.Util;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfGenerator extends AbstractPdfView {

	private Map<Double, String> buildPdfMetaData;
	private static final List<String> LIMITHEADINGS = Arrays.asList(" Min Amt per Txn", "Max Amt per Txn",
			"Daily Max Count", "Daily Max Amt", "Weekly Max Count", "Weekly Max Amt", "Monthly Max Count",
			"Monthly Max Amt", "Yearly Max Amt", "Yearly Max Amt");
	private static final List<String> LIMITTXNSHORTNAME = Arrays.asList("minAmtPerTx", "maxAmtPerTx", "dailyMaxCount",
			"dailyMaxAmt", "weeklyMaxCount", "weeklyMaxAmt", "monthlyMaxCount", "monthlyMaxAmt", "yearlyMaxCount",
			"yearlyMaxAmt");

	private static final List<String> TRANFEEHEADINGS = Arrays.asList(" Fee Description", "Clawback", " Clawback Count",
			"Fee Amount", " Fee Condition", " Fee %", " Free Count", "Free Count Frequency", "Max Count",
			" Max Count Frequency", " Monthly Fee Cap Available");
	private static final List<String> TRANFEETXNSHORTNAME = Arrays.asList("feeDesc", "clawback", "clawbackCount",
			"feeAmt", "feeCondition", "feePercent", "freeCount", "freeCountFreq", "maxCount", "maxCountFreq",
			"monthlyFeeCapAvail");
	private static final List<String> MAINFEEHEADINGS = Arrays.asList("Fee Description", "Assessment Date", "ProRation",
			"Fee Amt", "Clawback", "Clawback Count", "Clawback Option", "Clawback Maximum Amount",
			"First Month Fee Assessed Day", "Cap Fee Amt", "Free Count", "Max Count", "Monthly Fee Cap Available");
	private static final List<String> MAINFEETXNSHORTNAME = Arrays.asList("feeDesc", "assessmentDate", "proRation",
			"feeAmt", "clawback", "clawbackCount", "clawbackOption", "clawbackMaxAmt", "firstMonthFeeAssessedDays",
			"capFeeAmt", "freeCount", "maxCount", "FeeCapAvail");
	private static final List<String> MONTHLYFEECAPHEADINGS = Arrays.asList("Fee Description", "Time Period",
			"Assessment Date", "Fee Cap Amt");
	private static final List<String> MONTHLYFEECAPTXNSHORTNAME = Arrays.asList("feeDesc", "timePeriod",
			"assessmentDate", "feeCapAmt");
	private static final List<String> PRODUCT_PURSE_ATTRIBUTES = Arrays.asList("Purse", "Limits",
			"Transaction Fees", "Monthly Fee Cap");

	private static final String ALERTSMS = "alertSMS";
	private static final String ALERTEMAILSUB = "alertEmailSub";
	private static final String ALERTEMAILBODY = "alertEmailBody";

	public PdfGenerator(Map<Double, String> buildPdfMetaData) {
		this.buildPdfMetaData = buildPdfMetaData;
	}

	@SuppressWarnings("unchecked")
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String pinSupported = null;
		String cvvSupported = null;
		String limitSupported = null;
		String tranFeeSupported = null;
		String mainFeeSupported = null;
		String alertsSupported = null;
		String cardStatusSupported = null;
		String multiPurseSupported = null;
		Product productDTO = (Product) model.get("product");
		response.setHeader("Content-Disposition", "attachment; filename=" + convertAsString(productDTO.getProductName())
				+ "_" + getCurrentDateTime() + ".pdf");
		logger.debug(CCLPConstants.ENTER);

		Map<Double, String> attributeOrderMap = buildPdfMetaData;
		logger.debug("LOCAL Cache Attribute value : " + attributeOrderMap);
		List<DeliveryChannel> txnDtls = (List<DeliveryChannel>) model.get("txnDetails");

		List<String> cardStatusList = (List<String>) model.get("cardStatusList");
		ProductAlert productAlert = (ProductAlert) model.get("productAlerts");
		List<DeliveryChannel> txnData = new ArrayList<>();
		Map<String, String> lang = (Map<String, String>) model.get("languageDesc");
		Map<String, String> mainFeeMap = (Map<String, String>) model.get("mainFeeMap");
		Map<String, String> monthlyfeeCapMap = (Map<String, String>) model.get("monthlyfeeCapMap");
		List<LinkedHashMap<String, Object>> packageList = (List<LinkedHashMap<String, Object>>) model.get("package");
		List<LinkedHashMap<String, Object>> programId = (List<LinkedHashMap<String, Object>>) model
				.get("programIdData");
		Map<String, String> chwUserAuthTypes = (Map<String, String>) model.get("chwUserAuthTypes");
		Map<String, String> ivrUserAuthTypes = (Map<String, String>) model.get("ivrUserAuthTypes");

		Map<String, String> cardStatus = (Map<String, String>) model.get("cardStatus");
		String ruleSetName = model.get("ruleSet") != null ? (String) model.get("ruleSet") : "";
		Map<String, String> productPurseAttributes = (Map<String, String>) model.get("productPurseAttributes");

		Map<String, Map<String, String>> productAttributesMap = productDTO.getAttributes();

		String labelIdentifier = null;
		PdfPTable table = null;
		Rectangle pageSize = new Rectangle(PageSize.A2);
		document.setPageSize(pageSize);
		document.open();
		if (productAttributesMap != null && !productAttributesMap.isEmpty()) {

			Iterator<Entry<Double, String>> entries = attributeOrderMap.entrySet().iterator();
			while (entries.hasNext()) {

				Entry<Double, String> entry = entries.next();
				String[] attrInfo = String.valueOf(entry.getValue()).split("\\|");
				Map<String, String> groupProductAttributesMap = productAttributesMap.get(attrInfo[0]) == null ? new HashMap<>() : productAttributesMap.get(attrInfo[0]);
				groupProductAttributesMap.putAll(productAttributes(productDTO));
				if (attrInfo[0] != null && !attrInfo[0].equalsIgnoreCase(labelIdentifier)) {
					// Menu validation
					if (CCLPConstants.PRODUCT.equalsIgnoreCase(attrInfo[0])) {
						pinSupported = groupProductAttributesMap.get("pinSupported");
						cvvSupported = groupProductAttributesMap.get("cvvSupported");
						limitSupported = groupProductAttributesMap.get("limitSupported");
						tranFeeSupported = groupProductAttributesMap.get("feesSupported");
						mainFeeSupported = groupProductAttributesMap.get("maintainanceFeeSupported");
						alertsSupported = groupProductAttributesMap.get("alertSupported");
						cardStatusSupported = groupProductAttributesMap.get("cardStatusSupported");
						multiPurseSupported = groupProductAttributesMap.get("multiPurseSupport");
					}

					if (table != null) {
						document.add(table);
						table = null;
					}

					if("ProductPurse".equalsIgnoreCase(attrInfo[0])) {
						
						List<Object> listProductPurse = productDTO.getListProductPurseObject();
						
						if (!CollectionUtils.isEmpty(listProductPurse)) {
						
							for(Object productPurse: listProductPurse) {
								Map<String, Object> productPurseMap = (Map<String, Object>) productPurse;
								Map<String, Map<String, String>> purseAttributes = Util.convertJsonStringToHashMap(String.valueOf(productPurseMap.get("attributes")));
								
								Map<String, Object> purseMap = (Map<String, Object>) productPurseMap.get("purse");
								String extPurseId = String.valueOf(purseMap.get("extPurseId"));
								
									table = createTableCell(table, 1, 1);
									Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
									headFont.setColor(Color.red);
									Paragraph p = new Paragraph(extPurseId + "\n\n", headFont);
									p.setAlignment(Element.ALIGN_LEFT);
									document.add(p);
									
								
						for(String attributeGroup: PRODUCT_PURSE_ATTRIBUTES) {
							
							Map<String, String> groupPurseAttributesMap = (Map<String, String>) purseAttributes.get(attributeGroup);
					if(!CollectionUtils.isEmpty(groupPurseAttributesMap)) {
						if ("Transaction Fees".equalsIgnoreCase(attributeGroup)
								&& CCLPConstants.ENABLE.equalsIgnoreCase(tranFeeSupported)) {
							table = createTableCell(table, TRANFEEHEADINGS.size(), 2);
							buildTxnCells(table, document, attributeGroup, txnDtls, TRANFEEHEADINGS, TRANFEETXNSHORTNAME,
									groupPurseAttributesMap);

						} else if ("Limits".equalsIgnoreCase(attributeGroup)
								&& CCLPConstants.ENABLE.equalsIgnoreCase(limitSupported)) {
							table = createTableCell(table, LIMITHEADINGS.size(), 2);
							buildTxnCells(table, document, attributeGroup, txnDtls, LIMITHEADINGS, LIMITTXNSHORTNAME,
									groupPurseAttributesMap);

						} else if (CCLPConstants.MONTHLY_FEE_CAP.equalsIgnoreCase(attributeGroup)) {
							txnData.clear();
							table = createTableCell(table, MONTHLYFEECAPHEADINGS.size(), 2);
							DeliveryChannel del = new DeliveryChannel();
							del.setDeliveryChnlShortName(CCLPConstants.MONTHLY_FEE_CAP);
							del.setTransactionMap(monthlyfeeCapMap);
							txnData.add(del);
							buildTxnCells(table, document, attributeGroup, txnData, MONTHLYFEECAPHEADINGS,
									MONTHLYFEECAPTXNSHORTNAME, groupPurseAttributesMap);

						}else if ( CCLPConstants.PURSE.equalsIgnoreCase(attributeGroup)
						            && CCLPConstants.ENABLE.equalsIgnoreCase(multiPurseSupported)) {
							
							buildPurseCells(table,document,attributeGroup,productPurseAttributes,groupPurseAttributesMap);
							}
						document.add(table);
						}
						}
					}
				}
					
					}
					  if ("Card Status".equalsIgnoreCase(attrInfo[0])
							&& CCLPConstants.ENABLE.equalsIgnoreCase(cardStatusSupported)) {
						table = createTableCell(table, cardStatusList.size(), 2);
						buildTxnCells(table, document, attrInfo[0], txnDtls, cardStatusList, cardStatusList,
								groupProductAttributesMap);

					} else if (CCLPConstants.ALERTS.equalsIgnoreCase(attrInfo[0])
							&& CCLPConstants.ENABLE.equalsIgnoreCase(alertsSupported)) {

						table = createTableCell(table, 3, 1);
						Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
						headFont.setColor(Color.red);
						Paragraph p = new Paragraph(attrInfo[0] + "\n\n" + "", headFont);
						p.setAlignment(Element.ALIGN_LEFT);
						document.add(p);
						headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
						cellCreationWithData(CCLPConstants.PDF_NAME, table, headFont, Element.ALIGN_MIDDLE,
								Element.ALIGN_CENTER);
						cellCreationWithData(CCLPConstants.PDF_VALUE, table, headFont, Element.ALIGN_MIDDLE,
								Element.ALIGN_CENTER);
						cellCreationWithData(CCLPConstants.PDF_SUBJECT, table, headFont, Element.ALIGN_MIDDLE,
								Element.ALIGN_CENTER);
						cellCreationWithData(CCLPConstants.PDF_BODY, table, headFont, Element.ALIGN_MIDDLE,
								Element.ALIGN_CENTER);
					} else if (CCLPConstants.MAINTENANCE_FEES.equalsIgnoreCase(attrInfo[0])
							&& CCLPConstants.ENABLE.equalsIgnoreCase(mainFeeSupported)) {
						txnData.clear();
						table = createTableCell(table, MAINFEEHEADINGS.size(), 2);
						DeliveryChannel del = new DeliveryChannel();
						del.setDeliveryChnlShortName(CCLPConstants.MAINTENANCE_FEES);
						del.setTransactionMap(mainFeeMap);
						txnData.add(del);
						buildTxnCells(table, document, attrInfo[0], txnData, MAINFEEHEADINGS, MAINFEETXNSHORTNAME,
								groupProductAttributesMap);

					} 

					else {

						if ("General".equalsIgnoreCase(attrInfo[0])
								|| CCLPConstants.PRODUCT.equalsIgnoreCase(attrInfo[0])
								|| ("CVV".equalsIgnoreCase(attrInfo[0])
										&& CCLPConstants.ENABLE.equalsIgnoreCase(cvvSupported))
								|| ("PIN".equalsIgnoreCase(attrInfo[0])
										&& CCLPConstants.ENABLE.equalsIgnoreCase(pinSupported))) {
							table = createTableCell(table, 1, 1);
							Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
							headFont.setColor(Color.red);
							Paragraph p = new Paragraph(attrInfo[0] + "\n\n", headFont);
							p.setAlignment(Element.ALIGN_LEFT);
							document.add(p);
							headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
							cellCreationWithData(CCLPConstants.PDF_NAME, table, headFont, Element.ALIGN_MIDDLE,
									Element.ALIGN_CENTER);
							cellCreationWithData(CCLPConstants.PDF_VALUE, table, headFont, Element.ALIGN_MIDDLE,
									Element.ALIGN_CENTER);
						}
					}
				}

				// only for two cell data's
				if (groupProductAttributesMap != null) {

					if ("General".equalsIgnoreCase(attrInfo[0]) || CCLPConstants.PRODUCT.equalsIgnoreCase(attrInfo[0])
							|| (CCLPConstants.ALERTS.equalsIgnoreCase(attrInfo[0])
									&& CCLPConstants.ENABLE.equalsIgnoreCase(alertsSupported))
							|| ("CVV".equalsIgnoreCase(attrInfo[0])
									&& CCLPConstants.ENABLE.equalsIgnoreCase(cvvSupported))
							|| ("PIN".equalsIgnoreCase(attrInfo[0])
									&& CCLPConstants.ENABLE.equalsIgnoreCase(pinSupported))) {

						Font headFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);

						String denomination = groupProductAttributesMap.get("denominationType");
						String formfactor = groupProductAttributesMap.get("formFactor");
						String flag = "Y";

						if(!"Digital".equalsIgnoreCase(formfactor)) {
							if ("digitalInvAutoReplLvl".equalsIgnoreCase(attrInfo[1])
									|| "initialDigitalInvQty".equalsIgnoreCase(attrInfo[1])
									|| "digitalInvAutoReplQty".equalsIgnoreCase(attrInfo[1])) {
								flag = "N";
								
							}
							
						}
						

						if ("Retail".equalsIgnoreCase(groupProductAttributesMap.get("productType"))) {
							if ("b2bUpc".equalsIgnoreCase(attrInfo[1])
									|| "b2bInitSerialNumQty".equalsIgnoreCase(attrInfo[1])
									|| "b2bSerialNumAutoReplenishLevel".equalsIgnoreCase(attrInfo[1])
									|| "b2bSerialNumAutoReplenishVal".equalsIgnoreCase(attrInfo[1])
									|| "b2bProductFunding".equalsIgnoreCase(attrInfo[1])||"b2bSourceOfFunding".equalsIgnoreCase(attrInfo[1])) {
								flag = "N";
							} 
							
						}else {
								if ("retailUPC".equalsIgnoreCase(attrInfo[1])) {
									flag = "N";
								}
								if("ORDER_FULFILLMENT".equalsIgnoreCase(groupProductAttributesMap.get("b2bProductFunding"))){
									if ("b2bSourceOfFunding".equalsIgnoreCase(attrInfo[1])) {
										flag = "N";
									}
								}

							}
					
						if("Fixed".equalsIgnoreCase(denomination)) {
							if("denomVarMax".equalsIgnoreCase(attrInfo[1])||"denomVarMin".equalsIgnoreCase(attrInfo[1])||"denomSelect".equalsIgnoreCase(attrInfo[1])){
								flag = "N";
						}}else  if( "Select".equalsIgnoreCase(denomination)){
							if("denomVarMax".equalsIgnoreCase(attrInfo[1])||"denomVarMin".equalsIgnoreCase(attrInfo[1])||"denomFixed".equalsIgnoreCase(attrInfo[1])){
								flag = "N";
							}
						}else if( "Variable".equalsIgnoreCase(denomination)){
                             if("denomSelect".equalsIgnoreCase(attrInfo[1])||"denomFixed".equalsIgnoreCase(attrInfo[1])){
                            	 flag = "N";
							}
						}
					
						
						
							if ( "Y".equalsIgnoreCase(flag)) {

								cellCreationWithData(attrInfo[2], table, headFont, Element.ALIGN_MIDDLE,
										Element.ALIGN_LEFT);
							} 

							if ("pvk".equalsIgnoreCase(attrInfo[1]) || "cvkA".equalsIgnoreCase(attrInfo[1])
									|| "cvkB".equalsIgnoreCase(attrInfo[1])) {
								cellCreationWithData(maskSecuremsg(groupProductAttributesMap.get(attrInfo[1])), table,
										headFont, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);
							} else {
								String value = "";
								if ("alertLanguage".equalsIgnoreCase(attrInfo[1])) {
									value = String.valueOf(lang.get(groupProductAttributesMap.get(attrInfo[1])));

								} else if ("cvkFormat".equalsIgnoreCase(attrInfo[1])) {
									value = "true".equalsIgnoreCase(groupProductAttributesMap.get(attrInfo[1]))
											? "HSM Stored"
											: "Host Stored";
								} else if ("startTimeMins".equalsIgnoreCase(attrInfo[1])) {
									value = groupProductAttributesMap.get("startTimeHours") + "/"
											+ groupProductAttributesMap.get(attrInfo[1]);
								} else if ("cardVerifyType".equalsIgnoreCase(attrInfo[1])) {
									value = "true".equalsIgnoreCase(groupProductAttributesMap.get(attrInfo[1])) ? "CVV"
											: "";

								} else if ("ruleSetId".equalsIgnoreCase(attrInfo[1])) {
									value = ruleSetName;
								} else if ("alertCardStatus".equalsIgnoreCase(attrInfo[1])) {
									StringBuilder b = new StringBuilder();
									if (groupProductAttributesMap.get(attrInfo[1]) != null) {
										Stream.of(String.valueOf(groupProductAttributesMap.get(attrInfo[1])).split(","))
												.forEach(alertCardStatus -> {
													b.append(cardStatus.get(alertCardStatus).replace("_", " ") + ",");
												});
										value = String.valueOf(b).substring(0, String.valueOf(b).length() - 1);
									}
								} else if ("cardRenewReplaceProd".equalsIgnoreCase(attrInfo[1])) {
									if (groupProductAttributesMap.get(attrInfo[1]) != null) {
										switch (groupProductAttributesMap.get(attrInfo[1])) {

										case "SP":
											value = "Same PAN";
											break;
										case "NP":
											value = "New PAN";
											break;
										case "NPP":
											value = "New Product";
											break;
										default:
											value = "Select";
											break;
										}
									}
								} else if ("pinAlgorithm".equalsIgnoreCase(attrInfo[1])) {
									value = "true".equalsIgnoreCase(groupProductAttributesMap.get(attrInfo[1]))
											? "IBM DES"
											: "";
								} else if ("validityPeriod".equalsIgnoreCase(attrInfo[1])) {
									value = groupProductAttributesMap.get(attrInfo[1]) + " "
											+ groupProductAttributesMap.get("validityPeriodFormat");
								}else if ("defaultCardStatus".equalsIgnoreCase(attrInfo[1])) {
									value = cardStatus.get(groupProductAttributesMap.get(attrInfo[1]));

								} else if ("programId".equalsIgnoreCase(attrInfo[1])) {
									if (programId != null && groupProductAttributesMap.get(attrInfo[1]) != null) {
										LinkedHashMap<String, Object> programDto = programId.stream()
												.filter(pkg -> String.valueOf(pkg.get("programID")).equalsIgnoreCase(
														String.valueOf(groupProductAttributesMap.get(attrInfo[1]))))
												.findAny().orElse(null);
										value = String.valueOf(programDto.get("programIDName"));
									}

								} else if ("defaultPackage".equalsIgnoreCase(attrInfo[1])) {
									if (packageList != null && groupProductAttributesMap.get(attrInfo[1]) != null) {

										LinkedHashMap<String, Object> packageDto = packageList.stream()
												.filter(pkg -> String.valueOf(pkg.get("packageId")).equalsIgnoreCase(
														String.valueOf(groupProductAttributesMap.get(attrInfo[1]))))
												.findAny().orElse(null);

										value = packageDto.get("packageId") + "-" + packageDto.get("description");
									}
								} else if ("defaultPurse".equalsIgnoreCase(attrInfo[1])) {
									List<Object> listProductPurse = productDTO.getListProductPurseObject();
									
									if (!CollectionUtils.isEmpty(listProductPurse) && groupProductAttributesMap.get(attrInfo[1]) != null) {
									
										for(Object productPurse: listProductPurse) {
											Map<String, Object> productPurseMap = (Map<String, Object>) productPurse;
											String isDefault = String.valueOf(productPurseMap.get("isDefault"));
											if(isDefault.equalsIgnoreCase("Y")) {
												Map<String, Object> purseMap = (Map<String, Object>) productPurseMap.get("purse");
												value = String.valueOf(purseMap.get("extPurseId"));
												
											}
										}
									}
								} else if ("chwAuthType".equalsIgnoreCase(attrInfo[1])) {
									if (groupProductAttributesMap.get(attrInfo[1]) != null) {
										value = chwUserAuthTypes.get(groupProductAttributesMap.get(attrInfo[1]));
									}
								} else if ("ivrAuthType".equalsIgnoreCase(attrInfo[1])) {
									if (groupProductAttributesMap.get(attrInfo[1]) != null) {
										value = ivrUserAuthTypes.get(groupProductAttributesMap.get(attrInfo[1]));
									}
								} else {
									value = groupProductAttributesMap.get(attrInfo[1]);
								}
								if ( "Y".equalsIgnoreCase(flag)) {
								cellCreationWithData(changeMessage(convertAsString(value)), table, headFont,
										Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);
								
								}
								flag = "Y";
							}
						

						// Product Addition Data
						if (CCLPConstants.ALERTS.equalsIgnoreCase(attrInfo[0])) {
							if (attrInfo[1] != null && attrInfo[1].contains("_")) {
								String[] alertMetaData = String.valueOf(attrInfo[1]).split("_");
								String key = "";
								if ("SMS".equalsIgnoreCase(alertMetaData[1])) {
									key = ALERTSMS + "_" + alertMetaData[2];
									cellCreationWithData(null, table, headFont, Element.ALIGN_MIDDLE,
											Element.ALIGN_LEFT);
									cellCreationWithData(convertAsString(productAlert.getAlertAttributes().get(key)),
											table, headFont, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);
								} else {
									key = ALERTEMAILBODY + "_" + alertMetaData[2];
									cellCreationWithData(
											convertAsString(productAlert.getAlertAttributes()
													.get(ALERTEMAILSUB + "_" + alertMetaData[2])),
											table, headFont, Element.ALIGN_MIDDLE, Element.ALIGN_LEFT);
									cellCreationWithData(convertAsString(productAlert.getAlertAttributes().get(key)),
											table, headFont, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);
								}

							} else {
								cellCreationWithData(null, table, headFont, Element.ALIGN_MIDDLE, Element.ALIGN_LEFT);
								cellCreationWithData(null, table, headFont, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);
							}

						}
					}

					labelIdentifier = String.valueOf(attrInfo[0]);
				}
				/*if (!entries.hasNext()) {
					document.add(table);
				}*/

			}
		}

	}

	private void buildPurseCells(PdfPTable table, Document document, String attributeGroup,
			Map<String, String> productPurseAttributes, Map<String, String> groupProductAttributesMap) throws DocumentException {
		table = createTableCell(table, 1, 1);
		Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
		headFont.setColor(Color.red);
		Paragraph p = new Paragraph(attributeGroup + "\n\n", headFont);
		p.setAlignment(Element.ALIGN_LEFT);
		document.add(p);
		headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		cellCreationWithData(CCLPConstants.PDF_NAME, table, headFont, Element.ALIGN_MIDDLE,
				Element.ALIGN_CENTER);
		cellCreationWithData(CCLPConstants.PDF_VALUE, table, headFont, Element.ALIGN_MIDDLE,
				Element.ALIGN_CENTER);
		
		headFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
		for(Map.Entry<String, String> purseAttributes: productPurseAttributes.entrySet()) {
			String value = groupProductAttributesMap.get(purseAttributes.getKey());		
			
			cellCreationWithData(purseAttributes.getValue(), table, headFont, Element.ALIGN_MIDDLE,
					Element.ALIGN_LEFT);
			
			cellCreationWithData(changeMessage(convertAsString(value)), table, headFont,
					Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);
			
			if (CCLPConstants.ATTRIBUTE_AUTO_TOPUP_ENABLE.equalsIgnoreCase(purseAttributes.getKey()) && CCLPConstants.ENABLE.equalsIgnoreCase(value)) {
				buildPurseJobTypeCells(table, headFont, "Auto Topup Frequency", groupProductAttributesMap.get(CCLPConstants.ATTRIBUTE_AUTO_TOPUP_FREQUENCY));
				
				buildPurseFrequencyCells(table, headFont, groupProductAttributesMap.get(CCLPConstants.ATTRIBUTE_AUTO_TOPUP_FREQUENCY), groupProductAttributesMap.get(CCLPConstants.ATTRIBUTE_AUTO_TOPUP_CRON));				
			} else if (CCLPConstants.ATTRIBUTE_AUTO_RELOAD_ENABLE.equalsIgnoreCase(purseAttributes.getKey()) && CCLPConstants.ENABLE.equalsIgnoreCase(value)) {
				buildPurseJobTypeCells(table, headFont, "Auto Reload Frequency", groupProductAttributesMap.get(CCLPConstants.ATTRIBUTE_AUTO_RELOAD_FREQUENCY));
				
				buildPurseFrequencyCells(table, headFont, groupProductAttributesMap.get(CCLPConstants.ATTRIBUTE_AUTO_RELOAD_FREQUENCY), groupProductAttributesMap.get(CCLPConstants.ATTRIBUTE_AUTO_RELOAD_CRON));				
			}
			
		}
		document.add(table);		
	}
	
	private void buildPurseJobTypeCells(PdfPTable table, Font headFont, String frequencyKey, String frequencyValue) {

		
		cellCreationWithData(frequencyKey, table, headFont, Element.ALIGN_MIDDLE,
				Element.ALIGN_LEFT);
		
		cellCreationWithData(changeMessage(convertAsString(CronFrequency.byFrequencyShortName(frequencyValue).getfrequencyName())), table, headFont,
				Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);
	}
	
	private void buildPurseFrequencyCells(PdfPTable table, Font headFont, String frequency, String cron) {
		CronSchedule schedule = new CronSchedule();
		schedule.setFrequency(frequency);
		schedule.setCron(cron);
		CronUtil.parseCron(schedule);
		Map<String, Object> freqDtls = new HashMap<>();
		switch(CronFrequency.byFrequencyShortName(frequency)) {
		case DAY:
			freqDtls.put(CronFrequency.DAY.getfrequencyName(), schedule.getInterval());
			break;
		case DAY_OF_MONTH:
			freqDtls.put(CronFrequency.DAY_OF_MONTH.getfrequencyName(), schedule.getDayOfMonth().toString().replaceAll("\\[", "").replaceAll("\\]",""));
			break;
		case QUARTER:
		case YEAR:
			freqDtls.put(CronFrequency.DAY_OF_MONTH.getfrequencyName(), schedule.getDayOfMonth().get(0));
			freqDtls.put(CronFrequency.MONTH.getfrequencyName(), Month.of(schedule.getMonth()));
			break;
		default:
		}
		
		for(Map.Entry<String, Object> freqAttributes: freqDtls.entrySet()) {
			cellCreationWithData(freqAttributes.getKey(), table, headFont, Element.ALIGN_MIDDLE,
					Element.ALIGN_LEFT);
			
			cellCreationWithData(changeMessage(convertAsString(freqAttributes.getValue())), table, headFont,
					Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);
		}
		
	}

	/*
	 * Addtional Product attributes
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> productAttributes(Product productDTO) {

		Map<String, String> productMap = new HashMap<>();
		productMap.put("productName", productDTO.getProductName());
		productMap.put("productShortName", productDTO.getProductShortName());
		productMap.put("description", productDTO.getDescription());
		productMap.put("partnerName", productDTO.getPartnerName());
		productMap.put("issuerName", productDTO.getIssuerName());
		productMap.put("programId", String.valueOf(productDTO.getProgramId()));

		List<Object> cardRangeList = productDTO.getCardRangeObject();
		List<String> poductCurrencyList = productDTO.getPartnerCurrency();

		StringBuilder poductCurrencyBuild = new StringBuilder();
		if (poductCurrencyList != null && !poductCurrencyList.isEmpty()) {
			for (Object poductCurrency : poductCurrencyList) {
				Map<String, Object> result = (Map<String, Object>) poductCurrency;
				poductCurrencyBuild.append(result.get("currencyTypeID")+":" +result.get("currCodeAlpha")+":"+result.get("currencyDesc")+ ",");
				
			}
			productMap.put("partnerCurrency", poductCurrencyBuild.substring(0, poductCurrencyBuild.length() - 1));
		} else {
			productMap.put("partnerCurrency", "");
		}

		
		
		StringBuilder cardRangeBuild = new StringBuilder();
		if (cardRangeList != null && !cardRangeList.isEmpty()) {
			for (Object cardRange : cardRangeList) {
				Map<String, Object> result = (Map<String, Object>) cardRange;
				cardRangeBuild.append(String.valueOf(result.get("prefix")) + result.get("startCardNbr") + "-"
						+ result.get("prefix") + result.get("endCardNbr") + ",");
			}
			productMap.put("cardRanges", cardRangeBuild.substring(0, cardRangeBuild.length() - 1));
		} else {
			productMap.put("cardRanges", "");
		}

		List<Object> packageIsList = productDTO.getPackageIdObject();
		StringBuilder packageIsBuild = new StringBuilder();

		if (packageIsList != null && !packageIsList.isEmpty()) {

			for (Object packageId : packageIsList) {
				Map<String, Object> result = (Map<String, Object>) packageId;
				packageIsBuild.append(result.get("packageId") + "-" + result.get("description") + ",");
			}
			productMap.put("packageIds", packageIsBuild.substring(0, packageIsBuild.length() - 1));
		} else {
			productMap.put("packageIds", "");
		}
		List<Object> supportedPuseIdList = productDTO.getSupportedPurseObject();
		StringBuilder supportedPuseBuild = new StringBuilder();

		productMap.put("UPC", productDTO.getUpc());

		if (supportedPuseIdList != null && !supportedPuseIdList.isEmpty()) {
			for (Object supportedPuseId : supportedPuseIdList) {
				Map<String, Object> supportedPurse = (Map<String, Object>) supportedPuseId;
			    String purseId = String.valueOf(supportedPurse.get("extPurseId"));
					supportedPuseBuild
							.append(purseId+ ",");
			}
			productMap.put("supportedPurse", supportedPuseBuild.substring(0, supportedPuseBuild.length() - 1));
		} else {
			productMap.put("supportedPurse", "");
		}
		
		return productMap;

	}

	private void buildTxnCells(PdfPTable table, Document document, String title, List<DeliveryChannel> txnDtls,
			List<String> limitHeadings, List<String> limitTxnShortName, Map<String, String> groupProductAttributesMap)
			throws DocumentException {
		Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
		headFont.setColor(Color.red);
		Paragraph p = new Paragraph(convertAsString(title) + "\n\n", headFont);
		p.setAlignment(Element.ALIGN_LEFT);
		document.add(p);
		for (DeliveryChannel txnDtlsOut : txnDtls) {
			headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			cellCreationWithData(txnDtlsOut.getDeliveryChnlShortName().toUpperCase(), table, headFont,
					Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);

			for (String limitHeadingsOut : limitHeadings) {
				cellCreationWithData(limitHeadingsOut, table, headFont, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);
			}

			Map<String, String> transactionMap = txnDtlsOut.getTransactionMap();
			for (Map.Entry<String, String> transactionMapOut : transactionMap.entrySet()) {
				headFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
				cellCreationWithData(transactionMapOut.getValue(), table, headFont, Element.ALIGN_MIDDLE,
						Element.ALIGN_CENTER);

				String value = "";
				for (String limitTxnShortNameOut : limitTxnShortName) {
					if (CCLPConstants.MAINTENANCE_FEES.equalsIgnoreCase(title)
							|| CCLPConstants.MONTHLY_FEE_CAP.equalsIgnoreCase(title)) {
						if ("FeeCapAvail".equalsIgnoreCase(limitTxnShortNameOut)) {
							value = transactionMapOut.getKey() + "_" + transactionMapOut.getKey().replace("Fee", "")
									+ limitTxnShortNameOut;

						} else {
							value = transactionMapOut.getKey() + "_" + limitTxnShortNameOut;

						}
					} else {
						value = txnDtlsOut.getDeliveryChnlShortName() + "_" + transactionMapOut.getKey() + "_"
								+ limitTxnShortNameOut;
					}
					String result = "";
					if (CCLPConstants.MAINTENANCE_FEES.equalsIgnoreCase(title)) {

						if ("monthlyFee_assessmentDate".equalsIgnoreCase(value)) {
							if (groupProductAttributesMap.get(value) != null) {
								switch (groupProductAttributesMap.get(value)) {
								case "AD":
									result = "Anniversary Date of Account Activation";
									break;
								case "FD":
									result = "First Calendar Date of Each Month";
									break;
								case "AL":
									result = "Anniversary Date of First Load";
									break;
								case "FLI":
									result = "Funding Date of Account without Activation";
									break;
								default:
									result = "Anniversary Date of Account Activation";
									break;
								}
							}
						} else if (String.valueOf(transactionMapOut.getKey() + "_clawbackOption")
								.equalsIgnoreCase(value)) {
							if (groupProductAttributesMap.get(value) != null) {
								result = getOperatorFullForm(groupProductAttributesMap.get(value));
							}
						} else {
							result = groupProductAttributesMap.get(value) != null ? groupProductAttributesMap.get(value)
									: "NA";
						}
					} else if (CCLPConstants.MONTHLY_FEE_CAP.equalsIgnoreCase(title)) {
						if ("monthlyFeeCap_timePeriod".equalsIgnoreCase(value)) {
							if (groupProductAttributesMap.get(value) != null) {
								switch (groupProductAttributesMap.get(value)) {
								case "CM":
									result = "Calendar Months";
									break;
								case "SM":
									result = "Configure Date Of Month";
									break;
								default:
									result = "Calendar Months";
									break;
								}
							}
						} else {
							result = groupProductAttributesMap.get(value);

						}

					} else if ("Transaction Fees".equalsIgnoreCase(title)) {
						if (String.valueOf(txnDtlsOut.getDeliveryChnlShortName() + "_" + transactionMapOut.getKey()
								+ "_feeCondition").equalsIgnoreCase(value)) {

							if (groupProductAttributesMap.get(value) != null) {

								result = getOperatorFullForm(groupProductAttributesMap.get(value));
							}
						} else if (String
								.valueOf(txnDtlsOut.getDeliveryChnlShortName() + "_" + transactionMapOut.getKey()
										+ "_freeCountFreq")
								.equalsIgnoreCase(value)
								|| String
										.valueOf(txnDtlsOut.getDeliveryChnlShortName() + "_"
												+ transactionMapOut.getKey() + "_maxCountFreq")
										.equalsIgnoreCase(value)) {

							if (groupProductAttributesMap.get(value) != null) {
								switch (groupProductAttributesMap.get(value)) {
								case "D":
									result = "Daily";
									break;
								case "W":
									result = "Weekly";
									break;
								case "BM":
									result = "Fortnightly";
									break;
								case "M":
									result = "Monthly";
									break;
								case "Y":
									result = "Yearly";
									break;
								case "BW":
									result = "Bi-weekly";
									break;
								default:
									result = "Daily";

								}
							}
						} else {
							result = groupProductAttributesMap.get(value);

						}

					} else {
						result = groupProductAttributesMap.get(value);
					}

					cellCreationWithData(changeMessage(convertAsString(result)), table, headFont, Element.ALIGN_MIDDLE,
							Element.ALIGN_CENTER);
				}
			}

		}
	}

	private static PdfPTable createTableCell(PdfPTable pdfTable, int length, int startColumLen)
			throws DocumentException {
		pdfTable = new PdfPTable(length + 1);
		pdfTable.setWidthPercentage(100);
		pdfTable.getDefaultCell().setUseAscender(true);
		pdfTable.getDefaultCell().setUseDescender(true);
		int[] tableWidth = new int[length + 1];
		tableWidth[0] = startColumLen;
		for (int i = 1; i <= length; i++) {
			tableWidth[i] = 1;
		}
		pdfTable.setWidths(tableWidth);
		return pdfTable;
	}

	private static void cellCreationWithData(String string, PdfPTable table, Font headFont, int vertAlign,
			int horiAlign) {
		PdfPCell cell = new PdfPCell(new Phrase(convertAsString(string), headFont));
		cell.setVerticalAlignment(vertAlign);
		cell.setHorizontalAlignment(horiAlign);
		table.addCell(cell);
	}

	/**
	 * String validation
	 * 
	 * @param dataObject
	 * @return
	 */
	private static String convertAsString(Object dataObject) {

		return (dataObject != null) && !"null".equalsIgnoreCase(String.valueOf(dataObject).trim())
				&& !"".equalsIgnoreCase(String.valueOf(dataObject).trim()) ? String.valueOf(dataObject) : "";
	}

	private static String getCurrentDateTime() {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime currentdate = LocalDateTime.now();
		return String.valueOf(dtf.format(currentdate)).replace(":", "_").replace(" ", "_").replace("/", "_");
	}

	private static String maskSecuremsg(String value) {
		if (value != null && !convertAsString(value).isEmpty()) {
			String maskChar = "*";
			int length = value.length();
			String maskString = StringUtils.repeat(maskChar, length);
			return StringUtils.overlay(value, maskString, 0, length);
		}
		return value;
	}

	private static String changeMessage(String value) {

		switch (value) {
		case "true":
			value = "Enable";
			break;
		case "false":
			value = "Disable";
			break;
		default:
			return value;
		}

		return value;
	}

	private static String getOperatorFullForm(String result) {

		switch (result) {
		case "O":
			result = "OR";
			break;
		case "A":
			result = "AND";
			break;
		case "N":
			result = "NA";
			break;
		default:
			result = "NA";

		}
		return result;
	}

}
