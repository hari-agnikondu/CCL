 
function validateDropDownPanProductId(formId, eleId)
{
    var isValid = false;
    var eleVal = $("#" + formId + " #" + eleId).val();
    
    if (eleVal.length<=0 || eleVal == '-1'){
        generateAlert(formId, eleId, eleId + ".unselect");
    } else {
        clearError(formId + " #" + eleId);
        isValid = true;
    }
    return isValid;
}
 
 
function isNumericMonth(ctrl) {
    var isValid;
     var regex = /^[0-9]*$/;
    isValid = regex.test($("#" + ctrl.id + "").val());
    ctrl.value = ctrl.value.replace(/[^0-9]/g, '');
    
    if ((ctrl.value.charAt(0) == ' '))
        ctrl.value =ctrl.value.replace(/\s+/,'');
    
    return isValid;
}

function goSavePanExpiry(formId,event){
	event.preventDefault();
var isvalidProductName = validateDropDownPanProductId(formId, "panProductId");
if (isvalidProductName) {
	
	$("#panExpiryExemption").attr('action', 'savePanExpiryParameters');
	$("#panExpiryExemption").submit();
}

}

function resetPanExpiry(){
	$("#statusId").html('');

clearError("panProductId");


$("#januaryId").val('');
$("#februaryId").val('');
$("#marchId").val('');
$("#aprilId").val('');
$("#mayId").val('');
$("#juneId").val('');
$("#julyId").val('');
$("#augustId").val('');
$("#septemberId").val('');
$("#octoberId").val('');
$("#novemberId").val('');
$("#decemberId").val('');

$('select option[value="-1"]').prop("selected",true);
}
