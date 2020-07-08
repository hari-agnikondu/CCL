function clickAddMerchantRedemptionDelay(){
	
	
	
		$("#frmredemption").attr('action', 'showAddMerchantRedemptionDelayConfig');
		$("#frmredemption").submit();

}
function goAddRedemptionMerchant(formId){
	var validId = validateFields(formId, "merchantRedeemId");
	var validName = validateFields(formId, "merchantRedeemName");
	
	if (validId && validName) {
	
	$("#frmredemption").attr('action', 'addRedemptionMerchant');
	$("#frmredemption").submit();
	
	}
}
 function addRow(formId)
 
 {  var productData=$('#productRedeemId').val();
	var merhchantData=$('#merchantDelayId').val();

	
	if(productData == "-1")
	{
	generateAlert(formId, "productRedeemId","productRedeemId.unselect");
	retflag=false;
	}
	
	
	if(merhchantData == "-1")
	{
	generateAlert(formId, "merchantDelayId","merchantDelayId.unselect");
	retflag=false;
	}
	
	 if(validationTime()){ 
		 var totalRow ;
		 
		 totalRow = dataTable.rows.length;
			totalRow = parseInt(totalRow);
		    var newRow,newSelect,newSelect1,newtext;
		   
		  
		    if(count<10){
		    	
		  var sec=' <OPTION value = 0-1> 0-1 </OPTION> <OPTION value = 00> 00 </OPTION> <OPTION value = 01> 01 </OPTION> <OPTION value = 02> 02 </OPTION> <OPTION value = 03> 03 </OPTION> <OPTION value = 04> 04 </OPTION> <OPTION value = 05> 05 </OPTION> <OPTION value = 06> 06 </OPTION> <OPTION value = 07> 07 </OPTION> <OPTION value = 08> 08 </OPTION> <OPTION value = 09> 09 </OPTION> <OPTION value = 10> 10 </OPTION> <OPTION value = 11> 11 </OPTION> <OPTION value = 12> 12 </OPTION> <OPTION value = 13> 13 </OPTION> <OPTION value = 14> 14 </OPTION> <OPTION value = 15> 15 </OPTION> <OPTION value = 16> 16 </OPTION> <OPTION value = 17> 17 </OPTION> <OPTION value = 18> 18 </OPTION> <OPTION value = 19> 19 </OPTION> <OPTION value = 20> 20 </OPTION> <OPTION value = 21> 21 </OPTION> <OPTION value = 22> 22 </OPTION> <OPTION value = 23> 23 </OPTION> <OPTION value = 24> 24 </OPTION> <OPTION value = 25> 25 </OPTION> <OPTION value = 26> 26 </OPTION> <OPTION value = 27> 27 </OPTION> <OPTION value = 28> 28 </OPTION> <OPTION value = 29> 29 </OPTION> <OPTION value = 30> 30 </OPTION> <OPTION value = 31> 31 </OPTION> <OPTION value = 32> 32 </OPTION> <OPTION value = 33> 33 </OPTION> <OPTION value = 34> 34 </OPTION> <OPTION value = 35> 35 </OPTION> <OPTION value = 36> 36 </OPTION> <OPTION value = 37> 37 </OPTION> <OPTION value = 38> 38 </OPTION> <OPTION value = 39> 39 </OPTION> <OPTION value = 40> 40 </OPTION> <OPTION value = 41> 41 </OPTION> <OPTION value = 42> 42 </OPTION> <OPTION value = 43> 43 </OPTION> <OPTION value = 44> 44 </OPTION> <OPTION value = 45> 45 </OPTION> <OPTION value = 46> 46 </OPTION> <OPTION value = 47> 47 </OPTION> <OPTION value = 48> 48 </OPTION> <OPTION value = 49> 49 </OPTION> <OPTION value = 50> 50 </OPTION> <OPTION value = 51> 51 </OPTION> <OPTION value = 52> 52 </OPTION> <OPTION value = 53> 53 </OPTION> <OPTION value = 54> 54 </OPTION> <OPTION value = 55> 55 </OPTION> <OPTION value = 56> 56 </OPTION> <OPTION value = 57> 57 </OPTION> <OPTION value = 58> 58 </OPTION> <OPTION value = 59> 59 </OPTION>';
          var hrs=' <OPTION value = 0-1> 0-1 </OPTION> <OPTION value = 00> 00 </OPTION> <OPTION value = 01> 01 </OPTION> <OPTION value = 02> 02 </OPTION> <OPTION value = 03> 03 </OPTION> <OPTION value = 04> 04 </OPTION> <OPTION value = 05> 05 </OPTION> <OPTION value = 06> 06 </OPTION> <OPTION value = 07> 07 </OPTION> <OPTION value = 08> 08 </OPTION> <OPTION value = 09> 09 </OPTION> <OPTION value = 10> 10 </OPTION> <OPTION value = 11> 11 </OPTION> <OPTION value = 12> 12 </OPTION> <OPTION value = 13> 13 </OPTION> <OPTION value = 14> 14 </OPTION> <OPTION value = 15> 15 </OPTION> <OPTION value = 16> 16 </OPTION> <OPTION value = 17> 17 </OPTION> <OPTION value = 18> 18 </OPTION> <OPTION value = 19> 19 </OPTION> <OPTION value = 20> 20 </OPTION> <OPTION value = 21> 21 </OPTION> <OPTION value = 22> 22 </OPTION> <OPTION value = 23> 23 </OPTION>';
          var min=' <OPTION value = 0-1> 0-1 </OPTION> <OPTION value = 00> 00 </OPTION> <OPTION value = 01> 01 </OPTION> <OPTION value = 02> 02 </OPTION> <OPTION value = 03> 03 </OPTION> <OPTION value = 04> 04 </OPTION> <OPTION value = 05> 05 </OPTION> <OPTION value = 06> 06 </OPTION> <OPTION value = 07> 07 </OPTION> <OPTION value = 08> 08 </OPTION> <OPTION value = 09> 09 </OPTION> <OPTION value = 10> 10 </OPTION> <OPTION value = 11> 11 </OPTION> <OPTION value = 12> 12 </OPTION> <OPTION value = 13> 13 </OPTION> <OPTION value = 14> 14 </OPTION> <OPTION value = 15> 15 </OPTION> <OPTION value = 16> 16 </OPTION> <OPTION value = 17> 17 </OPTION> <OPTION value = 18> 18 </OPTION> <OPTION value = 19> 19 </OPTION> <OPTION value = 20> 20 </OPTION> <OPTION value = 21> 21 </OPTION> <OPTION value = 22> 22 </OPTION> <OPTION value = 23> 23 </OPTION> <OPTION value = 24> 24 </OPTION> <OPTION value = 25> 25 </OPTION> <OPTION value = 26> 26 </OPTION> <OPTION value = 27> 27 </OPTION> <OPTION value = 28> 28 </OPTION> <OPTION value = 29> 29 </OPTION> <OPTION value = 30> 30 </OPTION> <OPTION value = 31> 31 </OPTION> <OPTION value = 32> 32 </OPTION> <OPTION value = 33> 33 </OPTION> <OPTION value = 34> 34 </OPTION> <OPTION value = 35> 35 </OPTION> <OPTION value = 36> 36 </OPTION> <OPTION value = 37> 37 </OPTION> <OPTION value = 38> 38 </OPTION> <OPTION value = 39> 39 </OPTION> <OPTION value = 40> 40 </OPTION> <OPTION value = 41> 41 </OPTION> <OPTION value = 42> 42 </OPTION> <OPTION value = 43> 43 </OPTION> <OPTION value = 44> 44 </OPTION> <OPTION value = 45> 45 </OPTION> <OPTION value = 46> 46 </OPTION> <OPTION value = 47> 47 </OPTION> <OPTION value = 48> 48 </OPTION> <OPTION value = 49> 49 </OPTION> <OPTION value = 50> 50 </OPTION> <OPTION value = 51> 51 </OPTION> <OPTION value = 52> 52 </OPTION> <OPTION value = 53> 53 </OPTION> <OPTION value = 54> 54 </OPTION> <OPTION value = 55> 55 </OPTION> <OPTION value = 56> 56 </OPTION> <OPTION value = 57> 57 </OPTION> <OPTION value = 58> 58 </OPTION> <OPTION value = 59> 59 </OPTION>';
			 	
			 	newRow = dataTable.insertRow(totalRow);
				newcheck=newRow.insertCell();
				newSelect = newRow.insertCell();
				newSelect1 = newRow.insertCell();
				newtext = newRow.insertCell();
				newcheck.style.textAlign="center";
				newSelect.style.textAlign = "center";
				newSelect1.style.textAlign = "center";
				newtext.style.textAlign="center";
		    	
		newcheck.innerHTML='<input type="checkbox" class="chkMDelete" name="chkMDelete" id="chkMDelete">'
		newSelect.innerHTML ="<select class='StartselSHRS' name='StartselSHRS' style='max-width:20%'  id ='StartselSHRS' value="+hrs+"></select>" +
				":<select class='StartselSMIN' name='StartselSMIN' style='max-width:20%' id ='StartselSMIN' value="+min+">" +
				"</select>:<select class='StartselSSEC' name='StartselSSEC' style='max-width:20%' id ='StartselSSEC' value="+sec+" ></select>";
		newSelect1.innerHTML ='<select class="EndselSHRS" name="EndselSHRS" style="max-width:20%;" id ="EndselSHRS" value='+hrs+'></select>:<select class="EndselSMIN" name="EndselSMIN" style="max-width:20%" id ="EndselSMIN" value='+min+'></select>:<select class="EndselSSEC"  name="EndselSSEC" style="max-width:20%" id ="EndselSSEC" value='+sec+' ></select>';
		newtext.innerHTML='<input type="text" class="redemdelay" style="max-width:20%;" maxlength="5" name="redemdelay" onkeyup="return isNumeric(this)" id="redemdelay">'//newOpt.innerHTML ='<input type="button" name="view" id="view" value="View Rule" OnClick="viewRule(\''+operationsEle.trim()+'\');" class=submit>'; 
							 count=count+1;	
						}
						else
						{
							alert("Only 10 period window can be added");
						}
		 
	 }
	 
	 
 }
 
 
 function deleteRow()
 {
 	var i;
 	var flag=false;
 	var delcount = dataTable.rows.length;
 	if(delcount>1){
 		/*count= count-1;*/
 	 	for (i=(delcount-2);i>=0;i--)
 	{  
 		if( document.getElementsByName("chkMDelete")[i].checked)
       {  
 			flag = true;
       
       dataTable.deleteRow(i+1);     
              
       }
 	}
 	if(flag==false)
 		alert("No rows selected to delete");
 	}
 	else
 	{
 		alert("There is no rows to delete");
 	}
 }
 
 
 function validationTime()
 {
 	 var previousVal="";
 	 var currentVal="";
 	 var totalRow1 = parseInt(dataTable.rows.length);
 	if(totalRow1>1){
 		for(var i=(totalRow1-2);i>=0;i--){		 	  
 		  var startsel=(document.getElementsByName("StartselSHRS")[i].options[document.getElementsByName("StartselSHRS")[i].selectedIndex].value)+
 		 	  (document.getElementsByName("StartselSMIN")[i].options[document.getElementsByName("StartselSMIN")[i].selectedIndex].value)+
 		 	  (document.getElementsByName("StartselSSEC")[i].options[document.getElementsByName("StartselSSEC")[i].selectedIndex].value);
 		 	  
 		  var endsel=(document.getElementsByName("EndselSHRS")[i].options[document.getElementsByName("EndselSHRS")[i].selectedIndex].value)+
 		 	  (document.getElementsByName("EndselSMIN")[i].options[document.getElementsByName("EndselSMIN")[i].selectedIndex].value)+
 		 	  (document.getElementsByName("EndselSSEC")[i].options[document.getElementsByName("EndselSSEC")[i].selectedIndex].value);
 		  
 		  var redemmin=document.getElementsByName("redemdelay")[i].value;	 		
 			
 		  if(startsel==endsel && totalRow1>1)
 			{
 			  alert("Start time and End time should not be same");
 			  return false;	
 			}
 		  
 		  if(startsel>endsel)
 			{
 			  alert("Start time should not be greater than End time");
 			  return false;	
 			}
 		  if(redemmin==null||redemmin=='' && totalRow1>1)
 			{
 			alert('Please enter the redemption delay minutes');
 			return false;
 			}
 		  
 		  if(i==totalRow1-2)
 		  {
 			  currentVal=startsel+':'+endsel;
 		  }
 		  else
 		  {
 		  
 		  if(previousVal!="")
 		  {
 			  previousVal=previousVal+'|'+startsel+':'+endsel;
 		  }
 		  else
 		  {
 			  previousVal=startsel+':'+endsel;
 		  }
 		  }
 	
 	
 		if(previousVal!="")
 			{
 			 var Output= overlapDelay(previousVal,currentVal);
 	 
 	 	  if(Output.trim().toUpperCase()!='OK')
 	 	  {
 	 		  alert("Time slot should not be over lapped, please enter different time slot");
 	 		  return false;	  
 	 	  }
 	 	  return true; 
 			
 			
 			}
 		
 		} 
 		return true;
 }
 else
 	 return true;
 }
 
 function sendvalue(event,formId)
 {
	 if(event.preventDefault) event.preventDefault();
	 
	 
 	if (Validate(formId)){		
 	
 		 tab = dataTable.rows.length;
 		 
 		if(validationTime(formId)){
 		for(var i=(tab-2);i>=0;i--)
 		{ 
 	  var startsel=(document.getElementsByName("StartselSHRS")[i].options[document.getElementsByName("StartselSHRS")[i].selectedIndex].value)+
 	  (document.getElementsByName("StartselSMIN")[i].options[document.getElementsByName("StartselSMIN")[i].selectedIndex].value)+
 	  (document.getElementsByName("StartselSSEC")[i].options[document.getElementsByName("StartselSSEC")[i].selectedIndex].value);
 	  
 	  var endsel=(document.getElementsByName("EndselSHRS")[i].options[document.getElementsByName("EndselSHRS")[i].selectedIndex].value)+
 	  (document.getElementsByName("EndselSMIN")[i].options[document.getElementsByName("EndselSMIN")[i].selectedIndex].value)+
 	  (document.getElementsByName("EndselSSEC")[i].options[document.getElementsByName("EndselSSEC")[i].selectedIndex].value);
 	  
 		var redemmin=document.getElementsByName("redemdelay")[i].value; 
 		
 		rulesArray.push(startsel+"-"+endsel+"-"+redemmin);		
       }
 	document.getElementById("operationList").value=rulesArray;
 	
 	$("#operationList").val(rulesArray);
 	
 		$("#frmredemption").attr('action','updateRedemptionDelay');
 		
	$("#frmredemption").submit();
	
	
 	

		}
 	}
 }
 
 
 var operationList=new Array(); 
 var rulesArray=  new Array();
 var count=0;
 function Validate(formId)
 {
	 var productData=$('#productRedeemId').val();
		var merhchantData=$('#merchantDelayId').val();
	
		
		if(productData == "-1")
		{
		generateAlert(formId, "productRedeemId","productRedeemId.unselect");
		return false;
		}
		
		if(merhchantData == "-1")
		{
		generateAlert(formId, "merchantDelayId","merchantDelayId.unselect");
		return false;
		}
     if(dataTable.rows.length==1){
 		 alert("Please enter at least one value");
 		 return false;
 	}
     return true;
     
 }
 
function SubmitRedemptionDelay(data){
	
	
	
	$("#data").val(data);
	
	$("#frmredemption").attr('action','updateRedemptionDelay');
	$("#frmredemption").submit();
}


function overlapDelay(previousval,currentval){
	var servUrl=$('#addurl').val();
	var msg= null;
	var str = servUrl+"/ajax/getOverlapRedemptionDetails/"+previousval+"/"+currentval;

   $.ajax({

      
     url: str,

      async:false,

      type: "GET",

      dataType: 'json',

     
     success:function(response, status, xhr){ 
    	  $.each(response, function(key,value){     

    	msg= value;
  	    	  
  	      }); 
      },
      error : function(jqXHR, textStatus, errorThrown) {

		  console.log("error:" + textStatus + " exception:" + errorThrown);
	}

    });
  
   return  msg;
};

function ResetAddRedemptionMerchant(formId) {
$("#formErrorRedeemId").text('');
$("#" + formId + " #merchantRedeemName").val('');
$("#" + formId + " #merchantRedeemId").val('');

$("#formErrorRedeemId").html('');
$("#statusMerchantRedeemAdd").html('');
clearError(formId + " #merchantRedeemName");
clearError(formId + " #merchantRedeemId");

 
}
 
function goBackToRedemptionDelay(){
	
	

		$("#frmredemption").attr('action','redemptionDelayConfig');
		$("#frmredemption").submit();
	

}

function validateDropDownRedeemMerchantProduct(formId, eleId){
	
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
//var forminOnload="";


function getfillDelayTime(formId){
	count=0;
	
	//forminOnload=formId;
		var productRedeemId=$("#productRedeemId option:selected").val();
		if(productRedeemId == "-1")
		{
		generateAlert(formId, "productRedeemId","productRedeemId.unselect");
		retflag=false;
		}
		
		var productId = productRedeemId.split("~");
		
		var merchantDelayId=$("#merchantDelayId option:selected").val();
		
		var merchantId = merchantDelayId.split("~");
		
		var sec=' <OPTION value = 0-1> 0-1 </OPTION> <OPTION value = 00> 00 </OPTION> <OPTION value = 01> 01 </OPTION> <OPTION value = 02> 02 </OPTION> <OPTION value = 03> 03 </OPTION> <OPTION value = 04> 04 </OPTION> <OPTION value = 05> 05 </OPTION> <OPTION value = 06> 06 </OPTION> <OPTION value = 07> 07 </OPTION> <OPTION value = 08> 08 </OPTION> <OPTION value = 09> 09 </OPTION> <OPTION value = 10> 10 </OPTION> <OPTION value = 11> 11 </OPTION> <OPTION value = 12> 12 </OPTION> <OPTION value = 13> 13 </OPTION> <OPTION value = 14> 14 </OPTION> <OPTION value = 15> 15 </OPTION> <OPTION value = 16> 16 </OPTION> <OPTION value = 17> 17 </OPTION> <OPTION value = 18> 18 </OPTION> <OPTION value = 19> 19 </OPTION> <OPTION value = 20> 20 </OPTION> <OPTION value = 21> 21 </OPTION> <OPTION value = 22> 22 </OPTION> <OPTION value = 23> 23 </OPTION> <OPTION value = 24> 24 </OPTION> <OPTION value = 25> 25 </OPTION> <OPTION value = 26> 26 </OPTION> <OPTION value = 27> 27 </OPTION> <OPTION value = 28> 28 </OPTION> <OPTION value = 29> 29 </OPTION> <OPTION value = 30> 30 </OPTION> <OPTION value = 31> 31 </OPTION> <OPTION value = 32> 32 </OPTION> <OPTION value = 33> 33 </OPTION> <OPTION value = 34> 34 </OPTION> <OPTION value = 35> 35 </OPTION> <OPTION value = 36> 36 </OPTION> <OPTION value = 37> 37 </OPTION> <OPTION value = 38> 38 </OPTION> <OPTION value = 39> 39 </OPTION> <OPTION value = 40> 40 </OPTION> <OPTION value = 41> 41 </OPTION> <OPTION value = 42> 42 </OPTION> <OPTION value = 43> 43 </OPTION> <OPTION value = 44> 44 </OPTION> <OPTION value = 45> 45 </OPTION> <OPTION value = 46> 46 </OPTION> <OPTION value = 47> 47 </OPTION> <OPTION value = 48> 48 </OPTION> <OPTION value = 49> 49 </OPTION> <OPTION value = 50> 50 </OPTION> <OPTION value = 51> 51 </OPTION> <OPTION value = 52> 52 </OPTION> <OPTION value = 53> 53 </OPTION> <OPTION value = 54> 54 </OPTION> <OPTION value = 55> 55 </OPTION> <OPTION value = 56> 56 </OPTION> <OPTION value = 57> 57 </OPTION> <OPTION value = 58> 58 </OPTION> <OPTION value = 59> 59 </OPTION>';
        var hrs='<OPTION value =00>00</OPTION> <OPTION value = 01> 01 </OPTION> <OPTION value = 02> 02 </OPTION> <OPTION value = 03> 03 </OPTION> <OPTION value = 04> 04 </OPTION> <OPTION value =05>05</OPTION> <OPTION value = 06> 06 </OPTION> <OPTION value = 07> 07 </OPTION> <OPTION value = 08> 08 </OPTION> <OPTION value = 09> 09 </OPTION> <OPTION value = 10> 10 </OPTION> <OPTION value = 11> 11 </OPTION> <OPTION value = 12> 12 </OPTION> <OPTION value = 13> 13 </OPTION> <OPTION value = 14> 14 </OPTION> <OPTION value = 15> 15 </OPTION> <OPTION value = 16> 16 </OPTION> <OPTION value = 17> 17 </OPTION> <OPTION value = 18> 18 </OPTION> <OPTION value = 19> 19 </OPTION> <OPTION value = 20> 20 </OPTION> <OPTION value = 21> 21 </OPTION> <OPTION value = 22> 22 </OPTION> <OPTION value = 23> 23 </OPTION>';
        var min=' <OPTION value = 0-1> 0-1 </OPTION> <OPTION value = 00> 00 </OPTION> <OPTION value = 01> 01 </OPTION> <OPTION value = 02> 02 </OPTION> <OPTION value = 03> 03 </OPTION> <OPTION value = 04> 04 </OPTION> <OPTION value = 05> 05 </OPTION> <OPTION value = 06> 06 </OPTION> <OPTION value = 07> 07 </OPTION> <OPTION value = 08> 08 </OPTION> <OPTION value = 09> 09 </OPTION> <OPTION value = 10> 10 </OPTION> <OPTION value = 11> 11 </OPTION> <OPTION value = 12> 12 </OPTION> <OPTION value = 13> 13 </OPTION> <OPTION value = 14> 14 </OPTION> <OPTION value = 15> 15 </OPTION> <OPTION value = 16> 16 </OPTION> <OPTION value = 17> 17 </OPTION> <OPTION value = 18> 18 </OPTION> <OPTION value = 19> 19 </OPTION> <OPTION value = 20> 20 </OPTION> <OPTION value = 21> 21 </OPTION> <OPTION value = 22> 22 </OPTION> <OPTION value = 23> 23 </OPTION> <OPTION value = 24> 24 </OPTION> <OPTION value = 25> 25 </OPTION> <OPTION value = 26> 26 </OPTION> <OPTION value = 27> 27 </OPTION> <OPTION value = 28> 28 </OPTION> <OPTION value = 29> 29 </OPTION> <OPTION value = 30> 30 </OPTION> <OPTION value = 31> 31 </OPTION> <OPTION value = 32> 32 </OPTION> <OPTION value = 33> 33 </OPTION> <OPTION value = 34> 34 </OPTION> <OPTION value = 35> 35 </OPTION> <OPTION value = 36> 36 </OPTION> <OPTION value = 37> 37 </OPTION> <OPTION value = 38> 38 </OPTION> <OPTION value = 39> 39 </OPTION> <OPTION value = 40> 40 </OPTION> <OPTION value = 41> 41 </OPTION> <OPTION value = 42> 42 </OPTION> <OPTION value = 43> 43 </OPTION> <OPTION value = 44> 44 </OPTION> <OPTION value = 45> 45 </OPTION> <OPTION value = 46> 46 </OPTION> <OPTION value = 47> 47 </OPTION> <OPTION value = 48> 48 </OPTION> <OPTION value = 49> 49 </OPTION> <OPTION value = 50> 50 </OPTION> <OPTION value = 51> 51 </OPTION> <OPTION value = 52> 52 </OPTION> <OPTION value = 53> 53 </OPTION> <OPTION value = 54> 54 </OPTION> <OPTION value = 55> 55 </OPTION> <OPTION value = 56> 56 </OPTION> <OPTION value = 57> 57 </OPTION> <OPTION value = 58> 58 </OPTION> <OPTION value = 59> 59 </OPTION>';
       
		var surl ="http://localhost:8080/cclp-vms";
		var res = $('#addurl').val()+"/ajax/getMerchantProductRedemption/"+productId[0]+"/"+merchantId[0];
		var sDate=null;
		var eDate=null;
		var lp= null;
	
		 $.ajax({

			     url: res,

			      async:true,

			      type: "GET",

			      dataType: 'json',

			     
			     success:function(response, status, xhr){ 
			    	 
			    	  $.each(response, function(index){ 
			 
			    			 var totalRow ;
			    			 
			    			 totalRow = dataTable.rows.length;
			    				totalRow = parseInt(totalRow);
			    			    var newRow,newSelect,newSelect1,newtext;
			    			   
			    			
			    			    	
			    			  var sec=' <OPTION value = 0-1> 0-1 </OPTION> <OPTION value = 00> 00 </OPTION> <OPTION value = 01> 01 </OPTION> <OPTION value = 02> 02 </OPTION> <OPTION value = 03> 03 </OPTION> <OPTION value = 04> 04 </OPTION> <OPTION value = 05> 05 </OPTION> <OPTION value = 06> 06 </OPTION> <OPTION value = 07> 07 </OPTION> <OPTION value = 08> 08 </OPTION> <OPTION value = 09> 09 </OPTION> <OPTION value = 10> 10 </OPTION> <OPTION value = 11> 11 </OPTION> <OPTION value = 12> 12 </OPTION> <OPTION value = 13> 13 </OPTION> <OPTION value = 14> 14 </OPTION> <OPTION value = 15> 15 </OPTION> <OPTION value = 16> 16 </OPTION> <OPTION value = 17> 17 </OPTION> <OPTION value = 18> 18 </OPTION> <OPTION value = 19> 19 </OPTION> <OPTION value = 20> 20 </OPTION> <OPTION value = 21> 21 </OPTION> <OPTION value = 22> 22 </OPTION> <OPTION value = 23> 23 </OPTION> <OPTION value = 24> 24 </OPTION> <OPTION value = 25> 25 </OPTION> <OPTION value = 26> 26 </OPTION> <OPTION value = 27> 27 </OPTION> <OPTION value = 28> 28 </OPTION> <OPTION value = 29> 29 </OPTION> <OPTION value = 30> 30 </OPTION> <OPTION value = 31> 31 </OPTION> <OPTION value = 32> 32 </OPTION> <OPTION value = 33> 33 </OPTION> <OPTION value = 34> 34 </OPTION> <OPTION value = 35> 35 </OPTION> <OPTION value = 36> 36 </OPTION> <OPTION value = 37> 37 </OPTION> <OPTION value = 38> 38 </OPTION> <OPTION value = 39> 39 </OPTION> <OPTION value = 40> 40 </OPTION> <OPTION value = 41> 41 </OPTION> <OPTION value = 42> 42 </OPTION> <OPTION value = 43> 43 </OPTION> <OPTION value = 44> 44 </OPTION> <OPTION value = 45> 45 </OPTION> <OPTION value = 46> 46 </OPTION> <OPTION value = 47> 47 </OPTION> <OPTION value = 48> 48 </OPTION> <OPTION value = 49> 49 </OPTION> <OPTION value = 50> 50 </OPTION> <OPTION value = 51> 51 </OPTION> <OPTION value = 52> 52 </OPTION> <OPTION value = 53> 53 </OPTION> <OPTION value = 54> 54 </OPTION> <OPTION value = 55> 55 </OPTION> <OPTION value = 56> 56 </OPTION> <OPTION value = 57> 57 </OPTION> <OPTION value = 58> 58 </OPTION> <OPTION value = 59> 59 </OPTION>';
			    	          var hrs=' <OPTION value = 0-1> 0-1 </OPTION> <OPTION value = 00> 00 </OPTION> <OPTION value = 01> 01 </OPTION> <OPTION value = 02> 02 </OPTION> <OPTION value = 03> 03 </OPTION> <OPTION value = 04> 04 </OPTION> <OPTION value = 05> 05 </OPTION> <OPTION value = 06> 06 </OPTION> <OPTION value = 07> 07 </OPTION> <OPTION value = 08> 08 </OPTION> <OPTION value = 09> 09 </OPTION> <OPTION value = 10> 10 </OPTION> <OPTION value = 11> 11 </OPTION> <OPTION value = 12> 12 </OPTION> <OPTION value = 13> 13 </OPTION> <OPTION value = 14> 14 </OPTION> <OPTION value = 15> 15 </OPTION> <OPTION value = 16> 16 </OPTION> <OPTION value = 17> 17 </OPTION> <OPTION value = 18> 18 </OPTION> <OPTION value = 19> 19 </OPTION> <OPTION value = 20> 20 </OPTION> <OPTION value = 21> 21 </OPTION> <OPTION value = 22> 22 </OPTION> <OPTION value = 23> 23 </OPTION>';
			    	          var min=' <OPTION value = 0-1> 0-1 </OPTION> <OPTION value = 00> 00 </OPTION> <OPTION value = 01> 01 </OPTION> <OPTION value = 02> 02 </OPTION> <OPTION value = 03> 03 </OPTION> <OPTION value = 04> 04 </OPTION> <OPTION value = 05> 05 </OPTION> <OPTION value = 06> 06 </OPTION> <OPTION value = 07> 07 </OPTION> <OPTION value = 08> 08 </OPTION> <OPTION value = 09> 09 </OPTION> <OPTION value = 10> 10 </OPTION> <OPTION value = 11> 11 </OPTION> <OPTION value = 12> 12 </OPTION> <OPTION value = 13> 13 </OPTION> <OPTION value = 14> 14 </OPTION> <OPTION value = 15> 15 </OPTION> <OPTION value = 16> 16 </OPTION> <OPTION value = 17> 17 </OPTION> <OPTION value = 18> 18 </OPTION> <OPTION value = 19> 19 </OPTION> <OPTION value = 20> 20 </OPTION> <OPTION value = 21> 21 </OPTION> <OPTION value = 22> 22 </OPTION> <OPTION value = 23> 23 </OPTION> <OPTION value = 24> 24 </OPTION> <OPTION value = 25> 25 </OPTION> <OPTION value = 26> 26 </OPTION> <OPTION value = 27> 27 </OPTION> <OPTION value = 28> 28 </OPTION> <OPTION value = 29> 29 </OPTION> <OPTION value = 30> 30 </OPTION> <OPTION value = 31> 31 </OPTION> <OPTION value = 32> 32 </OPTION> <OPTION value = 33> 33 </OPTION> <OPTION value = 34> 34 </OPTION> <OPTION value = 35> 35 </OPTION> <OPTION value = 36> 36 </OPTION> <OPTION value = 37> 37 </OPTION> <OPTION value = 38> 38 </OPTION> <OPTION value = 39> 39 </OPTION> <OPTION value = 40> 40 </OPTION> <OPTION value = 41> 41 </OPTION> <OPTION value = 42> 42 </OPTION> <OPTION value = 43> 43 </OPTION> <OPTION value = 44> 44 </OPTION> <OPTION value = 45> 45 </OPTION> <OPTION value = 46> 46 </OPTION> <OPTION value = 47> 47 </OPTION> <OPTION value = 48> 48 </OPTION> <OPTION value = 49> 49 </OPTION> <OPTION value = 50> 50 </OPTION> <OPTION value = 51> 51 </OPTION> <OPTION value = 52> 52 </OPTION> <OPTION value = 53> 53 </OPTION> <OPTION value = 54> 54 </OPTION> <OPTION value = 55> 55 </OPTION> <OPTION value = 56> 56 </OPTION> <OPTION value = 57> 57 </OPTION> <OPTION value = 58> 58 </OPTION> <OPTION value = 59> 59 </OPTION>';
			    				 	
			    				 	newRow = dataTable.insertRow(totalRow);
			    					newcheck=newRow.insertCell();
			    					newSelect = newRow.insertCell();
			    					newSelect1 = newRow.insertCell();
			    					newtext = newRow.insertCell();
			    					newcheck.style.textAlign="center";
			    					newSelect.style.textAlign = "center";
			    					newSelect1.style.textAlign = "center";
			    					newtext.style.textAlign="center";
			    			
			    						newcheck.innerHTML='<input type="checkbox" class="chkMDelete"  name="chkMDelete" id="chkMDelete">'
			    						newSelect.innerHTML ='<select class="StartselSHRS" name="StartselSHRS" style="max-width:20%;" id=StartselSHRS'+index+' value='+hrs+'></select>:' +
			    								'<select class="StartselSMIN" name="StartselSMIN" style="max-width:20%;" id =StartselSMIN'+index+' value='+min+'>' +
			    								'</select>:<select class="StartselSSEC" name="StartselSSEC" style="max-width:20%;" id =StartselSSEC'+index+' value='+sec+' ></select>';
			    						newSelect1.innerHTML ='<select class="EndselSHRS" name="EndselSHRS" style="max-width:20%;" id =EndselSHRS'+index+' value='+hrs+'></select>:' +
			    							'<select class="EndselSMIN" name="EndselSMIN" style="max-width:20%;" id =EndselSMIN'+index+' value='+min+'>' +
			    							'</select>:<select class="EndselSSEC"  name="EndselSSEC" style="max-width:20%;" id =EndselSSEC'+index+' value='+sec+' ></select>';
			    						newtext.innerHTML='<input type="text" class="redemdelay" style="max-width:20%;" onkeyup="return isNumeric(this)" maxlength="5" name="redemdelay" id=redemdelay value='+response[index].redemptionDelayTime+'>'
			    								 count=count+1;	
			    						
			    						$("#StartselSHRS"+index+" > [value="+response[index].startTimeDisplay.substr(0,2)+"]").attr("selected", "true");
			    						$("#StartselSMIN"+index+" > [value="+response[index].startTimeDisplay.substr(2,2)+"]").attr("selected", "true");
			    						$("#StartselSSEC"+index+" > [value="+response[index].startTimeDisplay.substr(4,2)+"]").attr("selected", "true");
			    						$("#EndselSHRS"+index+" > [value="+response[index].endTimeDisplay.substr(0,2)+"]").attr("selected", "true");
			    						$("#EndselSMIN"+index+" > [value="+response[index].endTimeDisplay.substr(2,2)+"]").attr("selected", "true");
			    						$("#EndselSSEC"+index+" > [value="+response[index].endTimeDisplay.substr(4,2)+"]").attr("selected", "true");
			    						

			          }); 
			 
			      },
			      error : function(jqXHR, textStatus, errorThrown) {

					  console.log("error:" + textStatus + " exception:" + errorThrown);
				}


			    });
		
		 cleargrid();
		
			} 
			
function cleargrid()
{
	if(dataTable.rows.length > 1)
  		
	 	while(dataTable.rows.length > 1) {
	 		dataTable.deleteRow(1);
  		}
} 

function onloadRedeemDiv(){
	var id=document.getElementById(frmredemption);
	getfillDelayTime(id);
	
	
}