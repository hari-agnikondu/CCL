<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/programID.js"></script>

<body>
<c:if test="${successstatus !=''}">
						<div class="col-lg-11 success-green  text-center padding" id="message" >
						<b><strong>${successstatus}</strong></b>
					</div>
			
			</c:if> 
			<c:if test="${failstatus !=''}">
			
					
					<div id="errmessage" class="error-red col-lg-11 text-center padding">
						<b><strong>${failstatus}</strong></b>
					</div>
				
			</c:if>

	<div class="modal fade" id="define-constant-delete" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<form:form commandName="deleteBox" name="delProgram"
				id="delProgram" method="post">
				<div class="modal-content">
					<div class="modal-body col-lg-12">
						<div class="col-lg-12" style="display:inline-block">
								<span style="width: 100%; display: inline-block;word-wrap: break-word;">
							 Do you want to delete the Program ID record " <b
								id="programIdDisp"></b>" ?
							</span>
						</div>
					</div>
					<form:hidden path="" id="programIdToDelete" />
					<input type="hidden" name="programIdName" id="programIdToDelete" />
					<div class="modal-footer">
						<button type="button" onclick="deleteProgramId();"
							class="btn btn-primary">
							<i class="glyphicon glyphicon-trash"></i>
							<spring:message code="ProgramId.deleteBtn" />
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

	<div class="">
		<div class="body-container" style="min-height: 131px;">
			<div class="container">
				<article class="col-lg-12">

					<div class="graybox col-lg-5 col-lg-offset-3">

						<form:form id="programIdForm" method="post"
							commandName="programIdForm">

							<div class="col-lg-12">
								<h3>
									<spring:message code="programId.searchProgramId" />
								</h3>
							</div>

							<div class="col-lg-12">
								<div class="">
									<div class="col-lg-4">
										<label for="search_requesterName"> <spring:message
												code="programID.enterProgramIDName" /><font color='red'></font></label>
									</div>

									<div class="col-lg-6">
										<form:input title="Allowed Special Characters are .,;'_- "
											path="programIDName" id="programIdNameId" onkeyup="return isAlphaNumericWithSpecialChars(this)"
											class="textbox xlarge4label trim" name='search_programIdName'
											type="textarea" maxlength="100" />
										<font color="red"><span id="programIdNameIdError">
										</span></font>
										<div>
											<form:errors path="programIDName" id="programIdNameId"
												class="textbox xlarge4label" name='search_programIdName'
												cssStyle="color:red" />
										</div>
									</div>
									
									<security:authorize access="hasRole('SEARCH_PROGRAMID')">	
									<div class="col-lg-2 text-right">
										<button type="submit" onclick="searchProgramID(event)"
											class="btn btn-primary" id="search_submit">
											<i class="glyphicon glyphicon-search"></i>
											<spring:message code="button.search" />
										</button>
									</div>
									</security:authorize>
	
									<div class="col-lg-12 text-center">
										<label> <spring:message code="programID.hint" /></label>
									</div>

									<form:hidden path="programID" id="programID" />
									<input id="searchType" name="searchType" type="hidden"
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
						</form:form>
					</div>
				</article>
				
				<security:authorize access="hasRole('ADD_PROGRAMID')">
				<div class="col-lg-12 text-right">
				 
					<button type="button" class="btn btn-primary"
						style="bottom: 15px; position: relative;"
						onclick="clickAddProgramID()">
						<i class='glyphicon glyphicon-plus'></i>
						<spring:message code="programID.button.addProgramID" />
					</button>
					
						<button type="button" class="btn btn-primary"
						style="bottom: 15px; position: relative;"
						onclick="clickUpdateProgramIDAttributes()">
						<i class='glyphicon glyphicon-plus'></i>
						<spring:message code="programID.button.updateProgramIDAttributes" />
					</button>
				
				</div>
				</security:authorize>		

				<c:if test="${showGrid =='true' }">
					<div class="group">
						<h3>
							<i class="icon-table"></i>
						</h3>
						<div id="tableDiv">
							<table id=""
								class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
								style="width: 100% !important;">
								<thead class="table-head">
									<tr>
										<th><spring:message code="programID.programIDName" /></th>
										<th><spring:message code="programID.partnerName" /></th>
										<th><spring:message code="programID.description" /></th>
										<th><spring:message code="programID.action" /></th>
									</tr>
								</thead>
								<tbody class="row">
									<c:forEach items="${programIDTableList}" var="programDetails"
										varStatus="status">
										<tr id="${programDetails.programID}">
											<td class="dont-break-out">${programDetails.programIDName}</td>
											<td class="dont-break-out">${programDetails.partnerName}</td>
											<td>${programDetails.description}</td>
											<td>
											
												 <security:authorize access="hasRole('EDIT_PROGRAMID')">
												<button type="submit" class="btn btn-primary-table-button"
													id="search_submit"
													onclick="goEditProgramID(${programDetails.programID});">
													<i class='glyphicon glyphicon-edit'></i>
													<spring:message code="button.edit" />
												</button> 
												</security:authorize>
													 <security:authorize access="hasRole('VIEW_PROGRAMID')">
												<button type="submit" class="btn btn-primary-table-button"
													id="search_submit"
													onclick="goViewProgramID(${programDetails.programID});">
													<i class='glyphicon glyphicon-list'></i>
													<spring:message code="button.view" />
												</button> 
												</security:authorize>
												
												<c:set var="string1"
													value="${programDetails.programIDName}" /> <c:set
													var="string2" value="${fn:replace(string1,'\\'','*')} " />
												
												<security:authorize access="hasRole('DELETE_PROGRAMID')">
												<button type="submit" class="btn btn-primary-table-button"
													id="search_submit" data-toggle="modal"
													data-target="#define-constant-delete"
													onclick="goConfirmDeleteProgramID('${programDetails.programID}~${string2}')">
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
				</c:if>
			</div>
		</div>
	</div>	
</body>
