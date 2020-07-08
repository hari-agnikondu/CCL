function clickAddProgramID() {
	$("#programIdForm").attr('action', 'showAddProgramId');
	$("#programIdForm").submit();
}

function clickUpdateProgramIDAttributes() {
	$("#programIdForm").attr('action', 'showUpdateProgramId');
	$("#programIdForm").submit();

}

function searchProgramID(event) {

	event.preventDefault();
	var programIdName = "";
	var code = 'byNameID';

	programIdName = $("#programIdNameId").val();

	if (programIdName === undefined || programIdName == null
			|| programIdName.length <= 0 || programIdName == '') {
		var code = 'all';
		$("#searchType").val(code);
		$("#programIdForm").attr('action', 'showAllProgramIDs');
		$("#programIdForm").submit();
	} else {
		$("#searchType").val(code);
		$("#programIdForm").attr('action', 'showAllProgramIDsByName');
		$("#programIdForm").submit();
	}

}

function goEditProgramID(id) {

	var code = id;
	$("#programID").val(code);
	$("#programIdForm").attr('action', 'showEditProductID');
	$("#programIdForm").submit();
}

function goViewProgramID(id) {

	var code = id;
	$("#programID").val(code);
	$("#programIdForm").attr('action', 'showViewProgramID');
	$("#programIdForm").submit();
}

function goAddProgramId(formId) {
	var validName = validateFields(formId, "programIdName");
	var validDesc = validateFields(formId, "programDesc");
	var validpart = validateDropDownpartner(formId, "programPartnerName");

	if (validName && validDesc && validpart) {

		$("#programIdForm").attr('action', 'addProgramId');
		$("#programIdForm").submit();
	}

}

function goBackToProgramId() {
	$("#programIdForm").attr('action', 'programIdConfig');
	$("#programIdForm").submit();

}

function ResetAddProgramID(formId) {

	$("#" + formId + " #programIdName").val('');
	$("#" + formId + " #programDesc").val('');
	$('select option[value="-1"]').prop("selected", true);
	$("#statusProgramIdAdd").html('');
	$("#formErrorId").html('');
	clearError(formId + " #programIdName");
	clearError(formId + " #programPartnerName");
	clearError(formId + " #programDesc");
}

function resetEditProgramID(formId) {

	$("#" + formId + " #programDesc").val('');

	clearError(formId + " #programIdName");
	clearError(formId + " #programDesc");
}

function goConfirmProgramID(formId) {

	if (checkUpdateProgramId(formId)) {
		$("#Button").attr("data-target", "#define-constant-update");
		var id = $('#editprogramId').val();
		var name = $('#programIdName').val();
		name = name.trim();
		$("#programIdtoUpdate").val(name);
		document.getElementById("programIdNameDisp").innerHTML = name;
		$("#programID").val(id);
		$("#editprogramId").val(id);
	} else {
		$("#Button").removeAttr("data-target");
	}
}

function checkUpdateProgramId(formId) {
	var validName = validateFields(formId, "programIdName");
	var validDesc = validateFields(formId, "programDesc");
	if (validName && validDesc) {
		return true;
	} else {
		return false;
	}
}

function goUpdateProgramID() {

	$("#programIdForm").attr('action', 'updateProgramId');
	$("#programIdForm").submit();
}

var rowidforcolor = "";
function goConfirmDeleteProgramID(data) {
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
	$("#programIdToDelete").val(name);
	document.getElementById("programIdDisp").innerHTML = name;
	var nametosearch = $("#programIdNameId").val();
	$("#programID").val(id);
	$("#deletedName").val(name);
	$("#searchedName").val(nametosearch);

}

function goToPrevious() {
	document.getElementById(rowidforcolor).style.background = "#f9f9f9";

}
function deleteProgramId() {
	$("#programIdForm").attr('action', 'deleteProgramID');
	$("#programIdForm").submit();

}

function validateDropDownpartner(formId, eleId) {
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

function validateDropDownPurse(formId, eleId, limitFeesVal) {
	var isValid = false;

	var eleVal = $("#" + formId + " #" + eleId).val();

	if ((eleVal.length <= 0 || eleVal == '-1')
			&& limitFeesVal != 'Maintenance Fees') {
		generateAlert(formId, eleId, eleId + ".unselect");
	} else {
		clearError(formId + " #" + eleId);
		isValid = true;
	}
	return isValid;
}

function getDescription() {
	$("#serviceErrMsg").html('');
	var proId = $("#programIdDropDownName option:selected").val();
	var splitted = proId.split('~');
	var id = splitted[0];
	var str = $('#srvUrl').val() + "/ajax/getProgramIdDescription/" + id;
	var str1 = $('#srvUrl').val() + "/ajax/getProgramIdPurses/" + id;

	$.ajax({
		url : str,

		async : true,

		type : "GET",

		dataType : 'json',

		success : function(response, status, xhr) {

			//console.log(response.description);
			$("#programDesc").val(response.description);
			//$("#descId").val(response.description);

		}/*,
		error : function(jqXHR, textStatus, errorThrown) {

			console.log("error:" + textStatus + " exception:" + errorThrown);
		}*/

	});
	
}

function getPurses(){
	$("#purseListId").html("");
	$("#serviceErrMsg").html('');
	var proId = $("#programIdDropDownName option:selected").val();
	var splitted = proId.split('~');
	var id = splitted[0];
	var options = '<option value="-1"><strong>--- Select ---</strong></option>';
	var str = $('#srvUrl').val() + "/ajax/getProgramIdPurses/" + id;
	$.ajax({
		url : str,

		async : true,

		type : "GET",

		dataType : 'json',

		success : function(response, status, xhr) {

			$.each(response,
					function(index) {
				
				options += '<option value="'+response[index].purseId+'">'
				+ response[index].extPurseId + '</option>';
			});
			$("#purseListId").html(options);
		}

	});
}

function clickSavePrgmId() {

	var limitsField = $("#limitsFeesId").val();

	var validprogramIdDropdown = validateDropDownpartner("programIdForm",
			"programIdDropDownName");
	var validdeliverychannelListId = validateDropDownpartner("programIdForm",
			"deliveryChannelListId");
	var validLimitsField = validateDropDownpartner("programIdForm",
			"limitsFeesId");
	var validTransactionField = validateDropDownpartner("programIdForm",
			"transactionListId");
	var validPurseField = validateDropDownPurse("programIdForm", "purseListId",
			limitsField);
	if (limitsField == 'Limits') {
		$("#feedBackTd").html('');
		var minAmtPerTxValidFlag = true;
		$("input[id$='_minAmtPerTx']").each(function(i, el) {

			if (!positiveNonEmptyAmount(el))
				minAmtPerTxValidFlag = false;

		});

		var maxAmtPerTxValidFlag = true;
		$("input[id$='_maxAmtPerTx']").each(function(i, el) {
			if (!maxAmtPerTxnValidation(el))
				maxAmtPerTxValidFlag = false;
		});

		var amtValidationFlag = maxAmtValidation();
		var countValidationFlag = maxCountValidation();

		if (minAmtPerTxValidFlag && maxAmtPerTxValidFlag && amtValidationFlag
				&& countValidationFlag && validprogramIdDropdown
				&& validdeliverychannelListId && validLimitsField
				&& validTransactionField) {
			$("#programIdForm")
					.attr('action', 'saveLimitAttributesByProgramId');
			$("#programIdForm").submit();
		}

	}

	if (limitsField == 'Transaction Fees') {
		$("#feedBackTd").html('');
		var isValidFeeDesc = true;
		var isValidClawbackCnt = true;
		var isValidFeeAmt = true;
		var isValidFeeCondn = true;
		var isValidFreeCnt = true;
		var isValidMaxCnt = true;

		$("input[id$='_feeDesc']").each(function(i, el) {
			if (!validateFeeDescription(el))
				isValidFeeDesc = false;
		});
		$("input[id$='_clawback']").each(
				function(i, el) {
					if ($(el).is(":checked")) {

						var ctrlId = el.id;
						var clawbackCntId = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_clawbackCount").attr('id');
						if (!validateClawbackCount($("#" + clawbackCntId))) {
							isValidClawbackCnt = false;
						}
					}

				});
		$("input[id$='_feeAmt']").each(function(i, el) {
			if (!validateFeeAmt(el))
				isValidFeeAmt = false;
		});

		$("select[id$='_feeCondition']").each(
				function(i, el) {
					var ctrlId = el.id;
					var val = $("#" + ctrlId + " option:selected").val();

					var minFeeAmtObj = $("#"
							+ ctrlId.substring(0, ctrlId.lastIndexOf("_"))
							+ "_minFeeAmt");
					var feePercentObj = $("#"
							+ ctrlId.substring(0, ctrlId.lastIndexOf("_"))
							+ "_feePercent");
					if (val == 'A') {
						if (!validateFeePercentage(feePercentObj))
							isValidFeeCondn = false;
						else {
							if (!validateMinFeeAmt(minFeeAmtObj)) {
								isValidFeeCondn = false;
							}
						}
					} else if (val == 'O') {
						if (!validateFeePercentage(feePercentObj))
							isValidFeeCondn = false;
					}

				});

		$("input[id$='_freeCount']")
				.each(
						function(i, el) {
							var freeCnt = $(el).val();
							var ctrlId = el.id;
							if (freeCnt != null && freeCnt.trim() != '') {
								if (isNaN(freeCnt) || parseInt(freeCnt) < 1) {
									generateAlert("programIdForm", ctrlId,
											"product.freeCnt.invalid");
									$(
											"#" + ctrlId.split('_', 1)
													+ ctrlId.split('_', 1))
											.css('visibility', 'visible');
									isValidFreeCnt = false;
								} else {
									clearError(ctrlId);
									$(
											"#" + ctrlId.split('_', 1)
													+ ctrlId.split('_', 1))
											.css('visibility', 'hidden');
								}
							}
						});

		$("input[id$='_maxCount']")
				.each(
						function(i, el) {
							var freeCnt = $(el).val();
							var ctrlId = el.id;
							if (freeCnt != null && freeCnt.trim() != '') {
								if (isNaN(freeCnt) || parseInt(freeCnt) < 1) {
									generateAlert("programIdForm", ctrlId,
											"product.maxCnt.invalid");
									$(
											"#" + ctrlId.split('_', 1)
													+ ctrlId.split('_', 1))
											.css('visibility', 'visible');
									isValidMaxCnt = false;
								} else {
									clearError(ctrlId);
									$(
											"#" + ctrlId.split('_', 1)
													+ ctrlId.split('_', 1))
											.css('visibility', 'hidden');
								}
							}
						});

		if (isValidFeeDesc && isValidClawbackCnt && isValidFeeAmt
				&& isValidFeeCondn && isValidFreeCnt && isValidMaxCnt
				&& validprogramIdDropdown && validdeliverychannelListId
				&& validLimitsField && validTransactionField) {
			$("#programIdForm").attr('action',
					'saveTxnFeeAttributesByProgramId');
			$("#programIdForm").submit();
		}

	}

	if (limitsField == 'Maintenance Fees') {
		$("#feedBackTd").html('');
		var isValidFeeDesc1 = true;
		var isValidFeeAmt1 = true;
		var isValidClawbackCnt1 = true;
		var isfirstMonthFeeAssessedDays1 = true;
		var isValidCapFeeAmt1 = true;
		var isValidFreeCnt1 = true;
		var isValidMaxCnt1 = true;

		$("input[id$='_feeDesc']").each(function(i, el) {
			if (!validateFeeDescription(el))
				isValidFeeDesc1 = false;
		});

		$("input[id$='_feeAmt']").each(function(i, el) {
			if (!validateFeeAmt(el))
				isValidFeeAmt1 = false;
		});

		$("input[id$='_clawback']").each(
				function(i, el) {
					if ($(el).is(":checked")) {

						var ctrlId = $(el).attr('id');
						var clawbackCntId = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_clawbackCount").attr('id');
						if (!validateClawbackCount($("#" + clawbackCntId))) {
							isValidClawbackCnt1 = false;
						}
					}

				});

		$("input[id$='_firstMonthFeeAssessedDays']").each(function(i, el) {
			if (!validateCount(el))
				isfirstMonthFeeAssessedDays1 = false;
		});

		$("input[id$='_capFeeAmt']").each(function(i, el) {
			if (!validMaintenanceCapFeeAmt(el))
				isValidCapFeeAmt1 = false;
		});

		$("input[id$='_freeCount']").each(function(i, el) {
			if (!validateCount(el))
				isValidFreeCnt1 = false;
		});
		$("input[id$='_maxCount']").each(function(i, el) {
			if (!validateCount(el))
				isValidMaxCnt1 = false;
		});

		if (isValidFeeDesc1 && isValidFeeAmt1 && isValidClawbackCnt1
				&& isfirstMonthFeeAssessedDays1 && isValidCapFeeAmt1
				&& isValidFreeCnt1 && isValidMaxCnt1 && validprogramIdDropdown
				&& validLimitsField && validTransactionField) {
			$("select[id$='_clawbackOption']").each(function(i, el) {
				var objId = $(el).attr('id');
				$("#" + objId).prop("disabled", false);
			});

			$("#programIdForm").attr('action',
					'saveMaintenanceFeeAttributesByProgramId');
			$("#programIdForm").submit();
		}
	}
	if (limitsField == 'Monthly Fee Cap') {
		$("#feedBackTd").html('');
		var feeDescValid = validateFeeDescription($("#monthlyFeeCap_feeDesc"));
		$("#deliveryChannelListId").next().html('');
		$("#transactionListId").next().html('');
		var feeAmtValid = validateFeeCapAmt();
		if (feeAmtValid && feeDescValid && validprogramIdDropdown
				&& validLimitsField) {
			$("#monthlyFeeCap_assessmentDate").prop('disabled', false);
			$("#programIdForm").attr('action',
					'saveMonthlyFeeCapAttributesByProgramId');
			$("#programIdForm").submit();
		}
	}

}

function allowOnlyNumbers(ctrl) {

	var isValid;
	var regex = /^[0-9]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^0-9 ]*/g, '');

	return isValid;
}

function positiveNonEmptyAmount(ctrl) {
	var isValid = true;
	var amount = ctrl.value;
	var ctrlId = ctrl.id;
	// var txnShortName=ctrlId.substring(ctrlId.split('_',
	// 1).join('_').length+1,ctrlId.split('_', 2).join('_').length);
	var txnShortName = ctrlId.slice(0, ctrlId.lastIndexOf('_'));
	if ($("input[name='" + txnShortName + "']").val() == 'Y') {

		if (amount != null && amount.trim() != '') {
			/*
			 * generateAlert("programIdForm",
			 * ctrl.id,"product.minAmtPerTxn.empty"); isValid= false;
			 */
			if (isNaN(amount) || parseFloat(amount) < 0) {
				generateAlert("programIdForm", ctrl.id,
						"product.minAmtPerTxn.invalid");
				isValid = false;
			} else if (!DecimalValueFormat(ctrl)) {
				isValid = false;
			} else {

				clearError(ctrlId);
				$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
						'visibility', 'hidden');
			}
		} else {

			clearError(ctrlId);
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'hidden');
		}

		if (!isValid) {
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
		}
	} else {
		clearError(ctrlId);
	}
	return isValid;
}
function maxAmtPerTxnValidation(ctrl) {
	var isValid = true;
	var maxAmount = ctrl.value;
	var ctrlId = ctrl.id;
	// var txnShortName=ctrlId.substring(ctrlId.split('_',
	// 1).join('_').length+1,ctrlId.split('_', 1).join('_').length);
	var txnShortName = ctrlId.slice(0, ctrlId.lastIndexOf('_'));
	if ($("input[name='" + txnShortName + "']").val() == 'Y') {

		var minAmt = $(
				"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
						+ "_minAmtPerTx").val();

		if (maxAmount != null && maxAmount.trim() != '') {
			/*
			 * generateAlert("programIdForm",
			 * ctrlId,"product.maxAmtPerTxn.empty"); isValid= false; }
			 */
			if (minAmt == null || minAmt.trim() == '') {
				generateAlert("programIdForm", $(
						"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
								+ "_minAmtPerTx").attr('id'),
						"product.minAmtPerTxn.empty");
				isValid = false;
			} else {
				positiveNonEmptyAmount(document.getElementById(ctrlId
						.substring(0, ctrlId.lastIndexOf("_"))
						+ "_minAmtPerTx"));
			}
			if (isNaN(maxAmount) || parseFloat(maxAmount) <= 0) {
				generateAlert("programIdForm", ctrlId,
						"product.maxAmtPerTxn.invalid");
				isValid = false;
			} else if (!DecimalValueFormat(ctrl)) {
				isValid = false;
			} else if (minAmt != null && minAmt.trim() != ''
					&& parseFloat(maxAmount) < parseFloat(minAmt)) {
				generateAlert("programIdForm", ctrlId,
						"product.maxAmtPerTxn.lesser");
				isValid = false;
			} else {
				clearError(ctrlId);
				$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
						'visibility', 'hidden');
			}
		} else {
			clearError(ctrlId);
			clearError($(
					"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
							+ "_minAmtPerTx").attr('id'));
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'hidden');
		}
		if (!isValid) {
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
		}
	} else {
		clearError(ctrlId);
		clearError($(
				"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
						+ "_minAmtPerTx").attr('id'));
	}
	return isValid;
}

function maxAmtValidation() {

	var isValid = true;

	$("input[id$='_dailyMaxAmt']")
			.each(
					function(i, el) {

						var ctrlId = el.id;
						var minAmtPerTxn = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_minAmtPerTx").val();
						var maxAmtPerTxn = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_maxAmtPerTx").val();

						// var txnShortName=ctrlId.substring(ctrlId.split('_',
						// 1).join('_').length+1,ctrlId.split('_',
						// 2).join('_').length);
						var txnShortName = ctrlId.slice(0, ctrlId
								.lastIndexOf('_'));
						if ($("input[name='" + txnShortName + "']").val() == 'Y') {

							if ($(el).val() != null && $(el).val().trim() != '') {

								if (maxAmtPerTxn == null
										|| maxAmtPerTxn.trim() == '') {
									generateAlert(
											"programIdForm",
											$(
													"#"
															+ ctrlId
																	.substring(
																			0,
																			ctrlId
																					.lastIndexOf("_"))
															+ "_maxAmtPerTx")
													.attr('id'),
											"product.maxAmtPerTxn.empty");
									isValid = false;
								} else if (!maxAmtPerTxnValidation(document
										.getElementById(ctrlId.substring(0,
												ctrlId.lastIndexOf("_"))
												+ "_maxAmtPerTx"))) {
									isValid = false;
								} else {
									clearError($(
											"#"
													+ ctrlId
															.substring(
																	0,
																	ctrlId
																			.lastIndexOf("_"))
													+ "_maxAmtPerTx")
											.attr('id'));
								}
								if (minAmtPerTxn == null
										|| minAmtPerTxn.trim() == '') {
									generateAlert(
											"programIdForm",
											$(
													"#"
															+ ctrlId
																	.substring(
																			0,
																			ctrlId
																					.lastIndexOf("_"))
															+ "_minAmtPerTx")
													.attr('id'),
											"product.minAmtPerTxn.empty");
									isValid = false;
								} else if (!positiveNonEmptyAmount(document
										.getElementById(ctrlId.substring(0,
												ctrlId.lastIndexOf("_"))
												+ "_minAmtPerTx"))) {
									isValid = false;
								} else {
									clearError($(
											"#"
													+ ctrlId
															.substring(
																	0,
																	ctrlId
																			.lastIndexOf("_"))
													+ "_minAmtPerTx")
											.attr('id'));
								}
								if (!DecimalValueFormat(el)) {
									isValid = false;
								} else if (isNaN($(el).val())) {
									generateAlert("programIdForm", ctrlId,
											"product.NaN");
									isValid = false;
								} else if (maxAmtPerTxn != null
										&& maxAmtPerTxn != ''
										&& parseFloat($(el).val()) < parseFloat(maxAmtPerTxn)) {
									generateAlert("programIdForm", ctrlId,
											"product.dailyMaxAmt.lesser.maxAmtPerTxn");
									isValid = false;
								} else if (maxAmtPerTxn == null
										|| maxAmtPerTxn.trim() == ''
										|| minAmtPerTxn == null
										|| minAmtPerTxn.trim() == ''
										|| parseFloat(minAmtPerTxn) > parseFloat(maxAmtPerTxn)) {
									isValid = false;
								} else {
									clearError(ctrlId);
									$(
											"#" + ctrlId.split('_', 1)
													+ ctrlId.split('_', 1))
											.css('visibility', 'hidden');
								}
								if (!isValid) {
									$(
											"#" + ctrlId.split('_', 1)
													+ ctrlId.split('_', 1))
											.css('visibility', 'visible');
								}
							} else {
								clearError(ctrlId);
							}
						} else {
							clearError(ctrlId);
						}
					});

	$("input[id$='_weeklyMaxAmt']")
			.each(
					function(i, el) {

						var ctrlId = el.id;
						var minAmtPerTxn = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_minAmtPerTx").val();
						var dailyMaxAmt = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_dailyMaxAmt").val();
						var maxAmtPerTxn = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_maxAmtPerTx").val();
						// var txnShortName=ctrlId.substring(ctrlId.split('_',
						// 1).join('_').length+1,ctrlId.split('_',
						// 2).join('_').length);
						var txnShortName = ctrlId.slice(0, ctrlId
								.lastIndexOf('_'));
						if ($("input[name='" + txnShortName + "']").val() == 'Y') {

							if ($(el).val() != null && $(el).val().trim() != '') {

								if (maxAmtPerTxn == null
										|| maxAmtPerTxn.trim() == '') {
									generateAlert(
											"programIdForm",
											$(
													"#"
															+ ctrlId
																	.substring(
																			0,
																			ctrlId
																					.lastIndexOf("_"))
															+ "_maxAmtPerTx")
													.attr('id'),
											"product.maxAmtPerTxn.empty");
									isValid = false;
								} else if (!maxAmtPerTxnValidation(document
										.getElementById(ctrlId.substring(0,
												ctrlId.lastIndexOf("_"))
												+ "_maxAmtPerTx"))) {
									isValid = false;
								} else {
									clearError($(
											"#"
													+ ctrlId
															.substring(
																	0,
																	ctrlId
																			.lastIndexOf("_"))
													+ "_maxAmtPerTx")
											.attr('id'));
								}
								if (minAmtPerTxn == null
										|| minAmtPerTxn.trim() == '') {
									generateAlert(
											"programIdForm",
											$(
													"#"
															+ ctrlId
																	.substring(
																			0,
																			ctrlId
																					.lastIndexOf("_"))
															+ "_minAmtPerTx")
													.attr('id'),
											"product.minAmtPerTxn.empty");
									isValid = false;
								} else if (!positiveNonEmptyAmount(document
										.getElementById(ctrlId.substring(0,
												ctrlId.lastIndexOf("_"))
												+ "_minAmtPerTx"))) {
									isValid = false;
								} else {
									clearError($(
											"#"
													+ ctrlId
															.substring(
																	0,
																	ctrlId
																			.lastIndexOf("_"))
													+ "_minAmtPerTx")
											.attr('id'));
								}

								if (!DecimalValueFormat(el)) {
									isValid = false;
								} else if (isNaN($(el).val())) {
									generateAlert("programIdForm", ctrlId,
											"product.NaN");
									isValid = false;
								} else if (dailyMaxAmt != null
										&& dailyMaxAmt.trim() != ''
										&& parseFloat($(el).val()) < parseFloat(dailyMaxAmt)) {
									generateAlert("programIdForm", ctrlId,
											"product.weeklyMaxAmt.lesser.dailyMaxAmt");
									isValid = false;
								} else if (maxAmtPerTxn != null
										&& maxAmtPerTxn.trim() != ''
										&& parseFloat($(el).val()) < parseFloat(maxAmtPerTxn)) {
									generateAlert("programIdForm", ctrlId,
											"product.weeklyMaxAmt.lesser.maxAmtPerTxn");
									isValid = false;
								} else if (maxAmtPerTxn == null
										|| maxAmtPerTxn.trim() == ''
										|| minAmtPerTxn == null
										|| minAmtPerTxn.trim() == ''
										|| parseFloat(minAmtPerTxn) > parseFloat(maxAmtPerTxn)) {
									isValid = false;
								} else {
									clearError(ctrlId);
									$(
											"#" + ctrlId.split('_', 1)
													+ ctrlId.split('_', 1))
											.css('visibility', 'hidden');
								}
								if (!isValid) {
									$(
											"#" + ctrlId.split('_', 1)
													+ ctrlId.split('_', 1))
											.css('visibility', 'visible');
								}
							} else {
								clearError(ctrlId);
							}
						} else {
							clearError(ctrlId);
						}
					});

	$("input[id$='_monthlyMaxAmt']")
			.each(
					function(i, el) {

						var ctrlId = el.id;
						var minAmtPerTxn = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_minAmtPerTx").val();
						var weeklyMaxAmt = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_weeklyMaxAmt").val();
						var dailyMaxAmt = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_dailyMaxAmt").val();
						var maxAmtPerTx = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_maxAmtPerTx").val();
						// var txnShortName=ctrlId.substring(ctrlId.split('_',
						// 1).join('_').length+1,ctrlId.split('_',
						// 2).join('_').length);
						var txnShortName = ctrlId.slice(0, ctrlId
								.lastIndexOf('_'));
						if ($("input[name='" + txnShortName + "']").val() == 'Y') {

							if ($(el).val() != null && $(el).val().trim() != '') {

								if (maxAmtPerTx == null
										|| maxAmtPerTx.trim() == '') {
									generateAlert(
											"programIdForm",
											$(
													"#"
															+ ctrlId
																	.substring(
																			0,
																			ctrlId
																					.lastIndexOf("_"))
															+ "_maxAmtPerTx")
													.attr('id'),
											"product.maxAmtPerTxn.empty");
									isValid = false;
								} else if (!maxAmtPerTxnValidation(document
										.getElementById(ctrlId.substring(0,
												ctrlId.lastIndexOf("_"))
												+ "_maxAmtPerTx"))) {
									isValid = false;
								} else {
									clearError($(
											"#"
													+ ctrlId
															.substring(
																	0,
																	ctrlId
																			.lastIndexOf("_"))
													+ "_maxAmtPerTx")
											.attr('id'));
								}
								if (minAmtPerTxn == null
										|| minAmtPerTxn.trim() == '') {
									generateAlert(
											"programIdForm",
											$(
													"#"
															+ ctrlId
																	.substring(
																			0,
																			ctrlId
																					.lastIndexOf("_"))
															+ "_minAmtPerTx")
													.attr('id'),
											"product.minAmtPerTxn.empty");
									isValid = false;
								} else if (!positiveNonEmptyAmount(document
										.getElementById(ctrlId.substring(0,
												ctrlId.lastIndexOf("_"))
												+ "_minAmtPerTx"))) {
									isValid = false;
								} else {
									clearError($(
											"#"
													+ ctrlId
															.substring(
																	0,
																	ctrlId
																			.lastIndexOf("_"))
													+ "_minAmtPerTx")
											.attr('id'));
								}
								if (!DecimalValueFormat(el)) {
									isValid = false;
								} else if (isNaN($(el).val())) {
									generateAlert("programIdForm", ctrlId,
											"product.NaN");
									isValid = false;
								} else if (weeklyMaxAmt != null
										&& weeklyMaxAmt.trim() != ''
										&& parseFloat($(el).val()) < parseFloat(weeklyMaxAmt)) {
									generateAlert("programIdForm", ctrlId,
											"product.monthlyMaxAmt.lesser.weeklyMaxAmt");
									isValid = false;
								} else if (dailyMaxAmt != null
										&& dailyMaxAmt.trim() != ''
										&& parseFloat($(el).val()) < parseFloat(dailyMaxAmt)) {
									generateAlert("programIdForm", ctrlId,
											"product.monthlyMaxAmt.lesser.dailyMaxAmt");
									isValid = false;
								} else if (maxAmtPerTx != null
										&& maxAmtPerTx.trim() != ''
										&& parseFloat($(el).val()) < parseFloat(maxAmtPerTx)) {
									generateAlert("programIdForm", ctrlId,
											"product.monthlyMaxAmt.lesser.maxAmtPerTxn");
									isValid = false;
								} else if (maxAmtPerTx == null
										|| maxAmtPerTx.trim() == ''
										|| minAmtPerTxn == null
										|| minAmtPerTxn.trim() == ''
										|| parseFloat(minAmtPerTxn) > parseFloat(maxAmtPerTx)) {
									isValid = false;
								} else {
									clearError(ctrlId);
									$(
											"#" + ctrlId.split('_', 1)
													+ ctrlId.split('_', 1))
											.css('visibility', 'hidden');
								}
								if (!isValid) {
									$(
											"#" + ctrlId.split('_', 1)
													+ ctrlId.split('_', 1))
											.css('visibility', 'visible');
								}
							} else {
								clearError(ctrlId);
							}
						} else {
							clearError(ctrlId);
						}

					});

	$("input[id$='_yearlyMaxAmt']")
			.each(
					function(i, el) {

						var ctrlId = el.id;
						var minAmtPerTxn = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_minAmtPerTx").val();
						var monthlyMaxAmt = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_monthlyMaxAmt").val();
						var weeklyMaxAmt = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_weeklyMaxAmt").val();
						var dailyMaxAmt = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_dailyMaxAmt").val();
						var maxAmtPerTx = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_maxAmtPerTx").val();
						// var txnShortName=ctrlId.substring(ctrlId.split('_',
						// 1).join('_').length+1,ctrlId.split('_',
						// 2).join('_').length);
						var txnShortName = ctrlId.slice(0, ctrlId
								.lastIndexOf('_'));
						if ($("input[name='" + txnShortName + "']").val() == 'Y') {
							if ($(el).val() != null && $(el).val().trim() != '') {

								if (maxAmtPerTx == null
										|| maxAmtPerTx.trim() == '') {
									generateAlert(
											"programIdForm",
											$(
													"#"
															+ ctrlId
																	.substring(
																			0,
																			ctrlId
																					.lastIndexOf("_"))
															+ "_maxAmtPerTx")
													.attr('id'),
											"product.maxAmtPerTxn.empty");
									isValid = false;
								} else if (!maxAmtPerTxnValidation(document
										.getElementById(ctrlId.substring(0,
												ctrlId.lastIndexOf("_"))
												+ "_maxAmtPerTx"))) {
									isValid = false;

								} else {
									clearError($(
											"#"
													+ ctrlId
															.substring(
																	0,
																	ctrlId
																			.lastIndexOf("_"))
													+ "_maxAmtPerTx")
											.attr('id'));
								}
								if (minAmtPerTxn == null
										|| minAmtPerTxn.trim() == '') {
									generateAlert(
											"programIdForm",
											$(
													"#"
															+ ctrlId
																	.substring(
																			0,
																			ctrlId
																					.lastIndexOf("_"))
															+ "_minAmtPerTx")
													.attr('id'),
											"product.minAmtPerTxn.empty");
									isValid = false;
								} else if (!positiveNonEmptyAmount(document
										.getElementById(ctrlId.substring(0,
												ctrlId.lastIndexOf("_"))
												+ "_minAmtPerTx"))) {

									isValid = false;
								} else {
									clearError($(
											"#"
													+ ctrlId
															.substring(
																	0,
																	ctrlId
																			.lastIndexOf("_"))
													+ "_minAmtPerTx")
											.attr('id'));
								}

								if (!DecimalValueFormat(el)) {
									isValid = false;
								} else if (isNaN($(el).val())) {
									generateAlert("programIdForm", ctrlId,
											"product.NaN");
									isValid = false;
								} else if (monthlyMaxAmt != null
										&& monthlyMaxAmt.trim() != ''
										&& parseFloat($(el).val()) < parseFloat(monthlyMaxAmt)) {
									generateAlert("programIdForm", ctrlId,
											"product.yearlyMaxAmt.lesser.monthlyMaxAmt");
									isValid = false;
								} else if (weeklyMaxAmt != null
										&& weeklyMaxAmt.trim() != ''
										&& parseFloat(($(el).val())) < parseFloat(weeklyMaxAmt)) {
									generateAlert("programIdForm", ctrlId,
											"product.yearlyMaxAmt.lesser.weeklyMaxAmt");
									isValid = false;
								} else if (dailyMaxAmt != null
										&& dailyMaxAmt.trim() != ''
										&& parseFloat($(el).val()) < parseFloat(dailyMaxAmt)) {
									generateAlert("programIdForm", ctrlId,
											"product.yearlyMaxAmt.lesser.dailyMaxAmt");
									isValid = false;
								} else if (maxAmtPerTx != null
										&& maxAmtPerTx.trim() != ''
										&& parseFloat($(el).val()) < parseFloat(maxAmtPerTx)) {
									generateAlert("programIdForm", ctrlId,
											"product.yearlyMaxAmt.lesser.maxAmtPerTxn");
									isValid = false;
								} else if (maxAmtPerTx == null
										|| maxAmtPerTx.trim() == ''
										|| minAmtPerTxn == null
										|| minAmtPerTxn.trim() == ''
										|| parseFloat(minAmtPerTxn) > parseFloat(maxAmtPerTx)) {
									isValid = false;
								} else {
									clearError(ctrlId);
									$(
											"#" + ctrlId.split('_', 1)
													+ ctrlId.split('_', 1))
											.css('visibility', 'hidden');
								}
								if (!isValid) {
									$(
											"#" + ctrlId.split('_', 1)
													+ ctrlId.split('_', 1))
											.css('visibility', 'visible');
								}
							} else {
								clearError(ctrlId);
							}
						} else {
							clearError(ctrlId);
						}
					});

	return isValid;
}

function maxCountValidation() {

	var isValid = true;

	$("input[id$='_weeklyMaxCount']").each(
			function(i, el) {

				var ctrlId = el.id;
				var dailyMaxCnt = $(
						"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
								+ "_dailyMaxCount").val();

				if ($(el).val() != null && $(el).val().trim() != '') {
					if (isNaN($(el).val())) {
						generateAlert("programIdForm", ctrlId, "product.NaN");
						isValid = false;
					} else if (dailyMaxCnt != null && dailyMaxCnt != ''
							&& parseInt($(el).val()) < parseInt(dailyMaxCnt)) {
						generateAlert("programIdForm", ctrlId,
								"product.weeklyMaxCnt.lesser.dailyMaxCnt");
						isValid = false;
					} else {
						clearError(ctrlId);
						$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1))
								.css('visibility', 'hidden');
					}
					if (!isValid) {
						$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1))
								.css('visibility', 'visible');
					}
				} else {
					clearError(ctrlId);
				}
			});

	$("input[id$='_monthlyMaxCount']").each(
			function(i, el) {

				var ctrlId = el.id;
				var weeklyMaxCnt = $(
						"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
								+ "_weeklyMaxCount").val();
				var dailyMaxCnt = $(
						"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
								+ "_dailyMaxCount").val();
				if ($(el).val() != null && $(el).val().trim() != '') {
					if (isNaN($(el).val())) {
						generateAlert("programIdForm", ctrlId, "product.NaN");
						isValid = false;
					} else if (weeklyMaxCnt != null
							&& weeklyMaxCnt.trim() != ''
							&& parseInt($(el).val()) < parseInt(weeklyMaxCnt)) {
						generateAlert("programIdForm", ctrlId,
								"product.monthlyMaxCnt.lesser.weeklylyMaxCnt");
						isValid = false;
					} else if (dailyMaxCnt != null && dailyMaxCnt.trim() != ''
							&& parseInt($(el).val()) < parseInt(dailyMaxCnt)) {
						generateAlert("programIdForm", ctrlId,
								"product.monthlyMaxCnt.lesser.dailylyMaxCnt");
						isValid = false;
					} else {
						clearError(ctrlId);
						$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1))
								.css('visibility', 'hidden');
					}
					if (!isValid) {
						$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1))
								.css('visibility', 'visible');
					}
				} else {
					clearError(ctrlId);
				}
			});

	$("input[id$='_yearlyMaxCount']")
			.each(
					function(i, el) {

						var ctrlId = el.id;
						var monthlyMaxCnt = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_monthlyMaxCount").val();
						var weeklyMaxCnt = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_weeklyMaxCount").val();
						var dailyMaxCnt = $(
								"#"
										+ ctrlId.substring(0, ctrlId
												.lastIndexOf("_"))
										+ "_dailyMaxCount").val();

						if ($(el).val() != null && $(el).val().trim() != '') {
							if (isNaN($(el).val())) {
								generateAlert("programIdForm", ctrlId,
										"product.NaN");
								isValid = false;
							} else if (monthlyMaxCnt != null
									&& monthlyMaxCnt.trim() != ''
									&& parseInt($(el).val()) < parseInt(monthlyMaxCnt)) {
								generateAlert("programIdForm", ctrlId,
										"product.yearlyMaxCnt.lesser.monthlyMaxCnt");
								isValid = false;
							} else if (weeklyMaxCnt != null
									&& weeklyMaxCnt.trim() != ''
									&& parseInt($(el).val()) < parseInt(weeklyMaxCnt)) {
								generateAlert("programIdForm", ctrlId,
										"product.yearlyMaxCnt.lesser.weeklylyMaxCnt");
								isValid = false;
							} else if (dailyMaxCnt != null
									&& dailyMaxCnt.trim() != ''
									&& parseInt($(el).val()) < parseInt(dailyMaxCnt)) {
								generateAlert("programIdForm", ctrlId,
										"product.yearlyMaxCnt.lesser.dailylyMaxCnt");
								isValid = false;
							} else {
								clearError(ctrlId);
								$(
										"#" + ctrlId.split('_', 1)
												+ ctrlId.split('_', 1)).css(
										'visibility', 'hidden');
							}
							if (!isValid) {
								$(
										"#" + ctrlId.split('_', 1)
												+ ctrlId.split('_', 1)).css(
										'visibility', 'visible');
							}
						} else {
							clearError(ctrlId);
						}
					});

	return isValid;
}
// ,shdkjsa

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
	// var v_qty_int,
	var v_qty_dec;

	if (splitvalue.length > 1) {
		// v_qty_int = splitvalue[0];
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

/*
 * function allowNumbersWithDot(ctrl){ var isValid; var regex = /^[1-9.]*$/;
 * isValid = regex.test($("#" + ctrl.id + "").val()); ctrl.value =
 * ctrl.value.replace(/[^0-9.]/g, ''); if((ctrl.value.startsWith('
 * '))||(ctrl.value.endsWith(' '))) ctrl.value =ctrl.value.replace(/\s+/,'');
 * return isValid;
 *  }
 */
function allowNumbersWithDot(e) {
	var charCode;
	if (e.keyCode > 0) {
		charCode = e.which || e.keyCode;
	} else if (typeof (e.charCode) != "undefined") {
		charCode = e.which || e.keyCode;
	}
	if (charCode == 46)
		return true
	if (charCode > 31 && (charCode < 48 || charCode > 57))
		return false;
	return true;
}

function isNumericfn(e) {
	var charCode;
	if (e.keyCode > 0) {
		charCode = e.which || e.keyCode;
	} else if (typeof (e.charCode) != "undefined") {
		charCode = e.which || e.keyCode;
	}

	if (charCode > 31 && (charCode < 48 || charCode > 57))
		return false;
	return true;
}
function validateFeeDescription(ctrl) {

	var isValid = true;
	var ctrlId = $(ctrl).attr('id');
	var ctrlVal = $(ctrl).val();
	/*
	 * if(ctrlVal==null || ctrlVal==''){
	 * generateAlert($(ctrl).closest('form').attr('id'),
	 * ctrlId,"product.feeDesc.empty"); $("#"+ctrlId.split('_',
	 * 1)+ctrlId.split('_', 1)).css('visibility', 'visible'); isValid= false; }
	 * else
	 */if (ctrlVal != null && ctrlVal.length > 100) {
		generateAlert($(ctrl).closest('form').attr('id'), ctrlId,
				"product.feeDesc.length");
		$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css('visibility',
				'visible');
		isValid = false;
	} else {
		// $("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',
		// 'hidden');
		clearError(ctrlId);
	}
	return isValid;
}
function validateFeeAmt(ctrl) {
	var isValid = true;
	var feeAmount = $(ctrl).val();
	var ctrlId = $(ctrl).attr('id');
	var idPrefix = ctrlId.substring(0, ctrlId.lastIndexOf("_"));
	if (feeAmount == null || feeAmount.trim() == '') {
		if ($("#" + idPrefix + "_clawback").is(":checked")
				|| $("#" + idPrefix + "_feeCondition option:selected").val() != 'N'
				|| ($("#" + idPrefix + "_freeCount").val() != null && $(
						"#" + idPrefix + "_freeCount").val().trim() != '')
				|| ($("#" + idPrefix + "_maxCount").val() != null && $(
						"#" + idPrefix + "_maxCount").val().trim() != '')) {

			generateAlert($(ctrl).closest('form').attr('id'), ctrlId,
					"product.feeAmt.empty");
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		} else {
			clearError(ctrlId);
		}
	} else if (feeAmount.trim() != '') {
		if (isNaN(feeAmount)) {
			generateAlert($(ctrl).closest('form').attr('id'), ctrlId,
					"product.feeAmt.invalid");
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		} else if (parseFloat(feeAmount) <= 0) {
			generateAlert($(ctrl).closest('form').attr('id'), ctrlId,
					"product.feeAmt.zero");
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		} else if (!DecimalValueFormat(ctrl)) {
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		} else {
			clearError(ctrlId);
		}
	}
	return isValid;
}

// dhfsgjgdfgsdjfghj

function enableClawbackOptions(ctrl) {

	var ctrlId = $(ctrl).attr('id');
	if ($(ctrl).is(':checked')) {
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_clawbackCount")
				.prop("readonly", false);
		$(
				"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
						+ "_clawbackOption").prop("disabled", false);
		$(
				"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
						+ "_clawbackMaxAmt").prop("readonly", false);
		if ($(
				"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
						+ "_clawbackOption option:selected").val() == 'N')
			$(
					"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
							+ "_clawbackOption").find('option[value="O"]')
					.prop("selected", true);

	} else {
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_clawbackCount")
				.prop("readonly", true);
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_clawbackCount")
				.val('');
		$(
				"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
						+ "_clawbackOption").prop("disabled", true);

		$(
				"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
						+ "_clawbackOption").find('option[value="N"]').prop(
				"selected", true);
		$(
				"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
						+ "_clawbackMaxAmt").prop("readonly", true);
		$(
				"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
						+ "_clawbackMaxAmt").val('');
		clearError(ctrlId.substring(0, ctrlId.lastIndexOf("_"))
				+ "_clawbackCount");
		clearError(ctrlId.substring(0, ctrlId.lastIndexOf("_"))
				+ "_clawbackOption");
		clearError(ctrlId.substring(0, ctrlId.lastIndexOf("_"))
				+ "_clawbackMaxAmt");
	}
}
function clawbackOptionChange(ctrl) {
	var ctrlId = $(ctrl).attr('id');
	var ctrlVal = $("#" + ctrlId + " option:selected").val();
	if (ctrlVal == 'N') {

		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_clawback")
				.prop("checked", false);
		enableClawbackOptions($("#"
				+ ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_clawback"));
		$("#" + ctrlId).find('option[value="N"]').prop("selected", true);
	}
}

function validateClabackMaxFeeAmt(ctrl) {
	var isValid = true;
	var feeAmount = $(ctrl).val();
	var ctrlId = $(ctrl).attr('id');
	if (feeAmount != null && feeAmount.trim() != '') {
		if (isNaN(feeAmount)) {
			generateAlert($(Object).closest('form').attr('id'), ctrlId,
					"product.clawbackMaxFeeAmt.invalid");
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		}

		else if (!DecimalValueFormat(ctrl)) {
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		} else {
			// $("#"+ctrlId.split('_', 1)+ctrlId.split('_',
			// 1)).css('visibility', 'hidden');
			clearError(ctrlId);
		}
	}
	return isValid;
}
function validateCount(ctrl) {
	var ctrlId = $(ctrl).attr('id');
	var ctrlVal = $(ctrl).val();
	if (ctrlVal != null && ctrlVal != '') {
		if (isNaN(ctrlVal)) {
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			generateAlert($(ctrl).closest('form').attr('id'), ctrlId,
					"product.NaN");
			return false;
		} else if (parseInt(ctrlVal).length > 6
				&& !ctrlId.endsWith('firstMonthFeeAssessedDays')) {
			if (ctrlId.endsWith('maxCount')) {
				generateAlert($(ctrl).closest('form').attr('id'), ctrlId,
						"maintenanceFee.maxCnt.invalid");

			} else if (ctrlId.endsWith('freeCount')) {

				generateAlert($(ctrl).closest('form').attr('id'), ctrlId,
						"maintenanceFee.freeCnt.invalid");
			}

			return false;
		} else if (parseInt(ctrlVal).length > 3
				&& ctrlId.endsWith('firstMonthFeeAssessedDays')) {
			generateAlert($(ctrl).closest('form').attr('id'), ctrlId,
					"maintenanceFee.firstFeeAccessDays.invalid");

			return false;
		}
		return true;
	}

	return true;
}

function validateClawbackCount(obj) {
	var isValid = true;
	if (!$(obj).prop("readonly")) {
		var clawbackCnt = $(obj).val();
		var ctrlId = $(obj).attr('id');
		if (clawbackCnt != null && clawbackCnt.trim() != '') {
			if (isNaN(clawbackCnt) || parseInt(clawbackCnt) < 1) {
				generateAlert($(obj).closest('form').attr('id'), ctrlId,
						"product.clawbackCnt.invalid");
				$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
						'visibility', 'visible');
				isValid = false;
			} else {
				clearError(ctrlId);
			}
		} else {
			generateAlert($(obj).closest('form').attr('id'), ctrlId,
					"product.clawbackCnt.empty");
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		}
	}
	return isValid;
}

function isNonzeroNumeric(ctrl) {
	var isValid;
	var regex = /^[1-9]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^0-9]/g, '');
	if ((ctrl.value.startsWith(' ')) || (ctrl.value.endsWith(' ')))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	return isValid;
}

function enableClawbackCount(obj) {
	var ctrlId = obj.id;
	if ($(obj).is(':checked')) {
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_clawbackCount")
				.prop("readonly", false);
	} else {
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_clawbackCount")
				.prop("readonly", true);
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_clawbackCount")
				.val('');
		clearError(ctrlId.substring(0, ctrlId.lastIndexOf("_"))
				+ "_clawbackCount");
	}
}

function callFeeConditionChange(obj) {
	var ctrlId = obj.id;

	var ctrlVal = $("#" + ctrlId + " option:selected").val();
	if (ctrlVal == "N") {
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_feePercent")
				.prop("readonly", true);
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_feePercent")
				.val('');
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt")
				.prop("readonly", true);
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt")
				.val('');

	} else if (ctrlVal == "A") {
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_feePercent")
				.prop("readonly", false);
		enableMinFee($("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
				+ "_feePercent"));
	} else if (ctrlVal == "O") {
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_feePercent")
				.prop("readonly", false);
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt")
				.prop("readonly", true);
		$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt")
				.val('');

	}

	clearError(ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_feePercent");
	clearError(ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt");

}

function enableFrequency(obj) {
	var ctrlId = $(obj).attr('id');
	var objVal = $(obj).val();
	if (objVal != null && objVal.trim() != '') {
		if (!isNaN(objVal)) {
			/*	$("#" + ctrlId + "Freq").val("D");*/
			$("#" + ctrlId + "Freq").prop("disabled", false);
			clearError(ctrlId);
		} else {
			$("#" + ctrlId + "Freq").val("D");
			$("#" + ctrlId + "Freq").prop("disabled", true);
			if (ctrlId.endswith('_freeCount')) {
				generateAlert("programIdForm", ctrlId,
						"product.freeCnt.invalid");
				$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
						'visibility', 'visible');
			}

			else if ($(ctrlId.endsWith('_maxCount'))) {
				generateAlert("programIdForm", ctrlId, "product.maxCnt.invalid");
				$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
						'visibility', 'visible');
			}

		}
	} else {
		$("#" + ctrlId + "Freq").val("D");
		$("#" + ctrlId + "Freq").prop("disabled", true);

	}
}

function validateMinFeeAmt(ctrl) {
	var isValid = true;
	var minFeeAmount = $(ctrl).val();
	var ctrlId = $(ctrl).attr('id');
	var feeAmt = $(
			"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_feeAmt")
			.val();
	if (!$(ctrl).prop("readonly")) {
		if (minFeeAmount == null || minFeeAmount.trim() == '') {
			generateAlert("programIdForm", ctrlId, "product.minFeeAmt.empty");
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		} else if (isNaN(minFeeAmount)) {
			generateAlert("programIdForm", ctrlId, "product.minFeeAmt.invalid");
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		} else if (!DecimalValueFormat(ctrl)) {
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		} else if (parseFloat(minFeeAmount) < parseFloat(feeAmt)) {
			generateAlert("programIdForm", ctrlId, "product.minFeeAmt.greater");

			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		}

		else {
			clearError(ctrlId);
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'hidden');
		}
	} else {
		clearError(ctrlId);
	}
	return isValid;
}

/* Enable minimum fee amount if % fee is valid and applicable*/
function enableMinFee(ctrl) {
	var ctrlId = $(ctrl).attr('id');
	var minFeeAmtId = $(
			"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt")
			.attr('id');
	var feeCondnVal = $(
			"#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_feeCondition option:selected").val();
	var isValid = validateFeePercentage(ctrl);
	if (isValid && !$(ctrl).prop("readonly") && feeCondnVal == 'A') {
		$("#" + minFeeAmtId).prop("readonly", false);
	} else {
		$("#" + minFeeAmtId).prop("readonly", true);
		clearError(minFeeAmtId);
	}

}
function validateFeePercentage(ctrl) {

	var isValid = true;

	if (!$(ctrl).prop("readonly")) {
		var feePercent = $(ctrl).val();
		var ctrlId = $(ctrl).attr('id');

		if (feePercent == null || feePercent == '') {
			generateAlert("programIdForm", ctrlId, "product.feePerc.empty");

			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		} else if (isNaN(feePercent) || parseFloat(feePercent) <= 0) {
			generateAlert("programIdForm", ctrlId, "product.feePerc.zero");
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		} else if (!DecimalValueFormat(ctrl)) {
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		} else if (parseFloat(feePercent) > 100) {
			generateAlert("programIdForm", ctrlId, "product.feePerc.greater");
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css(
					'visibility', 'visible');
			isValid = false;
		} else {
			//$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
			clearError(ctrlId);
		}
	}
	return isValid;
}

function enableProRation() {
	if ($("#monthlyFee_assessmentDate option:selected").val() == 'FD') {
		$("#monthlyFee_proRation").prop('disabled', false);
	} else {
		$("#monthlyFee_proRation").prop('disabled', true);
		$("#monthlyFee_proRation").prop('checked', false);
	}

}

function validMaintenanceCapFeeAmt(ctrl) {
	var isValid = true;
	var ctrlId = $(ctrl).attr('id');
	var feeAmount = $(ctrl).val();
	if (feeAmount != null && feeAmount != '') {
		if (isNaN(feeAmount)) {
			generateAlert($(ctrl).closest('form').attr('id'), ctrlId,
					"maintenanceFee.capFeeAmt.invalid");
			isValid = false;
		} else if (!DecimalValueFormat(ctrl)) {
			isValid = false;
		} else {
			clearError(ctrlId);
		}
	}
	return isValid;
}

