<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<script src="<c:url value="/resources/js/clpvms/group.js"/>"></script>
</head>

<body>
	<c:if test="${statusMessage!='' && statusMessage!=null}">
		<br>
		<div id="errmessage" class="error-red col-lg-12 text-center">
			<b>${statusMessage}</b>
		</div>
	</c:if>

	<c:if test="${status!='' && status!=null}">
		<br>
		<div class="col-lg-12 success-green text-center" id="message">
			<b>${status}</b>
		</div>
	</c:if>

	<div class="modal fade" id="define-constant-approve" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<form:form commandName="groupSearch" name="groupStatus"
				id="groupStatus" method="post">
				<div class="modal-content">


					<div class="modal-body col-lg-12">
						<div class="col-lg-12">
							<label class="col-lg-3" for="Remarks"><spring:message
									code="Group.Remarks" /><font color='red'>*</font></label>
							<div class="text-right mandatory-red">
								<spring:message code="label.mandatory" />
							</div>

							<form:hidden path="groupId" id="groupIdModel" />
							<form:hidden path="groupStatus" id="approveStatus" value="APPROVED" />

						<span class="col-lg-9">	<form:textarea path="groupCheckerRemarks"
								style="overflow:auto;resize:none" id="statusChangeDesc"
								maxlength="255" cols="25" rows="11" />
						<br>
							<span id="dialogErrorApprove"> </span>
							
							</span>
							</div>
													
						</div>

					
					<div class="modal-footer">

						<button type="button" onclick="approveGroup();"
							class="btn btn-primary">
							<i class="glyphicon glyphicon-ok-sign"></i>
							<spring:message code="Group.approveBtn" />
						</button>

						<button type="button" onclick="goToPrevious()"
							data-dismiss="modal" class="btn btn-primary gray-btn">
							<spring:message code="Group.closeBtn" />
						</button>

					</div>

				</div>
			</form:form>
			</div>
		</div>
	

	<div class="modal fade" id="define-constant-reject" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<form:form commandName="groupSearch" name="groupReject"
					id="groupStatusReject" method="post">
					<div class="modal-content">

						<div class="modal-body col-lg-12">
							<div class="col-lg-12">
								<label class="col-lg-3" for="Remarks"><spring:message
									code="Group.Remarks" /><font color='red'>*</font></label>
									<div class="text-right mandatory-red">
											<spring:message code="label.mandatory"/>
									</div>
									<form:hidden path="groupId" id="groupIdModelReject" />
									<form:hidden path="groupStatus" id="rejectStatus" value="REJECTED" />
								
								<span class="col-lg-9">	<form:textarea path="groupCheckerRemarks" style="overflow:auto;resize:none"
										id="statusChangeDescReject" maxlength= "255" cols="25"  rows="11"  
										 />
										<br>
										<span id="dialogErrorReject"></span>
										</span>
							</div>

						</div>
						<div class="modal-footer">

							<button type="button" onclick="rejectGroup();"
								class="btn btn-primary"><i class="glyphicon glyphicon-remove-sign"></i><spring:message
									code="Group.rejectBtn" />
									</button>
							<button type="button" onclick="goToPrevious()" data-dismiss="modal" class="btn btn-primary gray-btn"><spring:message
									code="Group.closeBtn" /></button>

						</div>

					</div>
				</form:form>
			</div>
		</div>
		

	<div class="modal fade" id="define-constant-delete" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<form:form commandName="groupSearch" name="groupSearchDelete"
				id="groupSearchDelete" method="post">
				<div class="modal-content">
					<div class="modal-body col-lg-12">
						<div class="col-lg-12">
							<span> Do you want to delete the Group '<b
								id="displayName"></b>' ?
							</span>
						</div>
						
						<form:hidden path="groupId" id="groupIdModelDelete" />						
						<input type="hidden" name="groupModelDeleteName" id="groupModelDeleteName" />
						
						
					</div>
				
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" onclick="deleteGroupDet();">
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


	<div class="body-container">
		<div class="container">
			<form:form name="groupSearch"
				action='${pageContext.request.contextPath}/admin/group/searchAndDispGroup'
				class='form-horizontal groupSearch' commandName="groupSearch"
				id="groupSearch" method="POST">
				<section class="content-container">
					<article class="col-lg-12">
						<input type="hidden" name="groupId" id="groupId"/>

						<div class="graybox col-lg-5 col-lg-offset-3">
							<div class="col-lg-12">
								<h3>
									<spring:message code="group.searchLabel" />
								</h3>

							</div>

							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for="EnterGroupName"><spring:message
											code="group.groupNameEnter" /></label>
								</div>
								<div class="col-lg-4">
									<form:input  onpaste="false" path="groupName" class="trim"  
									onkeyup="return isAlphabetsWithSpaceAndUnderScore(this);"
									onblur="validateGroupNameSearch(this.form.id,this.id)"
									id="groupNameSearch" maxlength="100"	autocomplete="off" style="width:auto" />
									

								</div>

								<div class="col-lg-4 text-right">
									<button type="submit" name="button_Search"
										class="btn btn-primary" id="search_submit" >
										<i class="glyphicon glyphicon-search"></i>
										<spring:message code="Group.button_Search" />
									</button>
								</div>
							</div>

							<div class="col-lg-12 text-center">
								<label> <spring:message
										text="Hint: Empty search retrieves all Groups"
										code="group.hint" /></label>
							</div>

						</div>
						 <security:authorize access="hasRole('ADD_GROUP')">
						<article class="col-lg-12">
							<div class="col-lg-12">

								<div class="pull-right">

									<button type="button"  class="btn btn-primary btn-add"
										onclick="addGroup()">
										<i class='glyphicon glyphicon-plus'></i>
										<spring:message code="group.addGroupLabel" text="Add Group" />
									</button>
								</div>
							</div>
						</article>
						</security:authorize>


						<c:if test="${showGrid =='true' }">
					<div class="group">
							<table id="tableViewUsers"
								class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
								style="width: 100% !important;">

								<thead class="table-head">
									<tr>
										<th><spring:message code="group.groupName"
												text="Group Name" /></th>
										<th><spring:message code="group.roleName"
												text="Role Name" /></th>

										<th><spring:message code="group.status" text="Status" /></th>
										<%-- <th><spring:message code="group.createdUser" text="Created User" /></th>
										<th><spring:message code="group.approvedUser" text="Approved / Rejected User" /></th> --%>
										<th style="width:28em"><spring:message code="group.action" text="Action" /></th>
									</tr>
								</thead>
								<tbody>

									<c:forEach items="${groupList}" var="groupList"
										varStatus="status">
																
										<tr id="${groupList.groupId}-${groupList.groupName}">
											<td id="${groupList.groupName}">${groupList.groupName}</td>
											<td id="${groupList.roleName}"> 	
											<c:forEach items="${groupList.roles}" var="roles" varStatus="status">
											<c:set var="roleTrimmedText" value="${fn:trim(roles.roleName)}" />
											<c:if test="${roleTrimmedText!=null && roleTrimmedText!=''}">
												<c:set var="rolesVal" value="${status.first ? '' : '|'} ${roleTrimmedText}" />
												<c:out value="${rolesVal}" /> 
												</c:if>
												</c:forEach>
											</td>
											<td id="${groupList.groupStatus}">${groupList.groupStatus}</td>
											<%-- <td id="${groupList.insUser}">${groupList.insUser}</td>
											
											<td id="${groupList.lastUpdateUser}">
											<c:if test="${groupList.groupStatus =='APPROVED' || groupList.groupStatus =='REJECTED' }">
											${groupList.lastUpdateUser}</c:if></td> --%>
											
											<td><c:if test="${groupList.groupStatus =='NEW' && groupList.insUser!=loginUserId }">
										 	<security:authorize access="hasRole('APPROVE_GROUP')">
												<button type="button"
													class="btn btn-primary-table-button green-btn"
													data-toggle="modal" data-target="#define-constant-approve"
													onclick="updateGroupStatus(this,'${groupList.groupId}');">
													<i class="glyphicon glyphicon-ok-sign"></i>
													<spring:message code="Group.approveBtn" />
												</button>
												</security:authorize>
												
												<security:authorize access="hasRole('APPROVE_GROUP')">
												<button type="button"
													class="btn btn-primary-table-button red-btn"
													data-toggle="modal" data-target="#define-constant-reject"
													onclick="updateGroupStatus(this,'${groupList.groupId}');">
													<i class="glyphicon glyphicon-remove-sign"></i>
													<spring:message code="Group.rejectBtn" />
												</button>
												</security:authorize>
											 </c:if> 
											 
											 <c:if test="${groupList.groupStatus =='REJECTED' && groupList.insUser==loginUserId}">
											 <security:authorize access="hasRole('EDIT_GROUP')">
												<button type="button" class="btn btn-primary-table-button"
													onclick="clickEditForGroup('${groupList.groupId}');">
													<i class="glyphicon glyphicon-edit"></i>
													<spring:message code="button.edit" />
												</button>
												</security:authorize>
												</c:if> 
												<security:authorize access="hasRole('VIEW_GROUP')">
												<button type="button" class="btn btn-primary-table-button"
													onclick="clickViewForGroup('${groupList.groupId}');">
													<i class="glyphicon glyphicon-list"></i>
													<spring:message code="button.view" />
												</button>
												</security:authorize>
												<c:if test="${groupList.groupStatus =='REJECTED' && groupList.insUser==loginUserId}">
												<security:authorize access="hasRole('DELETE_GROUP')">
												<button type="button" class="btn btn-primary-table-button"
													onclick="deleteGroup(this,'${groupList.groupId}');" data-toggle="modal"
													data-target="#define-constant-delete">
													<i class="glyphicon glyphicon-trash"></i>
													<spring:message code="button.delete" />
												</button>
												</security:authorize>
											 </c:if> 
											</td>
										</tr>
									</c:forEach>
								</tbody>

							</table>
</div>
						</c:if>
					</article>
				</section>
			</form:form>
		</div>
	</div>
</body>

</html>