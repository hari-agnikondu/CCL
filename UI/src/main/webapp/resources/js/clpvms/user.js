/// VALIDATIONS FOR SEARCH user SCREEN STARTS HERE
 
function clickAddUser(event) {
    event.preventDefault();
    $("#userForm").attr('action', 'showAddUser');
    $("#userForm").submit();
}
 
/* To search user based on name or to show all user*/
function searchUser(event) {
    event.preventDefault();
 
    var userName = "";
    var code = 'byName';
 
    /*userName=$("#userNameId").val();
    
    
    if(userName === undefined || userName == null || 
            userName.length <= 0 || userName=='')
    {
        var code='all';
        
        $("#searchType").val(code);
        $("#userForm").attr('action','showAllUsers');
        $("#userForm").submit();
    }    
    else*/
 
    $("#userForm").attr('action', 'showAllUsers');
    $("#userForm").submit();
 
}
 
function ResetAddIssuer(formId) {
    $("#" + formId + " #issuerName").val('');
    $("#" + formId + " #issuerDesc").val('');
    $("#" + formId + " #issuerMdmId").val('');
    clearError(formId + " #issuerName");
    clearError(formId + " #issuerDesc");
    clearError(formId + " #issuerMdmId");
}
 
function clickAddCardRange() {
    $("#issuerForm").attr('action', 'cardRangeRegistration');
    $("#issuerForm").submit();
 
}
 
function goBack() {
    $("#issuerForm").attr('action', 'issuerConfig');
    $("#issuerForm").submit();
}
 
function goEditUser(id) {
 
    var code = id;
    $("#userId").val(code);
    $("#userForm").attr('action', 'showEditUser');
    $("#userForm").submit();
}
 
function goViewUser(id) {
	 
    var code = id;
    $("#userId").val(code);
    $("#userForm").attr('action', 'showViewUser');
    $("#userForm").submit();
}
 
function goConfirmUser() {
    
    
         $('#multiselect_to option').prop('selected', true);
        var options = $('#multiselect_to > option:selected');
        if(options.length == 0){
             generateAlert(formId, "multiselect_to","multiselect_to.user");
            return false;
        }
        else{
            clearError("multiselect_to");
        }
    
 
    var id = $('#userID').val();
    var name = $('#userNameId').val();
    name = name.trim();
    $("#userIdtoUpdate").val(name);
    document.getElementById("userNameDisp").innerHTML = name;
    $("#userID").val(id);
    $("#editUserId").val(id);
 
}
 
var rowidforcolor = "";
 
/*
function goDelete(data) {
rowidforcolor
var datatosplit="";*/
//datatosplit = data;
//datatosplit=datatosplit.replace(/\*/g,"'");
/*    var splitted = datatosplit.split('~');
var id=splitted[0];
var name=splitted[1];
rowidforcolor=id;    
document.getElementById(id).style.background = "#85b3fc";
name=name.trim();
$("#issuerIdtoDelete").val(name);
document.getElementById("issuerNameDisp").innerHTML =name;
var nametosearch=$("#issuerNameId").val();
$("#issuerId").val(id);
$("#deletedName").val(name);
$("#searchedName").val(nametosearch);     
}*/
 
function goToPrevious() {
	
    document.getElementById(rowidforcolor).style.background = "#f9f9f9";
    clearError("userCheckerRemarks");
    clearError("userCheckerRemarks");
    
    /*clearError("userCheckerRemarks");*/
 
}
 
function deleteIssuer() {
 
    $("#issuerForm").attr('action', 'deleteIssuer');
    $("#issuerForm").submit();
}
 
function goBackToIssuer() {
 
    $("#RegIssuerForm").attr('action', 'issuerConfig');
    $("#RegIssuerForm").submit();
}
 
 
 
 
function goAddUser(formId) {
    /*event.preventDefault();*/
	
    var isvalidLdap = validateLdapFields(formId, "userLoginId");
    
         $('#multiselect_to option').prop('selected', true);
        var options = $('#multiselect_to > option:selected');
        if(options.length == 0){
             generateAlert(formId, "multiselect_to","multiselect_to.user");
            return false;
        }
        else{
            clearError("multiselect_to");
        }
    
/*    var isvalidUserName = validateuserName(formId, "userNameId");
    var isvalidEmail = validateEmail(formId, "emailId");
    var isvalidPhone = validatewithmaxLength(formId, "contactId");*/
        
    if (isvalidLdap) {
    	var flag=$("#flag").val();
    	if(flag){
        $("#userForm").attr('action', 'addUser');
        $("#userForm").submit();
        return true;
    	}
    	else{
    		generateAlert(formId, "userLoginId","userLoginId.fetch");
    		return false;
    	}
    }
    else
        {
        return false;
        } 
	}
	
    /*$("#userForm").attr('action','addUser');
    $("#userForm").submit();*/
 
function goBacktoIssuerfromEdit() {
 
    $("#EditIssuerForm").attr('action', 'issuerConfig');
    $("#EditIssuerForm").submit();
}
 
function goUpdateUser() {
 
    $("#userForm").attr('action', 'editUser');
    $("#userForm").submit();
 
}
function goBackToUser() {
    $("#userForm").attr('action', 'userConfig');
    $("#userForm").submit();
}
 
function ResetAddUser(formId) {
    $("#userNameId").val('');
    $("#userLoginId").val('');
    $("#emailId").val('');
    $("#contactId").val('');
    $("#multiselect_to").val('');
    document.getElementById('multiselect_leftAll').click();
    clearError("userNameId");
    clearError("userLoginId");
    clearError("emailId");
    clearError("contactId");
    clearError("multiselect_to");
    $("#multiselect_to_Error").html('');
    $("#fstatus").html('');
    $("#sstatus").html('');
 
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
    
    /*var isValid = false;
    var regex = /[a-zA-Z0-9 ,.;'\-_]+$/;
    
    if (regex.test(ctrl.value)) {
        ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ,.;'\-_]+$/, "");
    }
    
    if ((ctrl.value.charAt(0) == ' '))
        ctrl.value = ctrl.value.replace(/\s+/, '');*/
    
    /*isValid = regex.test($("#" + ctrl.id + "").val());
    ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ,.;'\-_]+$/, '');
    if (ctrl.value.startsWith(' '))
        ctrl.value = ctrl.value.replace(/\s+/, '');
    return isValid;*/
}
 
 
 
 
/*To validate AlphaNumeric value, it replaces the characters other than alphabets and numbers by ''*/
function isAlphaNumeric(ctrl) {
    var isValid;
    var regex = /^[a-zA-Z0-9 ]*$/;
    isValid = regex.test($("#" + ctrl.id + "").val());
    ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ]/g, '');
    return isValid;
}
 
/*To clear common error */
function clearError(ctrl) {
    $("#" + ctrl + "").next().html('');
}
/*To display error messages for client side validation*/
function generateAlert(formId, eleId, key) {
 
    var text = readMessage(key);
    clearError(formId + " #" + eleId);
    $("#" + formId + " #" + eleId).after(
            '<span class="error_empty" style="color:red;"><br/>' + text
                    + '</span>');
}
 
/*To clear error messages of client side validation
*/
function clearFieldError(formId, eleId) {
    clearError(formId + " #" + eleId);
}
 
function validateFields(formId, eleId) {
 
    var isValid = false;
    var minLen = readValidationProp(eleId + '.min.length');
    var eleVal = $("#" + formId + " #" + eleId).val();
    clearError(formId + " #" + eleId);
    var pattern = new RegExp(readValidationProp(eleId + '.pattern'));
    if ((eleVal == null || eleVal == '') && minLen != 0) {
        generateAlert(formId, eleId, eleId + ".empty");
        return isValid;
    }
    if ((eleVal.trim().length < minLen)
            || (eleVal.trim().length > readValidationProp(eleId + '.max.length'))) {
        generateAlert(formId, eleId, eleId + ".length");
    } else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) { //
        generateAlert(formId, eleId, eleId + ".pattern");
    } else {
        clearError(formId + " #" + eleId);
 
        isValid = true;
    }
 
    return isValid;
 
}
 
function validateMdmId(formId, eleId) {
 
    var isValid = false;
    var minLen = readValidationProp(eleId + '.min.length');
    var eleVal = $("#" + formId + " #" + eleId).val();
    clearError(formId + " #" + eleId);
    var pattern = new RegExp(readValidationProp(eleId + '.pattern'));
    /*     if((eleVal==null ||eleVal=='')  && minLen!=0){ 
            generateAlert(formId, eleId,eleId+".empty");
            return isValid;
         }    */
    if (eleVal != null && eleVal != '' && minLen != 0) {
 
        if ((eleVal.trim().length < minLen)
                || (eleVal.trim().length > readValidationProp(eleId
                        + '.max.length'))) {
 
            generateAlert(formId, eleId, eleId + ".length");
        } else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) { //
            generateAlert(formId, eleId, eleId + ".pattern");
        } else {
            clearError(formId + " #" + eleId);
 
            isValid = true;
        }
    }
 
    return isValid;
 
}
 
/**
* for alphabetic validation
*/
function isAlphabetic(ctrl) {
    var isValid;
    var regex = /^[a-zA-Z ]*$/;
    isValid = regex.test($("#" + ctrl.id + "").val());
    ctrl.value = ctrl.value.replace(/[^a-zA-Z ]/g, '');
    return isValid;
}
/**
* for max length validation in search screen
*/
function validatewithmaxLength(formId, eleId) {
    var isValid = false;
    clearError(formId + " #" + eleId);
    var eleVal = $("#" + formId + " #" + eleId).val();
    clearError(formId + " #" + eleId);
 
    if (eleVal.trim().length > readValidationProp(eleId + '.max.length')) {
        generateAlert(formId, eleId, eleId + ".length");
        return isValid;
    } else {
        clearError(formId + " #" + eleId);
 
        isValid = true;
    }
 
    return isValid;
 
}
/**
* ldap validation
*/
 
function validateLdapFields(formId, eleId) {
 
    var isValid = false;
    var minLen = readValidationProp(eleId + '.min.length');
    var eleVal = $("#" + formId + " #" + eleId).val();
    clearError(formId + " #" + eleId);
    var pattern = new RegExp(readValidationProp(eleId + '.pattern'));
    if ((eleVal == null || eleVal == '') && minLen != 0) {
        generateAlert(formId, eleId, eleId + ".empty");
        return isValid;
    }
    if ((eleVal.trim().length < minLen)
            || (eleVal.trim().length > readValidationProp(eleId + '.max.length'))) {
        generateAlert(formId, eleId, eleId + ".length");
    } else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) { //
        generateAlert(formId, eleId, eleId + ".pattern");
    } else {
        clearError(formId + " #" + eleId);
 
        isValid = true;
    }
 
    return isValid;
 
}
/**
* validating email fields
*/
 
function validateEmail(formId, eleId/*, event*/) {
    /*event.preventDefault();*/
    var isValid = false;
    clearError(formId + " #" + eleId);
    var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    clearError(formId + " #" + eleId);
    var eleVal = $("#" + formId + " #" + eleId).val();
    clearError(formId + " #" + eleId);
 
    if ((eleVal != null && eleVal != '') && !regex.test(eleVal)) {
        generateAlert(formId, eleId, eleId + ".pattern");
 
        return isValid;
    } else if (eleVal.trim().length > readValidationProp(eleId + '.max.length')) {
 
        generateAlert(formId, eleId, eleId + ".length");
    } else {
        clearError(formId + " #" + eleId);
        isValid = true;
 
    }
    return isValid;
}
 
function isNumeric(ctrl) {
    var isValid;
    var regex = /^[0-9]*$/;
    isValid = regex.test($("#" + ctrl.id + "").val());
    ctrl.value = ctrl.value.replace(/[^0-9]/g, '');
    if ((ctrl.value.startsWith(' ')) || (ctrl.value.endsWith(' ')))
        ctrl.value = ctrl.value.replace(/\s+/, '');
    return isValid;
}
function multiselct(formId, eleId) {
    var isValid = false;
    var eleVal = $("#" + formId + " #" + eleId).val();
    clearError(formId + " #" + eleId);
    if ((eleVal == null || eleVal == '')) {
        generateAlert(formId, eleId, eleId + ".empty");
        return isValid;
    } else {
        clearError(formId + " #" + eleId);
        isValid = true;
 
    }
    return isValid;
 
}
 
function validateuserName(formId, eleId) {
 
    var isValid = false;
    var eleVal = $("#" + formId + " #" + eleId).val();
    clearError(formId + " #" + eleId);
    var pattern = new RegExp(readValidationProp(eleId + '.pattern'));
 
    if (eleVal != null && eleVal != '') {
 
        if (eleVal.trim().length > readValidationProp(eleId + '.max.length')) {
 
            generateAlert(formId, eleId, eleId + ".length");
        } else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) {
            generateAlert(formId, eleId, eleId + ".pattern");
        } else {
            clearError(formId + " #" + eleId);
 
            isValid = true;
        }
    }
 
    return isValid;
 
}
 
/**
* for alphanumeric validation userloginid
*/
function isAlphaNumericUserLoginId(ctrl) {
	 var invalidChars = /[^a-zA-Z0-9 ,.;'\-_]/gi; 
	   
		if (invalidChars.test(ctrl.value)) {
			ctrl.value = ctrl.value.replace(invalidChars, "");
		}

		if ((ctrl.value.charAt(0) == ' '))
			ctrl.value = ctrl.value.replace(/\s+/, '');
	
    /*var isValid = false;
    var regex = /[a-zA-Z0-9.\-_]+$/;
    isValid = regex.test($("#" + ctrl.id + "").val());
    ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9.\-_]+$/, '');
    if (ctrl.value.startsWith(' '))
        ctrl.value = ctrl.value.replace(/\s+/, '');
    return isValid;*/
}
/**
* username onkeyup validation
*/
 
function isAlphaNumericWithSpaceUserName(ctrl) {
/*    var isValid = false;
    var regex = /[a-zA-Z0-9 ,.;'\-_]+$/;
    isValid = regex.test($("#" + ctrl.id + "").val());
    ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ,.;'\-_]+$/, '');
    if (ctrl.value.startsWith(' '))
        ctrl.value = ctrl.value.replace(/\s+/, '');
    return isValid;*/
	 var invalidChars = /[^a-zA-Z0-9 ,.;'\-_]/gi; 
	   
		if (invalidChars.test(ctrl.value)) {
			ctrl.value = ctrl.value.replace(invalidChars, "");
		}

		if ((ctrl.value.charAt(0) == ' '))
			ctrl.value = ctrl.value.replace(/\s+/, '');
}
 
function validateUserRemarks(formId, eleId) {
    var isValid = false;
    var eleVal = $("#" + formId + " #" + eleId).val().trim();
   // var pattern = new RegExp(readValidationProp(eleId + '.pattern'));
    clearError(formId + " #" + eleId);
 
    if ((eleVal == null || eleVal == '' || eleVal.length == 0)) {
        generateAlert(formId, eleId, eleId + ".empty");
    }/* else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) {
        generateAlert(formId, eleId, eleId + ".pattern");
        return isValid;
    }*/
 
    else {
        clearError(formId + " #" + eleId);
        isValid = true;
    }
    return isValid;
 
}
 
function goConfirmApproveUser(data) {
 
    var datatosplit = "";
    datatosplit = data;
    datatosplit = datatosplit.replace(/\*/g, "'");
 
    var splitted = datatosplit.split('~');
    var id = splitted[0];
    var name = splitted[1];
    rowidforcolor = id;
    document.getElementById(id).style.background = "#85b3fc";
    name = name.trim();
 
    $("#userIdtoApprove").val(id);
    $("#userNametoApprove").val(name);
 
    
}
 
function goApproveUser(formId) {
 
    var userCheckerRemarks=$("#userCheckerRemarks").val().trim();
    $("#userCheckerRemarks").val(userCheckerRemarks);
    
    var isvalidRemarks = validateUserRemarks(formId, "userCheckerRemarks");
    
    if(isvalidRemarks)
        {
    $("#userApproveForm").attr('action', 'approveUser');
    $("#userApproveForm").submit();
        }
 
}
 
function goConfirmRejectUser(data) {
    
 
    var datatosplit = "";
    datatosplit = data;
    datatosplit = datatosplit.replace(/\*/g, "'");
 
    var splitted = datatosplit.split('~');
    var id = splitted[0];
    var name = splitted[1];
    rowidforcolor = id;
    document.getElementById(id).style.background = "#85b3fc";
    name = name.trim();
 
    $("#userIdtoReject").val(id);
    $("#userNametoReject").val(name);
 
        
}
 
function goRejectUser(formId) {
 
	var userCheckerRemarks=$("#userCheckerRemarks").val().trim();
    $("#userCheckerRemarks").val(userCheckerRemarks);
   
    var isvalidRemarks = validateUserRemarks(formId, "userCheckerRemarks");
    
    if(isvalidRemarks)
        {
    $("#userRejectForm").attr('action', 'rejectUser');
    $("#userRejectForm").submit();
        }
}
 
function goConfirmDeleteUser(data) {
    var datatosplit = "";
    datatosplit = data;
    datatosplit = datatosplit.replace(/\*/g, "'");
 
    var splitted = datatosplit.split('~');
    var id = splitted[0];
    var name = splitted[1];
    rowidforcolor = id;
    document.getElementById(id).style.background = "#85b3fc";
    name = name.trim();
 
    $("#userIdtoDelete").val(id);
    $("#userNametoDelete").val(name);
    document.getElementById("userNameDisp").innerHTML = name;
    //$("#userNameDisp").val(name);
 
}
 
function goDeleteUser() {
 
    $("#deleteUserForm").attr('action', 'deleteUser');
    $("#deleteUserForm").submit();
 
}
 
//// for add user
 
function getLdapData(event) {
 
    event.preventDefault();
    var ldapId = "";
    /*var ldapClicked=true;
    $("#flag").val(ldapClicked);*/
    
    ldapId = $('#userLoginId').val();
 
    if (ldapId == '' || ldapId == null) {
        generateAlert("userForm", "userLoginId", "userLoginId" + ".empty");
        return false;
 
    } else {
        $("#userForm").attr('action', 'getUserDetails');
        $("#userForm").submit();
 
    }
   
}




function multiSelectValidationUser(formId,event){
    var options = $('#multiselect_to > option:selected');
    if(options.length == 0){
         generateAlert(formId, "multiselect_to","multiselect_to.user");
        return false;
    }
    else{
        clearError("multiselect_to");
    }
}

function goConfirmDeactiveUser(data) {

	var datatosplit = "";
	datatosplit = data;
	datatosplit = datatosplit.replace(/\*/g, "'");

	var splitted = datatosplit.split('~');
	var id = splitted[0];
	var name = splitted[1];
	rowidforcolor = id;
	document.getElementById(id).style.background = "#85b3fc";
	name = name.trim();

	$("#userIdtoDeactive").val(id);
	$("#userNametoDeactive").val(name);
}

function goDeactiveUser(formId) {

	var userCheckerRemarks = $("#userCheckerRemarks").val().trim();
	$("#userCheckerRemarks").val(userCheckerRemarks);
	var isvalidRemarks = validateUserRemarks(formId, "userCheckerRemarks");

	if (isvalidRemarks) {
		$("#userDeactiveForm").attr('action', 'updateAccessStatus');
		$("#userDeactiveForm").submit();
	}
}

function goConfirmActiveUser(data) {

	var datatosplit = "";
	datatosplit = data;
	datatosplit = datatosplit.replace(/\*/g, "'");

	var splitted = datatosplit.split('~');
	var id = splitted[0];
	var name = splitted[1];
	rowidforcolor = id;
	document.getElementById(id).style.background = "#85b3fc";
	name = name.trim();

	$("#userIdtoActive").val(id);
	$("#userNametoActive").val(name);
}

function goActiveUser(formId) {

	var userCheckerRemarks = $("#userCheckerRemarks").val().trim();
	$("#userCheckerRemarks").val(userCheckerRemarks);
	var isvalidRemarks = validateUserRemarks(formId, "userCheckerRemarks");

	if (isvalidRemarks) {
		$("#userActiveForm").attr('action', 'updateAccessStatus');
		$("#userActiveForm").submit();
	}
}

