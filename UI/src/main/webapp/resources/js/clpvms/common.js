/*
	Author					:	Venkatesh,sampath
	Date of Creation		:	02/02/2018
	REVIWED					:	TEAM2
	JS						:	CommonValidation.js
	Purpose					:	To provide the all common sort of validations	

 */


/*	Purpose : To check whether the input satisfies all the criteria		isNumber,removeSpace,allowAlphabetsSpace,allowAlphabetsSpace,emailValidation,allowAlphaNumericWithSpecialChars,etc...
 *
 *In feature it will add commonn validation if an.y is required on this js
 */	

$(document).ready(function() {

	$(".allowAlphaNumericWithSpace").keyup(function(){
		var isValid = false;
		var regex = /^[a-zA-Z0-9 ]*$/;
		isValid = regex.test($("#" + event.target.id + "").val());
		$(this).val(function(_, v){
			return v.replace(/[^a-zA-Z0-9 ]/g, '');
		});
		if(!isValid){
			$("#" + event.target.id + "").next().html('');
			$("#" + event.target.id + "").after('<span class="error_empty" style="color:red;"  id=span'+event.target.id+'"><br/>Values should allow Alphanumeric with space only</span>');
		}
		else {
			$("#" + event.target.id + "").next().html('');
		}
	});

	$(".isNumber").keyup(function(event){
		if(event.which != 8 && isNaN(String.fromCharCode(event.which))){
			$("#" + event.target.id + "").next().html('');
			$("#" + event.target.id + "").after('<span class="error_empty animated fadeOut" style="color:red;"  id=span'+event.target.id+'"><br/>Value should be numeric</span>');

			event.preventDefault(); //stop character from entering input
		}else{
			$("#" + event.target.id + "").next().html('');
		}
	});

	$(".isNumber").blur(function(event){
		$("#" + event.target.id + "").next().html('');

	}); 
	$(".removeSpace").keyup(function(){
		$(this).val(function(_, v){
			return v.replace(/\s+/g, '');
		});

	}); 
	$(".allowAlphabetsSpace").keyup(function(){
		var isValid = false;
		var regex = /^[a-zA-Z ]*$/;
		isValid = regex.test($("#" + event.target.id + "").val());
		$(this).val(function(_, v){
			return v.replace(/[^a-zA-Z ]/g, '');
		});
		if(!isValid){
			$("#" + event.target.id + "").next().html('');
			$("#" + event.target.id + "").after('<span class="error_empty" style="color:red;"  id=span'+event.target.id+'"><br/>Values should allow Alphabets with space only</span>');
		} else {
			$("#" + event.target.id + "").next().html('');
		}
	});

	$(".allowAlphabetsSpace").blur(function(event){
		$("#" + event.target.id + "").next().html('');
	});

	$(".emailValidation").blur(function(event){

		var isValid = false;
		var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		isValid = regex.test($("#" + event.target.id + "").val());
		
		if(isValid || $("#" + event.target.id + "").val()==null || $("#" + event.target.id + "").val()=="") {
			$("#" + event.target.id + "").next().html('');
		}
		else if(!isValid){
			$("#" + event.target.id + "").next().html('');
			$("#" + event.target.id + "").after('<span class="error_empty " style="color:red;"  id=span'+event.target.id+'"><br/>Please enter valid Email Address</span>');
		}
		else{
			$("#" + event.target.id + "").next().html('');
		}
	});

	$(".allowAlphaNumericWithSpecialChars").keyup(function(){
//		/^[ A-Za-z0-9_@.#~`+$^%*()\\[\]&-=';,{\}!|":<>?]*$/g;
		var isValid = false;
		var regex = /^[ A-Za-z0-9_@.#~`+$^%*()\\[\]&-=';,{\}!|":<>?]*$/g;
		isValid = regex.test($("#" + event.target.id + "").val());
		if(!isValid){
			$("#" + event.target.id + "").next().html('');
			$("#" + event.target.id + "").after('<span class="error_empty" style="color:red;"  id=span'+event.target.id+'"><br/>This special char not allowed</span>');
		}else{
			$("#" + event.target.id + "").next().html('');
		}
	});
	$(".allowSpecificspecialchars").keyup(function(){
//		&-"
		var isValid = false;
		var regex = /^[ A-Za-z0-9&-]*$/g;
		isValid = regex.test($("#" + event.target.id + "").val());
		if(!isValid){
			$("#" + event.target.id + "").next().html('');
			$("#" + event.target.id + "").after('<span class="error_empty" style="color:red;"  id=span'+event.target.id+'"><br/>Special charters [&,-] only allowed </span>');
		}else{
			$("#" + event.target.id + "").next().html('');
		}
	});
	$(".isEmpty").blur(function(event) {  
//		var txt = $('#txtName');  
		if ($("#" + event.target.id + "").val() != null && $("#" + event.target.id + "").val() != '') { 
			$("#" + event.target.id + "").next().html('');
		} else {  
			$("#" + event.target.id + "").next().html('');
			$("#" + event.target.id + "").after('<span class="error_empty " style="color:red;"  id=span'+event.target.id+'"><br/>Should not be empty</span>');

		}  
	});

	$('.validateSplChar').blur(function()  {
		var yourInput = $(this).val();
		re = /[`~!@#$%^&*()_|+\-=?;:'",.<>\{\}\[\]\\\/]/gi;
		var isSplChar = re.test(yourInput);
		if(isSplChar)
		{
			$("#" + event.target.id + "").next().html('');
			$("#" + event.target.id + "").after('<span class="error_empty" style="color:red;"  id=span'+event.target.id+'"><br/>Special Characters are not allowed</span>');
		}else{
			$("#" + event.target.id + "").next().html('');
		}
	});
	$('.trim').blur(function () {                     
		$(this).val(
				$.trim($(this).val())
		);
	});		
	$('.lTrim').blur(function(){
//		$(this).replace(/|\s+$/gm,'');
		var val =$(this).val().replace(/^\s+|$/gm,'');
//		var val =$(this).val().replace(/^\s+|\s+$/gm,'');		
		$(this).val(val);
	});

	$('.rTrim').blur(function(){
//		$(this).replace(/|\s+$/gm,'');
		var val =$(this).val().replace(/\s+$/gm,'');
		$(this).val(val);
	});
	
	 var max = 255; 
	 $('.max').keypress(function(e) {var count =0
	       /* if (e.which < 0x20) {
	           // e.which < 0x20, then it's not a printable character
	            // e.which === 0 - Not a character
	            return;     // Do nothing
	           
               
	        	}*/
	       
	        if (this.value.replace(/\n/g, "\n\r").length == max) {
	            e.preventDefault();
	        } else if (this.value.replace(/\n/g, "\n\r").length> max) {
	            // Maximum exceeded
	            this.value = this.value.substring(0, max);
	        }
	    });

     
	$('.datelogic').blur(function(event){
		var dtVal = $(this).val();
		if(isDate(dtVal))
		{
			$("#" + event.target.id + "").next().html('');
		}else{
			$("#" + event.target.id + "").next().html('');
			$("#" + event.target.id + "").after('<span class="error_empty" style="color:red;"  id=span'+event.target.id+'"><br/>Invalid Date.(dd/mm/yyyy or dd-mm-yyyy)</span>');
		}
	});  
	
	$("#tableView").DataTable({
		"deferRender" : true,
		"paging" : true,
		"lengthChange" : false,
		"searching" : true,
		"ordering" : true,
		"info" : true,
		"autoWidth" : false,
		"sDom" : 'lfrtip',
		"bLengthChange": true,
		"lengthMenu": [ 10, 25, 50, 75, 100 ],
		//"pageLength": 10,
		"scrollY":  "340px",
		"pagingType": "full_numbers",
		"scrollCollapse": true,
		"oLanguage": {
			"sEmptyTable":" No Records Available"
		
		}
	});

});

function isAlphaNumericWithAllSpecialChars(ctrl) {
 	
	var isValid;
 	var regex = /^[a-zA-Z0-9]+[a-zA-Z0-9 \\!@:;#$%^&*,.( )\+\-_=\/{}[]\|'?\>\<~`]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 \\!@:;#$%^&*,.( )\+\-_=\/{}[]\|'?\>\<~`]*/g, '');
	if ((ctrl.value.charAt(0) == ' '))
		ctrl.value = ctrl.value.replace(/\s+/, '');
	return isValid;
}

/*To validate AlphaNumeric and spacial value, it replaces the characters other than alphabets,space,special char and numbers by ''*/
function isAlphaNumericWithSpecialChars(ctrl) {
	 	
		var isValid;
	 	var regex = /^[a-zA-Z0-9]+[a-zA-Z0-9 ,.;'_\-]*$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ,.;'_\-]*/g, '');
		if ((ctrl.value.charAt(0) == ' '))
			ctrl.value = ctrl.value.replace(/\s+/, '');
		return isValid;
}
/*using char code:To validate AlphaNumeric and space value, it replaces the characters other than alphabets,spaces and numbers by ''   */
function isAlphaNumericWithSpecialCharsWithCharCode(e){
	 var charCode;
    if (e.keyCode > 0) {
        charCode = e.which || e.keyCode;
    }
    else if (typeof (e.charCode) != "undefined") {
        charCode = e.which || e.keyCode;
    }
	 //space,comma,hiphen,dot,single quote,semicolon,underscore
    if (charCode == 32 || charCode == 44 || charCode == 45 || charCode == 46 || charCode == 39 || charCode == 59 || charCode == 95 )
        return true;
	 //numbers
    if (charCode >= 48 && charCode <= 57)
        return true;
    //for lowercase
    if(charCode >= 97 && charCode <= 122)	 
      return true;
	if(charCode >= 65 && charCode <= 90)	 
      return true; 
	return false;   
}

/*To validate AlphaNumeric and space value, it replaces the characters other than alphabets,spaces and numbers by ''*/
function isAlphaNumericWithSpace(ctrl) {
	 	var isValid;
	 	var regex = /^[a-zA-Z0-9]+[a-zA-Z0-9 ]*$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9 ]*/g, '');
		if((ctrl.value.startsWith(' '))||(ctrl.value.endsWith(' ')))
			ctrl.value =ctrl.value.replace(/\s+/,'');
		return isValid;
	}

function isAlphabetsWithSpaceAndUnderScore(ctrl) {
 	var isValid;
 	var regex = /^[a-zA-Z _]*$/;
	isValid = regex.test($("#" + ctrl.id + "").val());
	ctrl.value = ctrl.value.replace(/[^a-zA-Z _]*/g, '');
	if(ctrl.value.startsWith(' '))
		ctrl.value =ctrl.value.replace(/\s+/,'');
	return isValid;
}

/*To validate AlphaNumeric value, it replaces the characters other than alphabets and numbers by ''*/
 function isAlphaNumeric(ctrl) {
		var isValid;
	 	var regex = /^[a-zA-Z0-9]*$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^a-zA-Z0-9]/g, '');
		if((ctrl.value.startsWith(' '))||(ctrl.value.endsWith(' ')))
			ctrl.value =ctrl.value.replace(/\s+/,'');
		return isValid;
	}
 
 function isNumeric(ctrl) {
		var isValid;
	 	var regex = /^[0-9]*$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^0-9]/g, '');
		if((ctrl.value.startsWith(' '))||(ctrl.value.endsWith(' ')))
			ctrl.value =ctrl.value.replace(/\s+/,'');
		return isValid;
	}
 /*To validate AlphaNumeric value, it replaces the characters other than Hexa Decimal numbers ''*/ 
 
 function isHexaDecimal(ctrl) {
		var isValid;
	 	var regex = /^[0-9a-fA-F]+$/;
		isValid = regex.test($("#" + ctrl.id + "").val());
		ctrl.value = ctrl.value.replace(/[^0-9a-fA-F]/g, '');
		if((ctrl.value.startsWith(' '))||(ctrl.value.endsWith(' ')))
			ctrl.value =ctrl.value.replace(/\s+/,'');
		return isValid;
	}

/*To clear common error */
 function clearError(ctrl){
		$("#" + ctrl + "").next().html('');
	}

 /*To display error messages for client side validation*/
function generateAlert(formId, eleId,key){
	 
	var text=readMessage(key);
	clearError(formId+" #"+eleId);
	$("#"+formId+" #"+eleId).after('<span class="error_empty" style="color:red;"><br/>' + text + '</span>');
 }
 
/*To clear error messages of client side validation
*/ function clearFieldError(formId, eleId){
	 clearError(formId+" #"+eleId);
 }

//Common client side validation
function validateFields(formId,eleId){
	 
	 var isValid=false;
	 var minLen=readValidationProp(eleId+'.min.length');
	 var eleVal=$("#"+formId+" #"+eleId).val();
	 clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
	 if((eleVal==null ||eleVal=='')  && minLen!=0){ 
		generateAlert(formId, eleId,eleId+".empty");
		return isValid;
	 }	 
		 if((eleVal.trim().length<minLen) || ( eleVal.trim().length>readValidationProp(eleId+'.max.length'))){
			 generateAlert(formId, eleId,eleId+".length");
		 }
		 else if((eleVal!=null && eleVal!='') && !pattern.test(eleVal)){ 
			 generateAlert(formId, eleId,eleId+".pattern");
		 }
		 else
		 {
			 clearError(formId+" #"+eleId);
			 
			 isValid=true;
		 }
	
	return isValid;
	 
}


function validateEmailAddress(formId,eleId){	 
	 var isValid=false;	
	 var eleVal=$("#"+formId+" #"+eleId).val();
	// clearError(formId+" #"+eleId);
	 var pattern=new RegExp(readValidationProp(eleId+'.pattern'));
	 
		if(pattern.test(eleVal)|| eleVal==null || eleVal==""){
		clearError(formId+" #"+eleId);
		}
		 else if(!pattern.test(eleVal)){ 
			 generateAlert(formId, eleId,eleId+".pattern");
		 }
		 else
		 {
			 clearError(formId+" #"+eleId);
			 
			 isValid=true;
		 }
	
	
	return isValid;
	 
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

/* Add the CSRF token in the header for all AJAX requests */
$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});
