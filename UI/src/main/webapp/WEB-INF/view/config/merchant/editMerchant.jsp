<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/merchant.js"></script>

<body class="dashboard">

	<div class="body-container" style="min-height: 131px;">
	
		<div class="container">
			<!-- delete box starts -->
			<div class="modal fade" id="define-constant-update" tabindex="-1"
				role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<form:form commandName="confirmBox" name="updateMerchant"
						id="updateMerchant" method="post">
						
						<div class="modal-content">
							<div class="modal-body col-lg-12">
								<div class="col-lg-12" style="display:inline-block">
									<span style="width: 100%; display: inline-block;word-wrap: break-word;"> Do you want to Update the Merchant record '<b
										 id="merchantNameDisp"></b>' ?
									</span>
								</div>
							</div>
							
							<form:hidden path="" id="merchantIdtoUpdate" />
							
							<input type="hidden" name="merchantName" id="merchantIdtoUpdate" />
							
							<div class="modal-footer">
								<button type="button" onclick="goUpdateMerchant();"
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

			<div class="wrapper">
				<div class="text-center">
					<font class="errormsg"><b>${statusMessage}</b></font>
				</div>
			</div>

			<form:form name="merchantForm" id="merchantForm" method="POST"
				class='form-horizontal' commandName="merchantForm">

				<input type="hidden" id="actionparam" name="actionparam"
					value="${getButtonsAndAction.createAction}">

				<div id="ErrorStatus" class="text-center strong">
					<font color='red'>${ErrorStatus}</font>
				</div>

				<section class="content-container">
					<!--  <article class="col-xs-11"> -->
					<article class="col-lg-12">
						<!-- <ul class="nav nav-tabs"> -->
						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class='active SubMenu'>
								<a data-toggle='tab'>
									<i class="glyphicon glyphicon-tags"></i> 
									<spring:message code="merchant.editMerchant" />
								</a>
							</li>
						</ul>

						<div class="tabresp tab-content">
							<div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3"
								id="product">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<div class="form-inline">
									<span id='hidemydata'>
										<c:if test="${successstatus !=''}">
											<h4>
												<p class="">
													<b><font size="3"><center style="color: green;">
																<strong>${successstatus}</strong>
															</center></font></b>
												</p>
											</h4>
										</c:if> <c:if test="${failstatus !=''}">
											<h4>
												<p class="">
													<b><font size="3"><center style="color: red;">
																<strong>${failstatus}</strong>
															</center></font></b>
												</p>
											</h4>
										</c:if>
									</span>

									<!-- <div class="col-lg-6 form-col-2"> -->
									<!-- <div class="col-lg-5 col-lg-offset-1"> -->

									<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="MerchantName"> <spring:message
													code="merchant.merchantName" /><font color='red'>*</font></label>
										</div>
										<div class="col-lg-8">
											<form:input title="Allowed Special Characters are .,;'_- " class="trim"
												path="merchantName" id="merchantName"
												onkeyup="return isAlphaNumericWithSpecialChars(this)"
												type="textarea" minlength="2" maxlength="100"
												onblur="validateFields(this.form.id,this.id)" />
											<div>
												<form:errors path="merchantName" id="merchantName"
													maxlength="100" cssStyle="color:red" />
											</div>
										</div>
									</div>

									<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="Description"> <spring:message
													code="merchant.description" /><font color='red'></font></label>
										</div>
										<div class="col-lg-8">
											<form:textarea path="description" id="merchantDesc" class="trim"
												type="textarea"
												onblur="validateFields(this.form.id,this.id)"
												maxLength="255" rows="5" cols="51" style="resize:none" />
											<div>
												<form:errors path="description" id="description"
													cssStyle="color:red" />
											</div>
										</div>
									</div>

									<br>&nbsp;

									<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="MdmId"> <spring:message
													code="merchant.mdmId" /><font color='red'></font></label>
										</div>
										<div class="col-lg-8">
											<form:input path="mdmId" id="merchantMdmId" type="textarea"
												onkeyup="return isNumeric(this)"
												onblur="validateMdmId(this.form.id,this.id)" maxlength="20" />
											<div>
												<form:errors path="mdmId" id="mdmId" cssStyle="color:red" />
											</div>
										</div>
									</div>

									<!-- </div> -->

								</div>
								
								<input id="merchantID" name="merchantID" type="hidden"
									value="${merchantId}" /> <input
									id="editMerchantId" name="editMerchantId" type="hidden"
									value="${editMerchantId}" />
									
								<div class="col-lg-12 text-center">								
									<button type="button" id="Button" data-toggle="modal"
										data-target="#define-constant-update" class="btn btn-primary"
										onclick="goConfirmMerchant(this.form.id);">
										<i class='glyphicon glyphicon-saved'></i>
										<spring:message code="button.update" />
									</button>
									<button type="button" class="btn btn-primary gray-btn"
										onclick="goBackToMerchant();">
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