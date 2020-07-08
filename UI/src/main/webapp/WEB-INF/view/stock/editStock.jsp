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
<div class="modal fade" id="define-constant-edit" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<form:form commandName="stockForm" name="editStockConfirm" action=""
					id="editStockConfirm" method="post">
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12" style="display:inline-block">
								
							<span style="width: 100%; display: inline-block;word-wrap: break-word;"> Do you want to update the Stock record  ? </span>
								
							</div>

						</div>
						
						<div class="modal-footer">
							<button type="button" onclick="goUpdateStock();"
								class="btn btn-primary"><i class="glyphicon glyphicon-saved"></i><spring:message
									code="button.update" /></button>
							<button data-dismiss="modal"  class="btn btn-primary gray-btn"><spring:message
									code="button.cancel" /></button>

						</div>

					</div>
				</form:form>
			</div>
		</div>

	<article class="col-lg-12" id="editStock" style="margin-top:40px;margin-left:-15px">
	<div id="feedBackTd" class="col-lg-6 col-lg-offset-3">
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out><p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;" id="errorfield"><c:out value="${statusMessage }"></c:out></p></c:if>
			</div>
		<ul  class="nav nav-tabs col-lg-6 col-lg-offset-3">
			<li class="active"><a href="#editStock" data-toggle="tab"
				class="hoverColor"><i class="glyphicon glyphicon-tags"></i>&nbsp;<spring:message
						code="header.stock.edit" text="Edit Stock" /></a></li>

		</ul>
		<div class="tabresp tab-content">
			
			<div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3">
				<div class="form-group text-right Error-red">
					<spring:message code="label.mandatory" text="*Mandatory" />
				</div>
				<div class="form-inline">
					<form:form action="#" method="POST" modelAttribute="stockForm"
						id="editStockForm" name="editStockForm">

							<div class="col-lg-12">
								<div class="col-lg-4 ">
									<label for=merchantId><spring:message code="merchant"
											text="Merchant Name" /><font style="color: red">*</font></label>
								</div>
								<div class="col-lg-8">
								<form:input type="hidden"  path="merchantId" name="merchantId"  />
									<select  name="merchantId1" id="merchantId"  onchange="return retrieveStoresByMerchantId(this.form.id, this.id);" disabled="disabled">
										<option value="-1">--Select--</option>
										<c:forEach items="${merchantMap }" var="mer"><option value="${mer.key}" <c:if test="${mer.key eq stockForm.merchantId}" >selected</c:if>>${mer.value}</option></c:forEach>
										
									</select>
									<form:errors path="merchantId" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12">
								<div class="col-lg-4 ">
									<label for=locationId><spring:message code="location"
											text="Location Name" /><font style="color: red">*</font></label>
								</div>
								<div class="col-lg-8">
								<form:input type="hidden"  path="locationId" name="locationId"  />
									<select name="locationId1" id="locationId" onchange="return validateSelectBox(this.form.id, this.id);" disabled="disabled">
										<option value="-1">--Select--</option>
										<c:forEach items="${locationMap}" var="loc"><option value="${loc.key}" <c:if test="${loc.key eq stockForm.locationId}" >selected</c:if>>${loc.value}</option></c:forEach>
										
									</select>
									<form:errors path="locationId" cssClass="fieldError"></form:errors>
								</div>
							</div>
							
							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for="productId"><spring:message
											code="product.name" text="Product Name" /><font style="color: red">*</font></label>
								</div>
								<div class="col-lg-8">
								<form:input type="hidden"  path="productId" name="productId"  />
									<select  name="productId1" id="productId" onchange="return validateSelectBox(this.form.id, this.id);"  disabled="disabled" >
										<option value="-1">--Select--</option>
										
										<c:forEach items="${productMap}" var="prod"><option value="${prod.key}" <c:if test="${prod.key eq stockForm.productId}" >selected</c:if>>${prod.key}:${prod.value}</option></c:forEach>
										
									</select>
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
									<form:input path="initialOrder" name="initialOrder" type="text" id="initialOrder"  maxlength="5" onkeypress="return isNumericfn(event)" onblur="return validateFields(this.form.id,this.id);" readonly="true"/>
									<form:errors path="initialOrder" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for=reorderLevel><spring:message
											code="stock.reorderLevel" text="Reorder Level" /><font style="color: red">*</font> </label>
								</div>
								<div class="col-lg-5">
									<form:input path="reorderLevel" name="reorderLevel" type="text" id="reorderLevel"   maxlength="5"  onkeypress="return isNumericfn(event)" onblur="return validateReplenishmentFields(this.form.id,this.id);" />
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
									<form:input path="maxInventory" name="maxInventory" type="text" id="maxInventory" onpaste="return false;" maxlength="5"  onkeypress="return isNumericfn(event)" onblur="return validateFields(this.form.id,this.id);" />
									<form:errors path="maxInventory" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
								<div class="col-lg-12 text-center" >
									
									<button type="button" id="updateStock" class="btn btn-primary"  onclick="defineStock(this.form.id)" data-toggle="modal" >
									<i class='glyphicon glyphicon-saved'></i>
									<spring:message code="button.update" text="Update" />
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

<script>
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

$(document).ready(function(){
	   $('input').on("cut copy paste",function(e) {
	      e.preventDefault();
	   });
	   
	   
	   
	});
</script>
