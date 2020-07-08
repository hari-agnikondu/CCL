<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script
	src="${pageContext.request.contextPath}/resources/js/bootstrap-datepicker.js"></script>

<html>
<head>
<script src="<c:url value="/resources/js/clpvms/order.js" />"></script>
<script
	src="<c:url value="/resources/js/clpvms/common.js"/>"></script>

<script>

 $(document).ready(
		function() {
			var date_input = $('input[name="Date"]'); //our date input has the name "date"
			var container = $('.bootstrap-iso form').length > 0 ? $(
					'.bootstrap-iso form').parent() : "body";
			date_input.datepicker({
				format : 'dd/mm/yyyy',
				container : container,
				todayHighlight : true,
				autoclose : true,
			})	
		}); 
		
	
$(function() {
	$('.date-picker')
			.datepicker(
					{
						changeMonth : true,
						changeYear : true,
						showButtonPanel : true,
						dateFormat : 'dd/MM/yyyy',
						onClose : function(dateText, inst) {
							var date = $(
									"#ui-datepicker-div .ui-datepicker-date :selected")
									.val();
							var month = $(
									"#ui-datepicker-div .ui-datepicker-month :selected")
									.val();
							var year = $(
									"#ui-datepicker-div .ui-datepicker-year :selected")
									.val();
							$(this).datepicker('setDate',
									new Date(date, year, month, 1));
						}
					});
});

</script>
</head>

<div class="body-container" style="min-height: 131px;">     
	<div class="container">
		<form:form name="checkOrderStatus" class='form-horizontal cardRangeSearch' commandName="orderForm" id="checkOrderStatus" method="POST"
			action='checkOrderStatus'>
           <article class="col-lg-12">
			<div class="graybox col-lg-5 col-lg-offset-3">

				
				<div class="col-lg-12">
						<h3>
						<spring:message	code="order.product.search" text="Search Orders" />
						</h3>
				</div>	
				
				<div class="col-lg-12">
						<div class="col-lg-4">
						<label for="productName"><spring:message code="order.product.enter" text="Enter Product Name" /></label>
						</div>
						
						<div class="col-lg-6">
							<form:select path="productId" id="productId" name="productId" >
								<form:option value="-1">--Select--</form:option>
								<form:options items="${productMap}" />
							</form:select>
							<div>
							<form:errors path="productName" cssStyle="color:red" />		
							</div>
	
						</div>
				</div>
				
				<div class="col-lg-12">
						<div class="col-lg-4">
						<label for="orderId"><spring:message code="order.order.enter" text="Enter Order Number" /></label>
						</div>
						
						<div class="col-lg-6">
						<form:input path="orderId" id="orderId" name="orderId" class="textbox xlarge4label trim"  onkeyup="return isAlphaNumericWithDotAndHyphen(this)"
									maxlength="16" autocomplete="on"  title="Allowed Special Characters are - ." />
							<div>
							<form:errors path="orderId" cssStyle="color:red" />		
							</div>
	
						</div>
				</div>	
				
				<%-- <div class="col-lg-2 col-lg-offset-10">
							<security:authorize access="hasRole('SEARCH_ORDER')">
							<button type="submit" name="button_Search" class="btn btn-primary" id="search_submit">
								<i class="glyphicon glyphicon-search"></i> <spring:message code="button.search" text="Search"/>			
							</button>
							</security:authorize>
				</div>
				 --%>
				<div class="col-lg-12">
						<div class="col-lg-4">
						<label for="orderStatus"><spring:message code="order.status.select" text="Select Order Status" /></label>
						</div>
						
						<div class="col-lg-6">
						<form:select  path="status" id="orderStatus" name="orderStatus" >
								<form:option value="-1">--Select--</form:option>
								<form:option value="IN-PROGRESS">IN-PROGRESS</form:option>
								<form:option value="APPROVED">APPROVED</form:option>
                                <form:option value="FAILED">FAILED</form:option>
								<form:option value="PENDING">PENDING</form:option>
								<form:option value="REJECTED">REJECTED</form:option>
								<form:option value="ORDER-IN-PROGRESS">ORDER-IN-PROGRESS</form:option>
								<form:option value="ORDER-GENERATED">ORDER-GENERATED</form:option>
								<form:option value="CCF-IN-PROGRESS">CCF-IN-PROGRESS</form:option>
								<form:option value="CCF-GENERATED">CCF-GENERATED</form:option>
								<form:option value="SHIPPED">SHIPPED</form:option>
							</form:select>
							
	
						</div>
						
						
						<div class="col-lg-2">
							<security:authorize access="hasRole('SEARCH_ORDER')">
							<button type="submit" name="button_Search" class="btn btn-primary" id="search_submit" onclick="return validateFromDateToDate()">
								<i class="glyphicon glyphicon-search"></i> <spring:message code="button.search" text="Search"/>			
							</button>
							</security:authorize>
				</div>
				</div>	
				
				<div class="col-lg-12">
						<div class="col-lg-4">
						<label for="fromDate"><spring:message code="order.fromDate.select" text="Select From Date" /></label>
						</div>
						
						<div class="col-lg-6">
						<input id="fromDate" name="fromDate" 
						    	type="text"  placeholder="DD/MM/YYYY"  class="date-picker"
								 autocomplete="off"
						    	value="${fromDate}"

							  />
							<%-- <div>
							<form:errors path="orderId" cssStyle="color:red" />		
							</div> --%>
	
						</div>
				</div>	
				
				<div class="col-lg-12">
						<div class="col-lg-4">
						<label for="toDate"><spring:message code="order.toDate.select" text="Select To Date" /></label>
						</div>
						
						<div class="col-lg-6">
						<input id="toDate" name="toDate" 
						    	type="text"  placeholder="DD/MM/YYYY"  class="date-picker"
								 autocomplete="off"
						    	value="${toDate}"
							  />
							<%-- <div>
							<form:errors path="orderId" cssStyle="color:red" />		
							</div> --%>
	
						</div>
				</div>	
				
				<div class="col-lg-12 text-center">
							<label> <spring:message code="order.searchHint" /></label>
					<br />
				</div>	
						<%--  <div class="col-lg-12">
						 <label style="color:#337ab7;">	<spring:message	text="Do you know?" code="order.know" /></label>
						 <label style="font-size:12px" class="col-lg-12">	<spring:message	text="Enter part of product name for suggestion on product with similar name" code="order.hint" /></label>
						
						  </div>  --%>
					</div>
			</article>	
		</form:form>
		

			<c:if test="${showGrid =='true' }">

				<div class="group">


					<table id="tableViewUsers"
						class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
						style="width: 100% !important;">
						<thead class="table-head">
							<tr>
								<th style="width:85px"><spring:message code="order.id"
										text="Order Number" /></th>
								<th style="width:75px"><spring:message code="order.date"
										text="Date" /></th>
								<th style="width:150px"><spring:message code="product.name"
										text="Product Name" /></th>
								<th style="width:125px"><spring:message code="order.issuer"
										text="Issuer Name" /></th>
								<th style="width:125px"><spring:message code="order.partner"
										text="Partner Name" /></th>
								<th style="width:50px"><spring:message code="package.ID"
										text="Package ID" /></th>
								<th style="width:75px"><spring:message code="quantitY"
										text="Quantity" /></th>
								<th style="width:40px"><spring:message code="order.type"
										text="Type" /></th>
								<th style="width:125px"><spring:message code="order.status"
										text="Status" /></th>
								<th style="width:125px"><spring:message code="order.lineItemId"
										text="Line Item ID" /></th>
								<th style="width:125px"><spring:message code="order.lineItemIdStatus"
										text="Line Item ID Status" /></th>
								<th style="width:125px"><spring:message code="order.ccfFileName"
										text="CCF File Name" /></th>
								<th style="width:125px"><spring:message code="order.serialNumberRange"
										text="Serial Number Range" /></th>
														
							</tr>
						</thead>
						<tbody >

							<c:forEach items="${orderList}" var="order">
								<tr>
									<td>${order.orderId }</td>
									<td>${order.insDate }</td>
									<td>${order.productId }:${order.productName }</td>
									<td>${order.issuerName }</td>
									<td>${order.partnerName }</td>
									<td>${order.packageId }</td>
									<td>${order.quantity }</td>
									<td>${order.orderType }</td>
									<td>${order.status}</td>
									<td>${order.lineItemId}</td>
									<td>${order.lineItemIdStatus}</td>
									<td>${order.ccfFileName}</td>
									<td>${order.startSerialNo}-${order.endSerialNo}</td>
								</tr>
							</c:forEach>
						</tbody>

					</table>


				</div>
			</c:if>
			</div>
			</div>