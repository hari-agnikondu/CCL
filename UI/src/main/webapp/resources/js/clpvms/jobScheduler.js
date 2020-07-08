function FormSubmit(formId,event){
	
	 $("#feedBackTd").html('');
	 var isValidJob=validateJob("jobId");
	 var isValidMultInt=validateMultipleRunInterval("multipleRunInterval");
	 var isValidMultTimeUnit=validateMultipleRunTimeUnit("multipleRunTimeUnit");
	 var isValidRetry=isValidRetryCount();
	 
	 var isValidDay=validateDays();
	/* var isValidSuccessMail = validateMail("successEMail");
	 var isValidFailMail = validateMail("failEMail");*/
	 if ( isValidJob && isValidDay && isValidMultInt && isValidMultTimeUnit && isValidRetry ){
			$("#"+formId).attr('action','saveConfigJobScheduler');
			$("#"+formId).submit();
	 }
	 	
 }

function validateJob(ctrlId){
	
	if($("#"+ctrlId+" option:selected").val()==-1){
		generateAlert($("#"+ctrlId).closest('form').attr('id'), ctrlId,"scheduler.job.select");
		return false;
	}
	else{
		clearError(ctrlId);
		return true;
	}
}

function validateMultipleRunInterval(ctrlId){
	
	if($("#multipleRunFlag").is(":checked")){
		if($("#"+ctrlId).val()==null || $("#"+ctrlId).val()==""){
			generateAlert($("#"+ctrlId).closest('form').attr('id'), ctrlId,"scheduler.interval.empty");
			return false;
		}
		else if(isNaN($("#"+ctrlId).val())){
			generateAlert($("#"+ctrlId).closest('form').attr('id'), ctrlId,"scheduler.interval.invalid");
			return false;
		}
		else{
			clearError(ctrlId);
			return true;
		}
	}
	else{
		clearError(ctrlId);
		return true;
	}
}


function validateMultipleRunTimeUnit(ctrlId){
	if($("#multipleRunFlag").is(":checked")){
		if($("#"+ctrlId+" option:selected").val()==-1){
			generateAlert($("#"+ctrlId).closest('form').attr('id'), ctrlId,"scheduler.timeUnit.select");
			return false;
		}
		else{
			clearError(ctrlId);
			return true;
		}
	}
	else{
		clearError(ctrlId);
		return true;
	}
}


function isValidRetryCount(){
	var isValid=true;
	if($("#retryCount").val()!=null && $("#retryCount").val()!="" ){
		if(isNaN($("#retryCount").val())){
			generateAlert($("#retryCount").closest('form').attr('id'), "retryCount","scheduler.retryCnt.invalid");
			isValid= false;
		}
		else if(parseInt($("#retryCount").val())<=0){
			generateAlert($("#retryCount").closest('form').attr('id'), "retryCount","scheduler.retryCnt.zero");
			isValid= false;
		}
		else{
			clearError("retryCount");
			if($("#retryInterval").val()==null || $("#retryInterval").val()==""){
				generateAlert($("#retryInterval").closest('form').attr('id'),"retryInterval" ,"scheduler.interval.empty");
				isValid= false;
			}
			else if(isNaN($("#retryInterval").val())){
				generateAlert($("#retryInterval").closest('form').attr('id'),"retryInterval" ,"scheduler.interval.invalid");
				isValid= false;
			}
			else if($("#retryIntervalTimeUnit option:selected").val()=='HH' && (parseInt($("#retryInterval").val()) * parseInt($("#retryCount").val())) >=24){
				generateAlert($("#retryIntervalTimeUnit").closest('form').attr('id'),"retryInterval" ,"scheduler.interval.exceedsMaxHours");
				isValid= false;
			}
			else if($("#retryIntervalTimeUnit option:selected").val()=='MM' && (parseInt($("#retryInterval").val()) * parseInt($("#retryCount").val())) >=1440){
				generateAlert($("#retryIntervalTimeUnit").closest('form').attr('id'),"retryInterval" ,"scheduler.interval.exceedsMaxMins");
				isValid= false;
			}
			else{
				clearError("retryInterval");
				
			}
			
			if($("#retryIntervalTimeUnit option:selected").val()==-1){
				generateAlert($("#retryIntervalTimeUnit").closest('form').attr('id'), "retryIntervalTimeUnit","scheduler.timeUnit.select");
				isValid= false;
			}
			else{
				clearError("retryIntervalTimeUnit");
				
			}
		}
	}
	
	return isValid;
}
function showAndHideInterval(){
	if($("#multipleRunFlag").is(':checked')){
		$("#multipleRunIntervalDiv").show();
	}
	else{
		$("#multipleRunIntervalDiv").hide();
		$("#multipleRunTimeUnit").val(-1);
		$("#multipleRunInterval").val('');
		clearError("multipleRunTimeUnit");
		clearError("multipleRunInterval");
	}
}
function showAndHideRetryInterval(){
	if($("#retryCount").val()==null || $("#retryCount").val().trim()==""){
		$("#retryIntervalDiv").hide();
		$("#retryInterval").val('');
		$("#retryIntervalTimeUnit").val("-1");
		clearError("retryCount");
		clearError("retryInterval");
		clearError("retryIntervalTimeUnit");
	}else{
		if(isNaN($("#retryCount").val())){
			generateAlert($("#retryCount").closest('form').attr('id'), "retryCount","scheduler.retryCnt.invalid");
			$("#retryIntervalDiv").hide();
			$("#retryInterval").val('');
			$("#retryIntervalTimeUnit").val("-1");
		}
		else if($("#retryCount").val()<=0){
			generateAlert($("#retryCount").closest('form').attr('id'), "retryCount","scheduler.retryCnt.zero");
			
			$("#retryIntervalDiv").hide();
			$("#retryInterval").val('');
			$("#retryIntervalTimeUnit").val("-1");
		}
		else{
			clearError("retryCount");
			$("#retryIntervalDiv").show();
		}
	}
}

function validateDays(){
	var isValidDay=true;
	 if($("input[name=totalDays]:checked").length<1 && $("input[name=dayOfMonth]:checked").length<1){
		 $("#DaysErr").html(readMessage("scheduler.weekDay"));
		 isValidDay= false;
	 }
	 else if($("input[name=totalDays]:checked").length>0 && $("input[name=dayOfMonth]:checked").length>0){
		 $("#DaysErr").html(readMessage("scheduler.weekDay.notAllowed"));
		 isValidDay= false;
	 }
	 else{
		 $("#DaysErr").html('');
			
	}
	
	 return isValidDay;
}
function addSelectedSuccessMail(){
	 $('#successEMailSelected').html('');
	$.each($("#successEMail option:selected"), function(key, value) {
	     $('#successEMailSelected')
	         .append($("<option></option>")
	         .attr("value",$(value).val())
	         .text($(value).attr('data-mail')));
	});
	
	$("#successEMailSelected option:selected").removeAttr("selected");
}
function addSelectedFailMail(){
	 $('#failEMailSelected').html('');
	$.each($("#failEMail option:selected"), function(key, value) {
	     $('#failEMailSelected')
	         .append($("<option></option>")
	         .attr("value",$(value).val())
	         .text($(value).attr('data-mail')));
	});
	
	$("#failEMailSelected option:selected").removeAttr("selected");
}

function checkAll(){
	
	if($("#totalDays8").is(':checked')){
		$("input[name='totalDays']").prop('checked',true);
		
	}
	else{
		$("input[name='totalDays']").prop('checked',false);
	}
}

function getJobDetails(){
	if(validateJob("jobId")){
		$("#scheduler").attr('action','jobSchedulerConfig');
		$("#scheduler").submit();
	}
}

function validateMail(objId){
	var isSelected=true;
	if($("#"+objId+" option:selected").legth<1){
		generateAlert($("#"+objId).closest('form').attr('id'), objId,"scheduler.user.none");
		isSelected=false;
	}
	else{
		clearError(objId);
	}
	return isSelected;
}