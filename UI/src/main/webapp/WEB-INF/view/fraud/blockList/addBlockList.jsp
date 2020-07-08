<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<meta http-equiv="Pragma" content="no-cache">

<script
 src="${pageContext.request.contextPath}/resources/js/clpvms/blockList.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<div class="container">

	<article class="col-lg-12" id="addBlockId">
	<div id="feedBackTd" class="col-lg-12 text-center" > 
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
		</div>
	
		<ul  class="nav nav-tabs col-lg-6 col-lg-offset-3">
			<li class="active"><a href="#addForm" data-toggle="tab"
				class="hoverColor"><i class="glyphicon glyphicon-tags"></i>&nbsp;<spring:message
						code="header.blocklist.add" text="Add Blocklist" /></a></li>

		</ul>
		<div class="tabresp tab-content">

			<div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3" id="product">
				<div class="form-group text-right Error-red">
					<spring:message code="label.mandatory" text="*Mandatory" />
				</div>
				<div class="form-inline">
					<form:form action="" method="POST" modelAttribute="blockListForm"
						id="addForm" name="addBlockListForm">

						<!-- <div class="col-lg-6 form-col-2"> -->

							<div class="col-lg-12">
								<div class="col-lg-4">
									<label for="channelCode"><spring:message
											code="blockList.deliveryChannel" text="Delivery Channel" /><font color='red'>*</font></label>
								</div>

								<div class="col-lg-8">
								<c:forEach items="${deliveryChannels}" var="blockList">
											<input type=hidden  id="${blockList[0]}" value="${blockList[2]}">
								</c:forEach>
									<form:select path="channelCode" id="channelCode" onChange="setInsValue()"
										class="dropdown-medium" onblur="return validateSelectBox(this.id);">
										<form:option value="-1" label="- - - Select - - -" />
										<c:forEach items="${deliveryChannels}" var="blockList">
											<form:option value="${blockList[0]}">${blockList[1]}</form:option>
										</c:forEach>
									</form:select>
									<div>
									</div>
								</div>
							</div>

							<div class="col-lg-12" id="instrumentTypeDiv"  style="display:none;">
									<div class="col-lg-4 ">
										<label for=instrumentType><spring:message
												code="blockList.instrumentType" text="Instrument Type" /></label>
									</div>
									<div class="col-lg-8">
									<label id="instrumentTypeLbl"></label>
										<form:input path="instrumentType" readonly="true"
											class="form-group textbox-large" type="hidden" 
											name="instrumentType" id="instrumentType" maxLength="20"
											value="" />
									</div>

								</div>

								<div class="col-lg-12">
									<div class="col-lg-4 ">
										<label for=instrumentId><spring:message
												code="blockList.instrumentID" text="Instrument Id" /><font
											style="color: red">*</font></label>
									</div>
									<div class="col-lg-8"> <!-- onkeypress="return allowNumbersWithDot(event);" -->
										<form:input path="instrumentId" onpaste="return false;"
											class="form-group textbox-large" type="text"
											name="instrumentId" id="instrumentId" onblur="return validateInstrumentId(this.form.id,this.id)"
											maxLength="15"  /><!--  -->
									</div>

								</div> 


								<div class="col-lg-12 text-center" >
									<!-- <div class="col-lg-5 col-lg-offset-2"> -->
										<button type="submit" class="btn btn-primary" >
											<i class='glyphicon glyphicon-plus'></i>
											<spring:message text="Add" code="button.add" />
										</button>

										<!-- <input type="submit" class="btn btn-primary" name="add" value="Add" /> -->
										<button type="button" class="btn gray-btn btn-primary"
											value="Reset" onclick="clickReset()">
											<i class='glyphicon glyphicon-refresh'></i>
											<spring:message code="button.reset" text="Reset" />
										</button>
										<a
											href="${pageContext.request.contextPath}/fraud/blockList"
											class="btn gray-btn btn-primary"><i
											class='glyphicon glyphicon-backward'></i> <spring:message
												code="button.back" text="Back" /> </a>

								</div>
					</form:form>
				</div>
			</div>
		</div>
	</article>
</div>
<script>
 $( "#instrumentId" ).keypress(function(event) {
	if($("#instrumentType").val()=='IP_ADDRESS'){
		return allowNumbersWithDot(event);
	}else if($("#instrumentType").val()=='MOBILE_NO'){
		return isNumericfn(event);
	}
});
 
 $( "#instrumentId" ).keyup(function(event) {
	if($("#instrumentType").val()=='DEVICE_ID'){
		return isAlphaNumeric(document.getElementById('instrumentId'));
	}
}); 

 $( "#addForm" ).submit(function(event) {
	
	 $("#feedBackTd").html('');
		var isValidCode=validateSelectBox("channelCode");
		var isvalidId=validateInstrumentId("addForm", "instrumentId");

		if(isValidCode && isvalidId){
			$("#addForm").attr('action','addBlockList');
			/* $("#addForm").submit(); */
			return true;
		}
		return false;
});
 
if($("#instrumentType").val()!=null && $("#instrumentType").val().trim()!=''){
	$("#instrumentTypeDiv").show();
	var instrumentType=$("#instrumentType").val();
	if (instrumentType == 'IP_ADDRESS') {
	 	$("#instrumentId").attr('maxlength','15');
	}else if (instrumentType == 'MOBILE_NO') {
		$("#instrumentId").attr('maxlength','10');
	}else if (instrumentType == 'DEVICE_ID'){
		$("#instrumentId").attr('maxlength','14');
	}
	$("#instrumentTypeLbl").html($("#instrumentType").val());
}else {
	$("#instrumentTypeDiv").hide();
}
</script>
