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
			

			<form:form name="programIdForm" id="programIdForm" method="POST"
				class='form-horizontal' commandName="programIdForm">

				<input type="hidden" id="actionparam" name="actionparam"
					value="${getButtonsAndAction.createAction}">


				<section class="content-container">
					<!--  <article class="col-xs-11"> -->
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class='active SubMenu'>
								<a data-toggle='tab'>
									<i class="glyphicon glyphicon-tags"></i> 
									<spring:message code="programID.viewProgramId" />
								</a>
							</li>
						</ul>

						<div class="tabresp tab-content">
							<div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3"
								id="product">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="" />
								</div>
								<div class="form-inline">

									<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="ProgramIDName"> <spring:message
													code="program.programIDName" /><font color='red'></font></label>
										</div>
										<div class="col-lg-8">
											<form:input title="Allowed Special Characters are .,;'_- "
												path="programIDName" id="programIdName" class="trim"
												onkeyup="return isAlphaNumericWithSpecialChars(this)"
												type="textarea" readonly="true" minlength="2" maxlength="100"
												onblur="validateFields(this.form.id,this.id)"/>
												 
									<div>
										<form:errors path="programIDName" id="programIdName"
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
												
												maxLength="255" rows="5" cols="51" style="resize:none" readonly="true"/>
											<div>
												<form:errors path="description" id="programDesc"
													cssStyle="color:red" />
											</div>
										</div>
									</div>
									<br>&nbsp;



								<div class="col-lg-12 text-center">
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