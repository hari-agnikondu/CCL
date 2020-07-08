<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>

<script src="<c:url value="/resources/js/clpvms/package.js" />"></script>

<div class="container">


	<div class="row-fluid">

		<div class="form-group text-center" id="error"></div>

		<table style="text-align: center width: 90%;">
			<tr>
				<td id="selectionError"></td>
			</tr>
		</table>
		<div class="aln">
			<div>
				<center>
					<h6>
						<font class="errormsg"><b>${statusMessage}</b></font>
					</h6>
				</center>
			</div>
			<div>
				<center>
					<h6>
						<font style="color: green;"><b>${status}</b></font>
					</h6>
				</center>
			</div>
		</div>
		<span id='hidemydata'> <c:if test="${successstatus !=''}">
				<h4>
					<p class="">
						<b><font size="3"><center style="color: green;">
									<strong>${successstatus}</strong>
								</center></font></b>
					</p>
				</h4>
			</c:if> <c:if test="${failstatus !=''}">
				<h4>
					<p class="">
						<b><font size="3"><center style="color: red;">
									<strong>${failstatus}</strong>
								</center></font></b>
					</p>
				</h4>
			</c:if>
		</span>
		<form:form id="packageForm" method="post"
			commandName="packageForm">
			<div class="graybox col-lg-6 col-lg-offset-3">

				<div class="box-title">
					<h3>
						<spring:message code="package.searchPackage" />
					</h3>
				</div>
				<div class="form">
					<div class="col-lg-12">
						<div class="col-lg-4 space ">
							<label for="search_packageID"><spring:message
									code="package.searchPackageName" /></label>
						</div>

						<div class="col-lg-4">

							<form:input path="description" id="description"
								class="textbox xlarge4label" name='search_description'
								onkeyup="return allowSpecificspecialchars(this)" type="textarea"
								maxlength="200" />
							<font color="red"><span id="descriptionError">
							</span></font>
							<div>
								<form:errors path="description" id="description"
									class="textbox xlarge4label" name='search_description'
									cssStyle="color:red" />
							</div>

						</div>
						<div class="col-lg-3">
							<p class="text-right">
								<button type="submit" class="btn btn-primary"
									onclick="searchPackage(event)">
									<i class='glyphicon glyphicon-search'></i>
									<spring:message text="Search" code="button.search" />
								</button>
							</p>
						</div>
					</div>
					<div class="row space">
						<div class="col-lg-6 col-lg-offset-4">
							<label> <spring:message code="package.serachHint" /></label>
						</div>
					</div>
					  <input id="packageId" name="packageId"
						type="hidden" value="" /> <input id="searchType" name="searchType"
						type="hidden" value="${SearchType}" /> <input id="searchedName"
						name="searchedName" type="hidden" value="" /> <input
						type="hidden" name="jsPath" id="jsPath"
						value="${pageContext.request.contextPath}/resources/JS_Messages/" />
				</div>
				<br />

			</div>
		</form:form>
	</div>
</div>

<div class="pull-right">
 <security:authorize access="hasRole('ADD_PACKAGEID')">
	<button type="button" class="btn btn-primary"
		style="bottom: 15px; position: relative;"
		onclick="showAddPackage()">
		<i class='glyphicon glyphicon-plus'></i>
		<spring:message code="package.button.addPackage" />
	</button>
	</security:authorize>
</div>
<c:if test="${showGrid =='true' }">




	<div class="group">

		<h3>
			<i class="icon-table"></i>
		</h3>

		<div id="tableDiv">
			<table id=""
				class="table dataTable table-hover table-striped table-bordered datagridwithsearch "
				style="width: 100% !important;">


				<thead class="table-head">
					<tr>
						<th><spring:message code="package.packageId" /></th>
						<th><spring:message code="package.packageDescription" /></th>
						<th><spring:message code="package.replacementPackageId" /></th>
						<th><spring:message code="package.packagefulFillmentID" /></th>
						<th><spring:message code="fullFillment.action" /></th>
					</tr>
				</thead>
				<tbody class="row">

					<c:forEach items="${pkgTblList}" var="requesterDetails"
						varStatus="status">
						<tr>
							<td class="dont-break-out">${requesterDetails.packageId}</td>
							<td class="dont-break-out">${requesterDetails.description}</td>
							<td class="dont-break-out">${requesterDetails.replacementPackageId}</td>
							<td class="dont-break-out">${requesterDetails.fulfillmentId}</td>
							<td>
							 <security:authorize access="hasRole('EDIT_PACKAGEID')">
							<button type="submit" class="btn btn-primary-table-button"
									id="search_submit"
									onclick="editPackage('${requesterDetails.packageId}');">
									<i class='glyphicon glyphicon-edit'></i><spring:message code="button.edit" />
								</button>
								</security:authorize>
								 <security:authorize access="hasRole('VIEW_PACKAGEID')">
								<button type="submit" class="btn btn-primary-table-button"
									id="search_submit"
									onclick="viewPackage('${requesterDetails.packageId}');">
									<i class='glyphicon glyphicon-list'></i><spring:message code="button.view" />
								</button>
								</security:authorize>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</c:if> 