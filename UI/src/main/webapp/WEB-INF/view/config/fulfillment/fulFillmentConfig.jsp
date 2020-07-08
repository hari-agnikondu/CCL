<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils"%>


<body>
<!-- delete box starts -->

	<div class="modal fade" id="define-constant-delete" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<form:form commandName="deleteBox" name="deleteFulfillment" id="deleteFulfillment" method="post">
				<div class="modal-content">
					<div class="modal-body col-lg-12">
						<div class="col-lg-12" style="display:inline-block">
							<span style="width: 100%; display: inline-block;word-wrap: break-word;"> Do you want to delete the Fulfillment record '<b id="fulfillmentDisplay"></b>' ? </span>
						</div>

					</div>
				 <input type="hidden" name="fulFillmentSEQID" id="fulFillmentIdtoDel" />
				 <input type="hidden" name="fulfillmentID" id="fulFillmentIdtoDelete" />  
					<div class="modal-footer">
						<button type="button" onclick="goDeleteFulfillment();"	class="btn btn-primary">
							<i class="glyphicon glyphicon-trash"></i>
							<spring:message code="button.delete" />
						</button>
						<button data-dismiss="modal" class="btn btn-primary gray-btn">
							<spring:message code="button.cancel" />
						</button>

					</div>

				</div>
			</form:form>
		</div>
	</div>
<!-- delete box ends -->
<div class="body-container" style="min-height: 131px;">     
<div class="container">
	
		  <div id="feedBackTd" class="form-group text-center" > 
			<c:if test="${statusFlag=='success'}">
			<p class="successMsg" style="text-align:center;font-weight: bold; padding-top: 4px;"><c:out value="${statusMessage}"/></p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;padding-top: 4px;"><c:out value="${statusMessage}"/></p></c:if>
		</div> 
	
	
		<div id="searchFulfillment" class="graybox col-lg-5 col-lg-offset-3">
		<form:form id="fulFillmentForm" method="post" commandName="fulFillmentForm">
				<div class="col-lg-12">
					<h3>
						<spring:message code="fullFillment.searchFulfillment" />
					</h3>
				</div>
				<div class="col-lg-12">
						<div class="col-lg-4">
							<label for="search_FulFillmentID"><spring:message code="fullFillment.searchFulfillmentID" /></label>
						</div>
						<div class="col-lg-6">
							<form:input  path="fulFillmentName" id="fulfillmentNameID"
								class="textbox xlarge4label trim" name='search_fulfillmentName'
								onkeyup="return allowSpecificspecialchars(this)" type="textarea"
								maxlength="200" />
							<font color="red"><span id="fulfillmentNameIDError"></span></font>
							<form:errors path="fulFillmentName" id="fulfillmentNameID"
									 name='search_fulfillmentName' cssClass="fieldError" />
						</div>
						<div class="col-lg-2">
							<p class="text-center">
								<button type="submit" class="btn btn-primary"
									onclick="searchFulFillment(event)">
									<i class='glyphicon glyphicon-search'></i>
									<spring:message text="Search" code="button.search" />
								</button>
							</p>
						</div>
					</div>
				<div class="col-lg-12 text-center">
							<label> <spring:message code="fullFillment.serachHint" /></label>
					<br />
				</div>
					
				<input id="fulFillmentSEQID" name="fulFillmentSEQID" type="hidden" value="" />
				<input id="fulfillmentID" name="fulfillmentID" type="hidden" value="" /> 
				<input id="searchType" name="searchType" type="hidden" value="${SearchType}" /> 
				<input id="searchedName" name="searchedName" type="hidden" value="" /> 
				<input type="hidden" name="jsPath" id="jsPath" value="${pageContext.request.contextPath}/resources/JS_Messages/" />
				<br />
		</form:form>
	   </div>
	   
	

<security:authorize access="hasRole('ADD_FULFILLMENT_VENDOR')">
<article class="col-lg-12">	
<div class="col-lg-12 text-right">
	<button type="button" class="btn btn-primary"
		style="bottom: 15px; position: relative;"
		onclick="clickAddFullfilment()">
		<i class='glyphicon glyphicon-plus'></i>
		<spring:message code="fullFillment.button.addFulfillment" />
	</button>
</div>
</article>
</security:authorize>

<c:if test="${showGrid!=null && showGrid == 'true' }">
<div class="group">
		<h3>
			<i class="icon-table"></i>
		</h3>
		<div id="tableDiv">
			<table id="tableViewfullFillments"
				class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
				style="width: 100% !important;">
				<thead class="table-head">
					<tr>
						<th><spring:message code="fullFillment.fulfillmentID" /></th>
						<th><spring:message code="fullFillment.fulfillmentName" /></th>
						<th><spring:message code="fullFillment.automaticShipment" /></th>
						<th><spring:message code="fullFillment.shippedTimeDealy" /></th>
						<th><spring:message code="fullFillment.CCFFileFormat" /></th>
						<th><spring:message code="fullFillment.repCCFFileFormat" /></th>
						<th style="width: 300px;"><spring:message code="fullFillment.action" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${fulFillmentTblList}" var="requesterDetails" varStatus="status">
						<tr>
							<td class="dont-break-out">${requesterDetails.fulfillmentID}</td>
							<td class="dont-break-out">${requesterDetails.fulFillmentName}</td>
							<td class="dont-break-out">${requesterDetails.isAutomaticShipped}</td>
							<td class="dont-break-out">${requesterDetails.shippedTimeDealy}</td>
							<td class="dont-break-out"><c:out value="${requesterDetails.ccfFileFormat}"/></td>
							<td class="dont-break-out"><c:out value="${requesterDetails.replaceCcfFileFormat}"/></td>
							<td>
							<security:authorize access="hasRole('EDIT_FULFILLMENT_VENDOR')">
							<button type="submit" class="btn btn-primary-table-button"
									id="search_submit"
									style="position: relative;"
									onclick="editFulfillment(${requesterDetails.fulFillmentSEQID});">
									<i class='glyphicon glyphicon-edit'></i><spring:message code="button.edit" />
								</button>
								</security:authorize>
								
								<security:authorize access="hasRole('VIEW_FULFILLMENT_VENDOR')">
								<button type="submit" class="btn btn-primary-table-button"
									id="search_submit"
									style="position: relative;"
									onclick="viewFulfillment(${requesterDetails.fulFillmentSEQID});">
									<i class='glyphicon glyphicon-list'></i><spring:message code="button.view" />
								</button>
								</security:authorize>
								<security:authorize access="hasRole('DELETE_FULFILLMENT_VENDOR')">
								<button type="submit" class="btn btn-primary-table-button"
									id="search_submit" data-toggle="modal"
									data-target="#define-constant-delete"
									onclick="goDelete('${requesterDetails.fulFillmentSEQID}~${requesterDetails.fulfillmentID}~${requesterDetails.fulFillmentName}')">
									<i class='glyphicon glyphicon-trash'></i>
									<spring:message code="button.delete" />
								</button></security:authorize></td>

						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</c:if> 

</div>
</div>
</body>
<script src="<c:url value="/resources/js/clpvms/fulfillment.js" />"></script>
