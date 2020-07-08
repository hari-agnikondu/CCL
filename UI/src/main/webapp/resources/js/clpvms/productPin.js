
 /* PRODUCT FUNCTIONS STARTS */
 
/*To clear common error */
 function clearError(ctrl){
		$("#" + ctrl + "").next().html('');
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
 
 
function validatePinFields(formId,eleId){	 
	 var isValid=false;	 
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
	  
	  if((eleVal!=null && eleVal!='') && !pattern.test(eleVal)){ //
			 generateAlert(formId, eleId,eleId+".pattern");
			return isValid;
		 }
	  else if( eleVal.trim().length>readValidationProp(eleId+'.max.length')){
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


function validateMaxPinLengthField(formId,eleId){	 
	 var isValid=false;	 
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
	  
	  if((eleVal!=null && eleVal!='') && !pattern.test(eleVal)){ //
			 generateAlert(formId, eleId,eleId+".pattern");
			return isValid;
		 }
	  else if((eleVal!=null && eleVal!='') && eleVal<4 ){
			 generateAlert(formId, eleId,eleId+".minvalue");
			 return isValid;
		 }
	  
	  else if( eleVal.trim().length>readValidationProp(eleId+'.max.length')){
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

function validatePvkAndDecimalisationTableFields(formId,eleId){
	 
	 var isValid=false;
	 var minLen=readValidationProp(eleId+'.min.length');
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
	 
	if(eleVal=="" || eleVal==null){
		
		 clearError(formId+" #"+eleId);
		 
		 isValid=true;
	}
	 else if(!pattern.test(eleVal))
	 { 
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

 
function pinSubmit(formId,event)
{
	var pinLength =validatePinFields(formId,"pinLength");
	var pvk =validatePvkAndDecimalisationTableFields(formId,"pvk");
	var maximumPINLength =validateMaxPinLengthField(formId,"maximumPINLength");
	var checkLength =validatePinFields(formId,"checkLength");
	var panOffset =validatePinFields(formId,"panOffset");
	var panVerifyLength =validatePinFields(formId,"panVerifyLength");
	var decimalisationTable =validatePvkAndDecimalisationTableFields(formId,"decimalisationTable");
	var padCharacter =validatePinFields(formId,"padCharacter");
	
	if(pinLength && pvk && maximumPINLength && checkLength && panOffset && panVerifyLength && decimalisationTable && padCharacter){
		
		if(parseInt($("#pinLength").val()) > parseInt($("#maximumPINLength").val())){
			
			 generateAlert(formId, "maximumPINLength","maximumPINLength.MaxIsGraterError");
			 return false;
		}
		else{
			
			$("#"+formId).attr('action','savePinParameters');
	 		$("#"+formId).submit();
	 		return true;
			
		}
		
	}
	else{
		return false;
	}
 
}



function getParentProductDetails() {
	if($("#parentProductId  option:selected").val()==-1){
		 document.getElementById("parentProductIdError").innerHTML='<font color="red">'+readMessage("parentProductIdPinError.empty")+"</font>";
		return false;
	}
	clearError("parentProductIdError");	
	$("#productPIN").attr('action', 'getProductPinDetailsById')
	$("#productPIN").submit();

}

	
	
	
	
	
	
	
	