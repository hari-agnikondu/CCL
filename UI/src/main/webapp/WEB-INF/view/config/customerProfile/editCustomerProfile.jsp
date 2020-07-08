<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>


<script src="${pageContext.request.contextPath}/resources/js/clpvms/customerProfile.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<div class="modal fade" id="define-constant-viewcard" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				
					<div class="modal-content">
						<div class="modal-body col-lg-12" id="load1">
					
							
						</div>
						<div class="modal-footer">
							<button data-dismiss="modal" class="btn btn-primary gray-btn"><spring:message
									code="button.back" /></button>
						</div>

					</div>
				
			</div>
		</div>	


<div class="body-container">
   <div id="feedBackTd" class="col-lg-12 text-center" style="padding-top:7px">
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out><p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;" id="errorfield"><c:out value="${statusMessage }"></c:out></p></c:if>
			</div>

	<form:form name="editCustomerProfile" id="editCustomerProfile"
		modelAttribute="customerProfileForm" method="POST" class='form-horizontal'>
		<div class="container">
			<section class="content-container">
				<article class="col-lg-12">
					<ul class="nav nav-tabs  col-lg-11 col-lg-offset-1">
						<li class="active SubMenu"><a href="#" data-toggle="tab"><spring:message
									code="customerProfile.editCustomerProfile" /></a></li>
					</ul>
					<div class="tabresp tab-content">
						<div
							class="tab-pane fade in active graybox col-lg-11 col-lg-offset-1">
							<div class="text-right Error-red">
								<spring:message code="label.mandatory" text="*Mandatory" />
							</div>
							<div class="form-inline">


								<!-- -for copy from option -->
								<div class="col-lg-12">
								<%-- 	<c:if test="${showCopyFrom=='true' }"> --%>
										<div class="col-lg-4">
											<label for="copyFrom"><spring:message code="copyFrom" /></label>
										</div>

										<div class="ui-widget">
											<select class="space" id="spnumbertype" name="spnumbertype">
												<!-- onblur="validateDropDown(this.form.id,this.id)" -->
												<!-- onchange="includeRespDiv();" -->
												<option value="-1">--select--</option>
												<option value="cardNumber">Card Number</option>
												<option value="accountNumber">Account Number</option>
												<option value="proxyNumber">Proxy Number</option>
											</select>
                                            <input type="text" name="parentCardData" id="parentCardData" />
											<button type="button" id="copyCardForm"
												class="btn btn-primary" onclick="getCustomerProfileDetails(this.form.id,'getCustomerProfileDetails')">
												<spring:message code="button.copy" />
											</button>
											
											<button type="button" id="viewCard"
												class="btn btn-primary" onclick="viewCard_add_edit()">
												<spring:message code="customerProfile.viewCard.button" />
											</button>
											<div>
												<span id="parentCardError"></span>
											</div>
											<input type="hidden" id="viewPage" name="viewPage" value="editCustomerProfile">
											<input type="hidden" name="url_viewpage" id="url_viewpage" value="${pageContext.request.contextPath}"/>
										</div>
								<%-- 	</c:if> --%>
								</div>
								<!-- end -->
								<form:input type="hidden" name="profileId" id="profileId" path="profileId" />

								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="accountNumber"><spring:message
												code="customerProfile.accountNumber" text="Account Number" /><font
											color='red'>*</font></label>
									</div>

									<div class="col-lg-6">
										<form:input path="accountNumber" id="accountNumber"
											name="accountNumber" class="textbox xlarge4label trim"
											onkeyup="return isAlphaNumeric(this)" maxlength="20"
											autocomplete="on" readonly="true" />
										<div>
											<form:errors path="accountNumber" cssStyle="color:red" />
										</div>

									</div>
								</div>


								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="proxyNumber"><spring:message
												code="customerProfile.proxyNumber" text="Proxy Number" /><font
											color='red'>*</font></label>
									</div>

									<div class="col-lg-6">
										<form:input path="proxyNumber" id="proxyNumber"
											name="proxyNumber" class="textbox xlarge4label trim"
											onkeyup="return isNumeric(this)" maxlength="19"
											autocomplete="on" readonly="true"/>
										<div>
											<form:errors path="proxyNumber" cssStyle="color:red" />
										</div>

									</div>
								</div>
                 
								<div class="col-lg-12">

									<div class="col-lg-4">
										<label for="Transaction Fee Supported"><spring:message
												code="card.transactionFeeSupported" /> <font color='red'></font></label>
									</div>
									<div class="col-lg-8">
										<form:radiobutton path="cardAttributes['feesSupportedCard']"
											id="feesSupported" name="feesSupported" value="Disable"
											checked="checked" />
										<label class='radiobox-line'>Disable</label>
										<form:radiobutton path="cardAttributes['feesSupportedCard']"
											value="Enable" id="feesSupported" name="feesSupported"
											data-skin="square" data-color="blue" />
										<label class='radiobox-line'>Enable</label>
									</div>

								</div>

								<div class="col-lg-12">

									<div class="col-lg-4">
										<label for="Maintenance Fee Supported"><spring:message
												code="card.maintenanceFeeSupported" /> <font color='red'></font></label>
									</div>
									<div class="col-lg-8">
										<form:radiobutton
											path="cardAttributes['maintainanceFeeSupportedCard']"
											checked="checked" id="maintainanceFeeSupported"
											name="maintainanceFeeSupported" value="Disable" />
										<label class='radiobox-line'>Disable</label>
										<form:radiobutton
											path="cardAttributes['maintainanceFeeSupportedCard']"
											value="Enable" id="maintainanceFeeSupported"
											name="maintainanceFeeSupported" data-skin="square"
											data-color="blue" />
										<label class='radiobox-line'>Enable</label>
									</div>

								</div>
								<div class="col-lg-12">

									<div class="col-lg-4">
										<label for="Limit Supported"><spring:message
												code="card.limitSupported" /> <font color='red'></font></label>
									</div>
									<div class="col-lg-8">
										<form:radiobutton path="cardAttributes['limitSupportedCard']"
											id="limitSupported" name="limitSupported" value="Disable" />
										<label class='radiobox-line'>Disable</label>
										<form:radiobutton path="cardAttributes['limitSupportedCard']"
											value="Enable" id="limitSupported"
											name="limitSupported" data-skin="square" data-color="blue" />
										<label class='radiobox-line'>Enable</label>
									</div>

								</div>
                               
								<div class="col-lg-12">

									<div class="col-lg-4"></div>
									<div class="col-lg-8">
										<button type="button" 
											onclick="updateCustomerProfile(this.form.id,'updateCustomerProfile')"
											class="btn btn-primary">
											<i class='glyphicon glyphicon-plus'></i>
											<spring:message code="button.update" />
										</button>
										<button type="button" class="btn btn-primary gray-btn"
											onclick="setActionAttrOfForm(this.form.id,'customerProfileConfig');">
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
	</form:form>

</div>

