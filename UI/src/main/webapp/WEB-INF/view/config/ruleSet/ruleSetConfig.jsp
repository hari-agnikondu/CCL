<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>
<script src="<c:url value="/resources/js/clpvms/ruleset.js" />"></script>
<script src="<c:url value="/resources/js/clpvms/General_Validation.js" />"></script>
<script type="text/javascript">


	if (!Array.prototype.indexOf) {
		Array.prototype.indexOf = function(what, i) {
			i = i || 0;
			var L = this.length;
			while (i < L) {
				if (this[i] === what)
					return i;
				++i;
			}
			return -1;
		};
	}

	var operationList = new Array();
	var rulesArray = new Array();
	var existingRulesArray = new Array();
	var deletedValues = new Array();

	function addRule() {
		var totalRow = tblFieldDef.rows.length;
		totalRow = parseInt(totalRow);
		var i = 0;
		var newRow, newSelect, newRule;
		if(document.getElementById("ruleId").value == "-1")
	    {
		 generateAlert('ruleSetForm', 'ruleId','ruleId.empty');
	      return false;
	    }
		clearError('ruleSetForm #ruleId');
	    
		var ruleId = document.getElementById("ruleId").value;
		var ruleIdSel = document.getElementById("ruleId");
		var ruleName = ruleIdSel.options[ruleIdSel.selectedIndex].text;
	
		if (rulesArray.length > 0) {
			for (var ruleIndex = 0; ruleIndex < rulesArray.length; ruleIndex++) {
				if (rulesArray[ruleIndex].indexOf(ruleId) > -1) {
					alert("Rule already exist");
					return;
				}
			}
		}

		var operationsEle = ruleId;
		newRow = tblFieldDef.insertRow(totalRow);
		newSelect = newRow.insertCell();
		newRule = newRow.insertCell();
		newRuleView=newRow.insertCell();
		newSelect.style.textAlign = "center";
		newRule.style.textAlign = "center";
		newRuleView.style.textAlign ="center";
		newSelect.innerHTML = '<input type="button" name="delete" id="delete" value ="Delete Rule" OnClick="deleteRow(this, \''
				+ operationsEle
				+ '\');"   class="btn btn-primary" >';
		newRule.innerHTML = '<font color="black"><input type="text" name="rule" id="rule" maxlength="50" value="'+ruleName+'" readonly=true></font>';
		newRuleView.innerHTML = '<input type="button" name="view" id="view" value ="View Rule" OnClick="viewRule(\''+ operationsEle+ '\');"   class="btn btn-primary" >';
		operationList.push(operationsEle);
		rulesArray.push(new Array(ruleName.trim(), operationsEle));

		document.getElementById("operationList").value = operationList;
	}

	function deleteRow(src, rule) {
		var i = src.parentNode.parentNode.rowIndex;
		tblFieldDef.deleteRow(i);

		for (i = 0; i < rulesArray.length; i++) {
			var index = rulesArray[i].indexOf(rule);
			if (index >= 0) {
				rulesArray.splice(i, 1);
			}
		}
	   
		var index1 = operationList.indexOf(rule);
		if (index1 > -1) {
			operationList.splice(index1,1);
		} 

		
		for (i = 0; i < existingRulesArray.length; i++) {
			var strArray = existingRulesArray[i]+'';
			var arr = strArray.split(',');
			var last = arr[arr.length - 1];
			  if(last == rule){
				deletedValues.push(rule);
			}
			 
		}
		if (operationList.length == 0) {
			document.getElementById("operationList").value = null;
		} else {
			document.getElementById("operationList").value = operationList;
		}

		document.getElementById("deletedValues").value=deletedValues;
	}
	function viewRule(ruleId)
	{
		var url="<%=request.getContextPath()%>";
		url=url+'/config/rule/viewRule?ruleId='+ruleId;
		window.open (url,'viewRule', 'height=500,width=1080,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes');
	}


	function change() {
		removeCustomError('ruleId');
		if (tblFieldDef.rows.length > 1)

			while (tblFieldDef.rows.length > 1) {
				tblFieldDef.deleteRow(1);
			}
		rulesArray = new Array();
		deletedValues = new Array();
		operationList = new Array();

		if (document.getElementById("ruleSetId").value == "-1") {
			return;
		}
		var baseURL = window.location.protocol + "//" + window.location.host + "" + window.location.pathname;
 		baseURL = baseURL.substring(0,baseURL.lastIndexOf("/")+1);
 		
 		$.ajax({
			url : baseURL+'ruleDetails',
			type : "GET",
			data : {
				ruleSetId : $('#ruleSetId').val()
			},
			dataType : "json",
			async : false,
			crossDomain : true,
			success : function(response) {
				$.each(response, function(number, item) {
					var totalRow = parseInt(tblFieldDef.rows.length);
					var i=0;
					var newRow,newSelect,newRule;
					newRow = tblFieldDef.insertRow(totalRow);
					newSelect = newRow.insertCell();
					newRule = newRow.insertCell();
					newRuleView=newRow.insertCell();
					newSelect.style.textAlign = "center";
					newRule.style.textAlign = "center";		
					newRuleView.style.textAlign="center";
					newSelect.innerHTML = '<input type="button" name="delete" id="delete" value ="Delete Rule" OnClick="deleteRow(this, \''+ item.key+ '\');"   class="btn btn-primary" Onmouseover="SubmitOver(this)" Onmouseout="SubmitOut(this)">';
				    newRule.innerHTML = '<font color="black"><input type="text" name="rule" id="rule" maxlength="50" value="'+item.value+'" readonly=true></font>';
					newRuleView.innerHTML = '<input type="button" name="view" id="view" value ="View Rule" OnClick="viewRule(\''+ item.key+ '\');"   class="btn btn-primary" >';
				    operationList.push(item.key);
				    rulesArray.push(new Array(item.value, item.key));
	 				existingRulesArray.push(new Array(item.value, item.key));
		 			});
			},
			error : function(error) {
				//console.log('error:::'+error)   
			}
		});
		document.getElementById("operationList").value=operationList;
	}
</script>

<body>
	<div class="body-container">
		<div class="container">
			<br>&nbsp;
			<div id="feedBackTd" class="form-group text-center">
				<c:if test="${statusFlag=='success' }">
					<p class="successMsg"
						style="text-align: center; font-weight: bold;">
						<c:out value="${statusMessage }" />
					</p>
				</c:if>
				<c:if test="${statusFlag!='success' }">
					<p class="fieldError"
						style="text-align: center; font-weight: bold;">
						<c:out value="${statusMessage }" />
					</p>
				</c:if>
			</div>

			<form:form name="ruleSetForm" id="ruleSetForm" method="post"
				class='form-horizontal' commandName="ruleSetForm">
			<form:hidden name="operationList" path="operationList" id="operationList" />
			<form:hidden name="deletedValues" path="deletedValues" id="deletedValues" />
			
				<section class="content-container">

					<article class="col-lg-12">

						<ul class="nav nav-tabs col-lg-11 col-lg-offset-0.5">
							<li class='active SubMenu'><a data-toggle='tab'><i
									class="glyphicon glyphicon-tags"></i> <spring:message
										code="ruleSet.ruleSetName" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active  graybox col-lg-11 col-lg-offset-0.5"
								id="rule">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<table align="center" style="width: 50%">
									<tr>
										<td><label for="ruleSetName"><spring:message
													code="ruleSet.ruleSetName" /><font color='red'>*</font></label></td>
										<td><form:select path="ruleSetId" id="ruleSetId"
												class="dropdown-medium" onchange="fillRuleSetName(this);change();">
												<form:option value="-1" label="--- NEW ---" />
												<form:options items="${ruleSet}" />s
											</form:select></td>

										<td><form:input path="ruleSetNametxt" id="ruleSetNametxt"
									         	title="Allowed Special Characters are .,;'_- " minlength="2"
									         	 onkeyup="return isAlphaNumericWithSpecialChars(this)"
												class="textbox xlarge4label trim" name='ruleSetNametxt'
												type="textarea" maxlength="50" /></td>

									</tr>
									<tr>
										<td><label for="ruleName"><spring:message
													code="ruleSet.ruleName" /><font color='red'>*</font></label></td>
										<td colspan=2><form:select path="ruleId" id="ruleId"
												class="dropdown-medium">
												<form:option value="-1" label="--- NOT SELECTED ---" />
												<form:options items="${rules}" />
											</form:select></td>


										<!-- 		<td>
											<button type="button" class="btn btn-primary"
												style="bottom: 15px; position: relative;"
												onclick="addRule()">
												<i class='glyphicon glyphicon-plus'></i> Add Rule
											</button>
										</td> -->
									</tr>
									<tr></tr>
								</table>
								<br> <br>
								<table align="center" style="width: 50%">
									<tr align="center">
										<td>
											<button type="button" class="btn btn-primary"
												style="bottom: 15px; position: relative;"
												onclick="addRule()">
												<i class='glyphicon glyphicon-plus'></i>
												<spring:message code="ruleSet.addRule" />
											</button>
										</td>
									</tr>
								</table>
								<br>
							<table id="tblFieldDef" class="table table-hover table-bordered"
									style="width: 65% !important;">
									<thead>
										<tr>
											<th align="center"><spring:message code="ruleSet.delete" /></th>
											<th align="center"><spring:message
													code="ruleSet.ruleName" /></th>
											<th align="center"><spring:message code="ruleSet.view" /></th>		
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</article>
				</section>
				<center>
					<button type="button" class="btn btn-primary"
						style="bottom: 15px; position: relative;" onclick="saveRuleSet()">
						<i class='glyphicon glyphicon-plus'></i> <spring:message code="button.save" />
					</button>
				</center>
					</form:form>

		</div>
		</div>

</body>
