<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>

<html>
<head>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/globalParameter.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/panExpiry.js"></script>
</head>


<!-- <body OnLoad="selctedDateFormat();">
 -->
 <form:form name="panExpiryExemption" id="panExpiryExemption"  action="#"
		method="POST" class='form-horizontal' commandName="panExpiryParameters">
<div class="body-container">
	<div class="container">
	<section class="content-container">
	 <article class="col-lg-12">
		<ul class="nav nav-tabs col-lg-6 col-lg-offset-2">
				<li class='active'><a data-toggle='tab'><i class=""></i> 
					<spring:message code="panExp.heading" /></a></li>
			</ul>
<div class="tabresp tab-content">
	    <div class="tab-pane fade in active graybox col-md-6 col-lg-offset-2" >
								<div class="text-right Error-red"><spring:message code="label.mandatory" text="*Mandatory" /></div>
		<div class="col-lg-12">
			<c:if test="${statusMessage!='' && statusMessage!=null}">	
			<div class="aln error-red" id="statusId" style="style="margin-top: -17px; margin-left: 115px;"">
			<center><b><font color="red">${statusMessage}</font></b></center>
			</div>	
			</c:if>
			<c:if test="${status!='' && status!=null}">
			<div class="aln success-green" id="statusId" style="margin-top: -17px; margin-left: 90px;">
			<center><b><font color="green">${status}</font></b></center>
			</div>	
			</c:if>
			<div class="col-lg-7 col-lg-offset-1">
			
			
				
					<div class="col-lg-12">
										<div class="col-lg-6">
											<label for="Product Name"> <spring:message
													code="merchant.productName" /><font color='red'> *</font></label>
										</div>
										<div class="col-lg-6">
										
	<form:select path="productId" id="panProductId" class="dropdown-medium"
	onchange="getPanExpiryData();"
	 onblur="return validateDropDownPanProductId(this.form.id,this.id);">
	<form:option value="-1" label="- - - Select - - -"/> 
	<c:forEach items="${productDropDown}" var="product">
		<option value="${product.productId}" 
		<c:if test="${product.productId eq panExpiryParameters.productId}">selected="true"</c:if>>${product.productId}:${product.productName}</option>
	</c:forEach>
	
		</form:select>
											
											
											<div>
												<form:errors path="productId" id="panProductId"
													maxlength="100" cssStyle="color:red" />
											</div>
										</div>
									</div>
									
									<br>&nbsp;
				
				<div class="col-lg-12">
				<div class="col-lg-7">
				<label for="Expiry Month">
				<spring:message code="panExp.expiryMonth" />
				<font color='red'></font></label>
				</div>
				<div class="col-lg-5">
				<center><label for="Additional Months">
				<spring:message code="panExp.additionalMonths" />
				<font color='red'></font></label></center>				
			
				</div>
				</div>					
									
									
							
							<!-- jan -->		
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="JANUARY">
						<spring:message
								code="panExp.jan" /><font color='red'></font></label>
					</div>											
						 <div class="col-lg-6">
		<form:input path="productAttributes['January']" 
						id="januaryId" name="customerPasswordLength" 
						onkeyup="return isNumericMonth(this)"  
					 autocomplete="off" maxlength="2" class="textbox-large" />
						
						<div>
						<form:errors path="productAttributes['January']"   cssStyle="color:red" />
						</div>

									
							
					</div>		 									
				</div>
				
				<!-- jan -->
				
				<!-- FEB -->		
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="February">
						<spring:message
								code="panExp.feb" /><font color='red'></font></label>
					</div>											
						 <div class="col-lg-6">
		<form:input path="productAttributes['February']" 
						id="februaryId" name="customerPasswordLength" 
						onkeyup="return isNumericMonth(this)"  
					 autocomplete="off" maxlength="2" class="textbox-large" />
						
						<div>
						<form:errors path="productAttributes['February']"   cssStyle="color:red" />
						</div>

									
							
					</div>		 									
				</div>
				
				<!-- mar -->


<!-- mar-->		
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="March">
						<spring:message
								code="panExp.mar" /><font color='red'></font></label>
					</div>											
						 <div class="col-lg-6">
		<form:input path="productAttributes['March']" 
						id="marchId" name="customerPasswordLength" 
						onkeyup="return isNumericMonth(this)" 
					 autocomplete="off" maxlength="2" class="textbox-large" />
						
						<div>
						<form:errors path="productAttributes['March']"   cssStyle="color:red" />
						</div>

									
							
					</div>		 									
				</div>
				
				<!-- mar -->
				
				<!-- apr -->		
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="April">
						<spring:message
								code="panExp.apr" /><font color='red'></font></label>
					</div>											
						 <div class="col-lg-6">
		<form:input path="productAttributes['April']" 
						id="aprilId" name="customerPasswordLength" 
						onkeyup="return isNumericMonth(this)"
					 autocomplete="off" maxlength="2" class="textbox-large" />
						
						<div>
						<form:errors path="productAttributes['April']"   cssStyle="color:red" />
						</div>

									
							
					</div>		 									
				</div>
				
				<!-- apr -->
				
				
				<!-- may -->		
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="May">
						<spring:message
								code="panExp.may" /><font color='red'></font></label>
					</div>											
						 <div class="col-lg-6">
		<form:input path="productAttributes['May']" 
						id="mayId" name="customerPasswordLength" 
						onkeyup="return isNumericMonth(this)"
					 autocomplete="off" maxlength="2" class="textbox-large" />
						
						<div>
						<form:errors path="productAttributes['May']"   cssStyle="color:red" />
						</div>

									
							
					</div>		 									
				</div>
				
				<!-- may -->
				
				<!-- jun -->		
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="June">
						<spring:message
								code="panExp.jun" /><font color='red'></font></label>
					</div>											
						 <div class="col-lg-6">
		<form:input path="productAttributes['June']" 
						id="juneId" name="customerPasswordLength" 
						onkeyup="return isNumericMonth(this)" 
					 autocomplete="off" maxlength="2" class="textbox-large" />
						
						<div>
						<form:errors path="productAttributes['June']"   cssStyle="color:red" />
						</div>

									
							
					</div>		 									
				</div>
				
				<!-- jun -->
				
				<!-- jul -->		
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="July">
						<spring:message
								code="panExp.jul" /><font color='red'></font></label>
					</div>											
						 <div class="col-lg-6">
		<form:input path="productAttributes['July']" 
						id="julyId" name="customerPasswordLength" 
						onkeyup="return isNumericMonth(this)"
					 autocomplete="off" maxlength="2" class="textbox-large" />
						
						<div>
						<form:errors path="productAttributes['July']"   cssStyle="color:red" />
						</div>

									
							
					</div>		 									
				</div>
				
				<!-- jul -->
				
					<!-- aug -->		
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="August">
						<spring:message
								code="panExp.aug" /><font color='red'></font></label>
					</div>											
						 <div class="col-lg-6">
		<form:input path="productAttributes['August']" 
						id="augustId" name="customerPasswordLength" 
						onkeyup="return isNumericMonth(this)" 
					 autocomplete="off" maxlength="2" class="textbox-large" />
						
						<div>
						<form:errors path="productAttributes['August']"   cssStyle="color:red" />
						</div>

									
							
					</div>		 									
				</div>
				
				<!-- aug -->
				
					<!-- sep -->		
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="September">
						<spring:message
								code="panExp.sep" /><font color='red'></font></label>
					</div>											
						 <div class="col-lg-6">
		<form:input path="productAttributes['September']" 
						id="septemberId" name="customerPasswordLength" 
						onkeyup="return isNumericMonth(this)" 
					 autocomplete="off" maxlength="2" class="textbox-large" />
						
						<div>
						<form:errors path="productAttributes['September']"   cssStyle="color:red" />
						</div>

									
							
					</div>		 									
				</div>
				
				<!-- sep -->
				
						<!-- oct -->		
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="October">
						<spring:message
								code="panExp.oct" /><font color='red'></font></label>
					</div>											
						 <div class="col-lg-6">
		<form:input path="productAttributes['October']" 
						id="octoberId" name="customerPasswordLength" 
						onkeyup="return isNumericMonth(this)" 
					 autocomplete="off" maxlength="2" class="textbox-large" />
						
						<div>
						<form:errors path="productAttributes['October']"   cssStyle="color:red" />
						</div>

									
							
					</div>		 									
				</div>
				
				<!-- oct -->
				
					<!-- nov -->		
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="November">
						<spring:message
								code="panExp.nov" /><font color='red'></font></label>
					</div>											
						 <div class="col-lg-6">
		<form:input path="productAttributes['November']" 
						id="novemberId" name="customerPasswordLength" 
						onkeyup="return isNumericMonth(this)" 
					 autocomplete="off" maxlength="2" class="textbox-large" />
						
						<div>
						<form:errors path="productAttributes['November']"   cssStyle="color:red" />
						</div>

									
							
					</div>		 									
				</div>
				
				<!-- nov -->
				
				<!-- dec -->		
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="December">
						<spring:message
								code="panExp.dec" /><font color='red'></font></label>
					</div>											
						 <div class="col-lg-6">
		<form:input path="productAttributes['December']" 
						id="decemberId" name="customerPasswordLength" 
						onkeyup="return isNumericMonth(this)" 
					 autocomplete="off" maxlength="2" class="textbox-large" />
						
						<div>
						<form:errors path="productAttributes['December']"   cssStyle="color:red" />
						</div>

									
							
					</div>		 									
				</div>
				
				<!-- dec -->
				
<br>&nbsp;



<!--  ends buttons -->	
							</div>
							<!-- <div> -->
				<div class="col-lg-12 text-center">

			<!-- 	<div class="col-lg-4 space"></div> -->
					<div class="col-lg-12">
						<button type="submit" id="globalParametersSubmit" class="btn btn-primary" onclick="goSavePanExpiry(this.form.id,event);">
							<i class="glyphicon glyphicon-saved"></i><spring:message code="globalParameters.submit" />
						</button>
						
							<button type="button" onclick="resetPanExpiry();"   id="globalParametersReset" class="btn btn-primary gray-btn">
										<i class='glyphicon glyphicon-refresh'></i><spring:message code="button.reset" />
									</button>
										

									</div>
								</div> 			
							
							<!-- </div> -->

						</div><!-- form-inline -->
					</div>
					
					
				</div>
	
	
	
</article>
</section>
	
	</div>
	</div>
		
		
</form:form>


</body>
</html>



<script>
function getPanExpiryData()
{
	
	$("#panExpiryExemption").attr('action','getPanExpiryDetails');
	$("#panExpiryExemption").submit();
	
	}
</script>