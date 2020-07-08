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

<body>
	<div class="body-container">
		<div class="container">
			<form:form name="addGroup" id="addGroup" method="POST"
				class='form-horizontal ' commandName="addGroup">
				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-7 col-lg-offset-2">
							<li class="active SubMenu"><a href="#addGroup"
								data-toggle="tab"><i class='glyphicon glyphicon-tags'></i> <spring:message
										code="group.addGroup" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active graybox col-lg-7 col-lg-offset-2">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<article class="col-lg-12">	
								<c:if test="${statusMessage!='' && statusMessage!=null}">
									<div class="text-center error-red" id="serviceErrMsg">
										<b>${statusMessage}</b>
									</div>
								</c:if>
								<c:if test="${status!='' && status!=null}">
									<div class="text-center success-green">
										<b>${status}</b>
									</div>
								</c:if>
</article>
								<div class="col-lg-12">
									<div class="col-lg-2">
										<label for="group Name"><spring:message
												code="group.GroupName" /><font color='red'>*</font></label>
									</div>
									<div class="col-lg-10">
										<form:input path="groupName" id="groupName" autocomplete="off"
											type="textarea" maxlength="100"
											onkeyup="return isAlphabetsWithSpaceAndUnderScore(this);"
											onblur="return validateFieldsGroup(this.form.id,this.id);" />
										<div>
											<form:errors path="groupName" class="error-red" />
										</div>
									</div>
								</div>

								<div class="col-lg-12" id="multiSelectBox">
									<div class="col-lg-2">
										<label for="Roles"><spring:message
												code="group.RoleName" /><font color='red'>*</font></label>
									</div>
									<div class="col-lg-4">
										<div>Available</div>
										<form:select path="" name="RoleList" id="multiselect" size="8"
											multiple="multiple">
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
									<%-- <c:set var="roleIds" value="${groupConfig.roleId}" />
									<c:out value="${roleIds}"/> --%>
									<div>Selected</div>										
								<div class="col-lg-3 select-multiple">
										<form:select name="to[]" id="multiselect_to" path="selectedRoleList" class="dropdown-medium " size="8"
											multiple="multiple"  onchange="multiSelectValidationforGroup(this.form.id,event)" >
											<c:forEach items="${roleIds}" var="roleId">
													<c:if test="${roleId.roleId !=null }">
														<form:option value="${roleId.roleId}">
								${roleId.roleName}</form:option>
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
									<button type="button" id="addGroupButton"
										class="btn btn-primary"
										onclick="formSubmitForAdd(this.form.id,event);">
										<i class="glyphicon glyphicon-plus"></i>
										<spring:message code="button.add" />
									</button>



									<button type="button" onclick="goResetAddGroup();"
										class="btn btn-primary gray-btn">
										<i class="glyphicon glyphicon-refresh"></i>
										<spring:message code="button.reset" />
									</button>
									<button type="button" class="btn btn-primary gray-btn"
										onclick="backAddToSearchGroupConfig();">
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