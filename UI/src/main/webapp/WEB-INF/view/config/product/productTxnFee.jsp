<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<style>
.btn{
margin-left: 15px;}
input{
	width:-moz-available;
}
.tab-pane{
	width: -moz-fit-content;
	width : fit-content; 
	border-top:1px solid #a1a1a1;
	float:left;
}
.boing {
    margin-left:5px;
    color:red;
    visibility: hidden;
}
input{
	width:90%;
	padding: 0px 0px;
}
input[type='checkbox']{
	
	border:0px;

}

input:-moz-read-only { /* For Firefox */
     background-color: rgb(235, 235, 228);!important
}

input:read-only {
     background-color: rgb(235, 235, 228);!important
}
.nav-tabs{

border-bottom:0px;
}
	@media screen and (-ms-high-contrast: active), screen and (-ms-high-contrast: none) {  
  input{
	width:90%;
	
	line-height : normal !important;
	    -webkit-appearance: textfield;
    background-color: white;
    -webkit-rtl-ordering: logical;
    cursor: text;
    padding: 1px;
    text-rendering: auto;
    color: initial;
    letter-spacing: normal;
    word-spacing: normal;
    text-transform: none;
    text-indent: 0px;
    text-shadow: none;
    display: inline-block;
    text-align: start;
    margin: 0em;
    font: 400 13.3333px Arial
} 

</style>
<!--[if IE]>
<style>

input{
	width:90%;
	
	line-height : normal !important;
	  -webkit-appearance: textfield;
    background-color: white;
    -webkit-rtl-ordering: logical;
    cursor: text;
    padding: 1px;
    	  text-rendering: auto;
    color: initial;
    letter-spacing: normal;
    word-spacing: normal;
    text-transform: none;
    text-indent: 0px;
    text-shadow: none;
    display: inline-block;
    text-align: start;
    margin: 0em;
    font: 400 13.3333px Arial
}

</style>
<![endif]-->
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/productLimitsAndFees.js"></script>

<form:form name="txnFeeForm" id="txnFeeForm" action="saveTransactionFees"
	modelAttribute="productForm" class='form-horizontal'>

	<form:input path="productId" name="productId" type="hidden" />

	<div id="feedBackTd" class="form-group text-center" style="padding-top:7px">
		<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align: center; font-weight: bold;">
				<c:out value="${statusMessage }" />
			</p>
		</c:if>
		<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align: center; font-weight: bold;">
				<c:out value="${statusMessage }" />
			</p>
		</c:if>
	</div>
	
	<div class="col-lg-12 text-center" >
		<c:if test="${showCopyFrom=='true' }">
			<!-- <div class="col-lg-1 col-lg-offset-3"> -->
				<label><spring:message code="product.copyFrom"
						text="Copy From" /> </label>
			<!-- </div>
			<div class="col-lg-2"> -->
				<select class="dropdown-medium" name="copyFromProductId"  
					id="copyFromProduct">
					<option value="-1">---Select---</option>
					<c:forEach items="${productMap}" var="parentProduct">
						<option value="${parentProduct.key }"
							<c:if test="${copyProductId==parentProduct.key}">selected="selected"</c:if>>${parentProduct.value }</option>
					</c:forEach>
				</select><!-- </div><div class="col-lg-1"> --><button type="button" id="copy" value="Copy" class="btn btn-primary"
					onclick="copyProductTxnFee()">
				<spring:message code="button.copy" text="Copy" />
				</button>
			<!-- </div> -->
			<div>
								<span id="parentProductIdError"></span>
							</div>
		
		</c:if>
	</div>
	
	<div class="col-lg-11 text-center" style="margin-left:15px">
                                   
                                        <label for="purseTypeID"><spring:message
                                                code="Product.purseTypeID" /></label>
                 

                                   <form:select path="purseId" id="purseId"
                                            class="dropdown-medium"  onchange="ProductPurseTxnFee()" >
                                            <option value="-1" label="--- Select ---" />
                                            <c:forEach items="${purseList}" var="pursetype">
                                            <!--  <c:if test="${pursetype.purseType.purseTypeName=='CONSUMER FUNDED CURRENCY' || pursetype.purseType.purseTypeName=='PARTNER FUNDED CURRENCY' }">  -->
                                           	<option value="${pursetype.purseId}" 
                                           	   <c:if test="${selectedpurseId==pursetype.purseId}">selected="selected"</c:if> >${pursetype.extPurseId}</option> <!--</c:if> -->
                                           	</c:forEach>
                                        </form:select>
										<div>
											<form:errors path="purseId"
											id="purseId" cssClass="fieldError" />
										</div>
                                   
                </div>               

	<ul class="nav nav-tabs col-lg-7">
		<c:forEach items="${deliverChannelList}" var="deliveryChannel"
			varStatus="status">
			<li <c:if test="${status.index == 0}">class="active"</c:if>><a
				href="#${deliveryChannel.deliveryChnlShortName}" data-toggle="tab"
				class="hoverColor">${deliveryChannel.deliveryChnlName}<span id="${deliveryChannel.deliveryChnlShortName}${deliveryChannel.deliveryChnlShortName}" class="boing">!</span></a></li>
		</c:forEach>
	</ul>
	<div class="tabresp tab-content" style="width: fit-content">
		<c:forEach items="${deliverChannelList}" var="deliveryChannel"
			varStatus="status">
			<div
				<c:if test="${status.index == 0}">class="tab-pane fade graybox in active"</c:if>
				<c:if test="${status.index != 0}">class="tab-pane fade graybox in"</c:if>
				id="${deliveryChannel.deliveryChnlShortName}"
				style="width: fit-content; border-top: 1px solid #a1a1a1; float: left">

				<!-- <div class="col-lg-12"> -->


					<table id="tableViewUsers"
						class="table table-hover table-striped table-bordered"
						style="table-layout: fixed;">

						<thead class="table-head">

							<tr>
								<th style="width: 175px"><spring:message
										code="product.limit.transactions" text="Transactions" /></th>
								<th style="width: 175px"><spring:message
										code="product.fees.desc" text="Fee Description" /></th>
								<th style="width: 65px"><spring:message
										code="product.fees.clawBack" text="Clawback" /></th>
								<th style="width: 65px;"><spring:message
										code="product.fees.clawbackCnt" text="Clawback Count" /></th>
								<th style="width: 110px"><spring:message
										code="product.fees.feeAmt" text="Fee Amt" /></th>
								<th style="width: 70px"><spring:message
										code="product.fees.feeCondition" text="Fee Condition" /></th>
								<th style="width: 90px"><spring:message
										code="product.fees.feePercentage" text="Fee %" /></th>
								<th style="width: 110px"><spring:message
										code="product.fees.minFeeAmt" text="Min Fee Amt" /></th>
								<th style="width: 65px;"><spring:message
										code="product.fees.freeCnt" text="Free Count" /></th>
								<th style="width: 110px"><spring:message
										code="product.fees.freeCntFreq" text="Free Count Frequency" /></th>
								<th style="width: 65px;"><spring:message
										code="product.fees.maxCnt" text="Max Count" /></th>
								<th style="width: 110px"><spring:message
										code="product.fees.maxCntFreq" text="Max Count Frequency" /></th>
								<th style="width: 110px"><spring:message
										code="product.fees.monthlyFeeCapAvl"
										text="Monthly Fee Cap Available" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${deliveryChannel.transactionMap}" var="txn">
								<tr id="${txn.key}">
									<td>${txn.value }<c:set var="txnShortName"
											value="${txn.key}" /> <input type="hidden" name="${txn.key}"
										value="${txnFinancialFlagMap[txnShortName]}" /></td>

									<td><form:input type="text"
											path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_feeDesc]"
											name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_feeDesc"
											id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_feeDesc"
											style="width: -webkit-fill-available;" maxLength="100" onblur="validateFeeDescription(this)"
											 /><form:errors path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_feeDesc]" cssClass="fieldError"></form:errors></td>

									<td><form:checkbox
											path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_clawback]"
											value="true"
											id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_clawback"
											name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_clawback"
											onchange="enableClawbackCount(this)" /><form:errors path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_clawback]" cssClass="fieldError"></form:errors></td>


									<td><form:input type="text"
											path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_clawbackCount]"
											name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_clawbackCount"
											id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_clawbackCount"
											style="width: -webkit-fill-available;"
											onkeypress="return isNumericfn(event);"
											onblur="validateClawbackCount(this)" maxlength="6"
											readonly="true" /><form:errors path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_clawbackCount]" cssClass="fieldError"></form:errors></td>



									<td><form:input
											path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_feeAmt]"
											type="text" onkeypress="return allowNumbersWithDot(event);"
											onblur="validateFeeAmt(this);"
											name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_feeAmt" id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_feeAmt"
											style="width: -webkit-fill-available;" maxlength="10" /><form:errors path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_feeAmt]" cssClass="fieldError"></form:errors></td>



									<td><form:select
											path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_feeCondition]"
											name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_feeCondition"
											id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_feeCondition"
											style="width:60px;width: -webkit-fill-available;"
											onchange="callFeeConditionChange(this)">
											<form:option value="N">NA</form:option>
											<form:option value="O">OR</form:option>
											<form:option value="A">AND</form:option>
										</form:select><form:errors path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_feeCondition]" cssClass="fieldError"></form:errors></td>


									<td><form:input type="text"
											path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_feePercent]"
											onkeypress="return allowNumbersWithDot(event)"
											name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_feePercent"
											id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_feePercent"
											style="width: -webkit-fill-available;" maxlength="6"
											onblur="enableMinFee(this); "
											readonly="true" /><form:errors path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_feePercent]" cssClass="fieldError"></form:errors></td>




									<td><form:input type="text"
											path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_minFeeAmt]"
											onkeypress="return allowNumbersWithDot(event);"
											name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_minFeeAmt"
											id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_minFeeAmt"
											style="width: -webkit-fill-available;" maxlength="10"
											onblur="return validateMinFeeAmt(this)"
											readonly="true" /><form:errors path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_minFeeAmt]" cssClass="fieldError"></form:errors></td>



									<td><form:input type="text"
											path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_freeCount]"
											id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_freeCount"
											name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_freeCount"
											onkeypress="return isNumericfn(event);;" onblur="enableFrequency(this)"
											style="width: -webkit-fill-available;" maxlength="6" /><form:errors path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_freeCount]" cssClass="fieldError"></form:errors></td>


									<td><form:select type="text"
											path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_freeCountFreq]"
											name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_freeCountFreq"
											id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_freeCountFreq"
											style="width:100px;width: -webkit-fill-available;" disabled="true">
											<form:option value="D">Daily</form:option>
											<form:option value="W">Weekly</form:option>
											<form:option value="BW">Bi-weekly</form:option>
											<form:option value="BM">Fortnightly</form:option>
											<form:option value="M">Monthly</form:option>
											<form:option value="Y">Yearly</form:option>
										</form:select><form:errors path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_freeCountFreq]" cssClass="fieldError"></form:errors></td>


									<td><form:input type="text"
											path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_maxCount]"
											id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_maxCount"
											name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_maxCount"
											onkeypress="return isNumericfn(event);;"
											style="width: -webkit-fill-available;" maxlength="6"
											onblur="enableFrequency(this)" /><form:errors path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_maxCount]" cssClass="fieldError"></form:errors></td>




									<td><form:select
											path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_maxCountFreq]"
											name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_maxCountFreq"
											id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_maxCountFreq"
											style="width:100px;width: -webkit-fill-available;"  disabled="true">
											<form:option value="D">Daily</form:option>
											<form:option value="W">Weekly</form:option>
											<form:option value="BW">Bi-weekly</form:option>
											<form:option value="BM">Fortnightly</form:option>
											<form:option value="M">Monthly</form:option>
											<form:option value="Y">Yearly</form:option>
										</form:select><form:errors path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_maxCountFreq]" cssClass="fieldError"></form:errors></td>



									<td><form:checkbox
											path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_monthlyFeeCapAvail]"
											value="true"
											id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_monthlyFeeCapAvail"
											name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_monthlyFeeCapAvail" />
											<form:errors path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_monthlyFeeCapAvail]" cssClass="fieldError"></form:errors>

									</td>




								</tr>
							</c:forEach>
						</tbody>
					</table>
			<!-- 	</div> -->


			</div>
		</c:forEach>
	</div>

	<div class="col-lg-12 text-center">
		<br>
		<br>
		<button type="button" class="btn btn-primary" onclick="saveTxnFee()"><i class="glyphicon glyphicon-saved"></i>
			<spring:message code="button.update" text="Update" />
		</button>

	</div>
</form:form>

<script>
	$("#txnFeesTab").addClass("active");
	$("#txnFeesTab").siblings().removeClass('active');

	
	
	$("input[id$='_clawback']").each(function (i, el) {
		enableClawbackCount(el);
		
	});
	
 	$("select[id$='_feeCondition']").each(function (i, el) {
		var ctrlId=$(el).attr('id');
		var val = $("#"+ctrlId+" option:selected").val();
		
		var minFeeAmtObj=$("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minFeeAmt");
		var feePercentObj=$("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_feePercent");
		if(val=='A'){
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))+ "_feePercent").prop("readonly", false);
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt").prop("readonly", false);
			
		}else if(val=='O'){
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))+ "_feePercent").prop("readonly", false);
		}
			
	}); 
 	
 	$("input[id$='_maxCount']").each(function (i, el) {
 		enableFrequency(el);
	});
 	$("input[id$='_freeCount']").each(function (i, el) {
 		enableFrequency(el);
	});
</script>

<script type="text/javascript">
function testMessage(){
	$("#feedBackTd").html('');
	if($("#copyFromProduct  option:selected").val()!=-1){
		//generateAlert("txnFeeForm", "copyFromProduct","copyFromProduct.empty");
		document.getElementById("parentProductIdError").innerHTML='<font color="red">'+readMessage("clickcopybutton")+"</font>";	
		return false;
	}
	document.getElementById("parentProductIdError").innerHTML='';
	//clearError("copyFromProduct");
	return true;
}
</script>
<script type="text/javascript">
  $('#copyFromProduct').change(testMessage);
</script>