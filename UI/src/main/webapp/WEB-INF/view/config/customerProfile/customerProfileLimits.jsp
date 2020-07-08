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
input{
	width:90%;
	padding: 0px 0px;
}

.boing {
    margin-left:5px;
    color:red;
    visibility: hidden;
}
input[type='checkbox']{
	
	border:0px;

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
<script src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/customerProfileLimitsAndFees.js"></script>

<form:form name="limitForm" id="limitForm" action="saveLimits"
	modelAttribute="customerProfileForm" class='form-horizontal' method="POST">
	
	<form:input path="profileId" name="profileId" type="hidden"/>
	<div id="feedBackTd" class="form-group text-center" style="padding-top:7px">
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
		</div>

	<!-- -for copy from option -->

	<div class="col-lg-10 text-center col-lg-offset-2">
		<div class="col-lg-2">
			<label for="copyFrom"><spring:message code="copyFrom" /></label>
		</div>

		<div class="col-lg-3">
			<select class="space" id="spnumbertype" name="spnumbertype">
				<!-- onblur="validateDropDown(this.form.id,this.id)" -->
				<option value="-1">--select--</option>
				<option value="cardNumber">Card Number</option>
				<option value="accountNumber">Account Number</option>
				<option value="proxyNumber">Proxy Number</option>
			</select>
		</div>
		<div class="col-lg-3" style="line-height: 29px">
			<input type="text" name="parentCardData" id="parentCardData" />
		</div>
		<div class="col-lg-2">
			<button type="button" id="copyCardForm" class="btn btn-primary"
				onclick="getCustomerProfileDetails(this.form.id,'getCustomerProfileDetails')">
				<spring:message code="button.copy" />
			</button>
			<div>
				<span id="parentCardError"></span>
			</div>
			<input type="hidden" id="viewPage" name="viewPage"
				value="customerProfileLimit">
		</div>
	</div>
	<!-- end -->

	<ul class="nav nav-tabs col-lg-7">
		<c:forEach items="${deliverChannelList}" var="deliveryChannel"
			varStatus="status">
			<li <c:if test="${status.index == 0}">class="active"</c:if> ><a href="#${deliveryChannel.deliveryChnlShortName}"
				data-toggle="tab" class="hoverColor">${deliveryChannel.deliveryChnlName}<span id="${deliveryChannel.deliveryChnlShortName}${deliveryChannel.deliveryChnlShortName}" class="boing">!</span></a></li>
		</c:forEach>
	</ul>
	<div class="tabresp tab-content" >
		<c:forEach items="${deliverChannelList}" var="deliveryChannel"
			varStatus="status">
			<div
				<c:if test="${status.index == 0}">class="tab-pane fade in active graybox"</c:if>
				<c:if test="${status.index != 0}">class="tab-pane fade in graybox"</c:if>
				id="${deliveryChannel.deliveryChnlShortName}"  >

				<div class="col-lg-12">


					<table id="tableViewUsers"
						class="table table-hover table-striped table-bordered"  style="table-layout:fixed;width:1300px">
						<colgroup span="5">
							<%-- <col style="width:65px;"> --%>
							<col style="width:175px;">
							<col style="width:110px;">
							
							<col style="width:110px;">
						
						</colgroup>
						<colgroup span="4">	
							<col style="width:65px;">
							
							<col style="width:110px;">
							
						</colgroup>
						<colgroup span="4">	
							<col style="width:65px;">
							
							<col style="width:110px;">
							
						</colgroup>
						<colgroup span="4">	
							<col style="width:65px;">
						
							<col style="width:110px;">
						
						</colgroup>
						<colgroup span="4">	
							<col style="width:65px;">
					
							<col style="width:110px;">
						
						</colgroup>
						<thead class="table-head">
						<tr>
								<th colspan="3" scope="colgroup" style=""></th>
								<th colspan="2" scope="colgroup" style="text-align: center;"><spring:message
										code="product.limit.daily" text="Daily" /></th>
								<th colspan="2" scope="colgroup" style="text-align: center;"><spring:message
										code="product.limit.weekly" text="Weekly" /></th>
								<th colspan="2" scope="colgroup" style="text-align: center;"><spring:message
										code="product.limit.monthly" text="Monthly" /></th>
								<th colspan="2" scope="colgroup" style="text-align: center;"><spring:message
										code="product.limit.yearly" text="Yearly" /></th>
							</tr>
							<tr>
								<!-- <th ></th> -->
								<th><spring:message code="product.limit.transactions"
										text="Transactions" /></th>
								<th><spring:message code="product.limit.minAmtPerTxn"
										text="Min Amt per TX" /></th>
							
								<th><spring:message code="product.limit.maxAmtPerTxn"
										text="Max Amt per Txn" /></th>
								
								<th><spring:message code="product.limit.maxCount"
										text="Max Count" /></th>
							
								<th><spring:message code="product.limit.maxAmt"
										text="Max Amt" /></th>
								
								<th><spring:message code="product.limit.maxCount"
										text="Max Count" /></th>
							
								<th><spring:message code="product.limit.maxAmt"
										text="Max Amt" /></th>
							
								<th><spring:message code="product.limit.maxCount"
										text="Max Count" /></th>
							
								<th><spring:message code="product.limit.maxAmt"
										text="Max Amt" /></th>
								
								<th><spring:message code="product.limit.maxCount"
										text="Max Count" /></th>
								
								<th><spring:message code="product.limit.maxAmt"
										text="Max Amt" /></th>
							
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${deliveryChannel.transactionMap}" var="txn">
								<tr id="${deliveryChannel.deliveryChnlShortName}_${txn.key}"><%--  <td><input type="checkbox" value="${deliveryChannel.deliveryChnlShortName}_${txn.key}" id="${deliveryChannel.deliveryChnlShortName}_${txn.key}" name="checkedTxns"/> </td> --%>
									<td>${txn.value }<c:set var="txnShortName" value="${txn.key}" />
										<input type="hidden" name="${deliveryChannel.deliveryChnlShortName}_${txn.key}" id="${deliveryChannel.deliveryChnlShortName}_${txn.key}_Flag" value="${txnFinancialFlagMap[txnShortName]}" /></td>
										<td>
										<form:input type="text"
													path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_minAmtPerTx]"
													onkeypress="return allowNumbersWithDot(event);" onblur="DecimalValueFormat(this); "
													name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_minAmtPerTx"
													id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_minAmtPerTx" style="width: -webkit-fill-available;" maxlength="10" />
											<form:errors path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_minAmtPerTx]" cssClass="fieldError"></form:errors>
											</td>
									<td>
												<form:input type="text"
													path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_maxAmtPerTx]"
													onkeypress="return allowNumbersWithDot(event);"  onblur="DecimalValueFormat(this); "
													name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_maxAmtPerTx" id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_maxAmtPerTx" maxlength="10"
													style="width: -webkit-fill-available;" />
													<form:errors path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_maxAmtPerTx]" cssClass="fieldError"></form:errors>
											</td>

									

									<td><form:input
													path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_dailyMaxCount]"
													type="text" onkeypress="return isNumericfn(event);" onblur="return isNumericfn(event);"
													name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_dailyMaxCount" id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_dailyMaxCount"
													style="width: -webkit-fill-available;"  maxlength="4" />
											<form:errors path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_dailyMaxCount]" cssClass="fieldError"></form:errors>
											</td>
									<td>
												<form:input type="text"
													path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_dailyMaxAmt]"
													onkeypress="return allowNumbersWithDot(event);" onblur="DecimalValueFormat(this);"
													name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_dailyMaxAmt" id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_dailyMaxAmt"
													style="width: -webkit-fill-available;" maxlength="10" />
										<form:errors path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_dailyMaxAmt]" cssClass="fieldError"></form:errors>
											</td>
									<td>
												<form:input type="text"
													path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_weeklyMaxCount]"
													onkeypress="return isNumericfn(event);" onblur="return isNumericfn(event);"
													name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_weeklyMaxCount" id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_weeklyMaxCount"
													style="width: -webkit-fill-available;" maxlength="4" />
											<form:errors path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_weeklyMaxCount]" cssClass="fieldError"></form:errors>
											</td>
									<td>
												<form:input type="text"
													path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_weeklyMaxAmt]"
													onkeypress="return allowNumbersWithDot(event);" onblur="DecimalValueFormat(this);"
													name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_weeklyMaxAmt" id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_weeklyMaxAmt"
													style="width: -webkit-fill-available;"  maxlength="10"/>
											<form:errors path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_weeklyMaxAmt]" cssClass="fieldError"></form:errors>
											</td>
									
									<td>
												<form:input type="text"
													path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_monthlyMaxCount]"
													name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_monthlyMaxCount" id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_monthlyMaxCount"
													 onkeypress="return isNumericfn(event);" onblur="return isNumericfn(event);"
													style="width: -webkit-fill-available;"  maxlength="4" />
														<form:errors path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_monthlyMaxCount]" cssClass="fieldError"></form:errors>
											</td>
									

									<td>
												<form:input type="text"
													path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_monthlyMaxAmt]" id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_monthlyMaxAmt" 
													name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_monthlyMaxAmt" onkeypress="return allowNumbersWithDot(event);" onblur="DecimalValueFormat(this);"
													style="width: -webkit-fill-available;"  maxlength="10"/>
														<form:errors path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_monthlyMaxAmt]" cssClass="fieldError"></form:errors>
											</td>
									

									<td>
												<form:input type="text"
													path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_yearlyMaxCount]" id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_yearlyMaxCount"  
													name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_yearlyMaxCount"  onkeypress="return isNumericfn(event);" onblur="isNumericfn(event);"
													style="width: -webkit-fill-available;" maxlength="4"  />
														<form:errors path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_yearlyMaxCount]" cssClass="fieldError"></form:errors>
										</td>

									

									<td>
												<form:input type="text"
													path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_yearlyMaxAmt]" id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_yearlyMaxAmt" 
													name="${deliveryChannel.deliveryChnlShortName}_${txn.key }_yearlyMaxAmt"  onkeypress="return allowNumbersWithDot(event);" onblur="DecimalValueFormat(this);"
													style="width: -webkit-fill-available;"  maxlength="10" />
														<form:errors path="cardAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_yearlyMaxAmt]" cssClass="fieldError"></form:errors>
											</td>

									


								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>


			</div>
		</c:forEach>
	</div>

	<div class="col-lg-12 text-center">
	<br><br>
		<button type="button" class="btn btn-primary" onclick="clickSave(this.form.id,event)"><i class="glyphicon glyphicon-saved"></i><spring:message code="button.update"
										text="Update" /></button>

	</div>
</form:form>

<script>
$("#limitsTab").addClass("active");
$("#limitsTab").siblings().removeClass('active');

//function DisableNonFinancialTxnsAmtField(){
	$("input[id$='_Flag']").each(function (i, el) {
		if($(el).val()!=null && $(el).val().trim()!='Y'){
			var ctrlId=el.id;
			var rowId=$("#"+ctrlId).closest('tr').get(0).id;
			$("#"+rowId).find("td input[id$='Amt'],td input[id$='AmtPerTx']").each(function(j,inpObj) {
				var inputId= inpObj.id;
				$("#"+inputId).prop('disabled',true);
			});
		}
	});
/* 	$("input[id$='_maxAmtPerTx']").each(function (i, el) {
		var ctrlId = el.id;
		var minAmtPerTxn = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").val();
		var maxAmtPerTxn = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").val();
		
		var txnShortName=ctrlId.substring(ctrlId.split('_', 1).join('_').length+1,ctrlId.split('_', 2).join('_').length);
		
		var checkTxn=ctrlId.substring(0,ctrlId.split('_', 2).join('_').length);	
		if(minAmtPerTxn!=null && minAmtPerTxn.trim()!='' &&  parseInt(minAmtPerTxn)!=0 && maxAmtPerTxn!=null && maxAmtPerTxn.trim()!='' &&  parseInt(maxAmtPerTxn)!=0){
			$("#"+checkTxn).prop('checked',true);
		}else{
			$("#"+checkTxn).prop('checked',false);
		
		}
	}); */

</script>
