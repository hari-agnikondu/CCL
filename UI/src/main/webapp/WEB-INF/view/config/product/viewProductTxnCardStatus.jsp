<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/product.js"></script>

<div class="body-container">
	<div class="container">
		<form:form name="txnBasedOnStat" id="txnBasedOnStat" action="#"
			modelAttribute="productStat" class='form-horizontal'>
			<section class="content-container">
				<article class="col-lg-12">
					<div id="feedBackTd" class="text-center">

						<article class="col-lg-12">
									<div id="messageResult">
										<c:if test="${statusMessage!='' && statusMessage!=null}">
											<p class="error-red text-center">
												<b>${statusMessage}</b>
											</p>

										</c:if>

										<c:if test="${status!='' && status!=null}">
											<p class="success-green text-center">
												<b>${status}</b>
											</p>
										</c:if>
									</div>
								</article>
					</div>

					<form:input path="productId" name="productId" type="hidden" />

					<ul class="nav nav-tabs col-lg-7">
						<c:forEach items="${deliverChannelList}" var="deliveryChannel"
							varStatus="status">
							<li <c:if test="${status.index == 0}">class="active"</c:if>><a
								href="#${deliveryChannel.deliveryChnlShortName}"
								data-toggle="tab" class="hoverColor" onclick="selectAllTabCheck('${deliveryChannel.deliveryChnlShortName}')">${deliveryChannel.deliveryChnlName}</a></li>
						</c:forEach>
						
					</ul>
					<div class="tabresp tab-content ">
						<c:forEach items="${deliverChannelList}" var="deliveryChannel"
							varStatus="status">
							<div
								<c:if test="${status.index == 0}">class="tab-pane fade in active graybox col-lg-12"</c:if>
								<c:if test="${status.index != 0}">class="tab-pane fade in graybox col-lg-12"</c:if>
								id="${deliveryChannel.deliveryChnlShortName}">

								<div class="col-lg-12 over">
								
									<table id="${deliveryChannel.deliveryChnlShortName}_tableTxnCardStatus"
										class="table table-hover table-striped table-bordered">

										<thead class="table-head">
											<tr>
												<th width="200"><spring:message
														code="product.limit.transactions" text="Transactions" /></th>
												<c:set var="cardStatusCount" value="0" scope="page" />
												<c:forEach items="${cardStatusList}" var="cardStatus">
													<c:set var="cardStatusLength"
														value="${fn:length(cardStatus)}" />
													<c:set var="cardStatusCount" value="${cardStatusCount + 1}" scope="page"/>
													<th width="160" class="text-center" id="${deliveryChannel.deliveryChnlShortName}_${fn:replace(fn:replace(cardStatus,'_',''),' ', '')}"><c:set
															var="cardStatus" value="${cardStatus}" />${cardStatus}
													<input type="checkbox" 	id="${deliveryChannel.deliveryChnlShortName}_${fn:replace(fn:replace(cardStatus,'_',''),' ', '')}_checkbox"  onclick="selectAll(this)" disabled/>
													<input type="hidden"  id="cardStatus_${cardStatusCount}"  value="${fn:replace(fn:replace(cardStatus,'_',''),' ', '')}"/> 		
													</th>
												</c:forEach>
											</tr>
										</thead>
										<tbody>

											<c:forEach items="${deliveryChannel.transactionMap}"
												var="txn">
												<tr class="text-center" id="${deliveryChannel.deliveryChnlShortName}_${txn.key}">
													<td class="text-left">${txn.value }</td>
													<c:forEach items="${cardStatusList}" var="cardStatus">
														<c:set var="conditionCheck"
															value="${deliveryChannel.deliveryChnlShortName}_${txn.key }_${cardStatus}" />
														<c:choose>
															<c:when
																test="${productStat.productAttributes[conditionCheck]=='true'}">
																<td><input type="checkbox"
																	id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_${fn:replace(fn:replace(cardStatus,'_',''),' ', '')}"
																	checked onclick="selectAllCheck(this)" disabled="disabled"/></td>
															</c:when>
															<c:otherwise>
																<td><input type="checkbox"
																	id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_${fn:replace(fn:replace(cardStatus,'_',''),' ', '')}" onclick="selectAllCheck(this)" disabled="disabled"/>
																</td>
															</c:otherwise>
														</c:choose>
														<form:hidden
															path="productAttributes[${deliveryChannel.deliveryChnlShortName}_${txn.key }_${cardStatus}]"
															id="${deliveryChannel.deliveryChnlShortName}_${txn.key }_${fn:replace(fn:replace(cardStatus,'_',''),' ', '')}_hidden" />
										                 <input type="hidden" value="${cardStatusCount}"  id="cardStatusLen"/>	
										                 <input type="hidden" value="${deliveryChannel.deliveryChnlShortName}"  id="first_del_channel"/>	
										               </c:forEach>
												</tr>

											</c:forEach>
										</tbody>
									</table>
										
								</div>
							</div>

						</c:forEach>

					</div>

				</article>
			</section>
		</form:form>
	</div>
</div>

<script>
$("#txnCardStatTab").addClass("active");
$("#txnCardStatTab").siblings().removeClass('active');


$(document).ready(function() {
	
	 $('input[type=checkbox]').change(function(event){
		 if(event.target.checked){
			 $("#" + event.target.id + "_hidden").val("true");
		 }
		 else{
			 $("#" + event.target.id + "_hidden").val("false");
		 }
	 });
		
   var channel=$("#first_del_channel").val();  //has first tab value
   //selectAll checkbox-enable/disble for first tab
   var cardStatusLength=$("#cardStatusLen").val();
   for(var i=1;i<=cardStatusLength;i++)
   {
		  var isChecked=true;
		  var cardStatusHidden='cardStatus_'+i;
		  var cardStatus=$("#"+cardStatusHidden).val();
		  var headerId=channel+"_"+cardStatus+"_checkbox";
  	   $('#'+channel+'_tableTxnCardStatus tr:has(td)').find('input[type="checkbox"][id^='+channel+'][id$='+cardStatus+']').each(function() {
	        	if ($(this).prop("checked") == false)
	                isChecked = false;
	        });
	        $("#"+headerId).prop('checked', isChecked);
		  }   
	   
	   
});


function selectAll(eleObj)
{
	var colID=$(eleObj).closest('th').attr('id');
	var channel=colID.split('_')[0];
	var cardStatus=colID.split('_')[1];
	var isChecked = $(eleObj).prop("checked");
	$('#'+channel+'_tableTxnCardStatus tr:has(td)').find('input[type="checkbox"][id^='+channel+'][id$='+cardStatus+']').prop('checked', isChecked);
	//updating hidden path value
	$('#'+channel+'_tableTxnCardStatus tr:has(td)').find('input[type="hidden"][id^='+channel+'][id$='+cardStatus+'_hidden]').val(isChecked);
	

}
 function selectAllCheck(eleObj)
{
	var isChecked = $(eleObj).prop("checked");
	var channel=eleObj.id.split('_')[0];
	var cardStatus=eleObj.id.split('_')[2];
	var headerId=channel+"_"+cardStatus+"_checkbox";
    var isHeaderChecked = $("#"+headerId).prop("checked");
    if (isChecked == false && isHeaderChecked)
    	$("#"+headerId).prop('checked', isChecked);
    else {
        $('#'+channel+'_tableTxnCardStatus tr:has(td)').find('input[type="checkbox"][id^='+channel+'][id$='+cardStatus+']').each(function() {
        	if ($(this).prop("checked") == false)
                isChecked = false;
        });
        $("#"+headerId).prop('checked', isChecked);
    }

	
	} 
//selectAll checkbox-enable/disble for all other tabs 
function selectAllTabCheck(channel)
 {
	  var cardStatusLength=$("#cardStatusLen").val();
	  for(var i=1;i<=cardStatusLength;i++)
	   {
		  var isChecked=true;
		  var cardStatusHidden='cardStatus_'+i;
		  var cardStatus=$("#"+cardStatusHidden).val();
		  var headerId=channel+"_"+cardStatus+"_checkbox";
     	   $('#'+channel+'_tableTxnCardStatus tr:has(td)').find('input[type="checkbox"][id^='+channel+'][id$='+cardStatus+']').each(function() {
	        	if ($(this).prop("checked") == false)
	                isChecked = false;
	        });
	        $("#"+headerId).prop('checked', isChecked);
		} 
 } 
</script>
