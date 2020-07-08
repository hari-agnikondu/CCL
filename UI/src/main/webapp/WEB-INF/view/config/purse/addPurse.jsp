<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<script src="<c:url value="/resources/js/clpvms/purse.js" />"></script>
<script src="<c:url value="/resources/js/clpvms/common.js"/>"></script>
</head>

<body onload="purseTypeDivDisp()">

	<div class="body-container">
		<div class="container">
			<form:form name="addPurse" id="addPurse" method="POST"
				class='form-horizontal ' commandName="addPurse">
				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-5 col-lg-offset-3">
							<li class="active SubMenu"><a href="#addPurse"
								data-toggle="tab"><i class='glyphicon glyphicon-tags'></i> <spring:message
										code="purse.addPurse" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3">
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

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="purse Type"><spring:message
												code="purse.purseType" /><font color='red'>*</font></label>
									</div>
									<div class="col-lg-6">
										<form:select path="purseTypeId" id="purseTypeId"
											class="dropdown-medium" onchange="purseTypeDivDisp()"
											onblur="validateDropDownFields(this.form.id,this.id)">
											<form:option value="-1" label="- - - Select - - -" />
											<form:options items="${purseTypeList}" />
										</form:select>
										<div>
											<form:errors path="purseTypeId" cssClass="fieldError" />
										</div>
									</div>
								</div>
								<div class="col-lg-12" id="currencyDiv" style="display: none;">
									<div class="col-lg-6">
										<label for="currencyCode"><spring:message
												code="purse.currencyCode" /><font color='red'>*</font></label>
									</div>
									<div class="col-lg-6">
										<form:select path="currencyTypeID" id="currencyTypeID"
											onblur="validateDropDownFields(this.form.id,this.id)">
											<form:option value="-1" label="- - - Select - - -" />
											<form:options items="${currencyCodeList}" />
										</form:select>
										<div>
											<form:errors path="currencyTypeID" cssClass="fieldError" />
										</div>
									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="extPurseId"><spring:message code="purse.extPurseId" /><font
											color='red'>*</font></label>
									</div>
									<div class="col-lg-6">
										<form:input path="extPurseId" id="extPurseId" class="textbox textbox-xlarge" 
											autocomplete="off" onkeyup="return isAlphaNumericWithAllSpecialChars(this)"
											onblur="validateFields(this.form.id,this.id,true)" 
											maxlength="15" minlength="1"/>
										<div>
											<form:errors path="extPurseId" cssClass="fieldError" />
										</div>
									</div>
								</div>
								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="description"><spring:message
												code="purse.description" /></label>
									</div>
									<div class="col-lg-6">
										<form:textarea path="description"
											style="overflow:auto;resize:none" id="description"
											maxlength="255" cols="25" rows="11" class="textarea-medium" />
										<div>
											<form:errors path="description" cssClass="fieldError" />
										</div>
									</div>
								</div>

								<article class="col-lg-12">
									<div class="col-lg-12 text-center">
										<button type="button" id="addPurseButton"
											class="btn btn-primary"
											onclick="FormSubmit(this.form.id,event);">
											<i class="glyphicon glyphicon-plus"></i>
											<spring:message code="button.add" />
										</button>
										<button type="button" onclick="goReset();"
											class="btn btn-primary gray-btn">
											<i class="glyphicon glyphicon-refresh"></i>
											<spring:message code="button.reset" />
										</button>
										<button type="button" class="btn btn-primary gray-btn"
											onclick="backToSearchPurse();">
											<i class="glyphicon glyphicon-backward"></i>
											<spring:message code="button.back" />
										</button>
									</div>
								</article>
							</div>
						</div>
					</article>
				</section>
			</form:form>
		</div>
	</div>

	<script type="text/javascript">
		function backToSearchPurse() {

			$("#addPurse").attr('action', 'purseConfig')
			$("#addPurse").submit();

		}
	</script>
</body>

</html>