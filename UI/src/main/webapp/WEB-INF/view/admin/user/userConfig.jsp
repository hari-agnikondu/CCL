<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>



<script src="${pageContext.request.contextPath}/resources/js/clpvms/user.js"></script>
<body>

  <c:if test="${successstatus !=''}">
						<div class="col-lg-11 success-green  text-center" id="message" >
						<b><strong>${successstatus}</strong></b>
					</div>
			
			</c:if> 
			<c:if test="${failstatus !=''}">
			
					
					<div id="errmessage" class="error-red col-lg-11 text-center">
						<b><strong>${failstatus}</strong></b>
					</div>
				
			</c:if>
<!-- delete box starts -->


<div class="modal fade" id="define-constant-delete" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				 <form:form commandName="deleteBox" name="deleteUserForm"
					id="deleteUserForm" method="post">
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12">
								<span >
								Do you want to delete the User record "<b id="userNameDisp"></b>" ? 
								</span>
							</div>

						</div>
				<input type="hidden" name="userId" id="userIdtoDelete" /> 
				<form:hidden path="userName" id="userNametoDelete" />
				<form:hidden path="userStatus" id="deleteStatus" value="DELETED" />
						 
						 
						<div class="modal-footer">
							<button type="button" onclick="goDeleteUser();"
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


<!-- delete box starts -->


<div class="modal fade" id="define-constant-approve" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<form:form commandName="approveBox" name="userApproveForm"
					id="userApproveForm" method="post">
					<div class="modal-content">


						<div class="modal-body col-lg-12">
							<div class="col-lg-12">
								<label class="col-lg-3" for="Remarks"><spring:message
									code="user.Remarks" /><font color='red'>*</font></label>
									<div class="text-right mandatory-red">
											<spring:message code="label.mandatory"/>
									</div>
						<%-- <form:hidden path="userId" id="userIdtoDelete" /> --%> 
						<form:hidden path="userName" id="userNametoApprove" />
				<form:hidden path="userStatus" id="approveStatus" value="APPROVED" />
			<input type="hidden" name="userId" id="userIdtoApprove" /> 
		<span class="col-lg-9"> 
		<form:textarea path="checkerRemarks" style="overflow:auto;resize:none"
				id="userCheckerRemarks" maxlength="255" cols="25"  
				rows="11"  class="textarea-medium" 
				onblur="return validateUserRemarks(this.form.id,this.id)"/>
										
										<br>
										
								</span>
							</div>

						</div>
					
						<div class="modal-footer">

							<button type="button" onclick="goApproveUser(this.form.id);"
								class="btn btn-primary"><i class="glyphicon glyphicon-ok-sign"></i><spring:message
									code="CardRange.approveBtn" />
									
									</button>
									
							<button type="button" onclick="goToPrevious()"data-dismiss="modal" class="btn btn-primary gray-btn">
							<spring:message
									code="CardRange.closeBtn" /></button>

						</div>

					</div>
				</form:form>
			</div>
		</div>

<div class="modal fade" id="define-constant-reject" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<form:form commandName="rejectBox" name="userRejectForm"
					id="userRejectForm" method="post">
					<div class="modal-content">


						<div class="modal-body col-lg-12">
							<div class="col-lg-12">
								<label class="col-lg-3" for="Remarks"><spring:message
									code="user.Remarks" /><font color='red'>*</font></label>
									<div class="text-right mandatory-red">
											<spring:message code="label.mandatory"/>
									</div>
						<form:hidden path="userName" id="userNametoReject" />
				<form:hidden path="userStatus" id="approveStatus" value="REJECTED" />
			<input type="hidden" name="userId" id="userIdtoReject" /> 
		<span class="col-lg-9"> 
		<form:textarea path="checkerRemarks" style="overflow:auto;resize:none"
				id="userCheckerRemarks" maxlength="255" cols="25"  
				rows="11"  class="textarea-medium" 
				onblur="return validateUserRemarks(this.form.id,this.id)"/>
										
										<br>
										
								</span>
							</div>

						</div>
					
						<div class="modal-footer">

							<button type="button" onclick="goRejectUser(this.form.id);"
								class="btn btn-primary"><i class="glyphicon glyphicon-remove-sign"></i><spring:message
									code="CardRange.rejectBtn" />
									
									</button>
									
							<button type="button" onclick="goToPrevious()"data-dismiss="modal" class="btn btn-primary gray-btn">
							<spring:message
									code="CardRange.closeBtn" /></button>

						</div>

					</div>
				</form:form>
			</div>
		</div>
		
<!-- delete box ends -->

<!-- Deactivate the use popup Starts-->
 
<div class="modal fade" id="define-constant-deactive" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<form:form commandName="accessStatusBox" name="userDeactiveForm"
					id="userDeactiveForm" method="post">
					<div class="modal-content">


						<div class="modal-body col-lg-12">
							<div class="col-lg-12">
								<label class="col-lg-3" for="Remarks"><spring:message
									code="user.Remarks" /><font color='red'>*</font></label>
									<div class="text-right mandatory-red">
											<spring:message code="label.mandatory"/>
									</div>
						
						<form:hidden path="userName" id="userNametoDeactive" />
				<form:hidden path="accessStatus" id="accessStatus" value="DEACTIVE" />
			<input type="hidden" name="userId" id="userIdtoDeactive" /> 
		<span class="col-lg-9"> 
		<form:textarea path="checkerRemarks" style="overflow:auto;resize:none"
				id="userCheckerRemarks" maxlength="255" cols="25"  
				rows="11"  class="textarea-medium" 
				onblur="return validateUserRemarks(this.form.id,this.id)"/>
										
										<br>
										
								</span>
							</div>

						</div>
					
						<div class="modal-footer">

							<button type="button" onclick="goDeactiveUser(this.form.id);"
								class="btn btn-primary"><i class="glyphicon glyphicon-ok-sign"></i><spring:message
									code="user.deactiveBtn" />
									
									</button>
									
							<button type="button" onclick="goToPrevious()"data-dismiss="modal" class="btn btn-primary gray-btn">
							<spring:message
									code="button.close" /></button>

						</div>

					</div>
				</form:form>
			</div>
		</div> 
<!-- Deactivate the use popup Ends-->


<!-- Activate the use popup Starts-->
 
<div class="modal fade" id="define-constant-active" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<form:form commandName="accessStatusBox" name="userActiveForm" 
					id="userActiveForm" method="post">
					<div class="modal-content">


						<div class="modal-body col-lg-12">
							<div class="col-lg-12">
								<label class="col-lg-3" for="Remarks"><spring:message
									code="user.Remarks" /><font color='red'>*</font></label>
									<div class="text-right mandatory-red">
											<spring:message code="label.mandatory"/>
									</div>
						
						<form:hidden path="userName" id="userNametoActive" />
				<form:hidden path="accessStatus" id="accessStatus" value="ACTIVE" />
			<input type="hidden" name="userId" id="userIdtoActive" /> 
		<span class="col-lg-9"> 
		<form:textarea path="checkerRemarks" style="overflow:auto;resize:none"
				id="userCheckerRemarks" maxlength="255" cols="25"  
				rows="11"  class="textarea-medium" 
				onblur="return validateUserRemarks(this.form.id,this.id)"/>
										
										<br>
										
								</span>
							</div>

						</div>
					
						<div class="modal-footer">

							<button type="button" onclick="goActiveUser(this.form.id);"
								class="btn btn-primary"><i class="glyphicon glyphicon-ok-sign"></i><spring:message
									code="button.activeBtn" />
									
									</button>
									
							<button type="button" onclick="goToPrevious()"data-dismiss="modal" class="btn btn-primary gray-btn">
							<spring:message
									code="button.close" /></button>

						</div>

					</div>
				</form:form>
			</div>
		</div> 
<!-- Activate the use popup Ends-->

	<div class="">


	 
		<!-- graybox -->
		<!-- <div class="graybox col-lg-5 form-col-6 sb3"> -->
<div class="body-container" style="min-height: 131px;">   
<div class="container">

	<article class="col-lg-12"> 
		<div class="graybox col-lg-5 col-lg-offset-3">

			<form:form id="userForm" method="post" commandName="userForm" >

				<div class="col-lg-12">
					<h3>
						<spring:message code="user.searchUser" />
					</h3>
				</div>
				

				<!-- <div class="form"> -->

					<div class="col-lg-12">
                     <div class="">
                     <div class="col-lg-12">
						<div class="col-lg-4">
							<label for="search_requesterName"> <spring:message
									code="user.enterUserName" /><font color='red'></font></label>

						</div>



						<div class="col-lg-8">

							<form:input  path="userName" id="userNameId"
								class="textbox xlarge4label" name='search_issuerName'
									onkeyup="return isAlphaNumericWithSpaceUserName(this)"  
									onblur="return validatewithmaxLength(this.form.id,this.id)"
								 type="textarea" maxlength="50" />
							<font color="red"><span id="issuerNameIdError"> </span></font>
							<div>
								<form:errors path="userName" id="userNameId"
									class="textbox xlarge4label" name='search_issuerName'
									cssStyle="color:red" />
							</div>

						</div>
					
				<div class="col-lg-12">
				<div class="col-lg-12 text-right">
								<button type="submit" onclick="searchUser(event)"
									class="btn btn-primary" id="search_submit">
									<i class="glyphicon glyphicon-search"></i>
									<spring:message code="button.search" />
								</button>
</div>
				</div>
						
							<div class="col-lg-12 ">
							<div class="col-lg-4 ">
							<label for="search_requesterName"> <spring:message
									code="user.enterUserLoginId" /><font color='red'></font></label>
						</div>
									<div class="col-lg-8">

							<form:input  path="userLoginId" id="userLoginId"
								class="textbox xlarge4label" name='search_issuerName'
								onkeyup="return isAlphaNumericUserLoginId(this)"
								onblur="return validatewithmaxLength(this.form.id,this.id)"
								 type="textarea" maxlength="40" />
							<font color="red"><span id="issuerNameIdError"> </span></font>
							<div>
								<form:errors path="userLoginId" id="userLoginId"
									class="textbox xlarge4label" name='search_issuerName'
									cssStyle="color:red" />
							</div>

						</div>
						</div>
				
		
					

						<!-- <div class=""> -->
							<div class="col-lg-12 text-center">
							
								<label> <spring:message code="user.hint" /></label>
							</div>
						<!-- </div> -->

						<input id="userId" name="userId" type="hidden" value="" /> <input
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

	</div>


<article class="col-lg-12">
	<!-- <div class="pull-right"> -->
	<div class="col-lg-12 text-right">
	 <security:authorize access="hasRole('ADD_USER')">
		<button type="button" class="btn btn-primary"
			style="bottom: 15px; position: relative;" onclick="clickAddUser(event)">
			<i class='glyphicon glyphicon-plus'></i>
			<spring:message code="user.addUser" />
		</button>
		</security:authorize>
	</div>
</article>





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
						<th><spring:message code="user.ldapId" /></th>
						<th><spring:message code="user.userName" /></th>
						<th><spring:message code="user.emailId" /></th>
						<th><spring:message code="user.contactNumber" /></th>
						<th><spring:message code="user.groupName" /></th>
						<th><spring:message code="user.status" /></th>
						<th style="width:28em"><spring:message code="user.action" /></th>
 
					</tr>
				</thead>
				<tbody class="row">

					<c:forEach items="${userTableList}" var="userDetails"
						varStatus="status">
						
							
						<tr id="${userDetails.userId}">
				
						<td>${userDetails.userLoginId}</td>
						<td>${userDetails.userName}</td>
						<td>${userDetails.userEmail}</td>
						<td>${userDetails.userContactNumber}</td>

<td>						
 <c:forEach var="groupData" items = "${userDetails.groups}">
						 <c:if test="${groupData.groupName !='' ||  groupData.groupName !=null }">
						 ${groupData.groupName} <br> 
						</c:if>
						<c:if test="${groupData.groupName =='' ||  groupData.groupName ==null }">
						NONE <br> 
						</c:if>	
</c:forEach> 
</td>
						
						<td>${userDetails.userStatus}</td>
									
							<td>
							
							<c:if test="${(userDetails.userStatus =='NEW' && userDetails.insUser!=loginUserId ) || (userDetails.userStatus =='EDIT' && userDetails.lastUpdUser!=loginUserId) }">
								  <security:authorize access="hasRole('APPROVE_USER')">
								 	<button type="submit" class="btn btn-primary-table-button green-btn" id="search_submit" data-toggle="modal" data-target="#define-constant-approve"
									onclick="goConfirmApproveUser('${userDetails.userId}~${string2}')">
									<i class='glyphicon glyphicon-ok-sign'></i>
									<spring:message code="button.approveBtn" />
								</button>
								</security:authorize>
								 <security:authorize access="hasRole('APPROVE_USER')">
								<button type="submit" class="btn btn-primary-table-button red-btn" id="search_submit" data-toggle="modal" data-target="#define-constant-reject"
									onclick="goConfirmRejectUser('${userDetails.userId}~${string2}')">
									<i class='glyphicon glyphicon-remove-sign'></i>
									<spring:message code="button.rejectBtn" />
								</button>
								</security:authorize>
								
								 </c:if>
								 
						<input id="userId" name="userId" type="hidden" value="" />
						 <security:authorize access="hasRole('EDIT_USER')">
								<button type="submit" class="btn btn-primary-table-button" id="search_submit"
									onclick="goEditUser(${userDetails.userId});">
									<i class='glyphicon glyphicon-edit'></i>
									<spring:message code="button.edit" />
								</button>
								</security:authorize>
								 <security:authorize access="hasRole('VIEW_USER')">
								<button type="submit" class="btn btn-primary-table-button" id="search_view"
									onclick="goViewUser(${userDetails.userId});">
									<i class='glyphicon glyphicon-list'></i>
									<spring:message code="button.view" />
								</button>
								</security:authorize>
								<c:set var = "string1" value = "${userDetails.userName}"/>
								
								<c:set var = "string2" value ="${fn:replace(string1,'\\'','*')} "/> 
								 <security:authorize access="hasRole('DELETE_USER')">
								<button type="submit" class="btn btn-primary-table-button" id="search_submit" data-toggle="modal" data-target="#define-constant-delete"
									onclick="goConfirmDeleteUser('${userDetails.userId}~${string2}')">
									<i class='glyphicon glyphicon-trash'></i>
									<spring:message code="button.delete" />
								</button>
								</security:authorize>
									<c:if
												test="${(userDetails.accessStatus =='ACTIVE' && userDetails.insUser!=loginUserId ) }">
													<button type="submit"
														class="btn btn-primary-table-button red-btn"
														id="search_submit" data-toggle="modal" 
														data-target="#define-constant-deactive"
														onclick="goConfirmDeactiveUser('${userDetails.userId}~${string2}')">
														<i class='glyphicon glyphicon-ok-sign'></i>
														<spring:message code="button.deactiveBtn" />
													</button>
									</c:if>
 									<c:if
												test="${(userDetails.accessStatus =='DEACTIVE' && userDetails.insUser!=loginUserId ) }">
											
												<button type="submit"
													class="btn btn-primary-table-button green-btn"
													id="search_submit" data-toggle="modal"
													data-target="#define-constant-active"
													onclick="goConfirmActiveUser('${userDetails.userId}~${string2}')">
													<i class='glyphicon glyphicon-ok-sign'></i>
													<spring:message code="button.activeBtn" />
												</button>
							
									</c:if> 

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

</c:if>

</section>
</div>

</body>