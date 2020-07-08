<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
<script src="<c:url value="/resources/js/clpvms/cardRange.js" />"></script>
<script src="<c:url value="/resources/js/clpvms/common.js"/>"></script>

</head>


<body>
	<article class="col-lg-12">
		<div id="messageResult">
			<c:if test="${statusMessage!='' && statusMessage!=null}">
				<div class="error-red text-center">
					<b>${statusMessage}</b>
				</div>
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
			<form:form commandName="cardRangeSearch" name="deleteCardRange"
				id="deleteCardRange" method="post">
				<div class="modal-content">
					<div class="modal-body col-lg-12">
						<div class="col-lg-12">
							<span> Do you want to delete the Card Range record '<b
								id="issuerNameDisp"></b>' ?
							</span>
						</div>

					</div>
					<form:hidden path="cardRangeId" id="cardRangeIdModelDelete" />
					<input type="hidden" name="issuersName"
						id="cardRangeIdModelDeleteName" />
					<div class="modal-footer">
						<button type="button" onclick="goDeleteCardRange();"
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

	<div class="modal fade" id="define-constant-approve" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<form:form commandName="cardRangeSearch" name="cardRangeStatus"
				id="cardRangeStatus" method="post">
				<div class="modal-content">


					<div class="modal-body col-lg-12">
						<div class="col-lg-12">
							<label class="col-lg-3" for="Remarks"><spring:message
									code="CardRange.Remarks" /><font color='red'>*</font></label>
							<div class="text-right mandatory-red">
								<spring:message code="label.mandatory" />
							</div>
							<form:hidden path="cardRangeId" id="cardRangeIdModel" />
							<form:hidden path="status" id="approveStatus" value="APPROVED" />
							<input type="hidden" name="issuersName"
								id="cardRangeIdModelApproveName" /> <span class="col-lg-9">
								<form:textarea path="checkerDesc"
									style="overflow:auto;resize:none" id="statusChangeDesc"
									maxlength="255" cols="25" rows="11" /> <br> <span
								id="dialogErrorApprove" />
							</span>
						</div>

					</div>
					<div class="modal-footer">

						<button type="button" onclick="goApprove();"
							class="btn btn-primary">
							<i class="glyphicon glyphicon-ok-sign"></i>
							<spring:message code="CardRange.approveBtn" />

						</button>

						<button type="button" onclick="goToPrevious()"
							data-dismiss="modal" class="btn btn-primary gray-btn">
							<spring:message code="CardRange.closeBtn" />
						</button>

					</div>

				</div>
			</form:form>
		</div>
	</div>
	<div class="modal fade" id="define-constant-reject" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<form:form commandName="cardRangeSearch" name="cardRangeStatus"
				id="cardRangeStatusReject" method="post">
				<div class="modal-content">


					<div class="modal-body col-lg-12">
						<div class="col-lg-12">
							<label class="col-lg-3" for="Remarks"><spring:message
									code="CardRange.Remarks" /><font color='red'>*</font></label>
							<div class="text-right mandatory-red">
								<spring:message code="label.mandatory" />
							</div>
							<form:hidden path="cardRangeId" id="cardRangeIdModelReject" />
							<form:hidden path="status" id="rejectStatus" value="REJECTED" />
							<input type="hidden" name="issuersName"
								id="cardRangeIdModelRejectName" /> <span class="col-lg-9">
								<form:textarea path="checkerDesc"
									style="overflow:auto;resize:none" id="statusChangeDescReject"
									maxlength="255" cols="25" rows="11" /> <br> <span
								id="dialogErrorReject" />
							</span>
						</div>

					</div>
					<div class="modal-footer">

						<button type="button" onclick="goReject();"
							class="btn btn-primary">
							<i class="glyphicon glyphicon-remove-sign"></i>
							<spring:message code="CardRange.rejectBtn" />
						</button>
						<button type="button" onclick="goToPrevious()"
							data-dismiss="modal" class="btn btn-primary gray-btn">
							<spring:message code="CardRange.closeBtn" />
						</button>

					</div>

				</div>
			</form:form>
		</div>
	</div>


	<div class="body-container" style="min-height: 131px;">
		<div class="container">
			<form:form name="cardRangeSearch"
				class='form-horizontal cardRangeSearch'
				commandName="cardRangeSearch" id="cardRangeSearch" method="POST"
				action='${pageContext.request.contextPath}/config/cardrange/searchCardRange'>
				<section class="content-container">
						<div class="graybox col-lg-5 col-lg-offset-3">

							<input type="hidden" name="cardRangeID" id="cardRangeID" />
							<div class="col-lg-12">
								<h3>
									<spring:message code="CardRange.ForSearchLabel" />
								</h3>
							</div>

							<div class="col-lg-12">
								<div class="col-lg-5">
									<label for="EnterIssuer"><spring:message
											code="CardRange.Label_Issuer" /></label>
								</div>

								<div class="col-lg-5">
									<form:input path="issuerName" id="issuerName" class="trim"
										onkeyup="return isAlphaNumericWithSpecialChars(this)"
										maxlength="100" autocomplete="off" style="width:auto" />
									<div>
										<form:errors path="issuerName" id="issuerNameErr"
											cssStyle="color:red" />
									</div>

								</div>
							</div>

							<div class="col-lg-offset-10">
								<button type="submit" name="button_Search"
									class="btn btn-primary" id="search_submit">
									<i class="glyphicon glyphicon-search"></i>
									<spring:message code="CardRange.button_Search" />
								</button>

							</div>

							<div class="col-lg-12">
								<div class="col-lg-5">
									<label for="EnterPrefix"><spring:message
											code="CardRange.Label_Prefix" /></label>
								</div>

								<div class="col-lg-5">
									<form:input id="prefixsearch" path="prefix"
										onkeyup="return isNumeric(this)"
										onblur="validateEmptyFields(this.form.id,this.id,false)"
										maxlength="20" autocomplete="off" style="width:auto" />
									<div>
										<form:errors path="prefix" id="prefixErr" cssStyle="color:red" />
									</div>
								</div>
							</div>

							<div class="col-lg-12 text-center">
								<label> <spring:message
										text="Hint: Empty search retrieves all Card Ranges"
										code="CardRange.hint" /></label>
							</div>
						</div>
				</section>
			</form:form>

			<security:authorize access="hasRole('ADD_CARDRANGE')">
			<article class="col-lg-12">
				<div class="col-lg-12 ">
					<div class="pull-right">
						<button type="button" class="btn btn-primary btn-add "
							onclick="dispAddCardRange();">
							<i class='glyphicon glyphicon-plus'></i>
							<spring:message code="CardRange.AddCardRangeLabel" />
						</button>
					</div>
				</div>
			</article>
			</security:authorize>

			<c:if test="${showGrid =='true' }">

				<div class="group">


					<table id="tableViewUsers"
						class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
						style="width: 100% !important;">


						<thead class="table-head">
							<tr>
								<th style="width: 4em"><spring:message
										code="CardRange.TabIssuerName" text="Issuer Name" /></th>

								<th style="width: 3em"><spring:message
										code="CardRange.TabPrefix" text="Prefix" /></th>
								<th style="width: 4em"><spring:message
										code="CardRange.TabCardLength" text="Card Length" /></th>

								<th style="width: 4em"><spring:message
										code="CardRange.TabStartCardRange" text="Start Card Range" /></th>
								<th style="width: 4em"><spring:message
										code="CardRange.TabEndCardRange" text="End Card Range" /></th>
								<th style="width: 4em"><spring:message
										code="CardRange.TabCheckDigit" text="Check Digit" /></th>
								<th style="width: 4em"><spring:message
										code="CardRange.TabCardInventory" text="Card Inventory" /></th>
								<th style="width: 4em"><spring:message
										code="CardRange.TabAvailableInventory"
										text="Available Inventory" /></th>
								<th style="width: 4em"><spring:message
										code="CardRange.TabStatus" text="Status" /></th>
								<th style="width: 4em"><spring:message
										code="CardRange.TabCreatedUser" text="Created User" /></th>
								<th style="width: 4em"><spring:message
										code="CardRange.TabApprovedUser" text="Approved / Rejected User" /></th>
								
										<th style="width:10em"><spring:message
										code="CardRange.TabAction" text="Action" /></th>
							</tr>
						</thead>
						<tbody>

							<c:forEach items="${cardRangeList}" var="cardRangeDetails"
								varStatus="status">
								
								<tr
									id="${cardRangeDetails.cardRangeId}#${cardRangeDetails.issuerName}">
									<td class="dont-break-out">${cardRangeDetails.issuerName}</td>

									<td class="dont-break-out" id="${cardRangeDetails.prefix}">${cardRangeDetails.prefix}</td>
									<td id="${cardRangeDetails.cardLength}">${cardRangeDetails.cardLength}</td>
									<td class="dont-break-out"
										id="${cardRangeDetails.startCardNbr}">${cardRangeDetails.startCardNbr}</td>
									<td class="dont-break-out" id="${cardRangeDetails.endCardNbr}">${cardRangeDetails.endCardNbr}</td>

									<td id="${cardRangeDetails.isCheckDigitRequired}">${cardRangeDetails.isCheckDigitRequired}</td>

									<td id="${cardRangeDetails.cardInventory}">${cardRangeDetails.cardInventory}</td>
									<td id="${cardRangeDetails.availableInventory}">${cardRangeDetails.availableInventory}</td>
									<td id="${cardRangeDetails.status}">${cardRangeDetails.status}</td>
									<td id="${cardRangeDetails.insUserName}">${cardRangeDetails.insUserName}</td>
									<td id="${cardRangeDetails.updUserName}">
									<c:if test="${cardRangeDetails.status =='APPROVED' || cardRangeDetails.status =='REJECTED' }">
																${cardRangeDetails.updUserName}	
																</c:if>								
									</td>
									<td id="${cardRangeDetails.action}"><c:if test="${cardRangeDetails.status =='NEW' && cardRangeDetails.insUser!=loginUserId }">

											<security:authorize access="hasRole('APPROVE_CARDRANGE')">
											<button type="submit"
												class="btn btn-primary-table-button green-btn"
												data-toggle="modal" data-target="#define-constant-approve"
												onclick="goStatusChange(this,'${cardRangeDetails.cardRangeId}');">
												<i class="glyphicon glyphicon-ok-sign"></i>
												<spring:message code="CardRange.approveBtn" />
											</button>
											<button type="submit"
												class="btn btn-primary-table-button red-btn"
												data-toggle="modal" data-target="#define-constant-reject"
												onclick="goStatusChange(this,'${cardRangeDetails.cardRangeId}');">
												<i class="glyphicon glyphicon-remove-sign"></i>
												<spring:message code="CardRange.rejectBtn" />
											</button>
											</security:authorize>

											
										</c:if> 
										<c:if test="${cardRangeDetails.status =='REJECTED' && cardRangeDetails.insUser==loginUserId}">
											<security:authorize access="hasRole('EDIT_CARDRANGE')">
											<button type="submit" class="btn btn-primary-table-button"
												onclick="clickEditOrDelete(this,'Edit','${cardRangeDetails.cardRangeId}');">
												<i class="glyphicon glyphicon-edit"></i>
												<spring:message code="CardRange.editBtn" />
											</button>
											</security:authorize>
											</c:if>
											<security:authorize access="hasRole('VIEW_CARDRANGE')">
											<button type="view" class="btn btn-primary-table-button"
												onclick="clickView(this,'${cardRangeDetails.cardRangeId}');">
												<i class="glyphicon glyphicon-list"></i>
												<spring:message code="CardRange.viewBtn" />
											</button>
											</security:authorize>
											
									
											<c:if test="${cardRangeDetails.status =='REJECTED' && cardRangeDetails.insUser==loginUserId}">
											<security:authorize access="hasRole('DELETE_CARDRANGE')">
											<button type="submit" class="btn btn-primary-table-button"
												data-toggle="modal" data-target="#define-constant-delete"
												onclick="goStatusChange(this,'${cardRangeDetails.cardRangeId}');">
												<i class="glyphicon glyphicon-trash"></i>
												<spring:message code="CardRange.deleteBtn" />
											</button>
											</security:authorize>
										</c:if>
										</td>
								</tr>
							</c:forEach>
						</tbody>

					</table>


				</div>
			</c:if>
		
		</div>
	</div>

</body>
</html>