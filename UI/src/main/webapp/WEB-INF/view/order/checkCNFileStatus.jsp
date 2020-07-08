 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/order.js"></script>

<body>
	<div class="body-container" style="min-height: 131px;">     
	<div class="container">
	<div id="feedBackTd" class="text-center" style="padding-top:4px"> 
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
	</div>
	<div>
	</div>
	<div class="group">
	<form:form action="#" method="POST" id="cnFileStatusForm" name="cnFileStatusForm">
		<div id="TableData">				
		<div id="tableDiv">
			<div id="tableErr" class="fieldError" style="text-align:center;"></div>
					<table id="tableViewCNFileStatus"
				class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
				style="width: 100% !important;">
					
					<thead>
						<tr>
							<th style="width:8em;"><spring:message code="order.CNFileList"
									text="CN File List" /></th>
							<th style="width:2em;"><spring:message code="order.status"
									text="Status" /></th>
							<th style="width:2em;"><spring:message code="order.date"
									text="Date" /></th>
							<th style="width:11em;"><spring:message code="order.ErrorMsg"
									text="Error Occured" /></th>						
							
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${cnFileList}" var="cnFile" >
						<tr id="${cnFile.FILE_NAME}">
                        <td>${cnFile.FILE_NAME}</td>
                        <td>${cnFile.STATUS}</td>
                        <td>${cnFile.INS_DATE}</td>
                        <td>${cnFile.ERROR_MSG}</td>
                        </tr>
						</c:forEach>
					</tbody>
				</table>
		</div>
	<%-- 	<div class="col-lg-12">
				<div class="col-lg-5 col-lg-offset-5" style="padding-bottom:10px">
				
					<security:authorize access="hasRole('SEARCH_ORDER')">
					<button type="button" class="btn btn-primary-table-button green-btn" data-toggle="modal" data-target="#" id="GenerateBtn"
										onclick="uploadFiles(this.form.id)">
										<i class="glyphicon glyphicon-ok-sign"></i>
										<spring:message code="button.uploadBtn" text="Upload" />
					</button>
					</security:authorize>

				</div>
			</div> --%>
			</div>

			
			</form:form>
	</div>
 </div> 
 </div>
</body>



