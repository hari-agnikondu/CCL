function purseSubmit(formId, event) {
	clearError(formId + " #purseUIValidityPeriod");
	clearError(formId + " #purseActiveTime");
	clearError(formId + " #purseActiveTimeZone");
	clearError(formId + " #purseValidityTime");
	clearError(formId + " #purseValidityTimeZone");
	
	clearError(formId + " #autoTopupAmount");
	clearError(formId + " #rolloverMaxAmount");
	clearError(formId + " #autoReloadAmount");
	
	clearError(formId + " #autoTopupFrequency");
	clearError(formId + " #topupFreqDay");
	
	clearErrorMulti(formId + " #multierror");
	clearError(formId + " #topupFreqDayOfMonth");
	clearError(formId + " #topupFreqMonth");
	
	clearError(formId + " #autoReloadFrequency");
	clearError(formId + " #reloadFreqDay");	
	clearErrorMulti(formId + " #multierrorReload");
	clearError(formId + " #reloadFreqDayOfMonth");
	clearError(formId + " #reloadFreqMonth");

	var activeDate = $("#" + formId + " #" + "purseUIActiveDate").val();
	var validityDate = $("#" + formId + " #" + "purseUIValidityPeriod").val();

	var purseValidityTime = $("#" + formId + " #" + "purseValidityTime").val();
	var purseActiveTime = $("#" + formId + " #" + "purseActiveTime").val();

	var activeTimeZone = $("#" + formId + " #" + "purseActiveTimeZone").val();
	var validityTimeZone = $("#" + formId + " #" + "purseValidityTimeZone")
			.val();
	var valid = true;
	var autoTopupEnable = $("#autoTopup_Enable").is(":checked");
	var rolloverEnable = $("#rollover_Enable").is(":checked");
	var autoReloadEnable = $("#autoReload_Enable").is(":checked");
	if(autoTopupEnable){	
		var topupfreq = $("#" + formId + " #" + "autoTopupFrequency").val();
		var topupday = $("#" + formId + " #" + "topupFreqDay").val();
		var topupFreqDayOfMonthMulti = $("#" + formId + " #" + "topupFreqDayOfMonthMulti").val();
		var topupFreqDayOfMonth = $("#" + formId + " #" + "topupFreqDayOfMonth").val();
		var topupFreqMonth = $("#" + formId + " #" + "topupFreqMonth").val();
		if(topupfreq =="-1"){
			generateAlert(formId, "autoTopupFrequency", "autoTopupFrequency"
					+ ".empty");
			valid = false;
			
		}else if(topupfreq =="day" &&( topupday=='' || topupday == null)){
			generateAlert(formId, "topupFreqDay", "topupFreqDay"
					+ ".empty");
			valid = false;
		}else if(topupfreq =="dayOfMonth" && (topupFreqDayOfMonthMulti=='' || topupFreqDayOfMonthMulti == null)){
			generateAlertMulti(formId, "multierror", "topupFreqDayOfMonthMulti"
					+ ".empty");
			valid = false;
		}else if((topupfreq =="quarter" || topupfreq =="year" )&& (topupFreqDayOfMonth=='-1' || topupFreqDayOfMonth == null)){
			generateAlert(formId, "topupFreqDayOfMonth", "topupFreqDayOfMonth"
					+ ".empty");
			valid = false;
		}else if((topupfreq =="quarter" || topupfreq =="year" )&& (topupFreqMonth=='-1' || topupFreqMonth == null)){
			generateAlert(formId, "topupFreqMonth", "topupFreqMonth"
					+ ".empty");
			valid = false;
		} else if ((topupfreq == "quarter" || topupfreq == "year")
				&& (topupFreqMonth != '-1' || topupFreqMonth != null)
				&& (topupFreqDayOfMonth != '-1' || topupFreqDayOfMonth != null)) {
			if (!(topupFreqDayOfMonth > 0 && topupFreqDayOfMonth <= daysInMonth(topupFreqMonth,new Date().getFullYear()))){					
				generateAlert(formId, "topupFreqDayOfMonth",
						"topupFreqDayOfMonth" + ".validDayOfMonth");
				valid = false;
			}			
		}
		
		
		var topupamount = $("#" + formId + " #" + "autoTopupAmount").val();
		if (topupamount == "" || topupamount == null) {
			generateAlert(formId, "autoTopupAmount", "autoTopupAmount"
					+ ".empty");
			valid = false;
		}
		if(rolloverEnable){
			var rolloverMaxAmount = $("#" + formId + " #" + "rolloverMaxAmount").val();
			var rolloverPercent = $("#" + formId + " #" + "rolloverPercent").val();
			if ((rolloverMaxAmount == "" || rolloverMaxAmount == null) && (rolloverPercent == "" || rolloverPercent == null)) {
				generateAlert(formId, "rolloverMaxAmount", "rolloverMaxAmount"
						+ ".empty");
				valid = false;
			}
		}
		
		
	}
	if(autoReloadEnable){
		var reloadfreq = $("#" + formId + " #" + "autoReloadFrequency").val();
		var reloadday = $("#" + formId + " #" + "reloadFreqDay").val();
		var reloadFreqDayOfMonthMulti = $("#" + formId + " #" + "reloadFreqDayOfMonthMulti").val();
		var reloadFreqDayOfMonth = $("#" + formId + " #" + "reloadFreqDayOfMonth").val();
		var reloadFreqMonth = $("#" + formId + " #" + "reloadFreqMonth").val();
		if(reloadfreq =="-1"){
			generateAlert(formId, "autoReloadFrequency", "autoReloadFrequency"
					+ ".empty");
			valid = false;
			
		}else if(reloadfreq =="day" &&( reloadday=='' || reloadday == null)){
			generateAlert(formId, "reloadFreqDay", "reloadFreqDay"
					+ ".empty");
			valid = false;
		}else if(reloadfreq =="dayOfMonth" && (reloadFreqDayOfMonthMulti=='' || reloadFreqDayOfMonthMulti == null)){
			generateAlertMulti(formId, "multierrorReload", "reloadFreqDayOfMonthMulti"
					+ ".empty");
			valid = false;
		}else if((reloadfreq =="quarter" || reloadfreq =="year" )&& (reloadFreqDayOfMonth=='-1' || reloadFreqDayOfMonth == null)){
			generateAlert(formId, "reloadFreqDayOfMonth", "reloadFreqDayOfMonth"
					+ ".empty");
			valid = false;
		}else if((reloadfreq =="quarter" || reloadfreq =="year" )&& (reloadFreqMonth=='-1' || reloadFreqMonth == null)){
			generateAlert(formId, "reloadFreqMonth", "reloadFreqMonth"
					+ ".empty");
			valid = false;
		}else if ((reloadfreq == "quarter" || reloadfreq == "year")
				&& (reloadFreqMonth != '-1' || reloadFreqMonth != null)
				&& (reloadFreqDayOfMonth != '-1' || reloadFreqDayOfMonth != null)) {
			if (!(reloadFreqDayOfMonth > 0 && reloadFreqDayOfMonth <= daysInMonth(reloadFreqMonth,new Date().getFullYear()))){					
				generateAlert(formId, "reloadFreqDayOfMonth",
						"topupFreqDayOfMonth" + ".validDayOfMonth");
				valid = false;
			}			
		}
		var autoReloadAmount = $("#" + formId + " #" + "autoReloadAmount").val();
		if (autoReloadAmount == "" || autoReloadAmount == null) {
			generateAlert(formId, "autoReloadAmount", "autoReloadAmount"
					+ ".empty");
			valid = false;
		}
	}
	
	

	if (activeDate != "" && activeDate != null
			&& (activeTimeZone == "" || activeTimeZone == null)) {
		generateAlert(formId, "purseActiveTimeZone", "purseActiveTimeZone"
				+ ".empty");
		valid = false;
	} else if (validityDate != "" && validityDate != null) {
		if (validityTimeZone == "" || validityTimeZone == null) {
			generateAlert(formId, "purseValidityTimeZone",
					"purseValidityTimeZone" + ".empty");
			valid = false;
		} else if (purseValidityTime == null || purseValidityTime == "") {
			generateAlert(formId, "purseValidityTime", "purseValidityTime"
					+ ".empty");
			valid = false;
		}
	} else if (validityDate != "" && validityDate != null && activeDate != ""
			&& activeDate != null && validityTimeZone == activeTimeZone) {
		if ((validityDate == activeDate && purseValidityTime < purseActiveTime)
				|| Date.parse(validityDate) < Date.parse(activeDate)) {
			generateAlert(formId, "purseUIValidityPeriod",
					"purseUIValidityPeriod" + ".greater");
			valid = false;
		}
	} else if (purseActiveTime == null || purseActiveTime == "") {
		generateAlert(formId, "purseActiveTime", "purseActiveTime" + ".empty");
		valid = false;
	} else if ((validityTimeZone != "" && validityTimeZone != null)
			|| (purseValidityTime != null && purseValidityTime != "")) {
		generateAlert(formId, "purseUIValidityPeriod", "purseUIValidityPeriod"
				+ ".empty");
		valid = false;
	} else {
		valid = true;
	}

	var validPurseId = validateDropDown(formId, "purseId");
	var validPurseActiveDate = validateDate(formId, "purseUIActiveDate");
	if (validPurseId && validPurseActiveDate && valid && purseActiveTime
			&& activeDate && activeTimeZone) {
		$("#" + formId).attr('action', 'savePurseParameters');
		$("#" + formId).submit();
	} else {
		return false;
	}
}

function daysInMonth (month, year) { 
	return new Date(year, month, 0).getDate(); 
}

function validatewithActiveDate(formId, eleId) {
	clearError(formId + " #purseUIValidityPeriod");
	clearError(formId + " #purseActiveTime");
	clearError(formId + " #purseActiveTimeZone");
	clearError(formId + " #purseValidityTime");
	clearError(formId + " #purseValidityTimeZone");

	var activeDate = $("#" + formId + " #" + "purseUIActiveDate").val();
	var validityDate = $("#" + formId + " #" + "purseUIValidityPeriod").val();

	var purseActiveTime = $("#" + formId + " #" + "purseActiveTime").val();
	var purseValidityTime = $("#" + formId + " #" + "purseValidityTime").val();

	var activeTimeZone = $("#" + formId + " #" + "purseActiveTimeZone").val();
	var validityTimeZone = $("#" + formId + " #" + "purseValidityTimeZone")
			.val();

	if (validityDate != "" && validityDate != null) {
		if (validityTimeZone == "" || validityTimeZone == null) {
			generateAlert(formId, "purseValidityTimeZone",
					"purseValidityTimeZone" + ".empty");
			return false;
		} else if (purseValidityTime == null || purseValidityTime == "") {
			generateAlert(formId, "purseValidityTime", "purseValidityTime"
					+ ".empty");
			return false;
		}
	} else if (activeDate != "" && activeDate != null
			&& (activeTimeZone == "" || activeTimeZone == null)) {
		generateAlert(formId, "purseActiveTimeZone", "purseActiveTimeZone"
				+ ".empty");
		return false;
	} else if (validityDate != "" && validityDate != null && activeDate != ""
			&& activeDate != null && validityTimeZone == activeTimeZone) {
		if ((validityDate == activeDate && purseValidityTime < purseActiveTime)
				|| Date.parse(validityDate) < Date.parse(activeDate)) {
			generateAlert(formId, "purseUIValidityPeriod",
					"purseUIValidityPeriod" + ".greater");
			return false;
		}
	} else if (purseActiveTime == null || purseActiveTime == "") {
		generateAlert(formId, "purseActiveTime", "purseActiveTime" + ".empty");
		return false;
	} else if ((validityTimeZone != "" && validityTimeZone != null)
			|| (purseValidityTime != null && purseValidityTime != "")) {
		generateAlert(formId, "purseUIValidityPeriod", "purseUIValidityPeriod"
				+ ".empty");
		return false;
	} else {
		return true;
	}
}

function validateDropDown(formId, eleId) {
	var isValid = false;
	var eleVal = $("#" + formId + " #" + eleId).val();

	if (eleVal.length <= 0 || eleVal == 'NONE' || eleVal == '-1') {
		generateAlert(formId, eleId, eleId + ".unselect");
	} else {
		clearError(formId + " #" + eleId);
		isValid = true;
	}
	return isValid;
}

/*
 * Validate date format dd-mm-yyyy
 */
function validateDate(formId, eleId) {
	var isValid = false;
	var eleVal = $("#" + formId + " #" + eleId).val();
	var pattern = new RegExp(/\b\d{1,2}[/]\d{1,2}[/]\d{4}\b/);

	var selectedDate = $("#" + formId + " #" + eleId).datepicker('getDate');

	var today = new Date();
	today.setHours(0);
	today.setMinutes(0);
	today.setSeconds(0);
	clearFieldError(formId, eleId);

	if ((eleVal == null || eleVal == '') && eleId != 'purseUIValidityPeriod') {
		generateAlert(formId, eleId, eleId + ".empty");
	} else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) {
		generateAlert(formId, eleId, eleId + ".pattern");
	} else if ((Date.parse(today) >= Date.parse(selectedDate) && eleId == 'purseUIValidityPeriod')) {
		generateAlert(formId, eleId, eleId + ".invalid");
	} else {
		clearError(formId + " #" + eleId);
		isValid = true;
	}
	return isValid;
}
function allowNumbersWithSlash(ctrl) {
	var isValid = false;
	var regex = /^[0-9/]+$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^0-9/]/g, '');
	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	if ((ctrl.value.charAt(0) == '/'))
		ctrl.value = ctrl.value.replace('/', '');
	return isValid;
}

function isNumericValidity(ctrl) {

	var isValid;
	var regex = /^[0-9]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^0-9]/g, '');

	if ((ctrl.value.charAt(0) == '0'))
		ctrl.value = ctrl.value.replace('0', '');

	if(ctrl.value > 31){
		$("#" + ctrl.id + "").val('');
	}
	return isValid;
}
function validateDecimalFormatForMaxCardBalance(formId, eleId) {

	var eleVal = $("#" + formId + " #" + eleId).val();

	var splitvalue = eleVal.split(".");
	clearFieldError(formId, eleId);

	if ((splitvalue.length > 2)) {
		generateAlert(formId, eleId, eleId + ".NaN");
		return false;
	}
	if (eleVal.substr(0, 1) == '.') {
		eleVal = '0' + eleVal;
		$("#" + formId + " #" + eleId).value = eleVal;
	}
	if (eleVal.length > 0) {
		eleVal = Number(eleVal).toString();
	}
	$("#" + formId + " #" + eleId).val(eleVal);
	return true;
}

function DecimalValueFormatPurse(Object) {
	var v_qty = $(Object).val();
	var objId = $(Object).attr('id');
	var minorUnits = $("#minorUnits").val();
	

	var splitvalue = v_qty.split(".");

	if (splitvalue.length > 2) {
		$("#" + objId + "").val('');
		generateAlert($(Object).closest('form').attr('id'), Object.id,
				"product.NaN");
		$("#" + objId.split('_', 1) + objId.split('_', 1)).css('visibility',
				'visible');
		return false;
	}
	v_qty = v_qty.replace(/^0+/, '');
	Object.value = v_qty;
	if (v_qty.substr(0, 1) == '.') {
		v_qty = '0' + v_qty;
		Object.value = v_qty;
	}
	//  var v_qty_int,
	var v_qty_dec;

	if (splitvalue.length > 1) {
		//    v_qty_int = splitvalue[0];
		v_qty_dec = splitvalue[1];
		if (v_qty < 0.1) {
			$("#" + objId + "").val('');
			generateAlert($(Object).closest('form').attr('id'), Object.id,
					"product.NaN");
			$("#" + objId.split('_', 1) + objId.split('_', 1)).css(
					'visibility', 'visible');
			return false;
		}
		if (v_qty_dec.length > minorUnits) {
			$("#" + objId + "").val('');
			generateAlert($(Object).closest('form').attr('id'), Object.id,
					"purse.decimal.validation"+minorUnits);
			$("#" + objId.split('_', 1) + objId.split('_', 1)).css(
					'visibility', 'visible');
			return false;
		} else {
			clearError(Object.id);
			if (v_qty_dec.length == 0) {
				v_qty_dec = "000";
				Object.value = Object.value + v_qty_dec;
			}

			return true;
		}
	} else {
		clearError(Object.id);
		if (v_qty == 0) {
			$("#" + objId + "").val('');
			generateAlert($(Object).closest('form').attr('id'), Object.id,
					"product.NaN");
			$("#" + objId.split('_', 1) + objId.split('_', 1)).css(
					'visibility', 'visible');
			return false;
		}
		$("#" + objId.split('_', 1) + objId.split('_', 1)).css('visibility',
				'hidden');
		return true;
	}

}

/*function validateDecimalFormatPurse(formId, eleId) {
	var eleVal = $("#" + formId + " #" + eleId).val();
	var splitvalue = eleVal.split(".");
	clearFieldError(formId, eleId);

	if ((eleVal == null || eleVal == '')) {
		generateAlert(formId, eleId, eleId + ".empty");
		return false;
	}

	if (eleVal == '.'){
		 generateAlert(formId, eleId,eleId+".NaN");
	     return false;
	}

	if ((splitvalue.length > 2)) {
		generateAlert(formId, eleId, eleId + ".NaN");
		return false;
	}

	if (eleVal.substr(0, 1) == '.') {
		eleVal = '0' + eleVal;
		$("#" + formId + " #" + eleId).value = eleVal;
	}

	return true;
}*/

function allowPercentNumbersWithDot(ctrl) {
	var isValid = false;
	var regex ="";
	var purseType = $("#purseTypeName").val();
	if(purseType == "SKU" || purseType == "POINTS"){
		regex = /^[0-9]+$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^0-9]/g, '');
	}else{
		regex = /^[0-9.]+$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^0-9.]/g, '');
	}
	
	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	if ((ctrl.value.charAt(0) == '.'))
		ctrl.value = ctrl.value.replace('.', '0.');
	
	if(ctrl.value > 100)
		$("#" + ctrl.id + "").val('');
	return isValid;
}

function allowNumbersWithAndWithoutDot(ctrl) {
	var isValid = false;var regex ="";
	var purseType = $("#purseTypeName").val();
	if(purseType == "SKU" || purseType == "POINTS"){
		regex = /^[0-9]+$/;
	}else{
		regex = /^[0-9.]+$/;
	}
	
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^0-9.]/g, '');
	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	if ((ctrl.value.charAt(0) == '.'))
		ctrl.value = ctrl.value.replace('.', '0.');
	
	return isValid;
}

function topupFreqDiv() {

	var option = "";

	option = $("#autoTopupFrequency").val();

	if (option == "day") {
		$("#topupFreqDayBlock").attr("style", "display:block");
		$("#topupFreqDayOfMonthBlockMulti").attr("style", "display:none");
		$("#topupFreqDayOfMonthBlock").attr("style", "display:none");
		$("#topupFreqMonthBlock").attr("style", "display:none");
		
	
		$("#topupFreqDayOfMonthMulti").multiselect("clearSelection");
		$("#topupFreqDayOfMonth").val('-1');
		$("#topupFreqMonth").val('-1');
	}
	if (option == "dayOfMonth") {
		$("#topupFreqDayBlock").attr("style", "display:none");
		$("#topupFreqDayOfMonthBlockMulti").attr("style", "display:block");
		$("#topupFreqDayOfMonthBlock").attr("style", "display:none");
		$("#topupFreqMonthBlock").attr("style", "display:none");
		$("#topupFreqDay").val('');	
		$("#topupFreqDayOfMonth").val('-1');
		$("#topupFreqMonth").val('-1');
	}
	if (option == "quarter" || option == "year") {
		$("#topupFreqDayBlock").attr("style", "display:none");
		$("#topupFreqDayOfMonthBlockMulti").attr("style", "display:none");
		$("#topupFreqDayOfMonthBlock").attr("style", "display:block");
		$("#topupFreqMonthBlock").attr("style", "display:block");
		$("#topupFreqDay").val('');
		$("#topupFreqDayOfMonthMulti").multiselect("clearSelection");

	}
	if (option == "-1") {
		$("#topupFreqDayBlock").attr("style", "display:none");
		$("#topupFreqDayOfMonthBlockMulti").attr("style", "display:none");
		$("#topupFreqDayOfMonthBlock").attr("style", "display:none");
		$("#topupFreqMonthBlock").attr("style", "display:none");
		$("#topupFreqDay").val('');
		$("#topupFreqDayOfMonthMulti").multiselect("clearSelection");
		$("#topupFreqDayOfMonth").val('-1');
		$("#topupFreqMonth").val('-1');
	}

}

function reloadFreqDiv() {

	var option = "";

	option = $("#autoReloadFrequency").val();

	if (option == "day") {
		$("#reloadFreqDayBlock").attr("style", "display:block");
		$("#reloadFreqDayOfMonthBlockMulti").attr("style", "display:none");
		$("#reloadFreqDayOfMonthBlock").attr("style", "display:none");
		$("#reloadFreqMonthBlock").attr("style", "display:none");
		
	
		$("#reloadFreqDayOfMonthMulti").multiselect("clearSelection");
		$("#reloadFreqDayOfMonth").val('-1');
		$("#reloadFreqMonth").val('-1');
	}
	if (option == "dayOfMonth") {
		$("#reloadFreqDayBlock").attr("style", "display:none");
		$("#reloadFreqDayOfMonthBlockMulti").attr("style", "display:block");
		$("#reloadFreqDayOfMonthBlock").attr("style", "display:none");
		$("#reloadFreqMonthBlock").attr("style", "display:none");
		$("#reloadFreqDay").val('');	
		$("#reloadFreqDayOfMonth").val('-1');
		$("#reloadFreqMonth").val('-1');
	}
	if (option == "quarter" || option == "year") {
		$("#reloadFreqDayBlock").attr("style", "display:none");
		$("#reloadFreqDayOfMonthBlockMulti").attr("style", "display:none");
		$("#reloadFreqDayOfMonthBlock").attr("style", "display:block");
		$("#reloadFreqMonthBlock").attr("style", "display:block");
		$("#reloadFreqDay").val('');
		$("#reloadFreqDayOfMonthMulti").multiselect("clearSelection");

	}
	if (option == "-1") {
		$("#reloadFreqDayBlock").attr("style", "display:none");
		$("#reloadFreqDayOfMonthBlockMulti").attr("style", "display:none");
		$("#reloadFreqDayOfMonthBlock").attr("style", "display:none");
		$("#reloadFreqMonthBlock").attr("style", "display:none");
		$("#reloadFreqDay").val('');
		$("#reloadFreqDayOfMonthMulti").multiselect("clearSelection");
		$("#reloadFreqDayOfMonth").val('-1');
		$("#reloadFreqMonth").val('-1');
	}

}

/*To clear common error */
function clearErrorMulti(ctrl){
		$("#" + ctrl + "").prev().html('');
	}

/*To display error messages for client side validation*/
function generateAlertMulti(formId, eleId,key){
	 
	var text=readMessage(key);
	clearErrorMulti(formId+" #"+eleId);
	$("#"+formId+" #"+eleId).before('<span class="error_empty" style="color:red;"><br/>' + text + '</span>');
}


function getPurseAttributes(formId, eleId) {
	clearError("purseUIValidityPeriod");
	clearError("purseUIActiveDate");
	clearError("maxPurseBalance");
	clearError("purseValidityTime");
	clearError("purseValidityTimeZone");
	clearError("autoTopupAmount");
	clearError("rolloverMaxAmount");
	clearError("rolloverPercent");
	clearError("autoReloadAmount");
	
	clearError("autoTopupFrequency");
	clearError("topupFreqDay");
	
	clearErrorMulti("multierror");
	clearError("topupFreqDayOfMonth");
	clearError("topupFreqMonth");
	
	clearErrorMulti("multierrorReload");
	
	$("#topupFreqDayOfMonthMulti").multiselect("clearSelection");
	$("#topupFreqDayOfMonth").val('-1');
	$("#topupFreqMonth").val('-1');
	$("#autoTopupFrequency").val('-1');
	$("#reloadFreqDayOfMonthMulti").multiselect("clearSelection");
	$("#reloadFreqDayOfMonth").val('-1');
	$("#reloadFreqMonth").val('-1');
	$("#autoReloadFrequency").val('-1');

	var productId;
	if ($("#parentProductId  option:selected").val() == -1
			|| $("#parentProductId  option:selected").val() == null) {
		productId = -1;
	} else {
		productId = $("#parentProductId  option:selected").val();
	}
	var data = $("#" + eleId + " option:selected").val();
	var servUrl = $('#addurl').val();
	var str = servUrl + "/ajax/getProductPurseAttributes/" + productId + "/"
			+ data;
	$.ajax({
		url : str,
		async : true,
		type : "GET",
		dataType : 'json',

		/* crossDomain: true, */
		success : function(response, status, xhr) {

			$.each(response,
					function(index, value) {
						if (index == "purseUIActiveDate") {
							$("#purseUIActiveDate").val(value);
						}
						if (index == "purseActiveTime") {
							$("#purseActiveTime").val(value);
						}

						if (index == "purseActiveTimeZone") {
							$("#purseActiveTimeZone").val(value).attr(
									"selected", "selected");
						}

						if (index == "purseUIValidityPeriod") {
							$("#purseUIValidityPeriod").val(value);
						}
						if (index == "purseValidityTime") {
							$("#purseValidityTime").val(value);
						}

						if (index == "purseValidityTimeZone") {

							$("#purseValidityTimeZone").val(value).attr(
									"selected", "selected");
						}

						if (index == "maxPurseBalance") {
							$("#maxPurseBalance").val(value);
						}
						if (index == "minorUnits") {
							$("#minorUnits").val(value);
						}
						if (index == "purseTypeName") {
							$("#purseTypeName").val(value);
						}
						if (index == "purseRedeemSplitTender") {
							$("#purseRedeemSplitTender_Enable").prop("checked",
									false);
							$("#purseRedeemSplitTender_Disable").prop(
									"checked", false);
							if (value == "true") {
								$("#purseRedeemSplitTender_Enable").prop(
										"checked", true);
							} else {
								$("#purseRedeemSplitTender_Disable").prop(
										"checked", true);
							}
						}

						if (index == "purseRedeemLockSplitTender") {
							$("#purseRedeemLockSplitTender_Enable").prop(
									"checked", false);
							$("#purseRedeemLockSplitTender_Disable").prop(
									"checked", false);
							if (value == "true") {
								$("#purseRedeemLockSplitTender_Enable").prop(
										"checked", true);
							} else {
								$("#purseRedeemLockSplitTender_Disable").prop(
										"checked", true);
							}
						}
						
						if (index == "autoTopupEnable") {
							$("#autoTopup_Enable").prop(
									"checked", false);
							$("#autoTopup_Disable").prop(
									"checked", false);
							if (value == "Enable") {
								$("#autoTopup_Enable").prop(
										"checked", true);
								$("#autoTopupEnableBlock").css(
										"display", "block");
							} else {
								$("#autoTopup_Disable").prop(
										"checked", true);
								$("#autoTopupEnableBlock").css(
										"display", "none");
							}
						}
						
						if (index == "autoTopupFrequency") {

							$("#autoTopupFrequency").val(value).attr(
									"selected", "selected");
						}
						/*if($("#autoTopupFrequency").val() == "day"){*/
						 if(index == "topupFreqDay" ){
							 $("#topupFreqDay").val(value);
							 if(value != null &&  value != ""){
								$("#topupFreqDayBlock").css(
										"display", "block");
								$("#topupFreqDayOfMonthBlockMulti").css(
										"display", "none");
								$("#topupFreqDayOfMonthBlock").css(
										"display", "none");
								$("#topupFreqMonthBlock").css(
										"display", "none");
							 }
						 }
						/*}else if($("#autoTopupFrequency").val() == "dayOfMonth"){*/
							if(index == "topupFreqDayOfMonthMulti"){
								//$("#topupFreqDayOfMonthMulti").val(value);
								if(value != null &&  value != ""){
									$("#topupFreqDayBlock").css(
											"display", "none");
									$("#topupFreqDayOfMonthBlockMulti").css(
											"display", "block");
									$("#topupFreqDayOfMonthBlock").css(
											"display", "none");
									$("#topupFreqMonthBlock").css(
											"display", "none");
								 }
								var hidValue =value;
							    var selectedOptions = hidValue.split(",");
							    for(var i in selectedOptions) {
							        var optionVal = selectedOptions[i];
							        $("#topupFreqDayOfMonthMulti").find("option[value="+optionVal+"]").prop("selected", "selected");
							    }
							    $("#topupFreqDayOfMonthMulti").multiselect('refresh');
								//$("#topupFreqDayOfMonthMulti").val('1');
								 
								/*var obj=$('#topupFreqDayOfMonthMulti');
								for (var i in value) {
								    var val=value[i];
								    console.log(val);
								   obj.find('option[value='+val+']').attr("checked","checked");
								}*/
								
							 }
						/*}else if($("#autoTopupFrequency").val() == "quarter" || $("#autoTopupFrequency").val(value) == "year"){*/
							if(index == "topupFreqDOM" ){
								$("#topupFreqDayOfMonth").val(value).attr(
										"selected", "selected");
								 if(value != null &&  value != ""){
									$("#topupFreqDayBlock").css(
											"display", "none");
									$("#topupFreqDayOfMonthBlockMulti").css(
											"display", "none");
									$("#topupFreqDayOfMonthBlock").css(
											"display", "block");
									$("#topupFreqMonthBlock").css(
											"display", "block");
									
								 }
								 
							 }
							
							if(index == "topupFreqMonth"){
								$("#topupFreqMonth").val(value).attr(
										"selected", "selected");
								 if(value != null &&  value != ""){
									$("#topupFreqDayBlock").css(
											"display", "none");
									$("#topupFreqDayOfMonthBlockMulti").css(
											"display", "none");
									$("#topupFreqDayOfMonthBlock").css(
											"display", "block");
									$("#topupFreqMonthBlock").css(
											"display", "block");
								 }
							 }
						//}
						
						
						
						if (index == "autoTopupAmount") {
							$("#autoTopupAmount").val(value);
						}
						
						if (index == "rolloverEnable") {
							$("#rollover_Enable").prop(
									"checked", false);
							$("#rollover_Disable").prop(
									"checked", false);
							if (value == "Enable") {
								$("#rollover_Enable").prop(
										"checked", true);
								$("#rolloverEnableBlock").css(
										"display", "block");
							} else {
								$("#rollover_Disable").prop(
										"checked", true);
								$("#rolloverEnableBlock").css(
										"display", "none");
							}
						}
						
						if (index == "rolloverMaxAmount") {
							$("#rolloverMaxAmount").val(value);
						}
						
						if (index == "rolloverPercent") {
							$("#rolloverPercent").val(value);
						}
						
						if (index == "autoReloadEnable") {
							$("#autoReload_Enable").prop(
									"checked", false);
							$("#autoReload_Disable").prop(
									"checked", false);
							if (value == "Enable") {
								$("#autoReload_Enable").prop(
										"checked", true);
								$("#autoReloadEnableBlock").css(
										"display", "block");
							} else {
								$("#autoReload_Disable").prop(
										"checked", true);
								$("#autoReloadEnableBlock").css(
										"display", "none");
							}
						}
						
						if (index == "autoReloadFrequency") {

							$("#autoReloadFrequency").val(value).attr(
									"selected", "selected");
						}
						
						 if(index == "reloadFreqDay" ){
							 $("#reloadFreqDay").val(value);
							 if(value != null &&  value != ""){
								$("#reloadFreqDayBlock").css(
										"display", "block");
								$("#reloadFreqDayOfMonthBlockMulti").css(
										"display", "none");
								$("#reloadFreqDayOfMonthBlock").css(
										"display", "none");
								$("#reloadFreqMonthBlock").css(
										"display", "none");
							 }
						 }

							if(index == "reloadFreqDayOfMonthMulti"){

								if(value != null &&  value != ""){
									$("#reloadFreqDayBlock").css(
											"display", "none");
									$("#reloadFreqDayOfMonthBlockMulti").css(
											"display", "block");
									$("#reloadFreqDayOfMonthBlock").css(
											"display", "none");
									$("#reloadFreqMonthBlock").css(
											"display", "none");
								 }
								var reloadValue =value;
							    var reloadOptions = reloadValue.split(",");
							    for(var i in reloadOptions) {
							        var reloadVal = reloadOptions[i];
							        $("#reloadFreqDayOfMonthMulti").find("option[value="+reloadVal+"]").prop("selected", "selected");
							    }
							    $("#reloadFreqDayOfMonthMulti").multiselect('refresh');
								
								
							 }
						
							if(index == "reloadFreqDOM" ){
								$("#reloadFreqDayOfMonth").val(value).attr(
										"selected", "selected");
								 if(value != null &&  value != ""){
									$("#reloadFreqDayBlock").css(
											"display", "none");
									$("#reloadFreqDayOfMonthBlockMulti").css(
											"display", "none");
									$("#reloadFreqDayOfMonthBlock").css(
											"display", "block");
									$("#reloadFreqMonthBlock").css(
											"display", "block");
									
								 }
								 
							 }
							
							if(index == "reloadFreqMonth"){
								$("#reloadFreqMonth").val(value).attr(
										"selected", "selected");
								 if(value != null &&  value != ""){
									$("#reloadFreqDayBlock").css(
											"display", "none");
									$("#reloadFreqDayOfMonthBlockMulti").css(
											"display", "none");
									$("#reloadFreqDayOfMonthBlock").css(
											"display", "block");
									$("#reloadFreqMonthBlock").css(
											"display", "block");
								 }
							 }
						
						
						if (index == "autoReloadAmount") {
							$("#autoReloadAmount").val(value);
						}

					});
		},
		error : function(error) {
			$("#purseUIActiveDate").val('');
			$("#purseUIValidityPeriod").val('');
			$("#maxPurseBalance").val('');
			$("#purseActiveTime").val('');
			$("#purseActiveTimeZone").val('');
			$("#purseValidityTime").val('');
			$("#purseValidityTimeZone").val('');
			$("#autoTopupAmount").val('');
			$("#rolloverMaxAmount").val('');
			$("#rolloverPercent").val('');
			$("#autoReloadAmount").val('');
			$("#topupFreqDayOfMonthMulti").multiselect("clearSelection");
			$("#topupFreqDayOfMonth").val('-1');
			$("#topupFreqMonth").val('-1');
			$("#autoTopupFrequency").val('-1');
			$("#topupFreqDay").val('');
			$("#reloadFreqDayOfMonthMulti").multiselect("clearSelection");
			$("#reloadFreqDayOfMonth").val('-1');
			$("#reloadFreqMonth").val('-1');
			$("#autoReloadFrequency").val('-1');
			$("#reloadFreqDay").val('');
		}
	});
}

function getParentProductPurseDetails() {
	$("#purseId").val('-1');
	$("#purseUIActiveDate").val('');
	$("#purseUIValidityPeriod").val('');
	$("#maxPurseBalance").val('');
	$("#purseActiveTime").val('');
	$("#purseActiveTimeZone").val('');
	$("#purseValidityTime").val('');
	$("#purseValidityTimeZone").val('');
	$("#autoTopupAmount").val('');
	$("#rolloverMaxAmount").val('');
	$("#rolloverPercent").val('');
	$("#autoReloadAmount").val('');
	$("#topupFreqDayOfMonthMulti").multiselect("clearSelection");
	$("#topupFreqDayOfMonth").val('-1');
	$("#topupFreqMonth").val('-1');
	$("#autoTopupFrequency").val('-1');
	$("#topupFreqDay").val('');
	$("#reloadFreqDayOfMonthMulti").multiselect("clearSelection");
	$("#reloadFreqDayOfMonth").val('-1');
	$("#reloadFreqMonth").val('-1');
	$("#autoReloadFrequency").val('-1');
	$("#reloadFreqDay").val('');

	if ($("#parentProductId  option:selected").val() == -1) {
		document.getElementById("parentProductIdError").innerHTML = '<font color="red">'
				+ readMessage("parentProductId.empty") + "</font>";
		return false;
	}
	clearError("parentProductIdError");

	$("#productPurse").attr('action', 'productPurse')
	$("#productPurse").submit();

}

function getProductPurses() {
	if ($("#parentProductId  option:selected").val() != -1) {
		document.getElementById("parentProductIdError").innerHTML = '<font color="red">'
				+ readMessage("clickcopybutton") + "</font>";
	} else {
		document.getElementById("parentProductIdError").innerHTML = '';
	}
}

$(function() {
	var container = $('.bootstrap-iso form').length > 0 ? $(
			'.bootstrap-iso form').parent() : "body";
	$('.date-picker')
			.datepicker(
					{
						format : 'mm/dd/yyyy',
						container : container,
						todayHighlight : true,
						autoclose : true,
						changeMonth : true,
						changeYear : true,
						showButtonPanel : true,
						startDate : -Infinity,
						dateFormat : 'mm/dd/yyyy',
						onClose : function(dateText, inst) {
							var date = $(
									"#ui-datepicker-div .ui-datepicker-date :selected")
									.val();
							var month = $(
									"#ui-datepicker-div .ui-datepicker-month :selected")
									.val();
							var year = $(
									"#ui-datepicker-div .ui-datepicker-year :selected")
									.val();
							$(this).datepicker('setDate',
									new Date(date, year, month, 1));
						}
					})
});

$("#purseTab").addClass("active");
$("#purseTab").siblings().removeClass('active');