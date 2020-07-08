
 
 /*To search partner*/
function searchPartner(){
	$("#feedBackTd").html('');
	$("#searchForm").attr('action','searchPartnerByName');
	$("#searchForm").submit();
}


/*To load partner add screen*/
function clickAdd(){
	 $("#feedBackTd").html('');
	 $("#searchForm").attr('action','showAddPartner')
	 $("#searchForm").submit();
}

/*To load partner edit screen*/
var rowidforcolor="";
function clickEditOrDelete(eleObj,action){

	
	$("#feedBackTd").html('');
	var rowID=$(eleObj).closest('tr').attr('id');
	
	rowidforcolor=rowID;
	/*$("#deletePartner #partnerId").val(rowID);*/
	
	if(action!=null && action.trim()!=''){
		if(action=='Edit'){
			$("#searchForm #partnerId").val(rowID);
			$("#searchForm").attr('action','showEditPartner');
			$("#searchForm").submit();
		}
		else if(action=='Delete'){
			   document.getElementById(rowID).style.background = "#85b3fc";
			//if (confirm("Do you want to delete record of "+$("#tableViewPartners #"+rowID+" td:nth-child(1)").text()+"?")) {
				$("#deletePartner #deletePartnerName").val($("#tableViewPartners #"+rowID+" td:nth-child(1)").text());
				$("#deletePartner #partnerId").val(rowID);
				$("#deletePartner #partnerName").val($("#searchPartnerByName #partnerName").val());
				$("#partnerNameDisp").html($("#deletePartner #deletePartnerName").val());
				//$("#deletePartner").attr('action','deletePartner');
				//$("#deletePartner").submit();
			//}
		}
	}
} 


function goToPrevious() {
	 document.getElementById(rowidforcolor).style.background = "#f9f9f9";

}

function goDeletePartner(){
	 $("#feedBackTd").html('');
	 $("#deletePartner").attr('action','deletePartner');
	 $("#deletePartner").submit();
}

function goUpdatePartner(){
	
	$('#purse_to option').prop('selected', true);
	var optionsPurse = $('#purse_to > option:selected');
	if(optionsPurse.length == 0){
    	 generateAlert(formId, "purse_to","partner.purse_to.noPurseSupports");
    	 }
    else{
    	clearError("purse_to");
    	}
    if (optionsPurse.length!=0){
     $("#feedBackTd").html('');
	 $("#editForm").attr('action','editPartner');
	 $("#editForm").submit();
    }
}
 
 /*Add/Update partner form submission*/
 function FormSubmit(formId,event){
	/* event.preventDefault();*/
	/* $("#feedBackTd").html('');*/
	 var validName=validateFields(formId,"partnerName"); 
	 var validDesc=validateFields(formId,"partnerDesc");
	 var validMdMiD=validateFields(formId,"partnerMdmId");
	 //var validSuppPurse=validateFields(formId,"purse");
	 $('#purse_to option').prop('selected', true);
		var optionsPurse = $('#purse_to > option:selected');
		if(optionsPurse.length == 0){
	    	 generateAlert(formId, "purse_to","partner.purse_to.noPurseSupports");
	    	 }
	    else{
	    	clearError("purse_to");
	    	}
	 if ( validName && validDesc && validMdMiD && optionsPurse.length>0){
		 
		if(formId=='editForm' && optionsPurse.length>0){
			$("#partnerNameDisp1").html($("#"+formId+" #partnerName").val());
			$("#edit_submit").attr("data-target","#define-constant-edit");
		
		}
		if(formId=='addForm'){
			$("#"+formId).attr('action','addPartner');
			$("#"+formId).submit();
		}
		
	 }else{
		 $("#edit_submit").removeAttr("data-target");
	 }
 }
 /*To reset add partner page*/
 function ResetAdd(formId){
	 	$("#feedBackTd").html('');
		$("#"+formId+" #partnerName").val('');
		$("#"+formId+" #partnerDesc").val('');
		$("#"+formId+" #partnerMdmId").val('');
		$("#"+formId+" input[name='partnerStatus']:checked").val('true')
		$("#"+formId+" #partnerStatusYes").parent().addClass('checked');
		$("#"+formId+" #partnerStatusNo").parent().removeClass('checked');
		clearError(formId+" #partnerName");
		clearError(formId+" #partnerDesc");
		clearError(formId+" #partnerMdmId");
 }
 /*To reset update partner page*/
 function resetUpdate(formId){
	
	
	clearError(formId+" #partnerName");
	clearError(formId+" #partnerDesc");
	clearError(formId+" #partnerMdmId");
	$("#feedBackTd").html('');
	
 }
 

	function clickViewPartner(eleObj,partnerId){

		$("#searchForm #partnerId").val(partnerId);
		
				$("#searchForm").attr('action','showViewPartner');
				$("#searchForm").submit();
			
		
		
	} 