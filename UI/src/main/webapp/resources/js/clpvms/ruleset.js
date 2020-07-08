//RuleSet name display based on dropdown selection.

function fillRuleSetName(param) {
	if (param.value == '-1') {
		$('#ruleSetNametxt').val("");
	} else {
		clearError('ruleSetForm #ruleSetNametxt');
		$("#feedBackTd").html('');
		var ruleSetName = param.options[param.selectedIndex].text;
		$('#ruleSetNametxt').val(ruleSetName);
	}
}

function saveRuleSet()
 {
	//validate rule name
	if ($("#ruleSetId").val() == '-1') {
		var validRuleSetName = validateRuleSetName('ruleSetForm','ruleSetNametxt');
		if(!validRuleSetName)
			return false;
		
	}
	else
		clearError('ruleSetForm #ruleSetNametxt');
	
	//validate rule
/*	var validRuleId=addRule();  //ruleId empty check is present in addRule()
	if(!validRuleId)
		return false;*/
	
/*	if($("#ruleId").val() == '-1')
		{
		generateAlert('ruleSetForm', 'ruleId','ruleId.empty');
		return false;
		}
	else
		clearError('ruleSetForm #ruleId');*/
	
		$("#ruleSetForm").attr('action', 'addRuleSet');
		$("#ruleSetForm").submit();
	
	
}

/* Valid input rule set Name */
function validateRuleSetName(formId,eleId){
	 
	 var isValid=false;
	 var minLen=readValidationProp(eleId+'.min.length');
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);

	 if((eleVal==null ||eleVal=='')  && minLen!=0){ 
		generateAlert(formId, eleId,eleId+".empty");
		return isValid;
	 }	 
		 if( !(eleVal==null ||eleVal=='') &&  ( (eleVal.trim().length<minLen) || (eleVal.trim().length>readValidationProp(eleId+'.max.length'))) ){
			 generateAlert(formId, eleId,eleId+".length");
		 }
		 else
		 {
			 clearError(formId+" #"+eleId);
			 
			 isValid=true;
		 }
	
	return isValid;
	 
}




