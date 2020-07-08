<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>

<script src="<c:url value="/resources/js/clpvms/package.js" />"></script>
<script src="<c:url value="/resources/js/clpvms/common.js" />"></script>
 <link href="${pageContext.request.contextPath}/resources/css/autocomp/jquery-ui.css" rel="stylesheet" /> 
 <script
	src="${pageContext.request.contextPath}/resources/js/jqueryui.js"></script>

<body class="dashboard" onload="onloadDiv()">
	<form:form name="addPackageForm" id="addPackageForm" method="post"
		class='form-horizontal' commandName="packageForm">
		<div class="body-container">
			<div class="container">
				<section class="content-container">
					<article class="col-lg-9">
						<p>
						<ul class="nav nav-tabs col-lg-9 col-lg-offset-2">
							<li class="active SubMenu">
	<a href="#addPackage" data-toggle="tab" id="mandatory" onClick="Manadatory();"><i class="glyphicon glyphicon-tags"></i> 
	                       <spring:message code="package.AddPackageLabel" /></a></li>
										<li class="SubMenu">
	<a href="#addPrinter" data-toggle="tab" id="nonMandatory" onClick="NonManadatory();"><i class="glyphicon glyphicon-tags"></i> 
							<spring:message code="package.printPackageLabel" /></a></li>
						</ul>
						<div class="tabresp tab-content col-lg-12 col-lg-offset-2 hidden-xs">
							<div class=" graybox tab-pane fade in active" id="package">
								<div class="form-inline">
									<input type="hidden" name="jsPath" id="jsPath"
										value="${pageContext.request.contextPath}/resources/JS_Messages/" />
									<div class="text-right mandatory-red">
										<spring:message code="label.mandatory" text="*Mandatory" />
									</div>
									<c:if test="${failstatus!='' && failstatus!=null}">
										<div class="aln error-red" id="statusPackageId">
											<center>
												<b>${failstatus}</b>
											</center>
										</div>
									</c:if>
									<c:if test="${successstatus!='' && successstatus!=null}">
										<div class="aln success-green" id="statusPackageId">
											<center>
												<b>${successstatus}</b>
											</center>
										</div>
									</c:if>
									<div class="col-lg-9">
									
<!-- added for copy package -->

<!-- -for copy from option --><div id="data">	
										
									<div class="col-lg-12">
									<c:if test="${showCopyFrom=='true' }">
											<div class="col-lg-6">
												<label for="ParentPackageId"> <spring:message
														code="Package.copyFrom" /></label>
											</div>
											
											<input type="hidden" id="addPackageurl" value="${addPackageurl}" />
											<div class="col-lg-4 ui-widget">
												<form:input path="parentPackageId"
													id="comboboxparentPackageId" class="" maxlength="4"
													onkeyup="return isAlphaNumeric(this)"/>
											
												<div><span id="parentPackageIdError"></span></div>
												
											</div>
											<button type="button" id="addPackageForm" class="btn btn-primary"
													onclick="getPackageDetails()">
													<spring:message
														code="button.copy" />
												</button>
											</c:if>
										</div>
									<!-- end -->
<!-- ends -->									
									
									
										
										<div class="col-lg-12">
									
											<div class="col-lg-6">
												<label for="packageId"> <spring:message
														code="package.packageId" /><font color='red'>*</font></label>
											</div>

											<div class="col-lg-6">
												<form:input path="packageId" id="packageId"
													class="textbox textbox-xlarge" type="textarea"
													 maxlength="4"
													onkeyup="return isAlphaNumeric(this)"
													onblur="validateFields(this.form.id,this.id)" />
												<div>
													<form:errors path="packageId" id="packageId"
														class="textbox textbox-xlarge" cssStyle="color:red" />
												</div>
											</div>

										</div>
										<div class="col-lg-12">
											<div class="col-lg-6">
												<label for="description"> <spring:message
														code="package.packageDescription" /><font color='red'>*</font></label>
											</div>

											<div class="col-lg-6">
												<form:input path="description" id="description"
													class="textbox textbox-xlarge" type="textarea"
													 maxlength="100"
													onblur="validateDescriptionField(this.form.id,this.id)" />
												<div>
													<form:errors path="description" id="description"
														class="textbox textbox-xlarge" cssStyle="color:red" />
												</div>
											</div>

										</div>
										<div class="col-lg-12">
											<div class="col-lg-6">
												<label for="replacementPackageId"> <spring:message
														code="package.replacementPackageId" /><font color='red'>*</font></label>
											</div>
											<div class="col-lg-6">
												<form:select path="replacementPackageId"
													id="replacementPackageId" class="dropdown-medium">
													<form:option value="-1" label="--- SAME ---" />
													<form:options items="${packageIdList}" />
												</form:select>
												<div>
													<form:errors path="replacementPackageId" id="replacementPackageId"
													cssStyle="color:red" />
												</div>
											</div>
										</div>
										<div class="col-lg-12">
											<div class="col-lg-6">
												<label for="fulfillmentId"> <spring:message
														code="package.packagefulFillmentID" /><font color='red'>*</font></label>
											</div>
											<div class="col-lg-6">
												<form:select path="fulfillmentId" id="fulfillmentId"
													class="dropdown-medium">
													<form:option value="-1" label="---Select---"/>
													<form:options items="${fulFillmentList}"/>
												</form:select>
												<div>
													<form:errors path="fulfillmentId"  id="fulfillmentId"
													cssStyle="color:red" />
												</div>
											</div>
										</div>
										<div class="col-lg-12">
											<div class="col-lg-6">
												<label for="shipMethods"> <spring:message
														code="package.packageshipMethods" /><font color='red'>*</font></label>
											</div>
											<div class="col-lg-6">
												<form:select path="packageAttributes[shipMethods]"
													id="shipMethods" class="dropdown-medium"
													multiple="multiple">
													<form:options items="${shipmentMethodList}" />
												</form:select>
												<div>
													<form:errors path="packageAttributes['shipMethods']"
														id="shipId" class="textbox textbox-xlarge"
														cssStyle="color:red" />
												</div>
											</div>
										</div>

										<div class="col-lg-12">
											<div class="col-lg-6">
												<label for="replacementshipMethods"> <spring:message
														code="package.packagereplacementshipMethods" /><font
													color='red'>*</font></label>
											</div>
											<div class="col-lg-6">
												<form:select
													path="packageAttributes[replacementshipMethods]"
													id="replacementshipMethods" class="dropdown-medium">
													<form:options items="${shipmentMethodList}" />
												</form:select>
											</div>
										</div>

										<div class="col-lg-12">
											<div class="col-lg-6">
												<label for="expRepshipMethods"> <spring:message
														code="package.packageexpRepshipMethods" /><font
													color='red'>*</font></label>
											</div>
											<div class="col-lg-6">
												<form:select path="packageAttributes[expRepshipMethods]"
													id="expRepshipMethods" class="dropdown-medium">
													<form:options items="${shipmentMethodList}" />
												</form:select>
											</div>
										</div>
										
												<div class="col-lg-12">

												<div class="col-lg-6">
													<label for="IsActive"> <spring:message
															code="package.isActive" /><font color='red'></font></label>
												</div>
												<label for="isActive"> <font color='BLACK'><strong>YES</strong></font></label>
											</div>
											</div>

<div id="Printer">
										<div class="col-lg-12">
											<div class="col-lg-6">
												<label for="carrierId"> <spring:message
														code="package.packagecarrierId" /></label>
											</div>

											<div class="col-lg-6">
												<form:input path="packageAttributes[carrierId]"
													id="carrierId" class="textbox textbox-xlarge"
													type="textarea" maxlength="4" onkeyup="return isNumeric(this)" />
												<div>
													<form:errors path="packageAttributes['carrierId']"
														id="carrierId" class="textbox textbox-xlarge"
														cssStyle="color:red" />
												</div>
											</div>
										</div>

										<div class="col-lg-12">
											<div class="col-lg-6">
												<label for="logoId"> <spring:message
														code="package.packagelogoId" /></label>
											</div>

											<div class="col-lg-6">
												<form:input path="packageAttributes[logoId]" id="logoId"
													class="textbox textbox-xlarge" type="textarea"
													maxlength="10" onkeyup="return isAlphaNumericWithSpacePackage(this)"/>
												<div>
													<form:errors path="packageAttributes['logoId']" id="logoId"
														class="textbox textbox-xlarge" cssStyle="color:red" />
												</div>
											</div>

										</div>
										<div class="col-lg-12">
											<div class="col-lg-6">
												<label for="embossLine3"> <spring:message
														code="package.packageembossLine3" /></label>
											</div>

											<div class="col-lg-6">
												<form:input path="packageAttributes[embossLine3]"
													id="embossLine3" class="textbox textbox-xlarge"
													type="textarea" maxlength="50" onkeyup="return isAlphaNumericPackage(this)"/>
												<div>
													<form:errors path="packageAttributes['embossLine3']"
														id="embossLine3" class="textbox textbox-xlarge"
														cssStyle="color:red" />
												</div>
											</div>

										</div>

										<div class="col-lg-12">
											<div class="col-lg-6">
												<label for="embossLine4"> <spring:message
														code="package.packageembossLine4" /></label>
											</div>

											<div class="col-lg-6">
												<form:input path="packageAttributes[embossLine4]"
													id="embossLine4" class="textbox textbox-xlarge"
													type="textarea" maxlength="50" onkeyup="return isAlphaNumericPackage(this)"/>
												<div>
													<form:errors path="packageAttributes['embossLine4']"
														id="embossLine4" class="textbox textbox-xlarge"
														cssStyle="color:red" />
												</div>
											</div>

										</div>

										<div class="col-lg-12">
											<div class="col-lg-6">
												<label for="envelopeId"> <spring:message
														code="package.packageenvelopeId" /></label>
											</div>

											<div class="col-lg-6">
												<form:input path="packageAttributes[envelopeId]"
													id="envelopeId" class="textbox textbox-xlarge"
													type="textarea" maxlength="10" onkeyup="return isAlphaNumericWithSpacePackage(this)"/>
												<div>
													<form:errors path="packageAttributes['envelopeId']"
														id="envelopeId" class="textbox textbox-xlarge"
														cssStyle="color:red" />
												</div>

											</div>
											</div>

											<div class="col-lg-12">
												<div class="col-lg-6">
													<label for="envelopeSealed"> <spring:message
															code="package.packageenvelopeSealed" /></label>
												</div>

												<div class="col-lg-6">
													<form:input path="packageAttributes[envelopeSealed]"
														id="envelopeSealed" class="textbox textbox-xlarge"
														type="textarea" maxlength="10" onkeyup="return isAlphaNumericWithSpacePackage(this)"/>
													<div>
														<form:errors path="packageAttributes['envelopeSealed']"
															id="envelopeSealed" class="textbox textbox-xlarge"
															cssStyle="color:red" />
													</div>
												</div>

											</div>

											<div class="col-lg-12">
												<div class="col-lg-6">
													<label for="insertId"> <spring:message
															code="package.packageinsertId" /></label>
												</div>

												<div class="col-lg-6">
													<form:input path="packageAttributes[insertId]"
														id="insertId" class="textbox textbox-xlarge"
														type="textarea" maxlength="10" onkeyup="return isAlphaNumericWithSpacePackage(this)" />

													<div>
														<form:errors path="packageAttributes['insertId']"
															id="insertId" class="textbox textbox-xlarge"
															cssStyle="color:red" />
													</div>
												</div>

											</div>

											<div class="col-lg-12">
												<div class="col-lg-6">
													<label for="activationStickerId"> <spring:message
															code="package.packageactivationStickerId" /></label>
												</div>

												<div class="col-lg-6">
													<form:input path="packageAttributes[activationStickerId]"
														id="activationStickerId" class="textbox textbox-xlarge"
														type="textarea" maxlength="10" onkeyup="return isAlphaNumericWithSpacePackage(this)" />
													<div>
														<form:errors
															path="packageAttributes['activationStickerId']"
															id="activationStickerId" class="textbox textbox-xlarge"
															cssStyle="color:red" />
													</div>
												</div>

											</div>

											<div class="col-lg-12">
												<div class="col-lg-6">
													<label for="thermalPrintColorId"> <spring:message
															code="package.packagethermalPrintColorId" /></label>
												</div>

												<div class="col-lg-6">
													<form:input path="packageAttributes[thermalPrintColorId]"
														id="thermalPrintColorId" class="textbox textbox-xlarge"
														type="textarea" maxlength="10" onkeyup="return isAlphaNumericWithSpacePackage(this)" />

													<div>
														<form:errors
															path="packageAttributes['thermalPrintColorId']"
															id="thermalPrintColorId" class="textbox textbox-xlarge"
															cssStyle="color:red" />
													</div>
												</div>

											</div>

											<div class="col-lg-12">
												<div class="col-lg-6">
													<label for="cardPrintVersionId"> <spring:message
															code="package.packagecardPrintVersionId" /></label>
												</div>

												<div class="col-lg-6">
													<form:input path="packageAttributes[cardPrintVersionId]"
														id="cardPrintVersionId" class="textbox textbox-xlarge"
														type="textarea" maxlength="10" onkeyup="return isAlphaNumericWithSpacePackage(this)"/>
													<div>
														<form:errors
															path="packageAttributes['cardPrintVersionId']"
															id="cardPrintVersionId" class="textbox textbox-xlarge"
															cssStyle="color:red" />
													</div>
												</div>

											</div>
											<div class="col-lg-12">
												<div class="col-lg-6">
													<label for="packingSlipId"> <spring:message
															code="package.packagecardpackingSlipId" /></label>
												</div>

												<div class="col-lg-6">
													<form:input path="packageAttributes[packingSlipId]"
														id="packingSlipId" class="textbox textbox-xlarge"
														type="textarea" maxlength="10" onkeyup="return isAlphaNumericWithSpacePackage(this)" />
													<div>
														<form:errors path="packageAttributes['packingSlipId']"
															id="packingSlipId" class="textbox textbox-xlarge"
															cssStyle="color:red" />
													</div>
												</div>

											</div>

											<div class="col-lg-12">
												<div class="col-lg-6">
													<label for="fulfillmentMethod"><spring:message
															code="package.packagecardfulfillmentMethod" /></label>
												</div>

												<div class="col-lg-6">
													<form:select path="packageAttributes[fulfillmentMethod]"
														id="fulfillmentMethod" class="dropdown-medium">
														<form:option value="-1" label="--- Select ---" />
														<form:option value="IND" label="IND:Individual" />
														<form:option value="BLK" label="BLK:Bulk" />
													</form:select>
												</div>
											</div>

											<div class="col-lg-12">
												<div class="col-lg-6">
													<label for="bundleSize"> <spring:message
															code="package.packagecardbundleSize" /></label>
												</div>

												<div class="col-lg-6">
													<form:input path="packageAttributes[bundleSize]"
														id="bundleSize" class="textbox textbox-xlarge"
														type="textarea" maxlength="10" onkeyup="return isNumeric(this)"/>
													<div>
														<form:errors path="packageAttributes['bundleSize']"
															id="bundleSize" class="textbox textbox-xlarge"
															cssStyle="color:red" />
													</div>
												</div>

											</div>

									
											

										</div>
										</div>
											
											<div class="col-lg-12 text-center">

												<div class=""></div>
												<div class="">
													<button type="button" class="btn btn-primary"
														onclick="addPackage();">
														<i class="glyphicon glyphicon-plus"></i>
														<spring:message code="button.add" />
													</button>
													<button type="button" onclick="ResetAddPackage(this.form.id)"
														class="btn btn-primary gray-btn">
														<i class="glyphicon glyphicon-refresh"></i>
														<spring:message code="button.reset" />
													</button>
													<button type="button" class="btn btn-primary gray-btn"
														onclick="goBackToPackage();">
														<i class="glyphicon glyphicon-backward"></i>
														<spring:message code="button.back" />
													</button>


												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
					</article>
				</section>
			</div>
		</div>
	</form:form>
</body>



<!-- added by nawaz for smart search -->

   <style>
  .custom-combobox {
    position: relative;
    display: inline-block;
  }
  .custom-combobox-toggle {
    position: absolute;
    top: 0;
    bottom: 0;
    margin-left: -1px;
    padding: 0;
  }
  .custom-combobox-input {
    margin: 0;
    padding: 5px 10px;
  }
  
  .ui-autocomplete {
    max-height: 400px;
    overflow-y: auto;   /* prevent horizontal scrollbar */
    overflow-x: hidden; /* add padding to account for vertical scrollbar */
    z-index:1000 !important;
}
  </style>

<script>
$(document).ready(function() {
    BindControls();
});


function callAjax()
{
	
var parentMsg="";
var srvUrl=$('#addPackageurl').val();
var str=srvUrl+"/ajax/getParentPackagedetails";

$.ajax({

     
    url: str,

     async:false,

     type: "GET",

     dataType: 'json',

    
    success:function(response, status, xhr){ 

    	 $.each(response,function(index){     
    	   		
    		 parentMsg=parentMsg+'#'+response[index];
    		 
    		 	      }); 
   	  
     },
     error : function(jqXHR, textStatus, errorThrown) {

		  console.log("error:" + textStatus + " exception:" + errorThrown);
	}

   });
   
   
   return parentMsg;
   
} 

function BindControls() {
	
 	
var Output=callAjax();
	
	
	var newData=Output.split('#');
    var ProdData =newData;
	
     var selectItem = function (event, ui) {
        $("#comboboxparentPackageId").val(ui.item.value);
        return false;
    } 
    
    
    $('#comboboxparentPackageId').autocomplete({
        source: ProdData,
        minLength: 0,
        select:selectItem,
        change: function (event, ui) {
            if (!ui.item) {
           this.value='';
             
            }
        },
        scroll: true
    }).focus(function() {
        $(this).autocomplete("search", "");
    });
}


function Manadatory()
{ 
$("#Printer").hide();
$("#data").show();
}

function NonManadatory()
{ 
$("#data").hide();
$("#Printer").show();
}

function onloadDiv(){	
$("#Printer").attr("style", "display:none");
$("#data").attr("style", "display:block");
}

</script>