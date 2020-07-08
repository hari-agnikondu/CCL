//CCF Ajax scripts

var baseURL = window.location.protocol + "//" + window.location.host + "" + window.location.pathname;
baseURL = baseURL.substring(0,baseURL.lastIndexOf("/")+1);

function generateError(field,text){
	clearError(field);
	$("#"+field).after('<span class="error_empty" style="color:red;"><br/>'+text+' </span>');
}

function saveData() {
	
	var seqArray = [];
	var errorFlag = false;
	var tableArrays = [ "ccfHeaderTable", "ccfDtlTable", "ccfTrailerTable" ];
	var prev = 0;

	if (("N" == document.getElementById("versionsID").value)) {
		if ((document.getElementById("versionName").value == undefined)
				|| (document.getElementById("versionName").value == null)
				|| (document.getElementById("versionName").value == "")) {
			generateError("versionName","CCF Version Name is mandatory");
		//	customError("versionName", 'CCF Version Name is mandatory');
			errorFlag = true;
		} else {
			clearError("versionName");
//			removeCustomError("versionName");
		}
	}

	for (var k = 0; k < tableArrays.length; k++) {
		prev = 0;
		var table = document.getElementById(tableArrays[k]);
		for (var i = 1, row; row = table.rows[i]; i++) {
			for (var j = 1; j <= 3; j++) {
				var col = row.cells[j];
				if (j == 1) {

					if (isNaN(col.getElementsByTagName('input')[0].value)) {
						generateError(col.getElementsByTagName('input')[0].id,'Sequence No is invalid');
						//customError(col.getElementsByTagName('input')[0].id,'Sequence No is invalid');
						errorFlag = true;
						return;
					} else if (!isInteger(col.getElementsByTagName('input')[0].value)) {
						generateError(col.getElementsByTagName('input')[0].id,'Sequence No is invalid');
						//customError(col.getElementsByTagName('input')[0].id,'Sequence No is invalid');
						errorFlag = true;
						return;
					} else if (col.getElementsByTagName('input')[0].value != null
							&& col.getElementsByTagName('input')[0].value
									.trim() == "") {
						generateError(col.getElementsByTagName('input')[0].id,'Enter the Sequence No');
					//	customError(col.getElementsByTagName('input')[0].id,'Enter the Sequence No');
						errorFlag = true;
					} else if ((parseInt(prev) + 1) != (parseInt(col
							.getElementsByTagName('input')[0].value))) {
						generateError(col.getElementsByTagName('input')[0].id,'Invalid Sequence No');
						//customError(col.getElementsByTagName('input')[0].id,'Invalid Sequence No');
						errorFlag = true;
					} else {
						clearError(col.getElementsByTagName('input')[0].id);
					//	removeCustomError(col.getElementsByTagName('input')[0].id);
					}
					prev = col.getElementsByTagName('input')[0].value;
					seqArray.push(prev);
				} else if (j == 3) {
					if (col.getElementsByTagName('input')[0].value != null
							&& col.getElementsByTagName('input')[0].value
									.trim() == "") {
						generateError(col.getElementsByTagName('input')[0].id,'Enter the length');
					//	customError(col.getElementsByTagName('input')[0].id,'Enter the length');
						errorFlag = true;
					} else {
						clearError(col.getElementsByTagName('input')[0].id);
						//removeCustomError(col.getElementsByTagName('input')[0].id);
					}

				}
			}
		}

	}

	if (!errorFlag) {
		var myStringArray = [];
		for (var k = 0; k < tableArrays.length; k++) {
			var tempTab = document.getElementById(tableArrays[k]);
			for (var i = 1; i <= tempTab.rows.length - 1; i++) {
				var row = tempTab.rows[i];
				var myObject = new Object();
				myObject.dataSeqNo = row.cells[1].firstChild.value;
				myObject.dataTitle = row.cells[2].firstChild.value;
				myObject.dataLength = row.cells[3].firstChild.value;
				myObject.dataValue = row.cells[4].firstChild.value;
				myObject.dataFormat = row.cells[5].firstChild.value;
				myObject.valueKey = row.cells[6].firstChild.value;
				myObject.dataFiller = row.cells[7].firstChild.value;
				myObject.dataFillerSide = row.cells[8].firstChild.value;
				if (k == 0) {
					myObject.recordType = "H";
				} else if (k == 1) {
					myObject.recordType = "D";
				} else if (k == 2) {
					myObject.recordType = "T";
				}
				myStringArray.push(myObject);
			}
		}

		if (myStringArray.length > 0) {
			jQuery.support.cors = true;

			request = {
				"rowData" : myStringArray,
				"versionID" : $('#versionsID').val(),
				"versionName" : $('#versionName').val(),
				"addEdit" : $('#addEdit').val()
			};
			$.ajax({
				url : baseURL+'addCCF',
				type : "POST",
				data : JSON.stringify(request),
				dataType : "JSON",
				contentType : "application/json",
				async : false,
				success : function(response) {
					$('#respMsg').html(response);
				},
				error : function(error) {
					if (error.statusText == 'OK') {
						$('#respMsg').html(error.responseText);
					} else {
						$('#respMsg').html(error.statusText);
					}
				}
			});

		} else {
			customError("ccfDtlTables",
					'CCF configuration should be mandatory ');
		}

	} else {
		removeCustomError("ccfDtlTables");
	}
}

function ccfVerSelect() {
	removeAllRows();
	if ("N" != document.getElementById("versionsID").value) {
		document.getElementById("ccfVersionsNames").style.display = "none";
		document.getElementById("addEdit").value = "E";
		getCcfVersionsDtls();
	} else {
		document.getElementById("ccfVersionsNames").style.display = "";
		document.getElementById("addEdit").value = "A";
	}
}

function getCcfVersionsDtls() {
	$
			.ajax({
				url : baseURL+'showEditCCF',
				type : "GET",
				data : {
					versionId : $('#versionsID').val()
				},
				dataType : "json",
				async : false,
				contentType : "application/x-www-form-urlencoded",
				crossDomain : true,
				success : function(response) {
					// alert(response);
					var theader = '<TR class="TR3"><TD style="width:2%"  ></td><TD style="width:8%" align="center" >Sequence No</td><TD style="width:33%" align="center">Record Title</td><TD style="width:8%" align="center">Length</td><TD style="width:8%" align="center">Default Value</td><TD style="width:8%" align="center">Format</td><TD style="width:15%" align="center" >Key</td><TD style="width:8%" align="center">Filler</td><TD style="width:8%" align="center">Left/Right Justified</td></tr>';
					var tDetail = '<TR class="TR3"><TD style="width:2%"  ></td><TD style="width:8%" align="center">Sequence No</td><TD style="width:33%" align="center">Record Title</td><TD style="width:8%" align="center">Length</td><TD style="width:8%" align="center">Default Value</td><TD style="width:8%" align="center">Format</td><TD style="width:15%" align="center">Key</td><TD style="width:8%" align="center">Filler</td><TD style="width:8%" align="center">Left/Right Justified</td></tr>';
					var tTrailer = '<TR class="TR3"><TD style="width:2%" ></td><TD style="width:8%" align="center">Sequence No</td><TD style="width:33%" align="center">Record Title</td><TD style="width:8%" align="center">Length</td><TD style="width:8%" align="center">Default Value</td><TD style="width:8%" align="center">Format</td><TD style="width:15%" align="center">Key</td><TD style="width:8%" align="center">Filler</td><TD style="width:8%" align="center">Left/Right Justified</td></tr>';
					$
							.each(
									response,
									function(number, item) {
										if (item.recordType != null
												&& item.recordType
														.toUpperCase() == "H") {
											theader += '<tr><td><INPUT style="width:100%"  id=H_chk_'
													+ item.dataSeqNo
													+ ' type="checkbox" name="H_chk_'
													+ item.dataSeqNo
													+ '" /></td><td><input style="width:85%" id=H_seqNo_'
													+ item.dataSeqNo
													+ ' type="text" name="H_seqNo_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ item.dataSeqNo
													+ '" /></td><td><Select style="width:100%" id=H_titleName_'
													+ item.dataSeqNo
													+ ' type="text" name="H_titleName_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ item.dataTitle
													+ '">'
													+ headerOptions
													+ '</SELECT></td><td><input style="width:85%" id=H_dataLength_'
													+ item.dataSeqNo
													+ ' type="text" name="H_dataLength_'
													+ item.dataSeqNo
													+ '" value ="'
													+ item.dataLength
													+ '"/></td><td><input style="width:85%" id=H_defaultValue_'
													+ item.dataSeqNo
													+ ' type="text" name="H_defaultValue_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ returnblank(item.dataValue)
													+ '"/></td><td><input style="width:85%" id=H_format_'
													+ item.dataSeqNo
													+ ' type="text" name="H_format_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ returnblank(item.dataFormat)
													+ '"/></td>       <td><SELECT style="width:100%" id=H_key_'
													+ item.dataSeqNo
													+ ' type="text" name="H_key_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ item.valueKey
													+ '"><option value="">NS</option>'
													+ keyOptions
													+ '</SELECT></td>    <td><SELECT style="width:100%" id=H_filler_'
													+ item.dataSeqNo
													+ ' name="H_filler_'
													+ item.dataSeqNo
													+ '" style="width:100%";><option value=" ">Space</option><option value="0">Zero</option></SELECT></td><td><SELECT style="width:100%" id=H_justify_'
													+ item.dataSeqNo
													+ ' name="H_justify_'
													+ item.dataSeqNo
													+ '" style="width:100%";><option value="L">Left</option><option value="R">Right</option></SELECT></td></tr>';
										} else if (item.recordType != null
												&& item.recordType
														.toUpperCase() == "D") {

											tDetail += '<tr><td><INPUT style="width:100%"  id=D_chk_'
													+ item.dataSeqNo
													+ ' type="checkbox" name="D_chk_'
													+ item.dataSeqNo
													+ '" /></td><td><input style="width:85%" id=D_seqNo_'
													+ item.dataSeqNo
													+ ' type="text" name="D_seqNo_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ item.dataSeqNo
													+ '" /></td><td><Select style="width:100%" id=D_titleName_'
													+ item.dataSeqNo
													+ ' type="text" name="D_titleName_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ item.dataTitle
													+ '">'
													+ detailOptions
													+ '</SELECT></td><td><input style="width:85%" id=D_dataLength_'
													+ item.dataSeqNo
													+ ' type="text" name="D_dataLength_'
													+ item.dataSeqNo
													+ '" value ="'
													+ item.dataLength
													+ '"/></td><td><input style="width:85%"  id=D_defaultValue_'
													+ item.dataSeqNo
													+ ' type="text" name="D_defaultValue_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ returnblank(item.dataValue)
													+ '"/></td><td><input style="width:85%" id=D_format_'
													+ item.dataSeqNo
													+ ' type="text" name="D_format_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ returnblank(item.dataFormat)
													+ '"/></td><td><SELECT style="width:100%" id=D_key_'
													+ item.dataSeqNo
													+ ' type="text" name="D_key_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ item.valueKey
													+ '"><option value="">NS</option>'
													+ keyOptions
													+ '</SELECT></td><td><SELECT style="width:100%" id=D_filler_'
													+ item.dataSeqNo
													+ ' name="D_filler_'
													+ item.dataSeqNo
													+ '" style="width:100%";><option value=" ">Space</option><option value="0">Zero</option></SELECT></td><td><SELECT style="width:100%" id=D_justify_'
													+ item.dataSeqNo
													+ ' name="D_justify_'
													+ item.dataSeqNo
													+ '" style="width:100%";><option value="L">Left</option><option value="R">Right</option></SELECT></td></tr>';
										} else if (item.recordType != null
												&& item.recordType
														.toUpperCase() == "T") {
											tTrailer += '<tr><td><INPUT style="width:100%"  id=T_chk_'
													+ item.dataSeqNo
													+ ' type="checkbox" name="T_chk_'
													+ item.dataSeqNo
													+ '" /></td><td><input style="width:85%" id=T_seqNo_'
													+ item.dataSeqNo
													+ ' type="text" name="T_seqNo_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ item.dataSeqNo
													+ '" /></td><td><SELECT style="width:100%" id=T_titleName_'
													+ item.dataSeqNo
													+ ' type="text" name="T_titleName_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ item.dataTitle
													+ '">'
													+ trailerOptions
													+ '</SELECT></td><td><input style="width:85%" id=T_dataLength_'
													+ item.dataSeqNo
													+ ' type="text" name="T_dataLength_'
													+ item.dataSeqNo
													+ '" value ="'
													+ item.dataLength
													+ '"/></td><td><input style="width:85%" id=T_defaultValue_'
													+ item.dataSeqNo
													+ ' type="text" name="T_defaultValue_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ returnblank(item.dataValue)
													+ '"/></td><td><input style="width:85%" id=T_format_'
													+ item.dataSeqNo
													+ ' type="text" name="T_format_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ returnblank(item.dataFormat)
													+ '"/></td><td><SELECT style="width:100%" id=T_key_'
													+ item.dataSeqNo
													+ ' type="text" name="T_key_'
													+ item.dataSeqNo
													+ '"  value ="'
													+ item.valueKey
													+ '"><option value="">NS</option>'
													+ keyOptions
													+ '</SELECT></td><td><SELECT style="width:100%" id=T_filler_'
													+ item.dataSeqNo
													+ ' name="T_filler_'
													+ item.dataSeqNo
													+ '" style="width:100%";><option value=" ">Space</option><option value="0">Zero</option></SELECT></td><td><SELECT style="width:100%" id=T_justify_'
													+ item.dataSeqNo
													+ ' name="T_justify_'
													+ item.dataSeqNo
													+ '" style="width:100%";><option value="L">Left</option><option value="R">Right</option></SELECT></td></tr>';
										}
									});
					if (response != null && response.length > 0) {
						$('#ccfHeaderTable').append(theader);
						$('#ccfDtlTable').append(tDetail);
						$('#ccfTrailerTable').append(tTrailer);
					}
					$
							.each(
									response,
									function(number, item) {

										if (item.recordType != null) {

											document
													.getElementById(item.recordType
															.toUpperCase()
															+ "_titleName_"
															+ item.dataSeqNo).value = item.dataTitle;

											if (item.dataFillerSide != "") {
												document
														.getElementById(item.recordType
																.toUpperCase()
																+ "_justify_"
																+ item.dataSeqNo).value = item.dataFillerSide;
											}
											if (item.dataFiller != "") {
												document
														.getElementById(item.recordType
																.toUpperCase()
																+ "_filler_"
																+ item.dataSeqNo).value = item.dataFiller;
											}
											if (item.valueKey != null
													&& item.valueKey != "") {
												document
														.getElementById(item.recordType
																.toUpperCase()
																+ "_key_"
																+ item.dataSeqNo).value = item.valueKey;
											}
										}
									});

				},
				error : function(error) {
					// console.log(error);
				}
			});
}

function ccfVerSelect() {
	removeAllRows();
	if ("N" != document.getElementById("versionsID").value) {
		document.getElementById("ccfVersionsNames").style.display = "none";
		document.getElementById("addEdit").value = "E";
		getCcfVersionsDtls();
	} else {
		document.getElementById("ccfVersionsNames").style.display = "";
		document.getElementById("addEdit").value = "A";
	}
}

function removeAllRows() {
	if (document.getElementById("ccfHeaderTable") != null) {
		var x = document.getElementById("ccfHeaderTable").rows.length;
		for (var i = 0; i < x; i++) {
			document.getElementById("ccfHeaderTable").deleteRow(0);
		}
	}

	if (document.getElementById("ccfDtlTable") != null) {
		var y = document.getElementById("ccfDtlTable").rows.length;
		for (var i = 0; i < y; i++) {
			document.getElementById("ccfDtlTable").deleteRow(0);
		}
	}

	if (document.getElementById("ccfTrailerTable")) {
		var z = document.getElementById("ccfTrailerTable").rows.length;
		for (var i = 0; i < z; i++) {
			document.getElementById("ccfTrailerTable").deleteRow(0);
		}
	}
}

function returnblank(item) {
	if (item == null) {
		return "";
	} else {
		return item;
	}
}

function addMissingId() {
	var elementsToAddId = [ 'input', 'select', 'textarea' ];
	if (elementsToAddId.length < 200) {
		for (var j = 0; j < elementsToAddId.length; j++) {
			var inputElements = document
					.getElementsByTagName(elementsToAddId[j]);
			for (var i = 0; i < inputElements.length; i++) {
				includeIdIfNotExist(inputElements[i]);
			}
		}
	}
}

function removeCustomError(Field) {
	addMissingId();
	if (window.DOMParser) {
		var element = document.getElementById('lbl' + Field);
		if (element != null) {
			var br = element.previousElementSibling;
			if (br != null) {
				br.parentNode.removeChild(br);
			}
			element.parentNode.removeChild(element);
		}

	} else {
		if (document.getElementById(Field).parentNode.innerHTML
				.indexOf('<BR><LABEL class=required_errmsg>') > 0) {
			document.getElementById(Field).parentNode.innerHTML = document
					.getElementById(Field).parentNode.innerHTML.substring(0,
					document.getElementById(Field).parentNode.innerHTML
							.indexOf('<BR><LABEL class=required_errmsg>'));
		}

	}
}

function includeIdIfNotExist(element) {
	var id = element.getAttribute('id');
	var name = element.getAttribute('name');
	if (name && !id) {
		element.id = name;
	}
}

function customError(Field, msg) {
	addMissingId();
	if (window.DOMParser) {
		var element = document.getElementById('lbl' + Field);
		if (element == null) {
			document.getElementById(Field).parentNode.insertAdjacentHTML(
					'beforeend', '<br><font color="red"><label  id="lbl' + Field
							+ '" class="required_errmsg">' + msg + "</label></font>");
		} else {
			var element = document.getElementById('lbl' + Field); // will
																	// return
																	// element
			if (element != null) {
				var br = element.previousElementSibling;
				if (br != null) {
					br.parentNode.removeChild(br);
				}
				element.parentNode.removeChild(element);
			}
			document.getElementById(Field).parentNode.insertAdjacentHTML(
					'beforeend', '<br><label  id="lbl' + Field
							+ '" class="required_errmsg">' + msg + "</label>");
		}
	}

	else {
		if (document.getElementById(Field).parentNode.innerHTML
				.indexOf('<BR><LABEL id=lbl' + Field
						+ ' class=required_errmsg>') == -1) {
			document.getElementById(Field).parentNode.innerHTML = document
					.getElementById(Field).parentNode.innerHTML
					+ '<br><label id=lbl'
					+ Field
					+ ' class=required_errmsg>'
					+ msg + '</label>';
		} else {
			document.getElementById(Field).parentNode.innerHTML = document
					.getElementById(Field).parentNode.innerHTML.substring(0,
					document.getElementById(Field).parentNode.innerHTML
							.indexOf('<BR><LABEL id=lbl' + Field
									+ ' class=required_errmsg>'));
			document.getElementById(Field).parentNode.innerHTML = document
					.getElementById(Field).parentNode.innerHTML
					+ '<BR><LABEL id=lbl'
					+ Field
					+ ' class=required_errmsg>'
					+ msg + "</label>";
		}
	}

}
