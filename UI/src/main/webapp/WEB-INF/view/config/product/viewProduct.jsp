<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.*"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/product.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/css/jqueryui.css"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/jqueryui.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/css/blue-theme.css"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/bootstrap-datepicker.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/multiselect.min.js"></script>


<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/js/jquery.timepicker.js"></script>

<link
	href="${pageContext.request.contextPath}/resources/css/jquery.timepicker.css"
	rel="stylesheet" />


<script>
	$(function() {
		var date_input = $('input[name="date"]'); //our date input has the name "date"
		var container = $('.bootstrap-iso form').length > 0 ? $(
				'.bootstrap-iso form').parent() : "body";
		$('.date-picker')
				.datepicker(
						{
							format : 'mm/dd/yyyy',
							container : container,
							todayHighlight : true,
							autoclose : true,
							changeMonth : true,
							changeYear : true,
							showButtonPanel : true,
							startDate : new Date(),
							dateFormat : 'dd MM yy',
							onClose : function(dateText, inst) {

								var date = $(
										"#ui-datepicker-div .ui-datepicker-date :selected")
										.val();

								var month = $(
										"#ui-datepicker-div .ui-datepicker-month :selected")
										.val();
								var year = $(
										"#ui-datepicker-div .ui-datepicker-year :selected")
										.val();

								$(this).datepicker('setDate',
										new Date(date, year, month, 1));
							}
						})
	});
</script>

<script>
	function loadFrames() {

	}

	/* $(function() {
		 $('.date-picker')
				.datepicker(
						{
							minValue : 0,
							changeMonth : true,
							changeYear : true,
							showBPanel : true,
							dateFormat : 'dd MM yy',
							onClose : function(dateText, inst) {
								var date = $(
										"#ui-datepicker-div .ui-datepicker-date :selected")
										.val();
								var month = $(
										"#ui-datepicker-div .ui-datepicker-month :selected")
										.val();
								var year = $(
										"#ui-datepicker-div .ui-datepicker-year :selected")
										.val();
								$(this).datepicker('setDate',
										new Date(date, year, month, 1));
								
								  $(this).bind('dblclick', function(e) {
				                      var e=e||window.event;
				                       e.stopPropagation();
				                       e.preventDefault();
				                       return false; 
				                   });

							}
						}); 
		
	}); */
</script>


<style>
/* 
.col-sm-2.up {
    margin: -123px 3px 0px 526px;
}

.col-sm-2.down {
    margin: -89px 3px 0px 526px;
}


.col-sm-4.up {
    margin: -123px 3px 0px 526px;
}

.col-sm-4.down {
    margin: -89px 3px 0px 526px;
}


.col-sm-3.up{
    margin: -123px 3px 0px 526px;
}

.col-sm-3.down{
    margin: -90px 3px 0px 526px;
}
 */
.up {
	margin: -126px 3px 0px 688px;
}

.down {
	margin: -96px 3px 0px 688px;
}
</style>

<body class="dashboard" onload="onloadDiv()">

	<div>
		<ol class="breadcrumb col-lg-11">
			<li>
				<div class="breadCrumb-blue"></div>
			</li>

		</ol>
	</div>

	<form:form name="viewProductForm" id="viewProductForm" method="POST"
		class='form-horizontal' commandName="productForm">
		<!--  class='form-horizontal' commandName="productForm" modelAttribute="attributesForm"> -->

		<div class="body-container">


			<div class="modal fade" id="define-constant-serial" tabindex="-1"
				role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-9 col-lg-offset-5">

								<b><spring:message code="product.serialNumberRequest" /></b>

							</div>
							<div class="col-lg-12">
								<div class="col-lg-4 ">

									<label for="Product Name"><spring:message
											code="product.productName" /> <font color='red'>*</font></label>

								</div>

								<div class="col-lg-8">

									<form:input path="" id="serialProductName"
										class="textbox-large" type="textarea" minlength="2"
										maxlength="100"
										onkeyup="return isAlphaNumericWithSpace(this);"
										onblur="validateFields(this.form.id,this.id);" />
									<div>
										<form:errors path="" id="serialProductName"
											cssStyle="color:red" />
									</div>
								</div>
							</div>


							<!--  UPC -->
							<div class="col-lg-12">
								<div class="col-lg-4">

									<label for="UPC"><spring:message code="product.upc" />
										<font color='red'> *</font></label>

								</div>
								<div class="col-lg-8">

									<p class="textbox-large" type="textarea" id="serialRetailUPC">
										<form:input path="" id="serialProductRetailUPC"
											type="textarea" name="retailUPC"
											onkeyup="return isAlphaNumeric(this);" maxlength="50"
											onblur="validateFields(this.form.id,this.id)" />
									<div>
										<form:errors path="prodAttributes['retailUPC']"
											cssClass="error" cssStyle="color:red"></form:errors>
									</div>
									</p>
									<p class="textbox-large" id="serialB2bUPC" type="textarea"
										style="display: none;">
										<form:input path="" id="serialProductB2BUPC" type="textarea"
											name="b2bUPC" onkeyup="return isAlphaNumeric(this);"
											maxlength="50" onblur="validateFields(this.form.id,this.id)" />
									<div>
										<form:errors path="prodAttributes['b2bUpc']" cssClass="error"
											cssStyle="color:red"></form:errors>
									</div>

								</div>


							</div>

							<!-- serial Number Quantiy -->
							<div class="col-lg-12" id="serailIntialB2BSerialNumQty">

								<div class="col-lg-4">
									<label for="Initial B2B Serial Number Quantity"><spring:message
											code="product.SerialNumQty" /> <font color='red'>*</font></label>
								</div>
								<div class="col-lg-8">

									<form:input path="" id="serialB2bInitSerialNumQty"
										class="textbox-large" type="textarea" minlength="2"
										maxlength="50" onkeyup="return isNumeric(this)"
										onblur="validateFields(this.form.id,this.id)" />

								</div>

								<form:errors path="" id="serialB2bInitSerialNumQty"
									cssStyle="color:red" />

							</div>





						</div>

						<div class="modal-footer">
							<button type="button" onclick="deleteIssuer();"
								class="btn btn-primary">
								<i class="glyphicon glyphicon-plus"></i>
								<spring:message code="addProductserialNumberRequest.Submit" />
							</button>

							<button type="button" onclick="goResetProduct();"
								class="btn btn-primary gray-btn">
								<i class='glyphicon glyphicon-refresh'></i>
								<spring:message code="button.reset" />
							</button>
							<button data-dismiss="modal" onclick="goToPrevious()"
								class="btn btn-primary gray-btn">
								<spring:message code="button.close" />
							</button>


						</div>

					</div>

				</div>
			</div>



			<div class="container">




				<!-- delete box starts -->


				<div class="modal fade" id="define-constant-update" tabindex="-1"
					role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog">
						<form:form commandName="productConfirmBox" name="updateProduct"
							id="updateProduct" method="post">
							<div class="modal-content">
								<div class="modal-body col-lg-12">
									<div class="col-lg-12" style="display: inline-block">
										<span
											style="width: 100%; display: inline-block; word-wrap: break-word;">
											Do you want to Update the Product record '<b
											id="productNameDisp"></b>' ?
										</span>
									</div>

								</div>
								<form:hidden path="" id="productIdtoUpdate" />
								<input type="hidden" name="productIdtoUpdate"
									id="productIdtoUpdate" />
								<div class="modal-footer">
									<button type="button" onclick="goUpdateProduct();"
										class="btn btn-primary">
										<i class="glyphicon glyphicon-saved"></i>
										<spring:message code="button.update" />
									</button>
									<button data-dismiss="modal" onclick="goToPrevious()"
										class="btn btn-primary gray-btn">
										<spring:message code="button.cancel" />
									</button>

								</div>

							</div>
						</form:form>
					</div>
				</div>

				<!-- delete box ends -->






				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs   col-lg-11 col-lg-offset-1">
							<li class="active SubMenu"><a href="#" data-toggle="tab"><spring:message
										code="product.viewProduct" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active graybox col-lg-11 col-lg-offset-1">
								<div class="text-right Error-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<div class="form-inline">


									<div>
										<c:if test="${successstatus !=''}">
											<h4>
												<p class="">
													<b><font size="3"><center style="color: green;">
																<strong>${successstatus}</strong>
															</center></font></b>
												</p>
											</h4>
										</c:if>
										<c:if test="${failstatus !=''}">
											<h4>
												<p class="">
													<b><font size="3"><center style="color: red;">
																<strong>${failstatus}</strong>
															</center></font></b>
												</p>
											</h4>
										</c:if>
									</div>


									<div class="col-lg-12">
										<div class="col-lg-12">


											<form:hidden path="issuerId" id="issuerId" name="issuerId"
												value="" />
											<form:hidden path="partnerId" id="partnerId" name="partnerId"
												value="" />
											<!-- <input id="productId" name="productId" type="hidden"
									value=" " /> -->
											<input id="editProductId" name="editProductId"
												" type="hidden" value="${editProductId}" /> <input
												type="hidden" name="jsPath" id="jsPath"
												value="${pageContext.request.contextPath}/resources/JS_Messages/" />

											<div class="col-lg-4">

												<label for="Product Name"><spring:message
														code="product.productName" /> <font color='red'>*</font></label>

											</div>

											<div class="col-lg-8">

												<form:input title="Allowed Special Characters are .,;'_- "
													path="productName" id="productName" class="textbox-large"
													type="textarea" minlength="2" maxlength="100"
													onkeyup="return isAlphaNumericWithNewSpecialCharsProdName(this)"
													onblur="validateFields(this.form.id,this.id);"
													readonly="true" />
												<div>
													<form:errors path="productName" cssStyle="color:red" />


												</div>
											</div>
										</div>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Product Short Name"><spring:message
														code="product.productShortName" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">

												<form:input path="productShortName" id="productShortName"
													class="textbox-large" type="textarea" minlength="2"
													maxlength="50"
													onkeyup="return isAlphaNumericWithSpace(this)"
													onblur="validateFields(this.form.id,this.id)"
													readonly="true" />

												<div>
													<form:errors path="productShortName" class="textbox-large"
														cssStyle="color:red" />
												</div>
											</div>
										</div>


										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Description"><spring:message
														code="product.description" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-8">

												<form:textarea path="description" id="productDesc"
													class="textbox-large" type="textarea" maxLength="255"
													rows="5" cols="51" style="resize:none" readonly="true" />

												<div>
													<form:errors path="description" cssStyle="color:red" />

												</div>

											</div>
										</div>

										<br>&nbsp;

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Partner Name"><spring:message
														code="product.partnerName" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">


												<form:select id="productPartnerName" name="partnerName"
													path="partnerName" class="dropdown-medium"
													onChange="getPartnerCurrency(this.form.id,this.id);setTimeout(checkPartnerCurrencyExist, 100);"
													onblur="return validateDropDown(this.form.id,this.id);"
													disabled="true">
													<%-- onchange="processPartner(this.form.id);"  --%>
													<option value="NONE">--- Select ---</option>
													<%-- <form:options items="${partnerDropDown}" /> --%>
													<c:forEach items="${partnerDropDown}" var="partner">
														<option
															value="${partner.partnerId}~${partner.partnerName}"
															<c:if test="${partner.partnerName eq productForm.partnerName}">selected="true"</c:if>><c:out
																value="${partner.partnerName}" /></option>
													</c:forEach>
												</form:select>
												<div>
													<form:errors path="partnerName" cssClass="error"
														cssStyle="color:red" />
												</div>
											</div>
										</div>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Issuer Name"><spring:message
														code="product.issuerName" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">

												<form:select path="issuerName" class="dropdown-medium"
													id="productIssuerName" name="issuerName"
													onblur="return validateDropDown(this.form.id,this.id);"
													onChange="getCardRangeDetails();setTimeout(checkCardRangeExist, 100);"
													disabled="true">
													<option value="NONE">--- Select ---</option>
													<c:forEach items="${issuerDropDown}" var="issuer">
														<option value="${issuer.issuerId}~${issuer.issuerName}"
															<c:if test="${issuer.issuerName eq productForm.issuerName}">selected="true"</c:if>>${issuer.issuerName}</option>

													</c:forEach>

												</form:select>


												<div>
													<form:errors path="issuerName" cssClass="error"
														cssStyle="color:red" />
												</div>


											</div>
										</div>

										<br>&nbsp; <input type="hidden" id="addurl" name="addurl"
											value="${addUrl}" />

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Card Range"><spring:message
														code="product.cardRange" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">

												<!-- Multi select for card range starts -->

												<div class="col-lg-12">
													<div class="col-lg-4 ">
														<div>Available</div>
														<form:select path="" name="search_CardRange"
															id="multiselect" class="dropdown-medium" size="8"
															style="width: 322px;" multiple="multiple" disabled="true">

															<c:forEach items="${availableCardRangeList}"
																var="cardRange">

																<c:if
																	test="${cardRange.cardRangeId !=null && cardRange.prefix !=null &&
				cardRange.startCardNbr != null && cardRange.endCardNbr !=null}">

																	<form:option value="${cardRange.cardRangeId}">
				${cardRange.prefix}${cardRange.startCardNbr}-${cardRange.prefix}${cardRange.endCardNbr}</form:option>

																</c:if>

															</c:forEach>

															<%-- <form:options items="${cardRangeData}" /> --%>


														</form:select>
													</div>

													<div class="col-lg-1 col-lg-offset-1 ">
														<br />
														<button type="button" id="multiselect_rightAll"
															class="btn btn-block" disabled="disabled">
															<i class="glyphicon glyphicon-forward"></i>
														</button>
														<button type="button" id="multiselect_rightSelected"
															class="btn btn-block" disabled="disabled">
															<i class="glyphicon glyphicon-chevron-right"></i>
														</button>
														<button type="button" id="multiselect_leftSelected"
															class="btn btn-block" disabled="disabled">
															<i class="glyphicon glyphicon-chevron-left"></i>
														</button>
														<button type="button" id="multiselect_leftAll"
															class="btn btn-block" disabled="disabled">
															<i class="glyphicon glyphicon-backward"></i>
														</button>
													</div>

													<div class="col-lg-4 ">

														<c:set var="carddetails" value="${productForm.cardRanges}" />
														<%-- 	<c:out value="asd${carddetails}" /> --%>

														<div>Selected</div>
														<form:select name="to[]" id="multiselect_to"
															path="cardRangesUpdate" class="dropdown-medium" size="8"
															style="width: 322px;" multiple="multiple" disabled="true">

															<c:forEach items="${carddetails}" var="cardRange">

																<c:if
																	test="${cardRange.cardRangeId !=null && cardRange.prefix !=null &&
				cardRange.startCardNbr != null && cardRange.endCardNbr !=null}">

																	<form:option value="${cardRange.cardRangeId}">
				${cardRange.prefix}${cardRange.startCardNbr}-${cardRange.prefix}${cardRange.endCardNbr}</form:option>

																</c:if>
															</c:forEach>
														</form:select>
														<div>
															<form:errors path="cardRangesUpdate" cssClass="error"
																cssStyle="color:red"></form:errors>
														</div>


														<%-- path="cardRanges" class="form-control movetoright" size="8" multiple="multiple"> --%>


														<%-- <c:forEach items="${productForm.cardRanges}" var="cardRange">
				
				<c:if test="${cardRange.cardRangeId !=null && cardRange.prefix !=null &&
				cardRange.startCardNbr != null && cardRange.endCardNbr !=null}">
				
				<form:option value="${cardRange.cardRangeId}">
				${cardRange.prefix}${cardRange.startCardNbr}-${cardRange.prefix}${cardRange.endCardNbr}</form:option>
				
				</c:if>
				
				</c:forEach> --%>







													</div>
													<div class="col-lg-3 ">
														<div class="col-sm-4 up ">
															<button type="button" id="multiselect_move_up"
																class="btn btn-block">
																<i class="glyphicon glyphicon-arrow-up"></i>
															</button>
														</div>
														<div class="col-sm-4 down ">
															<button type="button" id="multiselect_move_down"
																class="btn btn-block col-sm-6">
																<i class="glyphicon glyphicon-arrow-down"></i>
															</button>
														</div>
													</div>
												</div>

												<!-- Multi select for card range ends -->






											</div>
										</div>

										<br>&nbsp;

										<%-- 	<div class="col-lg-12">

								<div class="col-lg-4">
									<label for="Parent Product Name"><spring:message
											code="product.parentProductName" /> </label>
								</div>
								<div class="col-lg-8">


									<form:select id="parentProductName" name="parentProductName"
										path="parentProductName" class="dropdown-medium">
										<form:option value="0~NONE" label="--- Select ---" />
										<c:forEach items="${parentProductDropDown}" var="parentProd">
											<option
												value="${parentProd.productId}~${parentProd.productName}"
												<c:if test="${parentProd.productName eq productForm.parentProductName}">selected="true"</c:if>>${parentProd.productName}</option>

										</c:forEach>



											<c:forEach items="${parentProductDropDown}" var="parentProd">
										<form:option value="${parentProd.productId}~${parentProd.productName}">${parentProd.productName}</form:option>
										</c:forEach>
									</form:select>
									<div>
										<form:errors path="parentProductName" id="parentProductName"
											name="parentProductName" cssClass="error"
											cssStyle="color:red" />
									</div>
								</div>
							</div> --%>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Form Factor"><spring:message
														code="product.formFactor" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-4" id="formFactor">
												<form:radiobutton path="prodAttributes['formFactor']"
													id="formFactor" name="formFactorPhysical" value="Physical"
													checked="checked" disabled="true" />
												<form:errors path="prodAttributes['formFactor']"
													cssClass="error" cssStyle="color:red"></form:errors>
												<label class='radiobox-line'>Physical</label>
												<form:radiobutton path="prodAttributes['formFactor']"
													value="Virtual" id="formFactorVirtual" name="formFactor"
													data-skin="square" data-color="blue" disabled="true" />
												<label class='radiobox-line'>Virtual</label>
												<form:radiobutton path="prodAttributes['formFactor']"
													value="Digital" id="formFactorDigital" name="formFactor"
													data-skin="square" data-color="blue" disabled="true" />
												<label class='radiobox-line'>Digital</label>
												<form:radiobutton path="prodAttributes['formFactor']"
													value="IH" id="formFactorIH" name="formFactor"
													data-skin="square" data-color="blue" disabled="true" />
												<label class='radiobox-line'>IH</label>
											</div>


										</div>


										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Product Type"><spring:message
														code="product.productType" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-4" id="productType">
												<form:radiobutton path="prodAttributes['productType']"
													id="productRetailType" name="productType" value="Retail"
													checked="checked" disabled="true" />
												<label class='radiobox-line'>Retail</label>
												<form:radiobutton path="prodAttributes['productType']"
													value="B2B" id="productB2BType" name="productType"
													data-skin="square" data-color="blue" disabled="true" />
												<label class='radiobox-line'>B2B</label> <br>&nbsp;
											</div>
										</div>


								<!-- Initial Digital Inventory Quantity -->


										<div class="col-lg-12" id="intialDigitalInventoryQty"
											style="display: none">

											<div class="col-lg-4">
												<label for="Initial Digital Inventory Quantity"><spring:message
														code="product.initialDigitalInvQty" /> </label><font color='red'>
													*</font>
											</div>
											<div class="col-lg-8">

												<form:input path="prodAttributes['initialDigitalInvQty']"
													id="initialDigitalInvQty" class="textbox-large"
													type="textarea" minlength="1" maxlength="50" readonly="true"
													onkeyup="return isNumeric(this)"
													onblur="validateFields(this.form.id,this.id)" />

											</div>

											<form:errors path="prodAttributes['initialDigitalInvQty']"
												id="initialDigitalInvQty" cssStyle="color:red" />

										</div>

										<!-- Digital Inventory Auto replenishment Level -->
										<div class="col-lg-12" id="digitalInventoryAuotReplLevel"
											style="display: none">

											<div class="col-lg-4">
												<label for="Digital Inventory Auto replenishment Level"><spring:message
														code="product.digitalInvAutoReplLvl" /> </label><font color='red'>
													*</font>
											</div>
											<div class="col-lg-8">

												<form:input path="prodAttributes['digitalInvAutoReplLvl']"
													id="digitalInvAutoReplLvl" class="textbox-large"
													type="textarea" minlength="1" maxlength="50" readonly="true"
													onkeyup="return isNumeric(this)"
													onblur="validateFields(this.form.id,this.id)" />



											</div>
											<form:errors path="prodAttributes['digitalInvAutoReplLvl']"
												id="digitalInvAutoReplLvl" cssStyle="color:red" />

										</div>
										

										<!-- Digital Inventory Auto replenishment Quantity -->


										<div class="col-lg-12" id="digitalAutoReplenishmentQty"
											style="display: none">

											<div class="col-lg-4">
												<label for="Digital Inventory Auto replenishment Quantity"><spring:message
														code="product.digitalInvAutoReplQty" /> </label><font color='red'>
													*</font>
											</div>
											<div class="col-lg-8">

												<form:input path="prodAttributes['digitalInvAutoReplQty']"
													id="digitalInvAutoReplQty" class="textbox-large"
													type="textarea" minlength="1" maxlength="50"  readonly="true"
													onkeyup="return isNumeric(this)"
													onblur="validateFields(this.form.id,this.id)" />


											</div>
											<form:errors path="prodAttributes['digitalInvAutoReplQty']"
												id="digitalInvAutoReplQty" cssStyle="color:red" />


										</div>

											<!-- Sale Active code Response type -->


										<div class="col-lg-12" id="SaleactiveCodeResponseType"
											style="display: none">

								<div class="col-lg-4">
									<label for="SaleactiveCode Response Type"><spring:message
											code="product.saleActiveCodeResponseType" /> <font color='red'></font></label>
								</div>
								<div class="col-lg-4" id="saleActiveCodeResType">
									<form:radiobutton path="prodAttributes['saleActiveCodeResponseType']"
										id="saleActiveCodeResponseTypePin" name="saleActiveCodeResponseTypePin" value="PIN"
										checked="checked" />
									<label class='radiobox-line'>PIN</label>
									<form:radiobutton path="prodAttributes['saleActiveCodeResponseType']"
										value="CVV" id="saleActiveCodeResponseTypeCVV" name="saleActiveCodeResponseTypeCVV"
										data-skin="square" data-color="blue" />
									<label class='radiobox-line'>CVV</label>
								</div>
								<form:errors path="prodAttributes['saleActiveCodeResponseType']"
										cssClass="error" cssStyle="color:red"></form:errors>


							</div>


										<%-- 	<div class="col-lg-1">

									<label for="UPC"><spring:message code="product.upc" /></label>

								</div>
								<div class="col-lg-2">

									<p class="space" id="retailUPC">
										<form:input path="prodAttributes['retailUPC']" id="productRetailUPC" type="text" name="retailUPC" onkeyup="return isAlphaNumeric(this);"
														onblur="validateFields(this.form.id,this.id)" />
										<form:errors path="prodAttributes['retailUPC']"
										cssClass="error" cssStyle="color:red"></form:errors>
										</div> 
									</p>
									<p class="space" id="b2bUPC" style="display: none;">
			<form:input path="prodAttributes['b2bUpc']" id="productB2BUPC"
			 type="text" name="b2bUPC" onkeyup="return isAlphaNumeric(this);" />
										<div>
										<form:errors path="prodAttributes['b2bUpc']"
										cssClass="error" cssStyle="color:red">
										</form:errors>
										</div> 

									</p>


								</div>
								<div class="col-lg-2">

									<button type="button" class="btn btn-primary"
										id="serialRequest" style="display: none;"
										onclick="goBackToProduct();">
										<!-- <i class='glyphicon glyphicon-backward'></i> -->
										<spring:message code="product.serialNumberRequest" />
									</button>

								</div> --%>

										<div class="col-lg-12" id="cardEncodingDiv">
											<div class="col-lg-4">
												<label for="Card Encoding"><spring:message
														code="product.cardEncoding" /> <font color='red'>
														*</font></label>
											</div>

											<div class="col-lg-4">
												<form:select path="prodAttributes['cardEncoding']"
													id="cardEncoding" name="cardEncoding" disabled="true">
													<form:option value="NONE" label="None" />
													<form:option value="CR80 Secure Wrap">CR80 Secure Wrap</form:option>
													<form:option value="M6 Snap Card">M6 Snap Card </form:option>
													<form:option value="CR80 On Carrier">CR80 On Carrier </form:option>
													<form:option value="CR80 Without Consumer Packaging">CR80 Without Consumer Packaging</form:option>
													<form:option value="CR80 Multi Packs">CR80 Multi Packs </form:option>
													<form:option value="CR80 Fraud Prevention Stickers">CR80 Fraud Prevention Stickers</form:option>
												</form:select>



												<div>
													<form:errors path="prodAttributes['cardEncoding']"
														id="cardEncoding" cssStyle="color:red" />

												</div>
											</div>
										</div>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="UPC"><spring:message code="product.upc" /><font
													color='red'> *</font></label>
											</div>

											<div class="col-lg-4">
												<p class="space" id="retailUPC">
													<form:input path="prodAttributes['retailUPC']"
														class="textbox-large" id="productRetailUPC" type="text"
														name="retailUPC" onkeyup="return isAlphaNumeric(this);"
														maxlength="50"
														onblur="validateFields(this.form.id,this.id)"
														readonly="true" />
													<form:errors path="prodAttributes['retailUPC']"
														cssClass="error" cssStyle="color:red"></form:errors>

												</p>
												<p class="space" id="b2bUPC" style="display: none;">
													<form:input path="prodAttributes['b2bUpc']"
														class="textbox-large" id="productB2BUPC" maxlength="50"
														type="text" name="b2bUPC"
														onkeyup="return isAlphaNumeric(this);"
														onblur="validateFields(this.form.id,this.id)"
														readonly="true" />
												<div>
													<form:errors path="prodAttributes['b2bUpc']"
														cssClass="error" cssStyle="color:red">
													</form:errors>
												</div>
											</div>
											<%-- <div class="col-lg-4">

									<button type="button" class="btn btn-primary"
										id="serialRequest" style="display: none;"
										 data-toggle="modal" data-target="#define-constant-serial">
										<!-- <i class='glyphicon glyphicon-backward'></i> -->
										<spring:message code="product.serialNumberRequest" />
									</button>

								</div> --%>





										</div>





										<div class="col-lg-12" id="intialB2BSerialNumQty"
											style="display: none;">

											<div class="col-lg-4">
												<label for="Initial B2B Serial Number Quantity"><spring:message
														code="product.b2bInitSerialNumQty" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-2">

												<form:input path="prodAttributes['b2bInitSerialNumQty']"
													id="b2bInitSerialNumQty" class="textbox-large"
													type="textarea" minlength="2" maxlength="50"
													onkeyup="return isNumeric(this)"
													onblur="validateFields(this.form.id,this.id)"
													readonly="true" />

											</div>

											<form:errors path="prodAttributes['b2bInitSerialNumQty']"
												id="b2bInitSerialNumQty" cssStyle="color:red" />

										</div>


										<div class="col-lg-12" id="autoRepLevel"
											style="display: none;">

											<div class="col-lg-4">
												<label for="Serial Number Auto Replenishment Level"><spring:message
														code="product.b2bSerialNumAutoReplenishLevel" /> <font
													color='red'></font></label>
											</div>
											<div class="col-lg-2">

												<form:input
													path="prodAttributes['b2bSerialNumAutoReplenishLevel']"
													id="b2bSerialNumAutoReplenishLevel" class="textbox-large"
													type="textarea" minlength="2" maxlength="50"
													onkeyup="return isNumeric(this)"
													onblur="validateFields(this.form.id,this.id)"
													readonly="true" />
												<div>
													<form:errors
														path="prodAttributes['b2bSerialNumAutoReplenishLevel']"
														id="b2bSerialNumAutoReplenishLevel" cssClass="error"
														cssStyle="color:red" />
												</div>

											</div>
										</div>


										<div class="col-lg-12" id="autoRepValue"
											style="display: none;">

											<div class="col-lg-4">
												<label for="Serial Number Auto Replenishment Value"><spring:message
														code="product.b2bSerialNumAutoReplenishVal" /> <font
													color='red'></font></label>
											</div>
											<div class="col-lg-2">

												<form:input
													path="prodAttributes['b2bSerialNumAutoReplenishVal']"
													id="b2bSerialNumAutoReplenishVal" class="textbox-large"
													type="textarea" minlength="2" maxlength="50"
													onkeyup="return isNumeric(this)"
													onblur="validateFields(this.form.id,this.id)"
													readonly="true" />
												<div>
													<form:errors
														path="prodAttributes['b2bSerialNumAutoReplenishVal']"
														id="b2bSerialNumAutoReplenishVal" cssClass="error"
														cssStyle="color:red" />
												</div>

											</div>
										</div>

										<div class="col-lg-12" id="productFunding"
											style="display: none;">
											<div class="col-lg-4">
												<label for="B2B Product Funding"><spring:message
														code="product.b2bProductFunding" /> <font color='red'>*</font></label>
											</div>

											<div class="col-lg-4">
												<form:select path="prodAttributes['b2bProductFunding']"
													id="b2bProductFunding" name="b2bProductFunding"
													disabled="true">
													<form:option value="NONE" label="- - - Select - - -" />
													<form:option value="ORDER_FULFILLMENT">Order Fulfillment</form:option>
													<form:option value="CARD_ACTIVATION">Successful Activation</form:option>
													<%-- <c:forEach items="${validityList}" var="validity">
        
        										<form:option value="${validity}"/>${validity}
        											<c:if test="${validity eq productForm.prodAttributes['validityPeriodFormat']}">selected="true"</c:if>>
        
        									</c:forEach> --%>
												</form:select>



												<div>
													<form:errors path="prodAttributes['b2bProductFunding']"
														id="b2bProductFunding" cssStyle="color:red" />

												</div>
											</div>
										</div>



										<div class="col-lg-12" id="srcOfFunding"
											style="display: none;">
											<div class="col-lg-4">
												<label for="Source of Funding"><spring:message
														code="product.sourceOfFunding" /> <font color='red'></font></label>
											</div>

											<div class="col-lg-4">
												<form:select path="prodAttributes['b2bSourceOfFunding']"
													id="sourceOfFunding" name="sourceOfFunding" disabled="true">
													<form:option value="NONE" label="- - - Select - - -" />
													<form:option value="ORDER_AMOUNT">Order Amount</form:option>
													<form:option value="CARD_ACTIVATION_AMOUNT">Activation Amount</form:option>
													<%-- <c:forEach items="${validityList}" var="validity">
        
        										<form:option value="${validity}"/>${validity}
        											<c:if test="${validity eq productForm.prodAttributes['validityPeriodFormat']}">selected="true"</c:if>>
        
        									</c:forEach> --%>
												</form:select>



												<div>
													<form:errors path="prodAttributes['sourceOfFunding']"
														id="sourceOfFunding" cssStyle="color:red" />

												</div>
											</div>
										</div>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="DCMSID"><spring:message
														code="product.dcmsId" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-8">

												<form:input path="prodAttributes['dcmsId']" id="dcmsId"
													class="textbox-large" type="textarea" minlength="2"
													maxlength="50" onkeyup="return isAlphaNumeric(this)"
													onblur="validateFields(this.form.id,this.id)"
													readonly="true" />
												<div>
													<form:errors path="prodAttributes['dcmsId']" id="dcmsId"
														cssStyle="color:red" />
												</div>

											</div>
										</div>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Card Validity Period"><spring:message
														code="product.validityPeriod" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-3">


												<form:input path="prodAttributes['validityPeriod']"
													name="validityPeriod" id="validityPeriod" maxlength="4"
													class="textbox-large" type="textarea"
													onkeyup="return isNumeric(this);"
													onblur="return validateFields(this.form.id,this.id);"
													readonly="true" />
												<%-- onblur="validateFields(this.form.id,this.id);" /> --%>
											</div>
											<div class="col-lg-4">
												<form:select path="prodAttributes['validityPeriodFormat']"
													onblur="return validateDropDown(this.form.id,this.id);"
													id="amntOfTimeType" name="amntOfTimeType" disabled="true">
													<form:option value="-1" label="- - - Select - - -" />
													<c:forEach items="${validityList}" var="validity">

														<form:option value="${validity}" />${validity}
        <c:if
															test="${validity eq productForm.prodAttributes['validityPeriodFormat']}">selected="true"</c:if>>
        
        </c:forEach>
												</form:select>


												<div>
													<form:errors path="prodAttributes['validityPeriod']"
														id="validityPeriod" cssStyle="color:red" />

												</div>
											</div>
										</div>

										<br>&nbsp;

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Product Validity Date"><spring:message
														code="product.productValidityDate" /> </label>
											</div>
											<div class="col-lg-8">

												<form:input type="textarea"
													path="prodAttributes['productValidityDate']"
													placeholder="MM/DD/YYYY" id="productValidityDate"
													onchange="validateDate(this.form.id,this.id)"
													onkeyup="return allowNumbersWithSlash(this);"
													readonly="true" />

												<input type="hidden" id="productValidityDateOld" value="" />
												<div>
													<form:errors path="prodAttributes['productValidityDate']"
														id="productValidityDate" cssStyle="color:red" />
												</div>
											</div>
										</div>


										<br>&nbsp;

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Month End - Card Expiry Date"><spring:message
														code="product.monthEndCardExpiryDate" /> <font
													color='red'></font></label>
											</div>
											<div class="col-lg-4">
												<form:radiobutton
													path="prodAttributes['monthEndCardExpiry']"
													id="monthEndCardExpiryDate" name="monthEndCardExpiryDate"
													value="Disable" checked="checked" disabled="true" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton
													path="prodAttributes['monthEndCardExpiry']" value="Enable"
													id="monthEndCardExpiryDate" name="monthEndCardExpiryDate"
													data-skin="square" data-color="blue" disabled="true" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>
										<br>&nbsp;
										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Reloadable Flag"><spring:message
														code="product.reloadableFlag" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-8">
												<form:radiobutton path="prodAttributes['reloadableFlag']"
													id="reloadableFlagId" name="reloadableFlagName"
													checked="checked" value="Disable" disabled="true" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['reloadableFlag']"
													value="Enable" id="reloadableFlagId"
													name="reloadableFlagName" data-skin="square"
													data-color="blue" disabled="true" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>


										<br>&nbsp;
										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Active From"><spring:message
														code="product.activeFrom" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">

												<form:input type="textarea"
													path="prodAttributes['activeFrom']"
													placeholder="MM/DD/YYYY" id="activeFrom"
													class="date-picker"
													onchange="validateDate(this.form.id,this.id)"
													onkeyup="return allowNumbersWithSlash(this);"
													readonly="true" />

												<input type="hidden" id="activeFromOld" value="" />
												<div>
													<form:errors path="prodAttributes['activeFrom']"
														id="activeFrom" cssStyle="color:red" />
												</div>
											</div>
										</div>
										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Cutover Time"><spring:message
														code="product.cutoverTime" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">

												<form:input type="textarea"
													path="prodAttributes['cutOverTime']" placeholder="HH:MM:SS"
													name="cutoverTime" id="cutoverTime" maxlength="8"
													onblur="validateCutoverTime(this.form.id,this.id)"
													readonly="true" />
												<div>
													<form:errors path="prodAttributes['cutOverTime']"
														cssStyle="color:red" />
												</div>

											</div>
										</div>


										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Denomination (Activation)">Denomination
													(Activation)<font color='red'>*</font>
												</label>
											</div>
											<div class="col-lg-8 space">

												<c:set var="denomTyp"
													value="${productForm.prodAttributes['denominationType']}" />
												<input type="hidden" name="denomtyp" id="denomtyp"
													value="${denomTyp}" />


												<form:select path="prodAttributes['denominationType']"
													class="space" id="denominationId"
													onblur="validateDropDown(this.form.id,this.id)"
													onchange="includeRespDiv();" disabled="true">


													<option value="NONE" label="--- Select ---">NONE</option>
													<option value="Fixed" label="Fixed">Fixed</option>
													<option value="Select" label="Select">Select</option>
													<option value="Variable" label="Variable">Variable</option>



												</form:select>

												<div>
													<form:errors path="prodAttributes['denominationType']"
														id="denominationId" cssStyle="color:red" />
												</div>

												<br>&nbsp;
												<p class="space" id="denomFixed" style="display: none;">
													<form:input path="prodAttributes['denomFixed']" type="text"
														onkeyup="return allowNumbersWithDot(this);"
														onblur="validateDecimalFormat(this.form.id,this.id);"
														name="denomFixed" id="denomFixedField" maxlength="10"
														readonly="true" />
												<div>
													<form:errors path="prodAttributes['denomFixed']"
														cssStyle="color:red" />
												</div>

												<div class="" id="denomSelect" style="display: none;">
													<div class="col-sm-4 space">
														<div>
															Available<font color='red'> *</font>
														</div>

														<form:input path="" id="denom" size="8"
															multiple="multiple" class="textbox" name="denom"
															type="textarea"
															onkeyup="return allowNumbersWithDot(this);"
															onblur="validateDecimalFormat(this.form.id,this.id);"
															maxlength="10" />
													</div>

													<div class="col-sm-1 col-sm-offset-1">
														<br />

														<button type="button" id="denom_rightSelected"
															class="btn btn-block" onClick="AddDenomSelectFields();"
															disabled="disabled">
															<i class="glyphicon glyphicon-chevron-right"></i>
														</button>
														<button type="button" id="denom_leftSelected"
															class="btn btn-block" onClick="RemoveSelFields();"
															disabled="disabled">
															<i class="glyphicon glyphicon-chevron-left"></i>
														</button>
													</div>

													<div class="col-sm-5">
														<div>Selected</div>
														<form:select path="prodAttributes['denomSelect']"
															id="denom_to" name="denom_to" class="dropdown-small"
															size="8" multiple="multiple">
															<c:forEach items="${selectedDenom}" var="denom">
																<form:option value="${denom}">${denom}</form:option>
															</c:forEach>
														</form:select>
														<div>
															<form:errors path="prodAttributes['denomSelect']"
																id="denomSelect" cssStyle="color:red" />
														</div>


													</div>
												</div>

												<div class="space" id="denomVar" style="display: none;">
													Min<font color='red'> *</font>:
													<form:input path="prodAttributes['denomVarMin']"
														type="text" id="denomMinField"
														onkeyup="return allowNumbersWithDot(this);"
														onblur="validateDecimalFormat(this.form.id,this.id);"
														name="minDenom" maxlength="10" readonly="true" />
													<div>
														<form:errors path="prodAttributes['denomVarMin']"
															cssStyle="color:red" />
													</div>
													Max<font color='red'> *</font>:
													<form:input path="prodAttributes['denomVarMax']"
														type="text" id="denomMaxField"
														onkeyup="return allowNumbersWithDot(this);"
														onblur="validateDecimalFormat(this.form.id,this.id);"
														name="maxDenom" maxlength="10" readonly="true" />
													<div>
														<form:errors path="prodAttributes['denomVarMax']"
															cssStyle="color:red" />
													</div>

												</div>
											</div>

										</div>


										<br>&nbsp;

										<!-- purse starts here -->

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Supported Purse(s)"><spring:message
														code="prodcut.supportedPurse" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">

												<!-- Multi select for card range starts -->

												<div class="">
													<div class="col-lg-4 ">
														<div>Available</div>
														<form:select path="" name="search_CardRange" id="purse"
															class="dropdown-medium" size="8" style="width: 322px;" multiple="multiple"
															disabled="true">
															<c:forEach items="${purseData}" var="purse">

																<form:option value="${purse.purseId}">

																	<%-- <c:if
																		test="${purse.purseTypeName !=null && purse.purseTypeName == 'CURRENCY' }">
					 ${purse.purseTypeName}-${purse.currCodeAlpha}
					</c:if>
																	<c:if
																		test="${purse.purseTypeName !=null && purse.purseTypeName == 'UPC' }">
					 ${purse.purseTypeName}-${purse.upc}
					</c:if>
																	<c:if
																		test="${purse.purseTypeName !=null && purse.purseTypeName == 'LOYALTY' }">
					${purse.purseTypeName}-${purse.currCodeAlpha} 
					</c:if> --%>
						${purse.extPurseId}

																</form:option>

															</c:forEach>
														</form:select>
													</div>

													<div class="col-lg-1 col-lg-offset-1 padding">
														<br />
														<button type="button" id="purse_rightAll"
															onblur="showDefaultpurse();" class="btn btn-block"
															disabled="disabled">
															<i class="glyphicon glyphicon-forward"></i>
														</button>
														<button type="button" id="purse_rightSelected"
															onblur="showDefaultpurse();" class="btn btn-block"
															disabled="disabled">
															<i class="glyphicon glyphicon-chevron-right"></i>
														</button>
														<button type="button" id="purse_leftSelected"
															onblur="showDefaultpurse();" class="btn btn-block"
															disabled="disabled">
															<i class="glyphicon glyphicon-chevron-left"></i>
														</button>
														<button type="button" id="purse_leftAll"
															onblur="showDefaultpurse();" class="btn btn-block"
															disabled="disabled">
															<i class="glyphicon glyphicon-backward"></i>
														</button>
													</div>

													<div class="col-lg-4 padding">
														<c:set var="purseList"
															value="${productForm.supportedPurse}" />


														<div>Selected</div>
														<form:select path="supportedPurseUpdate" name="to[]"
															class="dropdown-medium" id="purse_to" size="8"
															style="width: 322px;" multiple="multiple" disabled="true">
															<c:forEach items="${purseList}" var="purse">

																<form:option value="${purse.purseId}">

																	<%--  <c:if test="${purse.purseTypeName !=null && purse.purseTypeName == 'CURRENCY' }">
					 ${purse.purseTypeName}-${purse.currCodeAlpha}
					</c:if>
					 <c:if test="${purse.purseTypeName !=null && purse.purseTypeName == 'UPC' }">
					 ${purse.purseTypeName}-${purse.upc}
					</c:if>
					 <c:if test="${purse.purseTypeName !=null && purse.purseTypeName == 'LOYALTY' }">
					${purse.purseTypeName}-${purse.currCodeAlpha} 
					</c:if> --%>

																	<%-- <c:if
																		test="${purse.purseType['purseTypeName'] !=null && purse.purseType['purseTypeName'] == 'CURRENCY' }">
					 ${purse.purseType['purseTypeName']}-${purse.currencyCode['currCodeAlpha']}
					</c:if>
																	<c:if
																		test="${purse.purseType['purseTypeName'] !=null && purse.purseType['purseTypeName'] == 'UPC' }">
					 ${purse.purseType['purseTypeName']}-${purse.upc}
					</c:if>
																	<c:if
																		test="${purse.purseType['purseTypeName'] !=null && purse.purseType['purseTypeName'] == 'LOYALTY' }">
					${purse.purseType['purseTypeName']}-${purse.currencyCode['currCodeAlpha']} 
					</c:if> --%>
					${purse.extPurseId} 

																</form:option>

															</c:forEach>

														</form:select>
														<div>
															<form:errors path="supportedPurseUpdate" cssClass="error"
																cssStyle="color:red"></form:errors>
														</div>


													</div>
													 <div class="col-lg-3 padding">
												<div class="col-sm-4 up padding">
													<button type="button" id="purse_move_up"
														class="btn btn-block">
														<i class="glyphicon glyphicon-arrow-up"></i>
													</button>
												</div>
												<div class="col-sm-4 down padding">
													<button type="button" id="purse_move_down"
														class="btn btn-block col-sm-6">
														<i class="glyphicon glyphicon-arrow-down"></i>
													</button>
												</div>
											</div> 
												</div>

												<!-- Multi select for card range ends -->





											</div>

											<!-- purse ends here -->

										</div>

										<br>&nbsp; <br>&nbsp;
										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Package ID">Package ID<font color='red'>*</font></label>
											</div>



											<!-- Multi select for Package ID starts -->

											<div class="col-lg-8">
												<div class="col-lg-4 ">
													<div>Available</div>
													<form:select path="" name="search_CardRange" id="package"
														class="dropdown-medium" size="8" multiple="multiple"
														disabled="true">

														<c:forEach items="${packageData}" var="pkg">
															<c:if
																test="${pkg.packageId !=null && pkg.packageId !='' && pkg.description!=null && pkg.description!=''}">
																<form:option value="${pkg.packageId}">${pkg.packageId}-${pkg.description}</form:option>
															</c:if>
														</c:forEach>
													</form:select>
												</div>

												<div class="col-lg-1 col-lg-offset-1 padding">
													<br />
													<button type="button" id="package_rightAll"
														onblur="showDefaultpkg();" class="btn btn-block"
														disabled="disabled">
														<i class="glyphicon glyphicon-forward"></i>
													</button>
													<button type="button" id="package_rightSelected"
														onblur="showDefaultpkg();" class="btn btn-block"
														disabled="disabled">
														<i class="glyphicon glyphicon-chevron-right"></i>
													</button>
													<button type="button" id="package_leftSelected"
														onblur="showDefaultpkg();" class="btn btn-block"
														disabled="disabled">
														<i class="glyphicon glyphicon-chevron-left"></i>
													</button>
													<button type="button" id="package_leftAll"
														onblur="showDefaultpkg();" class="btn btn-block"
														disabled="disabled">
														<i class="glyphicon glyphicon-backward"></i>
													</button>
												</div>

												<div class="col-lg-4 padding">
													<c:set var="packageList" value="${productForm.packageId}" />
													Selected
													<form:select name="to[]" id="package_to"
														path="packageUpdate" class="dropdown-medium" size="8"
														multiple="multiple" disabled="true">

														<c:forEach items="${packageList}" var="pkg">
															<c:if
																test="${pkg.packageId !=null && pkg.packageId !=''}">
																<form:option value="${pkg.packageId}">${pkg.packageId}-${pkg.description}</form:option>
															</c:if>
														</c:forEach>

													</form:select>
													<div>
														<form:errors path="packageUpdate" cssClass="error"
															cssStyle="color:red"></form:errors>
													</div>


												</div>
												<!-- <div class="col-lg-3 padding">
												<div class="col-sm-3 up padding">
													<button type="button" id="package_move_up"
														class="btn btn-block">
														<i class="glyphicon glyphicon-arrow-up"></i>
													</button>
												</div>
												<div class="col-sm-3 down padding">
													<button type="button" id="package_move_down"
														class="btn btn-block col-sm-6">
														<i class="glyphicon glyphicon-arrow-down"></i>
													</button>
												</div>
											</div> -->
											</div>
										</div>

										<br>&nbsp;
										<!-- Multi select for Package ID ends -->

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Default Purse"><spring:message
														code="product.defaultPurse" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">
												<form:select path="prodAttributes['defaultPurse']"
													class="dropdown-medium" id="defaultPurse"
													name="defaultPurse"
													onblur="return validateDropDown(this.form.id,this.id);"
													disabled="true">
													<option value="NONE">--- Select ---</option>
													<c:forEach items="${purseList}" var="purse">
														<c:if test="${purse.purseId !=null && purse.purseId !='' && purse.purseType.purseTypeId == '1'}">
															<form:option value="${purse.purseId}">${purse.extPurseId}</form:option>
														</c:if>
													</c:forEach>
												</form:select>
												<div>
													<form:errors path="prodAttributes['defaultPurse']"
														id="defaultPurse" cssStyle="color:red" />
												</div>

											</div>
										</div>



										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Default Package"><spring:message
														code="product.defaultPackage" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">
												<form:select path="prodAttributes['defaultPackage']"
													class="dropdown-medium" id="defaultPackage"
													name="defaultPackage"
													onblur="return validateDropDown(this.form.id,this.id);"
													disabled="true">
													<option value="NONE">--- Select ---</option>
													<c:forEach items="${packageList}" var="pkg">
														<c:if test="${pkg.packageId !=null && pkg.packageId !=''}">
															<form:option value="${pkg.packageId}">${pkg.packageId}-${pkg.description}</form:option>
														</c:if>
													</c:forEach>
												</form:select>
												<div>
													<form:errors path="prodAttributes['defaultPackage']"
														id="defaultPackage" cssStyle="color:red" />
												</div>

											</div>
										</div>




										<!-- default purse and package ends -->


										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="CCF Fromat Version"><spring:message
														code="product.ccfFormatVersion" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">

												<form:select path="prodAttributes['ccfFormatVersion']"
													class="dropdown-medium" id="ccfFormatVersion"
													name="ccfFormatVersion"
													onblur="return validateDropDown(this.form.id,this.id);"
													disabled="true">
													<option value="NONE">--- Select ---</option>
													<form:options items="${ccfData}" />
												</form:select>
												<div>
													<form:errors path="prodAttributes['ccfFormatVersion']"
														id="ccfFormatVersion" cssStyle="color:red" />
												</div>

											</div>
										</div>
										
										
									<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="International Transaction Support"><spring:message
														code="product.internationalSupport" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-8">
												<form:radiobutton path="prodAttributes['internationalSupported']"
													id="internationalSupport" name="internationalSupport" value="Disable"
													checked="checked" disabled="true"/>
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['internationalSupported']"
													value="Enable" id="internationalSupport" name="internationalSupport"
													data-skin="square" data-color="blue"  disabled="true" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>



										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Currency Supported"><spring:message
														code="product.currencySupported" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">

												<!-- Multi select for card range starts -->

												<div class="col-lg-12">
													<div class="col-lg-4 ">
														<div>Available</div>
														<form:select path="" name="partnerCurrency"
															id="partnerCurrency" class="dropdown-medium" size="8"
															multiple="multiple" disabled="true">

															<c:forEach items="${availablePartnerCurrencyList}"
																var="partnerCurrency">

																<form:option value=" ${partnerCurrency.currencyTypeID}">
				                                                  ${partnerCurrency.currencyTypeID}: ${partnerCurrency.currCodeAlpha} : ${partnerCurrency.currencyDesc}
				                                                 </form:option>

															</c:forEach>

														</form:select>
													</div>

													<div class="col-lg-1 padding ">
														<br />
														<button type="button" id="partnerCurrency_rightAll"
															class="btn btn-block" disabled="disabled">
															<i class="glyphicon glyphicon-forward"></i>
														</button>
														<button type="button" id="partnerCurrency_rightSelected"
															class="btn btn-block" disabled="disabled">
															<i class="glyphicon glyphicon-chevron-right"></i>
														</button>
														<button type="button" id="partnerCurrency_leftSelected"
															class="btn btn-block" disabled="disabled">
															<i class="glyphicon glyphicon-chevron-left"></i>
														</button>
														<button type="button" id="partnerCurrency_leftAll"
															class="btn btn-block" disabled="disabled">
															<i class="glyphicon glyphicon-backward"></i>
														</button>
													</div>

													<div class="col-lg-4 ">

														<c:set var="currencyList"
															value="${productForm.partnerCurrency}" />

														<div>Selected</div>
														<form:select name="to[]" id="partnerCurrency_to"
															path="partnerCurrencyUpdate" class="dropdown-medium"
															size="8"  multiple="multiple"
															disabled="true">

															<c:forEach items="${currencyList}" var="currency">

																<form:option value="${currency.getCurrencyTypeID}">
																	 ${currency.currencyTypeID}: ${currency.currCodeAlpha} : ${currency.currencyDesc}
																</form:option>

															</c:forEach>
														</form:select>
														<div>
															<form:errors path="partnerCurrencyUpdate"
																cssClass="error" cssStyle="color:red"></form:errors>
														</div>


													</div>
													<!-- <div class="col-lg-3 ">
														<div class="col-sm-4 up ">
															<button type="button" id="partnerCurrency_move_up"
																class="btn btn-block">
																<i class="glyphicon glyphicon-arrow-up"></i>
															</button>
														</div>
														<div class="col-sm-4 down ">
															<button type="button" id="partnerCurrency_move_down"
																class="btn btn-block col-sm-6">
																<i class="glyphicon glyphicon-arrow-down"></i>
															</button>
														</div>
													</div> -->
												</div>

												<!-- Multi select for card range ends -->






											</div>
										</div>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="PIN Supported"><spring:message
														code="product.pinSupported" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-4">
												<form:radiobutton path="prodAttributes['pinSupported']"
													id="pinSupported" name="pinSupported" value="Disable"
													checked="checked" disabled="true" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['pinSupported']"
													value="Enable" id="pinSupported" name="pinSupported"
													data-skin="square" data-color="blue" disabled="true" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="CVV Supported"><spring:message
														code="product.cvvSupported" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-4">
												<form:radiobutton path="prodAttributes['cvvSupported']"
													id="cvvSupported" name="cvvSupported" value="Disable"
													checked="checked" disabled="true" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['cvvSupported']"
													value="Enable" id="cvvSupported" name="cvvSupported"
													data-skin="square" data-color="blue" disabled="true" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Multi Purse Supported"><spring:message
														code="product.multiPurseSupport" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-4">
												<form:radiobutton path="prodAttributes['multiPurseSupport']"
													id="multiPurseSupport" name="multiPurseSupport" value="Disable"
													checked="checked" disabled="true" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['multiPurseSupport']"
													value="Enable" id="multiPurseSupport" name="multiPurseSupport"
													data-skin="square" data-color="blue" disabled="true" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>
										
										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Limit Supported"><spring:message
														code="product.limitSupported" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-4">
												<form:radiobutton path="prodAttributes['limitSupported']"
													id="limitSupported" name="limitSupported" value="Disable"
													checked="checked" disabled="true" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['limitSupported']"
													value="Enable" id="limitSupported" name="limitSupported"
													data-skin="square" data-color="blue" disabled="true" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>


										<%-- <div class="col-lg-12">

								<div class="col-lg-4">
									<label for="Transaction Fee Supported"><spring:message
											code="product.transactionFeeSupported" /> <font color='red'></font></label>
								</div>
								<div class="col-lg-4">
									<form:radiobutton
										path="prodAttributes['transactionFeeSupported']"
										id="transactionFeeSupported" name="transactionFeeSupported"
										value="Enable" checked="checked" />
									<label class='radiobox-line'>Enable</label>
									<form:radiobutton
										path="prodAttributes['transactionFeeSupported']"
										value="Disable" id="transactionFeeSupported"
										name="transactionFeeSupported" data-skin="square"
										data-color="blue" />
									<label class='radiobox-line'>Disable</label>
								</div>

							</div> --%>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Transaction Fee Supported"><spring:message
														code="product.transactionFeeSupported" /> <font
													color='red'></font></label>
											</div>
											<div class="col-lg-4">
												<form:radiobutton path="prodAttributes['feesSupported']"
													id="feesSupported" name="feesSupported" value="Disable"
													checked="checked" disabled="true" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['feesSupported']"
													value="Enable" id="feesSupported" name="feesSupported"
													data-skin="square" data-color="blue" disabled="true" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Maintenance Fee Supported"><spring:message
														code="product.maintenanceFeeSupported" /> <font
													color='red'></font></label>
											</div>
											<div class="col-lg-4">
												<form:radiobutton
													path="prodAttributes['maintainanceFeeSupported']"
													checked="checked" id="maintainanceFeeSupported"
													name="maintainanceFeeSupported" value="Disable"
													disabled="true" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton
													path="prodAttributes['maintainanceFeeSupported']"
													value="Enable" id="maintainanceFeeSupported"
													name="maintainanceFeeSupported" data-skin="square"
													data-color="blue" disabled="true" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>



										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="SMS and Email Alerts Supported"><spring:message
														code="product.smsandemail" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-4">
												<form:radiobutton path="prodAttributes['alertSupported']"
													id="smsandemail" name="smsandemail" value="Disable"
													checked="checked" disabled="true" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['alertSupported']"
													value="Enable" id="smsandemail" name="smsandemail"
													data-skin="square" data-color="blue" disabled="true" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>
										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Txn based on Card Status Supported"><spring:message
														code="product.txnBasedonCardStatus" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-4">
												<form:radiobutton
													path="prodAttributes['cardStatusSupported']"
													id="txnBasedonCardStatus" name="txnBasedonCardStatus"
													checked="checked" value="Disable" disabled="true" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton
													path="prodAttributes['cardStatusSupported']" value="Enable"
													id="txnBasedonCardStatus" name="txnBasedonCardStatus"
													data-skin="square" data-color="blue" disabled="true" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>


										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Activation"><spring:message
														code="product.activation" /> <font color='red'> *</font></label>
											</div>
											<div class="col-lg-8" id="activationvalid">
												<%-- <form:radiobutton path="prodAttributes['activationId']" id="txnBasedonCardStatus"
					name="txnBasedonCardStatus" value="Enable"/>
					<label class='radiobox-line'>Enable</label>
					<form:radiobutton  path="prodAttributes['cardStatusSupported']" value="Disable" id="txnBasedonCardStatus"
					name="txnBasedonCardStatus" checked="checked" data-skin="square" data-color="blue"/>
					<label class='radiobox-line'>Disable</label> --%>

												<form:checkbox path="activationId" id="activationId"
													name="activationId" value="PAN" disabled="true" />
												<spring:message code="product.pan" />
												<form:checkbox path="activationId" id="activationId"
													name="activationId" value="Serial Number" disabled="true" />
												<spring:message code="product.serialNumber" />
												<form:checkbox path="activationId" id="activationId"
													name="activationId" value="Proxy Number" disabled="true" />
												<spring:message code="product.proxyNumber" />
												<form:checkbox path="activationId" id="activationId"
													name="activationId" value="Account Number" disabled="true" />
												<spring:message code="product.accountNumber" />
												<form:checkbox path="activationId" id="activationId"
													name="activationId" value="Customer Id" disabled="true" />
												<spring:message code="product.customerId" />
											</div>

										</div>

										<br>&nbsp;

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Other Transactions"><spring:message
														code="product.otherTransactions" /> <font color='red'>
														*</font></label>
											</div>
											<div class="col-lg-8" id="othertxnvalid">

												<%-- <c:set var="othertxns" value="${productForm.prodAttributes['otherTxnId']}"/>		
					<input type="hidden" name="otherdata" id="otherdata" value="${othertxns}"/>	 --%>


												<form:checkbox path="txnTypeId" id="otherTxnId"
													name="txnTypeId" value="PAN" disabled="true" />
												<spring:message code="product.pan" />
												<form:checkbox path="txnTypeId" id="otherTxnId"
													name="txnTypeId" value="Serial Number" disabled="true" />
												<spring:message code="product.serialNumber" />
												<form:checkbox path="txnTypeId" id="otherTxnId"
													name="txnTypeId" value="Proxy Number" disabled="true" />
												<spring:message code="product.proxyNumber" />
												<form:checkbox path="txnTypeId" id="otherTxnId"
													name="txnTypeId" value="Account Number" disabled="true" />
												<spring:message code="product.accountNumber" />
												<form:checkbox path="txnTypeId" id="otherTxnId"
													name="txnTypeId" value="Customer Id" disabled="true" />
												<spring:message code="product.customerId" />


											</div>

										</div>



										<div class="col-lg-12">

											<div class="col-lg-4"></div>
											<div class="col-lg-8">

												<button type="button" class="btn btn-primary gray-btn"
													onclick="goBackToFormproductConfig();">
													<i class='glyphicon glyphicon-backward'></i>
													<spring:message code="button.back" />
												</button>


											</div>
										</div>


									</div>





									<!-- Form controls end  -->

								</div>




							</div>
						</div>
			</div>
		</div>

	</form:form>
</body>

<script>
	function goBackToFormproductConfig() {
		$("#viewProductForm").attr('action', 'productConfig');
		$("#viewProductForm").submit();

	}

	$('#formFactor input[type=radio]').change(function() {
		if ($(this).val() == 'Virtual') {
			$("#cardEncodingDiv").attr("style", "display:none");
			$("#intialDigitalInventoryQty").hide();
			$("#digitalInventoryAuotReplLevel").hide();
			$("#digitalAutoReplenishmentQty").hide();
			$("#SaleactiveCodeResponseType").hide();
			$('#productB2BType').attr('disabled', false);
			$("#productB2BType").val('B2B');
		} else if (($(this).val() == 'Physical') || ($(this).val() == 'IH')) {
			$("#cardEncodingDiv").attr("style", "display:block");
			$("#intialDigitalInventoryQty").hide();
			$("#digitalInventoryAuotReplLevel").hide();
			$("#digitalAutoReplenishmentQty").hide();
			$("#SaleactiveCodeResponseType").hide();
			$('#productB2BType').attr('disabled', false);
			$("#productB2BType").val('B2B');
		} else if ($(this).val() == 'Digital') {
			$("#cardEncodingDiv").hide();
			$("#intialDigitalInventoryQty").show();
			$("#digitalInventoryAuotReplLevel").show();
			$("#digitalAutoReplenishmentQty").show();
			$("#SaleactiveCodeResponseType").show();
			//Auto check retail Type
			$('#productRetailType')[0].checked=true;
			$("#productB2BType").val('');
			$("#productType input[type=radio]").trigger("change");
			$('#productB2BType').attr('disabled', true);
		}

	});

	$("#b2bProductFunding").change(function() {

		if ($("#b2bProductFunding").val() == 'CARD_ACTIVATION') {
			$("#srcOfFunding").attr("style", "display:block");
		} else {
			$("#srcOfFunding").attr("style", "display:none");
		}
	})

		$("input[id='internationalSupport']").change(function() {
			if ($("input[id='internationalSupport']:checked").val() == 'Enable') {
				$("#currencySupport").attr("style", "display:block");
			} else {
				$("#currencySupport").attr("style", "display:none");
			} 
	});
	
	$(function() {
		$('#productType input[type=radio]').change(function() {
			//alert ( $(this).val() ) 

			if ($(this).val() == 'Retail') {
				$("#retailUPC").attr("style", "display:block");
				$("#b2bUPC").attr("style", "display:none");
				$("#serialRequest").attr("style", "display:none");
				$("#intialB2BSerialNumQty").attr("style", "display:none");
				$("#autoRepLevel").attr("style", "display:none");
				$("#autoRepValue").attr("style", "display:none");
				$("#productFunding").attr("style", "display:none");
			}
			if ($(this).val() == 'B2B') {

				$("#b2bUPC").attr("style", "display:block");
				$("#retailUPC").attr("style", "display:none");
				$("#serialRequest").attr("style", "display:block");
				$("#intialB2BSerialNumQty").attr("style", "display:block");
				$("#autoRepLevel").attr("style", "display:block");
				$("#autoRepValue").attr("style", "display:block");
				$("#productFunding").attr("style", "display:block");

			}

		})
	})

	function myJsonMethod(response) {

		console.log(response);

		/* alert("response " + response); */
	}
	 function getPartnerCurrency(formId, eleId) {
			console.log('inside' + index);

		//	$("#partnerCurrency").html("");
			

			$("#partnerCurrency_to").html("");

			var datatosplit = "";
			datatosplit = $("#" + eleId + " option:selected").val();

			var splitted = datatosplit.split('~');
			var id = splitted[0];
			console.log('id' + id);
			var servUrl = $('#addurl').val();
			var options = "";

			var str = servUrl + "/ajax/getPartnerCurrency/" + id;
			console.log('str' + str);

			$.ajax({

				url : str,

				async : true,

				type : "GET",

				dataType : 'json',

				/* crossDomain: true,  */

				success : function(response, status, xhr) {

					$.each(response, function(index) {
						
						$("#partnerCurrency_to").append(
								'<option value="'+response[index]+'">'
										+ response[index] + '</option>');
										
			    		// $("#partnerCurrency option[value="+response[index]+"]").remove();


						console.log('index' + index);

					});

				}
			
			
			
			
			/* $.each(response, function(index){     
	    		  
	    		  $("#multiselectEdit_to").append('<option value="'+response[index].partnerId+'" readonly>'
	        			  +response[index].partnerName+'</option>');
	    		 	    		  
	    		 $("#multiselectEdit option[value="+response[index].partnerId+"]").remove();

	          }); 
	      } */
			
			
			
			
			
			
			

			});
		}

	function getCardRangeDetails() {

		$("#multiselect").html("");
		$("#multiselect_to").html("");

		var datatosplit = "";

		datatosplit = $('#productIssuerName').val();

		var splitted = datatosplit.split('~');
		var id = splitted[0];

		var servUrl = $('#addurl').val();

		//alert(servUrl);

		$.ajax({

			//url: "http://localhost:8080/cclp-vms-1/ajax/getApprovedCardRangeDetails/"+id,
			url : servUrl + "/ajax/getApprovedCardRangeDetails/" + id,

			async : true,

			type : "GET",

			dataType : 'json',

			/* crossDomain: true, */

			success : function(response, status, xhr) {

				$.each(response,
						function(index) {

							$("#multiselect").append(
									'<option value="'+response[index].cardRangeId+'">'
											+ response[index].prefix + ''
											+ response[index].startCardNbr
											+ '-' + response[index].prefix + ''
											+ response[index].endCardNbr
											+ '</option>');

							//${cardRange.prefix}${cardRange.startCardNbr}-${cardRange.prefix}${cardRange.endCardNbr}

							//console.log(response.data);

						});

			}

		});

		/* var opt = $('#multiselect > option');
		if(opt.length == 0){
		 generateAlert("addProductForm", "multiselect","multiselect.noCardRange");
		clearError("multiselect_to");
		 return false;
		  
		} */

	}

	 if ($("input[id='internationalSupport']:checked").val() == 'Enable') {
			$("#currencySupport").attr("style", "display:block");
		} else {
			$("#currencySupport").attr("style", "display:none");

		} 
	 
	function checkCardRangeExist() {
		$('#multiselect option').prop('selected', true);
		options = $('#multiselect > option:selected');
		if (options.length == 0) {
			generateAlert("editProductForm", "multiselect",
					"multiselect.noCardRange");
			clearError("multiselect_to");
			return false;

		} else {
			clearError("multiselect");
		}
	}
	/* 	function timepick()

	 {
	 $('#cutoverTime').timepicker({
	 'scrollDefault' : 'now'
	 });
	

	 } */

	$(function() {
		$('#cutoverTime').timepicker({
			timeFormat : 'H:i:s'
		});
	});

	$('#multiselect').multiselect();
	$('#purse').multiselect({
		keepRenderingSort: true
	});
	$('#package').multiselect();
	$('#denom').multiselect();
	$('#partnerCurrency').multiselect();


	function onloadDiv() {
		if ($("#formFactorPhysical").is(":checked"))
			$("#cardEncodingDiv").attr("style", "display:block");

		if ($("#formFactorIH").is(":checked"))
			$("#cardEncodingDiv").attr("style", "display:block");

		if ($('#formFactorVirtual').is(":checked"))
			$("#cardEncodingDiv").attr("style", "display:none");

		if ($("#formFactorDigital").is(":checked")) {
			$("#cardEncodingDiv").hide();
			$("#intialDigitalInventoryQty").show();
			$("#digitalInventoryAuotReplLevel").show();
			$("#digitalAutoReplenishmentQty").show();
			$("#SaleactiveCodeResponseType").show();
			$('#productRetailType').attr('checked', true);
			$('#productB2BType').attr('disabled', true);


		}

		$("input[id='internationalSupport']").change(function() {
			if ($("input[id='internationalSupport']:checked").val() == 'Enable') {
				$("#currencySupport").attr("style", "display:block");
			} else {
				$("#currencySupport").attr("style", "display:none");

			}
		});
		
		$("#activeFromOld").val($("#activeFrom").val());
		$("#productValidityDateOld").val($("#productValidityDate").val());

		if ($("#productRetailType").is(":checked")) {
			$("#retailUPC").attr("style", "display:block");
			$("#b2bUPC").attr("style", "display:none");
			$("#serialRequest").attr("style", "display:none");
			$("#intialB2BSerialNumQty").attr("style", "display:none");
			$("#autoRepLevel").attr("style", "display:none");
			$("#autoRepValue").attr("style", "display:none");
			$("#productFunding").attr("style", "display:none");
			$("#srcOfFunding").attr("style", "display:none");

		}

		if ($("#productB2BType").is(":checked")) {

			$("#b2bUPC").attr("style", "display:block");
			$("#retailUPC").attr("style", "display:none");
			$("#serialRequest").attr("style", "display:block");
			$("#intialB2BSerialNumQty").attr("style", "display:block");
			$("#autoRepLevel").attr("style", "display:block");
			$("#autoRepValue").attr("style", "display:block");
			$("#productFunding").attr("style", "display:block");
			if ($("#b2bProductFunding").val() == 'CARD_ACTIVATION') {
				$("#srcOfFunding").attr("style", "display:block");
			} else {
				$("#srcOfFunding").attr("style", "display:none");
			}
		}

		var data = $("#denomtyp").val();

		if (data != null && data == 'Variable') {
			$("#denominationId").val('Variable');

			$("#denomFixed").attr("style", "display:none");
			$("#denomVar").attr("style", "display:block");
			$("#denomSelect").attr("style", "display:none");

		}

		if (data != null && data == 'Fixed') {
			$("#denominationId").val('Fixed');

			$("#denomFixed").attr("style", "display:block");
			$("#denomVar").attr("style", "display:none");
			$("#denomSelect").attr("style", "display:none");

		}

		if (data != null && data == 'Select') {
			$("#denominationId").val('Select');

			$("#denomFixed").attr("style", "display:none");
			$("#denomVar").attr("style", "display:none");
			$("#denomSelect").attr("style", "display:block");

		}
		//showDefaultpurse();

	}

	function showDefaultpurse() {
		var opts = $('#purse_to')[0].options;

		var array = $.map(opts, function(elem) {

			var data = elem.value + "~" + elem.text;

			return (data);

		});

		if (array.length > 0) {
			var options = '';

			$.each(array, function(val, text) {

				var splitted = text.split('~');

				var value = splitted[0];
				var name = splitted[1];

				options += '<option value="'+value+'">' + name + '</option>';

				$('#defaultPurse').html(options);

			});

		}

		else {

			var options = '<option value=""><strong>--- NONE ---</strong></option>'
			$('#defaultPurse').html(options);

		}

	}

	function showDefaultpkg() {
		var opts = $('#package_to')[0].options;

		var array = $.map(opts, function(elem) {
			//return (elem.value || elem.text);

			var data = elem.value + "~" + elem.text;

			return data;
		});

		if (array.length > 0) {
			var options = '<option value=""><strong>--- NONE ---</strong></option>';

			$.each(array, function(val, text) {

				var splitted = text.split('~');

				var value = splitted[0];
				var name = splitted[1];

				options += '<option value="'+value+'">' + name + '</option>';

				$('#defaultPackage').html(options);

			});
		}

		else {

			var options = '<option value=""><strong>--- NONE ---</strong></option>'
			$('#defaultPackage').html(options);

		}

	}

	function AddDenomSelectFields() {
		if (true)
			//return false;

			var val = ''
		var length = 0
		var flag = false

		if ($('#denom').val() >= 0) {

			//val=document.frmProduct.selFields.value;

			val = $('#denom').val();

			//alert("val "+val);

			for (i = 0; i < document.editProductForm.denom_to.options.length; i++) {

				var valOptions = document.editProductForm.denom_to.options[i].text;
				if (valOptions == val) {
					flag = true;
				}
			}
			if (flag == false) {
				document.editProductForm.denom_to.options[document.editProductForm.denom_to.options.length] = new Option(
						val);
				$('#denom').val("");
			} else {
				return false;
			}
		}
	}

	function RemoveSelFields() {
		var val = ''
		var txt = ''
		var length = 0
		var flag = 'selected'

		for (var i = 0; i < document.editProductForm.denom_to.options.length; i++) {
			if (document.editProductForm.denom_to.options[i].selected == true) {
				txt = document.editProductForm.denom_to.options[i].text;
				document.editProductForm.denom.value = txt;
			}
		}
		for (var i = document.editProductForm.denom_to.options.length - 1; i >= 0; i--) {
			if (document.editProductForm.denom_to.options[i].selected == true) {
				document.editProductForm.denom_to.options[i] = null
			}
		}
		if (document.editProductForm.denom_to.options.length > 0) {
			if (document.editProductForm.denom_to.options[0].selected == true) {
				document.editProductForm.denom_to.options[0] = null
			}
		}
	}

	$("#package").dblclick(function() {
		showDefaultpkg();
	});

	$("#purse").dblclick(function() {
		showDefaultpurse();
	});

	$(document).ready(function activationcheck() {
		$("#btnSubmit").click(function() {
			checkact();
		});
	});

	$(document).ready(function transactioncheck() {
		$("#btnSubmit").click(function() {
			checkothertxns();
		});
	});

	function checkact() {
		var isValid = true;
		var selectedActCheckBox = new Array();
		$('input[name="activationId"]:checked').each(function() {
			selectedActCheckBox.push(this.value);

		});
		if (selectedActCheckBox.length == 0) {
			isValid = false;
			$("#activationvalid").next().html('');
			$("#activationvalid").after(
					'<div><span class="error_empty" style="color:red;"><br/>'
							+ "Please select atleast one Activation"
							+ '</span> </div>');
			return isValid;
		}

		$("#activationvalid").next().html('');
		return isValid;
	}

	function checkothertxns() {
		var isValid = true;
		var selectedTxnCheckBox = new Array();
		$('input[name="txnTypeId"]:checked').each(function() {
			selectedTxnCheckBox.push(this.value);

		});
		if (selectedTxnCheckBox.length == 0) {
			isValid = false;
			$("#othertxnvalid").next().html('');
			$("#othertxnvalid").after(
					'<div><span class="error_empty" style="color:red;"><br/>'
							+ "Please select atleast one Other Transactions"
							+ '</span> </div>');
			return isValid;
		}

		$("#othertxnvalid").next().html('');
		return isValid;
	}
</script>