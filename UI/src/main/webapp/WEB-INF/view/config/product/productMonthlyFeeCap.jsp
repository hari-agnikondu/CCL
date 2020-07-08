<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<style>
.btn{
margin-left: 15px;}
input {
	width: -moz-available;
}

.tab-pane {
	width: -moz-fit-content;
	width: fit-content;
	border-top: 1px solid #a1a1a1;
	float: left;
}

input {
	width: 90%;
	padding: 0px 0px;
}

input[type='checkbox'] {
	border: 0px;
}

@media screen and (-ms-high-contrast: active) , screen and
	(-ms-high-contrast: none) {
	input {
		width: 90%;
		line-height: normal !important;
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

<form:form name="monthlyFeeCapForm" id="monthlyFeeCapForm"
	action="saveMonthlyFeeCap" modelAttribute="productForm"
	class='form-horizontal'>

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

	<div class="col-lg-12 text-center">
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
				</select><!-- </div><div class="col-lg-2"> --><button type="button" id="copy" value="Copy" class="btn btn-primary"
					onclick="copyProductMonthlyFeeCap()">
					<spring:message code="button.copy" text="Copy" />
				</button>
				<div>
								<span id="parentProductIdError"></span>
							</div>
			<!-- </div> -->
			
		</c:if>
	</div>
    <div class="col-lg-11 text-center" style="margin-left:15px">
                                   
                                        <label for="purseTypeID"><spring:message
                                                code="Product.purseTypeID" /></label>
                 

                                   <form:select path="purseId" id="purseId"
                                            class="dropdown-medium"  onchange="ProductPurseMonthlyFeeCap()" >
                                            <option value="-1" label="--- Select ---" />
                                            <c:forEach items="${purseList}" var="pursetype">
                                             <!--<c:if test="${pursetype.purseType.purseTypeName=='CONSUMER FUNDED CURRENCY' || pursetype.purseType.purseTypeName=='PARTNER FUNDED CURRENCY'}"> -->
                                           	<option value="${pursetype.purseId}" 
                                           	   <c:if test="${selectedpurseId==pursetype.purseId}">selected="selected"</c:if> >${pursetype.extPurseId}</option><!--</c:if> -->
                                           	</c:forEach>
                                        </form:select>
										<div>
											<form:errors path="purseId"
											id="purseId" cssClass="fieldError" />
										</div>
                                   
                </div>               
	
    


	<div
		<c:if test="${status.index == 0}">class="tab-pane fade in active graybox"</c:if>
		<c:if test="${status.index != 0}">class="tab-pane fade in graybox"</c:if>
		id="${deliveryChannel.deliveryChnlShortName}"
		style="width: fit-content; border-top: 1px solid #a1a1a1; float: left;padding-bottom: 15px;
    padding-top: 15px;
    padding-left: 15px;
    padding-right: 15px;margin:5px">

		<div class="col-lg-12">


			<table id="tableViewUsers"
				class="table table-hover table-striped table-bordered"
				style="width:1300px; table-layout: fixed;">

				<thead class="table-head">

					<tr>
						<th style="width: 175px"><spring:message code="" text="" /></th>
						<th style="width: 175px"><spring:message
								code="product.fees.desc" text="Fee Description" /></th>
						<th style="width: 175px"><spring:message
								code="product.monthlyFeeCap.timePeriod" text="Time Period" /></th>

						<th style="width: 65px;"><spring:message
								code="product.monthlyFeeCap.assessmentDate"
								text="Assessment Date" /></th>

						<th style="width: 110px"><spring:message
								code="product.monthlyFeeCap.capFeeAmt" text="Cap Fee Amt" /></th>


					</tr>
				</thead>
				<tbody>
					<tr>
						<td><spring:message code="product.monthlyFeeCap.label"
								text="Monthly Fee Capping" /></td>
						<td><form:input type="text"
								path="productAttributes[monthlyFeeCap_feeDesc]"
								name="monthlyFeeCap_feeDesc" id="monthlyFeeCap_feeDesc"
								style="width: -webkit-fill-available;" maxLength="100"
								onblur="return validateFeeDescription(this)" />
								<form:errors path="productAttributes[monthlyFeeCap_feeDesc]" cssClass="fieldError"></form:errors></td>
						<td><form:select
								path="productAttributes[monthlyFeeCap_timePeriod]"
								name="monthlyFeeCap_timePeriod" id="monthlyFeeCap_timePeriod"
								style="width:160px;width: -webkit-fill-available;"
								onchange="callTimePeiodCheck()">
								<form:option value="CM">Calendar Months</form:option>
								<form:option value="SM">Configure Date Of Month</form:option>
							</form:select>
							<form:errors path="productAttributes[monthlyFeeCap_timePeriod]" cssClass="fieldError"></form:errors>
							</td>

						<td><form:select style="width:85px; width: -webkit-fill-available;"
								path="productAttributes[monthlyFeeCap_assessmentDate]"
								name="monthlyFeeCap_assessmentDate"
								id="monthlyFeeCap_assessmentDate">
								
								<form:option value="1">1</form:option>
								<form:option value="2">2</form:option>
								<form:option value="3">3</form:option>
								<form:option value="4">4</form:option>
								<form:option value="5">5</form:option>
								<form:option value="6">6</form:option>
								<form:option value="7">7</form:option>
								<form:option value="8">8</form:option>
								<form:option value="9">9</form:option>
								<form:option value="10">10</form:option>
								<form:option value="11">11</form:option>
								<form:option value="12">12</form:option>
								<form:option value="13">13</form:option>
								<form:option value="14">14</form:option>
								<form:option value="15">15</form:option>
								<form:option value="16">16</form:option>
								<form:option value="17">17</form:option>
								<form:option value="18">18</form:option>
								<form:option value="19">19</form:option>
								<form:option value="20">20</form:option>
								<form:option value="21">21</form:option>
								<form:option value="22">22</form:option>
								<form:option value="23">23</form:option>
								<form:option value="24">24</form:option>
								<form:option value="25">25</form:option>
								<form:option value="26">26</form:option>
								<form:option value="27">27</form:option>
								<form:option value="28">28</form:option>
								<form:option value="29">29</form:option>
								<form:option value="30">30</form:option>
								<form:option value="31">31</form:option>
							</form:select>
							<form:errors path="productAttributes[monthlyFeeCap_assessmentDate]" cssClass="fieldError"></form:errors>
							</td>

						<td><form:input
								path="productAttributes[monthlyFeeCap_feeCapAmt]" type="text"
								onkeypress="allowNumbersWithDot(event);"
								onblur="DecimalValueFormat(this); validateFeeCapAmt();"
								name="monthlyFeeCap_feeCapAmt" id="monthlyFeeCap_feeCapAmt"
								style="width: -webkit-fill-available;" maxlength="10" />
								<form:errors path="productAttributes[monthlyFeeCap_feeCapAmt]" cssClass="fieldError"></form:errors>
								</td>

					</tr>
				</tbody>
			</table>
		</div>


	</div>

	<div class="col-lg-12 text-center">
		<br> <br>
		<button type="button" class="btn btn-primary" onclick="clickMonthlyFeeCapSave()"><i class="glyphicon glyphicon-saved"></i><spring:message code="button.update" text="Update" />
		</button>

	</div>
</form:form>

<script>
	$("#monthlyFeeCapTab").addClass("active");
	$("#monthlyFeeCapTab").siblings().removeClass('active');
	if ($("#monthlyFeeCap_timePeriod option:selected").val() == 'CM') {
		$("#monthlyFeeCap_assessmentDate").prop('disabled', true);
	}

	//	callTimePeiodCheck();

	function callTimePeiodCheck() {
		if ($("#monthlyFeeCap_timePeriod option:selected").val() == 'SM') {
			$("#monthlyFeeCap_assessmentDate").prop('disabled', false);
		} else {
			$("#monthlyFeeCap_assessmentDate").val('1');
			$("#monthlyFeeCap_assessmentDate").prop('disabled', true);
		}
	}
	function clickMonthlyFeeCapSave() {

		$("#feedBackTd").html('');
		var feeDescValid = validateFeeDescription($("#monthlyFeeCap_feeDesc"));

		var feeAmtValid = validateFeeCapAmt();
		if (feeAmtValid && feeDescValid) {
			$("#monthlyFeeCap_assessmentDate").prop('disabled', false);
			$("#monthlyFeeCapForm").submit();
		}
	}
	function validateFeeCapAmt() {
		var amount = $("#monthlyFeeCap_feeCapAmt").val();
		var isValid = true;
		if (amount == null || amount.trim() == '') {
			generateAlert("monthlyFeeCapForm", "monthlyFeeCap_feeCapAmt",
					"monthlyFeeCap_feeCapAmt.empty");
			isValid = false;
		} else if (isNaN(amount) || parseFloat(amount) < 0) {
			generateAlert("monthlyFeeCapForm", "monthlyFeeCap_feeCapAmt",
					"monthlyFeeCap_feeCapAmt.invalid");
			isValid = false;
		} else if (!DecimalValueFormat($("#monthlyFeeCap_feeCapAmt").get(0))) {
			isValid = false;
		} else {
			clearError("monthlyFeeCap_feeCapAmt");
		}
		return isValid;
	}
	
	function copyProductMonthlyFeeCap(){
		$("#feedBackTd").html('');
		if($("#copyFromProduct  option:selected").val()==-1){
		//	generateAlert("monthlyFeeCapForm", "copyFromProduct","copyFromProduct.empty");
		document.getElementById("parentProductIdError").innerHTML='<font color="red">'+readMessage("copyFromProduct.empty")+"</font>";	
		return false;
		}
		clearError("copyFromProduct");
		$("#monthlyFeeCapForm").attr('action','productMonthlyFeeCap');
		$("#monthlyFeeCapForm").submit();
	}
	
	function ProductPurseMonthlyFeeCap(){
		$("#feedBackTd").html('');
		if($("#copyFromProduct  option:selected").val()==-1){
			$("#copyFromProduct").val(null);		
		}
		
		$("#monthlyFeeCapForm").attr('action','productMonthlyFeeCap');
		$("#monthlyFeeCapForm").submit();
	}
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
	

