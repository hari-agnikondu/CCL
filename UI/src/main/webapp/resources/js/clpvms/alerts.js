	
	/**********ALERT****************/
	
	
	
		function validateEmailAlertMandatory(formId,eleId){

			var isValid = false;
			 clearError(formId+" #"+eleId);	
			var pattern =/^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			 clearError(formId+" #"+eleId);	
			var eleVal=$("#"+formId+" #"+eleId).val();
			 clearError(formId+" #"+eleId);	
			
			
				if((eleVal==null || eleVal=='')){
					generateAlert(formId, eleId,eleId+".empty");
					 return isValid;
				}
					
			 else if((eleVal!=null && eleVal!='') && !pattern.test(eleVal)){
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
		
		
		function validateAlertwithNumericandMaxlength(formId,eleId){			 
			 var isValid=false;
			 
			 var eleVal=$("#"+formId+" #"+eleId).val();
				 
			 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));	
			 
			 if((eleVal!=null && eleVal!='') && !pattern.test(eleVal)){ 
				 generateAlert(formId, eleId,eleId+".pattern");
				 return isValid;
			 }
			 else if(eleVal.trim().length>readValidationProp(eleId+'.max.length')){
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
		
		
		function validateAlerCheckWithoutPattern(formId,eleId){			 
			 var isValid=false;
			 clearError(formId+" #"+eleId);	 
			 var eleVal=$("#"+formId+" #"+eleId).val();
			 clearError(formId+" #"+eleId);
			 
			 
			 if(eleVal==null ||eleVal==''|| eleVal.length==0){ 
					generateAlert(formId, eleId,eleId+".empty");
					return isValid;
				 }			 
			 else if(eleVal.trim().length>readValidationProp(eleId+'.max.length')){
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
			 
		
		
		 function validateAlertswithMinMaxLengthCheck(formId,eleId){
			 
			 var isValid=false;
			 var minLen=readValidationProp(eleId+'.min.length');
			 var eleVal=$("#"+formId+" #"+eleId).val();
			 clearError(formId+" #"+eleId);	
			 
			 if((eleVal==null ||eleVal==''|| eleVal.length==0)){ 
					generateAlert(formId, eleId,eleId+".empty");			
				 }		
			  
			 else if((eleVal.trim().length<minLen) || ( eleVal.trim().length>readValidationProp(eleId+'.max.length'))){
					 generateAlert(formId, eleId,eleId+".length");
					 
				 }
			 else
				 {
				 clearError(formId+" #"+eleId);
				 isValid=true;
				 }
			 return isValid;
			 
		 }
		 function alertsSubmit(formId,event)
		 {
		 /*
		 	var txnCountForRecentStatement =validateTxnCount(formId,"txnCountForRecentStatement");
		 	var serviceCode = validateServiceCode(formId,"serviceCode");
		 	var url=validateGeneralFields(formId,"URLForROBOHelpLabel");
		 	var customerCareNumber=validateGeneralFields(formId,"customerCareNumber");
		 	var email= validateEmail(formId,"emailIDForStatementLabel");
		 	var URL= validateURL(formId,"URLForROBOHelpLabel");
		 	
		   */
		  	/*if (txnCountForRecentStatement && serviceCode && url && customerCareNumber&&email && URL){*/
		 	 
		  		$("#"+formId).attr('action','saveProductAlerts');
		  		$("#"+formId).submit();
		  	/*}
		  	else
		  		{
		  		return false;
		  		}*/
		  
		 } 
		
		
		

		 
		 function isAlphaNumericWithSpaceproductNameId(ctrl) {
			    var isValid = false;
			    var regex = /[a-zA-Z0-9 ]+$/;
			   isValid = regex.test($("#" + ctrl.id + "").val());
			   ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ]+$/, '');

				if ((ctrl.value.charAt(0) == ' '))
					ctrl.value = ctrl.value.replace(/\s+/, '');
			   return isValid;
			}
		 
		 function isAlphaNumericWithOutSpaceproductNameId(ctrl) {
			    var isValid = false;
			    var regex = /[a-zA-Z0-9]+$/;
			   isValid = regex.test($("#" + ctrl.id + "").val());
			   ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9]+$/, '');
			   if ((ctrl.value.charAt(0) == '0'))
					ctrl.value = ctrl.value.replace('0', '');
			   return isValid;
			}
		 
		 function isNumericproductNameId(ctrl) {


	
			var isValid;
		 	var regex = /^[0-9]*$/;
			isValid = regex.test($("#" + ctrl.id + "").val());
			ctrl.value = ctrl.value.replace(/[^0-9]/g, '');
			
			if ((ctrl.value.charAt(0) == '0'))
				ctrl.value = ctrl.value.replace('0', '');
			
			/*if((ctrl.value.startsWith(' '))||(ctrl.value.endsWith(' ')))
				ctrl.value =ctrl.value.replace(/\s+/,'');*/
			return isValid;
		}