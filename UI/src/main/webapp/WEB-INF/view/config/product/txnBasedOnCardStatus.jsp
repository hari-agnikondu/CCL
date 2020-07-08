<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/product.js"></script>

<form:form name="txnBasedOnStat" id="txnBasedOnStat" action="#"
	modelAttribute="productStat" class='form-horizontal'>
	
	<form:input path="productId" name="productId" type="hidden"/>

	<%--This Div is to diplay Copy Product Option--%>
	<div class="col-lg-12" style="padding-top: 1%;">
		 <c:if test="${showCopyFrom=='true' }">	 
			<div class="col-lg-1 col-lg-offset-4">
				<label><spring:message code="product.copyFrom"
						text="Copy From" /> </label>
			</div>
			<div class="col-lg-3">
				<form:select path="parentProductId" id="parentProductId" class="dropdown-medium">
					<form:option value="-1" label="Select"/> 
   					<form:options items="${parentProductMap}" />
				</form:select>												
			</div>
			<div class="col-lg-4">
				<button type="submit" id="productGeneral" class="btn btn-primary"
						onclick="getTxnStatDetails()"><spring:message code="button.copy" />	</button>
			</div>
	 	</c:if> 
	</div>
	
	<%--This is to Display "Success" message in "Green" and "Failure" message in "Red"--%>
	<div id="feedBackTd" class="form-group text-center">
			<c:if test="${status!='' && status!=null}">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${status }"/></p>
			</c:if>
			
			<c:if test="${statusMessage!='' && statusMessage!=null}">	
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p>
			</c:if>
	</div>
	
	
	<ul class="nav nav-tabs">
		<c:forEach items="${deliverChannelList}" var="deliveryChannel"
			varStatus="status">
			<li <c:if test="${status.index == 0}">class="active"</c:if> ><a href="#${deliveryChannel.deliveryChnlShortName}"
				data-toggle="tab" class="hoverColor">${deliveryChannel.deliveryChnlName}</a></li>
		</c:forEach>
	</ul>
	<div class="tabresp tab-content">
		<c:forEach items="${deliverChannelList}" var="deliveryChannel"
			varStatus="status">
			<div
				<c:if test="${status.index == 0}">class="tab-pane fade in active"</c:if>
				<c:if test="${status.index != 0}">class="tab-pane fade in"</c:if>
				id="${deliveryChannel.deliveryChnlShortName}">

				<div class="col-lg-6 form-col-2" style="overflow-x:auto;">


					<table id="tableViewUsers"
						class="table table-hover table-striped table-bordered">
						
						<thead class="table-head">
							<tr>
								<th><spring:message code="product.limit.transactions" text="Transactions" /></th>		
								<c:forEach items="${cardStatusList}" var="cardStatus"> 
								<th><c:set var="cardStatus" value="${cardStatus}" />${cardStatus}</th>
								</c:forEach>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${deliveryChannel.transactionMap}" var="txn">
								<tr id="${txn.key}"> 
									<td>${txn.value }<c:set var="txnShortName" value="${txn.key}" />
										<input type="hidden" name="${txn.key}" value="${txnFinancialFlagMap[txnShortName]}" /></td>
										
								</tr>
								<tr>
								
								<td>
								<c:set var="delChnl_txnName_cardStat"
											value="${deliveryChannel.deliveryChnlShortName}_${txn.key }_${cardStatus}" />
								<%-- <form:checkbox path="generalAttributes['${deliveryChannel.deliveryChnlShortName}_${txn.key }_${cardStatus}']" id= "delChnl_txnName_cardStat" name="delChnl_txnName_cardStat" checked="checked"/> --%>
								<input type="checkbox" name="dc" id="dc">
								</td> 
								</tr> 
							</c:forEach>
						</tbody>
					</table>
				</div>


			</div>
		</c:forEach>
	</div>

	<div class="panel-footer">
		<button type="button" class="btn btn-primary" onclick="saveTxnStat()">Save</button>

	</div>
</form:form>

<script>
$("#txnCardStatTab").addClass("active");
$("#txnCardStatTab").siblings().removeClass('active');
</script>
