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
      padding: 1px;
    text-rendering: auto;
    color: initial;
     cursor: text;
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
<form:form name="transflexForm" id="transflexForm" action=""
	modelAttribute="transflexForm" class='form-horizontal' >

	
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
	

	<ul class="nav nav-tabs col-lg-12">
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
				<c:if test="${status.index == 0}">class="tab-pane fade in active graybox"</c:if>
				<c:if test="${status.index != 0}">class="tab-pane fade in graybox"</c:if>
				id="${deliveryChannel.deliveryChnlShortName}"
				style="width: fit-content; border-top: 1px solid #a1a1a1; float: left;width: 100%;">

				<div class="col-lg-12">


					<table id="tableViewUsers"
						class="table table-hover table-striped table-bordered"
						style="table-layout: fixed;">

						<thead class="table-head">

							<tr>
								<th style="width: 50px"><spring:message
										code="TransFlex.transactions" text="Transactions" /></th>
								<th style="width: 70px"><spring:message
										code="TransFlex.TranDesc" text="Transaction Flex Description" /></th>
								
								
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${deliveryChannel.transactionMap}" var="txn">
								<tr id="${txn.key}">
									<td>${txn.value }<c:set var="txnShortName"
											value="${deliveryChannel.deliveryChnlShortName}_${txn.key}" /> <input type="hidden" name="${deliveryChannel.deliveryChnlShortName}_${txn.key}"
										value="${txndesc[txnShortName]}" /></td>
                                  <td><input type="text"  class="trim"
											id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_Desc" onpaste="return false" value="${txndesc[txnShortName]}"  
											name="TxnDesc[${dlctxn[txnShortName]}]" maxlength="100" onblur="validateDescription(this)"
											 /></td>
                           
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:forEach>
	</div>

	<div class="col-lg-12 text-center">
		<button type="button" class="btn btn-primary" onclick="return updateflexdesc()" ><i class="glyphicon glyphicon-saved"></i>
			<spring:message code="button.update" text="Update" />
		</button>

	</div>
</form:form>

<script>
function validateDescription(ctrl){ 
	
	var isValid= true;
	var ctrlId=$(ctrl).attr('id');
	var pattern = /^[a-zA-Z0-9]+[a-zA-Z0-9 ]*$/;
	
	var ctrlVal = $(ctrl).val();
	if(ctrlVal==null || ctrlVal==''){ 
		generateAlert($(ctrl).closest('form').attr('id'),  ctrlId,"tranflex.Desc.empty");
		isValid= false;
	}
	else if(ctrlVal.length<1 || ctrlVal.length>100){
		generateAlert($(ctrl).closest('form').attr('id'),  ctrlId,"tranflex.Desc.length");
		isValid= false;
	}
	else if((ctrlVal!=null && ctrlVal!='') && !pattern.test(ctrlVal)){ 
		generateAlert($(ctrl).closest('form').attr('id'),  ctrlId,"tranflex.pattern");
		isValid= false;
	 }
	else{
		//$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
		clearError(ctrlId);
	}
	return isValid;
}



	function updateflexdesc(){
		$("#feedBackTd").html('');
	
		var isValidDesc = true;
		$("input[id$='_Desc']").each(function (i, el) { 
			if(!validateDescription(el))
				isValidDesc=false;
		});	
		
		if(isValidDesc){
			
			$("#transflexForm").attr('action','savetransactionflex');
			$("#transflexForm").submit();
		}
	}
	

</script>