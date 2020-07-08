<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
	src="${pageContext.request.contextPath}/resources/js/jquery.timepicker.js"></script>

<link
	href="${pageContext.request.contextPath}/resources/css/jquery.timepicker.css"
	rel="stylesheet" />

<!-- added by nawaz for autocomplete -->
<link
	href="${pageContext.request.contextPath}/resources/css/autocomp/smooth/jquery-ui.css"
	rel="stylesheet" />
<link
	href="${pageContext.request.contextPath}/resources/css/autocomp/jquery-ui.css"
	rel="stylesheet" />
<%-- <script src="${pageContext.request.contextPath}/resources/js/autocomp/jquery-ui.js"></script> --%>
<!-- ends -->
<style>
/* 
.col-sm-2.up {
    margin: -123px 3px 0px 530px;
}

.col-sm-2.down {
    margin: -89px 3px 0px 530px;
}


.col-sm-4.up {
    margin: -123px 3px 0px 288px;
}

.col-sm-4.down {
    margin: -89px 3px 0px 288px;
}


.col-sm-3.up{
    margin: -123px 3px 0px 288px;
}

.col-sm-3.down{
    margin: -90px 3px 0px 288px;
} */
.up {
	margin: -130px 3px 0px 688px;
}

.down {
	margin: -99px 3px 0px 688px;
}
</style>


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
							dateFormat : 'mm/dd/yyyy',
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

	function onloadDiv() {
		if ($("#formFactorPhysical").is(":checked"))
			$("#cardEncodingDiv").attr("style", "display:block");

		if ($('#formFactorVirtual').is(":checked"))
			$("#cardEncodingDiv").attr("style", "display:none");

		if ($('#formFactorIH').is(":checked"))
			$("#cardEncodingDiv").attr("style", "display:block");

		if ($("#formFactorDigital").is(":checked")) {
			$("#cardEncodingDiv").hide();
			$("#intialDigitalInventoryQty").show();
			$("#digitalInventoryAuotReplLevel").show();
			$("#digitalAutoReplenishmentQty").show();
			$("#SaleactiveCodeResponseType").show();
			$('#productRetailType').attr('checked', true);
			$('#productB2BType').attr('disabled', true);

		}
		/* if ($("#internationalSupport").is(":checked"))
			$("#currencySupport").attr("style", "display:block"); */

			 if ($("input[id='internationalSupport']:checked").val() == 'Enable') {
				$("#currencySupport").attr("style", "display:block");
			} else {
				$("#currencySupport").attr("style", "display:none");

			} 
		

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

		// for getting the dropdown selected as incomm in default
		var objSelect = document.getElementById("productIssuerName");

		var m = "${defaultIssuer}";

		//Set selected
		setSelectedValue(objSelect, m.toUpperCase());

		function setSelectedValue(selectObj, valueToSet) {

			for (var i = 0; i < selectObj.options.length; i++) {

				if ((selectObj.options[i].text).toUpperCase() == valueToSet) {

					selectObj.options[i].selected = true;
					getCardRangeDetails();
					return;
				}
			}
		}

		// for getting the currencyusd selected in default
		var values = document.getElementById("purse");
		var currencyUsd = "CURRENCY-USD";

		setMultiSelectedValue(values, currencyUsd.toUpperCase());

		function setMultiSelectedValue(selectObj, valueToSet) {

			for (var i = 0; i < selectObj.options.length; i++) {

				if ((selectObj.options[i].text).toUpperCase() == valueToSet) {
					selectObj.options[i].selected = true;
					document.getElementById('purse_rightSelected').click();
					showDefaultpurse();
					return;
				}
			}
		}

		//showDefaultpurse();

	}
</script>

<script>
	function loadFrames() {

	}

	/* $(function() {
		$('.date-picker')
				.datepicker(
						{
							changeMonth : true,
							changeYear : true,
							showButtonPanel : true,
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
						});
	});
	 */
	/* $('#datetimepicker').datetimepicker({
	 inline:true
	 }); */

	/* jQuery(document)
			.ready(
					function($) {
						$('#multiselect').multiselect();
						$('#multiselect_p').multiselect();
						var $lastChar = 1;
						var $newRow;

						$get_lastID = function() {
							var $id = $(
									'#expense_table tr:last-child td:first-child input')
									.attr("name");

							var tempid = $id;

							alert("id  " + tempid);

							var tempchar = "";

							tempchar = parseInt(tempid
									.substr(tempid.length - 2), 10);

							alert("tempchar " + tempchar);

							$lastChar = parseInt($id.substr($id.length - 2), 10);

							alert("lastchar " + $lastChar);

							if (isNaN($lastChar)) {
								$lastChar = 0;
							} else {
								$lastChar = $lastChar + 1;
							}

							$newRow = "<tr id='rowNo_'" + $lastChar + "> \
	            <td><input type='radio' name='defaultPurse' value='"+ $lastChar +"' /></td> \
	            <td><input type='text' name='subjects_0"+$lastChar+"' maxlength='255' /></td> \
	            <td><input type='button' value='Delete' class='del_ExpenseRow' onclick='removeRow(' + $lastChar+')' /></td> \
	        </tr>"
							return $newRow;
						}

						$('#add_ExpenseRow').click(function() {
							if ($('#expense_table tr').size() <= 9) {
								$get_lastID();
								$('#expense_table tbody').append($newRow);

							} else {
								alert("Reached Maximum Rows!");
							}
							;
						});

						function removeRow(removeNum) {
							alert('hi');
							jQuery('#rowNo_' + removeNum).remove();
						}
						;

					});
	 */

	/* for card range */
</script>

<body class="dashboard" onload="onloadDiv()">

	<div>
		<ol class="breadcrumb col-lg-11">
			<li>
				<div class="breadCrumb-blue"></div>
			</li>

		</ol>
	</div>

	<form:form name="addProductForm" id="addProductForm" method="POST"
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
										<font color='red'>*</font></label>

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

									</p>


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
							<%-- <button type="button" onclick="deleteIssuer();"
								class="btn btn-primary"><i class="glyphicon glyphicon-plus"></i><spring:message
									code="addProductserialNumberRequest.Submit" /></button> --%>

							<%-- 							<button type="button" onclick="goResetProduct();"
												class="btn btn-primary gray-btn">
											<i class='glyphicon glyphicon-refresh'></i>
										<spring:message code="button.reset" /></button> --%>
							<button data-dismiss="modal" onclick="goToPrevious()"
								class="btn btn-primary gray-btn">
								<spring:message code="button.close" />
							</button>


						</div>

					</div>

				</div>
			</div>

			<div class="container">
				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs  col-lg-11 col-lg-offset-1">
							<li class="active SubMenu"><a href="#" data-toggle="tab"><spring:message
										code="product.addProduct" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active graybox col-lg-11 col-lg-offset-1">
								<div class="text-right Error-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<div class="form-inline">
									<c:if test="${statusMessage!='' && statusMessage!=null}">
										<div class="aln error-red">
											<center>
												<b>${statusMessage}</b>
											</center>
										</div>
									</c:if>
									<c:if test="${status!='' && status!=null}">
										<div class="aln success-green">
											<center>
												<b>${status}</b>
											</center>
										</div>
									</c:if>
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
										<!-- -for copy from option -->

										<div class="col-lg-12">
											<c:choose>
												<c:when test="${showCopyFrom=='true' }">
													<div class="col-lg-4">
														<label for="copyFrom"><spring:message
																code="Product.copyFrom" /></label>
													</div>
													<form:hidden path="productId" />
													<!-- <div class="col-lg-8 ui-widget"> -->
													<div class="ui-widget">

														<form:input path="parentProductName"
															onkeyup="return isAlphaNumericWithNewSpecialCharsProdName(this)"
															maxlength="100" id="combobox" class="dropdown-medium" />

														<%-- <form:options items="${parentProductMap}"/> --%>


														<button type="button" id="addProductForm"
															class="btn btn-primary" onclick="getProductDetails()">
															<spring:message code="button.copy" />
														</button>
														<form:checkbox path="copyAllCheck" id="copyAllCheck"
															name="copyAllCheck" value="Y" />
														<spring:message code="product.copyall" />

														<div>
															<span id="parentProductIdError"></span>
														</div>
													</div>
												</c:when>
												<c:otherwise>
													<form:hidden path="productId" value="" />
												</c:otherwise>
											</c:choose>

										</div>
										<!-- end -->

										<!-- Form controls start  -->

										<div class="col-lg-12">


											<form:hidden path="issuerId" id="issuerId" name="issuerId"
												value="" />
											<form:hidden path="partnerId" id="partnerId" name="partnerId"
												value="" />

											<div class="col-lg-4">

												<label for="Product Name"><spring:message
														code="product.productName" /> <font color='red'>*</font></label>

											</div>

											<div class="col-lg-8">

												<form:input title="Allowed Special Characters are .,;'_- "
													path="productName" id="productName" class="textbox-large"
													type="textarea" minlength="2" maxlength="100"
													onkeyup="return isAlphaNumericWithNewSpecialCharsProdName(this)"
													onblur="validateFields(this.form.id,this.id);" />
												<div>
													<form:errors path="productName" id="productName"
														cssStyle="color:red" />


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
													onkeyup="return isAlphaNumericWithSpace(this);"
													onblur="validateFields(this.form.id,this.id);" />

												<div>
													<form:errors path="productShortName" id="productShortName"
														class="textbox-large" cssStyle="color:red" />
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
													rows="5" cols="51" style="resize:none" />
												<%-- onblur="validateFields(this.form.id,this.id);"/> --%>

												<div>
													<form:errors path="description" id="description"
														cssStyle="color:red" />

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
													onChange="getProgramIds(this.form.id,this.id);getSupportedPurses();showDefaultpurse();getPartnerCurrency(this.form.id,this.id);setTimeout(checkPartnerCurrencyExist, 100);"
													onblur="return validateDropDown(this.form.id,this.id);">
													<%-- onchange="processPartner(this.form.id);"  --%>
													<form:option value="NONE" label="--- Select ---" />
													<%-- <form:options items="${partnerDropDown}" /> --%>
													<c:forEach items="${partnerDropDown}" var="partner">
														<option
															value="${partner.partnerId}~${partner.partnerName}"
															<c:if test="${partner.partnerName eq productForm.partnerName}">selected="true"</c:if>>
															<c:out value="${partner.partnerName}" /></option>
													</c:forEach>
												</form:select>
												<div>
													<form:errors path="partnerName" id="partnerName"
														cssClass="error" cssStyle="color:red" />
												</div>
											</div>
										</div>

										<!-- Phase 2B Change for Program ID -->
										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Program ID Name"><spring:message
														code="product.programIdName" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">


												<form:select id="programId" path="programId"
													class="dropdown-medium"
													onblur="return validateDropDown(this.form.id,this.id);">

													<form:option value="-1" label="--- Select ---" />
													<c:forEach items="${programDropDown}" var="program">
														<option value="${program.programID}"
															<c:if test="${program.programID eq productForm.programId}">selected="true"</c:if>>
															<c:out value="${program.programIDName}" /></option>
													</c:forEach>
												</form:select>
												<div>
													<form:errors path="programId" id="programId"
														cssClass="error" cssStyle="color:red" />
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
													onChange="getCardRangeDetails();setTimeout(checkCardRangeExist, 100);">
													<option value="NONE">--- Select ---</option>
													<c:forEach items="${issuerDropDown}" var="issuer">
														<option value="${issuer.issuerId}~${issuer.issuerName}"
															<c:if test="${ issuerFlag !='true'}">
												<c:if test="${issuer.issuerName eq productForm.issuerName}">selected="true"</c:if>>
															${issuer.issuerName}
															</c:if>
															<c:if test="${ issuerFlag =='true'}">
																<option value="${issuer.issuerId}~${issuer.issuerName}">${issuer.issuerName}
																</option>
															</c:if>

														</option>

													</c:forEach>

												</form:select>

												<div>
													<form:errors path="issuerName" cssClass="error"
														cssStyle="color:red" />
												</div>


											</div>
										</div>

										<input type="hidden" id="addurl" name="addurl"
											value="${addUrl}" /> <br>&nbsp;

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
															style="width: 322px;" multiple="multiple">
														</form:select>
													</div>

													<div class="col-lg-1 col-lg-offset-1 padding">
														<br />
														<button type="button" id="multiselect_rightAll"
															class="btn btn-block">
															<i class="glyphicon glyphicon-forward"></i>
														</button>
														<button type="button" id="multiselect_rightSelected"
															class="btn btn-block">
															<i class="glyphicon glyphicon-chevron-right"></i>
														</button>
														<button type="button" id="multiselect_leftSelected"
															class="btn btn-block">
															<i class="glyphicon glyphicon-chevron-left"></i>
														</button>
														<button type="button" id="multiselect_leftAll"
															class="btn btn-block">
															<i class="glyphicon glyphicon-backward"></i>
														</button>
													</div>

													<div class="col-lg-4 ">
														<c:set var="carddetails" value="${productForm.cardRanges}" />
														<div>Selected</div>
														<form:select name="to[]" id="multiselect_to"
															path="cardRangesUpdate" class="dropdown-medium" size="8"
															style="width: 322px;" multiple="multiple">

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


													</div>

													<div class="col-lg-3 padding">
														<div class="col-sm-4 up padding">
															<button type="button" id="multiselect_move_up"
																class="btn btn-block">
																<i class="glyphicon glyphicon-arrow-up"></i>
															</button>
														</div>
														<div class="col-sm-4 down padding">
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

										<%-- 							<div class="col-lg-12">

								<div class="col-lg-3 space">
									<label for="Parent Product Name"><spring:message
											code="product.parentProductName" />  <font color='red'></font></label>
								</div>
								<div class="col-lg-9">


									
									<form:select id="parentProductName" name="parentProductName"
										path="parentProductName" class="dropdown-medium">
										<form:option value="0~NONE" label="--- Select ---" />
										<c:forEach items="${parentProductDropDown}" var="parentProd">
											<option
												value="${parentProd.productId}~${parentProd.productName}"
												<c:if test="${parentProd.productName eq productForm.parentProductName}">selected="true"</c:if>>${parentProd.productName}</option>

										</c:forEach>

									</form:select>
									<div>
										<form:errors path="parentProductName" id="parentProductName" name="parentProductName" cssClass="error"
											cssStyle="color:red" />
									</div>
								</div>
							</div> --%>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Form Factor"><spring:message
														code="product.formFactor" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-8" id="formFactor">
												<form:radiobutton path="prodAttributes['formFactor']"
													id="formFactorPhysical" name="formFactor" value="Physical"
													checked="checked" />
												<form:errors path="prodAttributes['formFactor']"
													cssClass="error" cssStyle="color:red"></form:errors>
												<label class='radiobox-line'>Physical</label>
												<form:radiobutton path="prodAttributes['formFactor']"
													value="Virtual" id="formFactorVirtual" name="formFactor"
													data-skin="square" data-color="blue" />
												<label class='radiobox-line'>Virtual</label>


												<form:radiobutton path="prodAttributes['formFactor']"
													value="Digital" id="formFactorDigital" name="formFactor"
													data-skin="square" data-color="blue" />
												<label class='radiobox-line'>Digital</label>
												<form:radiobutton path="prodAttributes['formFactor']"
													value="IH" id="formFactorIH" name="formFactor"
													data-skin="square" data-color="blue" />
												<label class='radiobox-line'>IH</label>
											</div>


										</div>




										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Product Type"><spring:message
														code="product.productType" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-8" id="productType">
												<form:radiobutton path="prodAttributes['productType']"
													id="productRetailType" name="productType" value="Retail"
													checked="checked" />
												<label class='radiobox-line'>Retail</label>
												<form:radiobutton path="prodAttributes['productType']"
													value="B2B" id="productB2BType" name="productType"
													data-skin="square" data-color="blue" />
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
													type="textarea" minlength="1" maxlength="50"
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
													type="textarea" minlength="1" maxlength="50"
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
													type="textarea" minlength="1" maxlength="50"
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
														code="product.saleActiveCodeResponseType" /> <font
													color='red'></font></label>
											</div>
											<div class="col-lg-4" id="saleActiveCodeResType">
												<form:radiobutton
													path="prodAttributes['saleActiveCodeResponseType']"
													id="saleActiveCodeResponseTypePin"
													name="saleActiveCodeResponseTypePin" value="PIN"
													checked="checked" />
												<label class='radiobox-line'>PIN</label>
												<form:radiobutton
													path="prodAttributes['saleActiveCodeResponseType']"
													value="CVV" id="saleActiveCodeResponseTypeCVV"
													name="saleActiveCodeResponseTypeCVV" data-skin="square"
													data-color="blue" />
												<label class='radiobox-line'>CVV</label>
											</div>
											<form:errors
												path="prodAttributes['saleActiveCodeResponseType']"
												cssClass="error" cssStyle="color:red"></form:errors>


										</div>

										<div class="col-lg-12" id="cardEncodingDiv">
											<div class="col-lg-4">
												<label for="Card Encoding"><spring:message
														code="product.cardEncoding" /><font color='red'> *</font></label>
											</div>

											<div class="col-lg-4">
												<form:select path="prodAttributes['cardEncoding']"
													id="cardEncoding" name="cardEncoding">
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
														onblur="validateFields(this.form.id,this.id)" />
												<div>
													<form:errors path="prodAttributes['retailUPC']"
														cssClass="error" cssStyle="color:red"></form:errors>
												</div>
												</p>
												<p class="space" id="b2bUPC" style="display: none;">
													<form:input path="prodAttributes['b2bUpc']"
														class="textbox-large" id="productB2BUPC" type="text"
														name="b2bUPC" onkeyup="return isAlphaNumeric(this);"
														maxlength="50"
														onblur="validateFields(this.form.id,this.id)" />
												<div>
													<form:errors path="prodAttributes['b2bUpc']"
														cssClass="error" cssStyle="color:red"></form:errors>
												</div>

												</p>


											</div>
											<%-- <div class="col-lg-4">
																
																
							<button type="button" class="btn btn-primary" id="serialRequest" data-toggle="modal" 
							data-target="#define-constant-serial">
												<!-- <i class='glyphicon glyphicon-backward'></i> -->
												<spring:message code="product.serialNumberRequest" />
											</button>	
							
							
							</div> --%>

										</div>
									</div>

									<div class="col-lg-12" id="intialB2BSerialNumQty"
										style="display: none;">

										<div class="col-lg-4">
											<label for="Initial B2B Serial Number Quantity"><spring:message
													code="product.b2bInitSerialNumQty" /> <font color='red'></font></label>
										</div>
										<div class="col-lg-8">

											<form:input path="prodAttributes['b2bInitSerialNumQty']"
												id="b2bInitSerialNumQty" class="textbox-large"
												type="textarea" minlength="2" maxlength="50"
												onkeyup="return isNumeric(this)"
												onblur="validateFields(this.form.id,this.id)" />

										</div>

										<form:errors path="prodAttributes['b2bInitSerialNumQty']"
											id="b2bInitSerialNumQty" cssStyle="color:red" />

									</div>

									<div class="col-lg-12" id="autoRepLevel" style="display: none;">

										<div class="col-lg-4">
											<label for="Serial Number Auto Replenishment Level"><spring:message
													code="product.b2bSerialNumAutoReplenishLevel" /> <font
												color='red'></font></label>
										</div>
										<div class="col-lg-8">

											<form:input
												path="prodAttributes['b2bSerialNumAutoReplenishLevel']"
												id="b2bSerialNumAutoReplenishLevel" class="textbox-large"
												type="textarea" minlength="2" maxlength="50"
												onkeyup="return isNumeric(this)"
												onblur="validateFields(this.form.id,this.id)" />
											<div>
												<form:errors
													path="prodAttributes['b2bSerialNumAutoReplenishLevel']"
													id="b2bSerialNumAutoReplenishLevel" cssClass="error"
													cssStyle="color:red" />
											</div>

										</div>
									</div>


									<div class="col-lg-12" id="autoRepValue" style="display: none;">

										<div class="col-lg-4">
											<label for="Serial Number Auto Replenishment Value"><spring:message
													code="product.b2bSerialNumAutoReplenishVal" /> <font
												color='red'></font></label>
										</div>
										<div class="col-lg-8">

											<form:input
												path="prodAttributes['b2bSerialNumAutoReplenishVal']"
												id="b2bSerialNumAutoReplenishVal" class="textbox-large"
												type="textarea" minlength="2" maxlength="50"
												onkeyup="return isNumeric(this)"
												onblur="validateFields(this.form.id,this.id)" />
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
												id="b2bProductFunding" name="b2bProductFunding">
												<form:option value="NONE" label="- - - Select - - -" />
												<form:option value="ORDER_FULFILLMENT">Order Fulfillment</form:option>
												<form:option value="CARD_ACTIVATION">Successful Activation</form:option>
											</form:select>



											<div>
												<form:errors path="prodAttributes['b2bProductFunding']"
													id="b2bProductFunding" cssStyle="color:red" />

											</div>
										</div>
									</div>



									<div class="col-lg-12" id="srcOfFunding" style="display: none;">
										<div class="col-lg-4">
											<label for="Source of Funding"><spring:message
													code="product.sourceOfFunding" /> <font color='red'></font></label>
										</div>

										<div class="col-lg-4">
											<form:select path="prodAttributes['b2bSourceOfFunding']"
												id="sourceOfFunding" name="sourceOfFunding">
												<form:option value="NONE" label="- - - Select - - -" />
												<form:option value="ORDER_AMOUNT">Order Amount</form:option>
												<form:option value="CARD_ACTIVATION_AMOUNT">Activation Amount</form:option>
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
												onblur="validateFields(this.form.id,this.id)" />
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
												name="validityPeriod" id="validityPeriod"
												class="textbox-large" type="textarea" maxlength="4"
												onkeyup="return isNumeric(this);" />
										</div>
										<div class="col-lg-4">
											<form:select path="prodAttributes['validityPeriodFormat']"
												id="amntOfTimeType" name="amntOfTimeType">
												<form:option value="-1" label="- - - Select - - -" />
												<c:forEach items="${validityList}" var="validity">

													<form:option value="${validity}" />${validity}
        <c:if
														test="${validity eq productForm.prodAttributes['validityPeriodFormat']}">selected="true"</c:if>
													>

													</c:forEach>
											</form:select>




											<div>
												<form:errors path="prodAttributes['validityPeriod']"
													id="validityPeriod" cssStyle="color:red" />

											</div>

										</div>
									</div>


									<br>&nbsp;


									<%-- <div class="col-lg-12">

							<div class="col-lg-4">
								<label for="Product Validity Date"><spring:message
										code="product.productValidityDate" /> <font color='red'>*</font></label>
							</div>
							
            	<div class="col-lg-3">
             <form:select path="prodAttributes['productValidityMonth']" 
          id="productValidityMonth" name="productValidityMonth" >
             <form:option value="-1" label="- - - Select - - -"/> 
             <c:forEach items="${productValidityMonthList}" var="validityMonth">
        
        <form:option value="${validityMonth}"/>${validityMonth}
        <c:if test="${validityMonth eq productForm.prodAttributes['productValidityMonth']}">selected="true"</c:if>>
        
        </c:forEach>
        </form:select>   
		</div>
		
		<div class="col-lg-3">
		<form:select path="prodAttributes['productValidityDate']" 
          id="productValidityDate" name="productValidityDate" >
             <form:option value="-1" label="- - - Select - - -"/> 
             <c:forEach items="${productValidityDateList}" var="validityDate">
        
        <form:option value="${validityDate}"/>${validityDate}
        <c:if test="${validityDate eq productForm.prodAttributes['productValidityDate']}">selected="true"</c:if>>
        
        </c:forEach>
        </form:select>
		</div>
		
		<div class="col-lg-3">
		<form:select path="prodAttributes['productValidityYear']" 
          id="productValidityYear" name="productValidityYear" >
             <form:option value="-1" label="- - - Select - - -"/> 
             <c:forEach items="${productValidityYearList}" var="validityYear">
        
        <form:option value="${validityYear}"/>${validityYear}
        <c:if test="${validityYear eq productForm.prodAttributes['productValidityYear']}">selected="true"</c:if>>
        
        </c:forEach>
        </form:select>
									
								
									
				<div>
					<form:errors path="prodAttributes['productValidityMonth']"
						id="validityPeriod" cssStyle="color:red" />

				</div>
							</div>
		</div> --%>


									<div class="col-lg-12">

										<div class="col-lg-4">
											<label for="Product Validity Date"><spring:message
													code="product.productValidityDate" /> </label>
										</div>
										<div class="col-lg-8">

											<form:input type="textarea"
												path="prodAttributes['productValidityDate']"
												placeholder="MM/DD/YYYY" id="productValidityDate"
												class="date-picker"
												onChange="return validateDate(this.form.id,this.id)"
												onkeyup="return allowNumbersWithSlash(this);" />
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
													code="product.monthEndCardExpiryDate" /> <font color='red'></font></label>
										</div>
										<div class="col-lg-8">
											<form:radiobutton path="prodAttributes['monthEndCardExpiry']"
												id="monthEndCardExpiryDate" name="monthEndCardExpiryDate"
												checked="checked" value="Disable" />
											<label class='radiobox-line'>Disable</label>
											<form:radiobutton path="prodAttributes['monthEndCardExpiry']"
												value="Enable" id="monthEndCardExpiryDate"
												name="monthEndCardExpiryDate" data-skin="square"
												data-color="blue" />
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
												checked="checked" value="Disable" />
											<label class='radiobox-line'>Disable</label>
											<form:radiobutton path="prodAttributes['reloadableFlag']"
												value="Enable" id="reloadableFlagId"
												name="reloadableFlagName" data-skin="square"
												data-color="blue" />
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
												path="prodAttributes['activeFrom']" placeholder="MM/DD/YYYY"
												id="activeFrom" class="date-picker"
												onchange="validateDate(this.form.id,this.id)"
												onkeyup="return allowNumbersWithSlash(this);" />
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
												name="cutoverTime" id="cutoverTime"
												onblur="validateCutoverTime(this.form.id,this.id)"
												maxlength="8" />
											<!-- onclick="timepick();" /> -->
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
										<div class="col-lg-8">

											<c:set var="denomTyp"
												value="${productForm.prodAttributes['denominationType']}" />
											<input type="hidden" name="denomtyp" id="denomtyp"
												value="${denomTyp}" />

											<form:select path="prodAttributes['denominationType']"
												class="space" id="denominationId"
												onchange="includeRespDiv();"
												onblur="validateDropDown(this.form.id,this.id)">
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
													onblur="validateDecimalFormat(this.form.id,this.id);DecimalValueFormat(this);"
													id="denomFixedField" name="denomFixed" maxlength="10" />
											<div>
												<form:errors path="prodAttributes['denomFixed']"
													cssStyle="color:red" />
											</div>
											</p>


											<div class="" id="denomSelect" style="display: none;">
												<div class="col-sm-4 space">
													<div>
														Available<font color='red'> *</font>
													</div>

													<form:input path="" id="denom" size="8" multiple="multiple"
														onkeyup="return allowNumbersWithDot(this);"
														onblur="validateDecimalFormat(this.form.id,this.id);DecimalValueFormat(this);"
														class="textbox" name="denom" type="textarea"
														maxlength="10" />
												</div>

												<div class="col-sm-1 col-sm-offset-1">
													<br />

													<button type="button" id="denom_rightSelected"
														class="btn btn-block" onClick="AddDenomSelectFields();">
														<i class="glyphicon glyphicon-chevron-right"></i>
													</button>
													<button type="button" id="denom_leftSelected"
														class="btn btn-block" onClick="RemoveSelFields();">
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


												</div>
											</div>





											<div>
												<form:errors path="prodAttributes['denomSelect']"
													id="denomSelect" cssStyle="color:red" />
											</div>

											</p>
											<div class="space" id="denomVar" style="display: none;">
												<div>
													Min<font color='red'> *</font>:
													<form:input path="prodAttributes['denomVarMin']"
														type="text" id="denomMinField"
														onkeyup="return allowNumbersWithDot(this);"
														onblur="validateDecimalFormat(this.form.id,this.id);DecimalValueFormat(this);"
														name="minDenom" maxlength="10" />
													<div>
														<form:errors path="prodAttributes['denomVarMin']"
															cssStyle="color:red" />
													</div>
												</div>
												<br>&nbsp;
												<div>
													Max<font color='red'> *</font>:
													<form:input path="prodAttributes['denomVarMax']"
														type="text" id="denomMaxField"
														onkeyup="return allowNumbersWithDot(this);"
														onblur="validateDecimalFormat(this.form.id,this.id);DecimalValueFormat(this);"
														name="maxDenom" maxlength="10" />
													<div>
														<form:errors path="prodAttributes['denomVarMax']"
															cssStyle="color:red" />
													</div>
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

											<div class="col-lg-12">
												<div class="col-lg-4 ">
													<div>Available</div>
													<form:select path="" name="search_CardRange" id="purse"
														class="dropdown-medium" size="8" style="width: 322px;" multiple="multiple">
																	<c:forEach  items="${purseData}" var="purse">

																<form:option value="${purse.purseTypeId}~${purse.purseId}">

																	<%-- <c:if
																		test="${purse.purseTypeName !=null && purse.purseTypeName == 'CURRENCY' }">
					 ${purse.purseTypeName}-${purse.currCodeAlpha}
					</c:if>
																	<c:if
																		test="${purse.purseTypeName !=null && purse.purseTypeName == 'UPC' }">
					 ${purse.purseTypeName}-${purse.upc}
					</c:if> --%>
											${purse.extPurseId}
																</form:option>

															</c:forEach>
													</form:select>
												</div>

												<div class="col-lg-1 col-lg-offset-1 padding">
													<br />
													<button type="button" id="purse_rightAll"
														onblur="showDefaultpurse();" class="btn btn-block">
														<i class="glyphicon glyphicon-forward"></i>
													</button>
													<button type="button" id="purse_rightSelected"
														onblur="showDefaultpurse();" class="btn btn-block">
														<i class="glyphicon glyphicon-chevron-right"></i>
													</button>
													<button type="button" id="purse_leftSelected"
														onblur="showDefaultpurse();" class="btn btn-block">
														<i class="glyphicon glyphicon-chevron-left"></i>
													</button>
													<button type="button" id="purse_leftAll"
														onblur="showDefaultpurse();" class="btn btn-block">
														<i class="glyphicon glyphicon-backward"></i>
													</button>
												</div>

												<div class="col-lg-5 padding">
													<c:set var="purseList"
														value="${productForm.supportedPurse}" />
													<div>Selected</div>
													<form:select name="to[]" path="supportedPurseUpdate"
														id="purse_to" class="dropdown-medium" size="8"
														style="width: 322px;" multiple="multiple">
														<c:forEach items="${purseList}" var="purse">

															<form:option value="${purse.purseType.purseTypeId}~${purse.purseId}">
																<%--  ${purse.purseType['purseTypeName']}	 --%>
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
											<label for="Package ID">Package ID <font color='red'>*</font></label>
										</div>

										<div class="col-lg-8">


											<!-- Multi select for Package ID starts -->


											<div class="col-lg-12">
												<div class="col-lg-4 ">
													<div>Available</div>
													<form:select path="" name="search_CardRange" id="package"
														class="dropdown-medium" size="8" multiple="multiple">

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
														onblur="showDefaultpkg();" class="btn btn-block">
														<i class="glyphicon glyphicon-forward"></i>
													</button>
													<button type="button" id="package_rightSelected"
														onblur="showDefaultpkg();" class="btn btn-block">
														<i class="glyphicon glyphicon-chevron-right"></i>
													</button>
													<button type="button" id="package_leftSelected"
														onblur="showDefaultpkg();" class="btn btn-block">
														<i class="glyphicon glyphicon-chevron-left"></i>
													</button>
													<button type="button" id="package_leftAll"
														onblur="showDefaultpkg();" class="btn btn-block">
														<i class="glyphicon glyphicon-backward"></i>
													</button>
												</div>

												<div class="col-lg-4 padding">
													<c:set var="packageList" value="${productForm.packageId}" />
													Selected
													<form:select path="packageUpdate" name="to[]"
														id="package_to" class="dropdown-medium" size="8"
														multiple="multiple">


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
												<div class="col-sm-2 up padding">
													<button type="button" id="package_move_up"
														class="btn btn-block">
														<i class="glyphicon glyphicon-arrow-up"></i>
													</button>
												</div>
												<div class="col-sm-2 down padding">
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
													onblur="return validateDropDown(this.form.id,this.id);">
													<option value="NONE">--- Select ---</option>
													<c:forEach items="${purseList}" var="purse">

												<c:if test="${purse.purseType.purseTypeId !=null && purse.purseType.purseTypeId == '1' }">
															<form:option value="${purse.purseId}">
																<%--  ${purse.purseType['purseTypeName']}	 --%>
					 														
															${purse.extPurseId}
					
															</form:option>
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
													onblur="return validateDropDown(this.form.id,this.id);">
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
										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="CCF Fromat Version"><spring:message
														code="product.ccfFormatVersion" /> <font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">

												<form:select path="prodAttributes['ccfFormatVersion']"
													class="dropdown-medium" id="ccfFormatVersion"
													name="ccfFormatVersion"
													onblur="return validateDropDown(this.form.id,this.id);">
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
												<form:radiobutton
													path="prodAttributes['internationalSupported']"
													id="internationalSupport" name="internationalSupport"
													value="Disable" checked="checked" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton
													path="prodAttributes['internationalSupported']"
													value="Enable" id="internationalSupport"
													name="internationalSupport" data-skin="square"
													data-color="blue" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>



										<div class="col-lg-12" id="currencySupport"
											style="display: none">

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
															multiple="multiple">
															<c:forEach items="${availablePartnerCurrencyList}"
																var="partnerCurrency">

																<form:option value="${partnerCurrency.currencyTypeID}">
				                                                 ${partnerCurrency.currencyTypeID}: ${partnerCurrency.currCodeAlpha} : ${partnerCurrency.currencyDesc}
				                                                 </form:option>

															</c:forEach>
														</form:select>
													</div>

													<div class="col-lg-1  padding">
														<br />
														<button type="button" id="partnerCurrency_rightAll"
															class="btn btn-block">
															<i class="glyphicon glyphicon-forward"></i>
														</button>
														<button type="button" id="partnerCurrency_rightSelected"
															class="btn btn-block">
															<i class="glyphicon glyphicon-chevron-right"></i>
														</button>
														<button type="button" id="partnerCurrency_leftSelected"
															class="btn btn-block">
															<i class="glyphicon glyphicon-chevron-left"></i>
														</button>
														<button type="button" id="partnerCurrency_leftAll"
															class="btn btn-block">
															<i class="glyphicon glyphicon-backward"></i>
														</button>
													</div>

													<div class="col-lg-4 ">
														<c:set var="currencyList"
															value="${productForm.partnerCurrency}" />
														<div>Selected</div>
														<form:select name="to[]" id="partnerCurrency_to"
															path="partnerCurrencyUpdate" class="dropdown-medium"
															size="8" multiple="multiple">

															<c:forEach items="${currencyList}" var="currency">


																<form:option value="${currency.currencyTypeID}">
																	 ${currency.currencyTypeID}: ${currency.currCodeAlpha} : ${currency.currencyDesc}
																	</form:option>

															</c:forEach>

														</form:select>
														<div>
															<form:errors path="partnerCurrencyUpdate"
																cssClass="error" cssStyle="color:red"></form:errors>
														</div>


													</div>


													<!-- <div class="col-lg-3 padding">
														<div class="col-sm-4 up padding">
															<button type="button" id="partnerCurrency_move_up"
																class="btn btn-block">
																<i class="glyphicon glyphicon-arrow-up"></i>
															</button>
														</div>
														<div class="col-sm-4 down padding">
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
											<div class="col-lg-8">
												<form:radiobutton path="prodAttributes['pinSupported']"
													id="pinSupported" name="pinSupported" value="Disable"
													checked="checked" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['pinSupported']"
													value="Enable" id="pinSupported" name="pinSupported"
													data-skin="square" data-color="blue" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>
										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="CVV Supported"><spring:message
														code="product.cvvSupported" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-8">
												<form:radiobutton path="prodAttributes['cvvSupported']"
													id="cvvSupported" name="cvvSupported" value="Disable"
													checked="checked" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['cvvSupported']"
													value="Enable" id="cvvSupported" name="cvvSupported"
													data-skin="square" data-color="blue" />
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
													checked="checked" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['multiPurseSupport']"
													value="Enable" id="multiPurseSupport" name="multiPurseSupport"
													data-skin="square" data-color="blue" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>
										
										
										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Limit Supported"><spring:message
														code="product.limitSupported" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-8">
												<form:radiobutton path="prodAttributes['limitSupported']"
													id="limitSupported" name="limitSupported" value="Disable" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['limitSupported']"
													value="Enable" checked="checked" id="limitSupported"
													name="limitSupported" data-skin="square" data-color="blue" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>

										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Transaction Fee Supported"><spring:message
														code="product.transactionFeeSupported" /> <font
													color='red'></font></label>
											</div>
											<div class="col-lg-8">
												<form:radiobutton path="prodAttributes['feesSupported']"
													id="feesSupported" name="feesSupported" value="Disable"
													checked="checked" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['feesSupported']"
													value="Enable" id="feesSupported" name="feesSupported"
													data-skin="square" data-color="blue" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>


										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Maintenance Fee Supported"><spring:message
														code="product.maintenanceFeeSupported" /> <font
													color='red'></font></label>
											</div>
											<div class="col-lg-8">
												<form:radiobutton
													path="prodAttributes['maintainanceFeeSupported']"
													checked="checked" id="maintainanceFeeSupported"
													name="maintainanceFeeSupported" value="Disable" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton
													path="prodAttributes['maintainanceFeeSupported']"
													value="Enable" id="maintainanceFeeSupported"
													name="maintainanceFeeSupported" data-skin="square"
													data-color="blue" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>


										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="SMS and Email Alerts Supported"><spring:message
														code="product.smsandemail" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-8">
												<form:radiobutton path="prodAttributes['alertSupported']"
													id="smsandemail" name="smsandemail" value="Disable"
													checked="checked" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton path="prodAttributes['alertSupported']"
													value="Enable" id="smsandemail" name="smsandemail"
													data-skin="square" data-color="blue" />
												<label class='radiobox-line'>Enable</label>
											</div>

										</div>
										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Txn based on Card Status Supported"><spring:message
														code="product.txnBasedonCardStatus" /> <font color='red'></font></label>
											</div>
											<div class="col-lg-8">
												<form:radiobutton
													path="prodAttributes['cardStatusSupported']"
													id="txnBasedonCardStatus" name="txnBasedonCardStatus"
													value="Disable" />
												<label class='radiobox-line'>Disable</label>
												<form:radiobutton
													path="prodAttributes['cardStatusSupported']" value="Enable"
													id="txnBasedonCardStatus" name="txnBasedonCardStatus"
													checked="checked" data-skin="square" data-color="blue" />
												<label class='radiobox-line'>Enable</label>
											</div>
										</div>


										<div class="col-lg-12">

											<div class="col-lg-4">
												<label for="Activation"><spring:message
														code="product.activation" /> <font color='red'> *</font></label>
											</div>
											<div class="col-lg-8" id="activationvalid">

												<form:checkbox path="activationId" id="activationId"
													name="activationId" value="PAN" />
												<spring:message code="product.pan" />
												<form:checkbox path="activationId" id="activationId"
													name="activationId" value="Serial Number" />
												<spring:message code="product.serialNumber" />
												<form:checkbox path="activationId" id="activationId"
													name="activationId" value="Proxy Number" />
												<spring:message code="product.proxyNumber" />
												<form:checkbox path="activationId" id="activationId"
													name="activationId" value="Account Number" />
												<spring:message code="product.accountNumber" />
												<form:checkbox path="activationId" id="activationId"
													name="activationId" value="Customer Id" />
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

												<form:checkbox path="txnTypeId" id="otherTxnId"
													name="txnTypeId" value="PAN" />
												<spring:message code="product.pan" />
												<form:checkbox path="txnTypeId" id="otherTxnId"
													name="txnTypeId" value="Serial Number" />
												<spring:message code="product.serialNumber" />
												<form:checkbox path="txnTypeId" id="otherTxnId"
													name="txnTypeId" value="Proxy Number" />
												<spring:message code="product.proxyNumber" />
												<form:checkbox path="txnTypeId" id="otherTxnId"
													name="txnTypeId" value="Account Number" />
												<spring:message code="product.accountNumber" />
												<form:checkbox path="txnTypeId" id="otherTxnId"
													name="txnTypeId" value="Customer Id" />
												<spring:message code="product.customerId" />
											</div>

										</div>




										<!-- Form controls end  -->

									</div>

									<div class="col-lg-12">

										<div class="col-lg-4"></div>
										<div class="col-lg-8">
											<button type="button" id="btnSubmit"
												onclick="saveProductData(this.form.id,event);"
												class="btn btn-primary">
												<i class='glyphicon glyphicon-plus'></i>
												<spring:message code="button.add" />
											</button>


											<button type="button" onclick="goResetProduct();"
												class="btn btn-primary gray-btn">
												<i class='glyphicon glyphicon-refresh'></i>
												<spring:message code="button.reset" />
											</button>
											<button type="button" class="btn btn-primary gray-btn"
												onclick="goBackToProduct();">
												<i class='glyphicon glyphicon-backward'></i>
												<spring:message code="button.back" />
											</button>


										</div>
									</div>


								</div>
							</div>
						</div>
					</article>
				</section>
			</div>
		</div>
	</form:form>
</body>

<script>
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
			$('#productRetailType')[0].checked = true;
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
			if ($(this).val() == 'Retail') {
				$("#productFunding").attr("style", "display:none");
			}
			if ($(this).val() == 'B2B') {
				$("#b2bUPC").attr("style", "display:block");
			}

		})
	})

	$(function() {
		$('#productType input[type=radio]').change(function() {

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

	/* function getCardRangeDetails()
	 {
	
	 var issuerName="";
	
	 datatosplit=$("#issuerName").val();
	
	 var splitted = datatosplit.split('~');
	 var id=splitted[0];
	 var getData;
	 alert("issuer name "+issuerName);
	
	 document.getElementById("multiselect").innerHTML ="hi";



	 var request = $.ajax({
	 url: "http://localhost:8181/ConfigService/cardRanges/getCardRangesByIssuerId/"+id,
	 async:false,
	 type: "GET",
	 });

	 request.done(function(msg) {
	 var check=msg;
	 console.log(msg);
	
	 $('#multiselect').html(msg.data);
	 console.log(msg.data);
	
	 alert(JSON.stringify(msg.data));
	
	 });

	 request.fail(function(jqXHR, textStatus) {

	 alert( "Request failed: " + textStatus );
	 console.log(msg.data);
	
	 alert(JSON.stringify(msg.data));
	

	 });
	
	
	
	
	
	
	 } */

	function myJsonMethod(response) {

		console.log(response);

		alert("response " + response);
	}

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

	/* 

	 var request = $.ajax({
	 url: "http://localhost:8181/ConfigService/cardRanges/getCardRangesByIssuerId/591",
	 type: "GET",
	 dataType: 'json',
	 contentType: 'application/json; charset=utf-8',
	 async: false,
	 });

	 request.done(function(msg) {
	 var check=msg;
	
	 console.log(msg);
	
	 $('#multiselect').html(console.log(msg));
	 });

	 request.fail(function(jqXHR, textStatus) {

	 alert( "Request failed: " + textStatus );

	 });
	 */

	/* 
	 $.ajax({
	 url: 'http://localhost:8080/cclp-vms-1/config/cardrange/getCardRangeDetails/591',
	 type: 'GET',
	 dataType: 'json',
	 contentType: 'application/json; charset=utf-8',
	 success: function (data) {


	 },

	 error: function (response) {

	 var r = jQuery.parseJSON(response.responseText);
	 alert("Message: " + r.Message);

	 alert("StackTrace: " + r.StackTrace);

	 alert("ExceptionType: " + r.ExceptionType);

	 }



	 }); */

	/*     $("#activationId").click(function(){
	 var favorite = [];
	 $.each($("#activationId:checked"), function(){            
	 favorite.push($(this).val());
	 });
	 alert("My favourite sports are: " + favorite.join(", "));
	 }); */

	function getProgramIds(formId, eleId) {

		var datatosplit = "";
		datatosplit = $("#" + eleId + " option:selected").val();
		var options = '<option value="-1"><strong>--- Select ---</strong></option>';
		//$("#programId").append('<option value="-1">- - - Select - - -</option>');
		var splitted = datatosplit.split('~');
		var id = splitted[0];
		var servUrl = $('#addurl').val();

		var str = servUrl + "/ajax/getProgramIdDetails/" + id;

		$.ajax({
			url : str,
			async : true,
			type : "GET",
			dataType : 'json',
			success : function(response, status, xhr) {

				$.each(response, function(index) {

					options += '<option value="'+response[index].programID+'">'
							+ response[index].programIDName + '</option>';

					$("#programId").html(options);

				});

			}

		});

	}

	 function getPartnerCurrency(formId, eleId) {

			$("#partnerCurrency").html("");

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
						
						$("#partnerCurrency").append(
								'<option value="'+response[index].currencyTypeID+'">'
										+ response[index].currencyTypeID +":" 
										+ response[index].currCodeAlpha +":"  
										+ response[index].currencyDesc 
										+ '</option>');

						console.log('index' + index);

					});

				}

			});
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

	function showDefaultpurse() {
		var opts = $('#purse_to')[0].options;

		//alert(opts);

		var array = $.map(opts, function(elem) {

			var data = elem.value + "~" + elem.text;

			return (data);

		});

		if (array.length > 0) {
			/* var options = '<option value=""><strong>--- NONE ---</strong></option>'; */
			var options = '';
			var options1 = '';

			$.each(array, function(val, text) {

				var splitted = text.split('~');

				var purseTypeId = splitted[0];
				var value = splitted[1];
				var name = splitted[2];
				if (purseTypeId == "1") {

					options += '<option  value="'+value+'">' + name
					+ '</option>';

				}else{
					var options1 = '<option value=""><strong>--- NONE ---</strong></option>'
				}
					if(options != ''){
						$('#defaultPurse').html(options);		
					}else{
						$('#defaultPurse').html(options1);
					}
				

			});

		}

		else {

			var options = '<option value=""><strong>--- NONE ---</strong></option>'
			$('#defaultPurse').html(options);

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

			for (i = 0; i < document.addProductForm.denom_to.options.length; i++) {

				var valOptions = document.addProductForm.denom_to.options[i].text;
				if (valOptions == val) {
					flag = true;
				}
			}
			if (flag == false) {
				document.addProductForm.denom_to.options[document.addProductForm.denom_to.options.length] = new Option(
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

		for (var i = 0; i < document.addProductForm.denom_to.options.length; i++) {
			if (document.addProductForm.denom_to.options[i].selected == true) {
				txt = document.addProductForm.denom_to.options[i].text;
				document.addProductForm.denom.value = txt;
			}
		}
		for (var i = document.addProductForm.denom_to.options.length - 1; i >= 0; i--) {
			if (document.addProductForm.denom_to.options[i].selected == true) {
				document.addProductForm.denom_to.options[i] = null
			}
		}
		if (document.addProductForm.denom_to.options.length > 0) {
			if (document.addProductForm.denom_to.options[0].selected == true) {
				document.addProductForm.denom_to.options[0] = null
			}
		}
	}

	function getCardRangeDetails() {

		$("#multiselect").html("");

		$("#multiselect_to").html("");

		var datatosplit = "";

		datatosplit = $('#productIssuerName').val();

		var splitted = datatosplit.split('~');
		var id = splitted[0];

		var servUrl = $('#addurl').val();
		var options = "";

		//alert(servUrl);

		//alert("totalurl "+servUrl+"/ajax/getApprovedCardRangeDetails/"+id);

		var str = servUrl + "/ajax/getApprovedCardRangeDetails/" + id;

		$.ajax({

			//url: "http://localhost:8080/cclp-vms-1/ajax/getApprovedCardRangeDetails/"+id,

			url : str,

			async : true,

			type : "GET",

			dataType : 'json',

			/* crossDomain: true,  */

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

		/* $('#multiselect option').prop('selected', true);
		var options = $('#multiselect > option:selected');*/
		/*  alert(options.length);
		 if(options.length == 0){
			 generateAlert("addProductForm", "multiselect","multiselect.noCardRange");
			clearError("multiselect_to");
			 return false;
		   
		}  */

	}

	

	function getSupportedPurses() {

		$("#purse").html("");

		$("#purse_to").html("");
		
		var datatosplit = "";

		datatosplit = $('#productPartnerName').val();

		var splitted = datatosplit.split('~');
		var id = splitted[0];

		var servUrl = $('#addurl').val();
		var options = "";

		//alert(servUrl);

		//alert("totalurl "+servUrl+"/ajax/getApprovedCardRangeDetails/"+id);

		var str = servUrl + "/ajax/getAllSupportedPurses/" + id;

		$.ajax({

			//url: "http://localhost:8080/cclp-vms-1/ajax/getApprovedCardRangeDetails/"+id,

			url : str,

			async : true,

			type : "GET",

			dataType : 'json',

			/* crossDomain: true,  */

			success : function(response, status, xhr) {

				$.each(response,
						function(index) {
					
							$("#purse").append(
									'<option value="'+response[index].purseTypeId+'~'+response[index].purseId+'">'
											+ response[index].extPurseId
											+ '</option>');

							//${cardRange.prefix}${cardRange.startCardNbr}-${cardRange.prefix}${cardRange.endCardNbr}

							//console.log(response.data);

						});

			}

		});

		/* $('#multiselect option').prop('selected', true);
		var options = $('#multiselect > option:selected');*/
		/*  alert(options.length);
		 if(options.length == 0){
			 generateAlert("addProductForm", "multiselect","multiselect.noCardRange");
			clearError("multiselect_to");
			 return false;
		   
		}  */
		showDefaultpurse();
	}


	
	function checkCardRangeExist() {
		$('#multiselect option').attr('selected', true);
		options = $('#multiselect > option:selected');

		if (options.length == 0) {
			generateAlert("addProductForm", "multiselect",
					"multiselect.noCardRange");
			clearError("multiselect_to");
			return false;

		} else {
			clearError("multiselect");
		}
	}
	
	

	function checkPartnerCurrencyExist() {
		if ($("input[id='internationalSupport']:checked").val() == 'Enable') {
		$('#partnerCurrency option').attr('selected', true);
		options = $('#partnerCurrency > option:selected');
console.log("options::::::::::::"+options);
		if (options.length == 0) {
			console.log("options::::::::::::"+options.length );
			generateAlert("addProductForm", "partnerCurrency",
					"partnerCurrency.noCurrencySupported");
			clearError("partnerCurrency_to");
			return false;

		} else {
			console.log("options:::else:::::::::" );
			clearError("partnerCurrency");
		}
		}
	}
	
	/* $("#multiselect").append('<option value="'+response[index].issuerName+'">'+response[index].issuerName+'</option>'); */
</script>

<!-- added by nawaz for smart search -->

<style>
.custom-combobox {
	position: relative;
	display: inline-block;
}

.custom-combobox-toggle {
	position: absolute;
	top: 0;
	bottom: 0;
	margin-left: -1px;
	padding: 0;
}

.custom-combobox-input {
	margin: 0;
	padding: 5px 10px;
}

.ui-autocomplete {
	max-height: 400px;
	width: 100px;
	overflow-y: auto; /* prevent horizontal scrollbar */
	overflow-x: hidden; /* add padding to account for vertical scrollbar */
	z-index: 1000 !important;
}
</style>
<!-- 
  <script>
  $( function() {
    $.widget( "custom.combobox", {
      _create: function() {
        this.wrapper = $( "<span>" )
          .addClass( "custom-combobox" )
          .insertAfter( this.element );
 
        this.element.hide();
        this._createAutocomplete();
        this._createShowAllButton();
      },
 
      _createAutocomplete: function() {
        var selected = this.element.children( ":selected" ),
          value = selected.val() ? selected.text() : "";
 
        this.input = $( "<input>" )
          .appendTo( this.wrapper )
          .val( value )
          .attr( "title", "" )
          .addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
          .autocomplete({
            delay: 0,
            minLength: 0,
            source: $.proxy( this, "_source" )
          })
          .tooltip({
            classes: {
              "ui-tooltip": "ui-state-highlight"
            }
          });
 
        this._on( this.input, {
          autocompleteselect: function( event, ui ) {
            ui.item.option.selected = true;
            this._trigger( "select", event, {
              item: ui.item.option
            });
          },
 
          autocompletechange: "_removeIfInvalid"
        });
      },
 
      _createShowAllButton: function() {
        var input = this.input,
          wasOpen = false;
 
        $( "<a>" )
          .attr( "tabIndex", -1 )
          .attr( "title", "Show All Items" )
          .tooltip()
          .appendTo( this.wrapper )
          .button({
            icons: {
              primary: "ui-icon-triangle-1-s"
            },
            text: false
          })
          .removeClass( "ui-corner-all" )
          .addClass( "custom-combobox-toggle ui-corner-right" )
          .on( "mousedown", function() {
            wasOpen = input.autocomplete( "widget" ).is( ":visible" );
          })
          .on( "click", function() {
            input.trigger( "focus" );
 
            // Close if already visible
            if ( wasOpen ) {
              return;
            }
 
            // Pass empty string as value to search for, displaying all results
            input.autocomplete( "search", "" );
          });
      },
 
      _source: function( request, response ) {
        var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
        response( this.element.children( "option" ).map(function() {
          var text = $( this ).text();
          if ( this.value && ( !request.term || matcher.test(text) ) )
            return {
              label: text,
              value: text,
              option: this
            };
        }) );
      },
 
      _removeIfInvalid: function( event, ui ) {
 
        // Selected an item, nothing to do
        if ( ui.item ) {
          return;
        }
 
        // Search for a match (case-insensitive)
        var value = this.input.val(),
          valueLowerCase = value.toLowerCase(),
          valid = false;
        this.element.children( "option" ).each(function() {
          if ( $( this ).text().toLowerCase() === valueLowerCase ) {
            this.selected = valid = true;
            return false;
          }
        });
 
        // Found a match, nothing to do
        if ( valid ) {
          return;
        }
 
        // Remove invalid value
        this.input
          .val( "" )
          .attr( "title", value + " didn't match any item" )
          .tooltip( "open" );
        this.element.val( "" );
        this._delay(function() {
          this.input.tooltip( "close" ).attr( "title", "" );
        }, 2500 );
        this.input.autocomplete( "instance" ).term = "";
      },
 
      _destroy: function() {
        this.wrapper.remove();
        this.element.show();
      }
    });
 
    $( "#combobox" ).combobox();
    $( "#toggle" ).on( "click", function() {
      $( "#combobox" ).toggle();
    });
  } );
  </script>  -->

<!-- <script src="https://code.jquery.com/jquery-1.12.4.js"></script> -->
<!--   <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">    -->
<!-- ends -->



<!-- <script type="text/javascript" src="http://code.jquery.com/jquery-1.10.2.js"> </script>
 <script type="text/javascript" src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"> </script>-->
<!-- <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" /> -->
<script>
	$(document).ready(function() {
		BindControls();
	});

	function callAjax() {

		var parentMsg = "";
		var servUrl = $('#addurl').val();

		$.ajax({

			url : servUrl + "/ajax/getParentProducts",
			//url: "http://localhost:8080/cclp-vms/ajax/getParentProducts",

			async : false,

			type : "GET",

			dataType : 'json',

			success : function(response, status, xhr) {

				$.each(response, function(index) {

					parentMsg = parentMsg + '"' + response[index];

				});

			},
			error : function(jqXHR, textStatus, errorThrown) {

				console
						.log("error:" + textStatus + " exception:"
								+ errorThrown);
			}

		});

		return parentMsg;

	}

	function BindControls() {

		var Output = callAjax();

		//alert("before split"+Output);

		var newData = Output.split('"');

		var ProdData = newData;

		//   alert("data: "+ProdData);

		var selectItem = function(event, ui) {
			$("#combobox").val(ui.item.value);
			return false;
		}

		$('#combobox').autocomplete({
			source : ProdData,
			minLength : 0,
			select : selectItem,
			change : function(event, ui) {
				if (!ui.item) {
					this.value = '';
				}
			},
			scroll : true
		}).focus(function() {
			$(this).autocomplete("search", "");
		});
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