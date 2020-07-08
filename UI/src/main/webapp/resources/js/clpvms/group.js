
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



function clickEditForGroup(id){
	$("#groupId").val(id);
	$("#groupSearch").attr('action','editGroup');
 	$("#groupSearch").submit();
}

function clickViewForGroup(id){
	$("#groupId").val(id);
	$("#groupSearch").attr('action','viewGroup');
 	$("#groupSearch").submit();
}



var rowidforcolor="";
function deleteGroup(eleObj, id)
{
	
	var rowID=$(eleObj).closest('tr').attr('id');
	var val=rowID.split("-");
	console.log(rowID);
	rowidforcolor=rowID;
	 document.getElementById(rowID).style.background = "#85b3fc";
	 document.getElementById("groupId").value = id;
	document.getElementById("groupIdModelDelete").value = val[0];
	document.getElementById("groupModelDeleteName").value = val[1];

	
	document.getElementById("displayName").innerHTML = val[1];
	
}



function updateGroupStatus(eleObj, id)
{
	var rowID=$(eleObj).closest('tr').attr('id');
	
	console.log(rowID);
	rowidforcolor=rowID;
	 document.getElementById(rowID).style.background = "#85b3fc";
	 
	 document.getElementById("statusChangeDesc").value = "";
	document.getElementById("statusChangeDescReject").value = "";
		 
	 document.getElementById("groupIdModelReject").value = id;
	 document.getElementById("groupIdModel").value = id;
	 
	 clearDailogError();
}



function clearDailogError(){
	document.getElementById("dialogErrorApprove").innerHTML='';
	 	document.getElementById("dialogErrorReject").innerHTML='';
}

function goToPrevious() {
	 document.getElementById(rowidforcolor).style.background = "#f9f9f9";

}


function approveGroup() 
{
	if ($("#statusChangeDesc").val() != null && $("#statusChangeDesc").val().length != 0) 
	{
		$("#groupStatus").attr('action','groupStatusUpdate')
	 	$("#groupStatus").submit();
		$("#statusChangeDesc").next().html('');
	} else 
	{
		$("#statusChangeDesc").next().html('');
		 document.getElementById("dialogErrorApprove").innerHTML='<font color="red">'+readMessage("statusChangeDesc.empty")+"</font>";
		
		
	}

}




function rejectGroup() {
	if ($("#statusChangeDescReject").val() != null && $("#statusChangeDescReject").val().length != 0) 
	{
		$("#groupStatusReject").attr('action','groupStatusUpdate')
	 	$("#groupStatusReject").submit();
		$("#statusChangeDescReject").next().html('');
	} else 
	{
		$("#statusChangeDescReject").next().html('');
		 document.getElementById("dialogErrorReject").innerHTML='<font color="red">'+readMessage("statusChangeDesc.empty")+"</font>";
	}
}





function deleteGroupDet() 
{
		$("#groupSearch").attr('action','deleteGroup');
	 	$("#groupSearch").submit();
}


function editGroup(eleObj,id)
{
	$("#groupSearch #groupId").val(id);
	$("#groupSearch").attr('action','editGroup');
	$("#groupSearch").submit();
} 	



function validateFieldsGroup(formId,eleId){
	 
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



function validateGroupNameSearch(formId,eleId){
	 
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
	




function addGroup()
{
	$("#groupSearch").attr('action','addGroup');
	$("#groupSearch").submit();

}

function formSubmitForAdd(formId,event)
{
	var groupName =validateFieldsGroup(formId,"groupName");	
	
	 $('#multiselect_to option').prop('selected', true);
		var options = $('#multiselect_to > option:selected');
	    if(options.length == 0){
	    	 generateAlert(formId, "multiselect_to","multiselect_to.unSelect");
	        return false;
	    }
	    else{
	    	clearError("multiselect_to");
	    }
  
 	if (groupName ){	 
 		$("#"+formId).attr('action','saveGroup');
 		$("#"+formId).submit();
 	}
 	else
 		{
 		return false;
 		} 
} 







function goResetAddGroup() {
	
	clearError("groupName");
	document.getElementById('multiselect_leftAll').click();
	$("#selectedRoleListErr").html('');
	clearError("multiselect_to");	
	$("#groupName").val('');
	$("#serviceErrMsg").html('');
	
}






function backAddToSearchGroupConfig(){
	
	$("#addGroup").attr('action','groupConfig');
	$("#addGroup").submit();
}

function backEditToSearchGroupConfig(){
	
	$("#updateGroup").attr('action','groupConfig');
	$("#updateGroup").submit();
}


function goUpdateForGroup(){
	
	$("#updateGroup").attr('action','updateGroup');
	$("#updateGroup").submit();
}







function validateDropDownForGroupFields(formId, eleId)
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

/**
 * all selected partners based on group accessid
 * @returns
 */

function getGroupRolesForUpdateGroup(){
	
	$("#multiselect_to").html("");
	
	var groupId=$("#groupNameForEdit option:selected").val();	
	var str = $('#srvUrl').val()+"/ajax/getRoleList/"+groupId;  
	
	 $.ajax({
	     url: str,

	      async:true,

	      type: "GET",

	      dataType: 'json',

	     /* crossDomain: true,*/

	      success:function(response, status, xhr){
	    	  
	    	//  console.log(response.groupId+'  '+response.groupName);
	    	  
	    	  $.each(response.roles, function(index){
	    		  
	    		 if(response.roles[index].roleName!=null && response.roles[index].roleName!=""){
	    			 $("#multiselect_to").append('<option value="'+response.roles[index].roleId+'" >'
		        			  +response.roles[index].roleName+'</option>');
		    		  
		    		 // console.log(response.roles[index].roleId+'   '+response.roles[index].roleName);
		    		 	    		  
		    		 $("#multiselect option[value="+response.roles[index].roleId+"]").remove();
	    		 }
	    		  

	          }); 
	      }
	    });
}


function updateGroupSubmit(){
	

	 $('#multiselect_to option').prop('selected', true);
		var options = $('#multiselect_to > option:selected');
	
	document.getElementById("groupNameDisp").innerHTML= $("#groupNameForEdit").find("option:selected").text();
	
	
	
	var groupNameForEdit =validateDropDownForGroupFields("updateGroup","groupNameForEdit");
	
	if (groupNameForEdit  && options.length != 0 ){
		
		$("#editGroupButton").attr('data-target','#define-constant-edit');
		return true;
	}
	
 	else{
 		
 		if(options.length != 0){
			clearError("multiselect_to");
		}
		else{
			generateAlert("updateGroup", "multiselect_to","multiselect_to.unSelect");
			return false;
		}
		
	}
	
}


function multiSelectValidationforGroup(formId,event){
	var options = $('#multiselect_to > option:selected');
    if(options.length == 0){
    	 generateAlert(formId, "multiselect_to","multiselect_to.isEmptyInGroup");
        return false;
    }
    else{
    	clearError("multiselect_to");
    }
}





