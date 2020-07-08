/// VALIDATIONS FOR SEARCH ISSUER SCREEN STARTS HERE

/* To search Issuer based on name or to show all issuers*/
function searchIssuer(event)
{
	event.preventDefault();
	
	var issuerName="";
	var code='byName';
	
	issuerName=$("#issuerNameId").val();
	

	if(issuerName === undefined || issuerName == null || 
			issuerName.length <= 0 || issuerName=='')
	{
		var code='all';
		
		$("#searchType").val(code);
		$("#issuerForm").attr('action','showAllIssuers');
		$("#issuerForm").submit();
	}	
	else
	{
		$("#searchType").val(code);
		$("#issuerForm").attr('action','searchIssuerByName');
		$("#issuerForm").submit();
	}	
}
	


function clickAddIssuer() {
	$("#issuerForm").attr('action','showAddIssuer');
	$("#issuerForm").submit();
}	

function ResetAddIssuer(formId){

	var errData=$('#errStatus');
	
	errData.remove();
	errData.empty();
	
	$("#"+formId+" #issuerName").val('');
	$("#"+formId+" #issuerDesc").val('');
	$("#"+formId+" #issuerMdmId").val('');
	clearError(formId+" #issuerName");
	clearError(formId+" #issuerDesc");
	clearError(formId+" #issuerMdmId");
}

function clickAddCardRange() {
	$("#issuerForm").attr('action','cardRangeRegistration');
	$("#issuerForm").submit();
  
}	


function goBack() {
	$("#issuerForm").attr('action','issuerConfig');
	$("#issuerForm").submit();   
}


function goEdit(id) {
	
	var code=id;
	$("#issuerId").val(code);
	$("#issuerForm").attr('action','showEditIssuer');
	$("#issuerForm").submit();   
}

function goView(id){
	
	var code=id;
	$("#issuerId").val(code);
	$("#issuerForm").attr('action','showViewIssuer');
	$("#issuerForm").submit();   
	
}


function goConfirm() {

	
	var id=$('#editIssuerId').val();
	var name=$('#issuerName').val();
	name=name.trim();
	$("#issuerIdtoUpdate").val(name);
	document.getElementById("issuerNameDisp").innerHTML =name;
	$("#issuerId").val(id);
	$("#editIssuerId").val(id);
	 
}
 

var rowidforcolor="";
function goDelete(data) {
	rowidforcolor
		var datatosplit="";
		datatosplit = data;
		datatosplit=datatosplit.replace(/\*/g,"'");
		
	var splitted = datatosplit.split('~');
	var id=splitted[0];
	var name=splitted[1];
	rowidforcolor=id;	
	document.getElementById(id).style.background = "#85b3fc";
	name=name.trim();
	$("#issuerIdtoDelete").val(name);
	document.getElementById("issuerNameDisp").innerHTML =name;
	//$("#issuerNameDisp").val(name);
	var nametosearch=$("#issuerNameId").val();
	$("#issuerId").val(id);
	$("#deletedName").val(name);
	$("#searchedName").val(nametosearch);
	
	
	 
	 
}
function goToPrevious() {
	 document.getElementById(rowidforcolor).style.background = "#f9f9f9";

}




function deleteIssuer( ) {
	
	$("#issuerForm").attr('action','deleteIssuer');
	$("#issuerForm").submit();
}



function goBackToIssuer() {

	$("#RegIssuerForm").attr('action','issuerConfig');
	$("#RegIssuerForm").submit();
}

function goAddIssuer() {
	
	$("#RegIssuerForm").attr('action','addIssuer');
	$("#RegIssuerForm").submit();
}
function goBacktoIssuerfromEdit() {
	
	$("#EditIssuerForm").attr('action','issuerConfig');
	$("#EditIssuerForm").submit();
}
function goBacktoIssuerfromView() {
	
	$("#ViewIssuerForm").attr('action','issuerConfig');
	$("#ViewIssuerForm").submit();
}
function goUpdateIssuer() {
	
	$("#EditIssuerForm").attr('action','editIssuer');
	$("#EditIssuerForm").submit();


}

/// VALIDATIONS FOR ADD EDIT SCREEN ENDS HERE

/*To validate AlphaNumeric and space value, it replaces the characters other than alphabets,spaces and numbers by ''*/
function isAlphaNumericWithSpace(ctrl) {
   var invalidChars = /[^a-zA-Z0-9 ,.;'\-_]/gi;

	if (invalidChars.test(ctrl.value)) {
		ctrl.value = ctrl.value.replace(invalidChars, "");
	}

	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
}

/*To validate AlphaNumeric value, it replaces the characters other than alphabets and numbers by ''*/
 function isAlphaNumeric(ctrl) {
		var invalidChars = /[^a-zA-Z0-9]/gi;

	if (invalidChars.test(ctrl.value)) {
		ctrl.value = ctrl.value.replace(invalidChars, "");
	}

	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
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
 

 
 function validateFields(formId,eleId){
	 
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
	 
 }

 
 function validateMdmId(formId,eleId){
	 
	 var isValid=false;
	// var minLen=readValidationProp(eleId+'.min.length');
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
 /*	 if((eleVal==null ||eleVal=='')  && minLen!=0){ 
 		generateAlert(formId, eleId,eleId+".empty");
 		return isValid;
 	 }	*/ 
	 if(eleVal!=null && eleVal!='' && minLen!=0){
		 
	
		 
		 if( ( eleVal.trim().length>readValidationProp(eleId+'.max.length'))){
			
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
	 }
 		
	
 	return isValid;
	 
 }
 
 


