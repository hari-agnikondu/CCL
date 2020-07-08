function goSaveGlobalParameters(formId, event) {

	event.preventDefault();

	var dateformat = "";

	dateformat = savedata(formId);

	$('#dateFormatId').val(dateformat);

	var data = $('#dateFormatId').val();

	var mftUrl = validateFields(formId, "mftUrl");
	var mftPostBackUrl = validateFields(formId, "mftPostBackUrl");
	var mftPostBackUrlHeader = validateFields(formId, "mftPostBackUrlHeader");
	var mftRetryCount = validateFields(formId, "mftRetryCount");
	var mftChannelIdentifier = validateFields(formId, "mftChannelIdentifier");

	if (data != 'false' && mftUrl && mftPostBackUrl && mftPostBackUrlHeader
			&& mftRetryCount && mftChannelIdentifier) {
		$("#globalParameters").attr('action', 'saveGlobalParameters');
		$("#globalParameters").submit();
	}

}

function clearError(ctrl) {
	$("#" + ctrl + "").next().html('');
}

function generateAlert(formId, eleId, key) {

	var text = readMessage(key);
	clearError(formId + " #" + eleId);
	$("#" + formId + " #" + eleId).after(
			'<span class="error_empty" style="color:red;"><br/>' + text
					+ '</span>');
}

function clearFieldError(formId, eleId) {
	clearError(formId + " #" + eleId);
}

function validateglobalParamwithNumericMinMaxLength(formId, eleId) {

	var isValid = false;
	var minLen = readValidationProp(eleId + '.min.length');
	var eleVal = $("#" + formId + " #" + eleId).val();
	var pattern = new RegExp(readValidationProp(eleId + '.pattern'));
	clearError(formId + " #" + eleId);

	if ((eleVal == null || eleVal == '') && minLen != 0) {
		generateAlert(formId, eleId, eleId + ".empty");
	} else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) {
		generateAlert(formId, eleId, eleId + ".pattern");
		return isValid;
	} else if ((eleVal.trim().length < minLen)
			|| (eleVal.trim().length > readValidationProp(eleId + '.max.length'))) {
		generateAlert(formId, eleId, eleId + ".length");

	} else {
		clearError(formId + " #" + eleId);
		isValid = true;
	}
	return isValid;

}

function isNumericPasswordLength(ctrl) {
	var isValid;
	var regex = /^[0-9]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^0-9]/g, '');

	if ((ctrl.value.charAt(0) == '0'))
		ctrl.value = ctrl.value.replace('0', '');

	return isValid;
}
function isAlphabetsWithComma(ctrl) {
	var isValid;
	var regex = /^[a-zA-Z]+[a-zA-Z,]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^a-zA-Z,]*/g, '');
	if ((ctrl.value.charAt(0) == ' ') || (ctrl.value.charAt(0) == ',')) {
		ctrl.value = ctrl.value.replace(/[\s,]+/, '');
	}
	return isValid;
}

function isAlphaNumericWithSpl(ctrl) {
	var isValid = false;
	var regex = /[a-zA-Z0-9, \-_/%&.:]+$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9, \-_/%&.:]+$/, '');
	if (ctrl.value.startsWith(' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	return isValid;
}

/**
 * 
 * validate HSM dropdown
 */
function validateDropDown(formId, eleId) {
	var isValid = false;
	var eleVal = $("#" + formId + " #" + eleId).val();

	if (eleVal.length <= 0 || eleVal == '-1') {
		generateAlert(formId, eleId, eleId + ".unselect");
	} else {
		clearError(formId + " #" + eleId);
		isValid = true;
	}
	return isValid;
}

function clearSpanError(ctrl) {
	$("#" + ctrl + "").html('');
}

function generateDateErrorMsg(formId, key) {

	var text = readMessage(key);
	clearSpanError(formId + " #" + "dateMsg");
	$("#" + formId + " #" + "dateMsg").html(text);
}

function savedata(formId) {
	var sel1 = document.globalParameters.sel1.value;
	var sel2 = document.globalParameters.sel2.value;
	var sel3 = document.globalParameters.sel3.value;
	var seldel1 = document.globalParameters.seldel1.value;
	var seldel2 = document.globalParameters.seldel2.value;

	clearSpanError(formId + " #" + "dateMsg");

	if ((sel1.substring(0, 1) == 'm' && sel2.substring(0, 1) == 'm')
			|| (sel1.substring(0, 1) == 'm' && sel3.substring(0, 1) == 'm')
			|| (sel2.substring(0, 1) == 'm' && sel3.substring(0, 1) == 'm')) {
		generateDateErrorMsg(formId, "dateMsg.pattern");
		return false;

	} else if ((sel1.substring(0, 1) == 'd' && sel2.substring(0, 1) == 'd')
			|| (sel1.substring(0, 1) == 'd' && sel3.substring(0, 1) == 'd')
			|| (sel2.substring(0, 1) == 'd' && sel3.substring(0, 1) == 'd')) {
		generateDateErrorMsg(formId, "dateMsg.pattern");
		return false;
	} else if ((sel1.substring(0, 1) == 'y' && sel2.substring(0, 1) == 'y')
			|| (sel1.substring(0, 1) == 'y' && sel3.substring(0, 1) == 'y')
			|| (sel2.substring(0, 1) == 'y' && sel3.substring(0, 1) == 'y')) {
		generateDateErrorMsg(formId, "dateMsg.pattern");
		return false;
	} else if (seldel1 != seldel2) {
		generateDateErrorMsg(formId, "dateMsg.pattern");
		return false;
	} else {

		var dateformat = "";

		dateformat = sel1 + seldel1 + sel2 + seldel2 + sel3;

		return dateformat;
	}
}

function changeDel1() {
	document.globalParameters.seldel1.value = document.globalParameters.seldel2.value;
}

function changeDel2() {
	document.globalParameters.seldel2.value = document.globalParameters.seldel1.value;
}

function selctedDateFormat() {

	var Output = "";

	Output = $('#dateFormatId').val();

	Output = Output.replace(/^\s+|\s+$/g, "");
	var regex = "([A-Za-z]{1,4})([/-])([A-Za-z]{1,4})([/-])([A-Za-z]{1,4})";
	var regexResult = Output.match(regex);
	if (regexResult != null) {
		var sep = regexResult[2];
		temp = Output.split(sep);
		sel1 = temp[0];
		document.globalParameters.sel1.value = sel1;
		document.globalParameters.seldel1.value = sep;
		sel2 = temp[1];
		document.globalParameters.sel2.value = sel2;
		document.globalParameters.seldel2.value = sep;
		sel3 = temp[2];
		document.globalParameters.sel3.value = sel3;
	}

}

function resetGlobalParameters() {
	$("#statusId").html('');

	clearError("customerPasswordLengthId");
	clearError("maskingCharValueId");
	clearError("hsmId");
	$("#dateMsg").html('');
	$("#customerPasswordLengthId").val('');
	$("#maskingCharValueId").val('');
	$('select option[value="-1"]').prop("selected", true);
}

function validateTopupPreemptNumberOfDays(formId,ctrl) {
	var isValid;
	clearError("topupPreemptNumberOfDays");
	var regex = /^[0-9]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^0-9]/g, '');

	if ((ctrl.value.charAt(0) == '0'))
		ctrl.value = ctrl.value.replace('0', '');

	if((ctrl.value <= 0  || ctrl.value > 366)){
		$("#topupPreemptNumberOfDays").val('');
	    generateAlert(formId, ctrl.id, ctrl.id + ".invalid");
	}
	return isValid;
}


