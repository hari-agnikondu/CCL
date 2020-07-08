<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>



<script src="${pageContext.request.contextPath}/resources/js/clpvms/issuer.js"></script>



  <body class="dashboard">

<div class="body-container" style="min-height: 131px;">     

	<div class="container">
	<%-- <div class="wrapper">
		<div class="text-center"><font class="errormsg"><b>${statusMessage}</b></font></div>
	</div> --%>
				
		

			<form:form name="RegIssuerForm" id="RegIssuerForm"
				 method="POST" class='form-horizontal'
				commandName="issuerForm">

<%-- 
				<input type="hidden" id="actionparam" name="actionparam"
					value="${getButtonsAndAction.createAction}">

				<div id="ErrorStatus" class="text-center strong">
						<font color='red'>${ErrorStatus}</font>


				</div>
 --%>
	<section class="content-container">
  <!--  <article class="col-xs-11"> -->
   <article class="col-lg-12">						
						

				<!-- <ul class="nav nav-tabs"> -->
				<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
					<li class='active SubMenu'><a data-toggle='tab'><i
							class="glyphicon glyphicon-tags"></i> <spring:message code="issuer.addIssuer" /></a></li>
				</ul>
				
	<div class="tabresp tab-content">
	  <div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3" id="product">
							    <div class="text-right mandatory-red"><spring:message code="label.mandatory" text="*Mandatory" /></div>
							     <div class="">
							     
							     <input type="hidden" name="jsPath"  id="jsPath" value="${pageContext.request.contextPath}/resources/JS_Messages/" />

<c:if test="${successstatus !=''}">				
						<div class="text-center  success-green">
						<b>${successstatus}</b>
						</div>			
</c:if>
<c:if test="${failstatus !=''}">
				
				
						<%-- <b><font size="3"><center style="color: red;"> --%>
						<div class="text-center  error-red">
						<b id="errStatus">${failstatus}</b><%-- </center></font></b> --%>
						</div>
					
</c:if>

				
				
				
					<!-- <div class="col-lg-6 form-col-2"> -->
					 <div class="col-lg-12">
					
							<div class="col-lg-12">

								<div class="col-lg-4">
									<label for="IssuerName"> <spring:message
											code="issuer.issuerName" /><font
										color='red'>*</font></label>
								</div>
								<div class="col-lg-8">

														
									<form:input title="Allowed Special Characters are .,;'_- " path="issuerName" id="issuerName"
										
										onkeyup="return isAlphaNumericWithSpace(this)"
										type="textarea" minlength="2" maxlength="100" 
										 onblur="validateFields(this.form.id,this.id)" />
										 
									<div>
										<form:errors path="issuerName" id="issuerName"
											 maxlength="100"
											cssStyle="color:red" />
									</div>

								</div>
							</div>

							<div class="col-lg-12">

								<div class="col-lg-4">
									<label for="Description"> <spring:message
											code="issuer.description" /><font color='red'></font></label>
								</div>
								<div class="col-lg-8">
									<form:textarea path="description" id="issuerDesc"
										 type="textarea"
										onblur="validateFields(this.form.id,this.id)"
										 maxLength="255" rows="5" cols="51" style="resize:none" />
									<div>
									<form:errors path="description" id="description"
										cssStyle="color:red" />
										</div>


								</div>
							</div>

							<br>&nbsp;

							<%-- <div class="col-lg-12">

								<div class="col-lg-4">
									<label for="MdmId"> <spring:message code="issuer.mdmId" /><font
										color='red'></font></label>
								</div>
								<div class="col-lg-8">

									
									<form:input path="mdmId" id="issuerMdmId" type="textarea"
										
										onkeyup="return isNumeric(this)"
										onblur="validateMdmId(this.form.id,this.id)" maxlength="20" />
									<div>
										<form:errors path="mdmId" id="mdmId" cssStyle="color:red" />
									</div>

								</div>
							</div>
 --%>


							<div class="col-lg-12">

								<div class="col-lg-4">
									<label for="IsActive"> <spring:message
											code="issuer.isActive" /><font color='red'></font></label>
								</div>

							<%-- 	<div class="col-lg-4">
									<div class="">
										<form:radiobutton value="true" path="active" id="active"
											name="activeyes" checked="checked" disabled="true" />
										<label class='radiobox-line' for="search_status">Yes</label>
									</div>

									<div class="rightdisp">
										<form:radiobutton value="false" path="active" id="active"
											name="activeno" data-skin="square" data-color="blue"
											disabled="true" />
										<label class='radiobox-line' for="search_status">No</label>
									</div>
								</div> --%>
								<div class="col-lg-8">									
									<%-- <form:input path="active" id="active" type="textarea"
										class="textbox textbox-xlarge" disabled="true"/> --%>
										
								<label for="IsActive"> <font color='BLACK'><strong>YES</strong></font></label>
								</div>

							</div>
							<!-- <div class="col-lg-6 col-lg-offset-3"> -->
							
</div>
<div class="col-lg-12 text-center">

								<!-- <div class="col-lg-3 space"></div> -->
								<!-- <div class="col-lg-9"> -->
									<button type="button" id="Button"
										class="btn btn-primary" onclick="goAddIssuer();">
										<i class='glyphicon glyphicon-plus'></i><spring:message code="button.add" />
									</button>
									<button type="button" onclick="ResetAddIssuer(this.form.id)" class="btn btn-primary gray-btn">
										<i class='glyphicon glyphicon-refresh'></i><spring:message code="button.reset" />
									</button>
									<button type="button" class="btn btn-primary gray-btn"
										onclick="goBackToIssuer();">
										<i class='glyphicon glyphicon-backward'></i><spring:message code="button.back" />
									</button>


								<!-- </div> -->
							</div>
							

       <%-- <div class="col-lg-12 col-lg-offset-5">

								<!-- <div class="col-lg-3 space"></div> -->
								<!-- <div class="col-lg-9"> -->
									<button type="button" id="Button"
										class="btn btn-primary" onclick="goAddIssuer();">
										<i class='glyphicon glyphicon-plus'></i><spring:message code="button.add" />
									</button>
									<button type="button" onclick="ResetAddIssuer(this.form.id)" class="btn btn-primary gray-btn">
										<i class='glyphicon glyphicon-refresh'></i><spring:message code="button.reset" />
									</button>
									<button type="button" class="btn btn-primary gray-btn"
										onclick="goBackToIssuer();">
										<i class='glyphicon glyphicon-backward'></i><spring:message code="button.back" />
									</button>


								<!-- </div> -->
							</div>
 --%>






				<!-- 	</div> -->


                       
                       
                
</div>


						</div>
	</article>
	</section>	

			</form:form>
		
</div>
</div>

</body>





