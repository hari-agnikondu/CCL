<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<html>
<head>

<script
	src="<c:url value="/resources/js/clpvms/common.js"/>"></script>
 <script
	src="<c:url value="/resources/js/clpvms/groupAccessId.js"/>"></script>
	<script
	src="<c:url value="/resources/js/multiselect.min.js"/>"></script>
</head>
<body>


<input type="hidden" name="jsPath"  id="jsPath" value="${pageContext.request.contextPath}/resources/JS_Messages/" />

<div class="body-container" style="min-height: 131px;">

	<div class="container">
		<form:form name="viewAssignGroupAccess" id="viewAssignGroupAccess"  action="#" method="POST" class='form-horizontal ' commandName="groupAccessIdSearch">
		<section class="content-container">
           <article class="col-lg-12">

				<ul class="nav nav-tabs col-lg-7 col-lg-offset-2">
					<li class="active SubMenu"><a href="#viewAssignGroupAccess" data-toggle="tab">
					<i class="glyphicon glyphicon-tags"></i> <spring:message code="groupAccessId.assignGroupAccessIdLabel" /></a></li>
				</ul>
								
				<div class="tabresp tab-content">
				  <div class="tab-pane fade in active graybox col-lg-7 col-lg-offset-2" id="groupAccessIdTab">
											
						<form:hidden path="groupAccessName" id="groupAccessNameForEditAssign" />
							<div class="col-lg-12">
								<div class="col-lg-6">
								<label for="EnterGroupAccessName"><spring:message code="groupAccessId.groupAccessName" /></label>
								</div>
		
								<div class="col-lg-6">
								<form:hidden path="groupAccessId" id="groupAccessId_hidden"/>
								<form:select path="groupAccessId" id="groupAccessId" class="dropdown-medium"  disabled="true" >
								 <form:option value="-1" label="- - - Select - - -"/> 
			   					<form:options items="${groupAccessMap}" />
								</form:select>	
										<div>
										<form:errors path="groupAccessId" cssClass="fieldError"/>
										</div>
								</div>
							</div>			
							<div class="col-lg-12">
								<div class="col-lg-6">
								<label for="EnterProductName"><spring:message code="groupAccessId.productName" /></label>
								</div>
		
								<div class="col-lg-6">
								<form:hidden path="productId" id="productId_hidden"/>
								<form:select path="productId" id="productId" class="dropdown-medium" disabled="true">
								 <form:option value="-1" label="- - - Select - - -"/> 
			   					 <form:options items="${productMap}" />
								</form:select>	
										<div>
										<form:errors path="productId" cssClass="fieldError"/>
										</div>
								</div>
							</div>
										
				<div class="group">

					<table id="tablePartnersRange"
						class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
						style="width: 100% !important;">
						
						<thead class="table-head">
							<tr>
								<th><spring:message code="groupAccessId.TabGroupAccessName"
										text="Group Access Name" /></th>

								<th><spring:message code="groupAccessId.TabPartnerName"
										text="Product Name" /></th>
										<th><spring:message code="groupAccessId.TabPartyType"
										text="First Party" /></th>
								
							</tr>
						</thead>
						<tbody class="row">

							<c:forEach items="${groupAccessPartnersList}" var="groupAccessIdDetails"
								varStatus="status">
								<tr id="${groupAccessIdDetails.groupAccessId}">
								<td class="dont-break-out">${groupAccessIdDetails.groupAccessName}
								<input type="hidden" name="partnerArray[${status.index}].groupAccessId" value="${groupAccessIdDetails.groupAccessId}"/>
								</td>

								<td class="dont-break-out" id="${groupAccessIdDetails.partnerName}">${groupAccessIdDetails.partnerName}
								<input type="hidden" name="partnerArray[${status.index}].partnerId" value="${groupAccessIdDetails.partnerId}"/>
								<input type="hidden" name="partnerArray[${status.index}].partnerName" value="${groupAccessIdDetails.partnerName}"/>
								</td>
								<td class="dont-break-out">
								<c:choose>
								<c:when  test="${groupAccessIdDetails.partnerPartyType == 'FIRST PARTY'}">
								<input type="radio" name="partnerArray[${status.index}].partnerPartyType" value="FIRST PARTY" checked="true" disabled="true"/>First
								<input type="radio" name="partnerArray[${status.index}].partnerPartyType"  value="THIRD PARTY" disabled="true"/>Third
								</c:when>
								<c:when  test="${groupAccessIdDetails.partnerPartyType == 'FIRST PARTY OWNER'}">
								<input type="radio" name="partnerArray[${status.index}].partnerPartyType" value="FIRST PARTY" checked="true" disabled="true"/>First
								<input type="radio" name="partnerArray[${status.index}].partnerPartyType"  value="THIRD PARTY" disabled="true" />Third
								</c:when>
								<c:otherwise>	
								<input type="radio" name="partnerArray[${status.index}].partnerPartyType" value="FIRST PARTY" disabled="true"/>First
								<input type="radio" name="partnerArray[${status.index}].partnerPartyType"  value="THIRD PARTY" checked="true" disabled="true"/>Third
								</c:otherwise>
								</c:choose>
								</td>									
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>		
						<div class="col-lg-12 text-center">					
														
									<button type="button" class="btn btn-primary gray-btn" onclick="backFromViewAssignGroupAccess();">
									<i class="glyphicon glyphicon-backward"></i><spring:message code="button.back" />
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

<script type="text/javascript">
$(document).ready(function () {
$('#tablePartnersRange').DataTable({
    "bDestroy": true,
    "bPaginate": true,
    "bFilter": true,
    "bInfo": true,
    "bSort": false,
	"bSortable": false,
	"bSearching" : true,
    "iDisplayLength": 5,
    "pagingType": "simple_numbers",
    "oLanguage": {
        "sEmptyTable":" No Records Available"
    }
});

});
</script>
</html>