<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>



<script src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/customerProfile.js"></script>

<div class="modal fade" id="define-constant-delete" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<form:form modelAttribute="customerProfileForm" name="deleteCustomerProfileForm"
					id="deleteCustomerProfileForm" method="post" >
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12">
								<span>
									Do you want to delete the customer profile for the account number"<b id="accountNumberDisp"></b>" ? 
								</span>
							</div>
						</div>
						<form:hidden path="profileId" id="profileId" name="profileId"/> 
						<div class="modal-footer">
							<button type="button" onclick="setActionAttrOfForm(this.form.id,'deleteCustomerProfile');"
								class="btn btn-primary"><i class="glyphicon glyphicon-trash"></i><spring:message
									code="button.delete" /></button>
							<button data-dismiss="modal" onclick="goToPrevious()" class="btn btn-primary gray-btn"><spring:message
									code="button.cancel" /></button>
						</div>

					</div>
				</form:form>
			</div>
		</div>
		
<div class="modal fade" id="define-constant-viewcard" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				
					<div class="modal-content">
						<div class="modal-body col-lg-12" id="load1">
					
							
						</div>
						<div class="modal-footer">
							<button data-dismiss="modal" class="btn btn-primary gray-btn"><spring:message
									code="button.back" /></button>
						</div>

					</div>
				
			</div>
		</div>		
<div class="body-container" style="min-height: 131px;">
		<div class="container">
		<div id="feedBackTd" class="col-lg-12 text-center" style="padding-top:7px"> <!-- style="margin-top:10px"> -->
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;" id="feedBackTdError"><c:out value="${statusMessage }"/></p></c:if>
		</div>
		
			<form:form name="customerProfileConfigForm"
				id="customerProfileConfigForm" commandName="customerProfileForm"
				method="POST" action='' class='form-horizontal'>
				<article class="col-lg-12">
					<div class="graybox col-lg-5 col-lg-offset-3">


						<div class="col-lg-12">
							<h3>
								<spring:message code="customerProfile.customer.search"
									text="Search Customer" />
							</h3>
						</div>

						<div class="col-lg-12">
							<div class="col-lg-4">
								<label for="accountNumber"><spring:message
										code="customerProfile.accountNumber.enter"
										text="Enter Account Number" /></label>
							</div>

							<div class="col-lg-6">
								<form:input path="accountNumber" id="accountNumber"
									name="accountNumber" class="textbox xlarge4label trim"
									onkeyup="return isAlphaNumeric(this)" maxlength="20"
									autocomplete="on" />
								<div>
									<form:errors path="accountNumber" cssStyle="color:red" />
								</div>

							</div>
						</div>

						<div class="col-lg-12">
							<div class="col-lg-4">
								<label for="cardNumber"><spring:message
										code="customerProfile.cardNumber.enter"
										text="Enter Card Number" /></label>
							</div>

							<div class="col-lg-6">
								<form:input path="cardNumber" id="cardNumber" name="cardNumber"
									class="textbox xlarge4label trim"
									onkeyup="return isNumeric(this)" maxlength="21"
									autocomplete="on" />
								<div>
									<form:errors path="cardNumber" cssStyle="color:red" />
								</div>

							</div>

							<div class="col-lg-2">
								<%-- <security:authorize access=""> --%>
								<button type="submit" name="button_Search"
									class="btn btn-primary" id="search_submit"
									onclick="return searchCustomerProfile(this.form.id,'searchCustomerProfile');">
									<i class="glyphicon glyphicon-search"></i>
									<spring:message code="button.search" text="Search" />
								</button>
								<%-- </security:authorize> --%>
							</div>
						</div>

						<div class="col-lg-12">
							<div class="col-lg-4">
								<label for="proxyNumber"><spring:message
										code="customerProfile.proxyNumber.enter"
										text="Enter Proxy Number" /></label>
							</div>

							<div class="col-lg-6">
								<form:input path="proxyNumber" id="proxyNumber"
									name="proxyNumber" class="textbox xlarge4label trim"
									onkeyup="return isNumeric(this)" maxlength="19"
									autocomplete="on" />
								<div>
									<form:errors path="proxyNumber" cssStyle="color:red" />
								</div>

							</div>
						</div>
                        <input type="hidden" name="accountNumber_hidden" id="accountNumber_hidden"/>
						<input type="hidden" name="proxyNumber_hidden" id="proxyNumber_hidden" />
						<input type="hidden" name="profileId_hidden" id="profileId_hidden" />
					    <input type="hidden" name="url_viewpage" id="url_viewpage" value="${pageContext.request.contextPath}"/>
						<%-- <div class="col-lg-12 text-center">
						<label> <spring:message code="customerProfile.searchHint" /></label>
						<br />
					</div> --%>

					</div>
				</article>
			 </form:form> 
			<%-- <article class="col-lg-12">
				<div class="pull-right">
					<security:authorize access="hasRole('ADD_PRODUCT')">
						<button type="button" class="btn btn-primary btn-add" style=""
							onclick="clickAddCustomerProfile()">
							<i class='glyphicon glyphicon-plus'></i>
							<spring:message code="customerProfile.addCustomerProfile.button" />
						</button>
					</security:authorize>

				</div>
			</article> --%>
			<!--  <div>
            <a href="showAddCustomerProfile" class="hoverColor" >Edit Customer Profile</a>
            </div> -->
			<c:if test="${showGrid =='true' }">

				<div class="group">


					<table id="tableViewUsers"
						class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
						style="width: 100% !important;">
						<thead class="table-head">
							<tr>
								<th style="width: 85px"><spring:message
										code="customerProfile.profileID" text="Profile ID" /></th>
								<th style="width: 100px"><spring:message
										code="customerProfile.accountNumber" text="Account Number" /></th>
								<th style="width: 100px"><spring:message
										code="customerProfile.proxyNumber" text="Proxy Number" /></th>
								<th style="width: 100px;"><spring:message
										code="customerProfile.cardNumber" text="Card Number" /></th>
								<!-- <th style="width: 100px;"></th> -->
								<th style="width: 300px;" ><spring:message
										code="customerProfile.action" text="Action" /></th>
							</tr>
						</thead>
						<tbody>

							<c:forEach items="${customerProfileList}" var="customerProfile">
								<tr id="${customerProfile.accountNumber }">
								<td>${customerProfile.profileId }</td>
								<td>${customerProfile.accountNumber }</td>
								<td>${customerProfile.proxyNumber }</td>
								<td>${customerProfile.cardNumber }</td>
								<td ><button type="button" id="viewCard"
										class="btn btn-primary-table-button" onclick="viewCard(this,'customerProfileConfigForm')">
										<spring:message code="customerProfile.viewCard.button" />
									</button>
								<!-- </td>
								<td > -->
								
									<%-- <security:authorize access="hasRole('ADD_PRODUCT')"> --%>
									<c:if test="${customerProfile.profileId == '' || customerProfile.profileId == null}">
									<button type="submit" class="btn btn-primary-table-button"
										onclick="clickAddCustomerProfile(this)">
										<i class='glyphicon glyphicon-plus'></i>
										<spring:message code="button.add" />
									</button> </c:if>
									<%-- </security:authorize> --%>
									<c:if test="${customerProfile.profileId != ''  && customerProfile.profileId != null}">
									 <%-- <security:authorize access="hasRole('ADD_PRODUCT')"> --%>
									<button type="submit" class="btn btn-primary-table-button"
										onclick="clickEditCustomerProfile(this)">
										<i class="glyphicon glyphicon-edit"></i>
										<spring:message code="button.edit" />
									</button> <%-- </security:authorize> --%> <%-- <security:authorize access="hasRole('ADD_PRODUCT')"> --%>
									<button type="submit" class="btn btn-primary-table-button"
										data-toggle="modal" data-target="#define-constant-delete"
										onclick="clickDelete(this,'deleteCustomerProfileForm')">
										<i class="glyphicon glyphicon-trash"></i>
										<spring:message code="button.delete" />
									</button> <%-- 	</security:authorize> --%> 
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