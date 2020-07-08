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
		src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/css/jqueryui.css"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/jqueryui.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/css/blue-theme.css"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/bootstrap-datepicker.js"></script>
	<%-- <script
		src="${pageContext.request.contextPath}/resources/js/multiselect.min.js"></script> --%>

	<script
		src="${pageContext.request.contextPath}/resources/js/jquery.timepicker.js"></script>

	<script
		src="${pageContext.request.contextPath}/resources/js/clpvms/productPurse.js"></script>
	<link
		href="${pageContext.request.contextPath}/resources/css/jquery.timepicker.css"
		rel="stylesheet" />
	<link
		href="${pageContext.request.contextPath}/resources/css/autocomp/smooth/jquery-ui.css"
		rel="stylesheet" />
	<link
		href="${pageContext.request.contextPath}/resources/css/autocomp/jquery-ui.css"
		rel="stylesheet" />
		
			<script
		src="${pageContext.request.contextPath}/resources/js/bootstrap-multiselect.js"></script>
	<link
		href="${pageContext.request.contextPath}/resources/css/bootstrap-multiselect.css"
		rel="stylesheet" />

<style>
input, select, textarea {
	border: 1px solid #b1a091;
	padding: 5px;
	width:185px
}	
.multiselect-container{
  max-height:160px;
  overflow:auto;
}

.multiselect-container>li>a>label {
    margin: 0;
    height: 100%;
    cursor: pointer;
    font-weight: 400;
    padding: 5px 60px -2px 20px;
    }
    
    .dropdown-menu > .active > a, .dropdown-menu > .active > a:hover, .dropdown-menu > .active > a:focus {
    color: black;
    text-decoration: none;
   
    outline: 0;
    }
    
    .dropdown-menu > .active > a, .dropdown-menu > .active > a:hover, .dropdown-menu > .active > a:focus {
    color:black;
    text-decoration: none;
   
    outline: 0;
    }
    
    .dropdown-menu {
  
    min-width: 100px;
    
    font-size: 14px;
   
    
    }
    input[type="radio"], input[type="checkbox"] {
    margin: 4px 0 0;
    margin-top: 1px \9;
    line-height: normal;
    width: 15px;
}

</style>

<script>
$(function() {
		$('#purseActiveTime').timepicker({
			timeFormat: 'H:i:s',
			    interval: 30,
			    minTime: '00',
			    maxTime: '11:30pm',
			    defaultTime: '00',
			    startTime: '00:00:00',
			    dynamic: false,
			    dropdown: true,
			    scrollbar: true
		});
	});
	
$(function() {
	$('#purseValidityTime').timepicker({
		 timeFormat: 'H:i:s',
		    interval: 30,
		    minTime: '00',
		    maxTime: '11:30pm',
		    defaultTime: '00',
		    startTime: '00:00:00',
		    dynamic: false,
		    dropdown: true,
		    scrollbar: true
		
	});
});
</script>	
	
<body onload="onloadDiv();">
	<div class="body-container">
		<div class="container">

			<form:form name="productPurse" id="productPurse" action="#"
				method="POST" class='form-horizontal ' commandName="productPurse">

				<section class="content-container">
					<article class="col-lg-12 ">
						<ul class="nav nav-tabs col-lg-8 col-lg-offset-2">
							<li class="active SubMenu"><a href="#productPurse"
								data-toggle="tab"><spring:message
										code="Product.productPurseLable" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active graybox col-lg-8 col-lg-offset-2">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<article class="col-lg-12">
									<div id="messageResult">
										<c:if test="${statusMessage!='' && statusMessage!=null}">
											<p class="error-red text-center">
												<b>${statusMessage}</b>
											</p>

										</c:if>

										<c:if test="${status!='' && status!=null}">
											<p class="success-green text-center">
												<b>${status}</b>
											</p>
										</c:if>
									</div>
								</article>

								<div class="col-lg-12">
									<c:if test="${showCopyFrom=='true' }">
										<div class="col-lg-3">
											<label for="copyFrom"><spring:message
													code="Product.copyFrom" /></label>
										</div>
										<form:hidden path="productId" />
										<div class="col-lg-8">
											<form:select path="parentProductId" id="parentProductId"
												onchange="getProductPurses();" class="dropdown-medium">
												<form:option value="-1" label="- - - Select - - -" />
												<form:options items="${parentProductMap}" />

											</form:select>

											<button type="button" id="productPurse"
												class="btn btn-primary"
												onclick="getParentProductPurseDetails()">
												<spring:message code="button.copy" />
											</button>
											<div>
												<span id="parentProductIdError"></span>
											</div>
										</div>
									</c:if>
								</div>
								<input type="hidden" id="addurl" name="addurl" value="${addUrl}" />
								<br>
								<div class="col-lg-12">
									<div class="col-lg-3">
										<label for="purseTypeID"><spring:message
												code="Product.purseTypeID" /><font color='red'>*</font></label>
									</div>

									<div class="col-lg-8">
										<form:select path="purseId" id="purseId"
											class="dropdown-medium"
											onchange="getPurseAttributes(this.form.id,this.id);"
											onblur="return validateDropDown(this.form.id,this.id);">
											<form:option value="-1" label="--- Select ---" />
											<c:forEach items="${purseList}" var="pursetype">
												<option value="${pursetype.purseId}"
													<c:if test="${pursetype.purseId eq productPurse.purseId}">selected="true"</c:if>><c:out
														value="${pursetype.extPurseId}" /></option>
											</c:forEach>
										</form:select>
										<div>
											<form:errors path="purseId" id="purseId"
												cssClass="fieldError" />
										</div>
									</div>

								</div>
								<input type="hidden" id="minorUnits" name="minorUnits" value="${minorUnits}" />
								<input type="hidden" id="purseTypeName" name="purseTypeName" value="${purseTypeName}"/>
											
								<div class="col-lg-12">
									<div class="col-lg-3">
										<label for="Purse Active Date"><spring:message
												code="product.purseUIActiveDate" /> <font color='red'>*</font></label>
									</div>
									<div class="col-lg-3">
										<form:input type="textarea"
											path="productAttributes['purseUIActiveDate']"
											placeholder="MM/DD/YYYY" id="purseUIActiveDate"
											class="date-picker"
											onblur="return validateDate(this.form.id,this.id)"
											onkeyup="return allowNumbersWithSlash(this);" />
										<div>
											<form:errors path="productAttributes['purseUIActiveDate']"
												id="purseUIActiveDate" cssStyle="color:red" />
										</div>
									</div>
									<div class="col-lg-3 ">
											<form:input type="textarea"
												path="productAttributes['purseActiveTime']" placeholder="HH:MM:SS"
												name="purseActiveTime" id="purseActiveTime"
												onblur="validateCutoverTime(this.form.id,this.id)"
												maxlength="8" />
										
											<div>
												<form:errors path="productAttributes['purseActiveTime']"
													cssStyle="color:red" />
											</div>

									</div>
									<div class="col-lg-3">
										<form:select path="productAttributes['purseActiveTimeZone']" id="purseActiveTimeZone"
											class="dropdown-medium" >
											<form:option value="" label="--- Select ---" />
											<c:forEach items="${timeZone}" var="timeZone">
												<option value="${timeZone.key}"
												<c:if test="${timeZone.key eq productPurse.productAttributes['purseActiveTimeZone']}">selected="true"</c:if>
												>
													<c:out
														value="${timeZone.value}" /></option>
											</c:forEach>
										</form:select>
										<div>
											<form:errors path="productAttributes['purseActiveTimeZone']" id="purseActiveTimeZone"
												cssClass="fieldError" />
										</div>
									</div>
								</div>
								<div class="col-lg-12">

									<div class="col-lg-3">
										<label for="Purse Expiry Date"><spring:message
												code="product.purseUIValidityPeriod" /> </label>
									</div>
									<div class="col-lg-3">

										<form:input type="textarea"
											path="productAttributes['purseUIValidityPeriod']"
											placeholder="MM/DD/YYYY" id="purseUIValidityPeriod"
											class="date-picker"
											onblur="validateDate(this.form.id,this.id); return validatewithActiveDate(this.form.id,this.id);"
											onkeyup="return allowNumbersWithSlash(this);" />
										<div>
											<form:errors path="productAttributes['purseUIValidityPeriod']"
												id="purseUIValidityPeriod" cssStyle="color:red" />
										</div>
									</div>
									<div class="col-lg-3 ">

											<form:input type="textarea"
												path="productAttributes['purseValidityTime']" placeholder="HH:MM:SS"
												name="purseValidityTime" id="purseValidityTime"
												
												maxlength="8" />
											<div>
												<form:errors path="productAttributes['purseValidityTime']"
													cssStyle="color:red" />
											</div>

									</div>
									<div class="col-lg-3">
										<form:select path="productAttributes['purseValidityTimeZone']" id="purseValidityTimeZone"
											class="dropdown-medium"  name="purseValidityTimeZone">
											<form:option value="" label="--- Select ---" />
											<c:forEach items="${timeZone}" var="timeZone">
												<option value="${timeZone.key}"
												<c:if test="${timeZone.key eq productPurse.productAttributes['purseValidityTimeZone']}">selected="true"</c:if>
												>
													<c:out
														value="${timeZone.value}" /></option>
											</c:forEach>
										</form:select>
										<div>
											<form:errors path="productAttributes['purseValidityTimeZone']" id="purseValidityTimeZone"
												cssClass="fieldError" />
										</div>
									</div>
								</div>
								<div class="col-lg-12">
									<div class="col-lg-3">
										<label for="Maximum Purse Balance"><spring:message
												code="Product.maxPurseBalance" /> </label>
									</div>

									<div class="col-lg-8">
										<form:input path="productAttributes['maxPurseBalance']"
											id="maxPurseBalance" name="maxPurseBalance"
											onblur="validateDecimalFormatForMaxCardBalance(this.form.id,this.id)"
											autocomplete="off" maxlength="10" />
										<div>
											<form:errors path="productAttributes['maxPurseBalance']"
												id="maxPurseBalance" cssClass="fieldError" />
										</div>
									</div>
								</div>
								
								<div class="col-lg-12">
									<div class="col-lg-3">
										<label for="purseRedeemSplitTender"><spring:message
												code="Purse.purseRedeemSplitTender" /></label>
									</div>
									<div class="col-lg-8">
										<div class="col-lg-3">
											<form:radiobutton value="true"
												path="productAttributes['purseRedeemSplitTender']"
												id="purseRedeemSplitTender_Enable" name="purseRedeemSplitTender" />
											<label class='radiobox-line' for="purseRedeemSplitTender"><spring:message
													code="Purse.purseRedeemSplitTender_Enable" /></label>
										</div>

										<div class="col-lg-3">
											<form:radiobutton value="false"
												path="productAttributes['purseRedeemSplitTender']"
												id="purseRedeemSplitTender_Disable" name="purseRedeemSplitTender" />
											<label class='radiobox-line' for="purseRedeemSplitTender"><spring:message
													code="Purse.purseRedeemSplitTender_Disable" /></label>
										</div>
										<div>
											<form:errors path="productAttributes['purseRedeemSplitTender']"
												id="purseRedeemSplitTenderErr" cssStyle="color:red" />
										</div>
									</div>
								</div>
								
								<div class="col-lg-12">
									<div class="col-lg-3">
										<label for="purseRedeemLockSplitTender"><spring:message
												code="Purse.purseRedeemLockSplitTender" /></label>
									</div>
									<div class="col-lg-8">
										<div class="col-lg-3">
											<form:radiobutton value="true"
												path="productAttributes['purseRedeemLockSplitTender']"
												id="purseRedeemLockSplitTender_Enable" name="purseRedeemLockSplitTender" />
											<label class='radiobox-line' for="purseRedeemLockSplitTender"><spring:message
													code="Purse.purseRedeemLockSplitTender_Enable" /></label>
										</div>

										<div class="col-lg-3">
											<form:radiobutton value="false"
												path="productAttributes['purseRedeemLockSplitTender']"
												id="purseRedeemLockSplitTender_Disable" name="purseRedeemLockSplitTender" />
											<label class='radiobox-line' for="purseRedeemLockSplitTender"><spring:message
													code="Purse.purseRedeemLockSplitTender_Disable" /></label>
										</div>
										<div>
											<form:errors path="productAttributes['purseRedeemLockSplitTender']"
												id="purseRedeemLockSplitTenderErr" cssStyle="color:red" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-3">
										<label for="Auto Topup Enable"><spring:message
												code="Purse.autoTopupEnable" /></label>
									</div>
									<div class="col-lg-8" id="autoTopupEnable">
										<c:choose>
											<c:when
												test="${productPurse.productAttributes['autoTopupEnable']=='Enable'}">

												<div class="col-lg-3">
													<form:radiobutton value="Enable" title="System will create new purse for each Auto Top Up Transaction"
														path="productAttributes['autoTopupEnable']"
														id="autoTopup_Enable" name="autoTopupEnable"
														checked="checked" />
													<label class='radiobox-line' for="autoTopupEnable"><spring:message
															code="Purse.autoTopupEnable_Enable" /></label>

												</div>
												<div class="col-lg-3">
													<form:radiobutton value="Disable"
														path="productAttributes['autoTopupEnable']"
														id="autoTopup_Disable" name="autoTopupEnable" />
													<label class='radiobox-line' for="autoTopupEnable"><spring:message
															code="Purse.autoTopupEnable_Disable" /></label>
												</div>
											</c:when>
											<c:otherwise>
												<div class="col-lg-3">
													<form:radiobutton value="Enable" title="System will create new purse for each Auto Top Up Transaction"
														path="productAttributes['autoTopupEnable']"
														id="autoTopup_Enable" name="autoTopupEnable"
														 />
													<label class='radiobox-line' for="autoTopupEnable"><spring:message
															code="Purse.autoTopupEnable_Enable" /></label>

												</div>
												<div class="col-lg-3">
													<form:radiobutton value="Disable"
														path="productAttributes['autoTopupEnable']"
														id="autoTopup_Disable" name="autoTopupEnable"
														checked="checked" />
													<label class='radiobox-line' for="autoTopupEnable"><spring:message
															code="Purse.autoTopupEnable_Disable" /></label>
												</div>
											</c:otherwise>
										</c:choose>
									</div>
								</div>

								<div id="autoTopupEnableBlock" style="display: none">
									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="Auto Topup Frequency"><spring:message
													code="Purse.autoTopupFrequency" /> <font color='red'>*</font>
											</label>
										</div>

										<div class="col-lg-8">
											<form:select path="productAttributes['autoTopupFrequency']"
												id="autoTopupFrequency" class="dropdown-medium" onchange="topupFreqDiv();">
												<form:option value="-1" label="--- Select ---" selected="selected"/>
												<form:option value="day" label="Day" />
												<form:option value="dayOfMonth" label="Day of Month" />
												<form:option value="quarter" label="Quarter" />
												<form:option value="year" label="Year" />
											</form:select>
											<div>
												<form:errors path="productAttributes['autoTopupFrequency']"
													id="autoTopupFrequency" cssClass="fieldError" />
											</div>
										</div>
									</div>
									
									<div id="topupFreqDayBlock" style="display: none" >
									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="Topup Frequency Interval"><spring:message
													code="Purse.interval" /> <font color='red'>*</font>
											</label>
										</div>

										<div class="col-lg-8">
											<form:input path="productAttributes['topupFreqDay']"
												id="topupFreqDay" name="topupFreqDay" onkeyup="return isNumericValidity(this);"
												autocomplete="off" maxlength="2" />
											<div>
												<form:errors path="productAttributes['topupFreqDay']"
													id="topupFreqDay" cssClass="fieldError" />
											</div>
										</div>
									</div>
								</div>
								
								<div id="topupFreqDayOfMonthBlockMulti" style="display: none" >
									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="Topup Frequency Day Of Month"><spring:message
													code="Purse.dayOfMonth" /> <font color='red'>*</font>
											</label>
										</div>

										<div class="col-lg-8">
											<form:select path="productAttributes['topupFreqDayOfMonthMulti']"
												id="topupFreqDayOfMonthMulti" class="dropdown-medium" multiple="multiple">
												<%-- <form:option value="-1" label="--- Select ---" /> --%>
												<form:option value="1" >1</form:option>
												<form:option value="2" label="2" />
												<form:option value="3" label="3" />
												<form:option value="4" label="4" />
												<form:option value="5" label="5" />
												<form:option value="6" label="6" />
												<form:option value="7" label="7" />
												<form:option value="8" label="8" />
												<form:option value="9" label="9" />
												<form:option value="10" label="10" />
												<form:option value="11" label="11" />
												<form:option value="12" label="12" />
												<form:option value="13" label="13" />
												<form:option value="14" label="14" />
												<form:option value="15" label="15" />
												<form:option value="16" label="16" />
												<form:option value="17" label="17" />
												<form:option value="18" label="18" />
												<form:option value="19" label="19" />
												<form:option value="20" label="20" />
												<form:option value="21" label="21" />
												<form:option value="22" label="22" />
												<form:option value="23" label="23" />
												<form:option value="24" label="24" />
												<form:option value="25" label="25" />
												<form:option value="26" label="26" />
												<form:option value="27" label="27" />
												<form:option value="28" label="28" />
												<form:option value="29" label="29" />
												<form:option value="30" label="30" />
												<form:option value="31" label="31" />
											</form:select>
											 <div id="multierror" ></div> 
											<div>
												<form:errors path="productAttributes['topupFreqDayOfMonthMulti']"
													id="topupFreqDayOfMonthMulti" cssClass="fieldError" />
											</div>
										</div>
									</div>
								</div>
								
								<div id="topupFreqDayOfMonthBlock" style="display: none" >
									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="Topup Frequency Day Of Month"><spring:message
													code="Purse.dayOfMonth" /> <font color='red'>*</font>
											</label>
										</div>

										<div class="col-lg-8">
											<form:select path="productAttributes['topupFreqDOM']"
												id="topupFreqDayOfMonth" class="dropdown-medium">
												<form:option value="-1" label="--- Select ---" />
												<form:option value="1" label="1" />
												<form:option value="2" label="2" />
												<form:option value="3" label="3" />
												<form:option value="4" label="4" />
												<form:option value="5" label="5" />
												<form:option value="6" label="6" />
												<form:option value="7" label="7" />
												<form:option value="8" label="8" />
												<form:option value="9" label="9" />
												<form:option value="10" label="10" />
												<form:option value="11" label="11" />
												<form:option value="12" label="12" />
												<form:option value="13" label="13" />
												<form:option value="14" label="14" />
												<form:option value="15" label="15" />
												<form:option value="16" label="16" />
												<form:option value="17" label="17" />
												<form:option value="18" label="18" />
												<form:option value="19" label="19" />
												<form:option value="20" label="20" />
												<form:option value="21" label="21" />
												<form:option value="22" label="22" />
												<form:option value="23" label="23" />
												<form:option value="24" label="24" />
												<form:option value="25" label="25" />
												<form:option value="26" label="26" />
												<form:option value="27" label="27" />
												<form:option value="28" label="28" />
												<form:option value="29" label="29" />
												<form:option value="30" label="30" />
												<form:option value="31" label="31" />
											</form:select>
											<div>
												<form:errors path="productAttributes['topupFreqDOM']"
													id="topupFreqDayOfMonth" cssClass="fieldError" />
											</div>
										</div>
									</div>
								</div>
								
								<div id="topupFreqMonthBlock" style="display: none">
									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="Topup Frequency Month"><spring:message
													code="Purse.month" /> <font color='red'>*</font>
											</label>
										</div>

										<div class="col-lg-8">
											<form:select path="productAttributes['topupFreqMonth']"
												id="topupFreqMonth" class="dropdown-medium">
												<form:option value="-1" label="--- Select ---" />
												<form:option value="1" label="January" />
												<form:option value="2" label="February" />
												<form:option value="3" label="March" />
												<form:option value="4" label="April" />
												<form:option value="5" label="May" />
												<form:option value="6" label="June" />
												<form:option value="7" label="July" />
												<form:option value="8" label="August" />
												<form:option value="9" label="September" />
												<form:option value="10" label="October" />
												<form:option value="11" label="November" />
												<form:option value="12" label="December" />
											</form:select>
											<div>
												<form:errors path="productAttributes['topupFreqMonth']"
													id="topupFreqMonth" cssClass="fieldError" />
											</div>
											
										</div>
									</div>
								</div>
									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="Auto Topup Amount"><spring:message
													code="Purse.autoTopupAmount" /> <font color='red'>*</font>
											</label>
										</div>

										<div class="col-lg-8">
											<form:input path="productAttributes['autoTopupAmount']"
												id="autoTopupAmount" name="autoTopupAmount"
												autocomplete="off" maxlength="10"
												onkeyup="return allowNumbersWithAndWithoutDot(this);"
												onblur="DecimalValueFormatPurse(this);" />
											<div>
												<form:errors path="productAttributes['autoTopupAmount']"
													id="autoTopupAmount" cssClass="fieldError" />
											</div>
										</div>
									</div>



									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="Auto Rollover Enable"><spring:message
													code="Purse.rolloverEnable" /></label>
										</div>
										<div class="col-lg-8" id="rolloverEnable">
											<c:choose>
												<c:when
													test="${productPurse.productAttributes['rolloverEnable']=='Enable'}">

													<div class="col-lg-3">
														<form:radiobutton value="Enable"
															path="productAttributes['rolloverEnable']"
															id="rollover_Enable" name="rolloverEnable"
															checked="checked" />
														<label class='radiobox-line' for="rolloverEnable">
															<spring:message code="Purse.rolloverEnable_Enable" />
														</label>
													</div>

													<div class="col-lg-3">
														<form:radiobutton value="Disable"
															path="productAttributes['rolloverEnable']"
															id="rollover_Disable" name="rolloverEnable" />
														<label class='radiobox-line' for="rolloverEnable">
															<spring:message code="Purse.rolloverEnable_Disable" />
														</label>
													</div>
												</c:when>
												<c:otherwise>
													<div class="col-lg-3">
														<form:radiobutton value="Enable"
															path="productAttributes['rolloverEnable']"
															id="rollover_Enable" name="rolloverEnable" />
														<label class='radiobox-line' for="rolloverEnable">
															<spring:message code="Purse.rolloverEnable_Enable" />
														</label>
													</div>

													<div class="col-lg-3">
														<form:radiobutton value="Disable"
															path="productAttributes['rolloverEnable']"
															id="rollover_Disable" name="rolloverEnable"
															checked="checked" />
														<label class='radiobox-line' for="rolloverEnable">
															<spring:message code="Purse.rolloverEnable_Disable" />
														</label>
													</div>
												</c:otherwise>
											</c:choose>

										</div>
									</div>
								</div>

								

								<div  id="rolloverEnableBlock" style="display: none" >
									
									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="Rollover Max Amount"><spring:message
													code="Purse.rolloverMaxAmount" /> <font color='red'>*</font>
											</label>
										</div>

										<div class="col-lg-8">
											<form:input path="productAttributes['rolloverMaxAmount']"
												id="rolloverMaxAmount" name="rolloverMaxAmount" 
												autocomplete="off" maxlength="10" 
												onkeypress="percent_hidden();"
												onkeyup="return allowNumbersWithAndWithoutDot(this);"
														onblur="DecimalValueFormatPurse(this);"/>
											<div>
												<form:errors path="productAttributes['rolloverMaxAmount']"
													id="rolloverMaxAmount" cssClass="fieldError"
													 />
											</div>
										</div>
									</div>

									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="Rollover Percentage"><spring:message
													code="Purse.rolloverPercent" /> <font color='red'>*</font>
											</label>
										</div>

										<div class="col-lg-8">
											<form:input path="productAttributes['rolloverPercent']"
												id="rolloverPercent" name="rolloverPercent" 
												autocomplete="off" maxlength="5"  onkeypress="amount_hidden();"
												onkeyup="return allowPercentNumbersWithDot(this);"
														onblur="DecimalValueFormatPurse(this);"/>
											<div>
												<form:errors path="productAttributes['rolloverPercent']"
													id="rolloverPercent" cssClass="fieldError" />
											</div>
										</div>
									</div>
								</div> 
								
								<div class="col-lg-12">
									<div class="col-lg-3">
										<label for="Auto Reload Enable"><spring:message
												code="Purse.autoReloadEnable" /></label>
									</div>
									<div class="col-lg-8" id="autoReloadEnable">
									  <c:choose>
											<c:when
												test="${productPurse.productAttributes['autoReloadEnable']=='Enable'}">
                                          
					
										<div class="col-lg-3">
											<form:radiobutton value="Enable" 
												path="productAttributes['autoReloadEnable']"
												id="autoReload_Enable" name="autoReloadEnable" checked="checked" title="Reload is allowed only on existing Account purses" />
											<label class='radiobox-line' for="autoReloadEnable"><spring:message
													code="Purse.autoTopupEnable_Enable" /></label>
										</div>
										<div class="col-lg-3">
											<form:radiobutton value="Disable"
												path="productAttributes['autoReloadEnable']"
												id="autoReload_Disable" name="autoReloadEnable"
												 />
											<label class='radiobox-line' for="autoReloadEnable"><spring:message
													code="Purse.autoTopupEnable_Disable" /></label>
										</div>
										</c:when>
										<c:otherwise>
										  <div class="col-lg-3">
											<form:radiobutton value="Enable" 
												path="productAttributes['autoReloadEnable']"
												id="autoReload_Enable" name="autoReloadEnable" title="Reload is allowed only on existing Account purses" />
											<label class='radiobox-line' for="autoReloadEnable"><spring:message
													code="Purse.autoTopupEnable_Enable" /></label>
										</div>
										<div class="col-lg-3">
											<form:radiobutton value="Disable"
												path="productAttributes['autoReloadEnable']"
												id="autoReload_Disable" name="autoReloadEnable"
												checked="checked" />
											<label class='radiobox-line' for="autoReloadEnable"><spring:message
													code="Purse.autoTopupEnable_Disable" /></label>
										</div>
										</c:otherwise>
										</c:choose>
									</div>
								</div>
								
								<div  id="autoReloadEnableBlock" style="display: none" >
									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="Auto Reload Frequency"><spring:message
													code="Purse.autoReloadFrequency" /> <font color='red'>*</font>
											</label>
										</div>

										<div class="col-lg-8">
											<form:select path="productAttributes['autoReloadFrequency']"
												id="autoReloadFrequency" class="dropdown-medium"
												onchange="reloadFreqDiv();">
												<form:option value="-1" label="--- Select ---"
													selected="selected" />
												<form:option value="day" label="Day" />
												<form:option value="dayOfMonth" label="Day of Month" />
												<form:option value="quarter" label="Quarter" />
												<form:option value="year" label="Year" />
											</form:select>
											<div>
												<form:errors path="productAttributes['autoReloadFrequency']"
													id="autoReloadFrequency" cssClass="fieldError" />
											</div>
										</div>
									</div>

									<div id="reloadFreqDayBlock" style="display: none">
										<div class="col-lg-12">
											<div class="col-lg-3">
												<label for="Reload Frequency Interval"><spring:message
														code="Purse.interval" /> <font color='red'>*</font> </label>
											</div>

											<div class="col-lg-8">
												<form:input path="productAttributes['reloadFreqDay']"
													id="reloadFreqDay" name="reloadFreqDay"
													onkeyup="return isNumericValidity(this);"
													autocomplete="off" maxlength="2" />
												<div>
													<form:errors path="productAttributes['reloadFreqDay']"
														id="reloadFreqDay" cssClass="fieldError" />
												</div>
											</div>
										</div>
									</div>

									<div id="reloadFreqDayOfMonthBlockMulti" style="display: none">
										<div class="col-lg-12">
											<div class="col-lg-3">
												<label for="Reload Frequency Day Of Month"><spring:message
														code="Purse.dayOfMonth" /> <font color='red'>*</font> </label>
											</div>

											<div class="col-lg-8">
												<form:select
													path="productAttributes['reloadFreqDayOfMonthMulti']"
													id="reloadFreqDayOfMonthMulti" class="dropdown-medium"
													multiple="multiple">
													<form:option value="1" label="1" />
													<form:option value="2" label="2" />
													<form:option value="3" label="3" />
													<form:option value="4" label="4" />
													<form:option value="5" label="5" />
													<form:option value="6" label="6" />
													<form:option value="7" label="7" />
													<form:option value="8" label="8" />
													<form:option value="9" label="9" />
													<form:option value="10" label="10" />
													<form:option value="11" label="11" />
													<form:option value="12" label="12" />
													<form:option value="13" label="13" />
													<form:option value="14" label="14" />
													<form:option value="15" label="15" />
													<form:option value="16" label="16" />
													<form:option value="17" label="17" />
													<form:option value="18" label="18" />
													<form:option value="19" label="19" />
													<form:option value="20" label="20" />
													<form:option value="21" label="21" />
													<form:option value="22" label="22" />
													<form:option value="23" label="23" />
													<form:option value="24" label="24" />
													<form:option value="25" label="25" />
													<form:option value="26" label="26" />
													<form:option value="27" label="27" />
													<form:option value="28" label="28" />
													<form:option value="29" label="29" />
													<form:option value="30" label="30" />
													<form:option value="31" label="31" />
												</form:select>
												<div id="multierrorReload"></div>
												<div>
													<form:errors
														path="productAttributes['topupFreqDayOfMonthMulti']"
														id="topupFreqDayOfMonthMulti" cssClass="fieldError" />
												</div>
											</div>
										</div>
									</div>

									<div id="reloadFreqDayOfMonthBlock" style="display: none">
										<div class="col-lg-12">
											<div class="col-lg-3">
												<label for="Reload Frequency Day Of Month"><spring:message
														code="Purse.dayOfMonth" /> <font color='red'>*</font> </label>
											</div>

											<div class="col-lg-8">
												<form:select path="productAttributes['reloadFreqDOM']"
													id="reloadFreqDayOfMonth" class="dropdown-medium">
													<form:option value="-1" label="--- Select ---" />
													<form:option value="1" label="1" />
													<form:option value="2" label="2" />
													<form:option value="3" label="3" />
													<form:option value="4" label="4" />
													<form:option value="5" label="5" />
													<form:option value="6" label="6" />
													<form:option value="7" label="7" />
													<form:option value="8" label="8" />
													<form:option value="9" label="9" />
													<form:option value="10" label="10" />
													<form:option value="11" label="11" />
													<form:option value="12" label="12" />
													<form:option value="13" label="13" />
													<form:option value="14" label="14" />
													<form:option value="15" label="15" />
													<form:option value="16" label="16" />
													<form:option value="17" label="17" />
													<form:option value="18" label="18" />
													<form:option value="19" label="19" />
													<form:option value="20" label="20" />
													<form:option value="21" label="21" />
													<form:option value="22" label="22" />
													<form:option value="23" label="23" />
													<form:option value="24" label="24" />
													<form:option value="25" label="25" />
													<form:option value="26" label="26" />
													<form:option value="27" label="27" />
													<form:option value="28" label="28" />
													<form:option value="29" label="29" />
													<form:option value="30" label="30" />
													<form:option value="31" label="31" />
												</form:select>
												<div>
													<form:errors path="productAttributes['reloadFreqDOM']"
														id="reloadFreqDayOfMonth" cssClass="fieldError" />
												</div>
											</div>
										</div>
									</div>

									<div id="reloadFreqMonthBlock" style="display: none">
										<div class="col-lg-12">
											<div class="col-lg-3">
												<label for="Reload Frequency Month"><spring:message
														code="Purse.month" /> <font color='red'>*</font> </label>
											</div>

											<div class="col-lg-8">
												<form:select path="productAttributes['reloadFreqMonth']"
													id="reloadFreqMonth" class="dropdown-medium">
													<form:option value="-1" label="--- Select ---" />
													<form:option value="1" label="January" />
													<form:option value="2" label="February" />
													<form:option value="3" label="March" />
													<form:option value="4" label="April" />
													<form:option value="5" label="May" />
													<form:option value="6" label="June" />
													<form:option value="7" label="July" />
													<form:option value="8" label="August" />
													<form:option value="9" label="September" />
													<form:option value="10" label="October" />
													<form:option value="11" label="November" />
													<form:option value="12" label="December" />
												</form:select>
												<div>
													<form:errors path="productAttributes['reloadFreqMonth']"
														id="reloadFreqMonth" cssClass="fieldError" />
												</div>

											</div>
										</div>
									</div>
									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="Auto Reload Amount"><spring:message
													code="Purse.autoReloadAmount" /> <font color='red'>*</font>
											</label>
										</div>

										<div class="col-lg-8">
											<form:input path="productAttributes['autoReloadAmount']" 
												id="autoReloadAmount" name="autoReloadAmount" 
												autocomplete="off" maxlength="10" 
												onkeyup="return allowNumbersWithAndWithoutDot(this);"
														onblur="DecimalValueFormatPurse(this);"/>
											<div>
												<form:errors path="productAttributes['autoReloadAmount']"
													id="autoReloadAmount" cssClass="fieldError" />
											</div>
										</div>
									</div>
								</div> 
								<div>
								<!-- <select id="example-getting-started" multiple="multiple">
    <option value="cheese">Cheese</option>
    <option value="tomatoes">Tomatoes</option>
    <option value="mozarella">Mozzarella</option>
    <option value="mushrooms">Mushrooms</option>
    <option value="pepperoni">Pepperoni</option>
    <option value="onions">Onions</option>
</select> -->
								</div>
								<%-- 
								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="splitTender"><spring:message
												code="Product.splitTender" /></label>
									</div>
									<div class="col-lg-8">
										<div class="col-lg-4">
											<form:radiobutton value="true"
												path="productAttributes['splitTender']"
												id="splitTender_Enable" name="splitTender" />
											<label class='radiobox-line' for="splitTender"><spring:message
													code="Product.splitTender_Enable" /></label>
										</div>

										<div class="col-lg-4">
											<form:radiobutton value="false"
												path="productAttributes['splitTender']"
												id="splitTender_Disable" name="splitTender" />
											<label class='radiobox-line' for="splitTender"><spring:message
													code="Product.splitTender_Disable" /></label>
										</div>
										<div>
											<form:errors path="productAttributes['splitTender']"
												id="splitTenderErr" cssStyle="color:red" />
										</div>
									</div>
								</div> --%>
								
								
								<div class="col-lg-12 text-center">
									<button type="submit" id="productPurseButton"
										class="btn btn-primary btn-normal"
										onclick="return purseSubmit(this.form.id,event)">
										<i class="glyphicon glyphicon-saved"></i>
										<spring:message code="button.update" />
									</button>
								</div>

							</div>
						</div>

					</article>
				</section>
			</form:form>
		</div>
	</div>
	


</body>
<script>

$(document).ready(function() {
    $('#topupFreqDayOfMonthMulti').multiselect();
});

$(document).ready(function() {
    $('#reloadFreqDayOfMonthMulti').multiselect();
});
	
	$(function() {
		$('#autoTopupEnable input[type=radio]').change(function() {
			$("#autoTopupEnableBlock").attr("style", "display:none");
			$("#rolloverEnableBlock").attr("style", "display:none");
			$("#rollover_Disable").prop("checked", true);
			$("#autoTopupAmount").val('');
			$("#rolloverMaxAmount").val('');
			$("#rolloverPercent").val('');
			$("#autoTopupFrequency").val('-1');
			$("#topupFreqDayBlock").attr("style", "display:none");
			$("#topupFreqDayOfMonthBlockMulti").attr("style", "display:none");
			$("#topupFreqDayOfMonthBlock").attr("style", "display:none");
			$("#topupFreqMonthBlock").attr("style", "display:none");
			$("#topupFreqDay").val('');
			$("#topupFreqDayOfMonthMulti").multiselect("clearSelection");
			$("#topupFreqDayOfMonth").val('-1');
			$("#topupFreqMonth").val('-1');
			if ($(this).val() == 'Enable') {
				$("#autoTopupEnableBlock").attr("style", "display:block");
				/* var selValue = document.getElementById("rollover_Enable").checked;
				if (selValue) {
					$("#rolloverEnableBlock").attr("style", "display:block");
				}  */
				/* $("#autoTopupFrequency").val('-1');
				$("#topupFreqDayBlock").attr("style", "display:none");
				$("#topupFreqDayOfMonthBlockMulti").attr("style", "display:none");
				$("#topupFreqDayOfMonthBlock").attr("style", "display:none");
				$("#topupFreqMonthBlock").attr("style", "display:none"); */
			}/* else{
				$("#autoTopupEnableBlock").attr("style", "display:none");
				$("#rolloverEnableBlock").attr("style", "display:none");
				$("#rollover_Disable").prop("checked", true);
				$("#autoTopupAmount").val('');
				$("#rolloverMaxAmount").val('');
				$("#rolloverPercent").val('');
				$("#autoTopupFrequency").val('-1');
				$("#topupFreqDayBlock").attr("style", "display:none");
				$("#topupFreqDayOfMonthBlockMulti").attr("style", "display:none");
				$("#topupFreqDayOfMonthBlock").attr("style", "display:none");
				$("#topupFreqMonthBlock").attr("style", "display:none");
				$("#topupFreqDay").val('');
				$("#topupFreqDayOfMonthMulti").multiselect("clearSelection");
				$("#topupFreqDayOfMonth").val('-1');
				$("#topupFreqMonth").val('-1');
				
			} */
			

		})
	});
		
	$(function() {
		$('#autoReloadEnable input[type=radio]').change(function() {
			$("#autoReloadEnableBlock").attr("style", "display:none");
			$("#autoReloadAmount").val('');
			$("#autoReloadFrequency").val('-1');
			$("#reloadFreqDayBlock").attr("style", "display:none");
			$("#reloadFreqDayOfMonthBlockMulti").attr("style", "display:none");
			$("#reloadFreqDayOfMonthBlock").attr("style", "display:none");
			$("#reloadFreqMonthBlock").attr("style", "display:none");
			$("#reloadFreqDay").val('');
			$("#reloadFreqDayOfMonthMulti").multiselect("clearSelection");
			$("#reloadFreqDayOfMonth").val('-1');
			$("#reloadFreqMonth").val('-1');
			if ($(this).val() == 'Enable') {
				$("#autoReloadEnableBlock").attr("style", "display:block");
			}
		})
	});

	$(function() {
		$('#rolloverEnable input[type=radio]').change(function() {
			if ($(this).val() == 'Enable') {
				$("#rolloverEnableBlock").attr("style", "display:block");
			} else {
				$("#rolloverEnableBlock").attr("style", "display:none");
				document.getElementById("rolloverPercent").readOnly = false;
				document.getElementById("rolloverMaxAmount").readOnly = false;
				$("#rolloverMaxAmount").val('');
				$("#rolloverPercent").val('');
			}
		})
	});

	/* $(function() {
		$('#autoReloadEnable input[type=radio]').change(function() {
			if ($(this).val() == 'Enable') {
				$("#autoReloadEnableBlock").attr("style", "display:block");
			} else {
				$("#autoReloadEnableBlock").attr("style", "display:none");
				$("#autoReloadAmount").val('');
			}
		})
	}); */

	function onloadDiv() {

		if ($("#autoTopup_Enable").is(":checked")) {
			$("#autoTopupEnableBlock").attr("style", "display:block");
			if ($("#rollover_Enable").is(":checked")) {
				$("#rolloverEnableBlock").attr("style", "display:block");
			}
			var option = "";

			option = $("#autoTopupFrequency").val();

			if (option == "day") {
				$("#topupFreqDayBlock").attr("style", "display:block");
				$("#topupFreqDayOfMonthBlockMulti").attr("style",
						"display:none");
				$("#topupFreqDayOfMonthBlock").attr("style", "display:none");
				$("#topupFreqMonthBlock").attr("style", "display:none");

				$("#topupFreqDayOfMonthMulti").multiselect("clearSelection");
				$("#topupFreqDayOfMonth").val('-1');
				$("#topupFreqMonth").val('-1');
			}
			if (option == "dayOfMonth") {
				$("#topupFreqDayBlock").attr("style", "display:none");
				$("#topupFreqDayOfMonthBlockMulti").attr("style",
						"display:block");
				$("#topupFreqDayOfMonthBlock").attr("style", "display:none");
				$("#topupFreqMonthBlock").attr("style", "display:none");
				$("#topupFreqDay").val('');
				$("#topupFreqDayOfMonth").val('-1');
				$("#topupFreqMonth").val('-1');
			}
			if (option == "quarter" || option == "year") {
				$("#topupFreqDayBlock").attr("style", "display:none");
				$("#topupFreqDayOfMonthBlockMulti").attr("style",
						"display:none");
				$("#topupFreqDayOfMonthBlock").attr("style", "display:block");
				$("#topupFreqMonthBlock").attr("style", "display:block");
				$("#topupFreqDay").val('');
				$("#topupFreqDayOfMonthMulti").multiselect("clearSelection");

			}
			if (option == "-1") {
				$("#topupFreqDayBlock").attr("style", "display:none");
				$("#topupFreqDayOfMonthBlockMulti").attr("style",
						"display:none");
				$("#topupFreqDayOfMonthBlock").attr("style", "display:none");
				$("#topupFreqMonthBlock").attr("style", "display:none");
				$("#topupFreqDay").val('');
				$("#topupFreqDayOfMonthMulti").multiselect("clearSelection");
				$("#topupFreqDayOfMonth").val('-1');
				$("#topupFreqMonth").val('-1');
			}
			if($("#rolloverMaxAmount").val() == "" || $("#rolloverMaxAmount").val() == null){
				document.getElementById("rolloverMaxAmount").readOnly = true;
			}else{
				document.getElementById("rolloverPercent").readOnly = true;
			}
		}

		if ($("#autoReload_Enable").is(":checked")) {
			$("#autoReloadEnableBlock").attr("style", "display:block");
			
			var reloadOption = "";

			reloadOption = $("#autoReloadFrequency").val();

			if (reloadOption == "day") {
				$("#reloadFreqDayBlock").attr("style", "display:block");
				$("#reloadFreqDayOfMonthBlockMulti").attr("style",
						"display:none");
				$("#reloadFreqDayOfMonthBlock").attr("style", "display:none");
				$("#reloadFreqMonthBlock").attr("style", "display:none");

				$("#reloadFreqDayOfMonthMulti").multiselect("clearSelection");
				$("#reloadFreqDayOfMonth").val('-1');
				$("#reloadFreqMonth").val('-1');
			}
			if (reloadOption == "dayOfMonth") {
				$("#reloadFreqDayBlock").attr("style", "display:none");
				$("#reloadFreqDayOfMonthBlockMulti").attr("style",
						"display:block");
				$("#reloadFreqDayOfMonthBlock").attr("style", "display:none");
				$("#reloadFreqMonthBlock").attr("style", "display:none");
				$("#reloadFreqDay").val('');
				$("#reloadFreqDayOfMonth").val('-1');
				$("#reloadFreqMonth").val('-1');
			}
			if (reloadOption == "quarter" || reloadOption == "year") {
				$("#reloadFreqDayBlock").attr("style", "display:none");
				$("#reloadFreqDayOfMonthBlockMulti").attr("style",
						"display:none");
				$("#reloadFreqDayOfMonthBlock").attr("style", "display:block");
				$("#reloadFreqMonthBlock").attr("style", "display:block");
				$("#reloadFreqDay").val('');
				$("#reloadFreqDayOfMonthMulti").multiselect("clearSelection");

			}
			if (reloadOption == "-1") {
				$("#reloadFreqDayBlock").attr("style", "display:none");
				$("#reloadFreqDayOfMonthBlockMulti").attr("style",
						"display:none");
				$("#reloadFreqDayOfMonthBlock").attr("style", "display:none");
				$("#reloadFreqMonthBlock").attr("style", "display:none");
				$("#reloadFreqDay").val('');
				$("#reloadFreqDayOfMonthMulti").multiselect("clearSelection");
				$("#reloadFreqDayOfMonth").val('-1');
				$("#reloadFreqMonth").val('-1');
			}
		}
	}

	function percent_hidden() {
		document.getElementById("rolloverPercent").readOnly = true;

	}

	function amount_hidden() {
		document.getElementById("rolloverMaxAmount").readOnly = true;

	}
</script>

</html>



