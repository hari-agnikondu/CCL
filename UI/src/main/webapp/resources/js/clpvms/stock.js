function retrieveStoresByMerchantId(formId,eleId){
	var merchantId = $("#"+eleId+" option:selected").val();
	if(merchantId!=null && merchantId!=-1){
		clearError('merchantId');
		 
	$.ajax({
			url 		: "getStoresAndProductsByMerchantId?merchantId="+merchantId,
		  	type		: "GET",
		  	async		: false, 
		  	success: function (response) {
		  	  if(response!=null ){
                console.log(response);
                $('#locationId').children('option:not(:first)').remove();
                if(response[0]!=null){
                	$.each(response[0] , function(key , value){
                   	 $('#locationId')
                        .append($("<option></option>")
                        .attr("value",key)
                        .text(value));
                 });
                	$("#errorfield").html('');
                }else{
                	$("#errorfield").html('Failed to fetch Locations ');
                }
                if(response[1]!=null){   
                $('#productId').children('option:not(:first)').remove();
                $.each(response[1] , function(key , value){
               	 $('#productId')
                    .append($("<option></option>")
                    .attr("value",key)
                    .text(key+":"+value));
             });
                $("#errorfield").html('');
              }else{
              	$("#errorfield").html('Failed to fetch Products');
              }
		  	}else{
		  		$("#errorfield").html('Failed to fetch Locations and Products');
		  	}   
            },
            error: function (textStatus, errorThrown) {
                console.log(textStatus);
            }
		   });
	}else{
		generateAlert(formId, eleId, "select."+eleId);
		return false;
		 
	}
}  

function validateSelectBox(formID,eleID){
	
	if($("#"+formID+" #"+eleID+" option:selected").val()==-1){
		generateAlert(formID, eleID, "select."+eleID);
		return false;
	}else{
		clearError(formID+" #"+eleID);
		return true;
	}
}

function replenishmentOptions(){
	if($("input[name='autoReplenish']:checked").val()=='Y'){
		$("#reorderLevel").prop('readonly',false);
		$("#reorderValue").prop('readonly',false);
		$("#reorderLevel").val('');
		$("#reorderValue").val('');
		clearError('reorderValue');
		clearError('reorderLevel');
	}else{
		$("#reorderLevel").prop('readonly',true);
		$("#reorderValue").prop('readonly',true);
		$("#reorderLevel").val('0');
		$("#reorderValue").val('0');
		clearError('reorderValue');
		clearError('reorderLevel');
	}
}

function validateReplenishmentFields(formID,eleID){
	if($("input[name='autoReplenish']:checked").val()=='Y'){
		return validateFields(formID,eleID);
	}
	return true;
}

function defineStock(formId){
	$("#feedBackTd").html('');
	var isValidMerchant = true;
	var isValidLocations = true;
	var isValidProduct = true;
	if(formId=='defineStockForm'){
		isValidMerchant=validateSelectBox(formId,'merchantId');
		isValidLocations=validateSelectBox(formId,'locationId');
		isValidProduct=validateSelectBox(formId,'productId');
	}
	var isValidInit = validateFields(formId,'initialOrder');
	var isValidMaxInv = validateFields(formId,'maxInventory');
	var isValidReorderLvl = validateReplenishmentFields(formId,'reorderLevel');
	var isValidInitReorderVal = validateReplenishmentFields(formId,'reorderValue');
	
	if(isValidMerchant && isValidLocations && isValidProduct &&  isValidInit && isValidMaxInv && isValidReorderLvl && isValidInitReorderVal ){
		var isValidStock=true;
		if($("input[name='autoReplenish']:checked").val()=='Y'){
			
			var addReorderLevelAndValue =parseInt($("#reorderLevel").val())+parseInt($("#reorderValue").val());
			if( addReorderLevelAndValue >  parseInt($("#maxInventory").val())){
				generateAlert(formId, 'reorderLevel', "reorderLevel.maxInventory.greater");
				isValidStock=false;
			}else{
				clearError('reorderLevel');
				if( parseInt($("#reorderLevel").val()) >  parseInt($("#initialOrder").val())){
					generateAlert(formId, 'reorderLevel', "reorderLevel.initialOrder.greater");
					isValidStock=false;
				}else{
					clearError('reorderLevel');
				}
			}
			
			
			
			if( addReorderLevelAndValue  >  parseInt($("#maxInventory").val()) ){
				generateAlert(formId, 'reorderValue', "reorderValue.maxInventory.greater");
				isValidStock=false;
			}else{
				clearError('reorderValue');
				//if(!retrieveAvailableInventory(formId))
					//isValidStock=false	;
			}
			
		}
		if( parseInt($("#initialOrder").val()) >  parseInt($("#maxInventory").val()) ){
			generateAlert(formId, 'initialOrder', "initOrder.maxInventory.greater");
			isValidStock=false;
		}else{
			clearError('initialOrder');
		}
		if(isValidStock ){
			if(formId=='defineStockForm'){
				$('#defineStockForm').attr('action','addStock');
				$("#"+formId).submit();
			}else if(formId=='editStockForm'){
				//$("#updateStock").attr("data-toggle","modal");
				$("#updateStock").attr("data-target","#define-constant-edit");
				
			}
			
		}else
			if(formId=='editStockForm'){
			$("#updateStock").removeAttr("data-target");
		}
	}
	
}

/*To load stock edit screen*/

function clickEdit(eleObj){
	$("#feedBackTd").html('');
	var rowID=$(eleObj).closest('tr').attr('id');
	$("#merchantId").val($("#merchantId"+rowID).val());
	$("#locationId").val($("#locationId"+rowID).val());
	$("#productId").val($("#productId"+rowID).val());
	$("#stockSearch").attr('action','showEditStock');
	$("#stockSearch").submit();
} 

function clickViewFromStock(eleObj){
	$("#feedBackTd").html('');
	var rowID=$(eleObj).closest('tr').attr('id');
	$("#merchantId").val($("#merchantId"+rowID).val());
	$("#locationId").val($("#locationId"+rowID).val());
	$("#productId").val($("#productId"+rowID).val());
	$("#stockSearch").attr('action','showViewStock');
	$("#stockSearch").submit();
} 


function callAddPage(){
	 $("#feedBackTd").html('');
	 $("#stockSearch").attr('action','defineStock')
	 $("#stockSearch").submit();
}

function ResetStock(){
	$("#feedBackTd").html('');
	$("#merchantId").val('-1');
	$("#locationId").val('-1');
	$("#productId").val('-1');
	$("#initialOrder").val('');
	$("#autoReplenishY").prop('checked',true);
	$("#reorderLevel").val('');
	$("#reorderValue").val('');
	$("#maxInventory").val('');
	$("#reorderLevel").prop('readonly',false);
	$("#reorderValue").prop('readonly',false);
	
	clearError('merchantId');
	clearError('locationId');
	clearError('initialOrder');
	clearError('productId');
	clearError('reorderLevel');
	clearError('reorderValue');
	clearError('maxInventory');
	
}

function goUpdateStock(){
	$('#editStockForm').attr('action','editStock');
	$('#editStockForm').submit();
}

function cancelUpdate(){
	$("#define-constant-edit").hide();
}
/*function retrieveAvailableInventory(formID){
	
		var productId = $("#productId option:selected").val();
		var merchantId = $("#merchantId option:selected").val();
		var locationId = $("#locationId option:selected").val();
		if(merchantId!=null && merchantId!=-1){
		$.ajax({
				url 		: "/cclp-vms/order/getAvailableInventory?productId="+productId+"&&merchantId="+merchantId+"&&locationId="+locationId,
			  	type		: "GET",
			  	async		: false, 
			  	success: function (response) {
			  	 if(response!=null  && response!=''){
	               if( parseInt($("#reorderValue").val()) > parseInt(response) ){
	            	   generateAlert(formID, 'reorderValue', "reorderValue.available.greater");
	            	   return false;
	               }
	               	 clearError('reorderValue');
			  		 return true;
			  	}else{
	              
			  		return true;
			  	}   
	            },
	            error: function (textStatus, errorThrown) {
	                console.log(textStatus);
	                return false;
	            }
			   });
		}else{
			 clearError('merchantId');
			 clearError('locationId');
		}
}  */