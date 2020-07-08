
 /* PRODUCT CVV STARTS */
/*To validate AlphaNumeric and space value, it replaces the characters other than alphabets,spaces and numbers by ''*/
function isAlphaNumericWithSpace(ctrl) {
    var isValid = false;
    var regex = /[a-zA-Z0-9 ]+$/;
   isValid = regex.test($("#" + ctrl.id + "").val());
   ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ]+$/, '');
   if(ctrl.value.startsWith(' '))
       ctrl.value =ctrl.value.replace(/\s+/,'');
   return isValid;
}

/*To validate AlphaNumeric value, it replaces the characters other than alphabets and numbers by ''*/
 function isAlphaNumeric(ctrl) {
		var isValid;
	 	var regex = /^[a-zA-Z0-9]*$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9]/g, '');
		return isValid;
	}


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
 

	function getCvvDetails()
	{
		
		if($("#parentProductId  option:selected").val()==-1){
			 document.getElementById("parentProductIdCvvError").innerHTML='<font color="red">'+readMessage("parentProductIdCvvError.empty")+"</font>";
			return false;
		}
		clearError("parentProductIdCvvError");	
		$("#productCVV").attr('action', 'getCvvDetailsById')
		$("#productCVV").submit();
	}

	function cvvFunc()
	{
		
	var cvvParameters = $("#cvvParameters");
	var cvvParameters_hsm = $("#cvvParametershsm");
	//var cscParameters = $("#cscParameters");	
	var cvkKeySpec = $("#cvkKeySpec");
		
	var embossApplicable=$("#embossApplicable_Enable").is(":checked");
	var cvkFormatHsm=$("#cvkFormat_Enable").is(":checked");
	var cvkFormatHost=$("#cvkFormat_Disable").is(":checked");
	var cvv=$("#cardVerifyType_Cvv").is(":checked");
	//var csc=$("#cardVerifyType_Csc").is(":checked");

	if(embossApplicable &&cvkFormatHost && cvv )
		{
		$(cvvParameters).show();
		$(cvkKeySpec).show();
		$(cvvParameters_hsm).hide();
		//$(cscParameters).hide();
		}
	/*else if(embossApplicable && ( cvkFormatHost || cvkFormatHsm ) && csc)
		{
		$(cscParameters).show();
		$(cvvParameters).hide();
		$(cvvParameters_hsm).hide();
		$(cvkKeySpec).hide();
		}*/
	else if(embossApplicable && cvkFormatHsm && cvv )
		{
		$(cvvParameters_hsm).show();
		$(cvkKeySpec).show();
		$(cvvParameters).hide();
		//$(cscParameters).hide();
		}
	
	if(cvkFormatHsm)
		{
		clearError("cvkIndex");
		}
	
	if(cvkFormatHost)
		{
		clearError("cvkA");
		clearError("cvkB");
		}
	}

	function embossNotApplicable()
	{
		var cvvParameters = document.getElementById("cvvParameters");
		var cvvParameters_hsm = document.getElementById("cvvParametershsm");
		//var cscParameters = document.getElementById("cscParameters");	
		var cvkKeySpec = $("#cvkKeySpec");
		
		clearError("cvkA");
		clearError("cvkB");
		clearError("cvkIndex");
		clearError("cvkKeySpecifier");
		
		var embossNotApplicable=$("#embossApplicable_Disable").is(":checked");

		if (embossNotApplicable)
			{
			$(cvvParameters).hide();
			//$(cscParameters).hide();
			$(cvvParameters_hsm).hide();
			$(cvkKeySpec).hide();
			
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
		
		
		function validateCvvParameters(formId,eleId){	 
			 var isValid=false;	 
			 var eleVal=$("#"+formId+" #"+eleId).val();
			 clearError(formId+" #"+eleId);
			 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));	
			 
			 if(eleVal==null ||eleVal==''){ 
					generateAlert(formId, eleId,eleId+".empty");
					return isValid;
				 }	
			 
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
		
		function cvvSubmit(formId,event)
		{
			
			var cvkFormatHsm=$("#cvkFormat_Enable").is(":checked");
			var cvkFormatHost=$("#cvkFormat_Disable").is(":checked");
			var embossNotApplicable=$("#embossApplicable_Disable").is(":checked");
			var embossApplicable=$("#embossApplicable_Enable").is(":checked");

			if(embossApplicable&&cvkFormatHost){
			var cvkA =validateCvvParameters(formId,"cvkA");
			var cvkB = validateCvvParameters(formId,"cvkB");
			var cvkKeySpecifier= validateCvvParameters(formId,"cvkKeySpecifier");
			}
			else if( embossApplicable&&cvkFormatHsm)
				{
			var cvkIndex=validateCvvParameters(formId,"cvkIndex");
			var cvkKeySpecifier= validateCvvParameters(formId,"cvkKeySpecifier");
				}
			//var cscKey= validateCvvParameters(formId,"cscKey");
			
			
			
		 	//if (cvkA && cvkB && cvkIndex && cscKey && URL && cvkKeySpecifier){
			
			
			if (embossNotApplicable ||(embossApplicable && cvkFormatHsm && cvkIndex &&cvkKeySpecifier)||( embossApplicable && cvkFormatHost&&cvkKeySpecifier&&cvkB&&cvkA)){
			 
		 		$("#"+formId).attr('action','saveCVVDetails');
		 		$("#"+formId).submit();
		 	}
		 	else
		 		{
		 		return false;
		 		}
		 
		} 
	
	
	
	
	
	