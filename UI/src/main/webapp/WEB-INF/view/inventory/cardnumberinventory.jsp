<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/cardnumberinventory.js"></script>
<style type="text/css">
	.top-buffer{
	    margin-top: 60px;
	}
</style>
</head>
<body class="dashboard">
	<div class="body-container" style="min-height: 131px;"> 
		<div class="container"> 
			
			<article class="col-lg-12">	
			 	<ul class="nav nav-tabs col-lg-9 col-lg-offset-1">
					<li class="active"><a href="" data-toggle="tab"
					class="hoverColor"><i class="glyphicon glyphicon-tags"></i>&nbsp;<spring:message
					code="header.product.generatecardnumberinventory"
					text="Generate Card Number Inventory" /></a></li>
				</ul>
		
				<div class="tabresp tab-content">
					<div class="tab-pane fade in active graybox col-lg-10 col-lg-offset-1" id="ScheduleJob">
		
						<div style="margin-top: 8px;position: relative;">
							<c:if test="${successstatus !=''}">
								<div class="text-center  success-green" >
									<b>${successstatus}</b>
								</div>
							</c:if>
							<c:if test="${failstatus !=''}">
								<div class="text-center  error-red" id="formErrorId">
									<b>${failstatus}</b>
											<%-- </center></font></b> --%>
								</div>
							</c:if>
						</div>
		
		
						<form:form id="cardInventoryForm" method="post" commandName="cardInventoryForm">
							<input type="hidden" id="cardRangeId" name="cardRangeId" value=""/>
						</form:form>
			
			
			
						<div class="col-lg-12 top-buffer">
 							<div id="tableDiv" >
								<div id="tableErr" class="fieldError" style="text-align:center;"></div>
						
						
								<table id="tableViewCardNumberInventoryList" class="table table-hover table-striped table-bordered dataTable datagridwithsearch"  style="width: 100% !important;">
							
								<thead>
								<tr>
									<%-- <th style="width:5em;"><spring:message code="button.select"
									text="Select" /></th> --%>
									<th style="width:10em;"><spring:message code="cardnumberinventory.issuer"
									text="Issuer" /></th>
									<th style="width:10em;"><spring:message code="cardnumberinventory.bin/prefix"
									text="Bin/Prefix" /></th>
									<th style="width:10em;"><spring:message code="cardnumberinventory.startcardrange"
									text="Start Card Range" /></th>
									<th style="width:10em;"><spring:message code="cardnumberinventory.endcardrange"
									text="End Card Range" /></th>
									<th style="width:5em;"><spring:message code="cardnumberinventory.cardlength"
									text="Card Length" /></th>
									<th style="width:5em;"><spring:message code="cardnumberinventory.cardinventory"
									text="Card Inventory" /></th>
									<th style="width:6em;"><spring:message code="cardnumberinventory.status"
									text="Status" /></th>
									<th style="width:7em;"><spring:message code="cardnumberinventory.action"
									text="Action" /></th>
								</tr>
							 </thead>	
							 
							 <tbody>
							 	<c:forEach items="${cardInventoryList}" var="cardInventory" varStatus="status">
							 	<tr id="${cardInventory.cardRangeId}">
							 		<%-- <td><input type="checkbox" name="checkDelete" id="checkDelete${status}" value="${block.instrumentId}"></td> --%>
							 		<td>${cardInventory.issuerName}</td>
							 		<td>${cardInventory.prefix}</td>
							 		<td>${cardInventory.startCardNbr}</td>
							 		<td>${cardInventory.endCardNbr}</td>
							 		<td>${cardInventory.cardLength}</td>
							 		<td>${cardInventory.cardInventory}</td>
							 		<td>
							 			<c:if test="${cardInventory.status==''||cardInventory.status==NULL}">
							 			<spring:message code="cardnumberinventory.new" />
							 			</c:if>
							 			<c:if test="${cardInventory.status=='PAUSED'}">
							 			<spring:message code="cardnumberinventory.pause" />
							 			</c:if>
							 			<c:if test="${cardInventory.status=='STARTED'}">
							 			<spring:message code="cardnumberinventory.inprogress" />
							 			</c:if>
							 			<c:if test="${cardInventory.status=='FAILED'}">
							 			<spring:message code="cardnumberinventory.failed" />
							 			</c:if>
							 			
							 			<c:if test="${cardInventory.status=='COMPLETED'}">
							 			<spring:message code="cardnumberinventory.completed" />
							 			</c:if>
							 		
							 		</td>
							 		<td align="center">
							 			
							 			<c:if test="${cardInventory.status==''||cardInventory.status==NULL}">
										<button type="submit" class="btn btn-primary-table-button" 
											id="search_submit"
											onclick="generate(${cardInventory.cardRangeId});">
											<i class='glyphicon glyphicon-edit'></i><spring:message code="button.generate" />
										</button>
										</c:if>	
										<c:if test="${cardInventory.status=='PAUSED'}">
										<button type="submit" class="btn btn-primary-table-button green-btn"
											id="search_submit"
											onclick="doResume(${cardInventory.cardRangeId});">
											<i class='glyphicon glyphicon-play'></i><spring:message code="button.resume" />
										</button>
										</c:if>		
										<c:if test="${cardInventory.status=='STARTED'}">
										<button type="submit" class="btn btn-primary-table-button red-btn"
											id="search_submit"
											onclick="doPause(${cardInventory.cardRangeId});">
											<i class='glyphicon glyphicon-pause'></i><spring:message code="button.pause" />
										</button>
										</c:if>	
										<c:if test="${cardInventory.status=='FAILED'}">
										<button type="submit" class="btn btn-primary-table-button"
											id="search_submit"
											onclick="doRegenerate(${cardInventory.cardRangeId});">
											<i class='glyphicon glyphicon-edit'></i><spring:message code="button.regenerate" />
										</button>
										</c:if>						 		
							 		</td>
							 	</tr>	
							 	</c:forEach>
							 </tbody>
						</table>
					</div>
					
					
				<%-- <div class="col-lg-12">
					<div class="col-lg-5 col-lg-offset-5" style="padding-bottom:10px">
						<button type="button" class="btn btn-primary-table-button" data-toggle="modal" data-target="#" id="deleteBtn"
										onclick="">
										<i class='glyphicon glyphicon-edit'></i>
										<spring:message code="button.generate" text="Generate" />
					</button>
					</div>
				</div>	 --%>
				
					
			</div> 
			</div>
			</div>
			</article>
		</div>
 	</div> 
</body>
</html>





