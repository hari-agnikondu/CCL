function searchMerchantLinkedWithProduct(event) {
    event.preventDefault();
    $("#merchantToProductForm").attr('action', 'showAllMerchantsLinkedWithProducts');
    $("#merchantToProductForm").submit();
}

function clickShowPrdtoMer()
{
	$("#merchantToProductForm").attr('action', 'showAssignProductToMerchant');
	$("#merchantToProductForm").submit();
}

function goBackToAssignProdToMerchant()
{
	$("#merchantToProductForm").attr('action', 'assignMerchantConfig');
	$("#merchantToProductForm").submit();
}

function ResetAssignProdToMerchant(){
    
    clearError("productId");
    clearError("merchantId");
    $('select option[value="-1"]').prop("selected",true);
    $("#statusMerchant").html('');
    $("#formErrorId").html('');
}

function goConfirmDeleteMerchant(data) {
	var datatosplit = "";
	datatosplit = data;
	datatosplit = datatosplit.replace(/\*/g, "'");

	var splitted = datatosplit.split('~');
	var id = splitted[0];
	var name = splitted[1];
	document.getElementById(id).style.background = "#85b3fc";
	name = name.trim();

	$("#merchantIdtoDelete").val(id);
	$("#merchantNametoDelete").val(name);
	document.getElementById("merchantNameDisp").innerHTML = name;
	

}

function goDeleteMerchant() {

	$("#deleteMerchantForm").attr('action', 'deleteMerchantProduct');
	$("#deleteMerchantForm").submit();

}
 
 
 
 

function assignProductToMerchant(formId)
{
/** var merhchantData="";
	var productData="";*/
	
	
	$("#merchantForm").attr('action', 'testMerchant');
	$("#merchantForm").submit();
	
	/** productData=$('#productId').val();
	merhchantData=$('#merchantId').val();
	
	alert("productData : "+productData);
	alert("merhchantData : "+merhchantData);*/


}
 
 
 
 
 
 
 
 
/**
* validations 
*/
function validatewithmaxLengthMerchantProduct(formId, eleId) {
    var isValid = false;
    clearError(formId + " #" + eleId);
    var eleVal = $("#" + formId + " #" + eleId).val();
    clearError(formId + " #" + eleId);
 
    if (eleVal.trim().length > readValidationProp(eleId + '.max.length')) {
        generateAlert(formId, eleId, eleId + ".length");
        return isValid;
    } else {
        clearError(formId + " #" + eleId);
 
        isValid = true;
    }
 
    return isValid;
 
}
 
function isAlphaNumericMerchantNameId(ctrl) {
 
	var isValid;
 	var regex = /^[a-zA-Z0-9]+[a-zA-Z0-9 ,.;'_\-]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ,.;'_\-]*/g, '');
	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	return isValid;
    

	
}
 
 
function isAlphaNumericWithSpaceproductNameId(ctrl) {
  

	var isValid;
 	var regex = /^[a-zA-Z0-9]+[a-zA-Z0-9 ]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ]*/g, '');
	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	return isValid;
   
 

	
}
 
/**
* Validate drop down field  
*/
function validateDropDownMerchantProduct(formId, eleId)
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
 


function goAssignProdToMerchant(formId)

{
	var isvalidProductName = validateDropDownMerchantProduct(formId, "productId");

	var isValidMerchantName= validateDropDownMerchantProduct(formId, "merchantId");

	if(isvalidProductName && isValidMerchantName){
		$("#merchantToProductForm").attr('action', 'assignProductToMerchant');
		$("#merchantToProductForm").submit();
}
}





function goConfirmDeleteProductMerchant(data) {

	var datatosplit = "";
	var productData="";
	var merchantData="";
	
	datatosplit = data;
	datatosplit = datatosplit.replace(/\*/g, "'");


	var splitted = datatosplit.split('#');
	 merchantData = splitted[0].trim();
	 productData = splitted[1];
	
	var splittedMer = merchantData.split('~');
	var mid = splittedMer[0];
	var mname = splittedMer[1];
	
	var splittedPrd = productData.split('~');
	var pid = splittedPrd[0];
	var pname = splittedPrd[1];
	
	
	
	document.getElementById(mid).style.background = "#85b3fc";
	mname = mname.trim();
	pname = pname.trim();

	$("#merchantIdtoDelete").val(mid);
	$("#merchantNametoDelete").val(mname);
	$("#productIdtoDelete").val(pid);
	$("#productNametoDelete").val(pname);
	document.getElementById("merchantNameDisp").innerHTML = mname;
	document.getElementById("productNameDisp").innerHTML = pname;
	

}

function goDeleteProductMerchant() {

	$("#deleteMerchantForm").attr('action', 'deleteProductMerchant');
	$("#deleteMerchantForm").submit();

}

function goBackToMerchantFromView(){
	$("#merchantToProductForm").attr('action', 'assignMerchantConfig');
	$("#merchantToProductForm").submit();
	
}


function clickViewForProdAcess(id){
	var splitted = id.split('-');
	var mid = splitted[0];
	var mname = splitted[1];
	
	$("#merchantId").val(mid);
	$("#productId").val(mname);

	
	$("#merchantToProductForm").attr('action','viewAssignProductToMerchant');
	$("#merchantToProductForm").submit();  

	
}