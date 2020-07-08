
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

/* delete functions */


function goDeleteGroupAccess(){		
		$("#deleteGroupAccess").attr('action','deleteGroupAccess')
	 	$("#deleteGroupAccess").submit();
	
}


function goUpdate(){
	
	$("#editGroupAccessId").attr('action','updateGroupAccess');
	$("#editGroupAccessId").submit();
}


function updateGroupAccess(){
	
	 $('#multiselectEdit_to option').prop('selected', true);
		var options = $('#multiselectEdit_to > option:selected');
		
	document.getElementById("groupAccessNameForEdit").value=$("#groupAccessIdForEdit").find("option:selected").text();
	document.getElementById("groupAccessIdForEditDip").innerHTML= $("#groupAccessIdForEdit").find("option:selected").text();
	
	var groupAccessIdForEdit =validateDropDownForGroupAccessFields("editGroupAccessId","groupAccessIdForEdit");
	
	if (groupAccessIdForEdit && options.length != 0 ){
		
		$("#updateButton").attr('data-target','#define-constant-edit');
	}
 	else{
 		if(options.length != 0){
 			clearError("multiselectEdit_to");
 		}
 		else{
 			generateAlert("editGroupAccessId", "multiselectEdit_to","multiselectEdit_to.multiSelect");
 		}
 		return false;
 	}
	
}


function goUpdateAssignGroupAccess(){
	
	var tableOptions = {
	        'bPaginate': true,
	        'iDisplayLength': 5
	    };
	var table = $('#tablePartnersRange');
	 table.DataTable().destroy()
     tableOptions.bPaginate = false;
	 table.DataTable(tableOptions);
	$("#editAssignGroupAccess").attr('action','updateAssignGroupAccess');
	$("#editAssignGroupAccess").submit();
}


function updateAssignGroupAccess(){
	document.getElementById("groupAccessNameDispForAssign").innerHTML= $("#groupAccessId").find("option:selected").text();	
	document.getElementById("groupAccessProductDispForAssign").innerHTML= $("#productId").find("option:selected").text();	
	$("#updateForAssignButton").attr('data-target','#define-constant-edit');
	
}


var rowidforcolor="";
function goStatusChange(eleObj,id) 
	{	 
	var rowID=$(eleObj).closest('tr').attr('id');
	var val=rowID.split("-");
	console.log(rowID);
	rowidforcolor=rowID;	
 document.getElementById(rowID).style.background = "#85b3fc";
	 	 
	 document.getElementById("productId").value = id;
	 document.getElementById("groupAccessModelDelete").value = val[0];
	document.getElementById("groupAccessModelDeleteName").value = val[1];
	
	document.getElementById("groupAccessNameDisp").innerHTML =  val[1];		
	document.getElementById("groupAccessProductDisp").innerHTML =  val[2];	
		
	}

function goToPrevious() {
	 document.getElementById(rowidforcolor).style.background = "#f9f9f9";

}

function dispAddGroupAccessId() 
		{

	 	$("#groupAccessIdSearch").attr('action','addGroupAccessId')
	 	$("#groupAccessIdSearch").submit();
		}


function dispAssignProduct() 
{

	$("#groupAccessIdSearch").attr('action','assignGroupAccessIdToProduct')
	$("#groupAccessIdSearch").submit();
}

function backToAddGroupAccess() {
	
	$("#addGroupAccessId").attr('action','groupAccessIdConfig')
 	$("#addGroupAccessId").submit();

}

function backToUpdateGroupAccess() {
	
	$("#editGroupAccessId").attr('action','groupAccessIdConfig')
 	$("#editGroupAccessId").submit();

}

function backToAssignGroupAccess() {
	
	$("#assignGroupAccessId").attr('action','groupAccessIdConfig')
 	$("#assignGroupAccessId").submit();

}

function goResetAddGroupAccess() {
	
	clearError("groupAccessName");
	document.getElementById('multiselect_leftAll').click();
	$("#selectedPartnerListErr").html('');
	clearError("multiselect_to");
	$("#groupAccessName").val('');
	$("#serviceErrMsg").html('');
}

function clearDataTable(){
	 $("#dataTableDiv").hide();
	 $("#buttonsForAssign").hide();
	 $("#backAfterFetchBtn").show();
	 var dataTable = $('#tablePartnersRange').DataTable();
	 dataTable.clear();
}


function goResetAssingGroupAccess() {
	
	clearError("productId");
	clearError("groupAccessId");
	 $("#dataTableDiv").hide();
	 
	$('select option[value="-1"]').prop("selected",true);
	$("#buttonsForAssign").hide();
	$("#messageResult").html('');
	 $("#backAfterFetchBtn").show();
	 $("#serviceErrMsg").html('');
}
	

function clickEdit(id) {
		
	$("#groupAccessId").val(id.split("-")[0]);
	$("#productIdGrid").val(id.split("-")[1]);
	$("#groupAccessIdSearch").attr('action','editAssignGroupAccess');
	$("#groupAccessIdSearch").submit();  

}

function clickViewForGroupAcess(id){
	
	$("#groupAccessId").val(id.split("-")[0]);
	$("#productIdGrid").val(id.split("-")[1]);
	$("#groupAccessIdSearch").attr('action','viewAssignGroupAccess');
	$("#groupAccessIdSearch").submit();  

	
}

function backToEditAssignGroupAccess() {
	
	$("#editAssignGroupAccess").attr('action','groupAccessIdConfig')
 	$("#editAssignGroupAccess").submit();

}

function backFromViewAssignGroupAccess(){
	
	$("#viewAssignGroupAccess").attr('action','groupAccessIdConfig')
 	$("#viewAssignGroupAccess").submit();
	
}

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

function validateGroupAccessFields(formId,eleId){
	 
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
		  else if((eleVal <2) || ( eleVal >100)){
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

function validateMultiSelectFields(formId,eleId){
	 
	 var isValid=false;
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 
	 clearError(formId+" #"+eleId);
	
	 if(eleVal.length==null){ 
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
/**
 * all selected partners based on group accessid
 * @returns
 */

function getGroupAccessPartnersforUpdateGroupAccess(){
	
	$("#multiselectEdit_to").html("");
	
	var groupAccessId=$("#groupAccessIdForEdit option:selected").val();	
	var str = $('#srvUrl').val()+"/ajax/getGroupAccessPartners/"+groupAccessId;  
	
	 $.ajax({
	     url: str,

	      async:true,

	      type: "GET",

	      dataType: 'json',

	/*      crossDomain: true,*/

	      success:function(response, status, xhr){
	    	  
	    	  $.each(response, function(index){     
	    		  
	    		  $("#multiselectEdit_to").append('<option value="'+response[index].partnerId+'" readonly>'
	        			  +response[index].partnerName+'</option>');
	    		 	    		  
	    		 $("#multiselectEdit option[value="+response[index].partnerId+"]").remove();

	          }); 
	      }
	    });
}


/**
 * matched partner list with product and group access
 */



function getGroupAccessPartners(formId,event){
	
	var groupAccessId=$("#groupAccessId option:selected").val();
	
	var productId=$("#productId option:selected").val();
	
	var str = $('#srvUrl').val()+"/ajax/getGroupAccessPartners/"+groupAccessId+"/"+productId;
	
	 var dataTable = $('#tablePartnersRange').DataTable();
	 dataTable.clear();
		
	var groupAccessId1 =validateDropDownForGroupAccessFields(formId,"groupAccessId");	
	var productId1 =validateDropDownForGroupAccessFields(formId,"productId");	
	
	if (groupAccessId1 && productId1){		
	 $.ajax({
	     url: str,

	      async:true,

	      type: "GET",

	      dataType: 'json',

	      /*crossDomain: true,*/

	      success:function(response, status, xhr){
	    	 if(response!=null && response!=""){
	    		
	    		 $("#dataTableDiv").show();
	    		 $("#buttonsForAssign").show();
	    		 $("#backAfterFetchBtn").hide();
	    		 
		    	var table =$("#tablePartnersRange").dataTable({
		             "bDestroy": true,
		             "paging": true,
		             "bFilter": true,
		             "bInfo": true,
		             "bSort": true,
		 			"bSortable": false,
		 			"bSearching" : true,
		 			"ordering" : true,
		 			"iDisplayLength": 5,
		 			"pagingType": "simple_numbers"
		            
		         });
		    	
		    
		    	 $.each(response, function(index){  
		    		 var partyType="";
			    	  if(response[index].partnerPartyType!=null && response[index].partnerPartyType=="FIRST PARTY OWNER"){
				    		partyType= "<input type='radio' name=partnerArray["+index+"].partnerPartyType value='FIRST PARTY' checked='checked' />First" +
					    	"<input type='radio' name=partnerArray["+index+"].partnerPartyType    value='THIRD PARTY' disabled />Third" ;
				    	}
				    	else{
				    		partyType="<input type='radio' name=partnerArray["+index+"].partnerPartyType value='FIRST PARTY'/>First" +
					    	"<input type='radio' name=partnerArray["+index+"].partnerPartyType  checked='checked'  value='THIRD PARTY'/>Third" 
				    	}
			    	  
		    		 table.fnAddData([
		    		                  response[index].groupAccessName+"<input type='hidden' name=partnerArray["+index+"].groupAccessId value="+response[index].groupAccessId+">",
		    		                  response[index].partnerName+"<input type='hidden' name=partnerArray["+index+"].partnerId value="+response[index].partnerId+">",
		    		                  partyType
		    		                  ]);
		    
		      }); 
	    	
	    	 }
	    	 else{
	    		 $("#dataTableDiv").hide();
	    		 $("#buttonsForAssign").hide();
	    		 $("#backAfterFetchBtn").show();
	    	 }
	    	
	      },
	      error : function(jqXHR, textStatus, errorThrown) {
			  console.log("error:" + textStatus + " exception:" + errorThrown);
		}

	    });
	}
 	else{
 		return false;
 	}
	 
}

function getGroupAccessProducts(){
	//To clear server validation error message
	$("#serviceErrMsg").html('');
	
	var groupAccessId=$("#groupAccessId option:selected").val();
	var str = $('#srvUrl').val()+"/ajax/getGroupAccessProducts/"+groupAccessId;  
		 $("#productId").empty();
		 $("#productId").append('<option value="-1">- - - Select - - -</option>');
	 $.ajax({
	     url: str,

	      async:true,

	      type: "GET",

	      dataType: 'json',

	      /*crossDomain: true,*/

	      success:function(response, status, xhr){
	    	  
	    	 
	      $.each(response, function(key,value){     

	    	//  console.log(key+'  '+value);
	    	  $("#productId").append('<option value="'+key+'">'
	    			  +value+'</option>');
	    	  
	      }); 
	      },
	      error : function(jqXHR, textStatus, errorThrown) {

			  console.log("error:" + textStatus + " exception:" + errorThrown);
		}

	    });
}

function groupAccessFunc()
{
	
	var groupAccess_Add=$("#groupAccess_Add").is(":checked");
	var groupAccess_Edit=$("#groupAccess_Edit").is(":checked");
	var addGroupAccessName =$("#addGroupAccessName");
	var editGroupAccessName =$("#editGroupAccessName");
	
	if(groupAccess_Add)	
		{
		$(addGroupAccessName).show();
		$(editGroupAccessName).hide();
		

		}
	else if(groupAccess_Edit){
		if( $("#groupAccessIdForEdit option:selected").val()!="-1" ){
			$("#selectedPartnerListErr").html('');
		getGroupAccessPartnersforUpdateGroupAccess();}
		
		$(editGroupAccessName).show();
		$(addGroupAccessName).hide();
				
}
}

function validateDropDownForGroupAccessFields(formId, eleId)
{
	
	var isValid = false;
	var eleVal = $("#" + formId + " #" + eleId).val();
	
	if (eleVal.length<=0 || eleVal < 0){
		generateAlert(formId, eleId, eleId + ".unselect");
		isValid = false;
	} else {
		clearError(formId + " #" + eleId);
		isValid = true;
	}
	return isValid;
}


function addGroupAccessSubmit(formId,event)
{
	var groupAccessName =validateGroupAccessFields(formId,"groupAccessName");	
	 $('#multiselect_to option').prop('selected', true);
	var options = $('#multiselect_to > option:selected');
    if(options.length == 0){
    	 generateAlert(formId, "multiselect_to","multiselect_to.multiSelect");
        return false;
    }
    else{
    	clearError("multiselect_to");
    }
 	if (groupAccessName ){
 		$("#"+formId).attr('action','saveGroupAccess');
 		$("#"+formId).submit();
 	}
 	else
 		{
 		return false;
 		}
 
} 


function multiSelectValidation(formId,event){
	var options = $('#multiselect_to > option:selected');
    if(options.length == 0){
    	 generateAlert(formId, "multiselect_to","multiselect_to.multiSelect");
        return false;
    }
    else{
    	clearError("multiselect_to");
    }
}



 	
function saveAssingGroupAccess(formId,event){
	
	var groupAccessId =validateDropDownForGroupAccessFields(formId,"groupAccessId");	
	var productId =validateDropDownForGroupAccessFields(formId,"productId");	
	var tableOptions = {
	        'bPaginate': true,
	        'iDisplayLength': 5
	    };
	var table = $('#tablePartnersRange');
	 table.DataTable().destroy()
     tableOptions.bPaginate = false;
	 table.DataTable(tableOptions);
	 
	if (groupAccessId && productId  ){		
	
	$("#assignGroupAccessId").attr('action','saveAssignGroupProduct')
 	$("#assignGroupAccessId").submit();
	}
 	else{
 		return false;
 	}
	
}
