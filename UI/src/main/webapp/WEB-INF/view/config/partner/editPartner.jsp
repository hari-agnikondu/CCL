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
	
	         <div class="modal fade" id="define-constant-edit" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<form:form commandName="partnerForm" name="editPartner" action="editPartner"
					id="editPartner" method="post">
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12" style="display:inline-block">
								
							<span style="width: 100%; display: inline-block;word-wrap: break-word;">	Do you want to update the partner record "<b><span id="partnerNameDisp1"></span></b>" ? </span>
								
							</div>

						</div>
						
						<%-- <form:input path="partnerId" name="partnerId"
								id="partnerId" type="hidden"/>
						<form:input  path="partnerName" type="hidden" id="partnerName" name="partnerName" /> --%>
							<!-- <input type="hidden" name="deletePartnerName"
								id="deletePartnerName" /> -->
						<div class="modal-footer">
							<button type="button" onclick="goUpdatePartner();"
								class="btn btn-primary"><i class="glyphicon glyphicon-saved"></i><spring:message
									code="button.update" /></button>
							<button data-dismiss="modal"  class="btn btn-primary gray-btn"><spring:message
									code="button.cancel" /></button>

						</div>

					</div>
				</form:form>
			</div>
		</div>
		
<div class="body-container" style="min-height: 131px;">  	

<div class="container">

<!-- 	<article class="col-lg-12" id="editPartner" style="margin-top:20px;margin-left:-15px"> -->
	    	<div id="feedBackTd" class="col-lg-12 text-center">
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out><p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out></p></c:if>
			</div>
		<ul  class="nav nav-tabs col-lg-8 col-lg-offset-3">
			<li class="active"><a href="#partner" data-toggle="tab"
				class="hoverColor"><i class="glyphicon glyphicon-tags"></i>&nbsp;<spring:message
						code="header.partner.edit" text="Edit Partner" /></a></li>

		</ul>
		<div class="tabresp tab-content">
		
			<div class="tab-pane fade in active graybox col-lg-8 col-lg-offset-3" id="product">
				<div class="form-group text-right Error-red">
					<spring:message code="label.mandatory" text="*Mandatory" />
				</div>
				<div class="form-inline">
					<form:form action="#" method="POST" modelAttribute="partnerForm"
						id="editForm" name="editForm">
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
										onblur="validateFields(this.form.id,this.id)" />
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
										onblur="validateFields(this.form.id,this.id)" style="resize:none"/>
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
										onblur="validateFields(this.form.id,this.id)" />
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
														class="dropdown-medium" size="8" multiple="multiple">
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
														 class="btn btn-block">
														<i class="glyphicon glyphicon-forward"></i>
													</button>
													<button type="button" id="currency_rightSelected"
														 class="btn btn-block">
														<i class="glyphicon glyphicon-chevron-right"></i>
													</button>
													<button type="button" id="currency_leftSelected"
														class="btn btn-block">
														<i class="glyphicon glyphicon-chevron-left"></i>
													</button>
													<button type="button" id="currency_leftAll"
														 class="btn btn-block">
														<i class="glyphicon glyphicon-backward"></i>
													</button>
												</div>

												<div class="col-lg-3 ">
													<c:set var="currencyList"
														value="${partnerForm.supportedCurrency}" />
													<div>Selected</div>
													<form:select name="to[]" path="supportedCurrencyUpdate"
														id="currency_to" class="dropdown-medium" size="8"
														multiple="multiple">
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
														class="dropdown-medium" size="8" multiple="multiple">
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
													<button type="button" id="purse_leftAll"
														 class="btn btn-block">
														<i class="glyphicon glyphicon-backward"></i>
													</button>
												</div>

												<div class="col-lg-3 ">
													<c:set var="purseList"
														value="${partnerForm.supportedPurse}" />
													<div>Selected</div>
													<form:select name="to[]" path="supportedPurseUpdate"
														id="purse_to" class="dropdown-medium" size="8"
														multiple="multiple">
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
									<%-- <div class="col-lg-2">
										<input TYPE="radio" value="Y" id="partnerStatusYes"
											name="partnerStatus"
											<c:if test="${partnerForm.isActive=='Y'}">checked="checked"</c:if>
											disabled="disabled" /> <label class='radiobox-line'
											for="search_status"><spring:message code="label.yes"
												text="Yes" /></label>
									</div>

									<div class="col-lg-2">

										<input TYPE="radio" value="N" id="partnerStatusNo"
											name="partnerStatus" data-skin="square"
											<c:if test="${partnerForm.isActive=='N'}">checked="checked"</c:if>
											data-color="blue" disabled="disabled" /> <label
											class='radiobox-line' for="search_status"><spring:message
												code="label.no" text="No" /></label>
									</div> --%>
									<label for="IsActive"> <font color='BLACK'><strong>YES</strong></font></label>
								</div>
								<form:input path="isActive" name="isActive" type="hidden" />
								
							</div>
							<div class="col-lg-12 text-center">
								<!-- <div class="col-lg-5 col-lg-offset-2"> -->

									<button type="button" class="btn btn-primary"
										onclick="FormSubmit(this.form.id,event)" id="edit_submit" data-toggle="modal" data-target="#define-constant-edit">
										<i class='glyphicon glyphicon-saved'></i>
										<spring:message code="button.update" text="Update" />
									</button>

									<a
										href="${pageContext.request.contextPath}/config/partner/partnerConfig"
										class="btn gray-btn btn-primary"><i
										class='glyphicon glyphicon-backward'></i>
									<spring:message code="button.back" text="Back" /> </a>
								<!-- </div> -->

							</div>
						<!-- </div> -->
					</form:form>
				</div>
			</div>
		</div>
	<!-- </article> -->
</div>
</div>


<script>
$('#currency').multiselect();
$('#purse').multiselect();
</script>