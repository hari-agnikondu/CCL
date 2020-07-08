<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<html>
<head>
<script src="<c:url value="/resources/js/clpvms/cardRange.js" />"></script>
<script
	src="<c:url value="/resources/js/clpvms/common.js"/>"></script>

</head>
<body>

<input type="hidden" name="jsPath"  id="jsPath" value="${pageContext.request.contextPath}/resources/JS_Messages/" />

<div class="body-container" style="min-height: 131px;">     

	<div class="container">
		<form:form name="addCardRange" id="addCardRange"  action="#" method="POST" class='form-horizontal ' commandName="addCardRange">
		<section class="content-container">
           <article class="col-lg-12">

				<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
					<li class="active SubMenu"><a href="#addCardRange" data-toggle="tab">
					<i class="glyphicon glyphicon-tags"></i> <spring:message code="CardRange.AddCardRangeLabel" /></a></li>
				</ul>
								
				<div class="tabresp tab-content">
				  <div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3" id="cardRange">
					
						<div class="text-right mandatory-red">
						<spring:message code="label.mandatory" text="*Mandatory" />
						</div>
						
						 <article class="col-lg-12">
						 <div id="messageResult">					
						<c:if test="${statusMessage!='' && statusMessage!=null}">	
						<p class="error-red text-center"><b>${statusMessage}</b></p>
						</c:if>
											
						<c:if test="${status!='' && status!=null}">
						<p class="success-green text-center"><b>${status}</b></p>
						</c:if>
						</div>
						</article>					
						
						
							<form:hidden path="issuerName" id="issuerName"/>
				
							<div class="col-lg-12">
								<div class="col-lg-6">
								<label for="EnterIssuerName"><spring:message code="CardRange.IssuerName" /><font color='red'>*</font></label>
								</div>
		
								<div class="col-lg-6">
								<form:select path="issuerId" id="issuerId" class="dropdown-medium" onblur="return validateDropDownForIssuerName(this.form.id,this.id);">
								 <form:option value="-1" label="- - - Select - - -"/> 
			   					 <form:options items="${issuerList}" />
								</form:select>	
										<div>
										<form:errors path="issuerId" cssClass="fieldError"/>
										</div>
								</div>
							</div>
												
							<div class="col-lg-12">
								<div class="col-lg-6">
								<label for="Prefix"><spring:message code="CardRange.Network" /></label>
								</div>	
																		
								<div class="col-lg-6">
								<form:input path="network" id="network" autocomplete="off" class="trim"
								 onblur="return validateNetworkField(this.form.id,this.id)" type="textarea"  maxlength="20" />
										<div>	
										<form:errors path="network" cssClass="fieldError" />
										</div>
								</div>											
							</div>
												
							<div class="col-lg-12">
								<div class="col-lg-6">
									<label for="Prefix"><spring:message	code="CardRange.Prefix" /><font color='red'>*</font></label>
								</div>
													
								<div class="col-lg-6">
								<form:input path="prefix" id="prefix" autocomplete="off"
								 onkeyup="return isNumeric(this)" onblur="validateFields(this.form.id,this.id,true)"
								 type="textarea" minlength="6" maxlength="20" />
										<div>
										<form:errors path="prefix" cssClass="fieldError"  />
										</div>					
								</div>
							</div>	
		
							<div class="col-lg-12">
								<div class="col-lg-6">
								<label for="cardLength"><spring:message code="CardRange.CardLength" /><font color='red'>*</font></label>
								</div>
												
								<div class="col-lg-6">
								<form:input path="cardLength" id="cardLength" autocomplete="off"
								onkeyup="return isNumeric(this)" onblur="validateFields(this.form.id,this.id,true)"
								type="textarea" minlength="2" maxlength="2" />
										<div>
										<form:errors path="cardLength" cssClass="fieldError"  />
										</div>
		
								</div>
							</div>
		
							<div class="col-lg-12">
								<div class="col-lg-6">
								<label for="startCardRange"><spring:message code="CardRange.StartCardRange" /><font color='red'>*</font></label>
								</div>
														
								<div class="col-lg-6">
								<form:input path="startCardNbr" id="startCardRange"  autocomplete="off"
								onkeyup="return isNumeric(this)" onblur="validateFields(this.form.id,this.id,true)"
								type="textarea" minlength="5" maxlength="20" />
									<div>
									<form:errors path="startCardNbr" cssClass="fieldError" />
									</div>
								</div>
							</div>
		
							<div class="col-lg-12">
								<div class="col-lg-6">
								<label for="endCardRange"><spring:message code="CardRange.EndCardRange" /><font color='red'>*</font></label>
								</div>
													
								<div class="col-lg-6">
								<form:input path="endCardNbr" id="endCardRange" autocomplete="off"
								 onkeyup="return isNumeric(this)" onblur="validateFields(this.form.id,this.id,true)"
								 type="textarea" minlength="5" maxlength="20" />
								<br>
									<div>
									<form:errors path="endCardNbr" cssClass="fieldError" id="endCardRange_error"/>
									</div>
														
								</div>
							</div>
		
							<div class="col-lg-12">
								<div class="col-lg-6">
								<label for="checkDigit"><spring:message code="CardRange.CheckDigit" /></label>
								</div>
		
								<div class="col-lg-6">
									<div class="col-lg-6">
									<form:radiobutton value="Y"  path="isCheckDigitRequired" id="checkDigitYes" name="checkDigit" checked="checked"  />
									<label class='radiobox-line' for="checkDigit_status"><spring:message code="CardRange.search_status_yes" /></label>
									</div>
									
									<div class="col-lg-6">				
									<form:radiobutton value="N" path="isCheckDigitRequired" id="checkDigitNo" name="checkDigit" data-skin="square" data-color="blue" />
									<label class='radiobox-line' for="checkDigit_status"><spring:message code="CardRange.search_status_no" /></label>
									</div>
								</div>
							</div>
		
							<div class="col-lg-12">
								<div class="col-lg-6">
								<label for="cardInventory"><spring:message code="CardRange.cardInventory" /></label>
								</div>
											
								<div class="col-lg-6">
									<div class="col-lg-6">	
										<form:radiobutton value="Shuffled" path="cardInventory" id="shuffled" name="checkDigityes" checked="checked"  />
										<label class='radiobox-line' for="checkDigit_status"><spring:message code="CardRange.cardInventory_status_shuffled" /></label>
									</div>
									
									<div class="col-lg-6">	
										<form:radiobutton value="Sequential" path="cardInventory" id="sequential" name="checkDigitno" data-skin="square" data-color="blue" />
										<label class='radiobox-line' for="checkDigit_status"><spring:message code="CardRange.cardInventory_status_sequential" /></label>
									</div>
								</div>
							</div>
		
							<div class="col-lg-12 text-center ">
									<button type="button" id="addCardRange" class="btn btn-primary" onclick="FormSubmit(this.form.id,event);">
									<i class="glyphicon glyphicon-plus"></i><spring:message code="CardRange.addBtn" />
									</button>
									
									<button type="button" onclick="goReset();" class="btn btn-primary gray-btn">
									<i class="glyphicon glyphicon-refresh"></i><spring:message code="CardRange.resetBtn" />
									</button>
														
									<button type="button" class="btn btn-primary gray-btn" onclick="backToAddCardRange();">
									<i class="glyphicon glyphicon-backward"></i><spring:message code="CardRange.backBtn" />
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
</html>