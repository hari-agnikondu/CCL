<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/merchant.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/merchantProduct.js"></script>

<body class="dashboard">

	<div class="body-container" style="min-height: 131px;">
	
		<div class="container">
			
			<form:form name="merchantToProductForm" id="merchantToProductForm" method="POST"
				class='form-horizontal' commandName="merchantToProductForm">

				<input type="hidden" id="actionparam" name="actionparam"
					value="${getButtonsAndAction.createAction}">


				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class='active SubMenu'>
								<a data-toggle='tab'>
									<i class="glyphicon glyphicon-tags"></i> 
									<spring:message code="merchant.viewassignPrdtoMer" />
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
								<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="Product Name"> <spring:message
													code="merchant.productName" /><font color='red'> *</font></label>
										</div>
										<div class="col-lg-8">
										<form:select path="productId" id="productId" disabled="true">
												<form:option value="-1" label="- - - Select - - -" />
												<form:options items="${productDropDown}" />
											</form:select>


											<div>
												<form:errors path="productId" 
													maxlength="100" cssStyle="color:red" />
											</div>
										</div>
									</div>

							<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="Merchant Name"> <spring:message
													code="merchant.merchantName" /><font color='red'> *</font></label>
										</div>
										<div class="col-lg-8">
											<form:select path="merchantName" id="merchantName"
												class="dropdown-medium"
												onblur="return validateDropDownMerchantProduct(this.form.id,this.id);" disabled="true">
												<form:option value="-1" label="- - - Select - - -" />
												<c:forEach items="${merchantDropDown}" var="merchant">
													<option
														value="${merchant.merchantId}~${merchant.merchantName}"
														<c:if test="${merchant.merchantId eq merchantToProductForm.merchantId}">selected="true"</c:if>>${merchant.merchantName}</option>
												</c:forEach>
											</form:select>
											<div>
												<form:errors path="merchantName" id="merchantName"
													cssStyle="color:red" />
											</div>
										</div>
									</div>


									<br>&nbsp;

							

								</div>
								
								 <form:hidden path="merchantId" id="merchantId" name="merchantID" 
									value="${merchantId}" /> 
									
								<div class="col-lg-12 text-center">								
									<button type="button" class="btn btn-primary gray-btn"
										onclick="goBackToMerchantFromView();">
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