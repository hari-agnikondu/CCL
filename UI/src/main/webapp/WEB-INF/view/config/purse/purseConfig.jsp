<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<script src="<c:url value="/resources/js/clpvms/purse.js" />"></script>
<script src="<c:url value="/resources/js/clpvms/common.js"/>"></script>
</head>

<body>

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

	<div class="modal fade" id="define-constant-delete" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<form:form commandName="purseSearch" name="deletePurse"
				action='${pageContext.request.contextPath}/config/purse/deletePurse'
				id="deletePurse" method="post">
				<div class="modal-content">
					<div class="modal-body col-lg-12">
						<div class="col-lg-12">
							<span> Do you want to delete the purse record '<b
								id="displayName"></b>' ?
							</span>
						</div>

					</div>
					<form:hidden path="purseId" id="purseId" />
					<div class="modal-footer">
						<button type="submit" class="btn btn-primary">
							<i class="glyphicon glyphicon-trash"></i>
							<spring:message code="CardRange.deleteBtn" />
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
			<form:form name="purseSearch"
				class='form-horizontal purseSearch' commandName="purseSearch"
				id="purseSearch" method="POST">
				<section class="content-container">
					<article class="col-lg-12">
						<form:hidden path="purseId" id="purseId" />

						<div class="graybox col-lg-6 col-lg-offset-3">
							<div class="col-lg-12">
								<h3>
									<spring:message code="purse.searchLabel" />
								</h3>
								
							</div>
							
							<div class="col-lg-12">
								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="EnterConsumerFundedCurrency"><spring:message
												code="purse.Label_ConsumerFundedCurrency" /></label>
									</div>
									<div class="col-lg-5">
										<form:input maxlength="3" onpaste="false"
											path="${consumerFundedCurrency}" onkeyup="return checkValidate('purseSearch',this,'partnerFundedCurrency')" id="consumerFundedCurrency"
											autocomplete="off" style="width:auto"/>
										<div>
											<form:errors path="currencyTypeID"
												cssStyle="color:red" />
										</div>

									</div>
								</div>

								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="EnterPartnerFundedCurrency"><spring:message
												code="purse.Label_PartnerFundedCurrency" /></label>
									</div>
									<div class="col-lg-4">
										<form:input maxlength="3" onpaste="false"
											path="${partnerFundedCurrency}" onkeyup="return checkValidate('purseSearch',this,'consumerFundedCurrency')"
											id="partnerFundedCurrency" autocomplete="off"
											style="width:auto"/>
										<div>
											<form:errors path="currencyTypeID"
												cssStyle="color:red" />
										</div>

									</div>
									<form:input
											path="currencyTypeID" id="currencyTypeId"
											style="display:none" />
									<form:input
											path="purseTypeId" id="purseTypeId"
											style="display:none" />
											
									<div class=" col-lg-offset-10">
										<p class="text-center">
											<button type="submit" name="button_Search"
												class="btn btn-primary" id="search_submit" onclick="search(this.form.id,event)">
												<i class="glyphicon glyphicon-search"></i>
												<spring:message code="CardRange.button_Search" />
											</button>
										</p>
									</div>
								</div>
								<div class="col-lg-12">
									<div class="col-lg-6">
										<label for="EnterExtPurseId"><spring:message
												code="purse.Label_extPurseId" /></label>
									</div>

									<div class="col-lg-5">
										<form:input id="extPurseId" path="extPurseId" maxlength="15" onpaste="false"
											onkeyup="return isAlphaNumericWithAllSpecialChars(this)" autocomplete="off"
											style="width:auto" />
										<div>
											<form:errors path="extPurseId" cssStyle="color:red" />
										</div>
									</div>
								</div>
							</div>

							<div class="col-lg-12 text-center">
								<label> <spring:message
										text="Hint: Empty search retrieves all Purses"
										code="purse.hint" /></label>
							</div>

						</div>

						<security:authorize access="hasRole('ADD_PURSE')">
							<article class="col-lg-12">
								<div class="col-lg-12">
									<div class="pull-right">
										<button type="button" class="btn btn-primary btn-add"
											onclick="dispAddPurse();">
											<i class='glyphicon glyphicon-plus'></i>
											<spring:message code="purse.addPurseLabel" text="Add Purse" />
										</button>
									</div>
								</div>
							</article>
						</security:authorize>


						<c:if test="${showGrid =='true' }">

							<table id="tableViewUsers"
								class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
								style="width: 100% !important;">

								<thead class="table-head">
									<tr>
										<th><spring:message code="purse.purseType"
												text="Purse Type" /></th>
										<th><spring:message code="purse.currencyCode"
												text="Currency Code" /></th>

										<th><spring:message code="purse.description"
												text="Description" /></th>
<%-- 										<th><spring:message code="purse.upc" text="UPC" /></th> --%>
										<th><spring:message code="purse.extPurseId" text="Purse" /></th>
										<th style="width: 28em"><spring:message
												code="purse.action" text="Action" /></th>
									</tr>
								</thead>
								<tbody>

									<c:forEach items="${purseList}" var="purseList"
										varStatus="status">
										<tr id="${purseList.purseId}">
											<td class="dont-break-out" id="${purseList.purseTypeName}">${purseList.purseTypeName}</td>
											<td id="${purseList.currencyTypeID}">${purseList.currencyTypeID}</td>
											<td class="dont-break-out" id="${purseList.description}">${purseList.description}</td>
<%-- 											<td class="dont-break-out" id="${purseList.upc}">${purseList.upc}</td> --%>
											<td class="dont-break-out" id="${purseList.extPurseId}">${purseList.extPurseId}</td>

											<td><security:authorize access="hasRole('EDIT_PURSE')">
													<button type="submit" class="btn btn-primary-table-button"
														onclick="clickEdit(this,'Edit');">
														<i class="glyphicon glyphicon-edit"></i>
														<spring:message code="button.edit" />
													</button>
												</security:authorize> <security:authorize access="hasRole('VIEW_PURSE')">
													<button type="submit" class="btn btn-primary-table-button"
														onclick="clickViewPurse(this,'View');">
														<i class="glyphicon glyphicon-list"></i>
														<spring:message code="button.view" />
													</button>
												</security:authorize> <security:authorize access="hasRole('DELETE_PURSE')">
													<button type="button" class="btn btn-primary-table-button"
														onclick="goDeleteModel(this,'${purseList.purseTypeName}','${purseList.currencyTypeID}','${purseList.upc}');"
														data-toggle="modal" data-target="#define-constant-delete">
														<i class="glyphicon glyphicon-trash"></i>
														<spring:message code="button.delete" />
													</button>
												</security:authorize></td>
										</tr>
									</c:forEach>
								</tbody>

							</table>

						</c:if>
					</article>
				</section>
			</form:form>
		</div>
	</div>
</body>

</html>