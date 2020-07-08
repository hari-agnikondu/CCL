
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
 
 
function validateNonManFields(formId,eleId){	 
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
 
function validateServiceCode(formId,eleId){
	  var isValid=false;
	 var minLen=readValidationProp(eleId+'.min.length');
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
	 clearError(formId+" #"+eleId);
	
		 	if((eleVal!=null && eleVal!='') && eleVal==0){ 
		 		generateAlert(formId, eleId,eleId+".zero");	
		 	 }	
		 	 else if((eleVal!=null && eleVal!='') && !pattern.test(eleVal)){ 
	 			 generateAlert(formId, eleId,eleId+".pattern");
	 		 }
		 	else if(( eleVal.trim().length>readValidationProp(eleId+'.max.length'))){
			 generateAlert(formId, eleId,eleId+".length");
		 	}
		 	else
		 {
			 clearError(formId+" #"+eleId);
			 
			 isValid=true;
		 }
	
 	return isValid;
	 
 }
function validateTxnCount(formId,eleId){
	 
	 var isValid=false;
	 var minLen=readValidationProp(eleId+'.min.length');
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
	 
	/*	 if((eleVal!=null && eleVal!='') &&(eleVal<=0 || ( eleVal >10))){ */
	 if((eleVal==null ||eleVal==''|| eleVal.length==0)){ 
		generateAlert(formId, eleId,eleId+".empty");
		return isValid;
	 }	else{ 
		 
		  if((eleVal!=null && eleVal!='') && !pattern.test(eleVal)){ 
			 generateAlert(formId, eleId,eleId+".pattern");
		 }
		  else if((eleVal==0 || ( eleVal >10))){
			 generateAlert(formId, eleId,eleId+".length");
		 }
		 else
		 {
			 clearError(formId+" #"+eleId);
			 
			 isValid=true;
		 }
	 }
	
	return isValid;
	 
}

function validateURL(formId,eleId){

	var isValid = false;
	
	var pattern =/((([A-Za-z]{3,9}:(?:\/\/)?)(?:[\-;:&=\+\$,\w]+@)?[A-Za-z0-9\.\-]+|(?:www\.|[\-;:&=\+\$,\w]+@)[A-Za-z0-9\.\-]+)((?:\/[\+~%\/\.\w\-_]*)?\??(?:[\-\+=&;%@\.\w_]*)#?(?:[\.\!\/\\\w]*))?)/;
	
	
	var eleVal=$("#"+formId+" #"+eleId).val().trim();

	if((eleVal!=null && eleVal!='') && !pattern.test(eleVal.trim())){
		generateAlert(formId, eleId,eleId+".pattern");
		 return isValid;
	}
	else
		{
		 clearError(formId+" #"+eleId);
		 isValid=true;
		 
		}
	return isValid;
}

function validateEmail(formId,eleId){

	var isValid = false;
	var pattern =/^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	var eleVal=$("#"+formId+" #"+eleId).val().trim();

	if((eleVal!=null && eleVal!='') && !pattern.test(eleVal.trim())){
		generateAlert(formId, eleId,eleId+".pattern");
		 return isValid;
	}
	else
		{
		 clearError(formId+" #"+eleId);
		 isValid=true;
		 
		}
	return isValid;
}


function validateGeneralFields(formId,eleId){
	 
	 var isValid=false;
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));

	  
		 if( eleVal.trim().length>readValidationProp(eleId+'.max.length')){
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
	
	return isValid;
	 
}

function validatePinAttempts(formId,eleId){
	 
	 var isValid=false;
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));

	  
	 if (eleVal == null || eleVal == '') {
		generateAlert(formId, eleId, eleId + ".empty");
	} else if (eleVal.trim().length > readValidationProp(eleId + '.max.length')) {
		generateAlert(formId, eleId, eleId + ".length");
	} else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) {
		generateAlert(formId, eleId, eleId + ".pattern");
	} else {
		clearError(formId + " #" + eleId);

		isValid = true;
	}
	
	return isValid;
	 
}


function isNumericForGeneral(ctrl) {
	var isValid;
 	var regex = /^[0-9]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	if((ctrl.value.startsWith(' '))||(ctrl.value.endsWith(' ')))
		ctrl.value =ctrl.value.replace(/\s+/,'');
	return isValid;
}

function validateDecimalFormatForMaxCardBalance(formId,eleId){
    var eleVal=$("#"+formId+" #"+eleId).val();
    var splitvalue = eleVal.split(".");
    clearFieldError(formId, eleId);
    
    if((eleVal==null ||eleVal=='')){ 
        generateAlert(formId, eleId,eleId+".empty");
        return false;
    }
    if((splitvalue.length>2)) {   	
        generateAlert(formId, eleId,eleId+".NaN");
        return false;
    }
    
    if (eleVal.substr(0,1)=='.'){
        eleVal='0'+eleVal;
        $("#"+formId+" #"+eleId).value=eleVal;
    }
    if(!maxCardBalanceValidation(eleId)){
    	 generateAlert(formId, eleId,eleId+".invalid");
    	 return false;
    }
    else if(!maxCardBalanceDecimalValidation(eleId)){
    	generateAlert(formId, eleId,eleId+".invalidDecimal");
   	 return false;
    }
    
    return true;
}

function maxCardBalanceDecimalValidation(ctrl){
	  var isValid = false;
	    var regex = /^[0-9]\d{0,9}(\.\d{1,3})?%?$/;
	    isValid = regex.test($("#" + ctrl + "").val());
	    return isValid;
}

function maxCardBalanceValidation(ctrl){
	  var isValid = false;
	    var regex = /^[0-9]\d{0,9}(\.\d{0,10})?%?$/;
	    isValid = regex.test($("#" + ctrl + "").val());
	    return isValid;
}

 
function allowNumbersWithDotForMaxCardBalance(ctrl){
    var isValid = false;
    var regex = /[0-9.]+$/;
    isValid = regex.test($("#" + ctrl.id + "").val());
    ctrl.value = ctrl.value.replace(/[^0-9.]+$/, '');
    if ((ctrl.value.charAt(0)== ' '))
        ctrl.value = ctrl.value.replace(/\s+/,'');
    if ((ctrl.value.charAt(0)== '.'))
        ctrl.value = ctrl.value.replace('.','0.');
    return isValid;
}

function generalSubmit(formId,event)
{

	var txnCountForRecentStatement =validateTxnCount(formId,"txnCountForRecentStatement");
	var serviceCode = validateServiceCode(formId,"serviceCode");
	var customerCareNumber=validateNonManFields(formId,"customerCareNumber");
	var email= validateEmail(formId,"emailIDForStatementLabel");
	var URL= validateURL(formId,"URLForROBOHelpLabel");
	var cardExpiryPendingPeriodLabel=validateNonManFields(formId,"cardExpiryPendingPeriodLabel");
	var maxNumOfCardsPerCustomerLabel =validateNonManFields(formId,"maxNumOfCardsPerCustomerLabel");
	var preAuthExpiryPeriodLabel=validateNonManFields(formId,"preAuthExpiryPeriodLabel");
	var maxCardBalance=validateDecimalFormatForMaxCardBalance(formId,"maxCardBalance");
	var chwUserAuthentication=validateDropDown(formId,"chwUserAuthentication");
	var ivrUserAuthentication=validateDropDown(formId,"ivrUserAuthentication");
	
	hideBlockingFields();
	var invalidPinAttempts = callValidatePinAttempts(formId);
	replaceRenewalNewProduct();
	
/*	
	if($("#startTimeHours  option:selected").val()==-1){
		 $("#startTimeHoursIdError").innerHTML='<font color="red">'+readMessage("startTimeHoursId.empty")+"</font>";
		 generateAlert(formId, startTimeHours,startTimeHours+".empty");
		return false;
	}
	else if($("#startTimeMins  option:selected").val()==-1){
		$("#startTimeMinsIdError").innerHTML='<font color="red">'+readMessage("startTimeMinsId.empty")+"</font>";
		 generateAlert(formId, startTimeMins,startTimeMins+".empty");
			return false;
		}*/
  

	 	if (event != 'onload' && txnCountForRecentStatement && serviceCode
			&& customerCareNumber && email && URL
			&& cardExpiryPendingPeriodLabel && cardExpiryPendingPeriodLabel
			&& maxNumOfCardsPerCustomerLabel && preAuthExpiryPeriodLabel
			&& maxCardBalance && invalidPinAttempts && chwUserAuthentication
			&& ivrUserAuthentication) {
	 
 		$("#"+formId).attr('action','saveGeneralDetails');
 		$("#"+formId).submit();
 	}
 	else
 		{
 		return false;
 		}
 
} 

function getParentProductDetails() {
	if($("#parentProductId  option:selected").val()==-1){
		 document.getElementById("parentProductIdError").innerHTML='<font color="red">'+readMessage("parentProductId.empty")+"</font>";
		return false;
	}
	clearError("parentProductIdError");	
	$("#productGeneral").attr('action', 'getProductDetailsById')
	$("#productGeneral").submit();

}

function showBlockingFields() {
	var cardBlockingEnable = $("#cardBlocking_Enable").is(":checked");

	if (cardBlockingEnable) {
		$("#invalidPin").show();
		$("#timePeriodforCardBlocking").show();
	}
}

function hideBlockingFields() {
	var cardBlockingDisable = $("#cardBlocking_Disable").is(":checked");

	if (cardBlockingDisable) {
		$("#invalidPin").hide();
		$("#timePeriodforCardBlocking").hide();
	}
}
	

function callValidatePinAttempts(formId)
{
var cardBlockingEnable = $("#cardBlocking_Enable").is(":checked");
	
	if(cardBlockingEnable)
		{
		
		showBlockingFields();
		
		var invalidPinAttempts = validatePinAttempts(formId,"invalidPinAttempts");
		if (!invalidPinAttempts)
		{
			return false;
		}	
		}
return true;
}



	
function replaceRenewalNewProduct() {
	
	
	var newProduct = $("#cardRenewalOrReplacement").val();
	if ("NPP"==newProduct) {
		$("#cardReplaceRenewalNewProdShow").show();
	}
	else{
		$("#cardReplaceRenewalNewProdShow").hide();
	}
}

/*
 * Validate drop down field  
 */
function validateDropDown(formId, eleId)
{
	var isValid = false;
	var eleVal = $("#" + formId + " #" + eleId).val();
	
	if (eleVal.length<=0 || eleVal == 'NONE' || eleVal =='-1'){
		generateAlert(formId, eleId, eleId + ".unselect");
	} else {
		clearError(formId + " #" + eleId);
		isValid = true;
	}
	return isValid;
}	

	
	
	
	
	