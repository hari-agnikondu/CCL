<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/programID.js"></script>

<body class="dashboard">

	<div class="body-container" style="min-height: 131px;">

		<div class="container">
			<div class="wrapper">
				<div class="text-center">
					<font class="errormsg"><b>${statusMessage}</b></font>
				</div>
			</div>
			
			<form:form name="programIdForm" id="programIdForm" method="POST"
				class='form-horizontal' commandName="programIdForm">
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
										code="programID.addProgramID" /></a></li>
						</ul>

						<div class="tabresp tab-content">
							<div class="tab-pane fade in active graybox col-lg-8 col-lg-offset-3"
								id="product">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<div class="" >
									<c:if test="${successstatus !=''}">
										<div class="text-center  success-green padding" id="statusProgramIdAdd">
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
											<label for="ProgramIDName"> <spring:message
													code="program.programIDName" /><font color='red'>*</font></label>
										</div>
										<div class="col-lg-8">
											<form:input title="Allowed Special Characters are .,;'_- "
												path="programIDName" id="programIdName" class="trim"
												onkeyup="return isAlphaNumericWithSpecialChars(this)"
												type="textarea" minlength="2" maxlength="100"
												onblur="validateFields(this.form.id,this.id)" />
												 
									<div>
										<form:errors path="programIDName" id="programIdName"
											cssStyle="color:red" />
									</div>
										</div>
									</div>
									
											<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="partnerName"> <spring:message
														code="program.partnerName" /><font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">
												<form:select path="partnerId"
													id="programPartnerName" class="dropdown-medium" 
													onblur="return validateDropDownpartner(this.form.id,this.id);">
													<form:option value="-1" label="--- Select ---" />
													<form:options items="${partnerNameList}" />
												</form:select>
												<div>
													<form:errors path="partnerId" id="programPartnerName"
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
											<form:textarea path="description" id="programDesc" class="trim"
												type="textarea" onblur="validateFields(this.form.id,this.id)"
												
												maxLength="255" rows="5" cols="51" style="resize:none" />
											<div>
												<form:errors path="description" id="programDesc"
													cssStyle="color:red" />
											</div>
										</div>
									</div>

									<br>&nbsp;

									<!-- </div> -->
								</div>
								<div class="col-lg-12 text-center">
									<button type="button" id="Button" class="btn btn-primary"
										onclick="goAddProgramId(this.form.id);">
										<i class='glyphicon glyphicon-plus'></i>
										<spring:message code="button.add" />
									</button>
									<button type="button" onclick="ResetAddProgramID(this.form.id)"
										class="btn btn-primary gray-btn">
										<i class='glyphicon glyphicon-refresh'></i>
										<spring:message code="button.reset" />
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


