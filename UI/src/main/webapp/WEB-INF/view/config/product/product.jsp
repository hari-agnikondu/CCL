tc<!-- saved from url=(0014)about:internet -->
<!DOCTYPE html>
<html lang="en">
  <head>


  <script type="text/javascript">
  
  function includeRespDiv(){
  alert(document.getElementById('denominationId').value);
  
  var fixedDiv = document.getElementById("fixedDiv");
  var selectDiv = document.getElementById("selectDiv");
  var variableDiv = document.getElementById("variableDiv");
   
  if(document.getElementById('denominationId').value=='Fixed'){
   fixedDiv.style.display = "block";
   selectDiv.style.display = "none";
   variableDiv.style.display = "none";
  }
  else if(document.getElementById('denominationId').value=='Select'){
	 fixedDiv.style.display = "none";
     selectDiv.style.display = "block";
     variableDiv.style.display = "none";
  }
  else if(document.getElementById('denominationId').value=='Variable'){
	 fixedDiv.style.display = "none";
     selectDiv.style.display = "none";
     variableDiv.style.display = "block";
  }
  }//includeRespDiv
  /*
var tabs = ["KYCRegistration", "domainConfiguration", "transactionControl"];
 $('.nav-tabs a[href="#' + tabs[1] + '"]').css("cursor","no-drop");
    $('.nav-tabs a[href="#' + tabs[2] + '"]').css("cursor", "no-drop");*/
/*
function chk_control(str) {

if(str=='dsb'){document.getElementById('ck1').disabled=true;}

else {document.getElementById('ck1').disabled=false;}

}*/
var copyvar="";
function chk_control(str) {
//alert("chk_control");
var id_value="input_"+str;
////alert("chk_control"+id_value);
//alert("check"+id_value);
//alert("finish"+document.getElementById(id_value).readOnly);
document.getElementById(id_value).readOnly=false;
//alert("finish"+document.getElementById(id_value).readOnly);
}
function limitsJSON(myObj,inputreq,inputmode){
var myObj,i, j, x = "";
var myObj=myObj;
var mode="";
var ischeck="";
if(inputmode=="0"){
mode="readonly"
} else {
ischeck="checked";
}
var test1="'";
var k=0;
for (i in myObj.DELV) {
     x+="<tr>";
    x += "<td>" + myObj.DELV[i].name + "</td>";
    for (j in myObj.DELV[i].limits) {
	k++;
	var comval=inputreq+k;
		       x += "<td>"+"<input type='text' id='input_"+inputreq+k+"' value="+myObj.DELV[i].limits[j] +" size='3' "+mode+"></td><td><input type='checkbox' id='tran_"+j+"' onclick=chk_control('"+comval+"') "+ischeck+">";
    }
    x+="</tr>"
    //alert("x"+x);
}
//alert("Before "+inputreq);
if(inputreq=="SPIL"){
//alert("CHW"+inputreq);
document.getElementById("SPIL").innerHTML = x;
}
if(inputreq=="CHW"){
//alert("CHW"+inputreq);
document.getElementById("CHW").innerHTML = x;
}
if(inputreq=="MOB"){
//alert("CHW"+inputreq);
document.getElementById("MOB").innerHTML = x;
}
if(inputreq=="CSR"){
document.getElementById("CSR").innerHTML = x;
}
if(inputreq=="IVR"){
document.getElementById("IVR").innerHTML = x;
}
if(inputreq=="HOST"){
document.getElementById("HOST").innerHTML = x;
}else{
//alert("test");
document.getElementById("SPIL").innerHTML = x;
}

x="";
}
//valueswithFunction
function testonLoad(inpreq){

var myObj, i, j, x = "";


if(inpreq=="SPIL" || inpreq=="on"){
//alert("SPIL"+inpreq);
myObj = {
    "DELV": [
        { "name":"Value_Insertion", "limits":[ "0", "0", "0","0","0","0","0","0","0","0"  ] },
        { "name":"Activation", "limits":[ "23", "25", "26","27","28","29","30","40","50","10" ] },
       
    ]
}
}
if(inpreq=="SPIL"){
myObj = {
    "DELV": [
        { "name":"Value_Insertion", "limits":[ "23", "25", "26","27","28","29","30","40","50","10"  ] },
        { "name":"Activation", "limits":[ "23", "25", "26","27","28","29","30","40","50","10" ] },
       
    ]
}
}
if(inpreq=="CHW"){
myObj = {
    "DELV": [
        { "name":"Card Top up", "limits":[ "23", "25", "26","27","28","29","30","40","50","10"  ] },
        { "name":"Card to Card Transfer Inward", "limits":[ "23", "25", "26","27","28","29","30","40","50","10" ] },
		 { "name":"Card to card transfer Outward", "limits":[ "23", "25", "26","27","28","29","30","40","50","10" ] },
       
    ]
}
}
if(inpreq=="IVR"){
myObj = {
    "DELV": [
	{ "name":"Card Top up", "limits":[ "23", "25", "26","27","28","29","30","40","50","10"  ] },
       { "name":"Card to Card Transfer Inward", "limits":[ "23", "25", "26","27","28","29","30","40","50","10" ] },
		 { "name":"Card to card transfer Outward", "limits":[ "23", "25", "26","27","28","29","30","40","50","10" ] },
       
       
    ]
}
}
if(inpreq=="MOB"){
myObj = {
    "DELV": [
        { "name":"Card to Card Transfer Inward", "limits":[ "23", "25", "26","27","28","29","30","40","50","10" ] },
		 { "name":"Card to card transfer Outward", "limits":[ "23", "25", "26","27","28","29","30","40","50","10" ] },
       
       
    ]
}

}
if(inpreq=="CSR"){
myObj = {
    "DELV": [
        { "name":"Card to Card Transfer Inward", "limits":[ "23", "25", "26","27","28","29","30","40","50","10" ] },
		 { "name":"Card to card transfer Outward", "limits":[ "23", "25", "26","27","28","29","30","40","50","10" ] },
       
       
    ]
}
}
if(inpreq=="HOST"){
myObj = {
    "DELV": [
        { "name":"Batch file Upload - Card Unload", "limits":[ "23", "25", "26","27","28","29","30","40","50","10"  ] },
        { "name":"Batch file Upload - Card load", "limits":[ "23", "25", "26","27","28","29","30","40","50","10" ] },
       
    ]
}
}

//alert('testonLoad'+inpreq);
limitsJSON(myObj,inpreq,"0");
	
}
var copyvar="";

function funcCopy(){
	
	var val=document.getElementById("copytxt").value;
	//alert("val"+val+"copy "+val.length);
	if(val=="" || val.length==0){
	//alert("Please enter product");
	return false;
	}
	
	
	copyvar="copy";
	
	
	//alert('test'+copyvar);
	var myObj, i, j, x = "";


		
		//alert("SPIL"+inpreq);
			myObj = {
			"DELV": [
						{ "name":"Value_Insertion", "limits":[ "2", "3", "4","5","6","7","8","9","10","11"  ] },
					{ "name":"Activation", "limits":[ "23", "25", "26","27","28","29","30","40","50","10" ] },
       
				]
			}	
		
		var inpreq="SPIL";
		//testonLoad(inpreq);
		limitsJSON(myObj,"SPIL","0");
		
}
function checkCopy(inpreq){
//alert('test1');
	if(copyvar=="copy"){
	
	testonLoad(inpreq);

	}else{
	//alert("Default"+inpreq);
		defaultFunvalues(inpreq);
	}
	
}


function defaultFunvalues(inpreq){
//alert("SPIL"+inpreq);
if(inpreq=="SPIL" || inpreq=="on"){
//alert("SPIL2"+inpreq);
myObj = {
    "DELV": [
        { "name":"Value_Insertion", "limits":[ "0", "0", "0","0","0","0","0","0","0","0"  ] },
        { "name":"Activation", "limits":[ "0", "0", "0","0","0","0","0","0","0","0" ] },
       
    ]
}
}
if(inpreq=="CHW"){
myObj = {
    "DELV": [
        { "name":"Card Top up", "limits":[  "0", "0", "0","0","0","0","0","0","0","0"    ] },
        { "name":"Card to Card Transfer Inward", "limits":[ "0", "0", "0","0","0","0","0","0","0","0"  ] },
		 { "name":"Card to card transfer Outward", "limits":[  "0", "0", "0","0","0","0","0","0","0","0"   ] },
       
    ]
}
}
if(inpreq=="IVR"){
myObj = {
    "DELV": [
	{ "name":"Card Top up", "limits":[  "0", "0", "0","0","0","0","0","0","0","0"    ] },
       { "name":"Card to Card Transfer Inward", "limits":[  "0", "0", "0","0","0","0","0","0","0","0"   ] },
		 { "name":"Card to card transfer Outward", "limits":[  "0", "0", "0","0","0","0","0","0","0","0"   ] },
       
       
    ]
}
}
if(inpreq=="MOB"){
myObj = {
    "DELV": [
        { "name":"Card to Card Transfer Inward", "limits":[ "0", "0", "0","0","0","0","0","0","0","0"] },
		 { "name":"Card to card transfer Outward", "limits":[ "0", "0", "0","0","0","0","0","0","0","0" ] },
       
       
    ]
}

}
if(inpreq=="CSR"){
myObj = {
    "DELV": [
        { "name":"Card to Card Transfer Inward", "limits":[ "0", "0", "0","0","0","0","0","0","0","0" ] },
		 { "name":"Card to card transfer Outward", "limits":[ "0", "0", "0","0","0","0","0","0","0","0" ] },
       
       
    ]
}
}
if(inpreq=="HOST"){
myObj = {
    "DELV": [
        { "name":"Batch file Upload - Card Unload", "limits":[ "0", "0", "0","0","0","0","0","0","0","0"  ] },
        { "name":"Batch file Upload - Card load", "limits":[ "0", "0", "0","0","0","0","0","0","0","0" ] },
       
    ]
}
}
limitsJSON(myObj,inpreq,"l");
}
</script>

<SCRIPT type='text/javascript'>
	function myFunction() {
        var x = document.getElementById("myDIV");
     var x1 = document.getElementById("myDIV1");
   
    if (x.style.display === "none" && document.getElementById("visa").checked) {
        x.style.display = "block";
         x1.style.display = "none";
    }
    else if (x1.style.display === "none" && document.getElementById("ibm").checked) {
        x1.style.display = "block";
         x.style.display = "none";
    } 
     
}


function cvvFunc() {
        var x = document.getElementById("cvvParemters");
     var x1 = document.getElementById("cvvParameters2");
	  var x2 = document.getElementById("cscParameters");
  
    if (document.getElementById("host").checked  && document.getElementById("cvv").checked) {
        x.style.display = "block";
		 x1.style.display = "none";
        x2.style.display = "none";
        
    }
    
     
	 else  if ( document.getElementById("host").checked  && document.getElementById("csc").checked) {
        x.style.display = "none";
		 x1.style.display = "none";
        x2.style.display = "block";
        
    }
	
	else  if ( document.getElementById("hsm").checked  && document.getElementById("cvv").checked) {
        x.style.display = "none";
		 x1.style.display = "block";
        x2.style.display = "none";
        
    }
	
	else  if ( document.getElementById("hsm").checked  && document.getElementById("csc").checked) {
        x.style.display = "none";
		  x1.style.display = "none";
        x2.style.display = "block";
       
    }
}
function  feeFunvalues(inpreq){
//alert("Fee"+inpreq);
if(inpreq=="SPIL" || inpreq=="on"){
//alert("SPIL2"+inpreq);
myObj = {
    "DELV": [
        { "name":"Value_Insertion", "limits":[ "0", "0", "0","0","0","0","0","0","0","0","0","0","0","0","0"  ] },
        { "name":"Activation", "limits":[  "0", "0", "0","0","0","0","0","0","0","0","0","0","0","0","0" ] },
       
    ]
}
}
if(inpreq=="CHW"){
myObj = {
    "DELV": [
        { "name":"Card Top up", "limits":[  "0", "0", "0","0","0","0","0","0","0","0"    ] },
        { "name":"Card to Card Transfer Inward", "limits":[ "0", "0", "0","0","0","0","0","0","0","0"  ] },
		 { "name":"Card to card transfer Outward", "limits":[  "0", "0", "0","0","0","0","0","0","0","0"   ] },
       
    ]
}
}
if(inpreq=="IVR"){
myObj = {
    "DELV": [
	{ "name":"Card Top up", "limits":[  "0", "0", "0","0","0","0","0","0","0","0"    ] },
       { "name":"Card to Card Transfer Inward", "limits":[  "0", "0", "0","0","0","0","0","0","0","0"   ] },
		 { "name":"Card to card transfer Outward", "limits":[  "0", "0", "0","0","0","0","0","0","0","0"   ] },
       
       
    ]
}
}
if(inpreq=="MOB"){
myObj = {
    "DELV": [
        { "name":"Card to Card Transfer Inward", "limits":[ "0", "0", "0","0","0","0","0","0","0","0"] },
		 { "name":"Card to card transfer Outward", "limits":[ "0", "0", "0","0","0","0","0","0","0","0" ] },
       
       
    ]
}

}
if(inpreq=="CSR"){
myObj = {
    "DELV": [
        { "name":"Card to Card Transfer Inward", "limits":[ "0", "0", "0","0","0","0","0","0","0","0" ] },
		 { "name":"Card to card transfer Outward", "limits":[ "0", "0", "0","0","0","0","0","0","0","0" ] },
       
       
    ]
}
}
if(inpreq=="HOST"){
myObj = {
    "DELV": [
        { "name":"Batch file Upload - Card Unload", "limits":[ "0", "0", "0","0","0","0","0","0","0","0"  ] },
        { "name":"Batch file Upload - Card load", "limits":[ "0", "0", "0","0","0","0","0","0","0","0" ] },
       
    ]
}
}
feeJSON(myObj,inpreq);
}
function feeJSON(myObj,inputreq){
//alert('feeJSON')
var myObj,i, j, y = "";
var myObj=myObj;

for (i in myObj.DELV) {
     y+="<tr>";
   // y+= "<td><input type='checkbox' id='row1' name='' /></td><td>"+myObj.DELV[i].name+"</td>";
	//alert("x"+y);
    //for (j in myObj.DELV[i].limits) {
	
	//var comval=inputreq+k;
	y += "<td><input type='checkbox' id='row1' name='' /></td><td>"+myObj.DELV[i].name+"</td><td><input type='text' id='feedesc_'  size='10'></td><td><input type='checkbox' id='' name='clawback' /></td><td><input type='text' id='clawcnt'  size='3'></td><td><input type='text' id='feeAmt_'  size='5'></td><td> <select   ><option selected='true' value='NA'>NA</option><option selected='true' value='AND'>AND</option><option selected='true' value='OR'>OR</option></select><td><input type='text' id='feeper_'  size='5'></td><td><input type='text' id='minfee'  size='5'></td><td><input type='text' id='capfeeamt_'  size='5'></td><td><input type='text' id='freecnt'  size='10'></td><td><select name='freecntfre'><option selected='true' value='D'>Daily</option><option  value='M'>Monthly</option><option  value='W'>Weekly</option></select></td><td><input type='text' id='maxcnt'  size='3'></td><td><select name='maxcntfreq'><option selected='true' value='D'>Daily</option><option  value='M'>Monthly</option><option value='W'>Weekly</option></select></td><td><input type='checkbox' id='row1' name='' />";
	//}
    y+="</tr>"
   // alert("x"+y);
}
//alert("Before "+inputreq);
if(inputreq=="SPIL"){
//alert("CHW"+inputreq);
document.getElementById("feeSPIL").innerHTML = y;
}
if(inputreq=="CHW"){
//alert("CHW"+inputreq);
document.getElementById("feeCHW").innerHTML = y;
}
if(inputreq=="MOB"){
//alert("CHW"+inputreq);
document.getElementById("feeMOB").innerHTML = y;
}
if(inputreq=="CSR"){
document.getElementById("feeCSR").innerHTML = x;
}
if(inputreq=="IVR"){
document.getElementById("feeIVR").innerHTML = y;
}
if(inputreq=="HOST"){
document.getElementById("feeHost").innerHTML = y;
}else{
//alert("test");
document.getElementById("feeCSR").innerHTML = y;
}

y="";
}

</SCRIPT>



  
  
	  
	     
<script type="text/javascript">
/*  $(function() {
            var availablePartnerName  =  [
               "myVanilla",
               "myvanilladebitcard",
              
            ];
            $( "#search_PartnerName" ).autocomplete({
               source: availablePartnerName
            });
         });
		 
		  $(function() {
            var availableIssuerName  =  [
               "US Bank",
               "Bank of America",
              "Capital One",
			   " Wells Fargo",
			 
            ];
            $( "#search_IssuerName" ).autocomplete({
               source: availableIssuerName
            });
         });
		  */
		 

function getCardRanges() {

var issuerName=document.getElementById("search_IssuerName").value;
var x = document.getElementById("cardRange");

    var option = document.createElement("option");
	
	  var option1 = document.createElement("option");
	    var option2 = document.createElement("option");
if (x.length > 0) {
var len=x.length;
for (var i = 0; i <=len ; i++){
x.options[i] = null;
}

}
if(issuerName=='US Bank'){

    option.text = "46487600001 - 46487600008";
	 option1.text = "46487600011 - 46487600018";
	  option2.text = "46487600021 - 46487600028"; 
    x.add(option);  x.add(option1);  x.add(option2); 
}
else if(issuerName=='Bank of America'){

    option.text = "56487600001 - 56487600008";
	 option1.text = "56487600011 - 56487600018";
	  option2.text = "56487600021 - 56487600028"; 
    x.add(option);  x.add(option1);  x.add(option2); 
}
}

</script>  
  </head>
  <body class="dashboard" onload="checkCopy('SPIL')">
	<div>
		<ol class="breadcrumb col-lg-11">
			<li>
				<div class="breadCrumb-blue">Setup > Add Product</div>
			</li>

		</ol>
	</div>
	<article class="col-xs-11">
					
                    	 <p>
						 <ul class="nav nav-tabs">
                              <li class="active"><a href="#product" data-toggle="tab" class="hoverColor" >Product</a></li>
                              <li><a href="#General" data-toggle="tab" class="hoverColor" >General</a></li>
                              <li><a href="#EMAIL_Alerts" data-toggle="tab" class="hoverColor">SMS & EMAIL Alerts</a></li>
                              <li><a href="#Limits" data-toggle="tab" class="hoverColor">Limits</a></li>
                              <li><a href="#Fees" data-toggle="tab" onclick="feeFunvalues('SPIL')" class="hoverColor">Fees</a></li>
							 <li><a href="#Card_Status" data-toggle="tab" class="hoverColor"> Txn based on Card Status</a></li>
							 <li><a href="#PIN" data-toggle="tab" class="hoverColor"> PIN</a></li>
							<li><a href="#CVV" data-toggle="tab" class="hoverColor">   CVV</a></li>
                            </ul>
							 <div class="tabresp tab-content">
							  <div class="tab-pane fade in active" id="product">
							    <div class="form-group text-right Error-red">*=mandatory</div>
							     <div class="form-inline">
								
                                 	<div class="col-lg-6 form-col-2">
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Product Name">Product Name<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">

								<input  class="form-group textbox-large"
									maxlength="30" />

								
							</div>
						</div>
					
					<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Product Short Name">Product Short Name<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">

								<input  class="textbox textbox-xlarge"
									maxlength="30" />
						
							</div>
						</div>
						
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Partner Name">Partner Name<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">

								<input  id="search_PartnerName" class="textbox textbox-xlarge"
									maxlength="30" />
							
							</div>
						</div>
						
							<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Issuer Name">Issuer Name<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">

								<input id="search_IssuerName" onblur="getCardRanges()" class="textbox textbox-xlarge"
									maxlength="30" />

							</div>
						</div>
						
						
							<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Card Range">Card Range<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">
								
								 <select  id="cardRange" style="width: 195px;"  >
							
																		</select>
							</div>
						</div>
						
						
						
							<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Parent Product Name">Parent Product Name<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">

							 <select  style="width: 195px;"  >
							<option selected="true" value="-1" label="--- None ---"/>
											
																		</select>

							</div>
						</div>
						
							<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Default Currency Code">Default Currency Code<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">

								<input  class="textbox textbox-xlarge"
									maxlength="30" />

							</div>
						</div>
						
							<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Denomination (Activation)">Denomination (Activation)<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9 space">

						<select class="space" style="width: 195px;" id="denominationId" onchange="includeRespDiv()" >
							<option value="Fixed" label="Fixed"/>
							<option  value="Select" label="Select"/>
							<option  value="Variable" label="Variable"/>
							</select>
					<p class="space" id="fixedDiv" style="display:none">
					<input type="text" name="fixedDenom"/>
					</p>
					<p class="space" id="selectDiv" style="display:none">
					<select  style="width: 195px;" id="selectDenom" >
							<option value="25" label="25"/>
							<option  value="50" label="50"/>
							<option  value="100" label="100"/>
							</select>
					</p>
					<p class="space" id="variableDiv" style="display:none">
					Min: <input type="text" name="minDenom"/>
					Max: <input type="text" name="maxDenom"/>
					</p>
							</div>
						
						</div>
						
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Supported Purse(s)">Supported Purse(s)<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">

								<input  class="textbox textbox-xlarge"
									maxlength="30" />

							</div>
						</div>

						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Default Purse">Default Purse<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">

								<input class="textbox textbox-xlarge"
									maxlength="30" />

							</div>
						</div>
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Package ID">Package ID</label>
							</div>
							<div class="col-lg-9">
<select  multiple="multiple" >
												
					<option selected="false" value="P100" label="P100"/>
							<option selected="false	" value="P200" label="P200"/>
							<option selected="false" value="P300" label="P300"/>
											
											</select>

								
							</div>
						</div>
						

					</div>
					</div>
					<div class="panel-footer">
                                   		<button type="button" id="kycNextButton"
									onclick="return nextRegisterKYC();" class="btn btn-primary">Save</button>
                                    </div>
                                </div>
								<div class="tab-pane fade" id="General">
										
							<div class="col-lg-6 form-col-2">
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Form Factor">Form Factor<font
									color='red'></font></label>
							</div>
							<div class="col-lg-9">

								<input  class="textbox textbox-xlarge"
									maxlength="30" />

								
							</div>
						</div>
					
						
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Default Status">Default Status<font
									color='red'></font></label>
							</div>
							<div class="col-lg-9">

								<input  class="textbox textbox-xlarge"
									maxlength="30" />

								
							</div>
						</div>
					
						
							
							
							
						
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="HSM">HSM<font
									color='red'></font></label>
							</div>
							<div class="col-lg-9">

								<input  class="textbox textbox-xlarge"
									maxlength="30" />

								
							</div>
						</div>
					
						
							
					
						
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Validity">Validity<font
									color='red'></font></label>
							</div>
							<div class="col-lg-9">

								<input  class="textbox textbox-xlarge"
									maxlength="30" />

								
							</div>
						</div>
					
						
						
					
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Month End - Card Expiry Date">Month End - Card Expiry Date<font
									color='red'></font></label>
							</div>
							<div class="col-lg-9">

								<input  class="textbox textbox-xlarge"
									maxlength="30" />

								
							</div>
						</div>
					
					
						
						
						
					
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Service Code">Service Code<font
									color='red'></font></label>
							</div>
							<div class="col-lg-9">

								<input  class="textbox textbox-xlarge"
									maxlength="30" />

								
							</div>
						</div>
							
						
						
						
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Maximum Card Balance">Maximum Card Balance<font
									color='red'></font></label>
							</div>
							<div class="col-lg-9">

								<input  class="textbox textbox-xlarge"
									maxlength="30" />

								
							</div>
						</div><label >  </label>
					<div class="col-lg-12">
							<div class="col-lg-3 space"></div>
							<div class="col-lg-9">
								<button type="button" id="kycNextButton"
									onclick="return nextRegisterKYC();" class="btn btn-primary">Submit</button>
							
							</div>
						</div>
								
						
						
						</div>
						
						</div>
						
						
						
						<div class="tab-pane fade" id="EMAIL_Alerts">
					<div class="col-lg-6 form-col-2">
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="From Email Id For Alerts">From Email Id For Alerts<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">

								<input  class="textbox textbox-xlarge"
									maxlength="30" />

								
							</div>
						</div>
					
					<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Application Name">Application Name<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">

								<input  class="textbox textbox-xlarge"
									maxlength="30" />
						
							</div>
						</div>
						
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Application Notificaton Type">Application Notificaton Type<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">

								<input   class="textbox textbox-xlarge"
									maxlength="30" />
							
							</div>
						</div>
						
							<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="SMS Short Code">SMS Short Code<font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-9">

								<input   class="textbox textbox-xlarge"
									maxlength="30" />
							
							</div>
						</div>
											
						

						<label > <font color='red'>*</font> = MANDATORY </label>

					<div class="col-lg-12">
							<div class="col-lg-3 space"></div>
							<div class="col-lg-9">
								<button type="button" id="kycNextButton"
									onclick="return nextRegisterKYC();" class="btn btn-primary">Submit</button>
								
							</div>
						</div>


					</div>
						</div>
						<div class="tab-pane fade" id="Fees">
						<p>
							
							<div class="col-lg-12">
							<p>
                                        <label style="padding: 0px 15px;font-size: large;">Fee Plan Description </label>
                                        <input class="textbox" name="" id="copytxt" type="text" size="25" >
										
                                       
                              </p>
							</div>
							<ul class="nav nav-tabs">
                              <li class="active"><a href="#feeSPILtab" data-toggle="tab" onclick="feeFunvalues('SPIL')">SPIL</a></li>
                              <li><a href="#feeCHWtab" data-toggle="tab" class="hoverColor" onclick="feeFunvalues('CHW')">CHW</a></li>
                              <li><a href="#feeIVRtab" data-toggle="tab" class="hoverColor" onclick="feeFunvalues('IVR')">IVR</a></li>
                              <li><a href="#feeMOBtab" data-toggle="tab" class="hoverColor" onclick="feeFunvalues('MOB')">MOB</a></li>
							  <li><a href="#feeCSRtab" data-toggle="tab" class="hoverColor" onclick="feeFunvalues('CSR')">CSR</a></li>
                              <li><a href="#feeHosttab" data-toggle="tab" class="hoverColor" onclick="feeFunvalues('HOST')">HOST</a></li>
                            </ul>
							 <div class="tabresp tab-content">
							
							 	
							 <div class="tab-pane fade in active" id="feeSPILtab">
							 <form name="" action="" 	class='form-horizontal'>
                                 <div class="col-lg-6 form-col-2">
				
							
							<table id="tableViewUsers"
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;">
										<thead class="table-head" style="font-size='x-small'">
											<tr>
											<label for="Transactions">
											<th></th>
												<th>Transaction Desc</th>
												<th>Fee Desc</th>	
												<th>ClawBack</th>
												<th>ClawBack Count</th>	
												<th>Fee Amt</th>
												<th>Fee Condition</th>	
												<th>Fee %</th>	
												<th>Min Fee Amt</th>	
												<th>Cap Fee Amt</th>	
												<th>Free Count</th>
												<th>Free Count Freq</th>
												<th>Maximum Count</th>	
												<th>Maximum count Freq</th>
												<th>Monthly Fee Cap Applicable</th>	
											
											</tr>
											
										</thead>
										<tbody class="row" id="feeSPIL">
										
										</tbody>
							</table>
												
							
						
			</div>
						</div>
						
						<div class="tab-pane fade " id="feeCHWtab">
								 <form name="" action="" 	class='form-horizontal'>
                                 <div class="col-lg-6 form-col-2">
				
							
							<table id="tableViewUsers"
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;">
										<thead class="table-head" style="font-size='x-small'">
											<tr>
											<label for="Transactions">
											<th></th>
												<th>Transaction Desc</th>
												<th>Fee Desc</th>	
												<th>ClawBack</th>
												<th>ClawBack Count</th>	
												<th>Fee Amt</th>
												<th>Fee Condition</th>	
												<th>Fee %</th>	
												<th>Min Fee Amt</th>	
												<th>Cap Fee Amt</th>	
												<th>Free Count</th>
												<th>Free Count Freq</th>
												<th>Maximum Count</th>	
												<th>Maximum count Freq</th>
												<th>Monthly Fee Cap Applicable</th>	
											
											</tr>
											
										</thead>
										<tbody class="row" id="feeCHW">
										
										</tbody>
							</table>
												
							
						
			</div>
			</form>
							 </div>
							 
							 <div class="tab-pane fade " id="feeIVRtab">
								<div class="col-lg-6 form-col-2">
				
							
							<table id="tableViewUsers"
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;">
										<thead class="table-head" style="font-size='x-small'">
											<tr>
											<label for="Transactions">
											<th></th>
												<th>Transaction Desc</th>
												<th>Fee Desc</th>	
												<th>ClawBack</th>
												<th>ClawBack Count</th>	
												<th>Fee Amt</th>
												<th>Fee Condition</th>	
												<th>Fee %</th>	
												<th>Min Fee Amt</th>	
												<th>Cap Fee Amt</th>	
												<th>Free Count</th>
												<th>Free Count Freq</th>
												<th>Maximum Count</th>	
												<th>Maximum count Freq</th>
												<th>Monthly Fee Cap Applicable</th>	
											
											</tr>
											
										</thead>
										<tbody class="row" id="feeIVR">
										
										</tbody>
							</table>
												
							
						
			</div>
							 </div>
							  <div class="tab-pane fade " id="feeMOBtab">
							  <div class="col-lg-6 form-col-2">
				
							
							<table id="tableViewUsers"
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;">
										<thead class="table-head" style="font-size='x-small'">
											<tr>
											<label for="Transactions">
											<th></th>
												<th>Transaction Desc</th>
												<th>Fee Desc</th>	
												<th>ClawBack</th>
												<th>ClawBack Count</th>	
												<th>Fee Amt</th>
												<th>Fee Condition</th>	
												<th>Fee %</th>	
												<th>Min Fee Amt</th>	
												<th>Cap Fee Amt</th>	
												<th>Free Count</th>
												<th>Free Count Freq</th>
												<th>Maximum Count</th>	
												<th>Maximum count Freq</th>
												<th>Monthly Fee Cap Applicable</th>	
											
											</tr>
											
										</thead>
										<tbody class="row" id="feeMOB">
										
										</tbody>
							</table>
												
							
						
			</div>
								feeMOB spil
							 </div>
							 <div class="tab-pane fade" id="feeCSRtab">
							 <div class="col-lg-6 form-col-2">
				
							
							<table id="tableViewUsers"
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;">
										<thead class="table-head" style="font-size='x-small'">
											<tr>
											<label for="Transactions">
											<th></th>
												<th>Transaction Desc</th>
												<th>Fee Desc</th>	
												<th>ClawBack</th>
												<th>ClawBack Count</th>	
												<th>Fee Amt</th>
												<th>Fee Condition</th>	
												<th>Fee %</th>	
												<th>Min Fee Amt</th>	
												<th>Cap Fee Amt</th>	
												<th>Free Count</th>
												<th>Free Count Freq</th>
												<th>Maximum Count</th>	
												<th>Maximum count Freq</th>
												<th>Monthly Fee Cap Applicable</th>	
											
											</tr>
											
										</thead>
										<tbody class="row" id="feeCSR">
										
										</tbody>
							</table>
												
							
						
			</div>
								feeCSR spil
							 </div>
							 <div class="tab-pane fade" id="feeHosttab">
								 <div class="col-lg-6 form-col-2">
				
							
							<table id="tableViewUsers"
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;">
										<thead class="table-head" style="font-size='x-small'">
											<tr>
											<label for="Transactions">
											<th></th>
												<th>Transaction Desc</th>
												<th>Fee Desc</th>	
												<th>ClawBack</th>
												<th>ClawBack Count</th>	
												<th>Fee Amt</th>
												<th>Fee Condition</th>	
												<th>Fee %</th>	
												<th>Min Fee Amt</th>	
												<th>Cap Fee Amt</th>	
												<th>Free Count</th>
												<th>Free Count Freq</th>
												<th>Maximum Count</th>	
												<th>Maximum count Freq</th>
												<th>Monthly Fee Cap Applicable</th>	
											
											</tr>
											
										</thead>
										<tbody class="row" id="feeHost">
										
										</tbody>
							</table>
												
							
						
			</div>
							 </div>
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						</div>
						<button type="button" id="kycNextButton"
									onclick=""  style="color: #fff;padding: 0px 15px;background: #187fd7;" >Submit</button>
						
						</div>
						
						<div class="tab-pane fade" id="Card_Status">
						<div class="col-lg-6 form-col-2">
							<table id="tableViewUsers" 
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;"  >
										<thead class="table-head">
											<tr>
												<th>Channel</th>
												<th>Transaction</th>
												<th>Inactive</th>
												<th>Active</th>
												<th>Lost</th>
												<th>Stolen</th>
										</tr>
										</thead>
										<tbody class="row">
						
											
												<tr>
												
													<td >SPIL</td>
													<td>Activation</td>
													<td><input type="checkbox" class="checkbox" /></td>
													<td><input type="checkbox" class="checkbox" /></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													
												</tr>
												
												
												<tr>
												
													<td >SPIL</td>
													<td>Reload</td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													
												</tr>
											
												<tr>
												
													<td >CHW</td>
													<td>Card Top up</td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													
												</tr>
												<tr>
												
													<td >CHW</td>
													<td>Card to Card Transfer Inward </td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													
												</tr><tr>
												
													<td >CHW</td>
													<td>Card to card transfer Outward</td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													
												</tr>
												
												<tr>
												
													<td >IVR</td>
													<td>Card Top up</td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													
												</tr>
												
													<tr>
												
													<td >IVR</td>
													<td>Card to Card Transfer Inward </td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													
												</tr>
												
												<tr>
												
													<td >IVR</td>
													<td>Card to card transfer Outward </td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													
												</tr>
												
													<tr>
												
													<td >MOB</td>
													<td>Card to Card Transfer Inward  </td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													
												</tr>
												
												
													<tr>
												
													<td >MOB</td>
													<td>Card to card transfer Outward  </td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													<td><input type="checkbox" class="checkbox" checked/></td>
													
												</tr>
											
										</tbody>
									</table>
								
									<label >  </label>
					<div class="col-lg-12">
							<div class="col-lg-3 space"></div>
							<div class="col-lg-9">
								<button type="button" id="kycNextButton"
									onclick="return nextRegisterKYC();" class="btn btn-primary">Submit</button>
								
							</div>
						</div>
							</div>
						</div>
						<div class="tab-pane fade" id="PIN">
						

<div class="col-lg-6 form-col-2">
		
		<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="PIN Generation Algorithm">PIN Generation Algorithm</label>
							</div>
							<div class="col-lg-9">
								<div class="col-lg-3 space">
							<label for="VISA PVV">VISA PVV</label><input type="radio" id="visa" name="pinGen"  onclick="myFunction()" value="visa" checked> 
								</div>	
						
							</div>
			</div>
			<div class="col-lg-12">

							<div class="col-lg-3 space">
								
							</div>
							<div class="col-lg-9">
								<div class="col-lg-3 space">
								<label for="IBM DES">IBM DES</label><input type="radio"  id="ibm" name="pinGen" onclick="myFunction()" value="ibm"> 
								</div>	
						
							</div>
			</div>

						<div id="myDIV">
                            <div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="PVKA">PVKA<font color="red"></font></label>
							</div>
							<div class="col-lg-9">

								<input id="PVKA" name="PVKA" class="textbox textbox-xlarge" type="text" value="" maxlength="30">

								
							</div>
						</div>
					
					<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="PVKB">PVKB<font color="red"></font></label>
							</div>
							<div class="col-lg-9">

								<input id="PVKB" name="PVKB" class="textbox textbox-xlarge" type="text" value="" maxlength="3">

								
							</div>
						</div>
						
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="PVKI">PVKI<font color="red"></font></label>
							</div>
							<div class="col-lg-9">

								<input id="PVKI" name="PVKI" class="textbox textbox-xlarge" type="text" value="" maxlength="30">

								
							</div>
						</div>
						
							<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="PIN BLOCK FORMAT">PIN BLOCK FORMAT<font color="red"></font></label>
							</div>
							<div class="col-lg-9">

								<input id="PIN BLOCK FORMAT" name="PIN BLOCK FORMAT" class="textbox textbox-xlarge" type="text" value="" maxlength="30">

							
							</div>
						</div>
						


					</div>
				
						
                     <div id="myDIV1" style="display:none">
                            <div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="PVKA">PVK<font color="red"></font></label>
							</div>
							<div class="col-lg-9">

								<input id="PVK" name="PVK" class="textbox textbox-xlarge" type="text" value="" maxlength="30">

								
							</div>
						</div>
					
					<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Maximum PIN Length">Maximum PIN Length<font color="red"></font></label>
							</div>
							<div class="col-lg-9">

								<input id="Maximum PIN Length" name="Maximum PIN Length" class="textbox textbox-xlarge" type="text" value="" maxlength="3">

							</div>
						</div>
						
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Check Length">Check Length<font color="red"></font></label>
							</div>
							<div class="col-lg-9">

								<input id="Check Length" name="Check Length" class="textbox textbox-xlarge" type="text" value="" maxlength="30">

							
							</div>
						</div>
						
							<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="PAN offset">PAN offset<font color="red"></font></label>
							</div>
							<div class="col-lg-9">

								<input id="PAN offset" name="PAN offset" class="textbox textbox-xlarge" type="text" value="" maxlength="30">

						
							</div>
						</div>
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="PAN Verify Length">PAN Verify Length<font color="red"></font></label>
							</div>
							<div class="col-lg-9">

								<input id="PAN Verify Length" name="PAN Verify Length" class="textbox textbox-xlarge" type="text" value="" maxlength="30">

							
							</div>
						</div>
						
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="PIN BLOCK FORMAT">PIN BLOCK FORMAT<font color="red"></font></label>
							</div>
							<div class="col-lg-9">

								<input id="PIN BLOCK FORMAT" name="PIN BLOCK FORMAT" class="textbox textbox-xlarge" type="text" value="" maxlength="30">

							
							</div>
						</div>
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="Decimalisation Table">Decimalisation Table<font color="red"></font></label>
							</div>
							<div class="col-lg-9">

								<input id="Decimalisation Table" name="Decimalisation Table" class="textbox textbox-xlarge" type="text" value="" maxlength="30">

						
							</div>
						</div>
						
						<div class="col-lg-12">

							<div class="col-lg-3 space">
								<label for="PAD Character">PAD Character<font color="red"></font></label>
							</div>
							<div class="col-lg-9">

								<input id="PAD Character" name="PAD Character" class="textbox textbox-xlarge" type="text" value="" maxlength="30">

					
							</div>
						</div>
								
						
                    	</div>
						<label >  </label>
					<div class="col-lg-12">
							<div class="col-lg-3 space"></div>
							<div class="col-lg-9">
								<button type="button" id="kycNextButton"
									onclick="return nextRegisterKYC();" class="btn btn-primary">Submit</button>
								
							</div>
						</div>
                    </div>    
              

						
						
						</div>
						<div class="tab-pane fade" id="CVV">
						
							<div class="col-lg-6 form-col-2">
		
		<div class="col-lg-12">

							<div class="col-lg-3">
								<label for="CVK Format">CVK Format</label>
							</div>
							<div class="col-lg-9">
								<div class="col-lg-3">
							<label for="HSM Stored">HSM Stored</label>&nbsp;	<input type="radio" id="hsm" name="stored"  onclick="cvvFunc()"  value="hsm" > 
								</div>	
						
							</div>
			</div>
			<div class="col-lg-12">

							<div class="col-lg-3 space">
								
							</div>
							<div class="col-lg-9">
								<div class="col-lg-3 space">
								<label for="Host Stored">Host Stored</label> &nbsp;<input type="radio" onclick="cvvFunc()" id="host" name="stored"  value="host"> 
								</div>	
					
							</div>
			</div>
			
			
			<div class="col-lg-12">

							<div class="col-lg-3">
								<label for="Card Verify Type">Card Verify Type</label>
							</div>
							<div class="col-lg-9">
								<div class="col-lg-3">
							<label for="CVV">CVV</label>&nbsp;	<input type="radio" id="cvv" onclick="cvvFunc()"  name="cardVerifyType"  value="cvv" > 
								</div>	
					
							</div>
			</div>
			<div class="col-lg-12">

							<div class="col-lg-3 space">
								
							</div>
							<div class="col-lg-9">
								<div class="col-lg-3 space">
								<label for="CSC">CSC</label>&nbsp;&nbsp;<input type="radio"  id="csc"  onclick="cvvFunc()"  name="cardVerifyType" onclick="myFunction()" value="csc"> 
								</div>	
						
							</div>
			</div>
			
			
			
			
			
			<div class="col-lg-12" id="cvvParemters" style="display:none;">

							<div class="col-lg-3">
								<label for="CVV Parameters">CVV Parameters</label>
							</div>
							<div class="col-lg-9">
								<div class="col-lg-3 space">
							<label for="CVK_A">CVK_A</label>
								</div>	
						<div class="col-lg-9">
							<input id="cvkA" name="cvkA"  class="textbox textbox-xlarge" type="text" value="" maxlength="30">
						</div>	
							</div>
							
							<div class="col-lg-3">
							
							</div>
							<div class="col-lg-9">
								<div class="col-lg-3 space">
							<label for="CVK_B">CVK_B</label>
								</div>	
						<div class="col-lg-9">
							<input id="cvkB" name="cvkB" class="textbox textbox-xlarge" type="text" value="" maxlength="30">
						</div>	
							</div>
							
								<div class="col-lg-3">
							
							</div>
							<div class="col-lg-9">
								<div class="col-lg-3 space">
							<label for="CVK Key Specifier">CVK Key Specifier</label>
								</div>	
						<div class="col-lg-9">
							<input id="cvkKey" name="cvkKey" class="textbox textbox-xlarge" type="text" value="" maxlength="30">
						</div>	
							</div>
			</div>
			
			
			
			<div class="col-lg-12" id="cvvParameters2" style="display:none;">

							<div class="col-lg-3 space">
								<label for="CVV Parameters">CVV Parameters</label>
							</div>
							<div class="col-lg-9">
								<div class="col-lg-3 space">
							<label for="CVK_INDEX">CVK_INDEX</label>
								</div>	
						<div class="col-lg-9">
							<input id="cvkIndex" name="cvkIndex" class="textbox textbox-xlarge" type="text" value="" maxlength="30">
						</div>	
							</div>
							
							<div class="col-lg-3 space">
								
							</div>
							<div class="col-lg-9">
								<div class="col-lg-3 space">
							<label for="CSC KEY">CVK KEY Specifier</label>
								</div>	
						<div class="col-lg-9">
							<input id="cscKeySpecifier" name="cscKeySpecifier" class="textbox textbox-xlarge" type="text" value="" maxlength="30">
						</div>	
							</div>
									
													
			</div>
			
			
			
			<div class="col-lg-12" id="cscParameters" style="display:none;">

							<div class="col-lg-3 space">
								<label for="CVV Parameters">CSC Parameters</label>
							</div>
							<div class="col-lg-9">
								<div class="col-lg-3 space">
							<label for="CSC KEY">CSC KEY</label>
								</div>	
						<div class="col-lg-9">
							<input id="cscKey" name="cscKey" class="textbox textbox-xlarge" type="text" value="" maxlength="30">
						</div>	
							</div>
													
			</div>
			
						<label >  </label>
					<div class="col-lg-12">
							<div class="col-lg-3 space"></div>
							<div class="col-lg-9">
								<button type="button" id="kycNextButton"
									onclick="return nextRegisterKYC();" class="btn btn-primary">Submit</button>
								
							</div>
						</div>
                    </div> 
						
						
						</div>
							  <div class="tab-pane fade" id="Limits">
                        <p>
						<div class="col-lg-12">
						  <p>
                                        <label><input type="button" id="copy" value="Copy" style="color: #fff;padding: 0px 15px;background: #187fd7;" onclick="funcCopy()"/>  From   </label>
                                        <input class="textbox" name="" id="copytxt" type="text" >
										
                                       
                              </p>
							</div>  
                            <ul class="nav nav-tabs">
                              <li class="active"><a href="#tab1" data-toggle="tab" onclick="checkCopy('SPIL')">SPIL</a></li>
                              <li><a href="#tab2" data-toggle="tab" class="hoverColor" onclick="checkCopy('CHW')">CHW</a></li>
                              <li><a href="#tab3" data-toggle="tab" class="hoverColor" onclick="checkCopy('IVR')">IVR</a></li>
                              <li><a href="#tab4" data-toggle="tab" class="hoverColor" onclick="checkCopy('MOB')">MOB</a></li>
							  <li><a href="#tab5" data-toggle="tab" class="hoverColor" onclick="checkCopy('CSR')">CSR</a></li>
                              <li><a href="#tab6" data-toggle="tab" class="hoverColor" onclick="checkCopy('HOST')">HOST</a></li>
                            </ul>
                            <div class="tabresp tab-content">
                              <div class="tab-pane fade in active" id="tab1">
                                 	<form name="" action="" 	class='form-horizontal'>
							<div class="col-lg-6 form-col-2">
				
							
							<table id="tableViewUsers"
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;">
										<thead class="table-head">
										<col >
											<colgroup span="5" ></colgroup>
										    <colgroup span="4" ></colgroup>
											<colgroup span="4" ></colgroup>
										    <colgroup span="4" ></colgroup>
											<tr>
												<th colspan="5" scope="colgroup"></th>
												<th colspan="4" scope="colgroup">Daily</th>
												<th colspan="4" scope="colgroup">Weekly</th>
												<th colspan="4" scope="colgroup">Monthly</th>
												<th colspan="4" scope="colgroup">Yealy</th>
											</tr>
											<tr>
											
											
											<th class="table-head" scope="col" >Transactions</th>
												<th  scope="col">Min Amt per TX</th>
												<th  scope="col">	Override ?</th>	
												<th  scope="col">Max Amt per Txn</th>
												<th scope="col" >Override ?</th>	
												<th scope="col">Max Count</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Amt</th>	
												<th scope="col">Override ?</th>	
												<th scope="col">Max Count</th>	
												<th scope="col">Override ?</th>
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Count</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>
												<th scope="col">Max Count</th>	
												<th scope="col">Override ?</th>
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>
											
											</tr>
											
										</thead>
										<tbody class="row" id="SPIL">
										
										</tbody>
							</table>
												
							
						
			</div>
			
		
                                </div>
                                <div class="tab-pane fade" id="tab2">
                                  <div class="col-lg-6 form-col-2">
				
							
							<table id="tableViewUsers"
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;">
										<thead class="table-head">
										<col >
											<colgroup span="5" ></colgroup>
										    <colgroup span="4" ></colgroup>
											<colgroup span="4" ></colgroup>
										    <colgroup span="4" ></colgroup>
											<tr>
												<th colspan="5" scope="colgroup"></th>
												<th colspan="4" scope="colgroup">Daily</th>
												<th colspan="4" scope="colgroup">Weekly</th>
												<th colspan="4" scope="colgroup">Monthly</th>
												<th colspan="4" scope="colgroup">Yealy</th>
											</tr>
											<tr>
											
											
											<th class="table-head" scope="col" >Transactions</th>
												<th  scope="col">Min Amt per TX</th>
												<th  scope="col">	Override ?</th>	
												<th  scope="col">Max Amt per Txn</th>
												<th scope="col" >Override ?</th>	
												<th scope="col">Max Count</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Amt</th>	
												<th scope="col">Override ?</th>	
												<th scope="col">Max Count</th>	
												<th scope="col">Override ?</th>
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Count</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>
												<th scope="col">Max Count</th>	
												<th scope="col">Override ?</th>
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>
											
											</tr>
											
										</thead>
										<tbody class="row" id="CHW">
										
										</tbody>
							</table>
												
							
						
			</div>
                                </div>
                                <div class="tab-pane fade" id="tab3">
                                  <table id="tableViewUsers"
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;">
						
										<thead class="table-head">
										<col >
											<colgroup span="5" ></colgroup>
										    <colgroup span="4" ></colgroup>
											<colgroup span="4" ></colgroup>
										    <colgroup span="4" ></colgroup>
											<tr>
												<th colspan="5" scope="colgroup"></th>
												<th colspan="4" scope="colgroup">Daily</th>
												<th colspan="4" scope="colgroup">Weekly</th>
												<th colspan="4" scope="colgroup">Monthly</th>
												<th colspan="4" scope="colgroup">Yealy</th>
											</tr>
											<tr>
											
											
											<th class="table-head" scope="col" >Transactions</th>
												<th  scope="col">Min Amt per TX</th>
												<th  scope="col">	Override ?</th>	
												<th  scope="col">Max Amt per Txn</th>
												<th scope="col" >Override ?</th>	
												<th scope="col">Max Count</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Amt</th>	
												<th scope="col">Override ?</th>	
												<th scope="col">Max Count</th>	
												<th scope="col">Override ?</th>
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Count</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>
												<th scope="col">Max Count</th>	
												<th scope="col">Override ?</th>
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>
											
											</tr>
											
										</thead>
										<tbody class="row" id="IVR">
										
										</tbody>
							</table>
                                </div>
                                <div class="tab-pane fade" id="tab4">
                                  <table id="tableViewUsers"
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;">
										<thead class="table-head">
										<col >
											<colgroup span="5" ></colgroup>
										    <colgroup span="4" ></colgroup>
											<colgroup span="4" ></colgroup>
										    <colgroup span="4" ></colgroup>
											<tr>
												<th colspan="5" scope="colgroup"></th>
												<th colspan="4" scope="colgroup">Daily</th>
												<th colspan="4" scope="colgroup">Weekly</th>
												<th colspan="4" scope="colgroup">Monthly</th>
												<th colspan="4" scope="colgroup">Yealy</th>
											</tr>
											<tr>
											
											
											<th class="table-head" scope="col" >Transactions</th>
												<th  scope="col">Min Amt per TX</th>
												<th  scope="col">	Override ?</th>	
												<th  scope="col">Max Amt per Txn</th>
												<th scope="col" >Override ?</th>	
												<th scope="col">Max Count</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Amt</th>	
												<th scope="col">Override ?</th>	
												<th scope="col">Max Count</th>	
												<th scope="col">Override ?</th>
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Count</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>
												<th scope="col">Max Count</th>	
												<th scope="col">Override ?</th>
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>
											
											</tr>
											
										</thead>
										<tbody class="row" id="MOB">
										
										</tbody>
							</table>
                                </div>
                                <div class="tab-pane fade" id="tab5">
                                  <table id="tableViewUsers"
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;">
									<thead class="table-head">
										<col >
											<colgroup span="5" ></colgroup>
										    <colgroup span="4" ></colgroup>
											<colgroup span="4" ></colgroup>
										    <colgroup span="4" ></colgroup>
											<tr>
												<th colspan="5" scope="colgroup"></th>
												<th colspan="4" scope="colgroup">Daily</th>
												<th colspan="4" scope="colgroup">Weekly</th>
												<th colspan="4" scope="colgroup">Monthly</th>
												<th colspan="4" scope="colgroup">Yealy</th>
											</tr>
											<tr>
											
											
											<th class="table-head" scope="col" >Transactions</th>
												<th  scope="col">Min Amt per TX</th>
												<th  scope="col">	Override ?</th>	
												<th  scope="col">Max Amt per Txn</th>
												<th scope="col" >Override ?</th>	
												<th scope="col">Max Count</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Amt</th>	
												<th scope="col">Override ?</th>	
												<th scope="col">Max Count</th>	
												<th scope="col">Override ?</th>
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Count</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>
												<th scope="col">Max Count</th>	
												<th scope="col">Override ?</th>
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>
											
											</tr>
											
										</thead>
										<tbody class="row" id="CSR">
										
										</tbody>
							</table>
                                </div>
								
								<div class="tab-pane fade" id="tab6">
 <table id="tableViewUsers"
										class="table table-hover table-striped table-bordered"
										style="width: 100% !important;">
										<thead class="table-head">
										<col >
											<colgroup span="5" ></colgroup>
										    <colgroup span="4" ></colgroup>
											<colgroup span="4" ></colgroup>
										    <colgroup span="4" ></colgroup>
											<tr>
												<th colspan="5" scope="colgroup"></th>
												<th colspan="4" scope="colgroup">Daily</th>
												<th colspan="4" scope="colgroup">Weekly</th>
												<th colspan="4" scope="colgroup">Monthly</th>
												<th colspan="4" scope="colgroup">Yealy</th>
											</tr>
											<tr>
											
											
											<th class="table-head" scope="col" >Transactions</th>
												<th  scope="col">Min Amt per TX</th>
												<th  scope="col">	Override ?</th>	
												<th  scope="col">Max Amt per Txn</th>
												<th scope="col" >Override ?</th>	
												<th scope="col">Max Count</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Amt</th>	
												<th scope="col">Override ?</th>	
												<th scope="col">Max Count</th>	
												<th scope="col">Override ?</th>
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Count</th>
												<th scope="col">Override ?</th>	
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>
												<th scope="col">Max Count</th>	
												<th scope="col">Override ?</th>
												<th scope="col">Max Amt</th>
												<th scope="col">Override ?</th>
											
											</tr>
											
										</thead>
										<tbody class="row" id="HOST">
										
										</tbody>
							</table>
								</div>
								</form>
                            </div>  
							<label >  </label>
							
					<div class="col-lg-12">
							<div class="col-lg-3 space"></div>
							<div class="col-lg-9">
								<button type="button"  class="btn btn-primary">Submit</button>
							
							</div>
						</div>
						
                        </p>
						</div>
						</div>
						
						</p>
                    </article>
       
  </body>
</html>