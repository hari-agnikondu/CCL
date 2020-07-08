/**
 * 
 */

function clickAddCustomerProfile(eleObj) {
	var rowID=$(eleObj).closest('tr').attr('id');
	$("#accountNumber_hidden").val($("#"+rowID+" td:nth-child(2)").text());
	$("#proxyNumber_hidden").val($("#"+rowID+" td:nth-child(3)").text());
	$("#customerProfileConfigForm").attr('action','showAddCustomerProfile');
	$("#customerProfileConfigForm").submit();
}	

function clickEditCustomerProfile(eleObj)
{
	var rowID=$(eleObj).closest('tr').attr('id');
	$("#profileId_hidden").val($("#"+rowID+" td:nth-child(1)").text());
	$("#customerProfileConfigForm").attr('action','showEditCustomerProfile');
	$("#customerProfileConfigForm").submit();
	
	}

function updateCustomerProfile(formId,action)
{
	$("#"+formId).attr('action',action);
	$("#"+formId).submit();
	}

function setActionAttrOfForm(formId,action)
{
	$("#"+formId).attr('action',action);
	$("#"+formId).submit();
	}



function saveCustomerProfile(formId){
	
	
		$("#"+formId).attr('action','addCustomerProfile');
		$("#"+formId).submit();
	
}

/*delete customer profile*/
var rowidforcolor="";
function clickDelete(eleObj,formId){
	$("#feedBackTd").html('');
	var rowID=$(eleObj).closest('tr').attr('id');
	rowidforcolor=rowID;
	if(formId!=null && formId.trim()!=''){
			$("#accountNumberDisp").html($("#"+rowID+" td:nth-child(2)").text());
			$("#profileId").val($("#"+rowID+" td:nth-child(1)").text());
			$(eleObj).closest('tr').css("background-color","#85b3fc");
		
	}
}

function goToPrevious(eleObj) {
	document.getElementById(rowidforcolor).style.background = "#f9f9f9";
}

/*delete customer profile ends*/
/*search customer profile*/
function searchCustomerProfile(formId,action)
{
	if($("#accountNumber").val()!=''||$("#cardNumber").val()!=''||$("#proxyNumber").val()!='')
	{
	$("#feedBackTdError").html('');
	$("#"+formId).attr('action',action);
	$("#"+formId).submit();
	}
	else
	{
		var text=readMessage('customerProfileSearch.empty');
		$("#feedBackTdError").html(text);
		return false;
	}
}
/*search customer profile ends*/
/*Copy customer profile */
function getCustomerProfileDetails(form,action) {
	var type=$("#spnumbertype").val();
	var test=$("#parentCardData").val();
	if(type==-1){
         document.getElementById("parentCardError").innerHTML='<font color="red">'+readMessage("customerProfileSpnumbertype.empty")+"</font>";
        return false;
    }
	else if(test=='' || test==null)
	{
		document.getElementById("parentCardError").innerHTML='<font color="red">'+readMessage("customerProfileParentcarddata.empty")+"</font>";
        return false;
	}
	else
		{
		clearError("parentCardError");    
	    $("#"+form).attr('action', action)
	    $("#"+form).submit();
		}


}

/*Copy customer profile ends*/


function viewCard(eleObj, formId) {
	$("#feedBackTd").html('');
	var rowID = $(eleObj).closest('tr').attr('id');
	var url_viewpage = $("#url_viewpage").val();
	rowidforcolor = rowID;
	if (formId != null && formId.trim() != '') {
		$("#accountNumber_hidden").val(
				$("#" + rowID + " td:nth-child(2)").text());
		var accNum = $("#accountNumber_hidden").val();
		url_viewpage = url_viewpage
				+ "/customerProfile/viewCard?accountNumber_hidden=" + accNum;
		$('#load1').load(url_viewpage, function() {
			$('#define-constant-viewcard').modal({
				show : true
			});
		});

	}

}


function viewCard_add_edit()
{
	
var accNumber=$("#accountNumber").val();
var url_viewpage = $("#url_viewpage").val();
url_viewpage =url_viewpage+"/customerProfile/viewCard?accountNumber_hidden=" + accNumber;
$('#load1').load(url_viewpage, function() {
$('#define-constant-viewcard').modal({
show : true
});
});




}

