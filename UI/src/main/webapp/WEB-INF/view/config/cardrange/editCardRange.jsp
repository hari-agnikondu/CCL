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

	<div class="modal fade" id="define-constant-edit" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">

			<div class="modal-content">
				<div class="modal-body col-lg-12">
					<div class="col-lg-12" style="display: inline-block">

						<span
							style="width: 100%; display: inline-block; word-wrap: break-word;">
							Do you want to update the Card Range "<b><span
								id="issuerNameDisp"></span></b>" ?
						</span>

					</div>

				</div>
				<div class="modal-footer">
					<button type="button" onclick="goEdit()" class="btn btn-primary">
						<i class="glyphicon glyphicon-saved"></i>
						<spring:message code="button.update" />
					</button>
					<button data-dismiss="modal" class="btn btn-primary gray-btn">
						<spring:message code="button.cancel" />
					</button>

				</div>

			</div>
		</div>
	</div>

	<div class="body-container">
		<div class="container">
			<form:form name="editCardRange" id="editCardRange"
				action="updateCardRange" method="POST" class='form-horizontal '
				commandName="editCardRange">
				<form:hidden path="issuerName" id="issuerName" />
				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class="active SubMenu"><a href="#editCardRange"
								data-toggle="tab"><i class='glyphicon glyphicon-tags'> </i>
									<spring:message code="CardRange.editCardRange" /></a></li>
						</ul>

						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3"
								id="cardRange">

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

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="EnterIssuerName"><spring:message
												code="CardRange.IssuerName" /> <font color='red'>*</font></label>
									</div>

									<div class="col-lg-6">
										<form:select path="issuerId" id="issuerId"
											class="dropdown-medium"
											onblur="return validateDropDownForIssuerName(this.form.id,this.id);">
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
											type="textarea" minlength="2" maxlength="20" />
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
											type="textarea" minlength="6" maxlength="20" />
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
											type="textarea" minlength="2" maxlength="2" />
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
											type="textarea" minlength="5" maxlength="20" />
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
											type="textarea" minlength="5" maxlength="20" />
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
												id="checkDigitYes" name="checkDigit" checked="checked" />
											<label class='radiobox-line' for="checkDigit_status"><spring:message
													code="CardRange.search_status_yes" /></label>
										</div>

										<div class="col-lg-6">
											<form:radiobutton value="N" path="isCheckDigitRequired"
												id="checkDigitNo" name="checkDigit" data-skin="square"
												data-color="blue" />
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
												id="shuffled" name="checkDigityes" checked="checked" />
											<label class='radiobox-line' for="checkDigit_status"><spring:message
													code="CardRange.cardInventory_status_shuffled" /></label>
										</div>

										<div class="col-lg-6">
											<form:radiobutton value="Sequential" path="cardInventory"
												id="sequential" name="checkDigitno" data-skin="square"
												data-color="blue" />
											<label class='radiobox-line' for="checkDigit_status"><spring:message
													code="CardRange.cardInventory_status_sequential" /></label>
										</div>
									</div>
								</div>

								<div class="col-lg-12 text-center">

									<button type="button" id="editButton" data-toggle="modal"
										class="btn btn-primary"
										onclick="FormSubmit(this.form.id,event);">
										<i class="glyphicon glyphicon-saved"></i>
										<spring:message code="CardRange.updateBtn" />
									</button>

									<button type="button" class="btn btn-primary gray-btn"
										onclick="backToSearchCardRange();">
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