<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<meta http-equiv="Pragma" content="no-cache">
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/jobScheduler.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js">
	
	
	</script>

<style>
.col-lg-12 {
	padding-bottom: 10px;
}
</style>
<body onLoad="iniForm()" >
<div class="container">

	<article class="col-lg-12">
		<ul class="nav nav-tabs col-lg-9 col-lg-offset-1">
			<li class="active"><a href="#JobShceduler" data-toggle="tab"
				class="hoverColor"><i class="glyphicon glyphicon-tags"></i>&nbsp;<spring:message
						code="header.jobScheduler.schedulerService" text="Scheduler Service" /></a></li>

		</ul>
		<div class="tabresp tab-content">
			<div class="tab-pane fade in active graybox col-lg-9 col-lg-offset-1" id="ScheduleJob">

			

				<div id="tableDiv" >
				<form:form  name="jobSchedulerServiceJobs" action="jobSchedulerServiceJobs"
					id="processTrigger" method="post">

					<table  style="width: 30%; border-width: 1px; border-spacing: 1px; "
						id="tableViewSchedulerService"
						class="table table-hover table-striped table-bordered dataTable">
						<tbody>

							<tr>
								<th style="text-align: center;">Scheduler Service :
								<c:choose>
								<c:when test="${msSchedRunFlag == null || msSchedRunFlag == 'Running'}">
								<c:out value="Running"></c:out>
								</c:when>
								<c:otherwise><c:out value="Stopped"></c:out></c:otherwise>
								</c:choose>
								</th>

							</tr>

							<tr>
								<td align="center"><button type="button"
										class="btn btn-primary-table-button"
										name = "btnStart" id = "btnStart"
										style="position: relative;">
										<i class='glyphicon glyphicon-play'></i>
										<spring:message code="button.start" text="Start" />
									</button>&nbsp;&nbsp;
									<button type="button" class="btn btn-primary-table-button"
									name = "btnStop" id = "btnStop">
										<i class='glyphicon glyphicon-stop'></i>
										<spring:message code="button.stop" text="Stop" />
									</button></td>

							</tr>
						</tbody>
						
					</table>
					<input type=hidden name=hProcess id=hProcess>
					<input type=hidden name=hReqFrom id=hReqFrom>
					</form:form>
				</div>&nbsp;
				
				<div id="tableDiv2">
				
					<table id="tableViewJobs"
						class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
						style="width: 75% important;">

						<thead>
							<tr>
								<th style="width: 22em;"><spring:message
										code="jobScheduler.jobName" text="Job Name" /></th>
								<th style="width: 20em;"><spring:message
										code="jobScheduler.status" text="Status" /></th>
								<th style="width: 22em;"><spring:message
										code="jobScheduler.nextScheduledRun" text="Next Scheduled Run" /></th>
							</tr>
						</thead>


<tbody>
						
								<c:forEach items="${schedulerServiceJobs}" var="schedulerServiceJobsList" varStatus="status">
							<tr>
							<td><c:out value="${schedulerServiceJobsList[0]}" /></td>
               				 <td><c:out value="${schedulerServiceJobsList[1]}" /></td>
              				  <td><c:out value="${schedulerServiceJobsList[2]}" /></td>
								
							</tr>
						</c:forEach>
						
					</tbody>

					</table>
			
				</div>
			</div>

		</div>
	</article>
</div>
</body>
<script>
	

/* 	function startService()
	{
		
		$('#hProcess').val("ST");
		$('#hReqFrom').val("RSS1");
		//$('#btnStart').attr("disabled" true);
		$('#processTrigger').attr('action','jobSchedulerTrigger');
		$('#processTrigger').submit();
	}

	function stopService()
	{
		$('#hProcess').val("SP");
		$('#hReqFrom').val("RSS1");
		//$('#btnStart').attr("disabled" true);
		$('#processTrigger').submit(); 
	  
	}*/
	function iniForm()
	{
	  if("${msSchedRunFlag}" != "Running")
	  {
		 
		  $('#btnStop').prop('disabled', true);
	  }
	  else
	  {
		  $('#btnStart').prop('disabled', true);
	  }


	}
	
</script>