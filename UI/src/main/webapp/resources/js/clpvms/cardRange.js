
function dispAddCardRange() 
		{

	 	$("#cardRangeSearch").attr('action','addCardRange')
	 	$("#cardRangeSearch").submit();
		}
	var rowidforcolor="";
    function goStatusChange(eleObj,id) 
		{
    	
    	 
    	var rowID=$(eleObj).closest('tr').attr('id');
    	var val=rowID.split("#");
    	console.log(rowID);
    	rowidforcolor=rowID;
    	console.log(val[0]+' Name:'+val[1]);
    	 document.getElementById(rowID).style.background = "#85b3fc";
			document.getElementById("statusChangeDesc").value = "";
			document.getElementById("statusChangeDescReject").value = "";
		
			document.getElementById("cardRangeIdModel").value = id;
			document.getElementById("cardRangeIdModelReject").value = id;
			document.getElementById("cardRangeIdModelDelete").value = id;
			
			document.getElementById("cardRangeIdModelApproveName").value = val[1];
			document.getElementById("cardRangeIdModelRejectName").value = val[1];
			document.getElementById("cardRangeIdModelDeleteName").value = val[1];
			document.getElementById("issuerNameDisp").innerHTML = val[1];
			clearDailogError();
			
		}
    function clearDailogError(){
    	document.getElementById("dialogErrorApprove").innerHTML='';
   	 	document.getElementById("dialogErrorReject").innerHTML='';
    }
    
    
    function goToPrevious() {
    	 document.getElementById(rowidforcolor).style.background = "#f9f9f9";
    
	}
function goApprove() 
		{
			if ($("#statusChangeDesc").val() != null
					&& $("#statusChangeDesc").val().length != 0) {
				
				$("#cardRangeStatus").attr('action','approveCardRangeRequest')
			 	$("#cardRangeStatus").submit();
				$("#statusChangeDesc").next().html('');
			} else {
				$("#statusChangeDesc").next().html('');
				 document.getElementById("dialogErrorApprove").innerHTML='<font color="red">'+readMessage("statusChangeDesc.empty")+"</font>";
				
				
			}

		}

		function goReject() {
			if ($("#statusChangeDescReject").val() != null
					&& $("#statusChangeDescReject").val().length != 0) {
				$("#cardRangeStatusReject").attr('action','approveCardRangeRequest')
			 	$("#cardRangeStatusReject").submit();
				$("#statusChangeDescReject").next().html('');
			} else {
				$("#statusChangeDescReject").next().html('');
				 document.getElementById("dialogErrorReject").innerHTML='<font color="red">'+readMessage("statusChangeDesc.empty")+"</font>";
			}
		}
		
		function goDeleteCardRange() {
		
				$("#deleteCardRange").attr('action','deleteCardRange')
			 	$("#deleteCardRange").submit();
			
		}
		
		
		function clickEditOrDelete(eleObj,action,cardRangeId){

//			var rowID=$(eleObj).closest('tr').attr('id');
			
//			$("#cardRangeSearch #cardRangeID").val(rowID.split("-")[0]);
			$("#cardRangeSearch #cardRangeID").val(cardRangeId);
			if(action!=null && action.trim()!=''){
				if(action=='Edit'){
					$("#cardRangeSearch").attr('action','editCardRange');
					$("#cardRangeSearch").submit();
				}
			/*	else if(action=='Delete'){
					
					var answer = confirm("Are you sure you want to delete");
					if (answer == true) {
						$("#cardRangeSearch").attr('action','deleteCardRange');
						$("#cardRangeSearch").submit();
						return true;
					} else {
						return false;
					}
						
				}*/
			}
		} 	
		
		function clickView(eleObj,cardRangeId){

			$("#cardRangeSearch #cardRangeID").val(cardRangeId);
			
					$("#cardRangeSearch").attr('action','viewCardRange');
					$("#cardRangeSearch").submit();
				
			
			
		} 	
		
		
		function FormSubmit(formId,event){
			
			var retflag=true;
			
			var issuerName = $("#issuerId").val();
			if(issuerName == "-1")
			{
			generateAlert(formId, "issuerId","issuerId.unselect");
			retflag=false;
			}
		
			 var prefix=validateFields(formId,"prefix",true);
			 var cardLength=validateFields(formId,"cardLength",true);
			 var startCardRange=validateFields(formId,"startCardRange",true);
			 var endCardRange=validateFields(formId,"endCardRange",true);

			 if ( prefix && cardLength && startCardRange&& endCardRange){

				 var cardLengthVal=$("#cardLength").val();
				 var startCardRangeVal=($("#prefix").val()+$("#startCardRange").val());
				 var endCardRangeVal=$("#prefix").val()+$("#endCardRange").val();
				 var isChecked=$("#checkDigitYes").is(":checked");
				 
				 if(cardLengthVal < 12 || cardLengthVal > 21 ){
					 generateAlert(formId, "cardLength","cardLength.InvalidLength");
					 retflag=false; 
				 }
				 else{
					if(isChecked ){
						if (cardLengthVal !=(startCardRangeVal.length+1))
						{
						 generateAlert(formId, "cardLength","cardLength.MisMatchLength");
						 retflag=false;
						}
					}
						else if(cardLengthVal!=(startCardRangeVal.length))
						{
						 generateAlert(formId, "cardLength","cardLength.MisMatchLength");
						 retflag=false;
						}
					}
				 
				 
				 if(parseInt($("#startCardRange").val()) >= parseInt($("#endCardRange").val())){
					 generateAlert(formId, "endCardRange","endCardRange.EndIsGraterError");
					 retflag=false; 
				 }
				 
				 
				 if(startCardRangeVal.length!=endCardRangeVal.length) {
					 generateAlert(formId, "endCardRange","endCardRange.LengthNotMatch");
					 generateAlert(formId, "startCardRange","startCardRange.LengthNotMatch");
					 retflag=false;
				 }
				 
				 if(retflag){
					 document.getElementById("issuerName").value=$("#issuerId").find("option:selected").text();
					 if(formId=='editCardRange'){
						 document.getElementById("issuerNameDisp").innerHTML= $("#issuerId").find("option:selected").text();
						 $("#editButton").attr('data-target','#define-constant-edit');
					 }
					 if(formId=='addCardRange'){
						 $("#"+formId).attr('action','saveCardRange');
						 $("#"+formId).submit();
					 }
				 }
				else{
					 $("#editButton").removeAttr('data-target');
				 }
			 }
			 	
		 }
		
		
     function goEdit(){
    		$("#editCardRange").attr('action','updateCardRange');
			$("#editCardRange").submit();
     }
		
		
		function backToAddCardRange() {
			
			$("#addCardRange").attr('action','cardRangeConfig')
		 	$("#addCardRange").submit();

		}
		
		function backToSearchCardRange() {

			$("#editCardRange").attr('action','cardRangeConfig')
		 	$("#editCardRange").submit();

		}
		
		function backFromViewToSearchCardRange() {

			$("#viewCardRange").attr('action','cardRangeConfig')
		 	$("#viewCardRange").submit();

		}
		
		function goReset() {
			clearError("startCardRange");
			clearError("endCardRange");
			clearError("network");
			clearError("cardLength");
			clearError("issuerId");
			clearError("prefix");
			clearError("prefix");
			
			$("#endCardRange").val('');
			$("#network").val('');
			$("#prefix").val('');
			$("#startCardRange").val('');
			$("#cardLength").val('');
			$("#checkDigitYes").prop("checked", true);
			$("#shuffled").prop("checked", true);
			$('select option[value="-1"]').prop("selected",true);
			$('#messageResult').html('');
				
		}



/*To clear common error */
 function clearError(ctrl){
		$("#" + ctrl + "").next().html('');
	}
 
 

 /*To display success/failure messages*/
 function loadStatus(statusFlag,statusMsg)
	{
		if(statusMsg=='') 
		{ 		
		   document.getElementById("feedBackTd").innerHTML="&nbsp;";  
		}
		else
		{
			if(statusFlag=="success"){	
				 $("#feedBackTd").html('<center><font class="successMsg"><b>&nbsp;&nbsp;&nbsp;'+statusMsg+'</b></font></center>');
			 }
			 else
			 {
			  	$("#feedBackTd").html('<center><font class="fieldError"><b>&nbsp;&nbsp;&nbsp;'+ statusMsg + '</b></font></center>');
		}
	}
}

 /*To display error messages for client side validation*/
function generateAlert(formId, eleId,key){
	 
	var text=readMessage(key);
	clearError(formId+" #"+eleId);
	$("#"+formId+" #"+eleId).after('<span class="error_empty" style="color:red;"><br/>' + text + '</span>');
 }
 
/*To clear error messages of client side validation
*/ function clearFieldError(formId, eleId){
	 clearError(formId+" #"+eleId);
 }
function validateEmptyFields(formId,eleId,mandatory){
	 var isValid=false;
	 var minLen=readValidationProp(eleId+'.min.length');
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 if (mandatory){
		 if(eleVal==null || eleVal=='' || minLen==0){ 
		 	generateAlert(formId, eleId,eleId+".empty");
			 
		 		return isValid;
		 	 }
		 else
		 {
			 clearError(formId+" #"+eleId);
			 
			 isValid=true;
		 }
	
 	return isValid;
	 
 }
}

		 
	 
 
	
 
 
 function validateFields(formId,eleId,mandatory){
	 
	 var isValid=false;
	 var minLen=readValidationProp(eleId+'.min.length');
	 var eleVal=$("#"+formId+" #"+eleId).val();
	// clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
	 
	 if (mandatory)
		 {
		 if((eleVal==null ||eleVal=='')  && minLen!=0){ 
		 		generateAlert(formId, eleId,eleId+".empty");
		 		return isValid;
		 	 }	
		 
	  
 		 if((eleVal.trim().length<minLen) || ( eleVal.trim().length>readValidationProp(eleId+'.max.length'))){
			 generateAlert(formId, eleId,eleId+".length");
		 }
 		 else if((eleVal!=null && eleVal!='') && !pattern.test(eleVal)){ 
 			 generateAlert(formId, eleId,eleId+".pattern");
 		 }
		 else
		 {
			 clearError(formId+" #"+eleId);
			 
			 isValid=true;
		 }
		 }
	 else
		 {
		 clearError(formId+" #"+eleId);
		 
		 isValid=true;
		 
		 }
	
 	return isValid;
	 
 }
 
function validateNetworkField(formId,eleId){
	 
	 var isValid=false;
	 var minLen=readValidationProp(eleId+'.min.length');
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
	 
	 if(eleVal==null || eleVal=='' || minLen==0){
		 clearError(formId+" #"+eleId);
		 return isValid;
	 }
	 else if(!pattern.test(eleVal)){ //
			 generateAlert(formId, eleId,eleId+".pattern");
			return isValid;
		 }
	 else if((eleVal.trim().length<minLen) || ( eleVal.trim().length>readValidationProp(eleId+'.max.length'))){
			 generateAlert(formId, eleId,eleId+".length");
			 return isValid;
		 }
 		 
		 else
		 {
			 clearError(formId+" #"+eleId);
			 
			 isValid=true;
		 }
	
 	return isValid;
	 
 }

function validateDropDownForIssuerName(formId, eleId)
{
	var isValid = false;
	var eleVal = $("#" + formId + " #" + eleId).val();
	
	if (eleVal.length<=0 || eleVal < 0){
		generateAlert(formId, eleId, eleId + ".unselect");
	} else {
		clearError(formId + " #" + eleId);
		isValid = true;
	}
	return isValid;
}
 
 $(".allowNumber").keyup(function(event){
		if(event.which != 8 && isNaN(String.fromCharCode(event.which))){
			event.preventDefault(); //stop character from entering input
		}
	});