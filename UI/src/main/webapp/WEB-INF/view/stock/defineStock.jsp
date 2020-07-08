<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<meta http-equiv="Pragma" content="no-cache">

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/stock.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<div class="container">


	<article class="col-lg-12" id="defineStock" style="padding-top:4px">
	<div id="feedBackTd" class="col-lg-6 col-lg-offset-3">
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out><p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;" id="errorfield"><c:out value="${statusMessage }"></c:out></p></c:if>
			</div>
		<ul  class="nav nav-tabs col-lg-6 col-lg-offset-3">
			<li class="active"><a href="#defineStock" data-toggle="tab"
				class="hoverColor"><i class="glyphicon glyphicon-tags"></i>&nbsp;<spring:message
						code="header.stock.define" text="Define Stock" /></a></li>

		</ul>
		<div class="tabresp tab-content">
			
			<div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3">
				<div class="form-group text-right Error-red">
					<spring:message code="label.mandatory" text="*Mandatory" />
				</div>
				<div class="form-inline">
					<form:form action="#" method="POST" modelAttribute="stockForm"
						id="defineStockForm" name="defineStockForm">

							<div class="col-lg-12">
								<div class="col-lg-4 ">
									<label for=merchantId><spring:message code="merchant"
											text="Merchant Name" /><font style="color: red">*</font></label>
								</div>
								<div class="col-lg-8">
									<form:select path="merchantId" name="merchantId" id="merchantId"  onchange="return retrieveStoresByMerchantId(this.form.id, this.id);">
										<form:option value="-1">--Select--</form:option>
										<form:options items="${merchantMap }" />
									</form:select>
									<form:errors path="merchantId" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12">
								<div class="col-lg-4 ">
									<label for=locationId><spring:message code="location"
											text="Location Name" /><font style="color: red">*</font></label>
								</div>
								<div class="col-lg-8">
									<form:select path="locationId" name="locationId" id="locationId" onchange="return validateSelectBox(this.form.id, this.id);">
										<form:option value="-1">--Select--</form:option>
										<form:options items="${locationMap }" />
									</form:select>
									<form:errors path="locationId" cssClass="fieldError"></form:errors>
								</div>
							</div>
							
							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for="productId"><spring:message
											code="product.name" text="Product Name" /><font style="color: red">*</font></label>
								</div>
								<div class="col-lg-8">
									<form:select path="productId" name="productId" id="productId" onchange="return validateSelectBox(this.form.id, this.id);">
										<form:option value="-1">--Select--</form:option>
										<form:options items="${productMap}" />
									</form:select>
									<form:errors path="productId" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							
							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for=autoReplenish><spring:message
											code="stock.autoreplenishment" text="Auto Replenishment" /> <font style="color: red">*</font></label>
								</div>
								<div class="col-lg-8">
									<div class="col-lg-2">
										<form:radiobutton value="Y" id="autoReplenishY" path="autoReplenish" onclick="replenishmentOptions()"
											name="autoReplenish" checked="checked" />
										<label class='radiobox-line' for="autoReplenishY"><spring:message
												code="label.yes" text="Yes" /></label>
									</div>

									<div class="col-lg-2">

										<form:radiobutton value="N" id="autoReplenishN"  path="autoReplenish" onclick="replenishmentOptions()"
											name="autoReplenish" data-skin="square" data-color="blue"	/> <label class='radiobox-line'
											for="autoReplenishN"><spring:message code="label.no"
												text="No" /></label>
									</div>
									
								</div>
							</div>
							
							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for=initialOrder><spring:message
											code="stock.initOrder" text="Initial Order Count" /><font style="color: red">*</font> </label>
								</div>
								<div class="col-lg-5">
									<form:input path="initialOrder" name="initialOrder" type="text" id="initialOrder" maxlength="5" onkeypress="return isNumericfn(event)" onblur="return validateFields(this.form.id,this.id);" />
									<form:errors path="initialOrder" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for=reorderLevel><spring:message
											code="stock.reorderLevel" text="Reorder Level" /><font style="color: red">*</font> </label>
								</div>
								<div class="col-lg-5">
									<form:input path="reorderLevel" name="reorderLevel" type="text" id="reorderLevel"  maxlength="5"   onkeypress="return isNumericfn(event)" onblur="return validateReplenishmentFields(this.form.id,this.id);" />
									<form:errors path="reorderLevel" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for=reorderValue><spring:message
											code="stock.reorderCount" text="Reorder Value" /><font style="color: red">*</font> </label>
								</div>
								<div class="col-lg-5">
									<form:input path="reorderValue" name="reorderValue" type="text" id="reorderValue"  maxlength="5"  onkeypress="return isNumericfn(event)" onblur="return validateReplenishmentFields(this.form.id,this.id);" />
									<form:errors path="reorderValue" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for=maxInventory><spring:message
											code="stock.maxInventory" text="Maximum Inventory" /><font style="color: red">*</font> </label>
								</div>
								<div class="col-lg-5">
									<form:input path="maxInventory" name="maxInventory" type="text" id="maxInventory"   maxlength="5"  onkeypress="return isNumericfn(event)" onblur="return validateFields(this.form.id,this.id);" />
									<form:errors path="maxInventory" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
								<div class="col-lg-12 text-center" >
									<button type="button" class="btn btn-primary"
										onclick="defineStock(this.form.id)" style="width:auto">
										<i class='glyphicon glyphicon-plus'></i>
										<spring:message text="Define Stock" code="button.defineStock" />
									</button>
										<button type="button" class="btn gray-btn btn-primary" value="Reset"
										onclick="ResetStock()">
										<i class='glyphicon glyphicon-refresh'></i>
										<spring:message code="button.reset" text="Reset" />
									</button>
									<a href="${pageContext.request.contextPath}/config/stocks/stockConfig"
										class="btn gray-btn btn-primary"><i
										class='glyphicon glyphicon-backward'></i>
									<spring:message code="button.back" text="Back" /> </a>
							</div>
					</form:form>
				</div>
			</div>
		</div>
	</article>

</div>

<script type="text/javascript">
$(document).ready(function(){
	   $('input').on("cut copy paste",function(e) {
	      e.preventDefault();
	   });
	});
	
if($("input[name='autoReplenish']:checked").val()=='Y'){
	$("#reorderLevel").prop('readonly',false);
	$("#reorderValue").prop('readonly',false);
	clearError('reorderValue');
	clearError('reorderLevel');
}else{
	$("#reorderLevel").prop('readonly',true);
	$("#reorderValue").prop('readonly',true);
	$("#reorderLevel").val('0');
	$("#reorderValue").val('0');
	clearError('reorderValue');
	clearError('reorderLevel');
}

</script>
