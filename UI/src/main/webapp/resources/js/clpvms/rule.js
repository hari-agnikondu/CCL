
/* Load the rule configurations on page load */
$(function () {
   
	$('#builder-basic').queryBuilder({
		plugins: ['bt-tooltip-errors'],
  
		filters: JSON.parse($('#ruleFilters').val()) 
	});
   
});

/* Valid input rule Name */
function validateRuleName(formId,eleId){
	 
	 var isValid=false;
	 var minLen=readValidationProp(eleId+'.min.length');
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);

	 if((eleVal==null ||eleVal=='')  && minLen!=0){ 
		generateAlert(formId, eleId,eleId+".empty");
		return isValid;
	 }	 
	  if( !(eleVal==null ||eleVal=='') && ((eleVal.trim().length<minLen) || ( eleVal.trim().length>readValidationProp(eleId+'.max.length')) )){
			 generateAlert(formId, eleId,eleId+".length");
		 }
		 else
		 {
			 clearError(formId+" #"+eleId);
			 
			 isValid=true;
		 }
	
	return isValid;
	 
}


/* Get the rule details based on selected rule */
function getRule()
{

	// Clear out previous message
	$('#feedBackTd').empty();

	if ($("#ruleId").val() == "-1")
	{
		resetRule();
		return;
	}

	$.ajax({
		url 		: "rules?ruleName="+$("#ruleId option:selected").text(),
		type		: "GET",
		async		: false, 
		success: function (response) {

			$.each(response, function(i, rule) {

				$("#action").val(rule.action);

				// txnTypeId
				$('input[type=checkbox]').each(function () {
                       $(this).prop('checked', false);
					$(this).prop('checked', false);
					if (rule.txnTypeId.indexOf($(this).val()) >= 0)
					{
						$(this).prop('checked', true);
					}
					
				});

				$('#builder-basic').queryBuilder('setRules', JSON.parse(rule.jsonReq));

			});
		},
		error: function (textStatus, errorThrown) {
			//alert(textStatus);
		}
	});
}

/* Reset the input elements */
function resetRule()
{
	$('#feedBackTd').empty();
	var formId="ruleForm";
	$('#ruleName').val('');
	$('input:checkbox').removeAttr('checked');
	$("#action option:selected").removeAttr("selected");
	$('#builder-basic').queryBuilder('reset');
	clearError(formId+" #ruleName");
	clearError(formId+" #txnTypes");
	
}

/* Saves the Rule */
function saveRule()
{

	// New Rule and rule rename 
	 var validRuleName=validateRuleName("ruleForm", "ruleName");
	   if (!validRuleName)
			return; 
	var rules = $('#builder-basic').queryBuilder('getRules');
	
	if (rules == null)
		return false;
	
	if (!$.isEmptyObject(rules)) {
		$("#jsonReq").val(JSON.stringify(rules, null, 2));
	}
	var validTxnType=validate_txnType("ruleForm", "txnTypes");
	if(!validTxnType)
		return;
	$("#ruleForm").submit();
}

function validate_txnType(formId,eleId)
{
	//validate txn type
	var txnCount=0;
	$('input[type=checkbox]').each(function () {

		if ($(this).prop('checked')==true)
		{
			txnCount=txnCount+1;
		}
	});
	if(txnCount==0)
		{
		generateAlert(formId, eleId,eleId+".empty");
		return false;
		}
   clearError(formId+" #"+eleId);
  return true;
    	
	}
function fillRuleName(param) {
	if (param.value == '-1') {
		$('#ruleName').val("");
	} else {
		clearError('ruleForm #ruleName');
		$("#feedBackTd").html('');
		var ruleName = param.options[param.selectedIndex].text;
		$('#ruleName').val(ruleName);
	}
}


function viewTransaction(txnTypeId,url_viewpage) {
		url_viewpage = url_viewpage	+ "/config/rule/viewTransactions?txnTypeId=" + txnTypeId;
		$('#load1').load(url_viewpage, function() {
			$('#define-constant-viewTransaction').modal({
				show : true
			});
		});

	

}
