<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<script src="<c:url value="/resources/js/clpvms/location.js"/>"></script>
<script src="<c:url value="/resources/js/clpvms/common.js"/>"></script>
</head>

<body>
	
	<div class="modal fade" id="define-constant-editModel" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">

			<div class="modal-content">
				<div class="modal-body col-lg-12">
					<div class="col-lg-12" style="display: inline-block">

						<span> Do you want to update the Location '<b
							id="displayName"></b>' for Merchant '<b
							id="dispMerchName"></b>' ?
						</span>

					</div>
				</div>
				<div class="modal-footer">
					<button type="button" onclick="updateLocationSubmit();" class="btn btn-primary">
						<i class="glyphicon glyphicon-saved"></i>
						<spring:message code="button.update" />
					</button>
					<button data-dismiss="modal" class="btn btn-primary gray-btn">
						<spring:message code="button.cancel" />
					</button>

				</div>
			</div>
		</div>
	</div>

	<div class="body-container">
		<div class="container">
			<form:form name="editLocation" id="editLocation" method="POST"
				class='form-horizontal ' commandName="editLocation">
				<input type="hidden" id="srvUrl" value="${srvUrl}" />
				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class="active SubMenu"><a href="#editLocation"
								data-toggle="tab"><i class='glyphicon glyphicon-tags'></i> <spring:message
										code="location.editLocation" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<article class="col-lg-12">	
								<c:if test="${statusMessage!='' && statusMessage!=null}">
									<div class="text-center error-red">
										<b>${statusMessage}</b>
									</div>
								</c:if>
								<c:if test="${status!='' && status!=null}">
									<div class="text-center success-green">
										<b>${status}</b>
									</div>
								</c:if>
							</article>
							
							<form:hidden path="locationId" id="locationId"/>
							<form:hidden path="merchantName" id="merchantName"/>
							
							<div class="col-lg-12">
										<div class="col-lg-5">
										<label for="merchant Name"><spring:message
												code="location.merchantName" /><font color='red'>*</font></label>
									</div>
										
										<div class="col-lg-6">
											<form:select path="merchantId" id="merchantId" 
											onblur="validateDropDownForLocation(this.form.id,this.id)" >
												<form:option value="-1" label="- - - Select - - -" />
												<c:forEach items="${merchantList}" var="merchantList">
													<c:choose>
												<c:when test="${merchantId_obj == merchantList.merchantId}">
												<option value="${merchantList.merchantId}" selected="selected">${merchantList.merchantName}</option></c:when>
												<c:otherwise>
												<option value="${merchantList.merchantId}">${merchantList.merchantName}</option>
												</c:otherwise>
												</c:choose>
										</c:forEach>
																						
											</form:select>

											<div>
												<form:errors path="merchantId" id="merchantIdErr"
													cssClass="fieldError" />
											</div>
										</div>
										
										
									</div>
							
								<div class="col-lg-12">
									<div class="col-lg-5">
										<label for="location Name"><spring:message
												code="location.locationName" /><font color='red'>*</font></label>
									</div>
									<div class="col-lg-6">
										<form:input path="locationName" id="locationName" autocomplete="off"
											type="textarea" maxlength="50" class="trim"
											onkeyup="isAlphaNumericWithSpaceAndHash(this)"
											onblur="return validateFieldsLocation(this.form.id,this.id);" />
										<div>
											<form:errors path="locationName" class="error-red" />
										</div>
									</div>
								</div>
								
								<div class="col-lg-12">
									<div class="col-lg-5">
										<label for="Address1"><spring:message
												code="location.address1" /><font color='red'>*</font></label>
									</div>
									<div class="col-lg-6">
										<form:textarea path="addressOne" id="addressOne" autocomplete="off" class="trim"
											type="textarea" maxLength="255" rows="5" cols="51" style="resize:none" 
											onblur="return validateFieldsForNoPatternCheck(this.form.id,this.id);"/>
										<div>
											<form:errors path="addressOne" class="error-red" />
										</div>
									</div>
								</div>
								
								<div class="col-lg-12 padding-top-sm">
									<div class="col-lg-5">
										<label for="Address2"><spring:message
												code="location.address2" /></label>
									</div>
									<div class="col-lg-6">
										<form:textarea path="addressTwo" id="addressTwo" autocomplete="off"  class="trim"
											type="textarea" maxLength="255" rows="5" cols="51" style="resize:none"
											 onblur="return validateFieldsForAddressTwo(this.form.id,this.id);" />
										<div>
											<form:errors path="addressTwo" class="error-red" />
										</div>
									</div>
								</div>
								
							
								
									<div class="col-lg-12 padding-top-sm">
										<div class="col-lg-5">
										<label for="country"><spring:message
												code="location.country" /><font color='red'>*</font></label>
									</div>

										<div class="col-lg-6">
											<form:select path="countryCodeID" id="country"  onchange="getStates()"
											onblur="validateDropDownForLocation(this.form.id,this.id)">
												<form:option value="-1" label="- - - Select - - -" />
												<form:options items="${countryList}" />
											</form:select>

											<div>
												<form:errors path="countryCodeID" id="countryErr"
													cssClass="fieldError" />
											</div>
										</div>
									</div>
								
								
								<div class="col-lg-12">
										<div class="col-lg-5">
										<label for="state"><spring:message
												code="location.state" /><font color='red'>*</font></label>
									</div>

										<div class="col-lg-6">
											<form:select path="stateCodeID" id="state" 
											onblur="validateDropDownForLocation(this.form.id,this.id)" >
												<form:option value="-1" label="- - - Select - - -" />
												<form:options items="${stateList}" />
											</form:select>

											<div>
												<form:errors path="stateCodeID" id="stateErr"
													cssClass="fieldError" />
											</div>
										</div>
									</div>
									
										<div class="col-lg-12">
									<div class="col-lg-5">
										<label for="city"><spring:message
												code="location.city" /><font color='red'>*</font></label>
									</div>
									<div class="col-lg-6">
										<form:input path="city" id="city" autocomplete="off" class="trim"
											type="textarea" maxlength="20"
											onblur="return validateFieldsForNoPatternCheck(this.form.id,this.id);" />
										<div>
											<form:errors path="city" class="error-red" />
										</div>
									</div>
								</div>
									
									<div class="col-lg-12">
									<div class="col-lg-5">
										<label for="zip"><spring:message
												code="location.zip" /><font color='red'>*</font></label>
									</div>
									<div class="col-lg-6">
										<form:input path="zip" id="zip" autocomplete="off"
											type="textarea" maxlength="10" 
											onkeyup="isAlphaNumericWithSpaceforZip(this)" class="trim"
											onblur="return validateFieldsLocation(this.form.id,this.id);"/>
										<div>
											<form:errors path="zip" class="error-red" />
										</div>
									</div>
								</div>
							
						<article class="col-lg-12">					
								<div class="col-lg-12 text-center">
									<button type="button" id="updateLocation" data-toggle="modal"
										class="btn btn-primary"
										onclick="updateConfirm(this.form.id,event);" >
										<i class="glyphicon glyphicon-saved"></i>
										<spring:message code="button.update"  text="Update"/>
									</button>
									
									<button type="button" class="btn btn-primary gray-btn"
										onclick="backEditToLocationConfig(this.form.id,event);">
										<i class="glyphicon glyphicon-backward"></i>
										<spring:message code="button.back" />
									</button>
								
								</div>
								</article>
								</div>	
								</div>												
					</article>
				</section>
			</form:form>
		</div>
	</div>
</body>

</html>