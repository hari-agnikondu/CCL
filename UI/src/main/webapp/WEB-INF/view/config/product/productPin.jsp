<!doctype html>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<head>
<script src="<c:url value="/resources/js/clpvms/productPin.js" />"></script>
<script
	src="<c:url value="/resources/js/clpvms/common.js"/>"></script>

</head>


<body>

	<div class="body-container">
		<div class="container">

			<form:form name="productPIN" id="productPIN" action="#" method="POST"
				class='form-horizontal ' commandName="productPIN">

				<section class="content-container">
					<article class="col-lg-12 ">
						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class="active SubMenu"><a href="#productPIN"
								data-toggle="tab"><spring:message
										code="Product.pinProductLable" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3">

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

								<div class="col-lg-12">
									<c:if test="${showCopyFrom=='true' }">
										<div class="col-lg-4">
											<label for="copyFrom"><spring:message
													code="Product.copyFrom" /></label>
										</div>
										<form:hidden path="productId" />
										<div class="col-lg-8">
											<form:select path="parentProductId" id="parentProductId"
												class="dropdown-medium">
												<form:option value="-1" label="- - - Select - - -" />
												<form:options items="${parentProductMap}" />
											</form:select>

											<button type="button" id="productPIN" class="btn btn-primary"
												onclick="getParentProductDetails()">
												<spring:message code="button.copy" />
											</button>
											<div>
												<span id="parentProductIdError"></span>
											</div>
										</div>
									</c:if>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="pinLength"><spring:message
												code="Product.pinLength" /></label>
									</div>

									<div class="col-lg-8">
										<form:input path="productAttributes['pinLength']"
											id="pinLength" name="pinLength" maxlength="2"
											onkeyup="return isNumeric(this)"
											onblur="validatePinFields(this.form.id,this.id)"
											autocomplete="off" onpaste="return false" />
										<div>
											<form:errors path="productAttributes['pinLength']"
												cssClass="fieldError" id="pinLengthErr" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4">
										<label for="pinGenerationAlgorithm"><spring:message
												code="Product.pinGenerationAlgorithm" /></label>
									</div>
																	
									

									<div class="col-lg-8">
										<form:radiobutton value="true" id="ibmDes" name="ibmDes"
										 path="productAttributes['pinAlgorithm']"
										data-skin="square" data-color="blue" checked="checked" />
										<label for="pinGenerationAlgorithm_ibmDes"><spring:message
												code="Product.pinGenerationAlgorithm_ibmDes" /></label>
										<div>
											<form:errors
												path="productAttributes['pinAlgorithm']"
												cssClass="fieldError" id="pinGenerationAlgorithm_ibmDesErr" />
										</div>
									</div>
								</div>




								<div class="col-lg-12">
									<div class="col-lg-4"></div>
									<div class="col-lg-8">
										<div class="col-lg-4">
											<label for="pvk"><spring:message code="Product.pvk" /></label>
										</div>
										<div class="col-lg-4">
											<form:password path="productAttributes['pvk']" id="pvk"
												name="pvk" maxlength="100" onpaste="return false"
												autocomplete="off"  onkeyup="return isHexaDecimal(this)"
												onblur="validatePvkAndDecimalisationTableFields(this.form.id,this.id)" value="${productPIN.productAttributes['pvk']}" />
											<div>
												<form:errors path="productAttributes['pvk']"
													cssClass="fieldError" id="pvkErr" />
											</div>
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4"></div>
									<div class="col-lg-8">
										<div class="col-lg-4">
											<label for="maximumPINLength"><spring:message
													code="Product.maximumPINLength" /></label>
										</div>

										<div class="col-lg-4">
											<form:input path="productAttributes['maxPinLength']"
												id="maximumPINLength" name="maximumPINLength" maxlength="2"
												onpaste="return false" autocomplete="off"
												onkeyup="return isNumeric(this)"
												onblur="validateMaxPinLengthField(this.form.id,this.id)" />
											<div>
												<form:errors path="productAttributes['maximumPINLength']"
													cssClass="fieldError" id="maximumPINLengthErr" />
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-12">
									<div class="col-lg-4"></div>
									<div class="col-lg-8">
										<div class="col-lg-4">
											<label for="checkLength"><spring:message
													code="Product.checkLength" /></label>
										</div>

										<div class="col-lg-4">
											<form:input path="productAttributes['ibmCheckLength']"
												id="checkLength" name="checkLength" maxlength="2"
												onpaste="return false" 
												autocomplete="off" onkeyup="return isNumeric(this)"
												onblur="validatePinFields(this.form.id,this.id)" />
											<div>
												<form:errors path="productAttributes['checkLength']"
													cssClass="fieldError" id="checkLengthErr" />
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-12">
									<div class="col-lg-4"></div>
									<div class="col-lg-8">
										<div class="col-lg-4">
											<label for="panOffset"><spring:message
													code="Product.panOffset" /></label>
										</div>

										<div class="col-lg-4">
											<form:input path="productAttributes['ibmPanOffset']"
												id="panOffset" name="panOffset" maxlength="2"
												onpaste="return false" 
												autocomplete="off" onkeyup="return isNumeric(this)"
												onblur="validatePinFields(this.form.id,this.id)" />
											<div>
												<form:errors path="productAttributes['panOffset']"
													cssClass="fieldError" id="panOffsetErr" />
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-12">
									<div class="col-lg-4"></div>
									<div class="col-lg-8">
										<div class="col-lg-4">
											<label for="panVerifyLength"><spring:message
													code="Product.panVerifyLength" /></label>
										</div>

										<div class="col-lg-4">
											<form:input path="productAttributes['ibmPanVerifyLength']"
												id="panVerifyLength" name="panVerifyLength" maxlength="2"
												onpaste="return false"
												autocomplete="off" onkeyup="return isNumeric(this)"
												onblur="validatePinFields(this.form.id,this.id)" />
											<div>
												<form:errors path="productAttributes['panVerifyLength']"
													cssClass="fieldError" id="panVerifyLengthErr" />
											</div>
										</div>
									</div>
								</div>


								<div class="col-lg-12">
									<div class="col-lg-4"></div>
									<div class="col-lg-8">
										<div class="col-lg-4">
											<label for="pinBlockFormat"><spring:message
													code="Product.pinBlockFormat" /></label>
										</div>

										<div class="col-lg-4">
											<form:input path="productAttributes['ibmPinBlockFormat']"
												id="pinBlockFormat" name="pinBlockFormat" maxlength="2"
												onpaste="return false" 
												autocomplete="off" onkeyup="return isNumeric(this)"
												onblur="validatePinFields(this.form.id,this.id)" />
											<div>
												<form:errors path="productAttributes['pinBlockFormat']"
													cssClass="fieldError" id="pinBlockFormatErr" />
											</div>
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4"></div>
									<div class="col-lg-8">
										<div class="col-lg-4">
											<label for="decimalisationTable"><spring:message
													code="Product.decimalisationTable" /></label>
										</div>

										<div class="col-lg-4">
											<form:input path="productAttributes['ibmDecimalisationTable']"
												id="decimalisationTable" name="decimalisationTable"
												maxlength="16" onpaste="return false" 
												onkeyup="return isHexaDecimal(this)" autocomplete="off"
												onblur="return validatePvkAndDecimalisationTableFields(this.form.id,this.id)" />
											<div>
												<form:errors path="productAttributes['decimalisationTable']"
													cssClass="fieldError" id="decimalisationTableErr" />
											</div>
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-4"></div>
									<div class="col-lg-8">
										<div class="col-lg-4">
											<label for="padCharacter"><spring:message
													code="Product.padCharacter" /></label>
										</div>

										<div class="col-lg-4">
											<form:input path="productAttributes['ibmPadChar']"
												id="padCharacter" name="padCharacter" maxlength="1"
												onkeyup="return isHexaDecimal(this)" onpaste="return false"
												autocomplete="off"
												onblur="validatePinFields(this.form.id,this.id)" />
											<div>
												<form:errors path="productAttributes['padCharacter']"
													cssClass="fieldError" id="padCharacterErr" />
											</div>
										</div>
									</div>
								</div>

								<div class="col-lg-12 text-center">
									<button type="submit" id="productPIN"
										class="btn btn-primary btn-normal"
										onclick="return pinSubmit(this.form.id,event)">
										<i class="glyphicon glyphicon-saved"></i>
										<spring:message code="button.update" />
									</button>


								</div>
							</div>
						</div>

					</article>
				</section>
			</form:form>
		</div>
	</div>


	<script>
$("#showPINtab").addClass("active");
$("#showPINtab").siblings().removeClass('active');
</script>


</body>
</html>



