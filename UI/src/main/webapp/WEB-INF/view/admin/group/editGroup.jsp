<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<script src="<c:url value="/resources/js/clpvms/group.js" />"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/multiselect.min.js"></script>
</head>

<body onload="getGroupRolesForUpdateGroup()">
	<div class="body-container">
		<div class="container">
		
		<div class="modal fade" id="define-constant-edit" tabindex="-1"
				role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">

					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12" style="display: inline-block">

								<span
									style="width: 100%; display: inline-block; word-wrap: break-word;">
									Do you want to update the Group "<b><span
										id="groupNameDisp"></span></b>" ?
								</span>

							</div>
						</div>
						<div class="modal-footer">
							<button type="button" onclick="goUpdateForGroup()"
								class="btn btn-primary">
								<i class="glyphicon glyphicon-saved"></i>
								<spring:message code="button.update" />
							</button>
							<button data-dismiss="modal" class="btn btn-primary gray-btn">
								<spring:message code="button.cancel" />
							</button>

						</div>

					</div>
				</div>
			</div>
		
		
			<form:form name="updateGroup" id="updateGroup" method="POST"
				class='form-horizontal ' commandName="updateGroup">
				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-7 col-lg-offset-2">
							<li class="active SubMenu"><a href="#editGroup"
								data-toggle="tab"><i class='glyphicon glyphicon-tags'></i> <spring:message
										code="group.editGroup" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active graybox col-lg-7 col-lg-offset-2">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<article class="col-lg-12">	
								<c:if test="${statusMessage!='' && statusMessage!=null}">
									<div class="text-center error-red">
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
									<input type="hidden" id="srvUrl" value="${srvUrl}" />
									<!-- This hidden is used to send the server url to the js file ends -->
								<div class="col-lg-12">
										<div class="col-lg-2">
										<label for="group Name"><spring:message
												code="group.GroupName" /><font color='red'>*</font></label>
									</div>

										<div class="col-lg-9">
											<form:select path="groupId" id="groupNameForEdit" 
											onchange="getGroupRolesForUpdateGroup()" 
											 onblur="validateDropDownForGroupFields(this.form.id,this.id)" >
												<form:option value="-1" label="- - - Select - - -" />
												<form:options items="${groupNameMap}" />
											</form:select>

											<div>
												<form:errors path="groupName" id="groupNameErr"
													cssClass="fieldError" />
											</div>
										</div>
									</div>
								
								
								<div class="col-lg-12" id="multiSelectBox" >
									<div class="col-lg-2">
										<label for="Roles"><spring:message
												code="group.RoleName" /><font color='red'>*</font></label>
									</div>
									<div class="col-lg-4">
									<div>Available</div>
									<form:select path=""  name="RoleList" id="multiselect" size="8" multiple="multiple">
										<%-- <form:options items="${RoleList}" /> --%>		
										<c:forEach items="${RoleList}" var="Role">
										<form:option value="${Role.roleId}">${Role.roleName}</form:option>
										</c:forEach>
									</form:select>
									</div>
									
									<div class="col-lg-1 padding-top-lg">
								
											<button type="button" id="multiselect_rightAll"
												class="btn btn-block btn-multiple">
												<i class="glyphicon glyphicon-forward"></i>
											</button>
											<button type="button" id="multiselect_rightSelected"
												class="btn btn-block btn-multiple">
												<i class="glyphicon glyphicon-chevron-right"></i>
											</button>
											<button type="button" id="multiselect_leftSelected"
												class="btn btn-block btn-multiple">
												<i class="glyphicon glyphicon-chevron-left"></i>
											</button>
											<button type="button" id="multiselect_leftAll"
												class="btn btn-block btn-multiple">
												<i class="glyphicon glyphicon-backward"></i>
											</button>
										</div>
						
							<%-- <c:set var="roleIds" value="${Role.roleId}" /> --%>
									<div>Selected</div>										
								<div class="col-lg-3 select-multiple">
										<form:select name="to[]" id="multiselect_to" path="selectedRoleList" class="dropdown-medium " size="8"
											multiple="multiple">
											<c:forEach items="${roleIds}" var="roleIds">
													<c:if test="${roleIds.roleId !=null }">
														<form:option value="${roleIds.roleId}">
								${roleIds.roleName}</form:option>
													</c:if>
												</c:forEach>
										</form:select>									
					<div>
				<form:errors path="selectedRoleList" id="selectedRoleListErr" class="error-red" />
				</div>
				</div>
		
			</div>

								<article class="col-lg-12">	
								<div class="col-lg-12 text-center">								
									<button type="button" id="editGroupButton" data-toggle="modal"
										class="btn btn-primary"
										onclick="updateGroupSubmit(this.form.id,event);">
										<i class="glyphicon glyphicon-saved"></i>
										<spring:message code="button.update"  text="Update"/>
									</button>
									<button type="button" class="btn btn-primary gray-btn"
										onclick="backEditToSearchGroupConfig();">
										<i class="glyphicon glyphicon-backward"></i>
										<spring:message code="button.back" />
									</button>
								</div>
								</article>
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
</script>
</html>