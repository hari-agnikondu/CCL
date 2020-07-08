var rowidforcolor="";
var partnerFundedCurrency="";
var consumerFundedCurrency="";

function backToSearchPurse() {

	$("#editPurse").attr('action','purseConfig')
 	$("#editPurse").submit();

}

function backToSearchPurseFromView() {

	$("#viewPurse").attr('action','purseConfig')
 	$("#viewPurse").submit();

}
function goReset() {
	clearError("purseTypeId");
	clearError("extPurseId");
	clearError("description");
	clearError("currencyTypeID");
	//clearError("upc");
	//$("#upc").val('');
	$("#description").val('');
	$("#extPurseId").val('');
	$('select option[value="-1"]').prop("selected",true);
	//$("#upcDiv").hide();
	$("#currencyDiv").hide();
	$('#messageResult').html('');

}

function clearErrorMsg(event){
	clearError(event);
}

function updatePurse(){
	$("#editPurse").attr('action','updatePurse');
	$("#editPurse").submit();
}

function FormSubmit(formId,event){
	
	var flag=true;
	
	if(formId=='addPurse'){
		$("#"+formId).attr('action','savePurse');
		if($("#purseTypeId").val()=="-1"){
			generateAlert(formId, "purseTypeId","purseTypeId.empty");
			flag=false; 
		}
		//if($("#purseTypeId").val()=="3"){
			//flag=validateFields(formId,"upc",true);
		//}
		else if($("#purseTypeId").val()=="1"){
			if($("#currencyTypeID").val()=="-1"){
				generateAlert(formId, "currencyTypeID","currencyTypeID.empty");
				flag=false; 
			}
		}else if($("#purseTypeId").val()=="4"){
			if($("#currencyTypeID").val()=="-1"){
				generateAlert(formId, "currencyTypeID","currencyTypeID.empty");
				flag=false; 
			}
		}
		if($("#extPurseId").val()=='' || $("#extPurseId").val()==null){
			generateAlert(formId, "extPurseId","extPurseId.empty");
			flag=false; 
		}
	}
	if($("#purseTypeId").val()=="3"){
		$("#currencyTypeID").val('001');
	}
	else if($("#purseTypeId").val()=="2"){
		$("#currencyTypeID").val('000');
	}
	if(formId=='editPurse'){
		var  currencyMsgType =$("#purseTypeName").val()+' '+($("#currencyTypeID").val()!='-1'?$("#currCodeAlpha").val()+':'+$("#currencyTypeID").val():'')+' '+$("#upc").val();
 		document.getElementById("displayName").innerHTML=jQuery.trim(currencyMsgType);
			
	}
	
	if(flag && formId=='addPurse'){
		$("#"+formId).submit();}
	 	
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

function validateDropDownFields(formId,eleId){
	 
	 var isValid=false;

	 var eleVal=$("#"+formId+" #"+eleId).val();

		 if(eleVal=='-1'){ 
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
 
 function purseTypeDivDisp(){
	 
	if($("#purseTypeId").val()=="3"){
		$("#currencyDiv").hide();
		$("#currencyTypeID").val('-1')
	}
	else if($("#purseTypeId").val()=="1"){
		$("#currencyDiv").show();
	}
	else if($("#purseTypeId").val()=="4"){
		$("#currencyDiv").show();
	}
	else{
		$("#currencyTypeID").val('-1')
		$("#currencyDiv").hide();
	}
	 
 }
 function goDeleteModel(eleObj,purseTypeName,currencyTypeID,upc) 
 {
	 var rowID=$(eleObj).closest('tr').attr('id');
	 rowidforcolor=rowID;
	 document.getElementById(rowID).style.background = "#85b3fc";
		$("#purseId").val(rowID);
		document.getElementById("displayName").innerHTML =  purseTypeName+' '+currencyTypeID+' '+upc;
 }

 
 function goToPrevious() {
 	 document.getElementById(rowidforcolor).style.background = "#f9f9f9";
 
	}

 function clickEdit(eleObj,action){

 	var rowID=$(eleObj).closest('tr').attr('id');
 	
 	$("#purseSearch #purseId").val(rowID);

 	if(action!=null && action.trim()!=''){
 		if(action=='Edit'){
 			$("#purseSearch").attr('action','editPurse');
 			$("#purseSearch").submit();
 		}
 	}
 } 	
 function clickViewPurse(eleObj,action){

 	var rowID=$(eleObj).closest('tr').attr('id');
 	
 	$("#purseSearch #purseId").val(rowID);

 	if(action!=null && action.trim()!=''){
 		if(action=='View'){
 			$("#purseSearch").attr('action','viewPurse');
 			$("#purseSearch").submit();
 		}
 	}
 } 	

 function dispAddPurse() 
 {
	$("#extPurseId").val('');
	$("#currencyTypeId").val('');
	$("#purseTypeId").val('');
 	$("#purseSearch").attr('action','addPurse')
 	$("#purseSearch").submit();
 }
 
 function checkValidate(formId,event,attr){
	if ($("#" + attr).val() == "" || $("#" + attr).val() == null) {
		if($("#" + event.id + "").val()=="" || $("#" + event.id + "").val()==null){
			$("#" + attr).prop('disabled', false);
		}else{
			var isValid;
			var regex = /^[0-9]*$/;
			isValid = regex.test($("#" + event.id + "").val());
			event.value = event.value.replace(/[^0-9]/g, '');
			if ((event.value.startsWith(' ')) || (event.value.endsWith(' ')))
				event.value = event.value.replace(/\s+/, '');
			
			$("#" + attr).prop('disabled', true);
			return isValid;
		}		
	}
	return false
 }
 
 function search(formId,event){
	if($("#"+formId+" #consumerFundedCurrency").val()==null || $("#"+formId+" #consumerFundedCurrency").val()==""){
		if($("#"+formId+" #partnerFundedCurrency").val()==null || $("#"+formId+" #partnerFundedCurrency").val()==""){
			$("#"+formId+" #currencyTypeId").val('');
			$("#" + formId + " #purseTypeId").val('');
		}else{
			$("#"+formId+" #currencyTypeId").val($("#"+formId+" #partnerFundedCurrency").val());
			$("#" + formId + " #purseTypeId").val('4');
		}
	}else{
		$("#"+formId+" #currencyTypeId").val($("#"+formId+" #consumerFundedCurrency").val());
		$("#" + formId + " #purseTypeId").val('1');
	}
	clearError(formId+" #searchError");
	$("#purseSearch").attr('action','searchPurse')
	$("#"+formId).submit();
}