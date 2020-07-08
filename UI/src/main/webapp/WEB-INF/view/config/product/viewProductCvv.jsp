<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>

<script src="<c:url value="/resources/js/clpvms/productCvv.js" />">

</script>

<body onload="cvvFunc()">
         
		<div class="body-container">
			<div class="container">
			<form:form name="productCVV" id="productCVV"  action="#"
			method="POST" class='form-horizontal ' commandName="productCVV">
				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class="active SubMenu"><a href="#productCVV"
								data-toggle="tab"><spring:message code="Product.cvvProductLabel" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3" >
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
									<article class="col-lg-12">
									<div id="messageResult">
										<c:if test="${statusMessage!='' && statusMessage!=null}">
											<p class="error-red text-center">
												<b>${statusMessage}</b>
											</p>

										</c:if>

										<c:if test="${status!='' && status!=null}">
											<p class="success-green text-center">
												<b>${status}</b>
											</p>
										</c:if>
									</div>
								</article>

			     				<form:hidden path="productId"  /> 
										
								<div class="col-lg-12">
									<div class="col-lg-4">
									<label for="CVK Format"><spring:message	code="Product.embossApplicable" /></label>
									</div>
											
									 <div class="col-lg-4">
									<form:radiobutton value="true" path="productAttributes[embossApplicable]" id="embossApplicable_Enable" name="embossApplicable"  onclick="cvvFunc();" disabled="true"/>
									<label for="embossApplicable"><spring:message code="Product.embossApplicable_Status_Enable" />
									</label>
									</div>
									
									<div class="col-lg-4">	
									<form:radiobutton value="false" path="productAttributes[embossApplicable]" id="embossApplicable_Disable" name="embossApplicable" data-skin="square"  data-color="blue" onclick="embossNotApplicable();" disabled="true" />
											
									<label for="embossApplicable" ><spring:message
									code="Product.embossApplicable_Status_Disable"  />
									</label>
									</div>
			  										
										<div>
										<form:errors path="productAttributes['embossApplicable']" id="embossApplicableErr"
															cssStyle="color:red" />
										</div>
								</div>


								<div class="col-lg-12">
									<div class="col-lg-4">
									<label for="CVK Format"><spring:message	code="Product.cvkFormat" /></label>
									</div>
											
									<div class="col-lg-4">
									<form:radiobutton value="true" path="productAttributes['cvkFormat']" id="cvkFormat_Enable"
									name="cvkFormat" onclick="cvvFunc();" disabled="true"/>
									<label for="cvkFormat"><spring:message code="Product.cvkFormat_Hsm_Enable" /></label>
									</div>
									
									<div class="col-lg-4">	
									<form:radiobutton value="false" path="productAttributes['cvkFormat']" id="cvkFormat_Disable" name="cvkFormat" data-skin="square" data-color="blue"  onclick="cvvFunc();" disabled="true"/>
											
									<label class='radiobox-line' for="cvkFormat" ><spring:message
									code="Product.cvkFormat_Host_Enable"  /></label>
									</div>
			  										
										<div>
										<form:errors path="productAttributes['cvkFormat']" id="cvkFormatErr"
										cssStyle="color:red" />
										</div>
								</div>	
										
								 <div class="col-lg-12">
										<div class="col-lg-4">
										<label for="Card Verify Type"><spring:message	code="Product.cardVerifyType" /></label>
										</div>
											
										<div class="col-lg-4">
										<form:radiobutton value="true" path="productAttributes['cardVerifyType']" id="cardVerifyType_Cvv" name="cardVerifyType" onclick="cvvFunc();" disabled="true" />
										<label class='radiobox-line' for="cardVerifyType"><spring:message code="Product.cardVerifyType_Cvv_Enable" /></label>
										</div>
										
										<%-- <div class="col-lg-4">
										<form:radiobutton value="false" path="productAttributes['cardVerifyType']"  id="cardVerifyType_Csc" name="cardVerifyType" data-skin="square" data-color="blue"  onclick="cvvFunc();" />
											
										<label class='radiobox-line' for="cardVerifyType" ><spring:message
										code="Product.cardVerifyType_Csc_Enable"  /></label>
										</div> --%>
			  										
			  								<div>
											<form:errors path="productAttributes['cardVerifyType']" id="cardVerifyTypeErr"
														cssStyle="color:red" />
											</div>
									</div>

							
									<div id="cvvParameters" style="display:none;">
									<div class="col-lg-12">
										<div class="col-lg-4">
										<label for="CVV Parameters"><spring:message	code="Product.cvvParameters" /></label>
										</div>
										
										<div class="col-lg-8">
											<div class="col-lg-4">
												<label for="CVK_A"><spring:message	code="Product.cvkA" /><font
											color='red'>*</font></label>
				 							</div>	 
											
										 	<div class="col-lg-8">
												<form:password path="productAttributes['cvkA']" id="cvkA" name="cvkA"
												 maxlength="32" autocomplete="off" onkeyup="return isAlphaNumeric(this)"
												  onblur ="validateCvvParameters(this.form.id,this.id)" value="${productCVV.productAttributes['cvkA']}" readonly="true"/>
												<div>
												<form:errors path="productAttributes['cvkA']" id="cvkAErr"
															cssStyle="color:red" />
												</div>
											</div>
										</div>
									</div>
									<div class="col-lg-12">	
										<div class="col-lg-4">
							
										</div>
										
										<div class="col-lg-8">
											<div class="col-lg-4">
												<label for="CVK_B"><spring:message code="Product.cvkB" /><font
											color='red'>*</font></label>
											</div>	
											
											<div class="col-lg-8">
												<form:password path="productAttributes['cvkB']" id="cvkB" name="cvkB"
												 maxlength="32" autocomplete="off" onkeyup="return isAlphaNumeric(this)"
												 onblur ="validateCvvParameters(this.form.id,this.id)" value="${productCVV.productAttributes['cvkB']}" readonly="true"/>
												<div><form:errors path="productAttributes['cvkB']" id="cvkBErr"
															cssStyle="color:red" /></div>
											</div>
										</div>
										
									</div>							
								</div>
								
								
								<div class="col-lg-12" id="cvvParametershsm" style="display:none;">

										<div class="col-lg-4">
										<label for="CVV Parameters"><spring:message	code="Product.cvvParameters" /></label>
										</div>
										
										<div class="col-lg-8">
											<div class="col-lg-4">
												<label for="CVK_Index"><spring:message	code="Product.cvkIndex" /><font
											color='red'>*</font></label>
											</div>	
											
											<div class="col-lg-8">
												<form:input path="productAttributes['cvkIndex']" id="cvkIndex" name="cvkIndex"
												 maxlength="2" autocomplete="off" onkeyup="return isAlphaNumeric(this)"
												 onblur ="validateCvvParameters(this.form.id,this.id)" readonly="true"/>
												<div>
												<form:errors path="productAttributes['cvkIndex']" id="cvkIndexErr"
															cssStyle="color:red" />
												</div>
											</div>
										</div>
								</div>
								
								
												 
								<%--  <div class="col-lg-12" id="cscParameters" style="display:none;">

										<div class="col-lg-4">
										<label for="CSC Parameters"><spring:message	code="Product.cscParameters" /></label>
										</div>
										
										<div class="col-lg-8">
											<div class="col-lg-4">
												<label for="CSC Key"><spring:message	code="Product.cscKey" /></label>
											</div>	
											
											<div class="col-lg-4">
												<form:input path="productAttributes['cscKey']" id="cscKey" name="cscKey"
												 maxlength="32" class="textbox-large" onkeyup="return isAlphaNumeric(this)"
												 onblur ="validateCvvParameters(this.form.id,this.id)"/>
												<div>
												<form:errors path="productAttributes['cscKey']" id="cscKeyErr"
															cssStyle="color:red" />
												</div>
											</div>
										</div>
									
								</div>  --%>
								
								<div class="col-lg-12" id="cvkKeySpec" style="display:none;">
										<div class="col-lg-4">
										</div>
										
										<div class="col-lg-8">
											<div class="col-lg-4">
												<label for="cvkKeySpecifier"><spring:message code="Product.cvkKeySpecifier" /><font
											color='red'>*</font></label>
											</div>	
											
											<div class="col-lg-8">
												<form:input path="productAttributes['cvkKeySpecifier']" id="cvkKeySpecifier" name="cvkKeySpecifier"
												 maxlength="2" onkeyup="return isAlphaNumeric(this)" autocomplete="off"
												 onblur ="validateCvvParameters(this.form.id,this.id)" readonly="true"/>
												<div><form:errors path="productAttributes['cvkKeySpecifier']" id="cvkKeySpecifierErr"
															cssStyle="color:red" /></div>
											</div>
										</div>
								</div>		

							</div>
						</div>	
					</article>
				</section>
			</form:form>
		</div>
	</div>
</body>	

<script>
$("#cvvTab").addClass("active");
$("#cvvTab").siblings().removeClass('active');
</script>
