<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<meta http-equiv="Pragma" content="no-cache">

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/order.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<div class="container">


	<article class="col-lg-12" id="placeOrder" >
	<div id="feedBackTd" class="col-lg-6 col-lg-offset-3">
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out><p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;" id="errorfield"><c:out value="${statusMessage }"></c:out></p></c:if>
			</div>
		<ul  class="nav nav-tabs col-lg-6 col-lg-offset-3">
			<li class="active"><a href="#placeOrder" data-toggle="tab"
				class="hoverColor"><i class="glyphicon glyphicon-tags"></i>&nbsp;<spring:message
						code="header.order.place" text="Place Order for Retail Cards" /></a></li>

		</ul>
		<div class="tabresp tab-content">
			
			<div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3">
				<div class="form-group text-right Error-red">
					<spring:message code="label.mandatory" text="*Mandatory" />
				</div>
				<div class="form-inline">
					<form:form action="#" method="POST" modelAttribute="orderForm"
						id="orderForm" name="orderForm">

							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for="partnerName"><spring:message
											code="product.name" text="Product Name" /><font
										style="color: red">*</font></label>
								</div>
								<div class="col-lg-8">
									<form:select path="productId" name="productId" id="productId" onchange="return retrieveMerchantsAndPackage(this.form.id,this.id);">
										<form:option value="-1">--Select--</form:option>
										<form:options items="${productMap}" />
									</form:select>
									<form:errors path="productId" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12" >
								<div class="col-lg-4">
									<label for="packageId"><spring:message
											code="package.ID" text="Package ID" /><font style="color: red">*</font></label>
								</div>

								<div class="col-lg-8">
									<form:select path="packageId" name="packageId" id="packageId" onchange="return selectBoxValidation(this.form.id,this.id); ">
									<form:options items="${packageMap }" />
									</form:select>
									<form:errors path="packageId" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12">
								<div class="col-lg-4 ">
									<label for=merchantId><spring:message code="merchant"
											text="Merchant" /></label>
								</div>
								<div class="col-lg-8">
									<form:select path="merchantId" name="merchantId" id="merchantId"  onchange="return retrieveStoresByMerchantId(this.id);">
										<form:option value="-1">--Select--</form:option>
										<form:options items="${merchantMap }" />
									</form:select>
									<form:errors path="merchantId" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12">
								<div class="col-lg-4 ">
									<label for=locationId><spring:message code="store"
											text="Store" /></label>
								</div>
								<div class="col-lg-8">
									<form:select path="locationId" name="locationId" id="locationId" onchange="retrieveAvailableInventory()" >
										<form:option value="-1">--Select--</form:option>
										<form:options items="${storeMap }" />
									</form:select>
									<form:errors path="locationId" cssClass="fieldError"></form:errors>
								</div>
							</div>
							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for=orderId><spring:message
											code="order.id" text="Order Number" />
											<!--  <font style="color: red">*</font> -->
											 </label>
								</div>
								<div class="col-lg-8">
									
									<form:input path="orderId" name="orderId" type="text" id="orderId"  maxlength="16" onkeyup="return isAlphaNumeric(this)" onblur="return validateFields(this.form.id,this.id);" />
									<form:errors path="orderId" cssClass="fieldError"></form:errors>
								</div>
							</div>
							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for=quantity><spring:message
											code="quantity" text="Quantity" /><font style="color: red">*</font> </label>
								</div>
								<div class="col-lg-5">
									<form:input path="quantity" name="quantity" type="text" id="quantity"  maxlength="10" onkeypress="return isNumericfn(event)" onblur="return validateFields(this.form.id,this.id);" />
									<form:errors path="quantity" cssClass="fieldError"></form:errors>
								</div>
								<div class="col-lg-3">
									<a href="#availableInventoryModal" data-toggle="modal"  style="color:black" onclick="retrieveAvailableInventory()"><spring:message code="button.avalInventory" text="Available Inventory" /></a>
								</div>
							</div>
								<div class="col-lg-12 text-center" >
								 	<security:authorize access="hasRole('ADD_ORDER')">
									<button type="button" class="btn btn-primary"
										onclick="placeOrder(this.form.id)" style="width:auto">
										<i class='glyphicon glyphicon-plus'></i>
										<spring:message text="Place Order" code="button.placeOrder" />
									</button>
									</security:authorize>
									<a href="${pageContext.request.contextPath}/"
										class="btn gray-btn btn-primary"><i
										class='glyphicon glyphicon-backward'></i>
									<spring:message code="button.back" text="Back" /> </a>
							</div>
					</form:form>
				</div>
			</div>
		</div>
	</article>

<div class="modal fade" id="availableInventoryModal" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
			
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for=orderId><spring:message
											code="button.avalInventory" text="Available Inventory" /></label>
								</div>
								<div class="col-lg-8">
									
									<input  name="availableInventory" type="text" id="availableInventory"  readonly="readonly"/>
									
								</div>
							</div>
						</div>
						
						<div class="modal-footer" style="text-align: center;">
							
							<button data-dismiss="modal" class="btn btn-primary"><spring:message
									code="button.close" text="Close" /></button>
						</div>

					</div>
				
			</div>
		</div>
</div>

<script type="text/javascript">
$(document).ready(function(){
	   $('input').on("cut copy paste",function(e) {
	      e.preventDefault();
	   });
	});
</script>