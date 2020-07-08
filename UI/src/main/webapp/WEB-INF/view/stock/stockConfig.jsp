<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
<script src="<c:url value="/resources/js/clpvms/stock.js" />"></script>
<script
	src="<c:url value="/resources/js/clpvms/common.js"/>"></script>

</head>

<div class="body-container" style="min-height: 131px;">     
	<div class="container">
	<div id="feedBackTd" class="col-lg-6 col-lg-offset-3" style="padding-top:7px;">
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out><p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;" id="errorfield"><c:out value="${statusMessage }"></c:out></p></c:if>
			</div>
		
    
			<div class="graybox col-lg-5 col-lg-offset-3">

				<form:form name="stockSearch" class='form-horizontal' commandName="stockForm" id="stockSearch" method="POST"
			action='searchStock'>
				<div class="col-lg-12">
						<h3>
						<spring:message	code="" text="Search Stocks" />
						</h3>
				</div>	
				
				<div class="col-lg-12">
						<div class="col-lg-4">
						<label for="productName"><spring:message code="" text="Enter Merchant Name" /></label>
						</div>
						
						<div class="col-lg-6">
							<form:select path="merchantId" id="merchantId" name="merchantId" style="width:225px;">
								<form:option value="-1">--Select--</form:option>
								<form:options items="${merchantMap}" />
							</form:select>
							<div>
							<form:errors path="merchantId" cssStyle="color:red" />		
							</div>
	
						</div>
				</div>
				
				<div class="col-lg-2 col-lg-offset-10">
							<%-- <security:authorize access="hasRole('SEARCH_STOCK')"> --%>
							<button type="submit" name="button_Search" class="btn btn-primary" id="search_submit">
								<i class="glyphicon glyphicon-search"></i> <spring:message code="button.search" text="Search"/>			
							</button>
							<%-- </security:authorize> --%>
						</div>
				<div class="col-lg-12">
						<div class="col-lg-4">
						<label for="orderId"><spring:message code="" text="Enter Location Name" /></label>
						</div>
						
						<div class="col-lg-6">
						<form:select path="locationId" id="locationId" name="locationId" style="width:225px;">
								<form:option value="-1">--Select--</form:option>
								<form:options items="${locationMap}" />
							</form:select>
							<div>
							<form:errors path="locationId" cssStyle="color:red" />		
							</div>
	
						</div>
				</div>	
				<form:input type="hidden" name="productId" path="productId" id="productId" value="" />
				<div class="col-lg-12 text-center">
							<label> <spring:message code="stock.searchHint" /></label>
					<br />
				</div>
						<%--  <div class="col-lg-12">
						 <label style="color:#337ab7;">	<spring:message	text="Do you know?" code="order.know" /></label>
						 <label style="font-size:12px" class="col-lg-12">	<spring:message	text="Enter part of product name for suggestion on product with similar name" code="order.hint" /></label>
						
						  </div>  --%>
						  </form:form>
					</div>
		
		
		
	<article class="col-lg-12">
	<div class="text-right">
					 <security:authorize access="hasRole('ADD_STOCK')">
					<button type="button" class="btn btn-primary btn-add " onclick="callAddPage()">
						<i class='glyphicon glyphicon-plus'></i>
						<spring:message code="" text="Define Stock"/>
					</button>
					</security:authorize>
			</div>
	</article>
			<c:if test="${showGrid =='true' }">

				<div class="group">


					<table id="tableViewUsers"
						class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
						style="width: 100% !important;">


						<thead class="table-head">
							<tr>
								<th style="width:150px"><spring:message code=""
										text="Merchant Name" /></th>
								<%-- <th><spring:message code="order.date"
										text="Date" /></th> --%>
								<th style="width:175px"><spring:message code=""
										text="Location Name" /></th>
								<th style="width:175px"><spring:message code=""
										text="Auto Replenishment" /></th>
								<th style="width:175px"><spring:message code=""
										text="Initial Order Count" /></th>
								<th><spring:message code=""
										text="Reorder Level" /></th>
								<th><spring:message code=""
										text="Reorder Value" /></th>
								<th style="width:140px"><spring:message code=""
										text="Maximum Inventory" /></th>
								<th style="width:200px"><spring:message code=""
										text="Action" /></th>
							</tr>
						</thead>
						<tbody >

							<c:forEach items="${stockList}" var="stock" varStatus="loop">
								<tr id="${loop.index }">
									<td><input type="hidden" name="merchantId" id="merchantId${loop.index }" value="${stock.merchantId}"/><c:forEach items="${merchantMap}" var="mer"><c:if test="${mer.key eq  stock.merchantId}" ><c:out value="${mer.value}" /> </c:if></c:forEach></td>
									<td><input type="hidden" name="locationId" id="locationId${loop.index }" value="${stock.locationId}"/> 
										<input type="hidden" name="productId" id="productId${loop.index}" value="${stock.productId }"/> 
										<c:forEach items="${locationMap}" var="loc"><c:if test="${loc.key eq  stock.locationId}" ><c:out value="${loc.value}" /> </c:if></c:forEach> </td>
									<td><c:if test="${stock.autoReplenish =='Y'}"><spring:message code="label.yes" text="Yes" /></c:if>
											<c:if test="${stock.autoReplenish =='N'}"><spring:message code="label.no" text="No" /></c:if></td>
									<td>${stock.initialOrder }</td>
									<td>${stock.reorderLevel }</td>
									<td>${stock.reorderValue }</td>
									<td>${stock.maxInventory }</td>
									
									<td><security:authorize access="hasRole('EDIT_STOCK')">
										<button type="button" class="btn btn-primary-table-button"
												onclick="return clickEdit(this);">
												<i class="glyphicon glyphicon-edit"></i>
												<spring:message code="button.edit" />
										</button>
								</security:authorize>
								<security:authorize access="hasRole('VIEW_STOCK')">
									<button type="button" class="btn btn-primary-table-button"
												onclick="return clickViewFromStock(this);">
												<i class="glyphicon glyphicon-list"></i>
												<spring:message code="button.view" />
										</button>
										</security:authorize>
								
								</td>
								</tr>
							</c:forEach>
						</tbody>

					</table>


				</div>
			</c:if>
			</div>
			</div>
			
			<script>
			

			
			$(document).ready(function(){
				   $('input').on("cut copy paste",function(e) {
				      e.preventDefault();
				   });
				});
			</script>