function selectBoxValidation(formId,eleId){
	
	if($("#"+formId+" #"+eleId+" option:selected").val()==null || $("#"+formId+" #"+eleId+" option:selected").val()==-1){
		generateAlert(formId, eleId, "select."+eleId);
		return false;
	}else{
		clearError(formId+" #"+eleId);
		return true;
	}
}

function validateStore(){
	if($("#orderForm #merchantId option:selected").val()!=null && $("#orderForm #merchantId option:selected").val()!=-1){
		return selectBoxValidation("orderForm",'locationId');
	}
	return true;
}
function placeOrder(formId){
	
	var isValidProduct=selectBoxValidation(formId,'productId')
	var isValidPackage=selectBoxValidation(formId,'packageId');
	var isValidStore=validateStore();
	var isValidOrderId= validateFields(formId,'orderId');
	var isValidQuantity=validateFields(formId,'quantity');
	if(isValidQuantity){
		var availInv = $("#availableInventory").val();
		if(availInv!=null && !$("#availableInventory").val().trim()==''){
			if(parseInt($("#quantity").val()) > parseInt(availInv)){
				generateAlert(formId,  'quantity', "quantity.availInventory");
				isValidQuantity=false;
			}else{
				 clearError('quantity');
			}
			
		}
	}
	if(isValidQuantity && parseInt($("#quantity").val())<=0){
		generateAlert(formId, 'quantity', "quantity.notZero");
		isValidQuantity=false;
	}
	if(isValidProduct && isValidPackage && isValidStore && isValidOrderId && isValidQuantity){
		$("#"+formId).attr('action','saveOrder');
		$("#"+formId).submit();
	}
}

function retrieveMerchantsAndPackage(formId,eleId){
	 $("#errorfield").html('')
	 $("#feedBackTd").html('');
	 $("#quantity").val('');
	 $("#orderId").val('');
	 $("#packageId").val(-1);
	 $("#merchantId").val(-1);
	 $("#locationId").val(-1);
	 $("#availableInventory").val('0');
	 $('#packageId').children('option').remove();
	 $('#merchantId').children('option:not(:first)').remove();
	 $('#locationId').children('option:not(:first)').remove();
	 clearError('quantity');
	 clearError('orderId');
	 clearError('packageId');
	 clearError('merchantId');
	 clearError('locationId');
	 
	if(selectBoxValidation(formId,eleId)){
	
	var productId = $("#"+eleId+" option:selected").val();
	
	$.ajax({
			url 		: "getMerchantsAndPackages?productId="+productId,
		  	type		: "GET",
		  	async		: false, 
		  	success: function (response) {
              if(response!=null && response['packageIds']!=null && response['packageIds'].length > 0 && response['error']==null) {
            	  $("#errorfield").html('')
                $('#packageId').html('');
                $.each(response['packageIds'] , function(index , value){
                	 $('#packageId')
                     .append($("<option></option>")
                     .attr("value",value)
                     .text(value));
              });
                $('#merchantId').children('option:not(:first)').remove();
                if(response['merchants']!=null){
		                $.each(response['merchants'] , function(key , value){
		               	 $('#merchantId')
		                    .append($("<option></option>")
		                    .attr("value",key)
		                    .text(value));
		             });
                }
                if(response['availableInventory']!=null && response['availableInventory']!='')  {
                	          	$("#availableInventory").val(response['availableInventory']);
                	/*$("#availableInventory").removeClass('disable-link');
                	$("#availableInventory").addClass('enable-link');*/
                	//$("#availableInventory").attr("href",'availableInventoryModal');
                }else{
                	$("#availableInventory").val('0');
                }
		  	}else{
		  		if(response==null || response['error']==null)
		  			$("#errorfield").html('Failed to fetch Package ID');
		  		else
		  			$("#errorfield").html(response['error']);
		  	}
            },
            error: function (textStatus, errorThrown) {
                console.log(textStatus);
            }
		   });
	}else{
	/*	$("#availableInventory").addClass('disable-link');
    	$("#availableInventory").removeClass('enable-link');*/
    	//$("#availableInventory").attr("href",'');
	}
}  



function retrieveStoresByMerchantId(eleId){
	var merchantId = $("#"+eleId+" option:selected").val();
	$('#locationId').children('option:not(:first)').remove();
	if(merchantId!=null && merchantId!=-1){
	$.ajax({
			url 		: "getStoresByMerchantId?merchantId="+merchantId,
		  	type		: "GET",
		  	async		: false, 
		  	success: function (response) {
		  	  if(response!=null ){
                console.log(response);
                $('#locationId').children('option:not(:first)').remove();
                $.each(response , function(key , value){
                	 $('#locationId')
                     .append($("<option></option>")
                     .attr("value",key)
                     .text(value));
              });
		  	}else{
		  		$("#errorfield").html('Failed to fetch Store ID');
		  	}   
            },
            error: function (textStatus, errorThrown) {
                console.log(textStatus);
            }
		   });
	}else{
		 clearError('merchantId');
		 clearError('locationId');
	}
}  

function retrieveAvailableInventory(){
	
	if(validateStore()){
		var productId = $("#productId option:selected").val();
		var merchantId = $("#merchantId option:selected").val();
		var locationId = $("#locationId option:selected").val();
		if(productId!=null && productId!=-1 && (merchantId==null || merchantId==-1) )
			{
			$.ajax({
				url 		: "getMerchantsAndPackages?productId="+productId,
			  	type		: "GET",
			  	async		: false, 
			  	success: function (response) {
	              if(response!=null && response['packageIds']!=null && response['packageIds'].length > 0 && response['error']==null) {
	            	  $("#errorfield").html('')
	               	              
	                if(response['availableInventory']!=null && response['availableInventory']!='')  {
	                	          	$("#availableInventory").val(response['availableInventory']);
	               	}
	                else{
	                	$("#availableInventory").val('0');
	                }
			  	}else{
			  		if(response==null || response['error']==null)
			  			$("#errorfield").html('Failed to fetch Package ID');
			  		else
			  			$("#errorfield").html(response['error']);
			  	}
	            },
	            error: function (textStatus, errorThrown) {
	                console.log(textStatus);
	            }
			   });
			}
		if(merchantId!=null && merchantId!=-1){
			$("#availableInventory").val('0');
		$.ajax({
				url 		: "getAvailableInventory?productId="+productId+"&&merchantId="+merchantId+"&&locationId="+locationId,
			  	type		: "GET",
			  	async		: false, 
			  	success: function (response) {
			  	 if(response!=null  && response!=''){
	                $("#availableInventory").val(response);
			  	}else{
	                $("#availableInventory").val('0');
			  	}   
	            },
	            error: function (textStatus, errorThrown) {
	                console.log(textStatus);
	            }
			   });
		}
	}
}  

function clickApproveReject(status){
	$("#feedBackTd").html('');
	$("#tableErr").html('');
	$("#statusChangeDesc").val('');
	if($("input[name=orderPartnerId]:checked").length < 1){
		 $("#tableErr").html(readMessage("order.selectFor.approveReject"));
	}
	else{
		if(status=='APPROVED'){
			$("#showRejectBtn").hide();
			$("#showApproveBtn").show();
		}else if(status=='REJECTED'){
			$("#showRejectBtn").show();
			$("#showApproveBtn").hide();
		}
		$("#tableErr").html('');
		$("#"+status).show();
		$.each($("input[name=orderPartnerId]:checked"), function() {
			$(this).closest('tr').css("background-color","#85b3fc");
		});
		$("#status").val(status);
		$('#define-constant-approve').modal('show');
	}
}

function goToPrevious() {
	$('#define-constant-approve').modal('hide');
	$.each($("input[name=orderPartnerId]:checked"), function() {
		$(this).closest('tr').css("background-color","#f9f9f9");
	});
}

function goApproveRejectRole(){
	
	if($("#statusChangeDesc").val()!=null && $("#statusChangeDesc").val()!=''){
		$("#dialogErrorApprove").html('');
		$("#approveOrderForm").attr('action','approveRejectOrder');
		$("#approveOrderForm").submit();
	}
	else{
		$("#dialogErrorApprove").html('Please enter Remarks');
	}
}

function validateFromDateToDate()
{
	if(fromDate!=null && fromDate!='' && toDate!=null && toDate!='')
	{
	var firstValue=$("#fromDate").val().split('/');
	var secondValue=$("#toDate").val().split('/');

	 var firstDate=new Date(firstValue[2],(firstValue[1] - 1 ),firstValue[0]);
	// firstDate.setFullYear();

	 var secondDate=new Date(secondValue[2],(secondValue[1] - 1 ),secondValue[0]);
	// secondDate.setFullYear();   	
	

var formId="checkOrderStatus";
var eleId="toDate";
if(secondDate < firstDate)
	{
	generateAlert(formId, eleId,eleId+".pattern");
	return false;
	}
else
	{
	 clearError(formId+" #"+eleId);
	 return true;
	}
}
}


function isAlphaNumericWithDotAndHyphen(ctrl) {
 	
	var isValid;
 	var regex = /^[a-zA-Z0-9]+[a-zA-Z0-9.-]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 .-]*/g, '');
	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	return isValid;
}

