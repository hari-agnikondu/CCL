<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>



<script src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/programID.js"></script>

<body class="dashboard">

	<div class="body-container" style="min-height: 131px;">
	
		<div class="container">
			<!-- delete box starts -->
			<div class="modal fade" id="define-constant-update" tabindex="-1"
				role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<form:form commandName="confirmBox" name="updateProgramId"
						id="updateProgramId" method="post">
						
						<div class="modal-content">
							<div class="modal-body col-lg-12">
								<div class="col-lg-12" style="display:inline-block">
									<span style="width: 100%; display: inline-block;word-wrap: break-word;"> Do you want to Update the ProgramId record '<b
										 id="programIdNameDisp"></b>' ?
									</span>
								</div>
							</div>
							
							<form:hidden path="" id="programIdtoUpdate" />
							
							<input type="hidden" name="programIdtoUpdate" id="programIdtoUpdate" />
							
							<div class="modal-footer">
								<button type="button" onclick="goUpdateProgramID();"
									class="btn btn-primary">
									<i class="glyphicon glyphicon-saved"></i>
									<spring:message code="button.update" />
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

			<!-- delete box ends -->

			<div class="wrapper">
				<div class="text-center">
					<font class="errormsg"><b>${statusMessage}</b></font>
				</div>
			</div>

			<form:form name="programIdForm" id="programIdForm" method="POST"
				class='form-horizontal' commandName="programIdForm">

				<input type="hidden" id="actionparam" name="actionparam"
					value="${getButtonsAndAction.createAction}">

				<div id="ErrorStatus" class="text-center strong">
					<font color='red'>${ErrorStatus}</font>
				</div>

				<section class="content-container">
					<!--  <article class="col-xs-11"> -->
					<article class="col-lg-12">
						<!-- <ul class="nav nav-tabs"> -->
						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class='active SubMenu'>
								<a data-toggle='tab'>
									<i class="glyphicon glyphicon-tags"></i> 
									<spring:message code="programID.editProgramId" />
								</a>
							</li>
						</ul>

						<div class="tabresp tab-content">
							<div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3"
								id="product">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="" />
								</div>
								<div class="form-inline">
									<span id='hidemydata'>
										<c:if test="${successstatus !=''}">
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

									<!-- <div class="col-lg-6 form-col-2"> -->
									<!-- <div class="col-lg-5 col-lg-offset-1"> -->
									<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="ProgramIDName"> <spring:message
													code="program.programIDName" /><font color='red'></font></label>
										</div>
										<div class="col-lg-8">
											<form:input title="Allowed Special Characters are .,;'_- "
												path="programIDName" id="programIdName" class="trim"
												onkeyup="return isAlphaNumericWithSpecialChars(this)"
												type="textarea" readonly="true" minlength="2" maxlength="100"
												onblur="validateFields(this.form.id,this.id)" />
												 
									<div>
										<form:errors path="programIDName" id="programIdName"
											cssStyle="color:red" />
									</div>
										</div>
									</div>
										<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="Description"> <spring:message
													code="programID.description" /><font color='red'></font></label>
										</div>
										<div class="col-lg-8">
											<form:textarea path="description" id="programDesc" class="trim"
												type="textarea" onblur="validateFields(this.form.id,this.id)"
												
												maxLength="255" rows="5" cols="51" style="resize:none" />
											<div>
												<form:errors path="description" id="programDesc"
													cssStyle="color:red" />
											</div>
										</div>
									</div>
									<br>&nbsp;


									<!-- </div> -->

								</div>
								
								<input id="programID" name="programID" type="hidden"
									value="${programID}" /> <input
									id="editprogramId" name="editprogramId" type="hidden"
									value="${editprogramId}" />
									
								<div class="col-lg-12 text-center">								
									<button type="button" id="Button" data-toggle="modal"
										data-target="#define-constant-update" class="btn btn-primary"
										onclick="goConfirmProgramID(this.form.id);">
										<i class='glyphicon glyphicon-saved'></i>
										<spring:message code="button.save" />
									</button>
									
									<button type="button" onclick="resetEditProgramID(this.form.id)"
										class="btn btn-primary gray-btn">
										<i class='glyphicon glyphicon-refresh'></i>
										<spring:message code="button.reset" />
									</button>
									
									<button type="button" class="btn btn-primary gray-btn"
										onclick="goBackToProgramId();">
										<i class='glyphicon glyphicon-backward'></i>
										<spring:message code="button.back" />
									</button>
								</div>
							</div>
						</div>
					</article>
				</section>
			</form:form>
		</div>
	</div>
</body>