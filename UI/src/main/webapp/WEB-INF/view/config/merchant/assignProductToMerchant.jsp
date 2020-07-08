<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/merchantProduct.js"></script>

<body class="dashboard">

	<div class="body-container" style="min-height: 131px;">

		<div class="container">
			<div class="wrapper">
				<div class="text-center">
					<font class="errormsg"><b>${statusMessage}</b></font>
				</div>
			</div>
			
			<form:form name="merchantToProductForm" id="merchantToProductForm" method="POST"
				class='form-horizontal' commandName="merchantToProductForm">
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
										code="merchant.assignPrdtoMer" /></a></li>
						</ul>

						<div class="tabresp tab-content">
							<div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3"
								id="product">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<div class="" >
									<c:if test="${successstatus !=''}">
										<div class="text-center  success-green" id="statusMerchant" >
											<b>${successstatus}</b>
										</div>
									</c:if>
									<c:if test="${failstatus !=''}">

										<div class="text-center  error-red col-lg-12 padding" id="formErrorId">
											<b>${failstatus}</b>
											<%-- </center></font></b> --%>
										</div>

									</c:if>

									<!-- <div class="col-lg-5 col-lg-offset-1"> -->
									<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="Product Name"> <spring:message
													code="merchant.productName" /><font color='red'> *</font></label>
										</div>
										<div class="col-lg-8">
	<form:select path="productName" id="productId" class="dropdown-medium" onblur="return validateDropDownMerchantProduct(this.form.id,this.id);">
		<form:option value="-1" label="- - - Select - - -"/> 
	<%-- 	<form:options items="${productDropDown.productName}"/> --%>
	<c:forEach items="${productDropDown}" var="product">
		<option value="${product.productId}~${product.productName}"
		<c:if test="${product.productName eq merchantToProductForm.productName}">selected="true"</c:if>>${product.productId}:${product.productName}</option>
	</c:forEach>
	
		</form:select>
											
											
											<div>
												<form:errors path="productName" id="productId"
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
		<form:select path="merchantName" id="merchantId" class="dropdown-medium" onblur="return validateDropDownMerchantProduct(this.form.id,this.id);">
		<form:option value="-1" label="- - - Select - - -"/> 
		<%-- <form:options items="${merchantDropDown.merchantName}"/> --%>
	<c:forEach items="${merchantDropDown}" var="merchant">
		<option value="${merchant.merchantId}~${merchant.merchantName}"
		
		
		<c:if test="${merchant.merchantName eq merchantToProductForm.merchantName}">selected="true"</c:if>>${merchant.merchantName}</option>
	</c:forEach>
		</form:select>
							<div>
							<form:errors path="merchantName" id="merchantId"
													cssStyle="color:red" />
											</div>
										</div>
									</div>

								
									<!-- </div> -->
								</div>
								<br>&nbsp;
			<input type="hidden" name="merchantName"  id="merchantName" value="">
<input type="hidden" name="productName" id="productName" value="">
					
								<div class="col-lg-12 text-center">
									<button type="button"  class="btn btn-primary"
										onclick="goAssignProdToMerchant(this.form.id);">
										<i class='glyphicon glyphicon-plus'></i>
										<spring:message code="button.add" />
									</button>
									<button type="button" onclick="ResetAssignProdToMerchant(this.form.id)"
										class="btn btn-primary gray-btn">
										<i class='glyphicon glyphicon-refresh'></i>
										<spring:message code="button.reset" />
									</button>
									<button type="button" class="btn btn-primary gray-btn"
										onclick="goBackToAssignProdToMerchant();">
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


