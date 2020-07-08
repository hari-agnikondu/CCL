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

	<div class="modal fade" id="define-constant-editModel" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body col-lg-12">
					<div class="col-lg-12">
						<span> Do you want to update the purse record '<b
							id="displayName"></b>' ?
						</span>
					</div>

				</div>
				<div class="modal-footer">

					<button type="submit" class="btn btn-primary"
						onclick="updatePurse()">
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
			<form:form name="editPurse" id="editPurse" method="POST"
				class='form-horizontal ' commandName="editPurse">

				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class="active SubMenu"><a href="#editCardRange"
								data-toggle="tab"><i class='glyphicon glyphicon-tags'></i> <spring:message
										code="purse.editPurse" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3">

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

								<form:hidden path="purseId" id="purseId" />

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="purse Type"><spring:message
												code="purse.purseType" /></label>
									</div>
									<div class="col-lg-6">
										<form:hidden path="purseTypeId" id="purseTypeId"
											value="${editPurse.purseTypeId}" />
										<form:hidden path="purseTypeName" id="purseTypeName"
											value="${editPurse.purseTypeName}" />

										<label for="purse Type">${editPurse.purseType}</label>

										<%-- <form:select path="purseTypeId" id="purseTypeId"
											disabled="true">
											<form:option value="-1" label="- - - Select - - -" />
											<form:options items="${purseTypeList}" />
										</form:select> --%>
										<div>
											<form:errors path="purseTypeId" cssClass="fieldError" />
										</div>
									</div>
								</div>

								<div class="col-lg-12" id="currencyDiv" style="display: none;">
									<div class="col-lg-6">
										<label for="currencyCode"><spring:message
												code="purse.currencyCode" /></label>
									</div>

									<div class="col-lg-6">
										<form:hidden path="currencyTypeID" id="currencyTypeID"
											value="${editPurse.currencyTypeID}" />
										<form:hidden path="currCodeAlpha" id="currCodeAlpha"
											value="${editPurse.currCodeAlpha}" />
										<c:choose>
											<c:when
												test="${editPurse.currencyTypeID!='' && editPurse.currencyTypeID!=null}">
												<label for="currencyCode">${editPurse.currCodeAlpha}:${editPurse.currencyTypeID}</label>
											</c:when>
											<c:otherwise>
												<label for="currencyCode"></label>
											</c:otherwise>
										</c:choose>

										<%-- <form:select path="currencyTypeID" id="currencyTypeID"
											disabled="true">
											<form:option value="-1" label="- - - Select - - -" />
											<form:options items="${currencyCodeList}" />
										</form:select> --%>
										<div>
											<form:errors path="currencyTypeID" cssClass="fieldError" />
										</div>
									</div>
								</div>

								<div class="col-lg-12" id="upcDiv" style="display: none;">
									<div class="col-lg-6">
										<label for="UPC"><spring:message code="purse.upc" /></label>
									</div>
									<div class="col-lg-6">
										<form:hidden path="upc" id="upc" value="${editPurse.upc}" />
										<label for="UPC">${editPurse.upc} </label>

										<%-- <form:input path="upc" id="upc" readonly="true" maxlength="12"
											onkeyup="return isNumeric(this)" autocomplete="off"
											onpaste="false" /> --%>
										<div>
											<form:errors path="upc" cssClass="fieldError" />
										</div>
									</div>
								</div>
								<div class="col-lg-12" id="extPurseIdDiv">
									<div class="col-lg-6">
										<label for="extPurseId"><spring:message code="purse.extPurseId" /></label>
									</div>
									<div class="col-lg-6">
										<form:hidden path="extPurseId" id="extPurseId" value="${editPurse.extPurseId}" />
										<label for="extPurseId">${editPurse.extPurseId} </label>
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
										<form:textarea onpaste="false" path="description"
											style="overflow:auto;resize:none" id="description"
											maxlength="255" cols="25" rows="11" />
										<div>
											<form:errors path="description" cssClass="fieldError" />
										</div>
									</div>
								</div>

								<article class="col-lg-12">
									<div class="col-lg-12 text-center">
										<button type="button" id="updatePurse" class="btn btn-primary"
											onclick="FormSubmit(this.form.id,event);" data-toggle="modal"
											data-target="#define-constant-editModel">
											<i class="glyphicon glyphicon-saved"></i>
											<spring:message code="button.update" />
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

</body>
</html>