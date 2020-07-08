<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
<script src="<c:url value="/resources/js/clpvms/role.js" />"></script> 
<script
	src="<c:url value="/resources/js/clpvms/common.js"/>"></script>

</head>


<body>


<div class="modal fade" id="define-constant-delete" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<form:form commandName="roleForm" name="deleterole"
					id="deleterole" method="post">
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12">
								<span>
									Do you want to delete the role record "<b id="roleNameDisp"></b>" ? 
								</span>
							</div>
						</div>
						<form:hidden path="roleId" id="roleId" name="roleId"/> 
						<div class="modal-footer">
							<button type="button" onclick="goDeleteRole();"
								class="btn btn-primary"><i class="glyphicon glyphicon-trash"></i><spring:message
									code="button.delete" /></button>
							<button data-dismiss="modal" onclick="goToPrevious()" class="btn btn-primary gray-btn"><spring:message
									code="button.cancel" /></button>
						</div>

					</div>
				</form:form>
			</div>
		</div>

		<div class="modal fade" id="define-constant-approve" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<form:form commandName="roleForm" name="roleStatusApprove"
					id="roleStatusApprove" method="post">
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12">
								<label class="col-lg-3" for="Remarks"><spring:message
									code="label.Remarks" /><font color='red'>*</font></label>
									<div class="text-right mandatory-red">
										<spring:message code="label.mandatory"/>
									</div>
								<form:hidden path="roleId" id="roleId" /> 
								<form:hidden	path="status" id="approveStatus" value="APPROVED" />
								<input type="hidden" name="roleName" id="roleIdModelApproveName" /> 
								<div class="col-lg-9"> <form:textarea path="checkerRemarks" style="overflow:auto;resize:none"
									id="statusChangeDesc"  cols="25"  rows="11"  class="textarea-medium max"/>
									<div class="fieldError" id="dialogErrorApprove"></div>
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" onclick="goApproveRejectRole(this.form.id);"
								class="btn btn-primary"><i class="glyphicon glyphicon-ok-sign"></i><spring:message
									code="button.approveBtn" />
							</button>
							<button type="button" onclick="goToPrevious()" data-dismiss="modal" class="btn btn-primary gray-btn">
								<spring:message	code="button.close" /></button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
		<div class="modal fade" id="define-constant-reject" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<form:form commandName="roleForm" name="roleStatusReject"
					id="roleStatusReject" method="post">
					<div class="modal-content">


						<div class="modal-body col-lg-12">
							<div class="col-lg-12">
								<label class="col-lg-3" for="Remarks"><spring:message
									code="label.Remarks" /><font color='red'>*</font></label>
									<div class="text-right mandatory-red">
											<spring:message code="label.mandatory"/>
									</div>
									<form:hidden path="roleId" id="roleId" />
									<form:hidden path="status" id="rejectStatus" value="REJECTED" />
									 <input type="hidden" name="roleName" id="roleIdModelRejectName" /> 
								<div class="col-lg-9"> <form:textarea path="checkerRemarks" style="overflow:auto;resize:none"
										id="statusChangeDesc" name="statusChangeDesc"  cols="25"  rows="11"   class="textarea-medium max"/>
									<div class="fieldError" id="dialogErrorReject"></div>
								</div>
							</div>

						</div>
						<div class="modal-footer">

							<button type="button" onclick="goApproveRejectRole(this.form.id);"
								class="btn btn-primary"><i class="glyphicon glyphicon-remove-sign"></i><spring:message
									code="button.rejectBtn" />
									</button>
							<button type="button" onclick="goToPrevious()" data-dismiss="modal" class="btn btn-primary gray-btn"><spring:message
									code="button.closeBtn" /></button>

						</div>

					</div>
				</form:form>
			</div>
		</div>
	<div class="body-container" style="min-height: 131px; padding-top:1%">     
	<div class="container">
 <!--    <article class="col-lg-12">	 -->	
		<div id="feedBackTd" class="form-group text-center"> <!-- style="margin-top:10px"> -->
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
		</div>
		
 
		<div id="searchRole" class="graybox col-lg-5 col-lg-offset-3">
			<form:form action="searchRoleByName" method="POST" commandName="roleForm"
				id="roleForm" name="roleForm">
				<div class="col-lg-12">
					<h3>
						<spring:message code="header.role.search" text="Search Role" />
					</h3>
				</div>
				<div class="col-lg-12">

					<!-- <div class="row space"> -->
						<div class="col-lg-4">
							<label for="search_alcode"><spring:message
									text="Enter Role Name" code="Role.searchName" /></label>
						</div>

						<div class="col-lg-6">
							<form:input title="Allowed Special Characters are ,;'_- ." path="roleName" class="textbox textbox-xlarge trim"
								 name="roleName" id="roleName"
								onkeyup="return isAlphaNumericWithSpecialChars(this)" maxlength="100" />
							<form:errors path="roleName" cssClass="fieldError" />
							<form:input path="roleId" name="roleId"
								id="roleId" type="hidden"/>
							<input type="hidden" name="deleteRoleName"
								id="deleteRoleName" />
						</div>
							
						<div class="col-lg-2">
						<!-- 	<p class="text-center"> -->
								<button type="submit" class="btn btn-primary"
									onclick="searchRole()">
									<i class='glyphicon glyphicon-search'></i>
									<spring:message text="Search" code="button.search" />
								</button>
							<!-- </p> -->
						</div>
					<!-- </div> -->
					 <div class="col-lg-12 text-center"> 
						<label>	<spring:message	text="Hint : Empty search retrieves all roles"
										code="Role.hint" /></label></div>
					<br />
				</div>
			</form:form>
		</div>
  <!--   </article> -->
		<article class="col-lg-12">
	<div class="col-lg-12 text-right">
					 <security:authorize access="hasRole('ADD_ROLE')">
					<button type="button" class="btn btn-primary btn-add " onclick="callAddPage()">
						<i class='glyphicon glyphicon-plus'></i>
						<spring:message code="header.role.add" />
					</button>
					</security:authorize>
			</div>
	</article>
				<c:if test="${showGrid!=null && showGrid == 'true' }">

				<div class="group">


					<table id="tableViewUsers"
						class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
						style="width: 100% !important;">


						<thead class="table-head">
							<tr>
								<th><spring:message code="Role.TabRoleName"
										text="Role Name" /></th>

								<th><spring:message code="Role.TabDesc"
										text="Description" /></th>
								<th><spring:message code="Role.TabPermission"
										text="Permission" /></th>

								<th><spring:message code="Role.TabStatus"
										text="Status" /></th>
								<th style="width:28em"><spring:message code="Role.TabAction"
										text="Action" /></th>
							</tr>
						</thead>
						<tbody class="row">

							<c:forEach items="${roleList}" var="roleDetails"
								varStatus="status">
								<tr id="${roleDetails.roleId}">
									<td >${roleDetails.roleName }</td>
								    <td>${roleDetails.roleDesc }</td>
								    <td> <c:forEach items="${roleDetails.permissions}" var="permission" varStatus="perm">
       
   ${permission.permissionName} | </c:forEach></td>										
								    <td id="${roleDetails.status}">${roleDetails.status}</td>
									<td id="${roleDetails.action}">
				<c:if test="${roleDetails.status =='NEW'}">
				<c:if test="${roleDetails.insUser!=loginUserId}">
						<security:authorize access="hasRole('APPROVE_ROLE')">
						<button type="submit" class="btn btn-primary-table-button green-btn"
												data-toggle="modal" data-target="#define-constant-approve"
												onclick="clickEditOrDelete(this,'roleStatusApprove');">
												<i class="glyphicon glyphicon-ok-sign"></i>
												<spring:message code="button.approveBtn" />
						</button>
						</security:authorize>					
											
						<security:authorize access="hasRole('APPROVE_ROLE')">
						<button type="submit" class="btn btn-primary-table-button red-btn" data-toggle="modal"
												data-target="#define-constant-reject"
												onclick="clickEditOrDelete(this,'roleStatusReject');">
												<i class="glyphicon glyphicon-remove-sign"></i>
												<spring:message code="button.rejectBtn" />
						</button>
						</security:authorize>
						</c:if>
						</c:if>
						
						<security:authorize access="hasRole('EDIT_ROLE')">
						<button type="button" class="btn btn-primary-table-button"
												onclick="return clickEditOrDelete(this,'roleForm');">
												<i class="glyphicon glyphicon-edit"></i>
												<spring:message code="button.edit" />
						</button>
						</security:authorize>
							
							<security:authorize access="hasRole('VIEW_ROLE')">	
								<button type="button" class="btn btn-primary-table-button"
												onclick="return clickViewToSearch(this,'roleForm');">
												<i class="glyphicon glyphicon-list"></i>
												<spring:message code="button.view" />
								</button>
								</security:authorize>
						<security:authorize access="hasRole('DELETE_ROLE')">							
						<button type="submit" class="btn btn-primary-table-button" data-toggle="modal" data-target="#define-constant-delete"
												onclick="clickEditOrDelete(this,'deleterole');">
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
</div>
</div>
</body>

</html>