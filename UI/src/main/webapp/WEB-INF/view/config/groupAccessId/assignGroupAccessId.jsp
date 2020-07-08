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
		<form:form name="assignGroupAccessId" id="assignGroupAccessId"  action="#" method="POST" class='form-horizontal ' commandName="assignGroupAccessId">
			<section class="content-container">
           		<article class="col-lg-12">

				<ul class="nav nav-tabs col-lg-7 col-lg-offset-2">
					<li class="active SubMenu"><a href="#assignGroupAccessId" data-toggle="tab">
					<i class="glyphicon glyphicon-tags"></i> <spring:message code="groupAccessId.assignGroupAccessIdLabel" /></a></li>
				</ul>
								
				<div class="tabresp tab-content">
				  <div class="tab-pane fade in active graybox col-lg-7 col-lg-offset-2" id="groupAccessId123">
					
						<div class="text-right mandatory-red">
						<spring:message code="label.mandatory" text="*Mandatory" />
						</div>
						<article class="col-lg-12">											
						<c:if test="${statusMessage!='' && statusMessage!=null}">	
						<div class="text-center error-red " id="serviceErrMsg">
						<b>${statusMessage}</b>
						</div>	
						</c:if>
											
						<c:if test="${status!='' && status!=null}">
						<div class="text-center success-green">
						<b>${status}</b>
						</div>	
						</c:if>
						</article>
						<!-- This hidden is used to send the server url to the js file starts -->
						<input type="hidden" id="srvUrl" value="${srvUrl}"/>
						<!-- This hidden is used to send the server url to the js file ends -->
						<div class="col-lg-12">
								<div class="col-lg-6">
								<label for="EnterGroupAccessName"><spring:message code="groupAccessId.groupAccessName" /><font color='red'>*</font></label>
								</div>
		
								<div class="col-lg-6">
								<form:select path="groupAccessId" id="groupAccessId" class="dropdown-medium" 
								onblur="validateDropDownForGroupAccessFields(this.form.id,this.id)" 
								onchange="getGroupAccessProducts()" >
								 <form:option value="-1" label="- - - Select - - -"/> 
			   					<c:forEach items="${groupAccessMap}" var="groupAccess">
											<option value="${groupAccess.groupAccessId}">${groupAccess.groupAccessName}</option>
										</c:forEach>
								</form:select>	
										<div>
										<form:errors path="groupAccessId" cssClass="fieldError"/>
										</div>
								</div>
							</div>
							
							<div class="col-lg-12">
								<div class="col-lg-6">
								<label for="EnterProductName"><spring:message code="groupAccessId.productName" /><font color='red'>*</font></label>
								</div>
		
								<div class="col-lg-6">
								<form:select path="productId" id="productId" class="dropdown-medium" onchange="clearDataTable()" onblur="validateDropDownForGroupAccessFields(this.form.id,this.id)" >
								 <form:option value="-1" label="- - - Select - - -"/> 
								 <div id="productOptions"></div>
								</form:select>	
										<div>
										<form:errors path="productId" cssClass="fieldError"/>
										</div>
								</div>
								
							</div>
										<div class="col-lg-12  text-center">
										<button type="button" class="btn btn-primary btn-normal" onclick="getGroupAccessPartners(this.form.id,event);" > 
											<i class="glyphicon glyphicon-search"></i>
												<spring:message code="button.fetch" />
											</button>
											 <button type="button" class="btn btn-primary gray-btn" id="backAfterFetchBtn"  onclick="backToAssignGroupAccess();">
									<i class="glyphicon glyphicon-backward"></i><spring:message code="button.back" />
									</button>	
							</div>
							<div id= "fetchId"  class="col-lg-12 text-center">
							
						
				
				<div class="group" id="dataTableDiv" style="display: none;">
				
			<table id="tablePartnersRange" class="table table-hover table-striped table-bordered dataTable datagridwithsearch" 
			style="width: 100% !important;" ><thead class='table-head'>
			<tr><th><spring:message code='groupAccessId.TabGroupAccessName' text='Group Access Name' /></th>
			<th><spring:message code='groupAccessId.TabPartnerName' text='Partner Name' /></th>
			<th><spring:message code='groupAccessId.TabFirstParty' text='Party Type' /></th></tr>
			</thead><tbody class='row'>
						
						</tbody>
					</table>
				
				</div>			
								
							<div class="col-lg-12 text-center" id ="buttonsForAssign" style="display:none;"  >
							<br>
									<button type="button"  onclick="saveAssingGroupAccess(this.form.id,event)" class="btn btn-primary" >
									<i class="glyphicon glyphicon-plus"></i><spring:message code="CardRange.addBtn" />
									</button>
									
									<button type="button" onclick="goResetAssingGroupAccess();" class="btn btn-primary gray-btn">
									<i class="glyphicon glyphicon-refresh"></i><spring:message code="button.reset" />
									</button>
														
									<button type="button" class="btn btn-primary gray-btn" onclick="backToAssignGroupAccess();">
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
<script>
$('#multiselect').multiselect();
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
		"pagingType": "full_numbers",
	    "oLanguage": {
	        "sEmptyTable":" No Records Available"
	    },
	"processing": true,
    "serverSide": true,
    "deferLoading": 57
	});

	});
</script>
</html>