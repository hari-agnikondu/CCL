<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<meta http-equiv="Pragma" content="no-cache">
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/switchOverScheduler.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>

<style>
.col-lg-12 {
	padding-bottom: 10px;
}
</style>

<div class="container">

	<article class="col-xs-11" >
		<div id="feedBackTd" class="col-lg-12 text-center">
				<c:if test="${statusFlag=='success' }">
					<p class="successMsg"
						style="text-align: center; font-weight: bold;">
						<c:out value="${statusMessage }"></c:out>
					<p>
				</c:if>
				<c:if test="${statusFlag!='success' }">
					<p class="fieldError"
						style="text-align: center; font-weight: bold;  margin-top: 10px">
						<c:out value="${statusMessage }"></c:out>
					</p>
				</c:if>
			</div> 
		
		
		<ul class="nav nav-tabs col-lg-7">
			<li class="active"><a href="#switchoverscheduler" data-toggle="tab"
				class="hoverColor"><i class="glyphicon glyphicon-tags"></i>&nbsp;<spring:message
						code="header.jobScheduler.switchoverscheduler"
						text="Switch Over Scheduler" /></a></li>

		</ul>
		<div class="tabresp tab-content">
			 
	

			<div class="col-lg-12" id="product">

				<div class="form-inline">
					<form:form name="switchOverForm" id="switchOverForm" action="updateSwitchOverScheduler"
						modelAttribute="SwitchOverScheduler" class='form-horizontal' method="POST">
						<div id="tableDiv">

							<table id="tableViewSchedulers"
								class="table table-hover table-striped table-bordered dataTable "
								style="width: 100% !important;">

								<thead>
									<tr>
										<th style="width: 6em;"><spring:message code="number.sno"
												text="S.No" /></th>
										<th style="width: 15em;"><spring:message
												code="switchoverscheduler.physical.server"
												text="Physical Server" /></th>
										<th style="width: 15em;"><spring:message
												code="switchoverscheduler.managed.server"
												text="Managed Server" /></th>
										<th style="width: 6em;"><spring:message
												code="switchoverscheduler.port" text="Port" /></th>
										<th style="width: 6em; text-align: center;"><spring:message
												code="switchoverscheduler.status" text="Status" /></th>
										<th style="width: 10em;"><spring:message
												code="switchoverscheduler.select.stop"
												text="Select For Stop" /></th>
										<th style="width: 15em;"><spring:message
												code="switchoverscheduler.switchoverserver"
												text="Select For Start/SwitchOver" /></th>
									</tr>
								</thead>
								<tbody>
									<c:set var="count" value="0"></c:set>
									<c:forEach items="${scheduler}" var="scheduler"
										varStatus="status">
										<tr id="${scheduler.physicalServer}">
											<td><c:out value="${status.index+1}" /></td>
											<td >	${scheduler.physicalServer}
											<form:input type="hidden" readonly="true"
													path="physicalServer" 
													name="physicalServer"
													id="${scheduler.physicalServer}_physicalServer" style="width: -webkit-fill-available;"/>
										</td>
										<td >	${scheduler.managedServer}
											<form:input type="hidden" readonly="true"
													path="managedServer" 
													name="managedServer"
													id="${scheduler.physicalServer}_managedServer" style="width: -webkit-fill-available;"/>
										</td>
										<td >	${scheduler.port} <form:input type="hidden" readonly="true"
													path="port" 
													name="port"
													id="${scheduler.physicalServer}_port" style="width: -webkit-fill-available;"/>
										</td>
										<td>	
										
										<c:choose>
													<c:when test="${scheduler.status=='Y' }"> Yes
													<form:input type="hidden" readonly="true"
													path="status" 
													name="status"
													id="${scheduler.physicalServer}_status" style="width: -webkit-fill-available;"/>
														
													</c:when>
													<c:otherwise> No
													<form:input type="hidden" readonly="true"
													path="status" 
													name="status"
													id="${scheduler.physicalServer}_status" style="width: -webkit-fill-available;"/>
														
													</c:otherwise>
												</c:choose>
										
										</td>

										<td style="text-align: center;"><c:choose>
													<c:when test="${scheduler.status=='Y' }">
														<input type="radio" id="selectForStop"
															name="selectForStop" 
															value="${scheduler.physicalServer}~${scheduler.managedServer}~${scheduler.port}~${scheduler.status}" />
													</c:when>
													<c:otherwise>	-
													</c:otherwise>
												</c:choose>
										</td>

										<td style="text-align: center;"><c:choose>
													<c:when test="${scheduler.status=='Y' }">	-
													</c:when>
													<c:otherwise>
														<input type="radio" 
															id="selectForStart" name="selectForStart"
															value="${scheduler.physicalServer}~${scheduler.managedServer}~${scheduler.port}~${scheduler.status}" />
													</c:otherwise>
												</c:choose>
										</td>


										</tr>
									</c:forEach>
								</tbody>
							</table>

							<div class="col-lg-12 text-center" style="padding-top: 10px">
								<!-- <div class="col-lg-5 col-lg-offset-4"> -->
									<button type="button" class="btn btn-primary"
										onClick="onStart()" value="Start">

										<spring:message code="button.start" text="Start" />
									</button>

									<button type="button" class="btn gray-btn btn-primary"
										onClick="onStop()" value="Stop">

										<spring:message code="button.stop" text="Stop" />
									</button>
									<button type="button" class="btn gray-btn btn-primary"
										onClick="onSwitchover()" value="SwitchOver">

										<spring:message code="button.switchover" text="SwitchOver" />
									</button>
								<!-- </div> -->

							 </div> 
						</div>


					</form:form>
				</div>
			</div>
		</div>
	</article>
</div>

<script>
function onStart() {

		if (($('input[name="selectForStop"]').is(':checked')) && ($('input[name="selectForStart"]').is(':checked'))) {
			alert("Please unselect the stop server ")
			return;
		} else if ($('input[name="selectForStart"]').is(':checked')){
			$("#switchOverForm").attr('action', 'updateSwitchOverScheduler');
			$("#switchOverForm").submit();
		}
		else {
			alert("Please select one server to start")
			return
		}
}

function onStop() {
	if (($('input[name="selectForStart"]').is(':checked')) && ($('input[name="selectForStop"]').is(':checked'))) {
		alert("Please unselect the Start server ")
		return;
	}
	else if ($('input[name="selectForStop"]').is(':checked')) {
			$("#switchOverForm").attr('action', 'updateSwitchOverScheduler');
			$("#switchOverForm").submit();
	} else {
			alert("Please select One Server to Stop")
			return;
	}
}
function onSwitchover(data, action) {
		if (($('input[name="selectForStart"]').is(':checked')) && ($('input[name="selectForStop"]').is(':checked'))) {
			$("#switchOverForm").attr('action', 'updateSwitchOverScheduler');
			$("#switchOverForm").submit();
			
		} else if ($('input[name="selectForStart"]').is(':checked')) {
			alert("Please select one servers to stop");
			return;
		}else if ($('input[name="selectForStop"]').is(':checked')) {
			alert("Please select one servers to start");
			return;
		}  
		else {
			alert("Please select one servers to start and start");
			return;
		}
}
</script>

