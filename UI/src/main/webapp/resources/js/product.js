
 /* PRODUCT FUNCTIONS STARTS */
 

function searchProduct(event)
{
	event.preventDefault();
	
	var productName="";
	var code='byName';
	
	productName=$("#productNameId").val();
	

	if(productName === undefined || productName == null || 
			productName.length <= 0 || productName=='')
	{
		var code='all';
		
		$("#searchType").val(code);
		$("#productFormId").attr('action','showAllProducts');
		$("#productFormId").submit();
	}	
	else
	{
		$("#searchType").val(code);
		$("#productFormId").attr('action','searchProductByName');
		$("#productFormId").submit();
	}	
}

function clickAddProduct() {
	$("#productFormId").attr('action','showAddProduct');
	$("#productFormId").submit();
}

	function showEditProduct(id)
	{
		var code=id;
		$("#productId").val(code);
	$("#productFormId").attr('action','showEditProduct');
	$("#productFormId").submit();
	}
function saveProductData(formId, event) {
	event.preventDefault();
	
	
	/*var validProductName = validateFields(formId, "productName");
	var validProductShortName = validateFields(formId, "productShortName");
	var validProductDesc = validateFields(formId, "productDesc");
	var validProductPartnerName = validateDropDown(formId, "productPartnerName");
	var validProductIssuerName = validateDropDown(formId, "productIssuerName");
	var validB2BInitSerialNumQty = validateFields(formId,
			"b2bInitSerialNumQty");
	var validB2BSerialNumAutoReplenishLevel = validateFields(formId,
			"b2bSerialNumAutoReplenishLevel");
	var validB2BSerialNumAutoReplenishVal = validateFields(formId,
			"b2bSerialNumAutoReplenishVal");
	var validDcmsId = validateFields(formId, "dcmsId");
	var validValidityPeriod = validateFields(formId, "validityPeriod");
	if (validProductName && validProductShortName && validProductDesc
			&& validB2BInitSerialNumQty
			&& validB2BSerialNumAutoReplenishLevel
			&& validB2BSerialNumAutoReplenishVal && validDcmsId
			&& validValidityPeriod) {*/
		$("#addProductForm").attr('action', 'addProductDetails');
		$("#addProductForm").submit();
/*
	}*/
}

/*To validate AlphaNumeric and space value, it replaces the characters other than alphabets,spaces and numbers by ''*/
/*function isAlphaNumericWithSpace(ctrl) {
    var isValid = false;
    var regex = /[a-zA-Z0-9 ]+$/;
   isValid = regex.test($("#" + ctrl.id + "").val());
   ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ]+$/, '');
   if(ctrl.value.startsWith(' '))
       ctrl.value =ctrl.value.replace(/\s+/,'');
   return isValid;
}*/

/*To validate AlphaNumeric value, it replaces the characters other than alphabets and numbers by ''*/
 /*function isAlphaNumeric(ctrl) {
		var isValid;
	 	var regex = /^[a-zA-Z0-9]*$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9]/g, '');
		return isValid;
	}*/


/*To clear common error */
 /*function clearError(ctrl){
		$("#" + ctrl + "").next().html('');
	}*/
 /*To display error messages for client side validation*/
/*function generateAlert(formId, eleId,key){
	 
	var text=readMessage(key);
	clearError(formId+" #"+eleId);
	$("#"+formId+" #"+eleId).after('<span class="error_empty" style="color:red;"><br/>' + text + '</span>');
 }*/
 
/*To clear error messages of client side validation
*//* function clearFieldError(formId, eleId){
	 clearError(formId+" #"+eleId);
 }*/
 
 
 /*function validateFields(formId,eleId){
	 
	 var isValid=false;
	 var minLen=readValidationProp(eleId+'.min.length');
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
 	 if((eleVal==null ||eleVal=='')  && minLen!=0){ 
 		generateAlert(formId, eleId,eleId+".empty");
 		return isValid;
 	 }	 
 		 if((eleVal.trim().length<minLen) || ( eleVal.trim().length>readValidationProp(eleId+'.max.length'))){
			 generateAlert(formId, eleId,eleId+".length");
		 }
 		 else if((eleVal!=null && eleVal!='') && !pattern.test(eleVal)){ //
 			 generateAlert(formId, eleId,eleId+".pattern");
 		 }
		 else
		 {
			 clearError(formId+" #"+eleId);
			 
			 isValid=true;
		 }
	
 	return isValid;
	 
 }*/


function goBackToProduct() {

	$("#addProductForm").attr('action', 'productConfig');
	$("#addProductForm").submit();
}



function includeRespDiv() {

	var option = "";

	option = $("#denominationId").val();

	if (option == "Fixed") {
		$("#denomFixed").attr("style", "display:block");
		$("#denomSelect").attr("style", "display:none");
		$("#denomVar").attr("style", "display:none");
	}
	if (option == "Select") {
		$("#denomSelect").attr("style", "display:block");
		$("#denomFixed").attr("style", "display:none");
		$("#denomVar").attr("style", "display:none");
	}
	if (option == "Variable") {
		$("#denomVar").attr("style", "display:block");
		$("#denomSelect").attr("style", "display:none");
		$("#denomFixed").attr("style", "display:none");
	}
	if (option == "NONE") {
		$("#denomVar").attr("style", "display:none");
		$("#denomSelect").attr("style", "display:none");
		$("#denomFixed").attr("style", "display:none");
	}

}

