	function copyProduct(){
		$("#feedBackTd").html('');
		if($("#copyFromProduct  option:selected").val()==-1){
			document.getElementById("parentProductIdError").innerHTML='<font color="red">'+readMessage("copyFromProduct.empty")+"</font>";	
			return false;
		}
		clearError("copyFromProduct");
		$("#limitForm").attr('action','productLimit');
		$("#limitForm").submit();
	 }
	 
	function allowOnlyNumbers(ctrl) {
		
		var isValid;
	 	var regex = /^[0-9]*$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^0-9 ]*/g, '');
		
		return isValid;
	}
	
	function positiveNonEmptyAmount(ctrl){
		var isValid=true;
		var amount=ctrl.value;
		var ctrlId = ctrl.id;
		//var txnShortName=ctrlId.substring(ctrlId.split('_', 1).join('_').length+1,ctrlId.split('_', 2).join('_').length);	
		var txnShortName = ctrlId.slice(0, ctrlId.lastIndexOf('_')); 
		if($("input[name='"+txnShortName+"']").val()=='Y'){
			
			if(amount!=null && amount.trim()!=''){
				/*generateAlert("limitForm", ctrl.id,"product.minAmtPerTxn.empty");
				isValid= false;*/
				if(isNaN(amount) || parseFloat(amount)<0){
					generateAlert("limitForm",ctrl.id,"product.minAmtPerTxn.invalid");
					isValid= false;
				}
				else if(!DecimalValueFormat(ctrl)){
					isValid= false;
				}
	            else{
					
					clearError(ctrlId);
					$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
				}
			}
			else{
				
				clearError(ctrlId);
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
			}
			
			if(!isValid) {
				 $("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
			}
		}
		else{
			clearError(ctrlId);
		}
		return isValid;
	}
	function maxAmtPerTxnValidation(ctrl){
		var isValid=true;
		var maxAmount=ctrl.value;
		var ctrlId=ctrl.id;
		//var txnShortName=ctrlId.substring(ctrlId.split('_', 1).join('_').length+1,ctrlId.split('_', 1).join('_').length);	
		var txnShortName = ctrlId.slice(0, ctrlId.lastIndexOf('_')); 
		if($("input[name='"+txnShortName+"']").val()=='Y'){
			
			var minAmt= $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").val();
			
			if(maxAmount!=null  && maxAmount.trim()!=''){
				/*generateAlert("limitForm", ctrlId,"product.maxAmtPerTxn.empty");
				isValid= false;
			}*/
				if(minAmt==null || minAmt.trim()==''){
					generateAlert("limitForm",$("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").attr('id'),"product.minAmtPerTxn.empty");
					isValid= false;
				}else{
					positiveNonEmptyAmount(document.getElementById(ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx"));
				}
				if(isNaN(maxAmount) || parseFloat(maxAmount)<=0 ){
					generateAlert("limitForm",  ctrlId,"product.maxAmtPerTxn.invalid");
					isValid= false;
				}
				else if(!DecimalValueFormat(ctrl)){
					isValid= false;
				}
				else if(minAmt!=null && minAmt.trim()!='' && parseFloat(maxAmount)<parseFloat(minAmt)){
					generateAlert("limitForm",  ctrlId,"product.maxAmtPerTxn.lesser");
					isValid= false;
				}
				else{
					clearError(ctrlId);
					$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
				}
			}
			else{
				clearError(ctrlId);
				clearError($("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").attr('id'));
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
			}
			if(!isValid) {
				 $("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
			}
		}
		else{
			clearError(ctrlId);
			clearError($("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").attr('id'));
		}
		return isValid;
	}
	
	
	
	function maxAmtValidation(){
		
		var isValid=true;
		
		$("input[id$='_dailyMaxAmt']").each(function (i, el) {
	        
			var ctrlId = el.id;
			var minAmtPerTxn = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").val();
			var maxAmtPerTxn = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").val();
			
			//var txnShortName=ctrlId.substring(ctrlId.split('_', 1).join('_').length+1,ctrlId.split('_', 2).join('_').length);	
			var txnShortName = ctrlId.slice(0, ctrlId.lastIndexOf('_')); 
			if($("input[name='"+txnShortName+"']").val()=='Y'){
				
				if($(el).val()!=null && $(el).val().trim()!=''){
					
					if(maxAmtPerTxn==null || maxAmtPerTxn.trim()==''){
						generateAlert("limitForm", $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").attr('id'),"product.maxAmtPerTxn.empty");
						isValid= false;
					}else if(!maxAmtPerTxnValidation(document.getElementById(ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx"))){
							isValid= false;
					}
					else{
						clearError($("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").attr('id'));
					}
					if(minAmtPerTxn==null || minAmtPerTxn.trim()==''){
						generateAlert("limitForm",$("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").attr('id'),"product.minAmtPerTxn.empty");
						isValid= false;
					}else if(!positiveNonEmptyAmount(document.getElementById(ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx"))){
							isValid= false;
					}
					else{
						clearError($("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").attr('id'));
					}
					if(!DecimalValueFormat(el)){
						isValid= false;
					}
					else if(isNaN($(el).val())){
						generateAlert("limitForm", ctrlId,"product.NaN");
						isValid= false;
					}
					else if(maxAmtPerTxn!=null && maxAmtPerTxn!='' && parseFloat($(el).val()) <  parseFloat(maxAmtPerTxn )){
						generateAlert("limitForm", ctrlId,"product.dailyMaxAmt.lesser.maxAmtPerTxn");
						isValid= false;
					}
					else if(maxAmtPerTxn==null || maxAmtPerTxn.trim()=='' || minAmtPerTxn==null || minAmtPerTxn.trim()=='' || parseFloat(minAmtPerTxn) > parseFloat(maxAmtPerTxn)){
						isValid= false;
					}
					else{
						clearError(ctrlId);
						$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
					}
					if(!isValid) {
						 $("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
					}
				}
				else{
					clearError(ctrlId);
				}	
			}
			else{
				clearError(ctrlId);
			}
	     });
		
		$("input[id$='_weeklyMaxAmt']").each(function (i, el) {
	        
			var ctrlId = el.id;
			var minAmtPerTxn = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").val();
			var dailyMaxAmt = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_dailyMaxAmt").val();
			var maxAmtPerTxn = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").val();
			//var txnShortName=ctrlId.substring(ctrlId.split('_', 1).join('_').length+1,ctrlId.split('_', 2).join('_').length);	
			var txnShortName = ctrlId.slice(0, ctrlId.lastIndexOf('_')); 
			if($("input[name='"+txnShortName+"']").val()=='Y'){
				
				if($(el).val()!=null && $(el).val().trim()!=''){
					

					if(maxAmtPerTxn==null || maxAmtPerTxn.trim()==''){
						generateAlert("limitForm", $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").attr('id'),"product.maxAmtPerTxn.empty");
						isValid= false;
					}else if(!maxAmtPerTxnValidation(document.getElementById(ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx"))){
							isValid= false;
					}else{
						clearError($("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").attr('id'));
					}
					if(minAmtPerTxn==null || minAmtPerTxn.trim()==''){
						generateAlert("limitForm",$("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").attr('id'),"product.minAmtPerTxn.empty");
						isValid= false;
					}else if(!positiveNonEmptyAmount(document.getElementById(ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx"))){
							isValid= false;
					}else{
						clearError($("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").attr('id'));
					}
					
					if(!DecimalValueFormat(el)){
						isValid= false;
					}
					else if(  isNaN($(el).val())){
						generateAlert("limitForm", ctrlId,"product.NaN");
						isValid= false;
					}
					else if(dailyMaxAmt!=null && dailyMaxAmt.trim()!='' && parseFloat($(el).val()) < parseFloat(dailyMaxAmt) ){
						generateAlert("limitForm", ctrlId,"product.weeklyMaxAmt.lesser.dailyMaxAmt");
						isValid= false;
					}
					else if(maxAmtPerTxn!=null && maxAmtPerTxn.trim()!='' && parseFloat($(el).val()) < parseFloat(maxAmtPerTxn) ){
						generateAlert("limitForm", ctrlId,"product.weeklyMaxAmt.lesser.maxAmtPerTxn");
						isValid= false;
					}
					else if(maxAmtPerTxn==null || maxAmtPerTxn.trim()=='' || minAmtPerTxn==null || minAmtPerTxn.trim()=='' || parseFloat(minAmtPerTxn) > parseFloat(maxAmtPerTxn)){
						isValid= false;
					}
					else{
						clearError(ctrlId);
						$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
					}	
					if(!isValid) {
						 $("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
					}	
				}else{
					clearError(ctrlId);
				}		
			}else{
				clearError(ctrlId);
			}	
	     });
		
		$("input[id$='_monthlyMaxAmt']").each(function (i, el) {
	        
			var ctrlId = el.id;
			var minAmtPerTxn = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").val();
			var weeklyMaxAmt = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_weeklyMaxAmt").val();
			var dailyMaxAmt = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_dailyMaxAmt").val();
			var maxAmtPerTx = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").val();
			//var txnShortName=ctrlId.substring(ctrlId.split('_', 1).join('_').length+1,ctrlId.split('_', 2).join('_').length);	
			var txnShortName = ctrlId.slice(0, ctrlId.lastIndexOf('_')); 
			if($("input[name='"+txnShortName+"']").val()=='Y' ){
				
				if($(el).val()!=null && $(el).val().trim()!='' ){
					
					if(maxAmtPerTx==null || maxAmtPerTx.trim()==''){
						generateAlert("limitForm", $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").attr('id'),"product.maxAmtPerTxn.empty");
						isValid= false;
					}else if(!maxAmtPerTxnValidation(document.getElementById(ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx"))){
							isValid= false;
					}else{
						clearError($("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").attr('id'));
					}
					if(minAmtPerTxn==null || minAmtPerTxn.trim()==''){
						generateAlert("limitForm",$("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").attr('id'),"product.minAmtPerTxn.empty");
						isValid= false;
					}else if(!positiveNonEmptyAmount(document.getElementById(ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx"))){
							isValid= false;
					}
					else{
						clearError($("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").attr('id'));
					}
					if(!DecimalValueFormat(el)){
						isValid= false;
					}
					else if(isNaN($(el).val())){
						generateAlert("limitForm", ctrlId,"product.NaN");
						isValid= false;
					}
					else if(weeklyMaxAmt!=null && weeklyMaxAmt.trim()!='' && parseFloat($(el).val()) < parseFloat(weeklyMaxAmt) ){
						generateAlert("limitForm", ctrlId,"product.monthlyMaxAmt.lesser.weeklyMaxAmt");
						isValid= false;
					}
					else if(dailyMaxAmt!=null && dailyMaxAmt.trim()!='' && parseFloat($(el).val()) < parseFloat(dailyMaxAmt) ){
						generateAlert("limitForm", ctrlId,"product.monthlyMaxAmt.lesser.dailyMaxAmt");
						isValid= false;
					}
					else if(maxAmtPerTx!=null && maxAmtPerTx.trim()!='' && parseFloat($(el).val()) < parseFloat(maxAmtPerTx) ){
						generateAlert("limitForm", ctrlId,"product.monthlyMaxAmt.lesser.maxAmtPerTxn");
						isValid= false;
					}
					else if(maxAmtPerTx==null || maxAmtPerTx.trim()=='' || minAmtPerTxn==null || minAmtPerTxn.trim()=='' || parseFloat(minAmtPerTxn) > parseFloat(maxAmtPerTx)){
						isValid= false;
					}
					else{
						clearError(ctrlId);
						$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
					}
					if(!isValid) {
						 $("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
					}		
				}
				else{
					clearError(ctrlId);
				}	
			}
			else{
				clearError(ctrlId);
			}	
			
	     });
		
		$("input[id$='_yearlyMaxAmt']").each(function (i, el) {
	        
			var ctrlId = el.id;
			var minAmtPerTxn = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").val();
			var monthlyMaxAmt = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_monthlyMaxAmt").val();
			var weeklyMaxAmt = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_weeklyMaxAmt").val();
			var dailyMaxAmt = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_dailyMaxAmt").val();
			var maxAmtPerTx = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").val();
			//var txnShortName=ctrlId.substring(ctrlId.split('_', 1).join('_').length+1,ctrlId.split('_', 2).join('_').length);	
			var txnShortName = ctrlId.slice(0, ctrlId.lastIndexOf('_')); 
				if($("input[name='"+txnShortName+"']").val()=='Y'){
					if($(el).val()!=null && $(el).val().trim()!='' ){
						
						if(maxAmtPerTx==null || maxAmtPerTx.trim()==''){
							generateAlert("limitForm", $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").attr('id'),"product.maxAmtPerTxn.empty");
							isValid= false;
						}else if(!maxAmtPerTxnValidation(document.getElementById(ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx"))){
								isValid= false;
							
						}else{
							clearError($("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_maxAmtPerTx").attr('id'));
						}
						if(minAmtPerTxn==null || minAmtPerTxn.trim()==''){
							generateAlert("limitForm",$("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").attr('id'),"product.minAmtPerTxn.empty");
							isValid= false;
						}else if(!positiveNonEmptyAmount(document.getElementById(ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx"))){
							
								isValid= false;
						}else{
							clearError($("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minAmtPerTx").attr('id'));
						}
						
						if(!DecimalValueFormat(el)){
							isValid= false;
						}
						else if(  isNaN($(el).val())){
							generateAlert("limitForm", ctrlId,"product.NaN");
							isValid= false;
						}
						else if(monthlyMaxAmt!=null && monthlyMaxAmt.trim()!='' && parseFloat($(el).val()) < parseFloat(monthlyMaxAmt)){
							generateAlert("limitForm", ctrlId,"product.yearlyMaxAmt.lesser.monthlyMaxAmt");
							isValid= false;
						}
						else if(weeklyMaxAmt!=null && weeklyMaxAmt.trim()!='' && parseFloat(($(el).val())) < parseFloat(weeklyMaxAmt )){
							generateAlert("limitForm", ctrlId,"product.yearlyMaxAmt.lesser.weeklyMaxAmt");
							isValid= false;
						}
						else if(dailyMaxAmt!=null && dailyMaxAmt.trim()!='' && parseFloat($(el).val()) < parseFloat(dailyMaxAmt )){
							generateAlert("limitForm", ctrlId,"product.yearlyMaxAmt.lesser.dailyMaxAmt");
							isValid= false;
						}
						else if(maxAmtPerTx!=null && maxAmtPerTx.trim()!='' && parseFloat($(el).val()) < parseFloat(maxAmtPerTx) ){
							generateAlert("limitForm", ctrlId,"product.yearlyMaxAmt.lesser.maxAmtPerTxn");
							isValid= false;
						}
						else if(maxAmtPerTx==null || maxAmtPerTx.trim()=='' || minAmtPerTxn==null || minAmtPerTxn.trim()=='' || parseFloat(minAmtPerTxn) > parseFloat(maxAmtPerTx)){
							isValid= false;
						}
						else{
							clearError(ctrlId);
							$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
						}
						if(!isValid) {
							 $("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
						}		
					}else{
						clearError(ctrlId);
					}
			}
			else{
				clearError(ctrlId);
			}	
	     });
		
		return isValid;
	}
	
	function maxCountValidation(){
		
		var isValid=true;
		
		$("input[id$='_weeklyMaxCount']").each(function (i, el) {
	        
			var ctrlId = el.id;
			var dailyMaxCnt = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_dailyMaxCount").val();
			if($(el).val()!=null && $(el).val().trim()!=''){
				if(isNaN($(el).val())){
					generateAlert("limitForm", ctrlId,"product.NaN");
					isValid= false;
				}
				else if(dailyMaxCnt!=null && dailyMaxCnt!='' &&  parseInt($(el).val()) < parseInt(dailyMaxCnt) ){
					generateAlert("limitForm", ctrlId,"product.weeklyMaxCnt.lesser.dailyMaxCnt");
					isValid= false;
				}
				else{
					clearError(ctrlId);
					$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
				}
				if(!isValid) {
					 $("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				}	
			}
			else{
				clearError(ctrlId);
			}
	     });
		
		$("input[id$='_monthlyMaxCount']").each(function (i, el) {
	        
			var ctrlId = el.id;
			var weeklyMaxCnt = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_weeklyMaxCount").val();
			var dailyMaxCnt = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_dailyMaxCount").val();
			if($(el).val()!=null && $(el).val().trim()!=''){
				if( isNaN($(el).val())){
					generateAlert("limitForm", ctrlId,"product.NaN");
					isValid= false;
				}
				else if(weeklyMaxCnt!=null && weeklyMaxCnt.trim()!='' && parseInt($(el).val()) < parseInt(weeklyMaxCnt) ){
					generateAlert("limitForm", ctrlId,"product.monthlyMaxCnt.lesser.weeklylyMaxCnt");
					isValid= false;
				}
				else if(dailyMaxCnt!=null && dailyMaxCnt.trim()!='' && parseInt($(el).val()) < parseInt(dailyMaxCnt )){
					generateAlert("limitForm", ctrlId,"product.monthlyMaxCnt.lesser.dailylyMaxCnt");
					isValid= false;
				}
				else{
					clearError(ctrlId);
					$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
				}
				if(!isValid) {
					 $("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				}
			}else{
				clearError(ctrlId);
			}	
	     });
		
		$("input[id$='_yearlyMaxCount']").each(function (i, el) {
	        
			var ctrlId = el.id;
			var monthlyMaxCnt = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_monthlyMaxCount").val();
			var weeklyMaxCnt = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_weeklyMaxCount").val();
			var dailyMaxCnt = $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_dailyMaxCount").val();
			
			if($(el).val()!=null && $(el).val().trim()!='' ){
				if(isNaN($(el).val())){
					generateAlert("limitForm", ctrlId,"product.NaN");
					isValid= false;
				}
				else if(monthlyMaxCnt!=null && monthlyMaxCnt.trim()!='' && parseInt($(el).val()) < parseInt(monthlyMaxCnt )){
					generateAlert("limitForm", ctrlId,"product.yearlyMaxCnt.lesser.monthlyMaxCnt");
					isValid= false;
				}
				else if(weeklyMaxCnt!=null && weeklyMaxCnt.trim()!='' && parseInt($(el).val()) < parseInt(weeklyMaxCnt) ){
					generateAlert("limitForm", ctrlId,"product.yearlyMaxCnt.lesser.weeklylyMaxCnt");
					isValid= false;
				}
				else if(dailyMaxCnt!=null && dailyMaxCnt.trim()!='' && parseInt($(el).val()) < parseInt(dailyMaxCnt )){
					generateAlert("limitForm", ctrlId,"product.yearlyMaxCnt.lesser.dailylyMaxCnt");
					isValid= false;
				}
				else{
					clearError(ctrlId);
					$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
				}
				if(!isValid) {
					 $("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				}		
			}
			else{
				clearError(ctrlId);
			}		
	     });
		
		return isValid;
	}

	function clickSave() {
		
		$("#feedBackTd").html('');
		var minAmtPerTxValidFlag=true;
		$("input[id$='_minAmtPerTx']").each(function (i, el) {
			
			
			if(!positiveNonEmptyAmount(el))
				 minAmtPerTxValidFlag=false;
			
		});
		
		var maxAmtPerTxValidFlag=true;
		$("input[id$='_maxAmtPerTx']").each(function (i, el) {
			if(!maxAmtPerTxnValidation(el))
				maxAmtPerTxValidFlag=false;
		});
		
		var amtValidationFlag = maxAmtValidation();
		var countValidationFlag = maxCountValidation();
	
		if(minAmtPerTxValidFlag && maxAmtPerTxValidFlag && amtValidationFlag && countValidationFlag)
			$("#limitForm").submit();
	}
	
	function DecimalValueFormat(Object)
	{
	  var v_qty = $(Object).val();
	  var objId = $(Object).attr('id');
	  
	  var splitvalue = v_qty.split(".");
	  
	  if(splitvalue.length>2){
	    	generateAlert($(Object).closest('form').attr('id'), Object.id,"product.NaN");
	    	 $("#"+objId.split('_', 1)+objId.split('_', 1)).css('visibility',  'visible');
	    	return false;
	   }
	  	if(v_qty.substr(0,1)=='.'){
		 	 v_qty='0'+v_qty;
		 	Object.value=v_qty;
		}
	//  var v_qty_int,
	  var  v_qty_dec;
	  
	  if(splitvalue.length > 1)
	  {
	//    v_qty_int = splitvalue[0];
	    v_qty_dec = splitvalue[1];   
	    
	    
	    
	     if(v_qty_dec.length>3){
	    	generateAlert($(Object).closest('form').attr('id'), Object.id,"product.decimal.validation");
	    	 $("#"+objId.split('_', 1)+objId.split('_', 1)).css('visibility',  'visible');
	    	return false;
	    }
	    else{
	    	clearError( Object.id);
	    	if(v_qty_dec.length==0){
	    		v_qty_dec="000";
	    		Object.value=Object.value+v_qty_dec;
	    	}
	    	
	    	return true;
	    }
	  }
	  else{
	    	clearError( Object.id);
	    	 $("#"+objId.split('_', 1)+objId.split('_', 1)).css('visibility',  'hidden');
	    	return true;
	    }
	

	}

/*	function allowNumbersWithDot(ctrl){
		var isValid;
	 	var regex = /^[1-9.]*$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^0-9.]/g, '');
		if((ctrl.value.startsWith(' '))||(ctrl.value.endsWith(' ')))
			ctrl.value =ctrl.value.replace(/\s+/,'');
		return isValid;
		
	}*/
	function allowNumbersWithDot(e){
	 var charCode;
     if (e.keyCode > 0) {
         charCode = e.which || e.keyCode;
     }
     else if (typeof (e.charCode) != "undefined") {
         charCode = e.which || e.keyCode;
     }
     if (charCode == 46)
         return true
     if (charCode > 31 && (charCode < 48 || charCode > 57))
         return false;
     return true;
}
	
	function isNumericfn(e){
		 var charCode;
	     if (e.keyCode > 0) {
	         charCode = e.which || e.keyCode;
	     }
	     else if (typeof (e.charCode) != "undefined") {
	         charCode = e.which || e.keyCode;
	     }

	     if (charCode > 31 && (charCode < 48 || charCode > 57))
	         return false;
	     return true;
	}
	function validateFeeDescription(ctrl){
		
		var isValid= true;
		var ctrlId=$(ctrl).attr('id');
		var ctrlVal = $(ctrl).val();
		/*if(ctrlVal==null || ctrlVal==''){
			generateAlert($(ctrl).closest('form').attr('id'),  ctrlId,"product.feeDesc.empty");
			$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
			isValid= false;
		}
		else */if(ctrlVal!=null &&  ctrlVal.length>100){
			generateAlert($(ctrl).closest('form').attr('id'),  ctrlId,"product.feeDesc.length");
			$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
			isValid= false;
		}
		else{
			//$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
			clearError(ctrlId);
		}
		return isValid;
	}
	function validateFeeAmt(ctrl) {
		var isValid = true;
		var feeAmount = $(ctrl).val();
		var ctrlId = $(ctrl).attr('id');
		var idPrefix = ctrlId.substring(0, ctrlId.lastIndexOf("_"));
	
		if (isNaN(feeAmount)) {
			generateAlert($(ctrl).closest('form').attr('id'), ctrlId,
					"product.feeAmt.invalid");
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css('visibility',
					'visible');
			isValid = false;
		} else if (parseFloat(feeAmount) <= 0) {
			generateAlert($(ctrl).closest('form').attr('id'), ctrlId,
					"product.feeAmt.zero");
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css('visibility',
					'visible');
			isValid = false;
		} else if (!DecimalValueFormat(ctrl)) {
			$("#" + ctrlId.split('_', 1) + ctrlId.split('_', 1)).css('visibility',
					'visible');
			isValid = false;
		} else {
			clearError(ctrlId);
		}
		return isValid;
	}
	
	function copyProductMaintenanceFee() {
		$("#feedBackTd").html('');
		if ($("#copyFromProduct  option:selected").val() == -1) {
			//generateAlert("maintenanceFeeForm", "copyFromProduct","copyFromProduct.empty");
			document.getElementById("parentProductIdError").innerHTML='<font color="red">'+readMessage("copyFromProduct.empty")+"</font>";	
			return false;
		}
		clearError("copyFromProduct");
		$("#maintenanceFeeForm").attr('action', 'productMaintenanceFee');
		$("#maintenanceFeeForm").submit();
	}
	
	function enableClawbackOptions(ctrl){
		
		var ctrlId = $(ctrl).attr('id');
		if ($(ctrl).is(':checked')) {
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
							+ "_clawbackCount").prop("readonly", false);
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_clawbackOption").prop("disabled", false);
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_clawbackMaxAmt").prop("readonly", false);
			if($("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))+ "_clawbackOption option:selected" ).val()=='N')
				$( "#"+ ctrlId.substring(0, ctrlId.lastIndexOf("_"))+ "_clawbackOption" ).find( 'option[value="O"]' ).prop( "selected", true ); 
		
		} else {
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
							+ "_clawbackCount").prop("readonly", true);
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_clawbackCount").val('');
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_clawbackOption").prop("disabled", true);
			
			$( "#"+ ctrlId.substring(0, ctrlId.lastIndexOf("_"))+ "_clawbackOption" ).find( 'option[value="N"]' ).prop( "selected", true ); 
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_clawbackMaxAmt").prop("readonly", true);
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_clawbackMaxAmt").val('');
			clearError(ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_clawbackCount");
			clearError(ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_clawbackOption");
			clearError(ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_clawbackMaxAmt");
		}
	}
	function clawbackOptionChange(ctrl){
		var ctrlId= $(ctrl).attr('id');
		var ctrlVal = $("#"+ctrlId+" option:selected").val();
		if(ctrlVal=='N'){
			
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_clawback").prop("checked", false);
			enableClawbackOptions($("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))+ "_clawback"));
			$( "#"+ ctrlId).find( 'option[value="N"]' ).prop( "selected", true ); 
		}
	}
	
	function validateClabackMaxFeeAmt(ctrl){
		var isValid=true;
		var feeAmount=$(ctrl).val();
		var ctrlId=$(ctrl).attr('id');
		if(feeAmount!=null  && feeAmount.trim()!=''){
			if(isNaN(feeAmount) ){
				generateAlert($(Object).closest('form').attr('id'),  ctrlId,"product.clawbackMaxFeeAmt.invalid");
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				isValid= false;
			}
			
			else if(!DecimalValueFormat(ctrl)){
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				isValid= false;
			}
			else{
				//$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
				clearError(ctrlId);
			}
		}
		return isValid;
	}
	function validateCount(ctrl){
		var ctrlId = $(ctrl).attr('id');
		var ctrlVal = $(ctrl).val();
		if(ctrlVal!=null && ctrlVal!=''){
			if(isNaN(ctrlVal)){
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				generateAlert($(ctrl).closest('form').attr('id'), ctrlId,"product.NaN");
		    	return false;
			}
			else if(parseInt(ctrlVal).length > 6 && !ctrlId.endsWith('firstMonthFeeAssessedDays')){
				if(ctrlId.endsWith('maxCount')){
					generateAlert($(ctrl).closest('form').attr('id'), ctrlId,"maintenanceFee.maxCnt.invalid");
				
				}
				else if(ctrlId.endsWith('freeCount')){
					
					generateAlert($(ctrl).closest('form').attr('id'), ctrlId,"maintenanceFee.freeCnt.invalid");
				}
					
				
		    	return false;
			}
			else if(parseInt(ctrlVal).length > 3 && ctrlId.endsWith('firstMonthFeeAssessedDays')){
				generateAlert($(ctrl).closest('form').attr('id'), ctrlId,"maintenanceFee.firstFeeAccessDays.invalid");
				
				
				return false;
			}
			return true;
		}
		
		return true;
	}
	
	function validateClawbackCount(obj) {
		var isValid= true;
		if (!$(obj).prop("readonly")) {
			var clawbackCnt = $(obj).val();
			var ctrlId = $(obj).attr('id');
			if (clawbackCnt != null && clawbackCnt.trim() != '') {
				if ( isNaN(clawbackCnt) || parseInt(clawbackCnt) < 1) {
					generateAlert($(obj).closest('form').attr('id'), ctrlId, "product.clawbackCnt.invalid");
					$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
					isValid = false;
				} else {
					clearError(ctrlId);
				}
			} else {
				generateAlert($(obj).closest('form').attr('id'), ctrlId, "product.clawbackCnt.empty");
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				isValid = false;
			}
		}
		return isValid ;
	}
	
	
	
	
	function isNonzeroNumeric(ctrl) {
		var isValid;
		var regex = /^[1-9]*$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^0-9]/g, '');
		if ((ctrl.value.startsWith(' ')) || (ctrl.value.endsWith(' ')))
			ctrl.value = ctrl.value.replace(/\s+/, '');
		return isValid;
	}

	function enableClawbackCount(obj) {
		var ctrlId = obj.id;
		if ($(obj).is(':checked')) {
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
							+ "_clawbackCount").prop("readonly", false);
		} else {
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
							+ "_clawbackCount").prop("readonly", true);
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_clawbackCount").val('');
			clearError(ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_clawbackCount");
		}
	}

	function callFeeConditionChange(obj) {
		var ctrlId = obj.id;
		
		var ctrlVal = $("#"+ctrlId + " option:selected").val();
		if (ctrlVal == "N") {
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
							+ "_feePercent").prop("readonly", true);
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
					+ "_feePercent").val('');
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt").prop("readonly", true);
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt").val('');
			
		} else if (ctrlVal == "A") {
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
							+ "_feePercent").prop("readonly", false);
			enableMinFee($("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))+ "_feePercent"));
		} else if (ctrlVal == "O") {
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_"))
							+ "_feePercent").prop("readonly", false);
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt").prop("readonly", true);
			$("#" + ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt").val('');
			
		}
		
	
		clearError(ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_feePercent");
		clearError(ctrlId.substring(0, ctrlId.lastIndexOf("_")) + "_minFeeAmt");

	}

	function enableFrequency(obj) {
		var ctrlId = $(obj).attr('id');
		var objVal = $(obj).val();
		if(objVal != null && objVal.trim() != '' ){
			if (!isNaN(objVal)) {
			/*	$("#" + ctrlId + "Freq").val("D");*/
				$("#" + ctrlId + "Freq").prop("disabled", false);
				clearError(ctrlId);
			} else {
				$("#" + ctrlId + "Freq").val("D");
				$("#" + ctrlId + "Freq").prop("disabled", true);
				if(ctrlId.endswith('_freeCount')){
					generateAlert("txnFeeForm", ctrlId, "product.freeCnt.invalid");
					$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');	
				}
					
				else if($(ctrlId.endsWith('_maxCount'))){
					generateAlert("txnFeeForm", ctrlId, "product.maxCnt.invalid");
					$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				}
				
			}
		}else {
			$("#" + ctrlId + "Freq").val("D");
			$("#" + ctrlId + "Freq").prop("disabled", true);
			
		}
	}
	
	function validateMinFeeAmt(ctrl){
		var isValid=true;
		var minFeeAmount=$(ctrl).val();
		var ctrlId=$(ctrl).attr('id');
		var feeAmt= $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_feeAmt").val();
		if (!$(ctrl).prop("readonly")) {
			if(minFeeAmount==null  || minFeeAmount.trim()==''){
				generateAlert("txnFeeForm", ctrlId,"product.minFeeAmt.empty");
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				isValid= false;
			}
			else if(isNaN(minFeeAmount) ){
				generateAlert("txnFeeForm",ctrlId,"product.minFeeAmt.invalid");
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				isValid= false;
			}
			else if(!DecimalValueFormat(ctrl)){
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				isValid= false;
			}
			else if( parseFloat(minFeeAmount) < parseFloat(feeAmt) ){
				generateAlert("txnFeeForm",  ctrlId,"product.minFeeAmt.greater");
				
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				isValid= false;
			}
			
			else{
				clearError(ctrlId);
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
			}
		}
		else{
			clearError(ctrlId);
		}
		return isValid;
	}
	
	/* Enable minimum fee amount if % fee is valid and applicable*/
	function enableMinFee(ctrl){
		var ctrlId=$(ctrl).attr('id');
		var minFeeAmtId= $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minFeeAmt").attr('id');
		var feeCondnVal=$("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_feeCondition option:selected").val();
		var isValid=validateFeePercentage(ctrl);
		if(isValid && !$(ctrl).prop("readonly") && feeCondnVal=='A'){
			$("#"+minFeeAmtId).prop("readonly",false);
		}
		else{
			$("#"+minFeeAmtId).prop("readonly",true);
			clearError(minFeeAmtId);
		}
	
	}
	function validateFeePercentage(ctrl){
		
		var isValid= true;
		
		if (!$(ctrl).prop("readonly")) {
			var feePercent = $(ctrl).val();
			var ctrlId=$(ctrl).attr('id');
			
			if(feePercent==null || feePercent==''){
				generateAlert("txnFeeForm", ctrlId,"product.feePerc.empty");
				
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				isValid= false;
			}
			else if( isNaN(feePercent) || parseFloat(feePercent)<=0 ){
				generateAlert("txnFeeForm",  ctrlId,"product.feePerc.zero");
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				isValid= false;
			}
			else if(!DecimalValueFormat(ctrl)){
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				isValid= false;
			}
			else if( parseFloat(feePercent)>100){
				generateAlert("txnFeeForm",  ctrlId,"product.feePerc.greater");
				$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
				isValid= false;
			}
			else{
				//$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
				clearError(ctrlId);
			}
		}
		return isValid;
	}
	
	
	function saveTxnFee(){
		
			$("#feedBackTd").html('');
			var isValidFeeDesc= true; 	
			var isValidClawbackCnt= true; 	
			var isValidFeeAmt = true;
			var isValidFeeCondn = true;
			var isValidFreeCnt=true;
			var isValidMaxCnt=true;
			
			$("input[id$='_feeDesc']").each(function (i, el) {
				if(!validateFeeDescription(el))
					isValidFeeDesc=false;
			});
			$("input[id$='_clawback']").each(function (i, el) {
				if($(el).is(":checked")){
					
					var ctrlId = el.id;
					var clawbackCntId =  $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_clawbackCount").attr('id');
					if(! validateClawbackCount($("#"+clawbackCntId))){
						 isValidClawbackCnt= false; 	
					}
				}
				
			});
			$("input[id$='_feeAmt']").each(function (i, el) {
				if(!validateFeeAmt(el))
					isValidFeeAmt=false;
			});
			
			$("select[id$='_feeCondition']").each(function (i, el) {
				var ctrlId=el.id;
				var val = $("#"+ctrlId+" option:selected").val();
				
				var minFeeAmtObj=$("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_minFeeAmt");
				var feePercentObj=$("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_feePercent");
				if(val=='A'){
					if(!validateFeePercentage(feePercentObj))
						isValidFeeCondn=false;
					else {
						if(!validateMinFeeAmt(minFeeAmtObj)){
							isValidFeeCondn=false;
						}
					}
				}else if(val=='O'){
					if(!validateFeePercentage(feePercentObj))
						isValidFeeCondn=false;
				}
					
			});
			
			$("input[id$='_freeCount']").each(function (i, el) {
				var freeCnt = $(el).val();
				var ctrlId = el.id;
				if (freeCnt != null && freeCnt.trim() != '' ) {
					if (isNaN(freeCnt) || parseInt(freeCnt) < 1) {
						generateAlert("txnFeeForm", ctrlId,"product.freeCnt.invalid");
						$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
						isValidFreeCnt=false;
					} else {
						clearError(ctrlId);
						$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
					}
				} 
			});
			
			$("input[id$='_maxCount']").each(function (i, el) {
				var freeCnt = $(el).val();
				var ctrlId = el.id;
				if (freeCnt != null && freeCnt.trim() != '' ) {
					if (isNaN(freeCnt) || parseInt(freeCnt) < 1) {
						generateAlert("txnFeeForm", ctrlId,"product.maxCnt.invalid");
						$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'visible');
						isValidMaxCnt=false;
					} else {
						clearError(ctrlId);
						$("#"+ctrlId.split('_', 1)+ctrlId.split('_', 1)).css('visibility',  'hidden');
					}
				} 
			});
			
			if(isValidFeeDesc && isValidClawbackCnt && isValidFeeAmt && isValidFeeCondn && isValidFreeCnt && isValidMaxCnt )
				$("#txnFeeForm").submit();
		
			
	}
	
	function copyProductTxnFee(){
		$("#feedBackTd").html('');
		if($("#copyFromProduct  option:selected").val()==-1){
			//generateAlert("txnFeeForm", "copyFromProduct","copyFromProduct.empty");
			document.getElementById("parentProductIdError").innerHTML='<font color="red">'+readMessage("copyFromProduct.empty")+"</font>";	
			return false;
		}
		clearError("copyFromProduct");
		$("#txnFeeForm").attr('action','productTransactionFee');
		$("#txnFeeForm").submit();
	}
		
	function ProductPurseTxnFee(){
		$("#feedBackTd").html('');
		if($("#copyFromProduct  option:selected").val()==-1){
			$("#copyFromProduct").val(null);		
		}
		
		$("#txnFeeForm").attr('action','productTransactionFee');
		$("#txnFeeForm").submit();
	}
	
	function ViewProductPurseTxnFee(){
		$("#feedBackTd").html('');
		if($("#copyFromProduct  option:selected").val()==-1){
			$("#copyFromProduct").val(null);		
		}
		
		$("#txnFeeForm").attr('action','viewProductTransactionFee');
		$("#txnFeeForm").submit();
	}
	
	function ProductPurseLimit(){
		$("#feedBackTd").html('');
		if($("#copyFromProduct  option:selected").val()==-1){
			$("#copyFromProduct").val(null);		
		}
		
		$("#limitForm").attr('action','productLimit');
		$("#limitForm").submit();
	}
	
	function ViewProductPurseLimit(){
		$("#feedBackTd").html('');
		if($("#copyFromProduct  option:selected").val()==-1){
			$("#copyFromProduct").val(null);		
		}
		
		$("#limitForm").attr('action','viewProductLimit');
		$("#limitForm").submit();
	}
	
	
	function enableProRation(){
		if ($("#monthlyFee_assessmentDate option:selected").val() == 'FD') {
			$("#monthlyFee_proRation").prop('disabled', false);
		}else{
			$("#monthlyFee_proRation").prop('disabled', true);
			$("#monthlyFee_proRation").prop('checked',false);
		}

	}


	
	// save maintenance fee 
	function clickMaintenanceFeeSave() {

		$("#feedBackTd").html('');
		var isValidFeeDesc= true; 	
		var isValidFeeAmt = true;
		var isValidClawbackCnt=true;
		var isfirstMonthFeeAssessedDays=true;
		var isValidCapFeeAmt = true;
		var isValidFreeCnt=true;
		var isValidMaxCnt=true;
		
		$("input[id$='_feeDesc']").each(function (i, el) {
			if(!validateFeeDescription(el))
				isValidFeeDesc=false;
		});
		
		$("input[id$='_feeAmt']").each(function (i, el) {
			if(!validateFeeAmt(el))
				isValidFeeAmt=false;
		});
		
		$("input[id$='_clawback']").each(function (i, el) {
			if($(el).is(":checked")){
				
				var ctrlId = $(el).attr('id');
				var clawbackCntId =  $("#"+ctrlId.substring(0,ctrlId.lastIndexOf("_"))+"_clawbackCount").attr('id');
				if(! validateClawbackCount($("#"+clawbackCntId))){
					 isValidClawbackCnt= false; 	
				}
			}
			
		});
		
		$("input[id$='_firstMonthFeeAssessedDays']").each(function (i, el) {
			if(!validateCount(el))
				isfirstMonthFeeAssessedDays=false;
		});
		
		$("input[id$='_capFeeAmt']").each(function (i, el) {
			if(!validMaintenanceCapFeeAmt(el))
				isValidCapFeeAmt=false;
		});
		
		$("input[id$='_freeCount']").each(function (i, el) {
			if(!validateCount(el))
				isValidFreeCnt=false;
		});
		$("input[id$='_maxCount']").each(function (i, el) {
			if(!validateCount(el))
				isValidMaxCnt=false;
		});
		
		if(isValidFeeDesc && isValidFeeAmt && isValidClawbackCnt && isfirstMonthFeeAssessedDays && isValidCapFeeAmt && isValidFreeCnt && isValidMaxCnt){
			$("select[id$='_clawbackOption']").each(function (i, el) {
				var objId= $(el).attr('id');
				$("#"+objId).prop("disabled",false);
			});
			
			$("#maintenanceFeeForm").submit();
		}
	}

	function validMaintenanceCapFeeAmt(ctrl){
		var isValid= true;
		var ctrlId=$(ctrl).attr('id');
		var feeAmount=$(ctrl).val();
		if(feeAmount!=null && feeAmount!=''){
			if(isNaN(feeAmount) ){
				generateAlert($(ctrl).closest('form').attr('id'),  ctrlId,"maintenanceFee.capFeeAmt.invalid");
				isValid= false;
			}
			else if(!DecimalValueFormat(ctrl)){
				isValid= false;
			}
			else{
				clearError(ctrlId);
			}
		}
		return isValid;
	}
	