<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/order.js"></script>

<body>
	<div class="body-container" style="min-height: 131px;">     
	<div class="container">
	<div id="feedBackTd" class="text-center" style="padding-top:4px"> 
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
	</div>
	<div>
	</div>
	<div class="group">
	<form:form action="#" method="POST" modelAttribute="orderForm"
						id="orderForm" name="orderForm">
											<table  style="width: 30%; border-width: 1px; border-spacing: 1px; "
						id="tableViewGenerateOrder"
						class="table table-hover table-striped table-bordered dataTable">
						<tbody>

							<tr> <input type="hidden" id="GenerationType" name="Generationtype" value="${GenerationType}" />
								<td align="center"><button type="button" onclick="Action(this.form.id,'ORDER')"
										class="btn btn-primary-table-button"
										name = "Orderprocess" id = "Orderprocess"
										style="position: relative;">
										<i class='glyphicon glyphicon-ok-sign'></i>
										<spring:message code="order.Orderprocess" text="Order Process" />
									</button>&nbsp;&nbsp;
									<button type="button" onclick="Action(this.form.id,'CCF')" class="btn btn-primary-table-button"
									name = "CCFGeneration" id = "CCFGeneration">
										<i class='glyphicon glyphicon-ok-sign'></i>
										<spring:message code="order.CCF" text="Generate CCF" />
									</button></td>

							</tr>
						</tbody>
						
					</table>
		<div id="TableData">				
		<div id="tableDiv">
			<div id="tableErr" class="fieldError" style="text-align:center;"></div>
					<table id="tableViewOrder"
				class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
				style="width: 100% !important;">
					
					<thead>
						<tr>
							<th style="width:5em;"><spring:message code="button.action"
									text="Action" /></th>
							<th style="width:11em;"><spring:message code="order.partner"
									text="Partner Name" /></th>
							<th style="width:11em;"><spring:message
									code="order.issuer" text="Issuer Name" /></th>
							<th style="width:11em;"><spring:message code="product.name"
									text="Product Name" /></th>
							<th style="width:11em;"><spring:message code="merchant"
									text="Merchant Name" /></th>
							<th style="width:11em;"><spring:message
									code="store" text="Store" /></th>
							<th style="width:11em;"><spring:message code="package.ID"
									text="Package ID" /></th>
							<th style="width:7em;"><spring:message code="order.default.status"
									text="Default Card Status" /></th>
							<th style="width:7em;"><spring:message
									code="quantitY" text="Quantity" /></th>
							<th style="width:11em;"><spring:message code="order.user"
									text="User Name" /></th>
							<th style="width:11em;"><spring:message code="order.id"
									text="Order Number" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${orderList}" var="order" varStatus="status">
							<tr id="${order.orderId}~${order.lineItemId}">
								<td><form:checkbox name="orderPartnerId" id="orderPartnerId${status}" value="${order.orderId}~${order.lineItemId}" path="orderPartnerId" /><form:hidden  name="partnerId" id="partnerId" value="${order.partnerId}" path="partnerId" /><form:hidden  name="orderId" id="orderId" value="${order.orderId}" path="orderId" /></td>
								<td>${order.partnerName}</td>
								<td>${order.issuerName}</td>
								<td>${order.productId}:${order.productName}</td>
								<td>${order.merchantName}</td>
								<td>${order.locationName}</td>
								<td>${order.packageId}</td>
								<td>${order.defaultCardStatus}</td>
								<td>${order.quantity}</td>
								<td>${order.userName}</td>
								<td>${order.orderId}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
		</div>
		<div class="col-lg-12">
				<div class="col-lg-5 col-lg-offset-5" style="padding-bottom:10px">
				
					<security:authorize access="hasRole('APPROVE_ORDER')">
					<button type="button" class="btn btn-primary-table-button green-btn" data-toggle="modal" data-target="#" id="GenerateBtn"
										onclick="processing(this.form.id)">
										<i class="glyphicon glyphicon-ok-sign"></i>
										<spring:message code="button.generateBtn" text="Generate" />
					</button>
					</security:authorize>

				</div>
			</div>
			</div>
<%-- 			<div class="modal fade" id="define-constant-approve" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
			
					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12">
								<label class="col-lg-3" for="Remarks"><spring:message
									code="label.Remarks" /><font color='red'>*</font></label>
									<div class="text-right mandatory-red">
										<spring:message code="label.mandatory"/>
									</div>
								<form:hidden path="status" id="status" name='status'  value="" />
								
								<div class="col-lg-9"> <form:textarea path="checkerRemarks" style="overflow:auto;resize:none"
									id="statusChangeDesc" cols="25"  rows="11"  class="textarea-medium max"/>
									<div class="fieldError" id="dialogErrorApprove"></div>
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" onclick="goApproveRejectRole();" id="showApproveBtn" style="display:none;"
								class="btn btn-primary"><i class="glyphicon glyphicon-ok-sign"></i><spring:message code="button.approveBtn" text="Approve" />
							</button>
							<button type="button" onclick="goApproveRejectRole();" id="showRejectBtn" style="display:none;"
								class="btn btn-primary"><i class="glyphicon glyphicon-remove-sign"></i><spring:message code="button.rejectBtn" text="Reject" />
							</button>
							<button type="button" onclick="goToPrevious()" data-dismiss="modal" class="btn btn-primary gray-btn">
								<spring:message	code="button.close" />
							</button>
						</div>
					</div>
				
			</div>
		</div> --%>
			
			</form:form>
	</div>
 </div> 
 </div>
</body>

<script>
$('input[name="orderPartnerId"]').on('change', function() {
	if($("input[name=orderPartnerId]:checked").length > 0){
		$("#tableErr").html('');
	}
	
});


function Action(formId,key){
$("#GenerationType").val(key);
	if(key == "ORDER"){
		$("#"+formId).attr('action','orderProcessing');
	}else{
		$("#"+formId).attr('action','ccfGeneration');
	}
	
	$("#"+formId).submit();
}

function processing(formId){
	$("#feedBackTd").html('');
	$("#tableErr").html('');
	
	if($("input[name=orderPartnerId]:checked").length < 1){
		 $("#tableErr").html(readMessage("order.selectFor.approveReject"));
	}else{
		
		if($("#GenerationType").val() =="ORDER"){
			$("#"+formId).attr('action','generateOrder');
		}else if($("#GenerationType").val() =="CCF"){
			$("#"+formId).attr('action','generateCCF');
		}
		
		$("#"+formId).submit();
		
	}
	
}
$(document).ready(function(){
	 //code for submitting all page data
	   var table = $('#tableViewOrder').DataTable();
	    // Handle form submission event
	    $('#orderForm').on('submit', function(e){
	       var form = this;
      	 // Encode a set of form elements from all pages as an array of names and values
	       var params = table.$('input').serializeArray();
	       var params1 = $('input').serializeArray();
	     // Iterate over all form elements
	       $.each(params, function(){
	          
	        	    $(form).append(
	                $('<input>')
	                   .attr('type', 'hidden')
	                   .attr('name', this.name)
	                   .attr('class', 'test')
	                   .val(this.value)
	                );
	            
	       }); 
	    
	       $.each(params1, function(){
	    	    var value=this.value;
	        $('.test').each(function(){
	  		 
		      if($(this).val()==value){
             //current page data is removed bcoz it is aready present in form cache
		       $(this).remove();
		      }
       	 });
	     
	       }); 
	    });   //form submit
	   
	});//end of doc

</script>


