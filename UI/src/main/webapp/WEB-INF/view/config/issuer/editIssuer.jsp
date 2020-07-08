<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>


<script src="${pageContext.request.contextPath}/resources/js/clpvms/issuer.js"></script>


  <body class="dashboard" >
<div class="body-container" style="min-height: 131px;">  
<div class="container">

<!-- delete box starts -->


 <div class="modal fade" id="define-constant-update" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				 <form:form commandName="confirmBox" name="updateIssuer"
					id="updateIssuer" method="post">
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12" style="display:inline-block">
								<span style="width: 100%; display: inline-block;word-wrap: break-word;">
								Do you want to Update the Issuer record "<b id="issuerNameDisp"></b>" ? 
								</span>
							</div>

						</div>
						<form:hidden path="" id="issuerIdtoUpdate" /> 
						 <input type="hidden" name="issuerName" id="issuerIdtoUpdate" /> 
						<div class="modal-footer">
							<button type="button" onclick="goUpdateIssuer();"
								class="btn btn-primary"><i class="glyphicon glyphicon-saved"></i><spring:message
									code="button.update" /></button>
							<button data-dismiss="modal" onclick="goToPrevious()" class="btn btn-primary gray-btn"><spring:message
									code="button.cancel" /></button>

						</div>

					</div>
				</form:form> 
			</div>
		</div> 

<!-- delete box ends -->

			<%-- <div class="wrapper">
		<div ><center><font class="errormsg"><b>${statusMessage}</b></font></center></div>
	</div>	
	 --%>
	
	
	
	

		<form:form name="EditIssuerForm" id="EditIssuerForm" action="/config/issuer/editIssuer" method="POST"
			class='form-horizontal' commandName="issuerForm">


		<%-- 	<input type="hidden" id="actionparam" name="actionparam"
				value="${getButtonsAndAction.createAction}">

			<div id="ErrorStatus">
				<center>
					<font color='red'><strong>${ErrorStatus}</strong></font>


				</center>
			</div> --%>
			
			<input id="issuerId" name="issuerId" type="hidden" value="${issuerform.issuerCode}"/>
			<input id="editIssuerId" name="editIssuerId" type="hidden" value="${editIssuerId}"/>
			<input type="hidden" name="jsPath"  id="jsPath" value="${pageContext.request.contextPath}/resources/JS_Messages/" />
			
	<section class="content-container">
<article class="col-lg-12">	

			<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
				<li class='active SubMenu'><a data-toggle='tab'><i class="glyphicon glyphicon-tags"></i> 
					<spring:message code="issuer.editIssuer" /></a></li>
			</ul>
			<div class="tabresp tab-content">
			  <div class="tab-pane fade in active  graybox col-lg-6 col-lg-offset-3" id="product">
							    <div class="text-right mandatory-red"><spring:message code="label.mandatory" text="*Mandatory" /></div>
							  <!--    <div class="form-inline"> -->
			
		<div>
<c:if test="${successstatus !=''}">
				<h4>
					<p class="">
						<b><font size="3"><center style="color: green;">
						<strong>${successstatus}</strong></center></font></b>
					</p>
				</h4>
</c:if>
<c:if test="${failstatus !=''}">
				<h4>
					<p class="">
						<b><font size="3"><center style="color: red;">
						<strong>${failstatus}</strong></center></font></b>
					</p>
				</h4>
</c:if>
</div>

					<div class="col-lg-12">
						<div class="col-lg-12">

							<div class="col-lg-4">
								<label for="institutionName">
								<spring:message code="issuer.issuerName"/><font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-8">

									<form:input title="Allowed Special Characters are .,;'_- " path="issuerName" id="issuerName"
										class="notEmpty isAlphaNum textbox textbox-xlarge"
										onkeyup="return isAlphaNumericWithSpace(this)"
										 type="textarea"
										minlength="2" maxlength="100"  style="resize:none"
									 	onblur="validateFields(this.form.id,this.id)" />
									<div>
										<form:errors path="issuerName" id="issuerName"
											cssStyle="color:red" />
									</div>
					</div>
						</div>
					
					<div class="col-lg-12">

							<div class="col-lg-4">
								<label for="Description">
								<spring:message code="issuer.description"   /><font
									color='red'></font></label>
							</div>
							<div class="col-lg-8">
								<form:textarea path="description" id="issuerDesc"
								 class="isSplChar textbox textbox-xlarge"
										onblur="validateFields(this.form.id,this.id)"
									type="textarea" maxLength="255" rows="5" cols="51" style="resize:none" />
									<div>
										<form:errors path="description" id="description" cssStyle="color:red" />
									</div>
									</div>
						</div>
						<br>&nbsp;
		
						
						<%-- 	<div class="col-lg-12">

							<div class="col-lg-4">
								<label for="MdmId">
								<spring:message code="issuer.mdmId" /><font
									color='red'></font></label>
							</div>
							<div class="col-lg-8">

								<form:input path="mdmId" id="issuerMdmId" class="alphaNumeric textbox textbox-xlarge"
								onkeyup="return isNumeric(this)"
										onblur="validateMdmId(this.form.id,this.id)" 
									type="textarea" maxlength="20" />
									<div>
										<form:errors path="mdmId" id="mdmId" cssStyle="color:red" />
									</div>
							</div>
						</div> --%>
						
							<div class="col-lg-12">
							<div class="col-lg-4">
							<label for="Address Line 1">
							
							<spring:message code="issuer.isActive" />
							<font color='red'></font></label>
							</div>

								<div class="col-lg-8">									
								
										
								<label for="IsActive"> <font color='BLACK'><strong>YES</strong></font></label>
								</div>
								
						</div>
											
</div>
						<div class="col-lg-12 text-center">
							<!-- <div class="col-lg-4"></div> -->
							<!-- <div class="col-lg-12"> -->
								<button type="button" 
								onclick="goConfirm();"
class="btn btn-primary" id="search_submit" data-toggle="modal" data-target="#define-constant-update">
									 <i class='glyphicon glyphicon-saved'></i><spring:message code="button.update" /></button>
								<button type="button" class="btn btn-primary gray-btn"
									onclick="goBacktoIssuerfromEdit();">
									<i class='glyphicon glyphicon-backward'></i><spring:message code="button.back" /></button>
							<!-- </div> -->
						<!-- </div> -->
						<p class="text-center">&nbsp;</p>

				<!-- 	</div> -->
				</div>


			</div>
			</article>
	</section>
		</form:form>
	

</div>
</div>
</body>

