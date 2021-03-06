<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<style>

.btn{
margin-left: 15px;}
input {
	width: -moz-available;
}

.tab-pane {
	width: -moz-fit-content;
	width: fit-content;
	border-top: 1px solid #a1a1a1;
	float: left;
}


/* input:-moz-read-only { /* For Firefox 
     background-color: rgb(235, 235, 228);!important
}

input:read-only {
     background-color: rgb(235, 235, 228);!important
} */

input {
	width: 90%;
	padding: 0px 0px;
}

input[type='checkbox'] {
	border: 0px;
}

@media screen and (-ms-high-contrast: active) , screen and
	(-ms-high-contrast: none) {
	input {
		width: 90%;
		line-height: normal !important;
		-webkit-appearance: textfield;
		background-color: white;
		-webkit-rtl-ordering: logical;
		cursor: text;
		padding: 1px;
		text-rendering: auto;
		color: initial;
		letter-spacing: normal;
		word-spacing: normal;
		text-transform: none;
		text-indent: 0px;
		text-shadow: none;
		display: inline-block;
		text-align: start;
		margin: 0em;
		font: 400 13.3333px Arial
	}
}
</style>
<!--[if IE]>
<style>

input{
	width:90%;
	
	line-height : normal !important;
	  -webkit-appearance: textfield;
    background-color: white;
    -webkit-rtl-ordering: logical;
    cursor: text;
    padding: 1px;
    	  text-rendering: auto;
    color: initial;
    letter-spacing: normal;
    word-spacing: normal;
    text-transform: none;
    text-indent: 0px;
    text-shadow: none;
    display: inline-block;
    text-align: start;
    margin: 0em;
    font: 400 13.3333px Arial
}

</style>
<![endif]-->
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/productLimitsAndFees.js"></script>

<form:form name="maintenanceFeeForm" id="maintenanceFeeForm"
	action="saveMaintenanceFee" modelAttribute="productForm"
	class='form-horizontal'>

	<form:input path="productId" name="productId" type="hidden" />
	<div id="feedBackTd" class="form-group text-center" style="padding-top:7px">
		<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align: center; font-weight: bold;">
				<c:out value="${statusMessage }" />
			</p>
		</c:if>
		<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align: center; font-weight: bold;">
				<c:out value="${statusMessage }" />
			</p>
		</c:if>
	</div>
	<div
		<c:if test="${status.index == 0}">class="tab-pane fade in active graybox"</c:if>
		<c:if test="${status.index != 0}">class="tab-pane fade in graybox"</c:if>
		id="${deliveryChannel.deliveryChnlShortName}"
		style="width: fit-content; float: left;margin:5px;padding-bottom: 15px;
    padding-top: 15px;
    padding-left: 15px;
    padding-right: 15px;">

		<div class="col-lg-12">


			<table id="tableViewUsers"
				class="table table-hover table-striped table-bordered"
				style="width: 1300px; table-layout: fixed;">

				<thead class="table-head">

					<tr>
						<th style="width: 175px"><spring:message code="" text="" /></th>
						<th style="width: 175px"><spring:message
								code="product.fees.desc" text="Fee Description" /></th>
						<th style="width: 175px;"><spring:message
								code="product.monthlyFeeCap.assessmentDate"
								text="Assessment Date" /></th>
						<th style="width: 65px"><spring:message code="product.maintenanceFee.proration"
								text="ProRation" /></th>
						<th style="width: 110px"><spring:message
								code="product.fees.feeAmt" text="Fee Amt" /></th>

						<th style="width: 65px"><spring:message
								code="product.fees.clawBack" text="Clawback" /></th>
						<th style="width: 65px;"><spring:message
								code="product.fees.clawbackCnt" text="Clawback Count" /></th>
						<th style="width: 70px"><spring:message
								code="product.maintenanceFee.clawbackoptn"
								text="Clawback Option" /></th>
						<th style="width: 110px"><spring:message
								code="product.maintenanceFee.clawbackMaxAmt"
								text="Clawback Maximum Amount" /></th>

						<th style="width: 110px"><spring:message
								code="product.maintenanceFee.firstFeeAssessDay"
								text="First Month Fee Assessed Days" /></th>
						<th style="width: 110px"><spring:message
								code="product.fees.capFeeAmt" text="Cap Fee Amt" /></th>
						<th style="width: 65px;"><spring:message
								code="product.maintenanceFee.freeCnt" text="Free Count" /></th>
						<th style="width: 65px"><spring:message
								code="product.maintenanceFee.maxCnt" text="Max Count" /></th>
						<th style="width: 110px"><spring:message
								code="product.fees.monthlyFeeCapAvl"
								text="Monthly Fee Cap Available" /></th>

					</tr>
				</thead>
				<tbody>
					<tr>
						<td><spring:message code="product.monthlyFee.label"
								text="Monthly Fee" /></td>
						
						<td><form:input type="text"
											path="productAttributes[monthlyFee_feeDesc]"
											name="monthlyFee_feeDesc"
											id="monthlyFee_feeDesc"
											style="width: -webkit-fill-available;" maxLength="100" onblur="return validateFeeDescription(this)" readonly="true"/>
											<form:errors path="productAttributes[monthlyFee_feeDesc]" cssClass="fieldError"></form:errors></td>
						<td><form:select
								style="width:160px; width: -webkit-fill-available;"
								path="productAttributes[monthlyFee_assessmentDate]"
								name="monthlyFee_assessmentDate" id="monthlyFee_assessmentDate" onchange="enableProRation()" disabled="true">

								<form:option value="AD">Anniversary Date of Account Activation</form:option>
								<form:option value="FD">First Calendar Date of Each Month</form:option>
								<form:option value="AL">Anniversary Date of First Load</form:option>
								<form:option value="FLI">Funding Date of Account without Activation</form:option>
								

							</form:select> <form:errors path="productAttributes[monthlyFee_assessmentDate]"
								cssClass="fieldError"></form:errors></td>

						<td><form:checkbox
								path="productAttributes[monthlyFee_proRation]" value="true"
								id="monthlyFee_proRation" style="margin-left:15px" name="monthlyFee_proRation" disabled="true"/> <form:errors
								path="productAttributes[monthlyFee_proRation]"
								cssClass="fieldError"></form:errors></td>
						<td><form:input
								path="productAttributes[monthlyFee_feeAmt]" type="text"
								onkeypress="return allowNumbersWithDot(event);"
								onblur="validateFeeAmt(this);"
								name="monthlyFee_feeAmt" id="monthlyFee_feeAmt"
								style="width: -webkit-fill-available;" maxlength="10" readonly="true"/> <form:errors
								path="productAttributes[monthlyFee_feeAmt]"
								cssClass="fieldError" ></form:errors></td>
						
						<td><form:checkbox
								path="productAttributes[monthlyFee_clawback]" value="true"
								id="monthlyFee_clawback" name="monthlyFee_clawback"
								onchange="enableClawbackOptions(this)" disabled="true"/> <form:errors
								path="productAttributes[monthlyFee_clawback]"
								cssClass="fieldError"></form:errors></td>

						<td><form:input type="text"
								path="productAttributes[monthlyFee_clawbackCount]"
								name="monthlyFee_clawbackCount" id="monthlyFee_clawbackCount"
								style="width: -webkit-fill-available;"
								onkeypress="return isNumericfn(event);"
								onblur="return validateClawbackCount(this)" maxlength="6"
								readonly="true" />
							<form:errors path="productAttributes[monthlyFee_clawbackCount]"
								cssClass="fieldError"></form:errors></td>

						<td><form:select
								path="productAttributes[monthlyFee_clawbackOption]"
								name="monthlyFee_clawbackOption" id="monthlyFee_clawbackOption"
								style="width:60px;width: -webkit-fill-available;" onchange="return  clawbackOptionChange(this)" disabled="true">
								
								<form:option value="O">OR</form:option>
								<form:option value="A">AND</form:option>
								<form:option value="N">NA</form:option>
							</form:select>
							<form:errors path="productAttributes[monthlyFee_clawbackOption]"
								cssClass="fieldError"></form:errors></td>
						<td><form:input
								path="productAttributes[monthlyFee_clawbackMaxAmt]" type="text"
								onkeypress="return allowNumbersWithDot(event);"
								onblur="return validateClabackMaxFeeAmt(this);"
								name="monthlyFee_clawbackMaxAmt" id="monthlyFee_clawbackMaxAmt"
								style="width: -webkit-fill-available;" maxlength="10" readonly="true"/> <form:errors
								path="productAttributes[monthlyFee_clawbackMaxAmt]"
								cssClass="fieldError"></form:errors></td>
						<td><form:input
								path="productAttributes[monthlyFee_firstMonthFeeAssessedDays]"
								type="text" onkeypress="return isNumericfn(event);"
								onblur="return validateCount(this)"
								name="monthlyFee_firstMonthFeeAssessedDays"
								id="monthlyFee_firstMonthFeeAssessedDays"
								style="width: -webkit-fill-available;" maxlength="3" readonly="true"/> <form:errors
								path="productAttributes[monthlyFee_firstMonthFeeAssessedDays]"
								cssClass="fieldError"></form:errors></td>
								
						<td><form:input
								path="productAttributes[monthlyFee_capFeeAmt]" type="text"
								onkeypress="return allowNumbersWithDot(event);"
								onblur="return validMaintenanceCapFeeAmt(this);"
								name="monthlyFee_capFeeAmt" id="monthlyFee_capFeeAmt"
								style="width: -webkit-fill-available;" maxlength="10"  readonly="true"/> <form:errors
								path="productAttributes[monthlyFee_capFeeAmt]"
								cssClass="fieldError"></form:errors></td>
								
						<td><form:input type="text"
								path="productAttributes[monthlyFee_freeCount]"
								name="monthlyFee_freeCount" id="monthlyFee_freeCount"
								style="width: -webkit-fill-available;"
								onkeypress="return isNumericfn(event);"
								onblur="return validateCount(this)" maxlength="6" readonly="true"
								/>
							<form:errors path="productAttributes[monthlyFee_freeCount]"
								cssClass="fieldError"></form:errors></td>

						<td><form:input type="text"
								path="productAttributes[monthlyFee_maxCount]"
								name="monthlyFee_maxCount" id="monthlyFee_maxCount"
								style="width: -webkit-fill-available;"
								onkeypress="return isNumericfn(event);"
								onblur="return validateCount(this)" maxlength="6" readonly="true"
							 />
							<form:errors path="productAttributes[monthlyFee_maxCount]"
								cssClass="fieldError"></form:errors></td>

						<td><form:checkbox
								path="productAttributes[monthlyFee_monthlyFeeCapAvail]"
								value="true" id="monthlyFee_monthlyFeeCapAvail"
								name="monthlyFee_monthlyFeeCapAvail" disabled="true"/> <form:errors
								path="productAttributes[monthlyFee_monthlyFeeCapAvail]"
								cssClass="fieldError"></form:errors></td>

					</tr>
					<tr>
						<td><spring:message code="product.weeklyFee.label"
								text="Weekly Fee" /></td>
						<td><form:input type="text"
											path="productAttributes[weeklyFee_feeDesc]"
											name="weeklyFee_feeDesc"
											id="weeklyFee_feeDesc"
											style="width: -webkit-fill-available;" maxLength="100" onblur="return validateFeeDescription(this)" readonly="true"/>
											<form:errors path="productAttributes[weeklyFee_feeDesc]" cssClass="fieldError"></form:errors></td>
						<td style=" text-align:  center;"><%-- <form:select
								style="width:85px; width: -webkit-fill-available;"
								path="productAttributes[weekyFee_assessmentDate]"
								name="weekyFee_assessmentDate" id="weekyFee_assessmentDate"disabled="true">

								<form:option value="NA">NA</form:option>

							</form:select> <form:errors path="productAttributes[weekyFee_assessmentDate]"
								cssClass="fieldError"></form:errors> --%> NA</td>
								
						<td style=" text-align:  center;">NA</td>
						<td><form:input
								path="productAttributes[weeklyFee_feeAmt]" type="text"
								onkeypress="return allowNumbersWithDot(event);"
								onblur="return validateFeeAmt(this);"
								name="weeklyFee_feeAmt" id="weeklyFee_feeAmt"
								style="width: -webkit-fill-available;" maxlength="10" readonly="true"/> <form:errors
								path="productAttributes[weeklyFee_feeAmt]"
								cssClass="fieldError"></form:errors></td>
								
						<td><form:checkbox
								path="productAttributes[weeklyFee_clawback]" value="true"
								id="weeklyFee_clawback" name="weeklyFee_clawback"
								onchange="return enableClawbackOptions(this)" disabled="true"/> <form:errors
								path="productAttributes[weeklyFee_clawback]"
								cssClass="fieldError"></form:errors></td>

						<td><form:input type="text"
								path="productAttributes[weeklyFee_clawbackCount]"
								name="weeklyFee_clawbackCount" id="weeklyFee_clawbackCount"
								style="width: -webkit-fill-available;"
								onkeypress="return isNumericfn(event);"
								onblur="return validateClawbackCount(this)" maxlength="6"
								readonly="true" />
							<form:errors path="productAttributes[weeklyFee_clawbackCount]"
								cssClass="fieldError"></form:errors></td>

						<td><form:select
								path="productAttributes[weeklyFee_clawbackOption]"
								name="weeklyFee_clawbackOption" id="weeklyFee_clawbackOption"
								style="width:60px;width: -webkit-fill-available;" onchange="return clawbackOptionChange(this)" disabled="true">
								
								<form:option value="O">OR</form:option>
								<form:option value="A">AND</form:option>
								<form:option value="N">NA</form:option>
								
							</form:select>
							<form:errors path="productAttributes[weeklyFee_clawbackOption]"
								cssClass="fieldError"></form:errors></td>
						<td><form:input
								path="productAttributes[weeklyFee_clawbackMaxAmt]" type="text"
								onkeypress="return allowNumbersWithDot(event);"
								onblur="return validateClabackMaxFeeAmt(this);"
								name="weeklyFee_clawbackMaxAmt" id="weeklyFee_clawbackMaxAmt"
								style="width: -webkit-fill-available;" maxlength="10" readonly="true"/> <form:errors
								path="productAttributes[weeklyFee_clawbackMaxAmt]"
								cssClass="fieldError"></form:errors></td>
						<td><form:input
								path="productAttributes[weeklyFee_firstMonthFeeAssessedDays]"
								type="text" onkeypress="return isNumericfn(event);"
								onblur="return validateCount(this)"
								name="weeklyFee_firstMonthFeeAssessedDays"
								id="weeklyFee_firstMonthFeeAssessedDays"
								style="width: -webkit-fill-available;" maxlength="3" readonly="true" /> <form:errors
								path="productAttributes[weeklyFee_firstMonthFeeAssessedDays]"
								cssClass="fieldError"></form:errors></td>
								
						<td><form:input
								path="productAttributes[weeklyFee_capFeeAmt]" type="text"
								onkeypress="return allowNumbersWithDot(event);"
								onblur="return validMaintenanceCapFeeAmt(this);"
								name="weeklyFee_capFeeAmt" id="weeklyFee_capFeeAmt"
								style="width: -webkit-fill-available;" maxlength="10" readonly="true"/> <form:errors
								path="productAttributes[weeklyFee_capFeeAmt]"
								cssClass="fieldError"></form:errors></td>
								
						<td><form:input type="text"
								path="productAttributes[weeklyFee_freeCount]"
								name="weeklyFee_freeCount" id="weeklyFee_freeCount"
								style="width: -webkit-fill-available;"
								onkeypress="return isNumericfn(event);"
								onblur="return validateCount(this)" maxlength="6" readonly="true"
								 />
							<form:errors path="productAttributes[weeklyFee_freeCount]"
								cssClass="fieldError"></form:errors></td>

						<td><form:input type="text"
								path="productAttributes[weeklyFee_maxCount]"
								name="weeklyFee_maxCount" id="weeklyFee_maxCount"
								style="width: -webkit-fill-available;"
								onkeypress="return isNumericfn(event);"
								onblur="return validateCount(this)" maxlength="6" readonly="true"
							/>
							<form:errors path="productAttributes[weeklyFee_maxCount]"
								cssClass="fieldError"></form:errors></td>

						<td><form:checkbox
								path="productAttributes[weeklyFee_weeklyFeeCapAvail]"
								value="true" id="weeklyFee_weeklyFeeCapAvail"
								name="weeklyFee_weeklyFeeCapAvail" disabled="true"/> <form:errors
								path="productAttributes[weeklyFee_weeklyFeeCapAvail]"
								cssClass="fieldError"></form:errors></td>
					</tr>
					<tr>
						<td><spring:message code="product.annualFee.label"
								text="Annual Fee" /></td>
						<td><form:input type="text"
											path="productAttributes[yearlyFee_feeDesc]"
											name="yearlyFee_feeDesc"
											id="yearlyFee_feeDesc"
											style="width: -webkit-fill-available;" maxLength="100" onblur="return validateFeeDescription(this)" readonly="true"/>
											<form:errors path="productAttributes[yearlyFee_feeDesc]" cssClass="fieldError"></form:errors></td>
						<td style=" text-align:  center;"><%-- <form:select
								style="width:85px; width: -webkit-fill-available;"
								path="productAttributes[yearlyFee_assessmentDate]"
								name="yearlyFee_assessmentDate" id="yearlyFee_assessmentDate"disabled="true">

								<form:option value="NA">NA</form:option>

							</form:select> <form:errors path="productAttributes[yearlyFee_assessmentDate]"
								cssClass="fieldError"></form:errors> --%>NA</td>
							<td style=" text-align:  center;">NA</td>
							<td><form:input
								path="productAttributes[yearlyFee_feeAmt]" type="text"
								onkeypress="return allowNumbersWithDot(event);"
								onblur="return validateFeeAmt(this);"
								name="yearlyFee_feeAmt" id="yearlyFee_feeAmt"
								style="width: -webkit-fill-available;" maxlength="10" readonly="true"/> <form:errors
								path="productAttributes[yearlyFee_feeAmt]"
								cssClass="fieldError"></form:errors></td>
						<td><form:checkbox
								path="productAttributes[yearlyFee_clawback]" value="true"
								id="yearlyFee_clawback" name="yearlyFee_clawback"
								onchange="return enableClawbackOptions(this)" disabled="true"/> <form:errors
								path="productAttributes[yearlyFee_clawback]"
								cssClass="fieldError"></form:errors></td>

						<td><form:input type="text"
								path="productAttributes[yearlyFee_clawbackCount]"
								name="yearlyFee_clawbackCount" id="yearlyFee_clawbackCount"
								style="width: -webkit-fill-available;"
								onkeypress="return isNumericfn(event);"
								onblur="return validateClawbackCount(this)" maxlength="6"
								readonly="true" />
							<form:errors path="productAttributes[yearlyFee_clawbackCount]"
								cssClass="fieldError"></form:errors></td>

						<td><form:select
								path="productAttributes[yearlyFee_clawbackOption]"
								name="yearlyFee_clawbackOption" id="yearlyFee_clawbackOption"
								style="width:60px;width: -webkit-fill-available;" onchange="clawbackOptionChange(this)" disabled="true">
								
								<form:option value="O">OR</form:option>
								<form:option value="A">AND</form:option>
								<form:option value="N">NA</form:option>
								 
							</form:select>
							<form:errors path="productAttributes[yearlyFee_clawbackOption]"
								cssClass="fieldError"></form:errors></td>
						<td><form:input
								path="productAttributes[yearlyFee_clawbackMaxAmt]" type="text"
								onkeypress="return allowNumbersWithDot(event);"
								onblur="return validateClabackMaxFeeAmt(this)"
								name="yearlyFee_clawbackMaxAmt" id="yearlyFee_clawbackMaxAmt"
								style="width: -webkit-fill-available;" maxlength="10" readonly="true"/> <form:errors
								path="productAttributes[yearlyFee_clawbackMaxAmt]"
								cssClass="fieldError"></form:errors></td>
						<td><form:input
								path="productAttributes[yearlyFee_firstMonthFeeAssessedDays]"
								type="text" onkeypress="return isNumericfn(event);"
								onblur="return validateCount(this)"
								name="yearlyFee_firstMonthFeeAssessedDays"
								id="yearlyFee_firstMonthFeeAssessedDays"
								style="width: -webkit-fill-available;" maxlength="3" readonly="true"/> <form:errors
								path="productAttributes[yearlyFee_firstMonthFeeAssessedDays]"
								cssClass="fieldError"></form:errors></td>
								
						<td><form:input
								path="productAttributes[yearlyFee_capFeeAmt]" type="text"
								onkeypress="return allowNumbersWithDot(event);"
								onblur="return validMaintenanceCapFeeAmt(this);"
								name="yearlyFee_capFeeAmt" id="yearlyFee_capFeeAmt"
								style="width: -webkit-fill-available;" maxlength="10" readonly="true"/> <form:errors
								path="productAttributes[yearlyFee_capFeeAmt]"
								cssClass="fieldError"></form:errors></td>
								
						<td><form:input type="text"
								path="productAttributes[yearlyFee_freeCount]"
								name="yearlyFee_freeCount" id="yearlyFee_freeCount"
								style="width: -webkit-fill-available;"
								onkeypress="return isNumericfn(event);"
								onblur="return validateCount(this)" maxlength="6" readonly="true"
								/>
							<form:errors path="productAttributes[yearlyFee_freeCount]"
								cssClass="fieldError"></form:errors></td>

						<td><form:input type="text"
								path="productAttributes[yearlyFee_maxCount]"
								name="yearlyFee_maxCount" id="yearlyFee_maxCount"
								style="width: -webkit-fill-available;"
								onkeypress="return isNumericfn(event);"
								onblur="return validateCount(this)" maxlength="6" readonly="true"
								/>
							<form:errors path="productAttributes[yearlyFee_maxCount]"
								cssClass="fieldError"></form:errors></td>

						<td><form:checkbox
								path="productAttributes[yearlyFee_yearlyFeeCapAvail]"
								value="true" id="yearlyFee_yearlyFeeCapAvail"
								name="yearlyFee_yearlyFeeCapAvail" disabled="true"/> <form:errors
								path="productAttributes[yearlyFee_yearlyFeeCapAvail]"
								cssClass="fieldError"></form:errors></td>
					</tr>
					<tr>
						<td><spring:message code="product.dormancyFee.label"
								text="Dormancy Fee" /></td>
								
						<td><form:input type="text"
											path="productAttributes[dormancyFee_feeDesc]"
											name="dormancyFee_feeDesc"
											id="dormancyFee_feeDesc"
											style="width: -webkit-fill-available;" maxLength="100" onblur="return validateFeeDescription(this)" readonly="true"/>
											<form:errors path="productAttributes[dormancyFee_feeDesc]" cssClass="fieldError"></form:errors></td>
						<td style=" text-align:  center;"><%-- <form:select
								style="width:85px; width: -webkit-fill-available;"
								path="productAttributes[dormancyFee_assessmentDate]"
								name="dormancyFee_assessmentDate" id="dormancyFee_assessmentDate" disabled="true">

								<form:option value="NA">NA</form:option>
							

							</form:select> <form:errors path="productAttributes[dormancyFee_assessmentDate]"
								cssClass="fieldError"></form:errors> --%>NA</td><td style=" text-align:  center;">NA</td>
								
						<td><form:input
								path="productAttributes[dormancyFee_feeAmt]" type="text"
								onkeypress="return allowNumbersWithDot(event);"
								onblur="return validateFeeAmt(this);"
								name="dormancyFee_feeAmt" id="dormancyFee_feeAmt"
								style="width: -webkit-fill-available;" maxlength="10" readonly="true"/> <form:errors
								path="productAttributes[dormancyFee_feeAmt]"
								cssClass="fieldError"></form:errors></td>
						<td><form:checkbox
								path="productAttributes[dormancyFee_clawback]" value="true"
								id="dormancyFee_clawback" name="dormancyFee_clawback"
								onchange="return enableClawbackOptions(this)" disabled="true" /> <form:errors
								path="productAttributes[dormancyFee_clawback]"
								cssClass="fieldError"></form:errors></td>

						<td><form:input type="text"
								path="productAttributes[dormancyFee_clawbackCount]"
								name="dormancyFee_clawbackCount" id="dormancyFee_clawbackCount"
								style="width: -webkit-fill-available;"
								onkeypress="return isNumericfn(event);"
								onblur="return validateClawbackCount(this)" maxlength="6"
								readonly="true" />
							<form:errors path="productAttributes[dormancyFee_clawbackCount]"
								cssClass="fieldError"></form:errors></td>

						<td><form:select
								path="productAttributes[dormancyFee_clawbackOption]"
								name="dormancyFee_clawbackOption" id="dormancyFee_clawbackOption"
								style="width:60px;width: -webkit-fill-available;" onchange="clawbackOptionChange(this)" disabled="true">
								
								<form:option value="O">OR</form:option>
								<form:option value="A">AND</form:option>
								<form:option value="N">NA</form:option>
								
								
							</form:select>
							<form:errors path="productAttributes[dormancyFee_clawbackOption]"
								cssClass="fieldError"></form:errors></td>
						<td><form:input
								path="productAttributes[dormancyFee_clawbackMaxAmt]" type="text"
								onkeypress="return allowNumbersWithDot(event);"
								onblur="return validateClabackMaxFeeAmt(this);"
								name="dormancyFee_clawbackMaxAmt" id="dormancyFee_clawbackMaxAmt"
								style="width: -webkit-fill-available;" maxlength="10" readonly="true" /> <form:errors
								path="productAttributes[dormancyFee_clawbackMaxAmt]"
								cssClass="fieldError"></form:errors></td>
						<td style=" text-align:  center;"><%-- <form:input
								path="productAttributes[dormancyFee_firstMonthFeeAssessedDays]"
								type="text" onkeypress="allowNumbersWithDot(event);"
								onblur="DecimalValueFormat(this);"
								name="dormancyFee_firstMonthFeeAssessedDays"
								id="dormancyFee_firstMonthFeeAssessedDays"
								style="width: -webkit-fill-available;" maxlength="10" /> <form:errors
								path="productAttributes[dormancyFee_firstMonthFeeAssessedDays]"
								cssClass="fieldError"></form:errors> --%>NA</td>
								
						<td><form:input
								path="productAttributes[dormancyFee_capFeeAmt]" type="text"
								onkeypress="return allowNumbersWithDot(event);"
								onblur="return validMaintenanceCapFeeAmt(this);"
								name="dormancyFee_capFeeAmt" id="dormancyFee_capFeeAmt"
								style="width: -webkit-fill-available;" maxlength="10" readonly="true"/> <form:errors
								path="productAttributes[dormancyFee_capFeeAmt]"
								cssClass="fieldError"></form:errors></td>
								
						<td><form:input type="text"
								path="productAttributes[dormancyFee_freeCount]"
								name="dormancyFee_freeCount" id="dormancyFee_freeCount"
								style="width: -webkit-fill-available;"
								onkeypress="return isNumericfn(event);"
								onblur="return validateCount(this)" maxlength="6" readonly="true"
								/>
							<form:errors path="productAttributes[dormancyFee_freeCount]"
								cssClass="fieldError"></form:errors></td>

						<td><form:input type="text"
								path="productAttributes[dormancyFee_maxCount]"
								name="dormancyFee_maxCount" id="dormancyFee_maxCount"
								style="width: -webkit-fill-available;"
								onkeypress="return isNumericfn(event);"
								onblur="return validateCount(this)" maxlength="6" readonly="true"
								/>
							<form:errors path="productAttributes[dormancyFee_maxCount]"
								cssClass="fieldError"></form:errors></td>

						<td><form:checkbox
								path="productAttributes[dormancyFee_dormancyFeeCapAvail]"
								value="true" id="dormancyFee_dormancyFeeCapAvail"
								name="dormancyFee_dormancyFeeCapAvail" disabled="true"/> <form:errors
								path="productAttributes[dormancyFee_dormancyFeeCapAvail]"
								cssClass="fieldError"></form:errors></td>
					</tr>
					
				</tbody>
			</table>
		</div>


	</div>

</form:form>

<script>
	$("#maintenanceFeeCapTab").addClass("active");
	$("#maintenanceFeeCapTab").siblings().removeClass('active');
	enableProRation();
	
	$("input[id$='_clawback']").each(function (i, el) {
		enableClawbackOptions(el);
	});

	
	
</script>
