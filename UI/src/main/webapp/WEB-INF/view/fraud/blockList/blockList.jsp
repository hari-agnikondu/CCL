<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/blockList.js"></script>
 <body>
  	<div class="body-container" style="min-height: 131px;">     
	<div class="container"> 
		<div id="feedBackTd" class="form-group text-center" style="padding-top:7px"> <!-- style="margin-top:10px"> -->
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
		</div>
	
		<div class="graybox col-lg-5 col-lg-offset-3">
			<form:form action="searchBlockListByDeliveryChannel" method="POST" commandName="blockListForm"
				id="searchForm" name="searchForm">
				<div class="col-lg-12">
					<h3>
						<spring:message code="header.search.deliveryChannel" text="Search By Delivery Channel" />
					</h3>
				</div>
				<div class="col-lg-12">

							<div class="col-lg-4">
							<label for="search_alcode"><spring:message
									text="Delivery Channel" code="blockList.deliveryChannel" /></label>
						</div>

						<div class="col-lg-6">
							<form:select  path="channelCode"
								 name="channelCode" id="channelCode">
								<form:option value="-1">--Select--</form:option>
									<form:options items="${deliveryChannelList}"/>
							</form:select>
							<form:errors path="channelCode" cssClass="fieldError" />
						
						</div>
							
						<div class="col-lg-2">
								<button type="submit" class="btn btn-primary"
									onclick="searchBlockList()">
									<i class='glyphicon glyphicon-search'></i>
									<spring:message text="Search" code="button.search" />
								</button>
						</div>
					<div class="col-lg-12 text-center">
						<label>	<spring:message	text="Hint:Select option retrieves all delivery channel records"
										code="blockList.hint" /></label></div>
					<br />
				</div>
			</form:form>
		</div>

   <!--  </article> -->
      <security:authorize access="hasRole('ADD_BLOCKLIST')">
    <article class="col-lg-12">		
	<div class="col-lg-12 text-right">
		<button type="button" class="btn btn-primary btn-add"
			onclick="clickAdd()">
			<i class='glyphicon glyphicon-plus'></i>
			<spring:message text="Add To Block List" code="blockList.add" />
		</button>
	</div>
	</article>
	</security:authorize>
	
	<div class="col-lg-12">
		<form:form  name="gridForm" action="deleteFromBlockList"  id="gridForm" method="post">
		<c:if test="${showGrid!=null && showGrid == 'true' }">
			
			<div id="tableDiv" >
			<div id="tableErr" class="fieldError" style="text-align:center;"></div>
					<table id="tableViewBlockList"
				class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
				style="width: 100% !important;">
					
					<thead>
						<tr>
							<th style="width:5em;"><spring:message code="button.action"
									text="Action" /></th>
							<th style="width:20em;"><spring:message code="blockList.deliveryChannel"
									text="Delivery Channel" /></th>
							<th style="width:20em;"><spring:message
									code="blockList.instrumentType" text="Instrument Type" /></th>
							<th style="width:20em;"><spring:message code="blockList.instrumentID"
									text="Instrument ID" /></th>
						
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${blockList}" var="block" varStatus="status">
							<tr id="${block.instrumentId}">
								<td><input type="checkbox" name="checkDelete" id="checkDelete${status}" value="${block.instrumentId}"></td>
								<td >${deliveryChannelList[block.channelCode] }</td>
								<td>${block.instrumentType }</td>
								<td>${block.instrumentId }</td>
								
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="col-lg-12">
				<div class="col-lg-5 col-lg-offset-5" style="padding-bottom:10px">
					<security:authorize access="hasRole('DELETE_BLOCKLIST')">
					<button type="button" class="btn btn-primary-table-button" data-toggle="modal" data-target="#" id="deleteBtn"
										onclick="clickDelete(this,'Delete')">
										<i class='glyphicon glyphicon-trash'></i>
										<spring:message code="button.delete" text="Delete" />
					</button>
					</security:authorize>
				</div>
			</div>
			
		</c:if>
		<input type="hidden" name="search_channelCode" id="search_channelCode" />
		   <div class="modal fade" id="define-constant-delete" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12" style="display:inline-block">
								
							<span style="width: 100%; display: inline-block;word-wrap: break-word;">Do you want to delete selected records ? <b><span id="blockRecordsDisp"></span></b> </span>
								
							</div>

						</div>
						
						<input type="hidden" name="deleteDelcode" id="deleteDelcode" />
						<div class="modal-footer">
							<button type="submit"
								class="btn btn-primary"><i class="glyphicon glyphicon-trash"></i><spring:message
									code="button.delete" /></button>
							<button data-dismiss="modal" type="button" onclick="goToPrevious()" class="btn btn-primary gray-btn"><spring:message
									code="button.cancel" /></button>

						</div>

					</div>
				
			</div>
		</div> 
		</form:form>
	</div>
</div>
</div>
</body>

<script>
$('input[name="checkDelete"]').on('change', function() {
	if($("input[name=checkDelete]:checked").length > 0){
		$("#tableErr").html('');
	}
	
});


</script>

