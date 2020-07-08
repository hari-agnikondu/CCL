function FormSubmit(formId){
	var isvalid = validateFields(formId,"roleName");
	if($("input[type=checkbox]:checked").length<1){
		$("#tableErr").html("Please Permit To Access Atleast One Menu");
		isvalid=false;
	}else{
		$("#tableErr").html("");
	}

	if(isvalid){
		if(formId=='addForm'){
			$("#"+formId).attr('action','AddRole');
			$("#"+formId).submit();
		}else{
			$("#"+formId).attr('action','EditRole');
			$("#"+formId).submit();
		}
		//$("#"+formId).submit();
	}
	return 
}


/*To load role edit screen*/
var rowidforcolor="";
function clickEditOrDelete(eleObj,formId){
	$("#feedBackTd").html('');
	var rowID=$(eleObj).closest('tr').attr('id');
	rowidforcolor=rowID;
	if(formId!=null && formId.trim()!=''){
		if(formId=='roleForm'){
			$("#"+formId+" #roleId").val(rowID);
			$("#"+formId).attr('action','showEditRole');
			$("#"+formId).submit();
			return true;
		}
		else{
			$("#roleNameDisp").html($("#"+rowID+" td:nth-child(1)").text());
			$("#"+formId+" #roleId").val(rowID);
			$(eleObj).closest('tr').css("background-color","#85b3fc");
		}
	}
} 

function clickViewToSearch(eleObj,formId){
	$("#feedBackTd").html('');
	var rowID=$(eleObj).closest('tr').attr('id');
	rowidforcolor=rowID;
	if(formId!=null && formId.trim()!=''){
		if(formId=='roleForm'){
			$("#"+formId+" #roleId").val(rowID);
			$("#"+formId).attr('action','showViewRole');
			$("#"+formId).submit();
			return true;
		}
	}
} 


function goToPrevious() {
	$("#dialogErrorApprove").html('');
	$("#dialogErrorReject").html('');
	$("#roleStatusApprove #statusChangeDesc").val('');
	$("#roleStatusReject #statusChangeDesc").val('');
	document.getElementById(rowidforcolor).style.background = "#f9f9f9";
}
function ResetAdd(){
	$("#roleName").val('');
	$("#roleDesc").val('');
	$("input[type='checkbox']").prop('checked',false);
	$("#tableErr").html("");
	clearError("roleName");
	clearError("roleDesc");
}

function goDeleteRole(){
	$("#deleterole").attr('action','deleteRole');
	$("#deleterole").submit();
}

function goApproveRejectRole(formId){
	if($("#"+formId+" #statusChangeDesc").val()!=null && $("#"+formId+" #statusChangeDesc").val().trim()!=""){
		$("#dialogErrorApprove").html('');
		$("#dialogErrorReject").html('');
		$("#"+formId).attr('action','approveRejectRole');
		$("#"+formId).submit();
	}else{
		$("#dialogErrorApprove").html('Please enter Remarks');
		$("#dialogErrorReject").html('Please enter Remarks');
	}
}

function callAddPage(){
	
	 $("#feedBackTd").html('');
	 $("#roleForm").attr('action','showAddRole')
	 $("#roleForm").submit();

}