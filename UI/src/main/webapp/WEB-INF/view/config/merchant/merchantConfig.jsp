<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/merchant.js"></script>

<body>
<c:if test="${successstatus !=''}">
						<div class="col-lg-11 success-green  text-center padding" id="message" >
						<b><strong>${successstatus}</strong></b>
					</div>
			
			</c:if> 
			<c:if test="${failstatus !=''}">
			
					
					<div id="errmessage" class="error-red col-lg-11 text-center padding">
						<b><strong>${failstatus}</strong></b>
					</div>
				
			</c:if>

	<div class="modal fade" id="define-constant-delete" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<form:form commandName="deleteBox" name="delMerchant"
				id="delMerchant" method="post">
				<div class="modal-content">
					<div class="modal-body col-lg-12">
						<div class="col-lg-12" style="display:inline-block">
								<span style="width: 100%; display: inline-block;word-wrap: break-word;">
							 Do you want to delete the Merchant record " <b
								id="merchantNameDisp"></b>" ?
							</span>
						</div>
					</div>
					<form:hidden path="" id="merchantIdToDelete" />
					<input type="hidden" name="merchantName" id="merchantIdToDelete" />
					<div class="modal-footer">
						<button type="button" onclick="deleteMerchant();"
							class="btn btn-primary">
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

	<div class="">
		<div class="body-container" style="min-height: 131px;">
			<div class="container">
				<article class="col-lg-12">

					<div class="graybox col-lg-5 col-lg-offset-3">

						<form:form id="merchantForm" method="post"
							commandName="merchantForm">

							<div class="col-lg-12">
								<h3>
									<spring:message code="merchant.searchMerchant" />
								</h3>
							</div>

							<div class="col-lg-12">
								<div class="">
									<div class="col-lg-4">
										<label for="search_requesterName"> <spring:message
												code="merchant.enterMerchantName" /><font color='red'></font></label>
									</div>

									<div class="col-lg-6">
										<form:input title="Allowed Special Characters are .,;'_- "
											path="merchantName" id="merchantNameId"
											class="textbox xlarge4label trim" name='search_merchantName'
											onkeyup="return isAlphaNumericWithSpecialChars(this)"
											type="textarea" maxlength="100" />
										<font color="red"><span id="merchantNameIdError">
										</span></font>
										<div>
											<form:errors path="merchantName" id="merchantNameId"
												class="textbox xlarge4label" name='search_merchantName'
												cssStyle="color:red" />
										</div>
									</div>

									<div class="col-lg-2 text-right">
										<button type="submit" onclick="searchMerchant(event)"
											class="btn btn-primary" id="search_submit">
											<i class="glyphicon glyphicon-search"></i>
											<spring:message code="button.search" />
										</button>
									</div>

									<div class="col-lg-12 text-center">
										<label> <spring:message code="merchant.hint" /></label>
									</div>

									<input id="merchantId" name="merchantId" type="hidden" value="" />
									<input id="searchType" name="searchType" type="hidden"
										value="${SearchType}" /> <input id="searchedName"
										name="searchedName" type="hidden" value="" /> <input
										id="retrievedName" name="retrievedName" type="hidden"
										value="${SearchedName}" /> <input id="deletedName"
										name="deletedName" type="hidden" value="" /> <input
										type="hidden" name="jsPath" id="jsPath"
										value="${pageContext.request.contextPath}/resources/JS_Messages/" />
								</div>
								<br />
							</div>
						</form:form>
					</div>
				</article>

				<div class="col-lg-12 text-right">
				<security:authorize access="hasRole('ADD_MERCHANT')">
					<button type="button" class="btn btn-primary"
						style="bottom: 15px; position: relative;"
						onclick="clickAddMerchant()">
						<i class='glyphicon glyphicon-plus'></i>
						<spring:message code="merchant.button.addMerchant" />
					</button>
				</security:authorize>
				</div>
						

				<c:if test="${showGrid =='true' }">
					<div class="group">
						<h3>
							<i class="icon-table"></i>
						</h3>
						<div id="tableDiv">
							<table id=""
								class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
								style="width: 100% !important;">
								<thead class="table-head">
									<tr>
										<th><spring:message code="merchant.merchantName" /></th>
										<th><spring:message code="merchant.description" /></th>
										<th><spring:message code="merchant.mdmId" /></th>
										<th><spring:message code="merchant.action" /></th>
									</tr>
								</thead>
								<tbody class="row">
									<c:forEach items="${merchantTableList}" var="merchantDetails"
										varStatus="status">
										<tr id="${merchantDetails.merchantId}">
											<td class="dont-break-out">${merchantDetails.merchantName}</td>
											<td class="dont-break-out">${merchantDetails.description}</td>
											<td>${merchantDetails.mdmId}</td>
											<td>
												<security:authorize access="hasRole('EDIT_MERCHANT')">
												<button type="submit" class="btn btn-primary-table-button"
													id="search_submit"
													onclick="goEditMerchant(${merchantDetails.merchantId});">
													<i class='glyphicon glyphicon-edit'></i>
													<spring:message code="button.edit" />
												</button> 
												</security:authorize>
												
												<security:authorize access="hasRole('VIEW_MERCHANT')">
												<button type="submit" class="btn btn-primary-table-button"
													id="search_submit"
													onclick="goViewMerchant(${merchantDetails.merchantId});">
													<i class='glyphicon glyphicon-list'></i>
													<spring:message code="button.view" />
												</button>
												</security:authorize>
												<c:set var="string1"
													value="${merchantDetails.merchantName}" /> <c:set
													var="string2" value="${fn:replace(string1,'\\'','*')} " />
												<security:authorize access="hasRole('DELETE_MERCHANT')">
												<button type="submit" class="btn btn-primary-table-button"
													id="search_submit" data-toggle="modal"
													data-target="#define-constant-delete"
													onclick="goConfirmDeleteMerchant('${merchantDetails.merchantId}~${string2}')">
													<i class='glyphicon glyphicon-trash'></i>
													<spring:message code="button.delete" />
												</button>
												</security:authorize>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>

						<div id="tableViewUsersStatus">
							<br>
						</div>

					</div>
				</c:if>
			</div>
		</div>
	</div>	
</body>
