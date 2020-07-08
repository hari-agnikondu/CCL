<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>


<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/issuer.js"></script>


<body class="dashboard">
	<div class="body-container" style="min-height: 131px;">
		<div class="container">


			<form:form name="ViewIssuerForm" id="ViewIssuerForm"
				action="/config/issuer/viewIssuer" method="POST"
				class='form-horizontal' commandName="issuerForm">



				<input id="issuerId" name="issuerId" type="hidden"
					value="${issuerform.issuerCode}" />
				<input id="editIssuerId" name="editIssuerId" type="hidden"
					value="${editIssuerId}" />
				<input type="hidden" name="jsPath" id="jsPath"
					value="${pageContext.request.contextPath}/resources/JS_Messages/" />

				<section class="content-container">
					<article class="col-lg-12">

						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class='active SubMenu'><a data-toggle='tab'><i
									class="glyphicon glyphicon-tags"></i> <spring:message
										code="issuer.viewIssuer" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active  graybox col-lg-6 col-lg-offset-3"
								id="product">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
							
								<div class="col-lg-12">
									<div class="col-lg-12">

										<div class="col-lg-4">
											<label for="institutionName"> <spring:message
													code="issuer.issuerName" /><font color='red'>*</font></label>
										</div>
										<div class="col-lg-8">

											<form:input title="Allowed Special Characters are .,;'_- "
												path="issuerName" id="issuerName"
												class="notEmpty isAlphaNum textbox textbox-xlarge"
												onkeyup="return isAlphaNumericWithSpace(this)"
												type="textarea" minlength="2" maxlength="100"
												style="resize:none"
												onblur="validateFields(this.form.id,this.id)" readonly="true" />
											<div>
												<form:errors path="issuerName" id="issuerName"
													cssStyle="color:red" />
											</div>
										</div>
									</div>

									<div class="col-lg-12">

										<div class="col-lg-4">
											<label for="Description"> <spring:message
													code="issuer.description" /><font color='red'></font></label>
										</div>
										<div class="col-lg-8">
											<form:textarea path="description" id="issuerDesc"
												class="isSplChar textbox textbox-xlarge"
												onblur="validateFields(this.form.id,this.id)"
												type="textarea" maxLength="255" rows="5" cols="51"
												style="resize:none"  readonly="true"/>
											<div>
												<form:errors path="description" id="description"
													cssStyle="color:red" />
											</div>
										</div>
									</div>
									<br>&nbsp;



									<div class="col-lg-12">
										<div class="col-lg-4">


											<spring:message code="issuer.isActive" />
											<font color='red'></font></label>
										</div>

										<div class="col-lg-8">


											<label for="IsActive"> <font color='BLACK'><strong>YES</strong></font></label>
										</div>

									</div>

								</div>
								<div class="col-lg-12 text-center">

									<button type="button" class="btn btn-primary gray-btn"
										onclick="goBacktoIssuerfromView();">
										<i class='glyphicon glyphicon-backward'></i>
										<spring:message code="button.back" />
									</button>

									<p class="text-center">&nbsp;</p>


								</div>


							</div>
					</article>
				</section>
			</form:form>


		</div>
	</div>
</body>

