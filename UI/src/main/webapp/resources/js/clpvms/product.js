/* PRODUCT FUNCTIONS STARTS */

function validateDecimalFormat(formId, eleId) {
	var eleVal = $("#" + formId + " #" + eleId).val();
	var splitvalue = eleVal.split(".");
	clearFieldError(formId, eleId);

	if ((eleVal == null || eleVal == '')) {
		generateAlert(formId, eleId, eleId + ".empty");
		return false;
	}

	/*if (eleVal == '.'){
		 generateAlert(formId, eleId,eleId+".NaN");
	     return false;
	}*/

	if ((splitvalue.length > 2)) {
		generateAlert(formId, eleId, eleId + ".NaN");
		return false;
	}

	if (eleVal.substr(0, 1) == '.') {
		eleVal = '0' + eleVal;
		$("#" + formId + " #" + eleId).value = eleVal;
	}

	return true;
}

function allowNumbersWithDot(ctrl) {
	var isValid = false;
	var regex = /^[0-9.]+$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^0-9.]/g, '');
	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	if ((ctrl.value.charAt(0) == '.'))
		ctrl.value = ctrl.value.replace('.', '0.');
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

function DecimalValueFormat(Object) {
	var v_qty = $(Object).val();
	var objId = $(Object).attr('id');

	var splitvalue = v_qty.split(".");

	if (splitvalue.length > 2) {
		generateAlert($(Object).closest('form').attr('id'), Object.id,
				"product.NaN");
		$("#" + objId.split('_', 1) + objId.split('_', 1)).css('visibility',
				'visible');
		return false;
	}
	if (v_qty.substr(0, 1) == '.') {
		v_qty = '0' + v_qty;
		Object.value = v_qty;
	}
	//  var v_qty_int,
	var v_qty_dec;

	if (splitvalue.length > 1) {
		//    v_qty_int = splitvalue[0];
		v_qty_dec = splitvalue[1];

		if (v_qty_dec.length > 3) {
			generateAlert($(Object).closest('form').attr('id'), Object.id,
					"product.decimal.validation");
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
		$("#" + objId.split('_', 1) + objId.split('_', 1)).css('visibility',
				'hidden');
		return true;
	}

}

function isNumericValidity(ctrl) {

	var isValid;
	var regex = /^[0-9]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^0-9]/g, '');

	if ((ctrl.value.charAt(0) == '0'))
		ctrl.value = ctrl.value.replace('0', '');

	/*if((ctrl.value.startsWith(' '))||(ctrl.value.endsWith(' ')))
		ctrl.value =ctrl.value.replace(/\s+/,'');*/
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

	/* clearFieldError(formId, eleId);*/

	//Start - Added for JIRA 843
	if ((eleVal == null || eleVal == '') && formId == 'editProductForm'
			&& eleId == 'activeFrom') {
		var activeFromOld = $("#activeFromOld").val();
		if (activeFromOld != null && activeFromOld != '') {
			eleVal = activeFromOld;
		}
		$("#" + formId + " #" + eleId).val(activeFromOld);
	}
	//End - Added for JIRA 843 
	if ((eleVal == null || eleVal == '')
			&& (eleId != 'productValidityDate')) {
		generateAlert(formId, eleId, eleId + ".empty");
	} else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) {
		generateAlert(formId, eleId, eleId + ".pattern");
	} else if ((Date.parse(today) >= Date.parse(selectedDate) && eleId == 'productValidityDate')) {
		generateAlert(formId, eleId, eleId + ".invalid");
	} else if ((Date.parse(today) > Date.parse(selectedDate))) {
		generateAlert(formId, eleId, eleId + ".invalid");
	} else {
		clearError(formId + " #" + eleId);
		isValid = true;
	}
	return isValid;
}

function validateCutoverTime(formId, eleId) {
	var isValid = false;
	var eleVal = $("#" + formId + " #" + eleId).val();
	clearFieldError(formId, eleId);

	if ((eleVal == null || eleVal == '')) {
		generateAlert(formId, eleId, eleId + ".empty");
	} else {
		isValid = true;
	}
	return isValid;
}

/*
 * Validate drop down field  
 */
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

function searchProduct(event) {
	event.preventDefault();

	var productName = "";
	var code = 'byName';

	productName = $("#productNameId").val();

	if (productName === undefined || productName == null
			|| productName.length <= 0 || productName == '') {
		code = 'all';

		$("#searchType").val(code);
		$("#productFormId").attr('action', 'showAllProducts');
		$("#productFormId").submit();
	} else {
		$("#searchType").val(code);
		$("#productFormId").attr('action', 'searchProductByName');
		$("#productFormId").submit();
	}
}

function clickAddProduct() {
	$("#productFormId").attr('action', 'showAddProduct');
	$("#productFormId").submit();
}

function downloadPdf(id) {
	var code = id;
	$("#productId").val(code);
	$("#productFormId").attr('action', 'download');
	$("#productFormId").submit();
}

function showEditProduct(id) {
	var code = id;
	$("#productId").val(code);
	$("#productFormId").attr('action', 'showEditProduct');
	$("#productFormId").submit();
}

function goConfirmProduct(formId, eleId) {

	var flag = validateupdateProduct(formId, eleId);
	if (flag) {
		$("#search_submit").attr("data-target", "#define-constant-update");
		var name = $('#productName').val();
		name = name.trim();
		$("#productIdtoUpdate").val(name);
		document.getElementById("productNameDisp").innerHTML = name;
	} else {
		$("#search_submit").removeAttr("data-target");

	}
}

function goUpdateProduct() {

	$("#editProductForm").attr('action', 'editProduct');
	$("#editProductForm").submit();
}

function validateupdateProduct(formId, event) {
	event.preventDefault();

	var validProductName = validateFields(formId, "productName");
	var validProductShortName = validateFields(formId, "productShortName");
	//var validProductDesc = validateFields(formId, "productDesc");
	var validProductPartnerName = validateDropDown(formId, "productPartnerName");
	var validProgramId = validateDropDown(formId, "programId");
	var validProductIssuerName = validateDropDown(formId, "productIssuerName");
	/*var validB2BInitSerialNumQty = validateFields(formId,
			"b2bInitSerialNumQty");
	var validB2BSerialNumAutoReplenishLevel = validateFields(formId,
			"b2bSerialNumAutoReplenishLevel");
	var validB2BSerialNumAutoReplenishVal = validateFields(formId,
			"b2bSerialNumAutoReplenishVal");
	var validDcmsId = validateFields(formId, "dcmsId");*/

	var productRetailTypeValid = $("#productRetailType").is(":checked");
	var ProductUPCTypeValid = $("#productB2BType").is(":checked");
	var RetailValid = validateFields(formId, "productRetailUPC");
	var UpcValid = validateFields(formId, "productB2BUPC");
	var validValidityPeriod = validateFields(formId, "validityPeriod");

	var validDenomActivation = validateDropDown(formId, "denominationId");

	var denominationId = $("#" + formId + " #denominationId").val();

	var validCutOverTime = validateCutoverTime(formId, "cutoverTime");
	var validActiveFrom = true;
	var activeFrom = $("#" + formId + " #activeFrom").val();
	var activeFromOld = $("#activeFromOld").val();

	if ((!(activeFrom == activeFromOld)) || activeFrom == null
			|| activeFrom == '')
		validActiveFrom = validateDate(formId, "activeFrom");
	var validProductValidityDate = true;
	var productValidityDate = $("#" + formId + " #productValidityDate").val();
	var productValidityDateOld = $("#productValidityDateOld").val();

	if ((!(productValidityDate == productValidityDateOld)))
		validProductValidityDate = validateDate(formId, "productValidityDate");

	var formFactorDigitalValid = $("#formFactorDigital").is(":checked");
	var digitalInvAuotReplLevel = validateFields(formId,
			"digitalInvAutoReplLvl");
	var initialDigitalInvQty = validateFields(formId, "initialDigitalInvQty");
	var digitalInvAutoReplQty = validateFields(formId, "digitalInvAutoReplQty");

	var validDefaultPurse = validateDropDown(formId, "defaultPurse");
	var validDefaultPackage = validateDropDown(formId, "defaultPackage");
	var validCcfFormatVersion = validateDropDown(formId, "ccfFormatVersion");
	var validB2BProductFunding = true;
	var validSrcOfFunding = true;

	var currency = $("input[id='internationalSupport']:checked").val();

	if (ProductUPCTypeValid) {
		validB2BProductFunding = validateDropDown(formId, "b2bProductFunding");

		if ($("#" + formId + " #b2bProductFunding").val() == 'CARD_ACTIVATION') {
			validSrcOfFunding = validateDropDown(formId, "sourceOfFunding");
		} else {
			clearError("sourceOfFunding");
		}

	} else {
		clearError("b2bProductFunding");
		clearError("productB2BUPC");

	}
	var multiselect = false;
	var purseselect = false;
	var packageselect = false;
	var partnercurrencyselect = true;

	$('#multiselect_to option').prop('selected', true);
	var options = $('#multiselect_to > option:selected');
	if (options.length == 0) {

		generateAlert(formId, "multiselect_to", "multiselect_to.cardRange");

	} else {
		clearError("multiselect_to");
		multiselect = true;
	}

	$('#purse_to option').prop('selected', true);
	var ops = $('#purse_to > option:selected');
	if (ops.length == 0) {

		generateAlert(formId, "purse_to", "purse_to.purse");

	} else {
		clearError("purse_to");
		purseselect = true;
	}

	if (currency == "Enable") {
		$('#partnerCurrency_to option').prop('selected', true);
		var optionsCurrency = $('#partnerCurrency_to > option:selected');
		if (optionsCurrency.length == 0) {
			generateAlert(formId, "partnerCurrency_to",
					"partnerCurrency_to.noCurrencySupportes");
			partnercurrencyselect = false;
		} else {
			clearError("partnerCurrency_to");
			partnercurrencyselect = true;
		}
	} else {
		clearError("partnerCurrency_to");
		partnercurrencyselect = true;
	}

	$('#package_to option').prop('selected', true);
	var optn = $('#package_to > option:selected');
	if (optn.length == 0) {
		generateAlert(formId, "package_to", "package_to.packageId");
	} else {
		clearError("package_to");
		packageselect = true;
	}

	var validDenomFixed = true;
	var validDenomMinField = true;
	var validDenomMaxField = true;
	var digitalFlag = true;

	if (formFactorDigitalValid) {
		digitalFlag = false;

		if (digitalInvAuotReplLevel && initialDigitalInvQty
				&& digitalInvAutoReplQty) {
			digitalFlag = true;

		}

	}

	if (denominationId == "Fixed") {

		//Fixed text box should not be empty

		var validFixed = validateDecimalFormat(formId, "denomFixedField");

		if (validFixed) {
			var denomFixed = $("#denomFixedField").val();
			/*
			if(denomFixed==0){
				generateAlert(formId, "denomFixedField", "denomFixedField.notZero");
				validDenomFixed=false;
			}
			else{*/

			var decimalpoint = DecimalValueFormat($("#denomFixedField").get(0));
			if (!DecimalValueFormat($("#denomFixedField").get(0))) {
				generateAlert(formId, "denomFixedField",
						"denomFixedField.notDecimal");
				validDenomFixed = false;
				/*	}*/

			}

		} else {
			validDenomFixed = false;
		}

	} else if (denominationId == "Variable") {
		//Min & Max textbox should not be empty
		clearError(formId + " #" + "denomFixedField");

		var minVal = parseFloat($('#denomMinField').val());
		var maxVal = parseFloat($('#denomMaxField').val());

		validDenomMinField = validateDecimalFormat(formId, "denomMinField");
		validDenomMaxField = validateDecimalFormat(formId, "denomMaxField");

		/*	if(validDenomMinField){
				var denomMin=$("#denomMinField").val();
				if(denomMin==0){
					generateAlert(formId, "denomMinField", "denomMinField.notZero");
					validDenomMinField=false;
				}
			}
			if(validDenomMaxField){
				var denomMax=$("#denomMaxField").val();
				if(denomMax==0){
					generateAlert(formId, "denomMaxField", "denomMaxField.notZero");
					validDenomMaxField=false;
				}
			}*/

		if (validDenomMinField && validDenomMinField) {
			var isFlag = true;
			var decimalpoint = DecimalValueFormat($("#denomMinField").get(0));
			if (!DecimalValueFormat($("#denomMinField").get(0))) {
				generateAlert(formId, "denomMinField",
						"denomFixedField.notDecimal");
				isFlag = false;
			}
			var decimalpoint = DecimalValueFormat($("#denomMaxField").get(0));
			if (!DecimalValueFormat($("#denomMaxField").get(0))) {
				generateAlert(formId, "denomMaxField",
						"denomFixedField.notDecimal");
				isFlag = false;
			}
			if (!isFlag) {
				return flag;
			}

			if (minVal > maxVal) {

				generateAlert(formId, "denomMinField", "denomMinField.minCheck");
				return false;
			} else if (minVal == maxVal)

			{
				generateAlert(formId, "denomMaxField",
						"denomMaxField.equalCheck");

				return false;
			}

		} else {
			validDenomMinField = false;
			validDenomMaxField = false;

		}
	} else {
		//Select Textbox(Multi-select) value should not be empty
		var denom_toValue = $("#denom_to option").html();

		var opt = $('#denom_to > option');
		if (opt.length == 0) {
			generateAlert(formId, "denom_to", "denom_to.available");
			return false;

		} else {

			clearError("denom_to");
			/*var denom_toValue=$("#denom_to option").html();
			if(denom_toValue==0){
				generateAlert(formId, "denom_to","denom_to.notZero");
				return false;
			}*/
		}

	}

	var validCheckActivation = checkact();
	var validCheckOtherTxns = checkothertxns();

	/*var formFactorPhysicalValid = $("#formFactorPhysical").is(":checked"); 
	var validCardEncoding = true;
	
	if(formFactorPhysicalValid)
		{
		validCardEncoding = validateDropDown(formId,"cardEncoding");
		}*/

	if (validProductName
			&& validProductShortName
			&& validProgramId
			&& validDenomActivation
			&& validValidityPeriod
			&& ((productRetailTypeValid && RetailValid) || (ProductUPCTypeValid && UpcValid))
			&& digitalFlag && validCutOverTime && validActiveFrom
			&& validDefaultPurse && validDefaultPackage
			&& validCcfFormatVersion && validDenomFixed && multiselect
			&& purseselect && partnercurrencyselect && packageselect
			&& validProductValidityDate && validDenomMinField
			&& validDenomMaxField && validProductPartnerName
			&& validProductIssuerName && multiselect && purseselect
			&& packageselect && validB2BProductFunding && validSrcOfFunding
			&& validCheckActivation && validCheckOtherTxns) {
		return true;
	} else {
		return false;
	}

}

function saveProductData(formId, event, Object) {
	event.preventDefault();

	var validProductName = validateFields(formId, "productName");
	var validProductShortName = validateFields(formId, "productShortName");

	var validProductPartnerName = validateDropDown(formId, "productPartnerName");
	var validProgramId = validateDropDown(formId, "programId");
	var validProductIssuerName = validateDropDown(formId, "productIssuerName");
	var validB2BInitSerialNumQty = validateFields(formId, "b2bInitSerialNumQty");
	var validB2BSerialNumAutoReplenishLevel = validateFields(formId,
			"b2bSerialNumAutoReplenishLevel");
	var validB2BSerialNumAutoReplenishVal = validateFields(formId,
			"b2bSerialNumAutoReplenishVal");
	var validDcmsId = validateFields(formId, "dcmsId");
	var validValidityPeriod = validateFields(formId, "validityPeriod");
	var validDenomActivation = validateDropDown(formId, "denominationId");

	var denominationId = $("#" + formId + " #denominationId").val();
	var productRetailTypeValid = $("#productRetailType").is(":checked");
	var ProductUPCTypeValid = $("#productB2BType").is(":checked");

	var RetailValid = validateFields(formId, "productRetailUPC");
	var UpcValid = validateFields(formId, "productB2BUPC");
	var validCutOverTime = validateCutoverTime(formId, "cutoverTime");
	var validActiveFrom = validateDate(formId, "activeFrom");
	var validProductValidityDate = validateDate(formId, "productValidityDate");
	var validDefaultPurse = validateDropDown(formId, "defaultPurse");
	var validDefaultPackage = validateDropDown(formId, "defaultPackage");
	var validCcfFormatVersion = validateDropDown(formId, "ccfFormatVersion");

	var formFactorDigitalValid = $("#formFactorDigital").is(":checked");
	var digitalInvAuotReplLevel = validateFields(formId,
			"digitalInvAutoReplLvl");
	var initialDigitalInvQty = validateFields(formId, "initialDigitalInvQty");
	var digitalInvAutoReplQty = validateFields(formId, "digitalInvAutoReplQty");

	var currency = $("input[id='internationalSupport']:checked").val();

	var digitalFlag = true;

	if (formFactorDigitalValid) {
		digitalFlag = false;

		if (digitalInvAuotReplLevel && initialDigitalInvQty
				&& digitalInvAutoReplQty) {
			digitalFlag = true;

		}

	}

	var validB2BProductFunding = true;
	var validSrcOfFunding = true;

	if (ProductUPCTypeValid) {
		validB2BProductFunding = validateDropDown(formId, "b2bProductFunding");

		if ($("#" + formId + " #b2bProductFunding").val() == 'CARD_ACTIVATION') {
			validSrcOfFunding = validateDropDown(formId, "sourceOfFunding");
		} else {
			clearError("sourceOfFunding");
		}

	} else {
		clearError("b2bProductFunding");
		clearError("productB2BUPC");
	}

	var multiselect = false;
	var purseselect = false;
	var packageselect = false;
	var partnercurrencyselect = true;

	$('#multiselect_to option').prop('selected', true);
	var options = $('#multiselect_to > option:selected');
	if (options.length == 0) {

		generateAlert(formId, "multiselect_to", "multiselect_to.cardRange");

	} else {
		clearError("multiselect_to");
		multiselect = true;
	}

	if (currency == "Enable") {
		$('#partnerCurrency_to option').prop('selected', true);
		var optionsCurrency = $('#partnerCurrency_to > option:selected');
		if (optionsCurrency.length == 0) {
			generateAlert(formId, "partnerCurrency_to",
					"partnerCurrency_to.noCurrencySupportes");
			partnercurrencyselect = false;
		} else {
			clearError("partnerCurrency_to");
			partnercurrencyselect = true;
		}
	} else {
		clearError("partnerCurrency_to");
		partnercurrencyselect = true;
	}

	$('#purse_to option').prop('selected', true);
	var ops = $('#purse_to > option:selected');
	if (ops.length == 0) {

		generateAlert(formId, "purse_to", "purse_to.purse");

	} else {
		clearError("purse_to");
		purseselect = true;
	}

	$('#package_to option').prop('selected', true);
	var optn = $('#package_to > option:selected');
	if (optn.length == 0) {
		generateAlert(formId, "package_to", "package_to.packageId");

	} else {
		clearError("package_to");
		packageselect = true;
	}

	var validDenomFixed = true;
	var validDenomMinField = true;
	var validDenomMaxField = true;

	if (denominationId == "Fixed") {

		//Fixed text box should not be empty

		validDenomFixed = validateDecimalFormat(formId, "denomFixedField");

		if (validDenomFixed) {
			var denomFixed = $("#denomFixedField").val();
			/*if(denomFixed==0){
				generateAlert(formId, "denomFixedField", "denomFixedField.notZero");
				validDenomFixed=false;
			}
			else{*/

			var decimalpoint = DecimalValueFormat($("#denomFixedField").get(0));
			if (!DecimalValueFormat($("#denomFixedField").get(0))) {
				generateAlert(formId, "denomFixedField",
						"denomFixedField.notDecimal");
				/*}*/
			}

		}

	} else if (denominationId == "Variable") {
		//Min & Max textbox should not be empty
		clearError(formId + " #" + "denomFixedField");

		var minVal = parseFloat($('#denomMinField').val());
		var maxVal = parseFloat($('#denomMaxField').val());

		validDenomMinField = validateDecimalFormat(formId, "denomMinField");
		validDenomMaxField = validateDecimalFormat(formId, "denomMaxField");

		/*if(validDenomMinField){
			var denomMin=$("#denomMinField").val();
			
			if(denomMin==0){
				generateAlert(formId, "denomMinField", "denomMinField.notZero");
				validDenomMinField=false;
			}
		}
		if(validDenomMaxField){
			var denomMax=$("#denomMaxField").val();
			if(denomMax==0){
				generateAlert(formId, "denomMaxField", "denomMaxField.notZero");
				validDenomMaxField=false;
			}
		}*/

		if (validDenomMinField && validDenomMinField) {
			var isFlag = true;
			var decimalpoint = DecimalValueFormat($("#denomMinField").get(0));
			if (!DecimalValueFormat($("#denomMinField").get(0))) {
				generateAlert(formId, "denomMinField",
						"denomFixedField.notDecimal");
				isFlag = false;
			}
			var decimalpoint = DecimalValueFormat($("#denomMaxField").get(0));
			if (!DecimalValueFormat($("#denomMaxField").get(0))) {
				generateAlert(formId, "denomMaxField",
						"denomFixedField.notDecimal");
				isFlag = false;
			}
			if (!isFlag) {
				return flag;
			}

			if (minVal > maxVal) {

				generateAlert(formId, "denomMinField", "denomMinField.minCheck");
				return false;
			} else if (minVal == maxVal)

			{
				generateAlert(formId, "denomMaxField",
						"denomMaxField.equalCheck");

				return false;
			}
			/*else{
				var decimalpoint=DecimalValueFormat($("#denomMinField").get(0));
				if(!DecimalValueFormat($("#denomMinField").get(0))){
					generateAlert(formId, "denomMinField", "denomFixedField.notDecimal");
					return false;
				}
				var decimalpoint=DecimalValueFormat($("#denomMaxField").get(0));
				if(!DecimalValueFormat($("#denomMaxField").get(0))){
					generateAlert(formId, "denomMaxField", "denomFixedField.notDecimal");
					return false;
				}
				
			}*/

		} else {
			validDenomMinField = false;
			validDenomMaxField = false;

		}
	}

	else {

		//Select Textbox(Multi-select) value should not be empty
		var denom_toValue = $("#denom_to option").html();

		var opt = $('#denom_to > option');
		if (opt.length == 0) {
			generateAlert(formId, "denom_to", "denom_to.available");
			return false;

		} else {

			clearError("denom_to");
			var denom_toValue = $("#denom_to option").html();
			/*if(denom_toValue==0){
				generateAlert(formId, "denom_to","denom_to.notZero");
				return false;
			}*/
		}

	}

	var validCheckActivation = checkact();
	var validCheckOtherTxns = checkothertxns();

	/*var formFactorPhysicalValid = $("#formFactorPhysical").is(":checked"); 
	var validCardEncoding = true;
	
	if(formFactorPhysicalValid)
		{
		validCardEncoding = validateDropDown(formId,"cardEncoding");
		}*/

	if (validProductName
			&& validProductShortName
			&& validProgramId
			&& validB2BInitSerialNumQty
			&& validB2BSerialNumAutoReplenishLevel
			&& validB2BSerialNumAutoReplenishVal
			&& validDcmsId
			&& ((productRetailTypeValid && RetailValid) || (ProductUPCTypeValid && UpcValid))
			&& digitalFlag && validValidityPeriod && validDenomActivation
			&& validCutOverTime && validActiveFrom && validDefaultPurse
			&& validDefaultPackage && validCcfFormatVersion && validDenomFixed
			&& validDenomMinField && validDenomMaxField && multiselect
			&& purseselect && partnercurrencyselect && packageselect
			&& validProductIssuerName && validProductValidityDate
			&& validB2BProductFunding && validSrcOfFunding
			&& validCheckActivation && validCheckOtherTxns) {

		$("#addProductForm").attr('action', 'addProduct');
		$("#addProductForm").submit();

	}
}
/*To validate AlphaNumeric and space value, it replaces the characters other than alphabets,spaces and numbers by ''*/
function isAlphaNumericWithSpace(ctrl) {
	var isValid = false;
	var regex = /[a-zA-Z0-9 ]+$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ]+$/, '');
	if (ctrl.value.startsWith(' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	return isValid;
}

/*To validate AlphaNumeric value, it replaces the characters other than alphabets and numbers by ''*/
function isAlphaNumeric(ctrl) {
	var isValid;
	var regex = /^[a-zA-Z0-9]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9]/g, '');
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

function validateNonManFields(formId, eleId, mandatory) {

	var isValid = false;
	var minLen = readValidationProp(eleId + '.min.length');
	var eleVal = $("#" + formId + " #" + eleId).val();
	clearError(formId + " #" + eleId);
	var pattern = new RegExp(readValidationProp(eleId + '.pattern'));

	if (mandatory) {
		if ((eleVal == null || eleVal == '') && minLen != 0) {
			generateAlert(formId, eleId, eleId + ".empty");
			return isValid;
		}

	}

	if (eleVal.trim().length > readValidationProp(eleId + '.max.length')) {
		generateAlert(formId, eleId, eleId + ".length");
	} else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) { //
		generateAlert(formId, eleId, eleId + ".pattern");
	} else {
		clearError(formId + " #" + eleId);

		isValid = true;
	}

	return isValid;

}

function validateServiceCode(formId, eleId) {
	var isValid = false;
	var minLen = readValidationProp(eleId + '.min.length');
	var eleVal = $("#" + formId + " #" + eleId).val();
	clearError(formId + " #" + eleId);
	var pattern = new RegExp(readValidationProp(eleId + '.pattern'));

	if ((eleVal != null && eleVal != '') && eleVal == 0) {
		generateAlert(formId, eleId, eleId + ".zero");
	}

	else if ((eleVal.trim().length > readValidationProp(eleId + '.max.length'))) {
		generateAlert(formId, eleId, eleId + ".length");
	} else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) { //
		generateAlert(formId, eleId, eleId + ".pattern");
	} else {
		clearError(formId + " #" + eleId);

		isValid = true;
	}

	return isValid;

}
function validateTxnCount(formId, eleId) {

	var isValid = false;
	var minLen = readValidationProp(eleId + '.min.length');
	var eleVal = $("#" + formId + " #" + eleId).val();
	clearError(formId + " #" + eleId);
	var pattern = new RegExp(readValidationProp(eleId + '.pattern'));

	/*	 if((eleVal!=null && eleVal!='') &&(eleVal<=0 || ( eleVal >10))){ */
	if ((eleVal == null || eleVal == '' || eleVal.length == 0)) {
		generateAlert(formId, eleId, eleId + ".empty");
		return isValid;
	} else {
		if ((eleVal == 0 || (eleVal > 10))) {
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

function goBackToProduct() {

	$("#addProductForm").attr('action', 'productConfig');
	$("#addProductForm").submit();
}

function getCvvDetails() {

	if ($("#parentProductId  option:selected").val() == -1) {
		document.getElementById("parentProductIdCvvError").innerHTML = '<font color="red">'
				+ readMessage("parentProductIdCvvError.empty") + "</font>";
		return false;
	}
	clearError("parentProductIdCvvError");
	$("#productCVV").attr('action', 'getCvvDetailsById')
	$("#productCVV").submit();
}

function saveCVVTab(formId, event) {

	var cvkKeySpecifier = validateCvv(formId, "cvkKeySpecifier");
	var cvkIndex = validateCvv(formId, "cvkIndex");
	var cscKey = validateCvv(formId, "cscKey");

	if (cvkKeySpecifier && cvkIndex && cscKey) {
		$("#productCVV").attr('action', 'saveCVVDetails')
		$("#productCVV").submit();
	}
}

function validateCvv(formId, eleId) {

	var isValid = false;
	var eleVal = $("#" + formId + " #" + eleId).val();
	clearError(formId + " #" + eleId);
	var pattern = new RegExp(readValidationProp(eleId + '.pattern'));

	if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) {
		generateAlert(formId, eleId, eleId + ".pattern");
	} else {
		clearError(formId + " #" + eleId);

		isValid = true;
	}

	return isValid;

}

function cvvFunc() {

	var cvvParameters = $("#cvvParameters");
	var cvvParameters_hsm = $("#cvvParametershsm");
	var cscParameters = $("#cscParameters");
	var cvkKeySpec = $("#cvkKeySpec");

	var embossApplicable = $("#embossApplicable_Enable").is(":checked");
	var cvkFormatHsm = $("#cvkFormat_Enable").is(":checked");
	var cvkFormatHost = $("#cvkFormat_Disable").is(":checked");
	var cvv = $("#cardVerifyType_Cvv").is(":checked");
	var csc = $("#cardVerifyType_Csc").is(":checked");

	if (embossApplicable && cvkFormatHost && cvv) {
		$(cvvParameters).show();
		$(cvkKeySpec).show();
		$(cvvParameters_hsm).hide();
		$(cscParameters).hide();
	} else if (embossApplicable && (cvkFormatHost || cvkFormatHsm) && csc) {
		$(cscParameters).show();
		$(cvvParameters).hide();
		$(cvvParameters_hsm).hide();
		$(cvkKeySpec).hide();
	} else if (embossApplicable && cvkFormatHsm && cvv) {
		$(cvvParameters_hsm).show();
		$(cvkKeySpec).show();
		$(cvvParameters).hide();
		$(cscParameters).hide();
	}
}

function embossNotApplicable() {
	var cvvParameters = document.getElementById("cvvParameters");
	var cvvParameters_hsm = document.getElementById("cvvParametershsm");
	var cscParameters = document.getElementById("cscParameters");
	var cvkKeySpec = $("#cvkKeySpec");

	var embossNotApplicable = $("#embossApplicable_Disable").is(":checked");

	if (embossNotApplicable) {
		$(cvvParameters).hide();
		$(cscParameters).hide();
		$(cvvParameters_hsm).hide();
		$(cvkKeySpec).hide();

	}
}

function goBackToFormproductConfig() {
	$("#editProductForm").attr('action', 'productConfig');
	$("#editProductForm").submit();

}

function includeRespDiv() {

	var option = "";

	option = $("#denominationId").val();

	if (option == "Fixed") {
		$("#denomFixed").attr("style", "display:block");
		$("#denomSelect").attr("style", "display:none");
		$("#denomVar").attr("style", "display:none");
	}
	if (option == "Select") {
		$("#denomSelect").attr("style", "display:block");
		$("#denomFixed").attr("style", "display:none");
		$("#denomVar").attr("style", "display:none");
	}
	if (option == "Variable") {
		$("#denomVar").attr("style", "display:block");
		$("#denomSelect").attr("style", "display:none");
		$("#denomFixed").attr("style", "display:none");
	}
	if (option == "NONE") {
		$("#denomVar").attr("style", "display:none");
		$("#denomSelect").attr("style", "display:none");
		$("#denomFixed").attr("style", "display:none");
	}

}
/******************************Gerneral tab js function***********/
function validateURL(formId, eleId) {

	var isValid = false;
	var pattern = /^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\(\)\*\+,;=.]+$/gm;
	var eleVal = $("#" + formId + " #" + eleId).val();

	if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) {
		generateAlert(formId, eleId, eleId + ".pattern");
		return isValid;
	} else {
		clearError(formId + " #" + eleId);
		isValid = true;

	}
	return isValid;
}

function validateEmail(formId, eleId) {

	var isValid = false;
	var pattern = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	var eleVal = $("#" + formId + " #" + eleId).val();

	if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) {
		generateAlert(formId, eleId, eleId + ".pattern");
		return isValid;
	} else {
		clearError(formId + " #" + eleId);
		isValid = true;

	}
	return isValid;
}

function validateGeneralFields(formId, eleId) {

	var isValid = false;
	var eleVal = $("#" + formId + " #" + eleId).val();
	clearError(formId + " #" + eleId);
	var pattern = new RegExp(readValidationProp(eleId + '.pattern'));

	if (eleVal.trim().length > readValidationProp(eleId + '.max.length')) {
		generateAlert(formId, eleId, eleId + ".length");
	} else if ((eleVal != null && eleVal != '') && !pattern.test(eleVal)) {
		generateAlert(formId, eleId, eleId + ".pattern");
	} else {
		clearError(formId + " #" + eleId);

		isValid = true;
	}

	return isValid;

}

function generalSubmit(formId, event) {

	var txnCountForRecentStatement = validateTxnCount(formId,
			"txnCountForRecentStatement");
	var serviceCode = validateServiceCode(formId, "serviceCode");
	var url = validateGeneralFields(formId, "URLForROBOHelpLabel");
	var customerCareNumber = validateGeneralFields(formId, "customerCareNumber");
	var email = validateEmail(formId, "emailIDForStatementLabel");
	var URL = validateURL(formId, "URLForROBOHelpLabel");

	if (txnCountForRecentStatement && serviceCode && url && customerCareNumber
			&& email && URL) {

		$("#" + formId).attr('action', 'saveGeneralDetails');
		$("#" + formId).submit();
	} else {
		return false;
	}

}

function getParentProductDetails() {
	if ($("#parentProductId  option:selected").val() == -1) {
		document.getElementById("parentProductIdError").innerHTML = '<font color="red">'
				+ readMessage("parentProductId.empty") + "</font>";
		return false;
	}
	clearError("parentProductIdError");
	$("#productGeneral").attr('action', 'getProductDetailsById')
	$("#productGeneral").submit();

}

/******************************Gerneral tab js function***********/

function saveTxnStat() {
	$("#txnBasedOnStat").attr('action', 'saveTxnOnCardStatById')
	$("#txnBasedOnStat").submit();

}

function getTxnStatDetails() {
	if ($("#parentProductId  option:selected").val() == -1) {
		document.getElementById("parentProductIdError").innerHTML = '<font color="red">'
				+ readMessage("parentProductIdCardStatusError.empty")
				+ "</font>";
		return false;
	} else {

		$("#txnBasedOnStat").attr('action', 'getTxnStatDetails')
		$("#txnBasedOnStat").submit();
	}
}

/*****  COPY ADD PRODUCT ****/
///
/****copy from in add********/

function getProductDetails() {
	var test = $("#combobox").val();
	if (test == '' || test == null) {
		document.getElementById("parentProductIdError").innerHTML = '<font color="red">'
				+ readMessage("parentProductIdAdd.empty") + "</font>";
		return false;
	}
	clearError("parentProductIdError");
	$("#addProductForm").attr('action', 'getProductDetails')
	$("#addProductForm").submit();

}

/*
 * copy function in alerts tab
 */
function getAlertProductDetails() {
	if ($("#parentProductIdAlert  option:selected").val() == -1) {
		document.getElementById("parentProductIdError").innerHTML = '<font color="red">'
				+ readMessage("parentProductIdAlert.empty") + "</font>";
		return false;
	}
	clearError("parentProductIdError");
	$("#productAlerts").attr('action', 'getAlertProductDetails')
	$("#productAlerts").submit();

}

function goResetProduct() {
	$("#addProductForm").attr('action', 'showAddProduct');
	$("#addProductForm").submit();

}/*
	$("#addProductForm").get().reset();
	$('select option[value="-1"]'.prop("selected",true));*/

function isAlphaNumericWithNewSpecialCharsProdName(ctrl) {

	var isValid;
	var regex = /^[a-zA-Z0-9]+[a-zA-Z0-9 \\!@:;#$%^&*,.( )\+\-_=\/{}[]\|'?\>\<~`]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(
			/[^a-zA-Z0-9 \\!@:;#$%^&*,.( )\+\-_=\/{}[]\|'?\>\<~`]*/g, '');
	ctrl.value = ctrl.value.replace("\"", "");
	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	return isValid;
}
//show view product
function showViewProduct(id) {
	var code = id;
	$("#productId").val(code);
	$("#productFormId").attr('action', 'showViewProduct');
	$("#productFormId").submit();
}
