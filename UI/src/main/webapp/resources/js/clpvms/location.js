
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


function addLocation(){
	
	$("#locationSearch").attr('action','addLocation');
 	$("#locationSearch").submit();
	
}

function updateConfirm(formId,event){
	
	var merchantId =validateDropDownForLocation(formId,"merchantId");
	var country =validateDropDownForLocation(formId,"country");
	var state =validateDropDownForLocation(formId,"state");
	var locationName =validateFieldsLocation(formId,"locationName");
	var addressOne =validateFieldsForNoPatternCheck(formId,"addressOne");
	var addressTwo =validateFieldsForAddressTwo(formId,"addressTwo");
	var city =validateFieldsForNoPatternCheck(formId,"city");
	var zip =validateFieldsLocation(formId,"zip");
 	
 	 if(merchantId && locationName && country && state  && addressOne && addressTwo && city &&  zip){
 		 document.getElementById("displayName").innerHTML =$("#locationName").val();
 		
 		var  merchantName =$("#merchantId").find("option:selected").text();
 		document.getElementById("dispMerchName").innerHTML=jQuery.trim(merchantName);
 		
 		document.getElementById("merchantName").value=$("#merchantId").find("option:selected").text();
 		
		$("#updateLocation").attr('data-target','#define-constant-editModel');
	 }
 	else{
		 $("#editLocation").removeAttr('data-target');
	 }
}

function updateLocationSubmit(){
	
	$("#editLocation").attr('action','updateLocation');
 	$("#editLocation").submit();
}

function clickEditForLocation(eleObj,id){
	
	var rowID=$(eleObj).closest('tr').attr('id');
	
	$("#locationSearch #locationId").val(rowID.split("-")[0]);
	$("#locationSearch").attr('action','editLocation');
 	$("#locationSearch").submit();
}

function clickViewForLocation(eleObj,id){
var rowID=$(eleObj).closest('tr').attr('id');
	
	$("#locationSearch #locationId").val(rowID.split("-")[0]);
	$("#locationSearch").attr('action','viewLocation');
 	$("#locationSearch").submit();
	
}

var rowidforcolor="";
function deleteLocation(eleObj, id){
	
	var rowID=$(eleObj).closest('tr').attr('id');
	var val=rowID.split("-");
	rowidforcolor=rowID;
	 document.getElementById(rowID).style.background = "#85b3fc";
	 document.getElementById("locationId").value = id;
	 	
	 document.getElementById("locationIdModelDelete").value = val[0];
	 document.getElementById("locationModelDeleteName").value = val[1];	 
	 document.getElementById("displayName").innerHTML = val[1];
	
	
}

function deleteLocationDet() 
{
		$("#locationSearch").attr('action','deleteLocation');
	 	$("#locationSearch").submit();
}


function goToPrevious() {
	 document.getElementById(rowidforcolor).style.background = "#f9f9f9";

}



function goResetForAddLocation() {
	clearError("merchantId");
	clearError("locationName");
	clearError("addressOne");
	clearError("addressTwo");
	clearError("state");
	clearError("country");
	clearError("city");
	clearError("zip");
	
	
	$("#merchantId").val('');
	$("#locationName").val('');
	$("#addressOne").val('');
	$("#addressTwo").val('');
	$("#state").val('');
	$("#country").val('');
	$("#city").val('');
	$("#zip").val('');
	
	$('select option[value="-1"]').prop("selected",true);
	$('#serviceErrMsg').html('');
		
}





function backAddToLocationConfig(){
	
	$("#addLocation").attr('action','locationConfig');
 	$("#addLocation").submit();
}


function backEditToLocationConfig(formId,event){
	
	$("#"+formId).attr('action','locationConfig');
	$("#"+formId).submit();
	
}

function backViewToLocationConfig(formId,event){
	
	$("#"+formId).attr('action','locationConfig');
	$("#"+formId).submit();
	
}


function validateDropDownForLocation(formId, eleId)
{
	
	var isValid = false;
	var eleVal = $("#" + formId + " #" + eleId).val();
	
	if (eleVal.length<=0 || eleVal < 0){
		generateAlert(formId, eleId, eleId + ".empty");
		isValid = false;
	} else {
		clearError(formId + " #" + eleId);
		isValid = true;
	}
	return isValid;
}


function validateFieldsLocation(formId,eleId){
	 
	 var isValid=false;
	 var minLen=readValidationProp(eleId+'.min.length');
	 var maxLen=readValidationProp(eleId+'.max.length');
	 var eleVal=$("#" +eleId).val();
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
	 	
		 if( eleVal==null || eleVal==''  || eleVal.length==0){ 
		 		generateAlert(formId, eleId,eleId+".empty");
		 		return isValid;
		 	 }	
		 
		 else{
			 if((eleVal!=null && eleVal!='') && ((eleVal.trim().length<minLen) || ( eleVal.trim().length>maxLen))){
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
	 

	
	return isValid;
	 
}

function validateFieldsForNoPatternCheck(formId,eleId){	 
	 var isValid=false;
	 var minLen=readValidationProp(eleId+'.min.length');
	 var maxLen=readValidationProp(eleId+'.max.length');
	 var eleVal=$("#" +eleId).val();
		 	
		 if( eleVal==null || eleVal==''  || eleVal.length==0){ 
		 		generateAlert(formId, eleId,eleId+".empty");
		 		return isValid;
		 	 }			 
		 else{
			 if((eleVal!=null && eleVal!='') && ((eleVal.trim().length<minLen) || ( eleVal.trim().length>maxLen))){
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

function validateFieldsForAddressTwo(formId,eleId){	 
	 var isValid=false;
	 var maxLen=readValidationProp(eleId+'.max.length');
	 var eleVal=$("#" +eleId).val();
		 			
			 if(( eleVal.trim().length>maxLen)){
				 generateAlert(formId, eleId,eleId+".length");
			 }
			 else
			 {
				 clearError(formId+" #"+eleId);
				 
				 isValid=true;
			 }
		 	
	return isValid;
	 
}



function formSubmitForAddLocation(formId,event)
{
	var merchantId =validateDropDownForLocation(formId,"merchantId");
	var country =validateDropDownForLocation(formId,"country");
	var state =validateDropDownForLocation(formId,"state");
	var locationName =validateFieldsLocation(formId,"locationName");
	var addressOne =validateFieldsForNoPatternCheck(formId,"addressOne");
	var addressTwo =validateFieldsForAddressTwo(formId,"addressTwo");
	var city =validateFieldsForNoPatternCheck(formId,"city");
	var zip =validateFieldsLocation(formId,"zip");
	 document.getElementById("merchantName").value=$("#merchantId").find("option:selected").text();

	
	  
 	if (merchantId && locationName && country && state  && addressOne && addressTwo && city &&  zip){
 			
 		$("#"+formId).attr('action','saveLocation');
 		$("#"+formId).submit();
 	}
 	else
 		{
 		return false;
 		}
 
} 


/*To validate AlphaNumeric and space value, it replaces the characters other than alphabets,spaces and numbers by ''*/
function isAlphaNumericWithSpaceAndHash(ctrl) {
	 	var isValid;
	 	var regex = /^[a-zA-Z0-9]+[a-zA-Z0-9 #]*$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 #]*/g, '');
		if(ctrl.value.startsWith(' '))
			ctrl.value =ctrl.value.replace(/\s+/,'');
		return isValid;
}


function isAlphaNumericWithSpaceforZip(ctrl) {
 	var isValid;
 	var regex = /^[a-zA-Z0-9]+[a-zA-Z0-9 ]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ]*/g, '');
	if(ctrl.value.startsWith(' '))
		ctrl.value =ctrl.value.replace(/\s+/,'');
	return isValid;
}

function validateMerchantNameSearch(formId,eleId){
	 
	 var isValid=false;
	 var maxLen=readValidationProp(eleId+'.max.length');
	 var eleVal=$("#" +eleId).val();
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
				
			 if(eleVal!=null && (eleVal.trim().length>maxLen)){
				 generateAlert(formId, eleId,eleId+".length");
			 }
			 else if(eleVal!=null && eleVal!='' && !pattern.test(eleVal)){ 
				 generateAlert(formId, eleId,eleId+".pattern");
			 }
			 else
			 {
				 clearError(formId+" #"+eleId);
				 
				 isValid=true;
			 }
			 return isValid;
		 }


/**
 * all selected states based on country
 * @returns
 */

function getStates(){
	
	
	var country=$("#country option:selected").val();
	 $("#state").empty();
	 $("#state").append('<option value="-1">- - - Select - - -</option>');
	if(country!=-1){
		var str = $('#srvUrl').val()+"/ajax/getStates/"+country;  
		 $.ajax({
		     url: str,

	      async:true,

	      type: "GET",

	      dataType: 'json',

	     /* crossDomain: true,*/

	      success:function(response, status, xhr){
	    	  
	    	  console.log(response.countryCodeID);
	    	  console.log(response.statecodes.stateCodeID);
	    	  
	    	  var arrayObj=response.statecodes;
	    	  
	    	  
	    	  	for(var i=0;i<arrayObj.length;i++){
	    		  console.log(arrayObj[i].stateCodeID+'-'+ arrayObj[i].stateName);
	    		  if(arrayObj[i].stateCodeID!=null && arrayObj[i].stateName!=""){
	    			  $("#state").append('<option value="'+arrayObj[i].stateCodeID+'" >'
		        			  +arrayObj[i].stateName+'</option>');
	    		 }
	    		  
	    	  	}
		      }
	    });
	}
}
