<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<script src="<c:url value="/resources/js/clpvms/common.js"/>"></script>
<script src="<c:url value="/resources/js/clpvms/groupAccessId.js"/>"></script>

</head>
<body>
	<article class="col-lg-12">
	<c:if test="${statusMessage!='' && statusMessage!=null}">
		<div id="errmessage" class="error-red text-center col-lg-12">
			<b>${statusMessage}</b>
		</div>
	</c:if>


	<c:if test="${status!='' && status!=null}">
		<div class="col-lg-12 text-center success-green" id="message">
			<b>${status}</b>
		</div>
	</c:if>
	</article>

	<div class="modal fade" data-target="#define-constant-delete"
		id="define-constant-delete" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<form:form commandName="groupAccessIdSearch" name="deleteGroupAccess"
				id="deleteGroupAccess" method="post">
				<div class="modal-content">
					<div class="modal-body col-lg-12">
						<div class="col-lg-12">
							<span> Do you want to delete the Group Access '<b id="groupAccessNameDisp"></b>' record Assigned to Product '<b id="groupAccessProductDisp"></b>'
								 ?
							</span>
						</div>

					</div>
					<form:hidden path="groupAccessId" id="groupAccessModelDelete" />
					<form:hidden path="productId" id="productId" />

					<input type="hidden" name="groupAccessName"
						id="groupAccessModelDeleteName" />
					<div class="modal-footer">
						<button type="button" onclick="goDeleteGroupAccess();"
							class="btn btn-primary">
							<i class="glyphicon glyphicon-trash"></i>
							<spring:message code="button.delete" />
						</button>
						<button data-dismiss="modal" onclick="goToPrevious()"
							class="btn btn-primary gray-btn">
							<spring:message code="button.cancel" />
						</button>

					</div>

				</div>
			</form:form>
		</div>
	</div>




	<form:form name="groupAccessIdSearch"
		class='form-horizontal groupAccessIdSearch'
		commandName="groupAccessIdSearch" id="groupAccessIdSearch"
		method="POST"
		action='${pageContext.request.contextPath}/config/groupAccessId/searchGroupAccessId'>
		<div class="container">
			<section class="content-container">
				<article class="col-lg-12">

					<div class="graybox col-lg-5 col-lg-offset-3">
						<div class="col-lg-12">
							<h3>
								<spring:message code="groupAccessId.ForSearchLabel" />
							</h3>
						</div>
						<div class="col-lg-12">
							<div class="col-lg-5">
								<label for="EnterGroupAccessName"><spring:message
										code="groupAccessId.Label_enterGroupAccessName" /></label>
							</div>
							<div class="col-lg-5">
								<form:input path="groupAccessName" id="groupAccessName"
									onkeyup="return isAlphaNumericWithSpace(this)" maxlength="50"
									autocomplete="off" style="width:auto"/>
								<div>
									<form:errors path="groupAccessName" id="groupAccessNameErr"
										cssStyle="color:red" />
								</div>
							</div>
						</div>

						<div class="col-lg-12 col-lg-offset-10">
							<button type="submit" name="button_Search"
								class="btn btn-primary" id="search_submit">
								<i class="glyphicon glyphicon-search"></i>
								<spring:message code="groupAccessId.button_Search" />
							</button>
						</div>
						<div class="col-lg-12">
							<div class="col-lg-5">
								<label for="EnterProductName"><spring:message
										code="groupAccessId.Label_ProductName" /></label>
							</div>
							<div class="col-lg-5">
								<form:input id="productName" path="productName" maxlength="100"
									onkeyup="return isAlphaNumericWithSpace(this)"
									autocomplete="off" style="width:auto"/>
								<div>
									<form:errors path="productName" id="productNameErr"
										cssStyle="color:red" />
								</div>
							</div>
						</div>


						<div class="col-lg-12 text-center">
							<label> <spring:message
									text="Hint:Empty search retrieves the list of all Group Access Names available"
									code="groupAccessId.hint" /></label>
						</div>
					</div>

				</article>
			</section>
		</div>

	<security:authorize access="hasRole('ADD_ASSIGNGROUPACCESS_PRODUCT')">
		<article class="col-lg-12">
			<div class="pull-right">
				<button type="button" class="btn btn-primary btn-add"
					onclick="dispAssignProduct()">
					<i class='glyphicon glyphicon-plus'></i>
					<spring:message code="groupAccessId.assignProductLabel" />
				</button>
			</div>
		</article>
	</security:authorize>

	<security:authorize access="hasRole('ADD_GROUPACCESS')">
		<article class="col-lg-12">
			<div class="pull-right">
				<button type="button" class="btn btn-primary btn-add"
					onclick="dispAddGroupAccessId()">
					<i class='glyphicon glyphicon-plus'></i>
					<spring:message code="groupAccessId.addOrUpdategroupAccessIdLabel" />
				</button>
			</div>
		</article>
	</security:authorize>


		<form:hidden path="groupAccessId" id="groupAccessId" />
		<form:hidden path="productId" id="productIdGrid" />

		<c:if test="${showGrid =='true' }">

			<div class="group">
				<table id="tableViewUsers"
					class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
					style="width: 100% !important;">

					<thead class="table-head">
						<tr>
							<th><spring:message code="groupAccessId.TabGroupAccessName"
									text="Group Access Name" /></th>

							<th><spring:message code="groupAccessId.TabProductName"
									text="Product Name" /></th>
							<th style="width: 28em"><spring:message
									code="groupAccessId.TabAction" text="Action" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${groupAccessIdsList}"
							var="groupAccessIdDetails" varStatus="status">

							<tr
								id="${groupAccessIdDetails.groupAccessId}-${groupAccessIdDetails.groupAccessName}-${groupAccessIdDetails.productName}">
								<td class="dont-break-out">${groupAccessIdDetails.groupAccessName}</td>

								<td class="dont-break-out"
									id="${groupAccessIdDetails.productName}">${groupAccessIdDetails.productId}:${groupAccessIdDetails.productName}</td>
								<td>
								
								<security:authorize access="hasRole('EDIT_ASSIGNGROUPACCESS_PRODUCT')">
									<button type="button" class="btn btn-primary-table-button"
										onclick="clickEdit(${groupAccessIdDetails.groupAccessId}+'-'+${groupAccessIdDetails.productId});">
										<i class="glyphicon glyphicon-edit"></i>
										<spring:message code="groupAccessId.editBtn" />
									</button>
								</security:authorize>
								<security:authorize access="hasRole('VIEW_ASSIGNGROUPACCESS_PRODUCT')">
									<button type="button" class="btn btn-primary-table-button"
										onclick="clickViewForGroupAcess(${groupAccessIdDetails.groupAccessId}+'-'+${groupAccessIdDetails.productId});">
										<i class="glyphicon glyphicon-list"></i>
										<spring:message code="groupAccessId.viewBtn" />
									</button>
									</security:authorize>
									
 								<security:authorize access="hasRole('DELETE_GROUPACCESS_PRODUCT')">
									<button type="button" class="btn btn-primary-table-button"
										data-toggle="modal" data-target="#define-constant-delete"
										onclick="goStatusChange(this,'${groupAccessIdDetails.productId}');">
										<i class="glyphicon glyphicon-trash"></i>
										<spring:message code="button.delete" />
									</button>
								</security:authorize>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:if>

	</form:form>
</body>
</html>