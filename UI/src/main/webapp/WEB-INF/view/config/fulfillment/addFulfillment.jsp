<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>
<script src="<c:url value="/resources/js/clpvms/fulfillment.js" />"></script>

<script type="text/javascript">
	$(document).ready(function() { // OnLoad
		checkAutoShipped();
		checkCNfilereq();

	/* 	var elements = [ 'Date', 'FileCount', 'PrintVendor', 'ProductCode' ];
		$('#ccfFileFormat').textcomplete([ {
			match : /<(\w*)$/,
			search : function(term, callback) {
				callback($.map(elements, function(element) {
					return element.indexOf(term) === 0 ? element : null;
				}));
			},
			index : 1,
			replace : function(element) {
				return [ '<<' + element + '>>', '' ];
			}
		} ]);
		$('#replaceCcfFileFormat').textcomplete([ {
			match : /<(\w*)$/,
			search : function(term, callback) {
				callback($.map(elements, function(element) {
					return element.indexOf(term) === 0 ? element : null;
				}));
			},
			index : 1,
			replace : function(element) {
				return [ '<<' + element + '>>', '' ];
			}
		} ]); */
	})

	function checkCNfilereq() {
		var type = document.getElementById("b2bCNFileReqYes");
		if (type.checked == true) {
			document.getElementById("b2bCNFileIdentifierID").style.display = '';
			document.addFulFillmentForm.b2bCnFileIdentifier.value = "";
		} else {
			document.getElementById("b2bCNFileIdentifierID").style.display = 'none';
		}
	}

	function checkAutoShipped() {
		var type = document.addFulFillmentForm.isAutomaticShipped.value;

		if (type == "0") {
			document.getElementById("shippedTimeDealyID").style.display = 'none';
		} else {
			document.getElementById('shippedTimeDealy').value="48";
			document.getElementById("shippedTimeDealyID").style.display = '';
		}
	}
	
	function allowAlphaNumericWithSpaceAndUnderScore(e) {
		 var charCode;
		    if (e.keyCode > 0) {
		        charCode = e.which || e.keyCode;
		    }
		    else if (typeof (e.charCode) != "undefined") {
		        charCode = e.which || e.keyCode;
		    }
			 //space,underscore
		    if (charCode == 32 || charCode == 95 )
		        return true;
			 //numbers
		    if (charCode >= 48 && charCode <= 57)
		        return true;
		    //for lowercase
		    if(charCode >= 97 && charCode <= 122)	 
		      return true;
			if(charCode >= 65 && charCode <= 90)	 
		      return true; 
			return false;   
	}
	

	function checkZero(formId,eleId)
      {
		var val=$("#"+eleId).val();
		if(parseInt(val)==0)
		{
			generateAlert(formId, eleId ,eleId +".zero");
			return false;
		}
		else
		{
			return true;
		}
	 }
</script>
<div class="body-container" style="min-height: 131px;">     

<div class="container">

 <div id="feedBackTd" class="col-lg-12 text-center" style="padding-top:4px">
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out><p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"></c:out></p></c:if>
			</div>
 
<ul  class="nav nav-tabs col-lg-6 col-lg-offset-3">
			<li class="active"><a href="#addFulFillment" data-toggle="tab"
				class="hoverColor"><i class="glyphicon glyphicon-tags"></i>&nbsp;
				<spring:message code="fullFillment.AddFulFillmentLabel" /></a></li>

</ul>
		
<div class="tabresp tab-content">
			
		<div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3" id="fulfillment">
			<div class="textbox text-right Error-red">
				<spring:message code="label.mandatory" text="*Mandatory" />
			</div>
		<div class="form-inline">		
		
	<form:form name="addFulFillmentForm" id="addFulFillmentForm" method="post" class='form-horizontal' commandName="fulFillmentForm">
		<input type="hidden" name="jsPath" id="jsPath" value="${pageContext.request.contextPath}/resources/JS_Messages/" />
									
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="fulfillmentID"> <spring:message
														code="fullFillment.fulfillmentID" /><font style="color: red">*</font></label>
											</div>
											<div class="col-lg-8">
												<form:input  path="fulfillmentID" id="fulfillmentID"
													class="textbox textbox-large trim"
													onkeypress="return allowAlphaNumericWithSpaceAndUnderScore(event)"
													onblur="validateFields(this.form.id,this.id)"
													type="textarea" minlength="2" maxlength="50" />
												<div>
													<form:errors path="fulfillmentID" id="fulfillmentID" cssClass="fieldError"/>
												</div>
											</div>
										</div>
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="fulfillmentName"> <spring:message
														code="fullFillment.fulfillmentName" /><font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">
												<form:input  path="fulFillmentName" id="fulFillmentName"
													class="textbox textbox-large trim"
													onkeypress="return allowAlphaNumericWithSpaceAndUnderScore(event)"
													onblur="validateFields(this.form.id,this.id)"
													type="textarea" minlength="2" maxlength="200" />
													<div>
													<form:errors  path="fulFillmentName" id="fulFillmentName" cssClass="fieldError" />
													</div>
											</div>
										</div>
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="automaticShipment"> <spring:message
														code="fullFillment.automaticShipment" /></label>
											</div>
											<div class="col-lg-8">
												<form:select path="isAutomaticShipped"
												   	id="isAutomaticShipped" class="textbox dropdown-medium trim"
													onChange="checkAutoShipped('add');">
													<form:options items="${isAutomaticShippedList}" />
												</form:select>
											</div>
										</div>
										<div class="col-lg-12" id="shippedTimeDealyID">
											<div class="col-lg-4">
												<label for="shippedTimeDealy"><spring:message
														code="fullFillment.shippedTimeDealy" /></label>
											</div>
											<div class="col-lg-8">
												<form:input path="shippedTimeDealy" id="shippedTimeDealy"
													class="textbox textbox-large trim" value="48" type="textarea"
													onblur="validateFields(this.form.id,this.id);checkZero(this.form.id,this.id);"
													maxlength="3" />
											</div>
											<div>
											<form:errors  path="shippedTimeDealy" id="shippedTimeDealy" cssClass="fieldError" />
											</div>
										</div>
										<div class="col-lg-12" style="padding-bottom: 5px">
											<div class="col-lg-4">
												<label for="CCFFileFormat"><spring:message
														code="fullFillment.CCFFileFormat" /><font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">
												<form:textarea  path="ccfFileFormat" id="ccfFileFormat"
													class="textbox textbox-large trim" type="textarea"
													onblur="validateFields(this.form.id,this.id)"
													minlength="1"  maxlength="100"
													 />
												<div>
													<form:errors path="ccfFileFormat" id="ccfFileFormat" cssClass="fieldError" />
												</div>
											</div>
										</div>
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="repCCFFileFormat"><spring:message
														code="fullFillment.repCCFFileFormat" /><font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">
												<form:textarea  path="replaceCcfFileFormat"
													id="replaceCcfFileFormat" class="textbox textbox-large trim"
													onblur="validateFields(this.form.id,this.id)"
													type="textarea" minlength="1"
													maxlength="100"
													 />
												<div>
													<form:errors path="replaceCcfFileFormat"
														id="replaceCcfFileFormat" cssClass="fieldError" />
												</div>
											</div>
										</div>
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="b2bCNFileReq"><spring:message
														code="fullFillment.b2bCNFileReq" /><font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">
												<div class="radiobox-checkline">
														<form:radiobutton value="enabled" path="b2bVendorConfReq"
															id="b2bCNFileReqYes" name="b2bCNFileReqYes"
															onclick="checkCNfilereq();" checked="checked" />
														<label class='radiobox-line' for="b2bCNFile_status"><spring:message
																code="global.radioEnabled" /></label>

														<form:radiobutton value="disabled" path="b2bVendorConfReq"
															id="b2bCNFileReqNo" name="b2bCNFileReqNo"
															data-skin="square" onclick="checkCNfilereq();"
															data-color="blue" />
														<label class='radiobox-line' for="b2bCNFile_status"><spring:message
																code="global.radioDisabled" /></label>
													</div>
											</div>
										</div>
										<div class="col-lg-12" id="b2bCNFileIdentifierID">
											<div class="col-lg-4">
												<label for="b2bCNFileIdentifier"><spring:message
														code="fullFillment.b2bCNFileIdentifier" /><font
													color='red'>*</font></label>
											</div>

											<div class="col-lg-8">
												<form:input path="b2bCnFileIdentifier"
													id="b2bCnFileIdentifier" class="textbox textbox-large trim"
													type="textarea" minlength="1" maxlength="20" />
											</div>
											<div>
											<form:errors  path="b2bCnFileIdentifier" id="b2bCnFileIdentifier" cssClass="fieldError" />
											</div>
										</div>
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="b2bResFileIdentifier"><spring:message
														code="fullFillment.b2bResFileIdentifier" /></label>
											</div>

											<div class="col-lg-8">
												<form:input path="b2bPrinterRespIdentifier"
													id="b2bPrinterRespIdentifier"
													onblur="validateFields(this.form.id,this.id)"
													class="textbox textbox-large trim" type="textarea"
													minlength="1" maxlength="20" />
											</div>
											<div>
											<form:errors  path="b2bPrinterRespIdentifier" id="b2bPrinterRespIdentifier" cssClass="fieldError" />
											</div>
										</div>
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="b2bConfFileIdentifier"><spring:message
														code="fullFillment.b2bConfFileIdentifier" /></label>
											</div>

											<div class="col-lg-8">
												<form:input path="b2bConfFileIdentifier"
													id="b2bConfFileIdentifier" class="textbox textbox-large trim"
													onblur="validateFields(this.form.id,this.id)"
													type="textarea" minlength="1" maxlength="20" />
											</div>
											<div>
											<form:errors  path="b2bConfFileIdentifier" id="b2bConfFileIdentifier" cssClass="fieldError" />
											</div>
										</div>
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="mftDestinationKey"><spring:message
														code="fullFillment.mftDestinationKey" /></label>
											</div>

											<div class="col-lg-8">
												<form:input path="mftDestinationKey"
													id="mftDestinationKey" class="textbox textbox-large trim"
													type="textarea" minlength="1" maxlength="100" />
											</div>
											<%-- <div>
											<form:errors  path="mftDestinationKey" id="mftDestinationKey" cssClass="fieldError" />
											</div> --%>
										</div>
										<div class="col-lg-12">

											<div class="col-lg-4"></div>
											<div class="col-lg-8">
												<button type="button" class="btn btn-primary"
													onclick="addFulfillment(this.form.id);">
													<i class="glyphicon glyphicon-plus"></i>
													<spring:message code="button.add" />
												</button>
												<button type="button"
													onclick="ResetAdd(this.form.id);checkAutoShipped();checkCNfilereq();"
													class="btn btn-primary gray-btn">
													<i class="glyphicon glyphicon-refresh"></i>
													<spring:message code="button.reset" />
												</button>
												<button type="button" class="btn btn-primary gray-btn"
													onclick="goBackToFulfilment();">
													<i class="glyphicon glyphicon-backward"></i>
													<spring:message code="button.back" />
												</button>
											</div>
										</div>
	</form:form>
	</div>
	</div>
	</div>
	
</div>
</div>