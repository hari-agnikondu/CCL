<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<meta http-equiv="Pragma" content="no-cache">
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/partner.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/multiselect.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
	
	          
<div class="body-container" style="min-height: 131px;">  	

<div class="container">
	    
		<ul  class="nav nav-tabs col-lg-8 col-lg-offset-3">
			<li class="active"><a href="#partner" data-toggle="tab"
				class="hoverColor"><i class="glyphicon glyphicon-tags"></i>&nbsp;<spring:message
						code="header.partner.view" text="View Partner" /></a></li>

		</ul>
		<div class="tabresp tab-content">
		
			<div class="tab-pane fade in active graybox col-lg-8 col-lg-offset-3" id="product">
				<div class="form-group text-right Error-red">
					<spring:message code="label.mandatory" text="*Mandatory" />
				</div>
				<div class="form-inline">
					<form:form action="#" method="POST" modelAttribute="partnerForm"
						id="viewForm" name="viewForm">
						<!-- <div class="col-lg-6 form-col-2"> -->

							<div class="col-lg-12">
								<form:input path="partnerId" type="hidden" name="partnerId"
									id="partnerId" />
								<div class="col-lg-3">
									<label for="partnerName"><spring:message
											code="partner.name" text="Partner Name " /><font
										style="color: red">*</font></label>
								</div>
								<div class="col-lg-8">
									<form:input title="Allowed Special Characters are ,;'_- ." path="partnerName" class="form-group textbox-large trim"
										name="partnerName" id="partnerName" autocomplete="off"
										maxLength="100" onkeyup="return isAlphaNumericWithSpecialChars(this)"
										onblur="validateFields(this.form.id,this.id)" readonly="true" />
										<div>
									    <form:errors path="partnerName" cssClass="fieldError"></form:errors>
									    </div>
								</div>
								
							</div>
							<div class="col-lg-12" style="padding-bottom: 5px">
								<div class="col-lg-3">
									<label for="partnerDesc"><spring:message
											code="partner.description" text="Description" /></label>
								</div>

								<div class="col-lg-8">
									<form:textarea path="partnerDesc"
										class="form-group textbox-large trim" rows="5" cols="51"
										name="partnerDesc" id="partnerDesc" autocomplete="off"
										maxLength="255" 
										onblur="validateFields(this.form.id,this.id)" style="resize:none" readonly="true"/>
										<div>
									    <form:errors path="partnerDesc" cssClass="fieldError"></form:errors>
									    </div>
								</div>
								
							</div>
							<div class="col-lg-12">
								<div class="col-lg-3">
									<label for=mdmId><spring:message code="partner.mdmId"
											text="MDM ID" /><font style="color: red">*</font></label>
								</div>
								<div class="col-lg-8">
									<form:input path="mdmId" class="form-group textbox-large"
										type="text" name="mdmId" id="partnerMdmId" autocomplete="off"
										maxLength="20" onkeyup="return isNumeric(this)"
										onblur="validateFields(this.form.id,this.id)" readonly="true" />
										<div>
									    <form:errors path="mdmId" cssClass="fieldError"></form:errors>
									   </div>
								</div>
								
							</div>
							
							<div class="col-lg-12">

										<div class="col-lg-3">
											<label for="Supported Currency"><spring:message
													code="partner.supportedCurrency" /> </label>
										</div>
										<div class="col-lg-8">
											<div class="col-lg-12">
												<div class="col-lg-4 ">
													<div>Available</div>
													<form:select path="" name="search_currency" id="currency"
														class="dropdown-medium" size="8" multiple="multiple"
														disabled="true">
														<c:forEach items="${currencyData}" var="currency">
															<form:option value="${currency.currencyTypeID}">
															${currency.currencyTypeID}: ${currency.currCodeAlpha} : ${currency.currencyDesc}
															</form:option>
														</c:forEach>
													</form:select>
												</div>

												<div class="col-lg-1 col-lg-offset-1 padding">
													<br />
													<button type="button" id="currency_rightAll"
														 class="btn btn-block" disabled="disabled">
														<i class="glyphicon glyphicon-forward"></i>
													</button>
													<button type="button" id="currency_rightSelected"
														 class="btn btn-block" disabled="disabled">
														<i class="glyphicon glyphicon-chevron-right"></i>
													</button>
													<button type="button" id="currency_leftSelected"
														class="btn btn-block" disabled="disabled">
														<i class="glyphicon glyphicon-chevron-left"></i>
													</button>
													<button type="button" id="currency_leftAll"
														 class="btn btn-block" disabled="disabled">
														<i class="glyphicon glyphicon-backward"></i>
													</button>
												</div>

												<div class="col-lg-3 ">
													<c:set var="currencyList"
														value="${partnerForm.supportedCurrency}" />
													<div>Selected</div>
													<form:select name="to[]" path="supportedCurrencyUpdate"
														id="currency_to" class="dropdown-medium" size="8"
														multiple="multiple" disabled="true">
														<c:forEach items="${currencyList}" var="currency">

															<form:option value="${currency.currencyTypeID}">
															${currency.currencyTypeID}: ${currency.currCodeAlpha} : ${currency.currencyDesc}

															</form:option>

														</c:forEach>

													</form:select>
													<div>
														<form:errors path="supportedCurrencyUpdate" cssClass="error"
															cssStyle="color:red"></form:errors>
													</div>
												</div>
											</div>
											
								</div>
							</div>
							
							<div class="col-lg-12">

							<div class="col-lg-3">
								<label for="Supported Purses"><spring:message
										code="partner.supportedPurses" /> <font style="color: red">*</font></label>
							</div>
							<div class="col-lg-8">
								<div class="col-lg-12">
									<div class="col-lg-4 ">
										<div>Available</div>
										<form:select path="" name="search_purse" id="purse"
											class="dropdown-medium" size="8" multiple="multiple" disabled="true">
											<c:forEach items="${purseData}" var="purse">
												<form:option value="${purse.purseId}">
																		${purse.extPurseId}
															</form:option>
											</c:forEach>
										</form:select>
									</div>

									<div class="col-lg-1 col-lg-offset-1 padding">
										<br />
										<button type="button" id="purse_rightAll"
											class="btn btn-block">
											<i class="glyphicon glyphicon-forward"></i>
										</button>
										<button type="button" id="purse_rightSelected"
											class="btn btn-block">
											<i class="glyphicon glyphicon-chevron-right"></i>
										</button>
										<button type="button" id="purse_leftSelected"
											class="btn btn-block">
											<i class="glyphicon glyphicon-chevron-left"></i>
										</button>
										<button type="button" id="purse_leftAll" class="btn btn-block">
											<i class="glyphicon glyphicon-backward"></i>
										</button>
									</div>

									<div class="col-lg-3 ">
										<c:set var="purseList" value="${partnerForm.supportedPurse}" />
										<div>Selected</div>
										<form:select name="to[]" path="supportedPurseUpdate"
											id="purse_to" class="dropdown-medium" size="8"
											multiple="multiple" disabled="true">
											<c:forEach items="${purseList}" var="purse">

												<form:option value="${purse.purseId}">
																${purse.extPurseId}
															</form:option>

											</c:forEach>

										</form:select>
										<div>
											<form:errors path="supportedPurseUpdate" cssClass="error"
												cssStyle="color:red"></form:errors>
										</div>
									</div>
								</div>

							</div>
						</div>
							<div class="col-lg-12">
								<div class="col-lg-3 ">
									<label for=partnerStatus><spring:message
											code="partner.active" text="Active" /></label>
								</div>
								<div class="col-lg-8">
							
									<label for="IsActive"> <font color='BLACK'><strong>YES</strong></font></label>
								</div>
								<form:input path="isActive" name="isActive" type="hidden" />
								
							</div>
							<div class="col-lg-12 text-center">


									<a
										href="${pageContext.request.contextPath}/config/partner/partnerConfig"
										class="btn gray-btn btn-primary"><i
										class='glyphicon glyphicon-backward'></i>
									<spring:message code="button.back" text="Back" /> </a>

							</div>
					</form:form>
				</div>
			</div>
		</div>
</div>
</div>



<script>
	var statusFlag = "";
	var statusMsg = "";
	if ('<c:out value="${statusFlag}"/>' != null) {

		statusFlag = '<c:out value="${statusFlag}"/>';
		statusMsg = '<c:out value="${statusMessage}"/>';

	}
	var contextPath = "${pageContext.request.contextPath}";
	loadStatus(statusFlag, statusMsg);
</script>

<script>
$('#currency').multiselect();
$('#purse').multiselect();
</script>