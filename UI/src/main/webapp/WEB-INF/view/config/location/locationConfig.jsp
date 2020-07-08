<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
<script src="<c:url value="/resources/js/clpvms/location.js"/>"></script>
</head>

<body>
	<article class="col-lg-12">
		<c:if test="${statusMessage!='' && statusMessage!=null}">
			<br>
			<div id="errmessage" class="error-red col-lg-12 text-center">
				<b>${statusMessage}</b>
			</div>
		</c:if>

		<c:if test="${status!='' && status!=null}">
			<br>
			<div class="col-lg-12 success-green text-center" id="message">
				<b>${status}</b>
			</div>
		</c:if>
	</article>


	<div class="modal fade" id="define-constant-delete" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<form:form commandName="locationSearch" name="locationSearchDelete"
				id="locationSearchDelete" method="post">
				<div class="modal-content">
					<div class="modal-body col-lg-12">
						<div class="col-lg-12">
							<span> Do you want to delete the Location '<b
								id="displayName"></b>' ?
							</span>
						</div>

						<form:hidden path="locationId" id="locationIdModelDelete" />
						<input type="hidden" name="locationModelDeleteName"
							id="locationModelDeleteName" />
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-primary"
							onclick="deleteLocationDet();">
							<i class="glyphicon glyphicon-trash"></i>
							<spring:message code="button.delete" />
						</button>
						<button data-dismiss="modal" onclick="goToPrevious()"
							class="btn btn-primary gray-btn">
							<spring:message code="button.cancel" />
						</button>
					</div>
				</div>
			</form:form>
		</div>
	</div>


	<div class="body-container">
		<div class="container">
			<form:form name="locationSearch"
				action='${pageContext.request.contextPath}/config/location/searchLocation'
				class='form-horizontal groupSearch' commandName="locationSearch"
				id="locationSearch" method="POST">
				<section class="content-container">
					<article class="col-lg-12">
						
						<form:hidden path="locationId" id="locationId"/>

						<div class="graybox col-lg-5 col-lg-offset-3">
							<div class="col-lg-12">
								<h3>
									<spring:message code="location.searchLabel" />
								</h3>

							</div>

							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for="EnterMerchantName"><spring:message
											code="location.merchantNameEnter" /></label>
								</div>
								<div class="col-lg-4">
									<form:input onpaste="false" path="merchantName" class="trim"
										onkeyup="return isAlphaNumericWithSpecialChars(this)"
										onblur="validateMerchantNameSearch(this.form.id,this.id)"
										id="merchantNameSearch" maxlength="100" autocomplete="off"
										style="width:auto" />
									<font color="red"><span id="merchantNameIdError">
									</span></font>
									<div>
										<form:errors path="merchantName" id="merchantNameId"
											name='search_merchantName'
											cssStyle="color:red" />
									</div>
								</div>

								<div class="col-lg-4 text-right">
									<button type="submit" name="button_Search"
										class="btn btn-primary" id="search_submit">
										<i class="glyphicon glyphicon-search"></i>
										<spring:message code="button.search" />
									</button>
								</div>
							</div>

							<div class="col-lg-12 text-center">
								<label> <spring:message
										text="Hint: Empty search retrieves all the Merchant Locations"
										code="location.hint" /></label>
							</div>

						</div>
						<article class="col-lg-12">
							<div class="col-lg-12">
								<div class="pull-right">
								<security:authorize access="hasRole('ADD_LOCATION')">
									<button type="button" class="btn btn-primary btn-add"
										onclick="addLocation()">
										<i class='glyphicon glyphicon-plus'></i>
										<spring:message code="location.addLocationLabel"
											text="Add Location" />
									</button>
									</security:authorize>
								</div>
							</div>
						</article>


						<c:if test="${showGrid =='true' }">
							<div class="location">
								<table id="tableViewUsers"
									class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
									style="width: 100% !important;">

									<thead class="table-head">
										<tr>
											<th><spring:message code="location.merchantName"
													text="Merchant Name" /></th>
											<th><spring:message code="location.locationName"
													text="Location Name" /></th>

											<th style="width: 28em"><spring:message
													code="location.action" text="Action" /></th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${locationList}" var="locationList"
											varStatus="status">
											<tr	id="${locationList.locationId}-${locationList.locationName}">
												<td id="${locationList.merchantName}">${locationList.merchantName}</td>
												<td id="${locationList.locationName}">${locationList.locationName}</td>
												<td>
												<security:authorize access="hasRole('EDIT_LOCATION')">
													<button type="button" class="btn btn-primary-table-button"
														onclick="clickEditForLocation(this,'${locationList.locationId}');">
														<i class="glyphicon glyphicon-edit"></i>
														<spring:message code="button.edit" />
													</button>
													</security:authorize>
													
													<security:authorize access="hasRole('VIEW_LOCATION')">
														<button type="button" class="btn btn-primary-table-button"
														onclick="clickViewForLocation(this,'${locationList.locationId}');">
														<i class="glyphicon glyphicon-list"></i>
														<spring:message code="button.view" />
														</button>
														</security:authorize>
													
													
													<security:authorize access="hasRole('DELETE_LOCATION')">
													<button type="button" class="btn btn-primary-table-button"
														onclick="deleteLocation(this,'${locationList.locationId}');"
														data-toggle="modal" data-target="#define-constant-delete">
														<i class="glyphicon glyphicon-trash"></i>
														<spring:message code="button.delete" />
													</button>
													</security:authorize>
													
													
													
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</c:if>
					</article>
				</section>
			</form:form>
		</div>
	</div>
</body>

</html>