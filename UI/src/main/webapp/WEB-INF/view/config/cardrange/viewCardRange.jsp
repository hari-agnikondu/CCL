<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<script src="<c:url value="/resources/js/clpvms/cardRange.js" />"></script>
<script src="<c:url value="/resources/js/clpvms/common.js"/>"></script>

</head>
<body>

	
	<div class="body-container">
		<div class="container">
			<form:form name="viewCardRange" id="viewCardRange"
				 method="POST" class='form-horizontal '
				commandName="viewCardRange">
				<form:hidden path="issuerName" id="issuerName" />
				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class="active SubMenu"><a href="#viewCardRange"
								data-toggle="tab"><i class='glyphicon glyphicon-tags'> </i>
									<spring:message code="CardRange.viewCardRange" /></a></li>
						</ul>

						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3"
								id="cardRange">

							


								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="EnterIssuerName"><spring:message
												code="CardRange.IssuerName" /> <font color='red'>*</font></label>
									</div>

									<div class="col-lg-6">
										<form:select path="issuerId" id="issuerId"
											class="dropdown-medium"
											onblur="return validateDropDownForIssuerName(this.form.id,this.id);" disabled="true">
											<form:option value="-1" label="- - - Select - - -" />
											<form:options items="${issuerList}" />
										</form:select>
										<div>
											<form:errors path="issuerId" cssClass="fieldError" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="Prefix"><spring:message
												code="CardRange.Network" /></label>
									</div>

									<form:hidden path="cardRangeId" id="cardRangeId" />
									<form:hidden path="status" id="status" />

									<div class="col-lg-6">
										<form:input path="network" id="network" autocomplete="off"
											onblur="return validateNetworkField(this.form.id,this.id)"
											type="textarea" minlength="2" maxlength="20" readonly="true"/>
										<div>
											<form:errors path="network" cssClass="fieldError" />
										</div>
									</div>

								</div>

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="Prefix"><spring:message
												code="CardRange.Prefix" /><font color='red'>*</font></label>
									</div>

									<div class="col-lg-6">
										<form:input path="prefix" id="prefix" autocomplete="off"
											onkeyup="return isNumeric(this)"
											onblur="validateFields(this.form.id,this.id,true)"
											type="textarea" minlength="6" maxlength="20" readonly="true" />
										<div>
											<form:errors path="prefix" cssClass="fieldError" />
										</div>
									</div>

								</div>

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="cardLength"><spring:message
												code="CardRange.CardLength" /><font color='red'>*</font></label>
									</div>

									<div class="col-lg-6">
										<form:input path="cardLength" id="cardLength"
											autocomplete="off" onkeyup="return isNumeric(this)"
											onblur="validateFields(this.form.id,this.id,true)"
											type="textarea" minlength="2" maxlength="2" readonly="true"/>
										<div>
											<form:errors path="cardLength" cssClass="fieldError" />
										</div>

									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="startCardRange"><spring:message
												code="CardRange.StartCardRange" /><font color='red'>*</font></label>
									</div>

									<div class="col-lg-6">
										<form:input path="startCardNbr" id="startCardRange"
											autocomplete="off" onkeyup="return isNumeric(this)"
											onblur="validateFields(this.form.id,this.id,true)"
											type="textarea" minlength="5" maxlength="20" readonly="true"/>
										<div>
											<form:errors path="startCardNbr" cssClass="fieldError" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="endCardRange"><spring:message
												code="CardRange.EndCardRange" /><font color='red'>*</font></label>
									</div>

									<div class="col-lg-6">
										<form:input path="endCardNbr" id="endCardRange"
											autocomplete="off" onkeyup="return isNumeric(this)"
											onblur="validateFields(this.form.id,this.id,true)"
											type="textarea" minlength="5" maxlength="20" readonly="true"/>
										<div>
											<form:errors path="endCardNbr" cssClass="fieldError" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="checkDigit"><spring:message
												code="CardRange.CheckDigit" /></label>
									</div>

									<div class="col-lg-6">
										<div class="col-lg-6">
											<form:radiobutton value="Y" path="isCheckDigitRequired"
												id="checkDigitYes" name="checkDigit" checked="checked" disabled="true" />
											<label class='radiobox-line' for="checkDigit_status"><spring:message
													code="CardRange.search_status_yes" /></label>
										</div>

										<div class="col-lg-6">
											<form:radiobutton value="N" path="isCheckDigitRequired"
												id="checkDigitNo" name="checkDigit" data-skin="square"
												data-color="blue" disabled="true"/>
											<label class='radiobox-line' for="checkDigit_status"><spring:message
													code="CardRange.search_status_no" /></label>
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="cardInventory"><spring:message
												code="CardRange.cardInventory" /></label>
									</div>

									<div class="col-lg-6">
										<div class="col-lg-6">
											<form:radiobutton value="Shuffled" path="cardInventory"
												id="shuffled" name="checkDigityes" checked="checked" disabled="true"/>
											<label class='radiobox-line' for="checkDigit_status"><spring:message
													code="CardRange.cardInventory_status_shuffled" /></label>
										</div>

										<div class="col-lg-6">
											<form:radiobutton value="Sequential" path="cardInventory"
												id="sequential" name="checkDigitno" data-skin="square"
												data-color="blue" disabled="true"/>
											<label class='radiobox-line' for="checkDigit_status"><spring:message
													code="CardRange.cardInventory_status_sequential" /></label>
										</div>
									</div>
								</div>

								<div class="col-lg-12 text-center">
									<button type="button" class="btn btn-primary gray-btn"
										onclick="backFromViewToSearchCardRange();">
										<i class="glyphicon glyphicon-backward"></i>
										<spring:message code="CardRange.backBtn" />
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