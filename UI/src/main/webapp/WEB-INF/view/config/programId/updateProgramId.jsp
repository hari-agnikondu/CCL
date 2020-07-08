<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>

<style>
.btn {
	margin-left: 15px;
}

input {
	width: -moz-available;
}

.tab-pane {
	width: -moz-fit-content;
	width: fit-content;
	border-top: 1px solid #a1a1a1;
	float: left;
}

input {
	width: 90%;
	padding: 0px 0px;
}

.boing {
	margin-left: 5px;
	color: red;
	visibility: hidden;
}

input[type='checkbox'] {
	border: 0px;
}

.nav-tabs {
	border-bottom: 0px;
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


<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/programID.js"></script>

<body class="dashboard" onload="onloadData();">

	<div class="body-container" style="min-height: 131px;">

		<div class="container">


			<form:form name="programIdForm" id="programIdForm" method="POST"
				class='form-horizontal' commandName="programIdForm">
				<%-- 	<input type="hidden" id="actionparam" name="actionparam"
					value="${getButtonsAndAction.createAction}"> --%>
				<div id="feedBackTd" class="form-group text-center"
					style="padding-top: 7px">
					<c:if test="${statusFlag=='success' }">
						<p class="successMsg"
							style="text-align: center; font-weight: bold;">
							<c:out value="${statusMessage }" />
						</p>
					</c:if>
					<c:if test="${statusFlag!='success' }">
						<p class="fieldError"
							style="text-align: center; font-weight: bold;">
							<c:if test="${showErrorMessage =='true' }">
								<table id=""
									class="table table-hover table-striped table-bordered"
									style="width: 100% !important;">

									<thead class="table-head">
										<th
											style="text-align: center; font-weight: bold; color: white"><spring:message
												code="programID.errorDescription" /></th>
										<th
											style="text-align: center; font-weight: bold; color: white"><spring:message
												code="programID.productName" /></th>

									</thead>


									<tbody>

										<c:forEach items="${statusMessage}" var="statMsg">
											<c:set var="statMsg" value="${fn:split(statMsg, ':')}" />
											<tr>
												<td class="dont-break-out"
													style="text-align: center; color: red"><c:out
														value="${statMsg[0]}" /></td>
												<td class="dont-break-out"
													style="text-align: center; color: red"><c:out
														value="${statMsg[1]}" /></td>

											</tr>
										</c:forEach>
									</tbody>
								</table>
							</c:if>

							<%-- <c:out value="${statusMessage }" /> --%>

						</p>
					</c:if>
				</div>

				<section class="content-container">
					<article class="col-lg-12">

						<ul class="nav nav-tabs   col-lg-11">
							<li class='active SubMenu'><a data-toggle='tab'><i
									class="glyphicon glyphicon-tags"></i> <spring:message
										code="programID.updateProgramID" /></a></li>
						</ul>

						<div class="nav nav-tabs   col-lg-11">
							<div class="tab-pane fade in active graybox col-lg-18"
								id="product">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<div class="">
									<c:if test="${successstatus !=''}">
										<div class="text-center  success-green padding"
											id="statusProgramIdAdd">
											<b>${successstatus}</b>
										</div>
									</c:if>
									<c:if test="${failstatus !=''}">

										<div class="text-center  error-red padding" id="formErrorId">
											<b>${failstatus}</b>
										</div>

									</c:if>


									<input type="hidden" id="srvUrl" value="${srvUrl}" />

									<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="ProgramIDName"> <spring:message
													code="program.programIDName" /><font color='red'>*</font></label>
										</div>
										<input id="progrmId" name="progrmId" type="hidden"
											value="${progrmId}" /> <input id="descId" name="descId"
											type="hidden" value="" /> <input id="LfId" name="LfId"
											type="hidden" value="" /> <input id="deliveyChnlId"
											name="deliveyChnlId" type="hidden" value="${deliveyChnlId}" />
										<input id="txnId" name="txnId" type="hidden" value="" /> <input
											type="hidden" name="delchn" id="delchn" value="" /> <input
											type="hidden" name="seltxn" id="seltxn" value="" /> <input
											type="hidden" name="progrmName" id="progrmName"
											value="${progrmName}" /> <input id="pursId" name="pursId"
											type="hidden" value="" /> <input type="hidden"
											name="selProgrmNameFlag" id="selProgrmNameFlag" value="" />
										<input type="hidden" name="selLimitFeesNameFlag"
											id="selLimitFeesNameFlag" value="" />
										<div class="col-lg-8">
											<form:select path="programIDName" id="programIdDropDownName"
												onchange="getDescription();getPurses();"
												class="dropdown-medium"
												onblur="return validateDropDownpartner(this.form.id,this.id);">
												<form:option value="-1" label="--- Select ---" />
												<c:forEach items="${programIDList}" var="progId">
													<option value="${progId.programID}~${progId.programIDName}"
														<c:if test="${progId.programIDName eq programIdForm.programIDName}">selected="true"</c:if>>${progId.programIDName}
													</option>
												</c:forEach>
											</form:select>
											<div>
												<form:errors path="programID" id="programIdDropDownName"
													cssStyle="color:red" />
											</div>
										</div>
									</div>

									<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="Description"> <spring:message
													code="programID.description" /><font color='red'></font></label>
										</div>
										<div class="col-lg-8">
											<form:textarea path="description" id="programDesc"
												class="trim" type="textarea" readonly="true"
												onblur="validateFields(this.form.id,this.id)"
												maxLength="255" rows="5" cols="51" style="resize:none" />
											<div>
												<form:errors path="description" id="programDesc"
													cssStyle="color:red" />
											</div>
										</div>
									</div>

									<br>&nbsp;

									<div class="col-lg-12">

										<div class="col-lg-4">
											<label for="limitsFees"> <spring:message
													code="programID.LimitFee" /><font color='red'>*</font></label>
										</div>

										<div class="col-lg-8">

											<form:select path="limitsFees" name="limitsFees"
												id="limitsFeesId" class="dropdown-medium"
												onblur="return validateDropDownpartner(this.form.id,this.id);">
												<form:option value="-1" label="--- Select ---" />
												<form:option value="Limits">
													<spring:message code="programID.Limit" />
												</form:option>
												<form:option value="Transaction Fees">
													<spring:message code="programID.TransactionFee" />
												</form:option>
												<form:option value="Maintenance Fees">
													<spring:message code="programID.MaintenanceFee" />
												</form:option>
												<form:option value="Monthly Fee Cap">
													<spring:message code="programID.MonthlyFeeCap" />
												</form:option>
											</form:select>
										</div>
									</div>

									<div class="col-lg-12">

										<div class="col-lg-4">
											<label for="purseList"> <spring:message
													code="programID.purse" /> <c:set var="limitsFeesVar"
													value="${param.limitsFees}" /> <c:if
													test="${limitsFeesVar ne 'Maintenance Fees'}">
													<font color='red'>*</font>
												</c:if>

											</label>
										</div>

										<div class="col-lg-8">

											<form:select path="purseList" name="purseList"
												id="purseListId" class="dropdown-medium"
												onblur="return validateDropDownPurse(this.form.id,this.id,${limitsFeesVar});">
												 <form:option value="-1" label="--- Select ---" />
												<c:forEach items="${programPartnerPursesList}" var="purse">
													<form:option value="${purse.purseId}">${purse.extPurseId}
													</form:option>
												</c:forEach> 
											</form:select>
										</div>
									</div>


									<div class="col-lg-12">

										<div class="col-lg-4">
											<label for="deliverychannelList"> <spring:message
													code="programID.deliveryChannel" /><font color='red'>*</font></label>
										</div>

										<div class="col-lg-8">

											<form:select path="deliveryChannelList"
												name="deliveryChannelList" id="deliveryChannelListId"
												onchange="getTxndata();" class="dropdown-medium"
												onblur="return validateDropDownpartner(this.form.id,this.id);">
												<form:option value="-1" label="--- Select ---" />
												<c:forEach items="${deliverChannelList}"
													var="deliveryChannel">
													<form:option
														value="${deliveryChannel.deliveryChnlShortName}">${deliveryChannel.deliveryChnlName}
									</form:option>
												</c:forEach>
											</form:select>
										</div>
									</div>


									<div class="col-lg-12">

										<div class="col-lg-4">
											<label for="transactionList"> <spring:message
													code="programID.transaction" /><font color='red'>*</font></label>
										</div>

										<div class="col-lg-8">

											<form:select path="transaction" name="transaction"
												id="transactionListId" class="dropdown-medium"
												onchange="goTransactionDetails(this.form.id);">
												<form:option value="-1" label="--- Select ---" />
												<c:forEach items="${txnMap}" var="txmap">
													<form:option value="${txmap.key}">${txmap.value}
									</form:option>
												</c:forEach>

											</form:select>
										</div>
									</div>




									<c:if test="${showLimits =='true' }">

										<div class="col-lg-12" id="tableViewUsers">

											<table id="tableViewUsersPrgm"
												class="table table-hover table-striped table-bordered"
												style="table-layout: fixed; width: 1300px">
												<colgroup span="5">
													<col style="width: 175px;">
													<col style="width: 110px;">

													<col style="width: 110px;">

												</colgroup>
												<colgroup span="4">
													<col style="width: 65px;">

													<col style="width: 110px;">

												</colgroup>
												<colgroup span="4">
													<col style="width: 65px;">

													<col style="width: 110px;">

												</colgroup>
												<colgroup span="4">
													<col style="width: 65px;">

													<col style="width: 110px;">

												</colgroup>
												<colgroup span="4">
													<col style="width: 65px;">

													<col style="width: 110px;">

												</colgroup>
												<thead class="table-head">
													<tr>
														<th colspan="3" scope="colgroup" style=""></th>
														<th colspan="2" scope="colgroup"
															style="text-align: center;"><spring:message
																code="product.limit.daily" text="Daily" /></th>
														<th colspan="2" scope="colgroup"
															style="text-align: center;"><spring:message
																code="product.limit.weekly" text="Weekly" /></th>
														<th colspan="2" scope="colgroup"
															style="text-align: center;"><spring:message
																code="product.limit.monthly" text="Monthly" /></th>
														<th colspan="2" scope="colgroup"
															style="text-align: center;"><spring:message
																code="product.limit.yearly" text="Yearly" /></th>
													</tr>
													<tr>
														<th><spring:message code="product.limit.transactions"
																text="Transactions" /></th>
														<th><spring:message code="product.limit.minAmtPerTxn"
																text="Min Amt per TX" /></th>

														<th><spring:message code="product.limit.maxAmtPerTxn"
																text="Max Amt per Txn" /></th>

														<th><spring:message code="product.limit.maxCount"
																text="Max Count" /></th>

														<th><spring:message code="product.limit.maxAmt"
																text="Max Amt" /></th>

														<th><spring:message code="product.limit.maxCount"
																text="Max Count" /></th>

														<th><spring:message code="product.limit.maxAmt"
																text="Max Amt" /></th>

														<th><spring:message code="product.limit.maxCount"
																text="Max Count" /></th>

														<th><spring:message code="product.limit.maxAmt"
																text="Max Amt" /></th>

														<th><spring:message code="product.limit.maxCount"
																text="Max Count" /></th>

														<th><spring:message code="product.limit.maxAmt"
																text="Max Amt" /></th>

													</tr>
												</thead>
												<tbody>
													<c:forEach items="${transactionFlagForChnlName}"
														var="txnShortName">

														<c:set var="txnShortNamevalue"
															value="${txnShortName.value}" />
													</c:forEach>


													<c:forEach items="${txnMap2}" var="txn">
														<tr id="${delChnlkeyval}">
															<td>${txn.value }<input type="hidden"
																name="${delChnlkeyval}" id="${delChnlkeyval}_Flag"
																value="${txnShortNamevalue}" /></td>
															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_minAmtPerTx]"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="DecimalValueFormat(this); "
																	name="${delChnlkeyval}_minAmtPerTx"
																	id="${delChnlkeyval}_minAmtPerTx"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors
																	path="productAttributes[${delChnlkeyval}_minAmtPerTx]"
																	cssClass="fieldError"></form:errors></td>
															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_maxAmtPerTx]"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="DecimalValueFormat(this); "
																	name="${delChnlkeyval}_maxAmtPerTx"
																	id="${delChnlkeyval}_maxAmtPerTx" maxlength="10"
																	style="width: -webkit-fill-available;" /> <form:errors
																	path="productAttributes[delChnlkeyval_maxAmtPerTx]"
																	cssClass="fieldError"></form:errors></td>



															<td><form:input
																	path="productAttributes[${delChnlkeyval}_dailyMaxCount]"
																	type="text" onkeypress="return isNumericfn(event);"
																	onblur="return isNumericfn(event);"
																	name="${delChnlkeyval}_dailyMaxCount"
																	id="${delChnlkeyval}_dailyMaxCount"
																	style="width: -webkit-fill-available;" maxlength="4" />
																<form:errors
																	path="productAttributes[${delChnlkeyval}_dailyMaxCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_dailyMaxAmt]"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="DecimalValueFormat(this);"
																	name="${delChnlkeyval}_dailyMaxAmt"
																	id="${delChnlkeyval}_dailyMaxAmt"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors
																	path="productAttributes[delChnlkeyval}_dailyMaxAmt]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_weeklyMaxCount]"
																	onkeypress="return isNumericfn(event);"
																	onblur="return isNumericfn(event);"
																	name="${delChnlkeyval}_weeklyMaxCount"
																	id="${delChnlkeyval}_weeklyMaxCount"
																	style="width: -webkit-fill-available;" maxlength="4" />
																<form:errors
																	path="productAttributes[${delChnlkeyval}_weeklyMaxCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_weeklyMaxAmt]"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="DecimalValueFormat(this);"
																	name="${delChnlkeyval}_weeklyMaxAmt"
																	id="${delChnlkeyval}_weeklyMaxAmt"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors
																	path="productAttributes[${delChnlkeyval}_weeklyMaxAmt]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_monthlyMaxCount]"
																	name="${delChnlkeyval}_monthlyMaxCount"
																	id="${delChnlkeyval}_monthlyMaxCount"
																	onkeypress="return isNumericfn(event);"
																	onblur="return isNumericfn(event);"
																	style="width: -webkit-fill-available;" maxlength="4" />
																<form:errors
																	path="productAttributes[${delChnlkeyval}_monthlyMaxCount]"
																	cssClass="fieldError"></form:errors></td>


															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_monthlyMaxAmt]"
																	id="${delChnlkeyval}_monthlyMaxAmt"
																	name="${delChnlkeyval}_monthlyMaxAmt"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="DecimalValueFormat(this);"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors
																	path="productAttributes[${delChnlkeyval}_monthlyMaxAmt]"
																	cssClass="fieldError"></form:errors></td>


															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_yearlyMaxCount]"
																	id="${delChnlkeyval}_yearlyMaxCount"
																	name="${delChnlkeyval}_yearlyMaxCount"
																	onkeypress="return isNumericfn(event);"
																	onblur="isNumericfn(event);"
																	style="width: -webkit-fill-available;" maxlength="4" />
																<form:errors
																	path="productAttributes[${delChnlkeyval}_yearlyMaxCount]"
																	cssClass="fieldError"></form:errors></td>



															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_yearlyMaxAmt]"
																	id="${delChnlkeyval}_yearlyMaxAmt"
																	name="${delChnlkeyval}_yearlyMaxAmt"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="DecimalValueFormat(this);"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors
																	path="productAttributes[${delChnlkeyval}_yearlyMaxAmt]"
																	cssClass="fieldError"></form:errors></td>




														</tr>
													</c:forEach>





												</tbody>

											</table>

										</div>


									</c:if>



									<!-- txn fee starts -->

									<c:if test="${showTxnFee =='true' }">

										<div class="tabresp tab-content" style="width: fit-content">


											<table id="tableViewUsers"
												class="table table-hover table-striped table-bordered"
												style="table-layout: fixed;">

												<thead class="table-head">

													<tr>
														<th style="width: 175px"><spring:message
																code="product.limit.transactions" text="Transactions" /></th>
														<th style="width: 175px"><spring:message
																code="product.fees.desc" text="Fee Description" /></th>
														<th style="width: 65px"><spring:message
																code="product.fees.clawBack" text="Clawback" /></th>
														<th style="width: 65px;"><spring:message
																code="product.fees.clawbackCnt" text="Clawback Count" /></th>
														<th style="width: 110px"><spring:message
																code="product.fees.feeAmt" text="Fee Amt" /></th>
														<th style="width: 70px"><spring:message
																code="product.fees.feeCondition" text="Fee Condition" /></th>
														<th style="width: 90px"><spring:message
																code="product.fees.feePercentage" text="Fee %" /></th>
														<th style="width: 110px"><spring:message
																code="product.fees.minFeeAmt" text="Min Fee Amt" /></th>
														<th style="width: 65px;"><spring:message
																code="product.fees.freeCnt" text="Free Count" /></th>
														<th style="width: 110px"><spring:message
																code="product.fees.freeCntFreq"
																text="Free Count Frequency" /></th>
														<th style="width: 65px;"><spring:message
																code="product.fees.maxCnt" text="Max Count" /></th>
														<th style="width: 110px"><spring:message
																code="product.fees.maxCntFreq"
																text="Max Count Frequency" /></th>
														<th style="width: 110px"><spring:message
																code="product.fees.monthlyFeeCapAvl"
																text="Monthly Fee Cap Available" /></th>
													</tr>
												</thead>
												<tbody>

													<c:forEach items="${transactionFlagForChnlName}"
														var="txnShortName">

														<c:set var="txnShortNamevalue"
															value="${txnShortName.value}" />
													</c:forEach>


													<c:forEach items="${txnMap2}" var="txn">
														<tr id="${delChnlkeyval}">
															<td>${txn.value }<input type="hidden"
																name="${delChnlkeyval}" id="${delChnlkeyval}_Flag"
																value="${txnShortNamevalue}" /></td>

															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_feeDesc]"
																	name="$delChnlkeyval}_feeDesc"
																	id="${delChnlkeyval}_feeDesc"
																	style="width: -webkit-fill-available;" maxLength="100"
																	onblur="validateFeeDescription(this)" /> <form:errors
																	path="productAttributes[${delChnlkeyval}_feeDesc]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:checkbox
																	path="productAttributes[${delChnlkeyval}_clawback]"
																	value="true" id="${delChnlkeyval}_clawback"
																	name="${delChnlkeyval}_clawback"
																	onchange="enableClawbackCount(this)" /> <form:errors
																	path="productAttributes[${delChnlkeyval}_${txn.key }_clawback]"
																	cssClass="fieldError"></form:errors></td>


															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_clawbackCount]"
																	name="${delChnlkeyval}_clawbackCount"
																	id="${delChnlkeyval}_clawbackCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="validateClawbackCount(this)" maxlength="6"
																	readonly="true" /> <form:errors
																	path="productAttributes[${delChnlkeyval}_clawbackCount]"
																	cssClass="fieldError"></form:errors></td>


															<td><form:input
																	path="productAttributes[${delChnlkeyval}_feeAmt]"
																	type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="validateFeeAmt(this);"
																	name="${delChnlkeyval}_feeAmt"
																	id="${delChnlkeyval}_feeAmt"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors
																	path="productAttributes[${delChnlkeyval}_feeAmt]"
																	cssClass="fieldError"></form:errors></td>



															<td><form:select
																	path="productAttributes[${delChnlkeyval}_feeCondition]"
																	name="${delChnlkeyval}_feeCondition"
																	id="${delChnlkeyval}_feeCondition"
																	style="width:60px;width: -webkit-fill-available;"
																	onchange="callFeeConditionChange(this)">
																	<form:option value="N">NA</form:option>
																	<form:option value="O">OR</form:option>
																	<form:option value="A">AND</form:option>
																</form:select> <form:errors
																	path="productAttributes[${delChnlkeyval}_feeCondition]"
																	cssClass="fieldError"></form:errors></td>


															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_feePercent]"
																	onkeypress="return allowNumbersWithDot(event)"
																	name="${delChnlkeyval}_feePercent"
																	id="${delChnlkeyval}_feePercent"
																	style="width: -webkit-fill-available;" maxlength="6"
																	onblur="enableMinFee(this); " readonly="true" /> <form:errors
																	path="productAttributes[${delChnlkeyval}_feePercent]"
																	cssClass="fieldError"></form:errors></td>




															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_minFeeAmt]"
																	onkeypress="return allowNumbersWithDot(event);"
																	name="${delChnlkeyval}_minFeeAmt"
																	id="${delChnlkeyval}_minFeeAmt"
																	style="width: -webkit-fill-available;" maxlength="10"
																	onblur="return validateMinFeeAmt(this)" readonly="true" />
																<form:errors
																	path="productAttributes[${delChnlkeyval}_minFeeAmt]"
																	cssClass="fieldError"></form:errors></td>



															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_freeCount]"
																	id="${delChnlkeyval}_freeCount"
																	name="${delChnlkeyval}_freeCount"
																	onkeypress="return isNumericfn(event);;"
																	onblur="enableFrequency(this)"
																	style="width: -webkit-fill-available;" maxlength="6" />
																<form:errors
																	path="productAttributes[${delChnlkeyval}_freeCount]"
																	cssClass="fieldError"></form:errors></td>


															<td><form:select type="text"
																	path="productAttributes[${delChnlkeyval}_freeCountFreq]"
																	name="${delChnlkeyval}_freeCountFreq"
																	id="${delChnlkeyval}_freeCountFreq"
																	style="width:100px;width: -webkit-fill-available;"
																	disabled="true">
																	<form:option value="D">Daily</form:option>
																	<form:option value="W">Weekly</form:option>
																	<form:option value="BW">Bi-weekly</form:option>
																	<form:option value="BM">Fortnightly</form:option>
																	<form:option value="M">Monthly</form:option>
																	<form:option value="Y">Yearly</form:option>
																</form:select> <form:errors
																	path="productAttributes[${delChnlkeyval}_freeCountFreq]"
																	cssClass="fieldError"></form:errors></td>


															<td><form:input type="text"
																	path="productAttributes[${delChnlkeyval}_maxCount]"
																	id="${delChnlkeyval}_maxCount"
																	name="${delChnlkeyval}_maxCount"
																	onkeypress="return isNumericfn(event);;"
																	style="width: -webkit-fill-available;" maxlength="6"
																	onblur="enableFrequency(this)" /> <form:errors
																	path="productAttributes[${delChnlkeyval}_maxCount]"
																	cssClass="fieldError"></form:errors></td>




															<td><form:select
																	path="productAttributes[${delChnlkeyval}_maxCountFreq]"
																	name="${delChnlkeyval}_maxCountFreq"
																	id="${delChnlkeyval}_maxCountFreq"
																	style="width:100px;width: -webkit-fill-available;"
																	disabled="true">
																	<form:option value="D">Daily</form:option>
																	<form:option value="W">Weekly</form:option>
																	<form:option value="BW">Bi-weekly</form:option>
																	<form:option value="BM">Fortnightly</form:option>
																	<form:option value="M">Monthly</form:option>
																	<form:option value="Y">Yearly</form:option>
																</form:select> <form:errors
																	path="productAttributes[${delChnlkeyval}_maxCountFreq]"
																	cssClass="fieldError"></form:errors></td>



															<td><form:checkbox
																	path="productAttributes[${delChnlkeyval}_monthlyFeeCapAvail]"
																	value="true" id="${delChnlkeyval}_monthlyFeeCapAvail"
																	name="${delChnlkeyval}_monthlyFeeCapAvail" /> <form:errors
																	path="productAttributes[${delChnlkeyval}_monthlyFeeCapAvail]"
																	cssClass="fieldError"></form:errors></td>




														</tr>
													</c:forEach>
												</tbody>
											</table>


										</div>

									</c:if>

									<!-- txn fee ends -->
									<c:if test="${showMntFee =='true' }">
										<div class="col-lg-12">


											<table id="tableViewUsers"
												class="table table-hover table-striped table-bordered"
												style="width: 1300px; table-layout: fixed;">

												<thead class="table-head">

													<tr>
														<th style="width: 175px"><spring:message code=""
																text="" /></th>
														<th style="width: 175px"><spring:message
																code="product.fees.desc" text="Fee Description" /></th>
														<th style="width: 175px;"><spring:message
																code="product.monthlyFeeCap.assessmentDate"
																text="Assessment Date" /></th>
														<th style="width: 65px"><spring:message
																code="product.maintenanceFee.proration" text="ProRation" /></th>
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
													<c:if test="${showMonthlyFee =='true' }">
														<tr>
															<td><spring:message code="product.monthlyFee.label"
																	text="Monthly Fee" /></td>

															<td><form:input type="text"
																	path="productAttributes[monthlyFee_feeDesc]"
																	name="monthlyFee_feeDesc" id="monthlyFee_feeDesc"
																	style="width: -webkit-fill-available;" maxLength="100"
																	onblur="return validateFeeDescription(this)" /> <form:errors
																	path="productAttributes[monthlyFee_feeDesc]"
																	cssClass="fieldError"></form:errors></td>
															<td><form:select
																	style="width:160px; width: -webkit-fill-available;"
																	path="productAttributes[monthlyFee_assessmentDate]"
																	name="monthlyFee_assessmentDate"
																	id="monthlyFee_assessmentDate"
																	onchange="enableProRation()">

																	<form:option value="AD">Anniversary Date of Account Activation</form:option>
																	<form:option value="FD">First Calendar Date of Each Month</form:option>
																	<form:option value="AL">Anniversary Date of First Load</form:option>
																	<form:option value="FLI">Funding Date of Account without Activation</form:option>


																</form:select> <form:errors
																	path="productAttributes[monthlyFee_assessmentDate]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:checkbox
																	path="productAttributes[monthlyFee_proRation]"
																	value="true" id="monthlyFee_proRation"
																	style="margin-left:15px" name="monthlyFee_proRation" />
																<form:errors
																	path="productAttributes[monthlyFee_proRation]"
																	cssClass="fieldError"></form:errors></td>
															<td><form:input
																	path="productAttributes[monthlyFee_feeAmt]" type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="validateFeeAmt(this);" name="monthlyFee_feeAmt"
																	id="monthlyFee_feeAmt"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors path="productAttributes[monthlyFee_feeAmt]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:checkbox
																	path="productAttributes[monthlyFee_clawback]"
																	value="true" id="monthlyFee_clawback"
																	name="monthlyFee_clawback"
																	onchange="enableClawbackOptions(this)" /> <form:errors
																	path="productAttributes[monthlyFee_clawback]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[monthlyFee_clawbackCount]"
																	name="monthlyFee_clawbackCount"
																	id="monthlyFee_clawbackCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="return validateClawbackCount(this)"
																	maxlength="6" readonly="true" /> <form:errors
																	path="productAttributes[monthlyFee_clawbackCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:select
																	path="productAttributes[monthlyFee_clawbackOption]"
																	name="monthlyFee_clawbackOption"
																	id="monthlyFee_clawbackOption"
																	style="width:60px;width: -webkit-fill-available;"
																	onchange="return  clawbackOptionChange(this)"
																	disabled="true">

																	<form:option value="O">OR</form:option>
																	<form:option value="A">AND</form:option>
																	<form:option value="N">NA</form:option>
																</form:select> <form:errors
																	path="productAttributes[monthlyFee_clawbackOption]"
																	cssClass="fieldError"></form:errors></td>
															<td><form:input
																	path="productAttributes[monthlyFee_clawbackMaxAmt]"
																	type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="return validateClabackMaxFeeAmt(this);"
																	name="monthlyFee_clawbackMaxAmt"
																	id="monthlyFee_clawbackMaxAmt"
																	style="width: -webkit-fill-available;" maxlength="10"
																	readonly="true" /> <form:errors
																	path="productAttributes[monthlyFee_clawbackMaxAmt]"
																	cssClass="fieldError"></form:errors></td>
															<td><form:input
																	path="productAttributes[monthlyFee_firstMonthFeeAssessedDays]"
																	type="text" onkeypress="return isNumericfn(event);"
																	onblur="return validateCount(this)"
																	name="monthlyFee_firstMonthFeeAssessedDays"
																	id="monthlyFee_firstMonthFeeAssessedDays"
																	style="width: -webkit-fill-available;" maxlength="3" />
																<form:errors
																	path="productAttributes[monthlyFee_firstMonthFeeAssessedDays]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input
																	path="productAttributes[monthlyFee_capFeeAmt]"
																	type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="return validMaintenanceCapFeeAmt(this);"
																	name="monthlyFee_capFeeAmt" id="monthlyFee_capFeeAmt"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors
																	path="productAttributes[monthlyFee_capFeeAmt]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[monthlyFee_freeCount]"
																	name="monthlyFee_freeCount" id="monthlyFee_freeCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="return validateCount(this)" maxlength="6" /> <form:errors
																	path="productAttributes[monthlyFee_freeCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[monthlyFee_maxCount]"
																	name="monthlyFee_maxCount" id="monthlyFee_maxCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="return validateCount(this)" maxlength="6" /> <form:errors
																	path="productAttributes[monthlyFee_maxCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:checkbox
																	path="productAttributes[monthlyFee_monthlyFeeCapAvail]"
																	value="true" id="monthlyFee_monthlyFeeCapAvail"
																	name="monthlyFee_monthlyFeeCapAvail" /> <form:errors
																	path="productAttributes[monthlyFee_monthlyFeeCapAvail]"
																	cssClass="fieldError"></form:errors></td>

														</tr>
													</c:if>
													<c:if test="${showWeeklyFee =='true' }">
														<tr>
															<td><spring:message code="product.weeklyFee.label"
																	text="Weekly Fee" /></td>
															<td><form:input type="text"
																	path="productAttributes[weeklyFee_feeDesc]"
																	name="weeklyFee_feeDesc" id="weeklyFee_feeDesc"
																	style="width: -webkit-fill-available;" maxLength="100"
																	onblur="return validateFeeDescription(this)" /> <form:errors
																	path="productAttributes[weeklyFee_feeDesc]"
																	cssClass="fieldError"></form:errors></td>
															<td style="text-align: center;">NA</td>

															<td style="text-align: center;">NA</td>
															<td><form:input
																	path="productAttributes[weeklyFee_feeAmt]" type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="return validateFeeAmt(this);"
																	name="weeklyFee_feeAmt" id="weeklyFee_feeAmt"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors path="productAttributes[weeklyFee_feeAmt]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:checkbox
																	path="productAttributes[weeklyFee_clawback]"
																	value="true" id="weeklyFee_clawback"
																	name="weeklyFee_clawback"
																	onchange="return enableClawbackOptions(this)" /> <form:errors
																	path="productAttributes[weeklyFee_clawback]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[weeklyFee_clawbackCount]"
																	name="weeklyFee_clawbackCount"
																	id="weeklyFee_clawbackCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="return validateClawbackCount(this)"
																	maxlength="6" readonly="true" /> <form:errors
																	path="productAttributes[weeklyFee_clawbackCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:select
																	path="productAttributes[weeklyFee_clawbackOption]"
																	name="weeklyFee_clawbackOption"
																	id="weeklyFee_clawbackOption"
																	style="width:60px;width: -webkit-fill-available;"
																	onchange="return clawbackOptionChange(this)"
																	disabled="true">

																	<form:option value="O">OR</form:option>
																	<form:option value="A">AND</form:option>
																	<form:option value="N">NA</form:option>

																</form:select> <form:errors
																	path="productAttributes[weeklyFee_clawbackOption]"
																	cssClass="fieldError"></form:errors></td>
															<td><form:input
																	path="productAttributes[weeklyFee_clawbackMaxAmt]"
																	type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="return validateClabackMaxFeeAmt(this);"
																	name="weeklyFee_clawbackMaxAmt"
																	id="weeklyFee_clawbackMaxAmt"
																	style="width: -webkit-fill-available;" maxlength="10"
																	readonly="true" /> <form:errors
																	path="productAttributes[weeklyFee_clawbackMaxAmt]"
																	cssClass="fieldError"></form:errors></td>
															<td><form:input
																	path="productAttributes[weeklyFee_firstMonthFeeAssessedDays]"
																	type="text" onkeypress="return isNumericfn(event);"
																	onblur="return validateCount(this)"
																	name="weeklyFee_firstMonthFeeAssessedDays"
																	id="weeklyFee_firstMonthFeeAssessedDays"
																	style="width: -webkit-fill-available;" maxlength="3" />
																<form:errors
																	path="productAttributes[weeklyFee_firstMonthFeeAssessedDays]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input
																	path="productAttributes[weeklyFee_capFeeAmt]"
																	type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="return validMaintenanceCapFeeAmt(this);"
																	name="weeklyFee_capFeeAmt" id="weeklyFee_capFeeAmt"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors
																	path="productAttributes[weeklyFee_capFeeAmt]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[weeklyFee_freeCount]"
																	name="weeklyFee_freeCount" id="weeklyFee_freeCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="return validateCount(this)" maxlength="6" /> <form:errors
																	path="productAttributes[weeklyFee_freeCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[weeklyFee_maxCount]"
																	name="weeklyFee_maxCount" id="weeklyFee_maxCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="return validateCount(this)" maxlength="6" /> <form:errors
																	path="productAttributes[weeklyFee_maxCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:checkbox
																	path="productAttributes[weeklyFee_weeklyFeeCapAvail]"
																	value="true" id="weeklyFee_weeklyFeeCapAvail"
																	name="weeklyFee_weeklyFeeCapAvail" /> <form:errors
																	path="productAttributes[weeklyFee_weeklyFeeCapAvail]"
																	cssClass="fieldError"></form:errors></td>
														</tr>

													</c:if>
													<c:if test="${showAnnualFee =='true' }">
														<tr>
															<td><spring:message code="product.annualFee.label"
																	text="Annual Fee" /></td>
															<td><form:input type="text"
																	path="productAttributes[yearlyFee_feeDesc]"
																	name="yearlyFee_feeDesc" id="yearlyFee_feeDesc"
																	style="width: -webkit-fill-available;" maxLength="100"
																	onblur="return validateFeeDescription(this)" /> <form:errors
																	path="productAttributes[yearlyFee_feeDesc]"
																	cssClass="fieldError"></form:errors></td>
															<td style="text-align: center;">NA</td>
															<td style="text-align: center;">NA</td>
															<td><form:input
																	path="productAttributes[yearlyFee_feeAmt]" type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="return validateFeeAmt(this);"
																	name="yearlyFee_feeAmt" id="yearlyFee_feeAmt"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors path="productAttributes[yearlyFee_feeAmt]"
																	cssClass="fieldError"></form:errors></td>
															<td><form:checkbox
																	path="productAttributes[yearlyFee_clawback]"
																	value="true" id="yearlyFee_clawback"
																	name="yearlyFee_clawback"
																	onchange="return enableClawbackOptions(this)" /> <form:errors
																	path="productAttributes[yearlyFee_clawback]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[yearlyFee_clawbackCount]"
																	name="yearlyFee_clawbackCount"
																	id="yearlyFee_clawbackCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="return validateClawbackCount(this)"
																	maxlength="6" readonly="true" /> <form:errors
																	path="productAttributes[yearlyFee_clawbackCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:select
																	path="productAttributes[yearlyFee_clawbackOption]"
																	name="yearlyFee_clawbackOption"
																	id="yearlyFee_clawbackOption"
																	style="width:60px;width: -webkit-fill-available;"
																	onchange="clawbackOptionChange(this)" disabled="true">

																	<form:option value="O">OR</form:option>
																	<form:option value="A">AND</form:option>
																	<form:option value="N">NA</form:option>

																</form:select> <form:errors
																	path="productAttributes[yearlyFee_clawbackOption]"
																	cssClass="fieldError"></form:errors></td>
															<td><form:input
																	path="productAttributes[yearlyFee_clawbackMaxAmt]"
																	type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="return validateClabackMaxFeeAmt(this)"
																	name="yearlyFee_clawbackMaxAmt"
																	id="yearlyFee_clawbackMaxAmt"
																	style="width: -webkit-fill-available;" maxlength="10"
																	readonly="true" /> <form:errors
																	path="productAttributes[yearlyFee_clawbackMaxAmt]"
																	cssClass="fieldError"></form:errors></td>
															<td><form:input
																	path="productAttributes[yearlyFee_firstMonthFeeAssessedDays]"
																	type="text" onkeypress="return isNumericfn(event);"
																	onblur="return validateCount(this)"
																	name="yearlyFee_firstMonthFeeAssessedDays"
																	id="yearlyFee_firstMonthFeeAssessedDays"
																	style="width: -webkit-fill-available;" maxlength="3" />
																<form:errors
																	path="productAttributes[yearlyFee_firstMonthFeeAssessedDays]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input
																	path="productAttributes[yearlyFee_capFeeAmt]"
																	type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="return validMaintenanceCapFeeAmt(this);"
																	name="yearlyFee_capFeeAmt" id="yearlyFee_capFeeAmt"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors
																	path="productAttributes[yearlyFee_capFeeAmt]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[yearlyFee_freeCount]"
																	name="yearlyFee_freeCount" id="yearlyFee_freeCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="return validateCount(this)" maxlength="6" /> <form:errors
																	path="productAttributes[yearlyFee_freeCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[yearlyFee_maxCount]"
																	name="yearlyFee_maxCount" id="yearlyFee_maxCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="return validateCount(this)" maxlength="6" /> <form:errors
																	path="productAttributes[yearlyFee_maxCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:checkbox
																	path="productAttributes[yearlyFee_yearlyFeeCapAvail]"
																	value="true" id="yearlyFee_yearlyFeeCapAvail"
																	name="yearlyFee_yearlyFeeCapAvail" /> <form:errors
																	path="productAttributes[yearlyFee_yearlyFeeCapAvail]"
																	cssClass="fieldError"></form:errors></td>
														</tr>

													</c:if>

													<c:if test="${showDormancyFee =='true' }">
														<tr>
															<td><spring:message code="product.dormancyFee.label"
																	text="Dormancy Fee" /></td>

															<td><form:input type="text"
																	path="productAttributes[dormancyFee_feeDesc]"
																	name="dormancyFee_feeDesc" id="dormancyFee_feeDesc"
																	style="width: -webkit-fill-available;" maxLength="100"
																	onblur="return validateFeeDescription(this)" /> <form:errors
																	path="productAttributes[dormancyFee_feeDesc]"
																	cssClass="fieldError"></form:errors></td>
															<td style="text-align: center;">NA</td>
															<td style="text-align: center;">NA</td>

															<td><form:input
																	path="productAttributes[dormancyFee_feeAmt]"
																	type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="return validateFeeAmt(this);"
																	name="dormancyFee_feeAmt" id="dormancyFee_feeAmt"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors
																	path="productAttributes[dormancyFee_feeAmt]"
																	cssClass="fieldError"></form:errors></td>
															<td><form:checkbox
																	path="productAttributes[dormancyFee_clawback]"
																	value="true" id="dormancyFee_clawback"
																	name="dormancyFee_clawback"
																	onchange="return enableClawbackOptions(this)" /> <form:errors
																	path="productAttributes[dormancyFee_clawback]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[dormancyFee_clawbackCount]"
																	name="dormancyFee_clawbackCount"
																	id="dormancyFee_clawbackCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="return validateClawbackCount(this)"
																	maxlength="6" readonly="true" /> <form:errors
																	path="productAttributes[dormancyFee_clawbackCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:select
																	path="productAttributes[dormancyFee_clawbackOption]"
																	name="dormancyFee_clawbackOption"
																	id="dormancyFee_clawbackOption"
																	style="width:60px;width: -webkit-fill-available;"
																	onchange="clawbackOptionChange(this)" disabled="true">

																	<form:option value="O">OR</form:option>
																	<form:option value="A">AND</form:option>
																	<form:option value="N">NA</form:option>


																</form:select> <form:errors
																	path="productAttributes[dormancyFee_clawbackOption]"
																	cssClass="fieldError"></form:errors></td>
															<td><form:input
																	path="productAttributes[dormancyFee_clawbackMaxAmt]"
																	type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="return validateClabackMaxFeeAmt(this);"
																	name="dormancyFee_clawbackMaxAmt"
																	id="dormancyFee_clawbackMaxAmt"
																	style="width: -webkit-fill-available;" maxlength="10"
																	readonly="true" /> <form:errors
																	path="productAttributes[dormancyFee_clawbackMaxAmt]"
																	cssClass="fieldError"></form:errors></td>
															<td style="text-align: center;">NA</td>

															<td><form:input
																	path="productAttributes[dormancyFee_capFeeAmt]"
																	type="text"
																	onkeypress="return allowNumbersWithDot(event);"
																	onblur="return validMaintenanceCapFeeAmt(this);"
																	name="dormancyFee_capFeeAmt" id="dormancyFee_capFeeAmt"
																	style="width: -webkit-fill-available;" maxlength="10" />
																<form:errors
																	path="productAttributes[dormancyFee_capFeeAmt]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[dormancyFee_freeCount]"
																	name="dormancyFee_freeCount" id="dormancyFee_freeCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="return validateCount(this)" maxlength="6" /> <form:errors
																	path="productAttributes[dormancyFee_freeCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:input type="text"
																	path="productAttributes[dormancyFee_maxCount]"
																	name="dormancyFee_maxCount" id="dormancyFee_maxCount"
																	style="width: -webkit-fill-available;"
																	onkeypress="return isNumericfn(event);"
																	onblur="return validateCount(this)" maxlength="6" /> <form:errors
																	path="productAttributes[dormancyFee_maxCount]"
																	cssClass="fieldError"></form:errors></td>

															<td><form:checkbox
																	path="productAttributes[dormancyFee_dormancyFeeCapAvail]"
																	value="true" id="dormancyFee_dormancyFeeCapAvail"
																	name="dormancyFee_dormancyFeeCapAvail" /> <form:errors
																	path="productAttributes[dormancyFee_dormancyFeeCapAvail]"
																	cssClass="fieldError"></form:errors></td>
														</tr>
													</c:if>
												</tbody>
											</table>
										</div>

									</c:if>


									<c:if test="${ShowMonthlyFeeCap =='true' }">

										<div class="col-lg-12">


											<table id="tableViewUsers"
												class="table table-hover table-striped table-bordered"
												style="width: 1300px; table-layout: fixed;">

												<thead class="table-head">

													<tr>
														<th style="width: 175px"><spring:message code=""
																text="" /></th>
														<th style="width: 175px"><spring:message
																code="product.fees.desc" text="Fee Description" /></th>
														<th style="width: 175px"><spring:message
																code="product.monthlyFeeCap.timePeriod"
																text="Time Period" /></th>

														<th style="width: 65px;"><spring:message
																code="product.monthlyFeeCap.assessmentDate"
																text="Assessment Date" /></th>

														<th style="width: 110px"><spring:message
																code="product.monthlyFeeCap.capFeeAmt"
																text="Cap Fee Amt" /></th>


													</tr>
												</thead>
												<tbody>
													<tr>
														<td><spring:message
																code="product.monthlyFeeCap.label"
																text="Monthly Fee Capping" /></td>
														<td><form:input type="text"
																path="productAttributes[monthlyFeeCap_feeDesc]"
																name="monthlyFeeCap_feeDesc" id="monthlyFeeCap_feeDesc"
																style="width: -webkit-fill-available;" maxLength="100"
																onblur="return validateFeeDescription(this)" /> <form:errors
																path="productAttributes[monthlyFeeCap_feeDesc]"
																cssClass="fieldError"></form:errors></td>
														<td><form:select
																path="productAttributes[monthlyFeeCap_timePeriod]"
																name="monthlyFeeCap_timePeriod"
																id="monthlyFeeCap_timePeriod"
																style="width:160px;width: -webkit-fill-available;"
																onchange="callTimePeiodCheck()">
																<form:option value="CM">Calendar Months</form:option>
																<form:option value="SM">Configure Date Of Month</form:option>
															</form:select> <form:errors
																path="productAttributes[monthlyFeeCap_timePeriod]"
																cssClass="fieldError"></form:errors></td>

														<td><form:select
																path="productAttributes[monthlyFeeCap_assessmentDate]"
																name="monthlyFeeCap_assessmentDate"
																id="monthlyFeeCap_assessmentDate"
																style="width:85px; width: -webkit-fill-available;">

																<form:options items="${dateStr}" />
															</form:select> <form:errors
																path="productAttributes[monthlyFeeCap_assessmentDate]"
																cssClass="fieldError">
															</form:errors></td>

														<td><form:input
																path="productAttributes[monthlyFeeCap_feeCapAmt]"
																type="text" onkeypress="allowNumbersWithDot(event);"
																onblur="DecimalValueFormat(this); validateFeeCapAmt();"
																name="monthlyFeeCap_feeCapAmt"
																id="monthlyFeeCap_feeCapAmt"
																style="width: -webkit-fill-available;" maxlength="10" />
															<form:errors
																path="productAttributes[monthlyFeeCap_feeCapAmt]"
																cssClass="fieldError"></form:errors></td>

													</tr>
												</tbody>
											</table>
										</div>
									</c:if>
									<br>&nbsp;

								</div>
								<div class="col-lg-12 text-center">
									<button type="button" class="btn btn-primary"
										onclick="clickSavePrgmId(this.form.id,event)">
										<i class="glyphicon glyphicon-saved"></i>
										<spring:message code="button.update" text="Update" />
									</button>

									<button type="button" class="btn btn-primary gray-btn"
										onclick="goBackToProgramId();">
										<i class='glyphicon glyphicon-backward'></i>
										<spring:message code="button.back" />
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


function getTxndata()

{
	var deliveryChannel= $("#deliveryChannelListId option:selected").val();
	var limitsFees= $("#limitsFeesId option:selected").val();
	var purseIdd= $("#purseListId option:selected").val();
	var programDet= $("#programIdDropDownName option:selected").val();
	var descriptionDet= $("#programDesc").val();
	var splitted = programDet.split('~');
	var id=splitted[0];
	
	
		var id2=splitted[1];
		
		
		$("#progrmName").val(id2);
		$("#progrmId").val(id);
		
		var prg=$("#progrmId").val();
		
		
		$("#descId").val(descriptionDet);
		$("#LfId").val(limitsFees);
		$("#deliveyChnlId").val(deliveryChannel);
		$("#pursId").val(purseIdd);
		
		
		if(deliveryChannel!=-1){
		$("#delchn").val("true");
		$("#programIdForm").attr('action','goUpdateAttributesByProgramId');
		$("#programIdForm").submit();   
	
		}

		if(limitsFees!=-1){
			$("#selLimitFeesNameFlag").val("true");
			$("#programIdForm").attr('action','goUpdateAttributesByProgramId');
			$("#programIdForm").submit();   
		
			}
	
	}


	
	
	function goTransactionDetails(formId){
		
		$("#seltxn").val("true");
		
		var deliveryChannel= $("#deliveryChannelListId option:selected").val();
		var limitsFees= $("#limitsFeesId option:selected").val();
		var purseIdd= $("#purseListId option:selected").val();
		var transactionDet= $("#transactionListId option:selected").val();
		var programDet= $("#programIdDropDownName option:selected").val();
		var descriptionDet= $("#programDesc").val();
		var splitted = programDet.split('~');
		var id=splitted[0];
		var id2=splitted[1];
		
			
			$("#progrmId").val(id);
			$("#progrmName").val(id2);
			$("#descId").val(descriptionDet);
			$("#LfId").val(limitsFees);
			$("#deliveyChnlId").val(deliveryChannel);
			$("#pursId").val(purseIdd);
			$("#txnId").val(transactionDet);
			
			
			$("#programIdForm").attr('action','goUpdateAttributesByProgramId');
			$("#programIdForm").submit();   
		
		
		
		
	}
 
	
	//function DisableNonFinancialTxnsAmtField(){
	$("input[id$='_Flag']").each(function (i, el) {
		if($(el).val()!=null && $(el).val().trim()!='Y'){
			var ctrlId=el.id;
			var limits=$("#limitsFeesId option:selected").val();
			if(limits=='Limits'){
			var rowId=$("#"+ctrlId).closest('tr').get(0).id;
			$("#"+rowId).find("td input[id$='Amt'],td input[id$='AmtPerTx']").each(function(j,inpObj) {
				var inputId= inpObj.id;
				$("#"+inputId).prop('disabled',true);
			});
		}
		}
	});

	//fees changes
	
		
	$("input[id$='_clawback']").each(function (i, el) {
		enableClawbackCount(el);
		
	});
	
 	$("select[id$='_feeCondition']").each(function (i, el) {
		var ctrlId=$(el).attr('id');
		var val = $("#"+ctrlId+" option:selected").val();
		
		var minFeeAmtObj=$("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minFeeAmt");
		var feePercentObj=$("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_feePercent");
		if(val=='A'){
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))+ "_feePercent").prop("readonly", false);
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt").prop("readonly", false);
			
		}else if(val=='O'){
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))+ "_feePercent").prop("readonly", false);
		}
			
	}); 
 	
 	$("input[id$='_maxCount']").each(function (i, el) {
 		enableFrequency(el);
	});
 	$("input[id$='_freeCount']").each(function (i, el) {
 		enableFrequency(el);
	});
 	
	enableProRation();
	
	 
	$("#limitsFeesId").change(function(){
		
		$("#deliveryChannelListId").val('-1');
		$("#transactionListId").val('-1');
				
		if($("#limitsFeesId").val()=='Maintenance Fees')
		{
			 $("#deliveryChannelListId").val('host');
			 $('#deliveryChannelListId').prop('disabled', true);
 						
		}
		else
			{
			
			$('#deliveryChannelListId').prop('disabled', false);
			
			}
	}) 
	
	 
$("#limitsFeesId").change(function(){
	
	if($("#limitsFeesId").val()=='Maintenance Fees')
	{
		$("#deliveryChannelListId option:selected").val('host');
		var selchn=$("#deliveryChannelListId").val();
		
				
		getTxndata();
		
			 
		}
	else{ 
	
		getTxndata();
	}
}) 


function onloadData()
	
{
		if($("#limitsFeesId").val()=='Maintenance Fees')
		{
			 $("#deliveryChannelListId").val('host');
			 $('#deliveryChannelListId').prop('disabled', true);
		}
		else if($("#limitsFeesId").val()=='Monthly Fee Cap'){
			$("#deliveryChannelListId").val('-1');
			$('#deliveryChannelListId').prop('disabled', true);
			
			$("#transactionListId").val('-1');
			$('#transactionListId').prop('disabled', true);
		}
			
		else
		{
			
			$('#deliveryChannelListId').prop('disabled', false);
			$('#transactionListId').prop('disabled', false);
			
			}
		
		 callTimePeiodCheck();
		 validateFeeCapAmt();
		
}

 $("#limitsFeesId").change(function(){
	
	if($("#limitsFeesId").val()=='Monthly Fee Cap')
	{	$("#programIdForm").attr('action','showAttributesOfMonthlyFeeCap');
		$("#programIdForm").submit();   
	}
 	else {
 		$("#deliveryChannelListId").prop("disabled", false);
		$("#transactionListId").prop("disabled",false);
 	}
	
})  

function callTimePeiodCheck() {
		if ($("#monthlyFeeCap_timePeriod option:selected").val() == 'SM') {
			$("#monthlyFeeCap_assessmentDate").prop('disabled', false);
		} else {
			$("#monthlyFeeCap_assessmentDate").val('1');
			$("#monthlyFeeCap_assessmentDate").prop('disabled', true);
		}
	}
/* 	function clickMonthlyFeeCapSave() {

		$("#feedBackTd").html('');
		var feeDescValid = validateFeeDescription($("#monthlyFeeCap_feeDesc"));

		var feeAmtValid = validateFeeCapAmt();
		if (feeAmtValid && feeDescValid) {
			$("#monthlyFeeCap_assessmentDate").prop('disabled', false);
			$("#programIdForm").submit();
		}
	} */
	function validateFeeCapAmt() {
		var amount = $("#monthlyFeeCap_feeCapAmt").val();
		var isValid = true;
		if (amount == null || amount.trim() == '') {
			generateAlert("programIdForm", "monthlyFeeCap_feeCapAmt",
					"monthlyFeeCap_feeCapAmt.empty");
			isValid = false;
		} else if (isNaN(amount) || parseFloat(amount) < 0) {
			generateAlert("programIdForm", "monthlyFeeCap_feeCapAmt",
					"monthlyFeeCap_feeCapAmt.invalid");
			isValid = false;
		} else if (!DecimalValueFormat($("#monthlyFeeCap_feeCapAmt").get(0))) {
			isValid = false;
		} else {
			clearError("monthlyFeeCap_feeCapAmt");
		}
		return isValid;
	}
	
	

</script>
