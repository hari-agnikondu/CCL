<!doctype html>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<head>
<script src="<c:url value="/resources/js/clpvms/productGeneral.js" />"></script>
<script
	src="<c:url value="/resources/js/clpvms/common.js"/>"></script>
</head>


<body onload="generalSubmit('productGeneral','onload')">

	<div class="body-container">
		<div class="container">
			<form:form name="productGeneral" id="productGeneral" action="#"
				method="POST" class='form-horizontal ' commandName="productGeneral">
				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-9 col-lg-offset-1">
							<li class="active SubMenu"><a href="#productGeneral"
								data-toggle="tab"><spring:message
										code="Product.generalProductLable" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div class="tab-pane fade in active graybox col-lg-9 col-lg-offset-1">
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
									<div class="col-lg-4">
										<label for="defaultStatus"><spring:message
												code="Product.defaultStatus" /></label>
									</div>

									<div class="col-lg-8">
										<form:select path="productAttributes['defaultCardStatus']"
											id="defaultStatus" disabled="true">
											<form:option value="0" label="0:INACTIVE" />
										</form:select>
										<div>
											<form:errors path="productAttributes['defaultCardStatus']"
												cssClass="fieldError" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="serviceCode"><spring:message
												code="Product.serviceCode" /></label>
									</div>

									<div class="col-lg-8">
										<form:input path="productAttributes['serviceCode']"
											id="serviceCode" name="serviceCode"
											onkeyup="return isNumeric(this)"
											onblur="validateServiceCode(this.form.id,this.id)"
											autocomplete="off" maxlength="3" readonly="true"/>
										<div>
											<form:errors path="productAttributes['serviceCode']"
												cssClass="fieldError" />
										</div>
									</div>
								</div>


								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="txnCountForRecentStatement"><spring:message
												code="Product.txnCountForRecentStatement" /><font
											color='red'>*</font></label>
									</div>

									<div class="col-lg-8">
										<form:input
											path="productAttributes['txnCountRecentStatement']"
											id="txnCountForRecentStatement"
											name="txnCountForRecentStatement"
											onkeyup="return isNumeric(this)" autocomplete="off"
											onblur="validateTxnCount(this.form.id,this.id)" maxlength="2" readonly="true"/>
										<div>
											<form:errors
												path="productAttributes['txnCountRecentStatement']"
												cssClass="fieldError" />
										</div>
									</div>
								</div>



								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="URLForROBOHelp"><spring:message
												code="Product.URLForROBOHelp" /></label>
									</div>

									<div class="col-lg-8">
										<form:input path="productAttributes['roboHelpUrl']"
											id="URLForROBOHelpLabel" autocomplete="off"
											name="URLForROBOHelpLabel"
											onblur="validateURL(this.form.id,this.id)" maxlength="500" readonly="true"/>
										<div>
											<form:errors path="productAttributes['roboHelpUrl']"
												cssClass="fieldError" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="emailIDForStatement"><spring:message
												code="Product.emailIDForStatement" /></label>
									</div>

									<div class="col-lg-8">
										<form:input path="productAttributes['emailIdStatement']"
											id="emailIDForStatementLabel" name="emailIDForStatementLabel"
											autocomplete="off"
											onblur="validateEmail(this.form.id,this.id)" maxlength="50" readonly="true"/>
										<div>
											<form:errors path="productAttributes['emailIdStatement']"
												cssClass="fieldError" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="statementFooter"><spring:message
												code="Product.statementFooter" /></label>
									</div>

									<div class="col-lg-8">
										<form:input path="productAttributes['statementFooter']"
											id="statementFooterLabel" name="statementFooterLabel"
											autocomplete="off" maxlength="500" readonly="true"/>
										<div>
											<form:errors path="productAttributes['statementFooter']"
												cssClass="fieldError" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="customerCareNumber"><spring:message
												code="Product.customerCareNumber" /></label>
									</div>

									<div class="col-lg-8">
										<form:input path="productAttributes['customerCareNbr']"
											id="customerCareNumber" name="customerCareNumberLabel"
											autocomplete="off" maxlength="20"
											onkeyup="return isNumeric(this)"
											onblur="validateNonManFields(this.form.id,this.id)" readonly="true"/>
										<div>
											<form:errors path="productAttributes['customerCareNbr']"
												id="customerCareNbrErr" cssStyle="color:red" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="cardRenewalOrReplacement"><spring:message
												code="Product.cardRenewalOrReplacement" /></label>
									</div>

									<div class="col-lg-8">
										<form:select path="productAttributes['cardRenewReplaceProd']"
											id="cardRenewalOrReplacement" disabled="true"> 
											<form:option value="1" label="Same PAN" />
											<form:option value="2" label="New PAN" />
											<form:option value="3"
												label="New Product/Product category PAN" />
										</form:select>
										<div>
											<form:errors path="productAttributes['cardRenewReplaceProd']"
												id="cardRenewReplaceProdErr" cssStyle="color:red" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="cardExpiryPendingPeriod"><spring:message
												code="Product.cardExpiryPendingPeriod" /></label>
									</div>
									<div class="col-lg-8">
										<form:input path="productAttributes['cardExpiryPendingDays']"
											id="cardExpiryPendingPeriodLabel" autocomplete="off"
											name="cardExpiryPendingPeriodLabel"
											onblur="validateNonManFields(this.form.id,this.id)"
											onkeyup="return isNumeric(this)" maxlength="3" readonly="true"/>
										<div>
											<form:errors
												path="productAttributes['cardExpiryPendingDays']"
												id="cardExpiryPendingDaysErr" cssStyle="color:red" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4">

										<label for="maxNumOfCardsPerCustomer"><spring:message
												code="Product.maxNumOfCardsPerCustomer" /></label>
									</div>
									<div class="col-lg-8">
										<form:input path="productAttributes['maxCardsPerCust']"
											id="maxNumOfCardsPerCustomerLabel" autocomplete="off"
											name="maxNumOfCardsPerCustomerLabel"
											onblur="validateNonManFields(this.form.id,this.id)"
											onkeyup="return isNumeric(this)" maxlength="2" readonly="true"/>
										<div>
											<form:errors path="productAttributes['maxCardsPerCust']"
												id="maxCardsPerCustErr" cssStyle="color:red" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="preAuthExpiryPeriod"><spring:message
												code="Product.preAuthExpiryPeriod" /></label>
									</div>

									<div class="col-lg-8">
										<form:input path="productAttributes['preAuthExpiryPeriod']"
											id="preAuthExpiryPeriodLabel" name="preAuthExpiryPeriodLabel"
											onblur="validateNonManFields(this.form.id,this.id)"
											onkeyup="return isNumeric(this)" autocomplete="off"
											maxlength="3" readonly="true"/>
										<div>
											<form:errors path="productAttributes['preAuthExpiryPeriod']"
												id="preAuthExpiryPeriodErr" cssStyle="color:red" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="activationCode"><spring:message
												code="Product.activationCode" /></label>
									</div>
									<div class="col-lg-8">

										<c:set var="activationCode"
											value="${productGeneral.productAttributes['activationCode']}"></c:set>

										<c:choose>
											<c:when test="${activationCode}">
												<div class="col-lg-4">
													<form:radiobutton value="true"
														path="productAttributes['activationCode']"
														id="activationCode_Enable" name="activationCode"
														checked="checked" disabled="true"/>
													<label class='radiobox-line' for="activationCode"><spring:message
															code="Product.activationCode_Status_Enable" /></label>
												</div>

												<div class="col-lg-4">
													<form:radiobutton value="false"
														path="productAttributes['activationCode']"
														id="activationCode_Disable" name="activationCode"
														data-skin="square" data-color="blue" disabled="true"/>
													<label class='radiobox-line' for="activationCode"><spring:message
															code="Product.activationCode_Status_Disable" /></label>
												</div>
											</c:when>
											<c:otherwise>
												<div class="col-lg-4">
													<form:radiobutton value="true"
														path="productAttributes['activationCode']"
														id="activationCode_Enable" name="activationCode" disabled="true"/>
													<label class='radiobox-line' for="activationCode"><spring:message
															code="Product.activationCode_Status_Enable" /></label>
												</div>

												<div class="col-lg-4">
													<form:radiobutton value="false"
														path="productAttributes['activationCode']"
														id="activationCode_Disable" name="activationCode"
														data-skin="square" data-color="blue" checked="checked" disabled="true"/>
													<label class='radiobox-line' for="activationCode"><spring:message
															code="Product.activationCode_Status_Disable" /></label>
												</div>
											</c:otherwise>
										</c:choose>

										<div>
											<form:errors path="productAttributes['activationCode']"
												id="activationCodeErr" cssStyle="color:red" />
										</div>
									</div>
								</div>


								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="ruleSet"><spring:message
												code="Product.ruleSet" /></label>
									</div>

									<div class="col-lg-8">
										<form:select path="ruleSetId" id="ruleSet"
											class="dropdown-medium" disabled="true">
											<form:option value="-1" label="--- Select ---" />
											<form:options items="${ruleSetMap}" />
										</form:select>

									</div>

								</div>
								
								<div class="col-lg-12">
									<div class="col-lg-4">

										<label for="maxCardBalance"><spring:message
												code="Product.maxCardBalance" /><font
											color='red'>*</font></label>
									</div>
									<div class="col-lg-8">
										<form:input path="productAttributes['maxCardBalance']"
											id="maxCardBalance" autocomplete="off" name="maxCardBalance"
											onblur="return validateDecimalFormatForMaxCardBalance(this.form.id,this.id)"
											onkeyup="return allowNumbersWithDotForMaxCardBalance(this)" 
											maxlength="10" readonly="true"/>
										<div>
											<form:errors path="productAttributes['maxCardBalance']"
												cssStyle="color:red" />
										</div>
									</div>
								</div>
								
								<div class="col-lg-12">
                                    <div class="col-lg-4">
                                        <label for="chwUserAuthentication"><spring:message
                                                code="Product.chwUserAuthentication" /><font
                                            color='red'>*</font></label></label>
                                    </div>

                                    <div class="col-lg-8">
                                        <form:select path="productAttributes['chwAuthType']" id="chwUserAuthentication"
                                            class="dropdown-medium" disabled="true" >
                                            <form:option value="-1" label="--- Select ---" />
                                            <form:options items="${chwUserAuthTypes}" />
                                        </form:select>
										<div>
											<form:errors path="productAttributes['chwAuthType']"
												cssClass="fieldError" />
										</div>
                                    </div>

                                </div>
                                
                                <div class="col-lg-12">
                                    <div class="col-lg-4">
                                        <label for="ivrUserAuthentication"><spring:message
                                                code="Product.ivrUserAuthentication" /><font
                                            color='red'>*</font></label></label>
                                    </div>

                                    <div class="col-lg-8">
                                        <form:select path="productAttributes['ivrAuthType']" id="ivrUserAuthentication"
                                            class="dropdown-medium"  disabled="true" >
                                            <form:option value="-1" label="--- Select ---" />
                                            <form:options items="${ivrUserAuthTypes}" />
                                        </form:select>
										<div>
											<form:errors path="productAttributes['ivrAuthType']"
												cssClass="fieldError" />
										</div>
                                    </div>

                                </div>								
								
								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="cardBlocking"><spring:message
												code="Product.cardBlocking" /></label>
									</div>
									<div class="col-lg-8">

										<c:set var="cardBlocking"
											value="${productGeneral.productAttributes['cardBlocking']}"></c:set>

										<c:choose>
											<c:when test="${cardBlocking}">
												<div class="col-lg-4">
													<form:radiobutton value="true"
														path="productAttributes['cardBlocking']"
														id="cardBlocking_Enable" name="cardBlocking"
														checked="checked" onclick="showBlockingFields()" disabled="true"/>
													<label class='radiobox-line' for="cardBlocking"><spring:message
															code="Product.cardBlocking_Status_Enable" /></label>
												</div>

												<div class="col-lg-4">
													<form:radiobutton value="false"
														path="productAttributes['cardBlocking']"
														id="cardBlocking_Disable" name="cardBlocking"
														data-skin="square" data-color="blue" onclick="hideBlockingFields()" disabled="true"/>
													<label class='radiobox-line' for="cardBlocking"><spring:message
															code="Product.cardBlocking_Status_Disable" /></label>
												</div>
											</c:when>
											<c:otherwise>
												<div class="col-lg-4">
													<form:radiobutton value="true"
														path="productAttributes['cardBlocking']"
														id="cardBlocking_Enable" name="cardBlocking" onclick="showBlockingFields()" disabled="true"/>
													<label class='radiobox-line' for="cardBlocking"><spring:message
															code="Product.cardBlocking_Status_Enable" /></label>
												</div>

												<div class="col-lg-4">
													<form:radiobutton value="false"
														path="productAttributes['cardBlocking']"
														id="cardBlocking_Disable" name="cardBlocking" onclick="hideBlockingFields()"
														data-skin="square" data-color="blue" checked="checked" disabled="true"/>
													<label class='radiobox-line' for="cardBlocking"><spring:message
															code="Product.cardBlocking_Status_Disable" /></label>
												</div>
											</c:otherwise>
										</c:choose>

										<div>
											<form:errors path="productAttributes['cardBlocking']"
												id="cardBlockingErr" cssStyle="color:red" />
										</div>
									</div>
								</div>
								
								<div class="col-lg-12" id="invalidPin">
									<div class="col-lg-4">

										<label for="invalidPinAttempts"><spring:message
												code="Product.invalidPinAttempts" /><font
											color='red'>*</font></label>
									</div>
									<div class="col-lg-8">
										<form:input path="productAttributes['invalidPinAttempts']"
											id="invalidPinAttempts" autocomplete="off" name="invalidPinAttempts"
											onblur="return callValidatePinAttempts(this.form.id)"
											onkeyup="return isNumericForGeneral(this)" 
											maxlength="2" readonly="true"/>
										<div>
											<form:errors path="productAttributes['invalidPinAttempts']"
												cssStyle="color:red" />
										</div>
									</div>
								</div>
								
								<div class="col-lg-12" id="timePeriodforCardBlocking">
								
								<div class="col-lg-4">
									<label for="timePeriodforCardBlocking"><spring:message
											code="product.timePeriodforCardBlocking" /><font
										style="color: red">*</font></label>
								</div>
									<div class="col-lg-8"><form:select path="productAttributes['startTimeHours']" name="startTimeHours" id="startTimeHours" disabled="true">
									<form:option value="00">00</form:option>
									<form:option value="01">01</form:option>
									<form:option value="02">02</form:option>
									<form:option value="03">03</form:option>
									<form:option value="04">04</form:option>
									<form:option value="05">05</form:option>
									<form:option value="06">06</form:option>
									<form:option value="07">07</form:option>
									<form:option value="08">08</form:option>
									<form:option value="09">09</form:option>
									<form:option value="10">10</form:option>
									<form:option value="11">11</form:option>
									<form:option value="12">12</form:option>
									<form:option value="13">13</form:option>
									<form:option value="14">14</form:option>
									<form:option value="15">15</form:option>
									<form:option value="16">16</form:option>
									<form:option value="17">17</form:option>
									<form:option value="18">18</form:option>
									<form:option value="19">19</form:option>
									<form:option value="20">20</form:option>
									<form:option value="21">21</form:option>
									<form:option value="22">22</form:option>
									<form:option value="23">23</form:option>
									</form:select> 
									/&nbsp;<form:select name="startTimeMins" id="startTimeMins" path="productAttributes['startTimeMins']" disabled="true">
									<form:option value="00">00</form:option><form:option value="01">01</form:option><form:option value="02">02</form:option><form:option value="03">03</form:option><form:option value="04">04</form:option><form:option value="05">05</form:option><form:option value="06">06</form:option><form:option value="07">07</form:option><form:option value="08">08</form:option><form:option value="09">09</form:option><form:option value="10">10</form:option><form:option value="11">11</form:option><form:option value="12">12</form:option><form:option value="13">13</form:option><form:option value="14">14</form:option><form:option value="15">15</form:option><form:option value="16">16</form:option><form:option value="17">17</form:option><form:option value="18">18</form:option><form:option value="19">19</form:option><form:option value="20">20</form:option><form:option value="21">21</form:option><form:option value="22">22</form:option><form:option value="23">23</form:option><form:option value="24">24</form:option><form:option value="25">25</form:option><form:option value="26">26</form:option><form:option value="27">27</form:option><form:option value="28">28</form:option><form:option value="29">29</form:option><form:option value="30">30</form:option><form:option value="31">31</form:option><form:option value="32">32</form:option><form:option value="33">33</form:option><form:option value="34">34</form:option><form:option value="35">35</form:option><form:option value="36">36</form:option><form:option value="37">37</form:option><form:option value="38">38</form:option><form:option value="39">39</form:option><form:option value="40">40</form:option><form:option value="41">41</form:option><form:option value="42">42</form:option><form:option value="43">43</form:option><form:option value="44">44</form:option><form:option value="45">45</form:option><form:option value="46">46</form:option><form:option value="47">47</form:option><form:option value="48">48</form:option><form:option value="49">49</form:option><form:option value="50">50</form:option><form:option value="51">51</form:option><form:option value="52">52</form:option><form:option value="53">53</form:option><form:option value="54">54</form:option><form:option value="55">55</form:option><form:option value="56">56</form:option><form:option value="57">57</form:option><form:option value="58">58</form:option><form:option value="59">59</form:option></form:select>
							</div>
							</div>
								
								
								  <div class="col-lg-12">
                                    <div class="col-lg-4">
                                        <label for="redeemSplitTender"><spring:message
                                                code="Product.redeemSplitTender" /></label>
                                    </div>
                                    <div class="col-lg-8">

                                        <c:set var="redeemSplitTender"
                                            value="${productGeneral.productAttributes['redeemSplitTender']}"></c:set>

                                        <c:choose>
                                            <c:when test="${redeemSplitTender}">
                                                <div class="col-lg-4">
                                                    <form:radiobutton value="true"
                                                        path="productAttributes['redeemSplitTender']"
                                                        id="redeemSplitTender_Enable" name="redeemSplitTender" disabled="true"
                                                        checked="checked"/>
                                                    <label class='radiobox-line' for="redeemSplitTender"><spring:message
                                                            code="Product.redeemSplitTender_Status_Enable" /></label>
                                                </div>

                                                <div class="col-lg-4">
                                                    <form:radiobutton value="false"
                                                        path="productAttributes['redeemSplitTender']"
                                                        id="redeemSplitTender_Disable" name="redeemSplitTender" disabled="true" 
                                                        data-skin="square" data-color="blue"/>
                                                    <label class='radiobox-line' for="redeemSplitTender"><spring:message
                                                            code="Product.redeemSplitTender_Status_Disable" /></label>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="col-lg-4">
                                                    <form:radiobutton value="true"
                                                        path="productAttributes['redeemSplitTender']"
                                                        id="redeemSplitTender_Enable" name="redeemSplitTender" disabled="true"/>
                                                    <label class='radiobox-line' for="cardBlocking"><spring:message
                                                            code="Product.redeemSplitTender_Status_Enable" /></label>
                                                </div>

                                                <div class="col-lg-4">
                                                    <form:radiobutton value="false"
                                                        path="productAttributes['redeemSplitTender']"
                                                        id="redeemSplitTender_Disable" name="redeemSplitTender" disabled="true" 
                                                        data-skin="square" data-color="blue" checked="checked" />
                                                    <label class='radiobox-line' for="redeemSplitTender"><spring:message
                                                            code="Product.redeemSplitTender_Status_Disable" /></label>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>  
                                
                                 <div class="col-lg-12">
                                    <div class="col-lg-4">
                                        <label for="redeemLockSplitTender"><spring:message
                                                code="Product.redeemLockSplitTender" /></label>
                                    </div>
                                    <div class="col-lg-8">

                                        <c:set var="redeemLockSplitTender"
                                            value="${productGeneral.productAttributes['redeemLockSplitTender']}"></c:set>

                                        <c:choose>
                                            <c:when test="${redeemSplitTender}">
                                                <div class="col-lg-4">
                                                    <form:radiobutton value="true"
                                                        path="productAttributes['redeemLockSplitTender']"
                                                        id="redeemLockSplitTender_Enable" name="redeemLockSplitTender" disabled="true" 
                                                        checked="checked" />
                                                    <label class='radiobox-line' for="redeemLockSplitTender"><spring:message
                                                            code="Product.redeemLockSplitTender_Status_Enable" /></label>
                                                </div>

                                                <div class="col-lg-4">
                                                    <form:radiobutton value="false"
                                                        path="productAttributes['redeemLockSplitTender']"
                                                        id="redeemLockSplitTender_Disable" name="redeemLockSplitTender" disabled="true"
                                                        data-skin="square" data-color="blue" />
                                                    <label class='radiobox-line' for="redeemLockSplitTender"><spring:message
                                                            code="Product.redeemLockSplitTender_Status_Disable" /></label>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="col-lg-4">
                                                    <form:radiobutton value="true"
                                                        path="productAttributes['redeemLockSplitTender']"
                                                        id="redeemLockSplitTender_Enable" name="redeemLockSplitTender" disabled="true"/>
                                                    <label class='radiobox-line' for="cardBlocking"><spring:message
                                                            code="Product.redeemLockSplitTender_Status_Enable" /></label>
                                                </div>

                                                <div class="col-lg-4">
                                                    <form:radiobutton value="false"
                                                        path="productAttributes['redeemLockSplitTender']"
                                                        id="redeemLockSplitTender_Disable" name="redeemLockSplitTender"  disabled="true" 
                                                        data-skin="square" data-color="blue" checked="checked"  />
                                                    <label class='radiobox-line' for="redeemLockSplitTender"><spring:message
                                                            code="Product.redeemLockSplitTender_Status_Disable" /></label>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>  
                                
                            <%--     
							<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="partialAuthIndicator"><spring:message
												code="Product.partialAuthIndicator" /></label>
									</div>
									<div class="col-lg-8">

										<c:set var="partialAuthIndicator"
											value="${productGeneral.productAttributes['partialAuthIndicator']}"></c:set>

										<c:choose>
											<c:when test="${partialAuthIndicator}">
												<div class="col-lg-4">
													<form:radiobutton value="true"
														path="productAttributes['partialAuthIndicator']"
														id="partialAuthIndicator_Enable" name="partialAuthIndicator"
														checked="checked" disabled="true"/>
													<label class='radiobox-line' for="partialAuthIndicator"><spring:message
															code="Product.partialAuthIndicator_Status_Enable" /></label>
												</div>

												<div class="col-lg-4">
													<form:radiobutton value="false"
														path="productAttributes['partialAuthIndicator']"
														id="partialAuthIndicator_Disable" name="partialAuthIndicator"
														data-skin="square" data-color="blue" disabled="true"/>
													<label class='radiobox-line' for="partialAuthIndicator"><spring:message
															code="Product.partialAuthIndicator_Status_Disable" /></label>
												</div>
											</c:when>
											<c:otherwise>
												<div class="col-lg-4">
													<form:radiobutton value="true"
														path="productAttributes['partialAuthIndicator']"
														id="partialAuthIndicator_Enable" name="partialAuthIndicator" disabled="true"/>
													<label class='radiobox-line' for="cardBlocking"><spring:message
															code="Product.partialAuthIndicator_Status_Enable" /></label>
												</div>

												<div class="col-lg-4">
													<form:radiobutton value="false"
														path="productAttributes['partialAuthIndicator']"
														id="partialAuthIndicator_Disable" name="partialAuthIndicator" 
														data-skin="square" data-color="blue" checked="checked" disabled="true"/>
													<label class='radiobox-line' for="partialAuthIndicator"><spring:message
															code="Product.partialAuthIndicator_Status_Disable" /></label>
												</div>
											</c:otherwise>
										</c:choose>
									</div>
								</div>	
								 --%>
								<!-- added for new parameter consumed flag -->
								
										<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="consumedFlag"><spring:message
												code="Product.consumedFlag" /></label>
									</div>
									<div class="col-lg-8">

										<c:set var="consumedFlag"
											value="${productGeneral.productAttributes['consumedFlag']}"></c:set>

										<c:choose>
											<c:when test="${consumedFlag}">
												<div class="col-lg-4">
													<form:radiobutton value="true"
														path="productAttributes['consumedFlag']"
														id="consumedFlag_Enable" name="consumedFlag"
														checked="checked" disabled="true"/>
													<label class='radiobox-line' for="consumedFlag"><spring:message
															code="Product.consumedFlag_Status_Enable" /></label>
												</div>

												<div class="col-lg-4">
													<form:radiobutton value="false"
														path="productAttributes['consumedFlag']"
														id="consumedFlag_Disable" name="consumedFlag"
														data-skin="square" data-color="blue" disabled="true"/>
													<label class='radiobox-line' for="consumedFlag"><spring:message
															code="Product.consumedFlag_Status_Disable" /></label>
												</div>
											</c:when>
											<c:otherwise>
												<div class="col-lg-4">
													<form:radiobutton value="true"
														path="productAttributes['consumedFlag']"
														id="consumedFlag_Enable" name="consumedFlag" disabled="true"/>
													<label class='radiobox-line' for="consumedFlag"><spring:message
															code="Product.consumedFlag_Status_Enable" /></label>
												</div>

												<div class="col-lg-4">
													<form:radiobutton value="false"
														path="productAttributes['consumedFlag']"
														id="consumedFlag_Disable" name="consumedFlag"
														data-skin="square" data-color="blue" checked="checked" disabled="true"/>
													<label class='radiobox-line' for="consumedFlag"><spring:message
															code="Product.consumedFlag_Status_Disable" /></label>
												</div>
											</c:otherwise>
										</c:choose>

										<div>
											<form:errors path="productAttributes['consumedFlag']"
												id="activationCodeErr" cssStyle="color:red" />
										</div>
									</div>
								</div>
								
								<div class="col-lg-12" id="dormancyFeePeriod">
									<div class="col-lg-4">

										<label for="dormancyFeePeriod"><spring:message
												code="Product.dormancyFeePeriod" /></label>
									</div>
									<div class="col-lg-8">
										<form:input path="productAttributes['dormancyFeePeriod']"
											id="dormancyFeePeriod" autocomplete="off" name="dormancyFeePeriod"
											onkeyup="return isNumeric(this)" 
											maxlength="3" readonly="true"/>
										<div>
											<form:errors path="productAttributes['dormancyFeePeriod']"
												cssStyle="color:red" />
										</div>
									</div>
								</div>

								
							</div>
						</div>
					</article>
				</section>
			</form:form>
		</div>
	</div>

<script>

	$("#generalTab").addClass("active");
	$("#generalTab").siblings().removeClass('active');
</script>


</body>
</html>

