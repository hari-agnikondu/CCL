<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<meta http-equiv="Pragma" content="no-cache">
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/jobScheduler.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
	
<style>
	.col-lg-12{
		padding-bottom:8px;
	
	}
	input[type="radio"], input[type="checkbox"] {
    	margin: 9px 0 0;
   		margin-top: 1px \9;
    	line-height: normal;
	}
	
</style>

<div class="container">

	<article class="col-lg-12">
		<div id="feedBackTd" class="form-group text-center">
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out><p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out></p></c:if>
			</div>
		
		<ul  class="nav nav-tabs col-lg-9 col-lg-offset-1">
			<li class="active"><a href="#partner" data-toggle="tab"
				class="hoverColor"><i class="glyphicon glyphicon-tags"></i>&nbsp;<spring:message
						code="header.jobScheduler.scheduleJob" text="Schedule Job" /></a></li>

		</ul>
		<div class="tabresp tab-content">
			
			<div class="tab-pane fade in active graybox col-lg-9 col-lg-offset-1" id="product">
				<div class="form-group text-right Error-red">
					<spring:message code="label.mandatory" text="*Mandatory" />
				</div>
				<div class="form-inline">
					<form:form action="#" method="POST" modelAttribute="scheduler" id="scheduler" name="scheduler">
						<!-- <div class="col-lg-6 form-col-2"> -->
					<form:input path="jobName" name="jobName" type="hidden"/>
							<div class="col-lg-12">
								
								<div class="col-lg-3">
									<label for="jobId"><spring:message
											code="jobScheduler.job" text="Job" /><font	style="color: red">*</font></label>
								</div>
								<div class="col-lg-9">
									<form:select  path="jobId" class="form-group textbox-large trim"
										name="jobId" id="jobId" onblur="return validateJob(this.id)" onchange="getJobDetails()">	<form:option value="-1">---Select---</form:option>
										<form:options items="${jobDtlMap}"/>
										</form:select >
									<form:errors path="jobId" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12" >
								<div class="col-lg-3">
									<label for="totalDays"><spring:message
											code="jobScheduler.daysOfWeek" text="Days Of Week" /></label>
								</div>

								<div class="col-lg-9">
									<div class="col-lg-3">	<form:checkbox id="totalDays1" name="totalDays"  value="SUN" path = "totalDays" class="daysCls"/><label for="totalDays1" class="radiobox-line" style="padding-top: 4px;">Sunday</label></div>
									<div class="col-lg-3">	<form:checkbox id="totalDays2" name="totalDays"  value="MON" path = "totalDays" class="daysCls"/><label for="totalDays2" class="radiobox-line" style="padding-top: 4px;">Monday</label></div>
									<div class="col-lg-3">	<form:checkbox id="totalDays3" name="totalDays" value="TUE" path = "totalDays" class="daysCls"/><label for="totalDays3" class="radiobox-line" style="padding-top: 4px;">Tuesday</label></div>
									<div class="col-lg-3">	<form:checkbox id="totalDays4" name="totalDays" value="WED" path = "totalDays" class="daysCls"/><label for="totalDays4" class="radiobox-line" style="padding-top: 4px;">Wednesday</label></div>
									<div class="col-lg-3">	<form:checkbox id="totalDays5" name="totalDays" value="THU" path = "totalDays" class="daysCls"/><label for="totalDays5" class="radiobox-line" style="padding-top: 4px;">Thursday</label></div>
									<div class="col-lg-3">	<form:checkbox id="totalDays6" name="totalDays" value="FRI" path = "totalDays" class="daysCls"/><label for="totalDays6" class="radiobox-line" style="padding-top: 4px;">Friday</label></div>
									<div class="col-lg-3">	<form:checkbox id="totalDays7" name="totalDays" value="SAT" path = "totalDays" class="daysCls"/><label for="totalDays7" class="radiobox-line" style="padding-top: 4px;">Saturday</label></div>
									<div class="col-lg-3">	<form:checkbox id="totalDays8" name="totalDays"  value="ALL" path = "totalDays"  onchange="checkAll()"/><label for="totalDays8" class="radiobox-line" style="padding-top: 4px;">All</label></div>
								
									<form:errors path="totalDays" cssClass="fieldError"></form:errors>	<div id="DaysErr" class="col-lg-9" style="color: red;"></div>
								</div>
								
							</div>
						<div class="col-lg-12">
								<div class="col-lg-3">
									<label for="totalDays"><spring:message
											code="jobScheduler.daysOfMonth" text="Day Of Month" /></label>
								</div>

								<div class="col-lg-9">
									<div class="col-lg-3" style="width: 25%;">	<form:checkbox id="dayOfMonth1" name="dayOfMonth"  value="1" path = "dayOfMonth"/><label for="dayOfMonth1" class="radiobox-line" style="padding-top: 4px;">Every First Day Of Month</label></div>
									<div class="col-lg-4">	<form:checkbox id="dayOfMonth2" name="dayOfMonth"  value="2" path = "dayOfMonth"/><label for="dayOfMonth2" class="radiobox-line" style="padding-top: 4px;">Every Last Day Of Month</label></div>
									<form:errors path="dayOfMonth" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
						<div class="col-lg-12">
								
								<div class="col-lg-3">
									<label for="jobId"><spring:message
											code="jobScheduler.startTime" text="Start Time" /><font
										style="color: red">*</font></label>
								</div>
								<div class="col-lg-9"> &nbsp;HH <form:select path="startTimeHours" name="startTimeHours" id="startTimeHours">
									<form:option value="00">00</form:option>
									<form:option value="01">01</form:option>
									<form:option value="02">02</form:option>
									<form:option value="03">03</form:option>
									<form:option value="04">04</form:option>
									<form:option value="05">05</form:option>
									<form:option value="06">06</form:option>
									<form:option value="07">07</form:option>
									<form:option value="08">08</form:option><form:option value="09">09</form:option><form:option value="10">10</form:option><form:option value="11">11</form:option><form:option value="12">12</form:option><form:option value="13">13</form:option><form:option value="14">14</form:option><form:option value="15">15</form:option><form:option value="16">16</form:option><form:option value="17">17</form:option><form:option value="18">18</form:option><form:option value="19">19</form:option><form:option value="20">20</form:option><form:option value="21">21</form:option><form:option value="22">22</form:option> <form:option value="23">23</form:option></form:select>
									 &nbsp;&nbsp; / &nbsp;&nbsp; MM <form:select name="startTimeMins" id="startTimeMins" path="startTimeMins">
									<form:option value="00">00</form:option><form:option value="01">01</form:option><form:option value="02">02</form:option><form:option value="03">03</form:option><form:option value="04">04</form:option><form:option value="05">05</form:option><form:option value="06">06</form:option><form:option value="07">07</form:option><form:option value="08">08</form:option><form:option value="09">09</form:option><form:option value="10">10</form:option><form:option value="11">11</form:option><form:option value="12">12</form:option><form:option value="13">13</form:option><form:option value="14">14</form:option><form:option value="15">15</form:option><form:option value="16">16</form:option><form:option value="17">17</form:option><form:option value="18">18</form:option><form:option value="19">19</form:option><form:option value="20">20</form:option><form:option value="21">21</form:option><form:option value="22">22</form:option><form:option value="23">23</form:option><form:option value="24">24</form:option><form:option value="25">25</form:option><form:option value="26">26</form:option><form:option value="27">27</form:option><form:option value="28">28</form:option><form:option value="29">29</form:option><form:option value="30">30</form:option><form:option value="31">31</form:option><form:option value="32">32</form:option><form:option value="33">33</form:option><form:option value="34">34</form:option><form:option value="35">35</form:option><form:option value="36">36</form:option><form:option value="37">37</form:option><form:option value="38">38</form:option><form:option value="39">39</form:option><form:option value="40">40</form:option><form:option value="41">41</form:option><form:option value="42">42</form:option><form:option value="43">43</form:option><form:option value="44">44</form:option><form:option value="45">45</form:option><form:option value="46">46</form:option><form:option value="47">47</form:option><form:option value="48">48</form:option><form:option value="49">49</form:option><form:option value="50">50</form:option><form:option value="51">51</form:option><form:option value="52">52</form:option><form:option value="53">53</form:option><form:option value="54">54</form:option><form:option value="55">55</form:option><form:option value="56">56</form:option><form:option value="57">57</form:option><form:option value="58">58</form:option><form:option value="59">59</form:option></form:select>
									<form:errors path="startTimeHours" cssClass="fieldError"></form:errors>
									<form:errors path="startTimeMins" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12" >
								<div class="col-lg-3">
									<label for="multipleRunFlag"><spring:message
											code="jobScheduler.multipleRunFlag" text="Mutliple Run in a Day" /></label>
								</div>
								<div class="col-lg-9">
									<div class="col-lg-3">	<form:checkbox id="multipleRunFlag" name="multipleRunFlag"  value="1" path = "multipleRunFlag" onclick="showAndHideInterval()"/></div>
									<form:errors path="multipleRunFlag" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12" id="multipleRunIntervalDiv">
								<div class="col-lg-3">
									<label for="multipleRunInterval"><spring:message
											code="jobScheduler.interval" text="Interval" /></label>
								</div>
								<div class="col-lg-9">
									<div class="col-lg-4">	<form:input id="multipleRunInterval" name="multipleRunInterval"  maxlength="4" path = "multipleRunInterval" type="text" onkeypress="return isNumericfn(event)" onblur="validateMultipleRunInterval(this.id)"/></div>
									<form:errors path="multipleRunInterval" cssClass="fieldError"></form:errors><div class="col-lg-1" style="text-align:center;padding-top:5px">IN</div>
									<div class="col-lg-4"><form:select id="multipleRunTimeUnit" name="multipleRunTimeUnit"  path ="multipleRunTimeUnit" onblur="validateMultipleRunTimeUnit(this.id)">
									<form:option value="-1">---Select---</form:option>
									<form:option value="HH"><spring:message
											code="jobScheduler.hours" text="Hours" /></form:option>
									<form:option value="MM"><spring:message
											code="jobScheduler.mins" text="Minutes" /></form:option>
									</form:select></div>
									<form:errors path="multipleRunTimeUnit" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							
							
								<div class="col-lg-12">
								<div class="col-lg-3">
									<label for="retryCount"><spring:message
											code="jobScheduler.retryCnt" text="Retry Count" /></label>
								</div>
								<div class="col-lg-9">
									<div class="col-lg-4">	<form:input id="retryCount" name="retryCount"  path = "retryCount" type="text" onkeypress="return isNumericfn(event)" onblur="showAndHideRetryInterval()" maxlength="2"/></div>
									<form:errors path="retryCount" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12" id="retryIntervalDiv">
								<div class="col-lg-3">
									<label for="retryInterval"><spring:message
											code="jobScheduler.interval" text="Interval" /></label>
								</div>
								<div class="col-lg-9">
									<div class="col-lg-3" style="width:150px">	<form:input id="retryInterval" name="retryInterval"  path = "retryInterval" type="text" onkeypress="return isNumericfn(event)" maxlength="4" onblur="isValidRetryCount()"/></div>
									<form:errors path="retryInterval" cssClass="fieldError"></form:errors><div class="col-lg-1" style="text-align:center;padding-top:5px">IN</div>
									<div class="col-lg-4"><form:select id="retryIntervalTimeUnit" name="retryIntervalTimeUnit"  path ="retryIntervalTimeUnit" onblur="isValidRetryCount()">
									<form:option value="-1">---Select---</form:option>
									<form:option value="HH"><spring:message
											code="jobScheduler.hours" text="Hours" /></form:option>
									<form:option value="MM"><spring:message
											code="jobScheduler.mins" text="Minutes" /></form:option>
									</form:select></div>
									<form:errors path="retryIntervalTimeUnit" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							<div class="col-lg-12" >
								<div class="col-lg-3">
									<label for="totalDays"><spring:message
											code="jobScheduler.enableLabel" text="Enable/Disable" /><font style="color: red">*</font></label>
								</div>

								<div class="col-lg-9">
									<div class="col-lg-3">	<form:radiobutton id="scheduleFlag1" name="scheduleFlag"  value="E" path = "scheduleFlag"  checked="true" /><label for="scheduleFlag1" class="radiobox-line" style="padding-top: 4px;"><spring:message code="jobScheduler.enable" text="Enable"/></label></div>
									<div class="col-lg-4">	<form:radiobutton id="scheduleFlag2" name="scheduleFlag"  value="D" path = "scheduleFlag"/><label for="scheduleFlag2" class="radiobox-line" style="padding-top: 4px;"><spring:message code="jobScheduler.disable" text="Disable" /></label></div>
									<form:errors path="dayOfMonth" cssClass="fieldError"></form:errors>
								</div>
								
							</div>
							
							<div class="col-lg-12" >
								<div class="col-lg-3">
									<label for=""><spring:message
											code="jobScheduler.success" text="Notify Success To" /></label>
								</div>
								<div class="col-lg-9">
									<div class="col-lg-4">	<form:select id="successEMail" name="successEMail" path = "successMailUser" multiple="true" onclick="addSelectedSuccessMail()" style="width:250px">
									<c:forEach items="${userMailDetail}" var="userMail" >
									<%-- <form:option data-mail="${userMail.USER_EMAIL}" value="${userMail.USER_ID}">${userMail.USER_NAME}</form:option> --%>
									 <form:option data-mail="${userMail[2]}" value="${userMail[0]}">${userMail[1]}</form:option> 
									</c:forEach>
									</form:select></div>
									<div class="col-lg-4">	<select id="successEMailSelected" name="successEMailSelected" disabled="disabled" style="width:300px"  multiple="multiple"></select></div>
									
								</div>
								
							</div>
							<div class="col-lg-12" >
								<div class="col-lg-3">
									<label for=""><spring:message
											code="jobScheduler.failure" text="Notify Failure To" /></label>
								</div>

								<div class="col-lg-9">
									<div class="col-lg-4">	<form:select id="failEMail" name="failEMail" path = "failMailUser" multiple="true" onclick="addSelectedFailMail()" style="width:250px">
										<c:forEach items="${userMailDetail}" var="userMail" >
									 <form:option data-mail="${userMail[2]}" value="${userMail[0]}">${userMail[1]}</form:option> 
									</c:forEach>
									</form:select></div>
									<div class="col-lg-4">	<select id="failEMailSelected" name="failEMailSelected" disabled="disabled" style="width:300px"  multiple="multiple"></select></div>
									
								</div>
								
							</div>
							<div class="col-lg-12 text-center">
								<!-- <div class="col-lg-5 col-lg-offset-2"> -->

								
									<button type="button" class="btn btn-primary" onclick="FormSubmit(this.form.id,event)" >
										<i class='glyphicon glyphicon-saved'></i>
										<spring:message code="button.save" text="Save" />
									</button>
								
									<a
										href="${pageContext.request.contextPath}/"
										class="btn gray-btn btn-primary"><i
										class='glyphicon glyphicon-backward'></i>
									<spring:message code="button.back" text="Back" /> </a>
								<!-- </div> -->

							</div>
							
							
							
							
					</form:form>
				</div>
			</div>
		</div>
	</article>
</div>
<script>
showAndHideInterval();
showAndHideRetryInterval();
addSelectedFailMail();
addSelectedSuccessMail();

$('input[name="dayOfMonth"]').on('change', function() {
	   $('input[name="dayOfMonth"]').not(this).prop('checked', false);
});



$('input[name="totalDays"]').on('change', function(event) {
	
			var allChecked = true;
			$(".daysCls").each(function() {
				if (!$(this).prop("checked")) {
					allChecked = false;
				
					$("#totalDays8").prop('checked', false);
					
				}
			});
			if (allChecked) {
				
				$("#totalDays8").prop('checked', true);
				
			}else{$("#totalDays8").prop('checked', false);}
});

$('input[name="totalDays"],input[name="dayOfMonth"]').on('change', function() {
	
	validateDays();
});

</script>