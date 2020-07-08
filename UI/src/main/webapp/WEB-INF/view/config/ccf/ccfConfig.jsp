<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>
<head>
<link href="<c:url value="/resources/css/tab.css" />" rel="stylesheet" />
<script src="<c:url value="/resources/js/tabber.js" />"></script>
<script src="<c:url value="/resources/js/tp.js" />"></script>
<script src="<c:url value="/resources/js/json2.min.js" />"></script>
<script src="<c:url value="/resources/js/clpvms/ccfconfig.js" />"></script>
</head>
<body class="bodyClass">

	<form:form id="ccfForm" method="post" commandName="ccfForm">
		<br><br><br>
		
		<table style="width: 40%;" align="center" cellspacing="0"
			cellpadding="0">
			<tr>
				<td align="center"><div class="aln success-green">
						<span id="respMsg"></span>
					</div></td>
			</tr>
		</table>
		<table style="width:30%;" id="ccfDtlTables" border="1" align="center"
			cellspacing="0" cellpadding="0" class='form_bg'>
			<tr>
				<td><Label class='label2' style="width: 250px;"><spring:message
							code="ccf.versionID" /><font color='red'>*</font></label></td>
				<td style="text-align: center"><form:select path="ccfFormatVersion"
						class="dropdown-medium" name="versionsID" id="versionsID"
						onChange="ccfVerSelect();">
						<option value="N">--- NEW ---</option>
						<form:options items="${ccfData}" />
					</form:select></td>
			</tr>
			<tr id="ccfVersionsNames">
				<td><Label class='label2' style="width: 250px;"><spring:message
							code="ccf.versionName" /><font color='red'>*</font></label></td>
				<td style="text-align: center"><input type="text" style="width: 227px; height: 30px;"
					name="versionName" id="versionName" /></td>
			</tr>
		</table>
		<div class="tabber">
			<div class="tabbertab" align="center">
				<center>
					<div style="text-align: center;">
						<span> <input class="submit" type="button" value="Add Row"
							onclick="addRow('ccfHeaderTable')" /></span> <span> <input
							class="submit" type="button" value="Delete Row"
							onclick="deleteRow('ccfHeaderTable')" />
						</span>
					</div>
					<h2>CCF Header</h2>
					<table id="ccfHeaderTable" border="1" align="center"
						cellspacing="0" cellpadding="0" class='form_bg'>
					</table>
				</center>
			</div>
			<div class="tabbertab" align="center">
				<center>
					<div style="text-align: center;">
						<span> <input class="submit" type="button" value="Add Row"
							onclick="addRow('ccfDtlTable')" /></span> <span> <input
							class="submit" type="button" value="Delete Row"
							onclick="deleteRow('ccfDtlTable')" />
						</span>
					</div>
					<h2>CCF Detail</h2>
					<table id="ccfDtlTable" border="1" align="center" cellspacing="0"
						cellpadding="0" class='form_bg'>

					</table>
				</center>
			</div>
			<div class="tabbertab" align="center">
				<center>
					<div style="text-align: center;">
						<span> <input class="submit" type="button" value="Add Row"
							onclick="addRow('ccfTrailerTable')" /></span> <span> <input
							class="submit" type="button" value="Delete Row"
							onclick="deleteRow('ccfTrailerTable')" />
						</span>
					</div>
					<h2>CCF Trailer</h2>
					<table id="ccfTrailerTable" border="1" align="center"
						cellspacing="0" cellpadding="0" class='form_bg'>
					</table>
				</center>
			</div>
		</div>
		<br>
		<center>
			<button type="button" class="btn btn-primary" onclick="saveData();">
				<i class="glyphicon glyphicon-plus"></i>
				<spring:message code="button.save" />
			</button>
		</center>
		<input id="addEdit" type="hidden" value="A" />
		<br>
		<br>
		<br>
		<br>
		<br>
		<br>
	</form:form>

	<script type='text/javascript'>
		var headerOptions = '';
		var detailOptions = '';
		var trailerOptions = '';
		var keyOptions = '';
		var baseURL = '';
		$(document).ready(function() {
			baseURL = window.location.protocol + "//" + window.location.host + "" + window.location.pathname;
	 		baseURL = baseURL.substring(0,baseURL.lastIndexOf("/")+1);
			iniForm();
		})
		function iniForm() {
			$.ajax({
				url : baseURL+'ccfparam',
				type : "GET",
				dataType : "json",
				success : function(data) {
					$.each(data, function(number, item) {
						if (item.paramSection != null
								&& "H" == item.paramSection) {
							headerOptions += '<option value='+item.paramId+'>'
									+ item.paramDesc + '</option>'
						}
						if (item.paramSection != null
								&& "D" == item.paramSection) {
							detailOptions += '<option value='+item.paramId+'>'
									+ item.paramDesc + '</option>'
						}
						if (item.paramSection != null
								&& "T" == item.paramSection) {
							trailerOptions += '<option value='+item.paramId+'>'
									+ item.paramDesc + '</option>'
						}

					});
				},
				error : function(error) {
				}
			});

			$.ajax({
				url : baseURL+'ccfkey',
				type : "GET",
				dataType : "json",
				async : false,
				crossDomain : true,
				success : function(response) {
					$.each(response, function(number, item) {
						keyOptions += '<option value='+item.value+'>'
								+ item.key + '</option>'
					});
				},
				error : function(error) {
					//console.log('error:::'+error)   
				}
			});

		}

		function addRow(tableID) {
			var table = document.getElementById(tableID);
			var rowCount = table.rows.length;
			
			var header = '';
			if (rowCount == 0) {
				header = '<TR class="TR3"><TD style="width:2%"></td><TD style="width:8%" align="center" >Sequence No</td><TD style="width:33%" align="center">Record Title</td><TD style="width:8%" align="center">Length</td><TD style="width:8%" align="center">Default Value</td><TD style="width:8%" align="center">Format</td><TD style="width:15%" align="center" >Key</td><TD style="width:8%" align="center">Filler</td><TD style="width:8%" align="center">Left/Right Justified</td></tr>';
			}
			
			if (rowCount <= 1) {
				if ('ccfHeaderTable' == tableID) {
					header += '<tr><td><INPUT style="width:85%"  id=H_chk_1 type="checkbox" name="H_chk_1" /></td><td><input  style="width:85%"  id=H_seqNo_1 type="text" name="H_seqNo_1" value=1   /></td><td><SELECT style="width:100%" id=H_titleName_1 type="text" name="H_titleName_1"  >'
							+ headerOptions
							+ '</SELECT></td><td><input  style="width:85%" id=H_dataLength_1 type="text" name="H_dataLength_1" /></td><td><input  style="width:85%" id=H_defaultValue_1 type="text" name="H_defaultValue_1"  /></td><td><input  style="width:85%" id=H_format_1 type="text" name="H_format_1" /></td>  <td><SELECT  style="width:100%" id=H_key_1 type="text" name="H_key_1" ><option value="">NS</option>'
							+ keyOptions
							+ '</SELECT></td> <td><SELECT  style="width:99%" id=H_filler_1 name="H_filler_1" style="width:100%";><option value=" ">Space</option><option value="0">Zero</option></SELECT></td><td><SELECT  style="width:100%" id=H_justify_1 name="H_justify_1" style="width:100%";><option value="L">Left</option><option value="R">Right</option></SELECT></td></tr>';
				} else if ('ccfDtlTable' == tableID) {
					header += '<tr><td><INPUT style="width:100%"  id=D_chk_1 type="checkbox" name="D_chk_1" /></td><td><input  style="width:85%"  id=D_seqNo_1 type="text" name="D_seqNo_1"  value=1 /></td><td><SELECT  style="width:100%" id=D_titleName_1 type="text" name="D_titleName_1"  >'
							+ detailOptions
							+ '</SELECT></td><td><input  style="width:85%" id=D_dataLength_1 type="text" name="D_dataLength_1" /></td><td><input  style="width:85%" id=D_defaultValue_1 type="text" name="D_defaultValue_1"  /></td><td><input  style="width:85%" id=D_format_1 type="text" name="D_format_1" /></td>  <td><SELECT  style="width:100%" id=D_key_1 type="text" name="D_key_1" ><option value="">NS</option>'
							+ keyOptions
							+ '</SELECT></td>  <td><SELECT  style="width:100%" id=D_filler_1 name="D_filler_1" style="width:100%";><option value=" ">Space</option><option value="0">Zero</option></SELECT></td><td><SELECT  style="width:100%" id=D_justify_1 name="D_justify_1" style="width:100%";><option value="L">Left</option><option value="R">Right</option></SELECT></td></tr>';
				} else if ('ccfTrailerTable' == tableID) {
					header += '<tr><td><INPUT style="width:100%"  id=T_chk_1 type="checkbox" name="T_chk_1" /></td><td><input  style="width:85%"  id=T_seqNo_1 type="text" name="T_seqNo_1"  value=1 /></td><td><SELECT style="width:100%"  id=T_titleName_1 type="text" name="T_titleName_1"  >'
							+ trailerOptions
							+ '</SELECT></td><td><input  style="width:85%" id=T_dataLength_1 type="text" name="T_dataLength_1" /></td><td><input  style="width:85%" id=T_defaultValue_1 type="text" name="T_defaultValue_1"  /></td><td><input style="width:85%"  id=T_format_1 type="text" name="T_format_1" /></td>  <td><SELECT  style="width:100%" id=T_key_1 type="text" name="T_key_1" ><option value="">NS</option>'
							+ keyOptions
							+ '</SELECT></td> <td><SELECT  style="width:100%" id=T_filler_1 name="T_filler_1" style="width:100%";><option value=" ">Space</option><option value="0">Zero</option></SELECT></td><td><SELECT  style="width:100%" id=T_justify_1 name="T_justify_1" style="width:100%";><option value="L">Left</option><option value="R">Right</option></SELECT></td></tr>';
				}
				$('#' + tableID).append(header);
				table = document.getElementById(tableID);
				rowCount = table.rows.length;
			} else {
				var row = table.insertRow(rowCount);
				var colCount = table.rows[1].cells.length;
				var count = parseInt(rowCount);
				for (var i = 0; i < colCount; i++) {
					var newcell = row.insertCell(i);
					newcell.innerHTML = table.rows[1].cells[i].innerHTML;
					switch (newcell.childNodes[0].type) {

					case "text":
						var ids = newcell.childNodes[0].id;
						ids = ids.substr(0, ids.length - 1);
						newcell.childNodes[0].value = "";
						var radomCount = random(199, 9999999);
						newcell.childNodes[0].id = ids + (radomCount);
						if(ids.indexOf('dataLength') >= 0){
							$("#"+ids + (radomCount)).next().html('');
						}
						
						newcell.childNodes[0].name = ids + (count);
						break;
					case "checkbox":
						newcell.childNodes[0].checked = false;
						break;
					case "select-one":
						newcell.childNodes[0].selectedIndex = 0;
						break;
					}
				}
			}
		}

		function random(high, low) {
			high++;
			return Math.floor((Math.random()) * (high - low)) + low;
		}

		function random(high, low) {
			high++;
			return Math.floor((Math.random()) * (high - low)) + low;
		}

		function deleteRow(tableID) {
			var checkBoxFlag=true;
			try {
				var table = document.getElementById(tableID);
				var rowCount = table.rows.length;
				for (var i = 0; i < rowCount; i++) {
					var row = table.rows[i];
					var chkbox = row.cells[0].childNodes[0];
					if (null != chkbox && true == chkbox.checked) {
						checkBoxFlag=true
						if (rowCount <= 2) {
							alert("Cannot delete all the rows.");
							break;
						}

						table.deleteRow(i);
						rowCount--;
						i--;
					}
					else{
						checkBoxFlag=false;
					}
				}
				if(!checkBoxFlag){
					alert("Please select atleast one row.");
				}
			} catch (e) {
				alert(e);
			}
		}

		function isInteger(x) {
			var flag = false;
			try {
				flag = (x % 1 === 0);
			} catch (Exception) {
				flag = false;
			}
			return flag;
		}
	</script>
</body>