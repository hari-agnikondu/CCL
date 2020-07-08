function generate(id) {
	
	var code=id;
	$("#cardRangeId").val(code);
	$("#cardInventoryForm").attr('action','generateCardInventory');
	$("#cardInventoryForm").submit();   
}

function doPause(id){
	var code=id;
	$("#cardRangeId").val(code);
	$("#cardInventoryForm").attr('action','pauseCardInventory');
	$("#cardInventoryForm").submit();   
}

function doRegenerate(id){
	var code=id;
	$("#cardRangeId").val(code);
	$("#cardInventoryForm").attr('action','regenerateCardInventory');
	$("#cardInventoryForm").submit(); 
}

function doResume(id){
	var code=id;
	$("#cardRangeId").val(code);
	$("#cardInventoryForm").attr('action','resumeCardInventory');
	$("#cardInventoryForm").submit(); 
}