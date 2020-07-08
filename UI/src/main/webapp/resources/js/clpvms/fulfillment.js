/// FULFILLMENT
/*
$(document).ready(function() { // OnLoad
	checkAutoShipped();
	checkCNfilereq();

	var elements = [ 'Date', 'FileCount', 'PrintVendor', 'ProductCode' ];
	$('#ccfFileFormat').textcomplete([ { 
		match : /<(\w*)$/,
		search : function(term, callback) {
			callback($.map(elements, function(element) {
				return element.indexOf(term) === 0 ? element : null;
			}));
		},
		index : 1,
		replace : function(element) {
			return [ '<<' + element + '>>', '' ];
		}
	} ]);
	$('#replaceCcfFileFormat').textcomplete([ { 
		match : /<(\w*)$/,
		search : function(term, callback) {
			callback($.map(elements, function(element) {
				return element.indexOf(term) === 0 ? element : null;
			}));
		},
		index : 1,
		replace : function(element) {
			return [ '<<' + element + '>>', '' ];
		}
	} ]);
})*/

function clickAddFullfilment() {
	$("#fulFillmentForm").attr('action', 'showAddFullfilment');
	$("#fulFillmentForm").submit();
}

/*function resetFulfillment(formId){
	$("#"+formId+" #fulfillmentID").val('');
	$("#"+formId+" #fulFillmentName").val('');
	$("#"+formId+" #ccfFileFormat").val('');
	$("#"+formId+" #replaceCcfFileFormat").val('');
	$("#"+formId+" #b2bResFileIdentifier").val('');
	$("#"+formId+" #b2bPrinterRespIdentifier").val('');
	$("#"+formId+" #b2bConfFileIdentifier").val('');
	$('#isAutomaticShipped').val(0);
	clearError(formId+" #fulfillmentID");
	clearError(formId+" #fulFillmentName");
	clearError(formId+" #ccfFileFormat");
	clearError(formId+" #replaceCcfFileFormat");
	checkAutoShipped();
}*/

function goBackToFulfilment() {
	$("#addFulFillmentForm").attr('action', 'fulFillmentConfig');
	$("#addFulFillmentForm").submit();
}

function goBackToEditFulfilment() {
	$("#editFulFillmentForm").attr('action', 'fulFillmentConfig');
	$("#editFulFillmentForm").submit();
}

function goBackToEditFulfilmentFromView(){
	
	$("#viewFulFillmentForm").attr('action', 'fulFillmentConfig');
	$("#viewFulFillmentForm").submit();
	
}

/* To searchFulFillment based on name or to show all fulfillment */
function searchFulFillment(event) {
	event.preventDefault();
	var fulFillmentName = "";
	var code = 'byNameID';

	fulFillmentName = $("#fulfillmentNameID").val();

	if (fulFillmentName === undefined || fulFillmentName == null
			|| fulFillmentName.length <= 0 || fulFillmentName == '') {
		 code = 'all';

		$("#searchType").val(code);
		$("#fulFillmentForm").attr('action', 'showAllFulFillments');
		$("#fulFillmentForm").submit();
	} else {
		$("#searchType").val(code);
		$("#fulFillmentForm").attr('action', 'searchFulFillmentByName');
		$("#fulFillmentForm").submit();
	}
}


function addFulfillment(form) {
	var validZero_shippedTimeDealy = true;
	var validb2bCNFileIdentifierID  = true;
	var validfulfillmentID=validateFields(form,"fulfillmentID");
	var validfulFillmentName=validateFields(form,"fulFillmentName");
	var validshippedTimeDealy=validateFields(form,"shippedTimeDealy");
	//shipped time delay is checked when automaticShipped value is other than disabled option
	if($("#isAutomaticShipped").val() != "0")
	{
	validZero_shippedTimeDealy=checkZero(form,"shippedTimeDealy");
	}
	else
	{
		$("#shippedTimeDealy").val('48'); //server side notempty and pattern is checked
	}	
	
	//validation for b2b cn file identifier when b2bcnfilerequired is enabled
	if($("#b2bCNFileReqYes").prop('checked'))
	    validb2bCNFileIdentifierID = validateFields(form,"b2bCnFileIdentifier");
	else
		$("#b2bCnFileIdentifier").val('');
	var validccfFileFormat=validateFields(form,"ccfFileFormat");
	var validreplaceCcfFileFormat=validateFields(form,"replaceCcfFileFormat");
	var validb2bPrinterRespIdentifier=validateFields(form,"b2bPrinterRespIdentifier");
	var validb2bConfFileIdentifier=validateFields(form,"b2bConfFileIdentifier");
	if(validfulfillmentID && validfulFillmentName && validshippedTimeDealy && validccfFileFormat && validreplaceCcfFileFormat && validb2bPrinterRespIdentifier && validb2bConfFileIdentifier && validZero_shippedTimeDealy && validb2bCNFileIdentifierID )
		{
			$("#addFulFillmentForm").attr('action', 'addFulfillment');
        	$("#addFulFillmentForm").submit();
		}
}

function updateFulfillment(form) {
	var validZero_shippedTimeDealy = true;
	var validb2bCNFileIdentifierID  = true;
	var validfulfillmentID=validateFields(form,"fulfillmentID");
	var validfulFillmentName=validateFields(form,"fulFillmentName");
	var validshippedTimeDealy=validateFields(form,"shippedTimeDealy");
	if($("#isAutomaticShipped").val() != "0")
	   validZero_shippedTimeDealy=checkZero(form,"shippedTimeDealy");
	else
		$("#shippedTimeDealy").val('48'); //server side notempty and pattern is checked
	//validation for b2b cn file identifier when b2bcnfilerequired is enabled
	if($("#b2bCNFileReqYes").prop('checked'))
	   validb2bCNFileIdentifierID = validateFields(form,"b2bCnFileIdentifier");
	else
		$("#b2bCnFileIdentifier").val('');
	var validccfFileFormat=validateFields(form,"ccfFileFormat");
	var validreplaceCcfFileFormat=validateFields(form,"replaceCcfFileFormat");
	var validb2bPrinterRespIdentifier=validateFields(form,"b2bPrinterRespIdentifier");
	var validb2bConfFileIdentifier=validateFields(form,"b2bConfFileIdentifier");
	if(validfulfillmentID && validfulFillmentName && validshippedTimeDealy && validccfFileFormat && validreplaceCcfFileFormat && validb2bPrinterRespIdentifier && validb2bConfFileIdentifier && validZero_shippedTimeDealy && validb2bCNFileIdentifierID) 
		{
			$("#editFulFillmentForm").attr('action', 'updateFulfillment');
			$("#editFulFillmentForm").submit();
		}	
}

function editFulfillment(id) {
	var code = id;
	$("#fulFillmentSEQID").val(code);
	$("#fulFillmentForm").attr('action', 'showEditFulfillment');
	$("#fulFillmentForm").submit();
}


function viewFulfillment(id) {
	var code = id;
	$("#fulFillmentSEQID").val(code);
	$("#fulFillmentForm").attr('action', 'showViewFulfillment');
	$("#fulFillmentForm").submit();
}

function goDelete(data) {
	var splitted = data.split('~');
	var seqID = splitted[0];
	var id = splitted[1];
	document.getElementById("fulfillmentDisplay").innerHTML = data;
	$("#fulFillmentSEQID").val(seqID);
	$("#fulfillmentID").val(id);
	$("#fulFillmentIdtoDel").val(seqID);
	$("#fulFillmentIdtoDelete").val(id);
}

function goDeleteFulfillment() { 
	$("#fulFillmentForm").attr('action', 'deleteFulfillment');
	$("#fulFillmentForm").submit();
}

/*To reset add fulfillment page*/
function ResetAdd(formId){
 	$("#feedBackTd").html('');
	$("#"+formId+" #fulfillmentID").val('');
	$("#"+formId+" #fulFillmentName").val('');
	$("#"+formId+" #ccfFileFormat").val('');
	$("#"+formId+" #replaceCcfFileFormat").val('');
	$("#"+formId+" #b2bCnFileIdentifier").val('');
	$("#"+formId+" #b2bPrinterRespIdentifier").val('');
	$("#"+formId+" #b2bConfFileIdentifier").val('');
	$('select option[value="0"]').prop("selected",true);
	//$('#isAutomaticShipped option[value=-1]').attr("selected","selected");
	$("#b2bCNFileReqYes").prop("checked", true);
	/*$("#"+formId+" input[name='partnerStatus']:checked").val('true')
	$("#"+formId+" #b2bCNFileReqYes").parent().addClass('checked');
	$("#"+formId+" #b2bCNFileReqNo").parent().removeClass('checked');*/
	clearError(formId+" #fulfillmentID");
	clearError(formId+" #fulFillmentName");
	clearError(formId+" #ccfFileFormat");
	clearError(formId+" #replaceCcfFileFormat");
	clearError(formId+" #b2bCnFileIdentifier");
	clearError(formId+" #b2bPrinterRespIdentifier");
	clearError(formId+" #b2bConfFileIdentifier");
	clearError(formId+" #shippedTimeDealy");
}