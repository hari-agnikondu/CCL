<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>


<script src="${pageContext.request.contextPath}/resources/css/jqueryui.css"></script>
<script src="${pageContext.request.contextPath}/resources/js/jqueryui.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/user.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/multiselect.min.js"></script>


<style>

.col-sm-2.up {
    margin: -123px 3px 0px 530px;
}

.col-sm-2.down {
    margin: -89px 3px 0px 530px;
}


.col-sm-4.up {
    margin: -123px 3px 0px 232px;
}

.col-sm-4.down {
    margin: -89px 3px 0px 232px;
}


.col-sm-3.up{
    margin: -123px 3px 0px 288px;
}

.col-sm-3.down{
    margin: -90px 3px 0px 288px;
}

</style>
  <body class="dashboard">

<div class="body-container" style="min-height: 131px;">     

	<div class="container">
			<form:form name="userForm" id="userForm"
				 method="POST" class='form-horizontal'
				commandName="userForm">


				<input type="hidden" id="actionparam" name="actionparam"
					value="${getButtonsAndAction.createAction}">

	<section class="content-container">
   <article class="col-lg-12">						
				<ul class="nav nav-tabs col-lg-9 col-lg-offset-2">
					<li class='active SubMenu'><a data-toggle='tab'><i
							class="glyphicon glyphicon-tags"></i> <spring:message code="user.viewUser" /></a></li>
				</ul>
				
				<div class="tabresp tab-content">
							  <div class="tab-pane fade in active graybox col-lg-9 col-lg-offset-2" id="product">
							    <div class="text-right Error-red"><spring:message code="label.mandatory" text="*Mandatory" /></div>
							     
							     <input type="hidden" name="jsPath"  id="jsPath" value="${pageContext.request.contextPath}/resources/JS_Messages/" />

				
					 <div class="col-lg-10 col-lg-offset-1">
					
						

							<div class="col-lg-12">

								<div class="col-lg-4">
									<label for="LDAP ID"> <spring:message
											code="user.ldapId" /><font
										color='red'> *</font></label>
								</div>
								<div class="col-lg-8">

														
									<form:input title="Allowed alphabetics" path="userLoginId" id="userLoginId"
										class="textbox textbox-xlarge"
										type="textarea" minlength="2" maxlength="100" 
										style="color:black;"  readonly="true" />
										 
									<div>
										<form:errors path="userLoginId" id="userLoginId"
											class="textbox textbox-xlarge" maxlength="100"
											cssStyle="color:red" />
									</div>

								</div>
								
								
							</div>

							<br>&nbsp;

							<div class="col-lg-12">

								<div class="col-lg-4">
									<label for="USER NAME"> <spring:message code="user.userName" /><font
										color='red'></font></label>
								</div>
								<div class="col-lg-8">

									
									<form:input path="userName" id="userNameId" type="textarea"
										class="textbox textbox-xlarge" 
										onkeyup="return isAlphaNumericWithSpaceUserName(this)"
										onblur="validateuserName(this.form.id,this.id)" maxlength="50" style="color:black;"  readonly="true" />
									<div>
										<form:errors path="userName" id="userNameId" cssStyle="color:red" />
									</div>

								</div>
							</div>
<br>&nbsp;

	<div class="col-lg-12">

								<div class="col-lg-4">
									<label for="email"> <spring:message
											code="user.emailId" /><font
										color='red'></font></label>
								</div>
								<div class="col-lg-8">

										
					<form:input path="userEmail" id="emailId"
						class="textbox textbox-xlarge" readonly="true"
						
						type="textarea" minlength="2" maxlength="100"  novalidate="novalidate"
						 onblur="validateEmail(this.form.id,this.id)" style="color:black;"   />
						 
					<div>
						<form:errors path="userEmail" id="emailId"
							class="textbox textbox-xlarge" maxlength="100"
							cssStyle="color:red" />
					</div>

								</div>
							</div>
							
			<br>&nbsp;				

<div class="col-lg-12">

								<div class="col-lg-4">
									<label for="contact number"> <spring:message
											code="user.contactNumber" /><font
										color='red'></font></label>
								</div>
								<div class="col-lg-8">

										
					<form:input title="Allowed Special Characters are .,;'_- " path="userContactNumber" id="contactId"
						class="textbox textbox-xlarge"
						onkeyup="return isNumeric(this)" 
						type="textarea" minlength="2" maxlength="100" 
						 onblur="validatewithmaxLength(this.form.id,this.id)" style="color:black;"  readonly="true" />
						 
					<div>
						<form:errors path="userContactNumber" id=""
							class="textbox textbox-xlarge" maxlength="100"
							cssStyle="color:red" />
					</div>

								</div>
							</div>
							
			<br>&nbsp;				
							
							<!-- group multi select starts here -->
							
							<div class="col-lg-12">

								<div class="col-lg-4">
									<label for="GROUP NAME">
				<spring:message code="user.groupName" /> <font color='red'>*</font></label>
								</div>
								<div class="col-lg-8">
									<div class="col-lg-12">
											<div  class="col-lg-5">
											<div>Available</div>
					<form:select path=""  name="avail_GroupName"
						id="multiselect" class="dropdown-medium" size="8"
						multiple="multiple" disabled="true">
		<c:forEach items="${groupList}" var="groupData">
		<form:option value="${groupData.groupId}">${groupData.groupName}
		</form:option>				
			</c:forEach>			
					</form:select>
										</div>

										<div class="col-lg-1 col-lg-offset-1 padding">
											<br />
											<button type="button" id="multiselect_rightAll"
												class="btn btn-block" disabled="true">
												<i class="glyphicon glyphicon-forward"></i>
											</button>
											<button type="button" id="multiselect_rightSelected"
												class="btn btn-block" disabled="true">
												<i class="glyphicon glyphicon-chevron-right"></i>
											</button>
											<button type="button" id="multiselect_leftSelected"
												class="btn btn-block" disabled="true">
												<i class="glyphicon glyphicon-chevron-left"></i>
											</button>
											<button type="button" id="multiselect_leftAll"
												class="btn btn-block" disabled="true">
												<i class="glyphicon glyphicon-backward"></i>
											</button>
										</div>

										<div class="col-lg-5">
							<c:set var="selGroupDetails" value="${userForm.groups}"/>
						<div>Selected</div> <form:select name="to[]" id="multiselect_to" 
											path="groupNames" disabled="true"
												class="dropdown-medium" size="8" multiple="multiple" onchange="multiSelectValidationUser(this.form.id,event)">
												
 		<c:forEach items="${selGroupDetails}" var="selGroupList">
		<form:option value="${selGroupList.groupId}">${selGroupList.groupName}
		</form:option>				
			</c:forEach> 
												
												
												</form:select>

</div>
										</div>
									</div>
						<div class="col-lg-12 col-lg-offset-9">
												<form:errors path="groupNames" id="multiselect_to_Error"
										cssClass="error" cssStyle="color:red"></form:errors>
										</div>
								</div>
							</div>
							
							
							
							<!-- multi select ends here -->
							
							
							
							
							
							
							
			<br>&nbsp;<br>
<input id="userId" name="userId" type="hidden" value="${userID}" />
<input id="editUserId" name="editUserId" type="hidden" value="${editUserId}"/>

	
<div class="col-lg-12 text-center padding">

								
									
									<button type="button" class="btn btn-primary gray-btn"
										onclick="goBackToUser();">
										<i class='glyphicon glyphicon-backward'></i><spring:message code="button.back" />
									</button>


							</div>				
						
							
</div>




<br><br>					

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


function getLdapData()
{


var ldapId="";

ldapId=$('#userLoginId').val();

$("#userForm").attr('action','getUserDetails');
$("#userForm").submit();

}

</script>


