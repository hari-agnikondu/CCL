<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/merchant.js"></script>

<body class="dashboard">

	<div class="body-container" style="min-height: 131px;">

		<div class="container">
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
					<article class="col-lg-12">

						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class='active SubMenu'><a data-toggle='tab'><i
									class="glyphicon glyphicon-tags"></i> <spring:message
										code="merchant.addMerchant" /></a></li>
						</ul>

						<div class="tabresp tab-content">
							<div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3"
								id="product">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<div class="" >
									<c:if test="${successstatus !=''}">
										<div class="text-center  success-green padding" id="statusMerchantAdd">
											<b>${successstatus}</b>
										</div>
									</c:if>
									<c:if test="${failstatus !=''}">

										<div class="text-center  error-red padding" id="formErrorId">
											<b>${failstatus}</b>
											<%-- </center></font></b> --%>
										</div>

									</c:if>

									<!-- <div class="col-lg-5 col-lg-offset-1"> -->
									<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="MerchantName"> <spring:message
													code="merchant.merchantName" /><font color='red'>*</font></label>
										</div>
										<div class="col-lg-8">
											<form:input title="Allowed Special Characters are .,;'_- "
												path="merchantName" id="merchantName" class="trim"
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
												onblur="validateMdmId(this.form.id,this.id)"
												 maxlength="20" />
											<div>
												<form:errors path="mdmId" id="mdmId" cssStyle="color:red" />
											</div>
										</div>
									</div>
									<!-- </div> -->
								</div>
								<div class="col-lg-12 text-center">
									<button type="button" id="Button" class="btn btn-primary"
										onclick="goAddMerchant(this.form.id);">
										<i class='glyphicon glyphicon-plus'></i>
										<spring:message code="button.add" />
									</button>
									<button type="button" onclick="ResetAddMerchant(this.form.id)"
										class="btn btn-primary gray-btn">
										<i class='glyphicon glyphicon-refresh'></i>
										<spring:message code="button.reset" />
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


