<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>

 <body>

      <div class="modal fade" id="define-constant-delete" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<form:form commandName="partnerForm" name="deletePartner" action="deletePartner"
					id="deletePartner" method="post">
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12" style="display:inline-block">
								
							<span style="width: 100%; display: inline-block;word-wrap: break-word;">	Do you want to delete the partner record "<b><span id="partnerNameDisp"></span></b>" ? </span>
								
							</div>

						</div>
						
						<form:input path="partnerId" name="partnerId"
								id="partnerId" type="hidden"/>
						<form:input  path="partnerName" type="hidden" id="partnerName" name="partnerName" />
							<input type="hidden" name="deletePartnerName"
								id="deletePartnerName" />
						<div class="modal-footer">
							<button type="button" onclick="goDeletePartner();"
								class="btn btn-primary"><i class="glyphicon glyphicon-trash"></i><spring:message
									code="button.delete" /></button>
							<button data-dismiss="modal" onclick="goToPrevious()" class="btn btn-primary gray-btn"><spring:message
									code="button.cancel" /></button>

						</div>

					</div>
				</form:form>
			</div>
		</div>
	<div class="body-container" style="min-height: 131px;">     
	<div class="container">
	 <div id="feedBackTd" class="form-group text-center" style="padding-top:1%"> 
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
		</div>
	
   <!--  <article class="col-lg-12">	 -->	
		<div id="searchPartner" class="graybox col-lg-5 col-lg-offset-3">
			<form:form action="searchPartnerByName" method="POST" commandName="partnerForm"
				id="searchForm" name="searchForm">
				<div class="col-lg-12">
					<h3>
						<spring:message code="header.partner.search" text="Search Partner" />
					</h3>
				</div>
				<div class="col-lg-12">

					<!-- <div class="row space"> -->
						<div class="col-lg-4">
							<label for="search_alcode"><spring:message
									text="Enter Partner Name" code="partner.searchName" /></label>
						</div>

						<div class="col-lg-6">
							<form:input type="textarea" title="Allowed Special Characters are ,;'_- ." path="partnerName" class="textbox xlarge4label trim"
								 name="partnerName" id="partnerName"
								onkeyup="return isAlphaNumericWithSpecialChars(this)" maxlength="100"  />
							<form:errors path="partnerName" cssClass="fieldError" />
							<form:input path="partnerId" name="partnerId"
								id="partnerId" type="hidden"/>
							<input type="hidden" name="deletePartnerName"
								id="deletePartnerName" />
						
						</div>	
					<div class="col-lg-2">
							<!-- <p class="text-center"> -->
								<button type="submit" class="btn btn-primary"
									onclick="searchPartner()">
									<i class='glyphicon glyphicon-search'></i>
									<spring:message text="Search" code="button.search" />
								</button>
							<!-- </p> -->
						</div>
					<!-- </div> -->
					<div class="col-lg-12 text-center">
						<label>	<spring:message	text="Hint:Empty search retrieves all partners"
										code="partner.hint" /></label></div>
					<br />
				</div>
			</form:form>
		</div>
	<!-- </div> -->
   <!--  </article> -->
   <security:authorize access="hasRole('ADD_PARTNER')">
    <article class="col-lg-12">		<!-- style="margin-top:-45px"  -->
	<div class="col-lg-12 text-right">
		<button type="button" class="btn btn-primary btn-add"
			onclick="clickAdd()">
			<i class='glyphicon glyphicon-plus'></i>
			<spring:message text="Add Partner" code="partner.add" />
		</button>
	</div>
	</article>
	</security:authorize>

	<div class="group">
		
		<c:if test="${showGrid!=null && showGrid == 'true' }">
			<div id="tableDiv">
				<!-- <table id="tableViewPartners"
					class="table dataTable table-hover table-striped table-bordered datagridwithsearch"
					style="width: 100% !important;overflow-y: scroll;"> -->
					<table id="tableViewPartners"
				class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
				
				style="width: 100% !important;">
					
					<thead class="table-head">
						<tr>
							<th><spring:message code="partner.name"
									text="Partner Name" /></th>
							<th><spring:message
									code="partner.description" text="Description" /></th>
							<th><spring:message code="partner.mdmId"
									text="MDM ID" /></th>
							<th><spring:message code="partner.active"
									text="Active" /></th>		
							<th style="width: 300px;"><spring:message code="button.action"
									text="Action" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${partnerList}" var="partner" varStatus="status">
							<tr id="${partner.partnerId}">
								<td >${partner.partnerName }</td>
								<td>${partner.partnerDesc }</td>
								<td>${partner.mdmId }</td>
								<td id="status1"><c:if test="${partner.isActive=='Y'}"><spring:message code="label.yes" text="Yes" /></c:if>
											<c:if test="${partner.isActive=='N'}"><spring:message code="label.no" text="No" /></c:if></td>
								<td>
									<security:authorize access="hasRole('EDIT_PARTNER')">
									<button type="button" class="btn btn-primary-table-button"
										style="position: relative;"
										onClick="clickEditOrDelete(this,'Edit')">
										<i class='glyphicon glyphicon-edit'></i>
										<spring:message code="button.edit" text="Edit" />
									</button>
									</security:authorize>
									
									<security:authorize access="hasRole('VIEW_PARTNER')">
									<button type="button" class="btn btn-primary-table-button"
										style="position: relative;"
										onClick="clickViewPartner(this,'${partner.partnerId}')">
										<i class='glyphicon glyphicon-list'></i>
										<spring:message code="button.view" text="View" />
									</button>
									</security:authorize>
									
									<security:authorize access="hasRole('DELETE_PARTNER')">
									<button type="button" class="btn btn-primary-table-button" data-toggle="modal" data-target="#define-constant-delete"
										onclick="clickEditOrDelete(this,'Delete')">
										<i class='glyphicon glyphicon-trash'></i>
										<spring:message code="button.delete" text="Delete" />
									</button>
									</security:authorize>
									</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:if>
	</div>
 </div> 
 </div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/partner.js"></script>


