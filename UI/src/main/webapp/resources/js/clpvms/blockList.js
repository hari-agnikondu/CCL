
function clickDelete(){
	$("#feedBackTd").html('');
	if($("input[name=checkDelete]:checked").length < 1){
		 $("#tableErr").html(readMessage("blocklist.selectFor.delete"));
	}
	else{
		$("#search_channelCode").val($("#channelCode option:selected").val());
		$("#tableErr").html('');

		$.each($("input[name=checkDelete]:checked"), function() {
		
			$(this).closest('tr').css("background-color","#85b3fc");
		});
	
		$('#define-constant-delete').modal('show');
	
	}
}

function goToPrevious() {
	$('#define-constant-delete').modal('hide');
	$.each($("input[name=checkDelete]:checked"), function() {
	/*	var rowId=	$(this).closest('tr').attr('id');
	
		$("#"+rowId).css("background-color","#f9f9f9");*/
		$(this).closest('tr').css("background-color","#f9f9f9");
	});
}

function setInsValue() {
	if($("#channelCode option:selected").val()!=-1){
		$("#instrumentType").val($("#" + $("#channelCode").val()).val());
		$("#instrumentTypeLbl").html($("#" + $("#channelCode").val()).val());
		$("#instrumentTypeDiv").show();
		clearError("channelCode");
		clearError("instrumentId");
		var instrumentType=$("#instrumentType").val();
		if (instrumentType == 'IP_ADDRESS') {
		 	$("#instrumentId").attr('maxlength','15');
		}else if (instrumentType == 'MOBILE_NO') {
			$("#instrumentId").attr('maxlength','10');
		}else if (instrumentType == 'DEVICE_ID'){
			$("#instrumentId").attr('maxlength','14');
		}
	}
	else{
		$("#instrumentType").val('');
		$("#instrumentTypeLbl").html('');
		$("#instrumentTypeDiv").hide();
		generateAlert("addForm", "channelCode", "select.channelCode");
	}
	$("#instrumentId").val('');
}

function validateInstrumentId(formId, eleId) {
	var instrumentType = $("#instrumentType").val();
	var instrumentId = $("#instrumentId").val();
	clearError(formId + " #" + eleId);
	if(instrumentId!=null && instrumentId.trim()!=''){
	if (instrumentType == 'IP_ADDRESS') {

		if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/
				.test(instrumentId)) {

			clearError(formId + " #" + eleId);
			return true;
		} else {

			generateAlert(formId, eleId, "blocklist.ipAddress.pattern");
			return false;
		}
	}else if(instrumentType == 'MOBILE_NO'){
		if ( /^[0-9]+$/
				.test(instrumentId)) {
			if(instrumentId.length != 10 || parseInt(instrumentId)==0){
				
				generateAlert(formId, eleId,  "blocklist.mobile.pattern");
				return false
			}
			
			clearError(formId + " #" + eleId);
			return true;
		} 
		else{
			generateAlert(formId, eleId, "blocklist.mobile.pattern");
			
			return false;
		}
	}else if(instrumentType == 'DEVICE_ID'){
		if ( /^[a-zA-Z0-9]+$/
				.test(instrumentId)) {
			if(instrumentId.length != 14 ){
			
				generateAlert(formId, eleId, "blocklist.device.pattern");
				return false
			}
			
			clearError(formId + " #" + eleId);
			return true;
		} 
		else{
			
			generateAlert(formId, eleId, "blocklist.device.pattern");
			return false;
		}
	}
	}
	else{
		generateAlert(formId, eleId, "blocklist.instrumentID.empty");
	}
	return false;
}

function clickAdd(){
	$("#feedBackTd").html('');
	$("#searchForm").attr('action','showAddBlockList');
	$("#searchForm").submit();
}

/*function addtoblocklist(){
	$("#feedBackTd").html('');
	var isValidCode=validateSelectBox("channelCode");
	var isvalidId=validateInstrumentId("addForm", "instrumentId");

	if(isValidCode && isvalidId){
		$("#addForm").attr('action','addBlockList');
		$("#addForm").submit();
	}
}*/
function validateSelectBox(eleID){
	
	if($("#"+eleID+" option:selected").val()==-1){
		generateAlert("addForm", eleID, "select."+eleID);
		return false;
	}else{
		clearError(eleID);
		return true;
	}
}
function clickReset(){
	$("#channelCode").val('-1');
	$("#instrumentType").val('');
	$("#instrumentId").val('');
	clearError("channelCode");
	clearError("instrumentType");
	clearError("instrumentId");
	$("#feedBackTd").html('');
	$("#instrumentTypeLbl").html('');
	$("#instrumentTypeDiv").hide();
	$("#instrumentId").attr('maxlength','15');
}

function allowNumbersWithDot(e){
	 var charCode;
   if (e.keyCode > 0) {
       charCode = e.which || e.keyCode;
   }
   else if (typeof (e.charCode) != "undefined") {
       charCode = e.which || e.keyCode;
   }
   if (charCode == 46)
       return true
   if (charCode > 31 && (charCode < 48 || charCode > 57))
       return false;
   return true;
}