function clickAddMerchant() {
	$("#merchantForm").attr('action', 'showAddMerchant');
	$("#merchantForm").submit();
}

function goAddMerchant(formId) {
	var validName = validateFields(formId, "merchantName");
	var validDesc = validateFields(formId, "merchantDesc");
	var validMdmId = validateMdmId(formId, "merchantMdmId");
	if (validName && validDesc && validMdmId) {
		$("#merchantForm").attr('action', 'addMerchant');
		$("#merchantForm").submit();
	}
}

function checkUpdateMerchant(formId){
	var validName = validateFields(formId, "merchantName");
	var validDesc = validateFields(formId, "merchantDesc");
	var validMdmId = validateMdmId(formId, "merchantMdmId");
	if (validName && validDesc && validMdmId) {
		return true;
	}
	else{
		return false;
	}
}

function searchMerchant(event) {
	event.preventDefault();

	var merchantName = "";
	var code = 'byName';

	merchantName = $("#merchantNameId").val();

	if (merchantName === undefined || merchantName == null
			|| merchantName.length <= 0 || merchantName == '') {
		var code = 'all';

		$("#searchType").val(code);
		$("#merchantForm").attr('action', 'showAllMerchants');
		$("#merchantForm").submit();
	} else {
		$("#searchType").val(code);
		$("#merchantForm").attr('action', 'searchMerchantByName');
		$("#merchantForm").submit();
	}
}

function ResetAddMerchant(formId) {

	$("#formErrorId").text('');
	$("#" + formId + " #merchantName").val('');
	$("#" + formId + " #merchantDesc").val('');
	$("#" + formId + " #merchantMdmId").val('');
	$("#statusMerchantAdd").html('');
	$("#formErrorId").html('');
	clearError(formId + " #merchantName");
	clearError(formId + " #merchantDesc");
	clearError(formId + " #merchantMdmId");
}

function goBackToMerchant() {
	$("#merchantForm").attr('action', 'merchantConfig');
	$("#merchantForm").submit();
}

function goBackToMerchantFromView(){
	
	$("#merchantForm").attr('action', 'merchantConfig');
	$("#merchantForm").submit();
	
}
function goConfirmMerchant(formId) {
		if(checkUpdateMerchant(formId)){
		$("#Button").attr("data-target","#define-constant-update");
		var id = $('#merchantID').val();
		var name = $('#merchantName').val();
		name = name.trim();
		$("#merchantIdtoUpdate").val(name);
		document.getElementById("merchantNameDisp").innerHTML = name;
		$("#merchantID").val(id);
		$("#editMerchantId").val(id);
		}
		else{
			 $("#Button").removeAttr("data-target");
		}
}

function goUpdateUser() {

	$("#merchantForm").attr('action', 'editMerchant');
	$("#merchantForm").submit();

}

function goEditMerchant(id) {
	var code = id;
	$("#merchantId").val(code);
	$("#merchantForm").attr('action', 'showEditMerchant');
	$("#merchantForm").submit();
}

function goViewMerchant(id){
	
	var code = id;
	$("#merchantId").val(code);
	$("#merchantForm").attr('action', 'showViewMerchant');
	$("#merchantForm").submit();
	
}

function validateMdmId(formId, eleId) {
	var isValid = true;
	var minLen = readValidationProp(eleId + '.min.length');
	var eleVal = $("#" + formId + " #" + eleId).val();
	clearError(formId + " #" + eleId);
	var pattern = new RegExp(readValidationProp(eleId + '.pattern'));

	if (eleVal != null && eleVal != '' && minLen != 0) {
		if ((eleVal.trim().length < minLen)
				|| (eleVal.trim().length > readValidationProp(eleId
						+ '.max.length'))) {
			generateAlert(formId, eleId, eleId + ".length");
			isValid = false;
		} else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) {
			generateAlert(formId, eleId, eleId + ".pattern");
			isValid = false;
		} else {
			clearError(formId + " #" + eleId);

		}
	}
	return isValid;
}

function goUpdateMerchant() {

	$("#merchantForm").attr('action', 'editMerchant');
	$("#merchantForm").submit();

}

var rowidforcolor = "";

function goConfirmDeleteMerchant(data) {
	rowidforcolor
	var datatosplit = "";
	datatosplit = data;
	datatosplit = datatosplit.replace(/\*/g, "'");

	var splitted = datatosplit.split('~');
	var id = splitted[0];
	var name = splitted[1];
	rowidforcolor = id;
	document.getElementById(id).style.background = "#85b3fc";
	name = name.trim();
	$("#merchantIdtoDelete").val(name);
	document.getElementById("merchantNameDisp").innerHTML = name;
	var nametosearch = $("#merchantNameId").val();
	$("#merchantId").val(id);
	$("#deletedName").val(name);
	$("#searchedName").val(nametosearch);

}

function deleteMerchant() {

	$("#merchantForm").attr('action', 'deleteMerchant');
	$("#merchantForm").submit();

}


