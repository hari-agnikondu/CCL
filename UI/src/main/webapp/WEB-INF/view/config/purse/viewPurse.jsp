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
			<form:form name="viewPurse" id="viewPurse" method="POST"
				class='form-horizontal ' commandName="viewPurse">

				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class="active SubMenu"><a href="#viewPurse"
								data-toggle="tab"><i class='glyphicon glyphicon-tags'></i> <spring:message
										code="purse.viewPurse" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3">

								<form:hidden path="purseId" id="purseId" />

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="purse Type"><spring:message
												code="purse.purseType" /></label>
									</div>
									<div class="col-lg-6">
										<form:hidden path="purseTypeId" id="purseTypeId"
											value="${viewPurse.purseTypeId}" />
										<form:hidden path="purseTypeName" id="purseTypeName"
											value="${viewPurse.purseTypeName}" />

										<label for="purse Type">${viewPurse.purseType}</label>

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
											value="${viewPurse.currencyTypeID}" />
										<form:hidden path="currCodeAlpha" id="currCodeAlpha"
											value="${viewPurse.currCodeAlpha}" />
										<c:choose>
											<c:when
												test="${viewPurse.currencyTypeID!='' && viewPurse.currencyTypeID!=null}">
												<label for="currencyCode">${viewPurse.currCodeAlpha}:${viewPurse.currencyTypeID}</label>
											</c:when>
											<c:otherwise>
												<label for="currencyCode"></label>
											</c:otherwise>
										</c:choose>

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
										<form:hidden path="upc" id="upc" value="${viewPurse.upc}" />
										<label for="UPC">${viewPurse.upc} </label>

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
										<form:hidden path="extPurseId" id="extPurseId" value="${viewPurse.extPurseId}" />
										<label for="extPurseId">${viewPurse.extPurseId} </label>

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
											maxlength="255" cols="25" rows="11" readonly="true" />
										<div>
											<form:errors path="description" cssClass="fieldError" />
										</div>
									</div>
								</div>

								<article class="col-lg-12">
									<div class="col-lg-12 text-center">

										<button type="button" class="btn btn-primary gray-btn"
											onclick="backToSearchPurseFromView();">
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