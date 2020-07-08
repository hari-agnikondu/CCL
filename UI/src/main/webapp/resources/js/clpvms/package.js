// PACKAGE

function showAddPackage() {
	$("#packageForm").attr('action', 'showAddPackage');
	$("#packageForm").submit();
}

function addPackage() {
	$("#addPackageForm").attr('action', 'addPackage');
	$("#addPackageForm").submit();
}

function goBackToPackage() {
	$("#addPackageForm").attr('action', 'packageIDConfig');
	$("#addPackageForm").submit();
}

function searchPackage(event) {
	event.preventDefault();
	var packageName = "";
	var code = 'byNameID';

	packageName = $("#description").val();

	if (packageName === undefined || packageName == null
			|| packageName.length <= 0 || packageName == '') {
		var code = 'all';
		$("#searchType").val(code);
		$("#packageForm").attr('action', 'showAllPackages');
		$("#packageForm").submit();
	} else {
		$("#searchType").val(code);
		$("#packageForm").attr('action', 'showAllPackagesByName');
		$("#packageForm").submit();
	}
}

function editPackage(id) {
	var code = id;
	$("#packageId").val(code);
	$("#packageForm").attr('action', 'showEditPackage');
	$("#packageForm").submit();
}

function viewPackage(id) {
	
	var code = id;
	$("#packageId").val(code);
	$("#packageForm").attr('action', 'showViewPackage');
	$("#packageForm").submit();
}

function goBackToEditPackage()
{
	$("#editPackageForm").attr('action', 'packageIDConfig');
	$("#editPackageForm").submit();
}

function goBackToPackageFromView(){
	
	$("#viewPackageForm").attr('action', 'packageIDConfig');
	$("#viewPackageForm").submit();
	
}

function updatePackage() {
	$("#editPackageForm").attr('action', 'updatePackage');
	$("#editPackageForm").submit();
}

function ResetAddPackage(formId) {
	$("#" + formId + " #packageId").val('');
	$("#" + formId + " #description").val('');
	$("#" + formId + " #carrierId").val('');
	$("#" + formId + " #logoId").val('');
	$("#" + formId + " #embossLine3").val('');
	$("#" + formId + " #embossLine4").val('');
	$("#" + formId + " #envelopeId").val('');
	$("#" + formId + " #envelopeSealed").val('');
	$("#" + formId + " #insertId").val('');
	$("#" + formId + " #activationStickerId").val('');
	$("#" + formId + " #thermalPrintColorId").val('');
	$("#" + formId + " #cardPrintVersionId").val('');
	$("#" + formId + " #packingSlipId").val('');
	$("#" + formId + " #bundleSize").val('');
	$("#" + formId + " #replacementPackageId").val('');
	$("#" + formId + " #fulfillmentId").val('');
	$('#shipId').html('');
	$('#statusPackageId').html('');
	$('#parentPackageIdError').html('');
	$('select option[value="-1"]').prop("selected",true);
	var elements = document.getElementById("shipMethods").options;

    for(var i = 0; i < elements.length; i++){
      elements[i].selected = false;
    }
   /* $('#replacementPackageId').html('');
    $('#fulfillmentId').html('');*/
    
  
    $("#replacementshipMethods option:selected").prop("selected", false);
    $("#expRepshipMethods options:selected").prop("selected",false);
	clearError(formId + " #packageId");
	clearError(formId + " #description");
	clearError(formId + " #carrierId");
	clearError(formId + " #logoId");
	clearError(formId + " #embossLine3");
	clearError(formId + " #embossLine4");
	clearError(formId + " #envelopeSealed");
	clearError(formId + " #insertId");
	clearError(formId + " #activationStickerId");
	clearError(formId + " #thermalPrintColorId");
	clearError(formId + " #cardPrintVersionId");
	clearError(formId + " #packingSlipId");
	clearError(formId + " #fulfillmentId");
	clearError(formId + " #replacementPackageId");
	clearError(formId + " #fulfillmentMethod");
	clearError(formId + " #bundleSize");
	
	
	
	
	
}
function isAlphaNumericWithSpacePackage(ctrl) {
	var isValid;
 	var regex = /^[a-zA-Z0-9]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9]/g, '');
	
	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	
	
	return isValid;
}

function isAlphaNumericPackage(ctrl) {
	var isValid;
 	var regex = /^[a-zA-Z0-9 ]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ]/g, '');
	
	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	
	
	return isValid;
}




function getPackageDetails() {

	var test=$("#comboboxparentPackageId").val();
	if(test==''){
        document.getElementById("parentPackageIdError").innerHTML='<font color="red">'+readMessage("parentPackageIdErrorCopy.empty")+"</font>";
       return false;
   }
   clearError("parentProductIdError");    
	clearError("parentPackageIdError");	
	$("#addPackageForm").attr('action', 'getPackageDetails')
	$("#addPackageForm").submit();

}