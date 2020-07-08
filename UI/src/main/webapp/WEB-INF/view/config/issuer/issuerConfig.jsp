<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>


<script src="${pageContext.request.contextPath}/resources/js/clpvms/issuer.js"></script>
<body>

       <c:if test="${successstatus !=''}">
						<div class="col-lg-12 success-green  text-center" id="message" >
						<b>${successstatus}</b>
					</div>
			
			</c:if> 
			<c:if test="${failstatus !=''}">
			
					
					<div id="errmessage" class="error-red col-lg-12 text-center">
						<b>${failstatus}</b>
					</div>
				
			</c:if>
		<!-- </span> -->
		<!-- </div> -->
		
<div class="modal fade" id="define-constant-delete" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				 <form:form commandName="deleteBox" name="deleteCardRange"
					id="deleteCardRange" method="post">
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12" style="display:inline-block">
								<span style="width: 100%; display: inline-block;word-wrap: break-word;">
								Do you want to delete the Issuer record "<b id="issuerNameDisp"></b>" ? 
								</span>
							</div>

						</div>
						<form:hidden path="" id="issuerIdtoDelete" /> 
						 <input type="hidden" name="issuerName" id="issuerIdtoDelete" /> 
						<div class="modal-footer">
							<button type="button" onclick="deleteIssuer();"
								class="btn btn-primary"><i class="glyphicon glyphicon-trash"></i><spring:message
									code="CardRange.deleteBtn" /></button>
							<button data-dismiss="modal" onclick="goToPrevious()" class="btn btn-primary gray-btn"><spring:message
									code="button.cancel" /></button>

						</div>

					</div>
				</form:form> 
			</div>
		</div>

<!-- delete box ends -->

	<div class="">

		
		<!-- graybox -->
		<!-- <div class="graybox col-lg-5 form-col-6 sb3"> -->
<div class="body-container" style="min-height: 131px;">   
<div class="container">

	 <article class="col-lg-12"> 
		<div class="graybox col-lg-5 col-lg-offset-3 ">

			<form:form id="issuerForm" method="post" commandName="issuerForm">

				<div class="col-lg-12">
					<h3>
						<spring:message code="issuer.searchIssuer" />
					</h3>
				</div>

				<!-- <div class="form"> -->

					<div class="col-lg-12">
                     <div class="">
						<div class="col-lg-3  ">
							<label for="search_requesterName"> <spring:message
									code="issuer.enterIssuerName" /><font color='red'></font></label>

						</div>



						<div class="col-lg-6">

							<form:input title="Allowed Special Characters are .,;'_- " path="issuerName" id="issuerNameId"
								class="textbox xlarge4label" name='search_issuerName'
								onkeyup="return isAlphaNumericWithSpace(this)" type="textarea" maxlength="100" />
							<%-- <label><span><spring:message code="issuer.hint" /></span></label> --%>
							<font color="red"><span id="issuerNameIdError"> </span></font>
							<div>
								<form:errors path="issuerName" id="issuerNameId"
									class="textbox xlarge4label" name='search_issuerName'
									cssStyle="color:red" />
							</div>

						</div>
						<div class="col-lg-3 text-right">
							<!-- <p class="text-center"> -->
								<button type="submit" onclick="searchIssuer(event)"
									class="btn btn-primary" id="search_submit">
									<i class="glyphicon glyphicon-search"></i>
									<spring:message code="button.search" />
								</button>
							<!-- </p> -->

						</div>

						<!-- <div class=""> -->
							<div class="col-lg-12 text-center">
							
								<label> <spring:message code="issuer.hint" /></label>
							</div>
						<!-- </div> -->

						<input id="issuerId" name="issuerId" type="hidden" value="" /> <input
							id="searchType" name="searchType" type="hidden"
							value="${SearchType}" /> <input id="searchedName"
							name="searchedName" type="hidden" value="" /> <input
							id="retrievedName" name="retrievedName" type="hidden"
							value="${SearchedName}" /> <input id="deletedName"
							name="deletedName" type="hidden" value="" /> <input
							type="hidden" name="jsPath" id="jsPath"
							value="${pageContext.request.contextPath}/resources/JS_Messages/" />
					</div>
					<br />
</div>
				<!-- </div> -->
		</div>
		
		</article>
		</form:form>

	



	<!-- <div class="pull-right"> -->
	<div class="col-lg-12 text-right">
		<security:authorize access="hasRole('ADD_ISSUER')">
		<button type="button" class="btn btn-primary btn-add"
			style="" onclick="clickAddIssuer()">
			<i class='glyphicon glyphicon-plus'></i>
			<spring:message code="issuer.button.addIssuer" />
		</button>
		</security:authorize>
	</div>






<c:if test="${showGrid =='true' }">




	<div class="group">

		<h3>
			<i class="icon-table"></i>
		</h3>

		<div id="tableDiv">
			<!-- <table id="tableView" class="table table-hover table-striped table-bordered" style="width: 100% !important;"> -->
			<table id=""
				class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
				style="width: 100% !important;">
				


				<thead class="table-head">
					<tr>
						<%-- <th style="width:20em;"><spring:message code="issuer.issuerName" /></th>
						<th style="width:30em;"><spring:message code="issuer.description" /></th>
						<th style="width:6em;"><spring:message code="issuer.isActive" /></th>
						<th style="width:10em;"><spring:message code="issuer.mdmId" /></th>
						<th style="width:15em;" ><spring:message code="issuer.action" /></th>
 --%>
 <th><spring:message code="issuer.issuerName" /></th>
						<th><spring:message code="issuer.description" /></th>
						<th><spring:message code="issuer.isActive" /></th>
						<%-- <th><spring:message code="issuer.mdmId" /></th> --%>
						<th><spring:message code="issuer.action" /></th>
 
					</tr>
				</thead>
				<tbody class="row">

					<c:forEach items="${issuerTableList}" var="issuerDetails"
						varStatus="status">
						<tr id="${issuerDetails.issuerId}">
							<td class="dont-break-out">${issuerDetails.issuerName}</td>
							<td class="dont-break-out">${issuerDetails.description}</td>
							<c:if test="${issuerDetails.isActive =='Y' || issuerDetails.isActive =='1'}">
							<td class="dont-break-out">YES</td>
							</c:if>
							
							
							<%-- <td>${issuerDetails.mdmId}</td> --%>
							<td>
								<security:authorize access="hasRole('EDIT_ISSUER')">
								<button type="submit" class="btn btn-primary-table-button" id="search_submit"
									onclick="goEdit(${issuerDetails.issuerId});">
									<i class='glyphicon glyphicon-edit'></i>
									<spring:message code="button.edit" />
								</button>
								</security:authorize>
								
								
								<security:authorize access="hasRole('VIEW_ISSUER')">
								<button type="view" class="btn btn-primary-table-button" id="search_submit"
									onclick="goView(${issuerDetails.issuerId});">
									<i class='glyphicon glyphicon-list'></i>
									<spring:message code="button.view" />
								</button>
								</security:authorize>
								<c:set var = "string1" value = "${issuerDetails.issuerName}"/>
								
			<c:set var = "string2" value ="${fn:replace(string1,'\\'','*')} "/> 
								<security:authorize access="hasRole('DELETE_ISSUER')">
								<button type="submit" class="btn btn-primary-table-button" id="search_submit" data-toggle="modal" data-target="#define-constant-delete"
									onclick="goDelete('${issuerDetails.issuerId}~${string2}')">
									<i class='glyphicon glyphicon-trash'></i>
									<spring:message code="button.delete" />
								</button>
								</security:authorize>
								
							</td>


						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<div id="tableViewUsersStatus">
			<br>
		</div>

	</div>

</div>
</div>
</div>

</c:if>

</section>
</div>
</body>
