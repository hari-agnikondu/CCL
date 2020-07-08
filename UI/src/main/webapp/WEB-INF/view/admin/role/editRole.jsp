<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <%@taglib prefix="maps" uri="/WEB-INF/tlds/maps" %> --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<meta http-equiv="Pragma" content="no-cache">
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/role.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<div class="container">


	<!-- <article class="col-xs-11" id="roleForm" style="margin-top:40px;margin-left:-15px"> -->
	<div id="feedBackTd" class="col-lg-12 text-center">
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out><p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out></p></c:if>
			</div>
		<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
			<li class="active"><a href="" data-toggle="tab"
				class="hoverColor"><i class="glyphicon glyphicon-tags"></i>&nbsp;<spring:message
						code="header.role.edit" text="Edit Role" /></a></li>

		</ul>
		<div class="tabresp tab-content">
			
			<div class="tab-pane fade in active graybox col-lg-8 col-lg-offset-3" id="product">
				<div class="form-group text-right Error-red">
					<spring:message code="label.mandatory" text="*Mandatory" />
				</div>
				<div class="form-inline">
					<form:form action="#" method="POST" modelAttribute="roleForm"
						id="roleFormUpdate" name="roleFormUpdate">

						<!-- <div class="col-lg-6 form-col-2"> -->

							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for="roleName"><spring:message
											code="Role.TabRoleName" text="Role Name " /><font
										style="color: red">*</font></label>
								</div>
								<div class="col-lg-8">
									<form:input title="Allowed Special Characters are ,;'_- ." path="roleName" class="form-group textbox-large trim"
										name="roleName" id="roleName" autocomplete="off"
										maxLength="100" onkeypress="return isAlphaNumericWithSpecialCharsWithCharCode(event)"
										onblur="validateFields(this.form.id,this.id)" />
									<form:errors path="roleName" cssClass="fieldError"></form:errors>
								<form:input type="hidden" name="roleId" id="roleId" path="roleId" />
								<form:input type="hidden" name="status" id="status" path="status" value="NEW"/>
								</div>
								
							</div>
							<div class="col-lg-12" style="padding-bottom: 5px">
								<div class="col-lg-4">
									<label for="roleDesc"><spring:message
											code="Role.TabDesc" text="Description" /></label>
								</div>

								<div class="col-lg-8">
									<form:textarea 
										class="form-group textbox-large trim" name="roleDesc" path="roleDesc"
										id="roleDesc" autocomplete="off" maxLength="255" rows="5"
										cols="51" onblur="validateFields(this.form.id,this.id)"
										 style="resize:none"/>
									<form:errors path="roleDesc" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<table id="tableViewUsers"
						class="table table-hover table-striped table-bordered dataTable "
						style="width: 100% !important;">
						<thead class="table-head">
							<tr>
								<th><spring:message code="Role.TabPermission"
										text="Permission Name" /></th>

								<th><spring:message code="button.search"
										text="Search" /></th>
								<th><spring:message code="button.add"
										text="Add" /></th>

								<th><spring:message code="button.edit"
										text="Edit" /></th>
										<th><spring:message code="button.view"
										text="View" /></th>
								<th ><spring:message code="button.delete"
										text="Delete" /></th>
										 <th ><spring:message code="button.download"
                                        text="Download" /></th>
								<th ><spring:message code="Role.apprRej.button"
										text="Approve/Reject" /></th>
								<th ><spring:message code=""
										text="Menu" /></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${permissionMap}" var="permDto" >
							<tr><td>${permDto.key }<c:set var = "opMap"  value = "${permDto.value}"/></td>
								<td><c:if test="${ not empty opMap['Search']}">
									<form:checkbox  name="Search" id="search${opMap['Search']}" path="permissionID" value="${opMap['Search']}"/>
								</c:if></td>
								<td><c:if test="${not empty opMap['Add']}">
									<form:checkbox  name="Add" id="add${opMap['Add']}" path="permissionID" value="${opMap['Add']}"/>
								</c:if></td>
								<td><c:if test="${not empty opMap['Edit']}">
										<form:checkbox  name="Edit" id="edit${opMap['Edit']}" path="permissionID" value="${opMap['Edit']}"/>
								</c:if></td>
								<td><c:if test="${not empty opMap['View']}">
										<form:checkbox  name="View" id="view${opMap['View']}" path="permissionID" value="${opMap['View']}"/>
								</c:if></td>
								<td><c:if test="${not empty opMap['Delete']}">
									<form:checkbox  name="Delete" id="delete${opMap['Delete']}" path="permissionID" value="${opMap['Delete']}"/>
								</c:if></td>
								<td><c:if test="${not empty opMap['Download']}">
                                    <form:checkbox  name="Download" id="download${opMap['Download']}" path="permissionID" value="${opMap['Download']}"/>
                                </c:if></td>
								<td><c:if test="${not empty opMap['Approve']}">
									<form:checkbox  name="Approve" id="search${opMap['Approve']}" path="permissionID" value="${opMap['Approve']}"/>
								</c:if></td>
								<td><c:if test="${not empty opMap['Menu']}">
									<form:checkbox  name="Menu" id="menu${opMap['Menu']}" path="permissionID" value="${opMap['Menu']}"/>
								</c:if></td>
							</tr></c:forEach>
						</tbody>
						</table>	<div id="tableErr" class="fieldError" style="text-align:center;"></div>
						<div class="col-lg-12 text-center" style="padding-top:5px" >
								<!-- <div class="col-lg-5 col-lg-offset-2"> -->
									<button type="button" class="btn btn-primary"  onclick="FormSubmit(this.form.id)" >
										<i class='glyphicon glyphicon-saved'></i>
										<spring:message code="button.update" text="Update" />
									</button>
									<a
										href="${pageContext.request.contextPath}/admin/roleConfig"
										class="btn gray-btn btn-primary"><i
										class='glyphicon glyphicon-backward'></i>
									<spring:message code="button.back" text="Back" /> </a>
								<!-- </div> -->

							</div>
						<!-- </div> -->
					</form:form>
				</div>
			</div>
		</div>
	<!-- </article> -->


</div>

<script>

$('input[type="checkbox"]').on('change', function() {
	if($("input[type='checkbox']").length > 0){
		$("#tableErr").html('');
	}
	
});

</script>
