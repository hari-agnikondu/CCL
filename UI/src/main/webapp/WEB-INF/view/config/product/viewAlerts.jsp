<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<head>
<script src="<c:url value="/resources/js/clpvms/product.js" />"></script>
<script src="<c:url value="/resources/js/clpvms/alerts.js" />"></script>

</head>

<style>

.col-lg-2.movup {
    /*margin: -8px 0px 0px 0px;*/
}

</style>
<body>


	
<form:form name="productAlerts" id="productAlerts"  action="#"
		method="POST" class='form-horizontal' commandName="productAlerts">

<div class="body-container">
	<div class="container">
	
	
		
	
		<section class="content-container">
			<article class="col-lg-12">
				<ul class="nav nav-tabs col-lg-8 col-lg-offset-1">
					<li class="active SubMenu"><a href="#showProductAlerts"
						data-toggle="tab"><spring:message code="product.alerts.tab" /></a></li>
</ul>
<div class="tabresp tab-content">
	<div class="tab-pane fade in active graybox col-lg-8 col-lg-offset-1" >
	 <div class="text-right mandatory-red"><spring:message code="label.mandatory" text="*Mandatory" /></div>
		<div class="">
			<c:if test="${statusMessage!='' && statusMessage!=null}">	
			<div class="aln error-red">
			<center><b>${statusMessage}</b></center>
			</div>	
			</c:if>
			<c:if test="${status!='' && status!=null}">
			<div class="aln success-green">
			<center><b>${status}</b></center>
			</div>	
			</c:if>
			<div class="col-lg-12 ">
											
				<div class="col-lg-12">
				<div class="col-lg-6">
						<label for="From Email ID for Alerts"><spring:message
								code="product.alerts.fromEmailId" /><font color='red'>*</font></label>
					</div>											
						 <div class="col-lg-6">
						<form:input path="alertAttributes['alertFromEmail']" id="alertFromEmail" name="alertFromEmail" 
					 autocomplete="off" maxlength="50" class="textbox-large" onblur ="return validateEmailAlertMandatory(this.form.id,this.id)" readonly="true"/>	
							<div><form:errors path="alertAttributes['alertFromEmail']" cssClass="fieldError"  /></div>				
							
					</div>		 									
				</div>
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						
						<label for="Application Name"><spring:message
								code="product.alerts.appName" /><font color='red'>*</font></label> 
					</div>
					<div class="col-lg-6">
					<form:input path="alertAttributes['alertAppName']" id="alertAppName" name="alertAppName" 
					 autocomplete="off" maxlength="50"  
					 onkeyup="isAlphaNumericWithSpaceproductNameId(this)" 
					 onblur ="validateAlerCheckWithoutPattern(this.form.id,this.id)" class="textbox-large" readonly="true"/>
					<div>
						<form:errors path="alertAttributes['alertAppName']" cssClass="fieldError"  /></div>
					</div>
							
				</div>
				
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						
						<label for="Application Notification Type"><spring:message
								code="product.alerts.appNotificationType" /><font color='red'>*</font></label> 
					</div>
					<div class="col-lg-6">
					<form:input path="alertAttributes['alertAppNotifyType']" id="alertAppNotifyType" name="alertAppNotifyType" 
					 autocomplete="off" maxlength="6" class="textbox-large" onblur ="validateAlerCheckWithoutPattern(this.form.id,this.id)"
					    onkeyup="isAlphaNumericWithSpaceproductNameId(this)"  readonly="true" />
					<div>
						<form:errors path="alertAttributes['alertAppNotifyType']" cssClass="fieldError"  /></div>
					</div>
							
				</div>
				
				<div class="col-lg-12">
				<div class="col-lg-6">
						
						<label for="SMS Short Code"><spring:message
								code="product.alerts.smsShortCode" /><font color='red'>*</font></label> 
					</div>
					<div class="col-lg-6">
					<form:input path="alertAttributes['alertSMSCode']" id="alertSMSCode" name="alertSMSCode" 
					 autocomplete="off" maxlength="6" class="textbox-large" 
					 onkeyup="isAlphaNumericWithOutSpaceproductNameId(this)" 
					 onblur ="validateAlertswithMinMaxLengthCheck(this.form.id,this.id)" readonly="true"/>
					<div>
						<form:errors path="alertAttributes['alertSMSCode']" cssClass="fieldError"  /></div>
					</div>
							
				</div>
				
				
	<div class="col-lg-12">
	<div class="col-lg-6">
			
			<label for="Minimum Balance For Daily Alert"><spring:message
					code="product.alerts.minBalanceDailyAlert" /></label> 
		</div>
		<div class="col-lg-6">
		<form:input path="alertAttributes['alertMinBalance']" id="alertMinBalance" name="alertMinBalance" 
		 autocomplete="off" maxlength="6" class="textbox-large"
		 onkeyup="isNumericproductNameId(this)" 
		  onblur="return validateAlertwithNumericandMaxlength(this.form.id,this.id)" readonly="true"/>
		<div>
			<form:errors path="alertAttributes['alertMinBalance']" cssClass="fieldError"  /></div>
		</div>
				
	</div>
	
				
	<div class="col-lg-12">
	<div class="col-lg-6">
			
			<label for="Card Status For Daily Alert"><spring:message
					code="product.alerts.cardStatusDailyAlert" /></label> 
		</div>
			<input type="hidden" id="cardSelDet" name="cardSelDet" value="${selCardStatus}" />
		<div class="col-lg-6">
		<form:select path="alertAttributes['alertCardStatus']" id="alertCardStatus" name="alertCardStatus" 
		 autocomplete="off" maxlength="6" class="textbox-large" multiple="multiple" disabled="true">
		
		<form:options items="${cardStatus}"/>
		
		</form:select>
		 
		 
		<div>
			<form:errors path="alertAttributes['alertCardStatus']" cssClass="fieldError"  /></div>
		</div>
				
	</div>
	
	<div class="col-lg-12">
	<div class="col-lg-6">
			
			<label for="Inactivity Period to Restrict Daily Alert"><spring:message
					code="product.alerts.inactivePeriodAlert" /></label> 
		</div>
		<div class="col-lg-6">
		<form:input path="alertAttributes['alertInactivityPeriod']" id="alertInactivityPeriod" name="alertInactivityPeriod" 
		 autocomplete="off" maxlength="6" class="textbox-large" 
		  onkeyup="isNumericproductNameId(this)" 
		 onblur="return validateAlertwithNumericandMaxlength(this.form.id,this.id)" readonly="true"/>
		<div>
			<form:errors path="alertAttributes['alertInactivityPeriod']" cssClass="fieldError"  /></div>
		</div>
				
	</div>		


<div class="col-lg-12">
		<div class="col-lg-6">
		<label for="Select Language"><spring:message
				code="product.alerts.setLanguage" /></label>
	</div>											
		 <div class="col-lg-6">
			<form:select path="alertAttributes['alertLanguage']" id="alertLanguage" class="dropdown-medium" disabled="true">
			 <form:option value="1" label="English"/>
			 <form:option value="2" label="Spanish"/>	
			 <form:option value="3"  label="French"/>											 
			</form:select>			
			<div><form:errors path="alertAttributes['alertLanguage']" cssClass="fieldError"  /></div>				
			
	</div>		 									
</div>				

	<div class="col-lg-12">
	<div class="col-lg-6">
			
			<label for="Opt-In Check"><spring:message
					code="product.alerts.optInCheck" /></label> 
		</div>
		<div class="col-lg-6">
		<form:checkbox path="alertAttributes['alertOptIn']"
				id="alertOptIn" name="alertOptIn" value="Yes" disabled="true"/>
		<div>
			<form:errors path="alertAttributes['alertOptIn']" cssClass="fieldError"  /></div>
		</div>
				
	</div>	
	


				
					
<div class="col-lg-12">
		<div class="col-lg-4 space">
			<label for="Welcome Message for Registering"></label> 
		</div>
<div class="col-lg-3">
SMS		
</div>				
<div class="col-lg-3">
Email
</div>	
<div class="col-lg-2">			
</div>
</div>						
		
	

 <c:forEach items="${messagesMap}" var="msg">



<!-- MESSAGE box starts -->

<div class="modal fade" id="define-constant-update_${msg.alertShortName}" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
						<div class="modal-content">
						<div class="modal-body col-lg-12">
		<div class="col-lg-12"> 
		 <span >
		Configure Alerts for : '<b id="messageNameDisp_${msg.alertShortName}"></b>'  
		</span>
			</div> 
						
		<!-- message template -->
		<div class="modal-body col-lg-12">
		<div class="col-lg-4 space">
		
		<label for="Message Template"><spring:message
				code="product.alerts.msgTemplate" /><font color='red'></font></label> 
	</div>
	<div class="col-lg-8">
	<form:input path="alertAttributes['alertSMS_${msg.alertShortName}']" id="alertSMS_${msg.alertShortName}"   class="alertSMS" type="textarea"
	  maxlength="1000" readonly="true"/>
	  
	<div>
		<form:errors path="" cssClass="fieldError"  /></div>
	</div>
	</div>
	<!-- message template -->
		<!-- Email subject -->
		<div class="modal-body col-lg-12">
		<div class="col-lg-4 space">
		
		<label for="Email Subject"><spring:message
				code="product.alerts.emailSubject" /><font color='red'></font></label> 
	</div>
	<div class="col-lg-8">
	<form:input path="alertAttributes['alertEmailSub_${msg.alertShortName}']" id="alertEmailSub_${msg.alertShortName}"  class="alertEmailSub" 
	type="textarea" maxlength="200" readonly="true"/>
	<div>
		<form:errors path="" cssStyle="color:red" cssClass="fieldError"  /></div>
	</div>
	</div>
	<!-- email subject-->
		
	<!-- email body -->
		<div class="modal-body col-lg-12">
		<div class="col-lg-4 space">
		
		<label for="Email Body"><spring:message
				code="product.alerts.emailBody" /><font color='red'></font></label> 
	</div>
	<div class="col-lg-8">
	
	<form:input path="alertAttributes['alertEmailBody_${msg.alertShortName}']" id="alertEmailBody_${msg.alertShortName}" class="textbox-large" 
	type="textarea"  style="min-height: 168px;" maxlength="1000" readonly="true"/>
	<div>
		<form:errors path="" cssClass="fieldError"  /></div>
	</div>
	</div>
	<!-- email body-->
	
				
						</div>
						<form:hidden path="" id="messageIdtoUpdate" /> 
						 <input type="hidden" name="issuerName" id="messageIdtoUpdate" /> 
						<div class="modal-footer">
							<%-- <button type="button" onclick="goUpdateAlert(this.form.id,event);"
								class="btn btn-primary"><i class="glyphicon glyphicon-saved"></i><spring:message
									code="button.update" /></button> --%>
							<button data-dismiss="modal" onclick="goToPrevious()" class="btn btn-primary gray-btn"><spring:message
									code="button.close" /></button>

						</div>

					</div>

=				<%-- </form:form> --%> 
			</div>
		</div>

<!-- MESSAGE box ends -->

<!-- Welcome Message for Registering -->

<div class="col-lg-12">
		<div class="col-lg-4 space">
			<label for="Welcome Message for Registering">${msg.alertName}</label> 
		</div>
<div class="col-lg-3">
		<form:checkbox path="alertAttributes['alertMode_SMS_${msg.alertShortName}']"
				id="alertMode_SMS_${msg.alertShortName}" name="alertMode_SMS_${msg.alertShortName}" value="SMS" disabled="true"/>
</div>				
<div class="col-lg-3">
		<form:checkbox path="alertAttributes['alertMode_EMAIL_${msg.alertShortName}']"
				id="alertMode_EMAIL_${msg.alertShortName}" name="alertMode_EMAIL_${msg.alertShortName}" value="Email" disabled="true"/>
</div>	
<div class="col-lg-2 movup">			
		<button type="button"
						onclick="alertsSubmit('${msg.alertId}','${msg.alertName}','${msg.alertShortName}');"
						class="btn btn-primary-table-button" id="search_submit" data-toggle="modal"
						 data-target="#define-constant-update_${msg.alertShortName}">
						<i class="glyphicon glyphicon-envelope"></i><spring:message code="product.alerts.msg" />
		</button>
</div>
</div>	

<br>&nbsp;

</c:forEach>

<!-- check boxes ends here -->				
								
							</div>

						</div>
					</div>
				</div>
			</article>
		</section>
		</form:form>


<script>

$("#alertsTab").addClass("active");
$("#alertsTab").siblings().removeClass('active');

 var values=$("#cardSelDet").val();

 
 $.each(values.split(","), function(i,e){
     $("#alertCardStatus option[value='" + e + "']").prop("selected", true);
 });
function alertsSubmit(id,message,shortname) {
	
	var msgid=id;
	var msgtxt=message;
	var sname=shortname;
	
	
/* 	alert("id "+msgid);
	alert("msgtxt "+msgtxt);
	alert("sname "+sname); */
	
	<c:set var="alertShortName" value="" /> 
	
		alertShortName=sname;
		
		
	/* 	
$(".alertEmailBody").attr("path","alertAttributes['alertEmailBody_"+alertShortName+"']");
	$(".alertEmailSub").attr("path","alertAttributes['alertEmailSub_"+alertShortName+"']");
	$(".alertSMS").attr("path","alertAttributes['alertSMS_"+alertShortName+"']");
	
	$(".alertEmailBody").attr('id','alertEmailBody_'+alertShortName);
	$(".alertEmailSub").attr('id','alertEmailSub_'+alertShortName);
	$(".alertSMS").attr('id','alertSMS_'+alertShortName);
	
	$(".alertEmailBody").attr('name','alertEmailBody_'+alertShortName);
	$(".alertEmailSub").attr('name','alertEmailSub_'+alertShortName);
	$(".alertSMS").attr('name','alertSMS_'+alertShortName);
	 */
	document.getElementById("messageNameDisp_"+alertShortName).innerHTML =msgtxt;

	 
}





function saveProductAlerts(formid,event) {
	event.preventDefault();
	
	$("#productAlerts").attr('action','saveProductAlerts');
	$("#productAlerts").submit();

	 
}

/* 
function goUpdateAlert(formId,event)
{
	
	var msgTemplate="";
	var emailSubject="";
	var emailBody="";
	var alertid="";
	var alertname="";
	
/* 	var alertSMS_registration="";
	alertSMS_registration=$('#alertSMS_registration').val();
	
	alert(alertSMS_registration);
	
	var alertEmailSub_registration="";
	alertEmailSub_registration=$('#alertEmailSub_registration').val();
	
	alert(alertEmailSub_registration);
	
	var alertEmailBody_registration="";
	alertEmailBody_registration=$('#alertEmailBody_registration').val();
	
	alert(alertEmailBody_registration); */
	
	
	//alertSMS_declineVerificationMsg
	
/* 	var alertSMS_declineVerificationMsg="";
	alertSMS_declineVerificationMsg=$('#alertSMS_declineVerificationMsg').val();
	
	alert(alertSMS_declineVerificationMsg);
	
	var alertEmailSub_declineVerificationMsg="";
	alertEmailSub_declineVerificationMsg=$('#alertEmailSub_declineVerificationMsg').val();
	
	alert(alertEmailSub_declineVerificationMsg);
	
	var alertEmailBody_declineVerificationMsg="";
	alertEmailBody_declineVerificationMsg=$('#alertEmailBody_declineVerificationMsg').val();
	
	alert(alertEmailBody_declineVerificationMsg);
	
	
	
	
	
	
	
	
	
	
}  */

</script>


</body>
</html>

